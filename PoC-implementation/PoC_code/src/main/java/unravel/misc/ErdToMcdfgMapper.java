package unravel.misc;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
//import java.util.Queue;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.LinkedList;
import unravel.erd.*;
import unravel.mcdfg.*;
import unravel.cdi.*;

public class ErdToMcdfgMapper {

	static public class Settings {

		public boolean cdiDefinition = false;
		public boolean nodeSplitting = true;
		public boolean minimizeEntriesOverNodes = true;

		public Settings() {
			this(null);
		}

		public Settings(Settings s) {
			if (s != null) {
				cdiDefinition = s.cdiDefinition;
				nodeSplitting = s.nodeSplitting;
				minimizeEntriesOverNodes = s.minimizeEntriesOverNodes;
			}
		}
	}

	private Settings settings;

	/**
	 * Default constructor
	 */
	public ErdToMcdfgMapper() {
		settings = new Settings();
	}

	/**
	 * Main constructor
	 */
	public ErdToMcdfgMapper(Settings s) {
		settings = new Settings(s);
	}

	/***********************************************************************************
	 * translates a given enriched robustness diagram (erd) to a managed component
	 * data flow graph (mcdfg).
	 *
	 ***********************************************************************************/
	public ManagedComponentDataFlowGraph translate(EnrichedRobustnessDiagram erd,
			McdfgBuilder b) {

		b.eraseMcdfg();

		/* bo2n is a map that connects erd boundary objects to lists of mcdfg nodes, it
		 * is necessary during loop management, to get all the nodes associated to a
		 * given boundary object
		 */
		Map<BoundaryObject, List<Node>> bo2n = new HashMap<>();

		/* waiting is a list of pairs of boundary objects and nodes, used to keep track
		 * of the boundary objects that can be expanded on a new iteration, and their
		 * relative node from which to resume the expansion. it can be a queue or a
		 * stack, this choice will determine the strategy of the expansion,
		 * respectively a breadth-first or a depth-first approach
		 */
		//Deque<Pair<BoundaryObject, Node>> waiting = new LinkedList<>();
		Deque<Pair<BoundaryObject, Node>> waiting = new ArrayDeque<>();

		// get actor and boundary object from which the translation will begin
		Actor a = erd.getDefaultActor();

		// invalid actor exception
		if (a == null) {
			return null;
		}

		BoundaryObject bo0 = a.getDefaultBoundaryObject();

		// invalid boundary object exception
		if (bo0 == null) {
			return null;
		}

		// create the first node
		Node n0 = b.addNode();
		b.beginApplication(n0);
		b.beginSession(n0);

		// add defs for the elements that are assumed to be pre-existing
		for (Entity e : erd.getPreexisting()) {
			define(e, n0, b);
		}

		// add the current pair to the waiting list
		waiting.add(new Pair<BoundaryObject, Node>(bo0, n0));
		//waiting.addFirst(new Pair<BoundaryObject, Node>(bo0, n0));

		// main loop
		while (waiting.isEmpty() == false) {
			// extract the components from the pair
			Pair<BoundaryObject, Node> p = waiting.remove();
			//Pair<BoundaryObject, Node> p = waiting.removeFirst();
			BoundaryObject bo1 = p.getA();
			Node n1 = p.getB();

			// phase 1: definition
			for (RequestEdge re : bo1.getOuterRequestEdges()) {
				for (RequestController rqstCtr : re.getRequestControllers()) {
					define(rqstCtr, n1, b);
				}
			}

			// phase 2: initialization (optional)
			for (InitializationEdge ie : bo1.getInitializationEdges()) {
				for (RequestController rqstCtr : ie.getRequestControllers()) {
					b.beginRequest(n1);
					define(rqstCtr, n1, b);
					n1 = expand(rqstCtr, n1, b);
					b.endRequest(n1);
				}
			}

			// get the nodes associated with the boundary object
			List<Node> bo2Nodes = bo2n.get(bo1);
			// confront the status of the nodes to get only the loop candidates
			List<Node> loopCandidates = new LinkedList<>();
			if (bo2Nodes != null) {
				for (Node boundaryNode : bo2Nodes) {
					if (n1.getStatus().equals(boundaryNode.getStatus())) {
						// loop candidate found
						loopCandidates.add(boundaryNode);
					}
				}
			}

			if (loopCandidates.isEmpty() == false) {
				// close the loop
				PathData bestMatch = manageLoop(n1, loopCandidates, bo2n, b);
				if (bestMatch == null) {
					/* bestMatch == null denotes that the entries of n2 are different
					 * from the entries of any other loop candidate, this implies that
					 * n2 has to be associated to bo2 as well
					 */
					bo2Nodes.add(n1);
				}
			}
			else {
				// add n1 to the list of nodes associated with bo1
				List<Node> bo1Nodes = bo2n.get(bo1);
				if (bo1Nodes == null) {
					// create a new list
					bo1Nodes = new LinkedList<>();
					bo2n.put(bo1, bo1Nodes);
				}
				bo1Nodes.add(n1);

				// phase 3: expansion
				for (RequestEdge re : bo1.getOuterRequestEdges()) {
					Node n2 = n1;
					Node n3 = null;
					for (RequestController rqstCtr : re.getRequestControllers()) {
						// create a new node by copying the status of n1
						n3 = b.addNodeByCopy(n2);
						// add an edge between n1 and n2
						link(n2, n3, rqstCtr, b);

						// expand
						b.beginRequest(n3);
						define(rqstCtr, n3, b);
						n3 = expand(rqstCtr, n3, b);
						b.endRequest(n3);

						n2 = n3;
					}

					// get the landing node of the edge
					BoundaryObject bo2 = re.getLandingBoundaryObject();

					// prepare a new iteration
					waiting.add(new Pair<BoundaryObject, Node>(bo2, n3));
					//waiting.addFirst(new Pair<BoundaryObject, Node>(bo2, n3));
				}
			}
		}
		// return the translated mcdfg
		return b.buildMcdfg();
	}

