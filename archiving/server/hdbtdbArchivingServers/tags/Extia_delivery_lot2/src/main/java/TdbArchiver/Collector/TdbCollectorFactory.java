//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/TdbCollectorFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TdbCollectorFactory.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.25 $
//
// $Log: TdbCollectorFactory.java,v $
// Revision 1.25  2007/08/27 14:14:35  pierrejoseph
// Traces addition : the logger object is stored in the TdbCollectorFactory
//
// Revision 1.24  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.23  2007/01/09 15:15:24  ounsy
// put the Giacomo version back for removeAllForAttribute
//
// Revision 1.22  2006/11/24 14:54:17  ounsy
// we re-use the old removeAllForAttribute
//
// Revision 1.21  2006/11/24 14:04:16  ounsy
// the diary entry in case of missing collector is now done by he archiver
//
// Revision 1.20  2006/11/24 13:19:35  ounsy
// TdbCollectorFactory.get(attrLightMode) has a new parameter "doCreateCollectorIfMissing". A missing collector will only be created if this is true, otherwise a message in logged into the archiver's diary and an exception launched
//
// Revision 1.19  2006/11/15 15:48:38  ounsy
// added the Giacomo correction of removeAllForAttribute
//
// Revision 1.18  2006/08/23 09:42:56  ounsy
// Spectrum_xxx renamed as NumberSpectrum_xxx
//
// Revision 1.17  2006/07/26 08:44:19  ounsy
// TdbCollectorFactory no more static
//
// Revision 1.16  2006/07/25 09:47:39  ounsy
// added a factoryLoadAssessment() method
//
// Revision 1.15  2006/06/27 12:07:13  ounsy
// Corrected the bug that made the collectors all have the same logger for a given
// SuperMode (instead of one logger per TdbArchiver)
//
// Revision 1.14  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.13  2006/06/15 15:16:39  ounsy
// added a protection against the ConcurrentModificationException that sometimes occur when launching the archivers
//
// Revision 1.12  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.11  2006/05/12 09:20:17  ounsy
// list concurrent modification bug correction
//
// Revision 1.10  2006/04/11 09:13:32  ounsy
// added some attribute removing methods
//
// Revision 1.9  2006/04/05 13:49:52  ounsy
// new types full support
//
// Revision 1.8  2006/03/10 12:01:59  ounsy
// state and string support
//
// Revision 1.7  2006/02/15 11:13:53  chinkumo
// Minor change : Exception message updated.
//
// Revision 1.6  2006/02/07 11:56:54  ounsy
// added spectrum RW support
//
// Revision 1.5  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.4.8.3  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.4.8.2  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.4.8.1  2005/09/09 10:18:25  chinkumo
// Since the collecting politic was simplified and improved CollectorFactory was modified.
//
// Revision 1.4  2005/06/24 12:06:38  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.3  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.2.6.1  2005/06/13 13:46:45  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.2  2005/02/04 17:10:40  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.1  2004/12/06 16:43:24  chinkumo
// First commit (new architecture).
//
// Revision 1.3  2004/09/27 13:06:33  chinkumo
// The destroy method were changed.
//
// Revision 1.2  2004/09/01 15:32:37  chinkumo
// Heading was updated.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package TdbArchiver.Collector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import TdbArchiver.Collector.Tools.SuperMode;
import TdbArchiver.Collector.image.Image_RO;
import TdbArchiver.Collector.scalar.BooleanScalar_RO;
import TdbArchiver.Collector.scalar.BooleanScalar_RW;
import TdbArchiver.Collector.scalar.BooleanScalar_WO;
import TdbArchiver.Collector.scalar.NumberScalar_RO;
import TdbArchiver.Collector.scalar.NumberScalar_RW;
import TdbArchiver.Collector.scalar.NumberScalar_WO;
import TdbArchiver.Collector.scalar.StateScalar_RO;
import TdbArchiver.Collector.scalar.StringScalar_RO;
import TdbArchiver.Collector.scalar.StringScalar_RW;
import TdbArchiver.Collector.scalar.StringScalar_WO;
import TdbArchiver.Collector.spectrum.BooleanSpectrum_RO;
import TdbArchiver.Collector.spectrum.BooleanSpectrum_RW;
import TdbArchiver.Collector.spectrum.NumberSpectrum_RO;
import TdbArchiver.Collector.spectrum.NumberSpectrum_RW;
import TdbArchiver.Collector.spectrum.StringSpectrum_RO;
import TdbArchiver.Collector.spectrum.StringSpectrum_RW;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeSupport;

