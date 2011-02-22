/*	Synchrotron Soleil 
 *  
 *   File          :  DummyExtractingManager.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  22 sept. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: DummyExtractingManager.java,v 
 *
 */
 /*
 * Created on 22 sept. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.db.extracting;

import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

public class DummyExtractingManager implements IExtractingManager
{

    public DummyExtractingManager() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Double[] getMinAndMax(String[] param, boolean _historic) {
        // TODO Auto-generated method stub
        return null;
    }

    public Hashtable retrieveDataHash(String[] param, boolean _historic, SamplingType samplingType) {
        // TODO Auto-generated method stub
        return null;
    }

    public Vector retrieveImageDatas(String[] param, boolean _historic, SamplingType samplingType) throws DevFailed {
        // TODO Auto-generated method stub
        return null;
    }

    public String timeToDateSGBD(long milli) throws DevFailed, ArchivingException {
        // TODO Auto-generated method stub
        return null;
    }

    public void cancel() {
        // TODO Auto-generated method stub
        
    }

    public boolean isCanceled() {
        // TODO Auto-generated method stub
        return false;
    }

    public void allow() {
        // TODO Auto-generated method stub
        
    }

    public void setShowWrite(boolean b) {
        // TODO Auto-generated method stub
        
    }

    public void setShowRead(boolean b) {
        // TODO Auto-generated method stub
        
    }

    public void openConnection() throws ArchivingException {
        // TODO Auto-generated method stub
        
    }

}
