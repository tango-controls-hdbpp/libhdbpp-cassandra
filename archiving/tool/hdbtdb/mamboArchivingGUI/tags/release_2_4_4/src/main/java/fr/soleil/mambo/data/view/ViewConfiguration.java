// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfiguration.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewConfiguration.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.14 $
//
// $Log: ViewConfiguration.java,v $
// Revision 1.14 2008/04/09 10:45:46 achouri
// no message
//
// Revision 1.13 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.12 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.11 2006/12/06 15:12:22 ounsy
// added parametrisable sampling
//
// Revision 1.10 2006/10/04 09:59:40 ounsy
// modified the cloneVC() method to something cleaner
//
// Revision 1.9 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.8 2006/09/18 08:48:30 ounsy
// added start and end dates in charts
//
// Revision 1.7 2006/09/05 14:15:52 ounsy
// updated for sampling compatibility
//
// Revision 1.6 2006/09/05 14:02:23 ounsy
// updated for sampling compatibility
//
// Revision 1.5 2006/08/29 14:19:27 ounsy
// now the view dialog is filled through a thread. Closing the dialog will kill
// this thread.
//
// Revision 1.4 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.3 2006/01/13 13:37:25 ounsy
// File replacement warning
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
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
package fr.soleil.mambo.data.view;

