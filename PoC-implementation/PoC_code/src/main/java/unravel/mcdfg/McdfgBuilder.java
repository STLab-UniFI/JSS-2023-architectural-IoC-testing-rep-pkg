package unravel.mcdfg;

import java.util.List;
import java.util.LinkedList;
import unravel.mcdfg.Entry.Type;
import unravel.cdi.*;

/**
 *
 */
public class McdfgBuilder {

	static public class Settings {

		public boolean transientConversations = true;

		public Settings() {
			this(null);
		}

		public Settings(Settings s) {
			if (s != null) {
				transientConversations = s.transientConversations;
			}
		}
	}

	// attributes

	private ManagedComponentDataFlowGraph mcdfg;
	private McdfgValidator v;
	private Settings settings;

    /**
     * Default constructor
     */
    public McdfgBuilder() {
    	this(new Settings());
    }

    /**
     * Settings constructor
     */
    public McdfgBuilder(Settings s) {
    	mcdfg = new ManagedComponentDataFlowGraph();
    	v = new McdfgValidator();
    	settings = new Settings(s);
    }

	// methods

    public ManagedComponentDataFlowGraph buildMcdfg() {
    	return mcdfg;
    }

    public void eraseMcdfg() {
    	mcdfg = new ManagedComponentDataFlowGraph();
    }

	public Node addNode() {
    	Node n = new Node();
    	mcdfg.addNode(n);
    	return n;
	}

	public Node addNodeByCopy(Node n) {
		if (mcdfg.contains(n) && v.validate(n)) {
			Node copy = new Node();
			copy.setStatus(n.getStatus().getCopy());
			mcdfg.addNode(copy);
			return copy;
		}
		return null;
	}

	public boolean removeNode(Node n) {
		if (mcdfg.contains(n) && v.validate(n)) {
			for (Edge e : n.getInnerEdges()) {
				backwardCleanUp(e, 1);
			}
			for (Edge e : n.getOuterEdges()) {
				forwardCleanUp(e, 1);
			}
			return mcdfg.removeNode(n);
		}
		return false;
	}

	public Node splitNode(Node n1, int index) {
		if (mcdfg.contains(n1) && v.validate(n1) && 0 <= index &&
				index < n1.getEntries().size() - 1) {
			Node n0 = addNodeByCopy(n1);
			for (int i = 0; i < index + 1; i++) {
				addEntryByCopy(n0, n1.getEntries().get(0));
				removeEntry(n1, 0);
			}
			for (Edge e : n1.getInnerEdges()) {
				n0.addInnerEdge(e);
				e.setLandingNode(n0);
			}
			n1.getInnerEdges().clear();
			addSimpleEdge(n0, n1);
		}
		return n1;
	}

	public SimpleEdge addSimpleEdge(Node startingN, Node landingN) {
		if (mcdfg.contains(startingN) && mcdfg.contains(landingN) &&
				v.validate(startingN) && v.validate(landingN)) {
			SimpleEdge se = new SimpleEdge(startingN, landingN);
			startingN.addOuterEdge(se);
			landingN.addInnerEdge(se);
			mcdfg.addEdge(se);
			return se;
		}
		return null;
	}

	public NavigationEdge addNavigationEdge(Node startingN, Node landingN,
			ManagedComponent mc, String method, String[] arguments) {
		if (mcdfg.contains(startingN) && mcdfg.contains(landingN) &&
				mcdfg.contains(mc) && v.validate(startingN) && v.validate(landingN) &&
				v.validate(mc, method, arguments)) {
			NavigationEdge ne =
					new NavigationEdge(startingN, landingN, mc, method, arguments);
			startingN.addOuterEdge(ne);
			landingN.addInnerEdge(ne);
			mcdfg.addEdge(ne);
			return ne;
		}
		return null;
	}

	public ContextBoundaryEdge addContextBoundaryEdge(Node startingN, Node landingN,
			Scope s, Action a) {
		if (mcdfg.contains(startingN) && mcdfg.contains(landingN) &&
				v.validate(startingN) && v.validate(landingN)) {
			ContextBoundaryEdge cbe =
					new ContextBoundaryEdge(startingN, landingN, s, a);
			startingN.addOuterEdge(cbe);
			landingN.addInnerEdge(cbe);
			mcdfg.addEdge(cbe);
			return cbe;
		}
		return null;
	}

	public Edge addEdgeByCopy(Node startingN, Node landingN, Edge e) {
		if (mcdfg.contains(startingN) && mcdfg.contains(landingN) &&
				mcdfg.contains(e) && v.validate(startingN) && v.validate(landingN) &&
				v.validate(e)) {
			Edge copy = e.getCopy();
			copy.setStartingNode(startingN);
			copy.setLandingNode(landingN);
			startingN.addOuterEdge(copy);
			landingN.addInnerEdge(copy);
			mcdfg.addEdge(copy);
		}
		return null;
	}

	public ManagedComponent addManagedComponent(String name, Scope s) {
		ManagedComponent mc = new ManagedComponent(name, s);
		mcdfg.addManagedComponent(mc);
		return mc;
    }

	public boolean addDef(Node n, ManagedComponent mc) {
		if (mcdfg.contains(n) && mcdfg.contains(mc) && v.validate(n) &&
				v.validate(mc)) {
			Entry def = new Entry(Type.DEF, mc);
			n.addEntry(def);
			Context ctx = n.getContext(mc.getScope());
			if (ctx != null) {
				ctx.addDefinedComponent(mc);
				return true;
			}
		}
		return false;
	}

