package tour;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class City {
	protected final String name;
	protected final Set<Road> outgoingRoads;
	protected final Map<City,Integer> shortestDistanceByCity;

	public City(String name) {
		this.name = name;
		this.outgoingRoads = new LinkedHashSet<Road>();
		this.shortestDistanceByCity = new LinkedHashMap<City, Integer>();
                this.shortestDistanceByCity.put(this,0);
	}
	public String getName() {
		return name;
	}
	public Set<Road> getOutgoingRoads() {
		return outgoingRoads;
	}
	public int getShortestDistanceTo(City city) {
		Integer distance = shortestDistanceByCity.get(city);
		if (distance == null)
			return Integer.MAX_VALUE;
		else
			return distance.intValue();
	}
}
