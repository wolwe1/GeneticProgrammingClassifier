package resources.gpLibrary.infrastructure.implementation.operators;

import resources.gpLibrary.infrastructure.abstractClasses.GeneticOperator;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.ArrayList;
import java.util.List;

public class Create<T> extends GeneticOperator<T> {

    private final ITreeGenerator<T> generator;

    protected Create(int populationSize,int outputCount,ITreeGenerator<T> generator) {
        super("Create",0,outputCount,populationSize);
        this.populationSize = populationSize;
        this.outputCount = outputCount;
        this.generator = generator;
    }

    public static <T> Create<T> create(int populationSize, double percentageOfPopulation, ITreeGenerator<T> generator){
        int outputCount = (int) (percentageOfPopulation * populationSize);
        return new Create<>(populationSize,outputCount,generator);
    }

    @Override
    public List<NodeTree<T>> operate(List<PopulationMember<T>> chromosomes) {

        List<NodeTree<T>> newChromosomes = new ArrayList<>();

        for (int i = 0; i < outputCount; i++) {
            NodeTree<T> tree = generator.createRandom();
            newChromosomes.add(tree);
        }

        return newChromosomes;
    }
}
