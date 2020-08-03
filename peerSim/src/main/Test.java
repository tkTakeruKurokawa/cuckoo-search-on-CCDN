package main;

import peersim.core.Network;
import peersim.core.Control;

public class Test implements Control {

    public Test(String prefix) {
    }

    public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);
            Link link = SharedData.getLink(node);

            System.out.printf("%d: ", i);
            for (int j = 0; j < link.degree(); j++) {
                System.out.printf("%d, ", link.getNeighbor(j).getIndex());
            }
            System.out.println();
        }

        System.out.println("=================================================================");

        ReplicaServer node2 = SharedData.getNode(1);
        Link link2 = SharedData.getLink(node2);
        link2.setTransmissionCapacity(0, link2.getNeighbor(0).getIndex(), -1000);

        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);
            Link link = SharedData.getLink(node);

            System.out.println(node.getIndex());
            for (int j = 0; j < 2; j++) {
                System.out.printf(" algorithm%d: ", j);
                for (int j2 = 0; j2 < link.degree(); j2++) {
                    System.out.printf("%d, ", link.getTransmissionCapacity(j, link.getNeighbor(j2).getIndex()));
                }
                System.out.println();
            }
            System.out.println();

        }

        return false;
    }
}