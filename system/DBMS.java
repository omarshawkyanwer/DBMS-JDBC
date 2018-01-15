package eg.edu.alexu.csd.oop.jdbc.system;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import eg.edu.alexu.csd.oop.jdbc.database.DTDCreator;
import eg.edu.alexu.csd.oop.jdbc.database.ProtobufDB;
import eg.edu.alexu.csd.oop.jdbc.database.XMLDB;
import eg.edu.alexu.csd.oop.jdbc.interfaces.IDB;
import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;
import eg.edu.alexu.csd.oop.jdbc.parser.Parser;
import eg.edu.alexu.csd.oop.jdbc.parser.SQLChecker;

public class DBMS implements IDBMS {

	private IDB currentDB;
	private DTDCreator dTDHandler;
	private ArrayList<ArrayList<String>> selectedData;
	private ArrayList<String> selectedColumnNames;
	private ArrayList<String> selectedColumnTypes;
	private String selectedTableName;
	private String dbType;
	private String path;
	private int updateCount;
	private Parser parser;
	private SQLChecker sqlChecker;

	public DBMS(final String path, final String type) throws Exception {
		dTDHandler = new DTDCreator();
		createWorkspace(path);
		dbType = type;
		this.path = path;
		parser = new Parser(this);
		sqlChecker = new SQLChecker();
	}

	@Override
	public void alterTable(final String tableName, final String colName, final String dataType) {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}

		currentDB.alterTable(tableName, colName, dataType);
	}

	@Override
	public void createDB(final String dbName) {
		File db = new File(path + File.separator + dbName);
		if (db.exists()) {
			throw new RuntimeException("This database doesn\'t exist.");
		}

		db.mkdir();
	}

	@Override
	public void createTable(final String tableName, final ArrayList<String> columns,
			final ArrayList<String> types) throws TransformerConfigurationException,
			ParserConfigurationException {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}

		currentDB.createTable(tableName, columns, types);
	}

	private void createWorkspace(final String path) {
		File workspace = new File(path);
		if (!workspace.exists()) {
			workspace.mkdir();
		}
	}

	@Override
	public void deleteFromTable(final String table, final String condition) {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}

		updateCount = currentDB.deleteFromTable(table, condition);
	}

	@Override
	public void dropDB(final String dbName) {
		File db = new File(path + File.separator + dbName);
		if (!db.exists()) {
			throw new RuntimeException("This database doesn\'t exist.");
		}
		File[] directoryListing = db.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				child.delete();
			}
		}
		db.delete();
		if (currentDB != null && dbName.equals(currentDB.getName())) {
			currentDB = null;
		}
	}

	@Override
	public void dropTable(final String tableName) {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}

		currentDB.dropTable(tableName);
	}

	@Override
	public void execute(final String command) throws Exception {
		parser.execute(command);
	}

	private IDB getDB(final String dbName) throws Exception {
		if (dbType.equals("xmldb")) {
			return new XMLDB(dbName, dTDHandler, path, this);
		} else if (dbType.equals("altdb")) {
			return new ProtobufDB(dbName, path, this);
		}
		throw new RuntimeException("Unsupported database type.");
	}

	@Override
	public ArrayList<String> getSelectedColumnNames() {
		return selectedColumnNames;
	}

	@Override
	public ArrayList<String> getSelectedColumnTypes() {
		return selectedColumnTypes;
	}

	@Override
	public ArrayList<ArrayList<String>> getSelectedData() {
		return selectedData;
	}

	@Override
	public String getSelectedTableName() {
		return selectedTableName;
	}

	@Override
	public int getUpdateCount() {
		return updateCount;
	}

	@Override
	public void insert(final String tableName, final ArrayList<String> columnsName,
			final ArrayList<String> values) throws Exception {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}

		updateCount = currentDB.insert(tableName, columnsName, values);
	}

	@Override
	public boolean isQueryCommand(final String command) {
		return sqlChecker.isQueryCommand(command);
	}

	@Override
	public void select(final ArrayList<String> columnNames, final String tableName,
			final String condition, final boolean isDistinct) {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}
		currentDB.select(columnNames, tableName, condition, isDistinct);
	}

	@Override
	public void setSelectedColumnNames(final ArrayList<String> columnNames) {
		selectedColumnNames = columnNames;
	}

	@Override
	public void setSelectedColumnTypes(final ArrayList<String> types) {
		selectedColumnTypes = types;
	}

	@Override
	public void setSelectedData(final ArrayList<ArrayList<String>> selected) {
		selectedData = selected;
	}

	@Override
	public void setSelectedTableName(final String tableName) {
		selectedTableName = tableName;
	}

	@Override
	public void update(final ArrayList<String> coloumnsName,
			final ArrayList<String> values, final String tableName, final String condition)
			throws Exception {
		if (currentDB == null) {
			throw new RuntimeException("No database selected.");
		}

		updateCount = currentDB.update(coloumnsName, values, tableName,
				condition);
	}

	@Override
	public void useDB(final String dbName) throws Exception {
		File db = new File(path + File.separator + dbName);
		if (!db.exists()) {
			throw new RuntimeException("This database doesn\'t exist.");
		}

		currentDB = getDB(dbName);
		if (!currentDB.didLoadTablesSucceed()) {
			currentDB = null;
			throw new RuntimeException("Failed to load tables.");
		}
	}

}
