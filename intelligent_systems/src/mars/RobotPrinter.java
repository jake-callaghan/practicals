package mars;

import search.*;
import java.util.Hashtable;

public class RobotPrinter extends Printing {

	public Hashtable<Cell,String> moves;	

	public RobotPrinter() {
		moves = new Hashtable<Cell,String>();
		moves.put(new Cell(-1,0),"UP");
		moves.put(new Cell(0,1),"RIGHT");
		moves.put(new Cell(1,0),"DOWN");
		moves.put(new Cell(0,-1),"LEFT");
		System.out.println(moves);
	}	

	public void print(Action action) {
		Cell move = (Cell) action;
		System.out.println(moves.get(move));
	}

	public void print(State state) {
		Robot robot = (Robot) state;
		System.out.println("| battery life = "+robot.batteryLife+" | current cell = ("+robot.currentCell.x+","+robot.currentCell.y+") |");
		System.out.println("| "+robot.visitedCells.size()+" visited cells = "+ robot.visitedCells+" |");
	}
}
