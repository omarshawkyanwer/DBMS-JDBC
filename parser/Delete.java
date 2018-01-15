package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Delete extends SQLKeywords {
	private String[] splittedCommand;
	private String tableName, condition;

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand) {
		this.splittedCommand = splittedCommand;
		validate();
		checkCondition(condition);
		dbms.deleteFromTable(tableName.toLowerCase(), condition);
	}

	private String hasCondition() {
		if (splittedCommand.length == 4) {
			return null;
		}
		if (splittedCommand.length == 6 && !splittedCommand[3].equalsIgnoreCase("where")) {
			throw new RuntimeException("Invalid delete command.");
		}
		return splittedCommand[4];
	}

	private void validate() {
		boolean isDelete;
		isDelete = splittedCommand.length == 5 && splittedCommand[1].equalsIgnoreCase("*");
		if (isDelete) {
			String[] temp = new String[4];
			int index = 0;
			for (String str : splittedCommand) {
				if (!str.equalsIgnoreCase("*")) {
					temp[index++] = str;
				}
			}
			splittedCommand = temp;
		}
		if ((splittedCommand.length != 4 && splittedCommand.length != 6)
				|| !splittedCommand[1].equalsIgnoreCase("from")) {
			throw new RuntimeException("Invalid delete command.");
		}
		if (splittedCommand[2].contains(",")) {
			throw new RuntimeException("Unsupported delete command.");
		}
		tableName = splittedCommand[2];
		condition = hasCondition();
	}
}
