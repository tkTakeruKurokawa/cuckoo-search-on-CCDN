package main.test;

import java.util.ArrayList;

import main.Link;
import main.ReplicaServer;
import main.SharedData;
import peersim.cdsim.CDState;
import peersim.core.Control;
import peersim.core.Network;

public class ShowParameters implements Control {

    private static int totalAlgorithms;
    private static ArrayList<FullNodes> algorithms;

    public ShowParameters(String prefix) {
        totalAlgorithms = SharedData.getTotalAlgorithms();

        algorithms = new ArrayList<>();
        for (int algorithmId = 0; algorithmId < SharedData.getTotalAlgorithms(); algorithmId++) {
            algorithms.add(new FullNodes());
        }
    }

    private static void showFulls() {
        System.out.println();
        System.out.println();
        for (int algorithmId = 0; algorithmId < SharedData.getTotalAlgorithms(); algorithmId++) {
            System.out.println("--------------------------------------------------------------");
            System.out.println(SharedData.getAlgorithmName(algorithmId) + ": ");
            System.out.println("This Cycle " + algorithms.get(algorithmId).storage + " Nodes Storage Capacity Full");
            System.out.println(
                    "This Cycle " + algorithms.get(algorithmId).processing + " Nodes Processing Capacity Full");
            System.out.println(
                    "This Cycle " + algorithms.get(algorithmId).transmission + " Nodes Transmission Capacity Full");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println();
        System.out.println();
    }

    private static void aggregate() {
        for (FullNodes algorithm : algorithms) {
            algorithm.totalStorage += algorithm.storage;
            algorithm.totalProcessing += algorithm.processing;
            algorithm.totalTransmission += algorithm.transmission;

            algorithm.reset();
        }
    }

    private static void showAggregate() {
        System.out.println();
        System.out.println();
        for (int algorithmId = 0; algorithmId < SharedData.getTotalAlgorithms(); algorithmId++) {
            System.out.println("--------------------------------------------------------------");
            System.out.println(SharedData.getAlgorithmName(algorithmId) + ": ");
            System.out.println(
                    "This Simulation " + algorithms.get(algorithmId).totalStorage + " Nodes Storage Capacity Full");
            System.out.println("This Simulation " + algorithms.get(algorithmId).totalProcessing
                    + " Nodes Processing Capacity Full");
            System.out.println("This Simulation " + algorithms.get(algorithmId).totalTransmission
                    + " Nodes Transmission Capacity Full");
            System.out.println("--------------------------------------------------------------");
        }
        System.out.println();
        System.out.println();
    }

    @Override
    public boolean execute() {

        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);
            Link link = SharedData.getLink(node);

            for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
                System.out.println(
                        SharedData.getAlgorithmName(algorithmId) + ": " + "ID: " + node.getIndex() + ", Status: "
                                + node.getServerState() + ", Processing: " + node.getProcessingCapacity(algorithmId)
                                + ", Storage: " + node.getStorageCapacity(algorithmId) + ", Contents: "
                                + node.showContents(algorithmId) + ", Size: " + node.showContentSize(algorithmId));
                if (node.getStorageCapacity(algorithmId) == 0) {
                    algorithms.get(algorithmId).storage++;
                }
                if (node.getProcessingCapacity(algorithmId) == 0) {
                    algorithms.get(algorithmId).processing++;
                }

                for (int index = 0; index < link.degree(); index++) {
                    ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                    System.out.println("    " + algorithmId + ": (" + node.getIndex() + ", " + neighbor.getIndex()
                            + "), Transmission: " + link.getTransmissionCapacity(algorithmId, neighbor.getIndex()));
                    if (link.getTransmissionCapacity(algorithmId, neighbor.getIndex()) == 0) {
                        algorithms.get(algorithmId).transmission++;
                    }
                }
            }
            System.out.println();
        }

        for (FullNodes algorithm : algorithms) {
            algorithm.transmission = algorithm.transmission / 2;
        }

        showFulls();
        aggregate();

        if (CDState.getCycle() == SharedData.getTotalCycles() - 1) {
            showAggregate();
        }

        return false;
    }
}

/**
 * InnerShowParameters
 */
class FullNodes {
    public int storage;
    public int processing;
    public int transmission;

    public int totalStorage;
    public int totalProcessing;
    public int totalTransmission;

    public FullNodes() {
        storage = 0;
        processing = 0;
        transmission = 0;

        totalStorage = 0;
        totalProcessing = 0;
        totalTransmission = 0;
    }

    public void reset() {
        storage = 0;
        processing = 0;
        transmission = 0;
    }
}