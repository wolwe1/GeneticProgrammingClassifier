package resources.gpLibrary.models.classification;

import resources.gpLibrary.functionality.interfaces.ITreeVisitor;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.primitives.implementation.Node;

import java.util.ArrayDeque;
import java.util.Queue;

public class ClassificationTree extends NodeTree<Integer> {

    private ClassifierNode<Integer> root;

    public ClassificationTree(int maxDepth, int maxBreadth) {
        super(maxDepth, maxBreadth);
    }

    public ClassificationTree(NodeTree<Integer> other) {
        super(other);
    }

    public int feedProblem(ProblemSet problem){
        return root.feed(problem);
    }


    @Override
    public int getTreeSize()
    {
        return sumNodes(root);
    }

    @Override
    public void addNode(Node<Integer> node) throws Exception {
        if (getTreeSize() == _maxNodes)
            throw new Exception("Tree full");

        node._maxChildren = maxBreadth;
        //Empty tree
        if (root == null)
        {
            node._level = 0;
            root = (ClassifierNode<Integer>) node;
        }
        else
        {
            breadthFirstInsert(node);
        }
    }

    @Override
    public void visitTree(ITreeVisitor<Integer> visitor)
    {
        Queue<Node<Integer>> queue = new ArrayDeque<>();
        Node<Integer> temp;

        queue.add(root);

        while (queue.size() != 0)
        {
            temp = queue.remove();

            visitor.visit(temp);

            queue.addAll(temp.getChildren());
        }
    }

    protected void breadthFirstInsert(Node<Integer> node) throws Exception {
        Queue<Node<Integer>> queue = new ArrayDeque<>();
        Node<Integer> temp;

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

    public NodeTree<Integer> getCopy(){
        NodeTree<Integer> newTree = new ClassificationTree(maxDepth,maxBreadth);
        try {
            newTree.addNode(root.getCopy(true));
        } catch (Exception e) {
            throw new RuntimeException("Unable to copy tree");
        }
        newTree.depth = depth;
        return newTree;
    }

}
