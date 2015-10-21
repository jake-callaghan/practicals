package search;

import java.util.Stack;

public abstract class Printing {
	public void printSolution(Node solution) {
		if (solution == null)
			System.out.println("No solution found!");
		else {
			Stack<Node> stack = new Stack<Node>();
			Node node = solution;
			while (node != null) {
				stack.push(node);
				node = node.parent;
			}
			int stepNo = 0;
			while (!stack.isEmpty()) {
				node = stack.pop();
				System.out.print(stepNo++);
				if (node.parent == null)
					System.out.println(": start");
				else {
					System.out.print(": ");
					print(node.action);
					System.out.println();
				}
				System.out.println();
				print(node.state);
				System.out.println();
			}
		}
	}
	public abstract void print(Action action);
	public abstract void print(State state);
}
