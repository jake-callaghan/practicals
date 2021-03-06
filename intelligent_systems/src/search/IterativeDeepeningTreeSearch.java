package search;

public class IterativeDeepeningTreeSearch implements Search {
	
	protected DepthFirstFrontier frontier;

	public IterativeDeepeningTreeSearch() {
		frontier = new DepthFirstFrontier();
	}

	public int maxNodesInFrontier() {
		return frontier.maxSeen();
	}

	public int nodesGenerated() {
		return frontier.seen();
	}

	public Node findGoal(Node root, GoalTest goal) {
		int depth = 0;
		frontier.clearMaxSeen();
		frontier.clearSeen();
		while (true) {	
			frontier.clear();
			frontier.add(root);
			
			while (!frontier.isEmpty()) {
				try {
					Node node = frontier.remove();
					if (depth > node.depth) {
						if (goal.isGoal(node.state)) {
							return node;
						}
						for (Action action : node.state.getApplicableActions()) {
							State newState = node.state.getActionResult(action);
							Node newNode = new Node(node,action,newState,(node.depth)+1,0,0);
							frontier.add(newNode); 
						}
					} else {
						if (goal.isGoal(node.state)) { return node; }
					}
				} catch (FrontierException fe) {
					return null;
				}
			}
			depth++;	// no solution -> increment the depth!
		}
	}
}
