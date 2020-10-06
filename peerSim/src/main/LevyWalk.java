package main;

import java.util.ArrayList;
import java.util.HashMap;

import peersim.config.Configuration;
import peersim.core.Control;

public class LevyWalk implements Control {
    private static final String PAR_FRACTAL_DIMENSION = "fractalDimension";
    private static double fractalDimension;
    private static final String PAR_TOLERANCE = "tolerance";
    private static double tolerance;
    private static final String PAR_TYPE = "type";
    private static int type;

    private static HashMap<SurrogateServer, Double> candidateNodes;
    private static Content content;
    private static int availableNodeSize;
    private static int algorithmId;

    public LevyWalk(String prefix) {
        fractalDimension = Configuration.getDouble(prefix + "." + PAR_FRACTAL_DIMENSION);
        tolerance = Configuration.getDouble(prefix + "." + PAR_TOLERANCE);
        type = Configuration.getInt(prefix + "." + PAR_TYPE);
    }

    public static Egg runWalk(Egg egg, Content content, int availableNodeSize, int algorithmId) {
        initialize(content, availableNodeSize, algorithmId);

        Egg newEgg = new Egg();
        newEgg.clonePlacementNodes(egg.getPlacementNodes());

        switch (type) {
            case 0:
                newEgg.setPlacementNodes(createNewSolution(newEgg.getPlacementNodes(), true));
                break;

            case 1:
                newEgg.setPlacementNodes(createNewSolution(newEgg.getPlacementNodes(), false));
                break;

            case 2:
                newEgg.setPlacementNodes(createNewSolution(newEgg.getPlacementNodes(), true));
                newEgg.setPlacementNodes(createNewSolution(newEgg.getPlacementNodes(), false));
                break;

            default:
                System.exit(0);
                break;
        }

        return newEgg;
    }

    private static void initialize(Content content, int availableNodeSize, int algorithmId) {
        LevyWalk.content = content;
        LevyWalk.availableNodeSize = availableNodeSize;
        LevyWalk.algorithmId = algorithmId;
    }

    private static int levyDistribution() {
        int totalChanges;
        do {
            double d = SharedData.getRandomDoubleForCuckoo();
            totalChanges = (int) Math.round(Math.pow(d, -1.0 * fractalDimension));
        } while (totalChanges > availableNodeSize || totalChanges < 0);

        return totalChanges;
    }

    private static ArrayList<Integer> createNewSolution(ArrayList<Integer> placementNodes, boolean isRemove) {
        int totalChanges = SharedData.getRandomIntForCuckoo(placementNodes.size());
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

    private static ArrayList<Integer> search(SurrogateServer startNode, int totalChanges) {
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

    private static void addCandidateNode(SurrogateServer neighbor, int srcX, int srcY, int dstX, int dstY,
            double direction) {

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
    private static void checkRangeAndAdd(SurrogateServer neighbor, double degree, double direction) {
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

    private static SurrogateServer minNode() {
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

    private static boolean isNewSolutionBetter(ArrayList<Integer> placementNodes,
            ArrayList<Integer> newPlacementNodes) {
        double evaluation = ObjectiveFunction.getEvaluation(placementNodes, content);
        // System.out.println(" Now: " + evaluation);
        double newEvaluation = ObjectiveFunction.getEvaluation(newPlacementNodes, content);
        // System.out.println(" New: " + newEvaluation);

        return (newEvaluation < evaluation) ? true : false;
    }

    @Override
    public boolean execute() {
        return false;
    }
}
