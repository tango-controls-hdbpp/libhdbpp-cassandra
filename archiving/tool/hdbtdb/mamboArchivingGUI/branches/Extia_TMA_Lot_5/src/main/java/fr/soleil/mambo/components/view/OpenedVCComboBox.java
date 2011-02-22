// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/OpenedVCComboBox.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenedVCComboBox.
// (Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: OpenedVCComboBox.java,v $
// Revision 1.4 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.3 2006/04/05 13:45:17 ounsy
// minor changes
//
// Revision 1.2 2005/12/15 11:18:30 ounsy
// Allowing last opened AC/VC deletion
//
// Revision 1.1 2005/11/29 18:28:12 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.view;

import fr.soleil.mambo.actions.view.listeners.OpenedVCItemListener;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.OpenedConfComboBox;
import fr.soleil.mambo.components.renderers.OpenedVCListCellRenderer;
import fr.soleil.mambo.containers.archiving.ArchivingActionPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;

public class OpenedVCComboBox extends OpenedConfComboBox<ViewConfiguration> {

    private static final long       serialVersionUID = 9034737213535638732L;
    // MULTI-CONF
    private static OpenedVCComboBox instance         = null;
    private OpenedVCItemListener    openedVCComboBoxItemListener;

    /**
     * @return 8 juil. 2005
     */
    public static OpenedVCComboBox getInstance(LimitedVCStack _stack) {
        if (instance == null) {
            instance = new OpenedVCComboBox(_stack);
        }

        return instance;
    }

    public static OpenedVCComboBox getInstance() {
        return instance;
    }

    private OpenedVCComboBox(LimitedVCStack _stack) {
        super(_stack);
        openedVCComboBoxItemListener = new OpenedVCItemListener();
        this.addItemListener(openedVCComboBoxItemListener);
        this.setRenderer(new OpenedVCListCellRenderer());
    }

    /**
     * 
     */
    public void empty() {
        this.stack.removeAllElements();
        this.setElements(new LimitedVCStack());
    }

    /**
     * 
     */
    public static void refresh() {
        if (instance != null) {
            instance.repaint();
        }
    }

    public void setElements(LimitedVCStack _stack) {
        super.setElements(_stack);
        ViewConfigurationBeanManager.getInstance().clean();
    }

    public LimitedVCStack getVCElements() {
        return (LimitedVCStack) this.stack;
    }

    /**
     * @param currentDomainName
     */
    public void selectElement(ViewConfiguration vc) {
        ViewConfiguration previous = null;
        if (vc != null) {
            previous = getSelectedVC();
            setSelectedItem(vc);
        }
        updateUIWithSelected(previous);
    }

    /**
     * 
     */
    public static void removeSelectedElement() {
        if (instance != null) {
            ViewConfiguration selectedVC = ViewConfigurationBeanManager
                    .getInstance().getSelectedConfiguration();
            instance.removeElement(selectedVC);
            instance.repaint();
        }
    }

    /**
     * @param selectedVC
     */
    public void removeElement(ViewConfiguration selectedVC) {
        if ((selectedVC != null) && (this.stack.size() > 0)) {
            this.stack.removeElement(selectedVC);
            super.removeItem(selectedVC);
            ViewConfigurationBeanManager.getInstance().removeConfiguration(
                    selectedVC);
            updateUIWithSelected(selectedVC);
            // RG : Even if following code looks useless, it is necessary.
            // Otherwise, the combobox is not fully cleaned when removing last
            // element
            if (getItemCount() == 0) {
                removeAllItems();
            }
        }
    }

    public ViewConfiguration getSelectedVC() {
        return ((LimitedVCStack) stack).getSelectedVC();
    }

    public OpenedVCItemListener getOpenedVCComboBoxItemListener() {
        return openedVCComboBoxItemListener;
    }

    public void updateUIWithSelected() {
        updateUIWithSelected(null);
    }

    private void updateUIWithSelected(ViewConfiguration previousSelection) {
        if (previousSelection != null) {
            ViewConfigurationBean bean = ViewConfigurationBeanManager
                    .getInstance().getBeanFor(previousSelection);
            if (bean != null) {
                bean.getAttributesPanel().getViewAttributesGraphPanel()
                        .removePropertyChangeListener(
                                ArchivingActionPanel.getInstance());
            }
        }
        ViewConfigurationBeanManager.getInstance().setSelectedConfiguration(
                getSelectedVC());
        ViewConfigurationBean bean = ViewConfigurationBeanManager.getInstance()
                .getBeanFor(getSelectedVC());
        if (bean != null) {
            bean.getAttributesPanel().getViewAttributesGraphPanel()
                    .addPropertyChangeListener(
                            ArchivingActionPanel.getInstance());
        }
        repaint();
    }

}
