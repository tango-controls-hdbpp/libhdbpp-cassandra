// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributeDetailTableModel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACAttributeDetailTableModel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACAttributeDetailTableModel.java,v $
// Revision 1.3 2006/12/07 16:45:39 ounsy
// removed keeping period
//
// Revision 1.2 2005/11/29 18:27:08 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.models;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.tools.Messages;

public class ACAttributeDetailTableModel extends DefaultTableModel {

    /**
	 *
	 */
    private static final long serialVersionUID = 6776615145158986394L;
    private final String[] columnsNames;
    private final String isArchived;
    private final String modes;

    private static ACAttributeDetailTableModel instance;
    private ArchivingConfigurationAttribute attribute;
    private ArchivingConfigurationAttributeHDBProperties HDBProperties;
    private ArchivingConfigurationAttributeTDBProperties TDBProperties;

    private final JTextArea hdbArea, tdbArea;
    private final JScrollPane hdbScroll, tdbScroll;

    /**
     * @param snapshot
     * @return 8 juil. 2005
     */
    public static ACAttributeDetailTableModel getInstance() {
	if (instance == null) {
	    // instance = new ACAttributeDetailTableModel ();
	    final String msgCol1 = "A";
	    final String msgCol2 = "B";
	    final String msgTDB = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_TDB_LABEL");
	    final String msgHDB = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_HDB_LABEL");

	    /*
	     * String isArchived1 = Messages.getMessage (
	     * "ARCHIVING_ATTRIBUTES_DETAIL_IS_ARCHIVED_LABEL" ); String modes1
	     * = Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_MODE_LABEL"
	     * );
	     */
	    final String[] _columnNames = new String[4];

	    _columnNames[0] = msgCol1;
	    _columnNames[1] = msgCol2;
	    _columnNames[2] = msgTDB;
	    _columnNames[3] = msgHDB;

	    instance = new ACAttributeDetailTableModel(_columnNames, 8);
	}

	return instance;
    }

    private ACAttributeDetailTableModel(final String[] _columnNames, final int rowCount) {
	super(_columnNames, rowCount);
	columnsNames = _columnNames;
	isArchived = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_IS_ARCHIVED_LABEL");
	modes = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_MODE_LABEL");
	// System.out.println (
	// "ACAttributeDetailTableModel/new/msgCol1/"+msgCol1+"/msgCol2/"+msgCol2+"/msgTDB/"+msgTDB+"/msgHDB/"+msgHDB+"/"
	// );
	hdbArea = new JTextArea("");
	tdbArea = new JTextArea("");
	hdbArea.setBackground(new Color(210, 190, 190));
	tdbArea.setBackground(new Color(210, 190, 190));
	hdbScroll = new JScrollPane(hdbArea);
	tdbScroll = new JScrollPane(tdbArea);
	hdbScroll.setBackground(new Color(210, 190, 190));
	tdbScroll.setBackground(new Color(210, 190, 190));
    }

    /**
     *
     */
    /*
     * private ACAttributeDetailTableModel () { String msgCol1 = "A"; String
     * msgCol2 = "B"; String msgTDB = Messages.getMessage (
     * "ARCHIVING_ATTRIBUTES_DETAIL_TDB_LABEL" ); String msgHDB =
     * Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_HDB_LABEL" );
     * isArchived = Messages.getMessage (
     * "ARCHIVING_ATTRIBUTES_DETAIL_IS_ARCHIVED_LABEL" ); modes =
     * Messages.getMessage ( "ARCHIVING_ATTRIBUTES_DETAIL_MODE_LABEL" );
     * columnsNames = new String [ this.getColumnCount () ]; columnsNames [ 0 ]
     * = msgCol1; columnsNames [ 1 ] = msgCol2; columnsNames [ 2 ] = msgTDB;
     * columnsNames [ 3 ] = msgHDB; System.out.println (
     * "ACAttributeDetailTableModel/new/msgCol1/"
     * +msgCol1+"/msgCol2/"+msgCol2+"/msgTDB/"+msgTDB+"/msgHDB/"+msgHDB+"/" ); }
     */

