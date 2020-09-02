package main;

import java.util.ArrayList;
import java.util.Arrays;

public class GreedyAlgorithm {
    private static ArrayList<Integer> availableNodes;
    private static ArrayList<Integer> remainingNodes;
    private static ArrayList<Integer> bestPlaces;

    private static int localBestNodeId;

    public GreedyAlgorithm(String prefix) {
    }

    public static ArrayList<Integer> runSearch(int algorithmId, Content content) {
        if (!initialize(algorithmId, content)) {
            return new ArrayList<Integer>(Arrays.asList(SharedData.getOriginId()));
        }

        double bestEvaluation = ObjectiveFunction.getEvaluation(bestPlaces, content);
        for (int i = 0; i < availableNodes.size(); i++) {
            double nowEvaluation = greedySearch(content);

            if (nowEvaluation >= bestEvaluation) {
                return bestPlaces;
            } else {
                bestEvaluation = nowEvaluation;
                bestPlaces.add(localBestNodeId);
            }
        }

        return bestPlaces;
    }

    private static boolean initialize(int algorithmId, Content content) {
        availableNodes = SharedData.getAvailableNodes(algorithmId, content);

        if (availableNodes.size() == 0) {
            return false;
        }

        remainingNodes = new ArrayList<Integer>(availableNodes);

        bestPlaces = new ArrayList<>();
        bestPlaces.add(SharedData.getOriginId());

        return true;
    }

    private static double greedySearch(Content content) {
        double localBestEvaluation = Double.MAX_VALUE;
        int removeId = -1;
        for (int index = 0; index < remainingNodes.size(); index++) {
            ArrayList<Integer> placementNodes = new ArrayList<Integer>(bestPlaces);
            int nodeId = remainingNodes.get(index);

            placementNodes.add(nodeId);
            double evaluation = ObjectiveFunction.getEvaluation(placementNodes, content);

            if (evaluation < localBestEvaluation) {
                localBestNodeId = nodeId;
                localBestEvaluation = evaluation;
                removeId = index;
            }
        }

        remainingNodes.remove(removeId);

        return localBestEvaluation;
    }
}