//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeRealDetailTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeRealDetailTableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeRealDetailTableModel.java,v $
// Revision 1.2  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
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
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeRealDetailTableModel extends DefaultTableModel
{
    private String[] columnsNames;
    private String isArchived;
    private String modes;

    private static ACAttributeRealDetailTableModel instance;
    private ArchivingConfigurationAttribute attribute;
    private ArchivingConfigurationAttributeHDBProperties HDBProperties;
    private ArchivingConfigurationAttributeTDBProperties TDBProperties;

    /**
     * @param snapshot
     * @return 8 juil. 2005
     */
    public static ACAttributeRealDetailTableModel getInstance ()
    {
        if ( instance == null )
        {
            //instance = new ACAttributeDetailTableModel ();
            String msgCol1 = "A";
            String msgCol2 = "B";
            String msgTDB = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TDB_LABEL" );
            String msgHDB = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_HDB_LABEL" );

            /*String isArchived1 = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_IS_ARCHIVED_LABEL" );
            String modes1 = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_MODE_LABEL" );*/
            String[] _columnNames = new String[ 4 ];

            _columnNames[ 0 ] = msgCol1;
            _columnNames[ 1 ] = msgCol2;
            _columnNames[ 2 ] = msgTDB;
            _columnNames[ 3 ] = msgHDB;


            instance = new ACAttributeRealDetailTableModel( _columnNames , 8 );
        }

        return instance;
    }

    private ACAttributeRealDetailTableModel ( String[] _columnNames , int rowCount )
    {
        super( _columnNames , rowCount );
        this.columnsNames = _columnNames;
        isArchived = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_IS_ARCHIVED_LABEL" );
        modes = Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODE_LABEL" );
        //System.out.println ( "ACAttributeDetailTableModel/new/msgCol1/"+msgCol1+"/msgCol2/"+msgCol2+"/msgTDB/"+msgTDB+"/msgHDB/"+msgHDB+"/" );
    }
   
    /**
     * 
     */
    /*private ACAttributeDetailTableModel ()
    {
        String msgCol1 = "A";
        String msgCol2 = "B";
        String msgTDB = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_TDB_LABEL" );
        String msgHDB = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_HDB_LABEL" );
        
        isArchived = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_IS_ARCHIVED_LABEL" );
        modes = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_MODE_LABEL" );
        
        columnsNames = new String [ this.getColumnCount () ];
        columnsNames [ 0 ] = msgCol1;
        columnsNames [ 1 ] = msgCol2;
        columnsNames [ 2 ] = msgTDB;
        columnsNames [ 3 ] = msgHDB;
        System.out.println ( "ACAttributeDetailTableModel/new/msgCol1/"+msgCol1+"/msgCol2/"+msgCol2+"/msgTDB/"+msgTDB+"/msgHDB/"+msgHDB+"/" );
    }*/
    
    /**
     *  17 juin 2005
     * @throws SnapshotingException
     */
    /**
     * @param snapshot 8 juil. 2005
     */
    public void load ( ArchivingConfigurationAttribute _attribute )
    {
        this.attribute = _attribute;

        ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
        HDBProperties = attrProperties.getHDBProperties();
        TDBProperties = attrProperties.getTDBProperties();

        /*System.out.println("--------ACAttributeDetailTableModel/load-----------");
        System.out.println(attrProperties.toString());
        System.out.println("--------ACAttributeDetailTableModel/load-----------");*/

        this.fireTableDataChanged();
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
        return 9;
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
                return getTDBColumnValue( rowIndex );

            case 3:
                return getHDBColumnValue( rowIndex );

            default:
                return null;
                //we have a bug
        }
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getHDBColumnValue ( int rowIndex )
    {
        Boolean ret = new Boolean( false );

        if ( rowIndex == 0 )
        {
            return columnsNames[ 3 ];
        }
        else if ( rowIndex == 1 )
        {
            if ( attribute == null )
            {
                return ret;
            }

            try
            {
                ret = new Boolean( attribute.isArchived( true ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        else
        {
            if ( HDBProperties == null || HDBProperties.isEmpty() )
            {
                return ret;
            }
            IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
            try
            {
                Mode mode = manager.getArchivingMode( attribute.getCompleteName() , true );
                ret = this.hasMode( mode , rowIndex );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }

        return ret;
    }

    /**
     * @param mode
     * @param rowIndex
     * @return
     */
    private Boolean hasMode ( Mode mode , int rowIndex )
    {
        Boolean _ret = new Boolean( false );

        switch ( rowIndex )
        {
            case 2:
                _ret = new Boolean( mode.getModeP() != null );
                break;

            case 3:
                _ret = new Boolean( mode.getModeA() != null );
                break;

            case 4:
                _ret = new Boolean( mode.getModeR() != null );
                break;

            case 5:
                _ret = new Boolean( mode.getModeT() != null );
                break;

            case 6:
                _ret = new Boolean( mode.getModeD() != null );
                break;

            case 7:
                _ret = new Boolean( mode.getModeC() != null );
                break;

            case 8:
                _ret = new Boolean( mode.getModeA() != null );
                break;
        }
        return _ret;
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getTDBColumnValue ( int rowIndex )
    {
        Boolean ret = new Boolean( false );
        if ( rowIndex == 0 )
        {
            return columnsNames[ 2 ];
        }
        else if ( rowIndex == 1 )
        {
            if ( attribute == null )
            {
                return ret;
            }

            try
            {
                ret = new Boolean( attribute.isArchived( false ) );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        else
        {
            if ( TDBProperties == null || TDBProperties.isEmpty() )
            {
                return ret;
            }
            IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
            try
            {
                Mode mode = manager.getArchivingMode( attribute.getCompleteName() , false );
                ret = this.hasMode( mode , rowIndex );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getSecondColumnValue ( int rowIndex )
    {
        String ret = "";

        switch ( rowIndex )
        {
            case 2:
                ret = ArchivingConfigurationMode.TYPE_P_NAME;
                break;

            case 3:
                ret = ArchivingConfigurationMode.TYPE_A_NAME;
                break;

            case 4:
                ret = ArchivingConfigurationMode.TYPE_R_NAME;
                break;

            case 5:
                ret = ArchivingConfigurationMode.TYPE_T_NAME;
                break;

            case 6:
                ret = ArchivingConfigurationMode.TYPE_D_NAME;
                break;

            case 7:
                ret = ArchivingConfigurationMode.TYPE_C_NAME;
                break;

            case 8:
                ret = ArchivingConfigurationMode.TYPE_E_NAME;
                break;
        }

        return ret;
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getFirstColumnValue ( int rowIndex )
    {
        String ret = "";

        switch ( rowIndex )
        {
            case 1:
                ret = isArchived;
                break;

            case 2:
                ret = modes;
                break;
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName ( int columnIndex )
    {
        //System.out.println ("getColumnName" );
        //System.out.println ( "ACAttributeDetailTableModel/columnIndex/"+columnIndex+"/columnsNames [ columnIndex ]/"+columnsNames [ columnIndex ]+"/" );
        return columnsNames[ columnIndex ];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setColumnName(int, String)
     */
    public void setColumnName ( int columnIndex , String columnName )
    {
        if ( columnIndex < getColumnCount() )
        {
            columnsNames[ columnIndex ] = columnName;
        }
    }

}
