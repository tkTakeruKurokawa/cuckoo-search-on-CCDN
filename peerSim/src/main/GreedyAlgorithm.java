package main;

import java.util.ArrayList;
import java.util.Arrays;

import peersim.cdsim.CDState;
import peersim.core.Control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class GreedyAlgorithm implements Control {
    private static ArrayList<Integer> availableNodes;
    private static ArrayList<Integer> remainingNodes;
    private static ArrayList<Integer> bestPlaces;

    private static int localBestNodeId;

    private static PrintWriter writer;

    public GreedyAlgorithm(String prefix) {
        try {
            writer = new PrintWriter(
                    new BufferedWriter(new FileWriter(SharedData.getDirectoryName() + "/Greedy.txt", false)));
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public static ArrayList<Integer> runSearch(int algorithmId, Content content) {
        if (!initialize(algorithmId, content)) {
            return new ArrayList<Integer>(Arrays.asList(SharedData.getOriginId()));
        }

        double bestEvaluation = ObjectiveFunction.getEvaluation(bestPlaces, content);
        for (int i = 0; i < availableNodes.size(); i++) {
            double nowEvaluation = greedySearch(content);

            if (nowEvaluation >= bestEvaluation) {
                writeFile(content);
                // System.out.println("Greedy: " + bestEvaluation);
                return bestPlaces;
            } else {
                bestEvaluation = nowEvaluation;
                bestPlaces.add(localBestNodeId);
            }
        }

        // System.out.println("Greedy: " + bestEvaluation);
        writeFile(content);
        return bestPlaces;
    }

    public static void closeFile() {
        writer.close();
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

        if (removeId != -1) {
            remainingNodes.remove(removeId);
        }

        return localBestEvaluation;
    }

    private static void writeFile(Content content) {
        ObjectiveFunction.getEvaluation(bestPlaces, content);
        writer.println("==========================================================================================");
        writer.println("Content ID: " + content.getContentId() + ", Popularity: " + content.getPopularity() + ", Size: "
                + content.getSize() + ", Cycle: " + CDState.getCycle() + "\n");
        writer.println("Placement Nodes:");
        writer.println(bestPlaces.toString());
        writer.println("Number of Replicas");
        writer.println(bestPlaces.size());
        writer.println("Number of Available Nodes");
        writer.println(availableNodes.size());
        writer.println("\nFlooding Result");
        writer.println(Flooding.getData());
        writer.println("\nObjective Function Result:");
        writer.println(ObjectiveFunction.getData());
        writer.println("==========================================================================================");
        writer.println("\n\n");
    }

    @Override
    public boolean execute() {
        return false;
    }
}