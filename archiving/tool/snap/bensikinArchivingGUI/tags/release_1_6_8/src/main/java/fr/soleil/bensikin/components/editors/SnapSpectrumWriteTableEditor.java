/*	Synchrotron Soleil 
 *  
 *   File          :  SnapSpectrumWriteTableEditor.java
 *  
 *   Project       :  Bensikin_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  14 f�vr. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SnapSpectrumWriteTableEditor.java,v 
 *
 */
package fr.soleil.bensikin.components.editors;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * 
 * @author SOLEIL
 */
public class SnapSpectrumWriteTableEditor extends AbstractCellEditor implements
		TableCellEditor {

	// private ImageAttibuteDialog imageAttibuteDialog;
	// private SpectrumAttributeDialog spectrumAttibuteDialog;
	private JTextField buffer;

	JTable correspondingTable;

	/**
	 * Default constructor
	 */
	public SnapSpectrumWriteTableEditor(JTable table) {
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
		buffer = new JTextField(value.toString());
		return buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return buffer.getText();
	}

}
