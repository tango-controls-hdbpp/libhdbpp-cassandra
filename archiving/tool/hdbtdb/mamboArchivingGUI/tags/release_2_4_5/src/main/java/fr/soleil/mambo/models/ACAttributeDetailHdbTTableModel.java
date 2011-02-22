//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailHdbTTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbTTableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACAttributeDetailHdbTTableModel.java,v $
// Revision 1.3  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.2  2005/12/15 11:41:33  ounsy
// period formating
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
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

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeDetailHdbTTableModel extends DefaultTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 152646176577479727L;
	private static ACAttributeDetailHdbTTableModel instance;
	private ArchivingConfigurationAttribute attribute;
	private ArchivingConfigurationAttributeHDBProperties HDBProperties;

	public static ACAttributeDetailHdbTTableModel getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailHdbTTableModel();
		}

		return instance;
	}

	private ACAttributeDetailHdbTTableModel() {
		super();
	}

	public void load(ArchivingConfigurationAttribute _attribute) {
		attribute = _attribute;
		ArchivingConfigurationAttributeProperties attrProperties = attribute
				.getProperties();
		HDBProperties = attrProperties.getHDBProperties();
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
		return 3;
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
		IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
		try {
			Mode mode = manager.getArchivingMode(attribute.getCompleteName(),
					true);
			if (mode != null && mode.getModeT() != null) {
				switch (rowIndex) {
				case 0:
					ret = mode.getModeT().getPeriod() / 1000 + "s";
					break;
				case 1:
					ret = mode.getModeT().getThresholdSup() + "";
					break;
				case 2:
					ret = mode.getModeT().getThresholdInf() + "";
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private Object getACColumnValue(int rowIndex) {
		String ret = "";
		if (HDBProperties != null) {
			ArchivingConfigurationMode ACTMode = HDBProperties
					.getMode(ArchivingConfigurationMode.TYPE_T);
			if (ACTMode != null) {
				switch (rowIndex) {
				case 0:
					ret = ACTMode.getMode().getModeT().getPeriod() / 1000 + "s";
					break;
				case 1:
					ret = ACTMode.getMode().getModeT().getThresholdSup() + "";
					break;
				case 2:
					ret = ACTMode.getMode().getModeT().getThresholdInf() + "";
					break;
				}

			}
		}
		return ret;
	}

	public boolean toPaint() {
		if (HDBProperties != null) {
			if (HDBProperties.getMode(ArchivingConfigurationMode.TYPE_T) != null) {
				return true;
			} else if (attribute != null && attribute.getCompleteName() != null) {
				try {
					Mode mode = ArchivingManagerFactory
							.getCurrentImpl()
							.getArchivingMode(attribute.getCompleteName(), true);
					if (mode.getModeT() != null) {
						return true;
					}
				} catch (Exception e) {
					return false;
				}
			}
		}
		return false;
	}

	private Object getSecondColumnValue(int rowIndex) {
		String ret = "";
		switch (rowIndex) {
		case 0:
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODET_PERIOD");
			break;
		case 1:
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODET_UPPER");
			break;
		case 2:
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODET_LOWER");
			break;
		}
		return ret;
	}

	private Object getFirstColumnValue(int rowIndex) {
		String ret = "";
		if (rowIndex == 0) {
			ret = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODET_NAME");
		}
		return ret;
	}

}
