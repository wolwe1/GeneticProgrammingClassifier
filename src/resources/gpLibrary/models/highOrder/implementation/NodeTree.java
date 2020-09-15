package resources.gpLibrary.models.highOrder.implementation;

import resources.gpLibrary.functionality.implementation.BreadthFirstVisitor;
import resources.gpLibrary.functionality.implementation.TreeCombinationVisitor;
import resources.gpLibrary.functionality.interfaces.ITreeVisitor;
import resources.gpLibrary.models.primitives.implementation.Node;

import java.util.ArrayDeque;
import java.util.Queue;

public class NodeTree<T>
{
    //Structure
    public  Node<T> root;
    public int depth;
    public final int maxDepth;
    public final int maxBreadth;

    private final int _maxNodes;

    public NodeTree(int maxDepth,int maxBreadth)
    {
        this.maxDepth = maxDepth;
        this.maxBreadth = maxBreadth;

        root = null;
        _maxNodes = calculateMaximumSize(maxBreadth,maxDepth);
    }

    /**
     * Creates a tree with the same specification as the other, does not copy nodes
     * @param other
     */
    public NodeTree(NodeTree<T> other){
        this.maxDepth = other.maxDepth;
        this.maxBreadth = other.maxBreadth;

        root = null;
        _maxNodes = other._maxNodes;
    }

    /**
     * Returns the amount of nodes in the tree
     * @return The amount of nodes in the tree
     */
    public int getTreeSize()
    {
        return sumNodes(root);
    }

    /**
     * Adds a node into the tree, breadth first manner
     * @param node The node to be added
     * @throws Exception If the tree is full
     */
    public void addNode(Node<T> node) throws Exception {

        if (getTreeSize() == _maxNodes)
            throw new Exception("Tree full");

        node._maxChildren = maxBreadth;
        //Empty tree
        if (root == null)
        {
            node._level = 0;
            root = node;
        }
        else
        {
            breadthFirstInsert(node);
        }
    }

    /**
     * This function allows for a visitor to perform an action on the trees members, breadth first
     * @param visitor The visitor object that will have its visit function called on every node
     */
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

    /**
     * Returns the total amount of possible leaf nodes the tree can contain
     * @return
     */
    public int getNumberOfPossibleLeafNodes() {
        return (int) Math.pow(maxBreadth,maxDepth - 1);
    }

    /**
     * Calculates the hypothetical maximum size of the tree
     * @param maxBreadth The breadth of the tree, ie. The number of children each node may have
     * @param maxDepth The maximum depth of the tree, ie. The maximum number of nodes from root to terminal
     * @return The maximum number of nodes the tree can hold
     */
    private int calculateMaximumSize(int maxBreadth, int maxDepth)
    {
        int total = 0;

        for (int i = 0; i < maxDepth; i++)
        {
            total += (int)Math.pow(maxBreadth, i);
        }

        return total;
    }


    /**
     * Counts the number of nodes in the tree
     * @param node The root of the tree
     * @return The amount of nodes in the tree
     */
    private int sumNodes(Node<T> node)
    {
        int total = 0;

        if (node == null)
            return 0;

        for(var child : node.getChildren())
        {
            total += sumNodes(child);
        }
        return total + 1;
    }

    /**
     * Inserts a new node into the tree, breadth first manner
     * @param node The node to be added to the tree
     * @throws Exception If the tree is full
     */
    private void breadthFirstInsert(Node<T> node) throws Exception {
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

    /**
     * Returns a string representing the tree's members, in breadth first order
     * @return The trees makeup
     */
    public String getCombination() {

        //Get the nodes in breadth first order
        TreeCombinationVisitor<T> visitor = new TreeCombinationVisitor<>();
        visitTree(visitor);

        return visitor.getCombination();
    }

    public Node<T> getNode(int nodeIndex) throws Exception {

        BreadthFirstVisitor<T> visitor = new BreadthFirstVisitor<>();
        visitTree(visitor);

        return visitor.getNode(nodeIndex);
    }

    public void clearLeaves() {
        root.removeLeaves();
    }

    public NodeTree<T> getCopy(){
        NodeTree<T> newTree = new NodeTree<>(maxDepth,maxBreadth);
        try {
            newTree.addNode(root.getCopy(true));
        } catch (Exception e) {
            throw new RuntimeException("Unable to copy tree");
        }
        newTree.depth = depth;
        return newTree;
    }
}