package unravel.misc;

import unravel.mcdfg.Node;

public abstract class PathData {

	// attributes
	int entryRank;
	Node targetNode;
	int targetRank;
	int targetIndex;
	Node controlNode;
	int controlRank;
	int controlIndex;

	/*
	 * Main constructor
	 */
	public PathData(int entryRank,  Node targetNode, int targetRank, int targetIndex,
			Node controlNode, int controlRank, int controlIndex) {
		update(entryRank, targetNode, targetRank, targetIndex,
				controlNode, controlRank, controlIndex);
	}

	abstract public boolean betterThan(int entryRank, int targetRank, int controlRank);

	public PathData update(int entryRank,  Node targetNode, int targetRank, int targetIndex,
			Node controlNode, int controlRank, int controlIndex) {
		this.entryRank = entryRank;
		this.targetNode = targetNode;
		this.targetRank = targetRank;
		this.targetIndex = targetIndex;
		this.controlNode = controlNode;
		this.controlRank = controlRank;
		this.controlIndex = controlIndex;
		return this;
	}

	public int getEntryRank() {
		return entryRank;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public int getTargetRank() {
		return targetRank;
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public Node getControlNode() {
		return controlNode;
	}

	public int getControlRank() {
		return controlRank;
	}

	public int getControlIndex() {
		return controlIndex;
	}

	public void setEntryRank(int entryRank) {
		this.entryRank = entryRank;
	}
}
