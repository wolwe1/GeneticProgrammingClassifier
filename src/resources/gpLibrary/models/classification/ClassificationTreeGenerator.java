package resources.gpLibrary.models.classification;

import resources.gpLibrary.infrastructure.interfaces.IGeneticOperator;
import resources.gpLibrary.infrastructure.interfaces.ITreeGenerator;
import resources.gpLibrary.models.highOrder.implementation.FunctionalSet;
import resources.gpLibrary.models.highOrder.implementation.NodeTree;
import resources.gpLibrary.models.highOrder.implementation.TerminalSet;
import resources.gpLibrary.models.primitives.implementation.GeneticFunction;
import resources.gpLibrary.models.primitives.implementation.TerminalNode;

import java.util.List;
import java.util.Random;

public class ClassificationTreeGenerator implements ITreeGenerator<Integer> {

    private final int maxTreeDepth;
    private final int maxTreeBreadth;
    private final FunctionalSet<Integer> functionalSet;
    private final TerminalSet<Integer> terminals;
    private Random randomGenerator;
    private int ratio;

    public ClassificationTreeGenerator(FunctionalSet<Integer> functionalSet,TerminalSet<Integer> terminals,int maxTreeDepth, int maxTreeBreadth){
        this.functionalSet = functionalSet;
        this.terminals = terminals;

        this.maxTreeDepth = maxTreeDepth;
        this.maxTreeBreadth = maxTreeBreadth;
        randomGenerator = new Random(0);
        ratio = 20;
    }

    @Override
    public NodeTree<Integer> createRandom() {
        ClassificationTree newTree = new ClassificationTree(maxTreeDepth,maxTreeBreadth);
        int maxTreeSize = newTree.getMaxNodes();

        for (int i = 0; i < maxTreeSize; i++) {
            int choice = randomGenerator.nextInt(100);

            try {
            if(choice < ratio){
                int terminalIndex = randomGenerator.nextInt(terminals.size());
                TerminalNode<Integer> terminal = terminals.get(terminalIndex);

                newTree.addNode(terminal);

            }else{
                int functionIndex = randomGenerator.nextInt(functionalSet.size());
                GeneticFunction<Integer> function = functionalSet.get(functionIndex);

                newTree.addNode(function);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newTree;
    }

    @Override
    public NodeTree<Integer> create(String chromosome) {
        NodeTree<Integer> newTree = new ClassificationTree(maxTreeDepth,maxTreeBreadth);

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
                }
            }
        }

        return newTree;
    }

    @Override
    public void setRandomFunction(Random randomNumberGenerator) {
        randomGenerator = randomNumberGenerator;
    }

    public void setTerminalToFunctionRatio(int percentage){
        this.ratio = percentage;
    }
}
