package resources.gpLibrary.models.classification;

import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.highOrder.implementation.FunctionalSet;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.PopulationMember;
import resources.gpLibrary.models.highOrder.implementation.TerminalSet;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassificationTreeGenerator<T> implements ITreeGenerator<T> {

    private final int maxTreeDepth;
    private final int maxTreeBreadth;
    private final FunctionalSet<T> functionalSet;
    private final TerminalSet<T> terminals;
    private Random randomGenerator;
    private int ratio;

    public ClassificationTreeGenerator(FunctionalSet<T> functionalSet,TerminalSet<T> terminals,int maxTreeDepth, int maxTreeBreadth){
        this.functionalSet = functionalSet;
        this.terminals = terminals;

        this.maxTreeDepth = maxTreeDepth;
        this.maxTreeBreadth = maxTreeBreadth;
        randomGenerator = new Random(0);
        ratio = 20;
    }

    @Override
    public NodeTree<T> createRandom() {
        ClassificationTree<T> newTree = new ClassificationTree<>(maxTreeDepth,maxTreeBreadth);
        try {
            newTree.addNode(pickFunction());
        } catch (Exception e) {
            throw new RuntimeException("Unable to create tree root");
        }
        //TODO: Ensure branch uniqueness
        return fillTree(newTree);
    }

    private Node<T> pickNode() {
        int choice = randomGenerator.nextInt(100);

        if(choice < ratio)
            return pickTerminal();
        else
            return pickFunction();
    }

    private Node<T> pickFunction() {
        int functionIndex = randomGenerator.nextInt(functionalSet.size());
        return functionalSet.get(functionIndex);
    }

    private Node<T> pickTerminal() {
        int terminalIndex = randomGenerator.nextInt(terminals.size());
        return terminals.get(terminalIndex);
    }

    @Override
    public NodeTree<T> create(String chromosome) {
        NodeTree<T> newTree = new ClassificationTree<>(maxTreeDepth,maxTreeBreadth);
        String[] chromosomeItems = chromosome.split("\\.");

        for (String name : chromosomeItems) {
            try {
                var function = functionalSet.get(name);
                newTree.addNode(function);

            } catch (Exception e) { //Chromosome item represents a terminal

                try {
                    var terminal = terminals.get(name);
                    newTree.addNode(terminal);

                } catch (Exception exception) {
                    exception.printStackTrace();
                    throw new RuntimeException("Unable to generate tree from chromosome");
                }
            }
        }

        return newTree;
    }

    @Override
    public void setRandomFunction(Random randomNumberGenerator) {
        randomGenerator = randomNumberGenerator;
    }

    @Override
    public NodeTree<T> replaceSubTree(PopulationMember<T> chromosome) {
        ClassificationTree<T> tree = (ClassificationTree<T>) chromosome.getTree();
        int pointToReplace = randomGenerator.nextInt(tree.getTreeSize());

        Node<T> replacingNode = pickNode();

        tree.replaceNode(pointToReplace,replacingNode);
        return fillTree(tree);
    }

    private NodeTree<T> fillTree(ClassificationTree<T> tree) {
        while (!tree.IsFull()) {
            try {
                if(tree.requiresTerminals())
                    tree.addNode(pickTerminal());
                else{
                    Node<T> nodeToAdd = pickNode();

                    while(!tree.acceptsNode(nodeToAdd)){
                        nodeToAdd = pickNode();
                    }
                    tree.addNode(nodeToAdd);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to generate tree");
            }
        }
        return tree;
    }

    @Override
    public List<NodeTree<T>> replaceSubTrees(PopulationMember<T> first, PopulationMember<T> second) {
        NodeTree<T> firstTree = first.getTree();
        NodeTree<T> secondTree = second.getTree();

        int pointToReplaceInFirst = randomGenerator.nextInt(firstTree.getTreeSize());
        int pointToReplaceInSecond = randomGenerator.nextInt(secondTree.getTreeSize());

        Node<T> firstSubtree = firstTree.getNode(pointToReplaceInFirst);
        Node<T> secondSubTree = secondTree.getNode(pointToReplaceInSecond);

        firstTree.replaceNode(pointToReplaceInFirst,secondSubTree);
        secondTree.replaceNode(pointToReplaceInSecond,firstSubtree);

        List<NodeTree<T>> newTrees = new ArrayList<>();
        newTrees.add(firstTree);
        newTrees.add(secondTree);

        return newTrees;
    }

    public void setTerminalToFunctionRatio(int percentage){
        this.ratio = percentage;
    }
}
