package resources.gpLibrary.models.primitives.nodes.interfaces;

import resources.gpLibrary.models.classification.Problem;

public interface IChoiceNode<T> {

    T feed(Problem<T> problem);
}
