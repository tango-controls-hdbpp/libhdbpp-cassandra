//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/editors/SnapshotDetailEditor.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SnapshotDetailEditor.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.10 $
//
//$Log: SnapshotDetailEditor.java,v $
//Revision 1.10  2006/11/29 09:58:31  ounsy
//minor changes
//
//Revision 1.9  2006/06/28 12:46:25  ounsy
//image support
//
//Revision 1.8  2006/03/16 16:40:30  ounsy
//taking care of String formating
//
//Revision 1.7  2006/02/15 09:15:42  ounsy
//small spectrum bug correcion
//
//Revision 1.6  2005/12/14 16:11:41  ounsy
//minor changes
//
//Revision 1.5  2005/11/29 18:25:27  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.BensikinBooleanViewer;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.StringFormater;

/**
 * Cell editor used by SnapshotDetailTable. The used component for scalar
 * attributes is a TextField.
 * 
 * @author CLAISSE
 */
public class SnapshotDetailEditor extends AbstractCellEditor implements
		TableCellEditor {

	private JComponent buffer;

	JTable correspondingTable;

	/**
	 * Default constructor
	 */
	public SnapshotDetailEditor(JTable table) {
		super();
		buffer = null;
		correspondingTable = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing
	 * .JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		return getScalarTableCellEditorComponent(table, value, isSelected, row,
				column);
	}

	/**
	 * Used for scalar values.
	 * 
	 * @param table
	 * @param value
	 * @param isSelected
	 * @param row
	 * @param column
	 * @return A JTextField which content is filled with the String
	 *         representation of value, or a JCheckBox if value is Boolean.
	 */
	private Component getScalarTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int row, int column) {
		if (value instanceof Boolean) {
			buffer = new JCheckBox();
			((JCheckBox) buffer).setSelected(((Boolean) value).booleanValue());
			((JCheckBox) buffer).addActionListener(new CheckBoxListener(
					((JCheckBox) buffer), correspondingTable, row, column));
		} else {
			if (value instanceof SnapshotAttributeValue) {
				if ((((SnapshotAttributeValue) value).getDataFormat() == SnapshotAttributeValue.SCALAR_DATA_FORMAT)
						&& (((SnapshotAttributeValue) value).getDataType() == TangoConst.Tango_DEV_BOOLEAN)) {
					Boolean val = (Boolean) ((SnapshotAttributeValue) value)
							.getScalarValue();
					buffer = new BensikinBooleanViewer(val);
					buffer.setOpaque(true);
					if (isSelected) {
						buffer.setBackground(new Color(0, 0, 120));
						buffer.setForeground(Color.WHITE);
					} else {
						buffer.setBackground(Color.WHITE);
						buffer.setForeground(Color.BLACK);
					}
					((JCheckBox) buffer)
							.addActionListener(new CheckBoxListener(
									((JCheckBox) buffer), correspondingTable,
									row, column));
				} else {
					buffer = new JTextField();
					Object val = ((SnapshotAttributeValue) value).getValue();
					if (val == null) {
						((JTextField) buffer).setText("");
						// System.out.println("null value at line " + row );
					} else if (val instanceof String) {
						((JTextField) buffer).setText(StringFormater
								.formatStringToRead(new String((String) val)));
					} else {
						((JTextField) buffer).setText(val.toString());
					}
				}
			} else {
				((JTextField) buffer).setText(value.toString());
			}
		}
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		if (buffer instanceof JCheckBox) {
			return new Boolean(((JCheckBox) buffer).isSelected());
		} else if (buffer instanceof JTextField) {
			return ((JTextField) buffer).getText();
		} else {
			return null;
		}
	}

}

class CheckBoxListener implements ActionListener {

	private JCheckBox checkbox;
	private JTable correspondingTable;
	private int row;
	private int column;

	public CheckBoxListener(JCheckBox _checkbox, JTable table, int _row,
			int _column) {

		this.checkbox = _checkbox;
		this.correspondingTable = table;
		this.row = _row;
		this.column = _column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		correspondingTable.setValueAt(new Boolean(checkbox.isSelected()), row,
				column);
		correspondingTable.editingCanceled(null);
	}
}