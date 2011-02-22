package fr.soleil.mambo.tools;

import java.util.ArrayList;
import java.util.List;

import fr.esrf.tangoatk.widget.util.chart.DataList;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.comete.dao.util.DefaultDataArrayDAO;
import fr.soleil.comete.dao.util.DefaultStackDataArrayDAO;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.util.StackDataArray;

/**
 * Temporary class for the migration from ATK to Comet
 * 
 * @author awo
 * 
 */
public class ATKToCometUtilities {

    /**
     * Creates a new DefaultDataArrayDAO instance populated from DataView arrays
     * 
     * @param readView
     *            ATK readView array
     * @param writeView
     *            ATK writeView array
     * @return the new Spectrum DAO instance
     */
    public static DefaultDataArrayDAO createSpectrumDAO(JLDataView[] readView,
            JLDataView[] writeView) {

        List<DataArray> dataArrayList = new ArrayList<DataArray>();

        if (readView != null) {
            for (int i = 0; i < readView.length; i++) {
                JLDataView dv = readView[i];
                DataArray dataArray = new DataArray();
                dataArray.setName(dv.getName());
                DataList dl = dv.getData();
                DataList current = dl;
                while (current != null) {
                    dataArray.add(current.x, current.y);
                    current = current.next;
                }
                dataArrayList.add(dataArray);
            }
        }

        if (writeView != null) {
            for (int i = 0; i < writeView.length; i++) {
                JLDataView dv = writeView[i];
                DataArray dataArray = new DataArray();
                dataArray.setName(dv.getName());
                DataList dl = dv.getData();
                DataList current = dl;
                while (current != null) {
                    dataArray.add(current.x, current.y);
                    current = current.next;
                }
                dataArrayList.add(dataArray);
            }
        }

        DefaultDataArrayDAO dao = new DefaultDataArrayDAO();
        dao.setData(dataArrayList);

        return dao;
    }

    /**
     * Creates a new DefaultStackDataArrayDAO instance populated from DataView
     * arrays
     * 
     * @param readView
     *            ATK readView array
     * @param writeView
     *            ATK writeView array
     * @return the new Spectrum DAO instance
     */
    public static DefaultStackDataArrayDAO createSpectrumStackDAO(
            JLDataView[] readView, JLDataView[] writeView) {

        int readViewSize = 0;
        int writeViewSize = 0;
        if (readView != null) {
            readViewSize = readView.length;
        } else if (writeView != null) {
            writeViewSize = writeView.length;
        }

        StackDataArray data = new StackDataArray();

        for (int i = 0; i < Math.max(readViewSize, writeViewSize); i++) {
            ArrayList<DataArray> dataArrayList = new ArrayList<DataArray>();

            if (readView != null) {
                if (readView[i] != null) {
                    JLDataView dvRead = readView[i];
                    DataArray dataArrayRead = new DataArray();
                    dataArrayRead.setName(dvRead.getName());
                    DataList dlRead = dvRead.getData();
                    DataList currentRead = dlRead;
                    while (currentRead != null) {
                        dataArrayRead.add(currentRead.x, currentRead.y);
                        currentRead = currentRead.next;
                    }
                    dataArrayList.add(dataArrayRead);
                }
            }

            if (writeView != null) {
                if (writeView[i] != null) {
                    JLDataView dvWrite = writeView[i];
                    DataArray dataArrayWrite = new DataArray();
                    dataArrayWrite.setName(dvWrite.getName());
                    DataList dlWrite = dvWrite.getData();
                    DataList currentWrite = dlWrite;
                    while (currentWrite != null) {
                        dataArrayWrite.add(currentWrite.x, currentWrite.y);
                        currentWrite = currentWrite.next;
                    }
                    dataArrayList.add(dataArrayWrite);
                }
            }

            data.add(dataArrayList);
        }

        DefaultStackDataArrayDAO dao = new DefaultStackDataArrayDAO();
        dao.setData(data);
        return dao;
    }
}
