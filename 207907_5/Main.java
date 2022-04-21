// author : Amrit Pandey, 207907, MCA SEM II, YR II
// KE Lab Assignment 6
// date: 19-02-2022

// Extract Frequent itemsets with sup>=minsup using 
// joining and pruning steps of apriori, consider
// minsup=2 take textbook transaction database(from 
// page number 250 chapter 6)

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // apriori algorithm

        // List<List<String>> list = apriori("transactions.db", 2);
        PrintStream out = new PrintStream(
        new FileOutputStream("output.txt", false), true);
        System.setOut(out);
        System.out.println("Data: Textbook data from Page 250");
        System.out.println("Minimum Support = " + 2);
        System.out.println();
        apriori("transactions.db", 2);
    }


    public static List<List<String>> apriori(String dbname, int minSup) {
        // export database (in file) to a map
        Map<String, ArrayList<String>> D = database(dbname);
        System.out.println("Input database");
        System.out.println("--------------------------------------------");
        for (Map.Entry<String, ArrayList<String>> entry : D.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println();

        // apriori algorithm
        System.out.println("Apriori Iterations");
        System.out.println("--------------------------------------------");
        // frequent 1-itemset
        List<List<String>> L = frequent1ItemSet(D, minSup);
        // storage for each L(k)
        List<List<String>> fq = new ArrayList<>();
        fq.addAll(L); // add all frequent 1-itemset
        for (int k = 1; L.size() != 0; k++) {
            System.out.println("L("+k+")\t\t\t" + L);
            // generate candidate itemset list from L
            List<List<String>> C = aprioriGen(L, k);
            // prune itemsets having support < k
            List<Integer> pruneList = new ArrayList<>();
            for (int i = 0; i < C.size(); i++) {
                // if subset of any itemset is not in
                // L(k-1) then we we'll remove it according
                // to the apriori algorithm property
                if (!hasFrequentSubset(C.get(i), L, k)) {
                    // marking index of the item set
                    // for pruning
                    pruneList.add(i);
                }
            }
            System.out.println("C("+(k+1)+")[Join]\t\t" + C);
            // prune all itemset in prunelist
            C = prune(C, pruneList);
            System.out.println("C("+(k+1)+")[Prune]\t\t" + C);
            // collect frequent itemset from candidate list
            L = getFrequentSet(C, minSup, D);
            // add frequent set to fq
            fq.addAll(L);
            System.out.println();
        }
        System.out.println("Most Frequent Item sets");
        System.out.println("--------------------------------------------");
        System.out.println(fq);
        return fq;
    }

    public static List<List<String>> aprioriGen(List<List<String>> L, int k) {
        // join
        // generate Candidate C(k) itemset list
        // from Frequent set L(k) itemset list
        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i < L.size() - 1; i++) {
            for (int j = i + 1; j < L.size(); j++) {
                if (matching(L.get(i), L.get(j), k)) {
                    List<String> c = union(L.get(i), L.get(j));
                    Collections.sort(c);
                    res.add(c);
                }
            }
        }
        return res;
    }

    public static List<List<String>> prune(List<List<String>> C, List<Integer> pruneList) {
        // prune itemsets from C
        // in the pruneList
        List<List<String>> res = new ArrayList<>();
        for (int i = 0; i < C.size(); i++) {
            if (!pruneList.contains(i)) {
                res.add(C.get(i));
            }
        }
        return res;
    }

    public static Map<String, ArrayList<String>> database(String dbname) {
        // file to map database logic
        Map<String, ArrayList<String>> map = new HashMap<>();
        File db;
        BufferedReader br;
        try {
            db = new File(dbname);
            br = new BufferedReader(new FileReader(db));
            String line;
            while ((line = br.readLine()) != null) {
                String[] txn = line.split(",");
                ArrayList<String> itemList = new ArrayList<>();
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

    public static List<List<String>> frequent1ItemSet(Map<String, ArrayList<String>> database, int minSup) {
        // generate frequent 1-itemset list
        List<String> itemset = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : database.entrySet()) {
            for (String item : entry.getValue()) {
                if (!itemset.contains(item)) {
                    itemset.add(item);
                }
            }
        }
        Collections.sort(itemset);
        List<List<String>> it = new ArrayList<>();
        for (String item : itemset) {
            List<String> ap = new ArrayList<>();
            ap.add(item);
            if (getSupportOf(ap, database) >= minSup) {
                it.add(ap);
            }
        }
        return it;
    }

    public static boolean hasFrequentSubset(List<String> itemset, List<List<String>> L, int k) {
        // Apriori property: If any subset of the itemset
        // is not in the list of itemset in previous
        // frequent itemset L(k-1) then this itemset
        // also will not be in the candidate list of
        // current itemsets i.e. C(k)
        List<List<String>> ss = subsets(itemset, k);
        for (List<String> s : ss) {
            if (!L.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public static int getSupportOf(List<String> itemset, Map<String, ArrayList<String>> transactions) {
        // returns support count of itemset in transaction list
        int count = 0;
        for (List<String> txnList : transactions.values()) {
            if (txnList.containsAll(itemset)) {
                count++;
            }
        }
        return count;
    }

    public static List<List<String>> getFrequentSet(List<List<String>> C, int minSup,
            Map<String, ArrayList<String>> D) {
                // returns list of frequent itemset
                // in the itemset list
        List<List<String>> res = new ArrayList<>();
        for (List<String> itemset : C) {
            if (getSupportOf(itemset, D) >= minSup) {
                res.add(itemset);
            }
        }
        return res;
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

    public static List<String> union(List<String> l1, List<String> l2) {
        // simple union of two itemset list
        Set<String> s1 = new HashSet<>();
        s1.addAll(l1);
        s1.addAll(l2);
        return new ArrayList<String>(s1);
    }

    public static List<List<String>> subsets(List<String> list, int k) {
        // TODO: optimize this.
        // generate subset of itemset
        int n = list.size();
        int limit = (int) Math.pow(2, n);
        List<List<String>> res = new ArrayList<>();
        List<List<String>> res2 = new ArrayList<>();

        for (int i = limit - 1; i >= 0; i--) {
            int t = i;
            List<String> lst = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                int r = t % 2;
                t /= 2;
                if (r == 1) {
                    lst.add(list.get(j));
                }
            }
            res.add(lst);
        }

        for (List<String> s : res) {
            if (s.size() == k) {
                res2.add(s);
            }
        }

        return res2;
    }
}