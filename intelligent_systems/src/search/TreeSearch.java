package search;

public class TreeSearch implements Search {
	
	protected Frontier frontier;

	public TreeSearch(Frontier frontier) {
		this.frontier = frontier;
	}

	public Node findGoal(Node root, GoalTest goalTest) {
		frontier.clear();
		frontier.clearMaxSeen();
		frontier.add(root);
		frontier.clearSeen();
		while (!frontier.isEmpty()) {
			try {
				Node node = frontier.remove();
				// goal state?
				if (goalTest.isGoal(node.state)) { return node; }
				// apply applicable actions to get resulting states
				// return any if they satisfy the goal
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					Node newNode = new Node(node,action,newState,-1);
					frontier.add(newNode);
				}
			} catch (FrontierException fe) {
				return null;
			}
		}
		return null; // failed to satisfy GoalTest!
	} 

	public int maxNodesInFrontier() {
		return frontier.maxSeen();
	}

	public int nodesGenerated() {
		return frontier.seen();
	}

}
