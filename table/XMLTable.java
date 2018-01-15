package eg.edu.alexu.csd.oop.jdbc.table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;
import eg.edu.alexu.csd.oop.jdbc.interfaces.ITable;

public class XMLTable implements ITable {

	private Transformer transformer;
	private DocumentBuilder dBuilder;
	private Map<String, Integer> tableData;
	private ArrayList<String> order;
	private ColumnHandler check;
	private String dbName;
	private String tableName;
	private String fileWithExtension;
	private String path;
	private IDBMS dbms;
	private Model engine;

	public XMLTable(final String tableName, final ArrayList<String> columns,
			final ArrayList<String> types, final String dbName,
			final String path, final IDBMS dbms)
			throws TransformerConfigurationException,
			ParserConfigurationException {
		this.dbName = dbName;
		this.tableName = tableName;
		this.path = path;
		this.dbms = dbms;
		engine = new Model();
		fileWithExtension = tableName + ".xml";
		tableData = new HashMap<String, Integer>();
		order = new ArrayList<String>();
		ArrayList<Integer> typesInt = engine.determineTypes(types);
		for (int i = 0; i < columns.size(); i++) {
			tableData.put(columns.get(i), typesInt.get(i));
			order.add(columns.get(i));
		}
		check = new ColumnHandler(tableData);
		createWritersAndTransfomers();
	}

	private void addColumnToTable(final String colName, final String dataType)
			throws IOException, TransformerException, SAXException {
		Document document = dBuilder.parse(new File(path + File.separator
				+ dbName + File.separator + fileWithExtension));
		Node rootElement = document.getDocumentElement();
		NodeList rows = document.getElementsByTagName("row");
		ArrayList<ArrayList<String>> table = getTable(rows);
		while (rows.getLength() > 0) {
			rootElement.removeChild(rows.item(0));
		}
		for (ArrayList<String> row : table) {
			Element newRow = document.createElement("row");
			for (int j = 0; j < order.size(); ++j) {
				Element newColumn = document.createElement(order.get(j));
				newColumn.setTextContent(row.get(j));
				newRow.appendChild(newColumn);
			}
			Element extraColumn = document.createElement(colName);
			extraColumn.setTextContent("null");
			newRow.appendChild(extraColumn);
			rootElement.appendChild(newRow);
		}
		tableData.put(colName, engine.determineType(dataType));
		order.add(colName);
		writeXML(document);
	}

	@Override
	public void alterTable(final String colName, final String dataType)
			throws IOException, SAXException, TransformerException {
		if (dataType == null) {
			deleteColumnFromTable(colName);
		} else {
			if (tableData.containsKey(colName)) {
				throw new RuntimeException("This column already exists.");
			}
			addColumnToTable(colName, dataType);
		}
	}