	public boolean addUse(Node n, ManagedComponent mc) {
		if (mcdfg.contains(n) && mcdfg.contains(mc) && v.validate(n) &&
				v.validate(mc)) {
			Entry use = new Entry(Type.USE, mc);
			n.addEntry(use);
			Context ctx = n.getContext(mc.getScope());
			if (ctx != null) {
				return true;
			}
		}
		return false;
	}

	public boolean addDefUse(Node n, ManagedComponent mc) {
		if (mcdfg.contains(n) && mcdfg.contains(mc) && v.validate(n) &&
				v.validate(mc)) {
			Entry defUse = new Entry(Type.DEF_USE, mc);
			n.addEntry(defUse);
			Context ctx = n.getContext(mc.getScope());
			if (ctx != null) {
				ctx.addDefinedComponent(mc);
				return true;
			}
		}
		return false;
	}

	public boolean addEntryByCopy(Node n, Entry e) {
		if (mcdfg.contains(n) && v.validate(n) && v.validate(e)) {
			Entry copy = e.getCopy();
			n.addEntry(copy);
			if (copy.getType() == Type.DEF || copy.getType() == Type.DEF_USE) {
				ManagedComponent mc = copy.getManagedComponent();
				Context ctx = n.getContext(mc.getScope());
				if (ctx != null) {
					ctx.addDefinedComponent(mc);
					return true;
				}
			}
		}
		return false;
	}

	public boolean removeEntry(Node n, int index) {
		if (0 <= index && index < n.getEntries().size()) {
			n.getEntries().remove(index);
			return true;
		}
		return false;
	}

	public boolean beginApplication(Node n) {
		return n.beginApplication();
	}

	public boolean endApplication(Node n) {
		return n.endApplication();
	}

	public boolean beginSession(Node n) {
		return n.beginSession();
	}

	public boolean endSession(Node n) {
		return n.endSession();
	}

	public boolean beginRequest(Node n) {
		boolean outcome = n.beginRequest();
		if (settings.transientConversations) {
			if (n.getConversation() == null) {
				outcome = outcome && n.beginConversation(Conversation.Type.TRANSIENT);
			}
		}
		return outcome;
	}

	public boolean endRequest(Node n) {
		boolean outcome = n.endRequest();
		if (settings.transientConversations) {
			Conversation c = n.getConversation();
			while (c != null && c.isTransient()) {
				outcome = outcome & n.endConversation();
				c = n.getConversation();
			}
		}
		return outcome;
	}

	public boolean beginConversation(Node n) {
		if (settings.transientConversations) {
			Conversation c = n.getConversation();
			if (c != null && c.isTransient()) {
				c.setType(Conversation.Type.LONG_RUNNING);
				return true;
			}
		}
		return n.beginConversation();
	}

	public boolean endConversation(Node n) {
		if (settings.transientConversations) {
			Conversation c = n.getConversation();
			if (c != null && c.isTransient()) {
				return false;
			}
		}
		return n.endConversation();
	}

	public boolean backwardCleanUp(Edge e, int range) {
		if (mcdfg.contains(e) && v.validate(e)) {
			boolean outcome = true;
			if (range > 0) {
				Node landingNode = e.getLandingNode();
				Node startingNode = e.getStartingNode();
				List<Edge> nextEdges = new LinkedList<>(startingNode.getInnerEdges());
				for (Edge next : nextEdges) {
					outcome = outcome && backwardCleanUp(next, range - 1);
				}
				outcome = outcome && startingNode.removeOuterEdge(e);
				outcome = outcome && landingNode.removeInnerEdge(e);
				outcome = outcome && mcdfg.removeEdge(e);
				if (startingNode.getInnerEdges().size() == 0 &&
						startingNode.getOuterEdges().size() == 0) {
					outcome = outcome && mcdfg.removeNode(startingNode);
				}
			}
			return outcome;
		}
		return false;
	}

	public boolean forwardCleanUp(Edge e, int range) {
		if (mcdfg.contains(e) && v.validate(e)) {
			boolean outcome = true;
			if (range > 0) {
				Node startingNode = e.getStartingNode();
				Node landingNode = e.getLandingNode();
				List<Edge> nextEdges = new LinkedList<>(landingNode.getOuterEdges());
				for (Edge next : nextEdges) {
					outcome = outcome && forwardCleanUp(next, range - 1);
				}
				outcome = outcome && landingNode.removeInnerEdge(e);
				outcome = outcome && startingNode.removeOuterEdge(e);
				outcome = outcome && mcdfg.removeEdge(e);
				if (landingNode.getOuterEdges().size() == 0 &&
						landingNode.getInnerEdges().size() == 0) {
					outcome = outcome && mcdfg.removeNode(landingNode);
				}
			}
			return outcome;
		}
		return false;
	}

	public ManagedComponent getManagedComponent(String name) {
    	return mcdfg.getManagedComponent(name);
    }

	public boolean isDefined(Node n, ManagedComponent mc) {
		if (mcdfg.contains(n) && mcdfg.contains(mc) && v.validate(n)
				&& v.validate(mc)) {
			Context ctx = n.getContext(mc.getScope());
			if (ctx != null && ctx.getDefinedComponents().contains(mc)) {
				return true;
			}
		}
		return false;
	}

	public boolean isContextActive(Node n, Scope s) {
		if (mcdfg.contains(n) && v.validate(n)) {
			Context ctx = n.getContext(s);
			if (ctx != null) {
				return true;
			}
		}
		return false;
	}
}