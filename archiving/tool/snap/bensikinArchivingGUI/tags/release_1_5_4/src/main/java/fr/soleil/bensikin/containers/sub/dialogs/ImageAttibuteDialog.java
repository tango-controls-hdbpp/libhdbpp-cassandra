//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/ImageAttibuteDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ImageAttibuteDialog.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ImageAttibuteDialog.java,v $
// Revision 1.8  2007/02/08 16:47:31  ounsy
// bug correction with image attributes
//
// Revision 1.7  2006/07/24 07:38:59  ounsy
// better image support
//
// Revision 1.6  2006/06/28 12:48:00  ounsy
// image support
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SpringLayout;
import javax.swing.table.JTableHeader;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.snapshot.ViewImageAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.models.ColumnHeaderModel;
import fr.soleil.bensikin.models.ImageReadValueTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * Dialog used to represent image values.
 * 
 * @author CLAISSE
 */
public class ImageAttibuteDialog extends JDialog {
	protected final static ImageIcon imageIcon = new ImageIcon(Bensikin.class
			.getResource("icons/viewImage.gif"));
	protected final static int initColumnWidth = 50;
	protected final static Dimension dim = new Dimension(400, 400);
	protected JLabel noData;

	protected JPanel myPanel;
	protected JTable imageTable, headerTable;
	protected Object value;
	protected int data_type;
	protected JButton viewButton;
	protected String attributeName;
	protected String displayFormat;

	/**
	 *
	 */
	public ImageAttibuteDialog(String attributeName, Object _value,
			int data_type, String _displayFormat) {
		super(BensikinFrame.getInstance(), Messages
				.getMessage("DIALOGS_IMAGE_ATTRIBUTE_TITLE")
				+ ": " + attributeName, true);
		this.data_type = data_type;
		this.attributeName = attributeName;
		this.value = duplicateValue(_value);

		this.setSize(dim);
		int x = BensikinFrame.getInstance().getX()
				+ BensikinFrame.getInstance().getWidth();
		x -= (this.getWidth() + 50);
		if (x < 0)
			x = 0;
		int y = BensikinFrame.getInstance().getY()
				+ BensikinFrame.getInstance().getHeight();
		y -= (this.getHeight() + 50);
		if (y < 0)
			y = 0;
		this.setLocation(x, y);

		this.initComponents();
		this.addComponents();
		this.initLayout();
		this.setContentPane(myPanel);
		repaint();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * 15 juin 2005
	 */
	protected void initLayout() {
		myPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(myPanel, myPanel.getComponentCount(),
				1, 10, 10, 10, 10, true);
	}

	/**
	 *
	 */
	protected void initComponents() {
		myPanel = new JPanel();
		GUIUtilities.setObjectBackground(myPanel, GUIUtilities.SNAPSHOT_COLOR);
		if (hasNoData()) {
			noData = new JLabel(Messages
					.getMessage("DIALOGS_IMAGE_ATTRIBUTE_NAN"), JLabel.CENTER);
			GUIUtilities.setObjectBackground(noData,
					GUIUtilities.SNAPSHOT_COLOR);
		} else {
			initTable();
			initOthers();
		}
	}

	protected void initTable() {
		imageTable = new JTable(new ImageReadValueTableModel(value, data_type));
		imageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < imageTable.getColumnCount(); i++) {
			imageTable.getColumn(imageTable.getColumnName(i)).setMinWidth(
					initColumnWidth);
			imageTable.getColumn(imageTable.getColumnName(i)).setWidth(
					initColumnWidth);
			imageTable.getColumn(imageTable.getColumnName(i))
					.setPreferredWidth(initColumnWidth);
		}
	}

