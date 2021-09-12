import java.util.Scanner;
import java.util.Random;

import static ConsoleUtil.ConsoleUtil.*;

public class Player {
	private static final Random rand = new Random();
	private static final String importantStr = getColorStr(OutputModifier.FG_CYAN, OutputModifier.BG_DEFAULT);
	
	public boolean wantsToQuit = false;
	private final String name;
	protected final Board board, enemyBoard;

	public boolean printGameOnTurn = true;


	public Player(Board board, Board enemyBoard, String name) {
		this.board = board;
		this.enemyBoard = enemyBoard;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public void placeShip(Scanner scanner, String shipName, int shipLength) {
		// determine ship orientation
		boolean horizontal = false;
		System.out.print("Enter ship orientation ('H'orizontal, 'V'ertical, or 'R'andom): ");
		String line = scanner.nextLine().toUpperCase();
		if (line.contains("H")) {
			horizontal = true;
		} else if (line.contains("R")) {
			horizontal = rand.nextInt() % 2 == 0;
		}

		// print orientation
		System.out.println(
			"You chose " + 
			importantStr + 
			(horizontal ? "Horizontal" : "Vertical") + 
			getColorStr(OutputModifier.RESET)
		);

		// determine ship position
		System.out.print("Enter ship coordinate (enter a column letter and row number), or type 'R' for random: ");
		line = scanner.nextLine().toUpperCase();
		boolean firstAttempt = true;
		Coordinate coord;
		do {
			if (!firstAttempt && !line.contains("R")) {
				System.out.print("invalid coordinate entered, try again: ");
				line = scanner.nextLine().toUpperCase();
			}

			if (line.contains("R")) {
				if (horizontal) {
					coord = new Coordinate(rand.nextInt((Board.WIDTH - shipLength) + 1), rand.nextInt(Board.HEIGHT));
				} else {
					coord = new Coordinate(rand.nextInt(Board.WIDTH), rand.nextInt((Board.HEIGHT - shipLength) + 1));
				}
			} else {
				
				coord = Coordinate.getCoordinateFromString(line);
			}
			firstAttempt = false;
		} while (coord == null || board.isRegionOccupied(coord, shipLength, horizontal));
		
		// print position
		System.out.println(
			"Placing " + importantStr + shipName + getColorStr(OutputModifier.RESET) + " @ " +
			importantStr + coord + getColorStr(OutputModifier.RESET)
		);

		// add ship to board
		Ship ship = new Ship(shipName, coord, shipLength, horizontal);
		board.addShip(ship);
	}

	public Coordinate guessCoordinate(Scanner scanner) {
		System.out.print("Guess a coordinate (enter a column number and a row letter), or type 'q' to quit: ");

		Coordinate coord = null;
		boolean first = true;

		do {
			if (!first) {
				System.out.println("Invalid coordinate given, try again");
			}
			
			String line = scanner.nextLine().toUpperCase();
			if (line.contains("Q")) {
				wantsToQuit = true;
				return null;
			}

			coord = Coordinate.getCoordinateFromString(line);
			if (coord != null) {
				Board.CellState stateAtCoord = enemyBoard.getCellStateAtCoordinate(coord);
				if (stateAtCoord == Board.CellState.HIT || stateAtCoord == Board.CellState.MISS) {
					System.out.print("already guessed coordinate " + coord + ", guess again: ");
					coord = null;
				}
			}

			first = false;
		} while (coord == null);
		
		return coord;
	}
}
