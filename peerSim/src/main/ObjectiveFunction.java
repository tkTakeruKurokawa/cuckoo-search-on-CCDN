package main;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;

public class ObjectiveFunction implements Control {
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;
    private static final String PAR_TOTAL_NODES = "totalNodes";
    private static int totalNodes;
    private static final String PAR_USERS = "users";
    private static int users;
    // private static final String PAR_ACCESS_RATE_COEFFICIENT =
    // "accessRateCoefficient";
    // private static double accessRateCoefficient;
    private static final String PAR_FAILURE_RATE_COEFFICIENT = "failureRateCoefficient";
    private static double failureRateCoefficient;

    private static double accessRate;
    private static double cost;
    private static double failureRate;

    public ObjectiveFunction(String prefix) {
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
        totalNodes = Configuration.getInt(prefix + "." + PAR_TOTAL_NODES);
        users = Configuration.getInt(prefix + "." + PAR_USERS);
        // accessRateCoefficient = Configuration.getDouble(prefix + "." +
        // PAR_ACCESS_RATE_COEFFICIENT);
        failureRateCoefficient = Configuration.getDouble(prefix + "." + PAR_FAILURE_RATE_COEFFICIENT);
    }

    public static double getEvaluation(ArrayList<Integer> placementNodes, int availableNodes, Content content) {
        placementNodes.add(originId);

        accessRate = calculateAccessRate(placementNodes, availableNodes, content);
        // cost = Math.log(calculateCost(placementNodes.size(), content));
        cost = calculateCost(placementNodes.size(), content);
        failureRate = calculateFailureRate(placementNodes);
        double total = accessRate + cost + failureRate;

        // System.out.println("Number of Replica: " + placementNodes.size() + ",
        // Access:" + accessRate + ", Cost: " + cost
        // + ", Failure: " + failureRate);
        // System.out.println("Total = " + total);
        // System.out.println();
        return total;
    }

    // 合計ホップ数: [0, 311]
    // 平均ホップ数: [0.0, 3.11]
    // 人気度： [0.0008113572102320679, 0.3230071232736677]
    // 合計： [0.0, 1.004552153381106547]
    private static double calculateAccessRate(ArrayList<Integer> placementNodeIndices, int availableNodes,
            Content content) {
        // double totalRequests = content.getPopularity() * ((double) totalNodes) *
        // ((double) users);
        double totalRequests = content.getPopularity() * ((double) availableNodes);
        return Flooding.getAverageHop(placementNodeIndices) * content.getPopularity();
        // return Flooding.getAverageHop(placementNodeIndices) / (1.0 -
        // content.getPopularity());
        // return accessRateCoefficient * Flooding.getAverageHop(placementNodeIndices) *
        // totalRequests;
    }

    // 合計コンテンツ数： [1, 100] ＊オリジンサーバのコンテンツも含めるため1以上
    // コンテンツサイズ： [1, 100]
    // 合計： [1, 10000]
    private static double calculateCost(int totalReplicas, Content content) {
        return totalReplicas * content.getSize();
    }

    // 故障確率: [0.001095430219427455, 0.005380420859253009]
    private static double calculateFailureRate(ArrayList<Integer> placementNodes) {
        double totalFailureRate = 0.0;
        double totalReplicas = (double) placementNodes.size();

        for (Integer nodeId : placementNodes) {
            totalFailureRate += SharedData.getNode(nodeId).getFailureRate();
        }

        return failureRateCoefficient * (totalFailureRate / totalReplicas);
    }

    public static String getData() {
        return ("Access : " + String.valueOf(accessRate) + "\nCost : " + String.valueOf(cost) + "\nFailure : "
                + String.valueOf(failureRate) + "\nTotal : " + String.valueOf(accessRate + cost + failureRate));
    }

    @Override
    public boolean execute() {
        return false;
    }

}