package fr.soleil.bensikin.models;

import javax.swing.table.DefaultTableModel;

public class ColumnHeaderModel extends DefaultTableModel
{
    private int rows;

    public ColumnHeaderModel (int rowCount)
    {
        super();
        rows = rowCount;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount ()
    {
        return rows;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        return 1;
    }
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt ( int rowIndex , int columnIndex )
    {
        return Integer.toString(rowIndex + 1);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int columnIndex)
    {
        return "";
    }

}
