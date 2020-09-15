package resources.gpLibrary.models.highOrder.interfaces;


import java.util.Map;

public interface IMemberStatistics {

    /**
     * Returns whatever statistic measure is the fitness
     * @return The fitness value
     */
    Double getFitness();

    Map.Entry<String, Double>[] getMeasures();

    Map.Entry<String, Double> getMeasure(String key);

    void print();
}
