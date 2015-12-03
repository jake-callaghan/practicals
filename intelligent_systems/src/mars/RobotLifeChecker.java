package mars;

import search.*;

public class RobotLifeChecker implements GoalTest {
	public boolean isGoal(State state) {
		Robot robot = (Robot) state;
		return (robot.batteryLife == 0);
	}
}
