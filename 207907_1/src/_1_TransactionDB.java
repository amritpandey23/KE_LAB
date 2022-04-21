package src;
/*  KE Lab MCA II Year, Semester 4 
    author : Amrit Pandey, MCA, 207907
    date: 07-01-2022
    problem: Creation of Transaction Database in a file using file operations
            Ex: T1 a b c e
            T2 b d f
            T3 a c d f
            T4 d f
            T5 c d e
*/

import java.util.*;
import java.io.*;

// Transaction DB class for grouping all db
// related functions and methods
public class _1_TransactionDB {
    private static Scanner sc = new Scanner(System.in);
    public static String dbName = "output.txt"; // output file name

    public static void create(boolean withSampleData) {
        // create database
        // if withSampleData is set to true then
        // dummy data preprogrammed will be used
        // dummy data : see line 39
        File outFile = new File(dbName);
        if (outFile.exists()) {
            // if output file already exist,
            // user will be prompted to overwrite
            // exisiting output file
            System.out.print("DB(output.txt) file already exist.\nDo you still want to overwrite? (y/n):");
            String res = sc.nextLine().split("")[0];
            if (!(res.equals("y") || res.equals("Y"))) {
                return;
            }
        }

        // generation of dummy data
        if (withSampleData) {
            ArrayList<String[]> sampleData = new ArrayList<>();
            sampleData.add(new String[] { "T1", "a", "b", "c", "e" });
            sampleData.add(new String[] { "T2", "b", "d", "f" });
            sampleData.add(new String[] { "T3", "a", "c", "d", "f" });
            sampleData.add(new String[] { "T4", "d", "f" });
            sampleData.add(new String[] { "T5", "c", "d", "e" });
            write(sampleData);
            return;
        }

        // if user opted to enter data manually
        // it is done with input() method
        ArrayList<String[]> txn = input();

        // finally write data to file
        write(txn);
        System.out.println("Create : OK");
    }

    public static void create() {
        create(true);
    }

    public static void write(ArrayList<String[]> txn) {
        // take data passed and write to file
        PrintWriter pw = null;
        File outFile = null;
        try {
            outFile = new File(dbName);
            if (!outFile.exists()) {
                outFile.createNewFile(); // create file if not exist
            }
            pw = new PrintWriter(new FileOutputStream(outFile, true));
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
            System.out.println("SOME ERROR OCCURED!");
            exp.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public static ArrayList<String[]> input() {
        // method to take user input to write
        // to database file
        System.out.print("Enter total number of transactions followed\nby transaction list along with it's ID\n\n");
        System.out.print("Take help of 'sampleinput.txt' file for\nreference.\n> ");
        int t = sc.nextInt();
        sc.nextLine();
        ArrayList<String[]> txn = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            String txnData = sc.nextLine();
            String[] xt = txnData.split(" ");
            txn.add(xt);
        }
        return txn;
    }

    public static void delete() {
        // delete file
        try {
            File outFile = new File(dbName);
            if (outFile.exists()) {
                outFile.delete();
                System.out.println("Delete : OK");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
