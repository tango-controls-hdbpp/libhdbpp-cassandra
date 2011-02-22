/*
 * Synchrotron Soleil File : IExtractingManager.java Project : mambo Description
 * : Author : CLAISSE Original : 22 sept. 2006 Revision: Author: Date: State:
 * Log: IExtractingManager.java,v
 */
/*
 * Created on 22 sept. 2006 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.db.extracting;

import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

public interface IExtractingManager {

    public void openConnection() throws ArchivingException;

    public Double[] getMinAndMax(String[] param, boolean _historic);

    public Hashtable<String, Object> retrieveDataHash(String[] param,
            boolean _historic, SamplingType samplingType) throws DevFailed;

    public Vector<ImageData> retrieveImageDatas(String[] param,
            boolean _historic, SamplingType samplingType) throws DevFailed;

    public String timeToDateSGBD(long milli) throws DevFailed,
            ArchivingException;

    public void cancel();

    public void allow();

    public boolean isCanceled();

    public void setShowWrite(boolean b);

    public void setShowRead(boolean b);
}
