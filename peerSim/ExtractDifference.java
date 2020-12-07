import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.math.BigDecimal;

public class ExtractDifference {
    private static List<List<Double>> failedRequests;
    private static List<List<Double>> numOfHops;
    private static List<List<Double>> totalCosts;

    private static List<String> algorithmNames;

    private static void initialize() {
        failedRequests = new ArrayList<>();
        numOfHops = new ArrayList<>();
        totalCosts = new ArrayList<>();

        algorithmNames = new ArrayList<>();

        algorithmNames.add("Cuckoo_Search");
        algorithmNames.add("Random");
        algorithmNames.add("Greedy");
        algorithmNames.add("Original_Only");

        for (int i = 0; i < algorithmNames.size(); i++) {
            failedRequests.add(new ArrayList<>());
            numOfHops.add(new ArrayList<>());
            totalCosts.add(new ArrayList<>());
        }
    }

    public static void main(String[] args) {
        initialize();
        String directoryPath = "./result/" + args[0] + args[1] + "/";

        for (int i = 0; i < algorithmNames.size(); i++) {
            String fileName = "[" + algorithmNames.get(i) + "].tsv";

            failedRequests.set(i, readValue(directoryPath + "Cumulative_Fails" + fileName, failedRequests.get(i)));
            numOfHops.set(i, readValue(directoryPath + "Cumulative_Hops" + fileName, numOfHops.get(i)));
            totalCosts.set(i, readValue(directoryPath + "Cumulative_Total" + fileName, totalCosts.get(i)));
        }

        writeComparison(directoryPath);
    }

    private static List<Double> readValue(String fileName, List<Double> list) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\t");
                list.add(Double.valueOf(words[1]));
            }

            reader.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }

        return list;
    }

    private static void writeComparison(String directoryPath) {
        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new FileWriter(directoryPath + "Comparison.tsv", false)));

            writer.println("==================================================================");
            writer.println("Failed Requests\n\n");
            Comparison(writer, failedRequests);

            writer.println("==================================================================");
            writer.println("Total Costs\n\n");
            Comparison(writer, totalCosts);

            writer.println("==================================================================");
            writer.println("Number of Hops\n\n");
            Comparison(writer, numOfHops);

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void Comparison(PrintWriter writer, List<List<Double>> list) {
        for (int i = 0; i < 500; i++) {
            int cycle = i + 1;

            if (cycle % 100 == 0) {
                writer.println(cycle + " cycle");
                double cuckooValue = list.get(0).get(i);

                for (int j = 1; j < algorithmNames.size(); j++) {
                    writeFile(writer, algorithmNames.get(j), cuckooValue, list.get(j).get(i));
                }

                writer.println();
            }
        }
    }

    private static void writeFile(PrintWriter writer, String name, double cuckooValue, double otherValue) {
        if (cuckooValue > otherValue) {
            double value = (1.0 - (otherValue / cuckooValue)) * 100.0;
            double roundValue = ((double) Math.round(value * 100)) / 100;
            writer.println(name + "\t" + "+" + roundValue);
        } else if (cuckooValue < otherValue) {
            double value = (1.0 - (cuckooValue / otherValue)) * 100.0;
            double roundValue = ((double) Math.round(value * 100)) / 100;
            writer.println(name + "\t" + "-" + roundValue);
        } else {
            writer.println(name + "\t" + "0.00");
        }
    }
}
