// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingActionPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ArchivingActionPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ArchivingActionPanel.java,v $
// Revision 1.10 2007/01/09 16:25:49 ounsy
// look & feel with "expand all" buttons in main frame
//
// Revision 1.9 2006/11/06 09:28:05 ounsy
// icons reorganization
//
// Revision 1.8 2006/08/09 16:12:54 ounsy
// No more automatic tree expanding : user has to click on a button to fully
// expand a tree
//
// Revision 1.7 2006/07/28 10:07:12 ounsy
// icons moved to "icons" package
//
// Revision 1.6 2006/05/19 13:45:13 ounsy
// minor changes
//
// Revision 1.5 2006/05/16 12:49:41 ounsy
// modified imports
//
// Revision 1.4 2006/03/10 12:03:25 ounsy
// state and string support
//
// Revision 1.3 2006/01/23 09:01:33 ounsy
// Avoiding multi click on "start" button
//
// Revision 1.2 2005/11/29 18:28:26 chinkumo
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
package fr.soleil.mambo.containers.archiving;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.archiving.ACRecapAction;
import fr.soleil.mambo.actions.archiving.ArchivingStopAction;
import fr.soleil.mambo.actions.archiving.ArchivingTransferAction;
import fr.soleil.mambo.actions.archiving.LoadACAction;
import fr.soleil.mambo.actions.archiving.OpenACEditDialogAction;
import fr.soleil.mambo.actions.archiving.SaveSelectedACAction;
import fr.soleil.mambo.actions.listeners.ArchivingStartListener;
import fr.soleil.mambo.components.MamboActivableButton;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ArchivingActionPanel extends JPanel implements
        PropertyChangeListener {

    private static final long           serialVersionUID      = -7479379658816474365L;

    private static ArchivingActionPanel instance              = null;

    private JButton                     startButton;
    private JButton                     stopButton;
    private JButton                     modifyButton;
    // private JButton newButton;
    private JButton                     quickSaveButton;
    private JButton                     quickLoadButton;
    private JButton                     transferButton;
    private JButton                     recapButton;
    private JButton                     invisible;

    OpenACEditDialogAction              editAction;

    private final static ImageIcon      quickLoadIcon         = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Quick_Open.gif"));
    private final static ImageIcon      quickLoadDisabledIcon = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Quick_Open_Disabled.gif"));
    private final static ImageIcon      quickSaveIcon         = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Quick_Save.gif"));
    private final static ImageIcon      quickSaveDisabledIcon = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Quick_Save_Disabled.gif"));
    private final static ImageIcon      startIcon             = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Play.gif"));
    private final static ImageIcon      startDisabledIcon     = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Play_Disabled.gif"));
    private final static ImageIcon      stopIcon              = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Stop.gif"));
    private final static ImageIcon      stopDisabledIcon      = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Stop_Disabled.gif"));
    private final static ImageIcon      modifyIcon            = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Modify.gif"));
    private final static ImageIcon      modifyDisabledIcon    = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Modify_Disabled.gif"));
    private final static ImageIcon      transferIcon          = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Transfer.gif"));
    private final static ImageIcon      transferDisabledIcon  = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Transfer_Disabled.gif"));
    private final static ImageIcon      recapIcon             = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Variation.gif"));
    private final static ImageIcon      recapDisabledIcon     = new ImageIcon(
                                                                      Mambo.class
                                                                              .getResource("icons/Variation_Disabled.gif"));

    private static boolean              isAlternateSelectionMode;

    /**
     * @return 8 juil. 2005
     */
    public static ArchivingActionPanel getInstance() {
        if (instance == null) {
            instance = new ArchivingActionPanel();
            GUIUtilities.setObjectBackground(instance,
                    GUIUtilities.ARCHIVING_COLOR);
        }

        return instance;
    }

    /**
     * 
     */
    private ArchivingActionPanel() {
        super();

        initComponents();
        addComponents();
        setLayout();
        initBorder();
        // ViewAttributesGraphPanel.getInstance().addPropertyChangeListener(this);
        enableTransfer();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout() {
        this.setLayout(new SpringLayout());

        // Lay out the panel.
        SpringUtilities.makeCompactGrid(this, 2, 4, // rows, cols
                6, 6, // initX, initY
                6, 6, // xPad, yPad
                true);

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        // Line 1
        this.add(startButton);
        this.add(stopButton);
        this.add(modifyButton);
        this.add(Box.createHorizontalGlue());
        // Line 2
        this.add(transferButton);
        this.add(recapButton);
        this.add(invisible);
        this.add(Box.createHorizontalGlue());
        /*
         * this.add ( quickSaveButton ); this.add ( quickLoadButton );
         */
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents() {
        String msg = "";
        // boolean isAlternateSelectionMode = true;

        msg = Messages.getMessage("ARCHIVING_ACTION_START_BUTTON");
        // ArchivingStartAction archivingStartAction =
        // ArchivingStartAction.getInstance( msg );
        startButton = new MamboActivableButton(msg,
                new ArchivingStartListener(), startIcon, startDisabledIcon);
        GUIUtilities.setObjectBackground(startButton,
                GUIUtilities.ARCHIVING_COLOR);

        msg = Messages.getMessage("ARCHIVING_ACTION_STOP_BUTTON");
        ArchivingStopAction archivingStopAction = new ArchivingStopAction(msg);
        stopButton = new MamboActivableButton(archivingStopAction, stopIcon,
                stopDisabledIcon);
        GUIUtilities.setObjectBackground(stopButton,
                GUIUtilities.ARCHIVING_COLOR);

        msg = Messages.getMessage("ARCHIVING_ACTION_MODIFY_BUTTON");
        editAction = new OpenACEditDialogAction(msg, false,
                isAlternateSelectionMode);
        modifyButton = new MamboActivableButton(editAction, modifyIcon,
                modifyDisabledIcon);
        GUIUtilities.setObjectBackground(modifyButton,
                GUIUtilities.ARCHIVING_COLOR);

        msg = Messages.getMessage("ARCHIVING_ACTION_QUICK_SAVE_BUTTON");
        SaveSelectedACAction quickSaveAction = new SaveSelectedACAction(msg,
                true);
        quickSaveButton = new MamboActivableButton(quickSaveAction,
                quickSaveIcon, quickSaveDisabledIcon);
        GUIUtilities.setObjectBackground(quickSaveButton,
                GUIUtilities.ARCHIVING_COLOR);

        msg = Messages.getMessage("ARCHIVING_ACTION_QUICK_LOAD_BUTTON");
        LoadACAction quickLoadAction = new LoadACAction(msg, true);
        quickLoadButton = new MamboActivableButton(quickLoadAction,
                quickLoadIcon, quickLoadDisabledIcon);
        GUIUtilities.setObjectBackground(quickLoadButton,
                GUIUtilities.ARCHIVING_COLOR);

        msg = Messages.getMessage("ARCHIVING_ACTION_TRANSFER_BUTTON");
        ArchivingTransferAction tranferAction = ArchivingTransferAction
                .getInstance(msg);
        transferButton = new MamboActivableButton(tranferAction, transferIcon,
                transferDisabledIcon);
        GUIUtilities.setObjectBackground(transferButton,
                GUIUtilities.ARCHIVING_COLOR);

        msg = Messages.getMessage("ARCHIVING_ASSESSMENT_TITLE");
        ACRecapAction recapAction = new ACRecapAction(msg);
        recapButton = new MamboActivableButton(recapAction, recapIcon,
                recapDisabledIcon);
        GUIUtilities.setObjectBackground(recapButton,
                GUIUtilities.ARCHIVING_COLOR);

        invisible = new JButton();
        invisible.setVisible(false);

    }

    /**
     * 19 juil. 2005
     */
    private void initBorder() {
        String msg = Messages.getMessage("ARCHIVING_ACTION_BORDER");
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP);
        Border border = (Border) (tb);
        this.setBorder(border);

    }

    /**
     * @return Returns the isAlternateSelectionMode.
     */
    public static boolean isAlternateSelectionMode() {
        return isAlternateSelectionMode;
    }

    /**
     * @param isAlternateSelectionMode
     *            The isAlternateSelectionMode to set.
     */
    public static void setAlternateSelectionMode(
            boolean _isAlternateSelectionMode) {
        ArchivingActionPanel.isAlternateSelectionMode = _isAlternateSelectionMode;
        // System.out.println (
        // "ArchivingActionPanel/setAlternateSelectionMode/_isAlternateSelectionMode/"+_isAlternateSelectionMode+"/"
        // );
        if (instance != null) {
            instance.setOpenActions(_isAlternateSelectionMode);
        }
    }

    public void setOpenActions(boolean _isAlternateSelectionMode) {
        // System.out.println (
        // "ArchivingActionPanel/setOpenActions/_isAlternateSelectionMode/"+_isAlternateSelectionMode+"/"
        // );
        if (this.editAction != null) {
            editAction.setAlternateSelectionMode(_isAlternateSelectionMode);
        }
        if (ArchivingGeneralPanel.getInstance().newAction != null) {
            ArchivingGeneralPanel.getInstance().newAction
                    .setAlternateSelectionMode(_isAlternateSelectionMode);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof ViewAttributesGraphPanel) {
            String property = evt.getPropertyName();
            if (ViewAttributesGraphPanel.CANCELING.equals(property)
                    || ViewAttributesGraphPanel.CANCELED.equals(property)
                    || ViewAttributesGraphPanel.PANELS_LOADED.equals(property)) {
                enableTransfer();
            }
            else if (/*
                      * ViewAttributesGraphPanel.SETTING_PANELS.equals(property)
                      * || ViewAttributesGraphPanel.PANELS_SET.equals(property)
                      * ||
                      */ViewAttributesGraphPanel.ADDING_PANELS.equals(property)
                    || ViewAttributesGraphPanel.PANELS_ADDED.equals(property)
                    || ViewAttributesGraphPanel.LOADING_PANELS.equals(property)) {
                disableTransfer();
            }
        }
    }

    public void enableTransfer() {
        synchronized (this) {
            ArchivingTransferAction.getInstance().setEnabled(true);
            repaint();
        }
    }

    public void disableTransfer() {
        synchronized (this) {
            ArchivingTransferAction.getInstance().setEnabled(false);
            repaint();
        }
    }

}
