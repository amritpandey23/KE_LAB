package src;
/*  KE Lab MCA II Year, Semester 4 
    author : Amrit Pandey, MCA, 207907
    date: 07-01-2022
    problem: Find the support/count of items
            Ex: a 2
            b 2
            c 2
            d 3
            e 2
            f 3
*/


import java.util.*;
import java.io.*;

public class _3_FindSupport {
    public static void execute() {
        // find support of each item
        ArrayList<String[]> txn = _2_ReadTransaction.transactions(); // collect data from file
        if (txn == null) {
            return;
        }
        TreeMap<String, Integer> map = new TreeMap<>(); // maintain keys and count in sorted order

        for (String[] xt : txn) {
            for (int i = 1; i < xt.length; i++) {
                if (map.containsKey(xt[i])) {
                    map.put(xt[i], map.get(xt[i]) + 1);
                } else {
                    map.put(xt[i], 1);
                }
            }
        }

        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Iterator<Map.Entry<String, Integer>> it = entries.iterator();

        // iterate over data and save into file
        // as well as print to console
        try {
            File saveFile = new File("q3_output.txt");
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileOutputStream(saveFile));
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
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
