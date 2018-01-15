package eg.edu.alexu.csd.oop.jdbc.main;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import eg.edu.alexu.csd.oop.jdbc.jdbc.JDBCDriver;

public class Main {

	public static void main(final String[] args) {
		Main JDBC = new Main();
		if (JDBC.intialize()) {
			JDBC.run();
		}
	}

	private Driver driver;
	private Scanner s;
	private Connection connection;
	private Statement statement;
	private String URL;
	private Formatter formatter;

	private ArrayList<String> getColumns(final ResultSetMetaData data) {
		int size;
		try {
			size = data.getColumnCount();
		} catch (SQLException e1) {
			return null;
		}
		ArrayList<String> columns = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			try {
				columns.add(data.getColumnName(i + 1));
			} catch (SQLException e) {
				return null;
			}
		}
		return columns;
	}

	private ArrayList<ArrayList<String>> getData(final ResultSet result) {
		ArrayList<ArrayList<String>> data;
		try {
			int size = result.getMetaData().getColumnCount();
			data = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < size; i++) {
				data.add(new ArrayList<String>());
			}
			while (result.next()) {
				for (int i = 0; i < size; i++) {
					Object value = result.getObject(i + 1);
					String nxt;
					if (value == null) {
						nxt = "null";
					} else {
						nxt = value.toString();
					}
					ArrayList<String> tmp = data.get(i);
					tmp.add(nxt);
				}
			}
			return data;
		} catch (SQLException e) {
			return null;
		}
	}

	private Properties getProperty() {
		Properties info = new Properties();
		String tmpDir = System.getProperty("java.io.tmpdir");
		File dbDir = new File(tmpDir);
		info.put("path", dbDir.getAbsoluteFile());
		return info;
	}

	private boolean getURL() {
		System.out.println("Enter a URL:");
		URL = s.nextLine();
		try {
			boolean accept = driver.acceptsURL(URL);
			return accept;
		} catch (SQLException e) {
			return false;
		}
	}

	private boolean getUser() {
		String user, password;
		System.out.println("Enter username:");
		user = s.nextLine();
		System.out.println("Enter password:");
		password = s.nextLine();
		DriverPropertyInfo[] info;
		try {
			info = driver.getPropertyInfo(null, null);
			for (int i = 0; i < info.length; ++i) {
				if (user.equals(info[i].name) && password.equals(info[i].value)) {
					System.out.println("Welcome " + user + ".");
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	public boolean intialize() {
		s = new Scanner(System.in);
		formatter = new Formatter();
		String path;
		try {
			path = Main.class.getProtectionDomain().getCodeSource()
					.getLocation().toURI().getPath();
			driver = new JDBCDriver(path + "users.txt", path + "passwords.txt");
		} catch (URISyntaxException e) {
			System.err.println("Path not found.");
			return false;
		}
		while (!getUser()) {
			System.err.println("Invalid username or password.");
		}
		while (!getURL()) {
			System.err.println("Invalid URL.");
		}
		return true;
	}

	public void run() {
		try {
			connection = driver.connect(URL, getProperty());
			System.out.println("Enter a command or \'exit\' to exit.");
			while (true) {
				String command = s.nextLine().trim();
				if (command.equalsIgnoreCase("exit")) {
					break;
				}
				try {
					ResultSet data;
					ResultSetMetaData result;
					statement = connection.createStatement();
					if (statement.execute(command)) {
						data = statement.getResultSet();
						result = data.getMetaData();
						ArrayList<String> columnNames = getColumns(result);
						ArrayList<ArrayList<String>> rows = getData(data);
						formatter.print(rows, columnNames);
					}
					statement.close();
					System.out.println("Command was excuted successfully.");
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.close();
	}
}