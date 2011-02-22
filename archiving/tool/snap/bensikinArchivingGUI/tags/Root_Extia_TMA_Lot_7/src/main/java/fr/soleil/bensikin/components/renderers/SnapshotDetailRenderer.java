// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/renderers/SnapshotDetailRenderer.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotDetailRenderer.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.15 $
//
// $Log: SnapshotDetailRenderer.java,v $
// Revision 1.15 2007/03/26 08:07:53 ounsy
// *** empty log message ***
//
// Revision 1.14 2006/10/31 16:54:08 ounsy
// milliseconds and null values management
//
// Revision 1.13 2006/07/24 07:38:59 ounsy
// better image support
//
// Revision 1.12 2006/06/28 12:46:41 ounsy
// image support
//
// Revision 1.11 2006/06/16 08:52:57 ounsy
// ready for images
//
// Revision 1.10 2006/03/20 15:51:04 ounsy
// added the case of Snapshot delta value for spectrums which
// read and write parts are the same length.
//
// Revision 1.9 2006/03/16 15:34:32 ounsy
// State scalar support
//
// Revision 1.8 2006/03/15 15:08:07 ounsy
// renders boolean scalars
//
// Revision 1.7 2006/02/23 10:04:52 ounsy
// notice when modified
//
// Revision 1.6 2006/02/15 09:16:26 ounsy
// spectrums rw management
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:35 chinkumo
// First commit
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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.components.snapshot.ImageButton;
import fr.soleil.bensikin.components.snapshot.ImageWriteButton;
import fr.soleil.bensikin.components.snapshot.SpectrumButton;
import fr.soleil.bensikin.components.snapshot.SpectrumDeltaValueButton;
import fr.soleil.bensikin.components.snapshot.SpectrumWriteValueButton;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeDeltaValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeReadValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeWriteValue;
import fr.soleil.bensikin.models.SnapshotDetailTableModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoStateTranslation;

/**
 * A cell renderer used for SnapshotDetailTable. It paints cells containing "Not
 * applicable" values in grey, and modified values in red. In all other cases,
 * it paints the cell with the default component.
 * 
 * @author CLAISSE
 */
