package src;
/*  KE Lab MCA II Year, Semester 4 
    author : Amrit Pandey, MCA, 207907
    date: 07-01-2022
    problem: Extract item- transaction list
            Ex: a T1 T3
            b T1 T2
            c T1 T3 T5
            d T4 T5
            e T1 T5
*/

import java.util.*;
import java.io.*;

// class to group associated functions and methods
public class _4_ExtractItems {
    public static void extract() {
        ArrayList<String[]> txn = _2_ReadTransaction.transactions(); // read file and fetch data
        if (txn == null) {
            return;
        }
        TreeMap<String, ArrayList<String>> map = new TreeMap<>(); // for saving keys in sorted order

        for (String[] xt : txn) {
            String tid = xt[0];
            for (int i = 1; i < xt.length; i++) {
                if (map.containsKey(xt[i])) {
                    map.get(xt[i]).add(tid);
                } else {
                    ArrayList<String> coll = new ArrayList<>();
                    coll.add(tid);
                    map.put(xt[i], coll);
                }
            }
        }

        Set<Map.Entry<String, ArrayList<String>>> entries = map.entrySet();
        Iterator<Map.Entry<String, ArrayList<String>>> it = entries.iterator();

        // iterate over the map to save to data
        // to file as well as print to console
        try {
            File saveFile = new File("q4_output.txt");
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(saveFile));
            while (it.hasNext()) {
                Map.Entry<String, ArrayList<String>> entry = it.next();
                String out = entry.getKey() + " : " + entry.getValue();
                System.out.println(out);
                pw.println(out);
            }
            pw.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
}
