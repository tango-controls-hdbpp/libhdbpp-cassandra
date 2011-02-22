//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Bar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Bar.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: Bar.java,v $
// Revision 1.4  2007/02/01 14:21:46  pierrejoseph
// XmlHelper reorg
//
// Revision 1.3  2007/01/11 14:05:45  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
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

import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesTab;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Bar// extends Graph
{
    private Color fillColor;
    private int width;
    private int fillStyle;
    private int fillingMethod;

    public static final String XML_TAG = "bar";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String WIDTH_PROPERTY_XML_TAG = "width";
    public static final String FILL_STYLE_PROPERTY_XML_TAG = "fillStyle";
    public static final String FILL_METHOD_PROPERTY_XML_TAG = "fillMethod";
    private boolean isEmpty = true;

    public String toString ()
    {
        XMLLine openingLine = new XMLLine( XML_TAG , XMLLine.EMPTY_TAG_CATEGORY );

        openingLine.setAttribute( COLOR_PROPERTY_XML_TAG , GUIUtilities.colorToString( fillColor ) );
        openingLine.setAttribute( WIDTH_PROPERTY_XML_TAG , String.valueOf( width ) );
        openingLine.setAttribute( FILL_STYLE_PROPERTY_XML_TAG , String.valueOf( fillStyle ) );
        openingLine.setAttribute( FILL_METHOD_PROPERTY_XML_TAG , String.valueOf( fillingMethod ) );

        return openingLine.toString();
    }

    public Bar ()
    {
        isEmpty = true;
    }

    public Bar ( Color _fillColor , int _width , int _fillStyle , int _fillingMethod )
    {
        this.fillColor = _fillColor;
        this.fillingMethod = _fillingMethod;
        this.width = _width;
        this.fillStyle = _fillStyle;

        isEmpty = false;
    }

    /**
     * @return Returns the fillColor.
     */
    public Color getFillColor ()
    {
        return fillColor;
    }

    /**
     * @param fillColor The fillColor to set.
     */
    public void setFillColor ( Color fillColor )
    {
        this.fillColor = fillColor;
        isEmpty = false;
    }

    /**
     * @return Returns the fillingMethod.
     */
    public int getFillingMethod ()
    {
        return fillingMethod;
    }

    /**
     * @param fillingMethod The fillingMethod to set.
     */
    public void setFillingMethod ( int fillingMethod )
    {
        this.fillingMethod = fillingMethod;
        isEmpty = false;
    }

    /**
     * @return Returns the fillStyle.
     */
    public int getFillStyle ()
    {
        return fillStyle;
    }

    /**
     * @param fillStyle The fillStyle to set.
     */
    public void setFillStyle ( int fillStyle )
    {
        this.fillStyle = fillStyle;
        isEmpty = false;
    }

    /**
     * @return Returns the width.
     */
    public int getWidth ()
    {
        return width;
    }

    /**
     * @param width The width to set.
     */
    public void setWidth ( int width )
    {
        this.width = width;
        isEmpty = false;
    }


    /**
     * 25 août 2005
     */
    public void push ()
    {
        AttributesPlotPropertiesPanel plotPropertiesPanel = AttributesPlotPropertiesTab.getInstance().getPropertiesPanel();

        plotPropertiesPanel.setBarFillColor( this.getFillColor() );
        plotPropertiesPanel.setBarFillingMethod( this.getFillingMethod() );
        plotPropertiesPanel.setBarFillStyle( this.getFillStyle() );
        plotPropertiesPanel.setBarWidth( this.getWidth() );
    }

    public JLDataView applyProperties ( JLDataView in )
    {
        in.setBarWidth( this.width );
        in.setFillColor( this.fillColor );
        in.setFillMethod( this.fillingMethod );
        in.setFillStyle( this.fillStyle );

        return in;
    }

    /**
     * @return
     */
    public boolean isEmpty ()
    {
        return isEmpty;
    }

}
