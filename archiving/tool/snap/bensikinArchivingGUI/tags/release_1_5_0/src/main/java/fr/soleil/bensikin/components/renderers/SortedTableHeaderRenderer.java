
package fr.soleil.bensikin.components.renderers;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.components.BensikinComparator;
import fr.soleil.bensikin.models.SortedTableModel;

public class SortedTableHeaderRenderer extends DefaultTableCellRenderer {

    public SortedTableHeaderRenderer() {
        super();
    }

    public Component getTableCellRendererComponent (JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if ( table != null ) {
            JTableHeader header = table.getTableHeader();
            if ( header != null ) {
                setForeground( header.getForeground() );
                setBackground( header.getBackground() );
                setFont( header.getFont() );
            }
            if ( table.getModel() != null
                    && table.getModel() instanceof SortedTableModel ) {
                SortedTableModel model = (SortedTableModel)table.getModel();
                int sortedColumn = model.getSortedColumn();
                int idSort = model.getIdSort();
                if (column == sortedColumn) {
                    switch (idSort) {
                        case BensikinComparator.SORT_UP:
                            setIcon( new ImageIcon( Bensikin.class
                                    .getResource( "icons/sort_up.gif" ) ) );
                            break;
                        case BensikinComparator.SORT_DOWN:
                            setIcon( new ImageIcon( Bensikin.class
                                    .getResource( "icons/sort_down.gif" ) ) );
                            break;
                        default:
                            setIcon(null);
                            break;
                    }
                }
                else {
                    setIcon(null);
                }
            }
        }
        setText( ( value == null ) ? "" : value.toString() );
        setBorder( UIManager.getBorder( "TableHeader.cellBorder" ) );
        setHorizontalAlignment(JLabel.CENTER);
        return this;
    }
}
