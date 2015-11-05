package search;

public interface Search {

	Node findGoal(Node root, GoalTest goal);

	int maxNodesInFrontier();

	int nodesGenerated();

}