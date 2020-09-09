package main;

import java.util.ArrayList;
import java.util.Arrays;

import peersim.config.Configuration;
import peersim.core.Control;

public class RandomAlgorithm implements Control {
    private static final String PAR_TOTAL_TRIALS = "totalTrials";
    private static int totalTrials;

    private static ArrayList<Integer> availableNodes;
    private static ArrayList<Integer> placementNodes;

    public RandomAlgorithm(String prefix) {
        totalTrials = Configuration.getInt(prefix + "." + PAR_TOTAL_TRIALS);
    }

    public static ArrayList<Integer> runSearch(int algorithmId, Content content) {
        if (!initialize(algorithmId, content)) {
            return new ArrayList<Integer>(Arrays.asList(SharedData.getOriginId()));
        }

        double bestEvaluation = Double.MAX_VALUE;
        ArrayList<Integer> bestPlaces = new ArrayList<>();
        for (int i = 0; i < totalTrials; i++) {
            double nowEvaluation = randomSearch(content);

            if (nowEvaluation < bestEvaluation) {
                bestEvaluation = nowEvaluation;
                bestPlaces = new ArrayList<Integer>(placementNodes);
            }
        }

        return bestPlaces;
    }

    private static boolean initialize(int algorithmId, Content content) {
        availableNodes = SharedData.getAvailableNodes(algorithmId, content);

        if (availableNodes.size() == 0) {
            return false;
        }

        return true;
    }

    private static double randomSearch(Content content) {
        placementNodes = new ArrayList<>();

        setPlace(SharedData.getRandomIntForRandom(availableNodes.size() + 1));
        placementNodes.add(SharedData.getOriginId());

        return ObjectiveFunction.getEvaluation(placementNodes, content);
    }

    private static void setPlace(int totalReplicas) {
        ArrayList<Integer> nodes = new ArrayList<Integer>(availableNodes);
        for (int replicaCount = 0; replicaCount < totalReplicas; replicaCount++) {
            int index = SharedData.getRandomIntForRandom(nodes.size());

            int nodeId = nodes.get(index);
            placementNodes.add(nodeId);
            nodes.remove(index);
        }
    }

    @Override
    public boolean execute() {
        return false;
    }
}