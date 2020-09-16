package resources.gpLibrary.infrastructure.interfaces;

import resources.gpLibrary.models.highOrder.implementation.NodeTree;

import java.util.Random;

public interface ITreeGenerator<T> {

    NodeTree<T> createRandom();

    NodeTree<T> create(String chromosome);

    void setRandomFunction(Random randomNumberGenerator);
}
