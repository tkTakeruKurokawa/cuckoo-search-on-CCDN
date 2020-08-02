package main;

import peersim.core.*;
import peersim.config.*;

public class SharedData implements Control {
	private static final String PAR_LNK = "link";
	private static int pid_lnk;

	public SharedData(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_LNK);
	}

	public static Link getLink(Node node) {
		return (Link) node.getProtocol(pid_lnk);
	}

	public boolean execute() {
		return false;
	}
}