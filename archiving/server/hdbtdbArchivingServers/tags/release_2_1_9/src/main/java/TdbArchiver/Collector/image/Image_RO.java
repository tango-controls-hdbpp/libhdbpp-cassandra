//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/image/Image_RO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Image_RO.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.17 $
//
// $Log: Image_RO.java,v $
// Revision 1.17  2007/06/01 09:45:53  pierrejoseph
// Attribute ArchiverCollectro.logger has been renamed in m_logger
//
// Revision 1.16  2007/03/05 16:25:19  ounsy
// non-static DataBase
//
// Revision 1.15  2007/02/13 14:19:16  ounsy
// corrected a bug in addSource: an infinite nnumber of FileTools instances could potentially be created
//
// Revision 1.14  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
//
// Revision 1.13  2006/10/19 12:25:51  ounsy
// modfiied the removeSource to take into account the new isAsuynchronous parameter
//
// Revision 1.12  2006/08/23 09:55:15  ounsy
// FileTools compatible with the new TDB file management
// + keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)
//
// Revision 1.11  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.10  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.9  2006/05/23 11:58:03  ounsy
// now checks the timeCondition condition before calling FileTools.processEvent
//
// Revision 1.8  2006/05/16 07:51:15  ounsy
// added the keepingPeriod creation parameter, used to delete older files
//
// Revision 1.7  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.6.8.3  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.6.8.2  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.6.8.1  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.6  2005/06/24 12:06:38  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.5  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.4.1  2005/06/13 13:57:32  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/04/08 15:37:06  chinkumo
// errorChange method filled.

// The aim of this method is to manage possible attribute's problem while polling attributes.

// In case of unavailable value, a record is nevertheless carried out with the event timestamp, but with a kind of NULL value.
//
// Revision 1.3  2005/02/04 17:10:38  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/31 15:09:04  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:25  chinkumo
// First commit (new architecture).
//
// Revision 1.5  2004/09/27 13:17:36  chinkumo
// The addSource method were improved : The two calls 'myFile.checkDirs();' + 'myFile.initFile();' were gathered into one single call (myFile.initialize();).
//
// Revision 1.4  2004/09/14 07:00:32  chinkumo
// Some unused 'import' were removed.
// Some error messages were re-written to fit the 'error policy' recently decided.
//
// Revision 1.3  2004/09/01 15:40:44  chinkumo
// Heading was updated.
// As the Mode object now includes the exportPeriod information the way to build a FileTools object was modified (see addSource(..)).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package TdbArchiver.Collector.image;

import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import TdbArchiver.Collector.Tools.FileTools;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IImageListener;
import fr.esrf.tangoatk.core.INumberImage;
import fr.esrf.tangoatk.core.NumberImageEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;

public class Image_RO extends TdbCollector implements IImageListener {
	public Image_RO(TdbModeHandler _modeHandler, String currentDsPath,
			String currentDbPath) {
		super(_modeHandler, currentDsPath, currentDbPath);
	}

	synchronized public void addSource(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		System.out.println("Image_RO.addSource");
		try {
			synchronized (attributeList) {
				/*
				 * while ( ( INumberImage )
				 * attributeList.get(attributeLightMode.
				 * getAttribute_complete_name()) == null ) {
				 */
				INumberImage attribute = (INumberImage) attributeList
						.add(attributeLightMode.getAttribute_complete_name());
				attribute.addImageListener(this);
				attribute.addErrorListener(this);
				Util.out4.println("\t The attribute named "
						+ attributeLightMode.getAttribute_complete_name()
						+ " was hired to the Collector list...");
				String table_name = super.dbProxy
						.getDataBase()
						.getDbUtil()
						.getTableName(
								attributeLightMode.getAttribute_complete_name());
				FileTools myFile = new FileTools(table_name, attributeLightMode
						.getData_format(), attributeLightMode.getWritable(),
						attributeLightMode.getMode().getTdbSpec()
								.getExportPeriod(), super.m_logger, true,
						super.dbProxy, super.m_currentDsPath,
						super.m_currentDbPath);
				myFile.initialize();
				filesNames.put(attributeLightMode.getAttribute_complete_name(),
						myFile);
				if (attributeList.size() == 1) {
					startCollecting();
				}
				// }
			}
		} catch (ConnectionException e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed adding '"
					+ attributeLightMode.getAttribute_complete_name()
					+ "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing Image_RO.addSource() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);

		}
	}

