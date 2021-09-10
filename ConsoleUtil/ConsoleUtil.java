package ConsoleUtil;

//	https://misc.flogisoft.com/bash/tip_colors_and_formatting
//	https://misc.flogisoft.com/bash/tip_colors_and_formatting

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
		FG_LIGHTGRAY(37),
		FG_DEFAULT(39),
		FG_DARKGRAY(90),
		FG_LIGHTRED(91),
		FG_LIGHTGREEN(92),
		FG_LIGHTYELLOW(93),
		FG_LIGHTBLUE(94),
		FG_LIGHTMAGENTA(95),
		FG_LIGHTCYAN(96),
		FG_WHITE(97),
		
		// background
		BG_BLACK(40),
		BG_RED(41),
		BG_GREEN(42),
		BG_YELLOW(43),
		BG_BLUE(44),
		BG_MAGENTA(45),
		BG_CYAN(46),
		BG_LIGHTGRAY(47),
		BG_DEFAULT(49),
		BG_DARKGRAY(100),
		BG_LIGHTRED(101),
		BG_LIGHTGREEN(102),
		BG_LIGHTYELLOW(103),
		BG_LIGHTBLUE(104),
		BG_LIGHTMAGENTA(105),
		BG_LIGHTCYAN(106),
		BG_WHITE(107);

		public final int value;

		private OutputModifier(int value) {
			this.value = value;
		}
	};



	public static String getColorStr(OutputModifier... modifiers) {
		return getColorStr(true, true, modifiers);
	}

	public static String getColorStr(boolean includePrefix, boolean includeSuffix, OutputModifier... modifiers) {
		String str = includePrefix ? "\033[" : "";

		for (int i = 0; i < modifiers.length; i++) {
			str += modifiers[i].value;

			if (i > 0) 
				str += ";";
		}

		if (includeSuffix)
			str += "m";

		return str;
	}
}
