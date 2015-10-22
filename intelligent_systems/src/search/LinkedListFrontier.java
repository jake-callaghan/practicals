package search;
import java.util.LinkedList;

/** a linked list based data-structure for frontiers */
public abstract class LinkedListFrontier implements Frontier {

	// stores the nodes in the frontier
	protected LinkedList<Node> frontier; 

	public LinkedListFrontier() {
		clear();
	}

	public boolean isEmpty() {
		return frontier.isEmpty();
	}

	public void clear() {
		frontier = new LinkedList<Node>();
	}
	
	public void add(Node node) {
		frontier.add(node);
	}

	/* this differs depending on FIFO / LIFO */
	abstract public Node remove() throws FrontierException;

}