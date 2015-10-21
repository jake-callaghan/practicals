package search;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

// -------------------------------------------------
// edit: goalTest moved to when expanding the state, 
// thus reducing time complexity by a factor b
// -------------------------------------------------

public class BreadthFirstTreeSearch {
	public static Node findSolution(State initialConfiguration, GoalTest goalTest) {
		Queue<Node> fifoQueue = new LinkedList<Node>();
		fifoQueue.add(new Node(null, null, initialConfiguration));
		while (!fifoQueue.isEmpty()) {
			Node node = fifoQueue.remove();
			//if (goalTest.isGoal(node.state))
			//	return node;
			//else {
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					Node newNode = new Node(node, action, newState);
					if (goalTest.isGoal(newState)) {
						return newNode;
					} else {
						fifoQueue.add(newNode);
					}
				}
			}
		}
		return null;
	}
}
