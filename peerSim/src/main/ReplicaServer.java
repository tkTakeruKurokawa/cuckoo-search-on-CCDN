/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package main;

import peersim.config.*;
import peersim.core.Cleanable;
import peersim.core.CommonState;
import peersim.core.Fallible;
import peersim.core.Node;
import peersim.core.Protocol;

/**
 * This is the default {@link Node} class that is used to compose the
 * {@link Network}.
 */
public class ReplicaServer implements Node {

	// ================= fields ========================================
	// =================================================================

	/** used to generate unique IDs */
	private static long counterID = -1;

	/**
	 * The protocols on this node.
	 */
	protected Protocol[] protocol = null;

	/**
	 * The current index of this node in the node list of the {@link Network}. It
	 * can change any time. This is necessary to allow the implementation of
	 * efficient graph algorithms.
	 */
	private int index;

	/**
	 * The fail state of the node.
	 */
	protected int failstate = Fallible.OK;

	/**
	 * The ID of the node. It should be final, however it can't be final because
	 * clone must be able to set it.
	 */
	private long ID;

	private static final String PAR_TOTAL_CYCLE = "totalCycles";
	private static int totalCycles;
	private static final String PAR_TOTAL_ALGORITHM = "totalAlgorithms";
	private static int totalAlgorithms;
	private static final String PAR_MAX_STORAGE_CAPACITY = "maxStorageCapacity";
	private static int maxStorageCapacity;
	private static final String PAR_MAX_PROCESSING_CAPACITY = "maxProcessingCapacity";
	private static int maxProcessingCapacity;
	private static final String PAR_REPAIR_DURATION = "repairDuration";
	private static int repairDuration;

	private int storageCapacity[];
	private int processingCapacity[];
	private int position;

	private boolean serverState;
	private int progressCycle;

	// ================ constructor and initialization =================
	// =================================================================

	/**
	 * Used to construct the prototype node. This class currently does not have
	 * specific configuration parameters and so the parameter <code>prefix</code> is
	 * not used. It reads the protocol components (components that have type
	 * {@value peersim.core.Node#PAR_PROT}) from the configuration.
	 */
	public ReplicaServer(String prefix) {

		String[] names = Configuration.getNames(PAR_PROT);
		totalCycles = Configuration.getInt(prefix + "." + PAR_TOTAL_CYCLE);
		totalAlgorithms = Configuration.getInt(prefix + "." + PAR_TOTAL_ALGORITHM);
		maxStorageCapacity = Configuration.getInt(prefix + "." + PAR_MAX_STORAGE_CAPACITY);
		maxProcessingCapacity = Configuration.getInt(prefix + "." + PAR_MAX_PROCESSING_CAPACITY);
		repairDuration = Configuration.getInt(prefix + "." + PAR_REPAIR_DURATION);

		CommonState.setNode(this);
		ID = nextID();
		protocol = new Protocol[names.length];
		for (int i = 0; i < names.length; i++) {
			CommonState.setPid(i);
			Protocol p = (Protocol) Configuration.getInstance(names[i]);
			protocol[i] = p;
		}

	}

	// -----------------------------------------------------------------

	public Object clone() {

		ReplicaServer result = null;
		try {
			result = (ReplicaServer) super.clone();
		} catch (CloneNotSupportedException e) {
		} // never happens
		result.protocol = new Protocol[protocol.length];
		CommonState.setNode(result);
		result.ID = nextID();
		for (int i = 0; i < protocol.length; ++i) {
			CommonState.setPid(i);
			result.protocol[i] = (Protocol) protocol[i].clone();
		}

		result.storageCapacity = new int[totalAlgorithms];
		result.processingCapacity = new int[totalAlgorithms];
		for (int i = 0; i < totalAlgorithms; i++) {
			result.storageCapacity[i] = maxStorageCapacity;
			result.processingCapacity[i] = maxProcessingCapacity;
		}
		// result.position = SharedData.getRandomInt(totalCycles);
		result.position = 0;
		result.serverState = true;
		result.progressCycle = 0;

		return result;
	}

	// -----------------------------------------------------------------

	/** returns the next unique ID */
	private long nextID() {

		return counterID++;
	}

	// =============== public methods ==================================
	// =================================================================

	public void setFailState(int failState) {

		// after a node is dead, all operations on it are errors by definition
		if (failstate == DEAD && failState != DEAD)
			throw new IllegalStateException("Cannot change fail state: node is already DEAD");
		switch (failState) {
			case OK:
				failstate = OK;
				break;
			case DEAD:
				// protocol = null;
				index = -1;
				failstate = DEAD;
				for (int i = 0; i < protocol.length; ++i)
					if (protocol[i] instanceof Cleanable)
						((Cleanable) protocol[i]).onKill();
				break;
			case DOWN:
				failstate = DOWN;
				break;
			default:
				throw new IllegalArgumentException("failState=" + failState);
		}
	}

	// -----------------------------------------------------------------

	public int getFailState() {
		return failstate;
	}

	// ------------------------------------------------------------------

	public boolean isUp() {
		return failstate == OK;
	}

	// -----------------------------------------------------------------

	public Protocol getProtocol(int i) {
		return protocol[i];
	}

	// ------------------------------------------------------------------

	public int protocolSize() {
		return protocol.length;
	}

	// ------------------------------------------------------------------

	public int getIndex() {
		return index;
	}

	// ------------------------------------------------------------------

	public void setIndex(int index) {
		this.index = index;
	}

	// ------------------------------------------------------------------

	/**
	 * Returns the ID of this node. The IDs are generated using a counter (i.e. they
	 * are not random).
	 */
	public long getID() {
		return ID;
	}

	// ------------------------------------------------------------------

	public void setStorageCapacity(int algorithmId, int value) {
		storageCapacity[algorithmId] = value;
	}

	public int getStorageCapacity(int algorithmId) {
		return storageCapacity[algorithmId];
	}

	public void setProcessingCapacity(int algorithmId, int value) {
		processingCapacity[algorithmId] = value;
	}

	public int getProcessingCapacity(int algorithmId) {
		return processingCapacity[algorithmId];
	}

	public void setFailureRate(int startPosition) {
		position = startPosition;
	}

	public double getFailureRate() {
		return Parameters.getFailureRate(index, position);
	}

	public void proceedFailureRate() {
		position++;
		if (position == totalCycles) {
			position = 0;
		}
	}

	public int getPosition() {
		return position;
	}

	/*
	 * true => 生存, false => 故障中
	 */
	public void setServerState(boolean state) {
		serverState = state;
	}

	public boolean getServerState() {
		return serverState;
	}

	public void proceedProgressCycle() {
		progressCycle++;
		if (progressCycle == repairDuration) {
			progressCycle = 0;
			setServerState(true);
			setFailureRate(0);
		}
	}

	public double getAvailability() {
		return Parameters.getAvailability(index);
	}

	// ------------------------------------------------------------------

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ID: " + ID + " index: " + index + "\n");
		for (int i = 0; i < protocol.length; ++i) {
			buffer.append("protocol[" + i + "]=" + protocol[i] + "\n");
		}
		return buffer.toString();
	}

	// ------------------------------------------------------------------

	/** Implemented as <code>(int)getID()</code>. */
	public int hashCode() {
		return (int) getID();
	}

}
