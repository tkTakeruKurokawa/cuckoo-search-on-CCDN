package main.test;

import main.Link;
import main.ReplicaServer;
import main.SharedData;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class ShowParameters implements Control {
    private static final String PAR_TOTAL_ALGORITHMS = "totalAlgorithms";
    private static int totalAlgorithms;

    public ShowParameters(String prefix) {
        totalAlgorithms = Configuration.getInt(prefix + "." + PAR_TOTAL_ALGORITHMS);
    }

    @Override
    public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);
            Link link = SharedData.getLink(node);

            for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
                System.out.println(algorithmId + ": " + "ID: " + node.getIndex() + ", Status: " + node.getServerState()
                        + ", Storage: " + node.getStorageCapacity(algorithmId) + ", Contents: "
                        + node.showContents(algorithmId) + ", Size: " + node.showContentSize(algorithmId)
                        + ", Processing: " + node.getProcessingCapacity(algorithmId));

                for (int index = 0; index < link.degree(); index++) {
                    ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                    System.out.println("   " + algorithmId + ": (" + node.getIndex() + ", " + neighbor.getIndex()
                            + "), Transmission: " + link.getTransmissionCapacity(algorithmId, neighbor.getIndex()));
                }
            }
            System.out.println();
        }

        return false;
    }

}