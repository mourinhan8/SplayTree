import java.util.*;

public class SplayTree<T extends Comparable<T>> implements SortedSet<T> {
    private SplayNode<T> root, left, right;
    private int count;

    private void setParent(SplayNode<T> child, SplayNode<T> parent) {
        if (child != null) {
            child.parent = parent;
        }
    }

    private void keepParent(SplayNode<T> t) {
        setParent(t.left, t);
        setParent(t.right, t);
    }

    private void rotate(SplayNode<T> parent, SplayNode<T> child) {
        SplayNode grandParent = parent.parent;
        if (grandParent != null) {
            if (grandParent.left == parent)
                grandParent.left = child;
            else grandParent.right = child;
        }
        if (grandParent.left == child) {
            parent.left = child.right;
            child.right = parent;
        } else {
            parent.right = child.left;
            child.left = parent;
        }
        keepParent(child);
        keepParent(parent);
        child.parent = grandParent;
    }

    private SplayNode splay(SplayNode<T> n) {
        if (n.parent == null)
            return n;
        SplayNode<T> p = n.parent;
        SplayNode<T> gp = p.parent;
        if (gp == null) {
            rotate(p, n);
            return n;
        } else {
            if ((p.left == n) == (gp.left == p)) {
                rotate(gp, p);
                rotate(p, n);
            } else {
                rotate(p, n);
                rotate(gp, n);
            }
        }
        return splay(n);
    }

    private SplayNode<T> search(T value) {
        if (root == null) return null;
        return search(root, value);
    }

    private SplayNode<T> search(SplayNode<T> n, T v) {
        if (n == null)
            return null;
        int e = v.compareTo(n.value);
        if (e == 0)
            return splay(n);
        else if (e < 0)
            return search(n.left, v);
        else return search(n.right, v);
    }

    private SplayNode<T> findClosest(SplayNode<T> start, T v) {
        if (start == null) {
            return null;
        }
        int e = v.compareTo(start.value);
        if (e == 0)
            return splay(start);
        else if (e < 0){
            if (start.left == null) return splay(start);
            return findClosest(start.left, v);
        }
        else {
            if (start.right == null) return splay(start);
            return findClosest(start.right, v);
        }
    }

    private void split(SplayNode<T> r, T v) {
        if (r == null) {
            left = null;
            right = null;
            return;
        }
        r = findClosest(r, v);
        int e = v.compareTo(r.value);
        if (e > 0) {
            right = r.right;
            r.right = null;
            setParent(right, null);
            left = r;
        } else {
            left = r.left;
            r.left = null;
            setParent(left, null);
            right = root;
        }
    }

    private SplayNode<T> merge(SplayNode<T> left, SplayNode<T> right) {
        if (right == null)
            return left;
        if (left == null)
            return right;
        right = findClosest(right, left.value);
        right.left = left;
        left.parent = right;
        return right;
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator();
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (fromElement == null || toElement == null) throw new IllegalArgumentException();
        if (fromElement.compareTo(toElement) > 0) throw new IllegalArgumentException();
        return subSet(fromElement, toElement, false);
    }

    private SortedSet<T> subSet(T fromElement, T toElement, boolean i) {
        SortedSet<T> set = new TreeSet<>();
        subSet(root, set, fromElement, toElement, i);
        return set;
    }

    private void subSet(SplayNode<T> c, SortedSet<T> set, T fromElement, T toElement, boolean i) {
        if (c == null)
            return;
        int cf = c.value.compareTo(fromElement);
        int ct = c.value.compareTo(toElement);
        if (cf > 0)
            subSet(c.left, set, fromElement, toElement, i);
        if (i) {
            if (cf >= 0 && ct <= 0)
                set.add(c.value);
        } else {
            if (cf >= 0 && ct < 0)
                set.add(c.value);
        }
        if (ct < 0)
            subSet(c.right, set, fromElement, toElement, i);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        if (first().compareTo(toElement) > 0) throw new IllegalArgumentException();
        return subSet(first(), toElement, true);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (last().compareTo(fromElement) < 0) throw new IllegalArgumentException();
        return subSet(fromElement, last(), true);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        SplayNode<T> temp = root;
        SplayNode<T> min = findMin(temp);
        return min.value;
    }

