package resources.gpLibrary.models.primitives.functions;


import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

/**
 * This class is used by all implementers of a genetic function
 * @param <T> The type returned by the function
 */
public abstract class GeneticFunction<T> extends Node<T> {

    protected GeneticFunction(String name) {
        super(name);
    }

    @Override
    public boolean IsFull() {
        return _children.size() == _maxChildren;
    }
}
