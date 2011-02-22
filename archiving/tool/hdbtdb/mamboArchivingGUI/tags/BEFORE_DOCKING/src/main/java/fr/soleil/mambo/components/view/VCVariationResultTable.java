// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/VCVariationResultTable.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationResultTable.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationResultTable.java,v $
// Revision 1.5 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.4 2006/02/01 14:08:16 ounsy
// minor changes (column titles appear)
//
// Revision 1.3 2005/12/15 16:43:08 ounsy
// look and feel
//
// Revision 1.2 2005/12/15 11:19:21 ounsy
// "copy table to clipboard" management
//
// Revision 1.1 2005/11/29 18:28:12 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.view;

import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.MamboFormatableTable;
import fr.soleil.mambo.components.renderers.MamboBasicTableRenderer;
import fr.soleil.mambo.models.VCVariationResultTableModel;
import fr.soleil.mambo.options.Options;

public class VCVariationResultTable extends MamboFormatableTable {

    private static final long serialVersionUID = 5044988978516395461L;

    public VCVariationResultTable(ViewConfigurationBean viewConfigurationBean) {
        super(new VCVariationResultTableModel(viewConfigurationBean));
        setAutoCreateColumnsFromModel(true);
        autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;
        Enumeration<TableColumn> columns = getColumnModel().getColumns();
        int i = 0;
        while (columns.hasMoreElements()) {
            int margin = 10;
            if (i == 0) {
                margin = 50;
            }
            TableColumn column = columns.nextElement();
            JTextField textField = new JTextField(""
                    + getModel().getValueAt(0, i));
            int width = textField.getPreferredSize().width;
            if (column.getMaxWidth() < width + margin) {
                column.setMaxWidth(width + margin);
            }
            if (column.getWidth() < width + margin) {
                column.setWidth(width + margin);
            }
            if (column.getMinWidth() < width + margin) {
                column.setMinWidth(width + margin);
            }
            i++;
        }
        this.setDefaultRenderer(Object.class, new MamboBasicTableRenderer());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public boolean isColumnSelected(int column) {
        return false;
    }

    @Override
    public String toString() {
        String value = "";
        for (int i = 0; i < getModel().getColumnCount(); i++) {
            value += getModel().getColumnName(i)
                    + Options.getInstance().getGeneralOptions().getSeparator();
        }
        value = value.substring(0, value.lastIndexOf(Options.getInstance()
                .getGeneralOptions().getSeparator()));
        value += "\n";
        value += super.toString();
        return value;
    }

    @Override
    public VCVariationResultTableModel getModel() {
        return (VCVariationResultTableModel) super.getModel();
    }

    @Override
    public void setModel(TableModel dataModel) {
        if (dataModel instanceof VCVariationResultTableModel) {
            super.setModel(dataModel);
        }
    }

}
