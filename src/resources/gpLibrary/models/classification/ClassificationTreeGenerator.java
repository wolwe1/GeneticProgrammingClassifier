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
        int maxTreeSize = newTree.getMaxNodes();
        //TODO: Ensure branch uniqueness
        while (!newTree.IsFull()) {
            try {
                if(newTree.requiresTerminals())
                    newTree.addNode(pickTerminal());
                else{
                    newTree.addNode(pickNode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to generate tree");
            }
        }
        return newTree;
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

        for(int i = 0, n = chromosome.length() ; i < n ; i++) {
            String name = String.valueOf(chromosome.charAt(i));

            try{
                var function = functionalSet.get(name);
                newTree.addNode(function);

            }catch (Exception e){ //Chromosome item represents a terminal

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
    public String replaceSubTree(PopulationMember<T> chromosome) {
        NodeTree<T> tree = chromosome.getTree();
        int pointToReplace = randomGenerator.nextInt(tree.getTreeSize());
        int nodeToReplace = randomGenerator.nextInt(100);


        if(nodeToReplace < ratio){

            Node<T> terminal = terminals.get(randomGenerator.nextInt(terminals.size()));

            tree.replaceNode(pointToReplace,terminal);
            chromosome.setTree(tree);
            return chromosome.getId();
        }else{
            Node<T> function = functionalSet.get(randomGenerator.nextInt(functionalSet.size()));

            while (!function.IsFull()){
                Node<T> terminal = terminals.get(randomGenerator.nextInt(terminals.size()));
                try {
                    function.addChild(terminal);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Unable to append child to function node while replacing subtree");
                }
            }
            tree.replaceNode(pointToReplace,function);
            chromosome.setTree(tree);
            return chromosome.getId();
        }

    }

    @Override
    public List<String> replaceSubTrees(PopulationMember<T> first, PopulationMember<T> second) {
        NodeTree<T> firstTree = first.getTree();
        NodeTree<T> secondTree = second.getTree();

        int pointToReplaceInFirst = randomGenerator.nextInt(firstTree.getTreeSize());
        int pointToReplaceInSecond = randomGenerator.nextInt(secondTree.getTreeSize());

        Node<T> firstSubtree = firstTree.getNode(pointToReplaceInFirst);
        Node<T> secondSubTree = secondTree.getNode(pointToReplaceInSecond);

        firstTree.replaceNode(pointToReplaceInFirst,secondSubTree);
        secondTree.replaceNode(pointToReplaceInSecond,firstSubtree);

        first.setTree(firstTree);
        second.setTree(secondTree);

        List<String> newTrees = new ArrayList<>();
        newTrees.add(first.getId());
        newTrees.add(second.getId());

        return newTrees;

    }

    public void setTerminalToFunctionRatio(int percentage){
        this.ratio = percentage;
    }
}
