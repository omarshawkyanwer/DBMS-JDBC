package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Drop extends SQLKeywords {
	private String[] splittedCommand;
	private String name;

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand) {
		this.splittedCommand = splittedCommand;
		boolean isDatabase = validate();
		String[] temp = { name };
		isValidName(temp);
		if (isDatabase) {
			dbms.dropDB(name);
		} else {
			dbms.dropTable(name);
		}
	}

	private boolean validate() {
		if (splittedCommand.length == 4) {
			name = splittedCommand[2];
			if (splittedCommand[2].contains(",")) {
				throw new RuntimeException("Unsupported multiple drop command.");
			}
			if (splittedCommand[1].equalsIgnoreCase("database")) {
				return true;
			} else if (splittedCommand[1].equalsIgnoreCase("table")) {
				return false;
			}
		}
		throw new RuntimeException("Invalid drop command.");

	}

}
