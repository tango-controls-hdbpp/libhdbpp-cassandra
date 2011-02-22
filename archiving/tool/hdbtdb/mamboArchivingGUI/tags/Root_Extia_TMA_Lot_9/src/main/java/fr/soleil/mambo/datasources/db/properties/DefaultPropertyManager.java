package fr.soleil.mambo.datasources.db.properties;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.GetConf;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * This is an {@link IPropertyManager} that really checks for degraded modes
 * 
 * @author awo
 */
public class DefaultPropertyManager implements IPropertyManager {

    private final static String DEGRAD_PROPERTY = "isDegradMode";

    private boolean             hdbDegrad;
    private boolean             tdbDegrad;

    public DefaultPropertyManager() {
        super();
        hdbDegrad = false;
        tdbDegrad = false;
        readProperties();
    }

    private void readProperties() {
        readHdbDegradFromDb();
        readTdbDegradFromDb();
    }

    private void readHdbDegradFromDb() {
        try {
            // check HdbArchiver class properties
            hdbDegrad = GetConf.readBooleanInDB(ConfigConst.HDB_CLASS_DEVICE,
                    DEGRAD_PROPERTY);
        }
        catch (ArchivingException e) {
            e.printStackTrace();
            hdbDegrad = false;
        }
    }

    private void readTdbDegradFromDb() {
        try {
            // check TdbArchiver class properties
            tdbDegrad = GetConf.readBooleanInDB(ConfigConst.TDB_CLASS_DEVICE,
                    DEGRAD_PROPERTY);
        }
        catch (ArchivingException e) {
            e.printStackTrace();
            tdbDegrad = false;
        }
    }

    @Override
    public boolean isHdbDegrad() {
        return hdbDegrad;
    }

    @Override
    public boolean isTdbDegrad() {
        return tdbDegrad;
    }
}
