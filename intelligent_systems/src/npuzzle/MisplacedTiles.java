package npuzzle;

import search.*;

public class MisplacedTiles extends NodeFunction {
	
	public int apply(Node node) {
		Tiles state = (Tiles)node.state;
		int[] tiles = state.getTiles();
		int width = state.getWidth();
		int outofplacetiles = 0;
		int lastTileIndex = width * width - 1;
		for (int index = lastTileIndex - 1; index >=0; --index) {
			if (tiles[index] != index + 1) {
				outofplacetiles++;
			}
		}
		return outofplacetiles;
	}

}