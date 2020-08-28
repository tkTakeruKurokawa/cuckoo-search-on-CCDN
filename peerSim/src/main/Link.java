package main;

import java.util.ArrayList;
import java.util.HashMap;

import peersim.config.*;
import peersim.core.*;

public class Link implements Protocol, Linkable {
	private static final String PAR_MAX_TRANSMISSION_CAPACITY = "maxTransmissionCapacity";
	private final int maxTransmissionCapacity;

	private static int totalAlgorithms;

	protected Node[] neighbors;
	protected int len;
	private ArrayList<HashMap<Integer, Integer>> transmissionCapacityList;

	public Link(String prefix) {
		neighbors = new Node[Network.size()];
		maxTransmissionCapacity = Configuration.getInt(prefix + "." + PAR_MAX_TRANSMISSION_CAPACITY);

		totalAlgorithms = SharedData.getTotalAlgorithms();

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

		return true;
	}

	private void initializeCapacity(Node node) {
		for (int algorithmId = 0; algorithmId < totalAlgorithms; algorithmId++) {
			HashMap<Integer, Integer> tmp = transmissionCapacityList.get(algorithmId);
			tmp.put(node.getIndex(), maxTransmissionCapacity);
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

	public void resetTransmissionCapacity(int algorithmId, int neighborId) {
		transmissionCapacityList.get(algorithmId).replace(neighborId, maxTransmissionCapacity);
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
