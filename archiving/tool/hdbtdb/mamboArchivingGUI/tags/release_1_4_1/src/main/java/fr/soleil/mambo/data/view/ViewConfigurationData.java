//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationData.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfigurationData.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.14 $
//
// $Log: ViewConfigurationData.java,v $
// Revision 1.14  2007/12/12 17:49:51  pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.13  2007/10/30 17:49:25  soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment : Define the initializeIsHistoric method, and call it in each ViewConfigurationData builder.
//
// Revision 1.12  2007/05/11 07:23:44  ounsy
// * minor bug correction
//
// Revision 1.11  2007/02/01 14:21:46  pierrejoseph
// XmlHelper reorg
//
// Revision 1.10  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.9  2006/12/06 15:12:22  ounsy
// added parametrisable sampling
//
// Revision 1.8  2006/12/06 10:15:29  ounsy
// minor changes
//
// Revision 1.7  2006/10/04 09:59:11  ounsy
// added cloneData() and refreshContent() methods
//
// Revision 1.6  2006/09/18 08:48:30  ounsy
// added start and end dates in charts
//
// Revision 1.5  2006/09/05 14:03:51  ounsy
// updated for sampling compatibility
//
// Revision 1.4  2006/08/31 12:19:32  ounsy
// informations about forcing export only in case of tdb
//
// Revision 1.3  2006/02/01 14:11:51  ounsy
// minor changes (small date bug corrected)
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/26 07:52:25  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.view;

import java.sql.Timestamp;

