package unravel.mcdfg;

import java.util.UUID;
import unravel.misc.Identifiable;

/**
 *
 */
public abstract class Edge implements Identifiable {

	// attributes

	final private String uuid;
	private Node startingNode;
	private Node landingNode;

	/**
	 * Main constructor
	 */
	public Edge(Node startingN, Node landingN) {
		uuid = UUID.randomUUID().toString();
		startingNode = startingN;
		landingNode = landingN;
	}

	/*
	 * Copy constructor
	 */
	public Edge(Edge e) {
		uuid = UUID.randomUUID().toString();
		startingNode = e.getStartingNode();
		landingNode = e.getLandingNode();
	}

	// methods
	abstract public Edge getCopy();
	abstract public String getLabel();

	@Override
	public String getUuid() {
		return uuid;
	}

	public void print() {
		System.out.println(getLabel());
	}

	public Node getStartingNode() {
		return startingNode;
	}

	public Node getLandingNode() {
		return landingNode;
	}

	public void setStartingNode(Node n) {
		startingNode = n;
	}

	public void setLandingNode(Node n) {
		landingNode = n;
	}

	public abstract boolean isIdenticalTo(Edge e);
}