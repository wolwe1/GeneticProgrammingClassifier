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
    public List<PopulationMember<T>> operate(int numberOfOperations) {
        return null;
    }

    @Override
    public int getOutputCount() {
        return _outputCount;
    }

    public void setOutputCount(int outputCount) {
        _outputCount = outputCount;
    }

    public double get_ratio() {
        return _ratio;
    }

    public void set_ratio(double _ratio) {
        this._ratio = _ratio;
    }

    public int get_populationSize() {
        return _populationSize;
    }

    public void set_populationSize(int _populationSize) {
        this._populationSize = _populationSize;
    }
}
