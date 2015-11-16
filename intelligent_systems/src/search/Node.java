package search;

public class Node {
	public final Node parent;
	public final Action action;
	public final State state;
	public final int depth;
	public int value;
	public int g;
	
	public Node(Node parent, Action action, State state, int depth, int value, int g) {
		this.parent = parent;
		this.action = action;
		this.state = state;
		this.depth = depth;
		this.value = value;
		this.g = g;
	}
}
