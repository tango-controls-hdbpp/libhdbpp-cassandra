//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailTdbTitleTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailTdbTitleTableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ACAttributeDetailTdbTitleTableModel.java,v $
// Revision 1.4  2006/12/07 16:45:39  ounsy
// removed keeping period
//
// Revision 1.3  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.2  2006/08/09 10:35:27  ounsy
// Time formating in assessments + differences between database and archiving configuration highlighted
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
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
import fr.soleil.mambo.formaters.MamboTimeFormater;
import fr.soleil.mambo.tools.Messages;


public class ACAttributeDetailTdbTitleTableModel extends DefaultTableModel
{
    private static ACAttributeDetailTdbTitleTableModel instance;
    private ArchivingConfigurationAttribute attribute;
    private ArchivingConfigurationAttributeTDBProperties TDBProperties;

    public static ACAttributeDetailTdbTitleTableModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACAttributeDetailTdbTitleTableModel();
        }

        return instance;
    }

    private ACAttributeDetailTdbTitleTableModel ()
    {
        super();
    }

    public void load ( ArchivingConfigurationAttribute _attribute )
    {
        attribute = _attribute;
        ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
        TDBProperties = attrProperties.getTDBProperties();
        fireTableStructureChanged();
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
        int rc = 1;
        if ( TDBProperties != null )
        {
            try
            {
                ArchivingConfigurationMode[] modes = TDBProperties.getModes();
                if ( modes == null )
                {
                    modes = new ArchivingConfigurationMode[ 0 ];
                }
                if ( modes.length == 0 )
                {
                    Mode mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , false );
                    if ( mode != null && mode.getTdbSpec() != null )
                    {
                        rc = 2;
                    }
                }
                else
                {
                    rc = 2;
                }
            }
            catch ( Exception e )
            {
                return 1;
            }
        }
        return rc;
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
        switch ( rowIndex )
        {
            case 0:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_DB" );
                break;
            case 1:
                Mode mode;
                try
                {
                    mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , false );
                    if ( mode != null && mode.getTdbSpec() != null )
                    {
                        //ret = mode.getTdbSpec().getExportPeriod() + "ms";
                        ret = MamboTimeFormater.formatTime( mode.getTdbSpec().getExportPeriod() );
                    }
                }
                catch ( Exception e )
                {
                    ret = "";
                    e.printStackTrace();
                }
                break;
            /*case 2:
                try
                {
                    mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , false );
                    if ( mode != null && mode.getTdbSpec() != null )
                    {
                        //ret = mode.getTdbSpec().getKeepingPeriod() + "ms";
                        ret = MamboTimeFormater.formatTime( mode.getTdbSpec().getKeepingPeriod() );
                    }
                }
                catch ( Exception e )
                {
                    ret = "";
                    e.printStackTrace();
                }
                break;*/
        }
        return ret;
    }

    private Object getACColumnValue ( int rowIndex )
    {
        ArchivingConfigurationMode[] modes = null;
        ;
        if ( TDBProperties != null )
        {
            modes = TDBProperties.getModes();
        }
        if ( modes == null )
        {
            modes = new ArchivingConfigurationMode[ 0 ];
        }
        String ret = "";
        switch ( rowIndex )
        {
            case 0:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_AC" );
                break;
            case 1:
                if ( modes.length > 0 )
                {
                    //ret = TDBProperties.getExportPeriod() + "ms";
                    ret = MamboTimeFormater.formatTime( TDBProperties.getExportPeriod() );
                }
                break;
            /*case 2:
                if ( modes.length > 0 )
                {
                    //ret = TDBProperties.getKeepingPeriod() + "ms"; 
                    ret = MamboTimeFormater.formatTime( TDBProperties.getKeepingPeriod() );
                }
                break;*/
        }
        return ret;
    }

    public boolean toPaint ()
    {
        return true;
    }

    private Object getSecondColumnValue ( int rowIndex )
    {
        String ret = "";
        switch ( rowIndex )
        {
            case 0:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_CARACTERISTIC" );
                break;
            case 1:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_EXPORT" );
                break;
            case 2:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_KEEPING" );
                break;
        }
        return ret;
    }

    private Object getFirstColumnValue ( int rowIndex )
    {
        ArchivingConfigurationMode[] modes = null;
        if ( TDBProperties != null )
        {
            modes = TDBProperties.getModes();
        }
        if ( modes == null )
        {
            modes = new ArchivingConfigurationMode[ 0 ];
        }
        String ret = "";
        switch ( rowIndex )
        {
            case 0:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_MODE" );
                break;
            case 1:
                ret = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_ANY" );
                break;
        }
        return ret;
    }

}
