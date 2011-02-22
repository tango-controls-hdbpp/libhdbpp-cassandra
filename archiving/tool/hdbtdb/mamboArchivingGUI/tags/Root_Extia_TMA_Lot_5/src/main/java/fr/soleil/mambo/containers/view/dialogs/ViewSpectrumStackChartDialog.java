package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chart.temp.chart.JLChart;
import fr.soleil.comete.dao.util.DefaultStackDataArrayDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.widget.stackviewer.SpectrumStackViewer;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewSpectrumStackPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * @author awo
 */
public class ViewSpectrumStackChartDialog extends JDialog {

    private static final long      serialVersionUID = 9026556560129510126L;

    private SpectrumStackViewer    spectrumStackViewer;
    private JPanel                 mainPanel;

    private ViewSpectrumStackPanel specPanel;

    public ViewSpectrumStackChartDialog(ViewSpectrumStackPanel specPanel,
            ViewConfigurationBean viewConfigurationBean) {
        super(MamboFrame.getInstance(), specPanel.getFullName(), true);
        this.specPanel = specPanel;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        if (viewConfigurationBean.getViewConfiguration() == null)
            return;
        initComponents();
        addComponents();
        initLayout();
        initBounds();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (spectrumStackViewer != null) {
                    spectrumStackViewer.getRecordBarPanel().stop();
                }
            }
        });
    }

    public void updateContent() {
        clean();
        DefaultStackDataArrayDAO dao = specPanel.getFilteredSpectrumStackDAO();

        // we affect red color for read curves, and green color for write curves
        for (ArrayList<DataArray> dal : dao.getData()) {
            for (DataArray da : dal) {
                // read curves
                if (da.getName().contains(
                        Messages.getMessage("VIEW_SPECTRUM_READ"))) {
                    spectrumStackViewer.getViewer().setDataViewColor(
                            da.getName(), new Color(255, 0, 0));
                }
                // write curves
                else if (da.getName().contains(
                        Messages.getMessage("VIEW_SPECTRUM_WRITE"))) {
                    spectrumStackViewer.getViewer().setDataViewColor(
                            da.getName(), new Color(0, 255, 0));
                }
                // should never happen
                else {
                    spectrumStackViewer.getViewer().setDataViewColor(
                            da.getName(), new Color(0, 0, 255));
                }
            }
        }
        // Set DAO when dataview colors are registered
        spectrumStackViewer.setDAO(dao);
        // slider legend preparation START
        Hashtable<Integer, JLabel> legend = new Hashtable<Integer, JLabel>();
        ArrayList<String> timeList = specPanel.getTimeList();
        int increment = timeList.size() / 3;
        for (int i = 0; i < timeList.size(); i += increment) {
            legend.put(new Integer(i), new JLabel(timeList.get(i)));
        }
        spectrumStackViewer.getSlider().setLabelTable(legend);
        timeList = null;
        legend = null;
        // slider legend preparation END
        spectrumStackViewer.getSlider().setMajorTickSpacing(increment);
        spectrumStackViewer.getSlider().setMinorTickSpacing(0);
        // Force spectrumStackViewer to browse all spectrums
        spectrumStackViewer.getRecordBarPanel().setSlider_step(1);
        mainPanel.add(spectrumStackViewer);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
        prepareChart();
        GUIUtilities.setObjectBackground(spectrumStackViewer,
                GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(spectrumStackViewer
                .getRecordBarPanel(), GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(spectrumStackViewer
                .getRecordBarPanel().getSlider(), GUIUtilities.VIEW_COLOR);
    }

    public void clean() {
        if (spectrumStackViewer != null) {
            spectrumStackViewer.setDAO(null);
        }
        mainPanel.removeAll();
    }

    private void addComponents() {
        mainPanel.add(spectrumStackViewer);
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
        spectrumStackViewer = new SpectrumStackViewer();
        spectrumStackViewer.getViewer().setManagementPanelVisible(false);
        spectrumStackViewer.setPeriodInMs(100);
        spectrumStackViewer.getRecordBarPanel().getSlider().setForeground(
                Color.BLACK);

        JLChart chart = spectrumStackViewer.getViewer().getChart();
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
