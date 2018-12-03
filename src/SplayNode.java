public class SplayNode<T> {
    SplayNode<T> left, right, parent;
    T value;
    public SplayNode(T k) {
        this(k, null, null, null);
    }

    public SplayNode() {
        this(null,null, null, null);
    }

    public SplayNode(T k, SplayNode left, SplayNode right, SplayNode parent) {
        this.left = left;
        this.right = right;
        this.parent = parent;
        this.value = k;
    }
}