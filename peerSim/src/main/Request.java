package main;

import java.util.ArrayList;

import peersim.cdsim.CDState;
import peersim.core.Control;
import peersim.core.Network;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Request implements Control {
    private static int originId;
    private static int totalCycles;
    private static int totalContents;
    private static int totalAlgorithms;

    private static ArrayList<Integer> contentIndices;
    private static ArrayList<CostOfNetwork> networkCosts;
    private static ArrayList<CostOfOperation> operationCosts;

    private static PrintWriter writer;

    private void createCosts(String name) {
        networkCosts.add(new CostOfNetwork(name, totalCycles));
        operationCosts.add(new CostOfOperation(name, totalCycles));
    }

    public Request(String prefix) {
        originId = SharedData.getOriginId();
        totalCycles = SharedData.getTotalCycles();
        totalContents = SharedData.getTotalContents();
        totalAlgorithms = SharedData.getTotalAlgorithms();

        contentIndices = new ArrayList<>();
        for (int i = 0; i < totalContents; i++) {
            contentIndices.add(i);
        }

        networkCosts = new ArrayList<>();
        operationCosts = new ArrayList<>();
        for (int algorithmId = 0; algorithmId < SharedData.getTotalAlgorithms(); algorithmId++) {
            createCosts(SharedData.getAlgorithmName(algorithmId));
        }

        try {
            writer = new PrintWriter(new BufferedWriter(
                    new FileWriter(SharedData.getDirectoryName() + "/Simulation_Result.txt", false)));
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void resetProcessingCapacity() {
        for (int i = 0; i < Network.size(); i++) {
            ReplicaServer node = SharedData.getNode(i);
            Link link = SharedData.getLink(node);

            for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
                node.resetProcessingCapacity(algorithmId);

                for (int index = 0; index < link.degree(); index++) {
                    ReplicaServer neighbor = (ReplicaServer) link.getNeighbor(index);
                    link.resetTransmissionCapacity(algorithmId, neighbor.getIndex());
                }
            }
        }
    }

    private void resetCycleCost() {
        for (CostOfNetwork cost : networkCosts) {
            cost.resetCycleCost();
        }
        for (CostOfOperation cost : operationCosts) {
            cost.resetCycleCost();
        }
    }

    private Content getRandomContent(ArrayList<Integer> randomContentIndices) {
        int randomIndex = SharedData.getRandomIntForRequest(randomContentIndices.size());
        int contentId = randomContentIndices.get(randomIndex);

        Content content = SharedData.getContent(contentId);
        randomContentIndices.remove(randomIndex);
        return content;
    }

    private void resetContentCost(int totalRequests) {
        for (CostOfNetwork cost : networkCosts) {
            cost.resetContentCost();
            cost.setContentRequests(totalRequests);
        }

        for (CostOfOperation cost : operationCosts) {
            cost.resetContentCost();
        }
    }

    private void search(Content content) {
        int totalRequests = content.getRequest();
        for (int requestCount = 0; requestCount < totalRequests; requestCount++) {
            int nodeId;
            do {
                nodeId = SharedData.getRandomIntForRequest(Network.size());
            } while (nodeId == originId);

            for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
                // System.out.println("==================================================================");
                // System.out.println(SharedData.getAlgorithmName(algorithmId) + ": ");
                CostOfNetwork networkCost = networkCosts.get(algorithmId);
                CostOfOperation operationCost = operationCosts.get(algorithmId);

                int hop = Flooding.getHops(nodeId, algorithmId, content);
                if (hop <= 0) {
                    networkCost.setContentFails(networkCost.getContentFails() + 1);
                    // System.out.println("****************** Content " + content.getContentId()
                    // + " request Failed ******************");
                    // System.out.println(Flooding.getAllPath());
                    networkCost.setFailedHops(hop * -1);
                } else {
                    networkCost.setContentHops(networkCost.getContentHops() + hop);
                    operationCost.setContentProcessing(
                            operationCost.getContentProcessing() + (content.getSize() * (hop + 1)));
                    operationCost
                            .setContentTransmission(operationCost.getContentTransmission() + (content.getSize() * hop));
                    // System.out.println(Flooding.getAllPath());
                    // System.out.println(Flooding.getPath());
                }
            }
        }
    }

    private void showContentCost(Content content, CostOfNetwork networkCost) {
        System.out.println("Content " + content.getContentId() + ": " + networkCost.getContentRequests() + " requests, "
                + networkCost.getContentHops() + " hops, size: " + content.getSize());
    }

    private void calculateCycleCost(Content content) {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            CostOfNetwork networkCost = networkCosts.get(algorithmId);
            CostOfOperation operationCost = operationCosts.get(algorithmId);

            networkCost.calculateCycleCost();
            operationCost.calculateCycleCost();

            if (networkCost.getContentHops() > 0) {
                // showContentCost(content, networkCost);
            }
        }
    }

    private void calculateStorageCost(int algorithmId, ArrayList<CostOfOperation> operationCosts) {
        int cumulativeStorage = 0;
        for (int nodeId = 0; nodeId < Network.size(); nodeId++) {
            ReplicaServer node = SharedData.getNode(nodeId);
            if (!node.getServerState()) {
                continue;
            }
            ArrayList<Integer> contents = node.getContents(algorithmId);

            for (Integer contentId : contents) {
                Content content = SharedData.getContent(contentId);
                cumulativeStorage += content.getSize();
            }
        }

        operationCosts.get(algorithmId).setCycleStorage(cumulativeStorage);
    }

    private void showCycleCost(String name, CostOfNetwork networkCost, CostOfOperation operationCost) {
        System.out.println("==================================================================");
        System.out.println(name);
        System.out.println("This Cycle " + networkCost.getCycleRequests() + " requests");
        System.out.println("This Cycle " + networkCost.getCycleHops() + " hops");
        System.out.println("This Cycle " + networkCost.getCycleFails() + " fails");
        System.out.println("This Cycle " + operationCost.getCycleStorage() + " storage used");
        System.out.println("This Cycle " + operationCost.getCycleProcessing() + " processing used");
        System.out.println("This Cycle " + operationCost.getCycleTransmission() + " transmission used");
        System.out.println("==================================================================");
    }

    private void calculateSimulationCost() {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            CostOfNetwork networkCost = networkCosts.get(algorithmId);
            CostOfOperation operationCost = operationCosts.get(algorithmId);

            calculateStorageCost(algorithmId, operationCosts);

            networkCost.calculateSimulationCost();
            operationCost.calculateSimulationCost();

            showCycleCost(SharedData.getAlgorithmName(algorithmId), networkCost, operationCost);
        }
    }

    private void showSimulationCost() {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            CostOfNetwork networkCost = networkCosts.get(algorithmId);
            CostOfOperation operationCost = operationCosts.get(algorithmId);

            int totalCost = operationCost.getSimulationStorage() + operationCost.getSimulationProcessing()
                    + operationCost.getSimulationTransmission();
            double cycle = (double) CDState.getCycle();

            int request = networkCost.getSimulationRequests();
            int hop = networkCost.getSimulationHops();
            int fail = networkCost.getSimulationFails();
            int storage = operationCost.getSimulationStorage();
            int processing = operationCost.getSimulationProcessing();
            int transmission = operationCost.getSimulationTransmission();

            System.out.println("==================================================================");
            System.out.println(SharedData.getAlgorithmName(algorithmId));
            System.out.println("All Requests: " + request + ", Average: " + (((double) request) / cycle));
            System.out.println("All Hops: " + hop + ", Average: " + (((double) hop) / cycle));
            System.out.println("All Fails: " + fail + ", Average: " + (((double) fail) / cycle));
            System.out.println("All Storage Used: " + storage + ", Average: " + (((double) storage) / cycle));
            System.out.println("All Processing Used: " + processing + ", Average: " + (((double) processing) / cycle));
            System.out.println(
                    "All Transmission Used: " + transmission + ", Average: " + (((double) transmission) / cycle));
            System.out.println("Total Cost: " + totalCost + ", Average: " + ((double) totalCost / cycle));
            System.out.println("==================================================================");

            writer.println("==================================================================");
            writer.println(SharedData.getAlgorithmName(algorithmId));
            writer.println("All Requests: " + request + ", Average: " + (((double) request) / cycle));
            writer.println("All Hops: " + hop + ", Average: " + (((double) hop) / cycle));
            writer.println("All Fails: " + fail + ", Average: " + (((double) fail) / cycle));
            writer.println("All Storage Used: " + storage + ", Average: " + (((double) storage) / cycle));
            writer.println("All Processing Used: " + processing + ", Average: " + (((double) processing) / cycle));
            writer.println(
                    "All Transmission Used: " + transmission + ", Average: " + (((double) transmission) / cycle));
            writer.println("Total Cost: " + totalCost + ", Average: " + ((double) totalCost / cycle));
            writer.println("==================================================================");
            writer.println("\n\n");
        }
    }

    @Override
    public boolean execute() {

        System.out.println();
        System.out.println();

        resetProcessingCapacity();

        resetCycleCost();
        ArrayList<Integer> randomContentIndices = new ArrayList<>(contentIndices);
        for (int contentCount = 0; contentCount < totalContents; contentCount++) {
            Content content = getRandomContent(randomContentIndices);

            resetContentCost(content.getRequest());

            search(content);

            calculateCycleCost(content);
        }

        calculateSimulationCost();
        System.out.println();
        System.out.println();

        if (CDState.getCycle() == totalCycles - 1) {
            showSimulationCost();
            writer.close();
            CuckooSearch.closeFile();
            GreedyAlgorithm.closeFile();
            SharedData.closeFile();
        }

        return false;
    }

}