package fr.soleil.mambo.data.view;

import java.util.ArrayList;
import java.util.List;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.comete.dao.AbstractDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewSpectrumTableModel;

public class ViewSpectrumDAO extends AbstractDAO<List<DataArray>> {
    private List<DataArray>        arrayList = new ArrayList<DataArray>();
    private ViewSpectrumTableModel model;

    public ViewSpectrumDAO(DbData[] splitedData, ViewSpectrumTableModel model,
            int spectrumViewType) {
        this.model = model;
        if (splitedData != null) {
            int dataType = TangoConst.Tango_DEV_VOID;
            int readBooleanColumn = -1;
            int readNameColumn = -1;
            int writeBooleanColumn = -1;
            int writeNameColumn = -1;
            if (splitedData[0] == null) {
                dataType = splitedData[1].getData_type();
                writeBooleanColumn = 0;
                writeNameColumn = 1;
            } else {
                dataType = splitedData[0].getData_type();
                readBooleanColumn = 0;
                readNameColumn = 1;
                writeBooleanColumn = 3;
                writeNameColumn = 2;
            }
            switch (dataType) {
                case TangoConst.Tango_DEV_SHORT:
                case TangoConst.Tango_DEV_USHORT:
                case TangoConst.Tango_DEV_LONG:
                case TangoConst.Tango_DEV_ULONG:
                case TangoConst.Tango_DEV_FLOAT:
                case TangoConst.Tango_DEV_DOUBLE:

                    for (int row = 0; row < model.getRowCount(); row++) {
                        IExtractingManager extractingManager = ExtractingManagerFactory
                                .getCurrentImpl();
                        if (extractingManager != null) {
                            if (extractingManager.isShowRead()) {
                                DataArray readDataArray = extractDataArray(row,
                                        readNameColumn, readBooleanColumn,
                                        spectrumViewType, splitedData[0]);
                                if (readDataArray != null) {
                                    arrayList.add(readDataArray);
                                }
                                readDataArray = null;
                            }

                            if (extractingManager.isShowWrite()) {
                                DataArray writeDataArray = extractDataArray(
                                        row, writeNameColumn,
                                        writeBooleanColumn, spectrumViewType,
                                        splitedData[1]);
                                if (writeDataArray != null) {
                                    arrayList.add(writeDataArray);
                                }
                                writeDataArray = null;
                            }
                        }
                    }
            }
        }
    }

    private DataArray extractDataArray(int row, int nameColumn,
            int booleanColumn, int viewType, DbData data) {
        if ((data != null) && (data.getData_timed() != null) && (row > -1)
                && (row < model.getRowCount()) && (nameColumn > -1)
                && (nameColumn < model.getColumnCount())
                && (booleanColumn > -1)
                && (booleanColumn < model.getColumnCount())) {
            DataArray dataArray = null;
            Boolean selected = null;
            if (model.getValueAt(row, booleanColumn) instanceof Boolean) {
                selected = (Boolean) model.getValueAt(row, booleanColumn);
            }
            if (selected == null) {
                selected = Boolean.valueOf(false);
            }
            if (selected.booleanValue()) {
                switch (viewType) {
                    case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX:
                        // Index spectrum : each DataArray is the history of an
                        // index in the spectrum
                        dataArray = new DataArray();
                        dataArray.setId("" + model.getValueAt(row, nameColumn));
                        for (int timeIndex = 0; timeIndex < data
                                .getData_timed().length; timeIndex++) {
                            if ((data.getData_timed()[timeIndex].value != null)
                                    && row < data.getData_timed()[timeIndex].value.length) {
                                dataArray
                                        .add(
                                                data.getData_timed()[timeIndex].time,
                                                (Number) data.getData_timed()[timeIndex].value[row]);
                            }
                        }
                        break;
                    case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME:
                    case ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_TIME_STACK:
                        // Time spectrum : each DataArray is a full spectrum at
                        // a particular time
                        dataArray = new DataArray();
                        dataArray.setId("" + model.getValueAt(row, nameColumn));
                        if (row < data.getData_timed().length) {
                            for (int index = 0; index < data.getMax_x(); index++) {
                                Number y;
                                if (data.getData_timed()[row].value != null
                                        && index < data.getData_timed()[row].value.length
                                        && data.getData_timed()[row].value[index] != null) {
                                    y = (Number) data.getData_timed()[row].value[index];
                                } else {
                                    y = Double.valueOf(Double.NaN);
                                }
                                dataArray.add(Integer.valueOf(index), y);
                                y = null;
                            }
                        }
                        break;
                    default:
                        // Invalid view type
                        dataArray = null;
                        break;
                }
            } else {
                // Value is not selected
                dataArray = null;
            }
            return dataArray;
        } else {
            // Invalid parameters
            return null;
        }
    }

    @Override
    public List<DataArray> getData() {
        return arrayList;
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public boolean getReadOnly() {
        return false;
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public void setData(List<DataArray> value) {
        this.arrayList = value;
    }

    public void addData(DataArray value) {
        this.arrayList.add(value);
    }

}
