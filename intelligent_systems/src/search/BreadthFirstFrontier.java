package search;

/** a FIFO data-structure with all O(1) time operations */
public class BreadthFirstFrontier extends LinkedListFrontier {

	public BreadthFirstFrontier() {
		super();
	}

	public void add(Node node) {
		this.frontier.add(node);
		// update the seen value
		this.seen++;
		// potentially update the max value
		if (this.frontier.size() > max) {
			max = this.frontier.size();
		}
	}

	public Node remove() throws FrontierException {
		if (this.frontier.size() > 0) {
			return this.frontier.removeFirst();
		}
		throw new FrontierException("****** Cannot remove from an empty frontier *****");
	}

}