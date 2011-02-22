package fr.soleil.bensikin.containers.sub.dialogs;

import java.util.List;

import fr.soleil.comete.dao.AbstractDAO;
import fr.soleil.comete.util.DataArray;

/**
 * DAO class for view spectrum management
 * @author awo
 *
 */
public class SpectrumDAO extends AbstractDAO<List<DataArray>> {

    /**
     * The data to display
     */
    private List<DataArray> data;

    /**
     * Constructor
     * @param data
     */
    public SpectrumDAO(List<DataArray> data) {
        this.data = data;
    }

    @Override
    public List<DataArray> getData() {
        return data;
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public boolean getReadOnly() {
        return true;
    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public void setData(List<DataArray> value) {
        // Nothing to do
    }
}
