package eg.edu.alexu.csd.oop.jdbc.parser;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class Select extends SQLKeywords {

	private String[] splittedCommand;
	private String[] columns;
	private String tableName, condition;
	private boolean isDistinct;

	@Override
	public void excute(final IDBMS dbms, final String[] arr) {
		this.splittedCommand = arr;
		validate();
		isValidName(columns);
		checkCondition(condition);
		dbms.select(arrayToArrayList(columns), tableName.toLowerCase(), condition, isDistinct);
	}

	private String hasCondition() {
		if (splittedCommand.length == 5 || splittedCommand.length == 6) {
			return null;
		}
		if (splittedCommand.length == 8 && splittedCommand[5].equalsIgnoreCase("where")) {
			return splittedCommand[6];
		}
		if (splittedCommand.length == 7 && splittedCommand[4].equalsIgnoreCase("where")) {
			return splittedCommand[5];
		}
		throw new RuntimeException("Invalid select command.");
	}

	private void validate() {
		boolean sel, selCon, distict;
		sel = (splittedCommand.length == 5) && splittedCommand[2].equalsIgnoreCase("From")
				&& !splittedCommand[1].equalsIgnoreCase("Distinct");
		selCon = (splittedCommand.length == 7) && splittedCommand[2].equalsIgnoreCase("From")
				&& !splittedCommand[1].equalsIgnoreCase("Distinct");
		distict = (splittedCommand.length == 6 || splittedCommand.length == 8)
				&& splittedCommand[1].equalsIgnoreCase("Distinct") && splittedCommand[3].equalsIgnoreCase("From");
		if (!sel && !selCon && !distict) {
			throw new RuntimeException("Invalid select command.");
		}
		if (sel || selCon) {
			if (!splittedCommand[1].equals("*")) {
				columns = splittedCommand[1].split(",");
			}

			if (splittedCommand[3].contains(",")) {
				throw new RuntimeException("Unsupported multiple selection.");
			}
			tableName = splittedCommand[3];
			isDistinct = false;
		} else if (distict) {
			isDistinct = true;
			tableName = splittedCommand[4];
			if (splittedCommand[2].equals("*")) {
				return;
			}
			columns = splittedCommand[2].split(",");
		}
		condition = hasCondition();
	}

}