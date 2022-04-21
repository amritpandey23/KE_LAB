import java.util.*;

public class Solution1 {
    private static int CONSTANT = Integer.MIN_VALUE;

    public static void main(String[] args) {
        // create 20 random values with two missing position
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            values.add(random(80));
        }
        values.add(random(0, 18), null);
        values.add(random(0, 18), null);

        // displaying generated values
        System.out.println("Randomly generated values");
        System.out.println(values);
        System.out.println("\n--------------------------------------\n");

        // Technique 1
        System.out.println("1. Ignore the missing values");
        ignoreTuple(values);
        System.out.println("\n--------------------------------------\n");

        // Technique 2
        System.out.println("2. Replace missing values with global constant");
        globalConstant(values, CONSTANT); // CONSTANT: global constant
        System.out.println("\n--------------------------------------\n");

        // Technique 3
        System.out.println("3. Replace missing values with central tendency");
        System.out.println("\t- Mean is used as central tendency");
        centralTendency(values);
        System.out.println("\n--------------------------------------\n");
    }

    // 1. Ignore missing values
    public static List<Integer> ignoreTuple(List<Integer> values) {
        // remove all the missing(null) values
        // add rest of the values to the list
        List<Integer> cleanedList = new ArrayList<>();
        for (Integer val : values) {
            if (val != null) {
                cleanedList.add(val);
            }
        }
        System.out.println("Cleaned list = " + cleanedList);
        return cleanedList;
    }

    // 2. Fill missing values with global constant
    public static List<Integer> globalConstant(List<Integer> values, int gc) {
        // replace all the missing(null) values with
        // global constant(gc) passed in the function
        List<Integer> cleanedList = new ArrayList<>();
        System.out.println("Global constant = " + gc);
        for (Integer val : values) {
            cleanedList.add(val == null ? gc : val);
        }
        System.out.println("Cleaned list = " + cleanedList);
        return cleanedList;
    }

    // 3. Fill missing values with mean of data
    public static List<Integer> centralTendency(List<Integer> values) {
        // I am using mean as the central tendency
        // to replace missing values
        double mean = 0;
        int n = 0;
        for (Integer val : values) {
            mean += (val == null ? 0 : val);
            n += (val == null ? 0 : 1);
        }
        mean /= n;
        // this mean is rounded off so that
        // closest integer to mean can be
        // inserted at the place of missing
        // values
        int M = (int) Math.round(mean);
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) == null) {
                values.remove(i);
                values.add(i, M);
            }
        }
        System.out.println("\t- Mean = " + M);
        System.out.println("Cleaned list + " + values);
        return values;
    }

    // code to generate random values
    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static int random(int max) {
        return random(1, max);
    }
}
