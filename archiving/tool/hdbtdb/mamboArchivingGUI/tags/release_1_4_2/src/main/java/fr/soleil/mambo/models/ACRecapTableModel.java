//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACRecapTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACRecapTableModel.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: ACRecapTableModel.java,v $
// Revision 1.6  2006/12/07 16:45:39  ounsy
// removed keeping period
//
// Revision 1.5  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.4  2006/08/09 10:35:27  ounsy
// Time formating in assessments + differences between database and archiving configuration highlighted
//
// Revision 1.3  2006/04/10 09:12:48  ounsy
// optimisation on loading an archiving assessment
//
// Revision 1.2  2005/12/15 11:43:45  ounsy
// minor changes
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

import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.formaters.MamboTimeFormater;
import fr.soleil.mambo.tools.Messages;

public class ACRecapTableModel extends DefaultTableModel
{
    public final static int titleOrGeneral = -1;
    public final static int modeP = 0;
    public final static int modeA = 1;
    public final static int modeR = 2;
    public final static int modeT = 3;
    public final static int modeD = 4;
    public final static int modeC = 5;
    public final static int modeE = 6;
    private int mode;
    private boolean historic;
    private ArchivingConfigurationAttribute attribute;
    private ArchivingConfigurationAttributeDBProperties DBProperties;
    
    //--CLA
    private Hashtable TDBmodesBuffer = new Hashtable ();
    private Hashtable HDBmodesBuffer = new Hashtable ();
    //--CLA

    public ACRecapTableModel ()
    {
        super();
     
        mode = titleOrGeneral;
        historic = false;
        attribute = null;
        DBProperties = null;
        
        HDBmodesBuffer = new Hashtable ();
        TDBmodesBuffer = new Hashtable ();
    }

    public ACRecapTableModel (boolean _historic)
    {
        super();
     
        mode = titleOrGeneral;
        historic = _historic;
        attribute = null;
        DBProperties = null;
        
        HDBmodesBuffer = new Hashtable ();
        TDBmodesBuffer = new Hashtable ();
    }

    public void load ( ArchivingConfigurationAttribute _attribute ,
                       boolean _historic ,
                       int _mode )
    {
        attribute = _attribute;
        historic = _historic;
        mode = _mode;
        if ( attribute != null )
        {
            ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
            if ( _historic )
            {
                DBProperties = attrProperties.getHDBProperties();
            }
            else
            {
                DBProperties = attrProperties.getTDBProperties();
            }
        }
        //fireTableStructureChanged();
        fireTableDataChanged();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        return 5;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    
    public int getRowCount ()
    {
        Mode _mode = null;
        /*try
        {
            _mode = DBManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , historic );
        }
        catch ( Exception e1 )
        {
            _mode = null;
        }*/
        
        if ( HDBmodesBuffer == null )
        {
            this.HDBmodesBuffer = new Hashtable ();   
        }
        
        if ( TDBmodesBuffer == null )
        {
            this.TDBmodesBuffer = new Hashtable ();   
        }
        
        Hashtable modesBuffer = historic ? this.HDBmodesBuffer : this.TDBmodesBuffer;
        
        if ( attribute != null && attribute.getCompleteName() != null )
        {
            _mode = (Mode) modesBuffer.get ( attribute.getCompleteName() );
            
            if ( _mode == null )
	        {
	            try 
	            {
	                _mode = ArchivingManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() , historic );
	            } 
	            catch (Exception e) 
	            {
	                e.printStackTrace();
	                return 0;
	            }
	            
	            if ( attribute.getCompleteName () != null && _mode != null )
	            {
	                modesBuffer.put ( attribute.getCompleteName() , _mode );
	            }
	        }
        }
        //-CLA
        
        return this.getRowCount2 ( _mode );
    }
    
