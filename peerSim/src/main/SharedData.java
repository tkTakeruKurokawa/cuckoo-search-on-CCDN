package main;

import peersim.core.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import peersim.config.*;

public class SharedData implements Control {
	private static final String PAR_LNK = "link";
	private static int pid_lnk;
	private static final String PAR_ORIGIN_ID = "originId";
	private static int originId;
	private static final String PAR_TOTAL_CYCLES = "totalCycles";
	private static int totalCycles;
	private static final String PAR_TOTAL_CONTENTS = "totalContents";
	private static int totalContents;

	private static Random random;
	private static Random randomForCuckoo;
	private static Random randomForRandom;
	private static ArrayList<Content> contentSet;
	private static ArrayList<Content> replicatedContents;
	private static ArrayList<String> algorithmNames;

	public SharedData(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_LNK);
		originId = Configuration.getInt(prefix + "." + PAR_ORIGIN_ID);
		totalCycles = Configuration.getInt(prefix + "." + PAR_TOTAL_CYCLES);
		totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);

		File dir = new File("result/eps");
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public SharedData() {
		algorithmNames = new ArrayList<String>();
		algorithmNames.add("Cuckoo_Search");
		algorithmNames.add("Random");
		algorithmNames.add("Greedy");
		algorithmNames.add("Original_Only");

		random = new Random(1L);
		randomForCuckoo = new Random(2L);
		randomForRandom = new Random(3L);
	}

	public static ReplicaServer getNode(int nodeIndex) {
		return (ReplicaServer) Network.get(nodeIndex);
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

	public static int getOriginId() {
		return originId;
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

	public static int getRandomInt(int upperLimit) {
		return random.nextInt(upperLimit);
	}

	public static double getRandomDouble() {
		return random.nextDouble();
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

	public static ArrayList<Integer> getAvailableNodes(int algorithmId, Content content) {
		ArrayList<Integer> reachableNodes = Flooding.getReachableNodes();
		ArrayList<Integer> availableNodes = new ArrayList<Integer>();

		for (int i = 0; i < reachableNodes.size(); i++) {
			ReplicaServer node = getNode(reachableNodes.get(i));
			if ((node.getStorageCapacity(algorithmId) - content.getSize()) >= 0) {
				availableNodes.add(node.getIndex());
			}
		}

		return availableNodes;
	}

	public boolean execute() {

		contentSet = new ArrayList<Content>();
		for (int index = 0; index < totalContents; index++) {
			contentSet.add(index, new Content(index));
		}

		return false;
	}
}