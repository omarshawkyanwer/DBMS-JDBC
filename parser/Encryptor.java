package eg.edu.alexu.csd.oop.jdbc.parser;

public class Encryptor {

	public String decodeBrackets(String s) {
		s = s.replace("&Bsp%", " ");
		return s.replaceAll("\\( ", "(").replaceAll(" \\)", ")");
	}

	public String decodeQoutes(final String s) {
		return s.replaceAll("&Qsp%", " ").replaceAll("&cm%", ",").replaceAll("&op%", "(").replaceAll("&cp%", ")");
	}

	public String encodeBrackets(final String s) {
		StringBuilder builder = new StringBuilder();
		int opened = 0;
		for (int i = 0; i < s.length(); ++i) {
			if (s.charAt(i) == '(') {
				++opened;
			}
			if (s.charAt(i) == ')') {
				--opened;
			}
			if (opened < 0) {
				throw new RuntimeException("Missing brackets in command.");
			}
			if (opened > 0 && s.charAt(i) == ' ') {
				builder.append("&Bsp%");
			} else {
				builder.append(s.charAt(i));
			}
		}
		if (opened > 0) {
			throw new RuntimeException("Missing brackets in command.");
		}
		return builder.toString();
	}

	public String encodeQoutes(final String s) {
		StringBuilder builder = new StringBuilder();
		boolean openedSingle = false, openedDouble = false;
		for (int i = 0; i < s.length(); ++i) {
			if (!openedDouble && s.charAt(i) == '\'') {
				openedSingle ^= true;
			}
			if (!openedSingle && s.charAt(i) == '\"') {
				openedDouble ^= true;
			}
			if ((openedSingle || openedDouble) && s.charAt(i) == ' ') {
				builder.append("&Qsp%");
			} else if ((openedSingle || openedDouble) && s.charAt(i) == ',') {
				builder.append("&cm%");
			} else if ((openedSingle || openedDouble) && s.charAt(i) == '(') {
				builder.append("&op%");
			} else if ((openedSingle || openedDouble) && s.charAt(i) == ')') {
				builder.append("&cp%");
			} else {
				builder.append(s.charAt(i));
			}
		}
		if (openedSingle || openedDouble) {
			throw new RuntimeException("Invalid input quotes.");
		}
		return builder.toString();
	}
}
