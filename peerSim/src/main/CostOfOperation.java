package main;

import java.util.ArrayList;

import peersim.cdsim.CDState;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CostOfOperation {
    private int totalCycles;
    private ArrayList<Cost> costs;
    private Cost storage;
    private Cost processing;
    private Cost transmission;
    private PrintWriter writer;

    public CostOfOperation(String name, int totalCycles) {
        this.totalCycles = totalCycles;

        storage = new Cost("Cost_of_Storage" + "[" + name + "]");
        processing = new Cost("Cost_of_Processing" + "[" + name + "]");
        transmission = new Cost("Cost_of_Transmission" + "[" + name + "]");

        costs = new ArrayList<>();
        costs.add(storage);
        costs.add(processing);
        costs.add(transmission);

        try {
            File dir = new File("result/eps");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            writer = new PrintWriter(
                    new BufferedWriter(new FileWriter("./result/Cost_Total" + "[" + name + "].tsv", false)));
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void calculateSimulationCost() {
        for (Cost cost : costs) {
            int totalPerCycle = cost.getPerCycle();
            int totalPerSimulation = cost.getPerSimulation();
            cost.setPerSimulation(totalPerCycle + totalPerSimulation);

            writeCycleCost(cost);
        }

        writeTotalCostPerCycle();
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

    private void writeCycleCost(Cost cost) {
        String description = CDState.getCycle() + "\t" + cost.getPerCycle();
        cost.addedDescription(description);

        if (CDState.getCycle() == totalCycles - 1) {
            cost.closeFile();
        }
    }

    private void writeTotalCostPerCycle() {
        int totalCost = 0;

        for (Cost cost : costs) {
            totalCost += cost.getPerCycle();
        }

        int cycle = CDState.getCycle();
        String description = cycle + "\t" + totalCost;
        writer.println(description);

        if (cycle == totalCycles - 1) {
            writer.close();
        }
    }
}