package tour;

import search.*;

public class AStarTest {

	public static void main(String[] args) {
		Cities romania = SetUpRomania.getRomaniaMapSmall();
		City startCity = romania.getState("Bucharest");
		GoalTest goal = new TourGoalTest(romania.getAllCities(), startCity);
		TourPrinting print = new TourPrinting();

		Node root = new Node(null,null,new TourState(startCity),0,0,0);

		NodeFunction h = new AStarFunction(new FurthestCityHeuristic(romania.getAllCities(),startCity));
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