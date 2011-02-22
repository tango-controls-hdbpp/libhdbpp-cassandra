package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;

import fr.soleil.comete.util.StackDataArray;
import fr.soleil.comete.widget.ChartPlayer;
import fr.soleil.comete.widget.IChartPlayer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewSpectrumStackPanel;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.ViewDAOKey;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * @author awo
 */
public class ViewSpectrumStackChartDialog extends JDialog {

    private static final long      serialVersionUID = 9026556560129510126L;

    private ChartPlayer            spectrumStackViewer;
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
                    spectrumStackViewer.getComponent().stop();
                    clean();
                    spectrumStackViewer = null;
                }
            }
        });
    }

    public void updateContent() {
        clean();

        int checkBoxSelected = specPanel.getCheckBoxSelected();
        DbData[] splitedData = specPanel.getSplitedData();

        spectrumStackViewer.addKey(MamboViewDAOFactory.class.getName(),
                new ViewDAOKey(splitedData, checkBoxSelected));
        spectrumStackViewer.switchDAOFactory(MamboViewDAOFactory.class
                .getName());
        IChartPlayer spectrumStackViewerComponent = spectrumStackViewer
                .getComponent();
        StackDataArray data = spectrumStackViewer.getData();
        if (data != null) {
            // we affect red color for read curves, and blue
            // color for write curves
            for (int i = 0; i < data.size(); i++) {
                for (int j = 0; j < data.get(i).size(); j++) {
                    String name = data.getDataArray(i).get(j).getId();
                    if (name
                            .contains(Messages.getMessage("VIEW_SPECTRUM_READ"))) {
                        // read curves
                        spectrumStackViewerComponent.setDataViewCometeColor(
                                name, CometeColor.RED);
                    } else if (name.contains(Messages
                            .getMessage("VIEW_SPECTRUM_WRITE"))) {
                        // write curves
                        spectrumStackViewerComponent.setDataViewCometeColor(
                                name, CometeColor.BLUE);
                    } else {
                        // should never happen
                        spectrumStackViewerComponent.setDataViewCometeColor(
                                name, CometeColor.BLACK);
                    }
                }
            }
        }
        // Slider legend preparation
        ArrayList<String> timeList = specPanel.getTimeList();
        int increment = timeList.size() / 3;
        String[] label = new String[(timeList.size() / increment) + 1];
        int[] value = new int[(timeList.size() / increment) + 1];

        for (int i = 0; i < timeList.size() / increment; i++) {
            value[i] = i * increment;
            label[i] = timeList.get(i * increment);
        }
        value[timeList.size() / increment] = timeList.size() - 1;
        label[timeList.size() / increment] = timeList.get(timeList.size() - 1);

        spectrumStackViewerComponent.getSlider().setLabelTable(label, value);
        timeList = null;
        // slider legend preparation END

        // Though it should not, setMajorTickSpacing(0) throws an exception.
        // That's why such a test is done.

        if (increment == 0) {
            spectrumStackViewerComponent.getSlider().setPaintLabels(false);
        } else {
            spectrumStackViewerComponent.getSlider().setPaintLabels(true);
            spectrumStackViewerComponent.getSlider().setMajorTickSpacing(
                    increment);
            spectrumStackViewerComponent.getSlider().setMinorTickSpacing(0);
        }

        // Force spectrumStackViewer to browse all spectrums
        spectrumStackViewerComponent.getSlider().setStep(1);

        mainPanel.add((Component) spectrumStackViewer.getComponent());

    }

    private void initComponents() {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
        prepareChart();
        IChartPlayer spectrumStackViewerComponent = spectrumStackViewer
                .getComponent();
        spectrumStackViewerComponent.setCometeBackground(CometeColor
                .getColor(GUIUtilities.getViewColor()));
    }

    public void clean() {
        if (spectrumStackViewer != null) {
            spectrumStackViewer.removeKey(Mambo.class.getName());
            spectrumStackViewer.clearDAO();
        }
        mainPanel.removeAll();
    }

    private void addComponents() {
        mainPanel.add((Component) spectrumStackViewer.getComponent());
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
        spectrumStackViewer = new ChartPlayer();
        IChartPlayer spectrumStackViewerComponent = spectrumStackViewer
                .getComponent();
        spectrumStackViewerComponent.setManagementPanelVisible(false);
        spectrumStackViewerComponent.setPeriodInMs(100);
        spectrumStackViewerComponent.getSlider().setCometeForeground(
                CometeColor.BLACK);

        spectrumStackViewerComponent.setPreferDialogForTable(true, true);
        spectrumStackViewerComponent.setParentForTable(this);

        ViewConfiguration selectedViewConfiguration = ViewConfigurationBeanManager
                .getInstance().getSelectedConfiguration();
        if (selectedViewConfiguration != null) {
            ViewConfigurationData data = selectedViewConfiguration.getData();
            String title = "";
            if (data != null) {
                if (spectrumStackViewerComponent.getHeader() != null) {
                    title += spectrumStackViewerComponent.getHeader() + " - ";
                }
                title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE")
                        + data.getStartDate().toString() + ", "
                        + Messages.getMessage("VIEW_ATTRIBUTES_END_DATE")
                        + data.getEndDate().toString();
            }
            spectrumStackViewerComponent.setHeader(title);
        }
    }
}
