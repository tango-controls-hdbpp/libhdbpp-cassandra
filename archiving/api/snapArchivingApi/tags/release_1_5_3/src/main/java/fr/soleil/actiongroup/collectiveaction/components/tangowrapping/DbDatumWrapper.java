/*	Synchrotron Soleil 
 *  
 *   File          :  IDbDatum.java
 *  
 *   Project       :  DynamicTangoUtilities
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  9 févr. 07 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IDbDatum.java,v 
 *
 */
 /*
 * Created on 9 févr. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.actiongroup.collectiveaction.components.tangowrapping;

import fr.esrf.TangoApi.DbDatum;

public class DbDatumWrapper 
{
    private DbDatum dbDatum;

    public DbDatumWrapper (DbDatum dbDatum)
    {
        this.dbDatum = dbDatum;
    }
    
    /**
     * @return the dbDatum
     */
    public DbDatum getDbDatum() {
        return this.dbDatum;
    }

    /**
     * @param dbDatum the dbDatum to set
     */
    public void setDbDatum(DbDatum dbDatum) {
        this.dbDatum = dbDatum;
    }
    
    
}
