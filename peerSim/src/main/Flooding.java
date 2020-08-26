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
    private static HashMap<Integer, Integer> connection;

    private static int count;
    private static int total;

    private static String allPath;
    private static String path;

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
        ArrayList<Integer> nodes = new ArrayList<Integer>(Arrays.asList(originId));
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

    public static int getHops(int nodeId, int algorithmId, Content content) {
        initialize(nodeId, content);

        while (Objects.nonNull(queue.peek())) {
            ReplicaServer node = SharedData.getNode(queue.poll());
            int nodeIndex = node.getIndex();

            if (node.contains(algorithmId, content.getContentId())) {
                // System.out.println("Node " + node.getIndex() + " Having Content " +
                // content.getContentId());
                // System.out.println("Having Content ID: " + node.showStorage(algorithmId));
                updateCapacity(nodeIndex, algorithmId, content.getSize());
                return hop.get(nodeIndex);
            }

            Link link = SharedData.getLink(node);
            allPath += "hop: " + hop.get(nodeIndex) + ", Node " + nodeIndex + " → ";
            // System.out.printf("%d(%d) : ", nodeIndex, hop.get(nodeIndex));

            for (int index = 0; index < link.degree(); index++) {
                ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                // 隣接ノードが故障中，隣接ノードの処理能力が0未満，隣接ノードとのリンクの処理能力が0未満の場合，その隣接ノードは利用できない
                if (!checkAvailability(nodeIndex, neighborId, algorithmId, content.getSize())) {
                    continue;
                } else if (!addedNodes.contains(neighborId)) {
                    allPath += "" + neighborId + ", ";
                    // System.out.printf("%d, ", neighborId);
                    addedNodes.add(neighborId);
                    queue.add(neighborId);
                    hop.put(neighborId, hop.get(nodeIndex) + 1);
                    connection.put(neighborId, nodeIndex);
                }
            }
            allPath += "\n";
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + addedNodes.size());
        return -1;
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

    private static void initialize(int nodeId, Content content) {
        addedNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();
        connection = new HashMap<Integer, Integer>();
        path = "";
        allPath = "";

        addedNodes.add(nodeId);
        queue.add(nodeId);
        hop.put(nodeId, 0);
        // System.out.println("Start Node ID: " + nodeId);
        allPath += "Start Node ID: " + addedNodes.get(0) + ", Search Content ID: " + content.getContentId() + "\n";
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

    private static boolean checkAvailability(int nodeId, int neighborId, int algorithmId, int contentSize) {
        ReplicaServer node = SharedData.getNode(nodeId);
        ReplicaServer neighbor = SharedData.getNode(neighborId);

        if (!node.getServerState()) {
            return false;
        }
        if (!neighbor.getServerState()) {
            return false;
        }
        if (neighbor.getProcessingCapacity(algorithmId) - contentSize < 0) {
            return false;
        }

        Link link = SharedData.getLink(node);
        if (link.getTransmissionCapacity(algorithmId, neighborId) - contentSize < 0) {
            return false;
        }

        return true;
    }

    private static void updateCapacity(Integer nodeId, int algorithmId, int contentSize) {
        ArrayList<Integer> useNodes = new ArrayList<>();

        while (Objects.nonNull(nodeId)) {
            useNodes.add(nodeId);
            path += "" + nodeId + ", ";
            nodeId = connection.get(nodeId);
        }
        path += "\n";

        for (int index = 0; index < useNodes.size(); index++) {
            ReplicaServer nowNode = SharedData.getNode(useNodes.get(index));
            int processingCapacity = nowNode.getProcessingCapacity(algorithmId);
            nowNode.setProcessingCapacity(algorithmId, processingCapacity - contentSize);

            if (index > 0) {
                ReplicaServer oldNode = SharedData.getNode(useNodes.get(index - 1));
                calculateTransmissionCapacity(algorithmId, contentSize, oldNode, nowNode);
            }
            // else {
            // nowNode.setProcessingCapacity(algorithmId, processingCapacity - contentSize);
            // }
        }
    }

    private static void calculateTransmissionCapacity(int algorithmId, int contentSize, ReplicaServer oldNode,
            ReplicaServer nowNode) {
        Link oldToNew = SharedData.getLink(oldNode);
        int nowNodeId = nowNode.getIndex();
        int transmissionCapacity = oldToNew.getTransmissionCapacity(algorithmId, nowNodeId);
        oldToNew.setTransmissionCapacity(algorithmId, nowNodeId, transmissionCapacity - contentSize);

        Link NewToOld = SharedData.getLink(nowNode);
        int oldNodeId = oldNode.getIndex();
        transmissionCapacity = NewToOld.getTransmissionCapacity(algorithmId, oldNodeId);
        NewToOld.setTransmissionCapacity(algorithmId, oldNodeId, transmissionCapacity - contentSize);
    }

    public static String getData() {
        return "Count: " + String.valueOf(count) + "\nTotal Hops: " + String.valueOf(total) + "\nAverage Hops: "
                + String.valueOf((double) total / (double) count) + "\n";
    }

    public static String getPath() {
        return path;
    }

    public static String getAllPath() {
        return allPath;
    }

    @Override
    public boolean execute() {
        return false;
    }
}