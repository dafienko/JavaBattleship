import java.util.Random;
import java.util.Set;

import java.util.HashSet;

import static ConsoleUtil.ConsoleUtil.*;

public class Board {
	public static final int WIDTH = 10, HEIGHT = 10;
	private static final Random rand = new Random();

	private static final String  // terminal color codes - https://i.stack.imgur.com/9UVnC.png
		WATER_COLOR_1 = getColorStr(false, false, OutputModifier.BG_CYAN), 
		WATER_COLOR_2 = getColorStr(false, false, OutputModifier.BG_LIGHTBLUE), 
		HIT_COLOR = getColorStr(false, false, OutputModifier.FG_RED), 
		MISS_FG_COLOR = getColorStr(false, false, OutputModifier.FG_YELLOW), 
		SHIP_COLOR = getColorStr(false, false, OutputModifier.FG_WHITE, OutputModifier.BG_WHITE),
		RESET_COLOR = getColorStr(OutputModifier.RESET);

	public enum CellState {
		WATER {
			@Override
			public String toString(){
                return " ";
            }
		},

		MISS {
			@Override
			public String toString(){
                return "O";
            }
		},

		HIT {
			@Override
			public String toString(){
                return "X";
            }
		},

		SHIP {
			@Override
			public String toString(){
                return "█";
            }
		}
	}

	private final CellState[][] grid;
	private final Set<Ship> ships;

	public Board() {
		grid = new CellState[WIDTH][HEIGHT];
		ships = new HashSet<Ship>();

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				grid[x][y] = CellState.WATER;
			}
		}
	}

	public CellState getCellStateAtCoordinate(Coordinate coordinate) {
		return grid[coordinate.x][coordinate.y];
	}


	private Ship getShipFromCoordinate(Coordinate coordinate) {
		for (Ship ship : ships) {
			for (Coordinate c : ship.getFootprint()) {
				if (c.equals(coordinate)) {
					return ship;
				}
			}
		}

		return null;
	}

	public Set<Ship> getShips() {
		return new HashSet<Ship>(ships);
	}

	public boolean isShipAlive(String shipName) {
		for (Ship ship : ships) {
			if (ship.name.equals(shipName)) {
				for (Coordinate coord : ship.getFootprint()) {
					CellState state = getCellStateAtCoordinate(coord);

					if (state != CellState.HIT) {
						return true;
					}
				}

				return false;
			}
		}

		return false;
	}

	private boolean isShipSunk(Ship ship) {
		for (Coordinate c : ship.getFootprint()) {
			if (getCellStateAtCoordinate(c) != CellState.HIT) {
				return false;
			}
		}

		return true;
	}

	public boolean isShipSunk(String shipName) {
		for (Ship ship : ships) {
			if (ship.name.equals(shipName)) {
				return isShipSunk(ship);
			}
		}

		return false;
	}

	public class HitData {
		public final boolean hitShip;
		public final Ship sunkShip;

		public HitData(boolean hitShip, Ship sunkShip) {
			this.hitShip = hitShip;
			this.sunkShip = sunkShip;
		}
	}

	public HitData shootAt(Coordinate coordinate) {
		CellState state = getCellStateAtCoordinate(coordinate);
		boolean hitShip = state == CellState.SHIP;

		grid[coordinate.x][coordinate.y] = hitShip ? CellState.HIT : CellState.MISS;

		
		Ship sunkShip = null;
		if (hitShip) {
			Ship shipAtPos = getShipFromCoordinate(coordinate);
			if (isShipSunk(shipAtPos)) {
				sunkShip = shipAtPos;
			}
		}

		return new HitData(hitShip, sunkShip);
	}



	private String getCellColor(CellState cellState, int x, int y) {
		String color = "\033[";
		if (cellState == CellState.WATER || cellState == CellState.MISS) {
			color += (x + y % 2) % 2  == 0 ? WATER_COLOR_1 : WATER_COLOR_2; // checker-pattern water colors

			if (cellState == CellState.MISS) {
				color += ";" + MISS_FG_COLOR;
			}
		} else if (cellState == CellState.HIT) {
			color += HIT_COLOR;
		} else {
			color += SHIP_COLOR;
		}
		color += "m";

		return color;
	}

	public String getRowString(int y, boolean showShips) {
		String rowStr = "";
		
		for (int x = 0; x < WIDTH; x++) {
			CellState state = grid[x][y];
			
			if (!showShips && state == CellState.SHIP) 
				state = CellState.WATER;

			rowStr += getCellColor(state, x, y) + state;
		}

		rowStr += RESET_COLOR;

		return rowStr;
	}

	@Override
	public String toString() {
		String str = "";

		for (int y = 0; y < HEIGHT; y++) {
			str += getRowString(y, true) + "\n";
		}

		return str;
	}

	public boolean isRegionOccupied(Coordinate position, int length, boolean horizontal) {
		for (Coordinate c : Ship.getShipFootprint(position, length, horizontal)) {

			CellState state = getCellStateAtCoordinate(c);
			if (state == CellState.SHIP || state == CellState.HIT) {
				return true;
			}
		}

		return false;
	}

	private Coordinate getRandomShipPosition(int length, boolean horizontal) {
		Coordinate pos;
		
		do {
			if (horizontal) {
				pos = new Coordinate(rand.nextInt((WIDTH - length) + 1), rand.nextInt(HEIGHT));
			} else {
				pos = new Coordinate(rand.nextInt(WIDTH), rand.nextInt((HEIGHT - length) + 1));
			}
		} while (isRegionOccupied(pos, length, horizontal));

		return pos;
	}

	public void addShip(String name, int length) {
		boolean horizontal = rand.nextInt() % 2 == 0;
		Coordinate pos = getRandomShipPosition(length, horizontal);

		Ship ship = new Ship(name, pos, length, horizontal);
		addShip(ship);
	}

	public void addShip(Ship ship) {
		ships.add(ship);
		for (Coordinate c : Ship.getShipFootprint(ship.position, ship.length, ship.horizontal)) {
			grid[c.x][c.y] = CellState.SHIP;
		}
	}
}