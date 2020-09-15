package resources.gpLibrary.models.highOrder.implementation;


import resources.gpLibrary.helpers.Printer;

import java.util.HashMap;
import java.util.Map;

public class PopulationStatistics {
    Map<String,Double> measures;

    public PopulationStatistics(){
        measures = new HashMap<>();
    }

    public void addMeasure(Map.Entry<String, Double> measure) {
        measures.put(measure.getKey(),measure.getValue());
    }

    public void print() {

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            Printer.print("Average " + measure.getKey() + ": " + measure.getValue());
        }
    }
}
