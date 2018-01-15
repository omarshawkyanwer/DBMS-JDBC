package eg.edu.alexu.csd.oop.jdbc.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.jdbc.jdbc.JDBCDriver;

public class StatementTest {

	Driver driver = new JDBCDriver();
	Connection connection;
	Statement statement;

	public void inValidTests() {
		try {
			statement.close();
			statement.getConnection();
			fail("Connection should be unavialble after closing the statement.");
		} catch (SQLException e) {
		}

		try {
			statement.clearBatch();
			fail("Functionality should be unavialble after closing the statement.");
		} catch (SQLException e) {
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
			validTests();
			inValidTests();

		} catch (SQLException e) {
			fail("Error in connect function.");
		}

	}

	public void validTests() {
		try {

			try {
				statement.execute("drop database db1");
			} catch (Exception e) {
			}

			if (statement.execute("create database db1")) {
				fail("Invalid command type in execute .");
			}

			if (statement.execute("use db1")) {
				fail("Invalid command type in execute .");
			}

			if (statement
					.execute("create table tb1 (name varchar , age float)")) {
				fail("Invalid command type in execute .");
			}

			if (statement.execute("insert into tb1 values (\"hamada\",14)")) {
				fail("Invalid command type in execute .");
			}

			if (!statement.execute("select * from tb1")) {
				fail("Invalid command type in execute .");
			}

			if (statement.execute("delete from tb1 where name = \"hamada\"")) {
				fail("Invalid command type in execute .");
			}

		} catch (SQLException e) {
			fail("failed to execute vaild commands with message "
					+ e.getMessage());
		}

		try {

			statement.addBatch("insert into tb1 values (\"sasa\",20)");
			statement.addBatch("insert into tb1 values (\"shwshw\",20)");
			statement.addBatch("select * from tb1");
			statement.addBatch("select distinct age from tb1");
			statement.addBatch("update tb1 set age = 19.5 ");

			int[] result = statement.executeBatch();

			if (result[0] != 1 || result[1] != 1) {
				fail("Error in execute batch.");
			}
			if (result[2] != -1 || result[3] != -1) {
				fail("Error in execute batch.");
			}
			if (result[4] != 2) {
				fail("Error in execute batch.");
			}

			if (statement.getResultSet() == null) {
				fail("Result set is'nt updated.");
			}

		} catch (SQLException e) {
			fail(e.getMessage());
		}

	}

}
