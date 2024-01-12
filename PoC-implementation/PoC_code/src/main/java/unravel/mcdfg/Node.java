package unravel.mcdfg;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import unravel.misc.Identifiable;
import unravel.cdi.Scope;

/**
 *
 */
public class Node implements Identifiable {

	// attributes

	final private String uuid;
	private Status status;
	private List<Entry> entries;
	private List<Edge> innerEdges;
	private List<Edge> outerEdges;

	/**
	 * Default constructor
	 */
	public Node() {
		uuid = UUID.randomUUID().toString();
		status = new Status();
		entries = new ArrayList<Entry>();
		innerEdges = new ArrayList<Edge>();
		outerEdges = new ArrayList<Edge>();
	}

	// methods

	public void print() {
		if (innerEdges.size() > 1) {
			System.out.print("    [" + innerEdges.size() + " inner edges]");
		}
		System.out.println();
		System.out.println("      ------------------------------");

		for (Entry entry : entries) {
			System.out.println("      | " + entry.getLabel());
		}
		System.out.println("      ------------------------------");
	}

	public boolean beginApplication() {
		return status.beginApplication();
	}

	public boolean endApplication() {
		return status.endApplication();
	}

	public boolean beginSession() {
		return status.beginSession();
	}

	public boolean endSession() {
		return status.endSession();
	}

	public boolean beginRequest() {
		return status.beginRequest();
	}

	public boolean endRequest() {
		return status.endRequest();
	}

	public boolean beginConversation() {
		return status.beginConversation();
	}

	public boolean beginConversation(Conversation.Type t) {
		return status.beginConversation(t);
	}

	public boolean endConversation() {
		return status.endConversation();
	}

	public Context getContext(Scope s) {
		return status.getContext(s);
	}

	public Application getApplication() {
		return status.getApplication();
	}

	public Session getSession() {
		return status.getSession();
	}

	public Request getRequest() {
		return status.getRequest();
	}

	public Conversation getConversation() {
		return status.getConversation();
	}

	public String getLabel() {
		StringBuilder sb = new StringBuilder();
		for (Entry e : entries) {
			sb.append(e.getLabel());
			sb.append("\n");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public Status getStatus() {
		return status;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public List<Edge> getInnerEdges() {
		return innerEdges;
	}

	public List<Edge> getOuterEdges() {
		return outerEdges;
	}

	public void setStatus(Status s) {
		status = s;
	}

	public void addEntry(Entry e) {
		entries.add(e);
	}

	public void addInnerEdge(Edge e) {
		innerEdges.add(e);
	}

	public void addOuterEdge(Edge e) {
		outerEdges.add(e);
	}

	public boolean removeInnerEdge(Edge e) {
		return innerEdges.remove(e);
	}

	public boolean removeOuterEdge(Edge e) {
		return outerEdges.remove(e);
	}
}