import java.util.*;

public class Solution2 {
    public static void main(String[] args) {
        // generate 20 random values
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            values.add(random(40));
        }
        Collections.sort(values);

        // data from book:
        // 4, 8, 15, 21, 21, 24, 25, 28, 34 (Page 90)
        // NOTE: the data used here is for experimental purposes
        List<Integer> sampleData = new ArrayList<>();
        sampleData.addAll(Arrays.asList(new Integer[] { 4, 8, 15, 21, 21, 24, 25, 28, 34 }));

        // RESULTS
        System.out.println("1. Binning method on random values");
        smoothen(values, 4);
        System.out.println("\n--------------------------------------\n");

        System.out.println("2. Binning method on sample data from book");
        smoothen(sampleData, 3);
        System.out.println("\n--------------------------------------\n");
    }

    // smoothen values by binning methods:
    // - by means
    // - by boundary
    public static void smoothen(List<Integer> values, int freq) {
        // printing original values
        System.out.println("Original Values : " + values);
        List<List<Integer>> bins = createBins(values, freq);

        System.out.println("\nPartition into (equal-frequency) bins: ");
        for (List<Integer> bin : bins) {
            System.out.println("Bin " + bins.indexOf(bin) + ": " + bin);
        }

        System.out.println("\nSmoothing by bin means: ");
        for (List<Integer> bin : bins) {
            smoothenByMean(bins, bin);
        }
        for (List<Integer> bin : bins) {
            System.out.println("Bin " + bins.indexOf(bin) + ": " + bin);
        }

        bins = createBins(values, freq);

        System.out.println("\nSmoothing by bin boundary: ");
        for (List<Integer> bin : bins) {
            smoothenByBoundary(bins, bin);
        }
        for (List<Integer> bin : bins) {
            System.out.println("Bin " + bins.indexOf(bin) + ": " + bin);
        }
    }

    // smoothen bin values by mean
    public static void smoothenByMean(List<List<Integer>> bins, List<Integer> bin) {
        double mean = 0;
        int n = bin.size();
        for (int val : bin) {
            mean += val;
        }
        mean /= n;
        int M = (int) Math.round(mean);
        List<Integer> smoothBin = new ArrayList<>();
        for (int i = 0; i < bin.size(); i++) {
            // bin.remove(i);
            // bin.add(i, M);
            smoothBin.add(M);
        }
        bins.set(bins.indexOf(bin), smoothBin);
    }

    // smoothen bin values by boundary
    public static void smoothenByBoundary(List<List<Integer>> bins, List<Integer> bin) {
        if (bin.size() <= 2) {
            return;
        }
        int min = bin.get(0);
        int max = bin.get(bin.size() - 1);
        List<Integer> smoothBin = new ArrayList<>();
        smoothBin.add(min);
        for (int i = 1; i < bin.size() - 1; i++) {
            if (Math.abs(bin.get(i) - min) < Math.abs(bin.get(i) - max)) {
                // bin.remove(i);
                // bin.add(i, min);
                smoothBin.add(min);
            } else {
                // bin.remove(i);
                // bin.add(i, max);
                smoothBin.add(max);
            }
        }
        smoothBin.add(max);
        bins.set(bins.indexOf(bin), smoothBin);
    }

    public static List<List<Integer>> createBins(List<Integer> values, int freq) {
        List<List<Integer>> bins = new ArrayList<>();
        int i = 0;
        while (i < values.size()) {
            bins.add(values.subList(i, i + freq));
            i += freq;
        }
        return bins;
    }

    // code to generate random values
    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static int random(int max) {
        return random(1, max);
    }
}
