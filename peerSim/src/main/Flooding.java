package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

public class Flooding {
    private static ArrayList<Integer> availableNodes;
    private static Queue<Integer> queue;
    private static HashMap<Integer, Integer> hop;

    private static int count;
    private static int total;

    public static double getAverageHop(ArrayList<Integer> nodes) {
        initialize(nodes);

        while (Objects.nonNull(queue.peek())) {
            ReplicaServer node = SharedData.getNode(queue.poll());
            int nodeId = node.getIndex();
            Link link = SharedData.getLink(node);
            // System.out.printf("%d(%d) : ", nodeId, hop.get(nodeId));

            for (int index = 0; index < link.degree(); index++) {
                ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                if (!neighbor.getServerState()) {
                    continue;
                } else if (!availableNodes.contains(neighborId)) {
                    // System.out.printf("%d, ", neighborId);
                    availableNodes.add(neighborId);
                    queue.add(neighborId);
                    hop.put(neighborId, hop.get(nodeId) + 1);
                }
            }
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + availableNodes.size());
        return calculateAverage();
    }

    public static ArrayList<Integer> getReachableNodes() {
        ArrayList<Integer> nodes = new ArrayList<Integer>(Arrays.asList(57));
        initialize(nodes);

        while (Objects.nonNull(queue.peek())) {
            ReplicaServer node = SharedData.getNode(queue.poll());
            int nodeId = node.getIndex();
            Link link = SharedData.getLink(node);
            // System.out.printf("%d: ", nodeId);

            for (int index = 0; index < link.degree(); index++) {
                ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                if (!neighbor.getServerState()) {
                    continue;
                } else if (!availableNodes.contains(neighborId)) {
                    // System.out.printf("%d, ", neighborId);
                    availableNodes.add(neighborId);
                    queue.add(neighborId);
                }
            }
            // System.out.println();
        }

        // System.out.println("Reachable Nodes: " + availableNodes.size());
        availableNodes.remove(0);
        return availableNodes;
    }

    private static void initialize(ArrayList<Integer> nodes) {
        availableNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();

        for (int i = 0; i < nodes.size(); i++) {
            int nodeId = nodes.get(i);
            availableNodes.add(nodeId);
            queue.add(nodeId);
            hop.put(nodeId, 0);
        }
    }

    private static double calculateAverage() {
        total = 0;
        count = 0;
        for (HashMap.Entry<Integer, Integer> entry : hop.entrySet()) {
            total += entry.getValue();
            count++;
        }

        return ((double) total / (double) count);
        // return (double) total;
    }

    public static String getData() {
        return "Count: " + String.valueOf(count) + "\nTotal Hops: " + String.valueOf(total) + "\nAverage Hops: "
                + String.valueOf((double) total / (double) count) + "\n";
    }
}