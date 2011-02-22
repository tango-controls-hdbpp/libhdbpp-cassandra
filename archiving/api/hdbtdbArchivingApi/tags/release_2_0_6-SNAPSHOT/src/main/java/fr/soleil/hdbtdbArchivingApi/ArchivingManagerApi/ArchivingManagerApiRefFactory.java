/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

/**
 * @author AYADI
 *
 */
public class ArchivingManagerApiRefFactory {

	/**
	 * 
	 */
	public ArchivingManagerApiRefFactory() {
		// TODO Auto-generated constructor stub
	}
	public static IArchivingManagerApiRef getInstance(int type){
		switch(type){
		case ConfigConst.HDB: return new HdbArchivingManagerApiRef();
		case ConfigConst.TDB: return new TdbArchivingManagerApiRef();		
		}
	return null;
	}

}
