package resources.gpLibrary.models.classification;

import java.util.ArrayList;
import java.util.List;

public class ProblemSet<T> {

    List<Problem<T>> problems;

    public ProblemSet(List<String> data,List<String> dataNames,String answerField){

        problems = new ArrayList<>();

        for (String datum : data) {
            Problem<T> problemEntry = new Problem<T>(answerField);
            String[] dataPoints = datum.split(",");

            for (int i = 0, dataNamesSize = dataNames.size(); i < dataNamesSize; i++) {
                String dataName = dataNames.get(i);
                String dataValue = dataPoints[i];

                problemEntry.addItem(dataName,(T)dataValue);
            }
            problems.add(problemEntry);
        }
    }

    public List<Problem<T>> getProblems() {
        return problems;
    }
}
