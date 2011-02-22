// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ViewPanel.java,v $
// Revision 1.4 2008/04/09 10:45:46 achouri
// remove viewActionPanel
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
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
package fr.soleil.mambo.containers.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewPanel extends JPanel {

    private static final long     serialVersionUID = -3834427618013055362L;
    private static ViewPanel      instance         = null;
    private GridBagConstraints    generalConstraints;
    private GridBagConstraints    selectionConstraints;
    private JPanel                emptyPanel;
    private ViewConfigurationBean displayedConfigurationBean;

    /**
     * @return 8 juil. 2005
     */
    public static ViewPanel getInstance() {
        if (instance == null) {
            instance = new ViewPanel();
        }

        return instance;
    }

    /**
     *
     */
    private ViewPanel() {
        super(new GridBagLayout());
        displayedConfigurationBean = null;
        initComponents();
        addComponents();
    }

    private void initComponents() {
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        emptyPanel = new JPanel();
        GUIUtilities.setObjectBackground(emptyPanel, GUIUtilities.VIEW_COLOR);
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        selectionConstraints = new GridBagConstraints();
        selectionConstraints.fill = GridBagConstraints.BOTH;
        selectionConstraints.gridx = 0;
        selectionConstraints.gridy = 0;
        selectionConstraints.weightx = 1;
        selectionConstraints.weighty = 0;
        add(ViewSelectionPanel.getInstance(), selectionConstraints);

        generalConstraints = new GridBagConstraints();
        generalConstraints.fill = GridBagConstraints.BOTH;
        generalConstraints.gridx = 0;
        generalConstraints.gridy = 1;
        generalConstraints.weightx = 1;
        generalConstraints.weighty = 1;
        add(emptyPanel, generalConstraints);

        String msg = Messages.getMessage("VIEW_BORDER");
        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
                GUIUtilities.getTitleFont());
        setBorder(border);
    }

    public void setDisplayedConfigurationBean(
            ViewConfigurationBean displayedConfigurationBean) {
        if (this.displayedConfigurationBean == null) {
            remove(emptyPanel);
        }
        else {
            remove(this.displayedConfigurationBean.getViewDisplayPanel());
        }
        this.displayedConfigurationBean = displayedConfigurationBean;
        if (displayedConfigurationBean == null) {
            add(emptyPanel, generalConstraints);
        }
        else {
            add(displayedConfigurationBean.getViewDisplayPanel(),
                    generalConstraints);
        }
        revalidate();
        repaint();
    }

}
