package fr.soleil.bensikin.components.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import fr.soleil.bensikin.components.BensikinBooleanViewer;

public class SnapshotWriteValueBooleanEditor extends AbstractCellEditor
        implements TableCellEditor
{
    Component comp;
    public SnapshotWriteValueBooleanEditor ()
    {
        super();
    }

    public Component getTableCellEditorComponent ( 
            JTable table , Object value ,
            boolean isSelected , int row ,
            int column )
    {
        if ( value instanceof String )
        {
            if ("true".equalsIgnoreCase(value.toString().trim()) || "false".equalsIgnoreCase(value.toString().trim()))
            {
                comp = new BensikinBooleanViewer(new Boolean("true".equalsIgnoreCase(value.toString().trim())));
                ((BensikinBooleanViewer)comp).addActionListener(new BooleanListener(((BensikinBooleanViewer)comp), table, row, column));
                ((BensikinBooleanViewer)comp).setOpaque(false);
            }
            else comp = new JTextField(value.toString());
        }
        else
        {
            comp =  new JTextField(value.toString());
        }
        return comp;
    }

    public Object getCellEditorValue ()
    {
        if (comp instanceof BensikinBooleanViewer)
        {
            return "" + ((BensikinBooleanViewer)comp).isSelected();
        }
        else return ((JTextField)comp).getText();
    }
}

class BooleanListener implements ActionListener
{

    private BensikinBooleanViewer checkbox;
    private JTable correspondingTable;
    private int row;
    private int column;

    public BooleanListener ( BensikinBooleanViewer _checkbox , JTable table , int _row ,
                              int _column )
    {

        this.checkbox = _checkbox;
        this.correspondingTable = table;
        this.row = _row;
        this.column = _column;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        checkbox.setSelected(new Boolean(checkbox.isSelected()));
        correspondingTable.setValueAt( "" + checkbox.isSelected(), row, column );
        correspondingTable.editingCanceled( null );
    }
}