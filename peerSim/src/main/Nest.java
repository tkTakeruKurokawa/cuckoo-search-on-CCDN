package main;

import java.util.ArrayList;
import java.util.HashMap;

import peersim.config.Configuration;
import peersim.core.Control;

public class Nest implements Control {
    private static final String PAR_FRACTAL_DIMENSION = "fractalDimension";
    private static double fractalDimension;
    private static final String PAR_TOLERANCE = "tolerance";
    private static double tolerance;

    // private int[] egg;
    private Egg egg;
    private double evaluation;
    private Content content;

    private static ArrayList<Integer> availableNodes;
    private static int availableNodeSize;
    private static int algorithmId;

    private static HashMap<SurrogateServer, Double> candidateNodes;

    public Nest(String prefix) {
        fractalDimension = Configuration.getDouble(prefix + "." + PAR_FRACTAL_DIMENSION);
        tolerance = Configuration.getDouble(prefix + "." + PAR_TOLERANCE);
    }

    public Nest(ArrayList<Integer> useNodes, Content content, int id) {
        this.content = content;
        availableNodes = useNodes;
        availableNodeSize = availableNodes.size();
        algorithmId = id;
        initializeNest();
    }

    public void setEgg(Egg newEgg) {
        egg = newEgg;
        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), content);
    }

    public Egg getEgg() {
        return egg;
    }

    // Greedy的局所最適化
    // public Egg levyFlight() {
    // int nowIndex = SharedData.getRandomIntForCuckoo(availableNodeSize);
    // Egg newEgg = new Egg(availableNodes);
    // int totalChanges = levyDistribution(availableNodeSize);
    // double nowEvaluation = evaluation;
    // double newEvaluation = evaluation;

    // newEgg.clonePlacementLocation(egg.getLocations());

    // for (int changedCount = 0; changedCount < totalChanges; changedCount++) {
    // int previousValue;

    // if (newEgg.getLocation(nowIndex) == 0) {
    // previousValue = 0;
    // newEgg.setLocation(nowIndex, 1);
    // } else {
    // previousValue = 1;
    // newEgg.setLocation(nowIndex, 0);
    // }

    // newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(),
    // content);

    // if (newEvaluation > nowEvaluation) {
    // newEgg.setLocation(nowIndex, previousValue);
    // } else {
    // nowEvaluation = newEvaluation;
    // }

    // nowIndex++;
    // if (nowIndex == availableNodeSize) {
    // nowIndex = 0;
    // }
    // }

    // return (newEvaluation > evaluation) ? null : newEgg;
    // }

    // 完全ランダムな局所最適化
    // public Egg levyFlight() {
    // Egg newEgg = new Egg(availableNodes);
    // double newEvaluation = evaluation;
    // int nowId = SharedData.getRandomIntForCuckoo(availableNodeSize);
    // int totalCandidates = levyDistribution(availableNodeSize);

    // newEgg.clonePlacementLocation(egg.getLocations());

    // ArrayList<Integer> candidateIndices = new ArrayList<>();
    // for (int i = 0; i < totalCandidates; i++) {
    // candidateIndices.add(nowId);
    // newEgg.setLocation(nowId, 0);

    // nowId++;
    // if (nowId == availableNodes.size()) {
    // nowId = 0;
    // }
    // }

    // int totalReplicas = SharedData.getRandomIntForCuckoo(totalCandidates + 1);
    // for (int replicaCount = 0; replicaCount < totalReplicas; replicaCount++) {
    // int candidateId = SharedData.getRandomIntForCuckoo(candidateIndices.size());
    // int eggId = candidateIndices.get(candidateId);
    // newEgg.setLocation(eggId, 1);
    // candidateIndices.remove(candidateId);
    // }

    // newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(),
    // content);

    // return (newEvaluation > evaluation) ? null : newEgg;
    // }

    /* このネストが持つ複製配置状況を元に新しいネストを生成 */
    public Egg levyWalk() {
        Egg newEgg = new Egg();
        double newEvaluation = evaluation;
        newEgg.clonePlacementNodes(egg.getPlacementNodes());

        newEgg.setPlacementNodes(createNewSolution(newEgg.getPlacementNodes(), true));
        // newEgg.setPlacementNodes(createNewSolution(newEgg.getPlacementNodes(),
        // false));

        newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(), content);
        return (newEvaluation < evaluation) ? newEgg : null;
    }

    private ArrayList<Integer> createNewSolution(ArrayList<Integer> placementNodes, boolean isRemove) {
        int totalChanges = levyDistribution();
        int startIndex = SharedData.getRandomIntForCuckoo(placementNodes.size());
        SurrogateServer startNode = SharedData.getNode(placementNodes.get(startIndex));

        ArrayList<Integer> changedNodes = search(startNode, totalChanges);

        ArrayList<Integer> newPlacementNodes = new ArrayList<>(placementNodes);
        if (isRemove) {
            // System.out.println("Now: " + placementNodes.toString());
            // System.out.println("Remove: " + changedNodes.toString());
            for (Integer nodeId : changedNodes) {
                if (newPlacementNodes.contains(nodeId) && nodeId != SharedData.getOriginId()) {
                    newPlacementNodes.remove(nodeId);
                }
            }
            // System.out.println("New: " + newPlacementNodes.toString());
        } else {
            // System.out.println("Now: " + placementNodes.toString());
            // System.out.println("Add: " + changedNodes.toString());
            for (Integer nodeId : changedNodes) {
                int storage = SharedData.getNode(nodeId).getStorageCapacity(algorithmId);
                if (!newPlacementNodes.contains(nodeId) && (storage - content.getSize()) >= 0) {
                    newPlacementNodes.add(nodeId);
                }
            }
            // System.out.println("New: " + newPlacementNodes.toString());
        }

        return (isNewSolutionBetter(placementNodes, newPlacementNodes)) ? newPlacementNodes : placementNodes;
    }

    private ArrayList<Integer> search(SurrogateServer startNode, int totalChanges) {
        // System.out.println("Total Change: " + totalChanges);
        ArrayList<Integer> changedNodes = new ArrayList<>();
        SurrogateServer baseNode = startNode;

        for (int changedCount = 0; changedCount < totalChanges; changedCount++) {
            // System.out.println("Now Change: " + changedCount);
            int steps = levyDistribution();
            double direction = SharedData.getRandomDoubleForCuckoo() * 359.9999999999999999999999999999999999;

            // System.out.println("Steps: " + steps);
            while (steps > 0) {
                candidateNodes = new HashMap<>();

                // System.out.println("start node: " + baseNode.getIndex());

                int srcX = baseNode.getCoordinateX();
                int srcY = baseNode.getCoordinateY();

                // System.out.println("\tSRC Node: " + baseNode.getIndex() + " (" +
                // baseNode.getCoordinateX() + ", "
                // + baseNode.getCoordinateY() + ")");

                Link link = SharedData.getLink(baseNode);
                if (link.degree() == 0) {
                    System.out.println("No Neighbor");
                    System.out.println(link.toString());
                    System.exit(10000);
                }
                for (int neighborId = 0; neighborId < link.degree(); neighborId++) {
                    SurrogateServer neighbor = (SurrogateServer) link.getNeighbor(neighborId);

                    int dstX = neighbor.getCoordinateX();
                    int dstY = neighbor.getCoordinateY();

                    // System.out.println("\tTarget Node: " + neighbor.getIndex() + " (" +
                    // neighbor.getCoordinateX() + ", "
                    // + neighbor.getCoordinateY() + ")");

                    addCandidateNode(neighbor, srcX, srcY, dstX, dstY, direction);
                }
                if (candidateNodes.isEmpty()) {
                    break;
                } else {
                    baseNode = minNode();
                    // System.out.println("next Node:" + baseNode.getIndex());
                    steps--;
                }
            }

            if (!changedNodes.contains(baseNode.getIndex())) {
                changedNodes.add(baseNode.getIndex());
            }
        }

        // System.out.println("Changed Nodes: " + changedNodes.toString());
        // System.out.println("==========================================================");
        return changedNodes;
    }

    private void addCandidateNode(SurrogateServer neighbor, int srcX, int srcY, int dstX, int dstY, double direction) {

        double y = ((double) dstY - srcY);
        double x = ((double) dstX - srcX);

        // 2点間の角度
        double radian = Math.atan2(y, x);
        // 度に変換
        double degree = radian * 180.0d / Math.PI;
        if (y < 0.0d) {
            degree = 360.0d - Math.abs(degree);
        }

        checkRangeAndAdd(neighbor, degree, direction);
    }

    /**
     * direction: 進む方向（角度）
     * 
     * degree: ベースノードとターゲットノードの角度
     * 
     * top: ＋方向の許容範囲
     * 
     * bottom: ー方向の許容範囲
     **/
    private void checkRangeAndAdd(SurrogateServer neighbor, double degree, double direction) {
        double top = degree + tolerance;
        double bottom = degree - tolerance;
        // System.out
        // .println("\tdirection: " + direction + ", degree: " + degree + ", top: " +
        // top + ", bottom: " + bottom);

        double difference;
        if (bottom < direction && direction < top) {
            difference = Math.abs(degree - direction);
            candidateNodes.put(neighbor, difference);
            // System.out.println("\t\tdifference: " + difference);
            return;
        }
        if (bottom < 0.0d) {
            bottom = 360.0d + bottom;
            // System.out.println("\tbottom: " + bottom);
            if (bottom < direction && direction < 360.0d) {
                difference = Math.abs((360.0d - direction) + degree);
                candidateNodes.put(neighbor, difference);
                // System.out.println("\t\tdifference: " + difference);
                return;
            }
        }
        if (top > 360.0d) {
            top = top - 360.0d;
            // System.out.println("\ttop: " + top);
            if (0.0d < direction && direction < top) {
                difference = Math.abs((360.0d - degree) + direction);
                candidateNodes.put(neighbor, difference);
                // System.out.println("\t\tdifference: " + difference);
                return;
            }
        }
    }

    private SurrogateServer minNode() {
        double minValue = Double.MAX_VALUE;
        SurrogateServer minNode = null;

        for (HashMap.Entry<SurrogateServer, Double> entry : candidateNodes.entrySet()) {
            if (entry.getValue() < minValue) {
                minValue = entry.getValue();
                minNode = entry.getKey();
            }
        }

        return minNode;
    }

    private boolean isNewSolutionBetter(ArrayList<Integer> placementNodes, ArrayList<Integer> newPlacementNodes) {
        double evaluation = ObjectiveFunction.getEvaluation(placementNodes, content);
        // System.out.println(" Now: " + evaluation);
        double newEvaluation = ObjectiveFunction.getEvaluation(newPlacementNodes, content);
        // System.out.println(" New: " + newEvaluation);
        // System.out.println("==========================================================");

        return (newEvaluation < evaluation) ? true : false;
    }

    public void abandon() {
        Egg newEgg;
        double newEvaluation;

        newEgg = new Egg();
        initializeEgg(newEgg);
        newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(), content);

        if (newEvaluation < evaluation) {
            egg = newEgg;
            evaluation = newEvaluation;
        }
    }

    public double getEvaluation() {
        return evaluation;
    }

    private void initializeNest() {
        egg = new Egg();

        initializeEgg(egg);

        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), this.content);
    }

    private void initializeEgg(Egg egg) {
        int totalReplicas = SharedData.getRandomIntForCuckoo(availableNodeSize + 1);

        ArrayList<Integer> nodes = new ArrayList<Integer>(availableNodes);
        for (int replicaCount = 0; replicaCount < totalReplicas; replicaCount++) {
            int index = SharedData.getRandomIntForCuckoo(nodes.size());
            egg.addPlacementNode(nodes.get(index));
            nodes.remove(index);
        }

        egg.addPlacementNode(SharedData.getOriginId());
    }

    private int levyDistribution() {
        int totalChanges;
        do {
            double d = SharedData.getRandomDoubleForCuckoo();
            totalChanges = (int) Math.round(Math.pow(d, -1.0 * fractalDimension));
        } while (totalChanges > availableNodeSize || totalChanges < 0);

        return totalChanges;
    }

    public String getData() {
        String data = "\nPlacement Nodes: \n";
        ArrayList<Integer> dataList = egg.getPlacementNodes();
        for (Integer integer : dataList) {
            data += String.valueOf(integer) + ", ";
        }
        data += "\nNumber of Replicas: \n";
        data += String.valueOf(dataList.size());
        data += "\nNumber of Available Nodes: \n";
        data += String.valueOf(availableNodeSize);
        data += "\nEvaluation: \n";
        data += String.valueOf(evaluation);
        data += "\n";

        ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), content);

        return data;
    }

    @Override
    public boolean execute() {
        return false;
    }
}