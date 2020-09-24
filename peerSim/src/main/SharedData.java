package main;

import peersim.core.*;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import peersim.cdsim.CDState;
import peersim.config.*;

public class SharedData implements Control {
	private static final String PAR_LNK = "link";
	private static int pid_lnk;
	private static final String PAR_TOTAL_NODES = "totalNodes";
	private static int totalNodes;
	private static final String PAR_TOTAL_CYCLES = "totalCycles";
	private static int totalCycles;
	private static final String PAR_TOTAL_CONTENTS = "totalContents";
	private static int totalContents;
	private static final String PAR_DIRECTORY_NAME = "directoryName";
	private static String directoryName;

	private static Random randomForRequest;
	private static Random randomForCuckoo;
	private static Random randomForRandom;
	private static Random randomForFailure;
	private static Random randomForParameters;
	private static ArrayList<Content> contentSet;
	private static ArrayList<Content> replicatedContents;
	private static ArrayList<String> algorithmNames;
	private static HashMap<Integer, Integer> originIndices;
	private static ArrayList<FailedReason> failedCounters;

	public SharedData(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_LNK);
		totalNodes = Configuration.getInt(prefix + "." + PAR_TOTAL_NODES);
		totalCycles = Configuration.getInt(prefix + "." + PAR_TOTAL_CYCLES);
		totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
		directoryName = Configuration.getString(prefix + "." + PAR_DIRECTORY_NAME);

		failedCounters = new ArrayList<>();
		for (String name : algorithmNames) {
			failedCounters.add(new FailedReason(totalCycles, name));
		}
	}

	public SharedData() {
		algorithmNames = new ArrayList<String>();
		algorithmNames.add("Cuckoo_Search");
		algorithmNames.add("Random");
		algorithmNames.add("Greedy");
		algorithmNames.add("Original_Only");

		randomForRequest = new Random(1L);
		randomForCuckoo = new Random(2L);
		randomForRandom = new Random(3L);
		randomForFailure = new Random(4L);
		randomForParameters = new Random(5L);

		originIndices = new HashMap<>();
		originIndices.put(50, 44);
		originIndices.put(100, 69);
		originIndices.put(500, 249);
		originIndices.put(1000, 68);
	}

	public static SurrogateServer getNode(int nodeIndex) {
		return (SurrogateServer) Network.get(nodeIndex);
	}

	public static Link getLink(Node node) {
		return (Link) node.getProtocol(pid_lnk);
	}

	public static Content getContent(int contentId) {
		return contentSet.get(contentId);
	}

	public static ArrayList<Content> getReplicatedContents() {
		return replicatedContents;
	}

	public static void setReplicatedContents(ArrayList<Content> contents) {
		replicatedContents = contents;
	}

	public static String getDirectoryName() {
		return "result/" + directoryName;
	}

	public static int getOriginId() {
		return originIndices.get(totalNodes);
	}

	public static int getOriginId(int nodes) {
		return originIndices.get(nodes);
	}

	public static int getTotalCycles() {
		return totalCycles;
	}

	public static int getTotalContents() {
		return totalContents;
	}

	public static int getTotalAlgorithms() {
		return algorithmNames.size();
	}

	public static String getAlgorithmName(int algorithmId) {
		return algorithmNames.get(algorithmId);
	}

	public static int getRandomIntForRequest(int upperLimit) {
		return randomForRequest.nextInt(upperLimit);
	}

	public static int getRandomIntForCuckoo(int upperLimit) {
		return randomForCuckoo.nextInt(upperLimit);
	}

	public static double getRandomDoubleForCuckoo() {
		return randomForCuckoo.nextDouble();
	}

	public static int getRandomIntForRandom(int upperLimit) {
		return randomForRandom.nextInt(upperLimit);
	}

	public static int getRandomIntForFailure(int upperLimit) {
		return randomForFailure.nextInt(upperLimit);
	}

	public static double getRandomDoubleForFailure() {
		return randomForFailure.nextDouble();
	}

	public static int getRandomIntForParameters(int upperLimit) {
		return randomForParameters.nextInt(upperLimit);
	}

	public static double getRandomDoubleForParameters() {
		return randomForParameters.nextDouble();
	}

	/*
	 * 複製配置の際に使用
	 * 
	 * オリジンサーバから到達可能なノードの集合
	 * 
	 * かつ，該当コンテンツ分のストレージを持つノードの集合
	 */
	public static ArrayList<Integer> getAvailableNodes(int algorithmId, Content content) {
		ArrayList<Integer> reachableNodes = Flooding.getReachableNodes();
		ArrayList<Integer> availableNodes = new ArrayList<Integer>();

		for (int i = 0; i < reachableNodes.size(); i++) {
			SurrogateServer node = getNode(reachableNodes.get(i));
			if ((node.getStorageCapacity(algorithmId) - content.getSize()) >= 0) {
				availableNodes.add(node.getIndex());
			}
		}

		return new ArrayList<Integer>(availableNodes);
	}

	public static void increaseLackingProcessing(int algorithmId) {
		failedCounters.get(algorithmId).increaseProcessing();
	}

	public static void increaseLackingTransmission(int algorithmId) {
		failedCounters.get(algorithmId).increaseTransmission();
	}

	public static void closeFile() {
		for (FailedReason element : failedCounters) {
			element.closeFile();
		}
	}

	public boolean execute() {

		contentSet = new ArrayList<Content>();
		for (int index = 0; index < totalContents; index++) {
			contentSet.add(index, new Content(index));
		}

		return false;
	}
}

/**
 * FailedReason
 */
class FailedReason {
	private int totalCycles;
	private int[] lackProcessingCount;
	private int[] lackTransmissionCount;
	private PrintWriter lackProcessingWriter;
	private PrintWriter lackTransmissionWriter;

	public FailedReason(int totalCycles, String name) {
		this.totalCycles = totalCycles;

		lackProcessingCount = new int[totalCycles];
		lackTransmissionCount = new int[totalCycles];

		try {
			lackProcessingWriter = new PrintWriter(new BufferedWriter(new FileWriter(
					SharedData.getDirectoryName() + "/Cumulative_Lack_of_Processing[" + name + "].tsv", false)));
			lackTransmissionWriter = new PrintWriter(new BufferedWriter(new FileWriter(
					SharedData.getDirectoryName() + "/Cumulative_Lack_of_Transmission[" + name + "].tsv", false)));
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	public void increaseProcessing() {
		lackProcessingCount[CDState.getCycle()]++;
	}

	public void increaseTransmission() {
		lackTransmissionCount[CDState.getCycle()]++;
	}

	public void closeFile() {
		int totalProcessing = 0;
		int totalTransmission = 0;

		for (int i = 0; i < totalCycles; i++) {
			totalProcessing += lackProcessingCount[i];
			totalTransmission += lackTransmissionCount[i];

			lackProcessingWriter.println((i + 1) + "\t" + totalProcessing);
			lackTransmissionWriter.println((i + 1) + "\t" + totalTransmission);
		}

		lackProcessingWriter.close();
		lackTransmissionWriter.close();
	}
}