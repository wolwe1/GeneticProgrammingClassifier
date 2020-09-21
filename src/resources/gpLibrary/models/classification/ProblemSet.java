package resources.gpLibrary.models.classification;

import java.util.ArrayList;
import java.util.List;

public class ProblemSet {

    List<Problem> problems;

    public ProblemSet(List<String> data,List<String> dataNames){

        problems = new ArrayList<>();

        for (String datum : data) {
            Problem problemEntry = new Problem();
            String[] dataPoints = datum.split(",");

            for (int i = 0, dataNamesSize = dataNames.size(); i < dataNamesSize; i++) {
                String dataName = dataNames.get(i);
                String dataValue = dataPoints[i];

                problemEntry.addItem(dataName,dataValue);
            }
            problems.add(problemEntry);
        }
    }

}
