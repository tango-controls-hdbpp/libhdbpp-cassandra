package fr.soleil.bensikin.models;

import fr.soleil.bensikin.data.snapshot.Snapshot;

public class SnapshotDetailPrintTableModel extends SnapshotDetailTableModel {

    private static final long serialVersionUID = -4607571812713902904L;

    public SnapshotDetailPrintTableModel(Snapshot snapshot) {
        super();
        load(snapshot);
    }

    @Override
    public int getColumnCount() {
        return countColumns() - 1;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
