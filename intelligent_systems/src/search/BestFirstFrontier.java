package search;

import java.util.PriorityQueue;

public class BestFirstFrontier implements Frontier {

	protected int max;
	protected int seen;
	protected NodeFunction f;
	protected PriorityQueue<Node> frontier;

	public BestFirstFrontier(NodeFunction f) {
		this.f = f;
		max = 0;
		seen = 0;
		frontier = new PriorityQueue<Node>(1000,f);
	}

	public void add(Node node) {
		frontier.add(node);
		seen++;
		// potentially increase the max value
		if (this.frontier.size() > max) {
			max = this.frontier.size();
		}
	}

	public Node remove() throws FrontierException {
		return frontier.poll();
	}

	public boolean isEmpty() {
		return frontier.size() == 0;
	}
	public void clear() {
		frontier.clear();
	}
	public int maxSeen() {
		return max;
	}
	public int seen() {
		return seen;
	}
	public void clearMaxSeen() {
		max = 0;
	}
	public void clearSeen() {
		seen = 0;
	}
}