package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;

import fr.soleil.mambo.tools.Messages;

public class SupportedOperatorsTableModel extends DefaultTableModel
{
    public SupportedOperatorsTableModel ()
    {
        super();
    }

    public int getColumnCount ()
    {
        return 2;
    }

    public int getRowCount ()
    {
        return 12;
    }

    public boolean isCellEditable (int rowIndex, int columnIndex)
    {
        return false;
    }

    public Object getValueAt (int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
        {
            switch(rowIndex)
            {
                case 0 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_POWER");
                case 1 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_NOT");
                case 2 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_UNARY");
                case 3 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_MODULUS");
                case 4 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_DIVISION");
                case 5 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_MULTIPLICATION");
                case 6 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_ADDSUB");
                case 7 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_LESSMOREEQ");
                case 8 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_LESSMORE");
                case 9 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_EQUALNOT");
                case 10 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_AND");
                case 11 :
                    return Messages.getMessage("EXPRESSION_OPERATOR_OR");
                default :
                    return null;
            }
        }
        else if (columnIndex == 1)
        {
            switch(rowIndex)
            {
                case 0 :
                    return "^";
                case 1 :
                    return "!";
                case 2 :
                    return "+x, -x";
                case 3 :
                    return "%";
                case 4 :
                    return "/";
                case 5 :
                    return "*";
                case 6 :
                    return "+, -";
                case 7 :
                    return "<=, >=";
                case 8 :
                    return "<, >";
                case 9 :
                    return "!=, ==";
                case 10 :
                    return "&&";
                case 11 :
                    return "||";
                default :
                    return null;
            }
        }
        else return null;
    }

    public String getColumnName (int columnIndex)
    {
        switch(columnIndex)
        {
            case 0 :
                return Messages.getMessage("EXPRESSION_OPERATOR_NAME");
            case 1 :
                return Messages.getMessage("EXPRESSION_OPERATOR_REPRESENTATION");
            default :
                return null;
        }
    }
}
