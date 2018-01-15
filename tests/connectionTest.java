package eg.edu.alexu.csd.oop.jdbc.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.jdbc.jdbc.JDBCDriver;

public class connectionTest {

	@Test
	public void test() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		File dbDir = new File(tmpDir);
		Properties info = new Properties();
		info.put("path", dbDir.getAbsoluteFile());
		try {
			Driver driver = new JDBCDriver();
			Connection connection = driver.connect("jdbc:xmldb://localhost",
					info);
			connection.close();
			try {
				connection.createStatement();
				fail("Statement is created in closed connection .");
			} catch (SQLException e) {
			}

		} catch (SQLException e) {
			fail("Error in connect function.");
		}

	}

}
