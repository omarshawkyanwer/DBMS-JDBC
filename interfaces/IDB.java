package eg.edu.alexu.csd.oop.jdbc.interfaces;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

/**
 * The Interface IDB.
 */
public interface IDB {

	/**
	 * Alter table. Forwards the command to the table.
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
	 * Creates the table. Simulates the SQL create table command.
	 *
	 * @param tableName
	 *            the table name
	 * @param cloumns
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
	 * Delete from table. Forwards the command to the table.
	 *
	 * @param table
	 *            the table
	 * @param condition
	 *            the condition
	 * @return the update count
	 */
	public int deleteFromTable(String table, String condition);

	/**
	 * Did load tables succeed.
	 *
	 * @return true, if successful
	 */
	public boolean didLoadTablesSucceed();

	/**
	 * Drop table. Simulates the SQL drop table command.
	 *
	 * @param tableName
	 *            the table name
	 */
	public void dropTable(String tableName);

	/**
	 * Gets the name of the database.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Insert. Forwards the command to the table.
	 *
	 * @param tableName
	 *            the table name
	 * @param columnsName
	 *            the columns name
	 * @param values
	 *            the values
	 * @return the update count
	 * @throws Exception
	 *             the exception
	 */
	public int insert(String tableName, ArrayList<String> columnsName,
			ArrayList<String> values) throws Exception;

	/**
	 * Select. Forwards the command to the table.
	 *
	 * @param columnNames
	 *            the column names
	 * @param tableName
	 *            the table name
	 * @param condition
	 *            the condition
	 * @param isDistinct
	 *            the boolean that determines if the select command is a select
	 *            distinct command
	 */
	public void select(ArrayList<String> columnNames, String tableName,
			String condition, boolean isDistinct);

	/**
	 * Update. Forwards the command to the table.
	 *
	 * @param coloumnsName
	 *            the column names
	 * @param values
	 *            the values
	 * @param tableName
	 *            the table name
	 * @param condition
	 *            the condition
	 * @return the update count
	 * @throws Exception
	 *             the exception
	 */
	public int update(ArrayList<String> columnNames, ArrayList<String> values,
			String tableName, String condition) throws Exception;

}
