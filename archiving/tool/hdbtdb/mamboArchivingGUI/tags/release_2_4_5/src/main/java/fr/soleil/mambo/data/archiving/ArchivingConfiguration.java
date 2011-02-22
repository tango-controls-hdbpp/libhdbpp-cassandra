// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfiguration.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ArchivingConfiguration.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: ArchivingConfiguration.java,v $
// Revision 1.12 2007/12/12 17:49:51 pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.11 2007/02/01 14:16:29 pierrejoseph
// XmlHelper reorg
//
// Revision 1.10 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.9 2006/07/27 12:46:08 ounsy
// corrected bugs in updateCurrentAbsoluteMode and updateCurrentRelativeMode
//
// Revision 1.8 2006/03/27 14:07:27 ounsy
// Absolute and relative modes's lowerValue field is now always negative
//
// Revision 1.7 2006/03/20 10:35:42 ounsy
// removed useless logs
//
// Revision 1.6 2006/02/27 09:34:58 ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.5 2006/02/24 12:20:23 ounsy
// modified for HDB/TDB separation
//
// Revision 1.4 2006/01/13 13:37:04 ounsy
// File replacement warning
//
// Revision 1.3 2005/12/15 11:34:15 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:56 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.data.archiving;

import java.io.File;
import java.sql.Timestamp;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.archiving.ACFileFilter;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.archiving.ArchivingAttributesDetailPanel;
import fr.soleil.mambo.containers.archiving.ArchivingGeneralPanel;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.containers.archiving.dialogs.GeneralTab;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ArchivingConfiguration {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static ArchivingConfiguration selectedArchivingConfiguration;
    private static ArchivingConfiguration[] openedArchivingConfigurations;

    private static ArchivingConfiguration currentArchivingConfiguration = null;

    private ArchivingConfigurationData data;
    private ArchivingConfigurationAttributes attributes;
    public static final String XML_TAG = "archivingConfiguration";

    private boolean isModified = true;
    private boolean isHistoric = Mambo.isHdbAvailable();

    public static final int EMPTY_ATTRIBUTES = 1;

    public ArchivingConfiguration() {
	data = new ArchivingConfigurationData();
	attributes = new ArchivingConfigurationAttributes();

	attributes.setArchivingConfiguration(this);
    }

    public ArchivingConfiguration(final ArchivingConfigurationData _data,
	    final ArchivingConfigurationAttributes _attributes) {
	data = _data;
	attributes = _attributes;

	data.setArchivingConfiguration(this);
	attributes.setArchivingConfiguration(this);
    }

    /**
     * @param _contextData
     */
    public ArchivingConfiguration(final ArchivingConfigurationData _data) {
	data = _data;
    }

    public boolean containsAttribute(final String completeName) {
	return attributes.getAttributesMap().containsKey(completeName);
    }

    @Override
    public String toString() {
	String ret = "";

	final XMLLine openingLine = new XMLLine(ArchivingConfiguration.XML_TAG,
		XMLLine.OPENING_TAG_CATEGORY);

	openingLine.setAttribute(ArchivingConfigurationData.IS_HISTORIC_PROPERTY_XML_TAG,
		isHistoric + "");
	openingLine.setAttribute(ArchivingConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG,
		isModified + "");

	if (getCreationDate() != null) {
	    openingLine.setAttribute(ArchivingConfigurationData.CREATION_DATE_PROPERTY_XML_TAG,
		    getCreationDate().toString());
	}
	if (getLastUpdateDate() != null) {
	    openingLine.setAttribute(ArchivingConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG,
		    getLastUpdateDate().toString());
	}
	if (getName() != null) {
	    openingLine.setAttribute(ArchivingConfigurationData.NAME_PROPERTY_XML_TAG, getName());
	}
	if (getPath() != null) {
	    openingLine.setAttribute(ArchivingConfigurationData.PATH_PROPERTY_XML_TAG, getPath());
	}

	final XMLLine closingLine = new XMLLine(ArchivingConfiguration.XML_TAG,
		XMLLine.CLOSING_TAG_CATEGORY);

	ret += openingLine.toString();
	ret += GUIUtilities.CRLF;

	if (attributes != null) {
	    ret += attributes.toString();
	}

	ret += closingLine.toString();

	// ret = XMLUtils.replaceXMLChars ( ret );

	return ret;
    }

    public static ArchivingConfiguration[] findArchivingConfigurations(
	    final Criterions searchCriterions, final boolean forceGlobalReload) throws Exception {
	final IArchivingConfigurationManager manager = ArchivingConfigurationManagerFactory
		.getCurrentImpl();
	ArchivingConfiguration[] ret;

	if (openedArchivingConfigurations == null || forceGlobalReload) {
	    ret = manager.loadArchivingConfigurations(searchCriterions);
	} else {
	    ret = manager.findArchivingConfigurations(openedArchivingConfigurations,
		    searchCriterions);
	}

	return ret;
    }

    /**
     * @return Returns the selectedArchivingConfiguration.
     */
    public static ArchivingConfiguration getSelectedArchivingConfiguration() {
	return selectedArchivingConfiguration;
    }

    /**
     * @param selectedArchivingConfiguration
     *            The selectedArchivingConfiguration to set.
     */
    public static void setSelectedArchivingConfiguration(
	    final ArchivingConfiguration selectedArchivingConfiguration) {
	ArchivingConfiguration.selectedArchivingConfiguration = selectedArchivingConfiguration;
    }

    /**
     * @return Returns the attributes.
     */
    public ArchivingConfigurationAttributes getAttributes() {
	return attributes;
    }

    /**
     * @param attributes
     *            The attributes to set.
     */
    public void setAttributes(final ArchivingConfigurationAttributes attributes) {
	this.attributes = attributes;
    }

    /**
     * @return Returns the data.
     */
    public ArchivingConfigurationData getData() {
	return data;
    }

    /**
     * @param data
     *            The data to set.
     */
    public void setData(final ArchivingConfigurationData data) {
	this.data = data;
    }

    /**
     * @return Returns the currentArchivingConfiguration.
     */
    public static ArchivingConfiguration getCurrentArchivingConfiguration() {
	return currentArchivingConfiguration;
    }

    /**
     * @param currentArchivingConfiguration
     *            The currentArchivingConfiguration to set.
     */
    public static void setCurrentArchivingConfiguration(
	    final ArchivingConfiguration currentArchivingConfiguration) {
	ArchivingConfiguration.currentArchivingConfiguration = currentArchivingConfiguration;
    }

    public String getName() {
	if (data == null) {
	    return null;
	}

	return data.getName();
    }

    /**
     * @return Returns the creationDate.
     */
    public Timestamp getCreationDate() {
	if (data == null) {
	    return null;
	}

	return data.getCreationDate();
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(final Timestamp creationDate) {
	if (data == null) {
	    return;
	}
	data.setCreationDate(creationDate);
    }

    /**
     * @return Returns the lastUpdateDate.
     */
    public Timestamp getLastUpdateDate() {
	if (data == null) {
	    return null;
	}

	return data.getLastUpdateDate();
    }

    /**
     * @param lastUpdateDate
     *            The lastUpdateDate to set.
     */
    public void setLastUpdateDate(final Timestamp lastUpdateDate) {
	if (data == null) {
	    return;
	}
	data.setLastUpdateDate(lastUpdateDate);
    }

    /**
     * @return 27 juil. 2005
     */
    public boolean isNew() {
	if (data == null) {
	    return true;
	}

	return getCreationDate() == null;
    }

    public void push() {
	ArchivingConfiguration.setCurrentArchivingConfiguration(cloneAC());
	ArchivingConfiguration.setSelectedArchivingConfiguration(this);

	if (Mambo.hasACs()) {
	    ArchivingAttributesDetailPanel.getInstance(isHistoric).refresh(isHistoric);

	    final ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
	    model.removeAll();
	    if (ACEditDialog.getInstance() != null
		    && ACEditDialog.getInstance().getAttributeTableSelectionBean() != null) {
		ACEditDialog.getInstance().getAttributeTableSelectionBean().getSelectionPanel()
			.getAttributesSelectTable().getModel().reset();
	    }

	    if (attributes != null) {
		attributes.push();
	    }

	    final ArchivingGeneralPanel archivingGeneralPanel = ArchivingGeneralPanel.getInstance();
	    archivingGeneralPanel.setLastUpdateDate(getLastUpdateDate());
	    archivingGeneralPanel.setCreationDate(getCreationDate());
	    archivingGeneralPanel.setName(getName());
	    archivingGeneralPanel.setPath(getPath());

	    final GeneralTab generalTab = GeneralTab.getInstance();
	    generalTab.setLastUpdateDate(getLastUpdateDate());
	    generalTab.setCreationDate(getCreationDate());
	    generalTab.setName(getName());
	    generalTab.setHistoric(isHistoric);

	    final OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
	    if (openedACComboBox != null) {
		openedACComboBox.selectElement(this);
	    }
	}
    }

    public void pushLight() {
	ArchivingConfiguration.setCurrentArchivingConfiguration(cloneAC());

	if (Mambo.hasACs()) {
	    final ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
	    model.removeAll();
	    if (ACEditDialog.getInstance() != null
		    && ACEditDialog.getInstance().getAttributeTableSelectionBean() != null) {
		ACEditDialog.getInstance().getAttributeTableSelectionBean().getSelectionPanel()
			.getAttributesSelectTable().getModel().reset();
	    }

	    if (attributes != null) {
		attributes.push();
	    }

	    final ArchivingGeneralPanel archivingGeneralPanel = ArchivingGeneralPanel.getInstance();
	    archivingGeneralPanel.setLastUpdateDate(getLastUpdateDate());
	    archivingGeneralPanel.setCreationDate(getCreationDate());
	    archivingGeneralPanel.setName(getName());
	    archivingGeneralPanel.setPath(getPath());

	    final GeneralTab generalTab = GeneralTab.getInstance();
	    generalTab.setLastUpdateDate(getLastUpdateDate());
	    generalTab.setCreationDate(getCreationDate());
	    generalTab.setName(getName());
	    generalTab.setHistoric(isHistoric);
	}
    }

    private ArchivingConfiguration cloneAC() {
	final ArchivingConfiguration ret = new ArchivingConfiguration();

	ret.setModified(isModified());
	ret.setHistoric(isHistoric());
	ret.setPath(getPath());
	ret.setData(getData());
	ret.setAttributes(getAttributes().cloneAttrs());

	return ret;
    }

    /**
     * @return 9 sept. 2005
     */
    public void controlValues() throws ArchivingConfigurationException {
	if (attributes == null) {
	    throw new ArchivingConfigurationException(null, EMPTY_ATTRIBUTES);
	} else {
	    attributes.controlValues();
	}
    }

    /**
     * @throws ArchivingException
     */
    public static void updateCurrentModes(final boolean currentACIsHistoric)
	    throws ArchivingException {
	// currentProperties.setHDBProperties( HDBProperties );
	final ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
		.getCurrentProperties();

	if (currentACIsHistoric) {
	    updateCurrentAbsoluteMode(true);
	    updateCurrentRelativeMode(true);
	    updateCurrentThresholdMode(true);
	    updateCurrentPeriodicMode(true);
	    updateCurrentDifferenceMode(true);

	    currentProperties.setTDBProperties(new ArchivingConfigurationAttributeTDBProperties());
	} else {
	    updateCurrentAbsoluteMode(false);
	    updateCurrentRelativeMode(false);
	    updateCurrentThresholdMode(false);
	    updateCurrentPeriodicMode(false);
	    updateCurrentDifferenceMode(false);

	    currentProperties.setHDBProperties(new ArchivingConfigurationAttributeHDBProperties());

	    /*
	     * System.out.println ( "CLA updateCurrentModes---------------" );
	     * System.out.println ( currentProperties.toString() );
	     * System.out.println ( "CLA updateCurrentModes---------------" );
	     */
	}

	ArchivingConfigurationAttributeProperties.setCurrentProperties(currentProperties);
    }

    /**
     * @param b
     * @throws ArchivingException
     */
    private static void updateCurrentAbsoluteMode(final boolean historic) {
	final AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
	final boolean selected = historic ? panel.hasModeHDB_A() : panel.hasModeTDB_A();

	final Mode mode = new Mode();
	final ArchivingConfigurationMode currentMode = new ArchivingConfigurationMode(mode);
	currentMode.setType(ArchivingConfigurationMode.TYPE_A);
	ArchivingConfigurationMode.setCurrentMode(currentMode);
	final ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
		.getCurrentProperties();

	if (!selected) {
	    currentMode.getMode().setModeA(null);

	    if (historic) {
		final ArchivingConfigurationAttributeHDBProperties HDBProperties = currentProperties
			.getHDBProperties();
		HDBProperties.removeMode(ArchivingConfigurationMode.TYPE_A);
		currentProperties.setHDBProperties(HDBProperties);
	    } else {
		final ArchivingConfigurationAttributeTDBProperties TDBProperties = currentProperties
			.getTDBProperties();
		TDBProperties.removeMode(ArchivingConfigurationMode.TYPE_A);
		currentProperties.setTDBProperties(TDBProperties);
	    }
	} else {
	    final int absolutePeriod = historic ? panel.getHDBAbsolutePeriod() : panel
		    .getTDBAbsolutePeriod();
	    double upperLimit = historic ? Double.parseDouble(panel.getHDBAbsoluteUpper()) : Double
		    .parseDouble(panel.getTDBAbsoluteUpper());
	    double lowerLimit = historic ? Double.parseDouble(panel.getHDBAbsoluteLower()) : Double
		    .parseDouble(panel.getTDBAbsoluteLower());
	    final boolean slowDrift = historic ? panel.isHDBAbsoluteDLCheck() : panel
		    .isTDBAbsoluteDLCheck();
	    upperLimit = Math.abs(upperLimit);
	    lowerLimit = Math.abs(lowerLimit);

	    final ModeAbsolu ModeA = new ModeAbsolu(absolutePeriod, lowerLimit, upperLimit,
		    slowDrift);

	    currentMode.getMode().setModeA(ModeA);
	    currentProperties.addMode(currentMode, historic);
	}

	ArchivingConfigurationAttributeProperties.setCurrentProperties(currentProperties);
    }

    /**
     * @param b
     * @throws ArchivingException
     */
    private static void updateCurrentRelativeMode(final boolean historic) throws ArchivingException {
	final AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
	final boolean selected = historic ? panel.hasModeHDB_R() : panel.hasModeTDB_R();

	final Mode mode = new Mode();
	final ArchivingConfigurationMode currentMode = new ArchivingConfigurationMode(mode);
	currentMode.setType(ArchivingConfigurationMode.TYPE_R);
	ArchivingConfigurationMode.setCurrentMode(currentMode);
	final ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
		.getCurrentProperties();

	if (!selected) {
	    currentMode.getMode().setModeR(null);

	    if (historic) {
		final ArchivingConfigurationAttributeHDBProperties HDBProperties = currentProperties
			.getHDBProperties();
		HDBProperties.removeMode(ArchivingConfigurationMode.TYPE_R);
		currentProperties.setHDBProperties(HDBProperties);
	    } else {
		final ArchivingConfigurationAttributeTDBProperties TDBProperties = currentProperties
			.getTDBProperties();
		TDBProperties.removeMode(ArchivingConfigurationMode.TYPE_R);
		currentProperties.setTDBProperties(TDBProperties);
	    }
	} else {
	    final int relativePeriod = historic ? panel.getHDBRelativePeriod() : panel
		    .getTDBRelativePeriod();
	    double upperLimit = historic ? Double.parseDouble(panel.getHDBRelativeUpper()) : Double
		    .parseDouble(panel.getTDBRelativeUpper());
	    double lowerLimit = historic ? Double.parseDouble(panel.getHDBRelativeLower()) : Double
		    .parseDouble(panel.getTDBRelativeLower());
	    final boolean slowDrift = historic ? panel.isHDBRelativeDLCheck() : panel
		    .isTDBRelativeDLCheck();
	    upperLimit = Math.abs(upperLimit);
	    lowerLimit = Math.abs(lowerLimit);

	    final ModeRelatif ModeR = new ModeRelatif(relativePeriod, lowerLimit, upperLimit,
		    slowDrift);

	    currentMode.getMode().setModeR(ModeR);
	    currentProperties.addMode(currentMode, historic);
	}

	ArchivingConfigurationAttributeProperties.setCurrentProperties(currentProperties);
    }

    private static void updateCurrentThresholdMode(final boolean historic)
	    throws ArchivingException {
	final AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
	final boolean selected = historic ? panel.hasModeHDB_T() : panel.hasModeTDB_T();

	final Mode mode = new Mode();
	final ArchivingConfigurationMode currentMode = new ArchivingConfigurationMode(mode);
	currentMode.setType(ArchivingConfigurationMode.TYPE_T);
	ArchivingConfigurationMode.setCurrentMode(currentMode);
	final ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
		.getCurrentProperties();

	if (!selected) {
	    currentMode.getMode().setModeT(null);

	    if (historic) {
		final ArchivingConfigurationAttributeHDBProperties HDBProperties = currentProperties
			.getHDBProperties();
		HDBProperties.removeMode(ArchivingConfigurationMode.TYPE_T);
		currentProperties.setHDBProperties(HDBProperties);
	    } else {
		final ArchivingConfigurationAttributeTDBProperties TDBProperties = currentProperties
			.getTDBProperties();
		TDBProperties.removeMode(ArchivingConfigurationMode.TYPE_T);
		currentProperties.setTDBProperties(TDBProperties);
	    }
	} else {
	    final int thresholdPeriod = historic ? panel.getHDBThresholdPeriod() : panel
		    .getTDBThresholdPeriod();
	    final double upperLimit = historic ? Double.parseDouble(panel.getHDBThresholdUpper())
		    : Double.parseDouble(panel.getTDBThresholdUpper());
	    final double lowerLimit = historic ? Double.parseDouble(panel.getHDBThresholdLower())
		    : Double.parseDouble(panel.getTDBThresholdLower());
	    final ModeSeuil ModeT = new ModeSeuil(thresholdPeriod, lowerLimit, upperLimit);

	    currentMode.getMode().setModeT(ModeT);
	    currentProperties.addMode(currentMode, historic);
	}

	ArchivingConfigurationAttributeProperties.setCurrentProperties(currentProperties);
    }

    private static void updateCurrentPeriodicMode(final boolean historic) throws ArchivingException {
	final AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
	// System.out.println (
	// "CLA/updateCurrentPeriodicMode/historic/"+historic );
	final boolean selected = historic ? panel.hasModeHDB_P() : panel.hasModeTDB_P();
	// System.out.println (
	// "CLA/updateCurrentPeriodicMode/selected/"+selected );

	final Mode mode = new Mode();
	final ArchivingConfigurationMode currentMode = new ArchivingConfigurationMode(mode);
	currentMode.setType(ArchivingConfigurationMode.TYPE_P);
	ArchivingConfigurationMode.setCurrentMode(currentMode);
	final ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
		.getCurrentProperties();

	if (!selected) {
	    currentMode.getMode().setModeP(null);

	    if (historic) {
		final ArchivingConfigurationAttributeHDBProperties HDBProperties = currentProperties
			.getHDBProperties();
		HDBProperties.removeMode(ArchivingConfigurationMode.TYPE_P);
		currentProperties.setHDBProperties(HDBProperties);
	    } else {
		final ArchivingConfigurationAttributeTDBProperties TDBProperties = currentProperties
			.getTDBProperties();
		TDBProperties.removeMode(ArchivingConfigurationMode.TYPE_P);
		currentProperties.setTDBProperties(TDBProperties);
	    }
	} else {
	    final int periodicPeriod = historic ? panel.getHDBperiod() : panel.getTDBperiod();
	    // System.out.println (
	    // "CLA/updateCurrentPeriodicMode/periodicPeriod/"+periodicPeriod );
	    final ModePeriode ModeP = new ModePeriode(periodicPeriod);

	    currentMode.getMode().setModeP(ModeP);
	    currentProperties.addMode(currentMode, historic);
	}

	ArchivingConfigurationAttributeProperties.setCurrentProperties(currentProperties);
    }

    private static void updateCurrentDifferenceMode(final boolean historic)
	    throws ArchivingException {
	final AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
	final boolean selected = historic ? panel.hasModeHDB_D() : panel.hasModeTDB_D();

	final Mode mode = new Mode();
	final ArchivingConfigurationMode currentMode = new ArchivingConfigurationMode(mode);
	currentMode.setType(ArchivingConfigurationMode.TYPE_D);
	ArchivingConfigurationMode.setCurrentMode(currentMode);
	final ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
		.getCurrentProperties();

	if (!selected) {
	    currentMode.getMode().setModeD(null);

	    if (historic) {
		final ArchivingConfigurationAttributeHDBProperties HDBProperties = currentProperties
			.getHDBProperties();
		HDBProperties.removeMode(ArchivingConfigurationMode.TYPE_D);
		currentProperties.setHDBProperties(HDBProperties);
	    } else {
		final ArchivingConfigurationAttributeTDBProperties TDBProperties = currentProperties
			.getTDBProperties();
		TDBProperties.removeMode(ArchivingConfigurationMode.TYPE_D);
		currentProperties.setTDBProperties(TDBProperties);
	    }
	} else {
	    final int differencePeriod = historic ? panel.getHDBDifferencePeriod() : panel
		    .getTDBDifferencePeriod();
	    final ModeDifference ModeD = new ModeDifference(differencePeriod);

	    currentMode.getMode().setModeD(ModeD);
	    currentProperties.addMode(currentMode, historic);
	}

	ArchivingConfigurationAttributeProperties.setCurrentProperties(currentProperties);
    }

    /**
     * @param name
     * @return
     */
    public ArchivingConfigurationAttribute getAttribute(final String name) {
	return attributes.getAttributesMap().get(name);
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
	OpenedACComboBox.refresh();
    }

    public boolean equals(final ArchivingConfiguration this2) {
	boolean ret;
	boolean dataEquals;
	boolean attrEquals;

	final String path1 = data.getPath() == null ? "" : data.getPath();
	final String path2 = this2.data.getPath() == null ? "" : this2.data.getPath();
	if (path1.equals(path2)) {
	    return true;
	}

	final ArchivingConfigurationData data1 = getData();
	final ArchivingConfigurationData data2 = this2.getData();
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

	final ArchivingConfigurationAttributes attrs1 = getAttributes();
	final ArchivingConfigurationAttributes attrs2 = this2.getAttributes();
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

	ret = dataEquals && attrEquals;
	return ret;
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

    /**
     * @param name
     */
    public void setName(final String name) {
	if (data != null) {
	    data.setName(name);
	}
    }

    public void save(final IArchivingConfigurationManager manager, final boolean saveAs) {
	final String pathToUse = getPathToUse(manager, saveAs);
	if (pathToUse == null) {
	    return;
	}

	manager.setNonDefaultSaveLocation(pathToUse);
	try {
	    setPath(pathToUse);
	    setModified(false);
	    manager.saveArchivingConfiguration(this, null);

	    final String msg = Messages.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("SAVE_ARCHIVING_CONFIGURATION_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);

	    setPath(null);
	    setModified(true);

	    return;
	}

	// ----WARNING!!!!!!!!!!!!!!!!!!!!!
	// this.push ();
	final ArchivingGeneralPanel panel = ArchivingGeneralPanel.getInstance();
	panel.setPath(getPath());
	// ----WARNING!!!!!!!!!!!!!!!!!!!!!
    }

    /**
     * @return
     */
    private String getPathToUse(final IArchivingConfigurationManager manager, final boolean saveAs) {
	final String pathToUse = getPath();
	final JFileChooser chooser = new JFileChooser();
	final ACFileFilter ACfilter = new ACFileFilter();
	chooser.addChoosableFileFilter(ACfilter);

	if (pathToUse != null) {
	    if (saveAs) {
		chooser.setCurrentDirectory(new File(pathToUse));
	    } else {
		return pathToUse;
	    }
	} else {
	    // System.out.println (
	    // "CLA/ArchivingConfiguration/getPathToUse/manager.getDefaultSaveLocation ()/"+manager.getDefaultSaveLocation
	    // ()+"/" );
	    chooser.setCurrentDirectory(new File(manager.getDefaultSaveLocation()));
	}

	// ----show the user what he's saving
	final OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
	openedACComboBox.selectElement(this);
	// ----

	final int returnVal = chooser.showSaveDialog(MamboFrame.getInstance());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    final File f = chooser.getSelectedFile();
	    String path = f.getAbsolutePath();

	    if (f != null) {
		final String extension = ConfigurationFileFilter.getExtension(f);
		final String expectedExtension = ACfilter.getExtension();

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

    /**
     * @return Returns the isHistoric.
     */
    public boolean isHistoric() {
	return isHistoric;
    }

    /**
     * @param isHistoric
     *            The isHistoric to set.
     */
    public void setHistoric(final boolean isHistoric) {
	this.isHistoric = isHistoric;
    }
}
