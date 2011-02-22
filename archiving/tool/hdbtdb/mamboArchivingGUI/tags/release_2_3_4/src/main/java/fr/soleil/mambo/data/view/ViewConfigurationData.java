// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationData.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewConfigurationData.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.13 $
//
// $Log: ViewConfigurationData.java,v $
// Revision 1.13 2008/04/09 10:45:46 achouri
// no message
//
// Revision 1.14 2007/12/12 17:49:51 pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.13 2007/10/30 17:49:25 soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment : Define the initializeIsHistoric method, and call it in each
// ViewConfigurationData builder.
//
// Revision 1.12 2007/05/11 07:23:44 ounsy
// * minor bug correction
//
// Revision 1.11 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.10 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.9 2006/12/06 15:12:22 ounsy
// added parametrisable sampling
//
// Revision 1.8 2006/12/06 10:15:29 ounsy
// minor changes
//
// Revision 1.7 2006/10/04 09:59:11 ounsy
// added cloneData() and refreshContent() methods
//
// Revision 1.6 2006/09/18 08:48:30 ounsy
// added start and end dates in charts
//
// Revision 1.5 2006/09/05 14:03:51 ounsy
// updated for sampling compatibility
//
// Revision 1.4 2006/08/31 12:19:32 ounsy
// informations about forcing export only in case of tdb
//
// Revision 1.3 2006/02/01 14:11:51 ounsy
// minor changes (small date bug corrected)
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
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

import java.sql.Timestamp;

import javax.swing.JOptionPane;

