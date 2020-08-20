package main;

import peersim.core.Control;
import peersim.core.Network;

public class NetworkManager implements Control {

    public NetworkManager(String prefix) {
    }

    @Override
    public boolean execute() {

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
            }

            node.proceedFailureRate();
        }

        return false;
    }
}