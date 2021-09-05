import java.util.Scanner;
import java.util.Random;

public class Coordinate {
	private static Random rand = new Random();

	public final int x, y;

	public Coordinate(int x, int y) {
		this.x = x; 
		this.y = y;
	}

	public Coordinate(int x, char c) {
		this.x = x;
		this.y = Character.toString(c).toUpperCase().charAt(0) - 'A';
	}

	public static Coordinate random() {
		return new Coordinate(rand.nextInt(Board.WIDTH), rand.nextInt(Board.HEIGHT));
	}

	public static Coordinate getCoordinateFromString(String str) {
		int x;
		try {
			String numStr = str.replaceAll("[^\\d.]", "");
			x = Integer.parseInt(numStr);
		} catch(NumberFormatException e) {
			System.out.println("no row number given!");
			return null;
		}
		if (x >= Board.WIDTH) {
			System.out.println("row number out of range, must be less than " + Board.WIDTH);
			return null;
		}
		
		char y = '\0';
		for (char c : str.toCharArray()) {
			if (c >= 'A' && c <= 'Z') {
				y = c;
				break;
			}
		}
		if (y == '\0') {
			System.out.println("no column letter given");
			return null;
		}
		if (y >= 'A' + Board.HEIGHT - 1) {
			System.out.println("column letter out of range, must between 'A' and '" + (char)('A' + Board.HEIGHT - 1) + "'");
			return null;
		}

		return new Coordinate(x, y);
	}

	public static Coordinate scanCoordinate(Scanner scanner) {
		Coordinate coord = null;
		do {
			String line = scanner.nextLine().toUpperCase();
			
			coord = Coordinate.getCoordinateFromString(line);
		} while (coord == null);

		return coord;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + (char)('A' + y) + ")";
	}

	@Override 
	public boolean equals(Object o) {
		if (!(o instanceof Coordinate)) 
			return false;

		Coordinate c = (Coordinate)o;
		return x == c.x && y == c.y;
	}
}