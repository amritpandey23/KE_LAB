import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String[]> D = getDataFromFile("data.txt");
        String[] attribute_list = getAttributeList("data.txt");
        boolean[] usedAttributes = new boolean[attribute_list.length];
        usedAttributes[attribute_list.length - 1] = true;
        // System.out.println("Info(D) = " + Info(D));
        // attributeSelectionMethod(D, attribute_list, usedAttributes); 
        Node tree = GenerateDecisionTree(D, attribute_list, usedAttributes);

    }

    public static Node GenerateDecisionTree(List<String[]> D, String[] attribute_list, boolean[] usedAttributes) {
        Node leaf = new Node();
        if (isOfSameClass(D)) {
            leaf.isLeaf = true;
            leaf.classLabel = getPartitionLabel(D);
            return leaf;
        }
        if (isEmpty(usedAttributes)) {
            leaf.isLeaf = true;
            leaf.classLabel = getPartitionLabel(D);
            return leaf;
        }
        int splittingCriteria = attributeSelectionMethod(D, attribute_list, usedAttributes);
        leaf.splittingCriterion = splittingCriteria;
        usedAttributes[splittingCriteria] = true;
        List<String> attributeValues = collectAttributeValues(D, splittingCriteria);
        for (String outcome : attributeValues) {
            List<String[]> Dj = new ArrayList<>();
            for (String[] tuple : D) {
                if (tuple[splittingCriteria].equals(outcome)) {
                    Dj.add(tuple);
                }
            }
            leaf.children.add(GenerateDecisionTree(Dj, attribute_list, usedAttributes));
        }
        return leaf;
    }

    static class Node {
        public boolean isLeaf = false;
        public String classLabel = null;
        public int splittingCriterion = -1;
        public List<Node> children = new ArrayList<>();
    }

    public static List<String[]> getDataFromFile(String dbname) throws IOException {
        List<String[]> data = new ArrayList<>();
        File f = new File(dbname);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        int i = 0;

        while ((line = br.readLine()) != null) {
            if (i == 0) {
                i++;
                continue;
            }
            String[] tuple = line.split(" ");
            data.add(tuple);
        }

        br.close();

        return data;
    }

    public static String[] getAttributeList(String dbname) throws IOException {
        File f = new File(dbname);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        br.close();
        return line.split(" ");
    }

    public static int attributeSelectionMethod(List<String[]> D, String[] attribute_list, boolean[] usedAttributes) {
        int chosenIndex = -1;
        double maxGain = 0;
        for (int i = 0; i < attribute_list.length - 1; i++) {
            if (!usedAttributes[i] && Gain(D, i, attribute_list) > maxGain) {
                chosenIndex = i;
                maxGain = Gain(D, i, attribute_list);
            }
        }
        // System.out.println("Splitting Attribute = " + attribute_list[chosenIndex]);
        return chosenIndex;
    }

    public static double Gain(List<String[]> D, int attributeIndex, String[] attributeList) {
        System.out.println();
        System.out.println("Gain for " + attributeList[attributeIndex]);
        double Gain = 0;
        double InfoD = Info(D);
        System.out.println("Info(D) = " + InfoD);

        List<String> attributeValues = new ArrayList<>();
        // collect attribute values
        for (String[] tuple : D) {
            if (!attributeValues.contains(tuple[attributeIndex])) {
                attributeValues.add(tuple[attributeIndex]);
            }
        }

        // calc info gain
        for (String value : attributeValues) {
            List<String[]> Dj = new ArrayList<>();
            for (String[] tuple : D) {
                if (tuple[attributeIndex].equals(value)) {
                    Dj.add(tuple);
                }
            }
            Gain += ((double) Dj.size() / D.size()) * Info(Dj);
        }

        System.out.println("Info(" + attributeList[attributeIndex] + ") =" + Gain);

        Gain = InfoD - Gain;

        System.out.println("Gain(" + attributeList[attributeIndex] + ") = " + Gain);
        return Gain;
    }

    public static double Info(List<String[]> D) {
        double entropy = 0;
        int totalTuples = D.size();
        System.out.println("|D| = " + totalTuples);
        List<String> classLabels = new ArrayList<>();
        for (String[] tuple : D) {
            String label = tuple[tuple.length - 1];
            if (!classLabels.contains(label)) {
                classLabels.add(label);
            }
        }
        double[] pi = new double[classLabels.size()];
        for (int i = 0; i < classLabels.size(); i++) {
            String label = classLabels.get(i);
            for (String[] tuple : D) {
                if (label.equals(tuple[tuple.length - 1])) {
                    pi[i] += 1;
                }
            }
            System.out.println("Label(" + label + ") = " + pi[i]);
        }

        System.out.println("Total tuples = " + totalTuples);

        for (int i = 0; i < pi.length; i++) {
            System.out.println(pi[i] + "/" + totalTuples + " * " + Math.log(pi[i]));
            pi[i] /= totalTuples;
            entropy += -(pi[i] * Math.log(pi[i]));
        }

        return entropy;
    }

    public static boolean isOfSameClass(List<String[]> D) {
        String ex = D.get(0)[D.get(0).length - 1];
        for (String[] tuple : D) {
            String label = tuple[tuple.length - 1];
            if (!ex.equals(label)) {
                return false;
            }
        }
        return true;
    }

    public static String getPartitionLabel(List<String[]> D) {
        int count = 0;
        String ele = "";

        for (int i = 0; i < D.size(); i++) {
            if (count == 0) {
                ele = D.get(i)[D.get(i).length - 1];
            }
            if (D.get(i)[D.get(i).length - 1].equals(ele)) {
                count++;
            } else {
                count--;
            }
        }
        return ele;
    }

    public static boolean isEmpty(boolean[] attributeList) {
        for (boolean val : attributeList) {
            if (val) {
                return false;
            }
        }
        return true;
    }

    public static List<String> collectAttributeValues(List<String[]> D, int attribute) {
        List<String> values = new ArrayList<>();
        for (String[] tuple : D) {
            if (!values.contains(tuple[attribute])) {
                values.add(tuple[attribute]);
            }
        }
        return values;
    }
}
