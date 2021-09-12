import java.util.Random;

public class Coordinate {
	private static Random rand = new Random();

	public final int x, y;

	public Coordinate(int x, int y) {
		this.x = x; 
		this.y = y;
	}

	public Coordinate(int x, char rowLetter) {
		this.x = x;
		this.y = Character.toString(rowLetter).toUpperCase().charAt(0) - 'A';
	}

	public static Coordinate random() {
		return new Coordinate(rand.nextInt(Board.WIDTH), rand.nextInt(Board.HEIGHT));
	}

	public static Coordinate getCoordinateFromString(String str) {
		str = str.toUpperCase();
		
		int x;
		try {
			String numStr = str.replaceAll("[^\\d.]", ""); // remove any non-number characters
			x = Integer.parseInt(numStr);
		} catch(NumberFormatException e) {
			return null;
		}
		if (x >= Board.WIDTH) {
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
			return null;
		}
		if (y > 'A' + Board.HEIGHT - 1) {
			return null;
		}

		return new Coordinate(x, y);
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
		return this.x == c.x && this.y == c.y;
	}
}
