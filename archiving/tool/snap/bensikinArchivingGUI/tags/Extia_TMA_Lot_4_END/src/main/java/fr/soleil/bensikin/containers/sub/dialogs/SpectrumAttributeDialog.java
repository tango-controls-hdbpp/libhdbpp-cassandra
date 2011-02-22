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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import chart.temp.chart.JLAxis;
import chart.temp.chart.JLChart;
import chart.temp.chart.JLDataView;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.TangoDs.AttrData;
import fr.soleil.bensikin.components.renderers.BensikinTableCellRenderer;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.models.SpectrumReadValueTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.comete.dao.util.DefaultDataArrayDAO;
import fr.soleil.comete.util.DataArray;

/**
 * Not used yet.
 * 
 * @author CLAISSE
 */
public class SpectrumAttributeDialog extends JDialog {

    private static final long serialVersionUID = -862652819588288665L;

    private Dimension         dim              = new Dimension(500, 400);

    private JPanel            myPanel;
    private JLChart           chart;
    private DataArray         spectrumArray;
    private JTable            spectrumTable;

    /**
	 *
	 */
    public SpectrumAttributeDialog(String name, SnapshotAttributeValue value) {
        super(BensikinFrame.getInstance(), name, true);
        if (value == null)
            return;
        myPanel = new JPanel();
        GUIUtilities.setObjectBackground(myPanel, GUIUtilities.SNAPSHOT_COLOR);
        switch (value.getDataFormat()) {
            case AttrDataFormat._SPECTRUM:
                switch (value.getDataType()) {
                    case AttrData.Tango_DEV_CHAR:
                    case AttrData.Tango_DEV_UCHAR:
                        spectrumTable = null;
                        Object valc = value.getSpectrumValue();
                        if (!"Nan".equals(valc) && valc != null
                                && valc instanceof Byte[]) {
                            Byte[] byteArray = ((Byte[]) valc);
                            spectrumArray = new DataArray();
                            for (int i = 0; i < byteArray.length; i++) {
                                byteArray[i] = ((Byte[]) valc)[i];
                                if (byteArray[i] == null)
                                    spectrumArray.add(new Integer(i + 1),
                                            new Double(Double.NaN));
                                else
                                    spectrumArray.add(new Integer(i + 1),
                                            byteArray[i]);
                            }
                        } else {
                            spectrumArray = null;
                        }
                        break;
                    case AttrData.Tango_DEV_LONG:
                    case AttrData.Tango_DEV_ULONG:
                        spectrumTable = null;
                        Object vall = value.getSpectrumValue();
                        if (!"Nan".equals(vall) && vall != null
                                && vall instanceof Integer[]) {
                            Integer[] intArray = ((Integer[]) vall);
                            spectrumArray = new DataArray();
                            for (int i = 0; i < intArray.length; i++) {
                                intArray[i] = ((Integer[]) vall)[i];
                                if (intArray[i] == null)
                                    spectrumArray.add(new Integer(i + 1),
                                            new Double(Double.NaN));
                                else
                                    spectrumArray.add(new Integer(i + 1),
                                            intArray[i]);
                            }
                        } else {
                            spectrumArray = null;
                        }
                        break;
                    case AttrData.Tango_DEV_SHORT:
                    case AttrData.Tango_DEV_USHORT:
                        spectrumTable = null;
                        Object vals = value.getSpectrumValue();
                        if (!"Nan".equals(vals) && vals != null
                                && vals instanceof Short[]) {
                            Short[] shortArray = ((Short[]) vals);
                            spectrumArray = new DataArray();
                            for (int i = 0; i < shortArray.length; i++) {
                                shortArray[i] = ((Short[]) vals)[i];
                                if (shortArray[i] == null)
                                    spectrumArray.add(new Integer(i + 1),
                                            new Double(Double.NaN));
                                else
                                    spectrumArray.add(new Integer(i + 1),
                                            shortArray[i]);
                            }
                        } else {
                            spectrumArray = null;
                        }
                        break;
                    case AttrData.Tango_DEV_FLOAT:
                        spectrumTable = null;
                        Object valf = value.getSpectrumValue();
                        if (!"Nan".equals(valf) && valf != null
                                && valf instanceof Float[]) {
                            Float[] floatArray = ((Float[]) valf);
                            spectrumArray = new DataArray();
                            for (int i = 0; i < floatArray.length; i++) {
                                floatArray[i] = ((Float[]) valf)[i];
                                if (floatArray[i] == null)
                                    spectrumArray.add(new Integer(i + 1),
                                            new Double(Double.NaN));
                                else
                                    spectrumArray.add(new Integer(i + 1),
                                            floatArray[i]);
                            }
                        } else {
                            spectrumArray = null;
                        }
                        break;
                    case AttrData.Tango_DEV_DOUBLE:
                        spectrumTable = null;
                        Object vald = value.getSpectrumValue();
                        if (!"Nan".equals(vald) && vald != null
                                && vald instanceof Double[]) {
                            Double[] doubleArray = ((Double[]) vald);
                            spectrumArray = new DataArray();
                            for (int i = 0; i < doubleArray.length; i++) {
                                doubleArray[i] = ((Double[]) vald)[i];
                                if (doubleArray[i] == null)
                                    spectrumArray.add(new Integer(i + 1),
                                            new Double(Double.NaN));
                                else
                                    spectrumArray.add(new Integer(i + 1),
                                            doubleArray[i]);
                            }
                        } else {
                            spectrumArray = null;
                        }
                        break;
                    case AttrData.Tango_DEV_STRING:
                        Object valstr = value.getSpectrumValue();
                        spectrumArray = null;
                        if (!"Nan".equals(valstr) && valstr != null
                                && valstr instanceof String[]) {
                            String[] stringArray = ((String[]) valstr);
                            for (int i = 0; i < stringArray.length; i++) {
                                stringArray[i] = ((String[]) valstr)[i];
                            }
                            spectrumTable = new JTable(
                                    new SpectrumReadValueTableModel(value
                                            .getDataType(), stringArray));
                            spectrumTable.setDefaultRenderer(Object.class,
                                    new BensikinTableCellRenderer());
                        } else {
                            spectrumTable = null;
                        }
                        break;
                    case AttrData.Tango_DEV_STATE:
                        Object valsta = value.getSpectrumValue();
                        spectrumArray = null;
                        if (!"Nan".equals(valsta) && valsta != null
                                && valsta instanceof Integer[]) {
                            Integer[] stateArray = ((Integer[]) valsta);
                            for (int i = 0; i < stateArray.length; i++) {
                                stateArray[i] = ((Integer[]) valsta)[i];
                            }
                            spectrumTable = new JTable(
                                    new SpectrumReadValueTableModel(value
                                            .getDataType(), stateArray));
                            spectrumTable.setDefaultRenderer(Object.class,
                                    new BensikinTableCellRenderer());
                        } else {
                            spectrumTable = null;
                        }
                        break;
                    case AttrData.Tango_DEV_BOOLEAN:
                        Object valb = value.getSpectrumValue();
                        spectrumArray = null;
                        if (!"Nan".equals(valb) && valb != null
                                && valb instanceof Boolean[]) {
                            Boolean[] boolArray = ((Boolean[]) valb);
                            for (int i = 0; i < boolArray.length; i++) {
                                boolArray[i] = ((Boolean[]) valb)[i];
                            }
                            spectrumTable = new JTable(
                                    new SpectrumReadValueTableModel(value
                                            .getDataType(), boolArray));
                            spectrumTable.setDefaultRenderer(Object.class,
                                    new BensikinTableCellRenderer());
                        } else {
                            spectrumTable = null;
                        }
                        break;
                    default:
                        spectrumTable = null;
                        spectrumArray = null;
                }
                break;
            default:
                spectrumTable = null;
                spectrumArray = null;
        }
        if (spectrumArray != null && (!spectrumArray.isEmpty())) {
            spectrumArray.setName(name);
            // chart = new JLChart();
            chart = new JLChart();
            List<DataArray> dataArrayList = new ArrayList<DataArray>();
            dataArrayList.add(spectrumArray);
            DefaultDataArrayDAO dao = new DefaultDataArrayDAO();
            dao.setData(dataArrayList);
            chart.setDAO(dao);
            dao = null;
            dataArrayList = null;
            chart.setDataViewColor(name, Color.RED);
            chart.setDataViewMarkerColor(name, Color.RED);
            chart.setDataViewLineWidth(name, 1);
            chart.setDataViewMarkerSize(name, 5);
            chart.setDataViewMarkerStyle(name, JLDataView.MARKER_DIAMOND);
            chart.setDataViewLineStyle(name, JLDataView.TYPE_LINE);
            chart.setPreferDialogForTable(true, true);
            // chart.getY1Axis().addDataView(spectrumArray);
            // chart.getXAxis().setAutoScale(false);
            // double minTime = spectrumArray.getMinTime();
            // chart.getXAxis().setMinimum(minTime - 1);
            // double maxTime = spectrumArray.getMaxTime();
            // if (maxTime > minTime) {
            // chart.getXAxis().setMaximum(maxTime + 1);
            // } else {
            // chart.getXAxis().setMaximum(minTime + 1);
            // }
            chart.setAutoScale(true, JLChart.Y1);
            chart.setAnnotation(JLAxis.VALUE_ANNO, JLChart.X);
            GUIUtilities
                    .setObjectBackground(chart, GUIUtilities.SNAPSHOT_COLOR);
            myPanel.add(chart);
        } else if (spectrumTable != null && spectrumTable.getRowCount() > 0) {
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
            GUIUtilities
                    .setObjectBackground(label, GUIUtilities.SNAPSHOT_COLOR);
            myPanel.add(label);
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
