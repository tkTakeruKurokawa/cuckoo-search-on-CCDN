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
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Number_of_Failure_Servers.tsv", false)));
        } catch (Exception e) {
        }
    }

    @Override
    public boolean execute() {

        int failureCount = 0;
        for (int i = 0; i < Network.size(); i++) {
            SurrogateServer node = SharedData.getNode(i);

            if (!node.getServerState()) {
                node.proceedProgressCycle();
                failureCount++;
                continue;
            }

            double failureRate = node.getFailureRate();
            double rand = SharedData.getRandomDoubleForFailure();
            if (rand < failureRate) {
                node.setServerState(false);
                node.resetContents();
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