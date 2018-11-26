public class SplayNode<T> {
    SplayNode<T> left;
    SplayNode<T> right;
    SplayNode<T>parent;
    final T value;

    public SplayNode(T value) {
        this.value = value;
    }
}
