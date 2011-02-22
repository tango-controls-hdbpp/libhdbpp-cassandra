/*
 * Synchrotron Soleil
 * 
 * File : SpectrumButton.java
 * 
 * Project : Bensikin_CVS
 * 
 * Description :
 * 
 * Author : SOLEIL
 * 
 * Original : 3 f�vr. 2006
 * 
 * Revision: Author: Date: State:
 * 
 * Log: SpectrumButton.java,v
 */
package fr.soleil.bensikin.components.snapshot;

import javax.swing.JLabel;

import fr.soleil.bensikin.containers.sub.dialogs.SpectrumAttributeDialog;
import fr.soleil.bensikin.models.SnapshotDetailTableModel;
import fr.soleil.bensikin.tools.Messages;

/**
 * 
 * @author SOLEIL
 */
public class SpectrumButton extends JLabel {

    private static final long        serialVersionUID = -1635191568593307899L;
    private SnapshotDetailTableModel tableModel;
    private int                      row, column;
    private String                   name;

    public SpectrumButton(String name, int row, int column,
            SnapshotDetailTableModel tableModel) {
        super(Messages.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_VIEW"),
                JLabel.CENTER);
        // this.setOpaque(false);
        this.row = row;
        this.column = column;
        this.name = name;
        this.tableModel = tableModel;
        setEnabled((tableModel != null)
                && (tableModel.getValueAt(row, column) != null));
    }

    public void actionPerformed() {
        new SpectrumAttributeDialog(name, row, column, tableModel)
                .setVisible(true);
    }
}
