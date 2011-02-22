package fr.soleil.bensikin.models;

import javax.swing.table.AbstractTableModel;

import fr.soleil.bensikin.components.BensikinComparator;

public abstract class SortedTableModel extends AbstractTableModel {
    protected int idSort;
    protected int sortedColumn;

    public SortedTableModel() {
        super();
        idSort = BensikinComparator.NO_SORT;
        sortedColumn = -1;
    }

    public abstract void sort(int clickedColumnIndex);

    public int getSortedColumn () {
        return sortedColumn;
    }

    public int getIdSort () {
        return idSort;
    }
}
