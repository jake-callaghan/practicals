package search;

import java.util.HashSet;

public class GraphSearch implements Search {
	
	protected Frontier frontier;
	protected HashSet<State> pastStates;

	public GraphSearch(Frontier frontier) {
		pastStates = new HashSet<State>();
		this.frontier = frontier;
	}

	public Node findGoal(Node root, GoalTest goalTest) {
		frontier.clear();
		frontier.clearMaxSeen();
		pastStates.clear();

		frontier.add(root);
		pastStates.add(root.state);

		while (!frontier.isEmpty()) {
			try {
				Node node = frontier.remove();
				// apply each action, return any goals or add unvisited nodes
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					Node newNode = new Node(node,action,newState,-1);
					// goal state?
					if (goalTest.isGoal(newState)) { return newNode; }
					// have we not seen this state before?
					if (!pastStates.contains(newState)) {
						pastStates.add(newState);
						frontier.add(newNode);
					}
				}	
			} catch (FrontierException fe) {
				return null;
			}
		}
		return null;	// failed to satisfy GoalTest
	}

	public int maxNodesInFrontier() {
		return frontier.maxSeen();
	}

}