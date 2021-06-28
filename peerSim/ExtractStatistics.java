import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.math.BigDecimal;

public class ExtractStatistics {
    private static final int AVERAGE_CYCLE = 10;
    private static List<Statistics> failedRequests;
    private static List<Statistics> numOfHops;
    private static List<Statistics> storageCosts;
    private static List<Statistics> processingCosts;
    private static List<Statistics> transmissionCosts;
    private static List<Statistics> costs;
    private static List<Statistics> failedHops;
    private static Statistics numOfRequests;
    private static Statistics failedServers;

    private static List<String> algorithmNames;
    private static List<String> fileNames;

    private static void initialize(int totalTry) {
        failedRequests = new ArrayList<>();
        numOfHops = new ArrayList<>();
        storageCosts = new ArrayList<>();
        processingCosts = new ArrayList<>();
        transmissionCosts = new ArrayList<>();
        costs = new ArrayList<>();
        failedHops = new ArrayList<>();
        numOfRequests = new Statistics(totalTry);
        failedServers = new Statistics(totalTry);

        algorithmNames = new ArrayList<>();
        fileNames = new ArrayList<>();

        algorithmNames.add("Cuckoo_Search");
        algorithmNames.add("Random");
        algorithmNames.add("Greedy");
        algorithmNames.add("Original_Only");

        for (int i = 0; i < algorithmNames.size(); i++) {
            failedRequests.add(new Statistics(totalTry));
            numOfHops.add(new Statistics(totalTry));
            storageCosts.add(new Statistics(totalTry));
            processingCosts.add(new Statistics(totalTry));
            transmissionCosts.add(new Statistics(totalTry));
            costs.add(new Statistics(totalTry));
            failedHops.add(new Statistics(totalTry));
        }

        fileNames.add("Cumulative_Fails");
        fileNames.add("Cumulative_Hops");
        fileNames.add("Cumulative_Storage");
        fileNames.add("Cumulative_Processing");
        fileNames.add("Cumulative_Transmission");
        fileNames.add("Cumulative_Total");
        fileNames.add("Failed_Hops_Distribution");
        fileNames.add("Number_of_Requests");
        fileNames.add("Number_of_Failure_Servers");
        fileNames.add("Success_Ratio");
    }

    public static void main(String[] args) {
        int totalTry = Integer.valueOf(args[0]);
        String name = String.valueOf(args[2]);
        String directoryPath = "./result/" + name + args[1] + "/";

        initialize(totalTry);

        for (String fileName : fileNames) {
            if (fileName.equals("Number_of_Requests")) {
                String fullFileName = fileName + "[" + algorithmNames.get(0) + "].tsv";
                extractOneResult(numOfRequests, directoryPath, fileName, fullFileName, totalTry);
                continue;

            } else if (fileName.equals("Number_of_Failure_Servers")) {
                String fullFileName = fileName + ".tsv";
                extractOneResult(failedServers, directoryPath, fileName, fullFileName, totalTry);
                continue;
            }

            for (int algorithmId = 0; algorithmId < algorithmNames.size(); algorithmId++) {
                String fullFileName = fileName + "[" + algorithmNames.get(algorithmId) + "].tsv";
                extractResultPerAlgorithm(algorithmId, directoryPath, fileName, fullFileName, totalTry);
            }
        }
    }

