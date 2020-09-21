package PostOpClassification.infrastructure;

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

    ProblemSet problemSet;

    public ClassifierFitnessFunction(ProblemSet problems){
        problemSet = problems;
    }

    @Override
    public double getWorstPossibleValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public IMemberStatistics calculateFitness(NodeTree<T> populationMember) {
        return null;
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
