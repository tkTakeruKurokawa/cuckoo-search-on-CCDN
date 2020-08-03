package main;

import java.util.ArrayList;
import java.util.HashMap;

import peersim.config.*;
import peersim.core.*;

public class Link implements Protocol, Linkable {
	private static final String PAR_TOTAL_ALGORITHM = "totalAlgorithms";
	private static int totalAlgorithms;
	private static final String PAR_MAX_TRANSMISSION_CAPACITY = "maxTransmissionCapacity";
	private final int maxTransmissionCapacity;

	protected Node[] neighbors;
	protected int len;
	private ArrayList<HashMap<Integer, Integer>> transmissionCapacityList;
	private HashMap<Integer, Integer> transmissionCapacity;

	public Link(String prefix) {
		neighbors = new Node[Network.size()];
		totalAlgorithms = Configuration.getInt(prefix + "." + PAR_TOTAL_ALGORITHM);
		maxTransmissionCapacity = Configuration.getInt(prefix + "." + PAR_MAX_TRANSMISSION_CAPACITY);
		len = 0;
	}

	public Object clone() {
		Link link = null;
		try {
			link = (Link) super.clone();
		} catch (CloneNotSupportedException e) {
		} // never happens
		link.neighbors = new Node[neighbors.length];
		System.arraycopy(neighbors, 0, link.neighbors, 0, len);
		link.len = len;
		link.transmissionCapacityList = new ArrayList<HashMap<Integer, Integer>>();

		for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
			// for (int i = 0; i < neighbors.length; i++) {
			// link.transmissionCapacity.put(i, link.maxTransmissionCapacity);
			// }
			link.transmissionCapacityList.add(new HashMap<Integer, Integer>());
		}
		return link;
	}

	public boolean contains(Node n) {
		for (int i = 0; i < len; i++) {
			if (neighbors[i] == n)
				return true;
		}
		return false;
	}

	public boolean addNeighbor(Node n) {
		for (int i = 0; i < len; i++) {
			if (neighbors[i] == n) {
				// System.out.println(len);
				return false;
			}
		}
		if (len == neighbors.length) {
			Node[] temp = new Node[3 * neighbors.length / 2];
			System.arraycopy(neighbors, 0, temp, 0, neighbors.length);
			neighbors = temp;
		}
		neighbors[len] = n;
		len++;
		initializeCapacity(n);
		System.out.println(n.getIndex());

		return true;
	}

	private void initializeCapacity(Node node) {
		for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
			HashMap<Integer, Integer> tmp = transmissionCapacityList.get(algorithmId);
			tmp.put(node.getIndex(), SharedData.getRandomInt(1000));
			transmissionCapacityList.set(algorithmId, tmp);
		}
	}

	public boolean removeNeighbor(int i) {
		neighbors[i] = neighbors[len - 1];
		neighbors[len - 1] = null;
		len--;
		// System.out.println(len);
		return true;
	}

	public Node getNeighbor(int i) {
		return neighbors[i];
	}

	public int degree() {
		return len;
	}

	public void pack() {
		if (len == neighbors.length)
			return;
		Node[] temp = new Node[len];
		System.arraycopy(neighbors, 0, temp, 0, len);
		neighbors = temp;
	}

	public void setTransmissionCapacity(int algorithmId, int neighborId, int value) {
		transmissionCapacityList.get(algorithmId).replace(neighborId, value);
	}

	public int getTransmissionCapacity(int algorithmId, int neighborId) {
		return transmissionCapacityList.get(algorithmId).get(neighborId);
	}

	public String toString() {
		if (neighbors == null)
			return "DEAD!";
		StringBuffer buffer = new StringBuffer();
		buffer.append("len=" + len + " maxlen=" + neighbors.length + " [");
		for (int i = 0; i < len; ++i) {
			buffer.append(neighbors[i].getIndex() + " ");
		}
		return buffer.append("]").toString();
	}

	public void onKill() {
		neighbors = new Node[100];
		len = 0;
	}

}
