package eg.edu.alexu.csd.oop.jdbc.table;

import java.util.ArrayList;
import java.util.Map;

public class ColumnHandler {

	private Map<String, Integer> dataTable;

	public ColumnHandler(final Map<String, Integer> data) {
		dataTable = data;
	}

	public boolean areColumnsValid(final ArrayList<String> columnNames,
			final ArrayList<String> values) {
		for (int i = 0; i < columnNames.size(); ++i) {
			if (!isColumnValid(columnNames.get(i), values.get(i))) {
				return false;
			}
		}
		return (columnNames.size() == values.size());
	}

	public boolean isColumnValid(final String columnName, final String valueClaimed) {
		if (!dataTable.containsKey(columnName)) {
			return false;
		}
		String adjustedValue = null;
		if (dataTable.get(columnName).equals(DataTypesConstants.typeDate)
				|| dataTable.get(columnName).equals(
						DataTypesConstants.typeString)) {
			adjustedValue = valueClaimed
					.substring(1, valueClaimed.length() - 1);
			if (adjustedValue.length() == 0) {
				return false;
			}
		} else {
			adjustedValue = valueClaimed;
		}

		return DataTypesClassifier.typeClassification(adjustedValue,
				dataTable.get(columnName));
	}
}
