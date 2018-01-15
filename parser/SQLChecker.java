package eg.edu.alexu.csd.oop.jdbc.parser;

import java.util.HashMap;

import eg.edu.alexu.csd.oop.jdbc.interfaces.IDBMS;

public class SQLChecker {

	private HashMap<String, SQLKeywords> query;
	private HashMap<String, SQLKeywords> update;

	public SQLChecker() {
		query = new HashMap<String, SQLKeywords>();
		update = new HashMap<String, SQLKeywords>();
		query.put("select", new Select());
		update.put("use", new Use());
		update.put("update", new Update());
		update.put("delete", new Delete());
		update.put("insert", new Insert());
		update.put("create", new Create());
		update.put("drop", new Drop());
		update.put("alter", new Alter());
	}

	public void excute(final String[] inputArr, final IDBMS dbms) throws Exception {
		String keyWord = inputArr[0].toLowerCase();
		if (update.containsKey(keyWord)) {
			update.get(keyWord).excute(dbms, inputArr);
		} else if (query.containsKey(keyWord)) {
			query.get(keyWord).excute(dbms, inputArr);
		} else {
			throw new RuntimeException("Syntax error.");
		}
	}

	public boolean isQueryCommand(String s) {
		s = s.trim().split(" ")[0];
		return query.containsKey(s.toLowerCase());
	}

}