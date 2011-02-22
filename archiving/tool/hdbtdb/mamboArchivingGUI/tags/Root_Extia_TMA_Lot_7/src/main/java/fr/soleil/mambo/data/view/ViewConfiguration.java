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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.esrf.Tango.DevFailed;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.components.view.VCFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfiguration {

    // MULTI-CONF
    private ViewConfigurationData                data;
    private ViewConfigurationAttributes          attributes;
    private TreeMap<String, ExpressionAttribute> expressions;

    private boolean                              isModified = true;

    public static final String                   XML_TAG    = "viewConfiguration";

    public ViewConfiguration() {
        this.data = new ViewConfigurationData();
        this.attributes = new ViewConfigurationAttributes();
        this.expressions = new TreeMap<String, ExpressionAttribute>(Collator
                .getInstance());
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
    public void setAttributes(ViewConfigurationAttributes attributes) {
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
    public void setData(ViewConfigurationData data) {
        this.data = data;
    }

    public boolean isNew() {
        return this.data.getCreationDate() == null;
    }

    /**
     * @return
     */
    public ViewConfiguration cloneVC() {
        ViewConfiguration ret = new ViewConfiguration();

        ret.setModified(this.isModified());
        ret.setPath(this.getPath() == null ? null : new String(this.getPath()));
        ret.setData(this.getData().cloneData());
        ret.setAttributes(this.getAttributes().cloneAttrs());
        ret.setExpressions(this.getExpressionsDuplicata());

        return ret;
    }

    /**
     * @param name
     * @return 25 ao�t 2005
     */
    public boolean containsAttribute(String completeName) {
        return attributes.getAttributes().containsKey(completeName);
    }

    public ViewConfigurationAttribute getAttribute(String completeName) {
        return attributes.getAttributes().get(completeName);
    }

    /**
     * @return 26 ao�t 2005
     */
    public ChartViewer getChart() {
        ChartViewer chart = data.getChart();
        try {
            boolean historic = data.isHistoric();
            if (data.isDynamicDateRange()) {
                Timestamp[] range = data.getDynamicStartAndEndDates();
                chart = attributes.setChartAttributes(chart, range[0]
                        .toString(), range[1].toString(), historic, data
                        .getSamplingType());
            } else {
                chart = attributes.setChartAttributes(chart, data
                        .getStartDate().toString(), data.getEndDate()
                        .toString(), historic, data.getSamplingType());
            }
        } catch (DevFailed e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return chart;
    }

    public ChartViewer getChartLight() {
        ChartViewer chart = data.getChart();
        chart.getComponent().setAxisScaleEditionEnabled(false, IChartViewer.X);
        chart.getComponent().setVisible(true);
        return chart;
    }

    public void loadAttributes(ChartViewer chart) {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled())
            return;
        try {
            boolean historic = data.isHistoric();
            if (data.isDynamicDateRange()) {
                Timestamp[] range = data.getDynamicStartAndEndDates();
                chart = attributes.setChartAttributes(chart, range[0]
                        .toString(), range[1].toString(), historic, data
                        .getSamplingType());
            } else {
                chart = attributes.setChartAttributes(chart, data
                        .getStartDate().toString(), data.getEndDate()
                        .toString(), historic, data.getSamplingType());
            }

        } catch (DevFailed e) {
            e.printStackTrace();
        } catch (ParseException e) {
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
    public void setModified(boolean isModified) {
        this.isModified = isModified;
        OpenedVCComboBox.refresh();
    }

    /**
     * @param saveLocation
     */
    public void setPath(String path) {
        if (this.data != null) {
            this.data.setPath(path);
        }
    }

    public String getPath() {
        if (this.data != null) {
            return this.data.getPath();
        } else {
            return null;
        }
    }

    public boolean equals(ViewConfiguration this2) {
        boolean ret;
        boolean dataEquals;
        boolean attrEquals;

        String path1 = this.data.getPath() == null ? "" : this.data.getPath();
        String path2 = this2.data.getPath() == null ? "" : this2.data.getPath();
        if (path1.equals(path2)) {
            return true;
        }

        ViewConfigurationData data1 = this.getData();
        ViewConfigurationData data2 = this2.getData();
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

        ViewConfigurationAttributes attrs1 = this.getAttributes();
        ViewConfigurationAttributes attrs2 = this2.getAttributes();
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

    public void save(IViewConfigurationManager manager, boolean saveAs) {
        String pathToUse = getPathToUse(manager, saveAs);
        if (pathToUse == null) {
            return;
        }

        manager.setNonDefaultSaveLocation(pathToUse);
        ILogger logger = LoggerFactory.getCurrentImpl();
        try {
            this.setPath(pathToUse);
            this.setModified(false);
            manager.saveViewConfiguration(this, null);

            String msg = Messages
                    .getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_OK");
            logger.trace(ILogger.LEVEL_DEBUG, msg);
        } catch (Exception e) {
            String msg = Messages
                    .getLogMessage("SAVE_VIEW_CONFIGURATION_ACTION_KO");
            logger.trace(ILogger.LEVEL_ERROR, msg);
            logger.trace(ILogger.LEVEL_ERROR, e);

            this.setPath(null);
            this.setModified(true);

            return;
        }
    }

    /**
     * @return
     */
    private String getPathToUse(IViewConfigurationManager manager,
            boolean saveAs) {
        String pathToUse = this.getPath();
        JFileChooser chooser = new JFileChooser();
        VCFileFilter VCfilter = new VCFileFilter();
        chooser.addChoosableFileFilter(VCfilter);

        if (pathToUse != null) {
            if (saveAs) {
                chooser.setCurrentDirectory(new File(pathToUse));
            } else {
                return pathToUse;
            }
        } else {
            chooser.setCurrentDirectory(new File(manager
                    .getDefaultSaveLocation()));
        }

        // ----show the user what he's saving
        OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        openedVCComboBox.selectElement(this);
        // ----

        int returnVal = chooser.showSaveDialog(MamboFrame.getInstance());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            String path = f.getAbsolutePath();

            if (f != null) {
                String extension = ConfigurationFileFilter.getExtension(f);
                String expectedExtension = VCfilter.getExtension();

                if (extension == null
                        || !extension.equalsIgnoreCase(expectedExtension)) {
                    path += ".";
                    path += expectedExtension;
                }
            }
            if (f.exists()) {
                int choice = JOptionPane
                        .showConfirmDialog(
                                MamboFrame.getInstance(),
                                Messages
                                        .getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"),
                                Messages
                                        .getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
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
        this.data.refreshContent();
    }

    public TreeMap<String, ExpressionAttribute> getExpressions() {
        return expressions;
    }

    public TreeMap<String, ExpressionAttribute> getExpressionsDuplicata() {
        if (expressions == null) {
            return null;
        }
        TreeMap<String, ExpressionAttribute> duplicata = new TreeMap<String, ExpressionAttribute>();
        Set<String> keySet = expressions.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            duplicata.put(new String(key), (expressions.get(key)).duplicate());
        }
        return duplicata;
    }

    public void setExpressions(TreeMap<String, ExpressionAttribute> expressions) {
        this.expressions = expressions;
    }

    public void clearExpressions() {
        this.expressions.clear();
    }

    /**
     * Adds a new expression to the ViewConfiguration. Useful when loading a VC
     * from XML.
     * 
     * @param expression
     *            the expression to add
     */
    public void addExpression(ExpressionAttribute expression) {
        expressions.put(expression.getName(), expression);
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        XMLLine openingLine = new XMLLine(ViewConfiguration.XML_TAG,
                XMLLine.OPENING_TAG_CATEGORY);

        Timestamp creationDate = data.getCreationDate();
        Timestamp lastUpdateDate = data.getLastUpdateDate();
        Timestamp startDate = data.getStartDate();
        Timestamp endDate = data.getEndDate();
        boolean isHistoric = data.isHistoric();
        boolean dynamicDateRange = data.isDynamicDateRange();
        String dateRange = data.getDateRange();
        String name = data.getName();
        String path = data.getPath();
        SamplingType samplingType = data.getSamplingType();

        openingLine.setAttribute(
                ViewConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG, isModified
                        + "");
        if (creationDate != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.CREATION_DATE_PROPERTY_XML_TAG,
                    creationDate.toString());
        }
        if (lastUpdateDate != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG,
                    lastUpdateDate.toString());
        }
        if (startDate != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.START_DATE_PROPERTY_XML_TAG,
                    startDate.toString());
        }
        if (endDate != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.END_DATE_PROPERTY_XML_TAG, endDate
                            .toString());
        }
        openingLine.setAttribute(
                ViewConfigurationData.HISTORIC_PROPERTY_XML_TAG, String
                        .valueOf(isHistoric));
        openingLine.setAttribute(
                ViewConfigurationData.DYNAMIC_DATE_RANGE_PROPERTY_XML_TAG,
                String.valueOf(dynamicDateRange));
        openingLine.setAttribute(
                ViewConfigurationData.DATE_RANGE_PROPERTY_XML_TAG, dateRange);

        if (name != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.NAME_PROPERTY_XML_TAG, name);
        }
        if (path != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.PATH_PROPERTY_XML_TAG, path);
        }
        // System.out.println (
        // "---------------------------------------/samplingType/"+samplingType+"/"
        // );
        if (samplingType != null) {
            openingLine.setAttribute(
                    ViewConfigurationData.SAMPLING_TYPE_PROPERTY_XML_TAG,
                    samplingType.toString());
            openingLine.setAttribute(
                    ViewConfigurationData.SAMPLING_FACTOR_PROPERTY_XML_TAG, ""
                            + samplingType.getAdditionalFilteringFactor());
        }

        XMLLine closingLine = new XMLLine(ViewConfiguration.XML_TAG,
                XMLLine.CLOSING_TAG_CATEGORY);

        ret.append(openingLine.toString());
        ret.append(GUIUtilities.CRLF);

        if (this.data != null) {
            ret.append(this.data.toString());
            ret.append(GUIUtilities.CRLF);
        }

        // parameters specific to groups of attributes
        if (this.attributes != null) {
            ret.append(this.attributes.toString());
        }

        if (expressions != null) {
            for (ExpressionAttribute expression : expressions.values()) {
                ret.append(expression.toString());
            }
        }

        ret.append(closingLine.toString());

        return ret.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ViewConfiguration) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    public String getDisplayableTitle() {
        StringBuffer textBuffer = new StringBuffer();
        textBuffer.append(data.getName());
        Timestamp lastUpdateDate = data.getLastUpdateDate();
        if (lastUpdateDate != null) {
            textBuffer.append(" : ");
            textBuffer.append(lastUpdateDate.toString());
        }
        return textBuffer.toString();
    }
}
