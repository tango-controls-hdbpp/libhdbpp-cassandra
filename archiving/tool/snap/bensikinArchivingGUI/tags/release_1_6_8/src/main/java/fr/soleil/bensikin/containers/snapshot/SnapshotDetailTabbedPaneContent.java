// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotDetailTabbedPaneContent.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailTabbedPaneContent.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.13 $
//
// $Log: SnapshotDetailTabbedPaneContent.java,v $
// Revision 1.13 2007/10/19 14:08:35 soleilarc
// Author: XP
// Mantis bug ID: 6576
// Comment: In the SnapshotDetailTabbedPaneContent class, add a data timeLabel.
// timeLabel is defined in the SnapshotDetailTabbedPaneContent builder, to
// display the timestamp in the Snapshot details of Bensikin.
//
// Revision 1.12 2007/08/23 15:28:48 ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.11 2007/08/21 15:13:16 ounsy
// Print Snapshot as table or text (Mantis bug 3913)
//
// Revision 1.10 2007/08/21 08:44:39 ounsy
// Snapshot Detail : Transfer Read To Write (Mantis bug 5543)
//
// Revision 1.9 2006/11/29 09:59:22 ounsy
// minor changes
//
// Revision 1.8 2006/02/23 10:05:14 ounsy
// minor changes
//
// Revision 1.7 2006/02/15 09:18:38 ounsy
// minor changes : uncomment to debug
//
// Revision 1.6 2005/12/14 16:26:13 ounsy
// added all/none selection and clipboard functionalities
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.snapshot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.snapshot.AddSnapshotToCompareAction;
import fr.soleil.bensikin.actions.snapshot.CloseSnapshotDetailTabAction;
import fr.soleil.bensikin.actions.snapshot.OpenEditCommentAction;
import fr.soleil.bensikin.actions.snapshot.SaveSelectedSnapshotAction;
import fr.soleil.bensikin.actions.snapshot.SelectAllOrNoneAction;
import fr.soleil.bensikin.actions.snapshot.SetEquipmentsAction;
import fr.soleil.bensikin.actions.snapshot.SetEquipmentwithCommandAction;
import fr.soleil.bensikin.actions.snapshot.SnapshotDetailPrintChooseAction;
import fr.soleil.bensikin.actions.snapshot.SnapshotToTextAction;
import fr.soleil.bensikin.actions.snapshot.TransferRToWAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailTable;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * Represents a tab of SnapshotDetailTabbedPane. This contains a snapshot's data, and actions on
 * this snapshot ( adding to a comparison, editing the comment, quick save, and setting equipments
 * with this snapshot's values).
 * 
 * @author CLAISSE
 */
public class SnapshotDetailTabbedPaneContent extends JPanel {

    private static final long serialVersionUID = -323427403047768562L;

    private ImageIcon modifiedIcon = new ImageIcon(Bensikin.class.getResource("icons/modified.gif"));
    private ImageIcon downIcon = new ImageIcon(Bensikin.class.getResource("icons/down.gif"));
    private ImageIcon editIcon = new ImageIcon(Bensikin.class.getResource("icons/edit.gif"));
    private ImageIcon setIcon = new ImageIcon(Bensikin.class.getResource("icons/set.gif"));
    private ImageIcon setEquipCmdIcon = new ImageIcon(Bensikin.class
            .getResource("icons/device.gif"));
    private ImageIcon quickSaveIcon = new ImageIcon(Bensikin.class
            .getResource("icons/quick_save.gif"));
    private ImageIcon closeTabIcon = new ImageIcon(Bensikin.class.getResource("icons/delete.gif"));
    private ImageIcon docIcon = new ImageIcon(Bensikin.class.getResource("icons/document.gif"));
    private ImageIcon docModifiedIcon = new ImageIcon(Bensikin.class
            .getResource("icons/document_modified.gif"));
    private ImageIcon warningIcon = new ImageIcon(Bensikin.class.getResource("icons/warning.gif"));
    private ImageIcon modifiedWarningIcon = new ImageIcon(Bensikin.class
            .getResource("icons/modified_warning.gif"));
    private ImageIcon docModifiedWarningIcon = new ImageIcon(Bensikin.class
            .getResource("icons/document_modified_warning.gif"));
    private ImageIcon docWarningIcon = new ImageIcon(Bensikin.class
            .getResource("icons/document_warning.gif"));
    private ImageIcon printIcon = new ImageIcon(Bensikin.class.getResource("icons/print.gif"));
    private Snapshot snapshot;
    private JButton closeTabButton;
    private JButton setEquipmentsButton;
    private JButton setEquipmentWithCommandButton;
    private JButton editCommentButton;
    private JButton compareButton;
    private JButton toTextButton;
    private JButton toTextAndEditButton;
    private JButton quickSaveButton;
    private JButton printButton;
    private SnapshotDetailPrintChooseAction printAction;
    private JButton transferRWButton;
    private JButton selectNoneButton;
    private JButton selectAllButton;
    private JPanel buttonPanel;
    private JLabel timeTitleLabel;
    private JLabel timeLabel;
    private JLabel contextTitleLabel;
    private JLabel contextLabel;
    private JLabel attrCountTitleLabel;
    private JLabel attrCountLabel;
    private Box infoBox;
    private JScrollPane scrollpane;

    private OpenEditCommentAction openEditCommentAction;
    private AddSnapshotToCompareAction addSnapshotToCompareAction;
    private SnapshotToTextAction snapshotToTextAction;
    private SnapshotToTextAction snapshotToTextAndEditAction;

    private SnapshotDetailTable snapshotDetailTable;

    private boolean isModified;
    private boolean mayFilter;

    /**
     * Builds the tab with the data found in <code>_snapshot</code>
     * 
     * @param _snapshot The snaphot to use
     */
    public SnapshotDetailTabbedPaneContent(Snapshot _snapshot) {
        this.snapshotDetailTable = new SnapshotDetailTable(_snapshot);

        Font font = new Font("Arial", Font.PLAIN, 11);
        this.snapshot = _snapshot;
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new SpringLayout());
        GUIUtilities.setObjectBackground(buttonPanel, GUIUtilities.SNAPSHOT_COLOR);
        buttonPanel.setMinimumSize(new Dimension(200, 25));
        buttonPanel.setPreferredSize(new Dimension(200, 25));
        buttonPanel.setMaximumSize(new Dimension(200, 25));

        JPanel secondButtonPanel = new JPanel();
        secondButtonPanel.setLayout(new SpringLayout());
        GUIUtilities.setObjectBackground(secondButtonPanel, GUIUtilities.SNAPSHOT_COLOR);
        secondButtonPanel.setMinimumSize(new Dimension(200, 25));
        secondButtonPanel.setPreferredSize(new Dimension(200, 25));
        secondButtonPanel.setMaximumSize(new Dimension(200, 25));

        closeTabButton = new JButton(closeTabIcon);
        String msg = Messages.getMessage("SNAPSHOT_DETAIL_CLOSE_TAB");
        closeTabButton.addActionListener(new CloseSnapshotDetailTabAction(msg));
        closeTabButton.setPreferredSize(new Dimension(25, 25));
        closeTabButton.setToolTipText(msg);
        closeTabButton.setBackground(Color.BLACK);
        closeTabButton.setFocusPainted(false);
        closeTabButton.setFocusable(false);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_SET_EQUIPMENTS");
        setEquipmentsButton = new JButton(new SetEquipmentsAction(msg));
        setEquipmentsButton.setIcon(setIcon);
        setEquipmentsButton.setMargin(new Insets(0, 0, 0, 0));
        setEquipmentsButton.setFocusPainted(false);
        setEquipmentsButton.setFocusable(false);
        setEquipmentsButton.setFont(font);
        GUIUtilities.setObjectBackground(setEquipmentsButton, GUIUtilities.SNAPSHOT_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_SET_EQUIPMENT_WITH_COMMAND");
        setEquipmentWithCommandButton = new JButton(new SetEquipmentwithCommandAction(msg));
        setEquipmentWithCommandButton.setIcon(setEquipCmdIcon);
        setEquipmentWithCommandButton.setMargin(new Insets(0, 0, 0, 0));
        setEquipmentWithCommandButton.setFocusPainted(false);
        setEquipmentWithCommandButton.setFocusable(false);
        setEquipmentWithCommandButton.setFont(font);
        GUIUtilities
                .setObjectBackground(setEquipmentWithCommandButton, GUIUtilities.SNAPSHOT_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_EDIT_COMMENT");
        this.openEditCommentAction = new OpenEditCommentAction(msg);
        editCommentButton = new JButton(openEditCommentAction);
        editCommentButton.setIcon(editIcon);
        editCommentButton.setMargin(new Insets(0, 0, 0, 0));
        editCommentButton.setFocusPainted(false);
        editCommentButton.setFocusable(false);
        editCommentButton.setFont(font);
        GUIUtilities.setObjectBackground(editCommentButton, GUIUtilities.SNAPSHOT_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_ADD_COMPARE");
        this.addSnapshotToCompareAction = new AddSnapshotToCompareAction(msg);
        compareButton = new JButton(addSnapshotToCompareAction);
        compareButton.setIcon(downIcon);
        compareButton.setMargin(new Insets(0, 0, 0, 0));
        compareButton.setFocusPainted(false);
        compareButton.setFocusable(false);
        compareButton.setFont(font);
        GUIUtilities.setObjectBackground(compareButton, GUIUtilities.SNAPSHOT_COLOR);

        // ---------------AJOUT CLA 08/12/05
        msg = Messages.getMessage("SNAPSHOT_DETAIL_TEXT");
        this.snapshotToTextAction = new SnapshotToTextAction(msg, false);
        toTextButton = new JButton(snapshotToTextAction);
        toTextButton.setMargin(new Insets(0, 0, 0, 0));
        toTextButton.setFocusPainted(false);
        toTextButton.setFocusable(false);
        toTextButton.setFont(font);
        GUIUtilities.setObjectBackground(toTextButton, GUIUtilities.SNAPSHOT_CLIPBOARD_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_TEXT_AND_EDIT");
        this.snapshotToTextAndEditAction = new SnapshotToTextAction(msg, true);
        toTextAndEditButton = new JButton(snapshotToTextAndEditAction);
        toTextAndEditButton.setMargin(new Insets(0, 0, 0, 0));
        toTextAndEditButton.setFocusPainted(false);
        toTextAndEditButton.setFocusable(false);
        toTextAndEditButton.setFont(font);
        GUIUtilities
                .setObjectBackground(toTextAndEditButton, GUIUtilities.SNAPSHOT_CLIPBOARD_COLOR);

        msg = Messages.getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_SELECT_NONE_BUTTON");
        selectNoneButton = new JButton(new SelectAllOrNoneAction(msg,
                SelectAllOrNoneAction.SELECT_NONE_TYPE, this.snapshotDetailTable));
        selectNoneButton.setMargin(new Insets(0, 0, 0, 0));
        selectNoneButton.setFocusPainted(false);
        selectNoneButton.setFocusable(false);
        selectNoneButton.setFont(font);
        GUIUtilities.setObjectBackground(selectNoneButton, GUIUtilities.SELECT_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_READ_TO_WRITE");
        transferRWButton = new JButton(new TransferRToWAction(msg, this.snapshotDetailTable));
        transferRWButton.setMargin(new Insets(0, 0, 0, 0));
        transferRWButton.setFocusPainted(false);
        transferRWButton.setFocusable(false);
        transferRWButton.setFont(font);
        GUIUtilities.setObjectBackground(transferRWButton, GUIUtilities.SELECT_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_SELECT_ALL_BUTTON");
        selectAllButton = new JButton(new SelectAllOrNoneAction(msg,
                SelectAllOrNoneAction.SELECT_ALL_TYPE, this.snapshotDetailTable));
        selectAllButton.setMargin(new Insets(0, 0, 0, 0));
        selectAllButton.setFocusPainted(false);
        selectAllButton.setFocusable(false);
        selectAllButton.setFont(font);
        GUIUtilities.setObjectBackground(selectAllButton, GUIUtilities.SELECT_COLOR);

        // ---------------AJOUT CLA 08/12/05

        msg = Messages.getMessage("SNAPSHOT_DETAIL_QUICK_SAVE");
        quickSaveButton = new JButton(new SaveSelectedSnapshotAction(msg, snapshot, true));
        quickSaveButton.setIcon(quickSaveIcon);
        quickSaveButton.setMargin(new Insets(0, 0, 0, 0));
        quickSaveButton.setFocusPainted(false);
        quickSaveButton.setFocusable(false);
        quickSaveButton.setFont(font);
        GUIUtilities.setObjectBackground(quickSaveButton, GUIUtilities.SNAPSHOT_COLOR);

        msg = Messages.getMessage("SNAPSHOT_DETAIL_PRINT_TITLE");
        printAction = new SnapshotDetailPrintChooseAction(msg);
        printButton = new JButton(printAction);
        printButton.setIcon(printIcon);
        printButton.setText("");
        printButton.setToolTipText(msg);
        printButton.setMargin(new Insets(0, 0, 0, 0));
        printButton.setFocusPainted(false);
        printButton.setFocusable(false);
        printButton.setFont(font);
        GUIUtilities.setObjectBackground(printButton, GUIUtilities.SNAPSHOT_CLIPBOARD_COLOR);

        timeTitleLabel = new JLabel(Messages.getMessage("SNAPSHOT_DETAIL_TIME_TITLE") + ":");
        String timeText;
        if (snapshot.getSnapshotData().getTime() == null) {
            timeText = Messages.getMessage("SNAPSHOT_DETAIL_NO_TIMESTAMP");
        }
        else {
            timeText = _snapshot.getSnapshotData().getTime().toString();
            if (timeText == null) {
                timeText = Messages.getMessage("SNAPSHOT_DETAIL_NO_TIMESTAMP");
            }
            else {
                timeText = timeText.trim();
                if ("".equals(timeText)) {
                    timeText = Messages.getMessage("SNAPSHOT_DETAIL_NO_TIMESTAMP");
                }
            }
        }
        timeLabel = new JLabel(timeText);
        timeText = null;

        contextTitleLabel = new JLabel(Messages.getMessage("SNAPSHOT_DETAIL_CONTEXT_TITLE") + ":");

        contextLabel = new JLabel(Messages.getMessage("SNAPSHOT_DETAIL_CONTEXT_KO"));
        attrCountTitleLabel = new JLabel(Messages
                .getMessage("SNAPSHOT_DETAIL_ATTRIBUTE_COUNT_TITLE")
                + ":");
        attrCountLabel = new JLabel(" ");

        scrollpane = new JScrollPane(this.snapshotDetailTable);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpane.setPreferredSize(new Dimension(650, 300));
        GUIUtilities.setObjectBackground(scrollpane, GUIUtilities.SNAPSHOT_COLOR);
        GUIUtilities.setObjectBackground(scrollpane.getViewport(), GUIUtilities.SNAPSHOT_COLOR);

        Box box = new Box(BoxLayout.X_AXIS);
        GUIUtilities.setObjectBackground(box, GUIUtilities.SNAPSHOT_COLOR);
        box.add(closeTabButton);
        box.add(Box.createHorizontalStrut(10));
        box.add(setEquipmentsButton);
        box.add(Box.createHorizontalStrut(10));
        box.add(setEquipmentWithCommandButton);
        box.add(Box.createHorizontalStrut(10));
        box.add(editCommentButton);
        box.add(Box.createHorizontalStrut(10));
        box.add(compareButton);
        // box.add( Box.createHorizontalStrut(10) );

        Box box1 = new Box(BoxLayout.X_AXIS);
        GUIUtilities.setObjectBackground(box1, GUIUtilities.SNAPSHOT_COLOR);
        box1.add(printButton);
        box1.add(Box.createHorizontalStrut(10));
        box1.add(toTextButton);
        box1.add(Box.createHorizontalStrut(10));
        box1.add(toTextAndEditButton);
        box1.add(Box.createHorizontalStrut(10));
        box1.add(transferRWButton);
        box1.add(Box.createHorizontalStrut(10));
        box1.add(selectNoneButton);
        box1.add(selectAllButton);

        infoBox = new Box(BoxLayout.X_AXIS);
        infoBox.add(contextTitleLabel);
        infoBox.add(Box.createHorizontalStrut(3));
        infoBox.add(contextLabel);
        infoBox.add(Box.createHorizontalStrut(15));
        infoBox.add(timeTitleLabel);
        infoBox.add(Box.createHorizontalStrut(3));
        infoBox.add(timeLabel);
        infoBox.add(Box.createHorizontalStrut(15));
        infoBox.add(attrCountTitleLabel);
        infoBox.add(Box.createHorizontalStrut(3));
        infoBox.add(attrCountLabel);

        buttonPanel.add(box1);
        secondButtonPanel.add(box);
        this.add(buttonPanel);
        this.add(secondButtonPanel);
        this.add(infoBox);
        this.add(scrollpane);

        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this, 4, 1, // rows, cols
                6, 6, // initX, initY
                6, 6, // xPad, yPad
                true);
        GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
        this.repaint();
    }

    /**
     * @return The used snapshot
     */
    public Snapshot getSnapshot() {
        return this.snapshot;
    }

    /**
     * Sets the enabled state of the EditComment Action for this tab. If this tab has been modified,
     * the action is disabled regardless of the value of disabled.
     * 
     * @param disabled True if the action has to be disabled.
     */
    public void setEditCommentDisabled(boolean disabled) {
        openEditCommentAction.setEnabled(!disabled);

        if (this.isModified) {
            openEditCommentAction.setEnabled(false);
        }
    }

    /**
     * Sets the "modified" state of the tab. A modified tab has a star on the left of its title.
     * 
     * @param _modified True if the tab's snapshot has been modified
     */
    public void setModified(boolean _modified) {
        this.isModified = _modified;
        this.snapshot.setModified(_modified);

        SnapshotDetailTabbedPane parent = (SnapshotDetailTabbedPane) this.getParent();
        int index = parent.indexOfComponent(this);

        if (this.isModified) {
            if (this.isFileContent()) {
                if (this.mayFilter) {
                    parent.setIconAt(index, docModifiedWarningIcon);
                }
                else {
                    parent.setIconAt(index, docModifiedIcon);
                }
            }
            else {
                if (this.mayFilter) {
                    parent.setIconAt(index, modifiedWarningIcon);
                }
                else {
                    parent.setIconAt(index, modifiedIcon);
                }
            }
        }
        else {
            if (this.isFileContent()) {
                if (this.mayFilter) {
                    parent.setIconAt(index, docWarningIcon);
                }
                else {
                    parent.setIconAt(index, docIcon);
                }
            }
            else {
                if (this.mayFilter) {
                    parent.setIconAt(index, warningIcon);
                }
                else {
                    parent.setIconAt(index, null);
                }
            }
        }

        if (!this.isModified) {
            this.snapshot.setModified(false);
        }
    }

    /**
     * Sets the "may Filter" state of the tab. When "may filter" is true, this means that user
     * unchecked an attribute in snapshot, so that this attribute won't be used to set equipment. If
     * true, a "!" icon will appear
     * 
     * @param filter True if user clicked on a checkbox to uncheck an attribute
     */
    public void setMayFilter(boolean filter) {
        this.mayFilter = filter;
        // setModified updates the icon
        this.setModified(this.isModified);
    }

    /**
     * Returns true if this tab's snapshot is a file snapshot, false otherwise.
     * 
     * @return True if this tab's snapshot is a file snapshot, false otherwise
     */
    public boolean isFileContent() {
        SnapshotDetailTabbedPane parent = (SnapshotDetailTabbedPane) this.getParent();
        int index = parent.indexOfComponent(this);

        String title = parent.getTitleAt(index);
        try {
            Integer.parseInt(title);
        }
        catch (NumberFormatException e) {
            return true;
        }

        return false;
    }

    public void reload() {
        getSnapshotDetailTable().getModel().load(getSnapshot());
        updateAttrCount();
        updateContextIdLabel();
        repaint();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                infoBox.repaint();
                getSnapshotDetailTable().repaint();
            }
        });
    }

    /**
     * Calls the printAction
     */
    public void openPrintDialog() {
        if (printAction != null) {
            printAction.actionPerformed(null);
        }
    }

    public SnapshotDetailTable getSnapshotDetailTable() {
        return snapshotDetailTable;
    }

    private void updateAttrCount() {
        int count = 0;
        if ((getSnapshot() != null) && (getSnapshot().getSnapshotAttributes() != null)
                && (getSnapshot().getSnapshotAttributes().getSnapshotAttributes() != null)) {
            count = getSnapshot().getSnapshotAttributes().getSnapshotAttributes().length;
        }
        attrCountLabel.setText(Integer.toString(count));
    }

    private void updateContextIdLabel() {
        String contextText;
        if (snapshot.getContextId() < 0) {
            contextText = Messages.getMessage("SNAPSHOT_DETAIL_CONTEXT_KO");
        }
        else {
            contextText = Integer.toString(snapshot.getContextId());
        }
        contextLabel.setText(contextText);
        contextText = null;
        contextLabel.repaint();
    }
}
