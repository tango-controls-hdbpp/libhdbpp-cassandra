// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewActionPanel.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewActionPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ViewActionPanel.java,v $
// Revision 1.10 2008/04/09 18:00:00 achouri
// change place of new button
//
// Revision 1.9 2008/04/09 10:45:46 achouri
// change View button to Refresh button
//
// Revision 1.8 2007/04/06 14:27:06 ounsy
// *** empty log message ***
//
// Revision 1.7 2007/01/09 16:25:48 ounsy
// look & feel with "expand all" buttons in main frame
//
// Revision 1.6 2006/11/06 09:28:05 ounsy
// icons reorganization
//
// Revision 1.5 2006/08/09 16:12:54 ounsy
// No more automatic tree expanding : user has to click on a button to fully
// expand a tree
//
// Revision 1.4 2006/07/28 10:07:12 ounsy
// icons moved to "icons" package
//
// Revision 1.3 2006/04/05 13:46:41 ounsy
// new types full support
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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.CancelGraphPanelAction;
import fr.soleil.mambo.actions.view.OpenVCEditDialogModifyAction;
import fr.soleil.mambo.actions.view.VCRefreshAction;
import fr.soleil.mambo.actions.view.VCVariationAction;
import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewActionPanel extends JPanel implements PropertyChangeListener {

    private static ViewActionPanel instance      = null;

    private JButton                refreshButton;
    private JButton                cancelButton;
    private JButton                variationButton;
    private JButton                modifyButton;
    private Border                 refreshButtonDefaultBorder;

    private DateRangeBox           dateRangeBox;

    private VCRefreshAction        refreshAction;

    private final static ImageIcon viewIcon      = new ImageIcon(
                                                         Mambo.class
                                                                 .getResource("icons/View.gif"));
    private final static ImageIcon cancelIcon    = new ImageIcon(
                                                         Mambo.class
                                                                 .getResource("icons/cancel.gif"));
    private final static ImageIcon variationIcon = new ImageIcon(
                                                         Mambo.class
                                                                 .getResource("icons/Variation.gif"));
    private final static ImageIcon modifyIcon    = new ImageIcon(
                                                         Mambo.class
                                                                 .getResource("icons/Modify.gif"));

    private final static Insets    noMargin      = new Insets(0, 0, 0, 0);

    private GridBagConstraints     refreshConstraints;

    /**
     * @return 8 juil. 2005
     */
    public static ViewActionPanel getInstance() {
        if (instance == null) {
            instance = new ViewActionPanel();
            GUIUtilities.setObjectBackground(instance, GUIUtilities.VIEW_COLOR);
        }

        return instance;
    }

    /**
     *
     */
    private ViewActionPanel() {
        super(new GridBagLayout());

        initComponents();
        addComponents();
        initBorder();

        ViewAttributesGraphPanel.getInstance().addPropertyChangeListener(this);
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        Insets defaultInsets = new Insets(5, 5, 5, 5);

        refreshConstraints = new GridBagConstraints();
        refreshConstraints.fill = GridBagConstraints.BOTH;
        refreshConstraints.gridx = 0;
        refreshConstraints.gridy = 0;
        refreshConstraints.weightx = 0.34;
        refreshConstraints.weighty = 1;
        refreshConstraints.insets = defaultInsets;

        GridBagConstraints variationConstraints = new GridBagConstraints();
        variationConstraints.fill = GridBagConstraints.BOTH;
        variationConstraints.gridx = 1;
        variationConstraints.gridy = 0;
        variationConstraints.weightx = 0.33;
        variationConstraints.weighty = 1;
        variationConstraints.insets = defaultInsets;

        GridBagConstraints modifyConstraints = new GridBagConstraints();
        modifyConstraints.fill = GridBagConstraints.BOTH;
        modifyConstraints.gridx = 2;
        modifyConstraints.gridy = 0;
        modifyConstraints.weightx = 0.33;
        modifyConstraints.weighty = 1;
        modifyConstraints.insets = defaultInsets;

        this.add(refreshButton, refreshConstraints);
        this.add(variationButton, variationConstraints);
        this.add(modifyButton, modifyConstraints);
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents() {

        String msg = Messages.getMessage("VIEW_ACTION_REFRESH_BUTTON");

        refreshAction = VCRefreshAction.getInstance(msg);

        refreshButton = new JButton(refreshAction);
        refreshButton.setIcon(viewIcon);
        refreshButton.setMargin(noMargin);
        GUIUtilities
                .setObjectBackground(refreshButton, GUIUtilities.VIEW_COLOR);
        refreshButtonDefaultBorder = refreshButton.getBorder();

        msg = Messages.getMessage("DIALOGS_EDIT_VC_CANCEL");
        CancelGraphPanelAction cancelAction = new CancelGraphPanelAction(msg);
        cancelButton = new JButton(cancelAction);
        cancelButton.setIcon(cancelIcon);
        cancelButton.setMargin(noMargin);

        msg = Messages.getMessage("VIEW_ACTION_VARIATION_BUTTON");
        VCVariationAction variationAction = VCVariationAction.getInstance(msg);
        variationButton = new JButton(variationAction);
        variationButton.setIcon(variationIcon);
        variationButton.setMargin(noMargin);
        GUIUtilities.setObjectBackground(variationButton,
                GUIUtilities.VIEW_COLOR);

        msg = Messages.getMessage("VIEW_ACTION_MODIFY_BUTTON");
        OpenVCEditDialogModifyAction modifyAction = OpenVCEditDialogModifyAction
                .getInstance(msg);
        modifyButton = new JButton(modifyAction);
        modifyButton.setIcon(modifyIcon);
        modifyButton.setMargin(noMargin);
        GUIUtilities.setObjectBackground(modifyButton, GUIUtilities.VIEW_COLOR);
    }

    /**
     * 19 juil. 2005
     */
    private void initBorder() {
        String msg = Messages.getMessage("VIEW_ACTION_BORDER");
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
        Border border = (tb);
        this.setBorder(border);

    }

    public DateRangeBox getDateRangeBox() {
        return dateRangeBox;
    }

    public void setDateRangeBox(DateRangeBox dateRangeBox) {
        this.dateRangeBox = dateRangeBox;
    }

    public void refreshButtonChangeColor() {
        refreshButton.setBorder(new LineBorder(Color.RED, 1));
    }

    public void refreshButtonDefaultColor() {
        refreshButton.setBorder(refreshButtonDefaultBorder);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof ViewAttributesGraphPanel) {
            String property = evt.getPropertyName();
            if (ViewAttributesGraphPanel.CANCELING.equals(property)
                    || ViewAttributesGraphPanel.CANCELED.equals(property)
                    || ViewAttributesGraphPanel.PANELS_LOADED.equals(property)) {
                putRefreshButton();
            }
            else if (ViewAttributesGraphPanel.SETTING_PANELS.equals(property)
                    || ViewAttributesGraphPanel.PANELS_SET.equals(property)
                    || ViewAttributesGraphPanel.ADDING_PANELS.equals(property)
                    || ViewAttributesGraphPanel.PANELS_ADDED.equals(property)
                    || ViewAttributesGraphPanel.LOADING_PANELS.equals(property)) {
                putCancelButton();
            }
        }
    }

    public void putRefreshButton() {
        synchronized (this) {
            for (int i = 0; i < getComponentCount(); i++) {
                if (getComponent(i) == refreshButton) return;
            }
            remove(cancelButton);
            add(refreshButton, refreshConstraints);
            VCRefreshAction.getInstance().setEnabled(true);
            OpenVCEditDialogModifyAction.getInstance().setEnabled(true);
            VCVariationAction.getInstance().setEnabled(true);
            VCRefreshAction.getInstance().setEnabled(true);
            revalidate();
            repaint();
        }
    }

    public void putCancelButton() {
        synchronized (this) {
            for (int i = 0; i < getComponentCount(); i++) {
                if (getComponent(i) == cancelButton) return;
            }
            remove(refreshButton);
            add(cancelButton, refreshConstraints);
            VCRefreshAction.getInstance().setEnabled(false);
            OpenVCEditDialogModifyAction.getInstance().setEnabled(false);
            VCVariationAction.getInstance().setEnabled(false);
            VCRefreshAction.getInstance().setEnabled(false);
            revalidate();
            repaint();
        }
    }

}
