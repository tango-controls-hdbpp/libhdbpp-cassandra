//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailHdbRTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbRTableModel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACAttributeDetailHdbRTableModel.java,v $
// Revision 1.3  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.2  2005/12/15 11:41:21  ounsy
// period formating
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

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeDetailHdbRTableModel extends DefaultTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5669245151168904420L;
	private static ACAttributeDetailHdbRTableModel instance;
	private ArchivingConfigurationAttribute attribute;
	private ArchivingConfigurationAttributeHDBProperties HDBProperties;

	public static ACAttributeDetailHdbRTableModel getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailHdbRTableModel();
		}

		return instance;
	}

	private ACAttributeDetailHdbRTableModel() {
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
		return 4;
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
			if (mode != null && mode.getModeR() != null) {
				switch (rowIndex) {
				case 0:
					ret = mode.getModeR().getPeriod() / 1000 + "s";
					break;
				case 1:
					ret = mode.getModeR().getPercentSup() + "%";
					break;
				case 2:
					ret = mode.getModeR().getPercentInf() + "%";
					break;
				case 3:
					ret = Boolean.toString(mode.getModeR().isSlow_drift());
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
			ArchivingConfigurationMode ACRMode = HDBProperties
					.getMode(ArchivingConfigurationMode.TYPE_R);
			if (ACRMode != null) {
				switch (rowIndex) {
				case 0:
					ret = ACRMode.getMode().getModeR().getPeriod() / 1000 + "s";
					break;
				case 1:
					ret = ACRMode.getMode().getModeR().getPercentSup() + "%";
					break;
				case 2:
					ret = ACRMode.getMode().getModeR().getPercentInf() + "%";
					break;
				case 3:
					ret = Boolean.toString(ACRMode.getMode().getModeR()
							.isSlow_drift());
					break;

				}

			}
		}
		return ret;
	}

	public boolean toPaint() {
		if (HDBProperties != null) {
			if (HDBProperties.getMode(ArchivingConfigurationMode.TYPE_R) != null) {
				return true;
			} else if (attribute != null) {
				try {
					Mode mode = ArchivingManagerFactory
							.getCurrentImpl()
							.getArchivingMode(attribute.getCompleteName(), true);
					if (mode.getModeR() != null) {
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
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODER_PERIOD");
			break;
		case 1:
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODER_UPPER");
			break;
		case 2:
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODER_LOWER");
			break;
		case 3:
			ret = Messages
					.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODER_SLOW_DRIFT");
			break;

		}
		return ret;
	}

	private Object getFirstColumnValue(int rowIndex) {
		String ret = "";
		if (rowIndex == 0) {
			ret = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODER_NAME");
		}
		return ret;
	}

}