	/***********************************************************************************
	 * NOT USED
	 *
	 * makes a preliminary visit to the erd to retrieve all the entity hierarchies
	 *
	 ***********************************************************************************/
	/*private void retrieveDependencies(EnrichedRobustnessDiagramBuilder erdBuilder) {

		// initialize the data structures used to explore the graph
		List<BoundaryObject> visited = new LinkedList<>();
		Queue<BoundaryObject> waiting = new LinkedList<>();

		// get actor and boundary object from which the visit will begin
		Actor a = erdBuilder.getDefaultActor();

		// invalid actor exception
		if (a == null) {
			return;
		}

		BoundaryObject bo0 = a.getDefaultBoundaryObject();

		// invalid boundary object exception
		if (bo0 == null) {
			return;
		}

		waiting.add(bo0);

		while (waiting.isEmpty() == false) {
			// update the data structures
			BoundaryObject bo1 = waiting.remove();
			visited.add(bo1);

			// retrieve the hierarchies on the initialization edges
			for (InitializationEdge ie : bo1.getInitializationEdges()) {
				if (ie.getController() instanceof CallController) {
					r_retrieveDependencies((CallController) ie.getController(),
							erdBuilder);
				}
			}

			// retrieve the hierarchies on the request edges
			for (RequestEdge re : bo1.getOuterRequestEdges()) {
				if (re.getController() instanceof CallController) {
					r_retrieveDependencies((CallController) re.getController(),
							erdBuilder);
				}

				BoundaryObject bo2 = re.getLandingBoundaryObject();
				if (visited.contains(bo2) == false) {
					// prepare a new iteration
					waiting.add(bo2);
				}
			}
		}
	}*/

