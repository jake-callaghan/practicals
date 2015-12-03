package mars;

import search.*;
import java.util.Hashtable;

public class RobotPrinter extends Printing {

	public static Hashtable<Cell,String> moves;	

	public RobotPrinter() {
		moves.put(new Cell(-1,0),"LEFT");
		moves.put(new Cell(0,1),"UP");
		moves.put(new Cell(1,0),"RIGHT");
		moves.put(new Cell(0,-1),"DOWN");
	}	

	public void print(Action action) {
		Cell move = (Cell) action;
		if (moves.contains(move)) {
			System.out.println(moves.get(move));
		} else {
			System.out.println("****** error : unknown action ******");
		}
	}

	public void print(State state) {
		Robot robot = (Robot) state;
		System.out.println("| battery life = "+robot.batteryLife+" | current cell = ("+robot.currentCell.x+","+robot.currentCell.y+") |");
	}
}
