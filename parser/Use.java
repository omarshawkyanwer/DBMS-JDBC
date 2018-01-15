package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Use extends SQLKeywords {

	private String[] splittedCommand;
	private String dbName;

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand) throws Exception {
		this.splittedCommand = splittedCommand;
		validate();
		String[] temp = { dbName };
		isValidName(temp);
		dbms.useDB(dbName);
	}

	private void validate() {
		if (splittedCommand.length == 3 && !splittedCommand[1].contains(",")) {
			dbName = splittedCommand[1];
			return;
		}
		throw new RuntimeException("Invalid use command.");
	}

}
