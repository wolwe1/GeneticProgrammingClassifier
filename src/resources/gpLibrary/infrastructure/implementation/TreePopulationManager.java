package resources.gpLibrary.infrastructure.implementation;

import resources.gpLibrary.infrastructure.interfaces.IGeneticOperator;
import resources.gpLibrary.infrastructure.interfaces.IPopulationManager;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;
import resources.gpLibrary.models.primitives.interfaces.IFitnessFunction;

import java.util.ArrayList;
import java.util.List;

public class TreePopulationManager<T> implements IPopulationManager<T> {

    protected ITreeGenerator<T> _generator;
    protected IFitnessFunction<T> _fitnessFunction;
    protected List<PopulationMember<T>> _currentPopulation;
    protected List<PopulationMember<T>> _nextPopulation;

    public TreePopulationManager(ITreeGenerator<T> treeGenerator, IFitnessFunction<T> fitnessFunction){
        _generator = treeGenerator;
        _fitnessFunction = fitnessFunction;
        _currentPopulation = new ArrayList<>();
        _nextPopulation = new ArrayList<>();
    }

    @Override
    public void createPopulation(int populationSize) {

        for (int i = 0; i < populationSize; i++) {
            NodeTree<T> newTree = _generator.createRandom();
            PopulationMember<T> newMember = new PopulationMember<>(newTree);
            newMember.setFitness(_fitnessFunction.getWorstPossibleValue());

            _currentPopulation.add(newMember);
        }
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
        return null;
    }

    @Override
    public void printPopulationStatistics() {

    }

    @Override
    public void printBasicHistory() {

    }

    @Override
    public void printFullHistory() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void operateOnPopulation(IGeneticOperator<T> operator) {

    }

    @Override
    public void setNewPopulation() {

    }
}
