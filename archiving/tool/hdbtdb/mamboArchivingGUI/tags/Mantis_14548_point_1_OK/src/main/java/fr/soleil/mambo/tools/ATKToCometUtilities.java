package fr.soleil.mambo.tools;

import java.util.ArrayList;
import java.util.List;

import fr.esrf.tangoatk.widget.util.chart.DataList;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.comete.util.DataArray;
import fr.soleil.mambo.datasources.dao.SpectrumDAO;

/**
 * Temporary class for the migration from ATK to Comet
 * 
 * @author awo
 * 
 */
public class ATKToCometUtilities {

    /**
     * Creates a new SpectrumDAO instance populated from DataView arrays
     * @param readView ATK readView array
     * @param writeView ATK writeView array
     * @return the new Spectrum DAO instance
     */
    public static SpectrumDAO createSpectrumDAO(JLDataView[] readView,
            JLDataView[] writeView) {

        List<DataArray> dataArrayList = new ArrayList<DataArray>();

        if (readView != null) {
            for (int i=0 ; i<readView.length ; i++) {
                JLDataView dv = readView[i];
                DataArray dataArray = new DataArray();
                dataArray.setName("Courbe read"+i);
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
            for (int i=0 ; i<writeView.length ; i++) {
                JLDataView dv = readView[i];
                DataArray dataArray = new DataArray();
                dataArray.setName("Courbe write"+i);
                DataList dl = dv.getData();
                DataList current = dl;
                while (current != null) {
                    dataArray.add(current.x, current.y);
                    current = current.next;
                }
                dataArrayList.add(dataArray);
            }
        }

        return new SpectrumDAO(dataArrayList);
    }
}
