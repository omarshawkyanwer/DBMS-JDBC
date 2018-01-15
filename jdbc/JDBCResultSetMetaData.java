package eg.edu.alexu.csd.oop.jdbc.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCResultSetMetaData implements java.sql.ResultSetMetaData {

	private String tableName;
	private ArrayList<String> columnNames;
	private ArrayList<String> types;
	private int columnCount;

	public JDBCResultSetMetaData(final String tableName,
			final ArrayList<String> columnNames, final ArrayList<String> types) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.types = types;
		columnCount = columnNames.size();
	}

	@Override
	public String getCatalogName(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnClassName(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnCount() throws SQLException {
		return columnCount;
	}

	@Override
	public int getColumnDisplaySize(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnLabel(final int arg0) throws SQLException {
		return columnNames.get(arg0 - 1);
	}

	@Override
	public String getColumnName(final int arg0) throws SQLException {
		return columnNames.get(arg0 - 1);
	}

	@Override
	public int getColumnType(final int arg0) throws SQLException {
		String columnType = types.get(arg0 - 1);
		if (columnType.equals("date")) {
			return java.sql.Types.DATE;
		} else if (columnType.equals("int")) {
			return java.sql.Types.INTEGER;
		} else if (columnType.equals("varchar")) {
			return java.sql.Types.VARCHAR;
		} else {
			return java.sql.Types.FLOAT;
		}
	}

	@Override
	public String getColumnTypeName(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPrecision(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScale(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSchemaName(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTableName(final int arg0) throws SQLException {
		return tableName;
	}

	@Override
	public boolean isAutoIncrement(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCaseSensitive(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCurrency(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefinitelyWritable(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int isNullable(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSearchable(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSigned(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
