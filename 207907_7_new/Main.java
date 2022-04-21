import java.util.*;
import java.io.*;

public class Main {
    public static Map<String, TreeNode> HeaderTable = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Map<String, List<String>> D = extractDataFrom("transactions.db");
        TreeNode root = constructFPTree(D, 2);
        printTree(root, "");
    }

    public static void printTree(TreeNode root, String psf) {
        System.out.println(root);
        for (TreeNode child : root.children) {
            printTree(child, psf + "->" + child.label);
        }
        System.out.println();
    }

    public static Map<String, List<String>> extractDataFrom(String dbname) throws IOException {
        Map<String, List<String>> map = new HashMap<>();
        File txnfile = new File(dbname);
        FileReader fr = new FileReader(txnfile);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] txn = line.split(",");
            List<String> items = new ArrayList<>();
            for (int i = 1; i < txn.length; i++) {
                items.add(txn[i]);
            }
            map.put(txn[0], items);
        }
        br.close();
        return map;
    }

    public static class TreeNode {
        String label;
        int count;
        TreeNode parent;
        TreeNode next;
        List<TreeNode> children;

        public TreeNode(String label) {
            this.label = label;
            this.count = 0;
            this.children = new ArrayList<>();
        }

        public String toString() {
            return label + " = " + count;
        }
    }

    public static class Pair {
        String item;
        int count;

        public Pair(String item, int count) {
            this.item = item;
            this.count = count;
        }
    }

    public static TreeNode constructFPTree(Map<String, List<String>> D, int minSup) {
        List<Pair> F = getFrequentItems(D, minSup);
        TreeNode root = new TreeNode("null");
        for (Map.Entry<String, List<String>> entry : D.entrySet()) {
            List<String> trans = entry.getValue();
            trans = sortTransaction(trans, F);
            insertTree(trans, root, 0);
        }
        return root;
    }

    public static List<Pair> getFrequentItems(Map<String, List<String>> D, int minSup) {
        List<Pair> set = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : D.entrySet()) {
            List<String> trans = entry.getValue();
            for (String item : trans) {
                map.put(item, map.getOrDefault(item, 0) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= minSup) {
                set.add(new Pair(entry.getKey(), entry.getValue()));
            }
        }
        Collections.sort(set, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return b.count - a.count;
            }
        });
        return set;
    }

    public static List<String> sortTransaction(List<String> trans, List<Pair> F) {
        List<String> res = new ArrayList<>();
        for (Pair item : F) {
            if (trans.contains(item.item)) {
                res.add(item.item);
            }
        }
        return res;
    }

    public static void insertTree(List<String> P, TreeNode root, int p) {
        for (TreeNode child : root.children) {
            if (child.label.equals(P.get(p))) {
                child.count++;
                insertTree(P, child, p++);
                return;
            }
        }
        TreeNode child = new TreeNode(P.get(p));
        child.count = 1;
        child.parent = root;
        root.children.add(child);
        linkNode(child);
        if (p + 1 < P.size()) {
            insertTree(P, child, p + 1);
        }
    }

    public static void linkNode(TreeNode root) {
        TreeNode next = HeaderTable.getOrDefault(root.label, null);
        if (next == null) {
            HeaderTable.put(root.label, root);
        } else {
            TreeNode curr = next;
            while (curr.next != null) {
                curr = curr.next;
            }
            curr.next = root;
        }
    }
}
