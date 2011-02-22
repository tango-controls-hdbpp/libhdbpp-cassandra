// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationAttribute.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewConfigurationAttribute.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.11 $
//
// $Log: ViewConfigurationAttribute.java,v $
// Revision 1.11 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.10 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.9 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.8 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.7 2006/09/05 14:12:04 ounsy
// updated for sampling compatibility
//
// Revision 1.6 2006/07/24 07:37:51 ounsy
// image support with partial loading
//
// Revision 1.5 2006/03/10 12:05:08 ounsy
// state and string support
//
// Revision 1.4 2006/03/07 14:40:57 ounsy
// added a isImage ( boolean _historic ) method
//
// Revision 1.3 2006/02/01 14:10:49 ounsy
// spectrum management
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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

import java.text.ParseException;

import chart.temp.chart.JLDataView;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.dao.DAOFactoryManager;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;
import fr.soleil.tango.util.entity.data.Attribute;

public class ViewConfigurationAttribute extends Attribute {

    private static final long serialVersionUID = 4666786505575853480L;

    private ViewConfigurationAttributeProperties properties;

    private double factor;

    public static final String XML_TAG = "attribute";
    public static final String COMPLETE_NAME_PROPERTY_XML_TAG = "completeName";
    public static final String FACTOR_PROPERTY_XML_TAG = "factor";

    public static final String READ_DATA_VIEW_KEY = "read";
    public static final String WRITE_DATA_VIEW_KEY = "write";

    public double getFactor() {
        return factor;
    }

    public void setFactor(double _factor) {
        factor = _factor;
    }

