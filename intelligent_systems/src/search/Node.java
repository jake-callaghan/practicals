package search;

public class Node {
	public final Node parent;
	public final Action action;
	public final State state;
	
	public Node(Node parent, Action action, State state) {
		this.parent = parent;
		this.action = action;
		this.state = state;
	}
}
