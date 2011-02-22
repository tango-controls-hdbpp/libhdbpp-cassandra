package fr.soleil.mambo.bean.view;

import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.dao.DAOFactoryManager;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.mambo.actions.view.listeners.IDateRangeBoxListener;
import fr.soleil.mambo.actions.view.listeners.IViewConfigurationBeanListener;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.components.view.VCPopupMenu;
import fr.soleil.mambo.containers.view.ViewAttributesPanel;
import fr.soleil.mambo.containers.view.ViewGeneralPanel;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;
import fr.soleil.mambo.containers.view.dialogs.VCVariationDialog;
import fr.soleil.mambo.containers.view.dialogs.VCVariationEditCopyDialog;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.event.DateRangeBoxEvent;
import fr.soleil.mambo.event.ViewConfigurationBeanEvent;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.models.VCPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;

public class ViewConfigurationBean implements IDateRangeBoxListener {

    private ViewConfiguration viewConfiguration;
    private ViewConfiguration editingViewConfiguration;

    private ViewGeneralPanel generalPanel;
    private ViewAttributesPanel attributesPanel;
    private JPanel viewDisplayPanel;

    private VCPossibleAttributesTreeModel vcPossibleAttributesTreeModel;
    private VCAttributesTreeModel editingModel;
    private VCAttributesTreeModel validatedModel;

    private VCEditDialog editDialog;
    private VCVariationDialog variationDialog;
    private VCVariationEditCopyDialog variationEditCopyDialog;

    private boolean isShowingTreePanel;

    private ArrayList<WeakReference<IViewConfigurationBeanListener>> listeners;

    private VCPopupMenu popupMenu;

    public ViewConfigurationBean(ViewConfiguration viewConfiguration) {
        super();
        DAOFactoryManager.registerFactory(MamboViewDAOFactory.class.getName());
        this.viewConfiguration = viewConfiguration;
        this.editingViewConfiguration = null;
        listeners = new ArrayList<WeakReference<IViewConfigurationBeanListener>>();
        initUI();
    }

    private void initUI() {
        boolean historic = true;
        if ((viewConfiguration != null) && (viewConfiguration.getData() != null)) {
            historic = viewConfiguration.getData().isHistoric();
        }
        vcPossibleAttributesTreeModel = new VCPossibleAttributesTreeModel(historic);
        editingModel = new VCAttributesTreeModel(vcPossibleAttributesTreeModel);
        editingModel.setHistoric(historic);
        validatedModel = new VCAttributesTreeModel(vcPossibleAttributesTreeModel);
        validatedModel.setHistoric(historic);
    }

    public ViewConfiguration getViewConfiguration() {
        return viewConfiguration;
    }

    public ViewConfiguration getEditingViewConfiguration() {
        return editingViewConfiguration;
    }

    public void cancelEdition() {
        editingViewConfiguration = null;
        refreshEditingUI();
    }

    public void refreshMainUI() {
        pushSelectedViewConfigurationData();
        // pushSelectedAttributes();
        if (viewConfiguration != null) {
            OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
            if (openedVCComboBox != null) {
                openedVCComboBox.selectElement(viewConfiguration);
            }
        }
        getAttributesPanel().getViewAttributesTreePanel().getTree().expandAll(true);
        getAttributesPanel().revalidate();
        getAttributesPanel().repaint();
        getViewDisplayPanel().revalidate();
        getViewDisplayPanel().repaint();
    }

    public void refreshEditingUI() {
        pushEditingViewConfigurationData();
        pushEditingAttributes();
        if (editingViewConfiguration == null) {
            getEditDialog().getExpressionTab().getExpressionTree().getModel()
                    .setExpressionAttributes(new TreeMap<String, ExpressionAttribute>());
        }
        else {
            getEditDialog().getExpressionTab().getExpressionTree().getModel()
                    .setExpressionAttributes(editingViewConfiguration.getExpressions());
        }
    }

