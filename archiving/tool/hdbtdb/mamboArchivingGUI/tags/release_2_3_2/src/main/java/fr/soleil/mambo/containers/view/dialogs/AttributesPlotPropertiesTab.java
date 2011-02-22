// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AttributesPlotPropertiesTab.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AttributesPlotPropertiesTab.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: AttributesPlotPropertiesTab.java,v $
// Revision 1.5 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.4 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.3 2006/07/18 10:25:47 ounsy
// Less time consuming by setting tree expanding on demand only
//
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
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCAttributesPropertiesTreeExpandAllAction;
import fr.soleil.mambo.actions.view.VCAttributesPropertiesTreeExpandOneAction;
import fr.soleil.mambo.actions.view.VCAttributesPropertiesTreeExpandSelectedAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class AttributesPlotPropertiesTab extends JPanel {

    private static final long serialVersionUID = 5746310967824211793L;
    private final static ImageIcon expandAllIcon = new ImageIcon(Mambo.class
            .getResource("icons/expand_all.gif"));
    private final static ImageIcon expandSelectedIcon = new ImageIcon(Mambo.class
            .getResource("icons/expand_selected.gif"));
    private final static ImageIcon expandFirstIcon = new ImageIcon(Mambo.class
            .getResource("icons/expand.gif"));
    private final static ImageIcon warningIcon = new ImageIcon(Mambo.class
            .getResource("icons/warningBig.gif"));

    private VCAttributesPropertiesTree vcAttributesPropertiesTree;
    private JScrollPane scrollPane;
    private JButton expandAllButton, expandSelectedButton, expandFirstButton;
    private JLabel warningLabel;
    private JPanel leftPanel, actionPanel;

    private AttributesPlotPropertiesPanel propertiesPanel;
    private ViewConfigurationBean viewConfigurationBean;

    public AttributesPlotPropertiesTab(ViewConfigurationBean viewConfigurationBean,
            VCEditDialog editDialog) {
        super(new GridBagLayout());
        this.viewConfigurationBean = viewConfigurationBean;
        this.initComponents(editDialog);
        this.addComponents();
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        GridBagConstraints warningLabelConstraints = new GridBagConstraints();
        warningLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        warningLabelConstraints.gridx = 0;
        warningLabelConstraints.gridy = 0;
        warningLabelConstraints.weightx = 1;
        warningLabelConstraints.weighty = 0;
        warningLabelConstraints.insets = new Insets(5, 5, 0, 5);
        actionPanel.add(warningLabel, warningLabelConstraints);
        GridBagConstraints expandAllButtonConstraints = new GridBagConstraints();
        expandAllButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        expandAllButtonConstraints.gridx = 0;
        expandAllButtonConstraints.gridy = 1;
        expandAllButtonConstraints.weightx = 1;
        expandAllButtonConstraints.weighty = 0;
        expandAllButtonConstraints.insets = new Insets(5, 5, 0, 5);
        actionPanel.add(expandAllButton, expandAllButtonConstraints);
        GridBagConstraints expandSelectedButtonConstraints = new GridBagConstraints();
        expandSelectedButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        expandSelectedButtonConstraints.gridx = 0;
        expandSelectedButtonConstraints.gridy = 2;
        expandSelectedButtonConstraints.weightx = 1;
        expandSelectedButtonConstraints.weighty = 0;
        expandSelectedButtonConstraints.insets = new Insets(5, 5, 0, 5);
        actionPanel.add(expandSelectedButton, expandSelectedButtonConstraints);
        GridBagConstraints expandFirstButtonConstraints = new GridBagConstraints();
        expandFirstButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        expandFirstButtonConstraints.gridx = 0;
        expandFirstButtonConstraints.gridy = 3;
        expandFirstButtonConstraints.weightx = 1;
        expandFirstButtonConstraints.weighty = 0;
        expandFirstButtonConstraints.insets = new Insets(5, 5, 0, 5);
        actionPanel.add(expandFirstButton, expandFirstButtonConstraints);

        GridBagConstraints actionPanelConstraints = new GridBagConstraints();
        actionPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        actionPanelConstraints.gridx = 0;
        actionPanelConstraints.gridy = 0;
        actionPanelConstraints.weightx = 1;
        actionPanelConstraints.weighty = 0;
        actionPanelConstraints.insets = new Insets(5, 5, 0, 5);
        leftPanel.add(actionPanel, actionPanelConstraints);
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 1;
        scrollPaneConstraints.weightx = 1;
        scrollPaneConstraints.weighty = 1;
        scrollPaneConstraints.insets = new Insets(5, 5, 5, 5);
        leftPanel.add(scrollPane, scrollPaneConstraints);

        GridBagConstraints leftPanelConstraints = new GridBagConstraints();
        leftPanelConstraints.fill = GridBagConstraints.BOTH;
        leftPanelConstraints.gridx = 0;
        leftPanelConstraints.gridy = 0;
        leftPanelConstraints.weightx = 1;
        leftPanelConstraints.weighty = 1;
        this.add(leftPanel, leftPanelConstraints);
        GridBagConstraints propertiesPanelConstraints = new GridBagConstraints();
        propertiesPanelConstraints.fill = GridBagConstraints.VERTICAL;
        propertiesPanelConstraints.gridx = 1;
        propertiesPanelConstraints.gridy = 0;
        propertiesPanelConstraints.weightx = 0;
        propertiesPanelConstraints.weighty = 1;
        this.add(propertiesPanel, propertiesPanelConstraints);
    }

    private void initComponents(VCEditDialog editDialog) {
        vcAttributesPropertiesTree = new VCAttributesPropertiesTree(viewConfigurationBean
                .getEditingModel(), viewConfigurationBean, editDialog);
        expandAllButton = new JButton(new VCAttributesPropertiesTreeExpandAllAction(
                vcAttributesPropertiesTree));
        expandAllButton.setIcon(expandAllIcon);
        expandFirstButton = new JButton(new VCAttributesPropertiesTreeExpandOneAction(
                vcAttributesPropertiesTree));
        expandFirstButton.setIcon(expandFirstIcon);
        expandSelectedButton = new JButton(new VCAttributesPropertiesTreeExpandSelectedAction(
                vcAttributesPropertiesTree));
        expandSelectedButton.setIcon(expandSelectedIcon);
        warningLabel = new JLabel(Messages.getMessage("VIEW_ACTION_EXPAND_WARNING"), JLabel.CENTER);
        warningLabel.setIcon(warningIcon);
        scrollPane = new JScrollPane(vcAttributesPropertiesTree);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(new Dimension(100, 50));
        scrollPane.setPreferredSize(new Dimension(200, 50));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 700));
        leftPanel = new JPanel(new GridBagLayout());
        actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setBorder(GUIUtilities.getPlotSubPanelsLineBorder(Messages
                .getMessage("VIEW_ACTION_BORDER"), Color.BLACK, Color.BLACK));
        propertiesPanel = new AttributesPlotPropertiesPanel(viewConfigurationBean, editDialog);
    }

    public AttributesPlotPropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public VCAttributesPropertiesTree getVcAttributesPropertiesTree() {
        return vcAttributesPropertiesTree;
    }

}
