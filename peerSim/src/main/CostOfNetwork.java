package main;

import java.util.ArrayList;

public class CostOfNetwork {
    private ArrayList<Cost> costs;
    private Cost requests;
    private Cost hops;
    private Cost fails;

    public CostOfNetwork() {
        requests = new Cost();
        hops = new Cost();
        fails = new Cost();

        costs = new ArrayList<>();
        costs.add(requests);
        costs.add(hops);
        costs.add(fails);
    }

    public void calculateSimulationCost() {
        for (Cost cost : costs) {
            int totalPerCycle = cost.getPerCycle();
            int totalPerSimulation = cost.getPerSimulation();
            cost.setPerSimulation(totalPerCycle + totalPerSimulation);
        }
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

}