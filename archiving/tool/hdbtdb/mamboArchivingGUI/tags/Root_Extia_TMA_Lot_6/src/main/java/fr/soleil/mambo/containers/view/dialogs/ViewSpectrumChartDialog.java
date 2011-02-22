/*
 * Synchrotron Soleil File : ViewSpectrumChartDialog.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 27 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumChartDialog.java,v
 */
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import chart.temp.chart.JLAxis;
import chart.temp.chart.JLChart;

import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.ViewDAOKey;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.models.ViewSpectrumTableModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * @author SOLEIL
 */
public class ViewSpectrumChartDialog extends JDialog {

    private static final long serialVersionUID = 9026556560129510126L;
    private ChartViewer       chartRecorder;
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (chartRecorder != null) {
                    clean();
                }
            }
        });
    }

    public void updateContent() {
        clean();
        DbData[] splitedData = specPanel.getSplitedData();
        ViewSpectrumTableModel model = specPanel.getModel();
        int spectrumType = specPanel.getViewSpectrumType();

        chartRecorder.addKey(MamboViewDAOFactory.class.getName(),
                new ViewDAOKey(ViewDAOKey.TYPE_NUMBER_SPECTRUM, splitedData,
                        model, spectrumType));
        chartRecorder.switchDAOFactory(MamboViewDAOFactory.class.getName());
        IChartViewer chartRecorderComponent = chartRecorder.getComponent();
        if (specPanel.getViewSpectrumType() == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX) {
            chartRecorderComponent.setAnnotation(JLAxis.TIME_ANNO, JLChart.X);
        } else {
            chartRecorderComponent.setAnnotation(JLAxis.VALUE_ANNO, JLChart.X);
        }
        mainPanel.add((Component) chartRecorderComponent);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
        prepareChart();
        chartRecorder.getComponent().setCometeBackground(
                CometeColor.getColor(GUIUtilities.getViewColor()));
    }

    public void clean() {
        if (chartRecorder != null) {
            chartRecorder.removeKey(MamboViewDAOFactory.class.getName());
            chartRecorder.clearDAO();
        }
        mainPanel.removeAll();
    }

    private void addComponents() {
        mainPanel.add((Component) chartRecorder.getComponent());
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
        chartRecorder = new ChartViewer();
        IChartViewer chartRecorderComponent = chartRecorder.getComponent();
        chartRecorderComponent.setFreezePanelVisible(false);
        chartRecorderComponent.setManagementPanelVisible(false);
        chartRecorderComponent.setAnnotation(JLAxis.TIME_ANNO, JLChart.X);

        chartRecorderComponent.setPreferDialogForTable(true, true);
        chartRecorderComponent.setParentForTable(this);
        chartRecorderComponent.setLabelVisible(false);

        ViewConfiguration selectedViewConfiguration = ViewConfigurationBeanManager
                .getInstance().getSelectedConfiguration();
        if (selectedViewConfiguration != null) {
            ViewConfigurationData data = selectedViewConfiguration.getData();
            String title = "";
            if (data != null) {
                if (chartRecorderComponent.getHeader() != null) {
                    title += chartRecorderComponent.getHeader() + " - ";
                }
                title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE")
                        + data.getStartDate().toString() + ", "
                        + Messages.getMessage("VIEW_ATTRIBUTES_END_DATE")
                        + data.getEndDate().toString();
            }
            chartRecorderComponent.setHeader(title);
        }
    }
}
