package PostOpClassification.nodes;

import resources.gpLibrary.models.classification.Problem;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public class PatientResult extends ChoiceNode<String> {

    public PatientResult(String name) {
        super(name,0);
    }

    @Override
    public boolean IsFull() {
        return true;
    }

    @Override
    protected Node<String> getCopy() {
        return new PatientResult(this.name);
    }

    @Override
    public boolean canTakeMoreChildren() {
        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        return false;
        //throw new RuntimeException("Terminal asked if required terminals");
    }

    @Override
    public boolean hasAncestor(Node<String> nodeToAdd) {
        if(nodeToAdd.name.equals(this.name))
            return true;

        if(this.Parent == null)
            return false;

        return this.Parent.hasAncestor(nodeToAdd);
    }

    @Override
    public String feed(Problem problem) {
        return name;
    }
}
