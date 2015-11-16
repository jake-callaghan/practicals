package search;

public class AStarFunction extends NodeFunction {

	NodeFunction heuristic;

	public AStarFunction(NodeFunction h) {
		this.heuristic = h;
	}

	public int apply(Node node) {
		/* f(n) = h(n) + g(n) */
		return heuristic.apply(node) + node.g;
	}

}