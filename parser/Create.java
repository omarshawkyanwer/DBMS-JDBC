package eg.edu.alexu.csd.oop.jdbc.parser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Create extends SQLKeywords {
	private String[] splittedCommand;
	private String[] columns, columnsTypes;
	private String name;

	@Override
	public void excute(final IDBMS dbms, final String[] splittedCommand)
			throws TransformerConfigurationException, ParserConfigurationException {
		this.splittedCommand = splittedCommand;
		boolean isDatabase = validate();
		String[] temp = { name };
		isValidName(temp);
		isValidName(columns);
		decode(columns);
		if (isDatabase) {
			dbms.createDB(name);
		} else {
			dbms.createTable(name, arrayToArrayList(columns), arrayToArrayList(columnsTypes));
		}
	}

	private void setColsAndTypes(final String str) {
		String[] tmp = str.split(","), tmp2;
		int sz = tmp.length;
		String[] dataTypes = { "int", "float", "varchar", "date" };
		columnsTypes = new String[sz];
		columns = new String[sz];
		for (int i = 0; i < sz; i++) {
			tmp2 = tmp[i].split(" ");
			if (tmp2.length != 2) {
				throw new RuntimeException("Syntax error.");
			}
			columns[i] = tmp2[0];
			columnsTypes[i] = tmp2[1];
			boolean isValidType = false;
			for (int j = 0; j < 4; j++) {
				if (columnsTypes[i].equalsIgnoreCase(dataTypes[j])) {
					isValidType = true;
					break;
				}
			}
			if (!isValidType) {
				throw new RuntimeException("Unsupported data type.");
			}
		}
	}

	private boolean validate() {
		name = splittedCommand[2];
		if (splittedCommand[2].contains(",")) {
			throw new RuntimeException("Unsupported create command.");
		}
		boolean databaseCreation, tableCreation;
		databaseCreation = splittedCommand.length == 4 && splittedCommand[1].equalsIgnoreCase("database");
		tableCreation = splittedCommand.length == 5 && splittedCommand[1].equalsIgnoreCase("table");
		if (databaseCreation) {
			return true;
		}
		if (tableCreation) {
			Encryptor e = new Encryptor();
			splittedCommand[3] = e.decodeBrackets(splittedCommand[3]);
			String col = splittedCommand[3];
			if (col.charAt(0) != '(' || col.charAt(col.length() - 1) != ')') {
				throw new RuntimeException("Syntax error.");
			}
			col = col.trim();
			col = col.substring(1, col.length() - 1);
			setColsAndTypes(col);
			return false;
		}
		throw new RuntimeException("Invalid create command.");
	}

}
