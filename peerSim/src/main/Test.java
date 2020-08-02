package main;

import peersim.core.Network;
import peersim.core.Node;
import peersim.core.Control;

public class Test implements Control {

    public Test(String prefix) {
    }

    public boolean execute() {
        for (int i = 0; i < Network.size(); i++) {
            Node node = Network.get(i);
            Link link = SharedData.getLink(node);

            System.out.printf("%d: ", i);
            for (int j = 0; j < link.degree(); j++) {
                System.out.printf("%d, ", link.getNeighbor(j).getIndex());
            }
            System.out.println();
        }

        return false;
    }
}