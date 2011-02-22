// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/snapshot/detail/SnapshotDetailTable.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailTable.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.13 $
//
// $Log: SnapshotDetailTable.java,v $
// Revision 1.13  2007/08/28 07:39:13  ounsy
// bug with R-->W correction
//
// Revision 1.12  2007/08/23 12:58:26  ounsy
// toUserFriendlyString() available in SnapshotDetailTable
//
// Revision 1.11  2007/08/21 15:13:18  ounsy
// Print Snapshot as table or text (Mantis bug 3913)
//
// Revision 1.10 2007/08/21 08:44:39 ounsy
// Snapshot Detail : Transfer Read To Write (Mantis bug 5543)
//
// Revision 1.9 2007/06/29 09:18:58 ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.8 2006/04/13 12:21:41 ounsy
// added a sort on the snapshot detail table
//
// Revision 1.7 2006/02/15 09:17:24 ounsy
// spectrums rw management
//
// Revision 1.6 2005/12/14 16:18:03 ounsy
// added the selectAllOrNone method
//
// Revision 1.5 2005/11/29 18:25:27 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:35 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================

package fr.soleil.bensikin.components.snapshot.detail;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;

import fr.soleil.bensikin.actions.listeners.SnapshotDetailTableHeaderListener;
import fr.soleil.bensikin.actions.listeners.SnapshotDetailTableListener;
import fr.soleil.bensikin.components.editors.SnapshotDetailEditor;
import fr.soleil.bensikin.components.renderers.SnapshotDetailRenderer;
import fr.soleil.bensikin.components.renderers.SortedTableHeaderRenderer;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeWriteValue;
import fr.soleil.bensikin.models.SnapshotDetailTableModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.tools.GUIUtilities;

/**
 * A JTable used to view/edit the detail of a given snapshot.
 *
 * @author CLAISSE
 */
public class SnapshotDetailTable extends JXTable {
    protected Snapshot snapshot;

    /**
     * Builds its model from the given snapshot, makes the 1st column the same
     * color as the header, and adds a cell renderer and editor to itself.
     *
     * @param snapshot
     *            The snapshot we wish to view/edit
     */
    public SnapshotDetailTable (Snapshot snapshot) {
        super();
        setSortable(false);//for the JXTable not to interact with the sort mechanism already in place
        this.snapshot = snapshot;
        initialize();
    }

    protected void initialize () {
        initializeModel();
        initializeRenderer();
        initializeEditor();
        initializeTableColumns();
        this.setRowHeight( this.getRowHeight() + 2 );
        this.addMouseListener( new SnapshotDetailTableListener() );
    }

    protected void initializeTableColumns() {
        TableColumn column = this.getColumn( this.getColumnName( 4 ) );
        JCheckBox checkBox = new JCheckBox();
        column.setMinWidth( checkBox.getMinimumSize().width );
        this.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS );
        TableColumn column2 = this.getColumn( this.getColumnName( 0 ) );
        column2.setMinWidth( 200 );
        column2.setPreferredWidth( 200 );
    }

    protected void initializeRenderer () {
        this.setDefaultRenderer( Object.class, new SnapshotDetailRenderer() );
    }

    protected void initializeEditor () {
        this.setDefaultEditor( Object.class, new SnapshotDetailEditor( this ) );
    }

    protected void initializeModel () {
        SnapshotDetailTableModel model = SnapshotDetailTableModel.getInstance(
                snapshot, true );
        this.setModel( model );
        JTableHeader header = this.getTableHeader();
        header
                .addMouseListener( new SnapshotDetailTableHeaderListener( model ) );
        header.setDefaultRenderer( new SortedTableHeaderRenderer() );
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    @Override
	public boolean isCellEditable (int row, int column) {
        /*
         * if (column != 1)//the write column is the only editable value {
         * return false; } SnapshotAttributeValue objectAt =
         * (SnapshotAttributeValue) this.getValueAt(row, column); return
         * !objectAt.isNotApplicable();
         */
        Object value = this.getValueAt( row, column );
        if ( value instanceof SnapshotAttributeWriteValue ) {
            if ( ( (SnapshotAttributeWriteValue) value ).getDataFormat() == SnapshotAttributeWriteValue.SPECTRUM_DATA_FORMAT ) {
                return false;
            }
            return !( (SnapshotAttributeWriteValue) value ).isNotApplicable();
        }
        else if ( value instanceof Boolean ) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JTable#isColumnSelected(int)
     */
    @Override
	public boolean isColumnSelected (int column) {
        if ( column == 0 ) {
            return false;
        }
        return super.isColumnSelected( column );
    }

    /**
     * @param b
     */
    public void selectAllOrNone (boolean b) {
        SnapshotDetailTableModel model = (SnapshotDetailTableModel) this
                .getModel();
        model.selectAllOrNone( b );
    }

    public void transferSelectedReadToWrite () {
        SnapshotDetailTableModel model = (SnapshotDetailTableModel) this
                .getModel();
        model.transferSelectedReadToWrite();
        repaint();
    }

    /**
     *
     */
    public void applyChange () {
        SnapshotDetailTableModel model = (SnapshotDetailTableModel) this
                .getModel();
        SnapshotDetailEditor editor = (SnapshotDetailEditor) this
                .getCellEditor();
        if ( this.isEditing() ) {
            model.setValueAt( editor.getCellEditorValue(),
                    this.getEditingRow(), this.getEditingColumn() );
        }
    }

    /**
     * @return the snapshot
     */
    public Snapshot getSnapshot () {
        return snapshot;
    }

    public String toUserFriendlyString() {
        StringBuffer buffer = new StringBuffer();
        int rowCount = getRowCount();
        int columnCount = getColumnCount();
        for (int column = 0; column < columnCount; column++) {
            buffer.append( getColumnName(column) );
            if (column < columnCount - 1) {
                buffer.append( Options.getInstance().getSnapshotOptions()
                        .getSeparator() );
            }
            else {
                buffer.append(GUIUtilities.CRLF);
            }
        }
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                Object value = getValueAt(row, column);
                if (value instanceof SnapshotAttributeValue) {
                    ( (SnapshotAttributeValue)value ).appendToString(buffer);
                }
                else {
                    buffer.append(value);
                }
                if (column < columnCount - 1) {
                    buffer.append( Options.getInstance().getSnapshotOptions().getSeparator() );
                }
            }
            if (row < rowCount - 1) {
                buffer.append(GUIUtilities.CRLF);
            }
        }
        if (snapshot != null) {
            buffer.append( snapshot.toUserFriendlyString() );
        }
        return buffer.toString();
    }

}
