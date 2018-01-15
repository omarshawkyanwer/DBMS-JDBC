package eg.edu.alexu.csd.oop.jdbc.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Model {

	public ArrayList<ArrayList<String>> convertToColumnwise(
			final ArrayList<ArrayList<String>> unitIsRow,
			final ArrayList<String> columnNames) {
		ArrayList<ArrayList<String>> unitIsColumn = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < columnNames.size(); ++i) {
			ArrayList<String> column = new ArrayList<String>();
			for (ArrayList<String> row : unitIsRow) {
				column.add(row.get(i));
			}
			unitIsColumn.add(column);
		}
		return unitIsColumn;
	}

	public int determineType(final String type) {
		if (type.equalsIgnoreCase("int")) {
			return DataTypesConstants.typeInteger;
		} else if (type.equalsIgnoreCase("varchar")) {
			return DataTypesConstants.typeString;
		} else if (type.equalsIgnoreCase("date")) {
			return DataTypesConstants.typeDate;
		} else {
			return DataTypesConstants.typeFloat;
		}
	}

	public ArrayList<Integer> determineTypes(final ArrayList<String> types) {
		ArrayList<Integer> typesInt = new ArrayList<Integer>();
		for (String s : types) {
			typesInt.add(determineType(s));
		}
		return typesInt;
	}

	public ArrayList<ArrayList<String>> removeDulplicatesFromRows(
			final ArrayList<ArrayList<String>> selectedWithDuplicates) {
		Set<ArrayList<String>> toRemove = new HashSet<ArrayList<String>>();
		ArrayList<ArrayList<String>> selectedWithNoDuplicates = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> row : selectedWithDuplicates) {
			if (toRemove.add(row)) {
				selectedWithNoDuplicates.add(row);
			}
		}
		return selectedWithNoDuplicates;
	}

	public String typeToString(final int type) {
		if (type == DataTypesConstants.typeInteger) {
			return "int";
		} else if (type == DataTypesConstants.typeFloat) {
			return "float";
		} else if (type == DataTypesConstants.typeDate) {
			return "date";
		} else {
			return "varchar";
		}
	}

}
