// amrit pandey, MCA 2nd year, 207907
// ke mid lab exam
import java.util.*;
import java.io.*;

public class _207907_KE_MID {
    public static void main(String[] args) throws IOException {
        question1();
        System.out.println("\n");
        question2();
    }

    public static void question1() {
        // (i) Normalize the data, to make the norm of each data point equal to 1

        // I am using min-max normalization
        // Min max normalization formula:
        // zi = [(x - min) / (max - min)] * (new_max - new_min) + new_min;
        // here x = value, min = minimum in the set, max = maximum in the set
        // new_max = 1
        // new_min = 0
        // hence the formula effectively becomes
        // zi = [(x - min) / (max - min)];
        double[] a1 = { 1.4, 1.5, 0.8, -0.7, 2.5, 3.21 };
        double[] a2 = { 1.6, -0.7, -0.6, -1.7, 2.8, 1.56 };

        List<Double> A1 = normalize(a1);
        List<Double> A2 = normalize(a2);

        System.out.println("ORIGINAL");
        System.out.println("a1\t\ta2");
        for (int i = 0; i < a1.length; i++) {
            System.out.println(a1[i] + "\t\t" + a2[i]);
        }

        System.out.println();
        System.out.println("NORMALIZED");
        System.out.println("a1\t\ta2");
        for (int i = 0; i < A1.size(); i++) {
            System.out.println(change(A1.get(i), 2) + "\t\t" + change(A2.get(i), 2));
        }

        // (ii) Assume if the attributes are from independent sources, use the
        // data-integration method, and decide whether to include both attributes in
        // integrated data?

        // Ans = Here we will use Pearson’s product moment as the data are numeric data
        // to determine if they are correlated.

        double moment = 0;
        double meana1 = mean(A1);
        double meana2 = mean(A2);
        double sda1 = sd(A1);
        double sda2 = sd(A2);
        for (int i = 0; i < a1.length; i++) {
            moment += (A1.get(i) - meana1) * (A2.get(i) - meana2);
        }
        moment /= sda1 * sda2;
        if (moment > 0) {
            System.out.println("A1 and A2 are positively correlated");
        } else {
            System.out.println("A1 and A2 are negatively correlated");
        }

        /* output
        A1 and A2 are positively correlated,
        hence we will not add both the attribute in 
        the database
        */
    }

    public static List<Double> normalize(double[] set) {
        List<Double> normalizedset = new ArrayList<Double>();
        // zi = [(x - min) / (max - min)];
        Pair maxmin = findMinMaxValues(set);
        double max = maxmin.max;
        double min = maxmin.min;

        for (double value : set) {
            double z = (value - min) / (max - min);
            normalizedset.add(z);
        }

        return normalizedset;
    }

    public static class Pair {
        double min;
        double max;

        public Pair() {
            this.min = Integer.MAX_VALUE;
            this.max = Integer.MIN_VALUE;
        }
    }

    public static Pair findMinMaxValues(double[] values) {
        // finding minimum and maximum values in a list
        Pair res = new Pair();

        for (double n : values) {
            if (n < res.min) {
                res.min = n;
            }
            if (n > res.max) {
                res.max = n;
            }
        }
        return res;
    }

    static double change(double value, int decimalpoint) {
        // logic for trimming decimal values
        // upto decimalPoint places
        value = value * Math.pow(10, decimalpoint);
        value = Math.floor(value);
        value = value / Math.pow(10, decimalpoint);
        return value;
    }

    static double mean(List<Double> values) {
        // logic for calculating mean of data
        double sum = 0.0;
        for (double num : values) {
            sum += num;
        }
        double average = sum / values.size();
        return average;
    }

    public static double sd(List<Double> values) {
        // logic for calculating standard deviation
        // of data
        double sd = 0.0;
        double mean = mean(values);
        for (double num : values) {
            sd += Math.pow(num - mean, 2);
        }
        return Math.sqrt(sd / values.size());
    }

