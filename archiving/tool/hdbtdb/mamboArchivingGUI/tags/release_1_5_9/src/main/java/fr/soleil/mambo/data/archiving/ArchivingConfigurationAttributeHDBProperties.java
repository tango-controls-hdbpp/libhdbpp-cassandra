//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttributeHDBProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationAttributeHDBProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: ArchivingConfigurationAttributeHDBProperties.java,v $
// Revision 1.6  2007/02/01 14:16:29  pierrejoseph
// XmlHelper reorg
//
// Revision 1.5  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.4  2006/06/15 15:39:11  ounsy
// added support for dedicate archivers definition
//
// Revision 1.3  2006/02/24 12:21:19  ounsy
// small modifications
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
// no message
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

import java.util.Vector;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;


public class ArchivingConfigurationAttributeHDBProperties extends ArchivingConfigurationAttributeDBProperties
{
    public static final String XML_TAG = "HDBModes";
    private int defaultPeriod = -1;

    public ArchivingConfigurationAttributeHDBProperties ()
    {

    }

    public String toString ()
    {
        String ret = "";

        XMLLine openingLine = new XMLLine( XML_TAG , XMLLine.OPENING_TAG_CATEGORY );
        openingLine.setAttribute ( ArchivingConfigurationAttributeDBProperties.DEDICATED_ARCHIVER_PROPERTY_XML_TAG , super.getDedicatedArchiver () );
        XMLLine closingLine = new XMLLine( XML_TAG , XMLLine.CLOSING_TAG_CATEGORY );

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        ret += super.toString();
        ret += GUIUtilities.CRLF;

        ret += closingLine.toString();

        return ret;
    }

    /**
     * 25 juil. 2005
     */
    public void push ()
    {
        super.push ();
        
        Vector modesTypes = super.getModesTypes();
    
        AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance ();
        panel.setSelectedModes( true , modesTypes );

        //MODE P START
        ArchivingConfigurationMode periodicalMode = super.getMode( ArchivingConfigurationMode.TYPE_P );
        if ( periodicalMode != null && periodicalMode.getMode() != null && periodicalMode.getMode().getModeP() != null )
        {
            int period = periodicalMode.getMode().getModeP().getPeriod();
            panel.setHDBPeriod( period );
        }
        else if ( defaultPeriod != -1 )
        {
            panel.setHDBPeriod( defaultPeriod );
        }
        else
        {
            panel.setHDBPeriod( Integer.parseInt( AttributesPropertiesPanel.DEFAULT_HDB_PERIOD_VALUE ) );
        }
        //MODE P END

        //MODE A START
        ArchivingConfigurationMode absoluteMode = super.getMode( ArchivingConfigurationMode.TYPE_A );
        if ( absoluteMode != null && absoluteMode.getMode() != null && absoluteMode.getMode().getModeA() != null )
        {
            ModeAbsolu modeA = absoluteMode.getMode().getModeA();

            int period = modeA.getPeriod();
            double lower = modeA.getValInf();
            double upper = modeA.getValSup();

            panel.setHDBAbsolutePeriod( period );
            panel.setHDBAbsoluteLower( String.valueOf( lower ) );
            panel.setHDBAbsoluteUpper( String.valueOf( upper ) );
        }
        else
        {
            panel.setHDBAbsolutePeriod( -1 );
            panel.setHDBAbsoluteLower( "" );
            panel.setHDBAbsoluteUpper( "" );
        }
        //MODE A END

        //MODE R START
        ArchivingConfigurationMode relativeMode = super.getMode( ArchivingConfigurationMode.TYPE_R );
        if ( relativeMode != null && relativeMode.getMode() != null && relativeMode.getMode().getModeR() != null )
        {
            ModeRelatif modeR = relativeMode.getMode().getModeR();

            int period = modeR.getPeriod();
            double lower = modeR.getPercentInf();
            double upper = modeR.getPercentSup();

            panel.setHDBRelativePeriod( period );
            panel.setHDBRelativeLower( String.valueOf( lower ) );
            panel.setHDBRelativeUpper( String.valueOf( upper ) );
        }
        else
        {
            panel.setHDBRelativePeriod( -1 );
            panel.setHDBRelativeLower( "" );
            panel.setHDBRelativeUpper( "" );
        }
        //MODE R END

        //MODE T START
        ArchivingConfigurationMode thresholdMode = super.getMode( ArchivingConfigurationMode.TYPE_T );
        if ( thresholdMode != null && thresholdMode.getMode() != null && thresholdMode.getMode().getModeT() != null )
        {
            ModeSeuil modeT = thresholdMode.getMode().getModeT();

            int period = modeT.getPeriod();
            double lower = modeT.getThresholdInf();
            double upper = modeT.getThresholdSup();

            panel.setHDBThresholdPeriod( period );
            panel.setHDBThresholdLower( String.valueOf( lower ) );
            panel.setHDBThresholdUpper( String.valueOf( upper ) );
        }
        else
        {
            panel.setHDBThresholdPeriod( -1 );
            panel.setHDBThresholdLower( "" );
            panel.setHDBThresholdUpper( "" );
        }
        //MODE T END

        //MODE D START
        ArchivingConfigurationMode differenceMode = super.getMode( ArchivingConfigurationMode.TYPE_D );
        if ( differenceMode != null && differenceMode.getMode() != null && differenceMode.getMode().getModeD() != null )
        {
            ModeDifference modeD = differenceMode.getMode().getModeD();

            int period = modeD.getPeriod();

            panel.setHDBDifferencePeriod( period );
        }
        else
        {
            panel.setHDBDifferencePeriod( -1 );
        }
        //MODE D END
    }

    /**
     * @param period 26 juil. 2005
     */
    public void setDefaultPeriod ( int period )
    {
        this.defaultPeriod = period;
        //System.out.println ( "HDBProperties/setDefaultPeriod/period/"+period+"/" );
        ArchivingConfigurationMode ACPMode = this.getMode( ArchivingConfigurationMode.TYPE_P );
        if ( ACPMode != null )
        {
            ACPMode.getMode().getModeP().setPeriod( period );
        }

    }

    /**
     * @param completeName
     * @return 28 juil. 2005
     * @throws Exception
     */
    public static ArchivingConfigurationAttributeHDBProperties loadHDBProperties ( String completeName ) throws Exception
    {
        //return ArchivingConfigurationAttributeDBProperties.loadDBProperties ( completeName , true );
        IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
        Mode mode = manager.getArchivingMode( completeName , true );
        ArchivingConfigurationMode[] ACModes = ArchivingConfigurationMode.buildModesList( mode );
        ArchivingConfigurationAttributeHDBProperties ret = new ArchivingConfigurationAttributeHDBProperties();
        if ( ret == null )
        {
            return null;
        }
        ret.setModes( ACModes );
        return ret;
    }

    public void controlValues () throws ArchivingConfigurationException
    {
        if ( this.isEmpty() )
        {
            return;
        }

        super.controlValues();
    }


}
