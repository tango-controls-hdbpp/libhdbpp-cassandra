/*	Synchrotron Soleil 
 *  
 *   File          :  ViewSpectrumChartDialog.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  27 janv. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ViewSpectrumChartDialog.java,v 
 *
 */
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


import fr.esrf.tangoatk.widget.util.chart.JLChart;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * 
 * @author SOLEIL
 */
public class ViewSpectrumChartDialog extends JDialog {

    private JLDataView[]         readSpectrumData;
    private JLDataView[]         writeSpectrumData;
    private JLChart              chart;
    private JPanel               mainPanel;
    private int                  color;
    private int                  markerStyle;
    private static ViewSpectrumChartDialog instance;

    private final static Color[] defaultColor = ColorGenerator.generateColors(30,0xFFFFFF);

    private final static int [] defaultMarkerStyle = {
            JLDataView.MARKER_BOX, JLDataView.MARKER_CIRCLE, 
            JLDataView.MARKER_CROSS, JLDataView.MARKER_DIAMOND,
            JLDataView.MARKER_DOT, JLDataView.MARKER_HORIZ_LINE,
            JLDataView.MARKER_SQUARE, JLDataView.MARKER_STAR,
            JLDataView.MARKER_TRIANGLE, JLDataView.MARKER_VERT_LINE
    };

    public static ViewSpectrumChartDialog getInstance() {
        return instance;
    }

    public static ViewSpectrumChartDialog getInstance(boolean forceReload, String name, JLDataView[] readView, JLDataView[] writeView) {
        if (instance == null || forceReload) {
            instance = new ViewSpectrumChartDialog(name, readView, writeView);
        }
        return instance;
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private ViewSpectrumChartDialog(String name, JLDataView[] readView, JLDataView[] writeView) {
        super(VCViewDialog.getInstance(false), name, true);
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        if (ViewConfiguration.getSelectedViewConfiguration() == null)
            return;
        this.readSpectrumData = readView;
        this.writeSpectrumData = writeView;
        initComponents();
        addComponents();
        initLayout();
        initBounds();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
        color = 0;
        markerStyle = 0;
        prepareChart();
        GUIUtilities.setObjectBackground(chart, GUIUtilities.VIEW_COLOR);
        if (readSpectrumData.length == writeSpectrumData.length) {
            for (int i = 0; i < readSpectrumData.length; i++) {
                readSpectrumData[i].setLineWidth(1);
                writeSpectrumData[i].setLineWidth(2);
                readSpectrumData[i].setMarkerSize(3);
                writeSpectrumData[i].setMarkerSize(6);
                readSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
                writeSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
                Color drawColor = getNextColor();
                readSpectrumData[i].setColor(drawColor);
                writeSpectrumData[i].setColor(drawColor);
                readSpectrumData[i].setMarkerColor(drawColor);
                writeSpectrumData[i].setMarkerColor(drawColor);
                readSpectrumData[i].setMarker(getNextMarkerStyle());
                writeSpectrumData[i].setMarker(getNextMarkerStyle());
                readSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
                writeSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
                chart.getY1Axis().addDataView( readSpectrumData[i] );
                chart.getY1Axis().addDataView( writeSpectrumData[i] );
            }
        }
        else {
            for (int i = 0; i < readSpectrumData.length; i++) {
                readSpectrumData[i].setLineWidth(1);
                readSpectrumData[i].setMarkerSize(3);
                readSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
                Color drawColor = getNextColor();
                readSpectrumData[i].setColor(drawColor);
                readSpectrumData[i].setMarkerColor(drawColor);
                readSpectrumData[i].setMarker(getNextMarkerStyle());
                readSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
                chart.getY1Axis().addDataView( readSpectrumData[i] );
            }
            for (int i = 0; i < writeSpectrumData.length; i++) {
                writeSpectrumData[i].setLineWidth(2);
                writeSpectrumData[i].setMarkerSize(6);
                writeSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
                Color drawColor = getNextColor();
                writeSpectrumData[i].setColor(drawColor);
                writeSpectrumData[i].setMarkerColor(drawColor);
                writeSpectrumData[i].setMarker(getNextMarkerStyle());
                writeSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
                chart.getY1Axis().addDataView( writeSpectrumData[i] );
            }
        }
    }

    private void clean() {
        if (chart != null) {
            chart.getY1Axis().clearDataView();
        }
        this.removeAll();
    }

    private void addComponents() {
        mainPanel.add(chart);
        this.setContentPane(mainPanel);
    }

    private void initLayout() {
        mainPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                mainPanel,
                mainPanel.getComponentCount(), 1,
                0, 0,
                0, 0,
                true
        );
    }

