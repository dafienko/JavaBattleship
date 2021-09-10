import java.util.Scanner;

import static org.junit.Assert.*;
import org.junit.Test;

import static ConsoleUtil.ConsoleUtil.*;

public class Battleship {
	private final Board player1Board, player2Board;
	private final Player player1, player2;
	private final Scanner scanner;

	private static final String[] SHIP_NAMES = {
		"Carrier", 
		"Battleship",
		"Cruiser", 
		"Submarine",
		"Destroyer"
	};

	private static final int[] SHIP_LENGTHS = {
		5,
		4,
		3,
		3,
		2
	};

	public Battleship() {
		player1Board = new Board();
		player2Board = new Board();

		player1 = new Player(player1Board, player2Board, "YOU");
		player2 = new PlayerAI(player2Board, player1Board, "CPU");

		scanner = new Scanner(System.in);
	}

	private void placeShips() {
		for (Player player : new Player[] {player1, player2}) {
			for (int i = 0; i < SHIP_NAMES.length; i++) {
				player.placeShip(scanner, SHIP_NAMES[i], SHIP_LENGTHS[i]);
			}
		}
	}

	public boolean gameOver() {
		return false;
	}

	public boolean playerWon(Player player) {
		for (Ship ship : player.enemyBoard.getShips()) {
			if (!ship.isSunk(player.enemyBoard)) {
				return false;
			}
		}

		return true;
	}

	public void printGame(Player playerPerspective, boolean isPlayerTurn) {
		boolean showBoth = playerPerspective == null; // if playerPerspective is null, show the ships on both boards...
		playerPerspective = playerPerspective == null ? player1 : player2; // then default the left board to player1
		Board leftBoard = playerPerspective == player1 ? player1Board : player2Board;
		Board rightBoard = playerPerspective == player1 ? player2Board : player1Board;
		Player opponent = playerPerspective == player1 ? player2 : player1;

		final String resetColor = "\033[0m", activeTurnColor = "\033[0;97;40m", inactiveTurnColor = "\033[1;97;101m";
		final String 
			firstColor = isPlayerTurn ? activeTurnColor : inactiveTurnColor, 
			secondColor = isPlayerTurn ? inactiveTurnColor : activeTurnColor;
		final String separator = resetColor + "        " + secondColor;

		String str = "";

		String BC = " "; // border character
		str += firstColor;
	
		// top border
		int lNameLen = playerPerspective.toString().length();
		int rNameLen = opponent.toString().length();

		str += BC.repeat(1 + (Board.WIDTH - lNameLen) / 2) + playerPerspective + BC.repeat(1 + (int)Math.ceil((float)(Board.WIDTH - lNameLen) / 2.0f));
		str += separator;
		str += BC.repeat(1 + (Board.WIDTH - rNameLen) / 2) + opponent + BC.repeat(1 + (int)Math.ceil((float)(Board.WIDTH - rNameLen) / 2.0f));
		str += resetColor + "\n";

		// board rows
		for (int y = 0; y < Board.HEIGHT; y++) {
			str += firstColor + (char)('A' + y) + leftBoard.getRowString(y, true) + firstColor + BC + 
				separator + (char)('A' + y) + rightBoard.getRowString(y, false || showBoth) + secondColor + BC + resetColor;

			if (y < SHIP_NAMES.length) {
				String displayShipName = SHIP_NAMES[y];
				str += "\t" + displayShipName + ": " + 
					(playerPerspective.enemyBoard.isShipAlive(displayShipName) ? "☐" : "☑");
			}

			str += "\n";
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

	private void doTurn(Player player) {
		Player opponent = player == player1 ? player2 : player1;
		Board opponentBoard = player == player1 ? player2Board : player1Board;

		System.out.println("\n" + player + " turn");
		if (player.printGameOnTurn) {
			printGame(player, true);
		}
		Coordinate coord = player.guessCoordinate(scanner);

		Board.HitData hitData = opponentBoard.shootAt(coord);
		if (player.printGameOnTurn) {
			printGame(player, true);
		}
		System.out.println(player + " guessed " + coord);
		System.out.println(coord + " was a " + (hitData.hitShip ? "hit" : "miss") + "!");
		if (hitData.sunkShip != null) {
			System.out.println(
				getColorStr(OutputModifier.FG_RED) +
				player + " sunk " + opponent + "'s " + hitData.sunkShip + 
				getColorStr(OutputModifier.RESET)
			);
		}

		System.out.print("press enter to continue... ");
		scanner.nextLine();
	}

	public void play() {
		while (!gameOver()) {
			doTurn(player1);
			if (playerWon(player1)) {
				break;
			}
			doTurn(player2);
		}
		
		
		Player victor = playerWon(player1) ? player1 : player2;
		printGame(null, victor == player1);
		System.out.println(
			getColorStr(OutputModifier.FG_YELLOW) + victor + " Won!" + getColorStr(OutputModifier.RESET)
		);
		
		scanner.close();
	}

	public static void main(String[] args) {
		Battleship game = new Battleship();
		game.placeShips();
		game.play();
	}

	@Test
	public void testCoordinates() {
		Coordinate a01 = new Coordinate(0, 'A');
		Coordinate a02 = new Coordinate(0, 0);
		Coordinate a03 = Coordinate.getCoordinateFromString("a0");
		Coordinate a04 = Coordinate.getCoordinateFromString("0a");
		assertEquals(a01, a02);
		assertEquals(a02, a03);
		assertEquals(a02, a04);
	}

	@Test 
	public void testColorText() {
		System.out.println(
			getColorStr(OutputModifier.UNDERLINE, OutputModifier.FG_RED) +
			"red underlined text" + 
			getColorStr(OutputModifier.RESET)
		);

		System.out.println("normal text");

		System.out.println(
			getColorStr(OutputModifier.BLINKING, OutputModifier.FG_GREEN) +
			"green blinking text" + 
			getColorStr(OutputModifier.RESET)
		);

		System.out.println(
			getColorStr(OutputModifier.FG_RED, OutputModifier.BG_YELLOW) +
			"red text yellow background" + 
			getColorStr(OutputModifier.RESET)
		);

		System.out.println(
			getColorStr(OutputModifier.FG_BLUE, OutputModifier.BG_WHITE) +
			"blue text white background" + 
			getColorStr(OutputModifier.RESET)
		);
	}
}