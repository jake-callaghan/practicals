package mars;

import search.*;
import java.util.HashSet;

public class DeployRobot {
	public static void main(String[] args) {
		
		Planet mars = new Planet();
		HashSet<Cell> vcs = new HashSet<Cell>(); vcs.add(new Cell(4,4));		
		Robot rover = new Robot(mars,20,new Cell(4,4),vcs);
		RobotPrinter print = new RobotPrinter();
		Node root = new Node(null,null,rover,0,0,1);
		GoalTest goal = new RobotLifeChecker();
		BestFirstFrontier frontier = new BestFirstFrontier(new RobotHeuristic());
		Search search = new TreeSearch(frontier);
	
		Node solution = search.findGoal(root,goal);
		print.printSolution(solution);
		System.out.println("max = "+search.maxNodesInFrontier());
		System.out.println("total = "+search.nodesGenerated());

	}
}
