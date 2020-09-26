package main;

import java.util.ArrayList;

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

    public ObjectiveFunction(String prefix) {
        totalNodes = Network.size() - 1;
        users = Configuration.getInt(prefix + "." + PAR_USERS);
        type = Configuration.getInt(prefix + "." + PAR_TYPE);
        // availabilityCoefficient = Configuration.getDouble(prefix + "." +
        // PAR_FAILURE_RATE_COEFFICIENT);
    }

    public static double getEvaluation(ArrayList<Integer> placementNodes, Content content) {
        cost = calculateCost(placementNodes.size(), content);

        switch (type) {
            case 0:
                availability = calculateAvailability(placementNodes, content);
                total = cost + availability;
                break;

            case 1:
                accessRate = calculateAccessRate(placementNodes, content);
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

        return (totalRequests * ((double) content.getSize())) / Flooding.getAvailability(placementNodes);
    }

    public static String getData() {
        return ("Access : " + accessRate + "\nCost : " + cost + "\nAvailability : " + availability + "\nTotal : "
                + total);
    }

    @Override
    public boolean execute() {
        return false;
    }

}