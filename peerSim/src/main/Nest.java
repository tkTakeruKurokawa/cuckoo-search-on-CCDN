package main;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;

public class Nest implements Control {
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;
    private static final String PAR_FRACTAL_DIMENSION = "fractalDimension";
    private static double fractalDimension;

    // private int[] egg;
    private Egg egg;
    private double evaluation;
    private Content content;

    private static ArrayList<Integer> availableNodes;
    private static int availableNodeSize;

    public Nest(String prefix) {
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
        fractalDimension = Configuration.getDouble(prefix + "." + PAR_FRACTAL_DIMENSION);
    }

    public Nest(ArrayList<Integer> useNodes, Content content) {
        this.content = content;
        availableNodes = useNodes;
        availableNodeSize = availableNodes.size();
        initializeNest();
    }

    public void setEgg(Egg newEgg) {
        egg = newEgg;
        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), availableNodeSize, content);
    }

    public Egg getEgg() {
        return egg;
    }

    public Egg levyFlight() {
        int nowIndex = SharedData.getRandomInt(availableNodeSize);
        Egg newEgg = new Egg(availableNodes);
        int totalChanges = levyDistribution(availableNodeSize);
        double nowEvaluation = evaluation;
        double newEvaluation = evaluation;

        newEgg.clonePlacementLocation(egg.getLocations());

        for (int changedCount = 0; changedCount < totalChanges; changedCount++) {
            int previousValue;

            if (newEgg.getLocation(nowIndex) == 0) {
                previousValue = 0;
                newEgg.setLocation(nowIndex, 1);
            } else {
                previousValue = 1;
                newEgg.setLocation(nowIndex, 0);
            }

            newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(), availableNodeSize, content);

            if (newEvaluation > nowEvaluation) {
                newEgg.setLocation(nowIndex, previousValue);
            } else {
                nowEvaluation = newEvaluation;
            }

            nowIndex++;
            if (nowIndex == availableNodeSize) {
                nowIndex = 0;
            }
        }

        return (newEvaluation > evaluation) ? null : newEgg;
    }

    public void abandon() {
        Egg newEgg;
        double newEvaluation;

        newEgg = new Egg(availableNodes);
        initializeEgg(newEgg);
        newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(), availableNodeSize, content);

        if (newEvaluation < evaluation) {
            egg = newEgg;
            evaluation = newEvaluation;
        }
    }

    public double getEvaluation() {
        return evaluation;
    }

    private void initializeNest() {
        egg = new Egg(availableNodes);

        initializeEgg(egg);

        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), availableNodeSize, this.content);
    }

    private void initializeEgg(Egg egg) {
        int totalReplicas = SharedData.getRandomInt(availableNodeSize);

        ArrayList<Integer> nodes = new ArrayList<Integer>(availableNodes);
        for (int replicaCount = 0; replicaCount < totalReplicas; replicaCount++) {
            int eggId = SharedData.getRandomInt(nodes.size());
            egg.setLocation(eggId, 1);
            nodes.remove(eggId);
        }
    }

    private int levyDistribution(int totalNodes) {
        int totalChanges;
        do {
            double d = SharedData.getRandomDouble();
            totalChanges = (int) Math.round(Math.pow(d, -1.0 * fractalDimension));
        } while (totalChanges > totalNodes);

        return totalChanges;
    }

    public String getData() {
        String data = "Binary: \n";
        ArrayList<Integer> dataList = egg.getLocations();
        for (Integer integer : dataList) {
            data += String.valueOf(integer);
        }
        data += "\n\nPlacement Nodes: \n";
        dataList = egg.getPlacementNodes();
        dataList.add(originId);
        for (Integer integer : dataList) {
            data += String.valueOf(integer) + ", ";
        }
        data += "\n\nNumber of Replicas: \n";
        data += String.valueOf(dataList.size());
        data += "\n\nEvaluation: \n";
        data += String.valueOf(evaluation);
        data += "\n\n";

        ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), availableNodeSize, content);

        return data;
    }

    @Override
    public boolean execute() {
        return false;
    }
}