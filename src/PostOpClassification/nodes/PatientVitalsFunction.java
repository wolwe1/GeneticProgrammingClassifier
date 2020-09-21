package PostOpClassification.nodes;

import resources.gpLibrary.models.primitives.functions.interfaces.IFeedDownFunction;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.Node;

public class PatientVitalsFunction extends IFeedDownFunction<String> {

    public PatientVitalsFunction(String matchField, String name,int numberOfChoices) {
        super(matchField, name,numberOfChoices);
    }

    @Override
    protected Node<String> getCopy() {
        PatientVitalsFunction newFunction = new PatientVitalsFunction(this.matchField,this.name,this._maxChildren);
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
    public boolean requiresTerminals() {
        return false; //TODO: IMPLEMENT THIS
    }
}
