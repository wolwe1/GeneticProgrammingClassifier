package resources.gpLibrary.infrastructure.interfaces;

import resources.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.List;

public interface IGeneticOperator<T> {

    List<PopulationMember<T>> operate(int numberOfOperations,)

    int getOutputCount();
    void setOutputCount(int count);
}
