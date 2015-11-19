package npuzzle;

import search.*;

public class AStarTest {

	public static void main(String[] args) {
		NPuzzlePrinting print = new NPuzzlePrinting();

		Tiles initState = new Tiles(new int[][] {
			{ 7, 4, 2 },
			{ 8, 1, 3 },
			{ 5, 0, 6 }
		});

		Node root = new Node(null,null,initState,0,0,0);

		GoalTest goal = new TilesGoalTest();

		NodeFunction h = new AStarFunction(new MisplacedTiles());
		BestFirstFrontier frontier = new BestFirstFrontier(h);

		Search aStarTree = new TreeSearch(frontier);
		Search aStarGraph = new GraphSearch(frontier);

		System.out.println("----- A Star Tree Search -----");
		Node s = aStarTree.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max nodes = "+aStarTree.maxNodesInFrontier());
		System.out.println("total nodes = "+aStarTree.nodesGenerated());

		System.out.println("----- A Star Graph Search -----");
		s = aStarGraph.findGoal(root,goal);
		print.printSolution(s);
		System.out.println("max nodes = "+aStarGraph.maxNodesInFrontier());
		System.out.println("total nodes = "+aStarGraph.nodesGenerated());


	}
}