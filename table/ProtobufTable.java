package eg.edu.alexu.csd.oop.jdbc.table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;
import eg.edu.alexu.csd.oop.jdbc.interfaces.ITable;
import eg.edu.alexu.csd.oop.jdbc.table.PBGeneratedTable.Table;
import eg.edu.alexu.csd.oop.jdbc.table.PBGeneratedTable.Table.Column;
import eg.edu.alexu.csd.oop.jdbc.table.PBGeneratedTable.Table.Column.Builder;

public class ProtobufTable implements ITable {

	private Map<String, Integer> tableData;
	private ArrayList<String> order;
	private ColumnHandler check;
	private String dbName;
	private String tableName;
	private String fileWithExtension;
	private String path;
	private IDBMS dbms;
	private Model engine;

	public ProtobufTable(final String tableName,
			final ArrayList<String> columns, final ArrayList<String> types,
			final String dbName, final String path, final IDBMS dbms) {
		this.dbName = dbName;
		this.tableName = tableName;
		this.path = path;
		this.dbms = dbms;
		engine = new Model();
		fileWithExtension = tableName + ".ser";
		tableData = new HashMap<String, Integer>();
		order = new ArrayList<String>();
		ArrayList<Integer> typesInt = engine.determineTypes(types);
		for (int i = 0; i < columns.size(); i++) {
			tableData.put(columns.get(i), typesInt.get(i));
			order.add(columns.get(i));
		}
		check = new ColumnHandler(tableData);
	}

	private void addColumnToTable(final String colName, final String dataType)
			throws Exception {
		FileInputStream inStream = new FileInputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		Table table = Table.parseFrom(inStream);
		inStream.close();

		Table.Builder tableBuilder = table.toBuilder();
		Builder colBuilder = tableBuilder.addColumnsBuilder();
		colBuilder.setName(colName);
		for (int i = 0; i < table.getColumns(0).getValueCount(); ++i) {
			colBuilder.addValue("null");
		}

		tableData.put(colName, engine.determineType(dataType));
		order.add(colName);

		table = tableBuilder.build();
		FileOutputStream outStream = new FileOutputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		table.writeTo(outStream);
		outStream.close();
	}

	@Override
	public void alterTable(final String colName, final String dataType)
			throws Exception {
		if (dataType == null) {
			deleteColumnFromTable(colName);
		} else {
			if (tableData.containsKey(colName)) {
				throw new RuntimeException("This column already exists.");
			}
			addColumnToTable(colName, dataType);
		}
	}

	private void deleteColumnFromTable(final String colName) throws Exception {
		FileInputStream inStream = new FileInputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		Table table = Table.parseFrom(inStream);
		inStream.close();

		Table.Builder tableBuilder = table.toBuilder();
		for (int i = 0; i < table.getColumnsCount(); ++i) {
			if (table.getColumns(i).getName().equals(colName)) {
				tableBuilder.removeColumns(i);
				break;
			}
		}
		removeFromColumnsVariables(colName);

		table = tableBuilder.build();
		FileOutputStream outStream = new FileOutputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		table.writeTo(outStream);
		outStream.close();
	}

	@Override
	public int deleteFromTable(final String condition) {
		try {
			FileInputStream inStream = new FileInputStream(new File(path
					+ File.separator + dbName + File.separator
					+ fileWithExtension));
			Table table = Table.parseFrom(inStream);
			inStream.close();

			List<Column> columns = table.getColumnsList();
			Map<Integer, Boolean> deletedIndexes = getDeletedIndexes(columns,
					condition);

			Table.Builder newTableBuilder = Table.newBuilder();
			newTableBuilder.setName(table.getName());
			for (int i = 0; i < columns.size(); ++i) {
				Table.Column.Builder colBuilder = newTableBuilder
						.addColumnsBuilder();
				colBuilder.setName(columns.get(i).getName());
				for (int j = 0; j < columns.get(i).getValueCount(); ++j) {
					if (!deletedIndexes.containsKey(j)) {
						colBuilder.addValue(columns.get(i).getValue(j));
					}
				}
			}

			Table newTable = newTableBuilder.build();
			FileOutputStream outStream = new FileOutputStream(new File(path
					+ File.separator + dbName + File.separator
					+ fileWithExtension));
			newTable.writeTo(outStream);
			outStream.close();
			return deletedIndexes.size();
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete data.");
		}
	}

	public ArrayList<String> getColumnNames() {
		return order;
	}

	public ArrayList<String> getColumnTypes() {
		ArrayList<String> types = new ArrayList<String>();
		for (String column : order) {
			types.add(engine.typeToString(tableData.get(column)));
		}
		return types;
	}

