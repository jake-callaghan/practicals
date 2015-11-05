package search;
import java.util.LinkedList;

/** a linked list based data-structure for frontiers */
public abstract class LinkedListFrontier implements Frontier {

	// stores the nodes in the frontier
	protected LinkedList<Node> frontier; 

	// stores the max number of nodes seen since last clearMaxSeen()
	protected int max;

	// stores the number of nodes seen since last clear
	protected int seen;

	public LinkedListFrontier() {
		frontier = new LinkedList<Node>();
		clearMaxSeen();
		clearSeen();
	}

	public int maxSeen() {
		return max;
	}

	public int seen() {
		return seen;
	}

	public boolean isEmpty() {
		return frontier.isEmpty();
	}

	public void clear() {
		frontier.clear();
	}

	public void clearMaxSeen() {
		max = 0;
	}

	public void clearSeen() {
		seen = 0;
	}
	
	// these can be implemented to create the desired type
	// of frontier
	// they MUST handle max and seen accordingly
	abstract public void add(Node node);
	abstract public Node remove() throws FrontierException;

}