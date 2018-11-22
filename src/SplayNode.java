public class SplayNode<T> {
    SplayNode left, right, parent;
    final T value;

    public SplayNode(T value) {
        this.value = value;
    }
}
