package main;

import java.util.ArrayList;

public class Nest {
    private Egg egg;
    private double evaluation;
    private Content content;

    private static ArrayList<Integer> availableNodes;
    private static int availableNodeSize;
    private static int algorithmId;

    public Nest(ArrayList<Integer> useNodes, Content content, int id) {
        this.content = content;
        availableNodes = useNodes;
        availableNodeSize = availableNodes.size();
        algorithmId = id;
        initializeNest();
    }

    /* このネストが持つ複製配置状況を元に新しいネストを生成 */
    public Egg levyWalk() {
        Egg newEgg = LevyWalk.runWalk(egg, content, availableNodeSize, algorithmId);
        double newEvaluation = ObjectiveFunction.getEvaluation(newEgg.getPlacementNodes(), content);

        return (newEvaluation < evaluation) ? newEgg : null;
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

    public void setEgg(Egg newEgg) {
        egg = newEgg;
        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), content);
    }

    public Egg getEgg() {
        return egg;
    }

    public double getEvaluation() {
        return evaluation;
    }

    private void initializeNest() {
        egg = new Egg();

        initializeEgg(egg);

        evaluation = ObjectiveFunction.getEvaluation(egg.getPlacementNodes(), content);
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
}