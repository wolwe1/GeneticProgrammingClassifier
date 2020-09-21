package resources.gpLibrary.models.primitives.nodes.abstractClasses;

import resources.gpLibrary.models.primitives.nodes.interfaces.IChoiceNode;

public abstract class ChoiceNode<T> extends Node<T> implements IChoiceNode<T> {

    protected ChoiceNode(String name,int numberOfChoices) {
        super(name,numberOfChoices);
    }

}