import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.containers.view.ViewGeneralPanel;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.containers.view.dialogs.GeneralTabbedPane;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.models.VCPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfigurationData
{
//  MULTI-CONF
    private boolean isHistoric;

    private boolean dynamicDateRange = false;
    private String dateRange = "";

    private Timestamp startDate;
    private Timestamp endDate;

    private GeneralChartProperties generalChartProperties;
    private YAxis y1;
    private YAxis y2;

    private Timestamp creationDate;
    private Timestamp lastUpdateDate;
    private String name;
    private String path;

    private SamplingType samplingType;

    public static final String CREATION_DATE_PROPERTY_XML_TAG = "creationDate";
    public static final String LAST_UPDATE_DATE_PROPERTY_XML_TAG = "lastUpdateDate";
    public static final String START_DATE_PROPERTY_XML_TAG = "startDate";
    public static final String END_DATE_PROPERTY_XML_TAG = "endDate";
    public static final String HISTORIC_PROPERTY_XML_TAG = "historic";
    public static final String DYNAMIC_DATE_RANGE_PROPERTY_XML_TAG = "dynamicDateRange";

    public static final String IS_MODIFIED_PROPERTY_XML_TAG = "isModified";
    public static final String DATE_RANGE_PROPERTY_XML_TAG = "dateRange";
    public static final String NAME_PROPERTY_XML_TAG = "name";
    public static final String PATH_PROPERTY_XML_TAG = "path";
    public static final String SAMPLING_TYPE_PROPERTY_XML_TAG = "samplingType";
    public static final String SAMPLING_FACTOR_PROPERTY_XML_TAG = "samplingFactor";

    public static final String GENERIC_PLOT_PARAMETERS_XML_TAG = "genericPlotParameters";

    public ViewConfigurationData ()
    {
        this.generalChartProperties = new GeneralChartProperties();
        this.y1 = new YAxis();
        this.y2 = new YAxis();
        
        this.samplingType = SamplingType.getSamplingType ( SamplingType.ALL );
        isHistoric = Mambo.isHdbAvailable();
        //initializeIsHistoric();
    }

    public ViewConfigurationData ( GeneralChartProperties _generalChartProperties , YAxis _y1 , YAxis _y2 )
    {
        this.generalChartProperties = _generalChartProperties;
        this.y1 = _y1;
        this.y2 = _y2;
        
        this.samplingType = SamplingType.getSamplingType ( SamplingType.ALL );
        isHistoric = Mambo.isHdbAvailable();
        //initializeIsHistoric();
    }
    
  /*  private void initializeIsHistoric()
    {
        // if HDB is not available
        if ( Mambo.isHdbAvailable() == false ) {
        	isHistoric = false;
        }
        // else if the file mambo.properties doesn't exist or the syntax inside is wrong
        else {
        	// Behaviour by default:
        	isHistoric = true;
        }
    }*/

    public String toString ()
    {
        String ret = "";

        XMLLine openingLine = new XMLLine( GENERIC_PLOT_PARAMETERS_XML_TAG , XMLLine.OPENING_TAG_CATEGORY );
        XMLLine closingLine = new XMLLine( GENERIC_PLOT_PARAMETERS_XML_TAG , XMLLine.CLOSING_TAG_CATEGORY );

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if ( this.generalChartProperties != null )
        {
            ret += this.generalChartProperties.toString();
            ret += GUIUtilities.CRLF;
        }
        if ( this.y1 != null )
        {
            ret += this.y1.toString( true );
            ret += GUIUtilities.CRLF;
        }
        if ( this.y2 != null )
        {
            ret += this.y2.toString( false );
            ret += GUIUtilities.CRLF;
        }


        ret += closingLine.toString();

        return ret;
    }

    public boolean equals ( ViewConfigurationData this2 )
    {
        String path1 = this.getPath() == null ? "" : this.getPath();
        String path2 = this2.getPath() == null ? "" : this2.getPath();
        if ( path1.equals( path2 ) )
        {
            return true;
        }

        String name1 = this.getName() == null ? "" : this.getName();
        String name2 = this2.getName() == null ? "" : this2.getName();
        if ( !name1.equals( name2 ) )
        {
            return false;
        }

        Timestamp creationDate1 = this.getCreationDate();
        Timestamp creationDate2 = this2.getCreationDate();
        if ( creationDate1 == null )
        {
            if ( creationDate2 != null )
            {
                return false;
            }
        }
        else
        {
            if ( creationDate2 == null )
            {
                return false;
            }
            else
            {
                if ( !creationDate1.equals( creationDate2 ) )
                {
                    return false;
                }
            }
        }

        Timestamp lastUpdateDate1 = this.getLastUpdateDate();
        Timestamp lastUpdateDate2 = this2.getLastUpdateDate();
        if ( lastUpdateDate1 == null )
        {
            if ( lastUpdateDate2 != null )
            {
                return false;
            }
        }
        else
        {
            if ( lastUpdateDate2 == null )
            {
                return false;
            }
            else
            {
                if ( !lastUpdateDate1.equals( lastUpdateDate2 ) )
                {
                    return false;
                }
            }
        }
        
        SamplingType samplingType1 = this.getSamplingType ();
        SamplingType samplingType2 = this2.getSamplingType ();
        if ( samplingType1 == null )
        {
            if ( samplingType2 != null )
            {
                return false;
            }
        }
        else
        {
            if ( samplingType2 == null )
            {
                return false;
            }
            else
            {
                if ( samplingType1.getType() != samplingType2.getType () )
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @return Returns the creationDate.
     */
    public Timestamp getCreationDate ()
    {
        return creationDate;
    }

    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate ( Timestamp creationDate )
    {
        this.creationDate = creationDate;
    }

    /**
     * @return Returns the endDate.
     */
    public Timestamp getEndDate ()
    {
        return endDate;
    }

    /**
     * @param endDate The endDate to set.
     */
    public void setEndDate ( Timestamp endDate )
    {
        this.endDate = endDate;
    }

    /**
     * @return Returns the lastUpdateDate.
     */
    public Timestamp getLastUpdateDate ()
    {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate The lastUpdateDate to set.
     */
    public void setLastUpdateDate ( Timestamp lastUpdateDate )
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return Returns the startDate.
     */
    public Timestamp getStartDate ()
    {
        return startDate;
    }

    /**
     * @param startDate The startDate to set.
     */
    public void setStartDate ( Timestamp startDate )
    {
        this.startDate = startDate;
    }

    /**
     * @return Returns the generalChartProperties.
     */
    public GeneralChartProperties getGeneralChartProperties ()
    {
        return generalChartProperties;
    }

    /**
     * @param generalChartProperties The generalChartProperties to set.
     */
    public void setGeneralChartProperties ( GeneralChartProperties generalChartProperties )
    {
        this.generalChartProperties = generalChartProperties;
    }

    /**
     * @return Returns the y1.
     */
    public YAxis getY1 ()
    {
        return y1;
    }

    /**
     * @param y1 The y1 to set.
     */
    public void setY1 ( YAxis y1 )
    {
        this.y1 = y1;
    }

    /**
     * @return Returns the y2.
     */
    public YAxis getY2 ()
    {
        return y2;
    }

    /**
     * @param y2 The y2 to set.
     */
    public void setY2 ( YAxis y2 )
    {
        this.y2 = y2;
    }

    /**
     * 25 août 2005
     */
    public void push ()
    {
        //System.out.println ( "ViewConfigurationData/push/this.isHistoric ()/"+this.isHistoric ()+"/" );

        ViewGeneralPanel viewGeneralPanel = ViewGeneralPanel.getInstance();
        viewGeneralPanel.setLastUpdateDate( this.getLastUpdateDate() );
        viewGeneralPanel.setCreationDate( this.getCreationDate() );
        viewGeneralPanel.setName( this.getName() );
        viewGeneralPanel.setPath( this.getPath() );
        
        GeneralTab generalTab = GeneralTab.getInstance();
        generalTab.setLastUpdateDate( this.getLastUpdateDate() );
        generalTab.setCreationDate( this.getCreationDate() );
        generalTab.setName( this.getName() );
        //System.out.println ( "ViewConfigurationData/push/this.getSamplingType ()/"+this.getSamplingType ()+"/" );
        generalTab.setSamplingType( this.getSamplingType () );
        boolean dynamic = this.isDynamicDateRange();
        if ( dynamic )
        {
            Timestamp[] range = generalTab.getDynamicStartAndEndDates();
            generalTab.setStartDate( range[ 0 ] );
            generalTab.setEndDate( range[ 1 ] );
        }
        else
        {
            generalTab.setStartDate( this.getStartDate() );
            generalTab.setEndDate( this.getEndDate() );
        }
        generalTab.setDynamicDateRange( dynamic );
        generalTab.setDateRange( getDateRange() );
        generalTab.setHistoric( this.isHistoric() );
        VCAttributesTreeModel vCAttributesTreeModel = VCAttributesTreeModel.getInstance();
        vCAttributesTreeModel.setRootName( this.isHistoric() );
        //VCPossibleAttributesTreeModel vCPossibleAttributesTreeModel = VCPossibleAttributesTreeModel.getInstance ();
        //vCPossibleAttributesTreeModel.setRootName ( this.isHistoric () );
        VCPossibleAttributesTreeModel vCPossibleAttributesTreeModel = VCPossibleAttributesTreeModel.forceGetInstance( this.isHistoric() );
        VCPossibleAttributesTree vCPossibleAttributesTree = VCPossibleAttributesTree.getInstance();
        if ( vCPossibleAttributesTree != null )
        {
            vCPossibleAttributesTree.setModel( vCPossibleAttributesTreeModel );
        }

        if ( this.generalChartProperties != null )
        {
            generalChartProperties.push();
        }
        if ( this.y1 != null )
        {
            y1.push( true );
        }
        if ( this.y2 != null )
        {
            y2.push( false );
        }
        ViewGeneralPanel.getInstance().updateForceExport();
    }

    /**
     * @return Returns the isHistoric.
     */
    public boolean isHistoric ()
    {
        return isHistoric;
    }

    /**
     * @param isHistoric The isHistoric to set.
     */
    public void setHistoric ( boolean isHistoric )
    {
        this.isHistoric = isHistoric;
    }

    /**
     * @return returns the dynamicDateRange
     */
    public boolean isDynamicDateRange ()
    {
        return dynamicDateRange;
    }

    /**
     * @param dynamic the value to set to dynamicDateRange
     */
    public void setDynamicDateRange ( boolean dynamic )
    {
        dynamicDateRange = dynamic;
    }

    /**
     * @return returns dateRange
     */
    public String getDateRange ()
    {
        return dateRange;
    }

    /**
     * @param range the value to set to dateRange
     */
    public void setDateRange ( String range )
    {
        dateRange = range;
    }

    /**
     * @return 26 août 2005
     */
    public StaticChartMathExpression getChart ()
    {
        StaticChartMathExpression chart = generalChartProperties.getAsJLChart();
        //System.out.println ( "BEFORE/getMaximum/"+chart.getY1Axis ().getMaximum ()+"/" );
        int axisGrid = generalChartProperties.getAxisGrid();
        boolean xHasAxisGrid = false;
        boolean y1HasAxisGrid = false;
        boolean y2HasAxisGrid = false;

        if ( axisGrid == GeneralChartProperties.AXISGRID_ONX || axisGrid == GeneralChartProperties.AXISGRID_ONXANDY1 || axisGrid == GeneralChartProperties.AXISGRID_ONXANDY2 )
        {
            xHasAxisGrid = true;
        }
        if ( axisGrid == GeneralChartProperties.AXISGRID_ONY1 || axisGrid == GeneralChartProperties.AXISGRID_ONXANDY1 )
        {
            y1HasAxisGrid = true;
        }
        if ( axisGrid == GeneralChartProperties.AXISGRID_ONY2 || axisGrid == GeneralChartProperties.AXISGRID_ONXANDY2 )
        {
            y2HasAxisGrid = true;
        }

        double startDate_d = startDate.getTime();
        double endDate_d = endDate.getTime();
        chart.getXAxis().setAutoScale( false );
        chart.getXAxis().setMinimum( startDate_d );
        chart.getXAxis().setMaximum( endDate_d );

        y1.setupJLAxis( chart.getY1Axis() , y1HasAxisGrid , generalChartProperties.getAxisGridStyle() );
        //System.out.println ( "AFTER/getMaximum/"+chart.getY1Axis ().getMaximum ()+"/" );
        y2.setupJLAxis( chart.getY2Axis() , y2HasAxisGrid , generalChartProperties.getAxisGridStyle() );

        chart.getXAxis().setGridVisible( xHasAxisGrid );
        chart.getXAxis().setGridStyle( chart.getY1Axis().getGridStyle() );

        String title = "";
        if (chart.getHeader() != null)
        {
            title += chart.getHeader() + " - ";
        }
        title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE") + getStartDate().toString() + ", " + Messages.getMessage("VIEW_ATTRIBUTES_END_DATE") + getEndDate().toString();
        chart.setHeader(title);
        title = null;


        return chart;
    }

    /**
     * @return Returns the name.
     */
    public String getName ()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName ( String name )
    {
        this.name = name;
    }

    /**
     * @return Returns the path.
     */
    public String getPath ()
    {
        return path;
    }

    /**
     * @param path The path to set.
     */
    public void setPath ( String path )
    {
        this.path = path;
    }

    public Timestamp[] getDynamicStartAndEndDates ()
    {
        Timestamp[] startAndEnd = new Timestamp[ 2 ];
        long now = System.currentTimeMillis();
        Timestamp end = new Timestamp( now );
        startAndEnd[ 1 ] = end;
        long delta = 0;
        long hour = 3600000;
        long day = 86400000;
        String range = getDateRange();
        if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1H" ).equals( range ) )
        {
            delta = 1 * hour;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_4H" ).equals( range ) )
        {
            delta = 4 * hour;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_8H" ).equals( range ) )
        {
            delta = 8 * hour;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1D" ).equals( range ) )
        {
            delta = 1 * day;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_3D" ).equals( range ) )
        {
            delta = 3 * day;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_7D" ).equals( range ) )
        {
            delta = 7 * day;
        }
        else if ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_30D" ).equals( range ) )
        {
            delta = 30 * day;
        }
        Timestamp start = new Timestamp( now - delta );
        startAndEnd[ 0 ] = start;
        return startAndEnd;
    }

    public void setSamplingType(SamplingType _samplingType) 
    {
        this.samplingType = _samplingType;
    }

    /**
     * @return Returns the samplingType.
     */
    public SamplingType getSamplingType() {
        return this.samplingType;
    }

    public void refreshContent() 
    {
        GeneralTabbedPane generalTabbedPane = GeneralTabbedPane.getInstance();
        GeneralChartProperties generalChartProperties = generalTabbedPane.getGeneralChartProperties();
        YAxis y1Axis = generalTabbedPane.getY1Axis();
        YAxis y2Axis = generalTabbedPane.getY2Axis();
        this.setGeneralChartProperties ( generalChartProperties );
        this.setY1 ( y1Axis );
        this.setY2 ( y2Axis );

        if ( this.isDynamicDateRange () )
        {
            Timestamp[] range = this.getDynamicStartAndEndDates();
            this.setStartDate( range[ 0 ] );
            this.setEndDate( range[ 1 ] );
        }
    }

    public ViewConfigurationData cloneData () 
    {
        ViewConfigurationData ret = new ViewConfigurationData ();
        
        ret.setCreationDate ( this.getCreationDate () == null ? null : (Timestamp) this.getCreationDate ().clone () );
        ret.setDateRange ( this.getDateRange () == null ? null : new String ( this.getDateRange () ) );
        ret.setDynamicDateRange ( this.isDynamicDateRange () );
        ret.setEndDate ( this.getEndDate () == null ? null : (Timestamp) this.getEndDate ().clone () );
        ret.setGeneralChartProperties ( this.getGeneralChartProperties () == null ? null : this.getGeneralChartProperties().cloneGeneralChartProperties () );
        ret.setHistoric ( this.isHistoric );
        ret.setLastUpdateDate ( this.getLastUpdateDate () == null ? null : (Timestamp) this.getLastUpdateDate ().clone () );
        ret.setName ( this.getName () == null ? null : new String ( this.getName () ) );
        ret.setPath ( this.getPath () == null ? null : new String ( this.getPath () ) );
        ret.setSamplingType ( this.getSamplingType () == null ? null : this.getSamplingType ().cloneSamplingType () );
        ret.setStartDate ( this.getStartDate () == null ? null : (Timestamp) this.getStartDate ().clone () );
        ret.setY1 ( this.getY1 () == null ? null : this.getY1 ().cloneAxis () );
        ret.setY2 ( this.getY2 () == null ? null : this.getY2 ().cloneAxis () );
        
        return ret;
    }
}
