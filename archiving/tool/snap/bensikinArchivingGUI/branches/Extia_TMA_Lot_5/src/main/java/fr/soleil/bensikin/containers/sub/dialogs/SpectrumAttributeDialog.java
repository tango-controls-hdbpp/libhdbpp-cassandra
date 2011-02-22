// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/SpectrumAttributeDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SpectrumAttributeDialog.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.11 $
//
// $Log: SpectrumAttributeDialog.java,v $
// Revision 1.11 2006/10/31 16:54:08 ounsy
// milliseconds and null values management
//
// Revision 1.10 2006/08/23 09:57:20 ounsy
// use of the bug correction in JLChart that allows to have a JDialog for
// "show table" instead of a JFrame
//
// Revision 1.9 2006/04/13 12:37:33 ounsy
// new spectrum types support
//
// Revision 1.8 2006/03/14 12:18:10 ounsy
// Freeze bug in case of spectrums of dim 1 corrected
//
// Revision 1.7 2006/02/23 13:33:14 ounsy
// no more freezing when the snapshot does not respond
//
// Revision 1.6 2006/02/15 09:19:10 ounsy
// spectrums rw management
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import fr.esrf.TangoDs.AttrData;
import fr.soleil.bensikin.components.renderers.BensikinTableCellRenderer;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.data.snapshot.SpectrumDAOFactory;
import fr.soleil.bensikin.models.SnapshotDetailTableModel;
import fr.soleil.bensikin.models.SpectrumReadValueTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.comete.dao.AbstractKey;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.util.CometeColor;

/**
 * Not used yet.
 * 
 * @author CLAISSE
 */
public class SpectrumAttributeDialog extends JDialog {

    private static final long serialVersionUID = -862652819588288665L;

    private Dimension         dim              = new Dimension(500, 400);

    private JPanel            myPanel;
    private ChartViewer       chart;
    private JTable            spectrumTable;

