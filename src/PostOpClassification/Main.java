package PostOpClassification;

import PostOpClassification.nodes.PatientResult;
import PostOpClassification.nodes.PatientVitalsFunction;
import PostOpClassification.infrastructure.ClassifierFitnessFunction;
import PostOpClassification.nodes.PatientVitalsFunctionSingleValue;
import resources.gpLibrary.infrastructure.implementation.TreePopulationManager;
import resources.gpLibrary.infrastructure.implementation.operators.Crossover;
import resources.gpLibrary.infrastructure.implementation.operators.LazyReproduction;
import resources.gpLibrary.infrastructure.abstractClasses.GeneticAlgorithm;
import resources.gpLibrary.infrastructure.implementation.operators.Mutation;
import resources.gpLibrary.infrastructure.interfaces.IPopulationManager;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.classification.ClassificationTreeGenerator;
import resources.gpLibrary.models.classification.ProblemSet;
import resources.gpLibrary.models.highOrder.implementation.FunctionalSet;
import resources.gpLibrary.models.highOrder.implementation.TerminalSet;
import resources.gpLibrary.models.primitives.IFitnessFunction;
import resources.helpers.FileManager;

import java.util.Arrays;

public class Main {

    //1. L-CORE (patient's internal temperature in C): high (> 37), mid (>= 36 and <= 37), low (< 36)
    //2. L-SURF (patient's surface temperature in C): high (> 36.5), mid (>= 36.5 and <= 35), low (< 35)
    //3. L-O2 (oxygen saturation in %): excellent (>= 98), good (>= 90 and < 98), fair (>= 80 and < 90), poor (< 80)
    //4. L-O2 (last measurement of blood pressure): high (> 130/90), mid (<= 130/90 and >= 90/70), low (< 90/70)
    //5. SURF-STBL (stability of patient's surface temperature): stable, mod-stable, unstable
    //6. CORE-STBL (stability of patient's core temperature) stable, mod-stable, unstable
    //7. BP-STBL (stability of patient's blood pressure) stable, mod-stable, unstable
    //8. COMFORT (patient's perceived comfort at discharge, measured as an integer between 0 and 20)
    //9. decision ADM-DECS (discharge decision): I (patient sent to Intensive Care Unit),S (patient prepared to go home),
    //A (patient sent to general hospital floor)
    static String dataNames = "L-CORE,L-SURF,L-O2,L-BP,SURF-STBL,CORE-STBL,BP-STBL,COMFORT,ADM-DECS";

    public static void main(String[] args) {
        int seed = 0;
        int populationSize = 10;
        int numberOfGenerations = 1;

        FileManager fileManager = new FileManager();
        fileManager.setupDirectories();

        //Create fitness function
        ProblemSet<String> problemSet = new ProblemSet<>(fileManager.getData(), Arrays.asList(dataNames.split(",")),"ADM-DECS");
        IFitnessFunction<String> fitnessFunction = new ClassifierFitnessFunction<>(problemSet);

        //Create node sets
        FunctionalSet<String> functionalSet = new FunctionalSet<>();
        functionalSet.addFunction(new PatientVitalsFunction("L-CORE","L-CORE",Arrays.asList("high","mid","low")));
        functionalSet.addFunction(new PatientVitalsFunction("L-SURF","L-SURF",Arrays.asList("high","mid","low")));
        functionalSet.addFunction(new PatientVitalsFunction("L-O2","L-O2",Arrays.asList("excellent","good","fair","poor")));
        functionalSet.addFunction(new PatientVitalsFunction("L-BP","L-BP",Arrays.asList("high","mid","low")));
        functionalSet.addFunction(new PatientVitalsFunction("SURF-STBL","SURF-STBL",Arrays.asList("stable","mod-stable","unstable")));
        functionalSet.addFunction(new PatientVitalsFunction("CORE-STBL","CORE-STBL",Arrays.asList("stable","mod-stable","unstable")));
        functionalSet.addFunction(new PatientVitalsFunction("BP-STBL","BP-STBL",Arrays.asList("stable","mod-stable","unstable")));
        functionalSet.addFunction(new PatientVitalsFunctionSingleValue("COMFORT","COMFORT",Arrays.asList("7","15","20"))); //TODO: This is a special case where a choice must be created from a single number

        TerminalSet<String> terminalSet = new TerminalSet<>();
        terminalSet.addTerminal(new PatientResult("I"));
        terminalSet.addTerminal(new PatientResult("S"));
        terminalSet.addTerminal(new PatientResult("A"));

        ITreeGenerator<String> generator = new ClassificationTreeGenerator<>(functionalSet,terminalSet,8,4);
        IPopulationManager<String> manager = new TreePopulationManager<>(generator, fitnessFunction, seed);

        GeneticAlgorithm<String> geneticAlgorithm = new GeneticAlgorithm<>(populationSize, numberOfGenerations, manager);
        geneticAlgorithm.addOperator(Crossover.create(populationSize,0.6,generator));
        geneticAlgorithm.addOperator(Mutation.create(populationSize,0.2,generator));
        geneticAlgorithm.addOperator( LazyReproduction.create(2,populationSize,0.2,fitnessFunction));

        geneticAlgorithm.run();
    }
}
