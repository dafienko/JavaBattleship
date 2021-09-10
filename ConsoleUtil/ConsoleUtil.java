package ConsoleUtil;

//https://xdevs.com/guide/color_serial/

public class ConsoleUtil {
	public enum OutputModifier {
		// misc
		RESET(0),
		UNDERLINE(4),
		BLINKING(5),

		// foreground
		FG_BLACK(30),
		FG_RED(31),
		FG_GREEN(32),
		FG_YELLOW(33),
		FG_BLUE(34),
		FG_MAGENTA(35),
		FG_CYAN(36),
		FG_WHITE(37),
		FG_DEFAULT(39),
		
		// background
		BG_BLACK(40),
		BG_RED(41),
		BG_GREEN(42),
		BG_YELLOW(43),
		BG_BLUE(44),
		BG_MAGENTA(45),
		BG_CYAN(46),
		BG_WHITE(47),
		BG_DEFAULT(49);
		
		public final int value;

		private OutputModifier(int value) {
			this.value = value;
		}
	};

	public static String getColorStr(OutputModifier... modifiers) {
		String str = "\033[";

		for (int i = 0; i < modifiers.length; i++) {
			str += modifiers[i].value;

			if (i > 0) 
				str += ";";
		}

		str += "m";

		return str;
	}
}
