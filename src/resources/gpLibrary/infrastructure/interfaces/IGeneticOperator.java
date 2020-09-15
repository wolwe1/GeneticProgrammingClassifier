package resources.gpLibrary.infrastructure.interfaces;

import java.util.List;

public interface IGeneticOperator<T> {

    List<String> operate(List<String> chromosomes);

    int getOutputCount();

    void setOutputCount(int count);

    int getInputCount();

    String getName();

    void setPopulationSize(int populationSize);
}
