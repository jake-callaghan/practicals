package tour;

import search.BreadthFirstTreeSearch;
import search.GoalTest;
import search.Node;

public class BFTS_Demo {
	public static void main(String[] args) {
		System.out.println("This is a demonstration of breadth-first tree search on Romania tour");
		System.out.println();
		
		Cities romania = SetUpRomania.getRomaniaMapSmall();
		City startCity = romania.getState("Bucharest");
		
		GoalTest goalTest = new TourGoalTest(romania.getAllCities(), startCity);
		Node solution = BreadthFirstTreeSearch.findSolution(new TourState(startCity), goalTest);
		new TourPrinting().printSolution(solution);
	}
}
