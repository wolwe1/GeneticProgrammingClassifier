package resources.gpLibrary.models.primitives.implementation;

import resources.gpLibrary.models.primitives.interfaces.IValueNode;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a node that can have multiple children and a parent
 * @param <T> The type of the value stored by the node
 */
public abstract class Node<T> implements IValueNode<T>
{
    protected final List<Node<T>> _children;

    public Node<T> Parent;
    public final String name;
    public int _maxChildren;
    public int _level = 0;
    public int _drawPos = 0;
    public int _depth = 0;

    protected Node(int maxChildren,String name)
    {
        _maxChildren = maxChildren;
        this.name = name;
        _children = new ArrayList<>();
    }

    protected Node(String name)
    {
        this.name = name;
        _children = new ArrayList<>();
    }

    protected Node(Node<T> other){
        _maxChildren  = other._maxChildren;
        _depth = other._depth;
        _drawPos = other._drawPos;
        _level = other._level;

        _children = new ArrayList<>();
        name = other.name;
    }

    public Node<T> addChild(Node<T> newNode) throws Exception {

        if (IsFull()) throw new Exception("Node cannot have any more children");

        newNode.Parent = this;
        newNode._level = _level + 1;
        _children.add(newNode);

        return newNode;
    }

    public List<Node<T>> getChildren(){ return _children; }

    public Node<T> getChild(int index) throws Exception {
        if (index >= _children.size())
            return null;

        return _children.get(index);
    }

    public abstract boolean IsFull();

    protected abstract Node<T> getCopy();

    public void setChild(int index,Node<T> newChild) throws Exception {
        if(index < 0 || index >= _maxChildren)
            throw new Exception("Attempted to set a child out of range");

        newChild.Parent = this;
        _children.set(index,newChild);
    }

    public int getIndexOfChild(Node<T> child){

        for (int i = 0, childrenSize = _children.size(); i < childrenSize; i++) {
            Node<T> tNode = _children.get(i);
            if (tNode == child)
                return i;
        }
        return -1;
    }

    public void removeChildren() {
        //Check children
        for (int i = _children.size() - 1; i >= 0; i--) {
            removeChild(i);
        }
    }

    public Node<T> removeChild(int index){
        return (index >= 0 && index < _children.size()) ? _children.remove(index) : null;
    }


    public Node<T> getCopy(boolean includeChildren){
        var copy = getCopy();

        try{
            if(includeChildren){
                for (Node<T> child : _children) {
                    copy.addChild(child.getCopy());
                }
            }
        }catch(Exception e){
            throw new RuntimeException("Unable to create copy");
        }

        return copy;
    };

    public void removeLeaves(){

        for (int i = _children.size() - 1; i >= 0; i--) {
            Node<T> child = _children.get(i);

            if (child instanceof TerminalNode)
                removeChild(i);
            else
                child.removeLeaves();
        }
    }
}
