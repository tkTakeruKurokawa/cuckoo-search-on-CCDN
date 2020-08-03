package main;

import peersim.core.*;
import java.util.Random;
import peersim.config.*;

public class SharedData implements Control {
	private static final String PAR_LNK = "link";
	private static int pid_lnk;

	private static Random random = new Random();

	public SharedData(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_LNK);
	}

	public static ReplicaServer getNode(int nodeIndex) {
		return (ReplicaServer) Network.get(nodeIndex);
	}

	public static Link getLink(Node node) {
		return (Link) node.getProtocol(pid_lnk);
	}

	public static int getRandomInt(int upperLimit) {
		return random.nextInt(upperLimit);
	}

	public boolean execute() {
		return false;
	}
}