public class TdbCollectorFactory {
	private Hashtable tableCollector = null;
	private ILogger m_logger;

	public TdbCollectorFactory(ILogger logger) {
		tableCollector = new Hashtable();
		m_logger = logger;
	}

	/**
	 * This method returns the instance of TdbCollector associated / associable
	 * to an attribute. In this method, attributes are not grouped by mode.
	 * 
	 * @param attributeLightMode
	 *            Attribute associated to the looked collector.
	 * @return the instance of TdbCollector associated / associable to an
	 *         attribute.
	 */
	public TdbCollector get(AttributeLightMode attributeLightMode,
			boolean doCreateCollectorIfMissing, DbProxy dbProxy)
			throws ArchivingException {
		SuperMode superMode = new SuperMode(
				attributeLightMode.getData_format(), attributeLightMode
						.getData_type(), attributeLightMode.getWritable(),
				attributeLightMode.getMode());

		TdbCollector collector = null;

		synchronized (tableCollector)// CLA 15/06/06
		{
			collector = (TdbCollector) tableCollector.get(superMode);

			if (collector == null && doCreateCollectorIfMissing) {
				collector = create(attributeLightMode);
				collector.setDbProxy(dbProxy);
				tableCollector.put(superMode, collector);
			}
		}

		if (collector != null) {
			collector.setDiaryLogger(m_logger);
		}

		return collector;
	}

