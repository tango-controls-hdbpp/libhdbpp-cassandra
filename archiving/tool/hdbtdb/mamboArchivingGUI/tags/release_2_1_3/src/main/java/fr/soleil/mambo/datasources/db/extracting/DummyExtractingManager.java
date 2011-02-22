/*
 * Synchrotron Soleil File : DummyExtractingManager.java Project : mambo
 * Description : Author : CLAISSE Original : 22 sept. 2006 Revision: Author:
 * Date: State: Log: DummyExtractingManager.java,v
 */
package fr.soleil.mambo.datasources.db.extracting;

import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

public class DummyExtractingManager implements IExtractingManager {

	public DummyExtractingManager() {
		super();
	}

	public Double[] getMinAndMax(String[] param, boolean _historic) {
		return null;
	}

	public Hashtable<String, Object> retrieveDataHash(String[] param,
			boolean _historic, SamplingType samplingType) {
		return null;
	}

	public Vector<ImageData> retrieveImageDatas(String[] param,
			boolean _historic, SamplingType samplingType) throws DevFailed {
		return null;
	}

	public String timeToDateSGBD(long milli) throws DevFailed,
			ArchivingException {
		return null;
	}

	public void cancel() {
	}

	public boolean isCanceled() {
		return false;
	}

	public void allow() {
	}

	public void setShowWrite(boolean b) {
	}

	public void setShowRead(boolean b) {
	}

	public void openConnection() throws ArchivingException {
	}

}
