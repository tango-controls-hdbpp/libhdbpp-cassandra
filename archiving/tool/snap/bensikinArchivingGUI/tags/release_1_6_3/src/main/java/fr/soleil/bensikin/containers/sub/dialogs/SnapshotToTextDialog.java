// +======================================================================
// $Source$
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class UpdateCommentDialog.
// (Claisse Laurent) - 16 juin 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.1 2007/08/22 08:01:37 ounsy
// SnapshotToTextDialog moved to the right package
//
// Revision 1.3 2007/08/21 15:04:06 ounsy
// minor changes : look and feel
//
// Revision 1.2 2006/02/15 09:18:51 ounsy
// minor changes
//
// Revision 1.1 2005/12/14 16:54:06 ounsy
// added methods necessary for direct clipboard edition
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.snapshot.ValidateClipboardEditionAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTablePanel;
import fr.soleil.bensikin.tools.Messages;

/**
 * A small JDialog to update the comment field of a snapshot. The current value
 * is displayed and can be changed by the user
 * 
 * @author CLAISSE
 */
public class SnapshotToTextDialog extends CancelableDialog {

    private static final long                  serialVersionUID = -5995435503287529083L;
    private Dimension                          dim              = new Dimension(
                                                                        300,
                                                                        300);
    private JPanel                             myPanel;

    private JTextArea                          commentText;
    private JButton                            cancelButton;
    private JButton                            validateButton;
    private JScrollPane                        scrollPane;

    private WeakReference<SnapshotDetailTable> refTable;

    /**
     * Builds the dialog.
     */
    public SnapshotToTextDialog(SnapshotDetailTable snapshotDetailTable) {
        super(BensikinFrame.getInstance(), Messages
                .getMessage("DIALOGS_SNAPSHOT_TO_TEXT"), true);
        this.refTable = new WeakReference<SnapshotDetailTable>(
                snapshotDetailTable);

        this.initComponents();
        this.addComponents();

        this.setSize(dim);
        this.setLocation(SnapshotDetailTablePanel.getInstance()
                .getLocationOnScreen());
    }

    /**
     * Inits the dialog's components. The comment's value is initialized with
     * the snapshot's current comment.
     */
    private void initComponents() {
        commentText = new JTextArea();

        SnapshotDetailTable table = refTable.get();
        if (table != null) {
            commentText.setText(table.snapshotToCsvString());
        }
        table = null;

        String msg = Messages.getMessage("DIALOGS_SNAPSHOT_TO_TEXT_CANCEL");
        cancelButton = new JButton(new CancelAction(msg, this));
        cancelButton.setMargin(new Insets(0, 0, 0, 0));

        msg = Messages.getMessage("DIALOGS_SNAPSHOT_TO_TEXT_OK");
        validateButton = new JButton(new ValidateClipboardEditionAction(msg,
                this));
        validateButton.setMargin(new Insets(0, 0, 0, 0));

        scrollPane = new JScrollPane(commentText);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    /**
     * Adds the initialized components to the dialog.
     */
    private void addComponents() {
        myPanel = new JPanel();
        this.getContentPane().add(myPanel);

        myPanel.setLayout(new GridBagLayout());

        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.weightx = 1;
        scrollConstraints.weighty = 1;
        scrollConstraints.gridwidth = GridBagConstraints.REMAINDER;

        GridBagConstraints validateConstraints = new GridBagConstraints();
        validateConstraints.fill = GridBagConstraints.NONE;
        validateConstraints.gridx = 0;
        validateConstraints.gridy = 1;
        validateConstraints.weightx = 0;
        validateConstraints.weighty = 0;

        GridBagConstraints glueConstraints = new GridBagConstraints();
        glueConstraints.fill = GridBagConstraints.HORIZONTAL;
        glueConstraints.gridx = 1;
        glueConstraints.gridy = 1;
        glueConstraints.weightx = 1;
        glueConstraints.weighty = 0;

        GridBagConstraints cancelConstraints = new GridBagConstraints();
        cancelConstraints.fill = GridBagConstraints.NONE;
        cancelConstraints.gridx = 2;
        cancelConstraints.gridy = 1;
        cancelConstraints.weightx = 0;
        cancelConstraints.weighty = 0;

        myPanel.add(scrollPane, scrollConstraints);
        myPanel.add(validateButton, validateConstraints);
        myPanel.add(Box.createHorizontalGlue(), glueConstraints);
        myPanel.add(cancelButton, cancelConstraints);
    }

    public String getText() {
        String ret = this.commentText.getText();
        if (ret != null) {
            ret.trim();
        }
        return ret;
    }

    @Override
    public void cancel() {
        // nothing particular to do
    }
}
