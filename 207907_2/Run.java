
/*  KE Lab MCA II Year, Semester 4
    author : Amrit Pandey, MCA, 207907
    date: 21-01-2022
    Assignment 2 : KE Lab
*/
import java.util.*;
import java.io.*;

public class Run {
    private static Scanner sc = new Scanner(System.in);
    private static int limit = 15;
    private static int minSupportSample = 2; // minimum support count for assignment 1 database
    private static double minSupportRandom = 0.3; // minimum support count for random database
    private static boolean db_init = false;

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        // run wizard
        int n = 0;
        while (true) {
            System.out.println("--------------------------------------------------");
            if (!db_init) {
                System.out.println("\tFor first time run, please initialize databases.");
            }
            System.out.println("\t0. Exit");
            System.out.println("\t1. Initialize databases (assignment_1 & assignment_2)");
            System.out.println("\t2. Read database");
            System.out.println("\t3. Print support of all items");
            System.out.println("\t4. Extract items");
            System.out.println("\t5. Print frequent itemsets");
            System.out.println("--------------------------------------------------");
            System.out.print("Your choice : ");
            n = sc.nextInt();
            switch (n) {
                case 0:
                    System.exit(0);
                case 1: {
                    // initialize
                    generateSampleDatabase();
                    generateRandomDatabase();
                    db_init = true;
                    System.out.println("Datbases created successfully!");
                    break;
                }
                case 2: {
                    System.out.println("READ DATABASE");
                    System.out.println("------------------------------------------");
                    System.out.println("\t1. Assignment 1 database");
                    System.out.println("\t2. Assignment 2 database");
                    System.out.println("------------------------------------------");
                    System.out.print("Your choice : ");
                    int x = sc.nextInt();
                    if (x == 1) {
                        // read assignment 1
                        printDatabaseToConsole("assignment1_db");
                    } else if (x == 2) {
                        // read assignment 2
                        printDatabaseToConsole("assignment2_db");
                    } else {
                        System.out.println("Invalid response.");
                    }
                    break;
                }
                case 3: {
                    System.out.println("PRINT SUPPORT OF ALL ITEMS");
                    System.out.println("------------------------------------------");
                    System.out.println("\t1. Assignment 1 database");
                    System.out.println("\t2. Assignment 2 database");
                    System.out.println("------------------------------------------");
                    System.out.print("Your choice : ");
                    int x = sc.nextInt();
                    if (x == 1) {
                        // support of elements in assignment 1
                        printSupportOfAllItems("assignment1_db");
                    } else if (x == 2) {
                        // support of elements in assignment 2
                        printSupportOfAllItems("assignment2_db");
                    } else {
                        System.out.println("Invalid response.");
                    }
                    break;
                }
                case 4: {
                    System.out.println("EXTRACT ITEMS");
                    System.out.println("------------------------------------------");
                    System.out.println("\t1. Assignment 1 database");
                    System.out.println("\t2. Assignment 2 database");
                    System.out.println("------------------------------------------");
                    System.out.print("Your choice : ");
                    int x = sc.nextInt();
                    if (x == 1) {
                        // support of elements in assignment 1
                        printExtractItems("assignment1_db");
                    } else if (x == 2) {
                        // support of elements in assignment 2
                        printExtractItems("assignment2_db");
                    } else {
                        System.out.println("Invalid response.");
                    }
                    break;
                }
                case 5: {
                    System.out.println("PRINT FREQUENT ITEMSETS");
                    System.out.println("------------------------------------------");
                    System.out.println("\t1. Assignment 1 database");
                    System.out.println("\t2. Assignment 2 database");
                    System.out.println("------------------------------------------");
                    System.out.print("Your choice : ");
                    int x = sc.nextInt();
                    if (x == 1) {
                        // support of elements in assignment 1
                        printFrequentSet("assignment1_db", minSupportSample);
                    } else if (x == 2) {
                        // support of elements in assignment 2
                        printFrequentSet("assignment2_db", minSupportRandom);
                    } else {
                        System.out.println("Invalid response.");
                    }
                    break;
                }
                default: {
                    System.out.println("Invalid option, try again.");
                    break;
                }
            }
        }
    }

    // 1. create sample database
    // T1 a b c e
    // T2 b d f
    // T3 a c d f
    // T4 d f
    // T5 c d e
    public static void generateSampleDatabase() {
        ArrayList<String[]> txn = new ArrayList<>();
        txn.add(new String[] { "T1", "a", "b", "c", "e" });
        txn.add(new String[] { "T2", "b", "d", "f" });
        txn.add(new String[] { "T3", "a", "c", "d", "f" });
        txn.add(new String[] { "T4", "d", "f" });
        txn.add(new String[] { "T5", "c", "d", "e" });
        PrintWriter pw = null;
        File outFile = null;
        try {
            outFile = new File("assignment1_db");
            if (!outFile.exists()) {
                outFile.createNewFile(); // create file if not exist
            }
            pw = new PrintWriter(new FileOutputStream(outFile));
            for (String[] xt : txn) {
                // write to file each transaction
                StringBuilder xtEntry = new StringBuilder();
                for (int i = 0; i < xt.length; i++) {
                    if (i == xt.length - 1) {
                        xtEntry.append(xt[i] + "\n");
                    } else {
                        xtEntry.append(xt[i] + ",");
                    }
                }
                pw.write(xtEntry.toString());
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // 2. create random database - limit = 1-15
    public static void generateRandomDatabase() {
        char[] items = new char[limit];
        for (char i = 'a'; i < 'a' + limit; i++) {
            items[i - 'a'] = i;
        }
        PrintWriter pw;
        File db;

        try {
            db = new File("assignment2_db");
            if (!db.exists()) {
                db.createNewFile();
            }
            pw = new PrintWriter(new FileOutputStream(db));
            int totalTransactions = random();
            StringBuilder line;
            for (int i = 1; i <= totalTransactions; i++) {
                line = new StringBuilder();
                line.append("T" + i + ",");
                int totalItems = random();
                for (int j = 0; j < totalItems; j++) {
                    line.append(items[j] + ",");
                }
                line.deleteCharAt(line.length() - 1);
                pw.println(line.toString());
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 3. read transaction from databases
    public static Map<String, ArrayList<String>> transactions(String dbname) {
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

    // printing transactions from file database to console
    public static void printDatabaseToConsole(String dbname) {
        Map<String, ArrayList<String>> db = transactions(dbname);
        Iterator<Map.Entry<String, ArrayList<String>>> it = db.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = it.next();
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    // 5. extract items
    public static Set<String> getUniqueItems(Map<String, ArrayList<String>> transactions) {
        Set<String> uniqueItemList = new HashSet<>();
        for (ArrayList<String> list : transactions.values()) {
            for (String item : list) {
                uniqueItemList.add(item);
            }
        }
        return uniqueItemList;
    }

    // 4. support count of an item in database
    public static int getSupportOf(String item, Map<String, ArrayList<String>> transactions) {
        int count = 0;
        for (ArrayList<String> txnList : transactions.values()) {
            if (txnList.contains(item)) {
                count++;
            }
        }
        return count;
    }

    // 4. support of itemset in a database
    public static int getSupportOf(ArrayList<String> itemset, Map<String, ArrayList<String>> transactions) {
        int count = 0;
        for (ArrayList<String> txnList : transactions.values()) {
            if (txnList.containsAll(itemset)) {
                count++;
            }
        }
        return count;
    }

    // print support of all items in a database
    public static void printSupportOfAllItems(String dbname) {
        Map<String, ArrayList<String>> db = transactions(dbname);
        Set<String> itemList = getUniqueItems(db);
        for (String item : itemList) {
            System.out.println(item + " : " + getSupportOf(item, db));
        }
    }

    // 6. find frequent itemsets (assignment2 database)
    public static void printFrequentSet(String dbname, double minSupport) {
        System.out.println("Minimum Support = (%)" + minSupport);
        Map<String, ArrayList<String>> db = transactions(dbname);
        ArrayList<ArrayList<String>> possibleItemsSets = generateItemSet();
        System.out.println("ITEMSET\t\t\t\tSUPPORT");
        int totalTransactions = db.keySet().size();
        for (ArrayList<String> set : possibleItemsSets) {
            double support = (double) getSupportOf(set, db) / totalTransactions;
            if (support >= minSupport) {
                System.out.println(set + "\t\t\t\t\t" + support);
            }
        }
    }

    // print frequent itemset (assignment 1 database)
    public static void printFrequentSet(String dbname, int minSupport) {
        System.out.println("Minimum Support = " + minSupport);
        Map<String, ArrayList<String>> db = transactions(dbname);
        ArrayList<ArrayList<String>> possibleItemsSets = generateItemSet();
        System.out.println("ITEMSET\t\t\tSUPPORT");
        for (ArrayList<String> set : possibleItemsSets) {
            int support = getSupportOf(set, db);
            if (support >= minSupport) {
                System.out.println(set + "\t\t\t" + support);
            }
        }
    }

    // print extracted items
    public static void printExtractItems(String dbname) {
        Map<String, ArrayList<String>> db = transactions(dbname);
        Set<String> itemList = getUniqueItems(db);
        for (String item : itemList) {
            System.out.print(item + " : ");
            for (Map.Entry<String, ArrayList<String>> entry : db.entrySet()) {
                if (entry.getValue().contains(item)) {
                    System.out.print(entry.getKey() + " ");
                }
            }
            System.out.println();
        }
    }

    // generate all possible item sets
    public static ArrayList<ArrayList<String>> generateItemSet() {
        char[] items = new char[limit];
        for (char i = 'a'; i < 'a' + limit; i++) {
            items[i - 'a'] = i;
        }
        ArrayList<ArrayList<String>> itemSets = new ArrayList<>();
        int limit = (int) Math.pow(2, items.length);
        for (int i = 1; i < limit; i++) {
            int t = i;
            ArrayList<String> set = new ArrayList<>();
            for (int j = items.length - 1; j >= 0; j--) {
                int r = t % 2;
                t /= 2;
                if (r == 1) {
                    set.add(Character.toString(items[j]));
                }
            }
            itemSets.add(set);
        }
        return itemSets;
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static int random() {
        return random(1, limit);
    }
}