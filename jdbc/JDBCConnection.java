package eg.edu.alexu.csd.oop.jdbc.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCConnection implements Connection {

	private boolean opened;
	private Statement stmt;

	private final Logger log = LogManager.getLogger(JDBCStatement.class);

	public JDBCConnection(final String path, final String fileType) throws Exception {
		this.opened = true;
		stmt = new JDBCStatement(path, fileType, this);
	}

	@Override
	public void abort(final Executor executor) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void close() throws SQLException {
		log.info("Connection is closed.");
		opened = false;
	}

	@Override
	public void commit() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Array createArrayOf(final String typeName, final Object[] elements)
			throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Blob createBlob() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Clob createClob() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NClob createNClob() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Statement createStatement() throws SQLException {
		if (!opened) {
			log.error("Invalid use of a closed connection.");
			throw new SQLException("This connection is closed.");
		}
		log.info("Statement created.");
		((JDBCStatement) stmt).setOpened();
		return stmt;
	}

	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Statement createStatement(final int resultSetType,
			final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Struct createStruct(final String typeName, final Object[] attributes)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getCatalog() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Properties getClientInfo() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getClientInfo(final String name) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getSchema() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isClosed() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isReadOnly() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isValid(final int timeout) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String nativeSQL(final String sql) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType,
			final int resultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType,
			final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int autoGeneratedKeys)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType,
			final int resultSetConcurrency) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType,
			final int resultSetConcurrency, final int resultSetHoldability)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void rollback() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void rollback(final Savepoint savepoint) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setCatalog(final String catalog) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setClientInfo(final Properties properties)
			throws SQLClientInfoException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setClientInfo(final String name, final String value)
			throws SQLClientInfoException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setHoldability(final int holdability) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setNetworkTimeout(final Executor executor, final int milliseconds)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Savepoint setSavepoint(final String name) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setSchema(final String schema) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();

	}

}
