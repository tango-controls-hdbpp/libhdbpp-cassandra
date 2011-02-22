package fr.soleil.mambo.data.view;

import java.util.ArrayList;
import java.util.Calendar;

import fr.soleil.comete.dao.AbstractDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.util.StackDataArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.containers.view.AbstractViewSpectrumPanel;
import fr.soleil.mambo.tools.Messages;

public class ViewSpectrumStackDAO extends AbstractDAO<StackDataArray> {

    private StackDataArray arrayList = new StackDataArray();

    public ViewSpectrumStackDAO(DbData[] splitedData, int checkBoxSelected) {

        switch (checkBoxSelected) {
            case 0:
                arrayList = null;
                break;
            case 1:
                readData(splitedData);
                break;
            case 2:
                writeData(splitedData);
                break;
            case 3:
                readData(splitedData);
                writeData(splitedData);
                break;
            default:
                arrayList = null;
        }
    }

    private void readData(DbData[] splitedData) {
        if (splitedData[0] != null) {
            int readSize = splitedData[0].getData_timed().length;
            for (int i = 0; i < readSize; i++) {

                ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();
                DataArray readDataArray = extractDataArray(i, splitedData[0]);
                if (readDataArray != null) {
                    readDataArray.setId(readDataArray.getId() + " "
                            + Messages.getMessage("VIEW_SPECTRUM_READ"));
                    dataArrayList.add(readDataArray);
                }
                readDataArray = null;
                arrayList.add(dataArrayList);
                dataArrayList = null;
            }
        }

    }

    private void writeData(DbData[] splitedData) {
        int writeSize = splitedData[1].getData_timed().length;
        for (int i = 0; i < writeSize; i++) {

            ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();
            DataArray writeDataArray = extractDataArray(i, splitedData[1]);
            if (writeDataArray != null) {
                writeDataArray.setId(writeDataArray.getId() + " "
                        + Messages.getMessage("VIEW_SPECTRUM_WRITE"));
                dataArrayList.add(writeDataArray);
            }
            writeDataArray = null;
            arrayList.add(dataArrayList);
            dataArrayList = null;
        }
    }

    private DataArray extractDataArray(int timeIndex, DbData data) {
        DataArray dataArray = null;
        if ((data != null) && (data.getData_timed() != null)
                && timeIndex < data.getData_timed().length) {
            dataArray = new DataArray();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(data.getData_timed()[timeIndex].time
                    .longValue());
            dataArray.setId(AbstractViewSpectrumPanel.dateFormat
                    .format(calendar.getTime()));
            for (int index = 0; index < data.getMax_x(); index++) {
                Number y;
                if (data.getData_timed()[timeIndex].value != null
                        && index < data.getData_timed()[timeIndex].value.length
                        && data.getData_timed()[timeIndex].value[index] != null) {
                    y = (Number) data.getData_timed()[timeIndex].value[index];
                } else {
                    y = new Double(Double.NaN);
                }
                dataArray.add(new Integer(index), y);
                y = null;
            }
        }
        return dataArray;
    }

    @Override
    public StackDataArray getData() {
        // TODO Auto-generated method stub
        return arrayList;
    }

    @Override
    public String getFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setData(StackDataArray value) {
        // TODO Auto-generated method stub
        this.arrayList = value;
    }
}
