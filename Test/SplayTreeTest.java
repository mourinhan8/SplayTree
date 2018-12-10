import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SplayTreeTest {
    SplayTree<Integer> tree = new SplayTree<>();
    SplayTree<Integer> temp = new SplayTree<>();

    void add() {
        tree.add(10);
        tree.add(20);
        tree.add(30);
        tree.add(40);
        tree.add(50);
        tree.add(60);
        tree.add(70);
        tree.add(80);
        tree.add(90);
    }

    @Test
    void size() {
        add();
        System.out.println("Size test");
        assertEquals(9, tree.size());
        tree.remove(30);
        assertEquals(8, tree.size());
    }

    @Test
    void isEmpty() {
        System.out.println("IsEmpty test");
        assertTrue(tree.isEmpty());
        add();
        tree.remove(10);
        tree.remove(40);
        assertFalse(tree.isEmpty());
    }

    @Test
    void contains() {
        add();
        System.out.println("Contains test");
        assertTrue(tree.contains(20));
        assertFalse(tree.contains(55));
        assertTrue(tree.contains(80));
        assertTrue(tree.contains(30));
        assertTrue(tree.contains(40));
        tree.remove(30);
        assertFalse(tree.contains(30));
    }

    @Test
    void subSet() {
        add();
        System.out.println("Subset test");
        SortedSet<Integer> set = tree.subSet(10, 50);
        assertTrue(set.contains(10));
        assertTrue(set.contains(20));
        assertTrue(set.contains(30));
        assertTrue(set.contains(40));
        assertFalse(set.contains(50));
    }

    @Test
    void headSet() {
        add();
        System.out.println("headSet test");
        SortedSet<Integer> set = tree.headSet(40);
        assertTrue(set.contains(30));
        assertTrue(set.contains(10));
        assertTrue(set.contains(20));
        assertFalse(set.contains(40));
    }

    @Test
    void tailSet() {
        add();
        System.out.println("TailSet test");
        SortedSet<Integer> set = tree.tailSet(50);
        assertTrue(set.contains(50));
        assertTrue(set.contains(60));
        assertTrue(set.contains(70));
        assertTrue(set.contains(80));
        assertTrue(set.contains(90));
    }

    @Test
    void iterator() {
        add();
        Iterator<Integer> it = tree.iterator();
        for (int i = 90; i > 9; i -= 10) {
            assertEquals(i, (int) it.next());
        }
        Iterator<Integer> it2 = tree.iterator();
        while (it2.hasNext()) {
            it2.next();
            it2.remove();
        }
        assertEquals(0, tree.size());
    }

    @Test
    void toArray() {
        SortedSet<String> tree = new SplayTree<>();
        tree.add("cam");
        tree.add("on");
        tree.add("thay");
        tree.add("nhieu");
        tree.add("lam");
        add();
        System.out.println("toArray() test");
        String[] str = new String[5];
        str[0] = "thay";
        str[1] = "on";
        str[2] = "nhieu";
        str[3] = "lam";
        str[4] = "cam";
        assertArrayEquals(tree.toArray(), str);
    }

    @Test
    void toArray1() {
        add();
        System.out.println("ToArray(a) test");
        Integer a[] = new Integer[]{25, 40};
        Integer exp[] = new Integer[]{90, 80, 70, 60, 50, 40, 30, 20, 10};
        assertArrayEquals(tree.toArray(a), exp);
    }

    @Test
    void containsAll() {
        add();
        System.out.println("ContainsAll test");
        temp.add(20);
        temp.add(40);
        temp.add(60);
        temp.add(90);
        assertTrue(tree.containsAll(temp));
        temp.add(55);
        assertFalse(tree.containsAll(temp));
    }

    @Test
    void addAll() {
        add();
        System.out.println("AddAll test");
        Set<Integer> set = new HashSet<>();
        set.add(15);
        set.add(52);
        set.add(36);
        assertTrue(tree.addAll(set));
        temp.add(15);
        temp.add(52);
        temp.add(36);
        assertTrue(tree.containsAll(temp));

    }

    @Test
    void retainAll() {
        add();
        System.out.println("RetainAll test");
        temp.add(20);
        temp.add(40);
        temp.add(60);
        temp.add(90);
        tree.retainAll(temp);
        assertTrue(tree.contains(20));
        assertTrue(tree.contains(40));
        assertTrue(tree.contains(60));
        assertTrue(tree.contains(90));
        assertFalse(tree.contains(10));
        assertFalse(tree.contains(50));
        assertFalse(tree.contains(30));
        assertFalse(tree.contains(70));
    }

    @Test
    void removeAll() {
        add();
        System.out.println("removeAll test");
        temp.add(30);
        temp.add(40);
        temp.add(50);
        temp.add(60);
        tree.removeAll(temp);
        Integer exp[] = new Integer[]{90, 80, 70, 20, 10};
        assertArrayEquals(exp, tree.toArray());
    }

    @Test
    void clear() {
        add();
        tree.clear();
        SplayTree newTree = new SplayTree();
        assertArrayEquals(newTree.toArray(), tree.toArray());
    }
}
