// author: Amrit Pandey, 207907, MCA Year 2
// Implement FP Growth Algorithm

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Map<String, List<String>> transactions = database("transactions.db");
        List<Pair> os = sortedSet(transactions, 2);
        // for (Pair o : os) {
        // System.out.println(o.item + ", " + o.itemcount);
        // }
        Map<String, List<String>> ods = orderedDataSet(transactions, os);
        // System.out.println(ods);
        ItemSetNode n = constructFPTree(ods);
        // List<String> patternBase = conditionalPatternBase(n, "i5", "");
        // System.out.println(patternBase);
        // printTree(n, "");
        Map<String, List<String>> patternBase = conditionalPatternBase(n, os);
        // System.out.println(patternBase);
        System.out.println(conditionalFPTree(patternBase));
        generateFrequentPattern(conditionalFPTree(patternBase));
    }

    public static class ItemSetNode {
        String item;
        int count;
        List<ItemSetNode> childs;

        public ItemSetNode(String item, int count) {
            this.item = item;
            this.count = count;
            this.childs = new ArrayList<>();
        }

        public String toString() {
            return this.item + "(" + this.count + ")";
        }
    }

    public static void generateFrequentPattern(Map<String, List<ItemSetNode>> fpTree) {
        for (Map.Entry<String, List<ItemSetNode>> entry : fpTree.entrySet()) {
            for (ItemSetNode root : entry.getValue()) {
                printFrequentPattern(root, entry.getKey());
            }
        }
    }

    public static void printFrequentPattern(ItemSetNode root, String item) {
        System.out.println(root.item + "," + item);
        for (ItemSetNode child : root.childs) {
            printFrequentPattern(child, item);
        }
    }

    public static Map<String, List<ItemSetNode>> conditionalFPTree(Map<String, List<String>> patternBase) {
        Map<String, List<ItemSetNode>> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : patternBase.entrySet()) {
            ItemSetNode root = new ItemSetNode("NULL", Integer.MIN_VALUE);
            for (String i : entry.getValue()) {
                String[] item = i.split(",");
                List<String> txn = new ArrayList<>();
                for (int j = 0; j < item.length - 1; j++) {
                    txn.add(item[j]);
                }
                insertNode(txn, root, Integer.parseInt(item[item.length - 1]));
            }
            List<ItemSetNode> distinctNode = new ArrayList<>();
            for (ItemSetNode ch : root.childs) {
                distinctNode.add(ch);
            }
            map.put(entry.getKey(), distinctNode);
        }
        return map;
    }

    public static void printTree(ItemSetNode root, String psf) {
        if (root.childs.size() == 0) {
            System.out.println(psf + root.item + "(" + root.count + "),");
            return;
        }

        for (ItemSetNode n : root.childs) {
            printTree(n, psf + root.item + "(" + root.count + "),");
        }
    }

    public static ItemSetNode constructFPTree(Map<String, List<String>> orderdDataSet) {
        ItemSetNode root = new ItemSetNode("NULL", Integer.MIN_VALUE);

        for (Map.Entry<String, List<String>> entry : orderdDataSet.entrySet()) {
            insertNode(entry.getValue(), root);
        }

        return root;
    }

    public static void insertNode(List<String> txn, ItemSetNode root) {
        ItemSetNode curr = root;
        for (String item : txn) {
            if (curr.childs.size() == 0) {
                ItemSetNode node = new ItemSetNode(item, 1);
                curr.childs.add(node);
                curr = node;
                continue;
            }
            ItemSetNode child = null;
            for (ItemSetNode ch : curr.childs) {
                if (ch.item.equals(item)) {
                    child = ch;
                    break;
                }
            }
            if (child != null) {
                child.count++;
                curr = child;
            } else {
                ItemSetNode node = new ItemSetNode(item, 1);
                curr.childs.add(node);
                curr = node;
            }
        }
    }

    public static void insertNode(List<String> txn, ItemSetNode root, int count) {
        ItemSetNode curr = root;
        for (String item : txn) {
            if (curr.childs.size() == 0) {
                ItemSetNode node = new ItemSetNode(item, count);
                curr.childs.add(node);
                curr = node;
                continue;
            }

            ItemSetNode child = null;
            for (ItemSetNode ch : curr.childs) {
                if (ch.item.equals(item)) {
                    child = ch;
                    break;
                }
            }

            if (child != null) {
                child.count += count;
                curr = child;
            } else {
                ItemSetNode node = new ItemSetNode(item, count);
                curr.childs.add(node);
                curr = node;
            }
        }
    }

    public static Map<String, List<String>> conditionalPatternBase(ItemSetNode root, List<Pair> sortedItemset) {
        Map<String, List<String>> map = new HashMap<>();

        Collections.sort(sortedItemset, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return a.itemcount - b.itemcount;
            }
        });

        for (Pair o : sortedItemset) {
            List<String> patternBase = conditionalPatternBaseItem(root, o.item, "");
            if (patternBase.size() > 0) {
                map.put(o.item, patternBase);
            }
        }

        return map;
    }

    public static List<String> conditionalPatternBaseItem(ItemSetNode root, String item, String psf) {
        if (!root.item.equals(item) && root.childs.size() == 0) {
            return null;
        }

        if (root.item.equals(item)) {
            psf += root.count;
            List<String> ans = new ArrayList<>();
            ans.add(psf);
            return ans;
        }

        List<String> ans = new ArrayList<>();

        for (ItemSetNode ch : root.childs) {
            String ps = psf;
            if (!root.item.equals("NULL")) {
                ps += root.item + ",";
            }
            List<String> res = conditionalPatternBaseItem(ch, item, ps);
            if (res != null && res.size() > 0) {
                for (String r : res) {
                    if (r.split(",").length > 1) {
                        ans.add(r);
                    }
                }
            }
        }

        return ans;
    }

    public static Map<String, List<String>> database(String dbname) throws IOException {
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

    public static List<Pair> sortedSet(Map<String, List<String>> transactions, int minSup) {
        List<String> _1itemset = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : transactions.entrySet()) {
            for (String item : entry.getValue()) {
                if (!_1itemset.contains(item)) {
                    _1itemset.add(item);
                }
            }
        }
        List<Pair> orderedSet = new ArrayList<>();
        for (String item : _1itemset) {
            int count = getSupportOf(item, transactions);
            if (count >= minSup) {
                orderedSet.add(new Pair(item, count));
            }
        }

        Collections.sort(orderedSet, new Comparator<Pair>() {
            public int compare(Pair a, Pair b) {
                return b.itemcount - a.itemcount;
            }
        });

        return orderedSet;
    }

    public static Map<String, List<String>> orderedDataSet(Map<String, List<String>> transactions,
            List<Pair> orderedSet) {
        Map<String, List<String>> map = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : transactions.entrySet()) {
            List<String> items = new ArrayList<>();
            for (Pair i : orderedSet) {
                if (entry.getValue().contains(i.item)) {
                    items.add(i.item);
                }
            }
            map.put(entry.getKey(), items);
        }

        return map;
    }

    public static int getSupportOf(List<String> itemset, Map<String, List<String>> transactions) {
        int count = 0;
        for (Map.Entry<String, List<String>> entry : transactions.entrySet()) {
            if (entry.getValue().containsAll(itemset)) {
                count++;
            }
        }
        return count;
    }

    public static int getSupportOf(String item, Map<String, List<String>> transactions) {
        int count = 0;
        for (Map.Entry<String, List<String>> entry : transactions.entrySet()) {
            if (entry.getValue().contains(item)) {
                count++;
            }
        }
        return count;
    }

    public static class Pair {
        String item;
        int itemcount;

        public Pair(String item, int itemcount) {
            this.item = item;
            this.itemcount = itemcount;
        }
    }
}
