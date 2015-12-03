package npuzzle;

import search.*;

public class Test {

	public static void main(String[] args) {

		NPuzzlePrinting print = new NPuzzlePrinting();

		Tiles initState = new Tiles(new int[][] {
			{ 7, 4, 2 },
			{ 8, 1, 3 },
			{ 5, 0, 6 }
		});

		Node root = new Node(null,null,initState,0,0,0);

		GoalTest goal = new TilesGoalTest();

		Frontier bfs_f = new BreadthFirstFrontier();
		Search bfst = new TreeSearch(bfs_f);
		Search bfsg = new GraphSearch(bfs_f);

		Frontier dfs_f = new DepthFirstFrontier();
		Search dfst = new TreeSearch(dfs_f);
		Search dfsg = new GraphSearch(dfs_f);

		Search iter = new IterativeDeepeningTreeSearch();

		System.out.println("--- breadth first tree search ---");

		Node s = bfst.findGoal(root,goal);
		// print.printSolution(s);
		System.out.println("max = "+bfst.maxNodesInFrontier());
		System.out.println("total = "+bfst.nodesGenerated());

		System.out.println("--- breadth first graph search ---");
		s = bfsg.findGoal(root,goal);
		//print.printSolution(s);
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
		// print.printSolution(s);
		System.out.println("max = "+dfsg.maxNodesInFrontier());
		System.out.println("total = "+dfsg.nodesGenerated());

		System.out.println("--- iterative deepening tree search ---");
		s = iter.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+iter.maxNodesInFrontier());
		System.out.println("total = "+iter.nodesGenerated());

	}

}
