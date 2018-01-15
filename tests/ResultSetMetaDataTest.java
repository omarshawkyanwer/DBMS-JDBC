package eg.edu.alexu.csd.oop.jdbc.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.jdbc.jdbc.JDBCDriver;

public class ResultSetMetaDataTest {

	private Driver driver = new JDBCDriver();
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMetaData;

	@Test
	public void test() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		File dbDir = new File(tmpDir);
		Properties info = new Properties();
		info.put("path", dbDir.getAbsoluteFile());

		try {
			connection = driver.connect("jdbc:xmldb://localhost", info);
			statement = connection.createStatement();

			try {
				statement.execute("drop database db1");
			} catch (Exception e) {
			}

			statement.execute("create database db1 ");
			statement.execute("use db1 ");
			statement
					.execute("create table tb1 (name varchar , age int ,persent float ,birthDay date)");
			statement
					.execute("insert into tb1 values (\"shwshw\",20,.01,\"1996-9-9\")");
			resultSet = statement.executeQuery("select * from tb1");

			resultSetMetaData = resultSet.getMetaData();
			validTests();

		} catch (SQLException e) {
			fail("Error in connect function.");
		}

	}

	public void validTests() {

		try {
			org.junit.Assert.assertEquals("Error in getColumnCount.", 4,
					resultSetMetaData.getColumnCount());

			org.junit.Assert.assertEquals("Error in getColumnName.", "name",
					resultSetMetaData.getColumnName(1));
			org.junit.Assert.assertEquals("Error in getColumnName.", "age",
					resultSetMetaData.getColumnName(2));
			org.junit.Assert.assertEquals("Error in getColumnName.", "persent",
					resultSetMetaData.getColumnName(3));
			org.junit.Assert.assertEquals("Error in getColumnName.",
					"birthday", resultSetMetaData.getColumnName(4));

			org.junit.Assert.assertEquals("Error in getColumnType.",
					java.sql.Types.VARCHAR, resultSetMetaData.getColumnType(1));
			org.junit.Assert.assertEquals("Error in getColumnType.",
					java.sql.Types.INTEGER, resultSetMetaData.getColumnType(2));
			org.junit.Assert.assertEquals("Error in getColumnType.",
					java.sql.Types.FLOAT, resultSetMetaData.getColumnType(3));
			org.junit.Assert.assertEquals("Error in getColumnType.",
					java.sql.Types.DATE, resultSetMetaData.getColumnType(4));

			org.junit.Assert.assertEquals("Error in getTableName.", "tb1",
					resultSetMetaData.getTableName(0));

		} catch (SQLException e) {
		}
	}

}
