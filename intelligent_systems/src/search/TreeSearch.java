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
		while (!frontier.isEmpty()) {
			try {
				Node node = frontier.remove();
				// apply applicable actions to get resulting states
				// return any if they satisfy the goal
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					Node newNode = new Node(node,action,newState);
					// goal state?
					if (goalTest.isGoal(newState)) { return newNode; }
					// non-goal -> add to the frontier
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

}