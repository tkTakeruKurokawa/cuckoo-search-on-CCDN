package main.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Queue;
import java.io.*;
import java.lang.reflect.Array;

import main.Link;
import main.ReplicaServer;
import main.SharedData;
import peersim.core.Control;
import peersim.core.Network;
import peersim.core.Node;

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

        System.out.println("====================================================");

        ArrayList<Integer> failureNodes = new ArrayList<Integer>();
        int failureCount = 0;
        int totalFailure = 0;
        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);

            if (!node.getServerState()) {
                node.proceedProgressCycle();
                totalFailure++;
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

        ArrayList<Integer> totalAddedNodes = new ArrayList<Integer>();
        Queue<Integer> queue = new ArrayDeque<Integer>();
        ArrayList<Integer> brokenNodes = new ArrayList<Integer>();
        int separates = 0;
        System.out.println("Networks: ");
        for (int i = 0; i < 100; i++) {
            if (!SharedData.getNode(i).getServerState()) {
                brokenNodes.add(i);
            } else if (!totalAddedNodes.contains(i)) {
                ArrayList<Integer> addedNodes = new ArrayList<Integer>();
                queue.add(i);
                addedNodes.add(i);
                totalAddedNodes.add(i);

                while (Objects.nonNull(queue.peek())) {
                    ReplicaServer node = SharedData.getNode(queue.poll());
                    Link link = SharedData.getLink(node);
                    for (int index = 0; index < link.degree(); index++) {
                        ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                        if (!neighbor.getServerState()) {
                            continue;
                        }
                        int neighborId = neighbor.getIndex();
                        if (!addedNodes.contains(neighborId)) {
                            totalAddedNodes.add(neighborId);
                            addedNodes.add(neighborId);
                            queue.add(neighborId);
                        }
                    }
                }

                separates++;

                System.out.printf("[");
                for (Integer id : addedNodes) {
                    System.out.printf("%d ", id);
                }
                System.out.printf("]\n");
            }
        }

        System.out.println();
        System.out.println("Failure Nodes: ");
        System.out.printf("[");
        for (Integer id : brokenNodes) {
            System.out.printf("%d ", id);
        }
        System.out.printf("]\n");

        System.out.println();
        System.out.println("Number of Network: " + separates);
        System.out.println("This Cycle Failures: " + failureCount);
        System.out.println("Now Failures: " + totalFailure);

        plot.println(cycle + "\t" + failureCount);

        System.out.println("====================================================");

        if (cycle == 499) {
            // for (int i = 0; i < failures.size(); i++) {
            // System.out.println(i + ": " + failures.get(i));
            // // plot.println(i + "\t" + failures.get(i));
            // }

            plot.close();
        }

        cycle++;

        return false;
    }
}