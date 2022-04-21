// Author: Amrit Pandey, 207907, MCA Year 2
// KE Lab Assignment 6

// 1. Implement Hash Based technique to extract frequent itemsets on
// Textbook Static Transaction Database for 2-length and 3-length itemsets with
// minimum support=3
//     a. Consider hash function (x*10+y) %7 for 2 length  and (x*10+y*5+z) %7 for length 3
//     b. Consider you own hash functions.

import java.util.*;
import java.io.*;

public class Part1 {
    public static void main(String[] args) {
        int minSup = 3;
        System.out.println("Hash using hash function given in Assignment");
        System.out.println("--------------------------------------------");
        System.out.println("Frequent Itemset = " + hashedFrequentItemset("transactions.db", minSup, 1));
        System.out.println();
        System.out.println("Hash using hash function taken by self");
        System.out.println("--------------------------------------------");
        System.out.println("Frequent Itemset = " + hashedFrequentItemset("transactions.db", minSup, 0));
    }

    public static List<List<String>> hashedFrequentItemset(String dbname, int minSup, int hashVer) {
        System.out.println("Minimum Support = " + minSup);
        Map<String, ArrayList<String>> transactions = database(dbname);
        List<List<String>> fq = new ArrayList<>();

        List<List<String>> fq1 = frequent1ItemSet(transactions, minSup);
        List<List<String>> fq2 = frequent2ItemSet(fq1, minSup, hashVer);
        List<List<String>> fq3 = frequent3ItemSet(fq2, minSup, hashVer);

        fq.addAll(fq1);
        fq.addAll(fq2);
        fq.addAll(fq3);

        return fq;
    }

