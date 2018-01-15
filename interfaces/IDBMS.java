package eg.edu.alexu.csd.oop.jdbc.interfaces;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

/**
 * The Interface IDBMS.
 */
public interface IDBMS {

	/**
	 * Alter table. Forwards the command to the data base.
	 *
	 * @param tableName
	 *            the table name
	 * @param colName
	 *            the column name
	 * @param dataType
	 *            the data type
	 */
	public void alterTable(String tableName, String colName, String dataType);

	/**
	 * Creates the db.
	 *
	 * @param dbName
	 *            the db name
	 */
	public void createDB(String dbName);

	/**
	 * Create table. Forwards the command to the data base.
	 *
	 * @param tableName
	 *            the table name
	 * @param columns
	 *            the columns
	 * @param types
	 *            the types
	 * @throws TransformerConfigurationException
	 *             the transformer configuration exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 */
	public void createTable(String tableName, ArrayList<String> columns,
			ArrayList<String> types) throws TransformerConfigurationException,
			ParserConfigurationException;

	/**
	 * Delete from table. Forwards the command to the data base.
	 *
	 * @param table
	 *            the table
	 * @param condition
	 *            the condition
	 */
	public void deleteFromTable(String table, String condition);

	/**
	 * Drops the db.
	 *
	 * @param dbName
	 *            the db name
	 */
	public void dropDB(String dbName);

	/**
	 * Drop table. Forwards the command to the data base.
	 *
	 * @param tableName
	 *            the table name
	 */
	public void dropTable(String tableName);

	/**
	 * Execute. Executes an SQL command by forwarding it to the parser first.
	 *
	 * @param command
	 *            the command
	 * @throws Exception
	 *             the exception
	 */
	public void execute(String command) throws Exception;

	/**
	 * Gets the selected column names.
	 *
	 * @return the selected column names
	 */
	public ArrayList<String> getSelectedColumnNames();

	/**
	 * Gets the selected column types.
	 *
	 * @return the selected column types
	 */
	public ArrayList<String> getSelectedColumnTypes();

	/**
	 * Gets the selected data.
	 *
	 * @return the selected data
	 */
	public ArrayList<ArrayList<String>> getSelectedData();

	/**
	 * Gets the selected table name.
	 *
	 * @return the selected table name
	 */
	public String getSelectedTableName();

	/**
	 * Gets the update count.
	 *
	 * @return the update count
	 */
	public int getUpdateCount();

	/**
	 * Insert. Forwards the command to the data base.
	 *
	 * @param tableName
	 *            the table name
	 * @param columnsName
	 *            the columns name
	 * @param values
	 *            the values
	 * @throws Exception
	 *             the exception
	 */
	public void insert(String tableName, ArrayList<String> columnsName,
			ArrayList<String> values) throws Exception;

	/**
	 * Checks if the command is a query.
	 *
	 * @param command
	 *            the command
	 * @return true, if is query command
	 */
	boolean isQueryCommand(String command);

	/**
	 * Select. Forwards the command to the data base.
	 *
	 * @param columnNames
	 *            the column names
	 * @param tableName
	 *            the table name
	 * @param condition
	 *            the condition
	 * @param isDistinct
	 *            the is distinct
	 */
	public void select(ArrayList<String> columnNames, String tableName,
			String condition, boolean isDistinct);

	/**
	 * Sets the selected column names.
	 *
	 * @param columnNames
	 *            the new selected column names
	 */
	public void setSelectedColumnNames(ArrayList<String> columnNames);

	/**
	 * Sets the selected column types.
	 *
	 * @param types
	 *            the new selected column types
	 */
	public void setSelectedColumnTypes(ArrayList<String> types);

	/**
	 * Sets the selected data.
	 *
	 * @param selected
	 *            the new selected data
	 */
	public void setSelectedData(ArrayList<ArrayList<String>> selected);

	/**
	 * Sets the selected table name.
	 *
	 * @param tableName
	 *            the new selected table name
	 */
	public void setSelectedTableName(String tableName);

	/**
	 * Update. Forwards the command to the data base.
	 *
	 * @param columnsName
	 *            the columns name
	 * @param values
	 *            the values
	 * @param tableName
	 *            the table name
	 * @param condition
	 *            the condition
	 * @throws Exception
	 *             the exception
	 */
	public void update(ArrayList<String> columnsName, ArrayList<String> values,
			String tableName, String condition) throws Exception;

	/**
	 * Use db. Simulates the SQL use command.
	 *
	 * @param dbName
	 *            the db name
	 * @throws Exception
	 *             the exception
	 */
	public void useDB(String dbName) throws Exception;

}