// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/archiving/OpenedACComboBox.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenedACComboBox.
// (Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: OpenedACComboBox.java,v $
// Revision 1.5 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.4 2006/04/10 09:17:37 ounsy
// removed useless logs
//
// Revision 1.3 2006/04/05 13:44:24 ounsy
// minor changes
//
// Revision 1.2 2005/12/15 11:16:16 ounsy
// Allowing last opened AC/VC deletion
//
// Revision 1.1 2005/11/29 18:27:24 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.archiving;

import fr.soleil.mambo.actions.archiving.listeners.OpenedACItemListener;
import fr.soleil.mambo.components.OpenedConfComboBox;
import fr.soleil.mambo.components.renderers.OpenedACListCellRenderer;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public class OpenedACComboBox extends
        OpenedConfComboBox<ArchivingConfiguration> {

    private static final long       serialVersionUID = 4339464372602933991L;
    // MULTI-CONF
    private static OpenedACComboBox instance         = null;

    /**
     * @return 8 juil. 2005
     */
    public static OpenedACComboBox getInstance(LimitedACStack _stack) {
        if (instance == null) {
            instance = new OpenedACComboBox(_stack);
        }

        return instance;
    }

    public static OpenedACComboBox getInstance() {
        return instance;
    }

    private OpenedACComboBox(LimitedACStack _stack) {
        super(_stack);
        this.addItemListener(new OpenedACItemListener());
        this.setRenderer(new OpenedACListCellRenderer());
    }

    public void setMaxSize(int _size) {
        this.stack.setMaxSize(_size);
    }

    /**
     * 
     */
    public void empty() {
        this.stack.removeAllElements();
        this.setElements(new LimitedACStack());
    }

    /**
     * 
     */
    public static void refresh() {
        if (instance != null) {
            instance.repaint();
        }
    }

    public void setElements(LimitedACStack _stack) {
        super.setElements(_stack);
    }

    public LimitedACStack getACElements() {
        return (LimitedACStack) this.stack;
    }

    /**
     * @param currentDomainName
     */
    public void selectElement(ArchivingConfiguration vc) {
        super.setSelectedItem(vc);
    }

    /**
     * 
     */
    public static void removeSelectedElement() {
        if (instance == null) {
            return;
        }

        ArchivingConfiguration selectedAC = ArchivingConfiguration
                .getSelectedArchivingConfiguration();
        instance.removeElement(selectedAC);
        instance.repaint();
    }

    /**
     * @param selectedAC
     */
    public void removeElement(ArchivingConfiguration selectedAC) {

        if (this.stack.size() < 1) {
            return;
        }

        // boolean ret =
        this.stack.removeElement(selectedAC);
        super.removeItem(selectedAC);
        if (!this.stack.isEmpty()) {
            ArchivingConfiguration nextSelectedAC = this.stack.firstElement();
            this.selectElement(nextSelectedAC);
        }
        else {
            ArchivingConfiguration newAC = new ArchivingConfiguration();
            ArchivingConfiguration.setCurrentArchivingConfiguration(newAC);
            ArchivingConfiguration.setSelectedArchivingConfiguration(newAC);

            newAC.push();
            this.empty();
        }

        this.stack.removeElement(selectedAC);
        super.removeItem(selectedAC);

    }

    public ArchivingConfiguration getSelectedAC() {
        return ((LimitedACStack) this.stack).getSelectedAC();
    }
}
