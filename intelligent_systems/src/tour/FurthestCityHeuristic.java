package tour;

import java.util.Set;
import search.*;

public class FurthestCityHeuristic extends NodeFunction {

	// a consistent (and therefore admissible) heuristic that computes the maximal
	// sum of dist(currentCity,n) + distance(n,goalCity) where n is an unvisited node != currentCity

	protected City goalCity;
	protected Set<City> cities;

	public FurthestCityHeuristic(Set<City> cities, City goalCity) {
		this.goalCity = goalCity;
		this.cities = cities;
	}

	public int apply(Node node) {
		int maxSoFar = 0;
		TourState state = (TourState) node.state;
		City current = state.currentCity;
		for (City c : cities) {
			if (!(c == current) && !(state.visitedCities.contains(c))) {	// is c non-current and not yet visited?
				int x = current.getShortestDistanceTo(c) + c.getShortestDistanceTo(goalCity);
				if (x > maxSoFar) { maxSoFar = x; }
			}
		}
		return maxSoFar;
	}
}