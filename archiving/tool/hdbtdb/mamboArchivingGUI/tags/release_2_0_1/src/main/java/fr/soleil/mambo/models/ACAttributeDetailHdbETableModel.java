//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailHdbETableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbETableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACAttributeDetailHdbETableModel.java,v $
// Revision 1.3  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.2  2005/12/15 11:41:08  ounsy
// period formating
//
// Revision 1.1  2005/11/29 18:27:08  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeDetailHdbETableModel extends DefaultTableModel
{
    private static ACAttributeDetailHdbETableModel instance;
    private ArchivingConfigurationAttribute attribute;
    private ArchivingConfigurationAttributeHDBProperties HDBProperties;

    public static ACAttributeDetailHdbETableModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACAttributeDetailHdbETableModel();
        }

        return instance;
    }

    private ACAttributeDetailHdbETableModel ()
    {
        super();
    }

    public void load ( ArchivingConfigurationAttribute _attribute )
    {
        attribute = _attribute;
        ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
        HDBProperties = attrProperties.getHDBProperties();
        fireTableDataChanged();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount ()
    {
        return 1;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public void setValueAt ( Object aValue , int rowIndex , int columnIndex )
    {
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt ( int rowIndex , int columnIndex )
    {
        switch ( columnIndex )
        {
            case 0:
                return getFirstColumnValue( rowIndex );
            case 1:
                return getSecondColumnValue( rowIndex );
            case 2:
                return getACColumnValue( rowIndex );
            case 3:
                return getDBColumnValue( rowIndex );
            default:
                return null;
        }
    }

    private Object getDBColumnValue ( int rowIndex )
    {
        Boolean ret = new Boolean( false );
        IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
        try
        {
            Mode mode = manager.getArchivingMode( attribute.getCompleteName() , true );
            if ( mode != null && mode.getModeE() != null )
            {
                ret = new Boolean( true );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        return ret;
    }

    private Object getACColumnValue ( int rowIndex )
    {
        Boolean ret = new Boolean( false );
        if ( HDBProperties != null )
        {
            ArchivingConfigurationMode ACEMode = HDBProperties.getMode( ArchivingConfigurationMode.TYPE_E );
            if ( ACEMode != null )
            {
                ret = new Boolean( ACEMode.getMode().getModeE() != null );
            }
        }
        return ret;
    }

    public boolean toPaint ()
    {
        if ( HDBProperties != null )
        {
            if ( HDBProperties.getMode( ArchivingConfigurationMode.TYPE_E ) != null )
            {
                return true;
            }
            else if ( attribute != null )
            {
                try
                {
                    Mode mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , true );
                    if ( mode.getModeE() != null )
                    {
                        return true;
                    }
                }
                catch ( Exception e )
                {
                    return false;
                }
            }
        }
        return false;
    }

    private Object getSecondColumnValue ( int rowIndex )
    {
        String ret = "";
        switch ( rowIndex )
        {
            case 0:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEE_ARCHIVED" );
                break;
        }
        return ret;
    }

    private Object getFirstColumnValue ( int rowIndex )
    {
        String ret = "";
        if ( rowIndex == 0 )
        {
            ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEE_NAME" );
        }
        return ret;
    }

}
