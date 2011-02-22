package fr.soleil.bensikin.data.snapshot;

import java.util.List;

import fr.soleil.comete.dao.AbstractActionDAO;
import fr.soleil.comete.dao.AbstractDAO;
import fr.soleil.comete.dao.AbstractDAOFactory;
import fr.soleil.comete.dao.AbstractKey;
import fr.soleil.comete.dao.util.DefaultDataArrayDAO;
import fr.soleil.comete.dao.util.DefaultMatrixDAO;
import fr.soleil.comete.util.BooleanMatrix;
import fr.soleil.comete.util.DataArray;
import fr.soleil.comete.util.NumberMatrix;
import fr.soleil.comete.util.StackDataArray;
import fr.soleil.comete.util.StackNumberMatrix;
import fr.soleil.comete.util.StringMatrix;

public class BensikinDAOFactory extends AbstractDAOFactory {

    @Override
    public AbstractDAO<Boolean> createBooleanDAO(AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractDAO<BooleanMatrix> createBooleanImageDAO(AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractDAO<Number> createNumberDAO(AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractDAO<List<DataArray>> createNumberDataArrayDAO(AbstractKey key) {
        Object value = key.getPropertyValue("Attribute");
        if (value != null) {
            Object name = key.getPropertyValue("Name");
            if (name != null) {
                if ((value instanceof DefaultDataArrayDAO)
                        && (name instanceof String)) {
                    return (DefaultDataArrayDAO) value;
                }
            }
        }
        return null;
    }

    @Override
    public AbstractDAO<NumberMatrix> createNumberImageDAO(AbstractKey key) {
        if (key != null) {
            Object dao = key.getPropertyValue("dao");
            if (dao != null) {
                if (dao instanceof DefaultMatrixDAO) {
                    return (DefaultMatrixDAO) dao;
                }
            }
        }
        return null;
    }

    @Override
    public AbstractDAO<StackDataArray> createStackNumberDataArrayDAO(
            AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractDAO<StackNumberMatrix> createStackNumberImageDAO(
            AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractActionDAO<String> createStringActionDAO(AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractDAO<String> createStringDAO(AbstractKey arg0) {
        return null;
    }

    @Override
    public AbstractDAO<StringMatrix> createStringImageDAO(AbstractKey arg0) {
        return null;
    }

    @Override
    public void initBehaviorList() {
    }

    @Override
    public String initFactoryName() {
        return null;
    }
}
