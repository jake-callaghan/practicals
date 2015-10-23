package search;

/** a LIFO data-structure with all O(1) time operations */
public class DepthFirstFrontier extends LinkedListFrontier {

	public DepthFirstFrontier() {
		super();
	}

	public void add(Node node) {
		this.frontier.add(node);
	}

	public Node remove() throws FrontierException {
		if (frontier.size() > 0) {
			return this.frontier.removeLast();
		}
		throw new FrontierException("***** Cannot remove from an empty frontier *****");
	}
}