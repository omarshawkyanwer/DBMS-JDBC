package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Insert extends SQLKeywords {
	private String[] splittedCommand;
	private String[] columns, values;
	private String tableName;
	private Encryptor encryptor;

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand) throws Exception {
		this.splittedCommand = splittedCommand;
		validate();
		isValidName(columns);
		decode(columns);
		decode(values);
		isValidValue(values);
		dbms.insert(tableName.toLowerCase(), arrayToArrayList(columns), arrayToArrayList(values));
	}

	private String[] split(String s) {
		encryptor = new Encryptor();
		s = encryptor.decodeBrackets(s);
		if (s.charAt(0) != '(' || s.charAt(s.length() - 1) != ')') {
			throw new RuntimeException("Wrong format.");
		}
		String[] afterSplit = s.split(",");
		afterSplit[0] = afterSplit[0].replace("(", "");
		for (int i = 0; i < afterSplit.length; ++i) {
			afterSplit[i] = encryptor.decodeQoutes(afterSplit[i]);
		}
		afterSplit[afterSplit.length - 1] = afterSplit[afterSplit.length - 1].replace(")", "");
		return afterSplit;
	}

	private void validate() {
		if ((splittedCommand.length != 7 && splittedCommand.length != 6)
				|| !splittedCommand[1].equalsIgnoreCase("into")) {
			throw new RuntimeException("Invalid insert command.");
		}
		tableName = splittedCommand[2];
		if (tableName.contains(",")) {
			throw new RuntimeException("Unsupported multiple insertion.");
		}
		boolean withCols, withoutCols;
		withCols = splittedCommand.length == 7 && splittedCommand[4].equalsIgnoreCase("values");
		withoutCols = splittedCommand.length == 6 && splittedCommand[3].equalsIgnoreCase("values");
		if (withCols) {
			columns = split(splittedCommand[3]);
			values = split(splittedCommand[5]);
		} else if (withoutCols) {
			values = split(splittedCommand[4]);
		} else {
			throw new RuntimeException("Invalid insert command.");
		}
	}

}