	/***********************************************************************************
	 * NOT USED
	 *
	 * retrieves the dependencies of the current entity through the call hierarchy.
	 *
	 ***********************************************************************************/
	/*private void r_retrieveDependencies(CallController ctr,
			EnrichedRobustnessDiagramBuilder erdBuilder) {

		// get the main entity, associated with the controller
		Entity entity = ctr.getEntity();

		for (CallEdge e : ctr.getOuterCallEdges()) {
			// get the sub controller, associated with the current edge
			Controller subCtr = e.getLandingController();

			if (subCtr instanceof CallController == false) {

				return;
			}

			CallController subCallCtr = (CallController) subCtr;
			// get the sub entity, associated with the sub controller
			Entity subEntity = subCallCtr.getEntity();

			if (entity.getDependencies().containsKey(subEntity) == false) {
				// add a dependency with the sub entity in the main entity
				erdBuilder.addDependency(entity, subEntity);
			}
			// iterate again with the sub controller
			r_retrieveDependencies(subCallCtr, erdBuilder);
		}
	}*/

	/***********************************************************************************
	 * wrapper method for r_define.
	 *
	 ***********************************************************************************/
	private void define(Entity e, Node n, McdfgBuilder b) {

		if (settings.cdiDefinition) {
			return;
		}
		// initialize the defined list
		List<Entity> defined = new LinkedList<>();
		// start the recursion
		r_define(e, n, defined, b);
	}

	private void define(RequestController rqstCtr, Node n, McdfgBuilder b) {

		if (settings.cdiDefinition) {
			return;
		}
		// initialize the defined list
		List<Entity> defined = new LinkedList<>();
		// start the recursion
		for (CallController callCtr : rqstCtr.getCallControllers()) {
			r_define(callCtr.getEntity(), n, defined, b);
		}
	}

	/***********************************************************************************
	 * recursively defines the managed components using the class hierarchy.
	 *
	 ***********************************************************************************/
	private void r_define(Entity e, Node n, List<Entity> defined, McdfgBuilder b) {

		// add the entity to the defined list
		defined.add(e);

		// check if the context is not active
		if (b.isContextActive(n, e.getScope()) == false) {
			return;
		}

		// get the managed component from the mcdfg
		ManagedComponent mc = b.getManagedComponent(e.getName());
		if (mc == null) {
			// add the managed component to the mcdfg
			mc = b.addManagedComponent(e.getName(), e.getScope());
		}
		if (b.isDefined(n, mc) == false) {
			// define the managed component
			b.addDef(n, mc);
		}

		// iterate on every sub entity that has not been defined yet
		for (Entity dependency : e.getDependencies().keySet()) {
			if (defined.contains(dependency) == false) {
				r_define(dependency, n, defined, b);
			}
		}
	}

	/***********************************************************************************
	 * TODO
	 *
	 ***********************************************************************************/
	private void link(Node n1, Node n2, RequestController rqstCtr, McdfgBuilder b) {
		if (rqstCtr.getCallControllers().isEmpty() == false) {
			// FIXME the navigation edge should be more informative
			CallController callCtr = rqstCtr.getCallControllers().get(0);
			Entity e = callCtr.getEntity();
			ManagedComponent mc = b.getManagedComponent(e.getName());
			if (mc == null) {
				mc = b.addManagedComponent(e.getName(), e.getScope());
			}
			b.addNavigationEdge(n1, n2, mc, callCtr.getMethod(),
					callCtr.getArguments());
		} else {
			b.addSimpleEdge(n1, n2);
		}
	}

	/***********************************************************************************
	 * wrapper method for r_expand.
	 *
	 ***********************************************************************************/
	private Node expand(RequestController rqstCtr, Node n, McdfgBuilder b) {
		for (CallController callCtr : rqstCtr.getCallControllers()) {
			n = r_expand(callCtr, n, rqstCtr, b);
		}
		return n;
	}

//	private Node expand(CallController pageCtr, Node n1, McdfgBuilder b) {
//		return r_expand(pageCtr, n1, pageCtr, b);
//	}

