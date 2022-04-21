// Author: Amrit Pandey, 207907, MCA Year 2
// KE Lab Assignment 6

// 2. Implement Partition Based Algorithm on Textbook 
// Static Transaction Database with minimum support=35%

import java.util.*;
import java.io.*;

public class Part2 {
    public static void main(String[] args) {
        double minimum_support = 0.35;
        // data from textbook in transactions.db file
        partitionFrequentItemset("transactions.db", minimum_support); 
    }

    public static List<List<String>> partitionFrequentItemset(String dbname, double minimum_support) {
        System.out.println("Minimum Support = " + minimum_support);
        Set<List<String>> G = new HashSet<>(); // Global itemset list
        Map<String, List<String>> transactions = database(dbname); // transactions database
        Map<Integer, List<List<String>>> partitions = new HashMap<>(); // partition table
        double minSup = transactions.size() * minimum_support; // minimum support count

        // STEP 1 : Partition database into size of k
        int i, k;
        i = 0; // partition size counter
        k = 3; // partition size
        for (Map.Entry<String, List<String>> txn : transactions.entrySet()) {
            int idx = i / k;
            if (!partitions.containsKey(idx)) {
                partitions.put(idx, new ArrayList<>());
            }
            partitions.get(idx).add(txn.getValue());
            i++;
        }

        // Partition output:
        System.out.println("Partitions of Transactions");
        System.out.println("--------------------------");
        for (Map.Entry<Integer, List<List<String>>> part : partitions.entrySet()) {
            System.out.println(part.getKey() + " => " + part.getValue()); 
        }

        // STEP 2 : For each partition calculate frequent itemset
        for (Map.Entry<Integer, List<List<String>>> part : partitions.entrySet()) {
            List<List<String>> itemlist = part.getValue();
            List<List<String>> fqs = getFrequentItemset(itemlist, transactions, minSup);
            // collect all frequent itemset into Global itemset list
            G.addAll(fqs);
        }

        System.out.println();
        System.out.println("Frequent Itemsets");
        System.out.println("--------------------------");
        for (List<String> is : G) {
            System.out.println(is);
        }

        return new ArrayList<>(G); 
    }

    public static List<List<String>> getFrequentItemset
    (
        List<List<String>> itemset, 
        Map<String, List<String>> transactions,
        double minSup) {

        List<List<String>> fq = new ArrayList<>(); // Local Frequent itemset

        // First find all frequent 1-itemset
        List<List<String>> L = new ArrayList<>();
        for (List<String> is : itemset) {
            for (String item : is) {
                List<String> r = new ArrayList<>();
                r.add(item);
                if (!L.contains(r) && getSupportOf(r, transactions) >= minSup) {
                    L.add(r);
                }
            }
        }
        fq.addAll(L); // add 1-itemset to Local itemset list
        int k = 1;
        while (!L.isEmpty()) {
            // candidate itemset generation
             List<List<String>> C = new ArrayList<>();
             for (int i = 0; i < L.size(); i++) {
                 for (int j = i + 1; j < L.size(); j++) {
                     // join
                     if (matching(L.get(i), L.get(j), k)) {
                        List<String> u = union(L.get(i), L.get(j));
                        if (getSupportOf(u, transactions) >= minSup) {
                            Collections.sort(u);
                            C.add(u);
                        }
                    }
                }
            }
            L = C;
            k++;
            fq.addAll(L);
        }

        return fq;
    }

    // HELPER FUNCTIONS
    public static List<String> union(List<String> l1, List<String> l2) {
        // simple union of two itemset list
        Set<String> s1 = new HashSet<>();
        s1.addAll(l1);
        s1.addAll(l2);
        return new ArrayList<String>(s1);
    }

    public static boolean matching(List<String> l1, List<String> l2, int k) {
        // the first k - 1 elements has to match
        // to perform join operations
        for (int i = 0; i < k - 1; i++) {
            if (!l1.get(i).equals(l2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, List<String>> database(String dbname) {
        Map<String, List<String>> map = new HashMap<>();
        File db;
        BufferedReader br;
        try {
            db = new File(dbname);
            br = new BufferedReader(new FileReader(db));
            String line;
            while ((line = br.readLine()) != null) {
                String[] txn = line.split(",");
                List<String> itemList = new ArrayList<>();
                for (int i = 1; i < txn.length; i++) {
                    itemList.add(txn[i]);
                }
                map.put(txn[0], itemList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static int getSupportOf(List<String> itemset, Map<String, List<String>> transactions) {
        // returns support count of itemset in transaction list
        int count = 0;
        for (List<String> txnList : transactions.values()) {
            if (txnList.containsAll(itemset)) {
                count++;
            }
        }
        return count;
    }
}
