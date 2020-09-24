package PostOpClassification.nodes;

import resources.gpLibrary.models.classification.Problem;
import resources.gpLibrary.models.primitives.functions.interfaces.IFeedDownFunction;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

import java.util.List;

public class PatientVitalsFunctionSingleValue extends IFeedDownFunction<String> {
    public PatientVitalsFunctionSingleValue(String matchField, String name, List<String> choices) {
        super(matchField, name,choices);
    }

    @Override
    protected Node<String> getCopy() {
        PatientVitalsFunctionSingleValue newFunction = new PatientVitalsFunctionSingleValue(this.matchField,this.name,this.choices);
        return newFunction;
    }

    @Override
    public boolean canTakeMoreChildren() {

        if(_children.size() < _maxChildren)
            return true;

        for (Node<String> child : _children) {
            if(child.canTakeMoreChildren())
                return true;
        }

        return false;
    }

    @Override
    public boolean requiresTerminals(int maxDepth) {
        if(_level == maxDepth - 1)
            return true;

        for (Node<String> child : _children) {
            if(child.requiresTerminals(maxDepth))
                return true;
        }

        return false;
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
    public String feed(Problem<String> problem){

        int value =  Integer.parseInt(problem.getValue(matchField));

        for (int i = 0, choicesSize = choices.size(); i < choicesSize; i++) {
            int choice = Integer.parseInt(choices.get(i));
            ChoiceNode<String> child = (ChoiceNode<String>) _children.get(i);

            if(value <= choice)
                return child.feed(problem);
        }

        throw new RuntimeException("Function did not have appropriate child to resolve problem");
    }
}
