package main;

import peersim.core.*;

import java.util.ArrayList;
import java.util.Random;
import peersim.config.*;

public class SharedData implements Control {
	private static final String PAR_LNK = "link";
	private static int pid_lnk;
	private static final String PAR_TOTAL_CONTENTS = "totalContents";
	private static int totalContents;

	private static Random random = new Random(1L);
	private static ArrayList<Content> contentSet;

	public SharedData(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_LNK);
		totalContents = Configuration.getInt(prefix + "." + PAR_TOTAL_CONTENTS);
	}

	public static ReplicaServer getNode(int nodeIndex) {
		return (ReplicaServer) Network.get(nodeIndex);
	}

	public static ArrayList<ReplicaServer> getAliveNodes() {
		ArrayList<ReplicaServer> aliveNodes = new ArrayList<ReplicaServer>();

		for (int nodeId = 0; nodeId < Network.size(); nodeId++) {
			ReplicaServer node = getNode(nodeId);
			if (node.getServerState()) {
				aliveNodes.add(node);
			}
		}

		return aliveNodes;
	}

	public static Link getLink(Node node) {
		return (Link) node.getProtocol(pid_lnk);
	}

	public static Content getContent(int contentId) {
		return contentSet.get(contentId);
	}

	public static int getRandomInt(int upperLimit) {
		return random.nextInt(upperLimit);
	}

	public static double getRandomDouble() {
		return random.nextDouble();
	}

	public boolean execute() {
		contentSet = new ArrayList<Content>();
		for (int index = 0; index < totalContents; index++) {
			contentSet.add(index, new Content(index));
		}

		return false;
	}
}