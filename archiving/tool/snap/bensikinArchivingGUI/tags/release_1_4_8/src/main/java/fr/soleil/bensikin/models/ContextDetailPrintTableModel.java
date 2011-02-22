package fr.soleil.bensikin.models;

import javax.swing.table.DefaultTableModel;

public class ContextDetailPrintTableModel extends DefaultTableModel {

    public ContextDetailPrintTableModel() {
        super();
    }

    @Override
    public int getColumnCount () {
        AttributesSelectTableModel model = AttributesSelectTableModel.getInstance();
        if (model != null && model.getColumnCount() > 0) {
            return model.getColumnCount() - 1;
        }
        return 0;
    }

    @Override
    public int getRowCount () {
        AttributesSelectTableModel model = AttributesSelectTableModel.getInstance();
        if (model != null) {
            return model.getRowCount();
        }
        return 0;
    }

    @Override
    public Object getValueAt (int rowIndex, int columnIndex) {
        AttributesSelectTableModel model = AttributesSelectTableModel.getInstance();
        if (model != null) {
            return model.getValueAt(rowIndex, columnIndex);
        }
        return null;
    }

    @Override
    public String getColumnName (int column) {
        AttributesSelectTableModel model = AttributesSelectTableModel.getInstance();
        if (model != null) {
            return model.getColumnName(column);
        }
        return "";
    }

    @Override
    public boolean isCellEditable (int row, int column) {
        return false;
    }
}
