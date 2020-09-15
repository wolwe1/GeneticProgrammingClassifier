package resources.gpLibrary.functionality.interfaces;

import resources.gpLibrary.models.primitives.implementation.Node;

public interface ITreeVisitor<T> {
    void visit(Node<T> temp);
}