	protected void initOthers() {
		headerTable = new JTable(
				new ColumnHeaderModel(imageTable.getRowCount()));
		headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < headerTable.getColumnCount(); i++) {
			headerTable.getColumn(headerTable.getColumnName(i)).setMinWidth(
					initColumnWidth);
			headerTable.getColumn(headerTable.getColumnName(i)).setWidth(
					initColumnWidth);
			headerTable.getColumn(headerTable.getColumnName(i))
					.setPreferredWidth(initColumnWidth);
			headerTable.getColumn(headerTable.getColumnName(i)).setMaxWidth(
					initColumnWidth);
		}
		headerTable.setDefaultRenderer(Object.class, new JTableHeader()
				.getDefaultRenderer());
		headerTable.setMaximumSize(new Dimension(initColumnWidth,
				Integer.MAX_VALUE));
		viewButton = new JButton(new ViewImageAction(attributeName, value,
				data_type, displayFormat));
		viewButton.setIcon(imageIcon);
	}

	protected void addComponents() {
		if (hasNoData()) {
			myPanel.add(noData);
		} else {
			addWhenData();
		}
	}

	protected void addWhenData() {
		JScrollPane scrollpane = new JScrollPane(imageTable);
		GUIUtilities.setObjectBackground(scrollpane,
				GUIUtilities.SNAPSHOT_COLOR);
		GUIUtilities.setObjectBackground(scrollpane.getViewport(),
				GUIUtilities.SNAPSHOT_COLOR);
		JViewport rowHeader = new JViewport();
		rowHeader.setMaximumSize(new Dimension(initColumnWidth,
				Integer.MAX_VALUE));
		rowHeader.setPreferredSize(new Dimension(initColumnWidth,
				Integer.MAX_VALUE));
		rowHeader.setSize(new Dimension(initColumnWidth, Integer.MAX_VALUE));
		rowHeader.setMinimumSize(new Dimension(initColumnWidth,
				Integer.MAX_VALUE));
		rowHeader.setView(headerTable);
		scrollpane.setRowHeader(rowHeader);
		myPanel.add(scrollpane);
		myPanel.add(viewButton);
	}

	protected boolean hasNoData() {
		if (value == null) {
			// System.out.println("value is null");
			return true;
		} else {
			switch (data_type) {
			case TangoConst.Tango_DEV_CHAR:
			case TangoConst.Tango_DEV_UCHAR:
				if (((Byte[][]) value).length == 0
						|| ((Byte[][]) value)[0].length == 0) {
					return true;
				}
				break;
			case TangoConst.Tango_DEV_SHORT:
			case TangoConst.Tango_DEV_USHORT:
				if (((Short[][]) value).length == 0
						|| ((Short[][]) value)[0].length == 0) {
					return true;
				}
				break;
			case TangoConst.Tango_DEV_LONG:
			case TangoConst.Tango_DEV_ULONG:
				if (((Integer[][]) value).length == 0
						|| ((Integer[][]) value)[0].length == 0) {
					return true;
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (((Float[][]) value).length == 0
						|| ((Float[][]) value)[0].length == 0) {
					return true;
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (((Double[][]) value).length == 0
						|| ((Double[][]) value)[0].length == 0) {
					return true;
				}
				break;
			default: // nothing to do
			}
		}
		return false;
	}

	protected Object duplicateValue(Object _value) {
		// System.out.println("----duplicateValue");
		if (_value == null) {
			// System.out.println("-----stupid value");
			return null;
		}
		Object result = null;
		switch (data_type) {
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
			// System.out.println("case char");
			int cLength = ((Byte[][]) _value).length;
			if (cLength > 0) {
				int c0Length = ((Byte[][]) _value)[0].length;
				result = new Byte[cLength][c0Length];
				for (int i = 0; i < cLength; i++) {
					for (int j = 0; j < c0Length; j++) {
						((Byte[][]) result)[i][j] = ((Byte[][]) _value)[i][j];
					}
				}
			} else
				result = new Byte[0][0];
			break;
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
			// System.out.println("case short");
			int sLength = ((Short[][]) _value).length;
			if (sLength > 0) {
				int s0Length = ((Short[][]) _value)[0].length;
				result = new Short[sLength][s0Length];
				for (int i = 0; i < sLength; i++) {
					for (int j = 0; j < s0Length; j++) {
						((Short[][]) result)[i][j] = ((Short[][]) _value)[i][j];
					}
				}
			} else
				result = new Short[0][0];
			break;
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
			// System.out.println("case long");
			int lLength = ((Integer[][]) _value).length;
			if (lLength > 0) {
				int l0Length = ((Integer[][]) _value)[0].length;
				result = new Integer[lLength][l0Length];
				for (int i = 0; i < lLength; i++) {
					for (int j = 0; j < l0Length; j++) {
						((Integer[][]) result)[i][j] = ((Integer[][]) _value)[i][j];
					}
				}
			} else
				result = new Integer[0][0];
			break;
		case TangoConst.Tango_DEV_FLOAT:
			// System.out.println("case float");
			int fLength = ((Float[][]) _value).length;
			if (fLength > 0) {
				int f0Length = ((Float[][]) _value)[0].length;
				result = new Float[fLength][f0Length];
				for (int i = 0; i < fLength; i++) {
					for (int j = 0; j < f0Length; j++) {
						((Float[][]) result)[i][j] = ((Float[][]) _value)[i][j];
					}
				}
			} else
				result = new Float[0][0];
			break;
		case TangoConst.Tango_DEV_DOUBLE:
			// System.out.println("case double");
			int dLength = ((Double[][]) _value).length;
			if (dLength > 0) {
				int g0Length = ((Double[][]) _value)[0].length;
				result = new Double[dLength][g0Length];
				for (int i = 0; i < dLength; i++) {
					for (int j = 0; j < g0Length; j++) {
						((Double[][]) result)[i][j] = ((Double[][]) _value)[i][j];
					}
				}
			} else
				result = new Double[0][0];
			break;
		default: // nothing to do
			// System.out.println("default case");
		}
		return result;
	}

	/**
	 * Returns the registered value
	 * 
	 * @return The registered value
	 */
	public Object getValue() {
		return value;
	}
}
