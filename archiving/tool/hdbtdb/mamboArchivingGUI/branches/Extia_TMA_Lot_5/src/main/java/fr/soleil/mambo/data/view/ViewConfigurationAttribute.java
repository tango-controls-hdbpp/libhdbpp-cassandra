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

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.soleil.comete.dao.DAOFactoryManager;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;
import fr.soleil.tango.util.entity.data.Attribute;

public class ViewConfigurationAttribute extends Attribute {

    private static final long                    serialVersionUID               = 4666786505575853480L;

    private ViewConfigurationAttributeProperties properties;

    private double                               factor;

    public static final String                   XML_TAG                        = "attribute";
    public static final String                   COMPLETE_NAME_PROPERTY_XML_TAG = "completeName";
    public static final String                   FACTOR_PROPERTY_XML_TAG        = "factor";

    public static final String                   READ_DATA_VIEW_KEY             = "read";
    public static final String                   WRITE_DATA_VIEW_KEY            = "write";

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
        openingLine.setAttribute(COMPLETE_NAME_PROPERTY_XML_TAG, super
                .getCompleteName());
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
     * @param properties
     *            The properties to set.
     */
    public void setProperties(ViewConfigurationAttributeProperties properties) {
        this.properties = properties;
    }

    /**
     * @return 25 ao�t 2005
     */
    public boolean isEmpty() {
        ViewConfigurationAttributePlotProperties plotProperties = properties
                .getPlotProperties();
        ViewConfigurationAttributeExtractProperties extractProperties = properties
                .getExtractProperties();

        boolean ret = plotProperties.isEmpty() && extractProperties.isEmpty();

        return ret;
    }

    /**
     * @param chart
     *            29 ao�t 2005
     * @param endDate
     * @param startDate
     * @param historic
     * @param samplingType
     * @throws ParseException
     * @throws DevFailed
     */
    public void addToChart(ChartViewer chart, String startDate, String endDate,
            boolean historic, SamplingType samplingType) throws DevFailed,
            ParseException {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        String[] param = new String[3];
        param[0] = this.getCompleteName();
        param[1] = startDate;
        param[2] = endDate;

        DbData recoveredData = extractingManager.retrieveData(param, historic,
                samplingType);

        if (recoveredData != null) {
            recoveredData.setName(this.getCompleteName());
            /*
             * TODO see if it is possible to remove reference to
             * ViewConfigurationBeanManager by knowing the ViewConfiguration
             * linked with this attribute
             */
            String nameViewSelected = ViewConfigurationBeanManager
                    .getInstance().getSelectedConfiguration()
                    .getDisplayableTitle();
            String lastUpdate = Long.toString(ViewConfigurationBeanManager
                    .getInstance().getSelectedConfiguration().getData()
                    .getLastUpdateDate().getTime());
            String idViewSelected = nameViewSelected + " - " + lastUpdate;
            MamboViewDAOFactory daoFactory = (MamboViewDAOFactory) DAOFactoryManager
                    .getFactory(MamboViewDAOFactory.class.getName());
            ViewScalarDAO dao = daoFactory.getDAOScalar(idViewSelected);
            if (dao != null) {
                prepareView(recoveredData, chart, recoveredData.getName());
                dao.addData(recoveredData);
            }
        }
        recoveredData = null;
    }

    private void prepareView(DbData recoveredData, ChartViewer chart,
            String string) {
        boolean hasRead = false;
        boolean hasWrite = false;
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
                break;
        }
        if (hasRead && hasWrite) {
            setAttributeDataView(chart, recoveredData.getName() + "/"
                    + WRITE_DATA_VIEW_KEY, true);
            setAttributeDataView(chart, recoveredData.getName() + "/"
                    + READ_DATA_VIEW_KEY, false);
        } else {
            if (hasWrite) {
                setAttributeDataView(chart, recoveredData.getName() + "/"
                        + WRITE_DATA_VIEW_KEY, true);
            }
            if (hasRead) {
                setAttributeDataView(chart, recoveredData.getName() + "/"
                        + READ_DATA_VIEW_KEY, false);
            }
        }
    }

    private void setAttributeDataView(ChartViewer chart, String nameId,
            boolean hasWrite) {
        ViewConfigurationAttributePlotProperties plotProperties = properties
                .getPlotProperties();
        if (plotProperties != null) {
            Marker marker = plotProperties.getMarker();
            Curve curve = plotProperties.getCurve();
            Bar bar = plotProperties.getBar();
            // if the properties aren't set we don't add the attribute to the
            // plot
            IChartViewer chartComponent = chart.getComponent();
            if (marker != null) {
                chartComponent.setDataViewMarkerCometeColor(nameId, CometeColor
                        .getColor(marker.getColor()));
                chartComponent.setDataViewMarkerSize(nameId, marker.getSize());
                chartComponent
                        .setDataViewMarkerStyle(nameId, marker.getStyle());
            }
            if (curve != null) {
                if (hasWrite) {
                    chart.getComponent().setDataViewLineStyle(nameId,
                            IChartViewer.STYLE_LONG_DASH);
                } else {
                    chartComponent.setDataViewLineStyle(nameId, curve
                            .getLineStyle());
                }
                chartComponent.setDataViewLineWidth(nameId, curve.getWidth());
                chartComponent.setDataViewCometeColor(nameId, CometeColor
                        .getColor(curve.getColor()));
                chartComponent.setDataViewLineWidth(nameId, curve.getWidth());
            }
            if (bar != null) {
                chartComponent.setDataViewCometeFillColor(nameId, CometeColor
                        .getColor(bar.getFillColor()));
                chartComponent.setDataViewFillMethod(nameId, bar
                        .getFillingMethod());
                chartComponent.setDataViewFillStyle(nameId, bar.getFillStyle());
                chartComponent.setDataViewLineWidth(nameId, bar.getWidth());
            }
            int axis = plotProperties.getAxisChoice();
            chartComponent.setDataViewAxis(nameId, axis);
            chartComponent.setDataViewStyle(nameId, plotProperties
                    .getViewType());
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
