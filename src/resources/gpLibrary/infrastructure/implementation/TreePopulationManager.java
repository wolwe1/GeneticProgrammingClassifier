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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreePopulationManager<T> implements IPopulationManager<T> {

    protected Random _randomNumberGenerator;
    protected ITreeGenerator<T> _generator;
    protected IFitnessFunction<T> _fitnessFunction;
    protected List<PopulationMember<T>> _currentPopulation;
    protected List<PopulationMember<T>> _nextPopulation;

    //Statistics
    protected List<IMemberStatistics> _populationStatistics;
    protected List<PopulationStatistics> _populationHistory;


    public TreePopulationManager(ITreeGenerator<T> treeGenerator, IFitnessFunction<T> fitnessFunction,int seed){
        _generator = treeGenerator;
        _fitnessFunction = fitnessFunction;
        _randomNumberGenerator = new Random(seed);

        _generator.setRandomFunction(_randomNumberGenerator);

        _currentPopulation = new ArrayList<>();
        _nextPopulation = new ArrayList<>();
        _populationStatistics = new ArrayList<>();
        _populationHistory = new ArrayList<>();
    }

    @Override
    public void createPopulation(int populationSize) {

        for (int i = 0; i < populationSize; i++) {
            NodeTree<T> newTree = _generator.createRandom();
            PopulationMember<T> newMember = new PopulationMember<>(newTree);

            IMemberStatistics memberStatistics = _fitnessFunction.calculateFitness(newMember.getTree());
            newMember.setFitness(memberStatistics.getFitness());

            _currentPopulation.add(newMember);
            _populationStatistics.add(memberStatistics);
        }
        PopulationStatistics populationStatistics = StatisticsManager.calculateAverages(_populationStatistics);

        _populationHistory.add(populationStatistics);
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

        if(_populationStatistics.size() != _currentPopulation.size()) throw new RuntimeException("Population and statistics did not match");

        var popstats = _populationHistory.get(_populationHistory.size() - 1);

        Printer.print("Average performance of population");
        popstats.print();
        Printer.underline();
    }

    @Override
    public void printBasicHistory() {

        for (int i = 0, historySize = _populationHistory.size(); i < historySize; i++) {
            PopulationStatistics historyStat = _populationHistory.get(i);
            PopulationMember<T> member = _currentPopulation.get(i);

            Printer.print(member.getId());
            historyStat.print();
        }
    }

    @Override
    public void printFullHistory() {

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

            chosenMembers.add(chosenMember);
        }

        List<NodeTree<T>> newChromosomes = operator.operate(chosenMembers);
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
