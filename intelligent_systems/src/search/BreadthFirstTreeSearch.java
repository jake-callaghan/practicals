package search;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstTreeSearch {
	public static Node findSolution(State initialConfiguration, GoalTest goalTest) {
		Queue<Node> fifoQueue = new LinkedList<Node>();
		fifoQueue.add(new Node(null, null, initialConfiguration));
		while (!fifoQueue.isEmpty()) {
			Node node = fifoQueue.remove();
			if (goalTest.isGoal(node.state))
				return node;
			else {
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					fifoQueue.add(new Node(node, action, newState));
				}
			}
		}
		return null;
	}
}
