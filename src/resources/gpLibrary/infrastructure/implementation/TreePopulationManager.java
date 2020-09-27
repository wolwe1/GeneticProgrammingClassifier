package resources.gpLibrary.infrastructure.implementation;

import resources.gpLibrary.functionality.implementation.StatisticsManager;
import resources.gpLibrary.helpers.Printer;
import resources.gpLibrary.infrastructure.interfaces.IGeneticOperator;
import resources.gpLibrary.infrastructure.interfaces.IPopulationManager;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;
import resources.gpLibrary.models.highOrder.implementation.PopulationStatistics;
import resources.gpLibrary.models.highOrder.interfaces.IMemberStatistics;
import resources.gpLibrary.models.primitives.IFitnessFunction;

import java.util.*;

public class TreePopulationManager<T> implements IPopulationManager<T> {

    protected Random _randomNumberGenerator;
    protected ITreeGenerator<T> _generator;
    protected IFitnessFunction<T> _fitnessFunction;
    protected List<PopulationMember<T>> _currentPopulation;
    protected List<PopulationMember<T>> _nextPopulation;

    //Statistics
    protected List<IMemberStatistics> _populationStatistics;
    protected List<PopulationStatistics> _populationHistory;
    private  List<PopulationStatistics> compositionHistory;


    public TreePopulationManager(ITreeGenerator<T> treeGenerator, IFitnessFunction<T> fitnessFunction,int seed){
        _generator = treeGenerator;
        _fitnessFunction = fitnessFunction;
        _randomNumberGenerator = new Random(seed);

        _generator.setRandomFunction(_randomNumberGenerator);

        _currentPopulation = new ArrayList<>();
        _nextPopulation = new ArrayList<>();
        _populationStatistics = new ArrayList<>();
        _populationHistory = new ArrayList<>();
        compositionHistory = new ArrayList<>();
    }

    @Override
    public void createPopulation(int populationSize) {

        for (int i = 0; i < populationSize; i++) {
            NodeTree<T> newTree = _generator.createRandom();
            PopulationMember<T> newMember = new PopulationMember<>(newTree);

            _nextPopulation.add(newMember);
        }
        setNewPopulation();
    }

    @Override
    public boolean firstFitterThanSecond(PopulationMember<T> first, PopulationMember<T> second) {
        if(first == null && second == null) throw new RuntimeException("Attempted to calculate fitness on two nulls");

        if(first == null)
            return false;

        if(second == null)
            return true;

        return _fitnessFunction.firstFitterThanSecond(first.getFitness(),second.getFitness());
    }

    @Override
    public PopulationMember<T> getBest() {
        return _fitnessFunction.getFittest(_currentPopulation);
    }

    @Override
    public void printPopulationStatistics() {

        var popstats = _populationHistory.get(_populationHistory.size() - 1);

        popstats.print();
        Printer.underline();
    }

    @Override
    public void printBasicHistory() {
        PopulationStatistics changeOverRun = StatisticsManager.calculateChange(_populationHistory.get(0),_populationHistory.get(_populationHistory.size() - 1));
        changeOverRun.print();
    }

    @Override
    public void printPopulationComposition() {

        var latestTreeComp = compositionHistory.get(compositionHistory.size() - 1);
        Printer.print("Tree composition");
        latestTreeComp.print();
        Printer.underline();
    }

    @Override
    public void printFullHistory() {
        printBasicHistory();

        Printer.print("\nComposition change");
        Printer.underline();
        PopulationStatistics changeInComposition =
        StatisticsManager.calculateChange(compositionHistory.get(0),compositionHistory.get(compositionHistory.size() - 1));

        changeInComposition.print();
        Printer.underline();
    }

    private HashMap<String, Double> getTreeComposition() {
        HashMap<String,Integer> chromosomeFrequency = new HashMap<>();
        HashMap<String,Double> percentageComposition = new HashMap<>();
        int numItems = 0;

        for (PopulationMember<T> member : _currentPopulation) {
            String comp = member.getId();
            String[] items = comp.split("\\.");
            numItems += items.length;

            for (String item : items) {
                chromosomeFrequency.merge(item, 1, Integer::sum);
            }
        }

        for (Map.Entry<String, Integer> item : chromosomeFrequency.entrySet()) {
            double percentageValue = Math.round( (((double)item.getValue() / numItems) * 100d) * 100d)/100d;
            percentageComposition.put(item.getKey(),percentageValue);
        }

        return percentageComposition;
    }

    private double getAverageTreeSize(){

        double averageTreeSize = 0;
        for (PopulationMember<T> populationMember : _currentPopulation) {
            averageTreeSize += populationMember.getTree().getTreeSize();
        }
        averageTreeSize = averageTreeSize / _currentPopulation.size();

        return averageTreeSize;
    }

    @Override
    public void reset() {
        _currentPopulation = new ArrayList<>();
        _nextPopulation = new ArrayList<>();
        _populationStatistics = new ArrayList<>();
        _populationHistory = new ArrayList<>();
    }

    @Override
    public void operateOnPopulation(IGeneticOperator<T> operator) {
        List<PopulationMember<T>> unvisitedMembers = getUnvisitedMembers();
        List<PopulationMember<T>> chosenMembers = new ArrayList<>();

        //Get the number of members required
        int numberOfMembersRequired = operator.getInputCount();

        for (int i = 0; i < numberOfMembersRequired; i++) {
            int chosenIndex = _randomNumberGenerator.nextInt(unvisitedMembers.size());
            PopulationMember<T> chosenMember = unvisitedMembers.get(chosenIndex);
            unvisitedMembers.remove(chosenIndex);

            chosenMembers.add(chosenMember.getCopy());
        }

        List<NodeTree<T>> newChromosomes = operator.operate(chosenMembers);

        for (NodeTree<T> newChromosome : newChromosomes) {
            if(!newChromosome.isValid()){
                throw new RuntimeException("Operation created invalid tree");
            }

        }
        //Create new trees from the new members
        for (NodeTree<T> newChromosome : newChromosomes) {
            PopulationMember<T> newMember = new PopulationMember<>(newChromosome);
            _nextPopulation.add(newMember);
        }
    }

    @Override
    public void setNewPopulation() {
        _currentPopulation = _nextPopulation;
        _nextPopulation = new ArrayList<>();

        for (PopulationMember<T> member : _currentPopulation) {
            IMemberStatistics memberStatistics = _fitnessFunction.calculateFitness(member.getTree());
            member.setFitness(memberStatistics.getFitness());
            _populationStatistics.add(memberStatistics);
        }

        PopulationStatistics populationStatistics = StatisticsManager.calculateAverages(_populationStatistics);
        _populationHistory.add(populationStatistics);

        double averageTreeSize = getAverageTreeSize();
        var treeComposition = getTreeComposition();

        var stats = new PopulationStatistics();
        stats.setMeasure("Tree size",averageTreeSize);

        for (Map.Entry<String, Double> item : treeComposition.entrySet()) {
            stats.setMeasure(item.getKey(),item.getValue());
        }
        compositionHistory.add(stats);
    }

    @Override
    public ITreeGenerator<T> getTreeGenerator() {
        return _generator;
    }

    private List<PopulationMember<T>> getUnvisitedMembers() {
        List<PopulationMember<T>> members = new ArrayList<>();

        for (PopulationMember<T> member : _currentPopulation) {
            if(!member.isVisited())
                members.add(member);
        }

        return members;
    }
}