	/***********************************************************************************
	 * translates the current request with a recursive visit of the call hierarchy.
	 *
	 ***********************************************************************************/
	private Node r_expand(CallController callCtr, Node n1, RequestController rqstCtr,
			McdfgBuilder b) {

		// get the entity from the call controller
		Entity e = callCtr.getEntity();

		// check if the context is not active
		if (b.isContextActive(n1, e.getScope()) == false) {
			return n1;
		}

		// get the managed component from the mcdfg
		ManagedComponent mc = b.getManagedComponent(e.getName());
		if (mc == null) {
			// add the managed component to the mcdfg
			mc = b.addManagedComponent(e.getName(), e.getScope());
		}

		// add the proper entry
		if (b.isDefined(n1, mc) == false) {
			// define the managed component
			b.addDefUse(n1, mc);
		} else {
			// use the managed component
			b.addUse(n1, mc);
		}

		// iterate on every sub controller
		for (CallEdge ce : callCtr.getCallEdges()) {
			Controller ctr = ce.getLandingController();
			if (ctr instanceof CallController) {
				n1 = r_expand((CallController) ctr, n1, rqstCtr, b);
			} else if (ctr instanceof ContextController) {
				n1 = manageContextBoundary((ContextController) ctr, n1, rqstCtr, b);
			}
		}
		return n1;
	}

	/***********************************************************************************
	 * manages the context boundary.
	 *
	 ***********************************************************************************/
	private Node manageContextBoundary(ContextController ctxCtr, Node n1,
			RequestController rqstCtr, McdfgBuilder b) {

		// create a new node by copying the status of n1
		Node n2 = b.addNodeByCopy(n1);

		// manage the context boundary
		switch (ctxCtr.getScope()) {
		case SESSION:
			switch (ctxCtr.getAction()) {
				case BEGIN:
					// NOT SUPPORTED
					b.removeNode(n2);
					return n1;
				case END:
					// NOT SUPPORTED
					b.removeNode(n2);
					return n1;
				case END_BEGIN:
					// add an "end/begin session" context-boundary edge
					b.addContextBoundaryEdge(n1, n2, Scope.SESSION, Action.END_BEGIN);
					// refresh the session
					b.endSession(n2);
					b.beginSession(n2);
					// define the involved managed components
					define(rqstCtr, n2, b);
					break;
			}
			break;
		case CONVERSATION:
			switch (ctxCtr.getAction()) {
				case BEGIN:
					boolean endBegin = false;
					if (n1.getEntries().size() == 0 && n1.getInnerEdges().size() == 1) {
						Edge e = n1.getInnerEdges().get(0);
						if (e instanceof ContextBoundaryEdge) {
							ContextBoundaryEdge cbe = (ContextBoundaryEdge) e;
							if (cbe.getScope() == Scope.CONVERSATION &&
									cbe.getAction() == Action.END) {
								endBegin = true;
								b.addContextBoundaryEdge(e.getStartingNode(), n1,
										Scope.CONVERSATION, Action.END_BEGIN);
								b.removeNode(n2);
								b.forwardCleanUp(e, 1);
								n2 = n1;
							}
						}
					}
					if (endBegin == false) {
						// add a "begin conversation" context-boundary edge
						b.addContextBoundaryEdge(n1, n2, Scope.CONVERSATION, Action.BEGIN);
					}
					// open a new conversation
					b.beginConversation(n2);
					// define the involved managed components
					define(rqstCtr, n2, b);
					break;
				case END:
					// add an "end conversation" context-boundary edge
					b.addContextBoundaryEdge(n1, n2, Scope.CONVERSATION, Action.END);
					// close the last conversation
					b.endConversation(n2);
					break;
				case END_BEGIN:
					// add an "end/begin conversation" context-boundary edge
					b.addContextBoundaryEdge(n1, n2, Scope.CONVERSATION,
							Action.END_BEGIN);
					// refresh the last conversation
					b.endConversation(n2);
					b.beginConversation(n2);
					// define the involved managed components
					define(rqstCtr, n2, b);
					break;
			}
			break;
		default:
			// NOT SUPPORTED
			b.removeNode(n2);
			return n1;
		}
		return n2;
	}

