package main;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class ObjectiveFunction implements Control {
    private static final String PAR_USERS = "users";
    private static int users;
    private static final String PAR_FAILURE_RATE_COEFFICIENT = "availabilityCoefficient";
    private static double availabilityCoefficient;

    private static int totalNodes;

    private static double accessRate;
    private static double cost;
    private static double availability;

    public ObjectiveFunction(String prefix) {
        totalNodes = Network.size() - 1;
        users = Configuration.getInt(prefix + "." + PAR_USERS);
        availabilityCoefficient = Configuration.getDouble(prefix + "." + PAR_FAILURE_RATE_COEFFICIENT);
    }

    public static double getEvaluation(ArrayList<Integer> placementNodes, Content content) {
        accessRate = calculateAccessRate(placementNodes, content);
        // cost = Math.log(calculateCost(placementNodes.size(), content));
        cost = calculateCost(placementNodes.size(), content);
        availability = calculateAvailability(placementNodes, content);
        double total = cost + availability;
        // double total = accessRate + cost - availability;
        // double total = (accessRate / availability) + cost;
        // double total = (accessRate + cost) / availability;

        // System.out.println("Number of Replica: " + placementNodes.size() + ",
        // Access:" + accessRate + ", Cost: " + cost
        // + ", Failure: " + availability);
        // System.out.println("Total = " + total);
        // System.out.println();
        return total;
    }

    // 合計ホップ数: [0, 311]
    // 平均ホップ数: [0.0, 3.11]
    // 人気度： [0.0008113572102320679, 0.3230071232736677]
    // 合計： [0.0, 1.004552153381106547]
    private static double calculateAccessRate(ArrayList<Integer> placementNodeIndices, Content content) {
        double totalRequests = content.getPopularity() * (double) totalNodes * (double) users;
        return Flooding.getAverageHops(placementNodeIndices) * totalRequests * ((double) content.getSize());

        // return Flooding.getAverageHop(placementNodeIndices) / (1.0 -
        // content.getPopularity())
        // * ((double) content.getSize());
    }

    // 合計コンテンツ数： [1, 100] ＊オリジンサーバのコンテンツも含めるため1以上
    // コンテンツサイズ： [1, 100]
    // 合計： [1, 10000]
    private static double calculateCost(int totalReplicas, Content content) {
        return totalReplicas * content.getSize();
    }

    // 故障確率: [0.001095430219427455, 0.005380420859253009]
    private static double calculateAvailability(ArrayList<Integer> placementNodes, Content content) {
        // double totalAvailability = 0.0;
        // double totalReplicas = (double) placementNodes.size();

        // for (Integer nodeId : placementNodes) {
        // totalAvailability += SharedData.getNode(nodeId).getAvailability();
        // }
        double totalRequests = content.getPopularity() * (double) totalNodes * (double) users;

        return (totalRequests * ((double) content.getSize())) / Flooding.getAvailability();
        // return totalRequests / Flooding.getAvailability();
        // return availabilityCoefficient * Flooding.getAvailability();
        // return availabilityCoefficient * (1.0 - (totalAvailability / totalReplicas));
    }

    public static String getData() {
        return ("Access : " + String.valueOf(accessRate) + "\nCost : " + String.valueOf(cost) + "\nAvailability : "
                + String.valueOf(availability) + "\nTotal : " + String.valueOf(accessRate + cost + availability));
    }

    @Override
    public boolean execute() {
        return false;
    }

}