package main;

import peersim.cdsim.CDState;
import peersim.core.Control;
import peersim.core.Network;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class NetworkManager implements Control {
    private static int totalCycles;
    private static PrintWriter writer;

    public NetworkManager(String prefix) {
        totalCycles = SharedData.getTotalCycles();

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("./result/Failed_Servers.tsv", false)));
        } catch (Exception e) {
        }
    }

    @Override
    public boolean execute() {

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
                node.resetContents();
                failureCount++;
            }

            node.proceedFailureRate();
        }

        int cycle = CDState.getCycle();
        writer.println(cycle + "\t" + failureCount);

        if (cycle == totalCycles - 1) {
            writer.close();
        }

        return false;
    }
}