    /**
     * 17 juin 2005
     * 
     * @throws SnapshotingException
     */
    /**
     * @param snapshot
     *            8 juil. 2005
     */
    public void load(final ArchivingConfigurationAttribute _attribute) {
	hdbArea.setText("");
	tdbArea.setText("");
	attribute = _attribute;

	final ArchivingConfigurationAttributeProperties attrProperties = attribute.getProperties();
	HDBProperties = attrProperties.getHDBProperties();
	TDBProperties = attrProperties.getTDBProperties();

	if (TDBProperties != null) {
	    fillArea(TDBProperties, tdbArea);
	    if (TDBProperties.getExportPeriod() >= 0) {
		tdbArea.setText(tdbArea.getText() + GUIUtilities.CRLF + "Export Period : "
			+ TDBProperties.getExportPeriod() + "ms");
	    }
	    /*
	     * if ( TDBProperties.getKeepingPeriod() >= 0 ) { tdbArea.setText(
	     * tdbArea.getText() + GUIUtilities.CRLF + "Keeping Period : " +
	     * TDBProperties.getKeepingPeriod() + "ms" ); }
	     */
	}
	if (HDBProperties != null) {
	    fillArea(HDBProperties, hdbArea);
	}
	/*
	 * System.out.println("--------ACAttributeDetailTableModel/load-----------"
	 * ); System.out.println(attrProperties.toString());
	 * System.out.println("--------ACAttributeDetailTableModel/load-----------"
	 * );
	 */

	fireTableDataChanged();
    }

