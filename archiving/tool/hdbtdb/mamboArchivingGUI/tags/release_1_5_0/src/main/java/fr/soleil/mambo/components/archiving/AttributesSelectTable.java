//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/AttributesSelectTable.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ContextListTable.
//						(Claisse Laurent) - 17 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: AttributesSelectTable.java,v $
//Revision 1.2  2006/05/29 15:45:32  ounsy
//corrected a little  bug
//
//Revision 1.1  2005/11/29 18:27:24  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.archiving;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import fr.soleil.mambo.actions.archiving.listeners.AttributesSelectTableHeaderListener;
import fr.soleil.mambo.components.editors.AttributesSelectTableEditor;
import fr.soleil.mambo.components.renderers.AttributesSelectTableRenderer;
import fr.soleil.mambo.models.AttributesSelectTableModel;


/**
 * A singleton class containing the current list of contexts.
 * The table's cells are not editable.
 * A ContextTableListener is added that listens to line selection events, and
 * a ContextTableHeaderListener is added that listens to column double-clicks to sort them.
 */
public class AttributesSelectTable extends JTable
{
    private static AttributesSelectTable contextListTableInstance = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static AttributesSelectTable getInstance ()
    {
        if ( contextListTableInstance == null )
        {
            contextListTableInstance = new AttributesSelectTable();
        }

        return contextListTableInstance;
    }

    /**
     * Default constructor.
     * <UL>
     * <LI> Instantiates its table model
     * <LI> Adds a selection listener on its table body (ContextTableListener)
     * <LI> Adds a sort request listener on its table header (ContextTableHeaderListener)
     * <LI> Sets its columns sizes	and row height
     * <LI> Disables the columns auto resize mode
     * </UL>
     */
    private AttributesSelectTable ()
    {
        super( AttributesSelectTableModel.getInstance() );

        this.setDefaultRenderer( Object.class , new AttributesSelectTableRenderer() );
        this.setDefaultEditor( Object.class , new AttributesSelectTableEditor() );

        JTableHeader header = this.getTableHeader();
        header.addMouseListener( new AttributesSelectTableHeaderListener() );

        this.setRowHeight( 20 );
        this.getColumn( " " ).setMaxWidth( 25 );
        this.getColumn( " " ).setMinWidth( 25 );
    }

    /* (non-Javadoc)
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    public boolean isCellEditable ( int row , int column )
    {
        if ( column == 3 )
        {
            return true;
        }
        return false;
    }

    /**
     *
     */
    public void reverseSelection ()
    {
        int[] selectedRows = this.getSelectedRows();
        AttributesSelectTableModel model = ( AttributesSelectTableModel ) this.getModel();
        model.reverseSelectionForRows( selectedRows );
    }

    /**
     * @param b
     */
    public void selectAllOrNone ( boolean b )
    {
        AttributesSelectTableModel model = ( AttributesSelectTableModel ) this.getModel();
        model.selectAllOrNone( b );
    }

    /**
     *  
     */
    public void applyChange ()
    {
        AttributesSelectTableModel model = ( AttributesSelectTableModel ) this.getModel();
        AttributesSelectTableEditor editor = ( AttributesSelectTableEditor ) this.getCellEditor();

        if ( this.isEditing() )
        {
            model.setValueAt( editor.getCellEditorValue() , this.getEditingRow() , this.getEditingColumn() );
            editor.stopCellEditing();
        }
    }

}
