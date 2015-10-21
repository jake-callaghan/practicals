package tour;

public class SetUpRomania {
	public static Cities getRomaniaMap() {
		Cities cities = new Cities();

		// We order origin cities alphabetically
		cities.addRoad("Arad", "Sibiu", 140);
		cities.addRoad("Arad", "Timisoara", 118);
		cities.addRoad("Arad", "Zerind", 75);
		
		cities.addRoad("Bucharest", "Fagaras", 211);
		cities.addRoad("Bucharest", "Giurgiu", 90);
		cities.addRoad("Bucharest", "Pitesti", 101);
		cities.addRoad("Bucharest", "Urziceni", 85);

		cities.addRoad("Craiova", "Drobeta", 120);
		cities.addRoad("Craiova", "Pitesti", 138);
		cities.addRoad("Craiova", "Rimnicu Vilcea", 146);

		cities.addRoad("Drobeta", "Mehadia", 75);

		cities.addRoad("Eforie", "Hirsova", 86);

		cities.addRoad("Fagaras", "Sibiu", 99);

		cities.addRoad("Hirsova", "Urziceni", 98);

		cities.addRoad("Iasi", "Neamt", 87);
		cities.addRoad("Iasi", "Vaslui", 92);

		cities.addRoad("Lugoj", "Mehadia", 70);
		cities.addRoad("Lugoj", "Timisoara", 111);

		cities.addRoad("Oradea", "Sibiu", 151);
		cities.addRoad("Oradea", "Zerind", 71);

		cities.addRoad("Rimnicu Vilcea", "Sibiu", 80);

		cities.addRoad("Urziceni", "Vaslui", 142);
		
		cities.computeShortestDistances();
		return cities;
	}
	public static Cities getRomaniaMapSmall() {
		Cities cities = new Cities();

		// We order origin cities alphabetically
		cities.addRoad("Arad", "Sibiu", 140);
		cities.addRoad("Arad", "Zerind", 75);

		cities.addRoad("Bucharest", "Fagaras", 211);
		cities.addRoad("Bucharest", "Giurgui", 90);
		cities.addRoad("Bucharest", "Pitesti", 101);

		cities.addRoad("Craiova", "Pitesti", 138);
		cities.addRoad("Craiova", "Rimnicu Vilcea", 146);

		cities.addRoad("Fagaras", "Sibiu", 99);

		cities.addRoad("Oradea", "Sibiu", 151);
		cities.addRoad("Oradea", "Zerind", 71);

		cities.addRoad("Rimnicu Vilcea", "Sibiu", 80);

		cities.computeShortestDistances();
		return cities;
	}
}