    public static void question2() {
        // apriori algorithm

        // List<List<String>> list = apriori("transactions.db", 2);
        // PrintStream out = new PrintStream(
        // new FileOutputStream("output1.txt", false), true);
        // System.setOut(out);
        System.out.println("Minimum Support = " + 0.5);
        System.out.println();
        Map<String, ArrayList<String>> transactions = database("transactions.db");
        double minSup = 0.5 * transactions.size();

        // To solve the first three questions we'll use the apriori algorithm
        // to generate L(k) frequent itemsets. The apriori algorithm will run
        // as follows:
        // 1. Generate frequent 1-itemset from transaction
        // 2. From this we'll first generate C(2) candidate itemset
        // by joining technique of apriori algorithm.
        // joining algorithm is as follows:
        // - Use the L(1) set to generate C(2)
        // - join two itemset in L(1) only if
        // starting 2-1 elements are same
        // 3. After this, we'll use prune technique to
        // remove all the itemset in C(2) such that any item
        // in the itemset has not occured in L(1)
        // 4. After pruning the C(2) becomes L(2)
        // and C(2+1) is further generated by using L(2)
        // We'll generate k itemsets for L(k) using the
        // prior knowledge of L(k-1) until C(k) == empty
        // after pruning.

        // a. (Kidney Beans, Eggs, Onion) is the largest frequent itemset.
        // Ans = True
        // Justification: The above set occurs in the L(3) of the apriori
        // iterations and is the only itemset in it (as shown in output)

        // b. There are exactly 9 frequent itemsets.
        // Ans = True
        // Justification: The total number of itemsets in L1, L2 and L3
        // sum total to 9 as shown below in the output

        // c. There are exactly 10 frequent itemsets.
        // Ans = False

        apriori("transactions.db", minSup);

        /*
         * OUTPUT
         * Minimum Support = 0.5
         * 
         * Input database
         * --------------------------------------------
         * T4 = [Milk, Unicorn, Corn, Yogurt]
         * T5 = [Corn, Onion, Kidney Beans, Ice cream, Eggs]
         * T1 = [Milk, Onion, Nutmeg, Kidney Beans, Eggs, Yogurt]
         * T2 = [Dill, Onion, Nutmeg, Kidney Beans, Eggs, Yogurt]
         * T3 = [Milk, Apple, Kidney Beans, Eggs]
         * 
         * Apriori Iterations
         * --------------------------------------------
         * L(1) [[Eggs], [Kidney Beans], [Milk], [Onion], [Yogurt]]
         * C(2)[Join] [[Eggs, Kidney Beans], [Eggs, Milk], [Eggs, Onion], [Eggs,
         * Yogurt], [Kidney Beans, Milk], [Kidney Beans, Onion], [Kidney Beans, Yogurt],
         * [Milk, Onion], [Milk, Yogurt], [Onion, Yogurt]]
         * C(2)[Prune] [[Eggs, Kidney Beans], [Eggs, Milk], [Eggs, Onion], [Eggs,
         * Yogurt], [Kidney Beans, Milk], [Kidney Beans, Onion], [Kidney Beans, Yogurt],
         * [Milk, Onion], [Milk, Yogurt], [Onion, Yogurt]]
         * 
         * L(2) [[Eggs, Kidney Beans], [Eggs, Onion], [Kidney Beans, Onion]]
         * C(3)[Join] [[Eggs, Kidney Beans, Onion]]
         * C(3)[Prune] [[Eggs, Kidney Beans, Onion]]
         * 
         * L(3) [[Eggs, Kidney Beans, Onion]]
         * C(4)[Join] []
         * C(4)[Prune] []
         * 
         * Most Frequent Item sets
         * --------------------------------------------
         * [[Eggs], [Kidney Beans], [Milk], [Onion], [Yogurt], [Eggs, Kidney Beans],
         * [Eggs, Onion], [Kidney Beans, Onion], [Eggs, Kidney Beans, Onion]]
         * 
         * Total Itemsets in Frequent Itemset = 9
         */

        // d. (Kidney Beans, Eggs) is the most frequent itemset of size 2.
        // Ans = True
        // Justification: [[Eggs, Kidney Beans], [Eggs, Onion], [Kidney Beans, Onion]]
        // are the frequent
        // 2-itemset hence we'll find support of each itemset to determine if
        // [Kidney Beans, Eggs) is the most frequent among the three.

        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        List<String> l3 = new ArrayList<>();

        l1.add("Kidney Beans");
        l1.add("Eggs");

        l2.add("Eggs");
        l2.add("Onion");

        l3.add("Kidney Beans");
        l3.add("Onion");

        System.out.println("Support of [Kidney Beans, Eggs] = " + getSupportPercentOf(l1, transactions));
        System.out.println("Support of [Eggs, Onions] = " + getSupportPercentOf(l2, transactions));
        System.out.println("Support of [Kidney Beans, Onions] = " + getSupportPercentOf(l3, transactions));
        /*
         * OUTPUT
         * Support of [Kidney Beans, Eggs] = 0.8
         * Support of [Eggs, Onions] = 0.6
         * Support of [Kidney Beans, Onions] = 0.6
         */

        // e. No frequent itemset has support 0.8
        // Ans = False
        // Justification: As we have observed in question (d) the
        // support of [Kidney Beans, Eggs] = 0.8 hence false.

        // f. (Eggs, Yogurt) has support 0.5.
        // Ans = False
        // Justification: This itemset cannot have support of 0.5
        // as it has not occured in L(2) set of frequent itemset.
    }

    public static List<List<String>> apriori(String dbname, double minSup) {
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
            System.out.println("L(" + k + ")\t\t\t" + L);
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
            System.out.println("C(" + (k + 1) + ")[Join]\t\t" + C);
            // prune all itemset in prunelist
            C = prune(C, pruneList);
            System.out.println("C(" + (k + 1) + ")[Prune]\t\t" + C);
            // collect frequent itemset from candidate list
            L = getFrequentSet(C, minSup, D);
            // add frequent set to fq
            fq.addAll(L);
            System.out.println();
        }
        System.out.println("Most Frequent Item sets");
        System.out.println("--------------------------------------------");
        System.out.println(fq);
        System.out.println("Total Itemsets in Frequent Itemset = " + fq.size());
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

    public static List<List<String>> frequent1ItemSet(Map<String, ArrayList<String>> database, double minSup) {
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

    public static double getSupportPercentOf(List<String> itemset, Map<String, ArrayList<String>> transactions) {
        int supportCount = getSupportOf(itemset, transactions);
        double pc = supportCount;
        pc /= transactions.size();
        return pc;
    }

    public static List<List<String>> getFrequentSet(List<List<String>> C, double minSup,
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