package resources.gpLibrary.models.classification;

import resources.gpLibrary.functionality.interfaces.ITreeVisitor;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClassificationTree<T> extends NodeTree<T> {

    private ChoiceNode<T> root;

    public ClassificationTree(int maxDepth, int maxBreadth) {
        super(maxDepth, maxBreadth);
    }

    public ClassificationTree(NodeTree<T> other) {
        super(other);
    }

    public T feedProblem(Problem problem){
        return root.feed(problem);
    }


    @Override
    public int getTreeSize()
    {
        return sumNodes(root);
    }

    @Override
    public void addNode(Node<T> node) throws Exception {
        if (IsFull())
            throw new Exception("Tree full");

        //node._maxChildren = maxBreadth;
        //Empty tree
        if (root == null)
        {
            node._level = 0;
            root = (ChoiceNode<T>) node;
        }
        else
        {
            breadthFirstInsert(node);
        }
    }

    @Override
    public void visitTree(ITreeVisitor<T> visitor)
    {
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            visitor.visit(temp);

            queue.addAll(temp.getChildren());
        }
    }

    protected void breadthFirstInsert(Node<T> node) throws Exception {
        Queue<Node<T>> queue = new ArrayDeque<>();
        Node<T> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            if (!temp.IsFull())
            {
                temp.addChild(node);
                return;
            }

            queue.addAll(temp.getChildren());
        }
    }

    @Override
    public void clearLeaves() {
        root.removeLeaves();
    }

    public NodeTree<T> getCopy(){
        NodeTree<T> newTree = new ClassificationTree<>(maxDepth, maxBreadth);
        try {
            newTree.addNode(root.getCopy(true));
        } catch (Exception e) {
            throw new RuntimeException("Unable to copy tree");
        }
        newTree.depth = depth;
        return newTree;
    }

    @Override
    public boolean IsFull() {
        return !root.canTakeMoreChildren();
    }

    @Override
    public boolean requiresTerminals() {
        return root.requiresTerminals();
    }

}
