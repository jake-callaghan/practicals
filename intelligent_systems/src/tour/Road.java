package tour;

import search.Action;

public class Road implements Action {
	public final City sourceCity;
	public final City targetCity;
	public final int length;
	
	public Road(City sourceCity, City targetCity, int length) {
		this.sourceCity = sourceCity;
		this.targetCity = targetCity;
		this.length = length;
	}
	public int cost() {
		return length;
	}
}
