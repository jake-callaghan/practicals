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
		fifoQueue.add(new Node(null, null, initialConfiguration,-1,0,0));
		while (!fifoQueue.isEmpty()) {
			Node node = fifoQueue.remove();
			//if (goalTest.isGoal(node.state))
			//	return node;
			//else {
				for (Action action : node.state.getApplicableActions()) {
					State newState = node.state.getActionResult(action);
					Node newNode = new Node(node, action, newState,-1,0,0);
					// set the g(n)
					newNode.g = node.g + action.cost(node,newNode);
					if (goalTest.isGoal(newState)) {
						return newNode;
					} else {
						fifoQueue.add(newNode);
					}
				}
			//}
		}
		return null;
	}
}
