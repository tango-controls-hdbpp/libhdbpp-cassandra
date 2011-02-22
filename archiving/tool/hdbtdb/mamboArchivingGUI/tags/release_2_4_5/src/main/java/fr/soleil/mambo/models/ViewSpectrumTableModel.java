/*
 * Synchrotron Soleil File : ViewSpectrumTableModel.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 31 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumTableModel.java,v
 */
package fr.soleil.mambo.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.tools.Messages;

/**
 * @author SOLEIL
 */
public class ViewSpectrumTableModel extends DefaultTableModel {

    private static final long serialVersionUID = -2590232244788145159L;
    public final static int R = 0;
    public final static int W = 1;
    public final static int RW = 2;
    public final static int UNKNOWN = 3;

    private boolean[] selectedRead;
    private boolean[] selectedWrite;
    private int viewSpectrumType;
    private String[] readNames;
    private String[] writeNames;

    public ViewSpectrumTableModel(String[] readNames, String[] writeNames) {
        super();
        this.readNames = readNames;
        this.writeNames = writeNames;
        int readLength = Math.max(0, computeReadLength());
        int writeLength = Math.max(0, computeWriteLength());
        selectedRead = new boolean[readLength];
        selectedWrite = new boolean[writeLength];
        for (int i = 0; i < selectedRead.length; i++) {
            selectedRead[i] = true;
        }
    }

    private int computeReadLength() {
        if (readNames == null) {
            return -1;
        }
        else {
            return readNames.length;
        }
    }

    private int computeWriteLength() {
        if (writeNames == null) {
            return -1;
        }
        else {
            return writeNames.length;
        }
    }

    @Override
    public int getRowCount() {
        int readLength = Math.max(0, computeReadLength());
        int writeLength = Math.max(0, computeWriteLength());
        return Math.max(readLength, writeLength);
    }

    public int getSelectedReadRowCount() {
        int count = selectedRead.length;
        for (int i = 0; i < selectedRead.length; i++) {
            if (!selectedRead[i]) {
                count--;
            }
        }
        return count;
    }

    public int getSelectedWriteRowCount() {
        int count = selectedWrite.length;
        for (int i = 0; i < selectedWrite.length; i++) {
            if (!selectedWrite[i]) {
                count--;
            }
        }
        return count;
    }

    @Override
    public int getColumnCount() {
        if ((computeReadLength() > -1) && (computeWriteLength() > -1)) {
            return 4;
        }
        return 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (aValue instanceof Boolean) {
            if (columnIndex == 0) {
                if (selectedRead.length > 0) {
                    selectedRead[rowIndex] = ((Boolean) aValue).booleanValue();
                }
                else {
                    selectedWrite[rowIndex] = ((Boolean) aValue).booleanValue();
                }
            }
            else if (columnIndex == 3) {
                selectedWrite[rowIndex] = ((Boolean) aValue).booleanValue();
            }
            fireTableDataChanged();
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                if (selectedRead.length > 0) {
                    if (rowIndex < selectedRead.length) {
                        return new Boolean(selectedRead[rowIndex]);
                    }
                }
                else if (selectedWrite.length > 0) {
                    if (rowIndex < selectedWrite.length) {
                        return new Boolean(selectedWrite[rowIndex]);
                    }
                }
                return "";
            case 1:
                if (selectedRead.length > 0) {
                    if (rowIndex < readNames.length) {
                        return readNames[rowIndex];
                    }
                }
                else if (selectedWrite.length > 0) {
                    if (rowIndex < writeNames.length) {
                        return writeNames[rowIndex];
                    }
                }
                return Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA");
            case 2:
                if (rowIndex < writeNames.length) {
                    return writeNames[rowIndex];
                }
                return Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA");
            case 3:
                if (rowIndex < selectedWrite.length) {
                    return new Boolean(selectedWrite[rowIndex]);
                }
                return "";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            if (selectedRead.length > 0) {
                if (rowIndex < selectedRead.length) {
                    return true;
                }
            }
            else if (selectedWrite.length > 0) {
                if (rowIndex < selectedWrite.length) {
                    return true;
                }
            }
            return false;
        }
        if (columnIndex == 3) {
            if (rowIndex < selectedWrite.length) {
                return true;
            }
            return false;
        }
        return false;
    }

    public String getColumnName(int column) {
        String ret = "";
        switch (column) {
            case 0:
                ret = Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT");
                ret = appendWritableToString(ret);
                break;
            case 1:
                if (viewSpectrumType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX) {
                    ret = Messages.getMessage("VIEW_SPECTRUM_SUBPART");
                }
                else if (viewSpectrumType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME) {
                    ret = Messages.getMessage("VIEW_SPECTRUM_SUBPART_TIME");
                }
                ret = appendWritableToString(ret);
                break;
            case 2:
                if (viewSpectrumType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX) {
                    ret = Messages.getMessage("VIEW_SPECTRUM_SUBPART");
                }
                else if (viewSpectrumType == ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME) {
                    ret = Messages.getMessage("VIEW_SPECTRUM_SUBPART_TIME");
                }
                ret += " " + Messages.getMessage("VIEW_SPECTRUM_WRITE");
                break;
            case 3:
                ret = Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT") + " "
                        + Messages.getMessage("VIEW_SPECTRUM_WRITE");
                break;
        }
        return ret;
    }

    private String appendWritableToString(String ret) {
        if (selectedRead.length > 0) {
            ret += " " + Messages.getMessage("VIEW_SPECTRUM_READ");
        }
        else if (selectedWrite.length > 0) {
            ret += " " + Messages.getMessage("VIEW_SPECTRUM_WRITE");
        }
        return ret;
    }

    public int getReadLength() {
        return selectedRead.length;
    }

    public int getWriteLength() {
        return selectedWrite.length;
    }

    public int getViewSpectrumType() {
        return viewSpectrumType;
    }

    public void setViewSpectrumType(int viewSpectrumType) {
        this.viewSpectrumType = viewSpectrumType;
    }

    public List<Integer> getSelectedRead() {
        return getSelected(selectedRead);
    }

    public List<Integer> getSelectedWrite() {
        return getSelected(selectedWrite);
    }

    private List<Integer> getSelected(boolean[] ref) {
        ArrayList<Integer> result = null;
        if (ref != null) {
            result = new ArrayList<Integer>();
            boolean allTrue = true;
            for (int i = 0; i < ref.length; i++) {
                if (ref[i]) {
                    result.add(i);
                }
                else {
                    allTrue = false;
                }
            }
            if (allTrue) {
                result.clear();
                result = null;
            }
        }
        return result;
    }

}
