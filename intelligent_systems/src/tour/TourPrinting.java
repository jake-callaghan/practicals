package tour;

import search.Action;
import search.Printing;
import search.State;

public class TourPrinting extends Printing {
	public void print(Action action) {
		System.out.print("drive for ");
		System.out.print(((Road)action).length);
		System.out.print("km to");
	}
	public void print(State state) {
		System.out.println(((TourState)state).currentCity.getName());
	}
}
