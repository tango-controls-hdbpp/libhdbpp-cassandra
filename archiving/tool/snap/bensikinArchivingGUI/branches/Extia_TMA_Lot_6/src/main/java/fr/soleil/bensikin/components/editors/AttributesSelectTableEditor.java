//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/editors/AttributesSelectTableEditor.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SnapshotDetailEditor.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.1 $
//
//$Log: AttributesSelectTableEditor.java,v $
//Revision 1.1  2005/12/14 16:53:06  ounsy
//added methods necessary for alternate attribute selection
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

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Cell editor used by SnapshotDetailTable. The used component for scalar
 * attributes is a TextField. *
 */
public class AttributesSelectTableEditor extends AbstractCellEditor implements
		TableCellEditor {
	private JCheckBox buffer;

	/**
	 * Default constructor
	 */
	public AttributesSelectTableEditor() {
		buffer = null;
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
		buffer = new JCheckBox();
		Boolean _value = (Boolean) value;
		buffer.setSelected(_value.booleanValue());

		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return new Boolean(buffer.isSelected());
	}

}
