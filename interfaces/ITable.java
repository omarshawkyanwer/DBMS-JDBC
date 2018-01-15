package eg.edu.alexu.csd.oop.jdbc.interfaces;

import java.util.ArrayList;

/**
 * The Interface ITable.
 */
public interface ITable {

	/**
	 * Alter table. Simulates the SQL alter table command.
	 *
	 * @param colName
	 *            the column name
	 * @param dataType
	 *            the data type
	 * @throws Exception
	 *             the exception
	 */
	public void alterTable(String colName, String dataType) throws Exception;

	/**
	 * Delete from table. Simulates the SQL delete from table command.
	 *
	 * @param condition
	 *            the condition
	 * @return the update count
	 */
	public int deleteFromTable(String condition);

	/**
	 * Insert. Simulates the SQL insert into table command.
	 *
	 * @param columnNames
	 *            the column names
	 * @param values
	 *            the values
	 * @return the update count
	 * @throws Exception
	 *             the exception
	 */
	public int insert(ArrayList<String> columnNames, ArrayList<String> values)
			throws Exception;

	/**
	 * Select. Simulates the SQL select command.
	 *
	 * @param columnNames
	 *            the column names
	 * @param condition
	 *            the condition
	 * @param isDistinct
	 *            the is distinct boolean
	 */
	public void select(ArrayList<String> columnNames, String condition,
			boolean isDistinct);

	/**
	 * Send selected data to dbms.
	 *
	 * @param columnNames
	 *            the column names
	 * @param types
	 *            the types
	 * @param data
	 *            the data
	 */
	public void sendSelectedDataToDBMS(ArrayList<String> columnNames,
			ArrayList<String> types, ArrayList<ArrayList<String>> data);

	/**
	 * Update. Simulates the SQL update command.
	 *
	 * @param coloumnsName
	 *            the columns name
	 * @param values
	 *            the values
	 * @param condition
	 *            the condition
	 * @return the update count
	 * @throws Exception
	 *             the exception
	 */
	public int update(ArrayList<String> columnsName, ArrayList<String> values,
			String condition) throws Exception;

}
