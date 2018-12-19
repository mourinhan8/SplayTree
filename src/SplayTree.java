import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.*;

public class SplayTree<T extends Comparable<T>> implements SortedSet<T> {
    private SplayNode<T> root;
    private int count = 0;

    SplayTree() {
        root = null;
    }

    SplayNode getRoot() {
        return root;
    }

    /**
     * rotate
     **/
    private void makeRightChildParent(SplayNode<T> c, SplayNode<T> p) {
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

    /**
     * rotate
     **/
    private void makeLeftChildParent(SplayNode<T> c, SplayNode<T> p) {
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

    /**
     * function splay
     **/
    private void splay(SplayNode<T> x) {
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

    private SplayNode<T> findClosest(SplayNode<T> start, T value) {
        if (start == null) return null;
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return findClosest(start.left, value);
        } else {
            if (start.right == null) return start;
            return findClosest(start.right, value);
        }
    }

    private SplayNode<T> findWithOutSplay(SplayNode<T> start, T value) {
        if (start == null) return null;
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return null;
            return findWithOutSplay(start.left, value);
        } else {
            if (start.right == null) return null;
            return findWithOutSplay(start.right, value);
        }
    }

    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SplaySubSet<>(this, toElement, fromElement, false, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SplaySubSet<>(this, toElement, null, true, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SplaySubSet<>(this, null, fromElement, false, true);
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
        SplayNode<T> node = findClosest(this.root, (T) o);
        splay(node);
        return node.value.compareTo((T) o) == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator();
    }

    private Iterator<T> iterator(T fromElement, T toElement) {
        return new SplayTreeIterator(fromElement, toElement);
    }

    @Override
    public Object[] toArray() {
        Iterator iteratorIt = new SplayTreeIterator();
        Object[] a = new Object[count];
        int i = 0;
        while (iteratorIt.hasNext()) {
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
        if (findWithOutSplay(root, t) != null) return false;
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
        splay(z);
        count++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        SplayNode node = findWithOutSplay(root, (T) o);
        if (node == null) return false;
        remove(node);
        return true;
    }

    private void remove(SplayNode node) {
        if (node == null)
            return;
        splay(node);
        if ((node.left != null) && (node.right != null)) {
            SplayNode min = node.left;
            while (min.right != null)
                min = min.right;
            min.right = node.right;
            node.right.parent = min;
            node.left.parent = null;
            root = node.left;
            splay(min);
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
        for (Object o : c) {
            if (this.contains(o)) {
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
        private Stack<SplayNode<T>> stack;
        private SplayNode<T> previous = null;
        private T lowerBound, upperBound;

        SplayTreeIterator() {
            stack = new Stack<>();
            pushAll(root);
        }

        SplayTreeIterator(T lowerBound, T upperBound) {
            stack = new Stack<>();
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            pushAllWithLimits(root);
        }

        boolean inRange(Object o) {
            T t = (T) o;
            if (lowerBound != null && upperBound != null) {
                return t.compareTo(lowerBound) >= 0 && t.compareTo(upperBound) < 0;
            } else if (lowerBound == null) {
                return t.compareTo(upperBound) < 0;
            } else return t.compareTo(lowerBound) >= 0;
        }

        private void pushAllWithLimits(SplayNode<T> node) {
            if (node != null && inRange(node.value)) {
                stack.push(node);
                if (node.right != null)
                    pushAllWithLimits(node.right);
            }
        }

        private void pushAll(SplayNode<T> node) {
            if (node != null) {
                stack.push(node);
                if (node.right != null)
                    pushAll(node.right);
            }
        }

        private SplayNode<T> findNext() {
            SplayNode<T> temp = stack.pop();
            if (temp.left != null) {
                pushAll(temp.left);
            }
            return temp;
        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public T next() {
            previous = current;
            current = findNext();
            return current.value;
        }

        @Override
        public void remove() {
            if (current.left == null) previous = null;
            splay(current);
            if ((current.left != null) && (current.right != null)) {
                previous.right = current.right;
                current.right.parent = previous;
                current.left.parent = null;
                root = current.left;
            } else if (current.right != null) {
                current.right.parent = null;
                root = current.right;
            } else if (current.left != null) {
                current.left.parent = null;
                root = current.left;
            } else {
                root = null;
            }
            current.parent = null;
            current.left = null;
            current.right = null;
            count--;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object instanceof Set) {
            Set<?> o = (SplayTree<?>) object;
            return o.size() == this.size() && this.containsAll(o) && o.containsAll(this);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime = 0;
        for (T i : this) {
            prime += i.hashCode();
        }
        return prime;
    }

    public class SplaySubSet<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {
        T upperBound;
        T lowerBound;
        boolean toLast;
        boolean fromFirst;
        SplayTree<T> tree;

        SplaySubSet(SplayTree tree, T upBoarder, T lowBoarder, boolean fromFirst, boolean toLast) {
            this.upperBound = upBoarder;
            this.lowerBound = lowBoarder;
            this.fromFirst = fromFirst;
            this.toLast = toLast;
            this.tree = tree;
        }

        boolean inRange(Object o) {
            T t = (T) o;
            if (lowerBound != null && upperBound != null) {
                return t.compareTo(lowerBound) >= 0 && t.compareTo(upperBound) < 0;
            } else if (lowerBound == null) {
                return t.compareTo(upperBound) < 0;
            } else return t.compareTo(lowerBound) >= 0;
        }

        @Override
        public boolean add(T t) {
            if (inRange(t)) {
                tree.add(t);
                return true;
            } else return false;
        }

        @Override
        public boolean remove(Object o) {
            if (inRange(o)) {
                tree.remove(o);
                return true;
            } else return false;
        }

        @Override
        public boolean contains(Object o) {
            if (inRange(o)) {
                return tree.contains(o);
            }
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return tree.iterator(lowerBound, upperBound);
        }

        @Override
        public int size() {
            int size = 0;
            for (T aTree : tree) {
                if (inRange(aTree))
                    size++;
            }
            return size;
        }

        @Nullable
        @Override
        public Comparator<? super T> comparator() {
            return null;
        }

        @NotNull
        @Override
        public SortedSet<T> subSet(T fromElement, T toElement) {
            if (fromElement.compareTo(toElement) > 0) throw new NoSuchElementException();
            return new SplaySubSet<>(tree, fromElement, toElement, false, false);
        }

        @NotNull
        @Override
        public SortedSet<T> headSet(T toElement) {
            return new SplaySubSet<>(tree, lowerBound, toElement, fromFirst, false);
        }

        @NotNull
        @Override
        public SortedSet<T> tailSet(T fromElement) {
            return new SplaySubSet<>(tree, fromElement, upperBound, false, toLast);
        }

        @Override
        public T first() {
            if (this.size() == 0) throw new NoSuchElementException();
            if (lowerBound == null) return tree.first();
            else if (toLast) return lowerBound;
            else {
                Iterator<T> it = tree.iterator();
                T temp = null;
                while (it.hasNext()) {
                    temp = it.next();
                    if (temp.compareTo(lowerBound) == 0) {
                        temp = it.next();
                        break;
                    }
                }
                return temp;
            }
        }

        @Override
        public T last() {
            if (this.size() == 0) throw new NoSuchElementException();
            if (upperBound == null) return tree.last();
            else {
                Iterator<T> it = tree.iterator();
                T temp;
                T res = null;
                while (it.hasNext()) {
                    temp = it.next();
                    if (temp.compareTo(upperBound) == 0) break;
                    res = temp;
                }
                return res;
            }
        }
    }
}