// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/UpdateCommentDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class UpdateCommentDialog.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: UpdateCommentDialog.java,v $
// Revision 1.6 2006/06/28 12:48:56 ounsy
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
package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.snapshot.EditCommentAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * A small JDialog to update the comment field of a snapshot. The current value
 * is displayed and can be changed by the user
 * 
 * @author CLAISSE
 */
public class UpdateCommentDialog extends CancelableDialog {

    private Dimension  dim = new Dimension(300, 300);
    private JPanel     myPanel;

    private JLabel     commentLabel;
    private JTextField commentText;
    private JButton    updateButton;
    private JButton    cancelButton;

    /**
     * Builds the dialog.
     */
    public UpdateCommentDialog() {
        super(BensikinFrame.getInstance(), Messages
                .getMessage("DIALOGS_UPDATE_COMMENT"), true);

        this.initComponents();
        this.addComponents();
        this.initLayout();

        this.setSize(dim);
        this.setLocation(400, 400);
    }

    /**
     * Inits the dialog's components. The comment's value is initialized with
     * the snapshot's current comment.
     */
    private void initComponents() {
        SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane
                .getInstance();
        // SnapshotDetailTabbedPaneContent content = (
        // SnapshotDetailTabbedPaneContent ) tabbedPane.getSelectedComponent();
        Snapshot snapshotToUse = ((SnapshotDetailTabbedPaneContent) tabbedPane
                .getSelectedComponent()).getSnapshot();

        commentText = new JTextField();
        commentText.setPreferredSize(new Dimension(200, 20));
        String previousComment = snapshotToUse.getSnapshotData().getComment();
        commentText.setText(previousComment);

        String msgComment = Messages.getMessage("DIALOGS_UPDATE_COMMENT_LABEL");
        commentLabel = new JLabel(msgComment, JLabel.TRAILING);

        String msg = Messages.getMessage("DIALOGS_UPDATE_COMMENT_VALIDATE");
        updateButton = new JButton(new EditCommentAction(msg, this));
        updateButton.setPreferredSize(new Dimension(60, 20));

        msg = Messages.getMessage("DIALOGS_UPDATE_COMMENT_CANCEL");
        cancelButton = new JButton(new CancelAction(msg, this));
        cancelButton.setPreferredSize(new Dimension(75, 20));
    }

    /**
     * Inits the dialog's layout.
     */
    private void initLayout() {
        myPanel.setLayout(new SpringLayout());

        SpringUtilities.makeCompactGrid(myPanel, 2, 4, // rows, cols
                6, 6, // initX, initY
                6, 6, // xPad, yPad
                true);
    }

    /**
     * Adds the initialized components to the dialog.
     */
    private void addComponents() {
        Dimension emptyBoxDimension = new Dimension(40, 20);

        myPanel = new JPanel();
        this.getContentPane().add(myPanel);

        // Create and populate the panel.

        // START ROW 1
        myPanel.add(commentLabel);
        myPanel.add(commentText);
        myPanel.add(Box.createRigidArea(emptyBoxDimension));
        myPanel.add(Box.createRigidArea(emptyBoxDimension));
        // END ROW 1

        // START ROW 2
        myPanel.add(Box.createRigidArea(emptyBoxDimension));
        myPanel.add(Box.createRigidArea(emptyBoxDimension));
        myPanel.add(cancelButton);
        myPanel.add(updateButton);
        // END ROW 2
    }

    /**
     * Returns the new comment.
     * 
     * @return The new comment
     */
    public JTextField getCommentText() {
        return commentText;
    }

    @Override
    public void cancel() {
        // nothing particular to do
    }

}
