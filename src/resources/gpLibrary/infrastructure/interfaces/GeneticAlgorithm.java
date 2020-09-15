package resources.gpLibrary.infrastructure.interfaces;

import resources.gpLibrary.helpers.Printer;
import resources.gpLibrary.infrastructure.implementation.operators.RandomPopulationMemberGenerator;
import resources.gpLibrary.infrastructure.interfaces.IGeneticAlgorithm;
import resources.gpLibrary.infrastructure.interfaces.IGeneticOperator;
import resources.gpLibrary.infrastructure.interfaces.IPopulationManager;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;
import resources.gpLibrary.models.primitives.enums.PrintLevel;

import java.util.HashMap;

public class GeneticAlgorithm<T> implements IGeneticAlgorithm<T> {

    protected HashMap<String, IGeneticOperator<T>> _operators;
    protected IPopulationManager<T> _populationManager;

    protected int _populationSize;
    protected int _numGenerations;
    protected boolean _fixedPopulation;
    protected PrintLevel _printLevel;

    /**
     * Sets the values necessary for the genetic algorithm to operate
     * @param populationSize The size of the population to manage
     * @param numberOfGenerations The number of generations the algorithm runs for
     * @param printLevel The level of print out to be displayed
     */
    public GeneticAlgorithm(int populationSize,int numberOfGenerations,IPopulationManager<T> populationManager){
        _populationSize = populationSize;
        _numGenerations = numberOfGenerations;
        _populationManager = populationManager;

        _fixedPopulation = true;
        _printLevel = PrintLevel.ALL;

        _operators = new HashMap<>();
        _operators.put("Random", new RandomPopulationMemberGenerator<>(0d, populationSize));
    }

    /**
     * Performs a single generation, finding the best performer, displaying statistics and evolving the population
     * @param i The generation number
     * @return The best performer of the generation
     */
    public PopulationMember<T> performGeneration(int i) {

        //Prerequisites
        PopulationMember<T> bestTreeInGeneration = _populationManager.getBest();
        display(i,bestTreeInGeneration);
        evolve();

        return bestTreeInGeneration;
    }

    private void evolve() {

        evaluatePopulationIntegrity();

        for (IGeneticOperator<T> operator : _operators.values()) {
            _populationManager.operateOnPopulation(operator);
        }

        _populationManager.setNewPopulation();
    }

    /**
     * Performs the genetic algorithm, displaying statistics and returning the best performing member
     * @return The best performing member of the run
     */
    public PopulationMember<T> run() {

        if(_populationManager == null) throw new RuntimeException("Population manager was not initialised");

        PopulationMember<T> bestPerformer = null;
        int bestPerformingGeneration = 0;

        _populationManager.reset();
        _populationManager.createPopulation(_populationSize);


        for (int i = 0; i < _numGenerations; i++) {
            PopulationMember<T> best = performGeneration(i);

            if(_populationManager.firstFitterThanSecond(best,bestPerformer)){
                bestPerformer = best;
                bestPerformingGeneration = i;
            }
        }
        summarise(bestPerformer,bestPerformingGeneration);
        return bestPerformer;
    }

    public void addOperator(IGeneticOperator<T> newOperator){
        newOperator.setPopulationSize(_populationSize);
        _operators.put(newOperator.getName(),newOperator);
    }
    /**
     * Prints out the population histories statistics
     * @param bestPerformer The best performing member of the run
     * @param bestPerformingGeneration The generation the best performer was found in
     */
    protected void summarise(PopulationMember<T> bestPerformer, int bestPerformingGeneration){
        if(_printLevel == PrintLevel.NONE) return;

        Printer.printLine();
        Printer.print("Summary of algorithm performance");
        Printer.underline();

        if(_printLevel == PrintLevel.MINOR)
            _populationManager.printBasicHistory();
        else
            _populationManager.printFullHistory();

        Printer.printLine();
        Printer.print("The best tree was found in generation" + bestPerformingGeneration + " :" + bestPerformer.getId());
        Printer.print("Fitness:" + bestPerformer.getFitness());
    }

    private void evaluatePopulationIntegrity() {
        int totalNewPopulation = 0;

        for (IGeneticOperator<T> operator : _operators.values()) {
            totalNewPopulation += operator.getOutputCount();
        }

        if(totalNewPopulation > _populationSize) throw new RuntimeException("Population attempted to increase");

        //Fill up the pool if required
        if(_fixedPopulation && (totalNewPopulation < _populationSize)){
            int difference = _populationSize - totalNewPopulation;
            _operators.get("Random").setOutputCount(difference);
        }
    }

    private void display(int generation,PopulationMember<T> member) {

        if(_printLevel == PrintLevel.NONE) return;

        Printer.printLine();
        Printer.print("Generation " + generation);
        Printer.printLine();

        if(_printLevel == PrintLevel.MINOR) return;

        Printer.print("Best performing member:" + member.getId());
        Printer.underline();

        if(_printLevel == PrintLevel.MAJOR) return;

        Printer.print("Statistics:");
        _populationManager.printPopulationStatistics();
        Printer.printLine();
    }

    //Setters

    /**
     * Sets whether the population must maintain a fixed size or can reduce
     * @param val Fixed status
     */
    public void setFixedPopulation(boolean val){_fixedPopulation = val;}

    /**
     * Sets the population manager to use
     * @param populationManager The new population manager
     */
    public void setPopulationManager(IPopulationManager<T> populationManager){
        _populationManager = populationManager;
    }

    /**
     * Sets the level of output from the algorithm
     * @param level The output level
     */
    public void setPrintLevel(PrintLevel level){
        _printLevel = level;
    }


}
