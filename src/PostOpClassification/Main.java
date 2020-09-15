package PostOpClassification;

public class Main {

    public static void main(String[] args) {
	// write your code here
        loadTrainData();
        loadTestData();

        createFitnessFunction();
        addStatistic();
        addStatistic();

        createGeneticAlgorithm();
        setManager();

        repeat{
            bestTree = runForNGenerations();
        }

        compareBestTreesOnTestSet();
    }
}
