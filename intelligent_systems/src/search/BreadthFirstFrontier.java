package search;

/** a FIFO data-structure with all O(1) time operations */
public class BreadthFirstFrontier extends LinkedListFrontier {

	public BreadthFirstFrontier() {
		super();
	}

	public Node remove() throws FrontierException {
		if (this.frontier.size() > 0) {
			return this.frontier.removeFirst();
		}
		throw new FrontierException("****** Cannot remove from an empty frontier *****");
	}

}