package resources.gpLibrary.models.primitives.nodes.abstractClasses;

import resources.gpLibrary.models.primitives.nodes.interfaces.IValueNode;

public abstract class ValueNode<T> extends Node<T> implements IValueNode<T> {

    protected ValueNode(String name) {
        super(name);
    }

}
