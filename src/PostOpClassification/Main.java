package PostOpClassification;

import PostOpClassification.infrastructure.ClassifierFitnessFunction;
import resources.gpLibrary.infrastructure.implementation.TreePopulationManager;
import resources.gpLibrary.infrastructure.interfaces.GeneticAlgorithm;
import resources.gpLibrary.infrastructure.interfaces.IPopulationManager;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.classification.ClassificationTreeGenerator;
import resources.gpLibrary.models.classification.ProblemSet;
import resources.gpLibrary.models.highOrder.implementation.FunctionalSet;
import resources.gpLibrary.models.highOrder.implementation.TerminalSet;
import resources.gpLibrary.models.primitives.interfaces.IFitnessFunction;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int seed = 0;

        //Create fitness function
        List<ProblemSet> problemSet = new ArrayList<>();
        IFitnessFunction<Integer> fitnessFunction = new ClassifierFitnessFunction(problemSet);

        //Create node sets
        FunctionalSet<Integer> functionalSet = new FunctionalSet<>();
        functionalSet.addFunction();
        functionalSet.addFunction();
        functionalSet.addFunction();
        functionalSet.addFunction();
        functionalSet.addFunction();

        TerminalSet<Integer> terminalSet = new TerminalSet<>();
        terminalSet.addTerminal();
        terminalSet.addTerminal();
        terminalSet.addTerminal();

        ITreeGenerator<Integer> generator = new ClassificationTreeGenerator(functionalSet,terminalSet,8,3);
        IPopulationManager<Integer> manager = new TreePopulationManager<>(generator, fitnessFunction, seed);

        GeneticAlgorithm<Integer> geneticAlgorithm = new GeneticAlgorithm<>(10, 1, manager);

    }
}
