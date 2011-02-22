/*
 * Synchrotron Soleil File : ViewSpectrumChartDialog.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 27 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumChartDialog.java,v
 */
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;

import chart.temp.chart.JLAxis;
import chart.temp.chart.JLChart;
import chart.temp.chart.JLChartRecorder;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.tools.ATKToCometUtilities;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;

/**
 * @author SOLEIL
 */
public class ViewSpectrumChartDialog extends JDialog {

    private static final long     serialVersionUID   = 9026556560129510126L;
    private JLChartRecorder       chartRecorder;
    private JPanel                mainPanel;

    private int                   color;
    private int                   markerStyle;

    private final static Color[]  defaultColor       = ColorGenerator
                                                             .generateColors(
                                                                     31,
                                                                     0xFFFFFF);

    private final static int[]    defaultMarkerStyle = { JLDataView.MARKER_BOX,
            JLDataView.MARKER_CIRCLE, JLDataView.MARKER_CROSS,
            JLDataView.MARKER_DIAMOND, JLDataView.MARKER_DOT,
            JLDataView.MARKER_HORIZ_LINE, JLDataView.MARKER_SQUARE,
            JLDataView.MARKER_STAR, JLDataView.MARKER_TRIANGLE,
            JLDataView.MARKER_VERT_LINE             };

    private ViewConfigurationBean viewConfigurationBean;
    private ViewSpectrumPanel     specPanel;

    public ViewSpectrumChartDialog(ViewSpectrumPanel specPanel,
            ViewConfigurationBean viewConfigurationBean) {
        super(MamboFrame.getInstance(), specPanel.getFullName(), true);
        this.specPanel = specPanel;
        this.viewConfigurationBean = viewConfigurationBean;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        if (viewConfigurationBean.getViewConfiguration() == null)
            return;
        initComponents();
        addComponents();
        initLayout();
        initBounds();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void updateContent() {
        clean();
        chartRecorder.setDAO(ATKToCometUtilities.createSpectrumDAO(specPanel
                .getSelectedReadView(), specPanel.getSelectedWriteView()));
        mainPanel.add(chartRecorder);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
        color = 0;
        markerStyle = 0;
        prepareChart();
        GUIUtilities
                .setObjectBackground(chartRecorder, GUIUtilities.VIEW_COLOR);
    }

    public void clean() {
        if (chartRecorder != null) {
            chartRecorder.setDAO(null);
        }
        mainPanel.removeAll();
    }

    private void addComponents() {
        mainPanel.add(chartRecorder);
        this.setContentPane(mainPanel);
    }

    private void initLayout() {
        mainPanel.setLayout(new BorderLayout());
    }

     private Color getNextColor() {
     Color colorToReturn = defaultColor[color];
     color = (color + 5) % defaultColor.length;
     return colorToReturn;
     }

    private int getNextMarkerStyle() {
        if (color == 0) {
            return defaultMarkerStyle[(markerStyle++)
                    % defaultMarkerStyle.length];
        }
        return defaultMarkerStyle[(markerStyle) % defaultMarkerStyle.length];
    }

    private void initBounds() {
        int x = MamboFrame.getInstance().getX()
                + MamboFrame.getInstance().getWidth() - 700;
        int y = MamboFrame.getInstance().getY()
                + MamboFrame.getInstance().getHeight() - 700;
        y /= 2;
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;
        this.setBounds(x, y, 700, 700);
    }

    private void prepareChart() {
        chartRecorder = new JLChartRecorder();
        chartRecorder.setFreezePanelVisible(false);
        chartRecorder.setManagementPanelVisible(false);
        chartRecorder.setAnnotation(JLAxis.TIME_ANNO, JLChart.X);
        chartRecorder.getChart().setLabelVisible(false);
    }

}
