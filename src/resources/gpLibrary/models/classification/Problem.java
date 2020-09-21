package resources.gpLibrary.models.classification;

import java.util.HashMap;

public class Problem {

    private Integer _answer;
    private final HashMap<String,String> items;

    public Problem() {
        items = new HashMap<>();
    }

    public void addItem(String descriptor, String value){
        items.put(descriptor,value);
    }

    public Integer getAnswer() {
        return _answer;
    }

    public String getValue(String matchField) {
        return items.get(matchField);
    }
}
