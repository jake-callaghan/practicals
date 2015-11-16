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
		frontier.clearSeen();
		pastStates.clear();

		frontier.add(root);
		pastStates.add(root.state);

		while (!frontier.isEmpty()) {
			try {
				Node node = frontier.remove();
				// goal state?
				if (goalTest.isGoal(node.state)) { return node; }

				// apply each action, return any goals or add unvisited nodes
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					
					Node newNode = new Node(node,action,newState,-1,0,0);

					// set the g(n) value
					newNode.g = node.g + action.cost(node,newNode);
					
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

	public int nodesGenerated() {
		return frontier.seen();
	}

}
