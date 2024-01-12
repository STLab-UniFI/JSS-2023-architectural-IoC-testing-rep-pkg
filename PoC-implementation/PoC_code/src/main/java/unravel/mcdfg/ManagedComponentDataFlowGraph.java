package unravel.mcdfg;

import java.util.List;

import java.util.ArrayList;

/**
 * 
 */
public class ManagedComponentDataFlowGraph {
	
	// attributes
    
	private List<Node> nodes;
	private List<Edge> edges;
	private List<ManagedComponent> managedComponents;

	/**
	 * Default constructor
	 */
	public ManagedComponentDataFlowGraph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		managedComponents = new ArrayList<ManagedComponent>();
	}
	
	// methods
	
	public void print() {
		ArrayList<Node> visited = new ArrayList<Node>();
		
		print_r(nodes.get(0), visited);
	}
	
	private void print_r(Node node, ArrayList<Node> visited) {
		visited.add(node);
		int index = nodes.indexOf(node);
		System.out.print("   (" + index + ")");
		node.print();
		boolean first = true;
		
		for (Edge edge : node.getOuterEdges()) {
			if (first) {
				first = false;

				System.out.println("");
				System.out.println("    |");
				System.out.print("    |  ");
			}
			else {
				System.out.println();
				System.out.println("(" + index + ")");
				System.out.println(" |");
				System.out.println("  \\");
				System.out.print("   \\  ");
			}

			edge.print();
			Node next = edge.getLandingNode();
			
			if (visited.contains(next) == false) {
				System.out.println("    |");
				System.out.println("    V");
				System.out.println("");
				
				print_r(next, visited);
			}
			else {
				System.out.println("    \\");
				System.out.println("     -----> (" + nodes.indexOf(next) + ")");
				System.out.println("");
			}
		}
	}
	
	public boolean contains(Node n) {
		if (nodes.contains(n)) {
			return true;
		}
		return false;
	}
	
	public boolean contains(Edge e) {
		if (edges.contains(e)) {
			return true;
		}
		return false;
	}
	
	public boolean contains(ManagedComponent mc) {
		if (managedComponents.contains(mc)) {
			return true;
		}
		return false;
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<ManagedComponent> getManagedComponents() {
		return managedComponents;
	}
	
	public ManagedComponent getManagedComponent(String name) {
		for (ManagedComponent mc : managedComponents) {
			if (mc.getName().equals(name)) {
				return mc;
			}
		}
    	return null;
    }

	public void addNode(Node n) {
		nodes.add(n);
	}

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public void addManagedComponent(ManagedComponent mc) {
		managedComponents.add(mc);
	}

	public boolean removeNode(Node n) {
		return nodes.remove(n);
	}

	public boolean removeEdge(Edge e) {
		return edges.remove(e);
	}
}