	private Map<Integer, Boolean> getDeletedIndexes(final List<Column> columns,
			final String condition) {
		Map<Integer, Boolean> deletedIndexes = new HashMap<Integer, Boolean>();
		if (condition == null) {
			for (int i = 0; i < columns.get(0).getValueCount(); ++i) {
				deletedIndexes.put(i, true);
			}
		} else {
			ConditionHandler conditionTaker = new ConditionHandler(condition,
					tableData);
			Column conditionColumn = null;
			for (Column col : columns) {
				if (col.getName().equals(conditionTaker.getColumnName())) {
					conditionColumn = col;
					break;
				}
			}
			for (int i = 0; i < conditionColumn.getValueCount(); ++i) {
				if (conditionTaker.evaluateCondition(
						conditionColumn.getValue(i),
						tableData.get(conditionColumn.getName()))) {
					deletedIndexes.put(i, true);
				}
			}
		}
		return deletedIndexes;
	}

	private String getNullValueOfColumn(final String columnName) {
		return tableData.get(columnName).equals(DataTypesConstants.typeString)
				|| tableData.get(columnName)
						.equals(DataTypesConstants.typeDate) ? "\'null\'"
				: "null";
	}

	private ArrayList<Integer> getRowsMatchCondition(
			final ConditionHandler conditionTaker, final List<Column> columns) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		if (conditionTaker.getCondition() == null) {
			for (int i = 0; i < columns.get(0).getValueCount(); ++i) {
				ret.add(i);
			}
		} else {
			Column conditionColumn = null;
			for (Column col : columns) {
				if (col.getName().equals(conditionTaker.getColumnName())) {
					conditionColumn = col;
					break;
				}
			}
			for (int i = 0; i < conditionColumn.getValueCount(); ++i) {
				if (conditionTaker.evaluateCondition(
						conditionColumn.getValue(i),
						tableData.get(conditionColumn.getName()))) {
					ret.add(i);
				}
			}
		}
		return ret;
	}

	private ArrayList<String> getTypes(final ArrayList<String> columnNames) {
		ArrayList<String> types = new ArrayList<String>();
		for (String name : columnNames) {
			types.add(engine.typeToString(tableData.get(name)));
		}
		return types;
	}

	@Override
	public int insert(final ArrayList<String> columnNames,
			final ArrayList<String> values) throws Exception {
		ArrayList<String> orderedValues = setOrderedValuesInInsert(columnNames,
				values);
		return insertCore(orderedValues);
	}

	private int insertCore(final ArrayList<String> orderedValues)
			throws Exception {
		FileInputStream inStream = new FileInputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		Table table = Table.parseFrom(inStream);
		inStream.close();

		Table.Builder tableBuilder = table.toBuilder();
		for (int i = 0; i < order.size(); ++i) {
			String insertedValue = removeQuotes(orderedValues.get(i),
					order.get(i));
			tableBuilder.setColumns(i, table.getColumns(i).toBuilder()
					.addValue(insertedValue).build());
		}

		table = tableBuilder.build();
		FileOutputStream outStream = new FileOutputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		table.writeTo(outStream);
		outStream.close();
		return 1;
	}

	private void removeFromColumnsVariables(final String colName) {
		for (int i = 0; i < order.size(); ++i) {
			if (order.get(i).equals(colName)) {
				order.remove(i);
				break;
			}
		}
		tableData.remove(colName);
	}

	private String removeQuotes(final String valueOfColumn,
			final String columnName) {
		String returnValue = valueOfColumn;
		if (tableData.get(columnName).equals(DataTypesConstants.typeDate)
				|| tableData.get(columnName).equals(
						DataTypesConstants.typeString)) {
			returnValue = valueOfColumn
					.substring(1, valueOfColumn.length() - 1);
		}
		return returnValue;
	}

	@Override
	public void select(ArrayList<String> columnNames, final String condition,
			final boolean distinct) {
		if (columnNames == null) {
			columnNames = order;
		}
		ArrayList<ArrayList<String>> selected = null;
		try {
			ConditionHandler conditionTaker = new ConditionHandler(condition,
					this.tableData);
			selected = selectCore(columnNames, conditionTaker);
		} catch (Exception e) {
			throw new RuntimeException("Failed to select data.");
		}

		selected = distinct ? engine.convertToColumnwise(
				engine.removeDulplicatesFromRows(selected), columnNames)
				: engine.convertToColumnwise(selected, columnNames);
		ArrayList<String> types = getTypes(columnNames);
		sendSelectedDataToDBMS(columnNames, types, selected);
	}

	private ArrayList<ArrayList<String>> selectCore(
			final ArrayList<String> columnNames,
			final ConditionHandler conditionTaker) throws Exception {
		FileInputStream inStream = new FileInputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		Table table = Table.parseFrom(inStream);
		inStream.close();

		List<Column> columns = table.getColumnsList();
		ArrayList<Integer> selectedIndexes = getRowsMatchCondition(
				conditionTaker, columns);

		ArrayList<ArrayList<String>> selected = new ArrayList<ArrayList<String>>();
		for (int j : selectedIndexes) {
			ArrayList<String> row = new ArrayList<String>();
			for (int i = 0; i < columns.size(); ++i) {
				for (int k = 0; k < columnNames.size(); ++k) {
					if (columns.get(i).getName().equals(columnNames.get(k))) {
						row.add(columns.get(i).getValue(j));
						break;
					}
				}
			}
			selected.add(row);
		}

		return selected;
	}

	@Override
	public void sendSelectedDataToDBMS(final ArrayList<String> columnNames,
			final ArrayList<String> types,
			final ArrayList<ArrayList<String>> data) {
		dbms.setSelectedColumnNames(columnNames);
		dbms.setSelectedColumnTypes(types);
		dbms.setSelectedData(data);
		dbms.setSelectedTableName(tableName);
	}

	private ArrayList<String> setOrderedNamesInUpdate(
			final ArrayList<String> columnNames, final ArrayList<String> values) {
		ArrayList<String> orderedNames = new ArrayList<String>();
		if (columnNames != null) {
			for (int i = 0; i < order.size(); ++i) {
				boolean exists = false;
				for (int j = 0; j < columnNames.size(); ++j) {
					if (columnNames.get(j).equals(order.get(i))) {
						exists = true;
						break;
					}
				}
				if (exists) {
					orderedNames.add(order.get(i));
				}
			}
		} else {
			if (values.size() != order.size()) {
				throw new RuntimeException("Invalid update command.");
			}
			for (int i = 0; i < order.size(); ++i) {
				orderedNames.add(order.get(i));
			}
		}
		return orderedNames;
	}

	private ArrayList<String> setOrderedValuesInInsert(
			final ArrayList<String> columnNames, final ArrayList<String> values) {
		ArrayList<String> orderedValues = new ArrayList<String>();
		if (columnNames != null) {
			if (!check.areColumnsValid(columnNames, values)) {
				throw new RuntimeException("Invalid columns data types.");
			}
			for (int i = 0; i < order.size(); ++i) {
				boolean exists = false;
				String value = null;
				for (int j = 0; j < columnNames.size(); ++j) {
					if (columnNames.get(j).equals(order.get(i))) {
						exists = true;
						value = values.get(j);
						break;
					}
				}
				if (exists) {
					orderedValues.add(value);
				} else {
					orderedValues.add(getNullValueOfColumn(order.get(i)));
				}
			}
		} else {
			if (values.size() != order.size()) {
				throw new RuntimeException("Invalid insert command.");
			}
			if (!check.areColumnsValid(order, values)) {
				throw new RuntimeException("Invalid columns data types.");
			}
			for (int i = 0; i < order.size(); ++i) {
				orderedValues.add(values.get(i));
			}
		}
		return orderedValues;
	}

	private ArrayList<String> setOrderedValuesInUpdate(
			final ArrayList<String> columnNames, final ArrayList<String> values) {
		ArrayList<String> orderedValues = new ArrayList<String>();
		if (columnNames != null) {
			if (!check.areColumnsValid(columnNames, values)) {
				throw new RuntimeException("Invalid updated data.");
			}
			for (int i = 0; i < order.size(); ++i) {
				boolean exists = false;
				String value = null;
				for (int j = 0; j < columnNames.size(); ++j) {
					if (columnNames.get(j).equals(order.get(i))) {
						exists = true;
						value = values.get(j);
						break;
					}
				}
				if (exists) {
					orderedValues.add(value);
				}
			}
		} else {
			if (values.size() != order.size()) {
				throw new RuntimeException("Invalid update command.");
			}
			if (!check.areColumnsValid(order, values)) {
				throw new RuntimeException("Invalid columns data types.");
			}
			for (int i = 0; i < order.size(); ++i) {
				orderedValues.add(values.get(i));
			}
		}
		return orderedValues;
	}

	@Override
	public int update(final ArrayList<String> columnNames,
			final ArrayList<String> values, final String condition)
			throws Exception {
		ArrayList<String> orderedValues = setOrderedValuesInUpdate(columnNames,
				values);
		ArrayList<String> orderedNames = setOrderedNamesInUpdate(columnNames,
				values);

		ConditionHandler conditionTaker = new ConditionHandler(condition,
				this.tableData);
		return updateCore(conditionTaker, orderedValues, orderedNames);
	}

	private int updateCore(final ConditionHandler conditionTaker,
			final ArrayList<String> orderedValues,
			final ArrayList<String> orderedNames) throws Exception {
		FileInputStream inStream = new FileInputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		Table table = Table.parseFrom(inStream);
		inStream.close();

		List<Column> columns = table.getColumnsList();
		ArrayList<Integer> updateIndexes = getRowsMatchCondition(
				conditionTaker, columns);

		Table.Builder tableBuilder = table.toBuilder();
		for (int j : updateIndexes) {
			for (int i = 0; i < columns.size(); ++i) {
				for (int k = 0; k < orderedNames.size(); ++k) {
					if (columns.get(i).getName().equals(orderedNames.get(k))) {
						String newValue = removeQuotes(orderedValues.get(k),
								orderedNames.get(k));
						tableBuilder.getColumnsBuilder(i).setValue(j, newValue);
						break;
					}
				}
			}
		}

		table = tableBuilder.build();
		FileOutputStream outStream = new FileOutputStream(new File(path
				+ File.separator + dbName + File.separator + fileWithExtension));
		table.writeTo(outStream);
		outStream.close();
		return updateIndexes.size();
	}

}
