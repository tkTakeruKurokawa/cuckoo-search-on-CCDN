package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Cost {
    private int totalPerSimulation;
    private int totalPerCycle;
    private int totalPerContent;
    private PrintWriter writer;

    public Cost(String fileName) {
        totalPerSimulation = 0;
        totalPerCycle = 0;
        totalPerContent = 0;

        try {
            File dir = new File("result/eps");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            writer = new PrintWriter(new BufferedWriter(new FileWriter("./result/" + fileName + ".tsv", false)));
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
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

    public void addedDescription(String description) {
        writer.println(description);
    }

    public void closeFile() {
        writer.close();
    }
}