package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

import peersim.core.Control;
import peersim.core.Network;

public class Flooding implements Control {
    private static int originId;

    private static ArrayList<Integer> addedNodes;
    private static Queue<Integer> queue;
    private static HashMap<Integer, Integer> hop;
    private static HashMap<Integer, Integer> connection;

    private static int searchedNodes;
    private static int totalHops;

    private static double availability;

    private static String allPath;
    private static String path;

    public Flooding(String prefix) {
        originId = SharedData.getOriginId();
    }

    public static ArrayList<Integer> getReachableNodes() {
        initialize();

        while (Objects.nonNull(queue.peek())) {
            SurrogateServer node = SharedData.getNode(queue.poll());
            // int nodeId = node.getIndex();
            Link link = SharedData.getLink(node);
            // System.out.printf("%d: ", nodeId);

            for (int index = 0; index < link.degree(); index++) {
                SurrogateServer neighbor = (SurrogateServer) link.getNeighbor(index);
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

    public static double getAverageHops(ArrayList<Integer> nodes) {
        initialize(nodes);

        while (Objects.nonNull(queue.peek())) {
            SurrogateServer node = SharedData.getNode(queue.poll());
            int nodeId = node.getIndex();
            Link link = SharedData.getLink(node);
            // System.out.printf("%d(%d) : ", nodeId, hop.get(nodeId));

            for (int index = 0; index < link.degree(); index++) {
                SurrogateServer neighbor = (SurrogateServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                if (!neighbor.getServerState()) {
                    continue;
                } else if (!addedNodes.contains(neighborId)) {
                    // System.out.printf("%d, ", neighborId);
                    addedNodes.add(neighborId);
                    queue.add(neighborId);
                    hop.put(neighborId, hop.get(nodeId) + 1);
                    connection.put(neighborId, nodeId);
                }
            }
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + addedNodes.size());
        return calculateAverage();
    }

    public static double getAvailability(ArrayList<Integer> placementNodes) {
        double totalAvailability = 0.0;
        double totalNodes;

        // System.out.println("########## Replica Nodes ##########");
        // System.out.println(placementNodes.toString());

        ArrayList<Integer> reachableNodes = Flooding.getReachableNodes();
        for (Integer nodeId : reachableNodes) {
            totalAvailability += searchReplica(placementNodes, nodeId);
        }
        totalNodes = reachableNodes.size();

        // for (int nodeId = 0; nodeId < Network.size(); nodeId++) {
        // totalAvailability += searchReplica(placementNodes, nodeId);
        // // System.out.println("Total Availability: " + totalAvailability);
        // // System.out.println("===================================================");
        // }
        // totalNodes = Network.size();

        availability = (totalAvailability / totalNodes);

        // System.out.println("Average Availability: " + (totalAvailability /
        // totalNodes));
        // System.out.println();
        // System.out.println();

        return availability;
    }

    public static int getHops(int nodeIndex, int algorithmId, Content content) {
        initialize(nodeIndex, algorithmId, content);
        int failedHops = 0;

        while (Objects.nonNull(queue.peek())) {
            SurrogateServer node = SharedData.getNode(queue.poll());
            int nodeId = node.getIndex();
            failedHops = hop.get(nodeId);

            if (node.contains(algorithmId, content.getContentId())) {
                // System.out.println("Node " + node.getIndex() + " Having Content " +
                // content.getContentId());
                // System.out.println("Having Content ID: " + node.showStorage(algorithmId));
                updateCapacity(nodeId, algorithmId, content.getSize());
                return hop.get(nodeId);
            }

            Link link = SharedData.getLink(node);
            allPath += "hop: " + hop.get(nodeId) + ", Node " + nodeId + " → ";
            // System.out.printf("%d(%d) : ", nodeId, hop.get(nodeId));

            for (int index = 0; index < link.degree(); index++) {
                SurrogateServer neighbor = (SurrogateServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                // 隣接ノードが故障中，隣接ノードの処理能力が0未満，隣接ノードとのリンクの処理能力が0未満の場合，その隣接ノードは利用できない
                if (!checkNeighbor(nodeId, neighborId, algorithmId, content.getSize())) {
                    continue;
                } else if (!addedNodes.contains(neighborId)) {
                    allPath += "" + neighborId + ", ";
                    // System.out.printf("%d, ", neighborId);
                    addedNodes.add(neighborId);
                    queue.add(neighborId);
                    hop.put(neighborId, hop.get(nodeId) + 1);
                    connection.put(neighborId, nodeId);
                }
            }
            allPath += "\n";
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + addedNodes.size());
        return -1 * failedHops;
    }

    private static void initialize() {
        addedNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();

        addedNodes.add(originId);
        queue.add(originId);
        hop.put(originId, 0);
    }

    private static void initialize(ArrayList<Integer> nodes) {
        addedNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();
        connection = new HashMap<Integer, Integer>();

        for (int i = 0; i < nodes.size(); i++) {
            int nodeId = nodes.get(i);
            if (SharedData.getNode(nodeId).getServerState()) {
                addedNodes.add(nodeId);
                queue.add(nodeId);
                hop.put(nodeId, 1);
            }
        }
    }

    private static void initialize(int nodeId) {
        addedNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        connection = new HashMap<Integer, Integer>();
        hop = new HashMap<Integer, Integer>();

        // if (SharedData.getNode(nodeId).getServerState()) {
        addedNodes.add(nodeId);
        queue.add(nodeId);
        hop.put(nodeId, 1);
        // }
    }

    private static void initialize(int nodeId, int algorithmId, Content content) {
        addedNodes = new ArrayList<Integer>();
        queue = new ArrayDeque<Integer>();
        hop = new HashMap<Integer, Integer>();
        connection = new HashMap<Integer, Integer>();
        path = "";
        allPath = "";

        if (checkNode(nodeId, algorithmId, content.getSize())) {
            addedNodes.add(nodeId);
            queue.add(nodeId);
            hop.put(nodeId, 1);
            allPath += "Start Node ID: " + addedNodes.get(0) + ", Search Content ID: " + content.getContentId() + "\n";
        }
        // System.out.println("Start Node ID: " + nodeId);
    }

    private static double searchReplica(ArrayList<Integer> placementNodes, int nodeIndex) {
        initialize(nodeIndex);

        while (Objects.nonNull(queue.peek())) {
            SurrogateServer node = SharedData.getNode(queue.poll());
            int nodeId = node.getIndex();
            // System.out.printf("%d(%d) : ", nodeId, hop.get(nodeId));

            if (placementNodes.contains(nodeId)) {
                // System.out.println();
                return calculatePathAvailability(nodeId);
            }

            Link link = SharedData.getLink(node);

            for (int index = 0; index < link.degree(); index++) {
                SurrogateServer neighbor = (SurrogateServer) link.getNeighbor(index);
                int neighborId = neighbor.getIndex();

                if (!addedNodes.contains(neighborId)) {
                    // System.out.printf("%d, ", neighborId);
                    addedNodes.add(neighborId);
                    queue.add(neighborId);
                    connection.put(neighborId, nodeId);
                    hop.put(neighborId, hop.get(nodeId) + 1);
                }
            }
            // System.out.println();
        }

        System.exit(0);
        // System.out.println("ERROR: Replica Not Found");
        return -1;
    }

    private static double calculatePathAvailability(int nodeId) {
        double pathAvailability = SharedData.getNode(nodeId).getAvailability();

        int parentId = nodeId;
        // System.out.println(" " + parentId + "(" +
        // SharedData.getNode(parentId).getAvailability() + ")");
        while (Objects.nonNull(connection.get(parentId))) {
            parentId = connection.get(parentId);
            // System.out.println(" " + parentId + "(" +
            // SharedData.getNode(parentId).getAvailability() + ")");
            pathAvailability *= SharedData.getNode(parentId).getAvailability();
        }

        // System.out.println("Path Availability: " + pathAvailability);

        return pathAvailability;
    }

    private static double calculateAverage() {
        totalHops = 0;
        searchedNodes = 0;
        for (HashMap.Entry<Integer, Integer> entry : hop.entrySet()) {
            totalHops += entry.getValue();
            searchedNodes++;
        }

        return ((double) totalHops / (double) searchedNodes);
        // return (double) totalHops;
    }

    private static boolean checkNode(int nodeId, int algorithmId, int contentSize) {
        SurrogateServer node = SharedData.getNode(nodeId);

        if (!node.getServerState()) {
            return false;
        }
        if (node.getProcessingCapacity(algorithmId) - contentSize < 0) {
            SharedData.increaseLackingProcessing(algorithmId);
            return false;
        }

        return true;
    }

    private static boolean checkNeighbor(int nodeId, int neighborId, int algorithmId, int contentSize) {
        SurrogateServer neighbor = SharedData.getNode(neighborId);

        if (!neighbor.getServerState()) {
            return false;
        }
        if (neighbor.getProcessingCapacity(algorithmId) - contentSize < 0) {
            SharedData.increaseLackingProcessing(algorithmId);
            return false;
        }

        SurrogateServer node = SharedData.getNode(nodeId);
        Link link = SharedData.getLink(node);
        if (link.getTransmissionCapacity(algorithmId, neighborId) - contentSize < 0) {
            SharedData.increaseLackingTransmission(algorithmId);
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
            SurrogateServer nowNode = SharedData.getNode(useNodes.get(index));
            int processingCapacity = nowNode.getProcessingCapacity(algorithmId);
            nowNode.setProcessingCapacity(algorithmId, processingCapacity - contentSize);

            if (index > 0) {
                SurrogateServer oldNode = SharedData.getNode(useNodes.get(index - 1));
                calculateTransmissionCapacity(algorithmId, contentSize, oldNode, nowNode);
            }
            // else {
            // nowNode.setProcessingCapacity(algorithmId, processingCapacity - contentSize);
            // }
        }
    }

    private static void calculateTransmissionCapacity(int algorithmId, int contentSize, SurrogateServer oldNode,
            SurrogateServer nowNode) {
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
        return "Number of Searched Nodes: " + searchedNodes + "\nTotal Hops: " + totalHops + "\nAverage Hops: "
                + ((double) totalHops / (double) searchedNodes) + "\nAvailability: " + availability;
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