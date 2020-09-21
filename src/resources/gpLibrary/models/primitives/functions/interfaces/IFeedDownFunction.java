package resources.gpLibrary.models.primitives.functions.interfaces;

import resources.gpLibrary.models.classification.Problem;
import resources.gpLibrary.models.primitives.nodes.abstractClasses.ChoiceNode;

import java.util.List;

public abstract class IFeedDownFunction<T> extends ChoiceNode<T> {

    protected String matchField;

    protected List<String> choices;

    protected IFeedDownFunction(String matchField,String name,int numberOfChoices) {
        super(name,numberOfChoices);
        this.matchField = matchField;
    }
    
    @Override
    public T feed(Problem problem){

        String valueToSwitchOn = problem.getValue(matchField);

        for (int i = 0, choicesSize = choices.size(); i < choicesSize; i++) {
            String choice = choices.get(i);
            ChoiceNode<T> child = (ChoiceNode<T>) _children.get(i);

            if(choice.equals(valueToSwitchOn))
                return child.feed(problem);
        }

        throw new RuntimeException("Function did not have appropriate child to resolve problem");
    }


    @Override
    public boolean IsFull(){
        return _children.size() == _maxChildren;
    }
}
