package eg.edu.alexu.csd.oop.jdbc.table;

import java.util.ArrayList;
import java.util.Map;

public class ConditionHandler {

	private ColumnHandler checkColumnNameInCondition;
	private ArrayList<String> splitedConditon;
	private String condition;

	public ConditionHandler(final String condition, final Map<String, Integer> dataTable) {
		this.condition = condition;
		if (!specialCaseConditions(condition)) {
			checkColumnNameInCondition = new ColumnHandler(dataTable);
			splitedConditon = splitCondition(condition);
			splitedConditon.set(0, splitedConditon.get(0).toLowerCase());
			if (!checkColumnNameInCondition.isColumnValid(
					splitedConditon.get(0), splitedConditon.get(2))) {
				throw new RuntimeException("Error in condition types.");
			}
		} else {
			if (invalidCondition(condition)) {
				throw new RuntimeException("Wrong format in the condition.");
			}
		}
	}

	public boolean evaluateCondition(final String valueInTable, final Integer typeOfColumn) {
		if (isTrueCondition(getCondition())) {
			return true;
		}
		String afterTypeChecked = null;
		if (typeOfColumn == DataTypesConstants.typeString
				|| typeOfColumn == DataTypesConstants.typeDate) {
			afterTypeChecked = splitedConditon.get(2).substring(1,
					splitedConditon.get(2).length() - 1);
		} else {
			afterTypeChecked = splitedConditon.get(2);
		}

		ConditionEvaluator evaluator = new ConditionEvaluator(afterTypeChecked,
				splitedConditon.get(1));
		return evaluator.evalute(valueInTable, typeOfColumn);
	}

	public String getColumnName() {
		if (splitedConditon == null) {
			return null;
		}
		return splitedConditon.get(0);
	}

	public String getCondition() {
		return condition;
	}

	private boolean invalidCondition(final String condition) {
		if (condition == null) {
			return false;
		}
		if (condition.length() == 1) {
			return true;
		}
		return false;
	}

	private boolean isTrueCondition(final String condition) {
		if (condition == null) {
			return true;
		}
		return false;
	}

	private boolean isValidOperator(final char operator) {
		return (operator == '>' || operator == '<' || operator == '=');
	}

	public boolean specialCaseConditions(final String condition) {
		if (condition == null) {
			return true;
		} else if (condition.length() == 1) {
			return true;
		}
		return false;
	}

	private ArrayList<String> splitCondition(final String condition) {
		ArrayList<String> afterSpliting = new ArrayList<String>();
		for (int i = 0; i < condition.length(); ++i) {
			if (isValidOperator(condition.charAt(i))) {
				afterSpliting.add(condition.substring(0, i));
				afterSpliting.add(this.getCondition().substring(i, i + 1));
				afterSpliting
						.add(condition.substring(i + 1, condition.length()));
				break;
			}
		}
		return afterSpliting;
	}

}
