package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Update extends SQLKeywords {

	private String[] splittedCommand;
	private String[] columns;
	private String[] values;
	private String tableName;
	private String condition;

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand) throws Exception {
		this.splittedCommand = splittedCommand;
		validate();
		isValidName(columns);
		decode(columns);
		decode(values);
		checkCondition(condition);
		isValidValue(values);
		dbms.update(arrayToArrayList(columns), arrayToArrayList(values), tableName.toLowerCase(),
				condition);
	}

	private String hasCondition() {
		if (splittedCommand.length == 5) {
			return null;
		}
		if (splittedCommand.length == 7 && !splittedCommand[4].equalsIgnoreCase("where")) {
			throw new RuntimeException("Invalid update command.");
		}
		return splittedCommand[5];
	}

	private void validate() {
		if ((splittedCommand.length != 5 && splittedCommand.length != 7)
				|| !splittedCommand[2].equalsIgnoreCase("set")) {
			throw new RuntimeException("Invalid update command.");
		}
		if (splittedCommand[1].contains(",")) {
			throw new RuntimeException("Unsupported multiple update.");
		}
		tableName = splittedCommand[1];
		String[] conditions = splittedCommand[3].split(",");
		columns = new String[conditions.length];
		values = new String[conditions.length];
		for (int i = 0; i < conditions.length; ++i) {
			String[] testCondition = conditions[i].split("=");
			if (testCondition.length != 2) {
				throw new RuntimeException("Invalid update command.");
			}
			columns[i] = testCondition[0];
			values[i] = testCondition[1];
		}
		condition = hasCondition();
	}

}
