import java.util.Scanner;

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
			if (player.printGameOnTurn) {
				printGame(player, false);
			}

			for (int i = 0; i < SHIP_NAMES.length; i++) {
				player.placeShip(scanner, SHIP_NAMES[i], SHIP_LENGTHS[i]);
				
				if (player.printGameOnTurn) {
					printGame(player, false);
				}
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
		playerPerspective = playerPerspective == null ? player1 : playerPerspective; // then default the left board to player1
		Board leftBoard = playerPerspective == player1 ? player1Board : player2Board;
		Board rightBoard = playerPerspective == player1 ? player2Board : player1Board;
		Player opponent = playerPerspective == player1 ? player2 : player1;

		final String resetColor = "\033[0m", activeTurnColor = "\033[0;97;40m", inactiveTurnColor = "\033[1;97;101m";
		final String leftColor = isPlayerTurn ? activeTurnColor : inactiveTurnColor;
		final String rightColor = isPlayerTurn ? inactiveTurnColor : activeTurnColor;
		final String separator = resetColor + "        " + rightColor;

		String gameConsoleStr = "";

		String BC = " "; // border character
		gameConsoleStr += leftColor;
	
		// top border
		int lNameLen = playerPerspective.toString().length();
		int rNameLen = opponent.toString().length();

		// first line -  "BBplayer1BBB        BBplayer2BBB"
		gameConsoleStr += BC.repeat(1 + (Board.WIDTH - lNameLen) / 2) + 
			playerPerspective + 
			BC.repeat(1 + (int)Math.ceil((float)(Board.WIDTH - lNameLen) / 2.0f));
		gameConsoleStr += separator;
		gameConsoleStr += BC.repeat(1 + (Board.WIDTH - rNameLen) / 2) + 
			opponent + 
			BC.repeat(1 + (int)Math.ceil((float)(Board.WIDTH - rNameLen) / 2.0f));
		gameConsoleStr += resetColor + "\n";

		// board rows
		for (int y = 0; y < Board.HEIGHT; y++) {
			gameConsoleStr += leftColor + (char)('A' + y) + leftBoard.getRowString(y, true) + leftColor + BC + 
				separator + (char)('A' + y) + rightBoard.getRowString(y, false || showBoth) + rightColor + BC + resetColor;

			if (y < SHIP_NAMES.length) {
				String displayShipName = SHIP_NAMES[y];
				gameConsoleStr += "\t" + displayShipName + ": " + 
					(playerPerspective.enemyBoard.isShipAlive(displayShipName) ? "☐" : "☑");
			}

			gameConsoleStr += "\n";
		} 

		// bottom border - "BBBBBBBBBBBB        BBBBBBBBBBBB"
		gameConsoleStr += leftColor + BC;
		for (int x = 0; x < Board.WIDTH; x++) {
			gameConsoleStr += x;
		}
		gameConsoleStr += BC + separator + rightColor + BC;
		for (int x = 0; x < Board.WIDTH; x++) {
			gameConsoleStr += x;
		}
		gameConsoleStr += BC;

		gameConsoleStr += resetColor;

		System.out.println(gameConsoleStr);
	}

	private void runPlayerTurn(Player player) {
		Player opponent = player == player1 ? player2 : player1;
		Board opponentBoard = player == player1 ? player2Board : player1Board;

		System.out.println("\n" + player + " turn");
		if (player.printGameOnTurn) {
			printGame(player, true);
		}
		Coordinate coord = player.guessCoordinate(scanner);
		if (player.wantsToQuit) { 
			return;
		}

		Board.HitData hitData = opponentBoard.shootAt(coord);
		if (player.printGameOnTurn) {
			printGame(player, true);
		}
		System.out.println(player + " guessed " + coord);
		System.out.println(
			getColorStr(hitData.hitShip ? OutputModifier.FG_RED : OutputModifier.FG_LIGHTBLUE) +
			coord + " was a " + (hitData.hitShip ? "hit" : "miss") + "!" + 
			getColorStr(OutputModifier.RESET)
		);

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
			runPlayerTurn(player1);
			if (player1.wantsToQuit || playerWon(player1))
				break;
			
			runPlayerTurn(player2);
			if (player2.wantsToQuit)
				break;
		}
		
		if (!(player1.wantsToQuit || player2.wantsToQuit)) {
			Player victor = playerWon(player1) ? player1 : player2;
			printGame(null, victor == player1);
			System.out.println(
				getColorStr(OutputModifier.FG_YELLOW) + victor + " Won!" + getColorStr(OutputModifier.RESET)
			);
		}
		
		scanner.close();
	}

	public static void main(String[] args) {
		Battleship game = new Battleship();
		game.placeShips();
		game.play();
	}
}