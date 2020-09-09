package main;

import java.util.ArrayList;

import peersim.config.Configuration;
import peersim.core.Control;

public class Nest implements Control {
    private static final String PAR_FRACTAL_DIMENSION = "fractalDimension";
    private static double fractalDimension;

    // private int[] egg;
    private Egg egg;
    private double evaluation;
    private Content content;

    private static ArrayList<Integer> availableNodes;
    private static int availableNodeSize;

    public Nest(String prefix) {
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
    public Egg levyFlight() {
        Egg newEgg = new Egg(availableNodes);
        double newEvaluation = evaluation;
        int nowId = SharedData.getRandomIntForCuckoo(availableNodeSize);
        int totalCandidates = levyDistribution(availableNodeSize);

        newEgg.clonePlacementLocation(egg.getLocations());

        ArrayList<Integer> candidateIndices = new ArrayList<>();
        for (int i = 0; i < totalCandidates; i++) {
            candidateIndices.add(nowId);
            newEgg.setLocation(nowId, 0);

            nowId++;
            if (nowId == availableNodes.size()) {
                nowId = 0;
            }
        }

        int totalReplicas = SharedData.getRandomIntForCuckoo(totalCandidates + 1);
        for (int replicaCount = 0; replicaCount < totalReplicas; replicaCount++) {
            int candidateId = SharedData.getRandomIntForCuckoo(candidateIndices.size());
            int eggId = candidateIndices.get(candidateId);
            newEgg.setLocation(eggId, 1);
            candidateIndices.remove(candidateId);
        }

        newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(), content);

        return (newEvaluation > evaluation) ? null : newEgg;
    }

    public void abandon() {
        Egg newEgg;
        double newEvaluation;

        newEgg = new Egg(availableNodes);
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
        egg = new Egg(availableNodes);

        initializeEgg(egg);

        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), this.content);
    }

    private void initializeEgg(Egg egg) {
        int totalReplicas = SharedData.getRandomIntForCuckoo(availableNodeSize + 1);

        ArrayList<Integer> nodes = new ArrayList<Integer>(availableNodes);
        for (int replicaCount = 0; replicaCount < totalReplicas; replicaCount++) {
            int eggId = SharedData.getRandomIntForCuckoo(nodes.size());
            egg.setLocation(eggId, 1);
            nodes.remove(eggId);
        }
    }

    private int levyDistribution(int totalNodes) {
        int totalChanges;
        do {
            double d = SharedData.getRandomDoubleForCuckoo();
            totalChanges = (int) Math.round(Math.pow(d, -1.0 * fractalDimension));
        } while (totalChanges > totalNodes || totalChanges < 0);

        return totalChanges;
    }

    public String getData() {
        String data = "Binary: \n";
        ArrayList<Integer> dataList = egg.getLocations();
        for (Integer integer : dataList) {
            data += String.valueOf(integer);
        }
        data += "\nPlacement Nodes: \n";
        dataList = egg.getPlacementNodes();
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