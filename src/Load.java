public class Load {
    public static void main(String[] args) {
        SplayTree tree = new SplayTree();
        for (int i = 10; i < 90; i += 10)
            tree.add(i);
        System.out.println(tree.contains(20));
        System.out.println(tree.contains(50));
        System.out.println(tree.contains(70));
    }
}
