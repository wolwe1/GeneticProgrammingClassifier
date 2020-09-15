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
import resources.gpLibrary.models.primitives.interfaces.IFitnessFunction;

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


        for (int i = 0, populationStatisticsSize = _populationStatistics.size(); i < populationStatisticsSize; i++) {
            IMemberStatistics populationStatistic = _populationStatistics.get(i);
            PopulationMember<T> member = _currentPopulation.get(i);

            Printer.print(member.getId());
            populationStatistic.print();
        }
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
        List<String> unvisitedMembers = getUnvisitedMemberIds();
        List<String> chosenMembers = new ArrayList<>();

        //Get the number of members required
        int numberOfMembersRequired = operator.getInputCount();

        for (int i = 0; i < numberOfMembersRequired; i++) {
            int chosenIndex = _randomNumberGenerator.nextInt(unvisitedMembers.size());
            String chosenMember = unvisitedMembers.get(chosenIndex);
            unvisitedMembers.remove(chosenIndex);

            chosenMembers.add(chosenMember);
        }

        List<String> newChromosomes = operator.operate(chosenMembers);
        //Create new trees from the new members
        for (String newChromosome : newChromosomes) {
            NodeTree<T> newTree = _generator.create(newChromosome);
            PopulationMember<T> newMember = new PopulationMember<>(newTree);
            _nextPopulation.add(newMember);
        }

    }

    @Override
    public void setNewPopulation() {
        _currentPopulation = _nextPopulation;
        _nextPopulation = new ArrayList<>();
    }

    private List<String> getUnvisitedMemberIds() {
        List<String> memberIds = new ArrayList<>();

        for (PopulationMember<T> member : _currentPopulation) {
            if(!member.isVisited())
                memberIds.add(member.getId());
        }

        return memberIds;
    }
}
