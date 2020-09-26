package resources.gpLibrary.models.highOrder.implementation;


import resources.gpLibrary.helpers.Printer;
import resources.gpLibrary.models.highOrder.interfaces.IMemberStatistics;

import java.util.HashMap;
import java.util.Map;

public class PopulationStatistics implements IMemberStatistics {
    Map<String,Double> measures;

    public PopulationStatistics(){
        measures = new HashMap<>();
    }

    @Override
    public Double getFitness() {
        throw new RuntimeException("Population statistics has no fitness");
    }

    @Override
    public Map<String, Double> getMeasures() {
        return measures;
    }

    @Override
    public Map.Entry<String, Double> getMeasure(String key) {

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            if(measure.getKey().equals(key))
                return measure;
        }
        throw new RuntimeException("Measure requested that does not exist");
    }

    @Override
    public void print() {

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            Printer.print("Average " + measure.getKey() + ": " + measure.getValue());
        }
    }

    @Override
    public void setMeasure(String key, double value) {
        measures.put(key,value);
    }

}