	private void createWritersAndTransfomers()
			throws TransformerConfigurationException,
			ParserConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "3");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setValidating(true);
		dBuilder = dbFactory.newDocumentBuilder();
		dBuilder.setErrorHandler(new ErrorHandler() {
			@Override
			public void error(final SAXParseException arg0) throws SAXException {
				throw arg0;
			}

			@Override
			public void fatalError(final SAXParseException arg0)
					throws SAXException {
				throw arg0;
			}

			@Override
			public void warning(final SAXParseException arg0)
					throws SAXException {
				throw arg0;
			}
		});
	}

	private void deleteColumnFromTable(final String colName)
			throws IOException, SAXException, TransformerException {
		Document document = dBuilder.parse(new File(path + File.separator
				+ dbName + File.separator + fileWithExtension));
		NodeList rows = document.getElementsByTagName("row");
		for (int i = 0; i < rows.getLength(); i++) {
			Element row = (Element) rows.item(i);
			row.removeChild(row.getElementsByTagName(colName).item(0));
		}
		for (int i = 0; i < order.size(); ++i) {
			if (order.get(i).equals(colName)) {
				order.remove(i);
				break;
			}
		}
		tableData.remove(colName);
		writeXML(document);
	}

	@Override
	public int deleteFromTable(final String condition) {
		try {
			ConditionHandler conditionTaker = new ConditionHandler(condition,
					tableData);

			Document document = dBuilder.parse(new File(path + File.separator
					+ dbName + File.separator + fileWithExtension));
			Node rootElement = document.getDocumentElement();
			NodeList rows = document.getElementsByTagName("row");
			int deleted = 0;
			for (int i = 0; i < rows.getLength(); ++i) {
				Element row = (Element) rows.item(i);
				if (rowMatchesCondition(conditionTaker, row)) {
					rootElement.removeChild(row);
					i--;
					++deleted;
				}
			}
			writeXML(document);
			return deleted;
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

	private String getNullValueOfColumn(final String columnName) {
		return tableData.get(columnName).equals(DataTypesConstants.typeString)
				|| tableData.get(columnName)
						.equals(DataTypesConstants.typeDate) ? "\'null\'"
				: "null";
	}

	private ArrayList<ArrayList<String>> getTable(final NodeList rows) {
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < rows.getLength(); ++i) {
			ArrayList<String> newRow = new ArrayList<String>();
			Element row = (Element) rows.item(i);
			for (int j = 0; j < order.size(); ++j) {
				newRow.add(new String(row.getElementsByTagName(order.get(j))
						.item(0).getTextContent()));
			}
			table.add(new ArrayList<String>(newRow));
		}
		return table;
	}

	private ArrayList<String> getTypes(final ArrayList<String> columnNames) {
		ArrayList<String> types = new ArrayList<String>();
		for (String name : columnNames) {
			types.add(engine.typeToString(tableData.get(name)));
		}
		return types;
	}

	@Override
	public int insert(final ArrayList<String> columnsName,
			final ArrayList<String> values) throws Exception {
		ArrayList<String> orderedValues = setOrderedValuesInInsert(columnsName,
				values);
		return insertCore(orderedValues);
	}

	private int insertCore(final ArrayList<String> orderedValues)
			throws IOException, SAXException, TransformerException {
		Document document = dBuilder.parse(new File(path + File.separator
				+ dbName + File.separator + fileWithExtension));
		Node rootElement = document.getDocumentElement();
		Element newRow = document.createElement("row");
		for (int i = 0; i < order.size(); ++i) {
			String insertedValue = removeQuotes(orderedValues.get(i),
					order.get(i));
			Element newColumn = document.createElement(order.get(i));
			newColumn.setTextContent(insertedValue);
			newRow.appendChild(newColumn);
		}
		rootElement.appendChild(newRow);
		writeXML(document);
		return 1;
	}

	private String removeQuotes(final String valueOfColumn,
			final String columnName) {
		String returnValue = null;
		if (tableData.get(columnName).equals(DataTypesConstants.typeDate)
				|| tableData.get(columnName).equals(
						DataTypesConstants.typeString)) {
			returnValue = valueOfColumn
					.substring(1, valueOfColumn.length() - 1);
		} else {
			returnValue = valueOfColumn;
		}
		return returnValue;
	}

	private boolean rowMatchesCondition(final ConditionHandler conditionTaker,
			final Element row) {
		String conditionColumnName = conditionTaker.getColumnName();
		Integer conditionColumnNameInt = null;
		String toBeEvaluated = null;
		if (conditionColumnName != null) {
			toBeEvaluated = row
					.getElementsByTagName(conditionTaker.getColumnName())
					.item(0).getTextContent();
			conditionColumnNameInt = tableData.get(conditionColumnName);
		}
		return conditionTaker.evaluateCondition(toBeEvaluated,
				conditionColumnNameInt);
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
			selected = selectCore(columnNames, conditionTaker, distinct);
		} catch (Exception e) {
			throw new RuntimeException("Invalid selected data.");
		}

		selected = distinct ? engine.convertToColumnwise(
				engine.removeDulplicatesFromRows(selected), columnNames)
				: engine.convertToColumnwise(selected, columnNames);
		ArrayList<String> types = getTypes(columnNames);
		sendSelectedDataToDBMS(columnNames, types, selected);
	}

	private ArrayList<ArrayList<String>> selectCore(
			final ArrayList<String> columnNames,
			final ConditionHandler conditionTaker, final boolean distinct)
			throws IOException, SAXException {
		ArrayList<ArrayList<String>> selected = new ArrayList<ArrayList<String>>();

		Document document = dBuilder.parse(new File(path + File.separator
				+ dbName + File.separator + fileWithExtension));
		NodeList rows = document.getElementsByTagName("row");
		for (int i = 0; i < rows.getLength(); i++) {
			Element row = (Element) rows.item(i);
			if (rowMatchesCondition(conditionTaker, row)) {
				ArrayList<String> toBeAdded = new ArrayList<String>();
				for (int j = 0; j < columnNames.size(); ++j) {
					toBeAdded.add(row.getElementsByTagName(columnNames.get(j))
							.item(0).getTextContent());
				}
				selected.add(toBeAdded);
			}
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
				throw new RuntimeException("Invalid updated data.");
			}
			for (int i = 0; i < order.size(); ++i) {
				orderedNames.add(order.get(i));
			}
		}
		return orderedNames;
	}

	private ArrayList<String> setOrderedValuesInInsert(
			final ArrayList<String> columnsName, final ArrayList<String> values) {
		ArrayList<String> orderedValues = new ArrayList<String>();
		if (columnsName != null) {
			if (!check.areColumnsValid(columnsName, values)) {
				throw new RuntimeException("Invalid updated data.");
			}
			for (int i = 0; i < order.size(); ++i) {
				boolean exists = false;
				String value = null;
				for (int j = 0; j < columnsName.size(); ++j) {
					if (columnsName.get(j).equals(order.get(i))) {
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
				throw new RuntimeException("Invalid updated data.");
			}
			if (!check.areColumnsValid(order, values)) {
				throw new RuntimeException("Invalid updated data types.");
			}
			for (int i = 0; i < order.size(); ++i) {
				orderedValues.add(values.get(i));
			}
		}
		return orderedValues;
	}

	@Override
	public int update(final ArrayList<String> columnsName,
			final ArrayList<String> values, final String condition)
			throws Exception {
		ArrayList<String> orderedValues = setOrderedValuesInUpdate(columnsName,
				values);
		ArrayList<String> orderedNames = setOrderedNamesInUpdate(columnsName,
				values);

		ConditionHandler conditionTaker = new ConditionHandler(condition,
				this.tableData);
		return updateCore(conditionTaker, orderedValues, orderedNames);
	}

	private int updateCore(final ConditionHandler conditionTaker,
			final ArrayList<String> orderedValues,
			final ArrayList<String> orderedNames) throws IOException,
			SAXException, TransformerException {

		Document document = dBuilder.parse(new File(path + File.separator
				+ dbName + File.separator + fileWithExtension));
		NodeList rows = document.getElementsByTagName("row");
		int updated = 0;
		for (int i = 0; i < rows.getLength(); i++) {
			Element row = (Element) rows.item(i);
			if (rowMatchesCondition(conditionTaker, row)) {
				for (int j = 0; j < orderedNames.size(); ++j) {
					String newValue = removeQuotes(orderedValues.get(j),
							orderedNames.get(j));
					row.getElementsByTagName(orderedNames.get(j)).item(0)
							.setTextContent(newValue);
				}
				++updated;
			}
		}
		writeXML(document);
		return updated;
	}

	private void writeXML(final Document toBeWritten)
			throws TransformerException {
		DOMSource source = new DOMSource(toBeWritten);
		StreamResult result = new StreamResult(new File(path + File.separator
				+ dbName + File.separator + fileWithExtension));
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, tableName
				+ ".dtd");
		transformer.transform(source, result);
	}
}
