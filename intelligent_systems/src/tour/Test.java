package tour;

import search.*;

public class Test {

	/*
	• breadth-first tree search,
	• breadth-first graph search,
	• depth-first tree search,
	• depth-first graph search, and
	• iterative deepening tree search.
	*/

	public static void main(String[] args) {

		Cities romania = SetUpRomania.getRomaniaMapSmall();
		City startCity = romania.getState("Bucharest");
		GoalTest goal = new TourGoalTest(romania.getAllCities(), startCity);
		TourPrinting print = new TourPrinting();

		Node root = new Node(null,null,new TourState(startCity),0,0,0);
		Frontier bfs_f = new BreadthFirstFrontier();
		Search bfst = new TreeSearch(bfs_f);
		Search bfsg = new GraphSearch(bfs_f);
		Frontier dfs_f = new DepthFirstFrontier();
		Search dfst = new TreeSearch(dfs_f);
		Search dfsg = new GraphSearch(dfs_f);
		Search iter = new IterativeDeepeningTreeSearch();

		System.out.println("--- breadth first tree search ---");
		Node s = bfst.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+bfst.maxNodesInFrontier());
		System.out.println("total = "+bfst.nodesGenerated());

		System.out.println("--- breadth first graph search ---");
		s = bfsg.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+bfsg.maxNodesInFrontier());
		System.out.println("total = "+bfsg.nodesGenerated());

		/*
		System.out.println("--- depth first tree search ---");
		s = dfst.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+dfst.maxNodesInFrontier());
		System.out.println("total = "+dfst.nodesGenerated());
		*/
	
		System.out.println("--- depth first graph search ---");
		s = dfsg.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+dfsg.maxNodesInFrontier());
		System.out.println("total = "+dfsg.nodesGenerated());

		System.out.println("--- iterative deepening tree search ---");
		s = iter.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+iter.maxNodesInFrontier());
		System.out.println("total = "+iter.nodesGenerated());

	}

}
