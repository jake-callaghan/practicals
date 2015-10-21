package npuzzle;

import search.GoalTest;
import search.State;

public class TilesGoalTest implements GoalTest {
	public boolean isGoal(State state) {
		Tiles tiles = (Tiles)state;
		int lastTileIndex = tiles.width * tiles.width - 1;
		for (int index = lastTileIndex - 1; index >=0; --index)
			if (tiles.tiles[index] != index + 1)
				return false;
		return tiles.tiles[lastTileIndex] == Tiles.EMPTY_TILE;
	}
}
