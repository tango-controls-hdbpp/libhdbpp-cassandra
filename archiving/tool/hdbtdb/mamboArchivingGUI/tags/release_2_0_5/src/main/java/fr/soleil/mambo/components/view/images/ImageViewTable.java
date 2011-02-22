package fr.soleil.mambo.components.view.images;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;


/**
 * A singleton class containing the current list of snapshots.
 * The table's cells are not editable.
 * A SnapshotTableListener is added that listens to line selection events, and
 * a SnapshotTableHeaderListener is added that listens to column double-clicks to sort them.
 *
 * @author CLAISSE
 */
public class ImageViewTable extends JTable
{
    public ImageViewTable ( ImageViewTableModel model )
    {
        super ( model );
        
        this.setDefaultRenderer( Object.class , new ImageViewTableCellRenderer() );
        
        JTableHeader header = this.getTableHeader();
        header.resizeAndRepaint ();
        
        this.setRowHeight(20);
        this.resizeAndRepaint();
        
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF );
    }

    /* (non-Javadoc)
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row , int column)
    {
        return false;
    }
}
