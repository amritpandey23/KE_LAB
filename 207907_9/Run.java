import java.util.*;
import java.io.*;

public class Run {

    public static void main(String[] args) throws IOException {
        String dbname = "data.txt";
        String[] attributeList = getAttributeList(dbname);
        List<String> D = getDataFromFile(dbname);
        for (int i = 0; i < attributeList.length; i++) {
            getAttributeValues(D, i);
        }
        boolean[] usedAttribute = new boolean[attributeList.length];
        System.out.println("DECISION TREE CLASSIFIER");
        System.out.println("------------------------");
        System.out.println();
        Node tree = GenerateDecisionTree(D, attributeList, usedAttribute, 0);
    }

    // Implementation of standard Decision Tree Algorithm
    /**
     * @param Database
     * @param attributeList
     * @param usedAttribute
     * @param level
     */
    public static Node GenerateDecisionTree(List<String> D, String[] attributeList, boolean[] usedAttribute, int level) {
        Node node = new Node();
        if (hasSameClass(D)) {
            node.label = getClassOf(D);
            System.out.println("\t\tLeaf(" + node.label + ")");
            return node;
        }
        if (isEmpty(usedAttribute)) {
            node.label = getClassOf(D);
            System.out.println("\t\tLeaf(" + node.label + ")");
            return node;
        }
        int splitCriteria = selectAttribute(D, attributeList, usedAttribute);
        node.label = "SPLITTING CRITERIA : " + attributeList[splitCriteria];
        String[] outcomes = attributeValues.get(splitCriteria);
        for (String outcome : outcomes) {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.print("Split Criteria = " + attributeList[splitCriteria] + "\t");
            System.out.println("\tOutcome = " + outcome);
            List<String> Dj = partition(D, splitCriteria, outcome);
            if (Dj.size() == 0) {
                Node leaf = new Node();
                leaf.label = getClassOf(D);
                System.out.println("\t\tLeaf(" + leaf.label + ")");
                node.children.add(leaf);
            } else {
                boolean[] tmp = new boolean[usedAttribute.length];
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = usedAttribute[i];
                }
                node.children.add(GenerateDecisionTree(Dj, attributeList, tmp, level + 1));
            }
        }
        return node;
    }

    // variable to collect attribute values
    public static List<String[]> attributeValues = new ArrayList<>();

    // structure of Node
    public static class Node {
        String label;
        List<Node> children;

        public Node(String label, List<Node> children) {
            this.label = label;
            this.children = new ArrayList<>();
        }

        public Node() {
            this.label = "";
            this.children = new ArrayList<>();
        }
    }

    public static boolean hasSameClass(List<String> D) {
        int L = D.get(0).split(" ").length - 1;
        String beginClass = D.get(0).split(" ")[L];
        for (String tuple : D) {
            String[] tokens = tuple.split(" ");
            if (!beginClass.equals(tokens[L])) {
                return false;
            }
        }
        return true;
    }

    public static String[] getAttributeList(String dbname) throws IOException {
        File f = new File(dbname);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        br.close();
        return line.split(" ");
    }

    public static void getAttributeValues(List<String> D, int attribute) {
        List<String> values = new ArrayList<>();
        for (String row : D) {
            String[] tuple = row.split(" ");
            if (!values.contains(tuple[attribute])) {
                values.add(tuple[attribute]);
            }
        }
        String[] tmp = new String[values.size()];
        String[] o = values.toArray(tmp);
        attributeValues.add(o);
    }


    public static boolean isEmpty(boolean[] usedAttributes) {
        for (boolean val : usedAttributes) {
            if (!val) {
                return false;
            }
        }
        return true;
    }

    public static String getClassOf(List<String> D) {
        int count = 0;
        String ele = "";
        int L = D.get(0).split(" ").length - 1;

        for (String tuple : D) {
            String classLabel = tuple.split(" ")[L];
            if (count == 0) {
                ele = classLabel;
            }
            if (ele.equals(classLabel)) {
                count++;
            } else {
                count--;
            }
        }
        return ele;
    }

    public static List<String> partition(List<String> D, int splitCriteria, String outcome) {
        List<String> res = new ArrayList<>();
        for (String tuple : D) {
            String[] tokens = tuple.split(" ");
            if (outcome.equals(tokens[splitCriteria])) {
                res.add(tuple);
            }
        }
        return res;
    }

    public static int selectAttribute(List<String> D, String[] attribute_list, boolean[] usedAttributes) {
        int chosenIndex = -1;
        double maxGain = 0;
        for (int i = 0; i < attribute_list.length - 1; i++) {
            if (!usedAttributes[i] && Gain(D, i, attribute_list) > maxGain) {
                chosenIndex = i;
                maxGain = Gain(D, i, attribute_list);
            }
        }
        usedAttributes[chosenIndex] = true;
        return chosenIndex;
    }

    public static double Gain(List<String> D, int attributeIndex, String[] attributeList) {
        double Gain = 0;
        double InfoD = Info(D);

        List<String> attributeValues = new ArrayList<>();
        for (String t : D) {
            String[] tuple = t.split(" ");
            if (!attributeValues.contains(tuple[attributeIndex])) {
                attributeValues.add(tuple[attributeIndex]);
            }
        }

        // calc info gain
        for (String value : attributeValues) {
            List<String> Dj = new ArrayList<>();
            for (String t : D) {
                String[] tuple = t.split(" ");
                if (tuple[attributeIndex].equals(value)) {
                    Dj.add(t);
                }
            }
            Gain += ((double) Dj.size() / D.size()) * Info(Dj);
        }

        Gain = InfoD - Gain;

        return Gain;
    }

    public static double Info(List<String> D) {
        double entropy = 0;
        int totalTuples = D.size();
        List<String> classLabels = new ArrayList<>();
        for (String t : D) {
            String[] tuple = t.split(" ");
            String label = tuple[tuple.length - 1];
            if (!classLabels.contains(label)) {
                classLabels.add(label);
            }
        }
        double[] pi = new double[classLabels.size()];
        for (int i = 0; i < classLabels.size(); i++) {
            String label = classLabels.get(i);
            for (String t : D) {
                String[] tuple = t.split(" ");
                if (label.equals(tuple[tuple.length - 1])) {
                    pi[i] += 1;
                }
            }
        }

        for (int i = 0; i < pi.length; i++) {
            pi[i] /= totalTuples;
            entropy += -(pi[i] * Math.log(pi[i]));
        }

        return entropy;
    }

    public static List<String> getDataFromFile(String dbname) throws IOException {
        List<String> data = new ArrayList<>();
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
            data.add(line);
        }

        br.close();
        return data;
    }
}
