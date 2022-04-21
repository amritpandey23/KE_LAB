package src;
/*  KE Lab MCA II Year, Semester 4 
    author : Amrit Pandey, MCA, 207907
    date: 07-01-2022
    problem: Read Transactions from file using file operations
*/

import java.util.*;
import java.io.*;

// Class to group all associated functions and methods
public class _2_ReadTransaction {
    public static void read() {
        // read data from file
        ArrayList<String[]> txn = transactions(); // fetch data in an arraylist
        if (txn == null) {
            return;
        }
        for (String[] xt : txn) {
            printTransaction(xt);
        }
    }

    public static ArrayList<String[]> transactions() {
        // read file and collect data in a 
        // structured format
        ArrayList<String[]> txn = new ArrayList<>();
        BufferedReader br = null;
        File db = new File("output.txt");
        if (!db.exists()) {
            // abort read if file does not exist
            System.out.println("Error: Database file missing");
            return null;
        }
        try {
            br = new BufferedReader(new FileReader(db));
            String line;
            while ((line = br.readLine()) != null) {
                String[] xt = line.split(",");
                txn.add(xt);
            }
            br.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return txn;
    }

    public static void printTransaction(String[] xt) {
        // format and print transaction on console
        for (int i = 0; i < xt.length; i++) {
            if (i == 0) {
                System.out.print(xt[i] + " : ");
            } else {
                System.out.print(xt[i] + " ");
            }
        }
        System.out.println();
    }
}