    public void editViewConfiguration() {
        if (viewConfiguration == null) {
            editingViewConfiguration = new ViewConfiguration();
        }
        else {
            editingViewConfiguration = viewConfiguration.cloneVC();
        }
        for (int i = 1; i < getEditDialog().getVcCustomTabbedPane().getTabCount(); i++) {
            getEditDialog().getVcCustomTabbedPane().setEnabledAt(i, false);
        }
        refreshEditingUI();
        getEditDialog().getVcCustomTabbedPane().setEnabledAt(0, true);
        getEditDialog().getVcCustomTabbedPane().setSelectedIndex(0);
        getEditDialog().pack();
        getEditDialog().setLocationRelativeTo(viewDisplayPanel);
        Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();
        Rectangle bounds = getEditDialog().getBounds();
        if (!maxBounds.contains(bounds)) {
            Rectangle newBounds = new Rectangle(bounds);
            if (newBounds.x < maxBounds.x) {
                newBounds.x = maxBounds.x;
            }
            if (newBounds.y < maxBounds.y) {
                newBounds.y = maxBounds.y;
            }
            int maxWidth = maxBounds.x + maxBounds.width;
            int currentWidth = newBounds.x + newBounds.width;
            if (currentWidth > maxWidth) {
                newBounds.width -= (currentWidth - maxWidth);
                if (newBounds.width < 50) {
                    newBounds.x = maxBounds.x;
                    newBounds.width = maxBounds.width;
                }
            }
            int maxHeight = maxBounds.y + maxBounds.height;
            int currentHeight = newBounds.y + newBounds.height;
            if (currentHeight > maxHeight) {
                newBounds.height -= (currentHeight - maxHeight);
                if (newBounds.height < 50) {
                    newBounds.y = maxBounds.y;
                    newBounds.height = maxBounds.height;
                }
            }
            getEditDialog().setBounds(newBounds);
            newBounds = null;
        }
        bounds = null;
        maxBounds = null;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                getEditDialog().scrollBackTo0();
            }
        });
        getEditDialog().setVisible(true);
    }

    public VCEditDialog getEditDialog() {
        if (editDialog == null) {
            editDialog = new VCEditDialog(this);
        }
        return editDialog;
    }

    public VCPossibleAttributesTreeModel getVcPossibleAttributesTreeModel() {
        return vcPossibleAttributesTreeModel;
    }

    public VCAttributesTreeModel getEditingModel() {
        return editingModel;
    }

    public VCAttributesTreeModel getValidatedModel() {
        return validatedModel;
    }

    public synchronized ViewAttributesPanel getAttributesPanel() {
        if (attributesPanel == null) {
            attributesPanel = new ViewAttributesPanel(this);
            isShowingTreePanel = true;
            attributesPanel.getViewAttributesTreePanel().getDateRangeBox().addDateRangeBoxListener(
                    this);
            prepareGraphPanel();
        }
        return attributesPanel;
    }

    public synchronized ViewGeneralPanel getGeneralPanel() {
        if (generalPanel == null) {
            generalPanel = new ViewGeneralPanel();
        }
        return generalPanel;
    }

    private void pushSelectedViewConfigurationData() {
        ViewConfigurationData data = null;
        if (viewConfiguration != null) {
            data = viewConfiguration.getData();
        }
        if (data == null) {
            getGeneralPanel().setLastUpdateDate(null);
            getGeneralPanel().setCreationDate(null);
            getGeneralPanel().setName(null);
            getGeneralPanel().setPath(null);
        }
        else {
            if (validatedModel.isHistoric() != data.isHistoric()) {
                validatedModel.setHistoric(data.isHistoric());
            }
            getGeneralPanel().setLastUpdateDate(data.getLastUpdateDate());
            getGeneralPanel().setCreationDate(data.getCreationDate());
            getGeneralPanel().setName(data.getName());
            getGeneralPanel().setPath(data.getPath());
        }

        // No need to listen to the DateRangeBox while updating it
        getAttributesPanel().getViewAttributesTreePanel().getDateRangeBox()
                .removeDateRangeBoxListener(this);
        getAttributesPanel().getViewAttributesTreePanel().getDateRangeBox().update(data);
        getAttributesPanel().getViewAttributesTreePanel().getDateRangeBox()
                .addDateRangeBoxListener(this);
    }

    private void pushEditingViewConfigurationData() {
        ViewConfigurationData data = null;
        if (editingViewConfiguration != null) {
            data = editingViewConfiguration.getData();
        }
        if (data != null) {
            GeneralTab generalTab = getEditDialog().getGeneralTab();
            generalTab.setLastUpdateDate(data.getLastUpdateDate());
            generalTab.setCreationDate(data.getCreationDate());
            generalTab.setName(data.getName());
            generalTab.setSamplingType(data.getSamplingType());

            boolean dynamic = data.isDynamicDateRange();
            if (dynamic) {
                Timestamp[] range = generalTab.getDynamicStartAndEndDates();
                generalTab.setStartDate(range[0]);
                generalTab.setEndDate(range[1]);
            }
            else {
                generalTab.setStartDate(data.getStartDate());
                generalTab.setEndDate(data.getEndDate());
            }
            generalTab.setDynamicDateRange(dynamic);
            generalTab.setDateRange(data.getDateRange());
            generalTab.setHistoric(data.isHistoric());

            // we need to update the trees according to the "historic" state
            VCAttributesTreeModel vCAttributesTreeModel = getEditDialog().getAttributesTab()
                    .getSelectedAttributesTree().getModel();
            vCAttributesTreeModel.setRootName(data.isHistoric());

            vcPossibleAttributesTreeModel.setHistoric(data.isHistoric());
            editingModel.setHistoric(data.isHistoric());

            // General chart properties
            ChartProperties chartProperties = data.getChartProperties();
            if (chartProperties != null) {
                ChartGeneralTabbedPane generalTabbedPane = getEditDialog().getGeneralTab()
                        .getChartGeneralTabbedPane();
                generalTabbedPane.setProperties(chartProperties);
            }
            chartProperties = null;
        }
    }

    private void pushSelectedAttributes() {
        ViewConfigurationAttributes attributes = null;
        if (viewConfiguration != null) {
            attributes = viewConfiguration.getAttributes();
        }
        // Save expanded Row
        TreeMap<String, ViewConfigurationAttribute> attributesHash = null;
        if (attributes != null) {
            attributesHash = attributes.getAttributes();
        }
        if (attributesHash == null) {
            attributesHash = new TreeMap<String, ViewConfigurationAttribute>();
        }
        Set<String> keySet = attributesHash.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        Vector<Domain> domainList = new Vector<Domain>();
        while (keyIterator.hasNext()) {
            String completeName = (String) keyIterator.next();
            ViewConfigurationAttribute next = attributesHash.get(completeName);
            String domain_s = next.getDomainName();
            String family_s = next.getFamilyName();
            String member_s = next.getMemberName();
            String attr_s = next.getName();
            ViewConfigurationAttribute attr = new ViewConfigurationAttribute();
            attr.setName(attr_s);
            attr.setCompleteName(completeName);
            attr.setDevice(member_s);
            attr.setDomain(domain_s);
            attr.setFamily(family_s);
            Domain domain = new Domain(domain_s);
            Family family = new Family(family_s);
            Member member = new Member(member_s);
            Domain dom = Domain.hasDomain(domainList, domain_s);
            if (dom != null) {
                Family fam = dom.getFamily(family_s);
                if (fam != null) {
                    Member mem = fam.getMember(member_s);
                    if (mem != null) {
                        mem.addAttribute(attr);
                        fam.addMember(mem);
                        dom.addFamily(fam);
                        domainList = Domain.addDomain(domainList, dom);
                    }
                    else {
                        member.addAttribute(attr);
                        fam.addMember(member);
                        dom.addFamily(fam);
                        domainList = Domain.addDomain(domainList, dom);
                    }
                }
                else {
                    member.addAttribute(attr);
                    family.addMember(member);
                    dom.addFamily(family);
                    domainList = Domain.addDomain(domainList, dom);
                }
            }
            else {
                member.addAttribute(attr);
                family.addMember(member);
                domain.addFamily(family);
                domainList = Domain.addDomain(domainList, domain);
            }
        }
        validatedModel.removeAll();
        validatedModel.build(domainList);
        validatedModel.reload();
        getAttributesPanel().repaint();
    }

    private void pushEditingAttributes() {
        ViewConfigurationAttributes attributes = null;
        TreeMap<String, ExpressionAttribute> expressions = null;
        if (editingViewConfiguration != null) {
            attributes = editingViewConfiguration.getAttributes();
            expressions = editingViewConfiguration.getExpressions();
        }
        // Save expanded Row
        TreeMap<String, ViewConfigurationAttribute> attributesHash = null;
        if (attributes != null) {
            attributesHash = attributes.getAttributes();
        }
        if (attributesHash == null) {
            attributesHash = new TreeMap<String, ViewConfigurationAttribute>();
        }
        Set<String> keySet = attributesHash.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        Vector<Domain> domainList = new Vector<Domain>();
        while (keyIterator.hasNext()) {
            String completeName = (String) keyIterator.next();
            ViewConfigurationAttribute next = attributesHash.get(completeName);
            String domain_s = next.getDomainName();
            String family_s = next.getFamilyName();
            String member_s = next.getMemberName();
            String attr_s = next.getName();
            ViewConfigurationAttribute attr = new ViewConfigurationAttribute();
            attr.setName(attr_s);
            attr.setCompleteName(completeName);
            attr.setDevice(member_s);
            attr.setDomain(domain_s);
            attr.setFamily(family_s);
            Domain domain = new Domain(domain_s);
            Family family = new Family(family_s);
            Member member = new Member(member_s);
            Domain dom = Domain.hasDomain(domainList, domain_s);
            if (dom != null) {
                Family fam = dom.getFamily(family_s);
                if (fam != null) {
                    Member mem = fam.getMember(member_s);
                    if (mem != null) {
                        mem.addAttribute(attr);
                        fam.addMember(mem);
                        dom.addFamily(fam);
                        domainList = Domain.addDomain(domainList, dom);
                    }
                    else {
                        member.addAttribute(attr);
                        fam.addMember(member);
                        dom.addFamily(fam);
                        domainList = Domain.addDomain(domainList, dom);
                    }
                }
                else {
                    member.addAttribute(attr);
                    family.addMember(member);
                    dom.addFamily(family);
                    domainList = Domain.addDomain(domainList, dom);
                }
            }
            else {
                member.addAttribute(attr);
                family.addMember(member);
                domain.addFamily(family);
                domainList = Domain.addDomain(domainList, domain);
            }
        }
        editingModel.removeAll();
        editingModel.build(domainList);
        editingModel.reload();
        int size = 0;
        if (attributes != null) {
            if (attributes.getAttributes() != null) {
                size = attributes.getAttributes().size();
            }
        }
        getEditDialog().getAttributesPlotPropertiesTab().getPropertiesPanel().setColorIndex(
                AttributesPlotPropertiesPanel.getColorIndexForSize(size));
        getEditDialog().getAttributesPlotPropertiesTab().getPropertiesPanel().rotateCurveColor();
        size = 0;
        if (expressions != null) {
            size = expressions.size();
        }
        if (getEditDialog().isVisible()) {
            getEditDialog().repaint();
        }
    }

    public void pushAttributesPlotProperties(ViewConfigurationAttributePlotProperties plotProperties) {
        if (plotProperties != null) {
            AttributesPlotPropertiesPanel plotPropertiesPanel = getEditDialog()
                    .getAttributesPlotPropertiesTab().getPropertiesPanel();
            plotPropertiesPanel.setViewConfigurationAttributePlotProperties(plotProperties);
        }
    }

    public void validateEdition() {
        getEditDialog().setVisible(false);
        if (editingViewConfiguration != null) {
            if (attributesPanel != null) {
                // clean panels to clean dao factory
                attributesPanel.getViewAttributesGraphPanel().clean();
                attributesPanel.repaint();
            }
            if (viewConfiguration == null) {
                viewConfiguration = editingViewConfiguration;
            }
            else {
                viewConfiguration.setData(editingViewConfiguration.getData());
                viewConfiguration.setAttributes(editingViewConfiguration.getAttributes());
                viewConfiguration.setExpressions(editingViewConfiguration.getExpressions());
                viewConfiguration.setPath(editingViewConfiguration.getPath());
                viewConfiguration.setModified(editingViewConfiguration.isModified());
            }
        }
        editingViewConfiguration = null;
        refreshMainUI();
        prepareGraphPanel();
        fireSelectedStructureEvent();
        getAttributesPanel().getViewAttributesTreePanel().getViewActionPanel()
                .refreshButtonChangeColor();
        cancelEdition();
    }

    public VCVariationDialog getVariationDialog() {
        if (variationDialog == null) {
            variationDialog = new VCVariationDialog(this);
        }
        return variationDialog;
    }

    public VCVariationEditCopyDialog getVariationEditCopyDialog() {
        if (variationEditCopyDialog == null) {
            variationEditCopyDialog = new VCVariationEditCopyDialog(this);
        }
        return variationEditCopyDialog;
    }

    public void enableDisplayPanel(boolean enabled) {
        if (viewDisplayPanel != null) {
            viewDisplayPanel.setEnabled(enabled);
            if (!enabled) {
                if (popupMenu != null) {
                    popupMenu.setVisible(false);
                }
            }
        }
        if (attributesPanel != null) {
            attributesPanel.getViewAttributesTreePanel().getViewActionPanel()
                    .setEnableAllComponent(enabled);
        }
    }

    public synchronized JPanel getViewDisplayPanel() {
        if (viewDisplayPanel == null) {
            viewDisplayPanel = new JPanel(new GridBagLayout());
            popupMenu = new VCPopupMenu(this);
            viewDisplayPanel.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    maybeShowPopup(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    maybeShowPopup(e);
                }

                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        if (viewDisplayPanel.isEnabled()) {
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            });

            GridBagConstraints generalConstraints = new GridBagConstraints();
            generalConstraints.fill = GridBagConstraints.HORIZONTAL;
            generalConstraints.gridx = 0;
            generalConstraints.gridy = 0;
            generalConstraints.weightx = 1;
            generalConstraints.weighty = 0;
            viewDisplayPanel.add(getGeneralPanel(), generalConstraints);
            GridBagConstraints attributesConstraints = new GridBagConstraints();
            attributesConstraints.fill = GridBagConstraints.BOTH;
            attributesConstraints.gridx = 0;
            attributesConstraints.gridy = 1;
            attributesConstraints.weightx = 1;
            attributesConstraints.weighty = 1;
            viewDisplayPanel.add(getAttributesPanel(), attributesConstraints);
            GUIUtilities.setObjectBackground(viewDisplayPanel, GUIUtilities.VIEW_COLOR);
        }
        return viewDisplayPanel;
    }

    public void prepareGraphPanel() {
        pushSelectedAttributes();
        attributesPanel.getViewAttributesGraphPanel().clean();
        attributesPanel.getViewAttributesGraphPanel().initComponents();
    }

    @Override
    public void dateRangeBoxChanged(DateRangeBoxEvent event) {
        if ((viewConfiguration != null) && (viewConfiguration.getData() != null) && (event != null)
                && (event.getSource() != null)) {
            // Some changes occurred in DateRangeBox
            // --> update ViewConfigurationData
            // if (attributesPanel != null) {
            // // clean panels to clean dao factory
            // attributesPanel.getViewAttributesGraphPanel().clean();
            // attributesPanel.repaint();
            // }
            viewConfiguration.getData().updateDateRangeBox(event.getSource());
            viewConfiguration.setModified(true);
            fireSelectedDateEvent();
        }
    }

    public void addViewConfigurationBeanListener(IViewConfigurationBeanListener listener) {
        if (listener != null) {
            synchronized (listeners) {
                boolean canAdd = true;
                for (int i = 0; i < listeners.size(); i++) {
                    WeakReference<IViewConfigurationBeanListener> soft = listeners.get(i);
                    IViewConfigurationBeanListener temp = soft.get();
                    if (listener.equals(temp)) {
                        temp = null;
                        soft = null;
                        canAdd = false;
                        break;
                    }
                    temp = null;
                    soft = null;
                }
                if (canAdd) {
                    listeners.add(new WeakReference<IViewConfigurationBeanListener>(listener));
                }
            }
        }
    }

    public void removeViewConfigurationBeanListener(IViewConfigurationBeanListener listener) {
        if (listener != null) {
            synchronized (listeners) {
                ArrayList<WeakReference<IViewConfigurationBeanListener>> toRemove = new ArrayList<WeakReference<IViewConfigurationBeanListener>>();
                for (int i = 0; i < listeners.size(); i++) {
                    WeakReference<IViewConfigurationBeanListener> soft = listeners.get(i);
                    IViewConfigurationBeanListener temp = soft.get();
                    if ((temp == null) || listener.equals(temp)) {
                        toRemove.add(soft);
                    }
                    temp = null;
                    soft = null;
                }
                listeners.removeAll(toRemove);
                toRemove.clear();
                toRemove = null;
            }
        }
    }

    public void removeAllViewConfigurationBeanListeners() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

    private void fireSelectedDateEvent() {
        ViewConfigurationBeanEvent event = new ViewConfigurationBeanEvent(this);
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                WeakReference<IViewConfigurationBeanListener> soft = listeners.get(i);
                IViewConfigurationBeanListener temp = soft.get();
                if (temp != null) {
                    temp.configurationDateChanged(event);
                }
                temp = null;
                soft = null;
            }
        }
        event = null;
    }

    private void fireSelectedStructureEvent() {
        ViewConfigurationBeanEvent event = new ViewConfigurationBeanEvent(this);
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                WeakReference<IViewConfigurationBeanListener> soft = listeners.get(i);
                IViewConfigurationBeanListener temp = soft.get();
                if (temp != null) {
                    temp.configurationStructureChanged(event);
                }
                temp = null;
                soft = null;
            }
        }
        event = null;
    }

    private void fireVisibilityEvent() {
        ViewConfigurationBeanEvent event = new ViewConfigurationBeanEvent(this);
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                WeakReference<IViewConfigurationBeanListener> soft = listeners.get(i);
                IViewConfigurationBeanListener temp = soft.get();
                if (temp != null) {
                    temp.visibilityChanged(event);
                }
                temp = null;
                soft = null;
            }
        }
        event = null;
    }

    private void fireRefreshEvent(boolean refreshing) {
        ViewConfigurationBeanEvent event = new ViewConfigurationBeanEvent(this);
        synchronized (listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                WeakReference<IViewConfigurationBeanListener> soft = listeners.get(i);
                IViewConfigurationBeanListener temp = soft.get();
                if (temp != null) {
                    temp.refreshChanged(event, refreshing);
                }
                temp = null;
                soft = null;
            }
        }
        event = null;
    }

    public void updateHideShowPanel() {
        fireVisibilityEvent();
    }

    public void setShowTreePanelManagement() {
        if (attributesPanel != null) {
            String name;
            if (isShowingTreePanel) {
                isShowingTreePanel = false;
                name = Messages.getMessage("VIEW_ACTION_SHOW_TREE_VIEW");
                popupMenu.setNameTreeMenuItem(name);
                attributesPanel.hideTreePanel();
            }
            else {
                isShowingTreePanel = true;
                name = Messages.getMessage("VIEW_ACTION_HIDE_TREE_VIEW");
                popupMenu.setNameTreeMenuItem(name);
                attributesPanel.showTreePanel();
            }
            fireVisibilityEvent();
        }
    }

    public void setShowGeneralPanel() {
        if (generalPanel != null) {
            String name;
            if (generalPanel.isVisible()) {
                name = Messages.getMessage("VIEW_ACTION_SHOW_GENERAL_VIEW");
                popupMenu.setNameGeneralMenuItem(name);
                generalPanel.setVisible(false);
            }
            else {
                name = Messages.getMessage("VIEW_ACTION_HIDE_GENERAL_VIEW");
                popupMenu.setNameGeneralMenuItem(name);
                generalPanel.setVisible(true);
            }
            fireVisibilityEvent();
        }
    }

    public boolean isShowingTreePanel() {
        return isShowingTreePanel;
    }

    public boolean isShowingGeneralPanel() {
        return generalPanel.isVisible();
    }

    public void setEnableRefreshButton(boolean enabled) {
        popupMenu.setEnableRefreshItem(enabled);
        fireRefreshEvent(!enabled);
    }

    public void initBeforeRefresh() {
        fireRefreshEvent(true);
        getAttributesPanel().getViewAttributesGraphPanel().resetComponents();
        getAttributesPanel().getViewAttributesTreePanel().getViewActionPanel()
                .refreshButtonDefaultColor();
        getAttributesPanel().getViewAttributesTreePanel().getViewActionPanel().putCancelButton();
    }
}
