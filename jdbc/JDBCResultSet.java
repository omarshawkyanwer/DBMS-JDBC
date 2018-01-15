package eg.edu.alexu.csd.oop.jdbc.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCResultSet implements java.sql.ResultSet {

	private ArrayList<ArrayList<String>> selected;
	private Statement stmt;
	private int cursor;
	private int rowCount;
	private int columnCount;
	private boolean closed;
	private ResultSetMetaData metaData;
	private final Logger log = LogManager.getLogger(JDBCResultSet.class);

	public JDBCResultSet(final String tableName, final ArrayList<String> columnNames,
			final ArrayList<String> types, final ArrayList<ArrayList<String>> selected,
			final Statement stmt) {
		this.selected = selected;
		this.stmt = stmt;
		columnCount = selected.size();
		rowCount = selected.get(0).size();
		metaData = new JDBCResultSetMetaData(tableName, columnNames, types);
	}

	@Override
	public boolean absolute(final int arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		cursor = arg0 < 0 ? arg0 + rowCount + 1 : arg0;
		if (isBeforeFirst() || isAfterLast()) {
			return false;
		}
		return true;
	}

	@Override
	public void afterLast() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		cursor = rowCount + 1;
	}

	@Override
	public void beforeFirst() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		cursor = 0;
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void close() throws SQLException {
		log.info("Result set is closed.");
		closed = true;
		stmt = null;
		selected = null;
		metaData = null;
	}

	@Override
	public void deleteRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int findColumn(final String arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		for (int i = 0; i < columnCount; ++i) {
			if (metaData.getColumnName(i + 1).equalsIgnoreCase(arg0)) {
				return i + 1;
			}
		}
		throw new SQLException("Column not found.");
	}

	@Override
	public boolean first() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		cursor = 1;
		return rowCount > 0;
	}

	@Override
	public Array getArray(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Array getArray(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public InputStream getAsciiStream(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public InputStream getAsciiStream(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public BigDecimal getBigDecimal(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public BigDecimal getBigDecimal(final int arg0, final int arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public BigDecimal getBigDecimal(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public BigDecimal getBigDecimal(final String arg0, final int arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public InputStream getBinaryStream(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public InputStream getBinaryStream(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Blob getBlob(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Blob getBlob(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean getBoolean(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getBoolean(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public byte getByte(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public byte getByte(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public byte[] getBytes(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public byte[] getBytes(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getCharacterStream(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getCharacterStream(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Clob getClob(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Clob getClob(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getConcurrency() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getCursorName() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getDate(final int arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		if (arg0 < 1 || arg0 > columnCount) {
			throw new SQLException("Index out of bound.");
		}
		String value = selected.get(arg0 - 1).get(cursor - 1);
		return value.equals("null") ? null : Date.valueOf(value);
	}

	@Override
	public Date getDate(final int arg0, final Calendar arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getDate(final String arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		for (int i = 0; i < columnCount; ++i) {
			if (metaData.getColumnName(i + 1).equalsIgnoreCase(arg0)) {
				String value = selected.get(i).get(cursor - 1);
				return value.equals("null") ? null : Date.valueOf(value);
			}
		}
		throw new SQLException("Column not found.");
	}

	@Override
	public Date getDate(final String arg0, final Calendar arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public double getDouble(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public double getDouble(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public float getFloat(final int arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		if (arg0 < 1 || arg0 > columnCount) {
			throw new SQLException("Index out of bound.");
		}
		String value = selected.get(arg0 - 1).get(cursor - 1);
		return value.equals("null") ? null : Float.parseFloat(value);
	}

	@Override
	public float getFloat(final String arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		for (int i = 0; i < columnCount; ++i) {
			if (metaData.getColumnName(i + 1).equalsIgnoreCase(arg0)) {
				String value = selected.get(i).get(cursor - 1);
				return value.equals("null") ? null : Float.parseFloat(value);
			}
		}
		throw new SQLException("Column not found.");
	}

	@Override
	public int getHoldability() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getInt(final int arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		if (arg0 < 1 || arg0 > columnCount) {
			throw new SQLException("Index out of bound.");
		}
		String value = selected.get(arg0 - 1).get(cursor - 1);
		return value.equals("null") ? 0 : Integer.parseInt(value);
	}

	@Override
	public int getInt(final String arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		for (int i = 0; i < columnCount; ++i) {
			if (metaData.getColumnName(i + 1).equalsIgnoreCase(arg0)) {
				String value = selected.get(i).get(cursor - 1);
				return value.equals("null") ? 0 : Integer.parseInt(value);
			}
		}
		throw new SQLException("Column not found.");
	}

	@Override
	public long getLong(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public long getLong(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return metaData;
	}

	@Override
	public Reader getNCharacterStream(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Reader getNCharacterStream(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NClob getNClob(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public NClob getNClob(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getNString(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getNString(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getObject(final int arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		if (arg0 < 1 || arg0 > columnCount) {
			throw new SQLException("Index out of bound.");
		}

		String value = selected.get(arg0 - 1).get(cursor - 1);
		if (value.equals("null")) {
			return null;
		}
		int type = metaData.getColumnType(arg0);

		if (type == java.sql.Types.DATE) {
			return java.sql.Date.valueOf(value);
		} else if (type == java.sql.Types.INTEGER) {
			return Integer.parseInt(value);
		} else if (type == java.sql.Types.FLOAT) {
			return Float.parseFloat(value);
		} else {
			return value;
		}
	}

	@Override
	public <T> T getObject(final int arg0, final Class<T> arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getObject(final int arg0, final Map<String, Class<?>> arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getObject(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T getObject(final String arg0, final Class<T> arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getObject(final String arg0, final Map<String, Class<?>> arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Ref getRef(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Ref getRef(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public RowId getRowId(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public RowId getRowId(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public short getShort(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public short getShort(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public SQLXML getSQLXML(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public SQLXML getSQLXML(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Statement getStatement() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return stmt;
	}

	@Override
	public String getString(final int arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		if (arg0 < 1 || arg0 > columnCount) {
			throw new SQLException("Index out of bound.");
		}
		String value = selected.get(arg0 - 1).get(cursor - 1);
		return value.equals("null") ? null : value;
	}

	@Override
	public String getString(final String arg0) throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}

		for (int i = 0; i < columnCount; ++i) {
			if (metaData.getColumnName(i + 1).equalsIgnoreCase(arg0)) {
				String value = selected.get(i).get(cursor - 1);
				return value.equals("null") ? null : value;
			}
		}
		throw new SQLException("Column not found.");
	}

	@Override
	public Time getTime(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Time getTime(final int arg0, final Calendar arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Time getTime(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Time getTime(final String arg0, final Calendar arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(final int arg0, final Calendar arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public Timestamp getTimestamp(final String arg0, final Calendar arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getUnicodeStream(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getUnicodeStream(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getURL(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getURL(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return cursor == rowCount + 1;
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return cursor == 0;
	}

	@Override
	public boolean isClosed() throws SQLException {
		if (closed) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return closed;
	}

	@Override
	public boolean isFirst() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return cursor == 1;
	}

	@Override
	public boolean isLast() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		return cursor == rowCount;
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean last() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		if (rowCount == 0) {
			return false;
		}
		cursor = rowCount;
		return true;
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean next() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		++cursor;
		return cursor <= rowCount;
	}

	@Override
	public boolean previous() throws SQLException {
		if (isClosed()) {
			log.error("Invalid use of a closed result set.");
			throw new SQLException("This result set is closed.");
		}
		--cursor;
		return cursor > 0;
	}

	@Override
	public void refreshRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean relative(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rowInserted() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setFetchSize(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateArray(final int arg0, final Array arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateArray(final String arg0, final Array arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAsciiStream(final int arg0, final InputStream arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAsciiStream(final int arg0, final InputStream arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAsciiStream(final int arg0, final InputStream arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAsciiStream(final String arg0, final InputStream arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAsciiStream(final String arg0, final InputStream arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateAsciiStream(final String arg0, final InputStream arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBigDecimal(final int arg0, final BigDecimal arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBigDecimal(final String arg0, final BigDecimal arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBinaryStream(final int arg0, final InputStream arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBinaryStream(final int arg0, final InputStream arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBinaryStream(final int arg0, final InputStream arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBinaryStream(final String arg0, final InputStream arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBinaryStream(final String arg0, final InputStream arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBinaryStream(final String arg0, final InputStream arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBlob(final int arg0, final Blob arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBlob(final int arg0, final InputStream arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBlob(final int arg0, final InputStream arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBlob(final String arg0, final Blob arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBlob(final String arg0, final InputStream arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBlob(final String arg0, final InputStream arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBoolean(final int arg0, final boolean arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBoolean(final String arg0, final boolean arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateByte(final int arg0, final byte arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateByte(final String arg0, final byte arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBytes(final int arg0, final byte[] arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateBytes(final String arg0, final byte[] arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateCharacterStream(final int arg0, final Reader arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateCharacterStream(final int arg0, final Reader arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateCharacterStream(final int arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateCharacterStream(final String arg0, final Reader arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateCharacterStream(final String arg0, final Reader arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateCharacterStream(final String arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateClob(final int arg0, final Clob arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateClob(final int arg0, final Reader arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateClob(final int arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateClob(final String arg0, final Clob arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateClob(final String arg0, final Reader arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateClob(final String arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateDate(final int arg0, final Date arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateDate(final String arg0, final Date arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateDouble(final int arg0, final double arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateDouble(final String arg0, final double arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateFloat(final int arg0, final float arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateFloat(final String arg0, final float arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateInt(final int arg0, final int arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateInt(final String arg0, final int arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateLong(final int arg0, final long arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateLong(final String arg0, final long arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNCharacterStream(final int arg0, final Reader arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNCharacterStream(final int arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNCharacterStream(final String arg0, final Reader arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNCharacterStream(final String arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNClob(final int arg0, final NClob arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNClob(final int arg0, final Reader arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNClob(final int arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNClob(final String arg0, final NClob arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNClob(final String arg0, final Reader arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNClob(final String arg0, final Reader arg1, final long arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNString(final int arg0, final String arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNString(final String arg0, final String arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNull(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateNull(final String arg0) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateObject(final int arg0, final Object arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateObject(final int arg0, final Object arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateObject(final String arg0, final Object arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateObject(final String arg0, final Object arg1, final int arg2)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateRef(final int arg0, final Ref arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateRef(final String arg0, final Ref arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateRow() throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateRowId(final int arg0, final RowId arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateRowId(final String arg0, final RowId arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateShort(final int arg0, final short arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateShort(final String arg0, final short arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateSQLXML(final int arg0, final SQLXML arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateSQLXML(final String arg0, final SQLXML arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateString(final int arg0, final String arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateString(final String arg0, final String arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateTime(final int arg0, final Time arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateTime(final String arg0, final Time arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateTimestamp(final int arg0, final Timestamp arg1) throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void updateTimestamp(final String arg0, final Timestamp arg1)
			throws SQLException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean wasNull() throws SQLException {
		throw new UnsupportedOperationException();
	}

}
