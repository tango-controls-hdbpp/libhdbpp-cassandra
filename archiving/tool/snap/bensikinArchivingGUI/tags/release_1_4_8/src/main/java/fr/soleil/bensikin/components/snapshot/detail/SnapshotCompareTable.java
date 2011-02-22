//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/snapshot/detail/SnapshotCompareTable.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SnapshotDetailTable.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: soleilarc $
//
//$Revision: 1.8 $
//
//$Log: SnapshotCompareTable.java,v $
//Revision 1.8  2007/10/29 14:44:17  soleilarc
//Author: XP
//Mantis bug ID: 5629
//Comment :
//In the SnapshotCompareTable builder, change the renderer of the header of the table.
//In the setColumnId method, define an other content for the colName variable.
//
//Revision 1.7  2007/07/03 08:40:06  ounsy
//trace removed
//
//Revision 1.6  2007/06/29 09:18:58  ounsy
//devLong represented as Integer + SnapshotCompareTable sorting
//
//Revision 1.5  2007/06/28 12:24:20  ounsy
//minor changes
//
//Revision 1.4  2007/03/26 08:07:53  ounsy
//*** empty log message ***
//
//Revision 1.3  2006/06/28 12:47:35  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:16:55  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:25:27  chinkumo
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
package fr.soleil.bensikin.components.snapshot.detail;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import fr.soleil.bensikin.actions.listeners.SnapshotCompareTableHeaderListener;
import fr.soleil.bensikin.components.renderers.SnapshotCompareRenderer;
import fr.soleil.bensikin.components.renderers.SnapshotCompareTableHeaderRenderer;
import fr.soleil.bensikin.components.renderers.SortedTableHeaderRenderer;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;

/**
 * A JTable used to compare 2 Snapshots, that can take a variable set of columns.
 *
 * @author CLAISSE
 */
public class SnapshotCompareTable extends JTable
{
    private SnapshotCompareTablePrintModel model;
    
    private boolean showRead;
    private boolean showWrite;
    private boolean showDelta;
    private boolean showDiff;

    /**
     * Builds a SnapshotCompareTable with the specified columns.
     *
     * @param _showRead  If true, shows the read value of attributes
     * @param _showWrite If true, shows the write value of attributes
     * @param _showDelta If true, shows the delta value of attributes
     * @param _showDiff  If true, shows the delta between the two snapshots
     */
    public SnapshotCompareTable ( boolean _showRead , boolean _showWrite , boolean _showDelta , boolean _showDiff )
    {
        super();
        this.setDefaultRenderer( Object.class , new SnapshotCompareRenderer() );
        this.setColumnModel(new PersistentColumnModel ());
        
        this.showRead = _showRead;
        this.showWrite = _showWrite;
        this.showDelta = _showDelta;
        this.showDiff = _showDiff;

        this.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        this.setRowHeight( this.getRowHeight() + 2 );
        
        this.setName("SnapshotCompareTable");
        getTableHeader().setDefaultRenderer( new SortedTableHeaderRenderer() );
        this.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed (MouseEvent e) {
                if ( rowAtPoint( e.getPoint() ) == 0 ) {
                    MouseListener[] listeners = getTableHeader()
                            .getMouseListeners();
                    if ( listeners != null ) {
                        for (int i = 0; i < listeners.length; i++) {
                            if ( listeners[i] instanceof SnapshotCompareTableHeaderListener ) {
                                MouseEvent event = new MouseEvent(
                                        getTableHeader(),
                                        e.getID(),
                                        e.getWhen(),
                                        e.getModifiers(),
                                        e.getX(),
                                        e.getY(),
                                        e.getClickCount(),
                                        e.isPopupTrigger(),
                                        e.getButton()
                                );
                                listeners[i].mousePressed(event);
                                getTableHeader().repaint();
                            }
                        }
                    }
                }
                super.mousePressed(e);
            }
        } );
        getTableHeader().setDefaultRenderer(
        		new SnapshotCompareTableHeaderRenderer( getTableHeader().getDefaultRenderer() )
        );
    }

    /* (non-Javadoc)
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    public boolean isCellEditable ( int row , int column )
    {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.JTable#isColumnSelected(int)
     */
    public boolean isColumnSelected ( int column )
    {
        if ( column == 0 )
        {
            return false;
        }

        return super.isColumnSelected( column );
    }


    /**
     * Builds its model from <code>snapshot1</code> and <code>snapshot2</code>
     *
     * @param snapshot1 The 1st snapshot of the comparison
     * @param snapshot2 The 2nd snapshot of the comparison
     */
    public void build ( Snapshot snapshot1 , Snapshot snapshot2 )
    {
        model = SnapshotCompareTablePrintModel.getInstance ( this.showRead , this.showWrite , this.showDelta , this.showDiff );
        model.build( snapshot1 , snapshot2 );

        JTableHeader header = this.getTableHeader ();
        MouseListener[] listeners = header.getMouseListeners();
        if (listeners != null) {
            MouseListener[] copyListeners = new MouseListener[listeners.length];
            System.arraycopy( listeners, 0, copyListeners, 0, listeners.length );
            for (int i = 0; i < copyListeners.length; i++) {
                if (copyListeners[i] instanceof SnapshotCompareTableHeaderListener) {
                    header.removeMouseListener( copyListeners[i] );
                }
            }
        }

        this.setModel( model );
        this.setColumnsSize();
        header.addMouseListener ( new SnapshotCompareTableHeaderListener ( model ) );
    }

    /**
     * Presets its columns sizes
     */
    private void setColumnsSize ()
    {
        TableColumnModel columnModel = this.getColumnModel();
        Enumeration enumer = columnModel.getColumns();
        
        int i = 0;
        while ( enumer.hasMoreElements() )
        {
            TableColumn nextCol = ( TableColumn ) enumer.nextElement();
            String id = "" + nextCol.getIdentifier();
            this.setColumnId (nextCol,i);
            // public Object getIdentifier()
            int size = 120;
            if ( id.trim().equals( "" ) )
            {
                size = 200;
            }

            nextCol.setPreferredWidth( size );
            i++;
        }
    }


    private void setColumnId(TableColumn nextCol, int columnIndex) 
    {
        //String colName = subModel.getColumnName(columnIndex);
//        String colName = (String) this.model.getValueAt(0, columnIndex);
    	String colName = this.model.getColumnName(columnIndex);
        //System.out.println("SnapshotCompareTable/setColumnId/columnIndex/"+columnIndex+"/colName/"+colName);
        nextCol.setIdentifier(colName);
    }
}