import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.properties.AxisProperties;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;
import fr.soleil.mambo.tools.AxisPropertiesUtils;
import fr.soleil.mambo.tools.ChartPropertiesUtils;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfigurationData {

    // MULTI-CONF
    private boolean isHistoric;

    private boolean dynamicDateRange = false;
    private String dateRange = "";

    private Timestamp startDate;
    private Timestamp endDate;

    private ChartProperties chartProperties;

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

    public ViewConfigurationData() {
        this(null);
    }

    public ViewConfigurationData(ChartProperties chartProperties) {
        super();
        setChartProperties(chartProperties);
        samplingType = SamplingType.getSamplingType(SamplingType.ALL);
        isHistoric = Mambo.isHdbAvailable();
    }

    @Override
    public String toString() {
        String ret = "";

        XMLLine openingLine = new XMLLine(GENERIC_PLOT_PARAMETERS_XML_TAG,
                XMLLine.OPENING_TAG_CATEGORY);
        XMLLine closingLine = new XMLLine(GENERIC_PLOT_PARAMETERS_XML_TAG,
                XMLLine.CLOSING_TAG_CATEGORY);

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if (chartProperties != null) {
            ret += ChartPropertiesUtils.toXMLLine(chartProperties);
            ret += GUIUtilities.CRLF;
        }

        AxisProperties y1 = chartProperties.getY1AxisProperties();
        if (y1 != null) {
            ret += AxisPropertiesUtils.toXMLLine(AxisPropertiesUtils.XML_TAG_1, y1);
            ret += GUIUtilities.CRLF;
        }
        AxisProperties y2 = chartProperties.getY2AxisProperties();
        if (y2 != null) {
            ret += AxisPropertiesUtils.toXMLLine(AxisPropertiesUtils.XML_TAG_2, y2);
            ret += GUIUtilities.CRLF;
        }
        AxisProperties x = chartProperties.getXAxisProperties();
        if (x != null) {
            ret += AxisPropertiesUtils.toXMLLine(AxisPropertiesUtils.XML_TAG_3, x);
            ret += GUIUtilities.CRLF;
        }

        ret += closingLine.toString();

        return ret;
    }

    public boolean equals(ViewConfigurationData this2) {
        boolean pathEquals, nameEquals, creationDateEquals, lastUpdateDateEquals, samplingEquals;
        String path1 = this.getPath() == null ? "" : this.getPath();
        String path2 = this2.getPath() == null ? "" : this2.getPath();
        pathEquals = path1.equals(path2);

        String name1 = this.getName() == null ? "" : this.getName();
        String name2 = this2.getName() == null ? "" : this2.getName();
        nameEquals = name1.equals(name2);

        Timestamp creationDate1 = this.getCreationDate();
        Timestamp creationDate2 = this2.getCreationDate();
        creationDateEquals = true;
        if (creationDate1 == null) {
            if (creationDate2 != null) {
                creationDateEquals = false;
            }
        }
        else {
            if (creationDate2 == null) {
                creationDateEquals = false;
            }
            else {
                if (!creationDate1.equals(creationDate2)) {
                    creationDateEquals = false;
                }
            }
        }

        Timestamp lastUpdateDate1 = this.getLastUpdateDate();
        Timestamp lastUpdateDate2 = this2.getLastUpdateDate();
        lastUpdateDateEquals = true;
        if (lastUpdateDate1 == null) {
            if (lastUpdateDate2 != null) {
                lastUpdateDateEquals = false;
            }
        }
        else {
            if (lastUpdateDate2 == null) {
                lastUpdateDateEquals = false;
            }
            else {
                if (!lastUpdateDate1.equals(lastUpdateDate2)) {
                    lastUpdateDateEquals = false;
                }
            }
        }

        SamplingType samplingType1 = this.getSamplingType();
        SamplingType samplingType2 = this2.getSamplingType();
        samplingEquals = true;
        if (samplingType1 == null) {
            if (samplingType2 != null) {
                samplingEquals = false;
            }
        }
        else {
            if (samplingType2 == null) {
                samplingEquals = false;
            }
            else {
                if (samplingType1.getType() != samplingType2.getType()) {
                    samplingEquals = false;
                }
            }
        }

        return pathEquals && nameEquals && creationDateEquals && lastUpdateDateEquals
                && samplingEquals;
    }

    /**
     * @return Returns the creationDate.
     */
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate The creationDate to set.
     */
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return Returns the endDate.
     */
    public Timestamp getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The endDate to set.
     */
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    /**
     * @return Returns the lastUpdateDate.
     */
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate The lastUpdateDate to set.
     */
    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return Returns the startDate.
     */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
     * @param startDate The startDate to set.
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    /**
     * @return Returns the ChartProperties.
     */
    public ChartProperties getChartProperties() {
        return chartProperties;
    }

    /**
     * @param chartProperties The ChartProperties to set.
     */
    public void setChartProperties(ChartProperties chartProperties) {
        if (chartProperties == null) {
            chartProperties = new ChartProperties();
            chartProperties.setAutoHighlightOnLegend(true);
        }
        chartProperties.getXAxisProperties().setAnnotation(IChartViewer.TIME_ANNO);
        this.chartProperties = chartProperties;
    }

    /**
     * @return Returns the isHistoric.
     */
    public boolean isHistoric() {
        return isHistoric;
    }

    /**
     * @param isHistoric The isHistoric to set.
     */
    public void setHistoric(boolean isHistoric) {
        this.isHistoric = isHistoric;
    }

    /**
     * @return returns the dynamicDateRange
     */
    public boolean isDynamicDateRange() {
        return dynamicDateRange;
    }

    /**
     * @param dynamic the value to set to dynamicDateRange
     */
    public void setDynamicDateRange(boolean dynamic) {
        dynamicDateRange = dynamic;
    }

    /**
     * @return returns dateRange
     */
    public String getDateRange() {
        return dateRange;
    }

    /**
     * @param range the value to set to dateRange
     */
    public void setDateRange(String range) {
        dateRange = range;
    }

    /**
     * @return 26 ao�t 2005
     */
    public ChartViewer getChart() {
        ChartViewer chart = ChartPropertiesUtils.getAsJLChart(chartProperties);
        IChartViewer chartViewerComponent = chart.getComponent();
        chartViewerComponent.setParentForTable(MamboFrame.getInstance());

        chartViewerComponent.setChartProperties(chartProperties);

        double startDate_d;
        double endDate_d;
        if (isDynamicDateRange()) {
            Timestamp[] range = getDynamicStartAndEndDates();
            startDate_d = range[0].getTime();
            endDate_d = range[1].getTime();
        }
        else {
            startDate_d = startDate.getTime();
            endDate_d = endDate.getTime();
        }
        chartViewerComponent.setAutoScale(false, IChartViewer.X);
        chartViewerComponent.setAxisMinimum(startDate_d, IChartViewer.X);
        chartViewerComponent.setAxisMaximum(endDate_d, IChartViewer.X);

        String title = "";
        if (chartViewerComponent.getHeader() != null) {
            title += chartViewerComponent.getHeader() + " - ";
        }
        title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE") + getStartDate().toString()
                + ", " + Messages.getMessage("VIEW_ATTRIBUTES_END_DATE") + getEndDate().toString();
        chartViewerComponent.setHeader(title);
        title = null;
        chart.getComponent().setPreferDialogForTable(true, false);

        chartViewerComponent.setDataViewRemovingEnabled(true);
        chartViewerComponent.setCleanDataViewConfigurationOnRemoving(true);

        return chart;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the path.
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }

    public Timestamp[] getDynamicStartAndEndDates() {
        Timestamp[] startAndEnd = new Timestamp[2];
        long now = System.currentTimeMillis();
        Timestamp end = new Timestamp(now);
        startAndEnd[1] = end;
        long delta = 0;
        long hour = 3600000;
        long day = 86400000;
        String range = getDateRange();
        if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1H").equals(range)) {
            delta = 1 * hour;
        }
        else if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_4H").equals(range)) {
            delta = 4 * hour;
        }
        else if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_8H").equals(range)) {
            delta = 8 * hour;
        }
        else if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1D").equals(range)) {
            delta = 1 * day;
        }
        else if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_3D").equals(range)) {
            delta = 3 * day;
        }
        else if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_7D").equals(range)) {
            delta = 7 * day;
        }
        else if (Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_30D").equals(range)) {
            delta = 30 * day;
        }
        Timestamp start = new Timestamp(now - delta);
        startAndEnd[0] = start;
        return startAndEnd;
    }

    public void setSamplingType(SamplingType _samplingType) {
        samplingType = _samplingType;
    }

    /**
     * @return Returns the samplingType.
     */
    public SamplingType getSamplingType() {
        return samplingType;
    }

    public void refreshContent() {
        // ChartGeneralTabbedPane generalTabbedPane = ChartGeneralTabbedPane
        // .getInstance();
        // ChartProperties ChartProperties = generalTabbedPane
        // .getChartProperties();
        // YAxis y1Axis = generalTabbedPane.getY1Axis();
        // YAxis y2Axis = generalTabbedPane.getY2Axis();
        // this.setChartProperties(ChartProperties);
        // this.setY1(y1Axis);
        // this.setY2(y2Axis);

        if (this.isDynamicDateRange()) {
            Timestamp[] range = this.getDynamicStartAndEndDates();
            this.setStartDate(range[0]);
            this.setEndDate(range[1]);
        }
    }

    public ViewConfigurationData cloneData() {
        ViewConfigurationData ret = new ViewConfigurationData();

        ret.setCreationDate(this.getCreationDate() == null ? null : (Timestamp) this
                .getCreationDate().clone());
        ret.setDateRange(this.getDateRange() == null ? null : new String(this.getDateRange()));
        ret.setDynamicDateRange(this.isDynamicDateRange());
        ret.setEndDate(this.getEndDate() == null ? null : (Timestamp) this.getEndDate().clone());
        try {
            ret.setChartProperties(this.getChartProperties() == null ? null
                    : (ChartProperties) this.getChartProperties().clone());
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            ret.setChartProperties(new ChartProperties());
        }
        ret.setHistoric(isHistoric);
        ret.setLastUpdateDate(this.getLastUpdateDate() == null ? null : (Timestamp) this
                .getLastUpdateDate().clone());
        ret.setName(this.getName() == null ? null : new String(this.getName()));
        ret.setPath(this.getPath() == null ? null : new String(this.getPath()));
        ret.setSamplingType(this.getSamplingType() == null ? null : this.getSamplingType()
                .cloneSamplingType());
        ret.setStartDate(this.getStartDate() == null ? null : (Timestamp) this.getStartDate()
                .clone());

        return ret;
    }

    public void updateDateRangeBox(DateRangeBox _dateRangeBox) {
        boolean dynamic = _dateRangeBox.isDynamicDateRange();
        if (dynamic) {
            Timestamp[] range = _dateRangeBox.getDynamicStartAndEndDates();
            this.setStartDate(range[0]);
            this.setEndDate(range[1]);
        }
        else {
            this.setStartDate(_dateRangeBox.getStartDate());
            this.setEndDate(_dateRangeBox.getEndDate());
        }
        this.setDynamicDateRange(dynamic);
        this.setDateRange(_dateRangeBox.getDateRange());

    }

    public static boolean verifyDates(DateRangeBox dateRangeBox) {

        boolean ok = true;
        String msg = null;
        long startDate_l = 0;
        long endDate_l = 0;
        Timestamp[] range = dateRangeBox.getDynamicStartAndEndDates();

        try {
            Timestamp startDate;
            if (dateRangeBox.isDynamicDateRange()) {
                startDate = range[0];
            }
            else {
                startDate = dateRangeBox.getStartDate();
            }
            startDate_l = startDate.getTime();
        }
        catch (IllegalArgumentException iae) {
            ok = false;
            msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_START_DATE");
        }

        try {
            Timestamp endDate;
            if (dateRangeBox.isDynamicDateRange()) {
                endDate = range[1];
            }
            else {
                endDate = dateRangeBox.getEndDate();
            }
            endDate_l = endDate.getTime();
        }
        catch (IllegalArgumentException iae) {
            ok = false;
            msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_END_DATE");
        }

        if (ok) {
            if (endDate_l <= startDate_l) {
                ok = false;
                msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_DATE_RANGE");
            }
        }

        if (!ok) {
            String title = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_CONTROLS_INVALID_TITLE");
            JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
        }

        return ok;
    }

}
