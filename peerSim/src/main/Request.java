package main;

import java.util.ArrayList;

import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class Request implements Control {
    private static final String PAR_TOTAL_CONTENTS = "totalContents";
    private static int totalContents;
    private static final String PAR_TOTAL_ALGORITHMS = "totalAlgorithms";
    private static int totalAlgorithms;
    private static final String PAR_ORIGIN_ID = "originId";
    private static int originId;

    private static ArrayList<Integer> contentIndices;
    private static ArrayList<CostOfNetwork> networkCosts;
    private static ArrayList<CostOfOperation> operationCosts;

    public Request(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
        totalAlgorithms = Configuration.getInt(prefix + "." + PAR_TOTAL_ALGORITHMS);
        originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);

        contentIndices = new ArrayList<>();
        for (int i = 0; i < totalContents; i++) {
            contentIndices.add(i);
        }

        networkCosts = new ArrayList<>();
        operationCosts = new ArrayList<>();
        for (int i = 0; i < totalAlgorithms; i++) {
            networkCosts.add(new CostOfNetwork());
            operationCosts.add(new CostOfOperation());
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
        int randomIndex = SharedData.getRandomInt(randomContentIndices.size());
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
                nodeId = SharedData.getRandomInt(Network.size());
            } while (nodeId == originId);

            for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
                System.out.println("==================================================================");
                System.out.println(algorithmId + ": ");
                CostOfNetwork networkCost = networkCosts.get(algorithmId);
                CostOfOperation operationCost = operationCosts.get(algorithmId);

                int hop = Flooding.getHops(nodeId, algorithmId, content);
                if (hop == -1) {
                    networkCost.setContentFails(networkCost.getContentFails() + 1);
                    // System.out.println("****************** Content " + content.getContentId()
                    // + " request Failed ******************");
                    // System.out.println(Flooding.getAllPath());
                } else {
                    networkCost.setContentHops(networkCost.getContentHops() + hop);
                    operationCost.setContentProcessing(
                            operationCost.getContentProcessing() + (content.getSize() * (hop + 1)));
                    operationCost
                            .setContentTransmission(operationCost.getContentTransmission() + (content.getSize() * hop));
                    // System.out.println(Flooding.getAllPath());
                    System.out.println(Flooding.getPath());
                }
            }
        }
    }

    private void showContentCost(Content content) {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            CostOfNetwork networkCost = networkCosts.get(algorithmId);
            CostOfOperation operationCost = operationCosts.get(algorithmId);

            networkCost.calculateCycleCost();
            operationCost.calculateCycleCost();

            if (networkCost.getContentHops() > 0) {

                System.out.println("Content " + content.getContentId() + ": " + networkCost.getContentRequests()
                        + " requests, " + networkCost.getContentHops() + " hops, size: " + content.getSize());
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

    private void showCycleCost() {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            CostOfNetwork networkCost = networkCosts.get(algorithmId);
            CostOfOperation operationCost = operationCosts.get(algorithmId);

            calculateStorageCost(algorithmId, operationCosts);

            networkCost.calculateSimulationCost();
            operationCost.calculateSimulationCost();

            System.out.println("==================================================================");
            System.out.println("This Cycle " + networkCost.getCycleRequests() + " requests");
            System.out.println("This Cycle " + networkCost.getCycleHops() + " hops");
            System.out.println("This Cycle " + networkCost.getCycleFails() + " fails");
            System.out.println("This Cycle " + operationCost.getCycleStorage() + " storage used");
            System.out.println("This Cycle " + operationCost.getCycleProcessing() + " processing used");
            System.out.println("This Cycle " + operationCost.getCycleTransmission() + " transmission used");
            System.out.println("==================================================================");
        }
    }

    private void showSimulationCost() {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            CostOfNetwork networkCost = networkCosts.get(algorithmId);
            CostOfOperation operationCost = operationCosts.get(algorithmId);

            networkCost.calculateSimulationCost();
            operationCost.calculateSimulationCost();

            System.out.println("==================================================================");
            System.out.println("All Requests: " + networkCost.getSimulationRequests());
            System.out.println("All Hops: " + networkCost.getSimulationHops());
            System.out.println("All Fails: " + networkCost.getSimulationFails());
            System.out.println("All Storage Used: " + operationCost.getSimulationStorage());
            System.out.println("All Processing Used: " + operationCost.getSimulationProcessing());
            System.out.println("All Transmission Used: " + operationCost.getSimulationTransmission());
            System.out.println("==================================================================");
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

            showContentCost(content);
        }

        showCycleCost();
        System.out.println();
        System.out.println();

        if (CDState.getCycle() == 499) {
            showSimulationCost();
        }

        return false;
    }

}