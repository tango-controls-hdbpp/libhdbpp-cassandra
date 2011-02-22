//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationAttributeProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfigurationAttributeProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: ViewConfigurationAttributeProperties.java,v $
// Revision 1.4  2006/09/27 07:00:16  chinkumo
// correction for Mantis 2230
//
// Revision 1.3  2006/09/20 13:03:29  chinkumo
// Mantis 2230: Separated Set addition in the "Attributes plot properties" tab of the New/Modify VC View
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
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
package fr.soleil.mambo.data.view;

public class ViewConfigurationAttributeProperties {
	private ViewConfigurationAttributePlotProperties plotProperties;
	private ViewConfigurationAttributeExtractProperties extractProperties;

	private static ViewConfigurationAttributeProperties currentProperties;

	public static ViewConfigurationAttributeProperties getCurrentProperties() {
		return currentProperties;
	}

	public ViewConfigurationAttributeProperties() {
		this.plotProperties = new ViewConfigurationAttributePlotProperties();
		this.extractProperties = new ViewConfigurationAttributeExtractProperties();
	}

	/**
	 * @return Returns the extractProperties.
	 */
	public ViewConfigurationAttributeExtractProperties getExtractProperties() {
		return extractProperties;
	}

	/**
	 * @param extractProperties
	 *            The extractProperties to set.
	 */
	public void setExtractProperties(
			ViewConfigurationAttributeExtractProperties extractProperties) {
		this.extractProperties = extractProperties;
	}

	/**
	 * @return Returns the plotProperties.
	 */
	public ViewConfigurationAttributePlotProperties getPlotProperties() {
		return plotProperties;
	}

	/**
	 * @param plotProperties
	 *            The plotProperties to set.
	 */
	public void setPlotProperties(
			ViewConfigurationAttributePlotProperties _plotProperties) {
		this.plotProperties = _plotProperties;
	}

	/**
	 * 23 août 2005
	 */
	public static void resetCurrentProperties() {
		currentProperties = null;
		currentProperties = new ViewConfigurationAttributeProperties();
	}

	/**
	 * @param currentProperties2
	 *            23 août 2005
	 */
	public static void setCurrentProperties(
			ViewConfigurationAttributeProperties _currentProperties) {
		currentProperties = _currentProperties;
	}
}
