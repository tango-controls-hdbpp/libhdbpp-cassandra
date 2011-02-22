// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotFilterPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotFilterPanel.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotFilterPanel.java,v $
// Revision 1.6 2006/01/10 13:28:56 ounsy
// minor changes
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.snapshot.FilterSnapshotsAction;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * The panel containing the filtering criterions for the snapshots list.
 * 
 * @author CLAISSE
 */
public class SnapshotFilterPanel extends JPanel {

    private static final long          serialVersionUID            = 8032812660426288958L;
    private static SnapshotFilterPanel snapshotFilterPanelInstance = null;
    private ImageIcon                  filterIcon                  = new ImageIcon(
                                                                           Bensikin.class
                                                                                   .getResource("icons/search.gif"));
    private ImageIcon                  resetIcon                   = new ImageIcon(
                                                                           Bensikin.class
                                                                                   .getResource("icons/reload.gif"));

    /**
     * The operators combo box for the id criterion
     */
    private OperatorsList              selectId;

    /**
     * The operators combo box for the start time criterion
     */
    private OperatorsList              selectStartTime;

    /**
     * The operators combo box for the end time criterion
     */
    private OperatorsList              selectEndTime;

    /**
     * The operators combo box for the comment criterion
     */
    private OperatorsList              selectComment;

    /**
     * The text field for the id criterion
     */
    private JTextField                 textId;

    /**
     * The text field for the start time criterion
     */
    private JTextField                 textStartTime;

    /**
     * The text field for the end time criterion
     */
    private JTextField                 textEndTime;

    /**
     * The text field for the comment criterion
     */
    private JTextField                 textComment;

    private JButton                    filterButton;
    private JButton                    resetButton;
    private JPanel                     idPanel;
    private JPanel                     resetPanel;

    private String                     msgId                       = Messages
                                                                           .getMessage("SNAPSHOT_LIST_LABELS_ID");
    private String                     msgTime                     = Messages
                                                                           .getMessage("SNAPSHOT_LIST_LABELS_TIME");
    private String                     msgComment                  = Messages
                                                                           .getMessage("SNAPSHOT_LIST_LABELS_COMMENT");
    private String[]                   labels                      = { msgId,
            msgTime, msgComment                                   };

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static SnapshotFilterPanel getInstance() {
        if (snapshotFilterPanelInstance == null) {
            snapshotFilterPanelInstance = new SnapshotFilterPanel();
        }

        return snapshotFilterPanelInstance;
    }

    /**
     * Builds the panel
     */
    private SnapshotFilterPanel() {
        initComponents();
        initLayout();

        GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
    }

    /**
     * Sets the panel's SpringLayout
     */
    private void initLayout() {
        int numCriterions = labels.length;
        Dimension emptyBoxDimension = new Dimension(10, 20);

        // Create and populate the panel.
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);

        for (int i = 0; i < numCriterions; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);

            // START COLUMN 1
            this.add(l);
            // END COLUMN 1

            // START COLUMN 2
            switch (i) {
                case 0:
                    this.add(selectId);
                    break;

                case 1:
                    this.add(selectStartTime);
                    break;

                case 2:
                    this.add(selectComment);
                    break;
            }
            // START COLUMN 2

            // START COLUMN 3
            switch (i) {
                case 0:
                    this.add(idPanel);
                    break;

                case 1:
                    this.add(textStartTime);
                    break;

                case 2:
                    this.add(textComment);
                    break;
            }
            // START COLUMN 3

            // START COLUMN 4
            if (i == 1) {
                this.add(selectEndTime);
            }
            else if (i == 2) {
                this.add(filterButton);
            }
            else {
                this.add(Box.createRigidArea(emptyBoxDimension));
            }
            // END COLUMN 4

