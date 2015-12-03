package mars;

import search.*;

// this implements the heurstic : h(n) = #movements - #discovered cells

public class RobotHeuristic extends NodeFunction {
	public int apply(Node node) {
		Robot robot = (Robot) node.state;
		return node.g - robot.visitedCells.size();
	}
}
