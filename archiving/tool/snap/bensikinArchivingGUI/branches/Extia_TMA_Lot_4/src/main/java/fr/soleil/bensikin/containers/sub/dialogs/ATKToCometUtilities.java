package fr.soleil.bensikin.containers.sub.dialogs;

import java.util.ArrayList;
import java.util.List;

import fr.esrf.tangoatk.widget.util.chart.DataList;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.comete.util.DataArray;

/**
 * Temporary class for the migration from ATK to Comet. IT MUST BE MERGED WITH
 * ATKToCometUtilities CLASS IN MAMBO, AND MUST BE PUT IN COMMONARCHIVINGAPI
 * 
 * @author awo
 * 
 */
public class ATKToCometUtilities {

    /**
     * Creates a new SpectrumDAO instance populated from a DataView array Used
     * in Bensikin
     * 
     * @param readView
     *            ATK readView array
     * @param writeView
     *            ATK writeView array
     * @return the new Spectrum DAO instance
     */
    public static SpectrumDAO createSpectrumDAO(JLDataView spectrumView) {

        List<DataArray> dataArrayList = new ArrayList<DataArray>();

        if (spectrumView != null) {
            DataArray dataArray = new DataArray();
            dataArray.setName(spectrumView.getName());
            DataList dl = spectrumView.getData();
            DataList current = dl;
            while (current != null) {
                dataArray.add(current.x, current.y);
                current = current.next;
            }
            dataArrayList.add(dataArray);
        }
        return new SpectrumDAO(dataArrayList);
    }
}
