//+======================================================================
//$Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/Tools/FileTools.java,v $

//Project:      Tango Archiving Service

//Description:  Java source code for the class  FileTools.
//(Chinkumo Jean) - Jun 4, 2004

//$Author: pierrejoseph $

//$Revision: 1.52 $

//$Log: FileTools.java,v $
//Revision 1.52  2007/06/04 09:57:21  pierrejoseph
//NIO usage

//Revision 1.3  2007/04/16 14:57:21  ounsy
//threads are now started via an ExecutorService

//Revision 1.2  2007/04/16 12:54:51  ounsy
//added a trace in write() in case of IOException

//Revision 1.1  2007/03/20 14:54:38  ounsy
//creation

//Revision 1.46  2007/03/16 15:36:10  ounsy
//now uses a file channel

//Revision 1.45  2007/03/05 16:25:19  ounsy
//non-static DataBase

//Revision 1.44  2007/02/27 15:34:48  ounsy
//corrected a severe bug in  processEventSpectrum(SpectrumEvent_RO spectrumEvent_ro) (the value wasn't filled)

//Revision 1.43  2007/01/31 13:06:34  ounsy
//attempted to correct the "Stream Closed" bug by removing a useless call to flush in flusBuffer

//Revision 1.42  2007/01/08 12:44:51  ounsy
//modified WindowThread.destroy to comment the thread.interrupt command which causes InterruptedException for no good reason

//Revision 1.41  2006/12/07 15:19:17  ounsy
//simpler synchronize structuer

//Revision 1.40  2006/12/01 13:53:47  ounsy
//added the missing synchronizes on _bufferedWriter operations

//Revision 1.39  2006/11/29 15:20:11  ounsy
//removed the class-level reference to the FileWriter instance; the ThreadGroup is now an AttributeThreadGroup that overrides uncaughtException to add unexpected exceptions logging

//Revision 1.38  2006/11/29 13:37:29  ounsy
//added a trace in case of uncaught exception in WindowThread

//Revision 1.37  2006/11/20 09:25:35  ounsy
//the TMP file name has changed, and the flush is only on file closing

//Revision 1.36  2006/11/09 14:22:11  ounsy
//minor changes

//Revision 1.35  2006/11/07 16:13:50  ounsy
//minor changes

//Revision 1.34  2006/10/31 16:54:12  ounsy
//milliseconds and null values management

//Revision 1.33  2006/10/19 12:26:37  ounsy
//replaced ExportThread with an ExportTask that can be synchronous or asynchronous

//Revision 1.32  2006/08/23 09:55:15  ounsy
//FileTools compatible with the new TDB file management
//+ keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)

//Revision 1.31  2006/07/24 07:32:35  ounsy
//better image support

//Revision 1.30  2006/07/20 09:23:22  ounsy
//String encoding before file writing

//Revision 1.29  2006/07/18 08:00:47  ounsy
//swapFile() now returns the table name

//Revision 1.28  2006/07/06 09:46:56  ounsy
//removed the calls to file cleaning threads (job importing)

//Revision 1.27  2006/06/16 09:25:33  ounsy
//changed imports because of the diary package moving to the javaapi project

//Revision 1.26  2006/06/15 15:17:50  ounsy
//added a protection against writing in closed files in case of forced exports (eg. a stopArchiving command)

//Revision 1.25  2006/06/14 08:16:32  ounsy
//boolean value protected with '

//Revision 1.24  2006/06/13 14:06:44  ounsy
//Added calls to the garbage collector in file deletion Threads

//Revision 1.23  2006/06/13 13:30:30  ounsy
//added diary messages for tmp files deleting

//Revision 1.22  2006/06/08 08:34:31  ounsy
//added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)

//Revision 1.21  2006/06/07 12:57:02  ounsy
//minor changes

//Revision 1.20  2006/05/30 12:37:00  ounsy
//added a cleanOldFiles property

//Revision 1.19  2006/05/24 08:08:18  ounsy
//added a millisecond parameter to the Thread.join method

//Revision 1.18  2006/05/23 12:00:20  ounsy
//corrected a synchronization problem between file exporting and event writing in the same file

