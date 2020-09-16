package resources.gpLibrary.models.highOrder.implementation;

import resources.gpLibrary.models.primitives.implementation.GeneticFunction;

import java.util.ArrayList;
import java.util.List;

public class FunctionalSet<T>
{
    private final List<GeneticFunction<T>> _functions;

    public FunctionalSet(){
        _functions = new ArrayList<>();
    }

    public List<GeneticFunction<T>> get_functions() {
        return _functions;
    }

    public void addFunction( GeneticFunction<T> newFunc){
        _functions.add(newFunc);
    }

    public int size(){
        return _functions.size();
    }

    public GeneticFunction<T> get(int index){

        if(index < 0 || index >= size())
            throw new RuntimeException("Attempted to access function out of range");

        return (GeneticFunction<T>) _functions.get(index).getCopy(false);
    }

    public GeneticFunction<T> get(String functionName) throws Exception {

        for (GeneticFunction<T> function : _functions) {
            if(function.name == functionName)
                return (GeneticFunction<T>) function.getCopy(false);
        }

        throw new Exception("Attempted to access function '"+functionName+"' which does not exist");
    }
}