    @Override
    public String toString() {
        String ret = "";

        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
        openingLine.setAttribute(COMPLETE_NAME_PROPERTY_XML_TAG, super.getCompleteName());
        openingLine.setAttribute(FACTOR_PROPERTY_XML_TAG, getFactor() + "");
        XMLLine closingLine = new XMLLine(XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if (properties != null) {
            ViewConfigurationAttributePlotProperties plotProperties = properties
                    .getPlotProperties();
            ret += plotProperties.toString();
            ret += GUIUtilities.CRLF;
        }
        ret += closingLine.toString();

        return ret;
    }

    public ViewConfigurationAttribute() {
        super();
        factor = 1;
        properties = new ViewConfigurationAttributeProperties();
    }

    /**
     * @param _attribute
     */
    public ViewConfigurationAttribute(Attribute _attribute) {
        super();
        factor = 1;
        properties = new ViewConfigurationAttributeProperties();

        this.setCompleteName(_attribute.getCompleteName());
        this.setDevice(_attribute.getDeviceName());
        this.setDomain(_attribute.getDomainName());
        this.setFamily(_attribute.getFamilyName());
        this.setMember(_attribute.getMemberName());
        this.setName(_attribute.getName());
    }

    /**
     * @return Returns the properties.
     */
    public ViewConfigurationAttributeProperties getProperties() {
        return properties;
    }

    /**
     * @param properties The properties to set.
     */
    public void setProperties(ViewConfigurationAttributeProperties properties) {
        if (properties == null) {
            properties = new ViewConfigurationAttributeProperties();
        }
        properties.getPlotProperties().getCurve().setName(getCompleteName());
        this.properties = properties;
    }

    /**
     * @return 25 ao�t 2005
     */
    public boolean isEmpty() {
        ViewConfigurationAttributePlotProperties plotProperties = properties.getPlotProperties();
        ViewConfigurationAttributeExtractProperties extractProperties = properties
                .getExtractProperties();

        boolean ret = plotProperties.isEmpty() && extractProperties.isEmpty();

        return ret;
    }

    /**
     * @param chart 29 ao�t 2005
     * @param endDate
     * @param startDate
     * @param historic
     * @param samplingType
     * @param idViewSelected
     * @throws ParseException
     * @throws DevFailed
     */
    public void addToChart(ChartViewer chart, String startDate, String endDate, boolean historic,
            SamplingType samplingType, String idViewSelected) throws DevFailed, ParseException {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();

        String[] param = new String[3];
        param[0] = this.getCompleteName();
        param[1] = startDate;
        param[2] = endDate;

        DbData recoveredData = extractingManager.retrieveData(param, historic, samplingType);

        if (recoveredData != null) {
            recoveredData.setName(this.getCompleteName());
            if (idViewSelected != null) {
                MamboViewDAOFactory daoFactory = (MamboViewDAOFactory) DAOFactoryManager
                        .getFactory(MamboViewDAOFactory.class.getName());
                ViewScalarDAO dao = daoFactory.getDAOScalar(idViewSelected);
                if (dao != null) {
                    prepareView(recoveredData, chart);
                    dao.addData(recoveredData, extractingManager.isShowRead(), extractingManager
                            .isShowWrite());
                }
            }
        }
        recoveredData = null;
    }

    private void prepareView(DbData recoveredData, ChartViewer chart) {
        boolean hasRead = false;
        boolean hasWrite = false;
        boolean hasWriteRead = false;
        switch (recoveredData.getWritable()) {
            case AttrWriteType._READ:
                hasRead = true;
                break;
            case AttrWriteType._WRITE:
                hasWrite = true;
                break;
            case AttrWriteType._READ_WITH_WRITE:
            case AttrWriteType._READ_WRITE:
                hasRead = true;
                hasWrite = true;
                hasWriteRead = true;
                break;
        }
        ViewConfigurationAttributePlotProperties plotPropertie = properties.getPlotProperties();
        String displayName = plotPropertie.getCurve().getName();
        if (hasWrite) {
            setAttributeDataView(chart, displayName, recoveredData.getName(), true, hasWriteRead);
        }
        if (hasRead) {
            setAttributeDataView(chart, displayName, recoveredData.getName(), false, hasWriteRead);
        }
    }

    private void setAttributeDataView(ChartViewer chart, String displayName, String nameId,
            boolean hasWrite, boolean hasWriteRead) {
        ViewConfigurationAttributePlotProperties plotProperties = properties.getPlotProperties();

        if (hasWrite) {
            displayName = displayName + "/" + WRITE_DATA_VIEW_KEY;
            nameId = nameId + "/" + WRITE_DATA_VIEW_KEY;
        }
        else {
            displayName = displayName + "/" + READ_DATA_VIEW_KEY;
            nameId = nameId + "/" + READ_DATA_VIEW_KEY;
        }
        IChartViewer chartComponent = chart.getComponent();

        if (plotProperties != null) {
            PlotProperties plotPropertie = null;
            try {
                plotPropertie = (PlotProperties) plotProperties.clone();
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (plotPropertie != null) {
                plotPropertie.getCurve().setName(displayName);
                chartComponent.setDataViewPlotProperties(nameId, plotPropertie);
            }
            if (hasWrite && hasWriteRead) {
                chartComponent.setDataViewLineStyle(nameId, IChartViewer.STYLE_LONG_DASH);
            }
            if (plotProperties.isHidden()) {
                chartComponent.setDataViewLineWidth(nameId, 0);
                chartComponent.setDataViewMarkerStyle(nameId, JLDataView.MARKER_NONE);
                chartComponent.setDataViewBarWidth(nameId, 0);
                chartComponent.setDataViewFillStyle(nameId, JLDataView.FILL_STYLE_NONE);
                chartComponent.setDataViewLabelVisible(nameId, false);
                chartComponent.setDataViewClickable(nameId, false);
            }
        }
    }

    /**
     * @return 2 sept. 2005
     * @throws DevFailed
     */
    public boolean isScalar(boolean _historic) {
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();

        return manager.isScalar(this.getCompleteName(), _historic);
    }

    /**
     * @return 2 sept. 2005
     * @throws DevFailed
     */
    public boolean isSpectrum(boolean _historic) {
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();

        return manager.isSpectrum(this.getCompleteName(), _historic);
    }

    /**
     * @return 2 sept. 2005
     * @throws DevFailed
     */
    public int getDataType(boolean _historic) {
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();

        return manager.getDataType(this.getCompleteName(), _historic);
    }

    public int getDataWritable(boolean _historic) {
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();

        return manager.getDataWritable(this.getCompleteName(), _historic);
    }

    /**
     * @param _historic
     * @return
     */
    public boolean isImage(boolean _historic) {
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();

        return manager.isImage(this.getCompleteName(), _historic);
    }

    public String getDisplayFormat(boolean _historic) {
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();
        return manager.getDisplayFormat(this.getCompleteName(), _historic);
    }
}