//Revision 1.17  2006/05/16 09:30:41  ounsy
//added what's necessary for the old files deletion mechanism

//Revision 1.16  2006/03/28 11:15:39  ounsy
//minor changes

//Revision 1.15  2006/03/27 13:56:03  ounsy
//replaced
//( new exportThread(get_fileName()) ).start();
//with
//( new exportThread(get_fileName()) ).run();

//Revision 1.14  2006/03/21 14:18:42  ounsy
//bug correction on archiving stop

//Revision 1.13  2006/03/15 16:06:50  ounsy
//corrected the bug where some temp files were randomly not purged

//Revision 1.12  2006/03/13 14:54:59  ounsy
//minor changes

//Revision 1.11  2006/02/15 11:19:26  chinkumo
//Since the spectrum storage used type was changed on the Oracle side processEventSpectrum was modified.
//This change enables partitionning on Oracle server.

//Revision 1.10  2006/02/13 09:36:49  chinkumo
//Changes made into the DataBaseApi Class were reported here.
//(Methods 'exportToDB_ScalarRO/WO/RW', 'exportToDB_SpectrumRO/WO/RW' and 'exportToDB_ImageRO/WO/RW' were generalized into 'exportToDB_Scalar', 'exportToDB_Spectrum' and 'exportToDB_Image')

//Revision 1.9  2006/02/07 11:57:15  ounsy
//added spectrum RW support

//Revision 1.8  2005/11/29 17:34:14  chinkumo
//no message

//Revision 1.7.8.3  2005/11/29 16:15:11  chinkumo
//Code reformated (pogo compatible)

//Revision 1.7.8.2  2005/11/15 13:45:38  chinkumo
//...

//Revision 1.7.8.1  2005/09/09 10:26:21  chinkumo
//Since the collecting politic was simplified and improved this class was modified.

//Revision 1.7  2005/06/24 12:05:17  chinkumo
//Changes made to make the processEventScalarXXXXXX method use the fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil's milliToString() method.

//Revision 1.6  2005/06/14 10:39:09  chinkumo
//Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.

//Revision 1.5.4.2  2005/06/13 13:43:33  chinkumo
//Changes made to improve the management of exceptions were reported here.

//Revision 1.5.4.1  2005/04/29 18:40:09  chinkumo
//Date format changed to improve efficiency while  archiving. This improve a lot temporary archiving.

//Revision 1.5  2005/04/07 13:36:04  chinkumo
//the apostrophe date surround string (") was removed when . This is to avoid problems with some MySQL versions (4.1.x) when inserting records.

//Revision 1.4  2005/04/06 19:14:18  chinkumo
//Changes done to optimize the use of String object type.
//The quote string (') was changed into the apostrophe string ("). This is to avoid problems with some MySQL versions (4.1.x) when inserting records with date field.

//Revision 1.3  2005/02/04 17:10:38  chinkumo
//The trouble with the grouped stopping strategy was fixed.

//Revision 1.2  2005/01/31 15:09:04  chinkumo
//Changes made since the TdbProxy class was changed into the DbProxy class.

//Revision 1.1  2004/12/06 16:43:25  chinkumo
//First commit (new architecture).

//Revision 1.7  2004/10/07 15:05:39  chinkumo
//The problem of 'regional parameters' was fixed.

//Revision 1.5  2004/09/27 13:44:22  chinkumo
//A new thread was created to delete files.
//In general, the behavior of the various threads was improved (synchronization).

//Revision 1.4  2004/09/14 13:28:51  chinkumo
//Thread stuff simplified !

//Revision 1.3  2004/09/01 15:51:54  chinkumo
//Heading was updated.

//copyleft :   Synchrotron SOLEIL
//L'Orme des Merisiers
//Saint-Aubin - BP 48
//91192 GIF-sur-YVETTE CEDEX

//-======================================================================
package TdbArchiver.Collector.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.util.Date;

import TdbArchiver.Collector.DbProxy;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DateHeure;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

public class FileTools {
    private final int dataFormat;
    private final int writable;
    private String fileName;
    private final String tableName;
    private FileChannel channel;
    // private final long _windowsDuration;