    public int getRowCount2 ( Mode _mode )
    {
        switch ( this.mode )
        {
            case modeP:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_P ) != null )
                {
                    return 1;
                }
                else
                {
                    if ( _mode != null && _mode.getModeP() != null )
                    {
                        return 1;
                    }
                    return 0;
                }
            case modeA:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_A ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( _mode != null && _mode.getModeA() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeR:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_R ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( _mode != null && _mode.getModeR() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeT:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_T ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( _mode != null && _mode.getModeT() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeD:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_D ) != null )
                {
                    return 1;
                }
                else
                {
                    if ( _mode != null && _mode.getModeD() != null )
                    {
                        return 1;
                    }
                    return 0;
                }
            case modeC:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_C ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( _mode != null && _mode.getModeC() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeE:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_E ) != null )
                {
                    return 1;
                }
                else
                {
                    if ( _mode != null && _mode.getModeE() != null )
                    {
                        return 1;
                    }
                    return 0;
                }
            case titleOrGeneral:
                if ( attribute == null )
                {
                    return 1;
                }
                if ( !historic )
                {
                    if ( DBProperties != null )
                    {
                        if ( DBProperties.getModes().length > 0 )
                        {
                            return 1;
                        }
                        else
                        {
                            if ( _mode != null && _mode.getTdbSpec() != null )
                            {
                                return 1;
                            }
                        }
                    }
                    return 0;
                }
                else
                {
                    if ( DBProperties != null )
                    {
                        if ( DBProperties.getModes().length > 0 )
                        {
                            return 1;
                        }
                        else
                        {
                            if ( _mode != null && _mode.getTdbSpec() != null )
                            {
                                return 1;
                            }
                        }
                    }
                    return 0;
                }
            default:
                return 0;
        } // end switch (this.mode)
    }
    
    /*
ORIGINAL METHOD
     */
    /*public int getRowCount ()
    {
        Mode mode;
        try
        {
            mode = DBManagerFactory.getCurrentImpl().getArchivingMode( attribute.getCompleteName() ,
                                                                       historic );
        }
        catch ( Exception e1 )
        {
            mode = null;
        }
        switch ( this.mode )
        {
            case modeP:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_P ) != null )
                {
                    return 1;
                }
                else
                {
                    if ( mode != null && mode.getModeP() != null )
                    {
                        return 1;
                    }
                    return 0;
                }
            case modeA:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_A ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( mode != null && mode.getModeA() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeR:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_R ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( mode != null && mode.getModeR() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeT:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_T ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( mode != null && mode.getModeT() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeD:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_D ) != null )
                {
                    return 1;
                }
                else
                {
                    if ( mode != null && mode.getModeD() != null )
                    {
                        return 1;
                    }
                    return 0;
                }
            case modeC:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_C ) != null )
                {
                    return 3;
                }
                else
                {
                    if ( mode != null && mode.getModeC() != null )
                    {
                        return 3;
                    }
                    return 0;
                }
            case modeE:
                if ( DBProperties != null
                     && DBProperties.getMode( ArchivingConfigurationMode.TYPE_E ) != null )
                {
                    return 1;
                }
                else
                {
                    if ( mode != null && mode.getModeE() != null )
                    {
                        return 1;
                    }
                    return 0;
                }
            case titleOrGeneral:
                if ( attribute == null )
                {
                    return 1;
                }
                if ( !historic )
                {
                    if ( DBProperties != null )
                    {
                        if ( DBProperties.getModes().length > 0 )
                        {
                            return 2;
                        }
                        else
                        {
                            if ( mode != null && mode.getTdbSpec() != null )
                            {
                                return 2;
                            }
                        }
                    }
                    return 0;
                }
                else
                {
                    if ( DBProperties != null )
                    {
                        if ( DBProperties.getModes().length > 0 )
                        {
                            return 1;
                        }
                        else
                        {
                            if ( mode != null && mode.getTdbSpec() != null )
                            {
                                return 1;
                            }
                        }
                    }
                    return 0;
                }
            default:
                return 0;
        } // end switch (this.mode)
    } // end getRowCount()
*/
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
                return getThirdColumnValue( rowIndex );
            case 3:
                return getACColumnValue( rowIndex );
            case 4:
                return getDBColumnValue( rowIndex );
            default:
                return null;
        }
    }

    private Object getThirdColumnValue ( int rowIndex )
    {
        if ( attribute == null )
        {
            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_CARACTERISTIC" );
        }
        else
        {
            switch ( rowIndex )
            {
                case 0:
                    switch ( this.mode )
                    {
                        case titleOrGeneral:
                            if ( !historic )
                            {
                                return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_EXPORT" );
                            }
                            else
                            {
                                return "";
                            }
                        case modeP:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEP_PERIOD" );
                        case modeA:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEA_PERIOD" );
                        case modeR:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODER_PERIOD" );
                        case modeT:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODET_PERIOD" );
                        case modeD:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODED_PERIOD" );
                        case modeC:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_PERIOD" );
                        case modeE:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEE_ARCHIVED" );
                        default :
                            return "";
                    }
                case 1:
                    switch ( this.mode )
                    {
                        /*case titleOrGeneral:
                            if ( !historic )
                            {
                                return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_KEEPING" );
                            }
                            else
                            {
                                return "";
                            }*/
                        case modeA:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEA_UPPER" );
                        case modeR:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODER_UPPER" );
                        case modeT:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODET_UPPER" );
                        case modeC:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_RANGE" );
                        default :
                            return "";
                    }
                case 2:
                    switch ( this.mode )
                    {
                        case modeA:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEA_LOWER" );
                        case modeR:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODER_LOWER" );
                        case modeT:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODET_LOWER" );
                        case modeC:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_TYPE" );
                        default :
                            return "";
                    }
                default :
                    return "";
            } // end switch (rowIndex)
        } // end if (attribute == null) ... else
    } // end getThirdColumnValue(int rowIndex)

    private Object getDBColumnValue ( int rowIndex )
    {
        if ( attribute == null )
        {
            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_DB" );
        }
        else
        {
            IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
            Mode mode;
            try
            {
                mode = manager.getArchivingMode( attribute.getCompleteName() , historic );
            }
            catch ( Exception e )
            {
                mode = null;
            }
            if ( mode == null )
            {
                return "";
            }
            switch ( rowIndex )
            {
                case 0:
                    switch ( this.mode )
                    {
                        case titleOrGeneral:
                            if ( !historic )
                            {
                                if ( mode.getTdbSpec() != null )
                                {
                                    //return mode.getTdbSpec().getExportPeriod() + "ms";
                                    return MamboTimeFormater.formatTime( mode.getTdbSpec().getExportPeriod() );
                                }
                            }
                            return "";
                        case modeP:
                            if ( mode.getModeP() != null )
                            {
                                if (historic) {
                                    return mode.getModeP().getPeriod()/1000 + "s";
                                }
                                return mode.getModeP().getPeriod() + "ms";
                            }
                            return "";
                        case modeA:
                            if ( mode.getModeA() != null )
                            {
                                if (historic) {
                                    return mode.getModeA().getPeriod()/1000 + "s";
                                }
                                return mode.getModeA().getPeriod() + "ms";
                            }
                            return "";
                        case modeR:
                            if ( mode.getModeR() != null )
                            {
                                if (historic) {
                                    return mode.getModeR().getPeriod()/1000 + "s";
                                }
                                return mode.getModeR().getPeriod() + "ms";
                            }
                            return "";
                        case modeT:
                            if ( mode.getModeT() != null )
                            {
                                if (historic) {
                                    return mode.getModeT().getPeriod()/1000 + "s";
                                }
                                return mode.getModeT().getPeriod() + "ms";
                            }
                            return "";
                        case modeD:
                            if ( mode.getModeD() != null )
                            {
                                if (historic) {
                                    return mode.getModeD().getPeriod()/1000 + "s";
                                }
                                return mode.getModeD().getPeriod() + "ms";
                            }
                            return "";
                        case modeC:
                            if ( mode.getModeC() != null )
                            {
                                if (historic) {
                                    return mode.getModeC().getPeriod()/1000 + "s";
                                }
                                return mode.getModeC().getPeriod() + "ms";
                            }
                            return "";
                        case modeE:
                            if ( mode.getModeE() != null )
                            {
                                return new Boolean( true );
                            }
                            return new Boolean( false );
                        default :
                            return "";
                    }
                case 1:
                    switch ( this.mode )
                    {
                        /*case titleOrGeneral:
                            if ( !historic )
                            {
                                if ( mode.getTdbSpec() != null )
                                {
                                    //return mode.getTdbSpec().getKeepingPeriod() + "ms";
                                    return MamboTimeFormater.formatTime( mode.getTdbSpec().getKeepingPeriod() );
                                }
                            }
                            return "";*/
                        case modeA:
                            if ( mode.getModeA() != null )
                            {
                                return mode.getModeA().getValSup() + "";
                            }
                            return "";
                        case modeR:
                            if ( mode.getModeR() != null )
                            {
                                return mode.getModeR().getPercentSup() + "%";
                            }
                            return "";
                        case modeT:
                            if ( mode.getModeT() != null )
                            {
                                return mode.getModeT().getThresholdSup() + "";
                            }
                            return "";
                        case modeC:
                            if ( mode.getModeC() != null )
                            {
                                return mode.getModeC().getRange() + "";
                            }
                            return "";
                        default :
                            return "";
                    }
                case 2:
                    switch ( this.mode )
                    {
                        case modeA:
                            if ( mode.getModeA() != null )
                            {
                                return mode.getModeA().getValInf() + "";
                            }
                            return "";
                        case modeR:
                            if ( mode.getModeR() != null )
                            {
                                return mode.getModeR().getPercentInf() + "%";
                            }
                            return "";
                        case modeT:
                            if ( mode.getModeT() != null )
                            {
                                return mode.getModeT().getThresholdInf() + "";
                            }
                            return "";
                        case modeC:
                            if ( mode.getModeC() != null )
                            {
                                return mode.getModeC().getTypeCalcul() + "";
                            }
                            return "";
                        default :
                            return "";
                    }
                default :
                    return "";
            } // end switch (rowIndex)
        } // end if (attribute == null) ... else
    } // end getDBColumnValue(int rowIndex)

    private Object getACColumnValue ( int rowIndex )
    {
        if ( attribute == null )
        {
            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_AC" );
        }
        else
        {
            if ( DBProperties == null )
            {
                return "";
            }
            ArchivingConfigurationMode[] modes = null;
            ;
            modes = DBProperties.getModes();
            if ( modes == null )
            {
                modes = new ArchivingConfigurationMode[ 0 ];
            }
            switch ( rowIndex )
            {
                case 0:
                    switch ( this.mode )
                    {
                        case titleOrGeneral:
                            if ( !historic )
                            {
                                if ( modes.length > 0 )
                                {
                                    //return ( ( ArchivingConfigurationAttributeTDBProperties ) DBProperties ).getExportPeriod() + "ms";
                                    return MamboTimeFormater.formatTime( ( ( ArchivingConfigurationAttributeTDBProperties ) DBProperties ).getExportPeriod() );
                                }
                            }
                            return "";
                        case modeP:
                            ArchivingConfigurationMode ACPMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_P );
                            if ( ACPMode != null )
                            {
                                if (DBProperties instanceof ArchivingConfigurationAttributeTDBProperties) {
                                    return ACPMode.getMode().getModeP().getPeriod() + "ms";
                                }
                                return ACPMode.getMode().getModeP().getPeriod()/1000 + "s";
                            }
                            return "";
                        case modeA:
                            ArchivingConfigurationMode ACAMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_A );
                            if ( ACAMode != null )
                            {
                                if (DBProperties instanceof ArchivingConfigurationAttributeTDBProperties) {
                                    return ACAMode.getMode().getModeA().getPeriod() + "ms";
                                }
                                return ACAMode.getMode().getModeA().getPeriod()/1000 + "s";
                            }
                            return "";
                        case modeR:
                            ArchivingConfigurationMode ACRMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_R );
                            if ( ACRMode != null )
                            {
                                if (DBProperties instanceof ArchivingConfigurationAttributeTDBProperties) {
                                    return ACRMode.getMode().getModeR().getPeriod() + "ms";
                                }
                                return ACRMode.getMode().getModeR().getPeriod()/1000 + "s";
                            }
                            return "";
                        case modeT:
                            ArchivingConfigurationMode ACTMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_T );
                            if ( ACTMode != null )
                            {
                                if (DBProperties instanceof ArchivingConfigurationAttributeTDBProperties) {
                                    return ACTMode.getMode().getModeT().getPeriod() + "ms";
                                }
                                return ACTMode.getMode().getModeT().getPeriod()/1000 + "s";
                            }
                            return "";
                        case modeD:
                            ArchivingConfigurationMode ACDMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_D );
                            if ( ACDMode != null )
                            {
                                if (DBProperties instanceof ArchivingConfigurationAttributeTDBProperties) {
                                    return ACDMode.getMode().getModeD().getPeriod() + "ms";
                                }
                                return ACDMode.getMode().getModeD().getPeriod()/1000 + "s";
                            }
                            return "";
                        case modeC:
                            ArchivingConfigurationMode ACCMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_C );
                            if ( ACCMode != null )
                            {
                                if (DBProperties instanceof ArchivingConfigurationAttributeTDBProperties) {
                                    return ACCMode.getMode().getModeC().getPeriod() + "ms";
                                }
                                return ACCMode.getMode().getModeC().getPeriod()/1000 + "s";
                            }
                            return "";
                        case modeE:
                            ArchivingConfigurationMode ACEMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_E );
                            if ( ACEMode != null )
                            {
                                return new Boolean( true );
                            }
                            return new Boolean( false );
                        default :
                            return "";
                    }
                case 1:
                    switch ( this.mode )
                    {
                        /*case titleOrGeneral:
                            if ( !historic )
                            {
                                if ( modes.length > 0 )
                                {
                                    //return ( ( ArchivingConfigurationAttributeTDBProperties ) DBProperties ).getKeepingPeriod() + "ms";
                                    return MamboTimeFormater.formatTime( ( ( ArchivingConfigurationAttributeTDBProperties ) DBProperties ).getKeepingPeriod() );
                                }
                            }
                            return "";*/
                        case modeA:
                            ArchivingConfigurationMode ACAMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_A );
                            if ( ACAMode != null )
                            {
                                return ACAMode.getMode().getModeA().getValSup() + "";
                            }
                            return "";
                        case modeR:
                            ArchivingConfigurationMode ACRMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_R );
                            if ( ACRMode != null )
                            {
                                return ACRMode.getMode().getModeR().getPercentSup() + "%";
                            }
                            return "";
                        case modeT:
                            ArchivingConfigurationMode ACTMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_T );
                            if ( ACTMode != null )
                            {
                                return ACTMode.getMode().getModeT().getThresholdSup() + "";
                            }
                            return "";
                        case modeC:
                            ArchivingConfigurationMode ACCMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_C );
                            if ( ACCMode != null )
                            {
                                return ACCMode.getMode().getModeC().getRange() + "";
                            }
                            return "";
                        default :
                            return "";
                    }
                case 2:
                    switch ( this.mode )
                    {
                        case modeA:
                            ArchivingConfigurationMode ACAMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_A );
                            if ( ACAMode != null )
                            {
                                return ACAMode.getMode().getModeA().getValInf() + "";
                            }
                            return "";
                        case modeR:
                            ArchivingConfigurationMode ACRMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_R );
                            if ( ACRMode != null )
                            {
                                return ACRMode.getMode().getModeR().getPercentInf() + "%";
                            }
                            return "";
                        case modeT:
                            ArchivingConfigurationMode ACTMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_T );
                            if ( ACTMode != null )
                            {
                                return ACTMode.getMode().getModeT().getThresholdInf() + "";
                            }
                            return "";
                        case modeC:
                            ArchivingConfigurationMode ACCMode = DBProperties.getMode( ArchivingConfigurationMode.TYPE_C );
                            if ( ACCMode != null )
                            {
                                return ACCMode.getMode().getModeC().getTypeCalcul() + "";
                            }
                            return "";
                        default :
                            return "";
                    }
                default :
                    return "";
            } // end switch (rowIndex)
        } // end if (attribute == null) ... else
    } // end getACColumnValue(int rowIndex)

    private Object getSecondColumnValue ( int rowIndex )
    {
        if ( attribute == null )
        {
            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_MODE" );
        }
        else
        {
            switch ( rowIndex )
            {
                case 0:
                    switch ( this.mode )
                    {
                        case titleOrGeneral:
                            if ( !historic )
                            {
                                return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_ANY" );
                            }
                            else
                            {
                                return "";
                            }
                        case modeP:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEP_NAME" );
                        case modeA:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEA_NAME" );
                        case modeR:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODER_NAME" );
                        case modeT:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODET_NAME" );
                        case modeD:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODED_NAME" );
                        case modeC:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEC_NAME" );
                        case modeE:
                            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_MODEE_NAME" );
                        default :
                            return "";
                    }
                default :
                    return "";
            }
        }
    }

    private Object getFirstColumnValue ( int rowIndex )
    {
        if ( attribute == null )
        {
            return Messages.getMessage( "ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_NAME" );
        }
        else
        {
            switch ( rowIndex )
            {
                case 0:
                    switch ( this.mode )
                    {
                        case titleOrGeneral:
                            return attribute.getCompleteName();
                        default :
                            return "";
                    }
                default :
                    return "";
            }
        }
    }

}
