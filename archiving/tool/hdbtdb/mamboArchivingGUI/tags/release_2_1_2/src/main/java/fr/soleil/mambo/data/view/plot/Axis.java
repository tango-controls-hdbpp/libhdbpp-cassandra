//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Axis.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Axis.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: Axis.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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

public class Axis {
	protected double scaleMin;
	protected double scaleMax;
	protected int scaleMode;
	protected boolean autoScale = true;

	protected int labelFormat;
	protected String title;
	protected Color color = Color.black;

	protected boolean showSubGrid;
	protected boolean isVisible = true;
	protected boolean drawOpposite;

	public Axis() {

	}

	/**
	 * @return Returns the autoScale.
	 */
	public boolean isAutoScale() {
		return autoScale;
	}

	/**
	 * @param autoScale
	 *            The autoScale to set.
	 */
	public void setAutoScale(boolean autoScale) {
		this.autoScale = autoScale;
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
	}

	/**
	 * @return Returns the drawOpposite.
	 */
	public boolean isDrawOpposite() {
		return drawOpposite;
	}

	/**
	 * @param drawOpposite
	 *            The drawOpposite to set.
	 */
	public void setDrawOpposite(boolean drawOpposite) {
		this.drawOpposite = drawOpposite;
	}

	/**
	 * @return Returns the isVisible.
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible
	 *            The isVisible to set.
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * @return Returns the labelFormat.
	 */
	public int getLabelFormat() {
		return labelFormat;
	}

	/**
	 * @param labelFormat
	 *            The labelFormat to set.
	 */
	public void setLabelFormat(int labelFormat) {
		this.labelFormat = labelFormat;
	}

	/**
	 * @return Returns the scaleMax.
	 */
	public double getScaleMax() {
		return scaleMax;
	}

	/**
	 * @param scaleMax
	 *            The scaleMax to set.
	 */
	public void setScaleMax(double scaleMax) {
		this.scaleMax = scaleMax;
	}

	/**
	 * @return Returns the scaleMin.
	 */
	public double getScaleMin() {
		return scaleMin;
	}

	/**
	 * @param scaleMin
	 *            The scaleMin to set.
	 */
	public void setScaleMin(double scaleMin) {
		this.scaleMin = scaleMin;
	}

	/**
	 * @return Returns the scaleMode.
	 */
	public int getScaleMode() {
		return scaleMode;
	}

	/**
	 * @param scaleMode
	 *            The scaleMode to set.
	 */
	public void setScaleMode(int scaleMode) {
		this.scaleMode = scaleMode;
	}

	/**
	 * @return Returns the showSubGrid.
	 */
	public boolean isShowSubGrid() {
		return showSubGrid;
	}

	/**
	 * @param showSubGrid
	 *            The showSubGrid to set.
	 */
	public void setShowSubGrid(boolean showSubGrid) {
		this.showSubGrid = showSubGrid;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
}
