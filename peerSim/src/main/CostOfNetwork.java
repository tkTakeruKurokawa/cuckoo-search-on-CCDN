package main;

import java.util.ArrayList;
import peersim.cdsim.CDState;
import peersim.core.Network;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CostOfNetwork {
    private int totalCycles;
    private int failedHops[];
    private ArrayList<Cost> costs;
    private Cost requests;
    private Cost hops;
    private Cost fails;
    private PrintWriter hopsWriter;
    private PrintWriter failsWriter;
    private PrintWriter failedHopsWriter;

    public CostOfNetwork(String name, int totalCycles) {
        this.totalCycles = totalCycles;
        this.failedHops = new int[Network.size()];

        requests = new Cost("Number_of_Requests" + "[" + name + "]");
        hops = new Cost("Average_Hops" + "[" + name + "]");
        fails = new Cost("Average_Fails" + "[" + name + "]");

        costs = new ArrayList<>();
        costs.add(requests);
        costs.add(hops);
        costs.add(fails);

        try {
            hopsWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Cumulative_Hops" + "[" + name + "].tsv", false)));
            failsWriter = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Cumulative_Fails" + "[" + name + "].tsv", false)));
            failedHopsWriter = new PrintWriter(new BufferedWriter(new FileWriter(
                    SharedData.getDirectoryName() + "/Failed_Hops_Distribution" + "[" + name + "].tsv", false)));
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    public void calculateSimulationCost() {
        for (int i = 0; i < costs.size(); i++) {
            int totalPerCycle = costs.get(i).getPerCycle();
            int totalPerSimulation = costs.get(i).getPerSimulation();
            costs.get(i).setPerSimulation(totalPerCycle + totalPerSimulation);

            if (i == 0) {
                writeNumberOfRequests(costs.get(i));
            } else {
                writeAverageCost(costs.get(i));
            }
        }
        writeCumulativeCost(hops, hopsWriter);
        writeCumulativeCost(fails, failsWriter);
        writeFailedHops();
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

    public void setFailedHops(int hops) {
        failedHops[hops]++;
    }

    private void writeNumberOfRequests(Cost cost) {
        int cycle = CDState.getCycle();
        int requests = cost.getPerCycle();
        cost.addedDescription(cycle + "\t" + requests);

        if (cycle == totalCycles - 1) {
            cost.closeFile();
        }
    }

    private void writeAverageCost(Cost cost) {
        int cycle = CDState.getCycle() + 1;
        double average = ((double) cost.getPerSimulation()) / ((double) cycle);
        String description = CDState.getCycle() + "\t" + average;
        cost.addedDescription(description);

        if (cycle == totalCycles) {
            cost.closeFile();
        }
    }

    private void writeCumulativeCost(Cost cost, PrintWriter writer) {
        int cycle = CDState.getCycle();
        double cumulative = (double) cost.getPerSimulation();
        writer.println((cycle) + "\t" + cumulative);

        if (cycle == totalCycles - 1) {
            writer.close();
        }
    }

    private void writeFailedHops() {
        if (CDState.getCycle() == totalCycles - 1) {
            for (int i = 0; i < failedHops.length; i++) {
                if (failedHops[i] > 0) {
                    failedHopsWriter.println(i + "\t" + failedHops[i]);
                }
            }

            failedHopsWriter.close();
        }
    }
}