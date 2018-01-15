package eg.edu.alexu.csd.oop.jdbc.database;

import java.util.ArrayList;

public class DTDCreator {

	public String createDTD(final String tableName, final ArrayList<String> columns) {
		String columnNames = getColumns(columns);

		StringBuilder builder = new StringBuilder();
		builder.append("<!ELEMENT " + tableName + " (row*)>\n");
		if (columns.size() > 0) {
			builder.append("<!ELEMENT " + "row " + columnNames + ">\n");
			for (String column : columns) {
				builder.append("<!ELEMENT " + column + " (#PCDATA)>\n");
			}
		}
		return builder.toString();
	}

	private String getColumns(final ArrayList<String> columns) {
		StringBuilder builder = new StringBuilder("(");
		for (int i = 0; i < columns.size() - 1; ++i) {
			builder.append(columns.get(i) + ", ");
		}
		if (columns.size() >= 1) {
			builder.append(columns.get(columns.size() - 1) + ")");
		}
		return builder.toString();
	}

}
