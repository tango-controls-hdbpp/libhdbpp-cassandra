package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.CloseSelectedVCAction;
import fr.soleil.mambo.actions.view.OpenVCEditDialogNewAction;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.MamboActivableButton;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewSelectionPanel extends JPanel implements
        PropertyChangeListener {

    private static final long         serialVersionUID = -1016464963457042923L;
    private static ViewSelectionPanel instance         = null;

    private JButton                   newButton;
    private JLabel                    forceExportLabel;
    private JButton                   closeButton;
    private OpenedVCComboBox          stack;
    private Font                      forceExportFont  = new Font("Arial",
                                                               Font.BOLD, 12);

    private final static ImageIcon    newIcon          = new ImageIcon(
                                                               Mambo.class
                                                                       .getResource("icons/New.gif"));

    private Component                 forceExportStrut;

    private GridBagConstraints        gbc              = new GridBagConstraints();

    /**
     * @return 8 juil. 2005
     */
    public static ViewSelectionPanel getInstance() {
        if (instance == null) {
            instance = new ViewSelectionPanel();
            GUIUtilities.setObjectBackground(instance, GUIUtilities.VIEW_COLOR);
        }

        return instance;
    }

    /**
     *
     */
    private ViewSelectionPanel() {
        super();

        initComponents();
        addComponents();

        enableSelection();
        // ViewAttributesGraphPanel.getInstance().addPropertyChangeListener(this);
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        JPanel topBox = new JPanel();
        topBox.setBackground(GUIUtilities.getViewColor());
        topBox.setLayout(new GridBagLayout());
        Insets topBoxInsets = new Insets(0, 0, 0, 5);
        setConstraints(0, 0, 1, 1, 0.0, 0.0, topBoxInsets,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        topBox.add(newButton, gbc);
        setConstraints(1, 0, 1, 1, 0.0, 0.0, topBoxInsets,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        topBox.add(stack, gbc);
        setConstraints(2, 0, 1, 1, 0.0, 0.0, topBoxInsets,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        topBox.add(closeButton, gbc);
        setConstraints(3, 0, 1, 1, 0.0, 0.0, topBoxInsets,
                GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        topBox.add(forceExportLabel, gbc);

        this.setLayout(new GridBagLayout());
        setConstraints(0, 0, 1, 1, 1.0, 1.0, new Insets(0, 5, 0, 0),
                GridBagConstraints.NONE, GridBagConstraints.WEST);
        this.add(topBox, gbc);

        // String msg = Messages.getMessage("VIEW_SELECTION_BORDER");
        // TitledBorder titledBorder = BorderFactory.createTitledBorder(
        // BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), msg,
        // TitledBorder.CENTER, TitledBorder.TOP);
        // CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
        // titledBorder, BorderFactory.createEmptyBorder(1, 5, 2, 5));
        // this.setBorder(compoundBorder);
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents() {
        String msg = "";

        msg = Messages.getMessage("VIEW_ACTION_NEW_BUTTON");
        OpenVCEditDialogNewAction newAction = OpenVCEditDialogNewAction
                .getInstance(msg);
        newButton = new MamboActivableButton(newAction, newIcon, newIcon);
        GUIUtilities.setObjectBackground(newButton, GUIUtilities.VIEW_COLOR);

        forceExportLabel = new JLabel();
        forceExportLabel.setFont(forceExportFont);
        forceExportStrut = Box.createVerticalStrut(5);
        updateForceExport();

        stack = OpenedVCComboBox.getInstance(new LimitedVCStack());

        closeButton = new JButton(CloseSelectedVCAction.getInstance(""));
        ImageIcon newActionIcon = new ImageIcon(Mambo.class
                .getResource("icons/remove.gif"));
        closeButton.setIcon(newActionIcon);
        closeButton.setPreferredSize(new Dimension(22, 20));
    }

    public void updateForceExport() {
        ViewConfiguration vc = ViewConfigurationBeanManager.getInstance()
                .getSelectedConfiguration();
        if (vc != null && vc.getData() != null && !vc.getData().isHistoric()) {
            String msg;
            if (Options.getInstance().getVcOptions().isDoForceTdbExport()) {
                msg = Messages.getMessage("VIEW_ACTION_IS_EXPORT");
                forceExportLabel.setForeground(new Color(150, 0, 0));
            } else {
                msg = Messages.getMessage("VIEW_ACTION_IS_NOT_EXPORT");
                forceExportLabel.setForeground(new Color(0, 0, 150));
            }
            forceExportLabel.setText(msg);
            msg = null;
            showLabel(true);
        } else {
            showLabel(false);
        }
        revalidate();
    }

    private void showLabel(boolean visible) {
        forceExportStrut.setVisible(visible);
        forceExportLabel.setVisible(visible);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof ViewAttributesGraphPanel) {
            String property = evt.getPropertyName();
            if (ViewAttributesGraphPanel.CANCELING.equals(property)
                    || ViewAttributesGraphPanel.CANCELED.equals(property)
                    || ViewAttributesGraphPanel.PANELS_LOADED.equals(property)) {
                enableSelection();
            } else if (/*
                        * ViewAttributesGraphPanel.SETTING_PANELS.equals(property
                        * ) ||
                        * ViewAttributesGraphPanel.PANELS_SET.equals(property)
                        * ||
                        */ViewAttributesGraphPanel.ADDING_PANELS
                    .equals(property)
                    || ViewAttributesGraphPanel.PANELS_ADDED.equals(property)
                    || ViewAttributesGraphPanel.LOADING_PANELS.equals(property)) {
                disableSelection();
            }
        }
    }

    public void enableSelection() {
        synchronized (this) {
            OpenVCEditDialogNewAction.getInstance().setEnabled(true);
            CloseSelectedVCAction.getInstance().setEnabled(true);
            OpenedVCComboBox.getInstance().setEnabled(true);
            repaint();
        }
    }

    public void disableSelection() {
        synchronized (this) {
            OpenVCEditDialogNewAction.getInstance().setEnabled(false);
            CloseSelectedVCAction.getInstance().setEnabled(false);
            OpenedVCComboBox.getInstance().setEnabled(false);
            repaint();
        }
    }

    private void setConstraints(int gridX, int gridY, int gridWidth,
            int gridHeight, double weightX, double weightY, Insets insets,
            int fill, int anchor) {
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.gridwidth = gridWidth;
        gbc.gridheight = gridHeight;
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.insets = insets;
        gbc.fill = fill;
        gbc.anchor = anchor;
    }

}
