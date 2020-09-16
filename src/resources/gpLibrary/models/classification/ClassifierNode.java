package resources.gpLibrary.models.classification;

import resources.gpLibrary.models.primitives.implementation.Node;

public class ClassifierNode extends Node<Integer> {

    protected ClassifierNode(int maxChildren, String name) {
        super(maxChildren, name);
    }

    protected ClassifierNode(String name) {
        super(name);
    }

    protected ClassifierNode(Node<Integer> other) {
        super(other);
    }

    @Override
    public boolean IsFull() {
        return false;
    }

    @Override
    protected Node<Integer> getCopy() {
        return null;
    }

    @Override
    public Integer getValue() {
        return null;
    }

    @Override
    public Integer getBaseValue() {
        return null;
    }

    public Integer feed(ProblemSet problem) {
        //TODO: Implement feed
        return 0;
    }
}
