// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Marker.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Marker.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: Marker.java,v $
// Revision 1.4 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.3 2007/01/11 14:05:45 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.data.view.plot;

import java.awt.Color;

import chart.temp.chart.JLDataView;

import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Marker {

	private Color color;
	private int size;
	private int style;
	private boolean isLegendVisible;

	public static final String XML_TAG = "marker";
	public static final String COLOR_PROPERTY_XML_TAG = "color";
	public static final String SIZE_PROPERTY_XML_TAG = "size";
	public static final String STYLE_PROPERTY_XML_TAG = "style";
	public static final String IS_LEGEND_VISIBLE_PROPERTY_XML_TAG = "isLegendVisible";
	private boolean isEmpty = true;

	public String toString() {
		XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

		openingLine.setAttribute(COLOR_PROPERTY_XML_TAG, GUIUtilities
				.colorToString(color));
		openingLine.setAttribute(SIZE_PROPERTY_XML_TAG, String.valueOf(size));
		openingLine.setAttribute(STYLE_PROPERTY_XML_TAG, String.valueOf(style));
		openingLine.setAttribute(IS_LEGEND_VISIBLE_PROPERTY_XML_TAG, String
				.valueOf(isLegendVisible));

		return openingLine.toString();
	}

	public Marker() {
		isEmpty = true;
	}

	public Marker(Color _color, int _size, int _style, boolean _isLegendVisible) {
		this.color = _color;
		this.size = _size;
		this.style = _style;
		this.isLegendVisible = _isLegendVisible;

		isEmpty = false;
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(Color color) {
		this.color = color;
		isEmpty = false;
	}

	/**
	 * @return Returns the isLegendVisible.
	 */
	public boolean isLegendVisible() {
		return isLegendVisible;
	}

	/**
	 * @param isLegendVisible
	 *            The isLegendVisible to set.
	 */
	public void setLegendVisible(boolean isLegendVisible) {
		this.isLegendVisible = isLegendVisible;
		isEmpty = false;
	}

	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size
	 *            The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
		isEmpty = false;
	}

	/**
	 * @return Returns the style.
	 */
	public int getStyle() {
		return style;
	}

	/**
	 * @param style
	 *            The style to set.
	 */
	public void setStyle(int style) {
		this.style = style;
		isEmpty = false;
	}

	public JLDataView applyProperties(JLDataView in) {
		in.setMarkerColor(this.color);
		in.setMarkerSize(this.size);
		in.setMarker(this.style);

		return in;
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

}
