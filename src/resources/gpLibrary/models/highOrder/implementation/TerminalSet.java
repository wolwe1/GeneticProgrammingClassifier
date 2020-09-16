package resources.gpLibrary.models.highOrder.implementation;

import resources.gpLibrary.models.primitives.implementation.GeneticFunction;
import resources.gpLibrary.models.primitives.implementation.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class TerminalSet<T> {

    private final List<TerminalNode<T>> terminals;

    public TerminalSet(){
        terminals = new ArrayList<>();
    }

    public List<TerminalNode<T>> getTerminals() {
        return terminals;
    }

    public void addTerminal(TerminalNode<T> newFunc){
        terminals.add(newFunc);
    }

    public int size(){
        return terminals.size();
    }

    public TerminalNode<T> get(int index){

        if(index < 0 || index >= size())
            throw new RuntimeException("Attempted to access function out of range");

        return (TerminalNode<T>) terminals.get(index).getCopy(false);
    }

    public TerminalNode<T> get(String terminalName) throws Exception {

        for (TerminalNode<T> terminal : terminals) {
            if(terminal.name == terminalName)
                return (TerminalNode<T>) terminal.getCopy(false);
        }

        throw new Exception("Attempted to access terminal '"+terminalName+"' which does not exist");
    }
}
