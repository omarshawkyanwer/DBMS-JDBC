package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Alter extends SQLKeywords {

	private String[] splittedCommand;
	private String tableName, coulmnName, dataType;

	private void checkDataTypes() {
		String[] dataTypes = { "int", "float", "varchar", "date" };
		boolean isValidType = false;
		for (int i = 0; i < 4; i++) {
			if (dataType.equalsIgnoreCase(dataTypes[i])) {
				isValidType = true;
			}
		}
		if (!isValidType) {
			throw new RuntimeException("Invalid data type.");
		}
	}

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand) {
		this.splittedCommand = splittedCommand;
		validate();
		if (dataType != null) {
			checkDataTypes();
		}
		dbms.alterTable(tableName.toLowerCase(), coulmnName.toLowerCase(), dataType);
	}

	private void validate() {
		if (splittedCommand.length != 7 || !splittedCommand[1].equalsIgnoreCase("table")) {
			throw new RuntimeException("Unsupported alter command.");
		}
		tableName = splittedCommand[2];
		boolean add, del;
		add = splittedCommand[3].equalsIgnoreCase("add");
		del = splittedCommand[3].equalsIgnoreCase("drop") && splittedCommand[4].equalsIgnoreCase("column");
		if (!add && !del) {
			throw new RuntimeException("Unsupported alter command.");
		} else if (add) {
			coulmnName = splittedCommand[4];
			dataType = splittedCommand[5];
		} else if (del) {
			coulmnName = splittedCommand[5];
			dataType = null;
		}
		if (coulmnName.contains(",")) {
			throw new RuntimeException("Unsupported alter command.");
		}
	}

}
