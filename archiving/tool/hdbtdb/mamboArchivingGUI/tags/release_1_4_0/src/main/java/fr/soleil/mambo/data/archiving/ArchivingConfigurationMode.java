//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationMode.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationMode.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ArchivingConfigurationMode.java,v $
// Revision 1.4  2007/02/01 14:16:29  pierrejoseph
// XmlHelper reorg
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.archiving;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.TdbSpec;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ArchivingConfigurationMode
{
    public static ArchivingConfigurationMode currentMode;

    private Mode mode;
    private int type = -1;
    private boolean isTDB = false;

    //General tags START
    /*private static final String HDB_MODE_XML_TAG = "modeHDB";
    private static final String TDB_MODE_XML_TAG = "modeTDB";*/
    public static final String XML_TAG = "mode";
    public static final String TYPE_PROPERTY_XML_TAG = "type";
    
    //private static final String TDB_SPEC_XML_TAG = "TDBspec";
    /*private static final String TDB_SPEC_EXPORT_PERIOD_XML_TAG = "exportPeriod";
    private static final String TDB_SPEC_KEEPING_PERIOD_XML_TAG = "keepingPeriod";*/
    //General tags END
    
    //modeRoot tags START
    public static final String PERIOD_PROPERTY_XML_TAG = "period";
    //modeRoot tags END
    
    //modeAbsolu tags START
    public static final String VAL_INF_PROPERTY_XML_TAG = "val_inf";
    public static final String VAL_SUP_PROPERTY_XML_TAG = "val_sup";
    //modeAbsolu tags END
    
    //modeCalcul tags START
    public static final String RANGE_PROPERTY_XML_TAG = "range";
    public static final String CALC_TYPE_PROPERTY_XML_TAG = "calcul_type";
    //modeCalcul tags END
    
    //modeDifference tags START
    //modeDifference tags END
    
    //modeRoot tags START
    public static final String DESCRIPTION_PROPERTY_XML_TAG = "description";
    //modeRoot tags END
    
    //modePeriode tags START
    //modePeriode tags END
    
    //modeRelatif tags START
    public static final String PERCENT_INF_PROPERTY_XML_TAG = "percent_inf";
    public static final String PERCENT_SUP_PROPERTY_XML_TAG = "percent_sup";
    //modeRelatif tags END
    
    //modeSeuil tags START
    public static final String THRESHOLD_INF_PROPERTY_XML_TAG = "threshold_inf";
    public static final String THRESHOLD_SUP_PROPERTY_XML_TAG = "threshold_sup";
    //modeSeuil tags END
    
    public static final int TYPE_HDB = 0;
    public static final int TYPE_TDB = 1;

    public static final int TYPE_A = 0;
    public static final int TYPE_C = 1;
    public static final int TYPE_D = 2;
    public static final int TYPE_E = 3;
    public static final int TYPE_P = 4;
    public static final int TYPE_R = 5;
    public static final int TYPE_T = 6;

    public static String TYPE_A_NAME = "Absolute";
    public static String TYPE_C_NAME = "Calculation";
    public static String TYPE_D_NAME = "Difference";
    public static String TYPE_E_NAME = "External";
    public static String TYPE_P_NAME = "Periodical";
    public static String TYPE_R_NAME = "Relative";
    public static String TYPE_T_NAME = "Threshold";

    public static final int PERIOD_TOO_LOW = 100;
    public static final int MODE_A_VAL_INF_BIGGER_THAN_VAL_SUP = 101;
    public static final int MODE_R_PERCENT_INF_BIGGER_THAN_PERCENT_SUP = 102;

    private static final int PERIOD_MIN = 1000;

    public static String getLabel ( int _type )
    {
        String ret = null;

        switch ( _type )
        {
            case TYPE_A:
                ret = TYPE_A_NAME;
                break;

            case TYPE_C:
                ret = TYPE_C_NAME;
                break;

            case TYPE_D:
                ret = TYPE_D_NAME;
                break;

            case TYPE_E:
                ret = TYPE_E_NAME;
                break;

            case TYPE_P:
                ret = TYPE_P_NAME;
                break;

            case TYPE_R:
                ret = TYPE_R_NAME;
                break;

            case TYPE_T:
                ret = TYPE_T_NAME;
                break;

            default:
                throw new IllegalStateException();
        }

        return ret;
    }

    public static ArchivingConfigurationMode[] buildModesList ( Mode _mode )
    {
        ModeAbsolu modeA = _mode.getModeA();
        ModeRelatif modeR = _mode.getModeR();
        ModeSeuil modeT = _mode.getModeT();
        ModePeriode modeP = _mode.getModeP();
        ModeDifference modeD = _mode.getModeD();
        ModeExterne modeE = _mode.getModeE();
        ModeCalcul modeC = _mode.getModeC();

        int size = 0;
        if ( modeA != null ) size++;
        if ( modeR != null ) size++;
        if ( modeT != null ) size++;
        if ( modeD != null ) size++;
        if ( modeE != null ) size++;
        if ( modeC != null ) size++;
        if ( modeP != null ) size++;

        if ( size == 0 )
        {
            return null;
        }

        ArchivingConfigurationMode[] ret = new ArchivingConfigurationMode[ size ];
        int i = 0;
        if ( modeA != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeA( modeA );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }
        if ( modeR != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeR( modeR );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }
        if ( modeT != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeT( modeT );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }
        if ( modeD != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeD( modeD );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }
        if ( modeE != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeE( modeE );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }
        if ( modeC != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeC( modeC );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }
        if ( modeP != null )
        {
            Mode mode1 = new Mode();
            mode1.setModeP( modeP );
            ret[ i ] = new ArchivingConfigurationMode( mode1 );
            i++;
        }

        return ret;
    }

    public ArchivingConfigurationMode ( Mode _mode )
    {
        this.mode = _mode;

        if ( _mode.getModeA() != null )
        {
            type = TYPE_A;
        }
        else if ( _mode.getModeC() != null )
        {
            type = TYPE_C;
        }
        else if ( _mode.getModeD() != null )
        {
            type = TYPE_D;
        }
        else if ( _mode.getModeE() != null )
        {
            type = TYPE_E;
        }
        else if ( _mode.getModeP() != null )
        {
            type = TYPE_P;
        }
        else if ( _mode.getModeR() != null )
        {
            type = TYPE_R;
        }
        else if ( _mode.getModeT() != null )
        {
            type = TYPE_T;
        }

        if ( _mode.getTdbSpec() != null )
        {
            isTDB = true;
        }

        //initLabels ();

    }

    public String toString ()
    {
        String ret = "";
        /*String modeTag = isTDB ? TDB_MODE_XML_TAG : HDB_MODE_XML_TAG;
        XMLLine modeLine = new XMLLine ( modeTag , XMLLine.EMPTY_TAG_CATEGORY );*/

        XMLLine modeLine = new XMLLine( XML_TAG , XMLLine.EMPTY_TAG_CATEGORY );

        String type_s = String.valueOf( type );
        modeLine.setAttribute( TYPE_PROPERTY_XML_TAG , type_s );

        switch ( this.type )
        {
            case TYPE_A:
                ModeAbsolu modeA = this.mode.getModeA();
                modeLine.setAttribute( VAL_INF_PROPERTY_XML_TAG , String.valueOf( modeA.getValInf() ) );
                modeLine.setAttribute( VAL_SUP_PROPERTY_XML_TAG , String.valueOf( modeA.getValSup() ) );
                modeLine.setAttribute( PERIOD_PROPERTY_XML_TAG , String.valueOf( modeA.getPeriod() ) );
                break;

            case TYPE_C:
                ModeCalcul modeC = this.mode.getModeC();
                modeLine.setAttribute( RANGE_PROPERTY_XML_TAG , String.valueOf( modeC.getRange() ) );
                modeLine.setAttribute( CALC_TYPE_PROPERTY_XML_TAG , String.valueOf( modeC.getTypeCalcul() ) );
                modeLine.setAttribute( PERIOD_PROPERTY_XML_TAG , String.valueOf( modeC.getPeriod() ) );
                break;

            case TYPE_D:
                ModeDifference modeD = this.mode.getModeD();
                modeLine.setAttribute( PERIOD_PROPERTY_XML_TAG , String.valueOf( modeD.getPeriod() ) );
                break;

            case TYPE_E:
                ModeExterne modeE = this.mode.getModeE();
                modeLine.setAttribute( DESCRIPTION_PROPERTY_XML_TAG , String.valueOf( modeE.getDescription() ) );
                //modeLine.setAttribute ( PERIOD_PROPERTY_XML_TAG , String.valueOf ( modeE.getPeriod () ) );
                break;

            case TYPE_P:
                ModePeriode modeP = this.mode.getModeP();
                //System.out.println ( "ArchivingConfigurationMode/toString/TYPE_P/modeP.getPeriod ()/"+modeP.getPeriod ()+"/" );
                modeLine.setAttribute( PERIOD_PROPERTY_XML_TAG , String.valueOf( modeP.getPeriod() ) );
                break;

            case TYPE_R:
                ModeRelatif modeR = this.mode.getModeR();
                modeLine.setAttribute( PERCENT_INF_PROPERTY_XML_TAG , String.valueOf( modeR.getPercentInf() ) );
                modeLine.setAttribute( PERCENT_SUP_PROPERTY_XML_TAG , String.valueOf( modeR.getPercentSup() ) );
                modeLine.setAttribute( PERIOD_PROPERTY_XML_TAG , String.valueOf( modeR.getPeriod() ) );
                break;

            case TYPE_T:
                ModeSeuil modeT = this.mode.getModeT();
                modeLine.setAttribute( THRESHOLD_INF_PROPERTY_XML_TAG , String.valueOf( modeT.getThresholdInf() ) );
                modeLine.setAttribute( THRESHOLD_SUP_PROPERTY_XML_TAG , String.valueOf( modeT.getThresholdSup() ) );
                modeLine.setAttribute( PERIOD_PROPERTY_XML_TAG , String.valueOf( modeT.getPeriod() ) );
                break;
        }

        /*if ( this.isTDB )
        {
            long exportPeriod = mode.getTdbSpec().getExportPeriod ();
            long keepingPeriod = mode.getTdbSpec().getKeepingPeriod ();

            modeLine.setAttribute ( TDB_SPEC_EXPORT_PERIOD_XML_TAG , String.valueOf ( exportPeriod ) );
            modeLine.setAttribute ( TDB_SPEC_KEEPING_PERIOD_XML_TAG , String.valueOf ( keepingPeriod ) );
        }*/

        ret += modeLine.toString();

        return ret;
    }

    /**
     * @return Returns the currentMode.
     */
    public static ArchivingConfigurationMode getCurrentMode ()
    {
        return currentMode;
    }

    /**
     * @param currentMode The currentMode to set.
     */
    public static void setCurrentMode ( ArchivingConfigurationMode currentMode )
    {
        ArchivingConfigurationMode.currentMode = currentMode;
    }

    /**
     * @return Returns the isTDB.
     */
    public boolean isTDB ()
    {
        return isTDB;
    }

    /**
     * @param isTDB The isTDB to set.
     */
    public void setTDB ( boolean isTDB )
    {
        this.isTDB = isTDB;
    }

    /**
     * @return Returns the mode.
     */
    public Mode getMode ()
    {
        return mode;
    }

    /**
     * @param mode The mode to set.
     */
    public void setMode ( Mode mode )
    {
        this.mode = mode;
    }

    /**
     * @return Returns the type.
     */
    public int getType ()
    {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType ( int type )
    {
        this.type = type;
    }

    /**
     * @param modes
     * @return 27 juil. 2005
     */
    public static Mode buildCompleteHDBMode ( ArchivingConfigurationMode[] modes )
    {
        return buildCompleteDBMode( modes );
    }

    /**
     * @param modes
     * @return 27 juil. 2005
     */
    public static Mode buildCompleteTDBMode ( ArchivingConfigurationMode[] modes , long exportPeriod , long keepingPeriod )
    {
        Mode ret = buildCompleteDBMode( modes );

        TdbSpec tdbSpec = new TdbSpec( exportPeriod , keepingPeriod );
        ret.setTdbSpec( tdbSpec );

        return ret;
    }

    /**
     * @param modes
     * @return 27 juil. 2005
     */
    public static Mode buildCompleteDBMode ( ArchivingConfigurationMode[] modes )
    {
        Mode ret = new Mode();

        for ( int i = 0 ; i < modes.length ; i++ )
        {
            ArchivingConfigurationMode next = modes[ i ];
            int type = next.getType();
            switch ( type )
            {
                case TYPE_A:
                    ret.setModeA( next.getMode().getModeA() );
                    break;

                case TYPE_C:
                    ret.setModeC( next.getMode().getModeC() );
                    break;

                case TYPE_D:
                    ret.setModeD( next.getMode().getModeD() );
                    break;

                case TYPE_E:
                    ret.setModeE( next.getMode().getModeE() );
                    break;

                case TYPE_P:
                    ret.setModeP( next.getMode().getModeP() );
                    break;

                case TYPE_R:
                    ret.setModeR( next.getMode().getModeR() );
                    break;

                case TYPE_T:
                    ret.setModeT( next.getMode().getModeT() );
                    break;
            }
        }

        return ret;
    }

    /**
     * 9 sept. 2005
     *
     * @throws ArchivingConfigurationException
     *
     */
    public void controlValues () throws ArchivingConfigurationException
    {
        switch ( this.type )
        {
            case TYPE_A:
                ModeAbsolu modeA = this.mode.getModeA();
                if ( modeA.getValInf() >= modeA.getValSup() )
                {
                    throw new ArchivingConfigurationException( null , MODE_A_VAL_INF_BIGGER_THAN_VAL_SUP );
                }
                if ( modeA.getPeriod() < PERIOD_MIN )
                {
                    throw new ArchivingConfigurationException( null , PERIOD_TOO_LOW );
                }
                break;

            case TYPE_C:
                ModeCalcul modeC = this.mode.getModeC();
                if ( modeC.getPeriod() < PERIOD_MIN )
                {
                    throw new ArchivingConfigurationException( null , PERIOD_TOO_LOW );
                }
                break;
            case TYPE_D:
                ModeDifference modeD = this.mode.getModeD();
                if ( modeD.getPeriod() < PERIOD_MIN )
                {
                    throw new ArchivingConfigurationException( null , PERIOD_TOO_LOW );
                }
                break;

            case TYPE_E:
                //ModeExterne modeE = this.mode.getModeE();
                break;

            case TYPE_P:
                ModePeriode modeP = this.mode.getModeP();
                if ( modeP.getPeriod() < PERIOD_MIN )
                {
                    throw new ArchivingConfigurationException( null , PERIOD_TOO_LOW );
                }
                break;

            case TYPE_R:
                ModeRelatif modeR = this.mode.getModeR();
                if ( modeR.getPercentInf() >= modeR.getPercentSup() )
                {
                    throw new ArchivingConfigurationException( null , MODE_R_PERCENT_INF_BIGGER_THAN_PERCENT_SUP );
                }
                if ( modeR.getPeriod() < PERIOD_MIN )
                {
                    throw new ArchivingConfigurationException( null , PERIOD_TOO_LOW );
                }
                break;

            case TYPE_T:
                ModeSeuil modeT = this.mode.getModeT();
                //modeT.

                if ( modeT.getThresholdInf() >= modeT.getThresholdSup() )
                {
                    throw new ArchivingConfigurationException( null , MODE_R_PERCENT_INF_BIGGER_THAN_PERCENT_SUP );
                }
                if ( modeT.getPeriod() < PERIOD_MIN )
                {
                    throw new ArchivingConfigurationException( null , PERIOD_TOO_LOW );
                }
                break;
        }

    }
}
