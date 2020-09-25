package main;

import java.util.ArrayList;

public class Egg {
    private ArrayList<Integer> placementNodes;

    public Egg() {
        placementNodes = new ArrayList<>();
    }

    public void clonePlacementNodes(ArrayList<Integer> placementNodes) {
        this.placementNodes = new ArrayList<Integer>(placementNodes);
    }

    public void addPlacementNode(int nodeId) {
        this.placementNodes.add(nodeId);
    }

    public void setPlacementNodes(ArrayList<Integer> placementNodes) {
        this.placementNodes = placementNodes;
    }

    public ArrayList<Integer> getPlacementNodes() {
        return placementNodes;
    }
}