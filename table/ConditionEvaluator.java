package eg.edu.alexu.csd.oop.jdbc.table;

public class ConditionEvaluator {

	private String reference;
	private String operator;

	public ConditionEvaluator(final String reference, final String operator) {
		this.reference = reference;
		this.operator = operator;
	}

	private boolean compareDates(final String reference, final String toBeCompared,
			final String operator) {
		int comparisonResult = java.sql.Date.valueOf(toBeCompared).compareTo(
				java.sql.Date.valueOf(reference));
		if ((operator.equals(">") && comparisonResult > 0)
				|| (operator.equals("<") && comparisonResult < 0)
				|| (operator.equals("=") && comparisonResult == 0)) {
			return true;
		}
		return false;
	}

	private boolean compareFloats(final String reference, final String toBeCompared,
			final String operator) {
		Float referenceParsed = new Float(0);
		Float toBeComparedParsed = new Float(0);
		try {
			referenceParsed = Float.parseFloat(reference);
			toBeComparedParsed = Float.parseFloat(toBeCompared);
		} catch (Exception e) {
		}
		int comparisonResult = toBeComparedParsed.compareTo(referenceParsed);
		if ((operator.equals(">") && comparisonResult > 0)
				|| (operator.equals("<") && comparisonResult < 0)
				|| (operator.equals("=") && comparisonResult == 0)) {
			return true;
		}
		return false;
	}

	private boolean compareIntegers(final String reference, final String toBeCompared,
			final String operator) {
		int referenceParsed = 0;
		int toBeComparedParsed = 0;
		try {
			referenceParsed = Integer.parseInt(reference);
			toBeComparedParsed = Integer.parseInt(toBeCompared);
		} catch (Exception e) {
		}
		if ((operator.equals(">") && toBeComparedParsed > referenceParsed)
				|| (operator.equals("<") && toBeComparedParsed < referenceParsed)
				|| (operator.equals("=") && toBeComparedParsed == referenceParsed)) {
			return true;
		}
		return false;
	}

	private boolean compareStrings(final String reference, final String toBeCompared,
			final String operator) {
		int comparisonResult = toBeCompared.compareTo(reference);
		if ((operator.equals(">") && comparisonResult > 0)
				|| (operator.equals("<") && comparisonResult < 0)
				|| (operator.equals("=") && comparisonResult == 0)) {
			return true;
		}
		return false;
	}

	public boolean evalute(final String toBeCompared, final int typeOfColumn) {
		if (typeOfColumn == DataTypesConstants.typeInteger) {
			return compareIntegers(reference, toBeCompared, operator);
		} else if (typeOfColumn == DataTypesConstants.typeFloat) {
			return compareFloats(reference, toBeCompared, operator);
		} else if (typeOfColumn == DataTypesConstants.typeString) {
			return compareStrings(reference, toBeCompared, operator);
		} else if (typeOfColumn == DataTypesConstants.typeDate) {
			return compareDates(reference, toBeCompared, operator);
		}
		return false;
	}
}
