package resources.gpLibrary.models.classification;

import resources.gpLibrary.models.highOrder.implementation.NodeTree;

public class ClassificationTree extends NodeTree<Integer> {

    public ClassificationTree(int maxDepth, int maxBreadth) {
        super(maxDepth, maxBreadth);
    }

    public ClassificationTree(NodeTree<Integer> other) {
        super(other);
    }

    public int feedProblem(ProblemSet problem){
        root.feed()
    }
}
