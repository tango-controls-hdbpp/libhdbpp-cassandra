// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/SaveToDiskAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SaveToDiskAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: SaveToDiskAction.java,v $
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.tools.Messages;

public class SaveToDiskAction extends AbstractAction {

    private static final long serialVersionUID = 6816882795996725775L;

    private int               type             = NO_PRESET_TYPE;

    public static final int   NO_PRESET_TYPE   = -1;
    public static final int   AC_TYPE          = 0;
    public static final int   VC_TYPE          = 1;

    /**
     * @param name
     */
    public SaveToDiskAction(String name, Icon icon, int _type) {
        super(name, icon);
        this.putValue(Action.NAME, name);
        this.type = _type;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        int saveType = -1;

        if (this.type == NO_PRESET_TYPE) {
            String saveLabel = Messages.getMessage("MENU_SAVE");
            String chooseLabel = Messages.getMessage("ACTION_SAVE_CHOOSE");
            String currentACLabel = Messages
                    .getMessage("ACTION_SAVE_CURRENT_AC");
            String currentVCLabel = Messages
                    .getMessage("ACTION_SAVE_CURRENT_VC");

            String[] possibleValues = { currentACLabel, currentVCLabel };
            String selectedValue = (String) JOptionPane.showInputDialog(null,
                    chooseLabel, saveLabel, JOptionPane.INFORMATION_MESSAGE,
                    null, possibleValues, possibleValues[0]);

            if (selectedValue == null) {
                return;
            }

            if (selectedValue.equals(possibleValues[0])) {
                saveType = AC_TYPE;
            }
            else if (selectedValue.equals(possibleValues[1])) {
                saveType = VC_TYPE;
            }
            else {
                throw new IllegalStateException();
            }
        }
        else {
            saveType = this.type;
        }

        // ---------------
        switch (saveType) {
            case AC_TYPE:
                saveAc();
                break;

            case VC_TYPE:
                saveVc();
                break;

            default:

                throw new IllegalStateException();
        }

    }

    /**
     * 8 sept. 2005
     */
    private void saveVc() {
        ViewConfigurationBeanManager.getInstance().saveVC(
                ViewConfigurationBeanManager.getInstance()
                        .getSelectedConfiguration(), false);
    }

    /**
     * 8 sept. 2005
     */
    private void saveAc() {
        IArchivingConfigurationManager manager = ArchivingConfigurationManagerFactory
                .getCurrentImpl();
        ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
                .getSelectedArchivingConfiguration();
        if (selectedArchivingConfiguration == null) {
            return;
        }

        selectedArchivingConfiguration.save(manager, false);
    }
}