    // private final ExportTask _myWindowThread;
    // protected ScheduledExecutorService executorService =
    // Executors.newScheduledThreadPool(1);
    // ScheduledFuture<?> futureExport;
    private final String localFilePath;
    private final String remoteFilePath;

    private final ILogger logger;
    private final DbProxy dbProxy;
    private Timestamp lastTimestamp;
    private final String attributeName;
    private int writtenAttributes;
    private int attributePerFile;
    private Date creationFileDate;
    private final long exportPeriod;

    public FileTools(final String attributeName, final String tableName, final int dataFormat,
	    final int writable, final long windowsDuration, final ILogger logger,
	    final DbProxy dbProxy, final String workingDsPath, final String workingDbPath)
	    throws IOException {
	this.dataFormat = dataFormat;
	this.writable = writable;
	this.tableName = tableName;
	this.logger = logger;
	this.dbProxy = dbProxy;
	this.attributeName = attributeName;
	exportPeriod = windowsDuration;
	attributePerFile = 0;
	localFilePath = workingDsPath;
	remoteFilePath = workingDbPath;
	writtenAttributes = 0;
	logger.trace(ILogger.LEVEL_INFO, "new FileTools for " + attributeName + " at "
		+ localFilePath);

	checkDirs(localFilePath);
	initFile();

	// futureExport = executorService.scheduleAtFixedRate(new ExportTask(),
	// windowsDuration,
	// windowsDuration, TimeUnit.MILLISECONDS);

    }

    private String buidFileName(final String tableName) {
	final StringBuffer fileName = new StringBuffer();
	creationFileDate = new Date();
	final DateHeure dh = new DateHeure(creationFileDate);
	fileName.append(tableName);
	fileName.append("-");
	fileName.append(dh.toString("yyyyMMdd"));
	fileName.append("-");
	fileName.append(dh.toString("HHmmss"));
	fileName.append(".dat");
	return fileName.toString();
    }

    private static void checkDirs(final String path) {
	final File pathDir = new File(path);
	if (!pathDir.exists()) {
	    pathDir.mkdirs();
	}
    }

    private String getLocalFilePath() {
	return localFilePath + File.separator + fileName;
    }

    private void initFile() throws IOException {
	try {
	    fileName = buidFileName(tableName);
	    logger.trace(ILogger.LEVEL_INFO, "open file " + getLocalFilePath());
	    final FileOutputStream stream = new FileOutputStream(new File(getLocalFilePath()));
	    channel = stream.getChannel();
	} catch (final IOException e) {
	    logger.trace(ILogger.LEVEL_ERROR, "ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "FileTools.initFile" + "\r\n" + "\t Reason : \t " + e.getClass().getName()
		    + "\r\n" + "\t Description : \t " + e.getMessage() + "\r\n"
		    + "\t Additional information : \t " + "" + "\r\n");
	    e.printStackTrace();
	    throw e;
	}
    }

    private void closeFile() throws IOException {
	logger.trace(ILogger.LEVEL_DEBUG, "closing file " + getLocalFilePath());
	channel.close();
	logger.trace(ILogger.LEVEL_DEBUG, " file closed " + getLocalFilePath());
    }

