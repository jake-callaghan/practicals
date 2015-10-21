package npuzzle;

import search.Action;
import search.Printing;
import search.State;

public class NPuzzlePrinting extends Printing {
	public void print(Action action) {
		System.out.print("move ");
		System.out.print(((Movement)action).name());
	}
	public void print(State state) {
		Tiles tiles=(Tiles)state;
		int width = tiles.getWidth();
		int cellLength = String.valueOf(width * width).length();
		System.out.print('-');
		for (int column = 0; column < width; column++)
			printChar('-', cellLength + 3);
		System.out.println();
		for (int row = 0; row < width; row++) {
			System.out.print('|');
			for (int column = 0; column < width; column++) {
				System.out.print(' ');
				if (row == tiles.getEmptyTileRow() && column == tiles.getEmptyTileColumn())
					printChar(' ', cellLength);
				else {
					String cell = String.valueOf(tiles.getTile(row, column));
					printChar(' ', cellLength - cell.length());
					System.out.print(cell);
				}
				System.out.print(" |");
			}
			System.out.println();
			System.out.print('-');
			for (int column = 0; column < width; column++)
				printChar('-', cellLength + 3);
			System.out.println();
		}
	}
	protected static void printChar(char c, int number) {
		for (int index = 0; index < number; index++)
			System.out.print(c);
	}
}
