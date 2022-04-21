import java.util.*;

public class Solution3 {
    public static void main(String[] args) {
        List<Integer> column1 = new ArrayList<>();
        List<Integer> column2 = new ArrayList<>();
        column1.addAll(Arrays.asList(new Integer[] { 1, 2, 1, 2, null, 3, 2, 1, 3, 3 }));
        column2.addAll(Arrays.asList(new Integer[] { null, 45, 55, 45, 55, 55, 45, 34, 45, 34 }));
        missingClassLables(column1, column2);
    }

    public static void missingClassLables(List<Integer> column1, List<Integer> column2) {
        System.out.println("Original Values");
        System.out.println("----------------------------------");
        for (int i = 0; i < column1.size(); i++) {
            System.out.println(column1.get(i) + "\t" + column2.get(i));
        }
        System.out.println("----------------------------------\n");
        for (int i = 0; i < column1.size(); i++) {
            if (column1.get(i) == null) {
                int peg = column2.get(i);
                int sum = 0;
                int count = 0;
                for (int j = 0; j < column2.size(); j++) {
                    if (column2.get(j) != null) {
                        if (column2.get(j) == peg) {
                            sum += (column1.get(j) == null ? 0 : column1.get(j));
                            count++;
                        }
                    }
                }
                int mean = sum / count;
                column1.remove(i);
                column1.add(i, mean);
            }
        }

        for (int i = 0; i < column2.size(); i++) {
            if (column2.get(i) == null) {
                int peg = column1.get(i);
                int sum = 0;
                int count = 0;
                for (int j = 0; j < column1.size(); j++) {
                    if (column1.get(j) != null) {
                        if (column1.get(j) == peg) {
                            sum += (column2.get(j) == null ? 0 : column2.get(j));
                            count++;
                        }
                    }
                }
                int mean = sum / count;
                column2.remove(i);
                column2.add(i, mean);
            }
        }

        System.out.println("Fixed Missing Values");
        System.out.println("----------------------------------");
        for (int i = 0; i < column1.size(); i++) {
            System.out.println(column1.get(i) + "\t" + column2.get(i));
        }
        System.out.println("----------------------------------\n");
    }
}
