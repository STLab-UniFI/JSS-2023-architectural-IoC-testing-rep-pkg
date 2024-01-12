package unravel.misc;

import unravel.mcdfg.Node;

public class EntriesOverNodesPathData extends PathData {

	public EntriesOverNodesPathData(int entryRank, Node targetNode, int targetRank,
			int targetIndex, Node controlNode, int controlRank, int controlIndex) {
		super(entryRank, targetNode, targetRank, targetIndex, controlNode, controlRank,
				controlIndex);
	}

	@Override
	public boolean betterThan(int entryRank, int targetRank, int controlRank) {
		int thisSum = this.targetRank + this.controlRank;
		int otherSum = entryRank + controlRank;
		if (this.entryRank > entryRank || (this.entryRank == entryRank &&
				thisSum > otherSum)) {
			return true;
		}
		return false;
	}

}
