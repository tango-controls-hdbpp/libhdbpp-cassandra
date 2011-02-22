package fr.soleil.mambo.components.view.images;

import javax.swing.table.DefaultTableModel;

/**
 * The table model used by ImageViewTable, this model describes a completely
 * loaded image attribute. Its rows are the image's rows.
 * 
 * @author CLAISSE
 */
public class ImageViewTableModel extends DefaultTableModel {
	private double[][] rows;

	/**
	 * Returns the image row located at row <code>rowIndex</code>.
	 * 
	 * @param rowIndex
	 *            The specified row
	 * @return The image row located at row <code>rowIndex</code>
	 */
	public double[] getRow(int rowIndex) {
		return rows[rowIndex];
	}

	public ImageViewTableModel(double[][] _rows) {
		this.rows = _rows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (this.rows == null || this.rows.length == 0) {
			return 0;
		}

		return this.rows[0].length + 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (rows == null) {
			return 0;
		}

		return rows.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return String.valueOf(rowIndex);
		}

		return new Double(this.rows[rowIndex][columnIndex - 1]);
	}

	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return " ";
		}

		return String.valueOf(columnIndex - 1);
	}
}
