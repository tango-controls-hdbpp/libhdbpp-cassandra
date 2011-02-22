package fr.soleil.bensikin.components.snapshot.detail;

import javax.swing.table.TableColumn;

import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.models.SnapshotDetailPrintTableModel;

public class SnapshotDetailPrintTable extends SnapshotDetailTable {

    private static final long serialVersionUID = 4891176588729880824L;

    public SnapshotDetailPrintTable(Snapshot snapshot) {
        super(snapshot);
    }

    @Override
    protected void initializeModel() {
        SnapshotDetailPrintTableModel model = new SnapshotDetailPrintTableModel(
                snapshot);
        this.setModel(model);
    }

    @Override
    protected void initializeTableColumns() {
        if (getColumnCount() > 0) {
            TableColumn column2 = this.getColumn(this.getColumnName(0));
            column2.setMinWidth(200);
            column2.setPreferredWidth(200);
        }
    }

    @Override
    protected void initializeEditor() {
        // No editor;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
