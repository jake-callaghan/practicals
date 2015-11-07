package search;

public class ValuedNode extends Node {
	public final int value;

	public ValuedNode(Node parent, Action action, State state, int depth, int value) {
		super(parent,action,state,depth);
		this.value = value;
	}
}