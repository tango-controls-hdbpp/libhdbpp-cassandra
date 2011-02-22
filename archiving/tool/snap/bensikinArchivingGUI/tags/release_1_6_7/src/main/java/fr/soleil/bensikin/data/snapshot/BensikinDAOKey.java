package fr.soleil.bensikin.data.snapshot;

import fr.soleil.comete.dao.AbstractKey;
import fr.soleil.comete.dao.util.DefaultMatrixDAO;

public class BensikinDAOKey extends AbstractKey {

    public BensikinDAOKey(String name, Object value) {
        super(BensikinDAOFactory.class.getName());
        registerProperty("Attribute", value);
        registerProperty("Name", name);
    }

    public BensikinDAOKey(DefaultMatrixDAO dao) {
        super(BensikinDAOFactory.class.getName());
        registerProperty("dao", dao);
    }

    @Override
    public String getInformationKey() {
        return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
