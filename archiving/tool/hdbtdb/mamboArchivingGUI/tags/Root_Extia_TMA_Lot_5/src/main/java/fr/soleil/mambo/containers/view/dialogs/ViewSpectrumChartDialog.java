/*
 * Synchrotron Soleil File : ViewSpectrumChartDialog.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 27 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumChartDialog.java,v
 */
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

import chart.temp.chart.JLAxis;
import chart.temp.chart.JLChart;
import chart.temp.chart.JLChartRecorder;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * @author SOLEIL
 */
public class ViewSpectrumChartDialog extends JDialog {

    private static final long serialVersionUID = 9026556560129510126L;
    private JLChartRecorder   chartRecorder;
    private JPanel            mainPanel;

    private ViewSpectrumPanel specPanel;

    public ViewSpectrumChartDialog(ViewSpectrumPanel specPanel) {
        super(MamboFrame.getInstance(), specPanel.getFullName(), true);
        this.specPanel = specPanel;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        initComponents();
        addComponents();
        initLayout();
        initBounds();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void updateContent() {
        clean();
        chartRecorder.setDAO(specPanel.getFilteredSpectrumDAO());
        if (specPanel.getViewSpectrumType() == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX) {
            chartRecorder.setAnnotation(JLChart.X, JLAxis.TIME_ANNO);
        } else {
            chartRecorder.setAnnotation(JLChart.X, JLAxis.VALUE_ANNO);
        }
        mainPanel.add(chartRecorder);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
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
        JLChart chart = chartRecorder.getChart();
        chart.setLabelVisible(false);

        ViewConfiguration selectedViewConfiguration = ViewConfigurationBeanManager
                .getInstance().getSelectedConfiguration();
        if (selectedViewConfiguration != null) {
            ViewConfigurationData data = selectedViewConfiguration.getData();
            String title = "";
            if (data != null) {
                if (chart.getHeader() != null) {
                    title += chart.getHeader() + " - ";
                }
                title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE")
                        + data.getStartDate().toString() + ", "
                        + Messages.getMessage("VIEW_ATTRIBUTES_END_DATE")
                        + data.getEndDate().toString();
            }
            chart.setHeader(title);
        }
    }
}
