package main;

public class Cost {
    private int totalPerSimulation;
    private int totalPerCycle;
    private int totalPerContent;

    public Cost() {
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