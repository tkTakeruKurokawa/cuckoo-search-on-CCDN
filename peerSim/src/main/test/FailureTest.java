package main.test;

import java.util.ArrayList;
import java.io.*;
import main.ReplicaServer;
import main.SharedData;
import peersim.core.Control;
import peersim.core.Network;

public class FailureTest implements Control {
    ArrayList<Integer> failures = new ArrayList<Integer>();
    int cycle = 0;

    PrintWriter plot;

    public FailureTest(String prefix) {
        for (int i = 0; i < Network.size(); i++) {
            failures.add(i, 0);
        }

        try {
            plot = new PrintWriter(new BufferedWriter(new FileWriter("./src/main/test/plot.tsv", false)));
        } catch (Exception e) {
        }
    }

    public boolean execute() {

        System.out.println();

        ArrayList<Integer> failureNodes = new ArrayList<Integer>();
        int failureCount = 0;
        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);

            if (!node.getServerState()) {
                node.proceedProgressCycle();
                continue;
            }
            double failureRate = node.getFailureRate();

            double rand = SharedData.getRandomDouble();
            if (rand < failureRate) {
                node.setServerState(false);
                failures.set(i, failures.get(i) + 1);
                failureNodes.add(i);
                failureCount++;
            }

            node.proceedFailureRate();
        }

        for (Integer nodeId : failureNodes) {
            // System.out.printf("%d, ", nodeId);
        }
        System.out.println();
        System.out.println(failureCount);
        System.out.println();

        plot.println(cycle + "\t" + failureCount);

        if (cycle == 499) {
            for (int i = 0; i < failures.size(); i++) {
                System.out.println(i + ": " + failures.get(i));
                // plot.println(i + "\t" + failures.get(i));
            }

            plot.close();
        }

        cycle++;

        return false;
    }
}