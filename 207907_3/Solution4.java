import java.util.*;

public class Solution4 {
    /*
     *  test case:
     * first line is the number of tuples (N)
     * following (N) lines have tuple values

6
67 89
45 80
22 45
33 12
5 12
29 67

     * 
     */

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        List<Double> column1 = new ArrayList<>();
        List<Double> column2 = new ArrayList<>();

        System.out.println("Enter total number of entries/tuples, followed by tuple entries");
        int n = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < n; i++) {
            String values = sc.nextLine();
            String[] nums = values.split(" ");
            double c1 = Double.parseDouble(nums[0]);
            double c2 = Double.parseDouble(nums[1]);
            column1.add(c1);
            column2.add(c2);
        }

        System.out.println("Original Values");
        System.out.println("------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println(column1.get(i) + "\t" + column2.get(i));
        }
        System.out.println();

        System.out.println("Transformation using MinMax");
        System.out.println("------------------------------------");
        List<Double> mmList1 = minMax(column1);
        List<Double> mmList2 = minMax(column2);
        for (int i = 0; i < n; i++) {
            System.out.println(change(mmList1.get(i), 2) + "\t" + change(mmList2.get(i), 2));
        }
        System.out.println();

        System.out.println("Transformation using Z-Score");
        System.out.println("------------------------------------");
        List<Double> zList1 = zScore(column1);
        List<Double> zList2 = zScore(column2);
        for (int i = 0; i < n; i++) {
            System.out.println(change(zList1.get(i), 2) + "\t" + change(zList2.get(i), 2));
        }
        System.out.println();

        System.out.println("Transformation using Decimal Scaling");
        System.out.println("------------------------------------");
        List<Double> dsList1 = decimalScaling(column1);
        List<Double> dsList2 = decimalScaling(column2);
        for (int i = 0; i < n; i++) {
            System.out.println(dsList1.get(i) + "\t" + dsList2.get(i));
        }
    }

    // 1. min-max normalization
    public static List<Double> minMax(List<Double> values) {
        // min max transformation
        Pair minMaxValues = findMinMaxValues(values);
        double max = minMaxValues.max;
        double min = minMaxValues.min;
        double new_max = 1;
        double new_min = 0;
        List<Double> normalizedList = new ArrayList<>();
        for (double val : values) {
            double temp = (val - min);
            temp /= (max - min);
            temp *= (new_max - new_min);
            temp += new_min;
            normalizedList.add(temp);
        }
        return normalizedList;
    }

    // 2. z-score normalization
    public static List<Double> zScore(List<Double> values) {
        // z-score scaling transformation
        double mean = mean(values);
        double sd = sd(values);
        List<Double> normalizedList = new ArrayList<>();
        for (double v : values) {
            normalizedList.add((v - mean) / sd);
        }
        return normalizedList;
    }

    // 3. decimal scaling
    public static List<Double> decimalScaling(List<Double> values) {
        // decimal scaling transformation
        Pair minMax = findMinMaxValues(values);
        double max = Math.max(Math.abs(minMax.min), Math.abs(minMax.max));
        int i = 1;
        while (Math.round(max / i) > 1) {
            i *= 10;
        }
        List<Double> normalizedList = new ArrayList<>();
        for (double v : values) {
            normalizedList.add(v / i);
        }
        return normalizedList;
    }

    public static class Pair {
        double min;
        double max;

        public Pair() {
            this.min = Integer.MAX_VALUE;
            this.max = Integer.MIN_VALUE;
        }
    }

    public static Pair findMinMaxValues(List<Double> values) {
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
}