	synchronized public void removeSource(String attributeName)
			throws ArchivingException {
		System.out.println("Image_RO.removeSource");
		try {
			synchronized (attributeList) {
				/*
				 * while ( ( INumberImage ) attributeList.get(attributeName) !=
				 * null ) {
				 */
				INumberImage attribute = (INumberImage) attributeList
						.get(attributeName);
				if (attribute != null) {
					attribute.removeImageListener(this);
					attribute.removeErrorListener(this);

					this.attributeList.remove(attributeName);
					Util.out4.println("\t The attribute named " + attributeName
							+ " was fired from the Collector list...");
					((FileTools) filesNames.get(attributeName)).closeFile(true);
					filesNames.remove(attributeName);
					if (attributeList.isEmpty()) {
						stopCollecting();
					}
				}
			}
		} catch (Exception e) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ "Failed removing '" + attributeName + "' from sources";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing Image_RO.removeSource() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", e);

		}
	}

	public void errorChange(ErrorEvent errorEvent) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		Util.out3.println("Image_RO.errorChange : "
				+ "Unable to read the attribute named "
				+ errorEvent.getSource().toString());
		Double[][] value = null;
		ImageEvent_RO imageEvent_ro = new ImageEvent_RO();
		imageEvent_ro.setAttribute_complete_name(errorEvent.getSource()
				.toString());
		imageEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
		imageEvent_ro.setImageValueRO(value);
		processEventImage(imageEvent_ro, tryNumber);
	}

	public void imageChange(NumberImageEvent event) {
		int tryNumber = DEFAULT_TRY_NUMBER;
		String attribute_name = ((INumberImage) event.getSource()).getName();
		double[][] value = (double[][]) event.getValue();
		int x = 0, y = 0;
		if (value != null) {
			x = value.length;
			if (x > 0 && value[0] != null) {
				y = value[0].length;
			}
		}
		Double[][] imageValue = new Double[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				imageValue[i][j] = new Double(value[i][j]);
			}
		}

		ImageEvent_RO imageEvent_ro = new ImageEvent_RO();

		imageEvent_ro.setAttribute_complete_name(attribute_name);
		imageEvent_ro.setTimeStamp(event.getTimeStamp());
		imageEvent_ro.setDim_x(x);
		imageEvent_ro.setDim_y(y);
		imageEvent_ro.setImageValueRO(imageValue);

		processEventImage(imageEvent_ro, tryNumber);
	}

	public void stateChange(AttributeStateEvent event) {
	}

	public void processEventImage(ImageEvent_RO imageEvent_ro, int try_number) {
		Util.out4.println("Image_RO.processEventImage");

		boolean timeCondition = super
				.isDataArchivableTimestampWise(imageEvent_ro);
		if (!timeCondition) {
			return;
		}

		try {
			((FileTools) filesNames.get(imageEvent_ro
					.getAttribute_complete_name()))
					.processEventImage(imageEvent_ro);
			super.setLastTimestamp(imageEvent_ro);
		} catch (Exception e) {
			Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
					+ "Image_RO.processEventImage" + "\r\n" + "\t Reason : \t "
					+ e.getClass().getName() + "\r\n" + "\t Description : \t "
					+ e.getMessage() + "\r\n"
					+ "\t Additional information : \t " + "" + "\r\n");
			e.printStackTrace();
		}
		checkGC();
	}
}
