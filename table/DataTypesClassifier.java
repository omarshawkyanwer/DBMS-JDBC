package eg.edu.alexu.csd.oop.jdbc.table;

public class DataTypesClassifier {

	private static boolean isDate(final String data) {
		try {
			java.sql.Date.valueOf(data);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean isFloat(final String data) {
		try {
			Float.parseFloat(data);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean isIntger(final String data) {
		try {
			Integer.parseInt(data);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean typeClassification(final String data, final int typeClaimed) {
		if (typeClaimed == DataTypesConstants.typeInteger) {
			return isIntger(data);
		} else if (typeClaimed == DataTypesConstants.typeFloat) {
			return isFloat(data);
		} else if (typeClaimed == DataTypesConstants.typeDate) {
			return isDate(data);
		} else if (typeClaimed == DataTypesConstants.typeString) {
			return true;
		}
		return false;
	}
}