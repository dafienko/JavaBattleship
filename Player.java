import java.util.Scanner;
import java.util.Random;

import static ConsoleUtil.ConsoleUtil.*;

public class Player {
	private static final Random rand = new Random();

	public void placeShipOnBoard(Board board, Scanner scanner, String shipName, int shipLength) {
		boolean horizontal = false;
		System.out.print("Enter ship orientation ('H'orizontal, 'V'ertical, or 'R'andom): ");
		String line = scanner.nextLine().toUpperCase();
		if (line.contains("R")) {
			horizontal = rand.nextInt() % 2 == 0;
		} else if (line.contains("H")) {
			horizontal = true;
		}

		System.out.println(
			"You chose " + 
			getColorStr(OutputModifier.FG_CYAN, OutputModifier.BG_DEFAULT) + 
			(horizontal ? "Horizontal" : "Vertical") + 
			getColorStr(OutputModifier.RESET)
		);


		
	}
}