    private void fillArea(final ArchivingConfigurationAttributeDBProperties properties,
	    final JTextArea textArea) {
	final ArchivingConfigurationMode ACPMode = properties
		.getMode(ArchivingConfigurationMode.TYPE_P);
	if (ACPMode != null) {
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Periodic Mode Period : "
		    + ACPMode.getMode().getModeP().getPeriod() + "ms");
	}
	final ArchivingConfigurationMode ACAMode = properties
		.getMode(ArchivingConfigurationMode.TYPE_A);
	if (ACAMode != null) {
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Absolute Mode Period : "
		    + ACAMode.getMode().getModeA().getPeriod() + "ms");
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "Absolute Mode Upper Limit : " + ACAMode.getMode().getModeA().getValSup());
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "Absolute Mode Lower Limit : " + ACAMode.getMode().getModeA().getValInf());
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Absolute Mode Slow Drift : "
		    + ACAMode.getMode().getModeA().isSlow_drift());
	}
	final ArchivingConfigurationMode ACRMode = properties
		.getMode(ArchivingConfigurationMode.TYPE_R);
	if (ACRMode != null) {
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Relative Mode Period : "
		    + ACRMode.getMode().getModeR().getPeriod() + "ms");
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "Relative Mode Upper Percent Limit : "
		    + ACRMode.getMode().getModeR().getPercentSup() + "%");
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "Relative Mode Lower Percent Limit : "
		    + ACRMode.getMode().getModeR().getPercentInf() + "%");
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Relative Mode Slow Drift : "
		    + ACRMode.getMode().getModeR().isSlow_drift());
	}
	final ArchivingConfigurationMode ACTMode = properties
		.getMode(ArchivingConfigurationMode.TYPE_T);
	if (ACTMode != null) {
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Threshold Mode Period : "
		    + ACTMode.getMode().getModeT().getPeriod() + "ms");
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "Threshold Mode Upper Limit : "
		    + ACTMode.getMode().getModeT().getThresholdSup());
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "Threshold Mode Lower Limit : "
		    + ACTMode.getMode().getModeT().getThresholdInf());
	}
	final ArchivingConfigurationMode ACDMode = properties
		.getMode(ArchivingConfigurationMode.TYPE_D);
	if (ACDMode != null) {
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF
		    + "On Difference Mode Period : " + ACDMode.getMode().getModeD().getPeriod()
		    + "ms");
	}
	final ArchivingConfigurationMode ACCMode = properties
		.getMode(ArchivingConfigurationMode.TYPE_C);
	if (ACCMode != null) {
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Calculation Mode Period : "
		    + ACPMode.getMode().getModeC().getPeriod() + "ms");
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Calculation Mode Range : "
		    + ACPMode.getMode().getModeC().getRange());
	    textArea.setText(textArea.getText() + GUIUtilities.CRLF + "Calculation Mode Type : "
		    + ACPMode.getMode().getModeC().getTypeCalcul());
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
	return 4;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
	return 10;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
	switch (columnIndex) {
	case 0:
	    return getFirstColumnValue(rowIndex);

	case 1:
	    return getSecondColumnValue(rowIndex);

	case 2:
	    return getTDBColumnValue(rowIndex);

	case 3:
	    return getHDBColumnValue(rowIndex);

	default:
	    return null;
	    // we have a bug
	}
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getHDBColumnValue(final int rowIndex) {
	Boolean ret = new Boolean(false);

	if (rowIndex == 0) {
	    return columnsNames[3];
	} else if (rowIndex == 1) {
	    if (attribute == null) {
		return ret;
	    }

	    try {
		ret = new Boolean(ArchivingManagerFactory.getCurrentImpl().isArchived(
			attribute.getCompleteName(), true));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	} else if (rowIndex == 9) {
	    return hdbScroll;
	} else {
	    if (HDBProperties == null || HDBProperties.isEmpty()) {
		return ret;
	    }

	    ret = hasMode(HDBProperties, rowIndex);
	}
	return ret;
    }

    /**
     * @param properties
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Boolean hasMode(final ArchivingConfigurationAttributeDBProperties _properties,
	    final int rowIndex) {
	Boolean _ret = new Boolean(false);
	switch (rowIndex) {
	case 2:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_P);
	    if (_ret.booleanValue()) {
		if (_properties == HDBProperties) {

		}
	    }
	    break;

	case 3:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_A);
	    break;

	case 4:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_R);
	    break;

	case 5:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_T);
	    break;

	case 6:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_D);
	    break;

	case 7:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_C);
	    break;

	case 8:
	    _ret = _properties.hasMode(ArchivingConfigurationMode.TYPE_E);
	    break;
	}

	return _ret;
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getTDBColumnValue(final int rowIndex) {
	Boolean ret = new Boolean(false);
	if (rowIndex == 0) {
	    return columnsNames[2];
	} else if (rowIndex == 1) {
	    if (attribute == null) {
		return ret;
	    }

	    try {
		ret = new Boolean(ArchivingManagerFactory.getCurrentImpl().isArchived(
			attribute.getCompleteName(), false));
	    } catch (final Exception e) {
		e.printStackTrace();
	    }
	} else if (rowIndex == 9) {
	    return tdbScroll;
	} else {
	    if (TDBProperties == null || TDBProperties.isEmpty()) {
		return ret;
	    }

	    ret = hasMode(TDBProperties, rowIndex);
	}

	return ret;
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getSecondColumnValue(final int rowIndex) {
	String ret = "";

	switch (rowIndex) {
	case 2:
	    ret = ArchivingConfigurationMode.TYPE_P_NAME;
	    break;

	case 3:
	    ret = ArchivingConfigurationMode.TYPE_A_NAME;
	    break;

	case 4:
	    ret = ArchivingConfigurationMode.TYPE_R_NAME;
	    break;

	case 5:
	    ret = ArchivingConfigurationMode.TYPE_T_NAME;
	    break;

	case 6:
	    ret = ArchivingConfigurationMode.TYPE_D_NAME;
	    break;

	case 7:
	    ret = ArchivingConfigurationMode.TYPE_C_NAME;
	    break;

	case 8:
	    ret = ArchivingConfigurationMode.TYPE_E_NAME;
	    break;
	}

	return ret;
    }

    /**
     * @param rowIndex
     * @return 27 juil. 2005
     */
    private Object getFirstColumnValue(final int rowIndex) {
	String ret = "";

	switch (rowIndex) {
	case 1:
	    ret = isArchived;
	    break;

	case 2:
	    ret = modes;
	    break;
	}

	return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int columnIndex) {
	// System.out.println ("getColumnName" );
	// System.out.println (
	// "ACAttributeDetailTableModel/columnIndex/"+columnIndex+"/columnsNames [ columnIndex ]/"+columnsNames
	// [ columnIndex ]+"/" );
	return columnsNames[columnIndex];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#setColumnName(int, String)
     */
    public void setColumnName(final int columnIndex, final String columnName) {
	if (columnIndex < getColumnCount()) {
	    columnsNames[columnIndex] = columnName;
	}
    }

}
