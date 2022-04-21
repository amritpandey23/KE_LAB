import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Node tree = fpTree("transactions.db");
        printTree(tree, "");
    }

    public static Node fpTree(String dbname) {
        Node root = new Node("NULL", null);

        Map<String, List<String>> db = getDataFrom(dbname);
        List<Pair> l1 = L1(dbname, 2);

        for (Map.Entry<String, List<String>> entry : db.entrySet()) {
            List<String> sortedList = getSortedList(entry.getValue(), l1);
            insertTree(sortedList, 0, root);
        }

        return root;
    }

    public static void printTree(Node root, String psf) {
        if (root.children.size() == 0) {
            System.out.println(psf + root.item + "(" + root.count + "),");
            return;
        }

        for (Node n : root.children) {
            printTree(n, psf + root.item + "(" + root.count + "),");
        }
    }

    public static class Pair {
        String item;
        int count;

        public Pair(String item, int count) {
            this.item = item;
            this.count = count;
        }

        @Override
        public String toString() {
            return this.item + ":" + this.count;
        }
    }

    public static class Node {
        String item;
        int count;
        Node parent;
        Node next;
        List<Node> children;

        public Node (String item, Node parent) {
            this.item = item;
            this.count = 1;
            this.parent = parent;
            this.children = new ArrayList<>();
        }
    }

    public static void insertTree(List<String> itemset, int i, Node root) {

        if (i == itemset.size()) {
            return;
        }
        
        boolean childFound = false;
        for (Node child : root.children) {
            if (child.item.equals(itemset.get(i))) {
                child.count++;
                childFound = true;
                insertTree(itemset, i + 1, child);
            }
        }

        if (!childFound) {
            Node newChild = new Node(itemset.get(i), root);
            root.children.add(newChild);
            insertTree(itemset, i + 1, newChild);
        }

    }

    public static List<String> getSortedList(List<String> items, List<Pair> sortedOrderSet) {
        List<String> lst = new ArrayList<>();

        for (Pair p : sortedOrderSet) {
            if (items.contains(p.item)) {
                lst.add(p.item);
            }
        }

        return lst;
    }

    public static Map<String, List<String>> getDataFrom(String dbname) {
        Map<String, List<String>> database = new HashMap<>();
        FileReader fr = null;
        BufferedReader br = null;
        File f = null;
        try {
            f = new File(dbname);
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split(",");
                List<String> items = new ArrayList<>();
                for (int i = 1; i < vals.length; i++) {
                    items.add(vals[i]);
                }
                database.put(vals[0], items);
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;
    }

    public static List<Pair> L1(String dbname, int minSup) {
        List<Pair> l1 = new ArrayList<>();
        Map<String, List<String>> db = getDataFrom("transactions.db");
        List<String> allItems = getAllUniqueItems(dbname);

        for (String item : allItems) {
            int supportCount = getSupportOf(item, db);
            if (supportCount >= minSup) {
                l1.add(new Pair(item, supportCount));
            }
        }

        Collections.sort(l1, new Comparator<Pair> () {
            public int compare(Pair a, Pair b) {
                return b.count - a.count;
            }
        });

        return l1;
    }

    public static int getSupportOf(String item, Map<String, List<String>> database) {
        int count = 0;
        for (Map.Entry<String, List<String>> entry : database.entrySet()) {
            if (entry.getValue().contains(item)) {
                count++;
            }
        }
        return count;
    }

    public static List<String> getAllUniqueItems(String dbname) {
        List<String> items = new ArrayList<>();

        Map<String, List<String>> db = getDataFrom("transactions.db");

        for (Map.Entry<String, List<String>> entry : db.entrySet()) {
            for (String item : entry.getValue()) {
                if (!items.contains(item)) {
                    items.add(item);
                }
            }
        }

        return items;
    }
}
