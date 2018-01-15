package eg.edu.alexu.csd.oop.jdbc.main;

import java.util.ArrayList;

public class Formatter {

	private ArrayList<ArrayList<String>> data;
	private ArrayList<String> names;
	private int[] arr;
	private int len;

	public void print(ArrayList<ArrayList<String>> data, ArrayList<String> colNames) {
		this.data = new ArrayList<ArrayList<String>>(data);
		this.names = new ArrayList<String>(colNames);
		String[][] obj = ConvertToObj();
		for (int i = 0; i < obj.length; i++) {
			printTable(len);
			printObj(obj[i]);
			if (i == 0)
				printTable(len);
		}
		printTable(len);
	}

	private String handleString(String s, int len) {
		while (s.length() != len)
			s += " ";
		s = " " + s;
		return s;
	}

	private void printTable(int len) {
		for (int i = 0; i < len; i++)
			System.out.print("~");
		System.out.println();
	}

	private void printObj(String[] obj) {
		System.out.print("|");
		for (int i = 0; i < obj.length; i++) {
			System.out.print("|" + handleString(obj[i], arr[i]));
		}
		System.out.println("||");
	}

	private String[][] ConvertToObj() {
		int colNum = data.size();
		int rowNum = data.get(0).size();
		arr = new int[colNum];
		len = 3;
		String[][] ret = new String[rowNum + 1][colNum];
		for (int i = 0; i < colNum; i++) {
			arr[i] = names.get(i).length() + 3;
			ret[0][i] = names.get(i);
		}
		for (int i = 0; i < colNum; i++) {
			int max = arr[i];
			for (int j = 0; j < rowNum; j++) {
				String str = data.get(i).get(j);
				max = Math.max(max, str.length());
				ret[j + 1][i] = str;
			}
			arr[i] = max + 3;
			len += (max + 1 + 4);
		}
		return ret;
	}

}