    private SplayNode<T> findMin(SplayNode<T> node) {
        if (node.left == null) return node;
        else return findMin(node.left);
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        SplayNode<T> temp = root;
        SplayNode<T> max = findMax(temp);
        return max.value;
    }

    private SplayNode<T> findMax(SplayNode<T> node) {
        if (node.right == null) return node;
        else return findMax(node.right);
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        if (root == null || o.getClass() != root.value.getClass())
            return false;
        SplayNode n = search((T) o);
        if (n == null)
            return false;
        else {
            root = n;
            return true;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

    public class SplayTreeIterator implements Iterator<T> {

        private SplayNode<T> current = null;
        private int location = 0;
        private List<SplayNode<T>> list;

        SplayTreeIterator() {
            list = new ArrayList<>();
            addToList(root);
        }

        private void addToList(SplayNode<T> node) {
            if (node != null) {
                addToList(node.left);
                list.add(node);
                addToList(node.right);
            }
        }

        private SplayNode<T> findNext() {
            return list.get(location++);
        }

        @Override
        public boolean hasNext() {
            return location < list.size();
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        @Override
        public void remove() {
            SplayTree.this.remove(list.get(location - 1).value);
            list.remove(location - 1);
            location--;
        }
    }

    @Override
    public Object[] toArray() {
        Iterator iteratorIt = new SplayTreeIterator();
        Object[] arr = new Object[count];
        int index = 0;
        while (iteratorIt.hasNext()) {
            arr[index] = iteratorIt.next();
            index++;
        }
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] e = toArray();
        if (a.length < count)
            return (T[]) Arrays.copyOf(e, count, a.getClass());
        System.arraycopy(e, 0, a, 0, count);
        if (a.length > count)
            a[count] = null;
        return a;
    }

    @Override
    public boolean add(T t) {
        if (contains(t)) return false;
        root = insert(t);
        count++;
        return true;
    }

    private SplayNode<T> insert(T value) {
        split(root, value);
        root = new SplayNode(value);
        root.left = left;
        root.right = right;
        keepParent(root);
        return root;
    }

    @Override
    public boolean remove(Object o) {
        if(!contains(o)) return false;
        SplayNode<T> node = remove(root, (T) o);
        if (node == null) {
            if (root == null) {
                count--;
                return true;
            }
            return false;
        }
        root = node;
        count--;
        return true;
    }

    private SplayNode<T> remove(SplayNode<T> node, T v) {
        node = search(node, v);
        if (node == null) return null;
        setParent(node.left, null);
        setParent(node.right, null);
        SplayNode temp = merge(node.left, node.right);
        if (temp == null) {
            root = null;
            return null;
        } else return temp;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator iteratorIt = c.iterator();
        Object o;
        if (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (root == null || o.getClass() != root.value.getClass()) {
                return false;
            }
        } else return false;
        if (!contains(o)) return false;
        while (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Iterator iteratorIt = c.iterator();
        Object o;
        if (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (root == null || o.getClass() != root.value.getClass())
                return false;
        } else return false;
        boolean f = false;
        if (add((T) o))
            f = true;
        while (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (add((T) o))
                f = true;
        }
        return f;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Iterator iteratorIt = c.iterator();
        Object o;
        if (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (root == null || o.getClass() != root.value.getClass())
                return false;
        } else return false;
        boolean f = false;
        SplayTree<T> newTree = new SplayTree<>();
        if (contains(o)) {
            newTree.add((T) o);
            f = true;
        }
        while (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (contains(o))
                newTree.add((T) o);
            else f = true;
        }
        root = newTree.root;
        count = newTree.size();
        return f;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Iterator iteratorIt = c.iterator();
        Object o;
        if (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (root == null || o.getClass() != root.value.getClass())
                return false;
        } else return false;
        boolean f = false;
        if (remove(o))
            f = true;
        while (iteratorIt.hasNext()) {
            o = iteratorIt.next();
            if (remove(o))
                f = true;
        }
        return f;
    }

    @Override
    public void clear() {
        root = null;
        count = 0;
    }
}
