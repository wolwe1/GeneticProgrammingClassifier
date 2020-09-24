package resources.gpLibrary.infrastructure.implementation.operators;

import resources.gpLibrary.infrastructure.abstractClasses.GeneticOperator;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class Mutation<T> extends GeneticOperator<T> {

    private final ITreeGenerator<T> generator;

    protected Mutation(int inputCount, int populationSize, ITreeGenerator<T> generator) {
        super("Mutation", inputCount, inputCount, populationSize);
        this.generator = generator;
    }

    public static <T> Mutation<T> create(int populationSize, double percentageOfThePopulation, ITreeGenerator<T> generator){
        int inputCount = (int)(populationSize * percentageOfThePopulation);
        return new Mutation<>(inputCount,populationSize,generator);
    }

    @Override
    public List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes) {

        List<NodeTree<T>> mutatedChromosomes = new ArrayList<>();

        for (PopulationMember<T> chromosome : chromosomes) {
            mutatedChromosomes.add(generator.replaceSubTree(chromosome));
        }
        return mutatedChromosomes;
    }
}
