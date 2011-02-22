/*
 * Synchrotron Soleil File : BasicExtractingManager.java Project : mambo
 * Description : Author : CLAISSE Original : 22 sept. 2006 Revision: Author:
 * Date: State: Log: BasicExtractingManager.java,v
 */
/*
 * Created on 22 sept. 2006 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.db.extracting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.datasources.db.DbConnectionManager;
import fr.soleil.mambo.tools.GUIUtilities;

public class BasicExtractingManager extends DbConnectionManager implements
        IExtractingManager {

    private boolean                                 historic;
    private boolean                                 showRead    = true;
    private boolean                                 showWrite   = true;
    private boolean                                 canceled    = false;

    private static final java.text.SimpleDateFormat genFormatEU = new java.text.SimpleDateFormat(
                                                                        "dd-MM-yyyy HH:mm:ss");
    private static final java.text.SimpleDateFormat genFormatUS = new java.text.SimpleDateFormat(
                                                                        "yyyy-MM-dd HH:mm:ss");

    // private static final java.text.SimpleDateFormat genFormatUSName = new
    // java.text.SimpleDateFormat(
    // "yyyy-MM-dd HH:mm:ss.SSS");

    BasicExtractingManager() {
        super();
    }

    @Override
    public Double[] getMinAndMax(String[] param, boolean _historic) {
        Double[] minmax = new Double[2];
        try {
            DataBaseManager db = getDataBaseApi(_historic);

            minmax[0] = new Double(db.getExtractor()
                    .getMinMaxAvgGettersBetweenDates()
                    .getAttDataMinBetweenDates(param));
            minmax[1] = new Double(db.getExtractor()
                    .getMinMaxAvgGettersBetweenDates()
                    .getAttDataMaxBetweenDates(param));
        } catch (Exception e) {
            minmax[0] = new Double(Double.NaN);
            minmax[1] = new Double(Double.NaN);
        }
        return minmax;
    }

    @Override
    public DbData retrieveData(String[] param, boolean historic,
            SamplingType samplingType) {
        if (canceled) {
            return null;
        } else {
            return getDbDataWithSampling(historic, param, samplingType);
        }
    }

    @Override
    public Vector<ImageData> retrieveImageDatas(String[] param,
            boolean _historic, SamplingType samplingType) throws DevFailed {
        try {
            DataBaseManager database = this.getDataBaseApi(_historic);
            Vector<ImageData> ret = database.getExtractor()
                    .getAttPartialImageDataBetweenDates(param, samplingType);
            return ret;
        } catch (ArchivingException e) {
            e.printStackTrace();
            GUIUtilities.throwDevFailed(e);
            return null;
        }
    }

    @Override
    public String timeToDateSGBD(long milli) throws DevFailed,
            ArchivingException {
        switch (DbUtils.getDbType(getDataBaseApi(historic).getDbConn()
                .getDbType())) {
            case ConfigConst.BD_MYSQL:
                return formatDate(milli, genFormatUS);
            default:
                return formatDate(milli, genFormatEU);
        }
    }

    private DbData getDbDataWithSampling(boolean _historic, String[] param,
            SamplingType samplingType) {
        if (canceled)
            return null;
        try {
            DataBaseManager db = getDataBaseApi(_historic);
            DbData data = db.getExtractor().getDataGettersBetweenDates()
                    .getAttDataBetweenDates(param, samplingType);
            return data;
        } catch (ArchivingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cancel() {
        canceled = true;
        // this.getDataBaseApi(historic).cancel();
    }

    @Override
    public void allow() {
        canceled = false;
        // this.getDataBaseApi(historic).allow();
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    /**
     * @return Returns the showRead.
     */
    public boolean isShowRead() {
        return this.showRead;
    }

    @Override
    public void setShowRead(boolean showRead) {
        this.showRead = showRead;
    }

    /**
     * @return Returns the showWrite.
     */
    public boolean isShowWrite() {
        return this.showWrite;
    }

    @Override
    public void setShowWrite(boolean showWrite) {
        this.showWrite = showWrite;
    }

    private static String formatDate(long timeInMillis, SimpleDateFormat format) {
        String date = null;
        if (format != null) {
            Calendar calendar = Calendar.getInstance();
            synchronized (calendar) {
                calendar.setTimeInMillis(timeInMillis);
                date = format.format(calendar.getTime());
            }
        }
        return date;
    }

}
