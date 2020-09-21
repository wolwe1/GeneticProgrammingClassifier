package resources.gpLibrary.functionality.interfaces;

import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public interface ITreeVisitor<T> {
    void visit(Node<T> temp);
}