            // START COLUMN 5
            if (i == 1) {
                this.add(textEndTime);
            }
            else if (i == 2) {
                this.add(resetPanel);
            }
            else {
                this.add(Box.createRigidArea(emptyBoxDimension));
            }
            // END COLUMN 5
        }

        // Lay out the panel.
        SpringUtilities.makeCompactGrid(this, numCriterions, 5, // rows, cols
                6, 6, // initX, initY
                6, 6, // xPad, yPad
                true);
    }

    /**
     * 24 juin 2005
     */
    private void initComponents() {
        Font font = new Font("Arial", Font.PLAIN, 11);
        selectId = new OperatorsList(OperatorsList.ID_TYPE);
        selectStartTime = new OperatorsList(OperatorsList.AFTER_TIME_TYPE);
        selectEndTime = new OperatorsList(OperatorsList.BEFORE_TIME_TYPE);
        selectComment = new OperatorsList(OperatorsList.COMMENT_TYPE);

        textId = new JTextField();
        idPanel = new JPanel();
        GUIUtilities.setObjectBackground(idPanel, GUIUtilities.SNAPSHOT_COLOR);
        idPanel.setLayout(new SpringLayout());
        idPanel.add(textId);
        idPanel.add(Box.createHorizontalGlue());
        textStartTime = new JTextField();
        textEndTime = new JTextField();
        textComment = new JTextField();

        textId.setPreferredSize(new Dimension(40, 25));
        textId.setMaximumSize(new Dimension(40, 25));
        textComment.setPreferredSize(new Dimension(180, 25));

        textStartTime.setPreferredSize(new Dimension(180, 25));
        textEndTime.setPreferredSize(new Dimension(180, 25));

        String msg = Messages.getMessage("SNAPSHOT_LIST_FILTER");
        filterButton = new JButton(FilterSnapshotsAction.getInstance(msg));
        // Be sure to have the expected text
        filterButton.setText(msg);
        filterButton.setIcon(filterIcon);
        filterButton.setMargin(new Insets(0, 0, 0, 0));
        // filterButton.setPreferredSize(new Dimension(65, 20));
        filterButton.setFocusPainted(false);
        filterButton.setFocusable(false);
        filterButton.setFont(font);
        GUIUtilities.setObjectBackground(filterButton,
                GUIUtilities.SNAPSHOT_COLOR);

        String msg2 = Messages.getMessage("SNAPSHOT_LIST_RESET");
        resetButton = new JButton(FilterSnapshotsAction.getInstance(msg2));
        resetButton.setText(msg2);
        resetButton.setIcon(resetIcon);
        resetButton.setMargin(new Insets(0, 0, 0, 0));
        // resetButton.setPreferredSize(selectEndTime.getPreferredSize());
        // resetButton.setMaximumSize(selectEndTime.getPreferredSize());
        resetButton.setFocusPainted(false);
        resetButton.setFocusable(false);
        resetButton.setFont(font);
        GUIUtilities.setObjectBackground(resetButton,
                GUIUtilities.SNAPSHOT_COLOR);
        resetPanel = new JPanel();
        resetPanel.setLayout(new SpringLayout());
        resetPanel.add(resetButton);
        resetPanel.add(Box.createHorizontalGlue());
        GUIUtilities.setObjectBackground(resetPanel,
                GUIUtilities.SNAPSHOT_COLOR);

        SpringUtilities.makeCompactGrid(idPanel, 1, 2, // rows, cols
                0, 0, // initX, initY
                0, 0, // xPad, yPad
                false);
        SpringUtilities.makeCompactGrid(resetPanel, 1, 2, // rows, cols
                0, 0, // initX, initY
                0, 0, // xPad, yPad
                false);

    }

    /**
     * Resets all text fields and combo boxes to their default empty value.
     */
    public void resetFields() {
        selectId.setSelectedItem(OperatorsList.NO_SELECTION);
        selectStartTime.setSelectedItem(OperatorsList.NO_SELECTION);
        selectEndTime.setSelectedItem(OperatorsList.NO_SELECTION);
        selectComment.setSelectedItem(OperatorsList.NO_SELECTION);

        textId.setText("");
        textStartTime.setText("");
        textEndTime.setText("");
        textComment.setText("");

    }

    /**
     * @return Returns the resetButton.
     */
    public JButton getResetButton() {
        return resetButton;
    }

    public JTextField getTextStartTime() {
        return textStartTime;
    }

    public JTextField getTextEndTime() {
        return textEndTime;
    }

    public OperatorsList getSelectComment() {
        return selectComment;
    }

    public OperatorsList getSelectEndTime() {
        return selectEndTime;
    }

    public OperatorsList getSelectStartTime() {
        return selectStartTime;
    }

    public OperatorsList getSelectId() {
        return selectId;
    }

    public JTextField getTextId() {
        return textId;
    }

    public JTextField getTextComment() {
        return textComment;
    }

}
