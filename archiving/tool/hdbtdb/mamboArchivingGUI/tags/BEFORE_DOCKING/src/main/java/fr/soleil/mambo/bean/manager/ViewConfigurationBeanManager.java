package fr.soleil.mambo.bean.manager;

import java.util.HashMap;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.view.ViewPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;

public class ViewConfigurationBeanManager {

    private static ViewConfigurationBeanManager               instance = null;

    private HashMap<ViewConfiguration, ViewConfigurationBean> beanMap;

    private ViewConfiguration                                 selectedConfiguration;

    public static ViewConfigurationBeanManager getInstance() {
        if (instance == null) {
            instance = new ViewConfigurationBeanManager();
        }
        return instance;
    }

    private ViewConfigurationBeanManager() {
        super();
        beanMap = new HashMap<ViewConfiguration, ViewConfigurationBean>();
        selectedConfiguration = null;
    }

    public ViewConfiguration getSelectedConfiguration() {
        return selectedConfiguration;
    }

    public void setSelectedConfiguration(ViewConfiguration selectedConfiguration) {
        if (selectedConfiguration != this.selectedConfiguration) {
            this.selectedConfiguration = selectedConfiguration;
            ViewConfigurationBean bean = null;
            if (selectedConfiguration != null) {
                bean = beanMap.get(selectedConfiguration);
                if (bean == null) {
                    bean = new ViewConfigurationBean(selectedConfiguration);
                    beanMap.put(selectedConfiguration, bean);
                }
                bean.prepareGraphPanel();
                bean.refreshMainUI();
            }
            ViewPanel.getInstance().setDisplayedConfigurationBean(bean);
        }
    }

    public void newConfiguration() {
        ViewConfigurationBean bean = new ViewConfigurationBean(null);
        ViewPanel.getInstance().setDisplayedConfigurationBean(bean);
        bean.editViewConfiguration();
        if (bean.getViewConfiguration() == null) {
            ViewConfigurationBean previous = null;
            if (selectedConfiguration != null) {
                previous = beanMap.get(selectedConfiguration);
            }
            ViewPanel.getInstance().setDisplayedConfigurationBean(previous);
        }
        else {
            beanMap.put(bean.getViewConfiguration(), bean);
            setSelectedConfiguration(bean.getViewConfiguration());
        }
    }

    public void removeConfiguration(ViewConfiguration viewConfiguration) {
        if (viewConfiguration != null) {
            beanMap.remove(viewConfiguration);
            if (viewConfiguration == selectedConfiguration) {
                setSelectedConfiguration(null);
            }
        }
    }

    public void clean() {
        beanMap.clear();
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
    }

}
