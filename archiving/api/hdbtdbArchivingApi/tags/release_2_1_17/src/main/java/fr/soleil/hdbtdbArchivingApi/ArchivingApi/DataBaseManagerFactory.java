/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi;

/**
 * @author AYADI
 * 
 */
public class DataBaseManagerFactory {

	/**
	 * 
	 */
	public DataBaseManagerFactory() {
		// TODO Auto-generated constructor stub
	}

	public static DataBaseManager getDataBaseManager(int type) {
		switch (type) {
		case ConfigConst.TDB:
			return new TDBDataBaseManager();
		case ConfigConst.HDB:
			return new HDBDataBaseManager();
		}
		return null;
	}

	public static DataBaseManager getDataBaseManager(int type, int db_type) {
		switch (type) {
		case ConfigConst.TDB:
			return new TDBDataBaseManager(db_type);
		case ConfigConst.HDB:
			return new HDBDataBaseManager(db_type);
		}
		return null;
	}

}
