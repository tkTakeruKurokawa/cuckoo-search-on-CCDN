package main;

import java.util.ArrayList;
import java.util.Arrays;

import peersim.core.Control;

public class Replicate implements Control {

    public Replicate(String prefix) {
    }

    private static void setReplica(ArrayList<Integer> placementNodes, Content content, int algorithmId) {
        for (Integer nodeId : placementNodes) {
            ReplicaServer node = SharedData.getNode(nodeId);

            if (!node.setContent(algorithmId, content)) {
                System.exit(-1);
            }
        }
    }

    @Override
    public boolean execute() {

        ArrayList<Content> replicatedContents = SharedData.getReplicatedContents();
        for (Content content : replicatedContents) {
            ArrayList<Integer> placementNodes = new ArrayList<>();
            placementNodes = CuckooSearch.runSearch(0, content);
            setReplica(placementNodes, content, 0);
            placementNodes = RandomAlgorithm.runSearch(1, content);
            setReplica(placementNodes, content, 1);
            placementNodes = GreedyAlgorithm.runSearch(2, content);
            setReplica(placementNodes, content, 2);
            placementNodes = new ArrayList<>(Arrays.asList(SharedData.getOriginId()));
            setReplica(placementNodes, content, 3);
        }

        return false;
    }

}