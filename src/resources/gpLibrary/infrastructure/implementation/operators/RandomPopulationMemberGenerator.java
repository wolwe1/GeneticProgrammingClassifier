package resources.gpLibrary.infrastructure.implementation.operators;

import resources.gpLibrary.models.highOrder.implementation.PopulationMember;

import java.util.List;

public class RandomPopulationMemberGenerator<T> implements resources.gpLibrary.infrastructure.interfaces.IGeneticOperator<T> {

    private double _ratio;
    private int _populationSize;

    private int _outputCount;

    public RandomPopulationMemberGenerator(double ratio, int populationSize) {
        _ratio = ratio;
        _populationSize = populationSize;
        _outputCount = (int) Math.floor(_ratio * _populationSize);
    }


    @Override
    public List<String> operate(List<String> chromosomes) {
        return null;
    }

    @Override
    public int getOutputCount() {
        return _outputCount;
    }

    public void setOutputCount(int outputCount) {
        _outputCount = outputCount;
    }

    @Override
    public int getInputCount() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setPopulationSize(int populationSize) {

    }

    public double getRatio() {
        return _ratio;
    }

    public void setRatio(double _ratio) {
        this._ratio = _ratio;
    }

    public int getPopulationSize() {
        return _populationSize;
    }

}