    public static List<List<String>> frequent1ItemSet(Map<String, ArrayList<String>> database, double minSup) {
        // step 1: generate frequent 1 itemset from database
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

    public static List<List<String>> frequent2ItemSet(List<List<String>> fq1itemset, int minSup, int hashVer) {
        // step 2: generate frequent 2 itemset from L1:
        // 2.1: create candidate hashtable for 2itemset
        // 2.2: iterate hashtable and remove those
        // entries in L2 where bucketcount < min_sup

        HashMap<Integer, List<Pair>> hashtable = new HashMap<>();
        List<List<String>> fq = new ArrayList<>();
        Map<String, ArrayList<String>> transactions = database("transactions.db");

        for (int i = 0; i < fq1itemset.size(); i++) {
            for (int j = i + 1; j < fq1itemset.size(); j++) {
                Set<String> s = new HashSet<>();
                s.addAll(fq1itemset.get(i));
                s.addAll(fq1itemset.get(j));
                List<String> f = new ArrayList<>(s);
                Collections.sort(f);
                int hash = Integer.MIN_VALUE;
                if (hashVer == 1) {
                    hash = hash2(f);
                } else if (hashVer == 0) {
                    hash = hash2_self(f);
                }
                int count = getSupportOf(f, transactions);
                if (count > 0) {
                    if (!hashtable.containsKey(hash)) {
                        hashtable.put(hash, new ArrayList<>());
                    }
                    hashtable.get(hash).add(new Pair(count, f));
                }
            }
        }

        System.out.println("Hash Bucket for 2-itemset");
        System.out.println("Hash\tBucket Count\tBucket Contents");
        // hash table for 2-itemset
        for (Map.Entry<Integer, List<Pair>> entry : hashtable.entrySet()) {
            int count = 0;
            for (Pair is : entry.getValue()) {
                count += is.count;
            }
            System.out.print(entry.getKey() + "\t" + count + "\t\t");
            for (Pair p : entry.getValue()) {
                System.out.print(p.itemset);
            }
            System.out.println();
        }

        for (Map.Entry<Integer, List<Pair>> entry : hashtable.entrySet()) {
            int count = 0;
            for (Pair is : entry.getValue()) {
                count += is.count;
            }
            if (count >= minSup) {
                for (Pair is : entry.getValue()) {
                    fq.add(is.itemset);
                }
            }
        }

        return fq;
    }

    public static int hash2(List<String> itemset) {
        // a. Consider hash function (x*10+y) %7 for 2 length and (x*10+y*5+z) %7 for
        // length 3
        int x = itemset.get(0).charAt(1);
        int y = itemset.get(1).charAt(1);
        int hash = (x * 10 + y) % 7;
        return hash;
    }

    public static int hash2_self(List<String> itemset) {
        int x = itemset.get(0).charAt(1);
        int y = itemset.get(1).charAt(1);
        int hash = (x * 10 + y) % 11;
        return hash;
    }


    public static List<List<String>> frequent3ItemSet(List<List<String>> fq2itemset, int minSup, int hashVer) {
        // step 3: generate frequent 3 itemset from L2:
        // 3.1: create candidate hashtable for 3itemset
        // 3.2: iterate hashtabel and remove those
        // entried in L3 where bucketcount < min_sup

        HashMap<Integer, List<Pair>> hashtable = new HashMap<>();
        List<List<String>> fq = new ArrayList<>();
        Map<String, ArrayList<String>> transactions = database("transactions.db");

        for (int i = 0; i < fq2itemset.size(); i++) {
            for (int j = i + 1; j < fq2itemset.size(); j++) {
                if (matching(fq2itemset.get(i), fq2itemset.get(j), 2)) {
                    Set<String> s = new HashSet<>();
                    s.addAll(fq2itemset.get(i));
                    s.addAll(fq2itemset.get(j));
                    List<String> f = new ArrayList<>(s);
                    Collections.sort(f);
                    int hash = Integer.MIN_VALUE;
                    if (hashVer == 1) {
                        hash = hash3(f);
                    } else if (hashVer == 0) {
                        hash = hash3_self(f);
                    }
                    int count = getSupportOf(f, transactions);
                    if (count > 0) {
                        if (!hashtable.containsKey(hash)) {
                            hashtable.put(hash, new ArrayList<>());
                        }
                        hashtable.get(hash).add(new Pair(count, f));
                    }
                }
            }
        }

        System.out.println("Hash Bucket for 3-itemset");
        System.out.println("Hash\tBucket Count\tBucket Contents");
        // hash table for 2-itemset
        for (Map.Entry<Integer, List<Pair>> entry : hashtable.entrySet()) {
            int count = 0;
            for (Pair is : entry.getValue()) {
                count += is.count;
            }
            System.out.print(entry.getKey() + "\t" + count + "\t\t");
            for (Pair p : entry.getValue()) {
                System.out.print(p.itemset);
            }
            System.out.println();
        }

        for (Map.Entry<Integer, List<Pair>> entry : hashtable.entrySet()) {
            int count = 0;
            for (Pair is : entry.getValue()) {
                count += is.count;
            }
            if (count >= minSup) {
                for (Pair is : entry.getValue()) {
                    fq.add(is.itemset);
                }
            }
        }

        return fq;
    }

    public static int hash3(List<String> itemset) {
        // a. Consider hash function (x*10+y) %7 for 2 length and (x*10+y*5+z) %7 for
        // length 3
        int x = itemset.get(0).charAt(1);
        int y = itemset.get(1).charAt(1);
        int z = itemset.get(2).charAt(1);
        int hash = (x * 10 + y * 5 + z) % 7;
        return hash;
    }

    public static int hash3_self(List<String> itemset) {
        int x = itemset.get(0).charAt(1);
        int y = itemset.get(1).charAt(1);
        int z = itemset.get(2).charAt(1);
        int hash = (x * 15 + y * 10 + z * 5) % 11;
        return hash;
    }

    public static class Pair {
        int count;
        List<String> itemset;

        public Pair(int count, List<String> is) {
            this.count = count;
            this.itemset = is;
        }
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
}
