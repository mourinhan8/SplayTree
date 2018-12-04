import java.util.*;

class SplayTree<T extends Comparable<T>> implements SortedSet<T> {
    private SplayNode<T> root;
    private int count = 0;

    SplayTree() {
        root = null;
    }

    private void makeRightChildParent(SplayNode<T> c, SplayNode<T> p) {
        if ((c == null) || (p == null) || (p.right != c) || (c.parent != p))
            throw new RuntimeException("WRONG");
        if (p.parent != null) {
            if (p == p.parent.left)
                p.parent.left = c;
            else
                p.parent.right = c;
        }
        if (c.left != null)
            c.left.parent = p;
        c.parent = p.parent;
        p.parent = c;
        p.right = c.left;
        c.left = p;
    }

    private void makeLeftChildParent(SplayNode<T> c, SplayNode<T> p) {
        if ((c == null) || (p == null) || (p.left != c) || (c.parent != p))
            throw new RuntimeException("WRONG");

        if (p.parent != null) {
            if (p == p.parent.left)
                p.parent.left = c;
            else
                p.parent.right = c;
        }
        if (c.right != null)
            c.right.parent = p;
        c.parent = p.parent;
        p.parent = c;
        p.left = c.right;
        c.right = p;
    }

    private void Splay(SplayNode<T> x) {
        while (x.parent != null) {
            SplayNode<T> Parent = x.parent;
            SplayNode<T> GrandParent = Parent.parent;
            if (GrandParent == null) {
                if (x == Parent.left)
                    makeLeftChildParent(x, Parent);
                else
                    makeRightChildParent(x, Parent);
            } else {
                if (x == Parent.left) {
                    if (Parent == GrandParent.left) {
                        makeLeftChildParent(Parent, GrandParent);
                        makeLeftChildParent(x, Parent);
                    } else {
                        makeLeftChildParent(x, x.parent);
                        makeRightChildParent(x, x.parent);
                    }
                } else {
                    if (Parent == GrandParent.left) {
                        makeRightChildParent(x, x.parent);
                        makeLeftChildParent(x, x.parent);
                    } else {
                        makeRightChildParent(Parent, GrandParent);
                        makeRightChildParent(x, Parent);
                    }
                }
            }
        }
        root = x;
    }

    @Override
    public Comparator<? super T> comparator() {
        return null;
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
        SplayNode<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        SplayNode<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        return findNode((T) o) != null;
    }

    private SplayNode<T> findNode(T element) {
        SplayNode<T> prevNode = null;
        SplayNode<T> z = root;
        while (z != null) {
            prevNode = z;
            if (element.compareTo(z.value) > 0)
                z = z.right;
            else if (element.compareTo(z.value) < 0)
                z = z.left;
            else {
                Splay(z);
                return z;
            }
        }
        if (prevNode != null) {
            Splay(prevNode);
            return null;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

    @Override
    public Object[] toArray() {
        Iterator iteratorIt = new SplayTreeIterator();
        Object[] a = new Object[count];
        int i = 0;
        while (iteratorIt .hasNext()) {
            a[i] = iteratorIt.next();
            i++;
        }
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] elementData = toArray();
        if (a.length < count) {
            return (T[]) Arrays.copyOf(elementData, count, a.getClass());
        }
        System.arraycopy(elementData, 0, a, 0, count);
        if (a.length > count) {
            a[count] = null;
        }
        return a;
    }

    @Override
    public boolean add(T t) {
        SplayNode<T> z = root;
        SplayNode<T> p = null;
        while (z != null) {
            p = z;
            if (t.compareTo(p.value) > 0)
                z = z.right;
            else
                z = z.left;
        }
        z = new SplayNode<>();
        z.value = t;
        z.parent = p;
        if (p == null)
            root = z;
        else if (t.compareTo(p.value) > 0)
            p.right = z;
        else
            p.left = z;
        Splay(z);
        count++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        SplayNode node = findNode((T) o);
        if (node == null) return false;
        remove(node);
        return true;
    }

    private void remove(SplayNode node) {
        if (node == null)
            return;
        Splay(node);
        if ((node.left != null) && (node.right != null)) {
            SplayNode min = node.left;
            while (min.right != null)
                min = min.right;

            min.right = node.right;
            node.right.parent = min;
            node.left.parent = null;
            root = node.left;
        } else if (node.right != null) {
            node.right.parent = null;
            root = node.right;
        } else if (node.left != null) {
            node.left.parent = null;
            root = node.left;
        } else {
            root = null;
        }
        node.parent = null;
        node.left = null;
        node.right = null;
        count--;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean check = false;
        for (T value : c) {
            if (this.add(value)) check = true;
        }
        return check;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean check = false;
        Iterator<T> iteratorIt = this.iterator();
        while (iteratorIt.hasNext()) {
            if (!c.contains(iteratorIt.next())) {
                iteratorIt.remove();
                check = true;
            }
        }
        return check;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean check = false;
        for (Object o : c){
            if (this.contains(o)){
                this.remove(o);
                check = true;
            }
        }
        return check;
    }

    @Override
    public void clear() {
        root = null;
        count = 0;
    }

    public class SplayTreeIterator implements Iterator<T> {

        private SplayNode<T> current = null;
        private int location;
        private List<SplayNode<T>> list;

        SplayTreeIterator() {
            list = new ArrayList<>();
            if (root != null)
                addToList(root);
            location = 0;
        }

        private void addToList(SplayNode<T> node) {
            if (node.right != null)
                addToList(node.right);
            list.add(node);
            if (node.left != null)
                addToList(node.left);
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
}
abstract class SplaySubSet<T extends Comparable<T>> implements SortedSet<T> {}