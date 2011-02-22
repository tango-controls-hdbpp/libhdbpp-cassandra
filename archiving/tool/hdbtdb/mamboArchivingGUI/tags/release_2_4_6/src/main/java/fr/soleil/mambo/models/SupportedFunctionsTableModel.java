package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;

import fr.soleil.mambo.tools.Messages;

public class SupportedFunctionsTableModel extends DefaultTableModel {
	public SupportedFunctionsTableModel() {
		super();
	}

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return 21;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			switch (rowIndex) {
			case 0:
				return Messages.getMessage("EXPRESSION_FUNCTION_SIN");
			case 1:
				return Messages.getMessage("EXPRESSION_FUNCTION_COS");
			case 2:
				return Messages.getMessage("EXPRESSION_FUNCTION_TAN");
			case 3:
				return Messages.getMessage("EXPRESSION_FUNCTION_ASIN");
			case 4:
				return Messages.getMessage("EXPRESSION_FUNCTION_ACOS");
			case 5:
				return Messages.getMessage("EXPRESSION_FUNCTION_ATAN");
			case 6:
				return Messages.getMessage("EXPRESSION_FUNCTION_ATAN2");
			case 7:
				return Messages.getMessage("EXPRESSION_FUNCTION_SINH");
			case 8:
				return Messages.getMessage("EXPRESSION_FUNCTION_COSH");
			case 9:
				return Messages.getMessage("EXPRESSION_FUNCTION_TANH");
			case 10:
				return Messages.getMessage("EXPRESSION_FUNCTION_ASINH");
			case 11:
				return Messages.getMessage("EXPRESSION_FUNCTION_ACOSH");
			case 12:
				return Messages.getMessage("EXPRESSION_FUNCTION_ATANH");
			case 13:
				return Messages.getMessage("EXPRESSION_FUNCTION_LN");
			case 14:
				return Messages.getMessage("EXPRESSION_FUNCTION_LOG");
			case 15:
				return Messages.getMessage("EXPRESSION_FUNCTION_EXP");
			case 16:
				return Messages.getMessage("EXPRESSION_FUNCTION_ABS");
			case 17:
				return Messages.getMessage("EXPRESSION_FUNCTION_MOD");
			case 18:
				return Messages.getMessage("EXPRESSION_FUNCTION_SQRT");
			case 19:
				return Messages.getMessage("EXPRESSION_FUNCTION_SUM");
			case 20:
				return Messages.getMessage("EXPRESSION_FUNCTION_IF");
			default:
				return null;
			}
		} else if (columnIndex == 1) {
			switch (rowIndex) {
			case 0:
				return "sin(x)";
			case 1:
				return "cos(x)";
			case 2:
				return "tan(x)";
			case 3:
				return "asin(x)";
			case 4:
				return "acos(x)";
			case 5:
				return "atan(x)";
			case 6:
				return "atan2(y,x)";
			case 7:
				return "sinh(x)";
			case 8:
				return "cosh(x)";
			case 9:
				return "tanh(x)";
			case 10:
				return "asinh(x)";
			case 11:
				return "acosh(x)";
			case 12:
				return "atanh(x)";
			case 13:
				return "ln(x)";
			case 14:
				return "log(x)";
			case 15:
				return "exp(x)";
			case 16:
				return "abs(x)";
			case 17:
				return "mod(x,y) = x % y";
			case 18:
				return "sqrt(x)";
			case 19:
				return "sum(x,y,z)";
			case 20:
				return "if("
						+ Messages.getMessage("EXPRESSION_FUNCTION_IF_COND")
						+ ","
						+ Messages.getMessage("EXPRESSION_FUNCTION_IF_TRUE")
						+ ","
						+ Messages.getMessage("EXPRESSION_FUNCTION_IF_FALSE")
						+ ")";
			default:
				return null;
			}
		} else
			return null;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Messages.getMessage("EXPRESSION_FUNCTION_NAME");
		case 1:
			return Messages.getMessage("EXPRESSION_FUNCTION_REPRESENTATION");
		default:
			return null;
		}
	}
}
