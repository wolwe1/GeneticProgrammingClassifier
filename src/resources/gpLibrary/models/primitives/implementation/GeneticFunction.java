package resources.gpLibrary.models.primitives.implementation;

import resources.gpLibrary.models.primitives.interfaces.IGeneticFunction;

/**
 * This class is used by all implementers of a genetic function
 * @param <T> The type returned by the function
 */
public abstract class GeneticFunction<T> extends Node<T> implements IGeneticFunction<T> {

    protected GeneticFunction(String name) {
        super(name);
    }

    @Override
    public T getValue(){
        return Operation();
    }

    @Override
    public T getBaseValue(){
        return _children.get(0).getBaseValue();
    }

    @Override
    public boolean IsFull() {
        return _children.size() == _maxChildren;
    }
}
