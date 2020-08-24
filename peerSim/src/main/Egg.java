package main;

import java.util.ArrayList;
import peersim.config.Configuration;
import peersim.core.Control;

public class Egg implements Control {
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;

    private ArrayList<Integer> placementLocations = new ArrayList<Integer>();
    private ArrayList<Integer> nodeIndices = new ArrayList<Integer>();

    public Egg(String prefix) {
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
    }

    public Egg(ArrayList<Integer> availableNodes) {
        placementLocations = new ArrayList<Integer>();
        nodeIndices = new ArrayList<Integer>(availableNodes);

        for (int i = 0; i < availableNodes.size(); i++) {
            placementLocations.add(0);
        }
    }

    public ArrayList<Integer> getLocations() {
        return placementLocations;
    }

    public ArrayList<Integer> getNodes() {
        return nodeIndices;
    }

    public int getLocation(int index) {
        return placementLocations.get(index);
    }

    public void setLocation(int index, int value) {
        placementLocations.set(index, value);
    }

    public void clonePlacementLocation(ArrayList<Integer> placementLocations) {
        this.placementLocations = new ArrayList<Integer>(placementLocations);
    }

    public ArrayList<Integer> getPlacementNodes() {
        ArrayList<Integer> placementNodes = new ArrayList<Integer>();

        for (int i = 0; i < placementLocations.size(); i++) {
            if (placementLocations.get(i) == 1) {
                placementNodes.add(nodeIndices.get(i));
            }
        }

        placementNodes.add(originId);

        return placementNodes;
    }

    @Override
    public boolean execute() {
        return false;
    }

}