	/***********************************************************************************
	 * closes the loop by a rank-based path evaluation.
	 *
	 ***********************************************************************************/
	private PathData manageLoop(Node initialTarget, List<Node> loopCandidates,
			Map<BoundaryObject, List<Node>> bo2n, McdfgBuilder b) {

		// get the bestMatch to close the loop
		PathData bestMatch = evaluatePaths(initialTarget, loopCandidates, bo2n);

		if (bestMatch != null) {
			// get the matching nodes
			Node startingNode = null; // bestMatch.getTargetNode();
			Node landingNode = null; // bestMatch.getControlNode();
			Node targetNode = bestMatch.getTargetNode();
			Node controlNode = bestMatch.getControlNode();

			// get the target info
			int targetRank = bestMatch.getTargetRank();

			int targetIndex = bestMatch.getTargetIndex();
			int controlIndex = bestMatch.getControlIndex();

			// manage the control section
			if (controlIndex == -1) {
				// control node not divergent (control-link)
				landingNode = controlNode;
			} else if (controlIndex < controlNode.getEntries().size() - 1) {
				// control node partially divergent (control-split)
				landingNode = b.splitNode(controlNode, controlIndex);
			} else {
				// control node entirely divergent (control-skip)
				landingNode = controlNode.getOuterEdges().get(0).getLandingNode();
			}

			// edge used later to clean up the unused path
			Edge cleanUpEdge = null;

			// manage the target section
			if (targetIndex == -1) {
				// target node not divergent (target-cut)
				cleanUpEdge = targetNode.getInnerEdges().get(0);
				startingNode = cleanUpEdge.getStartingNode();
				b.addEdgeByCopy(startingNode, landingNode, cleanUpEdge);
				List<Edge> targetInnerEdges = new LinkedList<>(targetNode.getInnerEdges());
				for (Edge e : targetInnerEdges) {
					if (e != cleanUpEdge) {
						b.addEdgeByCopy(e.getStartingNode(), landingNode, e);
						b.forwardCleanUp(e, 1);
					}
				}
				targetRank += 1;
			} else {
				startingNode = targetNode;
				if (targetRank > 0) {
					cleanUpEdge = startingNode.getOuterEdges().get(0);
				}
				if (targetIndex < targetNode.getEntries().size() - 1) {
					// target node partially divergent (target-split)
					while (targetIndex < targetNode.getEntries().size() - 1) {
						b.removeEntry(targetNode, targetIndex + 1);
					}
					b.addSimpleEdge(startingNode, landingNode);
				} else {
					// target node entirely divergent (target-keep)
					b.addEdgeByCopy(startingNode, landingNode, cleanUpEdge);
				}
			}

			// clean up the unused branch, if necessary
			if (cleanUpEdge != null) {
				b.forwardCleanUp(cleanUpEdge, targetRank);
			}
		}
		else {
			/* bestMatch = null means that there hasn't been any match between the
			 * target node and the loop candidates, but since every loop candidate share
			 * the same status with the target node, the target node can be connected to
			 * the successors of any loop candidate by the same navigation edges
			 */
			for (Edge e : loopCandidates.get(0).getOuterEdges()) {
				/* add an edge between the target node and the successor of the chosen
				 * loop candidate by copying the current edge
				 */
				b.addEdgeByCopy(initialTarget, e.getLandingNode(), e);
			}
		}
		return bestMatch;
	}

