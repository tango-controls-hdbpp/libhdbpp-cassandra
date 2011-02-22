package fr.soleil.bensikin.models;

import javax.swing.table.DefaultTableModel;

import fr.soleil.bensikin.data.snapshot.Snapshot;

public class SnapshotDetailPrintTableModel extends DefaultTableModel {

    private Snapshot snapshot = null;

    public SnapshotDetailPrintTableModel(Snapshot snapshot) {
        super();
        this.snapshot = snapshot;
    }

    @Override
    public int getColumnCount () {
        if (snapshot != null) {
            SnapshotDetailTableModel model = SnapshotDetailTableModel.getInstance(snapshot);
            if (model != null && model.getColumnCount() > 0) {
                return model.getColumnCount() - 1;
            }
        }
        return 0;
    }

    @Override
    public int getRowCount () {
        if (snapshot != null) {
            SnapshotDetailTableModel model = SnapshotDetailTableModel.getInstance(snapshot);
            if (model != null) {
                return model.getRowCount();
            }
        }
        return 0;
    }

    @Override
    public Object getValueAt (int rowIndex, int columnIndex) {
        if (snapshot != null) {
            SnapshotDetailTableModel model = SnapshotDetailTableModel.getInstance(snapshot);
            if (model != null) {
                return model.getValueAt(rowIndex, columnIndex);
            }
        }
        return null;
    }

    @Override
    public String getColumnName (int column) {
        if (snapshot != null) {
            SnapshotDetailTableModel model = SnapshotDetailTableModel.getInstance(snapshot);
            if (model != null) {
                return model.getColumnName(column);
            }
        }
        return "";
    }

    @Override
    public boolean isCellEditable (int row, int column) {
        return false;
    }
}
