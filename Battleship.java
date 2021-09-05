import java.util.Scanner;

import static org.junit.Assert.*;
import org.junit.Test;

public class Battleship {
	private final Board playerBoard, computerBoard;
	private final Scanner scanner;

	public Battleship() {
		playerBoard = new Board();
		computerBoard = new Board();

		

		scanner = new Scanner(System.in);
	}

	private void placeShips() {
		for (Board b : new Board[] {playerBoard, computerBoard}) {
			b.addShip("Carrier", 5);
			b.addShip("Battleship", 4);
			b.addShip("Cruiser", 3);
			b.addShip("Submarine", 3);
			b.addShip("Destroyer", 2);
		}
	}

	public boolean gameOver() {
		return false;
	}

	public void printGame(boolean playerTurn) {
		final String resetColor = "\033[0m", activeTurnColor = "\033[0;97;40m", inactiveTurnColor = "\033[1;97;101m";
		final String 
			firstColor = playerTurn ? activeTurnColor : inactiveTurnColor, 
			secondColor = playerTurn ? inactiveTurnColor : activeTurnColor;
		final String separator = resetColor + "        " + secondColor;

		String str = "";

		String BC = " "; // border character
		str += firstColor;
	
		// top border
		str += BC.repeat(1 + (Board.WIDTH - 3) / 2) + "You" + BC.repeat(1 + (int)Math.ceil((Board.WIDTH - 3.0f) / 2.0f));
		str += separator;
		str += BC.repeat(1 + (Board.WIDTH - 3) / 2) + "CPU" + BC.repeat(1 + (int)Math.ceil((Board.WIDTH - 3.0f) / 2.0f));
		str += resetColor + "\n";

		// board rows
		for (int y = 0; y < Board.HEIGHT; y++) {
			str += firstColor + (char)('A' + y) + playerBoard.getRowString(y, true) + firstColor + BC + 
				separator + (char)('A' + y) + computerBoard.getRowString(y, false) + secondColor + BC + resetColor + "\n";
		} 

		// bottom border
		str += firstColor + BC;
		for (int x = 0; x < Board.WIDTH; x++) {
			str += x;
		}
		str += BC + separator + secondColor + BC;
		for (int x = 0; x < Board.WIDTH; x++) {
			str += x;
		}
		str += BC;

		str += resetColor;

		System.out.println(str);
	}

	public void playerTurn() {
		System.out.println("Your turn");
		printGame(true);

		System.out.print("Guess a coordinate (enter a column number and a row letter): ");
		Coordinate coord = Coordinate.scanCoordinate(scanner);
		Board.CellState stateAtCoord = computerBoard.getCellStateAtCoordinate(coord);
		while (stateAtCoord == Board.CellState.HIT || stateAtCoord == Board.CellState.MISS) {
			System.out.print("already guessed coordinate " + coord + ", guess again: ");
			coord = Coordinate.scanCoordinate(scanner);
			stateAtCoord = computerBoard.getCellStateAtCoordinate(coord);
		}
		
		Board.HitData hitData = computerBoard.shootAt(coord);
		printGame(true);
		System.out.println("You guessed " + coord);
		System.out.println(coord + " was a " + (hitData.hitShip ? "hit" : "miss") + "!");
		if (hitData.sunkShip != null) {
			System.out.println("You sunk computer's " + hitData.sunkShip);
		}

		System.out.print("press enter to continue... ");
		scanner.nextLine();
	}

	public void computerTurn() {
		System.out.println("Computer turn");
		Coordinate coord = Coordinate.random();
		Board.CellState stateAtCoord = computerBoard.getCellStateAtCoordinate(coord);
		while (stateAtCoord == Board.CellState.HIT || stateAtCoord == Board.CellState.MISS) {
			coord = Coordinate.random();
			stateAtCoord = computerBoard.getCellStateAtCoordinate(coord);
		}
		
		Board.HitData hitData = playerBoard.shootAt(coord);
		
		printGame(false);
		System.out.println("Computer guessed " + coord);
		System.out.println(coord + " was a " + (hitData.hitShip ? "hit" : "miss") + "!");
		if (hitData.sunkShip != null) {
			System.out.println("Computer sunk your " + hitData.sunkShip);
		}
		System.out.print("press enter to continue... ");
		scanner.nextLine();
	}

	public void play() {
		while (!gameOver()) {
			playerTurn();
			computerTurn();
		}
		
		scanner.close();
	}

	public static void main(String[] args) {
		Battleship game = new Battleship();
		game.placeShips();
		game.play();
	}

	@Test
	public void testCoordinates() {
		assertEquals(new Coordinate(0, 0), new Coordinate(0, 'A'));
		assertEquals(new Coordinate(0, 0), Coordinate.getCoordinateFromString("a0"));
		assertEquals(new Coordinate(0, 0), Coordinate.getCoordinateFromString("0a"));
		assertEquals(new Coordinate(8, 'H'), Coordinate.getCoordinateFromString("ha"));
	}
}