	/***********************************************************************************
	 * wrapper method for evaluatePaths.
	 *
	 ***********************************************************************************/
	private PathData evaluatePaths(Node targetNode, List<Node> controlNodes,
			Map<BoundaryObject, List<Node>> bo2n) {

		return r_evaluatePaths(0, targetNode, -1, -1, controlNodes, -1, -1, bo2n);
	}
	/***********************************************************************************
	 * recursively compares nodes and edges to find the match that will minimize
	 * the number of nodes used.
	 *
	 * returns bestMatch, that at the end of the recursion will contain:
	 * - entryRank: the total number of identical pairs of entries found
	 * - targetNode: the matching target node
	 * - targetRank: the total number of fully-visited target nodes
	 * - targetIndex: the index of the last matching entry in the target node
	 * - controlNode: the matching control node
	 * - controlRank: the total number of fully-visited control nodes
	 * - controlIndex: the index of the last matching entry in the control node
	 *
	 ***********************************************************************************/
	private PathData r_evaluatePaths(int rootEntryRank, Node rootTargetNode,
			int rootTargetRank, int rootTargetIndex, List<Node> rootControlNodes,
			int rootControlRank, int rootControlIndex,
			Map<BoundaryObject, List<Node>> bo2n) {

		// initialize the best data for the current iteration
		PathData bestMatch = null;
		int bestEntryRank = rootEntryRank;
		int bestTargetRank = rootTargetRank;
		int bestControlRank = rootControlRank;

		// check for a target boundary node (except for the first one)
		if (rootTargetRank == -1 || isBoundary(rootTargetNode, bo2n) == false) {
			for (Node rootControlNode : rootControlNodes) {
				// check for a control boundary node (except for the first one)
				if (rootControlRank == -1 ||
						isBoundary(rootControlNode, bo2n) == false) {
					// initialize the local data for the current branch
					int entryRank = rootEntryRank;
					Node targetNode = rootTargetNode;
					int targetRank = rootTargetRank;
					int targetIndex = rootTargetIndex;
					Node controlNode = rootControlNode;
					int controlIndex = rootControlIndex;
					int controlRank = rootControlRank;

					// if necessary, reset the indexes to the last valid position
					if (targetIndex < 0) {
						targetIndex = targetNode.getEntries().size() - 1;
						targetRank++;
					}
					if (controlIndex < 0) {
						controlIndex = controlNode.getEntries().size() - 1;
						controlRank++;
					}

					// compare the entries by a down-top approach
					boolean mismatch = false;
					while (mismatch == false && targetIndex >= 0 && controlIndex >= 0) {
						// compare the selected entries
						if (targetNode.getEntries().get(targetIndex).equals(
								controlNode.getEntries().get(controlIndex))) {
							// increase the entry rank
							entryRank += 1;
							// decrease the indexes
							targetIndex -= 1;
							controlIndex -= 1;
						}
						else {
							mismatch = true;
						}
					}

					// initialize the match object to null
					PathData match = null;

					if (mismatch == false) {
						/* if a mismatch has not occurred yet, then at least one of the
						 * nodes has reached its end, so iterate again on the
						 * predecessors of the fully-checked nodes, if possible
						 */
						Edge targetEdge = targetNode.getInnerEdges().get(0);
						Node nextTargetNode = null;
						List<Node> nextControlNodes = new LinkedList<>();
						boolean newRecursion = false;

						if (targetIndex < 0 && controlIndex < 0) {
							// both nodes have been fully checked
							for (Edge controlEdge : controlNode.getInnerEdges()) {
								// compare the two inner edges
								if (targetEdge.isIdenticalTo(controlEdge)) {
									/* in order to find a good candidate, the target
									 * node and the current control node must share an
									 * identical inner edge
									 */
									nextControlNodes.add(controlEdge.getStartingNode());
								}
							}

							if (nextControlNodes.isEmpty() == false) {
								nextTargetNode = targetEdge.getStartingNode();
								newRecursion = true;
							}
						}
						else if (targetIndex < 0) {
							// only the target node has been fully checked
							if (targetEdge instanceof SimpleEdge) {
								/* in order to find a good candidate, the target node
								 * must have an inner simple edge
								 */
								nextTargetNode = targetEdge.getStartingNode();
								nextControlNodes.add(controlNode);
								newRecursion = true;
							}
						}
						else if (controlIndex < 0) {
							// only the control node has been fully checked
							for (Edge controlEdge : controlNode.getInnerEdges()) {
								if (controlEdge instanceof SimpleEdge) {
									/* in order to find a good candidate, the current
									 * control node must have an inner simple edge
									 */
									nextControlNodes.add(controlEdge.getStartingNode());
								}
							}

							if (nextControlNodes.isEmpty() == false) {
								nextTargetNode = targetNode;
								newRecursion = true;
							}
						}

						if (newRecursion) {
							// call for a new recursion
							match = r_evaluatePaths(entryRank, nextTargetNode,
									targetRank, targetIndex, nextControlNodes,
									controlRank, controlIndex, bo2n);
						}
					}

					if (match == null && (entryRank > 0 ||
							(targetNode.getEntries().size() == 0 &&
							controlNode.getEntries().size() == 0))) {
						/* if no other match has been found yet, the current situation
						 * is the best one for this branch, so save it
						 */
						if (settings.minimizeEntriesOverNodes) {
							match = new EntriesOverNodesPathData(entryRank, targetNode,
									targetRank, targetIndex, controlNode, controlRank,
									controlIndex);
						} else {
							match = new NodesOverEntriesPathData(entryRank, targetNode,
									targetRank, targetIndex, controlNode, controlRank,
									controlIndex);
						}

						if (settings.nodeSplitting == false && match != null &&
								match.getControlIndex() > -1) {
							/* to prevent from splitting nodes, backtrack to the last
							 * fully-checked control node
							 */
							int range =
									match.getControlNode().getEntries().size() -
									match.getControlIndex() - 1;
							match = backtrack(match, range);
						}
					}

					// check if the new match (if found) is better than the current one
					if (match != null) {
						 if (match.betterThan(bestEntryRank, bestTargetRank,
								 bestControlRank)) {
							bestEntryRank = match.getEntryRank();
							bestTargetRank = match.getTargetRank();
							bestControlRank = match.getControlRank();
							bestMatch = match;
						}
					}
				}
			}
		}
		return bestMatch;
	}

