package fr.soleil.mambo.data.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.soleil.comete.dao.AbstractActionDAO;
import fr.soleil.comete.dao.AbstractDAO;
import fr.soleil.comete.dao.AbstractDAOFactory;
import fr.soleil.comete.dao.AbstractKey;
import fr.soleil.comete.util.BooleanMatrix;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.util.NumberMatrix;
import fr.soleil.comete.util.StackDataArray;
import fr.soleil.comete.util.StackNumberMatrix;
import fr.soleil.comete.util.StringMatrix;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.models.ViewSpectrumTableModel;

public class MamboViewDAOFactory extends AbstractDAOFactory {
    private Map<String, ViewScalarDAO> viewSelectedDAO = new HashMap<String, ViewScalarDAO>();

    @Override
    public AbstractDAO<Boolean> createBooleanDAO(AbstractKey key) {
        return null;
    }

    @Override
    public AbstractDAO<BooleanMatrix> createBooleanImageDAO(AbstractKey key) {
        return null;
    }

    @Override
    public AbstractDAO<Number> createNumberDAO(AbstractKey key) {
        return null;
    }

    @Override
    public AbstractDAO<List<DataArray>> createNumberDataArrayDAO(AbstractKey key) {
        if (key != null) {
            Object typeData = key.getPropertyValue("dataType");
            if (typeData != null) {
                if (typeData instanceof Integer) {
                    switch ((Integer) typeData) {
                        case ViewDAOKey.TYPE_BOOLEAN_SPECTRUM:
                        case ViewDAOKey.TYPE_NUMBER_SPECTRUM:
                        case ViewDAOKey.TYPE_STATE_SPECTRUM:
                        case ViewDAOKey.TYPE_STRING_SPECTRUM:
                            return createNumberDataArrayDAOForSpectrum(key);
                        case ViewDAOKey.TYPE_BOOLEAN_SCALAR:
                        case ViewDAOKey.TYPE_NUMBER_SCALAR:
                        case ViewDAOKey.TYPE_STATE_SCALAR:
                        case ViewDAOKey.TYPE_STRING_SCALAR:
                            return createNumberDataArrayDAOForScalar(key);
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public AbstractDAO<NumberMatrix> createNumberImageDAO(AbstractKey key) {
        // Ready to comete 0.1.0-SNAPSHOT
        // if (key != null) {
        // Object dao = key.getPropertyValue("dao");
        // if (dao != null) {
        // if (dao instanceof DefaultMatrixDAO) {
        // return (DefaultMatrixDAO) dao;
        // }
        // }
        // }
        return null;
    }

    @Override
    public AbstractDAO<StackDataArray> createStackNumberDataArrayDAO(
            AbstractKey key) {
        if (key != null) {
            Object splitedData = key.getPropertyValue("splitedData");
            if (splitedData != null) {
                if (splitedData instanceof DbData[]) {
                    Object checkBoxSelected = key
                            .getPropertyValue("checkBoxSelected");
                    if (checkBoxSelected != null) {
                        if (checkBoxSelected instanceof Integer) {
                            ViewSpectrumStackDAO dao = new ViewSpectrumStackDAO(
                                    (DbData[]) splitedData,
                                    (Integer) checkBoxSelected);
                            return dao;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public AbstractDAO<StackNumberMatrix> createStackNumberImageDAO(
            AbstractKey key) {
        return null;
    }

    @Override
    public AbstractActionDAO<String> createStringActionDAO(AbstractKey key) {
        return null;
    }

    @Override
    public AbstractDAO<String> createStringDAO(AbstractKey key) {
        return null;
    }

    @Override
    public AbstractDAO<StringMatrix> createStringImageDAO(AbstractKey key) {
        return null;
    }

    @Override
    public void initBehaviorList() {

    }

    @Override
    public String initFactoryName() {
        return null;
    }

    private AbstractDAO<List<DataArray>> createNumberDataArrayDAOForSpectrum(
            AbstractKey key) {
        Object spectrumType = key.getPropertyValue("spectrumType");
        if (spectrumType != null) {
            if (spectrumType instanceof Integer) {
                Object model = key.getPropertyValue("model");
                if (model != null) {
                    if (model instanceof ViewSpectrumTableModel) {
                        Object splitedData = key
                                .getPropertyValue("splitedData");
                        if (splitedData != null) {
                            if (splitedData instanceof DbData[]) {
                                ViewSpectrumDAO dao = new ViewSpectrumDAO(
                                        (DbData[]) splitedData,
                                        (ViewSpectrumTableModel) model,
                                        (Integer) spectrumType);
                                return dao;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private AbstractDAO<List<DataArray>> createNumberDataArrayDAOForScalar(
            AbstractKey key) {
        Object idViewSelected = key.getPropertyValue("viewSelectedId");
        if (idViewSelected != null) {
            if (idViewSelected instanceof String) {
                return getDAOScalar((String) idViewSelected);
            }
        }
        return null;
    }

    public ViewScalarDAO getDAOScalar(String idViewSelected) {
        if (!viewSelectedDAO.containsKey((String) idViewSelected)) {
            viewSelectedDAO.put((String) idViewSelected, new ViewScalarDAO());
        }
        return viewSelectedDAO.get((String) idViewSelected);
    }

    public void removeKeyForDaoScalar(String idViewSelected) {
        viewSelectedDAO.remove(idViewSelected);
    }
}
