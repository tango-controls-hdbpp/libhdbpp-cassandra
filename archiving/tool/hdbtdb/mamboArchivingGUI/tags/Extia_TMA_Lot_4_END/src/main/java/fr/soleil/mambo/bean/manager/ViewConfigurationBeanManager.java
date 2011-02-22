package fr.soleil.mambo.bean.manager;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import net.infonode.docking.RootWindow;
import net.infonode.gui.mouse.MouseButtonListener;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.listeners.IViewConfigurationBeanListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.containers.view.ViewSelectionPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;
import fr.soleil.mambo.event.ViewConfigurationBeanEvent;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.util.docking.ADockingManager;
import fr.soleil.util.docking.DefaultViewFactory;
import fr.soleil.util.docking.PerspectiveFactory;
import fr.soleil.util.docking.infonode.InfoNodeDockingManager;
import fr.soleil.util.docking.infonode.InfoNodeView;

public class ViewConfigurationBeanManager implements
        IViewConfigurationBeanListener {

    private static ViewConfigurationBeanManager               instance                        = null;
    public final static int                                   DEFAULT_MAXIMUM_DISPLAYED_VIEWS = 2;

    private HashMap<ViewConfiguration, ViewConfigurationBean> beanMap;
    private ViewConfiguration                                 selectedConfiguration;
    private ADockingManager                                   dockingManager;
    private LinkedList<ViewConfiguration>                     displayedConfigurations;
    private int                                               maximumDisplayedViews;
    private DefaultViewFactory                                viewFactory;
    private final static ImageIcon                            MODIFIED_VC_ICON                = new ImageIcon(
                                                                                                      Mambo.class
                                                                                                              .getResource("icons/star_red.gif"));

    public static ViewConfigurationBeanManager getInstance() {
        if (instance == null) {
            instance = new ViewConfigurationBeanManager();
        }
        return instance;
    }

    private ViewConfigurationBeanManager() {
        super();
        maximumDisplayedViews = Options.getInstance().getVcOptions()
                .getMaxDisplayedViews();
        viewFactory = new DefaultViewFactory();
        dockingManager = new InfoNodeDockingManager(viewFactory,
                new PerspectiveFactory());
        GUIUtilities.setObjectBackground(dockingManager.getDockingArea(),
                GUIUtilities.VIEW_COLOR);
        // We force preferred size in order not to have a too big area.
        dockingManager.getDockingArea().setPreferredSize(
                new Dimension(300, 300));
        // XXX not a clean code. Find a better way to do this
        ((RootWindow) dockingManager.getDockingArea()).getRootWindow()
                .getRootWindowProperties().getWindowAreaProperties()
                .setBackgroundColor(GUIUtilities.getViewColor());
        ((RootWindow) dockingManager.getDockingArea()).getRootWindow()
                .getRootWindowProperties().getTabWindowProperties()
                .getMaximizeButtonProperties().setVisible(false);
        ((RootWindow) dockingManager.getDockingArea()).getRootWindow()
                .getRootWindowProperties().getTabWindowProperties()
                .getMinimizeButtonProperties().setVisible(false);
        ((RootWindow) dockingManager.getDockingArea()).getRootWindow()
                .getRootWindowProperties().getTabWindowProperties()
                .getCloseButtonProperties().setVisible(false);
        beanMap = new HashMap<ViewConfiguration, ViewConfigurationBean>();
        displayedConfigurations = new LinkedList<ViewConfiguration>();
        selectedConfiguration = null;
        maximumDisplayedViews = DEFAULT_MAXIMUM_DISPLAYED_VIEWS;
    }

    public ViewConfiguration getSelectedConfiguration() {
        return selectedConfiguration;
    }

    public void setSelectedConfiguration(ViewConfiguration selectedConfiguration) {
        if (selectedConfiguration != this.selectedConfiguration) {
            InfoNodeView previousView = null;
            if (this.selectedConfiguration != null) {
                previousView = (InfoNodeView) viewFactory
                        .getView(this.selectedConfiguration);
            }
            this.selectedConfiguration = selectedConfiguration;
            ViewConfigurationBean bean = null;
            if (selectedConfiguration != null) {
                synchronized (displayedConfigurations) {
                    displayedConfigurations.remove(selectedConfiguration);
                    displayedConfigurations.addFirst(selectedConfiguration);
                    reduceViewCountIfNecessary();
                }
                bean = beanMap.get(selectedConfiguration);
                if (bean == null) {
                    bean = new ViewConfigurationBean(selectedConfiguration);
                    bean.addViewConfigurationBeanListener(this);
                    beanMap.put(selectedConfiguration, bean);
                }
                // bean.prepareGraphPanel();
                bean.refreshMainUI();
                final ViewConfigurationBean copyBean = bean;
                InfoNodeView view = (InfoNodeView) viewFactory
                        .getView(selectedConfiguration);
                if (view == null) {
                    JScrollPane scrollPane = new JScrollPane(bean
                            .getViewDisplayPanel());
                    GUIUtilities.setObjectBackground(scrollPane,
                            GUIUtilities.VIEW_COLOR);
                    GUIUtilities.setObjectBackground(scrollPane.getViewport(),
                            GUIUtilities.VIEW_COLOR);
                    GUIUtilities.setObjectBackground(scrollPane
                            .getHorizontalScrollBar(), GUIUtilities.VIEW_COLOR);
                    GUIUtilities.setObjectBackground(scrollPane
                            .getVerticalScrollBar(), GUIUtilities.VIEW_COLOR);
                    ImageIcon icon = null;
                    if (selectedConfiguration.isModified()) {
                        icon = MODIFIED_VC_ICON;
                    }
                    view = new InfoNodeView(selectedConfiguration
                            .getDisplayableTitle(), icon, scrollPane,
                            selectedConfiguration);
                    GUIUtilities.setObjectBackground(view,
                            GUIUtilities.VIEW_COLOR);
                    scrollPane = null;
                    // XXX not a clean code. Find a better way to do this
                    view.addTabMouseButtonListener(new MouseButtonListener() {

                        @Override
                        public void mouseButtonEvent(MouseEvent event) {
                            OpenedVCComboBox.getInstance().selectElement(
                                    copyBean.getViewConfiguration());
                        }

                    });
                    viewFactory.addView(view);
                }
                view.getWindowProperties().getTabProperties()
                        .getTitledTabProperties().getNormalProperties()
                        .getComponentProperties().setBackgroundColor(
                                GUIUtilities.getViewSelectionColor());
                view.getWindowProperties().getTabProperties()
                        .getTitledTabProperties().getHighlightedProperties()
                        .getComponentProperties().setBackgroundColor(
                                GUIUtilities.getViewSelectionColor());
                view.getViewProperties().setTitle(
                        selectedConfiguration.getDisplayableTitle());
                view.restore();
                view.makeVisible();
                ViewSelectionPanel.getInstance().updateForceExport();
                if (previousView != null) {
                    previousView.getWindowProperties().getTabProperties()
                            .getTitledTabProperties().getNormalProperties()
                            .getComponentProperties().setBackgroundColor(
                                    GUIUtilities.getViewColor());
                    previousView.getWindowProperties().getTabProperties()
                            .getTitledTabProperties()
                            .getHighlightedProperties()
                            .getComponentProperties().setBackgroundColor(
                                    GUIUtilities.getViewColor());
                }
                bean.getViewDisplayPanel().grabFocus();
            }
        }
    }

    public void newConfiguration() {
        ViewConfigurationBean bean = new ViewConfigurationBean(null);
        bean.editViewConfiguration();
        if (bean.getViewConfiguration() != null) {
            bean.addViewConfigurationBeanListener(this);
            beanMap.put(bean.getViewConfiguration(), bean);
            setSelectedConfiguration(bean.getViewConfiguration());
        }
    }

    public void removeConfiguration(ViewConfiguration viewConfiguration) {
        if (viewConfiguration != null) {
            ViewConfigurationBean bean = beanMap.get(viewConfiguration);
            if (bean != null) {
                bean.removeViewConfigurationBeanListener(this);
            }
            bean = null;
            beanMap.remove(viewConfiguration);
            displayedConfigurations.remove(viewConfiguration);
            viewFactory.removeId(viewConfiguration);
        }
    }

    public void clean() {
        beanMap.clear();
        displayedConfigurations.clear();
        setSelectedConfiguration(null);
    }

    public ViewConfigurationBean getBeanFor(ViewConfiguration configuration) {
        if (configuration == null) {
            return null;
        }
        else {
            return beanMap.get(configuration);
        }
    }

    public void saveVC(ViewConfiguration viewConfiguration, boolean isSaveAs) {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory
                .getCurrentImpl();
        if (viewConfiguration != null) {
            viewConfiguration.save(manager, isSaveAs);
            ViewConfigurationBean bean = beanMap.get(viewConfiguration);
            if (bean != null) {
                bean.getGeneralPanel().setPath(viewConfiguration.getPath());
            }
        }
        refreshIcon(viewConfiguration);
    }

    public ADockingManager getDockingManager() {
        return dockingManager;
    }

    public int getMaximumDisplayedViews() {
        return maximumDisplayedViews;
    }

    public void setMaximumDisplayedViews(int maximumDisplayedViews) {
        this.maximumDisplayedViews = maximumDisplayedViews;
        reduceViewCountIfNecessary();
    }

    public void refreshTitle(ViewConfiguration conf) {
        if (conf != null) {
            InfoNodeView view = (InfoNodeView) viewFactory.getView(conf);
            if (view != null) {
                view.getViewProperties().setTitle(conf.getDisplayableTitle());
            }
        }
    }

    public void refreshIcon(ViewConfiguration conf) {
        if (conf != null) {
            ImageIcon icon = null;
            if (conf.isModified()) {
                icon = MODIFIED_VC_ICON;
            }
            InfoNodeView view = (InfoNodeView) viewFactory.getView(conf);
            if (view != null) {
                view.getViewProperties().setIcon(icon);
            }
        }
    }

    private void reduceViewCountIfNecessary() {
        synchronized (displayedConfigurations) {
            while (displayedConfigurations.size() > maximumDisplayedViews) {
                viewFactory.removeId(displayedConfigurations.getLast());
                displayedConfigurations.removeLast();
            }
        }
    }

    @Override
    public void configurationDateChanged(ViewConfigurationBeanEvent event) {
        if ((event != null) && (event.getSource() != null)) {
            refreshIcon(event.getSource().getViewConfiguration());
        }
        ViewSelectionPanel.getInstance().repaint();
    }

    @Override
    public void configurationStructureChanged(ViewConfigurationBeanEvent event) {
        if ((event != null) && (event.getSource() != null)) {
            ViewSelectionPanel.getInstance().updateForceExport();
            refreshTitle(event.getSource().getViewConfiguration());
            refreshIcon(event.getSource().getViewConfiguration());
        }
    }

}