    /**
	 *
	 */
    public SpectrumAttributeDialog(String name, int row, int column,
            SnapshotDetailTableModel tableModel) {
        super(BensikinFrame.getInstance(), name, true);
        SnapshotAttributeValue value = null;
        Object temp = null;
        if (tableModel != null) {
            temp = tableModel.getValueAt(row, column);
        }
        if (temp instanceof SnapshotAttributeValue) {
            value = (SnapshotAttributeValue) temp;
        }
        if (value == null)
            return;
        myPanel = new JPanel();
        GUIUtilities.setObjectBackground(myPanel, GUIUtilities.SNAPSHOT_COLOR);

        if ((value.getDataType() == AttrData.Tango_DEV_CHAR)
                || (value.getDataType() == AttrData.Tango_DEV_UCHAR)
                || (value.getDataType() == AttrData.Tango_DEV_LONG)
                || (value.getDataType() == AttrData.Tango_DEV_ULONG)
                || (value.getDataType() == AttrData.Tango_DEV_SHORT)
                || (value.getDataType() == AttrData.Tango_DEV_USHORT)
                || (value.getDataType() == AttrData.Tango_DEV_FLOAT)
                || (value.getDataType() == AttrData.Tango_DEV_DOUBLE)) {
            chart = new ChartViewer();
            AbstractKey key = tableModel.getCometeKey(row, column);
            if (key == null) {
                chart.removeKey(SpectrumDAOFactory.class.getName());
            } else {
                chart.addKey(SpectrumDAOFactory.class.getName(), key);
            }
            chart.switchDAOFactory(SpectrumDAOFactory.class.getName());
            if (chart.getData().isEmpty()) {
                JLabel label = new JLabel(Messages
                        .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_NAN"),
                        JLabel.CENTER);
                GUIUtilities.setObjectBackground(label,
                        GUIUtilities.SNAPSHOT_COLOR);
                myPanel.add(label);
            } else {
                IChartViewer component = chart.getComponent();

                String dataName = (String) tableModel.getValueAt(row, 0);

                component.setDataViewCometeColor(dataName, CometeColor.RED);
                component.setDataViewMarkerCometeColor(dataName,
                        CometeColor.RED);
                component.setDataViewLineStyle(dataName, 1);
                component.setDataViewMarkerSize(dataName, 5);
                component.setDataViewMarkerStyle(dataName,
                        IChartViewer.MARKER_DIAMOND);
                component.setDataViewLineStyle(dataName,
                        IChartViewer.STYLE_SOLID);

                component.setAutoScale(true, IChartViewer.Y1);
                component.setAutoScale(true, IChartViewer.X);

                component
                        .setAnnotation(IChartViewer.VALUE_ANNO, IChartViewer.X);

                component.setManagementPanelVisible(false);
                component.setFreezePanelVisible(false);
                component.setPreferDialogForTable(true, true);
                component.setCometeBackground(CometeColor.getColor(GUIUtilities
                        .getSnapshotColor()));
                myPanel.add((JComponent) component);
            }

        } else {
            switch (value.getDataType()) {
                case AttrData.Tango_DEV_STATE:
                    Object valsta = value.getSpectrumValue();
                    if (!"Nan".equals(valsta) && valsta != null
                            && valsta instanceof Integer[]) {
                        Integer[] stateArray = ((Integer[]) valsta);
                        for (int i = 0; i < stateArray.length; i++) {
                            stateArray[i] = ((Integer[]) valsta)[i];
                        }
                        if (stateArray.length > 0) {
                            spectrumTable = new JTable(
                                    new SpectrumReadValueTableModel(value
                                            .getDataType(), stateArray));
                            spectrumTable.setDefaultRenderer(Object.class,
                                    new BensikinTableCellRenderer());
                        } else {
                            spectrumTable = null;
                        }

                    } else {
                        spectrumTable = null;
                    }
                    break;
                case AttrData.Tango_DEV_BOOLEAN:
                    Object valb = value.getSpectrumValue();
                    if (!"Nan".equals(valb) && valb != null
                            && valb instanceof Boolean[]) {
                        Boolean[] boolArray = ((Boolean[]) valb);
                        for (int i = 0; i < boolArray.length; i++) {
                            boolArray[i] = ((Boolean[]) valb)[i];
                        }
                        if (boolArray.length > 0) {
                            spectrumTable = new JTable(
                                    new SpectrumReadValueTableModel(value
                                            .getDataType(), boolArray));
                            spectrumTable.setDefaultRenderer(Object.class,
                                    new BensikinTableCellRenderer());
                        } else {
                            spectrumTable = null;
                        }
                    } else {
                        spectrumTable = null;
                    }
                    break;
                case AttrData.Tango_DEV_STRING:
                    Object valstr = value.getSpectrumValue();
                    if (!"Nan".equals(valstr) && valstr != null
                            && valstr instanceof String[]) {
                        String[] stringArray = ((String[]) valstr);
                        for (int i = 0; i < stringArray.length; i++) {
                            stringArray[i] = ((String[]) valstr)[i];
                        }
                        if (stringArray.length > 0) {
                            spectrumTable = new JTable(
                                    new SpectrumReadValueTableModel(value
                                            .getDataType(), stringArray));
                            spectrumTable.setDefaultRenderer(Object.class,
                                    new BensikinTableCellRenderer());
                        } else {
                            spectrumTable = null;
                        }
                    } else {
                        spectrumTable = null;
                    }
                    break;
                default:
                    spectrumTable = null;
            }
            if (spectrumTable != null) {
                GUIUtilities.setObjectBackground(spectrumTable,
                        GUIUtilities.SNAPSHOT_COLOR);
                JScrollPane scrollpane = new JScrollPane(spectrumTable);
                GUIUtilities.setObjectBackground(scrollpane,
                        GUIUtilities.SNAPSHOT_COLOR);
                GUIUtilities.setObjectBackground(scrollpane.getViewport(),
                        GUIUtilities.SNAPSHOT_COLOR);
                myPanel.add(scrollpane);
            } else {
                JLabel label = new JLabel(Messages
                        .getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_NAN"),
                        JLabel.CENTER);
                GUIUtilities.setObjectBackground(label,
                        GUIUtilities.SNAPSHOT_COLOR);
                myPanel.add(label);
            }
        }

        initLayout();
        this.setContentPane(myPanel);

        this.setSize(dim);
        this.setLocation((BensikinFrame.getInstance().getX() + BensikinFrame
                .getInstance().getWidth())
                - (this.getWidth() + 50),
                (BensikinFrame.getInstance().getY() + BensikinFrame
                        .getInstance().getHeight())
                        - (this.getHeight() + 50));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    /**
     * 15 juin 2005
     */
    private void initLayout() {
        myPanel.setLayout(new GridLayout(1, 1));
    }

}