	/**
	 * This method returns the instance of TdbCollector associated / associable
	 * to an attribute. In this method, attributes are grouped by mode.
	 * 
	 * @param attributeLightMode
	 *            Attribute associated to the looked collector.
	 * @return the instance of TdbCollector associated / associable to an
	 *         attribute.
	 */
	public TdbCollector create(AttributeLightMode attributeLightMode)
			throws ArchivingException {
		System.out.println("TdbCollectorFactory.create\r\n\t"
				+ attributeLightMode.toString());
		String name = attributeLightMode.getAttribute_complete_name();
		int data_type = attributeLightMode.getData_type();
		int data_format = attributeLightMode.getData_format();
		int writable = attributeLightMode.getWritable();
		TdbModeHandler modeHandler = new TdbModeHandler(attributeLightMode
				.getMode());
		TdbCollector collector = null;
		if (AttributeSupport.checkAttributeSupport(name, data_type,
				data_format, writable)) {
			switch (data_format) { // [0 - > SCALAR] (1 - > SPECTRUM] [2 - >
									// IMAGE]
			case AttrDataFormat._SCALAR: // SCALAR
				switch (writable) { // [0 - > READ] [1 - > READ_WITH_WRITE] ([2
									// - > WRITE)] [3 - > READ_WRITE]
				case AttrWriteType._READ: // READ -> 0
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
						collector = new NumberScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_USHORT:
						collector = new NumberScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_LONG:
						collector = new NumberScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_ULONG:
						collector = new NumberScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_DOUBLE:
						collector = new NumberScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_FLOAT:
						collector = new NumberScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_CHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_UCHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STATE:
						collector = new StateScalar_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringScalar_RO(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				case AttrWriteType._READ_WITH_WRITE: // READ_WITH_WRITE -> 1
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_USHORT:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_LONG:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_ULONG:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_DOUBLE:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_FLOAT:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_CHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_UCHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STATE:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringScalar_RW(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				case AttrWriteType._WRITE: // WRITE -> 2
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
						collector = new NumberScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_USHORT:
						collector = new NumberScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_LONG:
						collector = new NumberScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_ULONG:
						collector = new NumberScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_DOUBLE:
						collector = new NumberScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_FLOAT:
						collector = new NumberScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanScalar_WO(modeHandler);
						break;
					case TangoConst.Tango_DEV_CHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_UCHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STATE:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringScalar_WO(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				case AttrWriteType._READ_WRITE: // READ_WRITE -> 3
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_USHORT:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_LONG:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_ULONG:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_DOUBLE:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_FLOAT:
						collector = new NumberScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanScalar_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_CHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_UCHAR:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STATE:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
						// break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringScalar_RW(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				default:
					generateException(GlobalConst.DATA_WRITABLE_EXCEPTION,
							writable, name);
				}
				break;
			case AttrDataFormat._SPECTRUM: // SPECTRUM
				switch (writable) {
				case AttrWriteType._WRITE:
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
					case TangoConst.Tango_DEV_USHORT:
					case TangoConst.Tango_DEV_LONG:
					case TangoConst.Tango_DEV_ULONG:
					case TangoConst.Tango_DEV_DOUBLE:
					case TangoConst.Tango_DEV_FLOAT:
					case TangoConst.Tango_DEV_CHAR:
					case TangoConst.Tango_DEV_UCHAR:
						collector = new NumberSpectrum_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanSpectrum_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringSpectrum_RO(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				case AttrWriteType._READ:
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
					case TangoConst.Tango_DEV_USHORT:
					case TangoConst.Tango_DEV_LONG:
					case TangoConst.Tango_DEV_ULONG:
					case TangoConst.Tango_DEV_DOUBLE:
					case TangoConst.Tango_DEV_FLOAT:
					case TangoConst.Tango_DEV_CHAR:
					case TangoConst.Tango_DEV_UCHAR:
					case TangoConst.Tango_DEV_STATE:
						collector = new NumberSpectrum_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanSpectrum_RO(modeHandler);
						break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringSpectrum_RO(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				case AttrWriteType._READ_WITH_WRITE:
				case AttrWriteType._READ_WRITE:
					switch (data_type) {
					case TangoConst.Tango_DEV_SHORT:
					case TangoConst.Tango_DEV_USHORT:
					case TangoConst.Tango_DEV_LONG:
					case TangoConst.Tango_DEV_ULONG:
					case TangoConst.Tango_DEV_DOUBLE:
					case TangoConst.Tango_DEV_FLOAT:
					case TangoConst.Tango_DEV_CHAR:
					case TangoConst.Tango_DEV_UCHAR:
						collector = new NumberSpectrum_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_BOOLEAN:
						collector = new BooleanSpectrum_RW(modeHandler);
						break;
					case TangoConst.Tango_DEV_STRING:
						collector = new StringSpectrum_RW(modeHandler);
						break;
					default:
						generateException(GlobalConst.DATA_TYPE_EXCEPTION,
								data_type, name);
					}
					break;
				}
				break;
			case AttrDataFormat._IMAGE: // IMAGE
				collector = new Image_RO(modeHandler);
				break;
			default:
				generateException(GlobalConst.DATA_FORMAT_EXCEPTION,
						data_format, name);
			}
		}

		return collector;
	}

	synchronized public void destroy(AttributeLightMode attributeLightMode) {
		SuperMode superMode = new SuperMode(
				attributeLightMode.getData_format(), attributeLightMode
						.getData_type(), attributeLightMode.getWritable(),
				attributeLightMode.getMode());
		synchronized (tableCollector)// CLA 15/06/06
		{
			tableCollector.remove(superMode);
		}
	}

	public synchronized void removeAllForAttribute(
			AttributeLightMode attributeLightMode) throws ArchivingException {
		SuperMode superMode;
		// int i = 0;

		m_logger.trace(ILogger.LEVEL_INFO,
				"===> Entering removeAllForAttribute");
		synchronized (tableCollector)// CLA 15/06/06
		{
			HashSet toRemove = new HashSet();
			/*
			 * while( i < tableCollector.size() ) {
			 */
			superMode = new SuperMode(attributeLightMode.getData_format(),
					attributeLightMode.getData_type(), attributeLightMode
							.getWritable(), attributeLightMode.getMode());

			TdbCollector collector = (TdbCollector) tableCollector
					.get(superMode);

			if (collector != null) {
				m_logger
						.trace(ILogger.LEVEL_INFO,
								"===> Collector found : removeSource is requested ... ");
				collector.removeSource(attributeLightMode
						.getAttribute_complete_name());
				m_logger.trace(ILogger.LEVEL_INFO,
						"===> ... removeSource is done");
				if (collector.hasEmptyList()) {
					toRemove.add(superMode);
				}
			}
			/*
			 * i++; }
			 */
			Iterator toRemoveIterator = toRemove.iterator();
			while (toRemoveIterator.hasNext()) {
				tableCollector.remove(toRemoveIterator.next());
			}
		}

		this.m_logger.trace(ILogger.LEVEL_INFO,
				"===> Exiting removeAllForAttribute");
	}

	/*
	 * public synchronized void removeAllForAttribute(String att_name) throws
	 * ArchivingException { synchronized ( tableCollector )//CLA 15/06/06 { Set
	 * keySet = tableCollector.keySet(); Iterator keyIterator =
	 * keySet.iterator(); HashSet toRemove = new HashSet();
	 * while(keyIterator.hasNext()) { SuperMode superMode =
	 * (SuperMode)keyIterator.next();//CLA -->Concurrent TdbCollector collector
	 * = ( TdbCollector ) tableCollector.get(superMode);
	 * collector.removeSource(att_name); if (collector.hasEmptyList()) {
	 * toRemove.add(superMode); } } Iterator toRemoveIterator =
	 * toRemove.iterator(); while(toRemoveIterator.hasNext()) {
	 * tableCollector.remove(toRemoveIterator.next()); } } }
	 */

	public String factoryAssessment() {
		StringBuffer ass = new StringBuffer();

		synchronized (tableCollector)// CLA 15/06/06
		{
			if (tableCollector != null & !tableCollector.isEmpty()) {
				Collection collectors = tableCollector.values();
				int i = 1;
				int size = collectors.size();
				for (Iterator iterator = collectors.iterator(); iterator
						.hasNext();) {
					TdbCollector collector = (TdbCollector) iterator.next();
					ass.append("*********************************" + " " + i++
							+ "/" + size + " "
							+ "*********************************" + "\r\n");
					ass.append(collector.assessment());
				}
			}
		}

		return ass.toString();
	}

	private static void generateException(String cause, int cause_value,
			String name) throws ArchivingException {
		String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
		String reason = "Failed while executing TdbCollectorFactory.create() method...";
		String desc = cause + " (" + cause_value + ") not supported !! ["
				+ name + "]";
		throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc,
				"");
	}

	public short[] factoryLoadAssessment() {
		short[] ret = new short[3];

		synchronized (tableCollector) {
			if (tableCollector != null & !tableCollector.isEmpty()) {
				Collection collectors = tableCollector.values();
				for (Iterator iterator = collectors.iterator(); iterator
						.hasNext();) {
					TdbCollector collector = (TdbCollector) iterator.next();
					short[] collectorLoad = collector.loadAssessment();
					ret[0] += collectorLoad[0];
					ret[1] += collectorLoad[1];
					ret[2] += collectorLoad[2];
				}
			}
		}

		return ret;
	}
}
