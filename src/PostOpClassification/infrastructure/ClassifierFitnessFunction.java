package PostOpClassification.infrastructure;

import resources.gpLibrary.models.classification.ClassificationStatistic;
import resources.gpLibrary.models.classification.ClassificationTree;
import resources.gpLibrary.models.classification.Problem;
import resources.gpLibrary.models.classification.ProblemSet;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;
import resources.gpLibrary.models.highOrder.interfaces.IMemberStatistics;
import resources.gpLibrary.models.primitives.IFitnessFunction;

import java.util.List;

/**
 * Measures the fitness of an individual by calculating how "far" it is from the correct classification, lower is better
 */
public class ClassifierFitnessFunction<T> implements IFitnessFunction<T> {

    ProblemSet<T> problemSet;

    public ClassifierFitnessFunction(ProblemSet<T> problems){
        problemSet = problems;
    }

    @Override
    public double getWorstPossibleValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public IMemberStatistics calculateFitness(NodeTree<T> populationMember) {
        ClassificationTree<T> tree = (ClassificationTree<T>) populationMember;
        IMemberStatistics treeStats = new ClassificationStatistic();

        double accuracy = 0;
        double hits = 0;

        List<Problem<T>> problems = problemSet.getProblems();
        int totalProblems = problems.size();

        for (Problem<T> problem : problems) {
            T answer = problem.getAnswer();
            T guess = tree.feedProblem(problem);

            if(answer == guess)
                hits++;
        }
        accuracy = hits/totalProblems;

        treeStats.setMeasure("Accuracy",accuracy);
        treeStats.setMeasure("Hits",hits);

        return treeStats;
    }


    @Override
    public boolean firstFitterThanSecond(double firstFitness, double secondFitness) {
        return firstFitness < secondFitness;
    }

    @Override
    public PopulationMember<T> getFittest(List<PopulationMember<T>> list) {
        int bestFitness = Integer.MAX_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < list.size(); i++) {
            PopulationMember<T> member = list.get(i);

            if(member.getFitness() < bestFitness){
                bestFitness = (int) member.getFitness();
                bestIndex = i;
            }
        }
        return list.get(bestIndex);
    }
}
