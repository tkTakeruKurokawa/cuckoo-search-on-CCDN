package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

import peersim.core.Control;

public class Flooding implements Control {
    private static int originId;

    private static ArrayList<Integer> addedNodes;
    private static Queue<Integer> queue;
    private static HashMap<Integer, Integer> hop;
    private static HashMap<Integer, Integer> connection;
    private static HashMap<Integer, ArrayList<Integer>> coverNodes;

    private static double availability;

    private static int searchedNodes;
    private static int totalHops;

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
                    setCoverNodes(neighborId);
                }
            }
            // System.out.println();
        }

        // System.out.println("Total Nodes: " + addedNodes.size());
        // calculateAvailability();
        calculateReplicaAvailability(nodes.size());
        // calculateWholeAvailability();
        return calculateAverage();
    }

    public static double getRemainingStorage(ArrayList<Integer> placementNodes, int algorithmId) {
        double total = 0;
        for (Integer nodeId : placementNodes) {
            double remainingStorage = SharedData.getNode(nodeId).getStorageCapacity(algorithmId);
            total += (remainingStorage / 100.0);
        }

        return total;
    }

    public static double getAvailability() {
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
        coverNodes = new HashMap<Integer, ArrayList<Integer>>();

        for (int i = 0; i < nodes.size(); i++) {
            int nodeId = nodes.get(i);
            if (SharedData.getNode(nodeId).getServerState()) {
                addedNodes.add(nodeId);
                queue.add(nodeId);
                hop.put(nodeId, 0);
                coverNodes.put(nodeId, new ArrayList<Integer>());
            }
        }
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

    private static void setCoverNodes(Integer neighborId) {
        int parentId = neighborId;
        while (Objects.nonNull(connection.get(parentId))) {
            parentId = connection.get(parentId);
        }

        ArrayList<Integer> coveringNodes = coverNodes.get(parentId);
        coveringNodes.add(neighborId);
        coverNodes.replace(parentId, coveringNodes);
    }

    private static void calculateAvailability() {
        double totalAvailability = 0.0;

        ArrayList<Integer> reachableNodes = Flooding.getReachableNodes();
        for (Integer nodeId : reachableNodes) {
            double pathAvailability = SharedData.getNode(nodeId).getAvailability();

            int parentId = nodeId;
            while (Objects.nonNull(connection.get(parentId))) {
                parentId = connection.get(parentId);
                pathAvailability *= SharedData.getNode(parentId).getAvailability();
            }
            totalAvailability += pathAvailability;
        }

        double totalNodes = reachableNodes.size();

        availability = totalAvailability / totalNodes;
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
        return "Number of Searched Nodes: " + String.valueOf(searchedNodes) + "\nTotal Hops: "
                + String.valueOf(totalHops) + "\nAverage Hops: "
                + String.valueOf((double) totalHops / (double) searchedNodes) + "\nAvailability: " + availability;
    }

    public static String getPath() {
        return path;
    }

    public static String getAllPath() {
        return allPath;
    }

    public static double calculateReplicaAvailability(double totalReplicas) {
        double totalAvailability = 0;
        for (HashMap.Entry<Integer, ArrayList<Integer>> coverNode : coverNodes.entrySet()) {
            SurrogateServer replicaNode = SharedData.getNode(coverNode.getKey());

            double totalAvailabilityPerReplica = replicaNode.getAvailability();

            for (Integer coveredNodeId : coverNode.getValue()) {
                SurrogateServer node = SharedData.getNode(coveredNodeId);

                double totalAvailabilityPerPath = 1.0;
                totalAvailabilityPerPath *= node.getAvailability();

                Integer parentId = coveredNodeId;
                while (Objects.nonNull(connection.get(parentId))) {
                    parentId = connection.get(parentId);
                    totalAvailabilityPerPath *= SharedData.getNode(parentId).getAvailability();
                }
                totalAvailabilityPerReplica += totalAvailabilityPerPath;
            }
            totalAvailability += totalAvailabilityPerReplica / ((double) coverNode.getValue().size() + 1);
        }

        availability = totalAvailability / totalReplicas;
        return availability;
    }

    public static double calculateWholeAvailability() {
        double totalAvailability = 0.0;

        ArrayList<Integer> reachableNodes = Flooding.getReachableNodes();
        for (Integer nodeId : reachableNodes) {
            double pathAvailability = SharedData.getNode(nodeId).getAvailability();

            int parentId = nodeId;
            while (Objects.nonNull(connection.get(parentId))) {
                parentId = connection.get(parentId);
                pathAvailability *= SharedData.getNode(parentId).getAvailability();
            }
            totalAvailability += pathAvailability;
        }

        double totalNodes = reachableNodes.size();

        availability = totalAvailability / totalNodes;
        return availability;
    }

    @Override
    public boolean execute() {
        return false;
    }
}