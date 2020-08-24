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

    private static String path;

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

    public static int getHops(int nodeId, int algorithmId, Content content) {
        initialize(nodeId, content);

        while (Objects.nonNull(queue.peek())) {
            ReplicaServer node = SharedData.getNode(queue.poll());
            int nodeIndex = node.getIndex();

            if (node.contains(algorithmId, content.getContentId())) {
                System.out.println("Node " + node.getIndex() + " Having Content");
                System.out.println("Having Content ID: " + node.showStorage(algorithmId));
                return hop.get(nodeIndex);
            }

            Link link = SharedData.getLink(node);
            path += "hop: " + hop.get(nodeIndex) + ", Node " + nodeIndex + " → ";
            // System.out.printf("%d(%d) : ", nodeIndex, hop.get(nodeIndex));

            for (int index = 0; index < link.degree(); index++) {
                ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                if (!neighbor.getServerState()) {
                    continue;
                } else if (!availableNodes.contains(neighborId)) {
                    path += "" + neighborId + ", ";
                    // System.out.printf("%d, ", neighborId);
                    availableNodes.add(neighborId);
                    queue.add(neighborId);
                    hop.put(neighborId, hop.get(nodeIndex) + 1);
                }
            }
            path += "\n";
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + availableNodes.size());
        return -1;
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

    private static void initialize(int nodeId, Content content) {
        availableNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();
        path = "";

        availableNodes.add(nodeId);
        queue.add(nodeId);
        hop.put(nodeId, 0);
        // System.out.println("Start Node ID: " + nodeId);
        path += "Start Node ID: " + availableNodes.get(0) + ", Search Content ID: " + content.getContentId() + "\n";
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

    public static String getPath() {
        return path;
    }
}