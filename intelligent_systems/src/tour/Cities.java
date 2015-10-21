package tour;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Cities {
	protected final Map<String, City> citiesByName;
	
	public Cities() {
		citiesByName = new LinkedHashMap<String, City>();
	}
	public City getState(String cityName) {
		return citiesByName.get(cityName);
	}
	public void addRoad(String city1Name, String city2Name, int distance) {
		City city1 = getStateCreateIfMissing(city1Name);
		City city2 = getStateCreateIfMissing(city2Name);
		city1.outgoingRoads.add(new Road(city1, city2, distance));
		city2.outgoingRoads.add(new Road(city2, city1, distance));
	}
	protected City getStateCreateIfMissing(String cityName) {
		City inCity = citiesByName.get(cityName);
		if (inCity == null) {
			inCity = new City(cityName);
			citiesByName.put(cityName, inCity);
		}
		return inCity;
	}
	public Set<City> getAllCities() {
		return new LinkedHashSet<City>(citiesByName.values());
	}
	public void computeShortestDistances() {
		for (City city : citiesByName.values()) {
			city.shortestDistanceByCity.clear();
			for (Road road : city.outgoingRoads)
				city.shortestDistanceByCity.put(road.targetCity, road.length);
		}
		for (City cityK : citiesByName.values())
			for (City cityI : citiesByName.values())
				for (City cityJ : citiesByName.values()) {
					int distanceIJ = cityI.getShortestDistanceTo(cityJ);
					int distanceIK = cityI.getShortestDistanceTo(cityK);
					int distanceKJ = cityK.getShortestDistanceTo(cityJ);
					if (distanceIK != Integer.MAX_VALUE && distanceKJ != Integer.MAX_VALUE)
						distanceIJ = Math.min(distanceIJ, distanceIK + distanceKJ);
					cityI.shortestDistanceByCity.put(cityJ, distanceIJ);
				}
	}
}
