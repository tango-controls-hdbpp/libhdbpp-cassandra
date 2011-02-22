package fr.soleil.mambo.datasources.db.properties;

/**
 * Interface used to check for degraded mode
 * 
 * @author awo
 *
 */
public interface IPropertyManager {

    public boolean isHdbDegrad();

    public boolean isTdbDegrad();
}
