package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

import peersim.config.Configuration;
import peersim.core.Control;

public class Flooding implements Control {
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;

    private static ArrayList<Integer> addedNodes;
    private static Queue<Integer> queue;
    private static HashMap<Integer, Integer> hop;

    private static int count;
    private static int total;

    public Flooding(String prefix) {
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
    }

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
                } else if (!addedNodes.contains(neighborId)) {
                    // System.out.printf("%d, ", neighborId);
                    addedNodes.add(neighborId);
                    queue.add(neighborId);
                    hop.put(neighborId, hop.get(nodeId) + 1);
                }
            }
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + addedNodes.size());
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
                } else if (!addedNodes.contains(neighborId)) {
                    // System.out.printf("%d, ", neighborId);
                    addedNodes.add(neighborId);
                    queue.add(neighborId);
                }
            }
            // System.out.println();
        }

        // System.out.println("Reachable Nodes: " + addedNodes.size());
        addedNodes.remove(0);
        return addedNodes;
    }

    private static void initialize(ArrayList<Integer> nodes) {
        addedNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();

        for (int i = 0; i < nodes.size(); i++) {
            int nodeId = nodes.get(i);
            addedNodes.add(nodeId);
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

        // return ((double) total / (double) count);
        return (double) total;
    }

    public static String getData() {
        return "Count: " + String.valueOf(count) + "\nTotal Hops: " + String.valueOf(total) + "\nAverage Hops: "
                + String.valueOf((double) total / (double) count) + "\n";
    }

    @Override
    public boolean execute() {
        return false;
    }
}