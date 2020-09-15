package resources.gpLibrary.models.primitives.interfaces;

public interface IGeneticFunction<T> extends IValueNode<T> {

    /**
     * Specifies the operation performed by the function
     * @return The calculated value by performing the operation
     */
    T Operation();
}
