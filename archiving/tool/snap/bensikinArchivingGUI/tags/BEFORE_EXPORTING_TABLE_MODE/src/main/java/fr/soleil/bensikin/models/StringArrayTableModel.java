package fr.soleil.bensikin.models;

import javax.swing.table.DefaultTableModel;

public class StringArrayTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 493443661484021233L;
    private String[]          array;

    public StringArrayTableModel(String[] array) {
        super();
        this.array = array;
    }

    @Override
    public int getColumnCount() {
        if (array == null) {
            return 0;
        }
        else {
            return array.length;
        }
    }

    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public String getColumnName(int column) {
        if (array == null || column < 0 || column >= array.length) {
            return null;
        }
        else {
            return array[column];
        }
    }

}