public class SnapshotDetailRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 4496085584938218688L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
     * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof SnapshotAttributeValue) {
            if (((SnapshotAttributeValue) value).isNotApplicable()) {
                JPanel panel = new JPanel();
                if (isSelected) {
                    panel.setBackground(Color.DARK_GRAY);
                } else {
                    panel.setBackground(Color.GRAY);
                }
                return panel;
            }

            int dataFormat = ((SnapshotAttributeValue) value).getDataFormat();
            switch (dataFormat) {
                case SnapshotAttributeValue.SCALAR_DATA_FORMAT:

                    Component ret;
                    if (((SnapshotAttributeValue) value).getScalarValue() == null) {
                        ret = new JLabel(Messages
                                .getMessage("SNAPSHOT_DETAIL_NO_DATA"));
                        ((JLabel) ret).setBackground(Color.WHITE);
                        ((JLabel) ret).setOpaque(true);
                    } else if (((SnapshotAttributeValue) value).getDataType() == TangoConst.Tango_DEV_BOOLEAN
                            || ((SnapshotAttributeValue) value).getDataType() == TangoConst.Tango_DEV_STATE) {
                        ret = new BensikinTableCellRenderer()
                                .getTableCellRendererComponent(table,
                                        (SnapshotAttributeValue) value,
                                        isSelected, hasFocus, row, column);
                    } else {
                        ret = new BensikinTableCellRenderer()
                                .getTableCellRendererComponent(table,
                                        ((SnapshotAttributeValue) value)
                                                .toFormatedString(),
                                        isSelected, hasFocus, row, column);
                    }

                    if (((SnapshotAttributeValue) value).isModified()) {
                        ret.setBackground(Color.RED);
                    }
                    return ret;

                case SnapshotAttributeValue.SPECTRUM_DATA_FORMAT:

                    JComponent ret2 = (JComponent) new BensikinTableCellRenderer()
                            .getTableCellRendererComponent(table,
                                    ((SnapshotAttributeValue) value)
                                            .toFormatedString(), isSelected,
                                    hasFocus, row, column);

                    String name = "";
                    if (table.getColumnName(column).equals(
                            Messages.getMessage("SNAPSHOT_DETAIL_COLUMNS_R"))) {
                        name += Messages
                                .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_READ");
                    } else if (table.getColumnName(column).equals(
                            Messages.getMessage("SNAPSHOT_DETAIL_COLUMNS_W"))) {
                        name += Messages
                                .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_WRITE");
                    } else if (table
                            .getColumnName(column)
                            .equals(
                                    Messages
                                            .getMessage("SNAPSHOT_DETAIL_COLUMNS_DELTA"))) {
                        name += Messages
                                .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_DELTA");
                    }

                    JLabel button = null;
                    SnapshotDetailTableModel model = null;
                    if (table != null
                            && table.getModel() instanceof SnapshotDetailTableModel) {
                        model = (SnapshotDetailTableModel) table.getModel();
                    }
                    if (value instanceof SnapshotAttributeWriteValue) {
                        button = new SpectrumWriteValueButton(table.getValueAt(
                                row, 0).toString()
                                + " " + name, (SnapshotAttributeValue) value,
                                table, row, column);
                    } else if (value instanceof SnapshotAttributeReadValue) {
                        button = new SpectrumButton(table.getValueAt(row, 0)
                                .toString()
                                + " " + name, row, column, model);
                    } else if (value instanceof SnapshotAttributeDeltaValue) {
                        button = new SpectrumDeltaValueButton(table.getValueAt(
                                row, 0).toString()
                                + " " + name, (SnapshotAttributeValue) value,
                                table, row, column);
                    }

                    Border border = new JLabel().getBorder();
                    if (isSelected) {
                        button.setOpaque(true);
                        button.setBackground(ret2.getBackground());
                        button.setBorder(ret2.getBorder());
                    } else {
                        button.setOpaque(false);
                        button.setBorder(border);
                    }

                    if (((SnapshotAttributeValue) value).isModified()) {
                        button.setOpaque(true);
                        button.setBackground(Color.RED);
                    }
                    return button;

                case SnapshotAttributeValue.IMAGE_DATA_FORMAT:
                    JComponent ret3 = (JComponent) new BensikinTableCellRenderer()
                            .getTableCellRendererComponent(table,
                                    ((SnapshotAttributeValue) value)
                                            .toFormatedString(), isSelected,
                                    hasFocus, row, column);
                    JLabel imageButton = null;
                    if (value instanceof SnapshotAttributeWriteValue) {
                        imageButton = new ImageWriteButton(table, row, table
                                .getModel().getValueAt(row, 0).toString(),
                                ((SnapshotAttributeWriteValue) value)
                                        .getImageValue(),
                                ((SnapshotAttributeWriteValue) value)
                                        .getDataType(),
                                ((SnapshotAttributeWriteValue) value)
                                        .getDisplayFormat());
                        Border border2 = new JLabel().getBorder();
                        if (isSelected) {
                            imageButton.setOpaque(true);
                            imageButton.setBackground(ret3.getBackground());
                            imageButton.setBorder(ret3.getBorder());
                        } else {
                            imageButton.setOpaque(false);
                            imageButton.setBorder(border2);
                        }
                        return imageButton;
                    } else {
                        imageButton = new ImageButton(table.getModel()
                                .getValueAt(row, 0).toString(),
                                ((SnapshotAttributeReadValue) value)
                                        .getImageValue(),
                                ((SnapshotAttributeReadValue) value)
                                        .getDataType(),
                                ((SnapshotAttributeReadValue) value)
                                        .getDisplayFormat());
                        Border border2 = new JLabel().getBorder();
                        if (isSelected) {
                            imageButton.setOpaque(true);
                            imageButton.setBackground(ret3.getBackground());
                            imageButton.setBorder(ret3.getBorder());
                        } else {
                            imageButton.setOpaque(false);
                            imageButton.setBorder(border2);
                        }
                        return imageButton;
                    }

                default:
                    return null;
            }
        } else {
            return new BensikinTableCellRenderer()
                    .getTableCellRendererComponent(table, value, isSelected,
                            hasFocus, row, column);
        }
    }

    public static String getTextFor(SnapshotAttributeValue value) {
        if (value.isNotApplicable()) {
            return "";
        }
        int dataFormat = value.getDataFormat();
        switch (dataFormat) {
            case SnapshotAttributeValue.SCALAR_DATA_FORMAT:
                if (value.getScalarValue() == null) {
                    return Messages.getMessage("SNAPSHOT_DETAIL_NO_DATA");
                } else if (value.getDataType() == TangoConst.Tango_DEV_STATE) {
                    Integer valS = (Integer) value.getScalarValue();
                    return TangoStateTranslation.getStrStateValue(valS
                            .intValue());
                } else if (value.getDataType() == TangoConst.Tango_DEV_BOOLEAN) {
                    return value.getScalarValue().toString();
                } else {
                    return value.toFormatedString();
                }
            case SnapshotAttributeValue.SPECTRUM_DATA_FORMAT:
                if (value instanceof SnapshotAttributeReadValue) {
                    return Messages
                            .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_READ");
                } else if (value instanceof SnapshotAttributeWriteValue) {
                    return Messages
                            .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_WRITE");
                } else if (value instanceof SnapshotAttributeDeltaValue) {
                    return Messages
                            .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_DELTA");
                }
            case SnapshotAttributeValue.IMAGE_DATA_FORMAT:
                if (value instanceof SnapshotAttributeWriteValue) {
                    return Messages.getMessage("DIALOGS_IMAGE_ATTRIBUTE_VIEW");
                } else {
                    return Messages.getMessage("DIALOGS_IMAGE_ATTRIBUTE_VIEW");
                }
            default:
                return "";
        }
    }

}
