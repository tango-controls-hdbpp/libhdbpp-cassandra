package fr.soleil.mambo.datasources.db.properties;


public class DummyPropertyManager implements IPropertyManager {

    @Override
    public boolean isHdbDegrad() {
        return false;
    }

    @Override
    public boolean isTdbDegrad() {
        return false;
    }
}