    private Color getNextColor() {
        return defaultColor[ (color++)%defaultColor.length ];
    }

    private int getNextMarkerStyle() {
        if (color%defaultColor.length == 0) {
            return defaultMarkerStyle[(markerStyle++)%defaultMarkerStyle.length];
        }
        return defaultMarkerStyle[(markerStyle)%defaultMarkerStyle.length];
    }

    private void initBounds() {
        this.setBounds(
            VCViewDialog.getInstance(false).getX(),
            VCViewDialog.getInstance(false).getY(),
            VCViewDialog.getInstance(false).getWidth(),
            VCViewDialog.getInstance(false).getHeight()
        );
    }

    private void prepareChart()
    {
        JLChart refChart = ViewConfiguration.getSelectedViewConfiguration().getChart();
        ViewConfigurationData data = ViewConfiguration.getSelectedViewConfiguration().getData();
        GeneralChartProperties generalChartProperties = data.getGeneralChartProperties();
        chart = generalChartProperties.getAsJLChart();
        chart.setParentForTableDialog(this);

        String title = "";
        if (chart.getHeader() != null)
        {
            title += chart.getHeader() + " - ";
        }
        if (data != null)
        {
            title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE") + data.getStartDate().toString() + ", " + Messages.getMessage("VIEW_ATTRIBUTES_END_DATE") + data.getEndDate().toString();
        }
        chart.setHeader(title);
        title = null;


        chart.getY1Axis().setAnnotation(refChart.getY1Axis().getAnnotation());
        chart.getY1Axis().setAxeName(refChart.getY1Axis().getAxeName());
        chart.getY1Axis().setAxisColor(refChart.getY1Axis().getAxisColor());
        chart.getY1Axis().setDrawOpposite(refChart.getY1Axis().isDrawOpposite());
        chart.getY1Axis().setFont(refChart.getY1Axis().getFont());
        chart.getY1Axis().setGridStyle(refChart.getY1Axis().getGridStyle());
        chart.getY1Axis().setInverted(refChart.getY1Axis().isInverted());
        chart.getY1Axis().setLabelFormat(refChart.getY1Axis().getLabelFormat());
        chart.getY1Axis().setMaximum(refChart.getY1Axis().getMaximum());
        chart.getY1Axis().setMinimum(refChart.getY1Axis().getMinimum());
        chart.getY1Axis().setName(refChart.getY1Axis().getName());
        chart.getY1Axis().setPercentScrollback(refChart.getY1Axis().getPercentScrollback());
        chart.getY1Axis().setScale(refChart.getY1Axis().getScale());
        chart.getY1Axis().setSubGridVisible(refChart.getY1Axis().isSubGridVisible());
        chart.getY1Axis().setTickLength(refChart.getY1Axis().getTickLength());
        chart.getY1Axis().setTickSpacing(refChart.getY1Axis().getTickSpacing());
        chart.getY1Axis().setVisible(refChart.getY1Axis().isVisible());
        chart.getY1Axis().setAutoScale(refChart.getY1Axis().isAutoScale());

        chart.getXAxis().setAnnotation(refChart.getXAxis().getAnnotation());
        chart.getXAxis().setAxeName(refChart.getXAxis().getAxeName());
        chart.getXAxis().setAxisColor(refChart.getXAxis().getAxisColor());
        chart.getXAxis().setDrawOpposite(refChart.getXAxis().isDrawOpposite());
        chart.getXAxis().setFont(refChart.getXAxis().getFont());
        chart.getXAxis().setGridStyle(refChart.getXAxis().getGridStyle());
        chart.getXAxis().setInverted(refChart.getXAxis().isInverted());
        chart.getXAxis().setLabelFormat(refChart.getXAxis().getLabelFormat());
        chart.getXAxis().setMaximum(refChart.getXAxis().getMaximum());
        chart.getXAxis().setMinimum(refChart.getXAxis().getMinimum());
        chart.getXAxis().setName(refChart.getXAxis().getName());
        chart.getXAxis().setPercentScrollback(refChart.getXAxis().getPercentScrollback());
        chart.getXAxis().setScale(refChart.getXAxis().getScale());
        chart.getXAxis().setSubGridVisible(refChart.getXAxis().isSubGridVisible());
        chart.getXAxis().setTickLength(refChart.getXAxis().getTickLength());
        chart.getXAxis().setTickSpacing(refChart.getXAxis().getTickSpacing());
        chart.getXAxis().setVisible(refChart.getXAxis().isVisible());
        chart.getXAxis().setAutoScale(refChart.getXAxis().isAutoScale());

        chart.setChartBackground(refChart.getChartBackground());
        chart.setPaintAxisFirst( true );
        chart.setVisible( true );
    }

}