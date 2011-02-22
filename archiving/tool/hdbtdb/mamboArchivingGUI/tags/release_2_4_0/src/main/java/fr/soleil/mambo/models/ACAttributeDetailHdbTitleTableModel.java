//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailHdbTitleTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbTitleTableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailHdbTitleTableModel.java,v $
// Revision 1.2  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:08  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.models;

import javax.swing.table.DefaultTableModel;

import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.tools.Messages;

//import mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
//import mambo.data.archiving.ArchivingConfigurationAttributeProperties;

public class ACAttributeDetailHdbTitleTableModel extends DefaultTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 7069566942277346668L;
	private static ACAttributeDetailHdbTitleTableModel instance;

	// private ArchivingConfigurationAttribute attribute;
	// private ArchivingConfigurationAttributeHDBProperties HDBProperties;

	public static ACAttributeDetailHdbTitleTableModel getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailHdbTitleTableModel();
		}

		return instance;
	}

	private ACAttributeDetailHdbTitleTableModel() {
		super();
	}

	public void load(ArchivingConfigurationAttribute _attribute) {
		// attribute = _attribute;
		// ArchivingConfigurationAttributeProperties attrProperties =
		// attribute.getProperties();
		// HDBProperties = attrProperties.getHDBProperties();
		fireTableDataChanged();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 4;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getFirstColumnValue(rowIndex);
		case 1:
			return getSecondColumnValue(rowIndex);
		case 2:
			return getACColumnValue(rowIndex);
		case 3:
			return getDBColumnValue(rowIndex);
		default:
			return null;
		}
	}

	private Object getDBColumnValue(int rowIndex) {
		String ret = "";
		ret = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_DB");
		return ret;
	}

	private Object getACColumnValue(int rowIndex) {
		String ret = "";
		ret = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_AC");
		return ret;
	}

	public boolean toPaint() {
		return true;
	}

	private Object getSecondColumnValue(int rowIndex) {
		String ret = Messages
				.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_CARACTERISTIC");
		return ret;
	}

	private Object getFirstColumnValue(int rowIndex) {
		String ret = "";
		if (rowIndex == 0) {
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_TITLE_TABLE_MODE");
		}
		return ret;
	}

}
