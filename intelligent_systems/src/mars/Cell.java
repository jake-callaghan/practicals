package mars;

import search.*;

// a class used to encapsulate a cell in a planet's grid
public class Cell implements Action {
	public final int x;
	public final int y;
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int cost(Node a, Node b) {
		return 1;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		// we can safely type cast obj as a cell
		Cell that = (Cell) obj;

		return (this.x == that.x && this.y == that.y);
	}

	public int hashCode() {
		int p = 37;	// a cheeky prime
		int h = 1;
		h = p * h + this.x;
		h = p * h + this.y;
		return h;
	}	
}
