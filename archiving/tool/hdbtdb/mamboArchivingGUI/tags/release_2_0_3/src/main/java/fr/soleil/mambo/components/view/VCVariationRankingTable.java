//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/VCVariationRankingTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationRankingTable.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationRankingTable.java,v $
// Revision 1.5  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.4  2006/02/01 14:07:56  ounsy
// minor changes (column titles appear)
//
// Revision 1.3  2005/12/15 16:42:52  ounsy
// look and feel
//
// Revision 1.2  2005/12/15 11:19:10  ounsy
// "copy table to clipboard" management
//
// Revision 1.1  2005/11/29 18:28:12  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.view;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.JTextField;
//import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import fr.soleil.mambo.components.MamboFormatableTable;
import fr.soleil.mambo.components.renderers.MamboBasicTableRenderer;
import fr.soleil.mambo.models.VCVariationRankingTableModel;
import fr.soleil.mambo.options.Options;


public class VCVariationRankingTable extends MamboFormatableTable
{
    private static VCVariationRankingTable instance = null;

    public static VCVariationRankingTable getInstance ( boolean forceReload )
    {
        if ( instance == null || forceReload )
        {
            instance = new VCVariationRankingTable();
        }

        return instance;
    }

    private VCVariationRankingTable ()
    {
        super( VCVariationRankingTableModel.getInstance() );
        setAutoCreateColumnsFromModel( true );
        autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;
        //grayOutFirstColumn();
        Enumeration columns = getColumnModel().getColumns();
        int i = 0;
        while ( columns.hasMoreElements() )
        {
            int margin = 0;
            if ( i == 0 )
            {
                margin = 50;
            }
            TableColumn column = ( TableColumn ) columns.nextElement();
            JTextField textField = new JTextField( ""
                                                   + VCVariationRankingTableModel.getInstance().getValueAt( 0 , i ) );
            int width = textField.getPreferredSize().width;
            if ( column.getMaxWidth() < width + margin )
            {
                column.setMaxWidth( width + margin );
            }
            if ( column.getWidth() < width + margin )
            {
                column.setWidth( width + margin );
            }
            if ( column.getMinWidth() < width + margin )
            {
                column.setMinWidth( width + margin );
            }
            i++;
        }
        this.setDefaultRenderer(Object.class, new MamboBasicTableRenderer());
    }

    /**
     * 14 juin 2005
     */
    /*private void grayOutFirstColumn ()
    {
        String firstColumnIdentifier = this.getColumnName( 0 );
        TableColumn firstColumn = this.getColumn( firstColumnIdentifier );

        TableCellRenderer firstColumnCellRenderer = this.getTableHeader().getDefaultRenderer();
        firstColumn.setCellRenderer( firstColumnCellRenderer );

    }*/

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
        return false;
    }

    public String toString()
    {
        String value = "";
        for (int i = 0; i < getModel().getColumnCount(); i++)
        {
            value += getModel().getColumnName(i) + Options.getInstance().getGeneralOptions().getSeparator();
        }
        value = value.substring(0, value.lastIndexOf(Options.getInstance().getGeneralOptions().getSeparator()));
        value += "\n";
        value += super.toString();
        return value;
    }
}
