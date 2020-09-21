package resources.gpLibrary.infrastructure.interfaces;

import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.List;
import java.util.Random;

public interface ITreeGenerator<T> {

    NodeTree<T> createRandom();

    NodeTree<T> create(String chromosome);

    void setRandomFunction(Random randomNumberGenerator);

    String replaceSubTree(PopulationMember<T> chromosome);

    List<String> replaceSubTrees(PopulationMember<T> first, PopulationMember<T> second);
}
