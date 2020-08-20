package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import peersim.config.Configuration;
import peersim.core.Control;

public class CuckooSearch implements Control {
    private static final String PAR_MAX_GENERATION = "maxGeneration";
    private static int maxGeneration;
    private static final String PAR_TOTAL_NESTS = "totalNests";
    private static int totalNests;
    private static final String PAR_IMPROVEMENT_RATE = "improvementRate";
    private static double improvementRate;
    private static final String PAR_ABANDON_RATE = "abandonRate";
    private static double abandonRate;

    private static ArrayList<Nest> nestSet;
    private static ArrayList<Integer> availableNodes;

    public CuckooSearch(String prefix) {
        maxGeneration = Configuration.getInt(prefix + "." + PAR_MAX_GENERATION);
        totalNests = Configuration.getInt(prefix + "." + PAR_TOTAL_NESTS);
        improvementRate = Configuration.getDouble(prefix + "." + PAR_IMPROVEMENT_RATE);
        abandonRate = Configuration.getDouble(prefix + "." + PAR_ABANDON_RATE);
    }

    private static void initializeNestSet(Content content) {
        ArrayList<Integer> reachableNodes = Flooding.getReachableNodes();
        availableNodes = new ArrayList<Integer>();

        for (int i = 0; i < reachableNodes.size(); i++) {
            ReplicaServer node = SharedData.getNode(reachableNodes.get(i));
            if (node.getStorageCapacity(0) - content.getSize() >= 0) {
                availableNodes.add(node.getIndex());
            }
        }

        nestSet = new ArrayList<Nest>();
        for (int i = 0; i < totalNests; i++) {
            nestSet.add(new Nest(availableNodes, content));
        }

        sort();
    }

    public static void runSearch(Content content) {
        initializeNestSet(content);

        // for (int i = 0; i < nestSet.size(); i++) {
        // System.out.println("\t" + nestSet.get(i).getEvaluation());
        // }
        for (int generation = 0; generation < maxGeneration; generation++) {
            smartCuckoo();
            randomCuckoo();
            abandon();
        }

        for (int i = 0; i < nestSet.size(); i++) {
            System.out.println(nestSet.get(i).getEvaluation());
        }
        System.out.println();

        Nest bestNest = nestSet.get(0);
        System.out.println(bestNest.getData());
        System.out.println(Flooding.getData());
        System.out.println(ObjectiveFunction.getData());
    }

    private static void smartCuckoo() {
        int totalImprovements = (int) Math.round(((double) totalNests) * improvementRate);

        for (int nestId = 0; nestId < totalImprovements; nestId++) {
            Nest nest = nestSet.get(nestId);
            runLevyFlight(nest, nest);
        }

        sort();
    }

    private static void randomCuckoo() {
        Nest bestNest = nestSet.get(0);
        Nest randomNest = nestSet.get(SharedData.getRandomInt(nestSet.size()));

        runLevyFlight(bestNest, randomNest);

        sort();
    }

    private static void abandon() {
        int totalAbandon = (int) Math.round(((double) totalNests) * abandonRate);
        int nestSize = nestSet.size() - 1;

        for (int nestId = nestSize; nestId > nestSize - totalAbandon; nestId--) {
            Nest nest = nestSet.get(nestId);
            nest.abandon();
        }

        sort();
    }

    private static void runLevyFlight(Nest baseNest, Nest improvedNest) {
        Egg newEgg = baseNest.levyFlight();

        if (Objects.nonNull(newEgg)) {
            improvedNest.setEgg(newEgg);
        }
    }

    private static void sort() {
        Collections.sort(nestSet, new Compare());
    }

    @Override
    public boolean execute() {
        return false;
    }
}

final class Compare implements Comparator<Nest> {

    @Override
    public int compare(Nest o1, Nest o2) {
        return Double.compare(o1.getEvaluation(), o2.getEvaluation());
    }
}
