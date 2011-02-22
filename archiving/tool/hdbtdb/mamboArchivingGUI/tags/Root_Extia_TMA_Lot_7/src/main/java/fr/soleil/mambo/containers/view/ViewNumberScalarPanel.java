package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.Tango.AttrWriteType;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.chart.ChartActionEvent;
import fr.soleil.comete.widget.chart.ChartActionListener;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewNumberScalarPanel extends MamboCleanablePanel implements
        ChartActionListener {

    private static final long     serialVersionUID = -4059096160484432873L;
    protected ChartViewer         chart;
    protected JScrollPane         chartScrollPane;
    private JLabel                loadingLabel;
    private boolean               cleaning         = false;
    private ViewConfigurationBean viewConfigurationBean;
    private GridBagConstraints    loadingLabelConstraints;
    private GridBagConstraints    chartConstraints;

    public ViewNumberScalarPanel(ChartViewer _chart,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        initLayout();
        initConstraints();
        this.viewConfigurationBean = viewConfigurationBean;
        this.chart = _chart;
        loadingLabel = new JLabel(Messages
                .getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        chartScrollPane = new JScrollPane((JComponent) chart.getComponent());
        GUIUtilities.setObjectBackground(chartScrollPane,
                GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(chartScrollPane.getViewport(),
                GUIUtilities.VIEW_COLOR);
        layoutComponents();
        initBorder();
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
    }

    public void initLayout() {
        this.setLayout(new GridBagLayout());
    }

    private void initConstraints() {
        if (loadingLabelConstraints == null) {
            loadingLabelConstraints = new GridBagConstraints();
            loadingLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
            loadingLabelConstraints.gridx = 0;
            loadingLabelConstraints.gridy = 0;
            loadingLabelConstraints.weightx = 1;
            loadingLabelConstraints.weighty = 0;
            loadingLabelConstraints.insets = new Insets(5, 5, 5, 5);
            loadingLabelConstraints.anchor = GridBagConstraints.WEST;
        }
        if (chartConstraints == null) {
            chartConstraints = new GridBagConstraints();
            chartConstraints.fill = GridBagConstraints.BOTH;
            chartConstraints.gridx = 0;
            chartConstraints.gridy = 1;
            chartConstraints.weightx = 1;
            chartConstraints.weighty = 1;
        }
    }

    private void layoutComponents() {
        boolean containsLabel = false;
        boolean containsChart = false;
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) == loadingLabel) {
                containsLabel = true;
            } else if (getComponent(i) == chartScrollPane) {
                containsChart = true;
            }
        }
        if (!containsLabel) {
            add(loadingLabel, loadingLabelConstraints);
        }
        if (!containsChart) {
            add(chartScrollPane, chartConstraints);
        }
    }

    private void initBorder() {
        String msg = getFullName();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP);
        Border border = (Border) (tb);
        this.setBorder(border);
        border = null;
    }

    public void clean() {
        cleaning = true;
        removeAll();
        chartScrollPane = null;
        loadingLabel = null;
        if (chart != null) {
            MamboViewDAOFactory daoFactory = (MamboViewDAOFactory) chart
                    .getDaoFactory();
            if ((daoFactory != null) && (viewConfigurationBean != null)
                    && (viewConfigurationBean.getViewConfiguration() != null)) {
                String nameViewSelected = viewConfigurationBean
                        .getViewConfiguration().getDisplayableTitle();
                String lastUpdate = Long.toString(viewConfigurationBean
                        .getViewConfiguration().getData().getLastUpdateDate()
                        .getTime());
                String idViewSelected = nameViewSelected + " - " + lastUpdate;
                daoFactory.removeKeyForDaoScalar(idViewSelected);
            }
            chart.clearDAO();
        }
        chart = null;
    }

    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning)
            return;
        try {
            if (chart != null) {
                chart.switchDAOFactory(MamboViewDAOFactory.class.getName());
                chart.setReadOnly(false);
                IChartViewer chartViewerComponent = chart.getComponent();
                chartViewerComponent.setAnnotation(IChartViewer.TIME_ANNO,
                        IChartViewer.X);

                viewConfigurationBean.getViewConfiguration().loadAttributes(
                        chart);
                chartViewerComponent.addChartActionListener(this);
                if (chartScrollPane != null) {
                    remove(chartScrollPane);
                    chartScrollPane = null;
                }
                List<DataArray> data = chart.getData();
                if (data == null) {
                    JLabel empty = new JLabel(Messages
                            .getMessage("VIEW_ATTRIBUTES_NO_DATA"),
                            JLabel.CENTER);
                    empty.setOpaque(false);
                    chartScrollPane = new JScrollPane(empty);
                    empty = null;
                } else {
                    if (data.isEmpty()) {
                        JLabel empty = new JLabel(Messages
                                .getMessage("VIEW_ATTRIBUTES_NO_DATA"),
                                JLabel.CENTER);
                        empty.setOpaque(false);
                        chartScrollPane = new JScrollPane(empty);
                        empty = null;
                    } else {
                        chartScrollPane = new JScrollPane(
                                (Component) chartViewerComponent);
                    }
                }
                GUIUtilities.setObjectBackground(chartScrollPane,
                        GUIUtilities.VIEW_COLOR);
                GUIUtilities.setObjectBackground(chartScrollPane.getViewport(),
                        GUIUtilities.VIEW_COLOR);
            }
            loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
            loadingLabel.setToolTipText(Messages
                    .getMessage("VIEW_ATTRIBUTES_LOADED"));
            loadingLabel.setForeground(Color.GREEN);
            layoutComponents();
            revalidate();
            repaint();

        } catch (java.lang.OutOfMemoryError oome) {
            if (extractingManager.isCanceled() || cleaning)
                return;
            outOfMemoryErrorManagement();
            return;
        } catch (Exception e) {
            if (extractingManager.isCanceled() || cleaning)
                return;
            else
                e.printStackTrace();
        }
    }

    public String getName() {
        return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_TITLE");
    }

    public String getFullName() {
        return Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_TITLE");
    }

    @Override
    public void lightClean() {
        loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        remove(chartScrollPane);
        chart.getComponent().removeAllChartActionListeners();
        chartScrollPane = null;
        chart.clearDAO();
    }

    public void setChart(ChartViewer chart) {
        this.chart = chart;
    }

    @Override
    public void actionPerformed(ChartActionEvent evt) {

        // If these are the general properties that have changed
        if (evt.getName().startsWith(IChartViewer.CHART_PROPERTY_ACTION_NAME)) {
            ChartProperties newChartProperties = chart.getComponent()
                    .getChartProperties();
            String title = newChartProperties.getTitle();
            if (title != null) {
                if (title.contains(" - "
                        + Messages.getMessage("VIEW_ATTRIBUTES_START_DATE"))
                        && title.contains(Messages
                                .getMessage("VIEW_ATTRIBUTES_END_DATE"))) {
                    int index = title
                            .indexOf(" - "
                                    + Messages
                                            .getMessage("VIEW_ATTRIBUTES_START_DATE"));
                    String newTitle = title.substring(0, index);
                    newChartProperties.setTitle(newTitle);
                } else if (title.contains(Messages
                        .getMessage("VIEW_ATTRIBUTES_START_DATE"))
                        && title.contains(Messages
                                .getMessage("VIEW_ATTRIBUTES_END_DATE"))) {
                    int index = title.indexOf(Messages
                            .getMessage("VIEW_ATTRIBUTES_START_DATE"));

                    String newTitle = title.substring(0, index);
                    newChartProperties.setTitle(newTitle);
                }
            }
            ViewConfigurationData data = viewConfigurationBean
                    .getViewConfiguration().getData();
            data.getGeneralChartProperties().setGeneralChartProperties(
                    newChartProperties);
        }

        // if these are plotProperties that have changed
        if (evt.getName()
                .startsWith(IChartViewer.DATAVIEW_PROPERTY_ACTION_NAME)) {
            int startInd = IChartViewer.DATAVIEW_PROPERTY_ACTION_NAME.length();
            String nameIdPlot = evt.getName().substring(startInd);

            PlotProperties newPlotProperties = chart.getComponent()
                    .getDataViewPlotProperties(nameIdPlot);
            if (newPlotProperties != null) {
                if (nameIdPlot
                        .endsWith(ViewConfigurationAttribute.READ_DATA_VIEW_KEY)) {
                    nameIdPlot = nameIdPlot.substring(0, nameIdPlot.length()
                            - ViewConfigurationAttribute.READ_DATA_VIEW_KEY
                                    .length() - 1);
                }
                String newName = newPlotProperties.getCurve().getName();
                boolean isWrite = false;
                if (newName
                        .endsWith(ViewConfigurationAttribute.READ_DATA_VIEW_KEY)) {
                    newName = newName.substring(0, newName.length()
                            - ViewConfigurationAttribute.READ_DATA_VIEW_KEY
                                    .length() - 1);
                    newPlotProperties.getCurve().setName(newName);

                } else if (newName
                        .endsWith(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY)) {
                    newName = newName.substring(0, newName.length()
                            - ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY
                                    .length() - 1);
                    newPlotProperties.getCurve().setName(newName);
                    isWrite = true;
                }

                ViewConfigurationAttribute attr = viewConfigurationBean
                        .getViewConfiguration().getAttributes().getAttribute(
                                nameIdPlot);

                if (attr != null) { // If it is an attribute
                    ViewConfigurationAttributePlotProperties attrProperties = attr
                            .getProperties().getPlotProperties();
                    int writable = attr.getDataWritable(true);
                    switch (writable) {
                        case AttrWriteType._READ:
                        case AttrWriteType._WRITE:
                            attrProperties.setPlotProperties(newPlotProperties);
                            break;
                        case AttrWriteType._READ_WITH_WRITE:
                        case AttrWriteType._READ_WRITE:
                            // no change if it is in writing for an attribute of
                            // type READ / WRITE
                            if (!isWrite) {
                                attrProperties
                                        .setPlotProperties(newPlotProperties);
                            }
                            break;
                    }
                } else { // if it is an expression
                    TreeMap<String, ExpressionAttribute> expressions = viewConfigurationBean
                            .getViewConfiguration().getExpressions();

                    Set<String> keys = expressions.keySet();
                    Iterator<String> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        ExpressionAttribute currentAttribut = expressions
                                .get(key);
                        if (currentAttribut.getId().equals(nameIdPlot)) {
                            currentAttribut.getProperties().setPlotProperties(
                                    newPlotProperties);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean getActionState(ChartActionEvent evt) {
        return true;
    }

}