	/***********************************************************************************
	 * checks if a node is associated with a boundaryObject
	 * TODO: the complexity can be improved using an extra data structure
	 *
	 ***********************************************************************************/
	private boolean isBoundary(Node n, Map<BoundaryObject, List<Node>> bo2n) {

		for (List<Node> list : bo2n.values()) {
			if (list.contains(n)) {
				return true;
			}
		}
		return false;
	}

	/***********************************************************************************
	 * returns a new pathData backtracked by a given number of entries
	 *
	 ***********************************************************************************/
	private PathData backtrack(PathData pathData, int range) {

		// get the path data
		int entryRank = pathData.getEntryRank();
		Node targetNode = pathData.getTargetNode();
		int targetRank = pathData.getTargetRank();
		int targetIndex = pathData.getTargetIndex();
		Node controlNode = pathData.getControlNode();
		int controlRank = pathData.getControlRank();
		int controlIndex = pathData.getControlIndex();

		// update the values
		entryRank -= range;
		targetIndex += range;
		controlIndex += range;

		// out of bounds check
		if (entryRank <= 0) {
			return null;
		}

		// target backtrack
		while (targetIndex >= targetNode.getEntries().size()) {
			targetIndex -= targetNode.getEntries().size();
			targetNode = targetNode.getOuterEdges().get(0).getLandingNode();
			targetRank -= 1;
		}

		// control backtrack
		while (controlIndex >= controlNode.getEntries().size()) {
			controlIndex -= controlNode.getEntries().size();
			controlNode = controlNode.getOuterEdges().get(0).getLandingNode();
			controlRank -= 1;
		}

		return pathData.update(entryRank, targetNode, targetRank, targetIndex,
				controlNode, controlRank, controlIndex);
	}
}