import java.io.File;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.esrf.Tango.DevFailed;
import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.components.view.VCFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfiguration {

    final static ILogger logger = GUILoggerFactory.getLogger();

    // MULTI-CONF
    private ViewConfigurationData data;
    private ViewConfigurationAttributes attributes;
    private TreeMap<String, ExpressionAttribute> expressions;

    private boolean isModified = true;

    public static final String XML_TAG = "viewConfiguration";

    private String currentId = null;

    public ViewConfiguration() {
	super();
	setData(new ViewConfigurationData());
	setAttributes(new ViewConfigurationAttributes());
	setExpressions(new TreeMap<String, ExpressionAttribute>(Collator.getInstance()));
	setModified(true);
    }

    public void computeCurrentId() {
	currentId = null;
	final String selectedViewName = getDisplayableTitle();
	String lastUpdate = null;
	if (getData() != null && getData().getLastUpdateDate() != null) {
	    lastUpdate = Long.toString(getData().getLastUpdateDate().getTime());
	    currentId = selectedViewName + " - " + lastUpdate;
	}
    }

    public String getCurrentId() {
	return currentId;
    }

    /**
     * @return Returns the attributes.
     */
    public ViewConfigurationAttributes getAttributes() {
	return attributes;
    }

    /**
     * @param attributes
     *            The attributes to set.
     */
    public void setAttributes(final ViewConfigurationAttributes attributes) {
	this.attributes = attributes;
	if (attributes != null) {
	    attributes.setViewConfiguration(this);
	}
    }

    /**
     * @return Returns the data.
     */
    public ViewConfigurationData getData() {
	return data;
    }

    /**
     * @param data
     *            The data to set.
     */
    public void setData(final ViewConfigurationData data) {
	this.data = data;
    }

    public boolean isNew() {
	return data.getCreationDate() == null;
    }

    /**
     * @return
     */
    public ViewConfiguration cloneVC() {
	final ViewConfiguration ret = new ViewConfiguration();

	ret.setModified(isModified());
	ret.setPath(getPath() == null ? null : new String(getPath()));
	ret.setData(getData().cloneData());
	ret.setAttributes(getAttributes().cloneAttrs());
	ret.setExpressions(getExpressionsDuplicata());

	return ret;
    }

    /**
     * @param name
     * @return 25 ao�t 2005
     */
    public boolean containsAttribute(final String completeName) {
	return attributes.getAttributes().containsKey(completeName);
    }

    public ViewConfigurationAttribute getAttribute(final String completeName) {
	return attributes.getAttributes().get(completeName);
    }

    /**
     * @return 26 ao�t 2005
     */
    public ChartViewer getChart(final String idViewSelected) {
	ChartViewer chart = data.getChart();
	try {
	    final boolean historic = data.isHistoric();
	    attributes.setIdViewSelected(idViewSelected);
	    if (data.isDynamicDateRange()) {
		final Timestamp[] range = data.getDynamicStartAndEndDates();
		chart = attributes.setChartAttributes(chart, range[0].toString(), range[1]
			.toString(), historic, data.getSamplingType());
	    } else {
		chart = attributes.setChartAttributes(chart, data.getStartDate().toString(), data
			.getEndDate().toString(), historic, data.getSamplingType());
	    }
	} catch (final DevFailed e) {
	    e.printStackTrace();
	} catch (final ParseException e) {
	    e.printStackTrace();
	}
	return chart;
    }

    public ChartViewer getChartLight() {
	final ChartViewer chart = data.getChart();
	chart.getComponent().setAxisScaleEditionEnabled(false, IChartViewer.X);
	chart.getComponent().setVisible(true);
	chart.getComponent().setFileDirectoryPath(Mambo.getPathToResources());
	return chart;
    }

    public void loadAttributes(ChartViewer chart) {
	final IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();

	if (extractingManager.isCanceled()) {
	    return;
	}
	try {
	    final boolean historic = data.isHistoric();
	    attributes.setIdViewSelected(currentId);
	    if (data.isDynamicDateRange()) {
		final Timestamp[] range = data.getDynamicStartAndEndDates();
		chart = attributes.setChartAttributes(chart, range[0].toString(), range[1]
			.toString(), historic, data.getSamplingType());
	    } else {
		chart = attributes.setChartAttributes(chart, data.getStartDate().toString(), data
			.getEndDate().toString(), historic, data.getSamplingType());
	    }

	} catch (final DevFailed e) {
	    e.printStackTrace();
	} catch (final ParseException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @return Returns the isModified.
     */
    public boolean isModified() {
	return isModified;
    }

    /**
     * @param isModified
     *            The isModified to set.
     */
    public void setModified(final boolean isModified) {
	this.isModified = isModified;
	OpenedVCComboBox.refresh();
    }

    /**
     * @param saveLocation
     */
    public void setPath(final String path) {
	if (data != null) {
	    data.setPath(path);
	}
    }

    public String getPath() {
	if (data != null) {
	    return data.getPath();
	} else {
	    return null;
	}
    }

    public void save(final IViewConfigurationManager manager, final boolean saveAs) {
	final String pathToUse = getPathToUse(manager, saveAs);
	if (pathToUse == null) {
	    return;
	}

	manager.setNonDefaultSaveLocation(pathToUse);
	try {
	    setPath(pathToUse);
	    setModified(false);
	    manager.saveViewConfiguration(this, null);

	    final String msg = Messages.getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);

	    setPath(null);
	    setModified(true);

	    return;
	}
    }

    /**
     * @return
     */
    private String getPathToUse(final IViewConfigurationManager manager, final boolean saveAs) {
	final String pathToUse = getPath();
	final JFileChooser chooser = new JFileChooser();
	final VCFileFilter VCfilter = new VCFileFilter();
	chooser.addChoosableFileFilter(VCfilter);

	if (pathToUse != null) {
	    if (saveAs) {
		chooser.setCurrentDirectory(new File(pathToUse));
	    } else {
		return pathToUse;
	    }
	} else {
	    chooser.setCurrentDirectory(new File(manager.getDefaultSaveLocation()));
	}

	// ----show the user what he's saving
	final OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
	openedVCComboBox.selectElement(this);
	// ----

	final int returnVal = chooser.showSaveDialog(MamboFrame.getInstance());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    final File f = chooser.getSelectedFile();
	    String path = f.getAbsolutePath();

	    if (f != null) {
		final String extension = ConfigurationFileFilter.getExtension(f);
		final String expectedExtension = VCfilter.getExtension();

		if (extension == null || !extension.equalsIgnoreCase(expectedExtension)) {
		    path += ".";
		    path += expectedExtension;
		}
	    }
	    if (f.exists()) {
		final int choice = JOptionPane.showConfirmDialog(MamboFrame.getInstance(), Messages
			.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"), Messages
			.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"),
			JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (choice != JOptionPane.OK_OPTION) {
		    return null;
		}
	    }
	    manager.setNonDefaultSaveLocation(path);
	    return path;
	} else {
	    return null;
	}

    }

    public void refreshContent() {
	data.refreshContent();
    }

    public TreeMap<String, ExpressionAttribute> getExpressions() {
	return expressions;
    }

    public TreeMap<String, ExpressionAttribute> getExpressionsDuplicata() {
	if (expressions == null) {
	    return null;
	}
	final TreeMap<String, ExpressionAttribute> duplicata = new TreeMap<String, ExpressionAttribute>();
	final Set<String> keySet = expressions.keySet();
	final Iterator<String> keyIterator = keySet.iterator();
	while (keyIterator.hasNext()) {
	    final String key = keyIterator.next();
	    duplicata.put(new String(key), expressions.get(key).duplicate());
	}
	return duplicata;
    }

    public void setExpressions(final TreeMap<String, ExpressionAttribute> expressions) {
	this.expressions = expressions;
    }

    public void clearExpressions() {
	expressions.clear();
    }

    /**
     * Adds a new expression to the ViewConfiguration. Useful when loading a VC
     * from XML.
     * 
     * @param expression
     *            the expression to add
     */
    public void addExpression(final ExpressionAttribute expression) {
	expressions.put(expression.getName(), expression);
    }

    @Override
    public String toString() {
	final StringBuilder ret = new StringBuilder();

	final XMLLine openingLine = new XMLLine(ViewConfiguration.XML_TAG,
		XMLLine.OPENING_TAG_CATEGORY);

	final Timestamp creationDate = data.getCreationDate();
	final Timestamp lastUpdateDate = data.getLastUpdateDate();
	final Timestamp startDate = data.getStartDate();
	final Timestamp endDate = data.getEndDate();
	final boolean isHistoric = data.isHistoric();
	final boolean dynamicDateRange = data.isDynamicDateRange();
	final String dateRange = data.getDateRange();
	final String name = data.getName();
	final String path = data.getPath();
	final SamplingType samplingType = data.getSamplingType();

	openingLine.setAttribute(ViewConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG, isModified
		+ "");
	if (creationDate != null) {
	    openingLine.setAttribute(ViewConfigurationData.CREATION_DATE_PROPERTY_XML_TAG,
		    creationDate.toString());
	}
	if (lastUpdateDate != null) {
	    openingLine.setAttribute(ViewConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG,
		    lastUpdateDate.toString());
	}
	if (startDate != null) {
	    openingLine.setAttribute(ViewConfigurationData.START_DATE_PROPERTY_XML_TAG, startDate
		    .toString());
	}
	if (endDate != null) {
	    openingLine.setAttribute(ViewConfigurationData.END_DATE_PROPERTY_XML_TAG, endDate
		    .toString());
	}
	openingLine.setAttribute(ViewConfigurationData.HISTORIC_PROPERTY_XML_TAG, String
		.valueOf(isHistoric));
	openingLine.setAttribute(ViewConfigurationData.DYNAMIC_DATE_RANGE_PROPERTY_XML_TAG, String
		.valueOf(dynamicDateRange));
	openingLine.setAttribute(ViewConfigurationData.DATE_RANGE_PROPERTY_XML_TAG, dateRange);

	if (name != null) {
	    openingLine.setAttribute(ViewConfigurationData.NAME_PROPERTY_XML_TAG, name);
	}
	if (path != null) {
	    openingLine.setAttribute(ViewConfigurationData.PATH_PROPERTY_XML_TAG, path);
	}
	// System.out.println (
	// "---------------------------------------/samplingType/"+samplingType+"/"
	// );
	if (samplingType != null) {
	    openingLine.setAttribute(ViewConfigurationData.SAMPLING_TYPE_PROPERTY_XML_TAG,
		    samplingType.toString());
	    openingLine.setAttribute(ViewConfigurationData.SAMPLING_FACTOR_PROPERTY_XML_TAG, ""
		    + samplingType.getAdditionalFilteringFactor());
	}

	final XMLLine closingLine = new XMLLine(ViewConfiguration.XML_TAG,
		XMLLine.CLOSING_TAG_CATEGORY);

	ret.append(openingLine.toString());
	ret.append(GUIUtilities.CRLF);

	if (data != null) {
	    ret.append(data.toString());
	    ret.append(GUIUtilities.CRLF);
	}

	// parameters specific to groups of attributes
	if (attributes != null) {
	    ret.append(attributes.toString());
	}

	if (expressions != null) {
	    for (final ExpressionAttribute expression : expressions.values()) {
		ret.append(expression.toString());
	    }
	}

	ret.append(closingLine.toString());

	return ret.toString();
    }

    @Override
    public boolean equals(final Object obj) {
	if (obj instanceof ViewConfiguration) {
	    return vcEquals((ViewConfiguration) obj);
	}
	return false;
    }

    public boolean vcEquals(final ViewConfiguration this2) {
	boolean ret;
	boolean dataEquals;
	boolean attrEquals;
	boolean pathEquals;

	final String path1 = data.getPath() == null ? "" : data.getPath();
	final String path2 = this2.data.getPath() == null ? "" : this2.data.getPath();
	pathEquals = path1.equals(path2);

	final ViewConfigurationData data1 = getData();
	final ViewConfigurationData data2 = this2.getData();
	if (data1 == null) {
	    if (data2 == null) {
		dataEquals = true;
	    } else {
		dataEquals = false;
	    }
	} else {
	    if (data2 == null) {
		dataEquals = false;
	    } else {
		dataEquals = data1.equals(data2);
	    }
	}

	final ViewConfigurationAttributes attrs1 = getAttributes();
	final ViewConfigurationAttributes attrs2 = this2.getAttributes();
	if (attrs1 == null) {
	    if (attrs2 == null) {
		attrEquals = true;
	    } else {
		attrEquals = false;
	    }
	} else {
	    if (attrs2 == null) {
		attrEquals = false;
	    } else {
		attrEquals = attrs1.equals(attrs2);
	    }
	}

	ret = dataEquals && attrEquals && pathEquals;
	return ret;
    }

    public String getDisplayableTitle() {
	final StringBuffer textBuffer = new StringBuffer();
	textBuffer.append(data.getName());
	final Timestamp lastUpdateDate = data.getLastUpdateDate();
	if (lastUpdateDate != null) {
	    textBuffer.append(" : ");
	    textBuffer.append(lastUpdateDate.toString());
	}
	return textBuffer.toString();
    }

    public void setChartAttributeExpressionOfString(final HashMap<String, DbData[]> dataMap,
	    final ChartViewer chart) {
	attributes
		.setChartAttributeExpressionOfString(dataMap, data.isHistoric(), chart, currentId);
    }
}