    private static void extractResultPerAlgorithm(int algorithmId, String directoryPath, String fileName,
            String fullFileName, int totalTry) {
        if (fileName.equals("Cumulative_Fails")) {
            writeResult(failedRequests, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Cumulative_Hops")) {
            writeResult(numOfHops, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Cumulative_Storage")) {
            writeResult(storageCosts, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Cumulative_Processing")) {
            writeResult(processingCosts, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Cumulative_Transmission")) {
            writeResult(transmissionCosts, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Cumulative_Total")) {
            writeResult(costs, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Failed_Hops_Distribution")) {
            writeResult(failedHops, algorithmId, directoryPath, fullFileName, totalTry);
        } else if (fileName.equals("Success_Ratio")) {
            writeSuccessRatio(failedRequests, numOfRequests, algorithmId, directoryPath, fullFileName, totalTry);
        }
    }

    private static void extractOneResult(Statistics result, String directoryPath, String fileName, String fullFileName,
            int totalTry) {
        result = calculateStatistics(result, directoryPath, fullFileName, totalTry);
        writeTotalRequests(result, directoryPath, fileName, totalTry);

        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new FileWriter(directoryPath + "/" + fileName + ".tsv", false)));

            for (int cycle = 0; cycle < 500; cycle++) {
                double average = (result.total[cycle] / ((double) totalTry));
                double standardDeviation = calculateStandardDeviation(result, average, cycle, totalTry);

                writer.println(cycle + "\t" + BigDecimal.valueOf(average).toPlainString() + "\t"
                        + BigDecimal.valueOf(result.max[cycle]).toPlainString() + "\t"
                        + BigDecimal.valueOf(result.min[cycle]).toPlainString() + "\t"
                        + BigDecimal.valueOf(standardDeviation).toPlainString());
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void writeTotalRequests(Statistics result, String directoryPath, String fileName, int totalTry) {
        if (!fileName.equals("Number_of_Requests")) {
            return;
        }

        try {
            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new FileWriter(directoryPath + "/Cumulative_Requests.tsv", false)));

            double total = 0.0;
            for (int cycle = 0; cycle < 500; cycle++) {
                total += result.total[cycle] / ((double) totalTry);
                writer.println(cycle + "\t" + BigDecimal.valueOf(total).toPlainString());
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void writeResult(List<Statistics> results, int algorithmId, String directoryPath, String fileName,
            int totalTry) {
        Statistics result = calculateStatistics(results.get(algorithmId), directoryPath, fileName, totalTry);
        results.set(algorithmId, result);

        try {

            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new FileWriter(directoryPath + "/" + fileName, false)));

            if (fileName.contains("Transmission")) {
                writeTransmissionCostResult(result, totalTry, fileName, writer);
            } else {
                for (int cycle = 0; cycle < 500; cycle++) {
                    double average = result.total[cycle] / ((double) totalTry);
                    double standardDeviation = calculateStandardDeviation(result, average, cycle, totalTry);
                    String text = cycle + "\t" + BigDecimal.valueOf(average).toPlainString() + "\t"
                            + BigDecimal.valueOf(result.max[cycle]).toPlainString() + "\t"
                            + BigDecimal.valueOf(result.min[cycle]).toPlainString() + "\t"
                            + BigDecimal.valueOf(standardDeviation).toPlainString();

                    if (fileName.contains("Failed_Hops_Distribution")) {
                        if (average > 0.0) {
                            writer.println(text);
                        }
                    } else {
                        writer.println(text);
                    }
                }

                writer.close();
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void writeTransmissionCostResult(Statistics result, int totalTry, String fileName,
            PrintWriter writer) {
        int adjustCycle = 0;

        for (int cycle = 0; cycle <= 500; cycle += AVERAGE_CYCLE) {
            if (cycle != 0) {
                adjustCycle = cycle - 1;
            }
            double average = result.total[adjustCycle] / ((double) totalTry);
            double standardDeviation = calculateStandardDeviation(result, average, adjustCycle, totalTry);
            String text = adjustCycle + "\t" + BigDecimal.valueOf(average).toPlainString() + "\t"
                    + BigDecimal.valueOf(result.max[adjustCycle]).toPlainString() + "\t"
                    + BigDecimal.valueOf(result.min[adjustCycle]).toPlainString() + "\t"
                    + BigDecimal.valueOf(standardDeviation).toPlainString();

            if (fileName.contains("Failed_Hops_Distribution")) {
                if (average > 0.0) {
                    writer.println(text);
                }
            } else {
                writer.println(text);
            }
        }

        writer.close();
    }

    private static Statistics calculateStatistics(Statistics result, String directoryPath, String fileName,
            int totalTry) {
        try {
            for (int tryCount = 0; tryCount < totalTry; tryCount++) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(directoryPath + "/" + tryCount + "/" + fileName)));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\t");
                    if (words.length == 2) {
                        int cycle = Integer.valueOf(words[0]);
                        double value = Double.valueOf(words[1]);

                        if (fileName.contains("Transmission")) {
                            result.value[tryCount][cycle] = value;
                            result = calcTransmissionCost(result, cycle, value);
                        } else {
                            result.value[tryCount][cycle] = value;
                            result.total[cycle] += value;
                            result = calculateMaxAndMin(result, tryCount, cycle, value);
                        }
                    }
                }

                reader.close();
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }

        return result;
    }

    private static Statistics calcTransmissionCost(Statistics result, int cycle, double value) {
        if (cycle == 0 || (cycle + 1 % AVERAGE_CYCLE == 0)) {
            result.total[cycle] = value;
        } else {
            result.total[cycle] += value;
        }

        return result;
    }

    private static Statistics calculateMaxAndMin(Statistics result, int tryCount, int cycle, double value) {
        if (tryCount == 0) {
            result.max[cycle] = value;
            result.min[cycle] = value;
        } else {
            if (value > result.max[cycle]) {
                result.max[cycle] = value;
            }
            if (value < result.min[cycle]) {
                result.min[cycle] = value;
            }
        }

        return result;
    }

    private static double calculateStandardDeviation(Statistics result, double average, int cycle, int totalTry) {
        double total = 0.0;

        for (int tryCount = 0; tryCount < totalTry; tryCount++) {
            total += Math.pow((result.value[tryCount][cycle] - average), 2);
        }

        return Math.sqrt(total / totalTry);
    }

    private static void writeSuccessRatio(List<Statistics> failedRequests, Statistics numOfRequests, int algorithmId,
            String directoryPath, String fileName, int totalTry) {
        try {

            PrintWriter writer = new PrintWriter(
                    new BufferedWriter(new FileWriter(directoryPath + "/" + fileName, false)));

            double cumulativeRequests = 0.0;
            for (int cycle = 0; cycle < 500; cycle++) {
                double cumulativeFailedRequests = failedRequests.get(algorithmId).total[cycle] / ((double) totalTry);
                cumulativeRequests += numOfRequests.total[cycle] / ((double) totalTry);

                double successRatio = (cumulativeRequests - cumulativeFailedRequests) / cumulativeRequests;
                String text = cycle + "\t" + successRatio + "\t";
                // String text = cycle + "\t" + BigDecimal.valueOf(successRatio).toPlainString()
                // + "\t";

                writer.println(text);
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}

class Statistics {
    public double[][] value;
    public double[] total;
    public double[] max;
    public double[] min;

    public Statistics(int totalTry) {
        value = new double[totalTry][500];
        total = new double[500];
        max = new double[500];
        min = new double[500];
    }
}