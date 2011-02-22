//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailTdbCTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailTdbCTableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACAttributeDetailTdbCTableModel.java,v $
// Revision 1.3  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.2  2005/12/15 11:42:08  ounsy
// minor changes
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
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeDetailTdbCTableModel extends DefaultTableModel
{
    private static ACAttributeDetailTdbCTableModel instance;
    private ArchivingConfigurationAttribute attribute;
    private ArchivingConfigurationAttributeTDBProperties TDBProperties;

    public static ACAttributeDetailTdbCTableModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACAttributeDetailTdbCTableModel();
        }

        return instance;
    }

    private ACAttributeDetailTdbCTableModel ()
    {
        super();
    }

    public void load ( ArchivingConfigurationAttribute _attribute )
    {
        attribute = _attribute;
        ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
        TDBProperties = attrProperties.getTDBProperties();
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
        return 3;
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
        String ret = "";
        IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
        try
        {
            Mode mode = manager.getArchivingMode( attribute.getCompleteName() , false );
            if ( mode != null && mode.getModeC() != null )
            {
                switch ( rowIndex )
                {
                    case 0:
                        ret = mode.getModeC().getPeriod() + "ms";
                        break;
                    case 1:
                        ret = mode.getModeC().getRange() + "";
                        break;
                    case 2:
                        ret = mode.getModeC().getTypeCalcul() + "";
                        break;
                }
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
        String ret = "";
        if ( TDBProperties != null )
        {
            ArchivingConfigurationMode ACCMode = TDBProperties.getMode( ArchivingConfigurationMode.TYPE_C );
            if ( ACCMode != null )
            {
                switch ( rowIndex )
                {
                    case 0:
                        ret = ACCMode.getMode().getModeC().getPeriod() + "ms";
                        break;
                    case 1:
                        ret = ACCMode.getMode().getModeC().getRange() + "";
                        break;
                    case 2:
                        ret = ACCMode.getMode().getModeC().getTypeCalcul() + "";
                        break;
                }

            }
        }
        return ret;
    }

    public boolean toPaint ()
    {
        if ( TDBProperties != null )
        {
            if ( TDBProperties.getMode( ArchivingConfigurationMode.TYPE_C ) != null )
            {
                return true;
            }
            else if ( attribute != null )
            {
                try
                {
                    Mode mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , false );
                    if ( mode.getModeC() != null )
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
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_PERIOD" );
                break;
            case 1:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_RANGE" );
                break;
            case 2:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_TYPE" );
                break;
        }
        return ret;
    }

    private Object getFirstColumnValue ( int rowIndex )
    {
        String ret = "";
        if ( rowIndex == 0 )
        {
            ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_NAME" );
        }
        return ret;
    }

}
