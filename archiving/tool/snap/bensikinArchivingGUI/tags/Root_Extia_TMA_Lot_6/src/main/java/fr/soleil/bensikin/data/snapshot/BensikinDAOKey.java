package fr.soleil.bensikin.data.snapshot;

import fr.soleil.comete.dao.AbstractKey;

public class BensikinDAOKey extends AbstractKey{

    public BensikinDAOKey(String name, Object value){
        super(SpectrumDAOFactory.class.getName());
        registerProperty("Attribute", value);
        registerProperty("Name", name);
    }
    
    @Override
    public String getInformationKey() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return true;
    }

}
