package resources.gpLibrary.infrastructure.interfaces;

import resources.gpLibrary.models.highOrder.implementation.PopulationMember;

public interface IGeneticAlgorithm<T> {

    PopulationMember<T> performGeneration(int i);

    PopulationMember<T> run();
}
