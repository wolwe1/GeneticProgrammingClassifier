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
    public String feed(Problem problem) {
        return name;
    }
}
