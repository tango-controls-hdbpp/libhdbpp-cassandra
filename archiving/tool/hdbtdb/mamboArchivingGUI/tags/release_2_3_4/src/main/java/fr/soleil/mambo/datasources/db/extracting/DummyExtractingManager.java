/*
 * Synchrotron Soleil File : DummyExtractingManager.java Project : mambo
 * Description : Author : CLAISSE Original : 22 sept. 2006 Revision: Author:
 * Date: State: Log: DummyExtractingManager.java,v
 */
package fr.soleil.mambo.datasources.db.extracting;

import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

public class DummyExtractingManager implements IExtractingManager {

    public DummyExtractingManager() {
        super();
    }

    @Override
    public Double[] getMinAndMax(String[] param, boolean _historic) {
        return null;
    }

    @Override
    public DbData retrieveData(String[] param, boolean historic,
            SamplingType samplingType) {
        return null;
    }

    @Override
    public Vector<ImageData> retrieveImageDatas(String[] param,
            boolean _historic, SamplingType samplingType) throws DevFailed {
        return null;
    }

    @Override
    public String timeToDateSGBD(long milli) throws DevFailed,
            ArchivingException {
        return null;
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void allow() {
    }

    @Override
    public void setShowWrite(boolean b) {
    }

    @Override
    public void setShowRead(boolean b) {
    }

    @Override
    public void openConnection() throws ArchivingException {
    }

    @Override
    public boolean isShowRead() {
        return false;
    }

    @Override
    public boolean isShowWrite() {
        return false;
    }
}
