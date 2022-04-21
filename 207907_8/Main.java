import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        List<int[]> D = extractDataFromFile("data.txt");
        kMeans(D, 3);
    }

    public static List<int[]> extractDataFromFile(String dbname) throws IOException {
        List<int[]> D = new ArrayList<>();
        File dataFile = new File(dbname);
        FileReader fr = new FileReader(dataFile);
        BufferedReader br = new BufferedReader(fr);
        String line = null;

        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(" ");
            int x = Integer.parseInt(tokens[0]);
            int y = Integer.parseInt(tokens[1]);
            int[] point = new int[2];
            point[0] = x;
            point[1] = y;
            D.add(point);
        }

        br.close();
        fr.close();
        return D;
    }

    public static List<List<int[]>> kMeans(List<int[]> D, int k) {
        List<List<int[]>> clusters = new ArrayList<>();
        int[][] centroids = new int[k][2];

        // initializing empty clusters
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }

        // generating K arbitary points
        int temp = D.size() / k;
        int t = 0;
        for (int i = 0; i < D.size() && t < k; i += temp) {
            int z = (int) (Math.random() * D.size() + 1);
            centroids[t][0] = D.get(z)[0];
            centroids[t][1] = D.get(z)[1];
            t++;
        }

        System.out.println("Initial Centroid");
        System.out.println("----------------");
        for (int[] c : centroids) {
            System.out.println(c[0] + " " + c[1]);
        }

        // adding all the data points to
        // first cluster
        for (int[] point : D) {
            clusters.get(0).add(point);
        }

        int epochs = 100;
        do {
            // assign points to cluster
            List<List<int[]>> newClusters = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                newClusters.add(new ArrayList<>());
            }
            for (int i = 0; i < k; i++) {
                List<int[]> cluster = clusters.get(i);
                for (int[] point : cluster) {
                    int pick = -1;
                    for (int p = 0; p < k; p++) {
                        if (pick == -1 || distance(centroids[pick], point) > distance(centroids[p], point)) {
                            pick = p;
                        }
                    }
                    newClusters.get(pick).add(point);
                }
            }

            if (!isChanged(clusters, newClusters)) {
                System.out.println("\n!!Clusters have not changed!!\n");
                System.out.println("Centroids, iteration = " + epochs);
                printCentroids(centroids);
                System.out.println("Cluster, iteration = " + epochs);
                printClusters(clusters);
                System.out.println("-------------------------");
                break;
            }

            clusters = newClusters;

            // calculate new centroid
            for (int i = 0; i < k; i++) {
                int[] newCentroid = mean(clusters.get(i));
                centroids[i] = newCentroid;
            }

            System.out.println("Centroids, iteration = " + epochs);
            printCentroids(centroids);
            System.out.println("Cluster, iteration = " + epochs);
            printClusters(clusters);
            System.out.println("-------------------------");
        } while (--epochs != 0);
        return clusters;
    }

    public static int distance(int[] a, int[] b) {
        int z = (a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]);
        return (int) Math.sqrt(z);
    }

    public static int[] mean(List<int[]> cluster) {
        int x = 0;
        int y = 0;
        int t = cluster.size();

        for (int[] point : cluster) {
            x += point[0];
            y += point[1];
        }

        int[] ans = new int[2];
        ans[0] = x / t;
        ans[1] = y / t;

        return ans;
    }

    public static boolean isChanged(List<List<int[]>> c1, List<List<int[]>> c2) {
        for (int i = 0; i < c1.size(); i++) {
            if (c1.get(i).size() != c2.get(i).size()) {
                return true;
            }
            for (int j = 0; j < c1.get(i).size(); j++) {
                if (c1.get(i).get(j)[0] != c2.get(i).get(j)[0] && c1.get(i).get(j)[1] != c2.get(i).get(j)[1]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void printClusters(List<List<int[]>> clusters) {
        for (List<int[]> points : clusters) {
            for (int[] p : points) {
                System.out.print("(" + p[0] + " " + p[1] + "), ");
            }
            System.out.println();
        }
    }

    public static void printCentroids(int[][] centroids) {
        for (int[] c : centroids) {
            System.out.println(c[0] + " " + c[1]);
        }
    }
}
