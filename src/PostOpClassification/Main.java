package PostOpClassification;

import PostOpClassification.infrastructure.ClassifierFitnessFunction;
import resources.gpLibrary.infrastructure.implementation.TreePopulationManager;
import resources.gpLibrary.infrastructure.interfaces.GeneticAlgorithm;
import resources.gpLibrary.models.primitives.enums.PrintLevel;
import resources.gpLibrary.models.primitives.interfaces.IFitnessFunction;

public class Main {

    public static void main(String[] args) {
        int seed = 0;
        IFitnessFunction fitnessFunction = new ClassifierFitnessFunction();
        IPopulationManager<Double> manager = new TreePopulationManager<>();

        GeneticAlgorithm<Double> geneticAlgorithm = new GeneticAlgorithm<>(10,1,manager);

    }
}
