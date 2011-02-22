//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/YAxis.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  YAxis.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: YAxis.java,v $
// Revision 1.6  2007/02/01 14:21:46  pierrejoseph
// XmlHelper reorg
//
// Revision 1.5  2006/10/04 09:58:31  ounsy
// adedd a cloneAxis() method
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 12:00:45  ounsy
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
import java.util.Enumeration;
import java.util.Vector;

import fr.esrf.tangoatk.widget.util.chart.JLAxis;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.containers.view.dialogs.AxisPanel;
import fr.soleil.mambo.containers.view.dialogs.Y1AxisPanel;
import fr.soleil.mambo.containers.view.dialogs.Y2AxisPanel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class YAxis extends Axis
{
    private int position;

    public static final String XML_TAG_1 = "Y1Axis";
    public static final String XML_TAG_2 = "Y2Axis";
    public static final String POSITION_PROPERTY_XML_TAG = "position";
    public static final String SCALE_MIN_PROPERTY_XML_TAG = "scaleMin";
    public static final String SCALE_MAX_PROPERTY_XML_TAG = "scaleMax";
    public static final String SCALE_MODE_PROPERTY_XML_TAG = "scaleMode";
    public static final String AUTO_SCALE_PROPERTY_XML_TAG = "autoScale";
    public static final String LABEL_FORMAT_PROPERTY_XML_TAG = "labelFormat";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String SHOW_SUB_GRID_PROPERTY_XML_TAG = "showSubGrid";
    public static final String IS_VISIBLE_PROPERTY_XML_TAG = "isVisible";
    public static final String DRAW_OPPOSITE_PROPERTY_XML_TAG = "drawOpposite";
    public static final String TITLE_PROPERTY_XML_TAG = "title";

    public String toString ( boolean isY1 )
    {
        //String ret = "";

        String tag = isY1 ? XML_TAG_1 : XML_TAG_2;
        XMLLine openingLine = new XMLLine( tag , XMLLine.EMPTY_TAG_CATEGORY );

        openingLine.setAttributeIfNotNull( POSITION_PROPERTY_XML_TAG , String.valueOf( position ) );
        openingLine.setAttributeIfNotNull( SCALE_MIN_PROPERTY_XML_TAG , String.valueOf( scaleMin ) );
        openingLine.setAttributeIfNotNull( SCALE_MAX_PROPERTY_XML_TAG , String.valueOf( scaleMax ) );
        openingLine.setAttributeIfNotNull( SCALE_MODE_PROPERTY_XML_TAG , String.valueOf( scaleMode ) );
        openingLine.setAttributeIfNotNull( AUTO_SCALE_PROPERTY_XML_TAG , String.valueOf( autoScale ) );
        openingLine.setAttributeIfNotNull( LABEL_FORMAT_PROPERTY_XML_TAG , String.valueOf( labelFormat ) );
        openingLine.setAttributeIfNotNull( COLOR_PROPERTY_XML_TAG , GUIUtilities.colorToString( color ) );
        openingLine.setAttributeIfNotNull( SHOW_SUB_GRID_PROPERTY_XML_TAG , String.valueOf( showSubGrid ) );
        openingLine.setAttributeIfNotNull( IS_VISIBLE_PROPERTY_XML_TAG , String.valueOf( isVisible ) );
        openingLine.setAttributeIfNotNull( DRAW_OPPOSITE_PROPERTY_XML_TAG , String.valueOf( drawOpposite ) );
        openingLine.setAttributeIfNotNull( TITLE_PROPERTY_XML_TAG , title );

        return openingLine.toString();
    }

    public YAxis ()
    {
        super();
    }

    /**
     * @return Returns the position.
     */
    public int getPosition ()
    {
        return position;
    }

    /**
     * @param position The position to set.
     */
    public void setPosition ( int position )
    {
        this.position = position;
    }

    /**
     * 25 août 2005
     */
    public void push ( boolean first )
    {
        //System.out.println ( "YAxis/push/first/"+first+"/" );
        AxisPanel axisPanel;
        if ( first )
        {
            axisPanel = Y1AxisPanel.getInstance();
        }
        else
        {
            axisPanel = Y2AxisPanel.getInstance();
        }
        axisPanel.setLabelFormat( this.getLabelFormat() );//-
        axisPanel.setPosition( this.getPosition() );//-
        axisPanel.setScaleMode( this.getScaleMode() );//-
        axisPanel.setAutoScale( this.isAutoScale() );//-
        axisPanel.setColor( this.getColor() );//-
        axisPanel.setDrawOpposite( this.isDrawOpposite() );//-
        axisPanel.setScaleMax( this.getScaleMax() );
        axisPanel.setScaleMin( this.getScaleMin() );
        axisPanel.setShowSubGrid( this.isShowSubGrid() );//-
        axisPanel.setVisible( this.isVisible() );//-
        axisPanel.setTitle( this.title );
    }

    public void setupJLAxis ( JLAxis in , boolean gridVisible , int gridStyle )
    {
        Vector currentViews = in.getViews();
        Enumeration enumeration = currentViews.elements();
        while ( enumeration.hasMoreElements() )
        {
            JLDataView next = ( JLDataView ) enumeration.nextElement();
            in.removeDataView( next );
        }
        //in.getDataView ();

        in.setAutoScale( this.autoScale );
        in.setAxisColor( this.color );
        in.setDrawOpposite( this.drawOpposite );
        in.setLabelFormat( this.labelFormat );
        in.setPosition( this.position );
        in.setScale( this.scaleMode );
        in.setSubGridVisible( this.showSubGrid );
        in.setVisible( this.isVisible );
        in.setName( this.title );
        if ( this.scaleMax > this.scaleMin )
        {
            in.setMinimum( this.scaleMin );
            in.setMaximum( this.scaleMax );
        }

        in.setGridStyle( gridStyle );
        in.setGridVisible( gridVisible );

    }

    public YAxis cloneAxis() 
    {
        YAxis ret = new YAxis ();
        
        ret.setAutoScale ( this.isAutoScale () );
        ret.setColor ( this.getColor () == null ? null : new Color ( this.getColor ().getRGB () ) );
        ret.setDrawOpposite ( this.isDrawOpposite() );
        ret.setLabelFormat ( this.getLabelFormat () );
        ret.setPosition ( this.getPosition () );
        ret.setScaleMax ( this.getScaleMax () );
        ret.setScaleMin ( this.getScaleMin () );
        ret.setScaleMode ( this.getScaleMode () );
        ret.setShowSubGrid ( this.isShowSubGrid () );
        ret.setTitle ( this.getTitle () == null ? null : new String ( this.getTitle () ) );
        ret.setVisible ( this.isVisible() );
        
        return ret;
    }
}
