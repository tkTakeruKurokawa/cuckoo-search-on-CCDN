package main;

import java.util.ArrayList;

import peersim.cdsim.CDState;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CostOfOperation {
    private int totalCycles;
    private ArrayList<Cost> costs;
    private Cost storage;
    private Cost processing;
    private Cost transmission;
    private PrintWriter averageWriter;
    private PrintWriter cumulativeWriter;

    public CostOfOperation(String name, int totalCycles) {
        this.totalCycles = totalCycles;

        storage = new Cost("Average_Storage" + "[" + name + "]");
        processing = new Cost("Average_Processing" + "[" + name + "]");
        transmission = new Cost("Average_Transmission" + "[" + name + "]");

        costs = new ArrayList<>();
        costs.add(storage);
        costs.add(processing);
        costs.add(transmission);

        try {
            averageWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Average_Total" + "[" + name + "].tsv", false)));
            cumulativeWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Cumulative_Total" + "[" + name + "].tsv", false)));
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

            writeAverageCost(cost);
        }

        writeTotalCost();
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

    private void writeAverageCost(Cost cost) {
        int cycle = CDState.getCycle() + 1;
        double average = ((double) cost.getPerSimulation()) / ((double) cycle);
        String description = CDState.getCycle() + "\t" + average;
        cost.addedDescription(description);

        if (CDState.getCycle() == totalCycles - 1) {
            cost.closeFile();
        }
    }

    private void writeTotalCost() {
        int totalCost = 0;

        for (Cost cost : costs) {
            totalCost += cost.getPerSimulation();
        }

        int cycle = CDState.getCycle();
        String description = cycle + "\t" + totalCost;
        cumulativeWriter.println(description);

        description = cycle + "\t" + ((double) totalCost / (double) (cycle + 1));
        averageWriter.println(description);

        if (cycle == totalCycles - 1) {
            averageWriter.close();
            cumulativeWriter.close();
        }
    }
}