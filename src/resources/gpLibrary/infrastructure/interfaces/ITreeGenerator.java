package resources.gpLibrary.infrastructure.interfaces;

import resources.gpLibrary.models.highOrder.implementation.NodeTree;

public interface ITreeGenerator<T> {

    NodeTree<T> createRandom();
}
