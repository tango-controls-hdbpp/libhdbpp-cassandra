package fr.soleil.mambo.datasources.db.properties;

/**
 * An {@link IPropertyManager} factory
 * 
 * @author awo
 * @see IPropertyManager
 */
public class PropertyManagerFactory {

    public static final int         DUMMY   = 0;
    public static final int         DEFAULT = 1;

    private static IPropertyManager manager;

    public static IPropertyManager getCurrentImpl() {
        return manager;
    }

    public static IPropertyManager getImpl(int typeOfImpl) {
        switch (typeOfImpl) {
            case DUMMY:
                manager = new DummyPropertyManager();
                break;
            case DEFAULT:
                manager = new DefaultPropertyManager();
                break;
        }
        return manager;
    }

}
