package eg.edu.alexu.csd.oop.jdbc.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.jdbc.jdbc.JDBCDriver;

public class ResultSetTest {

	private Driver driver = new JDBCDriver();
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private void invalidTests() {

		try {
			try {
				resultSet.getString(7);
				fail("Invalid row.");
			} catch (SQLException e) {
			}

			resultSet.close();

			try {
				resultSet.getStatement();
				fail("It should not make any functionality after closing.");
			} catch (SQLException e) {
			}

		} catch (SQLException e) {
			fail("Unexcpected error.");
		}

	}

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
					.execute("insert into tb1(name , age  , persent , birthDay ) values (\"null\" , 0 , 0 ,\"2999-9-9\")");
			statement
					.execute("insert into tb1 values (\"shwshw\",20,.01,\"1996-9-9\")");
			resultSet = statement.executeQuery("select * from tb1");

			validTests();
			invalidTests();

		} catch (SQLException e) {
			fail("Error in connect function.");
		}
	}

	private void validTests() {
		int rowCount = 0;
		try {

			while (resultSet.next()) {
				++rowCount;
			}
			org.junit.Assert.assertEquals("Wrong number of rows.", 2, rowCount);
			resultSet.last();

			org.junit.Assert.assertEquals("Wrong in get string by index.",
					"shwshw", resultSet.getString(1));
			org.junit.Assert.assertEquals(
					"Wrong in get string by column name.", "shwshw",
					resultSet.getString("name"));

			org.junit.Assert.assertEquals("Wrong in get int by index.", 20,
					resultSet.getInt(2));
			org.junit.Assert.assertEquals("Wrong in get int by column name.",
					20, resultSet.getInt("age"));

			org.junit.Assert.assertEquals("Wrong in get float by index.",
					Float.valueOf(".01"), Float.valueOf(resultSet.getFloat(3)));
			org.junit.Assert.assertEquals("Wrong in get float by column name.",
					Float.valueOf(".01"),
					Float.valueOf(resultSet.getFloat("persent")));

			org.junit.Assert.assertEquals("Wrong in get date by index.",
					java.sql.Date.valueOf("1996-9-9"), resultSet.getDate(4));
			org.junit.Assert.assertEquals("Wrong in get date by column name.",
					java.sql.Date.valueOf("1996-9-9"),
					resultSet.getDate("birthDay"));

			resultSet.first();
			org.junit.Assert.assertEquals("Wrong in get first.", null,
					resultSet.getString(1));
			org.junit.Assert.assertEquals("Wrong in first or in isFirst.",
					true, resultSet.isFirst());

			resultSet.absolute(2);
			org.junit.Assert.assertEquals("Wrong in absolute.", "shwshw",
					resultSet.getString(1));
			org.junit.Assert.assertEquals("Wrong in absolute or in isLast.",
					true, resultSet.isLast());

			org.junit.Assert.assertEquals("wrong in find colunmn.", 1,
					resultSet.findColumn("name"));

			org.junit.Assert.assertEquals("Wrong in isClosed.", false,
					resultSet.isClosed());
			org.junit.Assert.assertEquals("Wrong in isAfterLast.", false,
					resultSet.isAfterLast());
			org.junit.Assert.assertEquals("Wrong in isBeforeFirst.", false,
					resultSet.isBeforeFirst());

			Object o = resultSet.getObject(4);
			try {
				java.sql.Date.valueOf(o.toString());
			} catch (Exception e) {
				fail("failed in getting date by get object .");
			}

			org.junit.Assert.assertEquals("Wrong in getStatementt.", statement,
					resultSet.getStatement());

		} catch (SQLException e) {
			fail("Unexcpected error.");
		}

	}

}
