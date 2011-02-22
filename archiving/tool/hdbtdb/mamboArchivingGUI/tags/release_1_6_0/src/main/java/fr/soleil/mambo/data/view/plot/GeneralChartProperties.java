//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/GeneralChartProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  GeneralChartProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: GeneralChartProperties.java,v $
// Revision 1.10  2007/08/24 09:02:44  ounsy
// Axis grid selection now corresponds to JLChart axis grid (Mantis bug 6129)
//
// Revision 1.9  2007/05/11 07:24:00  ounsy
// * minor bug correction with Fonts
//
// Revision 1.8  2007/05/10 14:48:16  ounsy
// possibility to change "no value" String in chart data file (default is "*") through vc option
//
// Revision 1.7  2007/02/01 14:21:46  pierrejoseph
// XmlHelper reorg
//
// Revision 1.6  2007/01/11 14:05:45  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.5  2006/10/04 09:58:45  ounsy
// added a cloneGeneralChartProperties() method
//
// Revision 1.4  2006/07/05 12:58:58  ounsy
// VC : data synchronization management
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
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
package fr.soleil.mambo.data.view.plot;

import java.awt.Color;
import java.awt.Font;
import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.mambo.components.view.CustomJLChart;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsVCTab;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class GeneralChartProperties
{
    private boolean legendsAreVisible;
    private int legendsPlacement;
    private Color backgroundColor;
    private Font headerFont = new Font( "Dialog", Font.PLAIN, 12 );
    private Font labelFont = new Font( "Dialog", Font.PLAIN, 12 );
    private int axisGrid;
    private int axisGridStyle;
    private String title;
    private long displayDuration;
    private int timePrecision = 0;

    public static final int AXISGRID_NONE     = 0;
    public static final int AXISGRID_ONX      = 1;
    public static final int AXISGRID_ONY1     = 2;
    public static final int AXISGRID_ONY2     = 3;
    public static final int AXISGRID_ONXANDY1 = 4;
    public static final int AXISGRID_ONXANDY2 = 5;

    public static final String XML_TAG = "mainParams";
    public static final String LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG = "legendsAreVisible";
    public static final String LEGENDS_PLACEMENT_PROPERTY_XML_TAG = "legendsPlacement";
    public static final String BACKGROUND_COLOR_PROPERTY_XML_TAG = "backgroundColor";
    public static final String HEADER_FONT_PROPERTY_XML_TAG = "headerFont";
    public static final String LABEL_FONT_PROPERTY_XML_TAG = "labelFont";
    public static final String AXIS_GRID_PROPERTY_XML_TAG = "axisGrid";
    public static final String AXIS_GRID_STYLE_PROPERTY_XML_TAG = "axisGridStyle";
    public static final String TITLE_PROPERTY_XML_TAG = "title";
    public static final String DISPLAY_DURATION_PROPERTY_XML_TAG = "displayDuration";
    public static final String TIME_PRECISION_XML_TAG = "timePrecision";

    public String toString ()
    {
        //String ret = "";

        XMLLine openingLine = new XMLLine( XML_TAG , XMLLine.EMPTY_TAG_CATEGORY );

        openingLine.setAttributeIfNotNull( LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG , String.valueOf( legendsAreVisible ) );
        openingLine.setAttributeIfNotNull( LEGENDS_PLACEMENT_PROPERTY_XML_TAG , String.valueOf( legendsPlacement ) );
        openingLine.setAttributeIfNotNull( BACKGROUND_COLOR_PROPERTY_XML_TAG , GUIUtilities.colorToString( backgroundColor ) );
        openingLine.setAttributeIfNotNull( HEADER_FONT_PROPERTY_XML_TAG , GUIUtilities.fontToString( headerFont ) );
        openingLine.setAttributeIfNotNull( LABEL_FONT_PROPERTY_XML_TAG , GUIUtilities.fontToString( labelFont ) );
        openingLine.setAttributeIfNotNull( AXIS_GRID_PROPERTY_XML_TAG , String.valueOf( axisGrid ) );
        openingLine.setAttributeIfNotNull( AXIS_GRID_STYLE_PROPERTY_XML_TAG , String.valueOf( axisGridStyle ) );
        openingLine.setAttributeIfNotNull( TITLE_PROPERTY_XML_TAG , String.valueOf( title ) );
        openingLine.setAttributeIfNotNull( DISPLAY_DURATION_PROPERTY_XML_TAG , String.valueOf( displayDuration ) );
        openingLine.setAttributeIfNotNull( TIME_PRECISION_XML_TAG , String.valueOf( timePrecision ) );

        //System.out.println ( "headerFont/" + GUIUtilities.fontToString ( headerFont ) + "/" );

        return openingLine.toString();
    }

    public GeneralChartProperties ()
    {

    }

    /**
     * @return Returns the axisGrid.
     */
    public int getAxisGrid ()
    {
        return axisGrid;
    }

    /**
     * @param axisGrid The axisGrid to set.
     */
    public void setAxisGrid ( int axisGrid )
    {
        this.axisGrid = axisGrid;
    }

    /**
     * @return Returns the axisGridStyle.
     */
    public int getAxisGridStyle ()
    {
        return axisGridStyle;
    }

    /**
     * @param axisGridStyle The axisGridStyle to set.
     */
    public void setAxisGridStyle ( int axisGridStyle )
    {
        this.axisGridStyle = axisGridStyle;
    }

    /**
     * @return Returns the backgroundColor.
     */
    public Color getBackgroundColor ()
    {
        return backgroundColor;
    }

    /**
     * @param backgroundColor The backgroundColor to set.
     */
    public void setBackgroundColor ( Color backgroundColor )
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return Returns the displayDuration.
     */
    public long getDisplayDuration ()
    {
        return displayDuration;
    }

    /**
     * @param displayDuration The displayDuration to set.
     */
    public void setDisplayDuration ( long displayDuration )
    {
        this.displayDuration = displayDuration;
    }

    /**
     * @return Returns the headerFont.
     */
    public Font getHeaderFont ()
    {
        return headerFont;
    }

    /**
     * @param headerFont The headerFont to set.
     */
    public void setHeaderFont ( Font headerFont )
    {
        if (headerFont != null) this.headerFont = headerFont;
    }

    /**
     * @return Returns the labelFont.
     */
    public Font getLabelFont ()
    {
        return labelFont;
    }

    /**
     * @param labelFont The labelFont to set.
     */
    public void setLabelFont ( Font labelFont )
    {
        if (labelFont != null) this.labelFont = labelFont;
    }

    /**
     * @return Returns the legendsAreVisible.
     */
    public boolean isLegendsAreVisible ()
    {
        return legendsAreVisible;
    }

    /**
     * @param legendsAreVisible The legendsAreVisible to set.
     */
    public void setLegendsAreVisible ( boolean legendsAreVisible )
    {
        this.legendsAreVisible = legendsAreVisible;
    }

    /**
     * @return Returns the legendsPlacement.
     */
    public int getLegendsPlacement ()
    {
        return legendsPlacement;
    }

    /**
     * @param legendsPlacement The legendsPlacement to set.
     */
    public void setLegendsPlacement ( int legendsPlacement )
    {
        this.legendsPlacement = legendsPlacement;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle ()
    {
        return title;
    }

    /**
     * @param title The title to set.
     */
    public void setTitle ( String title )
    {
        this.title = title;
    }

    /**
     * 25 août 2005
     */
    public void push ()
    {
        ChartGeneralTabbedPane generalTabbedPane = ChartGeneralTabbedPane.getInstance();

        generalTabbedPane.setAxisGrid( this.getAxisGrid() );
        generalTabbedPane.setAxisGridStyle( this.getAxisGridStyle() );
        generalTabbedPane.setLegendsPlacement( this.getLegendsPlacement() );
        generalTabbedPane.setBackgroundColor( this.getBackgroundColor() );//--
        generalTabbedPane.setDisplayDuration( this.getDisplayDuration() );//--
        generalTabbedPane.setLegendsAreVisible( this.isLegendsAreVisible() );
        generalTabbedPane.setHeaderFont( this.getHeaderFont() );//--
        generalTabbedPane.setLabelFont( this.getLabelFont() );//--
        generalTabbedPane.setTitle( this.getTitle() );
        generalTabbedPane.setTimePrecision( Integer.toString(this.getTimePrecision()) );
    }

    public StaticChartMathExpression getAsJLChart ()
    {
        CustomJLChart ret = new CustomJLChart();

        ret.setBackground( this.backgroundColor );
        ret.setHeaderFont( this.headerFont );
        ret.setLabelFont( this.labelFont );
        ret.setHeader( this.title );
        ret.setLabelVisible( this.legendsAreVisible );
        ret.setLabelPlacement( this.legendsPlacement );
        ret.setTimePrecision( this.timePrecision );
        ret.setNoValueString( OptionsVCTab.getInstance().getNoValueString() );

        return ret;
    }

    public int getTimePrecision ()
    {
        return timePrecision;
    }

    public void setTimePrecision (int timePrecision)
    {
        this.timePrecision = timePrecision;
    }

    public GeneralChartProperties cloneGeneralChartProperties() 
    {
        GeneralChartProperties ret = new GeneralChartProperties ();
        
        ret.setAxisGrid ( this.getAxisGrid () );
        ret.setAxisGridStyle ( this.getAxisGridStyle () );
        ret.setBackgroundColor ( this.getBackgroundColor () == null ? null : new Color ( this.getBackgroundColor ().getRGB () ) );
        ret.setDisplayDuration ( this.getDisplayDuration () );
        ret.setHeaderFont ( this.getHeaderFont () == null ? null : new Font ( this.getHeaderFont ().getAttributes () ) );
        ret.setLabelFont ( this.getLabelFont () == null ? null : new Font ( this.getLabelFont ().getAttributes () ) );
        ret.setLegendsAreVisible ( this.isLegendsAreVisible () );
        ret.setLegendsPlacement ( this.getLegendsPlacement () );
        ret.setTimePrecision ( this.getTimePrecision () );
        ret.setTitle ( this.getTitle () == null ? null : new String(this.getTitle()) );
        
        return ret;
    }
}
