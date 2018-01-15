package eg.edu.alexu.csd.oop.jdbc.jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCDriver implements Driver {

    private ArrayList<String> users;
    private ArrayList<String> passwords;

    private final Logger log = LogManager.getLogger(JDBCDriver.class);

    public JDBCDriver() {
        this.users = new ArrayList<String>();
        this.passwords = new ArrayList<String>();
    }

    public JDBCDriver(final String userPath, final String passwordPath) {
        this.users = new ArrayList<String>();
        this.passwords = new ArrayList<String>();
        setUserInfo(userPath, passwordPath);
    }

    @Override
    public boolean acceptsURL(final String url) throws SQLException {
        String xml, alternative;
        xml = "jdbc:xmldb://localhost";
        alternative = "jdbc:altdb://localhost";
        boolean accepted = (url.equals(xml) || url.equals(alternative));
        if (accepted) {
            log.info("URL " + url + " was accepted.");
        } else {
            log.warn("URL " + url + " was rejected.");
        }
        return accepted;
    }

    @Override
    public Connection connect(final String url, final Properties info)
            throws SQLException {
        Connection connection = null;
        if (!acceptsURL(url)) {
            return connection;
        }
        File path = (File) info.get("path");
        String[] arr = url.split(":");
        try {
            connection = new JDBCConnection(path.getAbsolutePath(), arr[1]);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SQLException(e.getMessage());
        }
        log.info("Connection is created to " + arr[1] + ".");
        return connection;
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public java.util.logging.Logger getParentLogger()
            throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url,
            final Properties info)
            throws SQLException {
        int size = users.size();
        DriverPropertyInfo[] ret = new DriverPropertyInfo[size];
        for (int i = 0; i < size; i++) {
            ret[i] = new DriverPropertyInfo(users.get(i), passwords.get(i));
        }
        return ret;
    }

    @Override
    public boolean jdbcCompliant() {
        throw new UnsupportedOperationException();
    }

    private void setUserInfo(final String userPath, final String passwordPath) {
        File userNames = new File(userPath);
        File password = new File(passwordPath);
        try {
            Scanner s1 = new Scanner(userNames);
            Scanner s2 = new Scanner(password);
            while (s1.hasNextLine()) {
                users.add(s1.nextLine());
                passwords.add(s2.nextLine());
            }
            s1.close();
            s2.close();
        } catch (FileNotFoundException e) {
        }
    }

}