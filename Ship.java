import java.util.HashSet;
import java.util.Set;

public class Ship {
	public final String name;
	/*
	private final int length;
	private final Coordinate position; // position is always top-left of ship
	private final boolean horizontal; // default vertical;
	*/
	private final Set<Coordinate> occupiedCoordinates;

	public static Set<Coordinate> getShipFootprint(Coordinate position, int length, boolean horizontal) {
		Set<Coordinate> set = new HashSet<Coordinate>(length);
		
		for (int i = 0; i < length; i++) {
			Coordinate c;
			if (horizontal) {
				c = new Coordinate(position.x + i, position.y);
			} else {
				c = new Coordinate(position.x, position.y + i);
			}

			set.add(c);
		}

		return set;
	}

	public Ship(String name, Coordinate position, int length, boolean horizontal) {
		this.name = name;
		/*
		this.length = length;
		this.position = position; 
		this.horizontal = horizontal;
		*/

		this.occupiedCoordinates = getShipFootprint(position, length, horizontal);
	}
	
	public boolean isSunk(Board board) {
		for (Coordinate coordinate : occupiedCoordinates) 
		if (board.getCellStateAtCoordinate(coordinate) != Board.CellState.HIT) 
		return false;
		
		return true;
	}

	public Set<Coordinate> getFootprint() {
		return new HashSet<Coordinate>(occupiedCoordinates);
	}

	@Override
	public String toString() {
		return name;
	}
}
