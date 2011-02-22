
package fr.soleil.mambo.components.renderers;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import fr.soleil.mambo.components.MamboBooleanViewer;
import fr.soleil.mambo.tools.Messages;

public class BooleanTableRenderer implements TableCellRenderer
{
    public BooleanTableRenderer ()
    {
        super();
    }

    public Component getTableCellRendererComponent (JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component comp;
        if ( value instanceof Boolean || (value == null && column > 0) )
        {
            comp = new MamboBooleanViewer( (Boolean) value );
        }
        else
        {
            comp = new JLabel( value == null ? Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA"): value.toString() );
        }
        return comp;
    }
}