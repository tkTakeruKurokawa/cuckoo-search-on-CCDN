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

    private static ArrayList<Integer> contentIndices;
    private static ArrayList<Result> algorithmResults;

    public Request(String prefix) {
        totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
        totalAlgorithms = Configuration.getInt(prefix + "." + PAR_TOTAL_ALGORITHMS);

        contentIndices = new ArrayList<>();
        for (int i = 0; i < totalContents; i++) {
            contentIndices.add(i);
        }

        algorithmResults = new ArrayList<>();
        for (int i = 0; i < totalAlgorithms; i++) {
            algorithmResults.add(new Result());
        }
    }

    private Content getRandomContent(ArrayList<Integer> randomContentIndices) {
        int randomIndex = SharedData.getRandomInt(randomContentIndices.size());
        int contentId = randomContentIndices.get(randomIndex);

        Content content = SharedData.getContent(contentId);
        randomContentIndices.remove(randomIndex);
        return content;
    }

    private void resetCycleResult() {
        for (Result result : algorithmResults) {
            result.resetCycleResult();
        }
    }

    private void resetContentResult(int totalRequests) {
        for (Result result : algorithmResults) {
            result.resetContentResult();
            result.setContentRequests(totalRequests);
        }
    }

    private void search(Content content) {
        int totalRequests = content.getRequest();
        for (int requestCount = 0; requestCount < totalRequests; requestCount++) {
            int nodeId = SharedData.getRandomInt(Network.size());

            for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
                System.out.println("==================================================================");
                System.out.println(algorithmId + ": ");
                Result result = algorithmResults.get(algorithmId);

                int hop = Flooding.getHops(nodeId, algorithmId, content);
                if (hop == -1) {
                    result.setContentFails(result.getContentFails() + 1);
                    System.out.println("******************   Content " + content.getContentId()
                            + " request Failed   ******************");
                    System.out.println(Flooding.getPath());
                } else {
                    result.setContentHops(result.getContentHops() + hop);
                    System.out.println(Flooding.getPath());

                }
            }
        }
    }

    private void showContentResult(int contentId) {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            Result result = algorithmResults.get(algorithmId);

            result.calculateCycleResult();

            if (result.getContentHops() > 0) {
                System.out.println("Content " + contentId + ": " + result.getContentRequests() + " requests, "
                        + result.getContentHops() + " hops");
            }
        }
    }

    private void showCycleResult() {
        for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
            Result result = algorithmResults.get(algorithmId);
            result.calculateSimulationResult();

            System.out.println("==================================================================");
            System.out.println("This Cycle " + result.getCycleRequests() + " requests");
            System.out.println("This Cycle " + result.getCycleHops() + " hops");
            System.out.println("This Cycle " + result.getCycleFails() + " fails");
            System.out.println("==================================================================");

            System.out.println();
            System.out.println();
        }
    }

    private void showSimulationResult(String name, Result result) {
        System.out.println(name + ": ");
        System.out.println("All Requests: " + result.getSimulationRequests());
        System.out.println("All Hops: " + result.getSimulationHops());
        System.out.println("All Fails: " + result.getSimulationFails());
        System.out.println();
    }

    @Override
    public boolean execute() {

        System.out.println();
        System.out.println();

        resetCycleResult();
        ArrayList<Integer> randomContentIndices = new ArrayList<>(contentIndices);
        for (int contentCount = 0; contentCount < totalContents; contentCount++) {
            Content content = getRandomContent(randomContentIndices);

            resetContentResult(content.getRequest());

            search(content);

            showContentResult(content.getContentId());
        }

        showCycleResult();

        if (CDState.getCycle() == 499)

        {
            showSimulationResult("Cuckoo", algorithmResults.get(0));
            showSimulationResult("Only Origin", algorithmResults.get(1));

        }

        return false;
    }

}