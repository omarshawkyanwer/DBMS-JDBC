package eg.edu.alexu.csd.oop.jdbc.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.TransformerConfigurationException;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDB;
import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;
import eg.edu.alexu.csd.oop.jdbc.table.XMLTable;

public class XMLDB implements IDB {

	private String name;

	private Map<String, XMLTable> tables;

	private DTDCreator dTDHandler;

	private boolean loadedTableSuccess;

	private String path;

	private IDBMS dbms;

	public XMLDB(final String name, final DTDCreator handler,
			final String path, final IDBMS dbms) throws Exception {
		this.name = name;
		this.path = path;
		this.dbms = dbms;
		tables = new HashMap<String, XMLTable>();
		dTDHandler = handler;
		loadTables();
	}

	@Override
	public void alterTable(final String tableName, final String colName,
			final String dataType) {
		if (!tables.containsKey(tableName)) {
			throw new RuntimeException("This table doesn\'t exist.");
		}
		try {
			tables.get(tableName).alterTable(colName, dataType);
			createDTDFile(tableName, tables.get(tableName).getColumnNames());
			createColumnsTxt(tableName, tables.get(tableName).getColumnNames(),
					tables.get(tableName).getColumnTypes());
		} catch (Exception e) {
			throw new RuntimeException("Invalid alter command.");
		}
	}

	private void createColumnsTxt(final String tableName,
			final ArrayList<String> columns, final ArrayList<String> types)
			throws Exception {
		File table = new File(path + File.separator + name + File.separator
				+ tableName + ".txt");
		table.createNewFile();
		FileWriter writer = new FileWriter(table);

		for (int i = 0; i < columns.size(); ++i) {
			writer.write(columns.get(i));
			writer.write("\n");
			writer.write(types.get(i));
			writer.write("\n");
		}
		writer.close();
	}

	private void createDTDFile(final String tableName,
			final ArrayList<String> columns) throws IOException {
		File dtdFile = new File(path + File.separator + name + File.separator
				+ tableName + ".dtd");
		dtdFile.createNewFile();
		FileWriter writer = new FileWriter(dtdFile);
		writer.write(dTDHandler.createDTD(tableName, columns));
		writer.close();
	}

	@Override
	public void createTable(final String tableName,
			final ArrayList<String> columns, final ArrayList<String> types)
			throws TransformerConfigurationException,
			ParserConfigurationException {
		if (tables.containsKey(tableName)) {
			throw new RuntimeException("This table already exists.");
		}
		tables.put(tableName, new XMLTable(tableName, columns, types, name,
				path, dbms));
		try {
			FileOutputStream tableWriter = createXMLTable(tableName);
			XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xMLStreamWriter = xMLOutputFactory
					.createXMLStreamWriter(tableWriter, "UTF-8");

			xMLStreamWriter.writeStartDocument("UTF-8", "1.0");
			xMLStreamWriter.writeCharacters("\n");
			xMLStreamWriter.writeDTD("<!DOCTYPE " + tableName + " SYSTEM \""
					+ tableName + ".dtd\">\n");
			xMLStreamWriter.writeStartElement(tableName);
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndDocument();

			xMLStreamWriter.flush();
			xMLStreamWriter.close();
			tableWriter.close();

			createDTDFile(tableName, columns);
			createColumnsTxt(tableName, columns, types);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create table.");
		}
	}

	private FileOutputStream createXMLTable(final String tableName)
			throws Exception {
		File table = new File(path + File.separator + name + File.separator
				+ tableName + ".xml");
		if (table.exists()) {
			throw new Exception();
		}
		return new FileOutputStream(table);
	}

	@Override
	public int deleteFromTable(final String table, final String condition) {
		if (!tables.containsKey(table)) {
			throw new RuntimeException("This table doesn\'t exist.");
		}
		return tables.get(table).deleteFromTable(condition);
	}

	@Override
	public boolean didLoadTablesSucceed() {
		return loadedTableSuccess;
	}

	@Override
	public void dropTable(final String tableName) {
		if (!tables.containsKey(tableName)) {
			throw new RuntimeException("This table doesn\'t exist.");
		}
		tables.remove(tableName);
		try {
			File table = new File(path + File.separator + name + File.separator
					+ tableName + ".xml");
			File columns = new File(path + File.separator + name
					+ File.separator + tableName + ".txt");

			table.delete();
			columns.delete();

		} catch (Exception e) {
			throw new RuntimeException("Failed to drop table.");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int insert(final String tableName,
			final ArrayList<String> columnNames, final ArrayList<String> values)
			throws Exception {
		if (!tables.containsKey(tableName)) {
			throw new RuntimeException("This table doesn\'t exist.");
		}

		return tables.get(tableName).insert(columnNames, values);
	}

	private void loadTables() throws Exception {
		File db = new File(path + File.separator + name);
		File[] directoryListing = db.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				if (child.getName().endsWith(".xml")) {
					String tableName = child.getName().replaceAll(".xml", "");
					ArrayList<ArrayList<String>> columnsData = readColumnsData(tableName);
					tables.put(tableName,
							new XMLTable(tableName, columnsData.get(0),
									columnsData.get(1), name, path, dbms));
				}
			}
		}
		loadedTableSuccess = true;
	}

	private ArrayList<ArrayList<String>> readColumnsData(final String tableName)
			throws Exception {
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();

		String columnsFileName = tableName + ".txt";
		File columnsFile = new File(path + File.separator + name
				+ File.separator + columnsFileName);
		FileReader fReader = new FileReader(columnsFile);
		BufferedReader reader = new BufferedReader(fReader);

		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<String> types = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			columns.add(line);
			line = reader.readLine();
			types.add(line);
		}
		reader.close();
		fReader.close();

		ret.add(columns);
		ret.add(types);
		return ret;
	}

	@Override
	public void select(final ArrayList<String> columnNames,
			final String tableName, final String condition,
			final boolean isDistinct) {
		if (!tables.containsKey(tableName)) {
			throw new RuntimeException("This table doesn\'t exist.");
		}
		tables.get(tableName).select(columnNames, condition, isDistinct);
	}

	@Override
	public int update(final ArrayList<String> coloumnsName,
			final ArrayList<String> values, final String tableName,
			final String condition) throws Exception {
		if (!tables.containsKey(tableName)) {
			throw new RuntimeException("This table doesn\'t exist.");
		}
		return tables.get(tableName).update(coloumnsName, values, condition);
	}

}
