import java.util.Scanner;

public class PlayerAI extends Player {
	public PlayerAI(Board board, Board enemyBoard, String name) {
		super(board, enemyBoard, name);

		printGameOnTurn = false;
	}

	@Override
	public void placeShip(Scanner scanner, String shipName, int shipLength) {
		board.addShip(shipName, shipLength);
	}

	@Override
	public Coordinate guessCoordinate(Scanner scanner) {
		Coordinate c = Coordinate.random();

		Board.CellState state = enemyBoard.getCellStateAtCoordinate(c);
		while (state == Board.CellState.HIT || state == Board.CellState.MISS) {
			c = Coordinate.random();
			state = enemyBoard.getCellStateAtCoordinate(c);
		}

		return c;
	}
}
