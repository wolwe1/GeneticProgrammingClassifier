package resources.gpLibrary.models.primitives.nodes.implementation;

import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;
import resources.gpLibrary.models.primitives.nodes.interfaces.IValueNode;

public abstract class TerminalNode<T> extends Node<T> implements IValueNode<T>{

    protected T value;

    protected TerminalNode(String name){
        super(name);
    }

    @Override
    public T getValue(){
        return value;
    }

    @Override
    public boolean IsFull() {
        return true;
    }
}