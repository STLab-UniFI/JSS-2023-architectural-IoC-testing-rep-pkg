package unravel.misc;

import unravel.mcdfg.Node;

public class NodesOverEntriesPathData extends PathData {

	public NodesOverEntriesPathData(int entryRank, Node targetNode, int targetRank,
			int targetIndex, Node controlNode, int controlRank, int controlIndex) {
		super(entryRank, targetNode, targetRank, targetIndex, controlNode, controlRank,
				controlIndex);
	}

	@Override
	public boolean betterThan(int entryRank, int targetRank, int controlRank) {
		int thisSum = this.targetRank + this.controlRank;
		int otherSum = entryRank + controlRank;
		if (thisSum > otherSum || (thisSum == otherSum && this.entryRank > entryRank)) {
			return true;
		}
		return false;
	}

}
