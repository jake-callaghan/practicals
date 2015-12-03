package mars;

import search.*;
import java.util.*;

public class Robot implements State {

	public static Planet planet;	// the planet the robot is on
	public int batteryLife;		// current battery life
	public Cell currentCell;	// robot's current position
	public HashSet<Cell> visitedCells;	// explicit representation of cells seen atleast once
		
	public Robot(Planet planet, int batteryLife, Cell currentCell, HashSet<Cell> visitedCells) {
		this.planet = planet;
		this.batteryLife = batteryLife;
		this.currentCell = currentCell;
		this.visitedCells = visitedCells;
		
	}

	// returns a set of Cells (i,j) s.t. (currentCell.x + i, currentCell.y + j) is a valid move
	public Set<? extends Action> getApplicableActions() {
		HashSet<Cell> cells = new HashSet<Cell>();
		List<Cell> moves = new ArrayList();	
		moves.add(new Cell(-1,0));	// move left
		moves.add(new Cell(0,1));	// move up
		moves.add(new Cell(1,0));	// move right
		moves.add(new Cell(0,-1));	// move down
		for (Cell m : moves) {
			// apply move (direction) to currentCell, is it accessible?
			if (planet.isAccessible(m.x+currentCell.x,m.y+currentCell.y)) {
				cells.add(new Cell(m.x,m.y));
			} 
		}
		return cells;
	}
	
	public State getActionResult(Action action) {
		Cell move = (Cell) action; 
		Cell nextCell = new Cell(currentCell.x + move.x, currentCell.y + move.y); // apply the move to the currentCell
		HashSet<Cell> visited = (HashSet<Cell>) visitedCells.clone();	// a shallow clone is fine here, as Cells are not modified
		visited.add(nextCell);	// add the new cell to the set of visited cells
		// decrease batteryLife by 1 and return a new robot
		return new Robot(planet,batteryLife-1,nextCell,visited);
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

		// we can safely type cast obj as a robot
		Robot that = (Robot) obj;

		return (this.batteryLife == that.batteryLife &&
			this.currentCell.equals(that.currentCell) &&
			this.visitedCells.equals(that.visitedCells));
		
	}

	public int hashCode() {
		int prime = 37;	// a cheeky prime
		int h = 3;	
		h = prime * h + this.batteryLife;
		h = prime * h + this.currentCell.hashCode();
		h = prime * h + this.visitedCells.hashCode();
		return h;
	}
}
