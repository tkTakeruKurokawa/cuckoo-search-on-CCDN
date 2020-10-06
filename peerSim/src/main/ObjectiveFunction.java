package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class ObjectiveFunction implements Control {
    private static final String PAR_USERS = "users";
    private static int users;
    private static final String PAR_TYPE = "type";
    private static int type;
    // private static final String PAR_FAILURE_RATE_COEFFICIENT =
    // "availabilityCoefficient";
    // private static double availabilityCoefficient;

    private static int totalNodes;

    private static double accessRate;
    private static double cost;
    private static double availability;
    private static double total;

    private static List<PrintWriter> availabilityWriter;
    private static List<PrintWriter> evaluationWriter;
    private static PrintWriter costWriter;

    public ObjectiveFunction(String prefix) {
        totalNodes = Network.size() - 1;
        users = Configuration.getInt(prefix + "." + PAR_USERS);
        type = Configuration.getInt(prefix + "." + PAR_TYPE);
        // availabilityCoefficient = Configuration.getDouble(prefix + "." +
        // PAR_FAILURE_RATE_COEFFICIENT);
    }

    public static double getEvaluation(ArrayList<Integer> placementNodes, Content content) {
        accessRate = calculateAccessRate(placementNodes, content);
        cost = calculateCost(placementNodes.size(), content);
        availability = calculateAvailability(placementNodes, content);

        switch (type) {
            case 0:
                total = cost + availability;
                break;
            case 1:
                total = accessRate + cost;
                break;
            default:
                System.exit(0);
                break;
        }

        // System.out.println("Number of Replica: " + placementNodes.size() + ",Access:"
        // + accessRate + ", Cost: " + cost
        // + ", Failure: " + availability);
        // System.out.println("Total = " + total);
        // System.out.println();
        return total;
    }

    private static double calculateAccessRate(ArrayList<Integer> placementNodes, Content content) {
        double totalRequests = content.getPopularity() * (double) totalNodes * (double) users;

        return Flooding.getAverageHops(placementNodes) * totalRequests * ((double) content.getSize());
    }

    private static double calculateCost(int totalReplicas, Content content) {
        return totalReplicas * content.getSize();
    }

    private static double calculateAvailability(ArrayList<Integer> placementNodes, Content content) {
        double totalRequests = content.getPopularity() * (double) totalNodes * (double) users;

        return (totalRequests * ((double) content.getSize())) / Flooding.getAvailability();
    }

    public static String getData() {
        return ("Access : " + accessRate + "\nCost : " + cost + "\nAvailability : " + availability + "\nTotal : "
                + total);
    }

    @Override
    public boolean execute() {
        availabilityWriter = new ArrayList<>();
        evaluationWriter = new ArrayList<>();
        String[] names = { "Replica", "Whole" };

        createFile(names);

        Content content = SharedData.getContent(3);
        ArrayList<Integer> placementNodes = new ArrayList<>();

        for (int i = 1; i <= Network.size(); i++) {
            double bestEvaluation = Double.MAX_VALUE;
            int bestNode = -1;

            for (int nodeId = 0; nodeId < Network.size(); nodeId++) {
                if (!placementNodes.contains(nodeId)) {
                    ArrayList<Integer> nodes = new ArrayList<>(placementNodes);
                    nodes.add(nodeId);

                    double evaluation = Flooding.getAverageHops(nodes);
                    if (evaluation < bestEvaluation) {
                        bestEvaluation = evaluation;
                        bestNode = nodeId;
                    }
                }
            }

            placementNodes.add(bestNode);
            Flooding.getAverageHops(placementNodes);
            writeFile(i, content);
        }

        closeFile(names);
        return false;
    }

    private static void createFile(String[] names) {
        try {
            for (String name : names) {
                availabilityWriter.add(new PrintWriter(new BufferedWriter(new FileWriter(
                        SharedData.getDirectoryName() + "/Transition_" + name + "_Availability.tsv", false))));
                evaluationWriter.add(new PrintWriter(new BufferedWriter(new FileWriter(
                        SharedData.getDirectoryName() + "/Transition_" + name + "_Evaluation.tsv", false))));
            }
            costWriter = new PrintWriter(
                    new BufferedWriter(new FileWriter(SharedData.getDirectoryName() + "/Transition_Cost.tsv", false)));

        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void writeFile(int totalReplicas, Content content) {
        double totalRequests = content.getPopularity() * (double) totalNodes * (double) users;
        double contentLoad = totalRequests * ((double) content.getSize());
        double totalCost = totalReplicas * content.getSize();

        double value = Flooding.calculateReplicaAvailability(totalReplicas);
        availabilityWriter.get(0).println(totalReplicas + "\t" + value);
        evaluationWriter.get(0).println(totalReplicas + "\t" + ((contentLoad / value) + totalCost));

        value = Flooding.calculateWholeAvailability();
        availabilityWriter.get(1).println(totalReplicas + "\t" + value);
        evaluationWriter.get(1).println(totalReplicas + "\t" + ((contentLoad / value) + totalCost));

        costWriter.println(totalReplicas + "\t" + totalCost);
    }

    private static void closeFile(String[] names) {
        for (int i = 0; i < names.length; i++) {
            availabilityWriter.get(i).close();
            evaluationWriter.get(i).close();
        }
        costWriter.close();
    }
}