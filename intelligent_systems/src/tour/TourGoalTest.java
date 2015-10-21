package tour;

import java.util.Set;

import search.GoalTest;
import search.State;

public class TourGoalTest implements GoalTest {
	protected final Set<City> citiesToVisit;
	protected final City targetCity;
	
	public TourGoalTest(Set<City> citiesToVisit, City targetCity) {
		this.citiesToVisit = citiesToVisit;
		this.targetCity = targetCity;
	}
	public boolean isGoal(State state) {
		TourState tourState = (TourState)state;
		return citiesToVisit.equals(tourState.visitedCities) && targetCity.equals(tourState.currentCity);
	}
}
