package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
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
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.util.Point;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.IChartViewerListener;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.Messages;

public class ViewNumberScalarPanel extends MamboCleanablePanel implements IChartViewerListener {

    private static final long serialVersionUID = -4059096160484432873L;
    protected ChartViewer chart;
    protected JScrollPane chartScrollPane;
    private JLabel loadingLabel;
    private boolean cleaning = false;
    private ViewConfigurationBean viewConfigurationBean;
    private GridBagConstraints loadingLabelConstraints;
    private GridBagConstraints chartConstraints;

    public ViewNumberScalarPanel(ChartViewer _chart, ViewConfigurationBean viewConfigurationBean) {
        super();
        initLayout();
        initConstraints();
        this.viewConfigurationBean = viewConfigurationBean;
        this.chart = _chart;
        loadingLabel = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        chartScrollPane = new JScrollPane((JComponent) chart.getComponent());
        GUIUtilities.setObjectBackground(chartScrollPane, GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(chartScrollPane.getViewport(), GUIUtilities.VIEW_COLOR);
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
            }
            else if (getComponent(i) == chartScrollPane) {
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
                .createEtchedBorder(EtchedBorder.LOWERED), msg, TitledBorder.CENTER,
                TitledBorder.TOP);
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
            MamboViewDAOFactory daoFactory = (MamboViewDAOFactory) chart.getDaoFactory();
            if ((daoFactory != null) && (viewConfigurationBean != null)
                    && (viewConfigurationBean.getViewConfiguration() != null)) {
                String nameViewSelected = viewConfigurationBean.getViewConfiguration()
                        .getDisplayableTitle();
                String lastUpdate = Long.toString(viewConfigurationBean.getViewConfiguration()
                        .getData().getLastUpdateDate().getTime());
                String idViewSelected = nameViewSelected + " - " + lastUpdate;
                daoFactory.removeKeyForDaoScalar(idViewSelected);
            }
            chart.clearDAO();
        }
        chart = null;
    }

    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning)
            return;
        try {
            if (chart != null) {
                chart.switchDAOFactory(MamboViewDAOFactory.class.getName());
                IChartViewer chartViewerComponent = chart.getComponent();
                chartViewerComponent.setAnnotation(IChartViewer.TIME_ANNO, IChartViewer.X);

                viewConfigurationBean.getViewConfiguration().loadAttributes(chart);
                chartViewerComponent.addChartViewerListener(this);
                if (chartScrollPane != null) {
                    remove(chartScrollPane);
                    chartScrollPane = null;
                }
                List<DataArray> data = chart.getData();
                if (data == null) {
                    JLabel empty = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA"),
                            JLabel.CENTER);
                    empty.setOpaque(false);
                    chartScrollPane = new JScrollPane(empty);
                    empty = null;
                }
                else {
                    if (data.isEmpty()) {
                        JLabel empty = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA"),
                                JLabel.CENTER);
                        empty.setOpaque(false);
                        chartScrollPane = new JScrollPane(empty);
                        empty = null;
                    }
                    else {
                        chartScrollPane = new JScrollPane((Component) chartViewerComponent);
                    }
                }
                GUIUtilities.setObjectBackground(chartScrollPane, GUIUtilities.VIEW_COLOR);
                GUIUtilities.setObjectBackground(chartScrollPane.getViewport(),
                        GUIUtilities.VIEW_COLOR);
            }
            loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
            loadingLabel.setToolTipText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
            loadingLabel.setForeground(Color.GREEN);
            layoutComponents();
            revalidate();
            repaint();

        }
        catch (java.lang.OutOfMemoryError oome) {
            if (extractingManager.isCanceled() || cleaning)
                return;
            outOfMemoryErrorManagement();
            return;
        }
        catch (Exception e) {
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
        chart.getComponent().removeChartViewerListener(this);
        chartScrollPane = null;
        chart.clearDAO();
    }

    public void setChart(ChartViewer chart) {
        this.chart = chart;
    }

    public void setChartAttributeExpressionOfString(HashMap<String, DbData[]> dataMap) {
        viewConfigurationBean.getViewConfiguration().setChartAttributeExpressionOfString(dataMap,
                chart);
    }

    @Override
    public void configurationChanged(String id) {
        if (id != null) {
            if (id.startsWith(IChartViewer.CHART_PROPERTY_ACTION_NAME)) {
                // Chart properties changed
                if (chart != null) {
                    ChartProperties newChartProperties = chart.getComponent().getChartProperties();
                    String title = newChartProperties.getTitle();
                    if (title != null) {
                        if (title.contains(" - "
                                + Messages.getMessage("VIEW_ATTRIBUTES_START_DATE"))
                                && title.contains(Messages.getMessage("VIEW_ATTRIBUTES_END_DATE"))) {
                            int index = title.indexOf(" - "
                                    + Messages.getMessage("VIEW_ATTRIBUTES_START_DATE"));
                            String newTitle = title.substring(0, index);
                            newChartProperties.setTitle(newTitle);
                        }
                        else if (title.contains(Messages.getMessage("VIEW_ATTRIBUTES_START_DATE"))
                                && title.contains(Messages.getMessage("VIEW_ATTRIBUTES_END_DATE"))) {
                            int index = title.indexOf(Messages
                                    .getMessage("VIEW_ATTRIBUTES_START_DATE"));

                            String newTitle = title.substring(0, index);
                            newChartProperties.setTitle(newTitle);
                        }
                    }
                    ViewConfigurationData data = viewConfigurationBean.getViewConfiguration()
                            .getData();
                    data.setChartProperties(newChartProperties);
                } // end if (chart != null)
            } // end if (id.startsWith(IChartViewer.CHART_PROPERTY_ACTION_NAME))
            else if (id.startsWith(IChartViewer.DATAVIEW_PROPERTY_ACTION_NAME)) {
                // plot properties changed
                if (chart != null) {
                    int startInd = IChartViewer.DATAVIEW_PROPERTY_ACTION_NAME.length();
                    String nameIdPlot = id.substring(startInd);

                    PlotProperties newPlotProperties = chart.getComponent()
                            .getDataViewPlotProperties(nameIdPlot);
                    if ((newPlotProperties != null) && (newPlotProperties.getCurve() != null)
                            && (newPlotProperties.getCurve().getName() != null)) {
                        boolean isWrite = false;
                        if (nameIdPlot.endsWith(ViewConfigurationAttribute.READ_DATA_VIEW_KEY)) {
                            nameIdPlot = nameIdPlot.substring(0, nameIdPlot.length()
                                    - ViewConfigurationAttribute.READ_DATA_VIEW_KEY.length() - 1);
                        }
                        else if (nameIdPlot
                                .endsWith(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY)) {
                            nameIdPlot = nameIdPlot.substring(0, nameIdPlot.length()
                                    - ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY.length() - 1);
                            isWrite = true;
                        }

                        ViewConfigurationAttribute attr = viewConfigurationBean
                                .getViewConfiguration().getAttributes().getAttribute(nameIdPlot);

                        if (attr != null) {
                            // If it is an attribute
                            ViewConfigurationAttributePlotProperties attrProperties = attr
                                    .getProperties().getPlotProperties();
                            newPlotProperties.getCurve().setName(
                                    attrProperties.getCurve().getName());
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
                                        attrProperties.setPlotProperties(newPlotProperties);
                                    }
                                    break;
                            }
                        } // end if (attr != null)
                        else {
                            // if it is an expression
                            TreeMap<String, ExpressionAttribute> expressions = viewConfigurationBean
                                    .getViewConfiguration().getExpressions();

                            Set<String> keys = expressions.keySet();
                            Iterator<String> iterator = keys.iterator();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                ExpressionAttribute currentAttribut = expressions.get(key);
                                if (currentAttribut.getId().equals(nameIdPlot)) {
                                    currentAttribut.getProperties().setPlotProperties(
                                            newPlotProperties);
                                    break;
                                }
                            }
                        } // end end if (attr != null)...else
                    } // end if ((newPlotProperties != null) && (newPlotProperties.getCurve() !=
                    // null) && (newPlotProperties.getCurve().getName() != null))
                } // end if (chart != null)
            } // end else if (id.startsWith(IChartViewer.DATAVIEW_PROPERTY_ACTION_NAME))
        } // end if (id != null)
    }

    @Override
    public void dataChanged(List<DataArray> arrayList) {
        // nothing to do
    }

    @Override
    public void selectedPointChanged(Point point) {
        // nothing to do
    }

    @Override
    public void selectedValueChanged(double y) {
        // nothing to do
    }

    @Override
    public void selectedXChanged(double x) {
        // nothing to do
    }

    @Override
    public void selectedYChanged(double y) {
        // nothing to do
    }

}
