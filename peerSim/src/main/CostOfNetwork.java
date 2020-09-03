package main;

import java.util.ArrayList;
import peersim.cdsim.CDState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CostOfNetwork {
    private int totalCycles;
    private ArrayList<Cost> costs;
    private Cost requests;
    private Cost hops;
    private Cost fails;
    private PrintWriter writer;

    public CostOfNetwork(String name, int totalCycles) {
        this.totalCycles = totalCycles;

        requests = new Cost("Number_of_Requests" + "[" + name + "]");
        hops = new Cost("Number_of_Hops" + "[" + name + "]");
        fails = new Cost("Number_of_Fails" + "[" + name + "]");

        costs = new ArrayList<>();
        costs.add(requests);
        costs.add(hops);
        costs.add(fails);

        try {
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Average_Hops" + "[" + name + "].tsv", false)));
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
        writeAverageHops(hops);
    }

    public void calculateCycleCost() {
        for (Cost cost : costs) {
            int totalPerContent = cost.getPerContent();
            int totalPerCycle = cost.getPerCycle();
            cost.setPerCycle(totalPerContent + totalPerCycle);
        }
    }

    public void resetCycleCost() {
        for (Cost cost : costs) {
            cost.setPerCycle(0);
        }
    }

    public void resetContentCost() {
        for (Cost cost : costs) {
            cost.setPerContent(0);
        }
    }

    public void setContentRequests(int value) {
        requests.setPerContent(value);
    }

    public void setContentHops(int value) {
        hops.setPerContent(value);
    }

    public void setContentFails(int value) {
        fails.setPerContent(value);
    }

    public int getSimulationRequests() {
        return requests.getPerSimulation();
    }

    public int getSimulationHops() {
        return hops.getPerSimulation();
    }

    public int getSimulationFails() {
        return fails.getPerSimulation();
    }

    public int getCycleRequests() {
        return requests.getPerCycle();
    }

    public int getCycleHops() {
        return hops.getPerCycle();
    }

    public int getCycleFails() {
        return fails.getPerCycle();
    }

    public int getContentRequests() {
        return requests.getPerContent();
    }

    public int getContentHops() {
        return hops.getPerContent();
    }

    public int getContentFails() {
        return fails.getPerContent();
    }

    private void writeCycleCost(Cost cost) {
        int cycle = CDState.getCycle() + 1;
        double average = ((double) cost.getPerSimulation()) / ((double) cycle);
        String description = CDState.getCycle() + "\t" + average;
        cost.addedDescription(description);

        if (CDState.getCycle() == totalCycles - 1) {
            cost.closeFile();
        }
    }

    private void writeAverageHops(Cost hops) {
        int cycle = CDState.getCycle() + 1;
        double average = ((double) getSimulationHops()) / ((double) cycle);
        writer.println((cycle - 1) + "\t" + average);

        if (cycle == totalCycles) {
            writer.close();
        }
    }
}