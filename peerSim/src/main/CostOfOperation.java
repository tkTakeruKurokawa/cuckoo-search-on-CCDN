package main;

import java.util.ArrayList;

public class CostOfOperation {
    private ArrayList<Cost> costs;
    private Cost storage;
    private Cost processing;
    private Cost transmission;

    public CostOfOperation() {
        storage = new Cost();
        processing = new Cost();
        transmission = new Cost();

        costs = new ArrayList<>();
        costs.add(storage);
        costs.add(processing);
        costs.add(transmission);
    }

    public void calculateSimulationCost() {
        for (Cost cost : costs) {
            int totalPerCycle = cost.getPerCycle();
            int totalPerSimulation = cost.getPerSimulation();
            cost.setPerSimulation(totalPerCycle + totalPerSimulation);
        }
    }

    public void calculateCycleCost() {
        for (int i = 1; i < 3; i++) {
            int totalPerContent = costs.get(i).getPerContent();
            int totalPerCycle = costs.get(i).getPerCycle();
            costs.get(i).setPerCycle(totalPerContent + totalPerCycle);
        }
    }

    public void restCycleStorageCost() {
        storage.setPerContent(0);
    }

    public void resetCycleCost() {
        for (int i = 1; i < 3; i++) {
            costs.get(i).setPerCycle(0);
        }
    }

    public void resetContentCost() {
        for (int i = 1; i < 3; i++) {
            costs.get(i).setPerContent(0);
        }
    }

    public void setCycleStorage(int value) {
        storage.setPerCycle(value);
    }

    public void setContentProcessing(int value) {
        processing.setPerContent(value);
    }

    public void setContentTransmission(int value) {
        transmission.setPerContent(value);
    }

    public int getSimulationStorage() {
        return storage.getPerSimulation();
    }

    public int getSimulationProcessing() {
        return processing.getPerSimulation();
    }

    public int getSimulationTransmission() {
        return transmission.getPerSimulation();
    }

    public int getCycleStorage() {
        return storage.getPerCycle();
    }

    public int getCycleProcessing() {
        return processing.getPerCycle();
    }

    public int getCycleTransmission() {
        return transmission.getPerCycle();
    }

    public int getContentProcessing() {
        return processing.getPerContent();
    }

    public int getContentTransmission() {
        return transmission.getPerContent();
    }
}