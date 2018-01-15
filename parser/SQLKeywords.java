package eg.edu.alexu.csd.oop.jdbc.parser;

import java.util.ArrayList;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public abstract class SQLKeywords {

	protected ArrayList<String> arrayToArrayList(final String[] s) {
		if (s == null) {
			return null;
		}
		ArrayList<String> data = new ArrayList<String>();

		for (String str : s) {
			data.add(str);
		}
		return data;
	}

	protected void checkCondition(final String s) {
		if (s == null || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
			return;
		}
		boolean foundOperator = false, openedSingleQoutes = false, openedDoupleQoutes = false;
		for (int i = 0; i < s.length(); ++i) {
			char currentChar = s.charAt(i);
			if (!openedSingleQoutes && !openedDoupleQoutes
					&& (currentChar == '=' || currentChar == '>' || currentChar == '<')) {
				if (!foundOperator) {
					foundOperator = true;
				} else {
					throw new RuntimeException("Invalid condition.");
				}
			}
			if (!openedDoupleQoutes && currentChar == '\'') {
				openedSingleQoutes ^= true;
			}
			if (!openedSingleQoutes && currentChar == '\"') {
				openedDoupleQoutes ^= true;
			}
		}
		if (!foundOperator) {
			throw new RuntimeException("Invalid condition.");
		}

	}

	protected void decode(final String[] str) {
		if (str == null) {
			return;
		}
		Encryptor e = new Encryptor();
		for (String s : str) {
			s = e.decodeQoutes((e.decodeBrackets(s)));
		}
	}

	public abstract void excute(IDBMS dbms, String[] splittedCommand) throws Exception;

	protected void isValidName(final String[] str) {
		if (str == null) {
			return;
		}
		Encryptor e = new Encryptor();
		for (int i = 0; i < str.length; ++i) {
			if (str[i] == null) {
				throw new RuntimeException("Invalid name.");
			}
			str[i] = e.decodeBrackets(str[i]);
			str[i] = e.decodeQoutes(str[i]);
			str[i] = str[i].toLowerCase();
			boolean invaled1, invaled2, invaled3, invaled4;
			invaled1 = str[i].contains(" ");
			invaled2 = str[i].contains("(") || str[i].contains(")");
			invaled3 = str[i].contains("'") || str[i].contains("\"");
			invaled4 = str[i].contains("=") || str[i].contains(">") || str[i].contains("<");
			if (invaled1 || invaled2 || invaled3 || invaled4) {
				throw new RuntimeException("Invalid name.");
			}
		}
	}

	protected void isValidValue(final String[] str) {
		if (str == null) {
			return;
		}
		for (String s : str) {
			boolean isString = false;
			for (char ch = 'a'; ch <= 'z'; ++ch) {
				isString |= s.contains("" + ch);
			}
			for (char ch = 'A'; ch <= 'Z'; ++ch) {
				isString |= s.contains("" + ch);
			}

			if (isString && s.charAt(0) != '\'' && s.charAt(s.length() - 1) != '\"') {
				throw new RuntimeException("Invalid type.");
			}
		}

	}

}
