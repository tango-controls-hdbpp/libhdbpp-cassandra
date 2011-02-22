// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/renderers/BensikinTableCellRenderer.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class BensikinTableCellRenderer.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: BensikinTableCellRenderer.java,v $
// Revision 1.8 2007/06/29 09:18:58 ounsy
// devLong represented as Integer + SnapshotCompareTable sorting
//
// Revision 1.7 2007/02/08 15:06:19 ounsy
// corrected a bug for State scalar attributes
//
// Revision 1.6 2006/10/31 16:54:08 ounsy
// milliseconds and null values management
//
// Revision 1.5 2006/04/13 12:37:33 ounsy
// new spectrum types support
//
// Revision 1.4 2006/03/16 15:34:14 ounsy
// State scalar support
//
// Revision 1.3 2006/03/15 15:07:41 ounsy
// renders boolean scalars
//
// Revision 1.2 2005/12/14 16:12:04 ounsy
// minor changes
//
// Revision 1.1 2005/11/29 18:25:08 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.BensikinBooleanViewer;
import fr.soleil.bensikin.components.StateViewer;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.tools.Messages;

public class BensikinTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -8395892111266157843L;

	public BensikinTableCellRenderer() {
		super();
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component comp3 = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		if (value instanceof Boolean) {
			JCheckBox checkbox = new JCheckBox();
			checkbox.setSelected(((Boolean) value).booleanValue());
			checkbox.setOpaque(false);
			if (isSelected) {
				Component comp = super.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, row, column);
				checkbox.setOpaque(true);
				checkbox.setBackground(comp.getBackground());
			}
			return checkbox;
		} else if (value instanceof SnapshotAttributeValue
				&& (((SnapshotAttributeValue) value).getDataFormat() == SnapshotAttributeValue.SCALAR_DATA_FORMAT)) {
			if (((SnapshotAttributeValue) value).getDataType() == TangoConst.Tango_DEV_BOOLEAN) {
				Boolean val = (Boolean) ((SnapshotAttributeValue) value)
						.getScalarValue();
				BensikinBooleanViewer buffer = new BensikinBooleanViewer(val);
				buffer.setOpaque(true);
				if (isSelected) {
					buffer.setBackground(comp3.getBackground());
					buffer.setForeground(comp3.getForeground());
				} else {
					buffer.setBackground(Color.WHITE);
					buffer.setForeground(Color.BLACK);
				}
				return buffer;
			}
			if (((SnapshotAttributeValue) value).getDataType() == TangoConst.Tango_DEV_STATE) {
				try {
					Integer valS = (Integer) ((SnapshotAttributeValue) value)
							.getScalarValue();
					if (valS != null) {
						int val = valS.intValue();
						return new StateViewer(val);
					}
				} catch (Exception e) {
					// do nothign
				}
			}
			if (((SnapshotAttributeValue) value).getDataType() == TangoConst.Tango_DEV_STRING) {
				String val = (String) ((SnapshotAttributeValue) value)
						.getScalarValue();
				if (val != null) {
					JLabel viewer = new JLabel(val);
					viewer.setOpaque(true);
					if (isSelected) {
						viewer.setBackground(comp3.getBackground());
						viewer.setForeground(comp3.getForeground());
					} else {
						viewer.setBackground(Color.WHITE);
						viewer.setForeground(Color.BLACK);
					}
					return viewer;
				}
			}
			if (((SnapshotAttributeValue) value).getScalarValue() == null) {
				JLabel label = new JLabel(Messages
						.getMessage("SNAPSHOT_DETAIL_NO_DATA"));
				label.setBackground(Color.WHITE);
				label.setOpaque(true);
				return label;
			}
		}
		if (value == null) {
			JLabel label = new JLabel(Messages
					.getMessage("SNAPSHOT_DETAIL_NO_DATA"));
			label.setBackground(Color.WHITE);
			label.setOpaque(true);
			return label;
		}
		Component comp = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		JTableHeader header = table.getTableHeader();
		if (header != null) {
			TableCellRenderer renderer = header.getDefaultRenderer();
			if (renderer != null) {
				Component comp2 = renderer.getTableCellRendererComponent(table,
						value, isSelected, hasFocus, 0, 0);
				if (comp2 != null) {
					if (row >= 0 && column == 0) {
						comp.setBackground(comp2.getBackground());
						if (comp instanceof JComponent
								&& comp2 instanceof JComponent) {
							((JComponent) comp).setBorder(((JComponent) comp2)
									.getBorder());
						}
						if (isSelected) {
							comp.setBackground(Color.GRAY);
							comp.setForeground(Color.WHITE);
						} else {
							comp.setBackground(comp2.getBackground());
							comp.setForeground(comp2.getForeground());
						}
					} else {

						if (isSelected) {
							comp.setBackground(comp3.getBackground());
							comp.setForeground(comp3.getForeground());
						} else {
							comp.setBackground(Color.WHITE);
							comp.setForeground(Color.BLACK);
						}
					}
				}
			}
		}
		return comp;
	}

}