    public synchronized void processEventScalar(final ScalarEvent scalarEvent)
	    throws ArchivingException {
	try {
	    String readValue = scalarEvent.valueToString(0);
	    String writeValue = scalarEvent.valueToString(1);
	    final long timeStampValue = scalarEvent.getTimeStamp();
	    if (isValidLine(timeStampValue)) {

		if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {
		    if (readValue == null
			    || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(readValue.trim())) {
			readValue = GlobalConst.ORACLE_NULL_VALUE;
		    }
		    if (writeValue == null
			    || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(writeValue.trim())) {
			writeValue = GlobalConst.ORACLE_NULL_VALUE;
		    }
		    if (scalarEvent.getData_type() == TangoConst.Tango_DEV_STRING) {
			readValue = StringFormater.formatStringToWrite(readValue);
			writeValue = StringFormater.formatStringToWrite(writeValue);
		    }
		    final StringBuffer stringBuffer = new StringBuffer();
		    stringBuffer.append("\"");
		    stringBuffer.append(DateUtil.milliToString(timeStampValue,
			    DateUtil.FR_DATE_PATTERN));
		    stringBuffer.append("\"");
		    stringBuffer.append(",");

		    switch (scalarEvent.getWritable()) {
		    case AttrWriteType._READ:
			stringBuffer.append("\"").append(readValue).append("\"");
			break;
		    case AttrWriteType._READ_WRITE:
		    case AttrWriteType._READ_WITH_WRITE:
			stringBuffer.append("\"").append(readValue).append("\"");
			stringBuffer.append(",");
			stringBuffer.append("\"").append(writeValue).append("\"");
			break;
		    case AttrWriteType._WRITE:
			stringBuffer.append("\"").append(writeValue).append("\"");
			break;
		    }
		    write(stringBuffer.toString());
		    write(ConfigConst.NEW_LINE);

		} else if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_MYSQL) {
		    if (readValue == null
			    || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(readValue.trim())) {
			readValue = GlobalConst.MYSQL_NULL_VALUE;
		    }
		    if (writeValue == null
			    || GlobalConst.ARCHIVER_NULL_VALUE.equalsIgnoreCase(writeValue.trim())) {
			writeValue = GlobalConst.MYSQL_NULL_VALUE;
		    }
		    if (scalarEvent.getData_type() == TangoConst.Tango_DEV_STRING) {
			readValue = StringFormater.formatStringToWrite(readValue);
			writeValue = StringFormater.formatStringToWrite(writeValue);
		    }

		    switch (scalarEvent.getWritable()) {
		    case AttrWriteType._READ:
			write(new StringBuffer().append(
				toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(
				ConfigConst.FIELDS_LIMIT).append(readValue).append(
				ConfigConst.LINES_LIMIT).toString());
			break;
		    case AttrWriteType._READ_WITH_WRITE:
			write(new StringBuffer().append(
				toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(
				ConfigConst.FIELDS_LIMIT).append(readValue).append(
				ConfigConst.FIELDS_LIMIT).append(writeValue).append(
				ConfigConst.LINES_LIMIT).toString());
			break;
		    case AttrWriteType._WRITE:
			write(new StringBuffer().append(
				toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(
				ConfigConst.FIELDS_LIMIT).append(writeValue).append(
				ConfigConst.LINES_LIMIT).toString());
			break;
		    case AttrWriteType._READ_WRITE:
			write(new StringBuffer().append(
				toDbTimeStringMySQL(scalarEvent.getTimeStamp())).append(
				ConfigConst.FIELDS_LIMIT).append(readValue).append(
				ConfigConst.FIELDS_LIMIT).append(writeValue).append(
				ConfigConst.LINES_LIMIT).toString());
			break;
		    }
		}
		doExport();
	    } else {
		logger.trace(ILogger.LEVEL_INFO, "This timestamps has already been inserted : "
			+ new Timestamp(timeStampValue) + " in the file " + fileName);
	    }

	} catch (final IOException e) {
	    e.printStackTrace();

	    logger.trace(ILogger.LEVEL_ERROR, "ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "FileTools.processEventScalar" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "File :\t " + fileName + "\r\n");
	}
    }

    private void doExport() throws IOException {

	// System.out.println("writtenAttributes " + writtenAttributes);
	final long currentDate = System.currentTimeMillis();
	final long elapseTime = currentDate - creationFileDate.getTime();
	// System.out.println("elapseTime " + elapseTime);
	// attributePerFile is not defined so just check exportPeriod
	if (attributePerFile <= 0 && elapseTime >= exportPeriod) {
	    logger.trace(ILogger.LEVEL_INFO, "export because of elapseTime " + elapseTime);
	    switchFile();
	    // check attributePerFile and
	    // exportPeriod
	} else if (attributePerFile > 0) {
	    writtenAttributes++;
	    if (writtenAttributes >= attributePerFile || elapseTime >= exportPeriod) {
		logger.trace(ILogger.LEVEL_INFO, "export because of writtenAttributes "
			+ writtenAttributes + "- elapseTime " + elapseTime);
		switchFile();
		writtenAttributes = 0;
	    }
	}
    }

    private synchronized void write(final String line) throws IOException {
	try {
	    final ByteBuffer buff = ByteBuffer.wrap(line.getBytes());
	    channel.write(buff);
	} catch (final IOException e) {
	    e.printStackTrace();
	    final String msg = "FileToolsWithNio/write/problem writing for attribute/" + tableName;
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    throw e;
	}
    }

    public synchronized void processEventSpectrum(final SpectrumEvent_RO spectrumEvent_ro)
	    throws ArchivingException {
	try {
	    final long timeStampValue = spectrumEvent_ro.getTimeStamp();

	    if (isValidLine(timeStampValue)) {

		if (spectrumEvent_ro.getData_type() == TangoConst.Tango_DEV_STRING) {
		    final String[] value = (String[]) spectrumEvent_ro.getValue();
		    String[] transformedValue = null;
		    if (value != null) {
			transformedValue = new String[value.length];
			for (int i = 0; i < value.length; i++) {
			    transformedValue[i] = StringFormater.formatStringToWrite(value[i]);
			}
			spectrumEvent_ro.setValue(transformedValue);
		    }

		}
		String value = spectrumEvent_ro.getValue_AsString();
		if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {

		    if (GlobalConst.ARCHIVER_NULL_VALUE.equals(value)) {
			value = GlobalConst.ORACLE_NULL_VALUE;
		    }
		    write(new StringBuffer().append("\"").append(
			    DateUtil.milliToString(spectrumEvent_ro.getTimeStamp(),
				    DateUtil.FR_DATE_PATTERN)).append("\"").append(",")
			    .append("\"").append(Double.toString(spectrumEvent_ro.getDim_x()))
			    .append("\"").append(",").append("\"").append(value).append("\"")
			    .toString());
		    write(ConfigConst.NEW_LINE);

		} else if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_MYSQL) {
		    if (GlobalConst.ARCHIVER_NULL_VALUE.equals(value)) {
			value = GlobalConst.MYSQL_NULL_VALUE;
		    }

		    final StringBuffer buff = new StringBuffer();
		    buff.append(toDbTimeStringMySQL(spectrumEvent_ro.getTimeStamp()));
		    buff.append(ConfigConst.FIELDS_LIMIT);
		    buff.append((double) spectrumEvent_ro.getDim_x());
		    buff.append(ConfigConst.FIELDS_LIMIT);
		    buff.append(value);
		    buff.append(ConfigConst.LINES_LIMIT);
		    write(buff.toString());
		}
		doExport();
	    } else {
		logger.trace(ILogger.LEVEL_INFO, "This timestamps has already been inserted : "
			+ new Timestamp(timeStampValue) + " in the file " + fileName);
	    }
	} catch (final IOException e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "FileTools.processEventSpectrum" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "File :\t " + fileName + "\r\n");
	}
    }

    /**
     * @param spectrumEvent_rw
     * @throws ArchivingException
     */
    public synchronized void processEventSpectrum(final SpectrumEvent_RW spectrumEvent_rw)
	    throws ArchivingException {
	try {
	    final long timeStampValue = spectrumEvent_rw.getTimeStamp();

	    if (isValidLine(timeStampValue)) {
		if (spectrumEvent_rw.getData_type() == TangoConst.Tango_DEV_STRING) {
		    final String[] value = (String[]) spectrumEvent_rw.getValue();
		    String[] transformedValue = null;
		    if (value != null) {
			transformedValue = new String[value.length];
			for (int i = 0; i < value.length; i++) {
			    transformedValue[i] = StringFormater.formatStringToWrite(value[i]);
			}
			spectrumEvent_rw.setValue(transformedValue);
		    }
		}
		String readValue = spectrumEvent_rw.getSpectrumValueRW_AsString_Read();
		String writeValue = spectrumEvent_rw.getSpectrumValueRW_AsString_Write();
		if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {

		    if (GlobalConst.ARCHIVER_NULL_VALUE.equals(readValue)) {
			readValue = GlobalConst.ORACLE_NULL_VALUE;
		    }
		    if (GlobalConst.ARCHIVER_NULL_VALUE.equals(writeValue)) {
			writeValue = GlobalConst.ORACLE_NULL_VALUE;
		    }
		    final StringBuffer buff = new StringBuffer();

		    buff.append("\"");
		    buff.append(DateUtil.milliToString(spectrumEvent_rw.getTimeStamp(),
			    DateUtil.FR_DATE_PATTERN));
		    buff.append("\"");

		    buff.append(",");
		    buff.append("\"");
		    buff.append(Double.toString(spectrumEvent_rw.getDim_x()));
		    buff.append("\"");

		    buff.append(",");
		    buff.append("\"");
		    buff.append(readValue);
		    buff.append("\"");

		    buff.append(",");
		    buff.append("\"");
		    buff.append(writeValue);
		    buff.append("\"");

		    final String content = buff.toString();

		    write(content);
		    write(ConfigConst.NEW_LINE);

		} else if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_MYSQL) {
		    if (GlobalConst.ARCHIVER_NULL_VALUE.equals(readValue)) {
			readValue = GlobalConst.MYSQL_NULL_VALUE;
		    }
		    if (GlobalConst.ARCHIVER_NULL_VALUE.equals(writeValue)) {
			writeValue = GlobalConst.MYSQL_NULL_VALUE;
		    }
		    final StringBuffer buff = new StringBuffer();

		    buff.append(toDbTimeStringMySQL(spectrumEvent_rw.getTimeStamp()));
		    buff.append(ConfigConst.FIELDS_LIMIT);
		    buff.append((double) spectrumEvent_rw.getDim_x());
		    buff.append(ConfigConst.FIELDS_LIMIT);
		    buff.append(readValue);
		    buff.append(ConfigConst.FIELDS_LIMIT);
		    buff.append(writeValue);
		    buff.append(ConfigConst.LINES_LIMIT).toString();

		    final String content = buff.toString();
		    write(content);
		}
		doExport();
	    } else {
		logger.trace(ILogger.LEVEL_INFO, "This timestamps has already been inserted : "
			+ new Timestamp(timeStampValue) + " in the file " + fileName);
	    }
	} catch (final IOException e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "FileTools.processEventSpectrum" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "File :\t " + fileName + "\r\n");
	}
    }

    public synchronized void processEventImage(final ImageEvent_RO imageEvent_ro)
	    throws ArchivingException {
	try {
	    if (imageEvent_ro.getData_type() == TangoConst.Tango_DEV_STRING) {
		final String[] value = (String[]) imageEvent_ro.getValue();
		String[] transformedValue = null;
		if (value != null) {
		    transformedValue = new String[value.length];
		    for (int i = 0; i < value.length; i++) {
			transformedValue[i] = StringFormater.formatStringToWrite(value[i]);
		    }
		    imageEvent_ro.setValue(transformedValue);
		}

	    }
	    if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {
		write(new StringBuffer().append("\"").append(
			DateUtil.milliToString(imageEvent_ro.getTimeStamp(),
				DateUtil.FR_DATE_PATTERN)).append("\"").append(",").append("\"")
			.append(Double.toString(imageEvent_ro.getDim_x())).append("\"").append(",")
			.append("\"").append(Double.toString(imageEvent_ro.getDim_y()))
			.append("\"").append(",").append("\"").append(
				imageEvent_ro.getValue_AsString()).append("\"").toString());
		write(ConfigConst.NEW_LINE);
	    } else if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_MYSQL) {
		final long timeStampValue = imageEvent_ro.getTimeStamp();

		if (isValidLine(timeStampValue)) {
		    write(new StringBuffer().append("\"").append(
			    DateUtil.milliToString(imageEvent_ro.getTimeStamp(),
				    DateUtil.FR_DATE_PATTERN)).append("\"").append(",")
			    .append("\"").append(Double.toString(imageEvent_ro.getDim_x())).append(
				    "\"").append(",").append("\"").append(
				    Double.toString(imageEvent_ro.getDim_y())).append("\"").append(
				    ",").append("\"").append(imageEvent_ro.getValue_AsString())
			    .append("\"").toString());
		    write(ConfigConst.NEW_LINE);
		} else {
		    logger.trace(ILogger.LEVEL_INFO, "This timestamps has already been inserted : "
			    + new Timestamp(timeStampValue) + " in the file " + fileName);
		}

	    }
	    doExport();
	} catch (final IOException e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "FileTools.processEventImage" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "File :\t " + fileName + "\r\n");
	}
    }

    private String toDbTimeStringMySQL(final long time) {
	return new Timestamp(time).toString();
    }

    /**
     * Switch file in an atomic action
     * 
     * @throws IOException
     */
    public synchronized String switchFile() throws IOException {
	final String oldFileName = fileName;
	logger.trace(ILogger.LEVEL_INFO, "#######Exporting file " + oldFileName + " - attribute "
		+ attributeName + "-  period " + exportPeriod + " - attrPerFile "
		+ attributePerFile);
	closeFile();
	initFile();
	exportFileToDB(oldFileName);
	return tableName;
    }

    /**
     * Close the file and launch an export of this file to the database.
     */
    public synchronized void stopFileGeneration() {
	logger.trace(ILogger.LEVEL_INFO, "shutdown task for " + getLocalFilePath());

	// futureExport.cancel(false);
	// while (!futureExport.isDone()) {
	// try {
	// Thread.sleep(100);
	// } catch (final InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// executorService.shutdown();
	logger.trace(ILogger.LEVEL_INFO, " task is shutdown for " + getLocalFilePath());
	try {
	    closeFile();
	} catch (final IOException e) {
	    e.printStackTrace();
	    final String message = "Error while closing file: " + remoteFilePath + " - tableName: "
		    + tableName;
	    logger.trace(ILogger.LEVEL_CRITIC, message);
	}
	exportFileToDB(fileName);

    }

    private synchronized void exportFileToDB(final String fileName) {
	try {
	    logger.trace(ILogger.LEVEL_DEBUG, "start exporting " + remoteFilePath + "/" + fileName
		    + " - _tableName:" + tableName + " - attribute " + attributeName);
	    switch (dataFormat) {
	    case AttrDataFormat._SCALAR:
		dbProxy.exportToDB_Scalar(remoteFilePath, fileName, tableName, writable);
		break;
	    case AttrDataFormat._SPECTRUM:
		dbProxy.exportToDB_Spectrum(remoteFilePath, fileName, tableName, writable);
		break;
	    case AttrDataFormat._IMAGE:
		dbProxy.exportToDB_Image(remoteFilePath, fileName, tableName, writable);
		break;
	    default:
		Util.out2
			.println("Export : " + "DataFormat (" + dataFormat + ") not supported !! ");
		break;
	    }

	    final String message = "Export out OK -  of " + remoteFilePath + "/" + fileName
		    + " - _tableName:" + tableName + " - attribute " + attributeName;
	    logger.trace(ILogger.LEVEL_DEBUG, message);
	} catch (final ArchivingException e) {
	    e.printStackTrace();
	    final String message = "Problem (ArchivingException) exporting file: _remoteFilePath|"
		    + remoteFilePath + "|_exportFileName|" + fileName + "|_tableName|" + tableName;
	    logger.trace(ILogger.LEVEL_ERROR, message);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	}
    }

    /**
     * This class represent the object that is called each time a file must be
     * sent to the database.
     */
    // private class ExportTask implements Runnable {
    //
    // public void run() {
    // try {
    // switchFile();
    // } catch (final IOException e) {
    // e.printStackTrace();
    // final String message = "Error while switching file: " + remoteFilePath
    // + " - tableName: " + tableName;
    // logger.trace(ILogger.LEVEL_CRITIC, message);
    // }
    // }
    // }

    private boolean isValidLine(final long currentTimestamp) {
	boolean res = false;
	final Timestamp ts = new Timestamp(currentTimestamp);
	if (ts != null && (lastTimestamp == null || ts.after(lastTimestamp))) {
	    res = true;
	    lastTimestamp = ts;
	}
	return res;
    }

    public int getAttributePerFile() {
	return attributePerFile;
    }

    public void setAttributePerFile(final int attributePerFile) {
	this.attributePerFile = attributePerFile;
    }

}