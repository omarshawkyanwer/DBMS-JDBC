package eg.edu.alexu.csd.oop.jdbc.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import eg.edu.alexu.csd.oop.jdbc.jdbc.JDBCDriver;

public class DriverTest {

	private Driver driver = new JDBCDriver();

	@Test
	public void acceptsUrlTest() {
		String url1 = "jdbc:xmldb://localhost", url2 = "jdbc:altdb://localhost", url3 = "jdbc:xmldb:\\D";
		try {
			if (!driver.acceptsURL(url1) || !driver.acceptsURL(url2)) {
				fail("Right urls refused.");
			}

			if (driver.acceptsURL(url3)) {
				fail("Wrong urls accepted.");
			}
		} catch (SQLException e) {
			fail("Error in accpets url function.");
		}
	}

	@Test
	public void connectTest() {
		String tmpDir = System.getProperty("java.io.tmpdir");
		File dbDir = new File(tmpDir);
		Properties info = new Properties();
		info.put("path", dbDir.getAbsoluteFile());

		String url1 = "jdbc:xmldb://localhost", url2 = "jdbc:altdb://localhost", url3 = "jdbc:xmldb:\\D";
		try {
			if (driver.connect(url3, info) != null) {
				fail("Accepts invalid url in connect function");
			}

			if (driver.connect(url1, info) == null
					|| driver.connect(url2, info) == null) {
				fail("Refuses valid url in connect function");
			}

		} catch (SQLException e) {
			fail("Error in connect function.");
		}

	}

}
