package npuzzle;

public class Test {

	/*
	breadth-first tree search,
	• breadth-first graph search,
	• depth-first tree search,
	• depth-first graph search, and
	• iterative deepening tree search (optional).
	*/

	public static void main(String[] args) {

		NPuzzlePrinting print = new NPuzzlePrinting();

		Tiles initState = new Tiles(new int[][] {
			{ 7, 4, 2 },
			{ 8, 1, 3 },
			{ 5, 0, 6 }
		});

		Node root = new Node(null,null,initState,0);

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
		print.printSolution(s);
		System.out.println("max = "+s.maxNodesInFrontier());
		System.out.println("total = "+s.nodesGenerated());

		System.out.println("--- breadth first graph search ---");

		Node s = bfsg.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+s.maxNodesInFrontier());
		System.out.println("total = "+s.nodesGenerated());

		System.out.println("--- depth first tree search ---");

		Node s = dfst.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+s.maxNodesInFrontier());
		System.out.println("total = "+s.nodesGenerated());

		System.out.println("--- depth first graph search ---");

		Node s = dfsg.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+s.maxNodesInFrontier());
		System.out.println("total = "+s.nodesGenerated());

		System.out.println("--- iterative deepening tree search ---");
		Node s = iter.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max = "+s.maxNodesInFrontier());
		System.out.println("total = "+s.nodesGenerated());

	}

}