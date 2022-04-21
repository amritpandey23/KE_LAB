import java.util.*;

public class Run {
    public static class ItemSetNode {
        String item;
        int count;
        List<ItemSetNode> children;

        public ItemSetNode(String item, int count) {
            this.item = item;
            this.count = count;
            this.children = new ArrayList<>();
        }

        public String toString() {
            return this.item + " (" + this.count + ") ";
        }

        public void printTree(ItemSetNode root) {
            if (root.children.size() == 0) {
                System.out.print(root);
                System.out.println();
                return;
            }

            System.out.print(root + " ");

            for (ItemSetNode child : root.children) {
                printTree(child);
            }
        }

        public void printTree() {
            printTree(this);
        }

        public void insert(ItemSetNode root, List<String> itemset) {
            
        }
    }

}
