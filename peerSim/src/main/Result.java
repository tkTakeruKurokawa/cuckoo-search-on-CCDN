package main;

import java.util.ArrayList;

public class Result {
    private ArrayList<Counter> results;
    private Counter requests;
    private Counter hops;
    private Counter fails;

    public Result() {
        requests = new Counter();
        hops = new Counter();
        fails = new Counter();

        results = new ArrayList<>();
        results.add(requests);
        results.add(hops);
        results.add(fails);
    }

    public void calculateSimulationResult() {
        for (Counter result : results) {
            int totalPerCycle = result.getPerCycle();
            int totalPerSimulation = result.getPerSimulation();
            result.setPerSimulation(totalPerCycle + totalPerSimulation);
        }
    }

    public void calculateCycleResult() {
        for (Counter result : results) {
            int totalPerContent = result.getPerContent();
            int totalPerCycle = result.getPerCycle();
            result.setPerCycle(totalPerContent + totalPerCycle);
        }
    }

    public void resetCycleResult() {
        for (Counter result : results) {
            result.setPerCycle(0);
        }
    }

    public void resetContentResult() {
        for (Counter result : results) {
            result.setPerContent(0);
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

class Counter {
    private int totalPerSimulation;
    private int totalPerCycle;
    private int totalPerContent;

    public Counter() {
        totalPerSimulation = 0;
        totalPerCycle = 0;
        totalPerContent = 0;
    }

    public void setPerSimulation(int value) {
        totalPerSimulation = value;
    }

    public void setPerCycle(int value) {
        totalPerCycle = value;
    }

    public void setPerContent(int value) {
        totalPerContent = value;
    }

    public int getPerSimulation() {
        return totalPerSimulation;
    }

    public int getPerCycle() {
        return totalPerCycle;
    }

    public int getPerContent() {
        return totalPerContent;
    }
}