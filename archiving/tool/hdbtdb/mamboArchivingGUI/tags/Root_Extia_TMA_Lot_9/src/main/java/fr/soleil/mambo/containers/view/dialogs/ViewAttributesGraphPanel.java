// +======================================================================
// $Source$
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewAttributesTreePanel.
// (Claisse Laurent) - 13 mars 2008
//
// $Author$
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.comete.dao.DAOFactoryManager;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.containers.view.ViewImagePanel;
import fr.soleil.mambo.containers.view.ViewNumberScalarPanel;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.containers.view.ViewSpectrumStackPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanScalarPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanSpectrumPanel;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewDAOKey;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewAttributesGraphPanel extends JPanel {

    private static final long serialVersionUID = -5615040405787083885L;

    private JTabbedPane attributesTab;

    private ViewConfigurationAttribute[] strings;
    private ChartViewer chart;

    private MamboCleanablePanel[] spectrumPanels;
    private MamboCleanablePanel[] imagePanels;
    private ViewNumberScalarPanel numberScalarsPanel;
    private ViewStringStateBooleanScalarPanel otherScalarsPanel;

    protected GridBagConstraints attrConstraints = null;

    protected PropertyChangeSupport support;

    public final static String SETTING_PANELS = "setting panels";
    public final static String PANELS_SET = "panels set";
    public final static String ADDING_PANELS = "adding panels";
    public final static String PANELS_ADDED = "panels added";
    public final static String LOADING_PANELS = "loading panels";
    public final static String PANELS_LOADED = "panels loaded";
    public final static String CANCELING = "canceling";
    public final static String CANCELED = "canceled";

    private ViewConfigurationBean viewConfigurationBean;
    private int selectedIndex = 0;

    public ViewAttributesGraphPanel(ViewConfigurationBean viewConfigurationBean) {
        super(new GridBagLayout());
        this.viewConfigurationBean = viewConfigurationBean;
        support = new PropertyChangeSupport(this);
        initBorder();
        initBounds();
        initComponents();
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        support.firePropertyChange(new PropertyChangeEvent(this, ADDING_PANELS, null, null));
        if (attrConstraints == null) {
            attrConstraints = new GridBagConstraints();
            attrConstraints.fill = GridBagConstraints.BOTH;
            attrConstraints.gridx = 0;
            attrConstraints.gridy = 0;
            attrConstraints.weightx = 1;
            attrConstraints.weighty = 1;
        }

        attributesTab.add(Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_TITLE"), numberScalarsPanel);
        attributesTab.setToolTipTextAt(0, Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_TOOLTIP"));

        if (strings != null && strings.length > 0) {
            JScrollPane stringScrollPane = new JScrollPane(otherScalarsPanel);
            GUIUtilities.setObjectBackground(stringScrollPane, GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(stringScrollPane.getViewport(),
                    GUIUtilities.VIEW_COLOR);
            attributesTab.add(
                    Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_TITLE"),
                    stringScrollPane);
        }

        for (int i = 0; i < spectrumPanels.length; i++) {
            attributesTab.add(spectrumPanels[i].getName(), spectrumPanels[i]);
            attributesTab.setToolTipTextAt(attributesTab.getComponentCount() - 1, spectrumPanels[i]
                    .getFullName());
        }

        for (int i = 0; i < imagePanels.length; i++) {
            attributesTab.add(imagePanels[i].getName(), imagePanels[i]);
            attributesTab.setToolTipTextAt(attributesTab.getComponentCount() - 1, imagePanels[i]
                    .getFullName());
        }

        this.add(attributesTab, attrConstraints);
        support.firePropertyChange(new PropertyChangeEvent(this, PANELS_ADDED, null, null));
        if ((selectedIndex > -1) && (selectedIndex < attributesTab.getTabCount())) {
            attributesTab.setSelectedIndex(selectedIndex);
        }
    }

    /**
     * 19 juil. 2005
     */
    public void initComponents() {
        support.firePropertyChange(new PropertyChangeEvent(this, SETTING_PANELS, null, null));
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        attributesTab = new JTabbedPane();
        GUIUtilities.setObjectBackground(attributesTab, GUIUtilities.VIEW_COLOR);
        ViewConfiguration vc = viewConfigurationBean.getViewConfiguration();
        TreeMap<String, ViewConfigurationAttribute> stringTable = vc.getAttributes()
                .getStringStateScalarAttributes(vc.getData().isHistoric());
        strings = new ViewConfigurationAttribute[stringTable.size()];
        Set<String> stringSet = stringTable.keySet();
        Iterator<String> stringIt = stringSet.iterator();
        int i = 0;
        while (stringIt.hasNext()) {
            strings[i++] = stringTable.get(stringIt.next());
        }
        if (i > 0) {
            otherScalarsPanel = new ViewStringStateBooleanScalarPanel(strings,
                    viewConfigurationBean);
        }
        else {
            otherScalarsPanel = null;
        }

        TreeMap<String, ViewConfigurationAttribute> imageTable = vc.getAttributes()
                .getImageAttributes(vc.getData().isHistoric());

        imagePanels = new MamboCleanablePanel[imageTable.size()];
        Set<String> imageSet = imageTable.keySet();
        Iterator<String> imageIt = imageSet.iterator();
        i = 0;
        while (imageIt.hasNext()) {
            ViewConfigurationAttribute attribute = imageTable.get(imageIt.next());
            imagePanels[i++] = new ViewImagePanel(attribute, viewConfigurationBean);
        }

        TreeMap<String, ViewConfigurationAttribute> spectrumTable = vc.getAttributes()
                .getSpectrumAttributes(vc.getData().isHistoric());
        setSpectrumPanels(new MamboCleanablePanel[spectrumTable.size()]);
        Set<String> spectrumSet = spectrumTable.keySet();
        Iterator<String> spectrIt = spectrumSet.iterator();
        i = 0;
        while (spectrIt.hasNext()) {
            ViewConfigurationAttribute attribute = spectrumTable.get(spectrIt.next());
            int data_type = attribute.getDataType(vc.getData().isHistoric());
            if (data_type == TangoConst.Tango_DEV_STRING || data_type == TangoConst.Tango_DEV_STATE
                    || data_type == TangoConst.Tango_DEV_BOOLEAN) {
                spectrumPanels[i++] = new ViewStringStateBooleanSpectrumPanel(attribute,
                        viewConfigurationBean);
            }
            else {
                // Time stack mode
                if (ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK == attribute
                        .getProperties().getPlotProperties().getSpectrumViewType()) {
                    spectrumPanels[i++] = new ViewSpectrumStackPanel(attribute,
                            viewConfigurationBean);
                }
                else {
                    // Index or time mode
                    spectrumPanels[i++] = new ViewSpectrumPanel(attribute, viewConfigurationBean);
                }
            }
        }
        setChart(vc.getChartLight());

        if ((viewConfigurationBean != null)
                && (viewConfigurationBean.getViewConfiguration() != null)) {
            String nameViewSelected = viewConfigurationBean.getViewConfiguration()
                    .getDisplayableTitle();
            String lastUpdate = Long.toString(viewConfigurationBean.getViewConfiguration()
                    .getData().getLastUpdateDate().getTime());
            String idViewSelected = nameViewSelected + " - " + lastUpdate;
            DAOFactoryManager.registerFactory(MamboViewDAOFactory.class.getName());
            chart.addKey(MamboViewDAOFactory.class.getName(), new ViewDAOKey(
                    ViewDAOKey.TYPE_NUMBER_SCALAR, idViewSelected));
        }
        numberScalarsPanel = new ViewNumberScalarPanel(chart, viewConfigurationBean);
        chart.getComponent().setCometeBackground(CometeColor.getColor(GUIUtilities.getViewColor()));
        support.firePropertyChange(new PropertyChangeEvent(this, PANELS_SET, null, null));
    }

    private void initBounds() {
        this.setBounds(new Rectangle(0, 0, 2000, 2000));
    }

    private void initBorder() {
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), "", TitledBorder.CENTER,
                TitledBorder.TOP);
        Border border = (Border) (tb);
        this.setBorder(border);
    }

    public void clean() {
        support.firePropertyChange(new PropertyChangeEvent(this, CANCELING, null, null));
        if (attributesTab != null) {
            if (attributesTab.getSelectedIndex() > -1) {
                selectedIndex = attributesTab.getSelectedIndex();
            }
            attributesTab.removeAll();
        }
        if (spectrumPanels != null) {
            for (int i = 0; i < spectrumPanels.length; i++) {
                if (spectrumPanels[i] != null) {
                    spectrumPanels[i].clean();
                }
            }
        }
        if (imagePanels != null) {
            for (int i = 0; i < imagePanels.length; i++) {
                if (imagePanels[i] != null) {
                    imagePanels[i].clean();
                }
            }
        }
        if (numberScalarsPanel != null) {
            numberScalarsPanel.clean();
        }
        if (otherScalarsPanel != null) {
            otherScalarsPanel.clean();
        }
        this.removeAll();
        support.firePropertyChange(new PropertyChangeEvent(this, CANCELED, null, null));
    }

    public void lightClean() {
        if (spectrumPanels != null) {
            for (int i = 0; i < spectrumPanels.length; i++) {
                if (spectrumPanels[i] != null) {
                    spectrumPanels[i].lightClean();
                }
            }
        }
        if (imagePanels != null) {
            for (int i = 0; i < imagePanels.length; i++) {
                if (imagePanels[i] != null) {
                    imagePanels[i].lightClean();
                }
            }
        }
        if (numberScalarsPanel != null) {
            numberScalarsPanel.lightClean();
            ViewConfiguration vc = viewConfigurationBean.getViewConfiguration();
            if (vc != null) {
                setChart(vc.getChartLight());
                numberScalarsPanel.setChart(chart);
            }
        }
        revalidate();
        repaint();
        if (otherScalarsPanel != null) {
            otherScalarsPanel.lightClean();
        }
        revalidate();
        repaint();
    }

    public void resetComponents() {
        clean();
        initComponents();
        addComponents();
    }

    public void loadPanels() {
        // Can't do lightClean because of cancel action

        if ((viewConfigurationBean != null)
                && (viewConfigurationBean.getViewConfiguration() != null)) {
            viewConfigurationBean.getViewConfiguration().computeCurrentId();
        }
        support.firePropertyChange(new PropertyChangeEvent(this, LOADING_PANELS, null, null));
        if (numberScalarsPanel != null) {
            numberScalarsPanel.loadPanel();
        }
        HashMap<String, DbData[]> dataMap = null;
        if (otherScalarsPanel != null) {
            dataMap = otherScalarsPanel.loadPanelAndGetAttrMap();
        }
        if (numberScalarsPanel != null) {
            if (otherScalarsPanel instanceof ViewStringStateBooleanScalarPanel) {
                numberScalarsPanel.setChartAttributeExpressionOfString(dataMap);
            }
        }
        if (dataMap != null) {
            dataMap.clear();
            dataMap = null;
        }
        if (spectrumPanels != null) {
            for (int i = 0; i < spectrumPanels.length; i++) {
                spectrumPanels[i].loadPanel();
            }
        }
        if (imagePanels != null) {
            for (int i = 0; i < imagePanels.length; i++) {
                imagePanels[i].loadPanel();
            }
        }
        support.firePropertyChange(new PropertyChangeEvent(this, PANELS_LOADED, null, null));
    }

    public JTabbedPane getAttributesTab() {
        return attributesTab;
    }

    public void setNumberScalarsPanel(ViewNumberScalarPanel numberScalarsPanel) {
        this.numberScalarsPanel = numberScalarsPanel;
    }

    public MamboCleanablePanel getNumberScalarsPanel() {
        return numberScalarsPanel;
    }

    public void setSpectrumPanels(MamboCleanablePanel[] spectrumPanels) {
        this.spectrumPanels = spectrumPanels;
    }

    public MamboCleanablePanel[] getSpectrumPanels() {
        return spectrumPanels;
    }

    public void setOtherScalarsPanel(ViewStringStateBooleanScalarPanel otherScalarsPanel) {
        this.otherScalarsPanel = otherScalarsPanel;
    }

    public MamboCleanablePanel getOtherScalarsPanel() {
        return otherScalarsPanel;
    }

    public void setChart(ChartViewer chart) {
        this.chart = chart;
    }

    public ChartViewer getChart() {
        return chart;
    }
}
