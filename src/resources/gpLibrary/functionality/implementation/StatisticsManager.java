package resources.gpLibrary.functionality.implementation;

import resources.gpLibrary.models.highOrder.interfaces.IMemberStatistics;
import resources.gpLibrary.models.highOrder.implementation.PopulationStatistics;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsManager {

    public static PopulationStatistics calculateAverages(List<IMemberStatistics> populationStatistics) {

        Map<String,Double> measures = new HashMap<>();
        var possibleMeasures = populationStatistics.get(0).getMeasures();

        for (Map.Entry<String,Double> measure : possibleMeasures){
            //Set a measure of 0
            Map.Entry<String,Double> averageForMeasure = new AbstractMap.SimpleEntry<>(measure.getKey(), 0d);

            for (IMemberStatistics stat : populationStatistics) {
                Map.Entry<String,Double> measureForThisMember = stat.getMeasure(averageForMeasure.getKey());

                averageForMeasure.setValue(averageForMeasure.getValue() + measureForThisMember.getValue());
            }
            measures.put(averageForMeasure.getKey(),averageForMeasure.getValue());
        }

        //Now have the total value for each measure, average it and set it in the populationStatistics
        PopulationStatistics averagePopStats = new PopulationStatistics();

        for (Map.Entry<String, Double> measure : measures.entrySet()) {
            measure.setValue( measure.getValue()/populationStatistics.size());
            averagePopStats.addMeasure(measure);
        }

        return averagePopStats;

    }
}
