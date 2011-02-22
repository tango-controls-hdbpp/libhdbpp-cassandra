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
import java.util.HashMap;
import java.util.Map;

import TdbArchiver.Collector.Tools.SuperMode;
import TdbArchiver.Collector.image.Image_RO;
import TdbArchiver.Collector.scalar.BooleanScalar;
import TdbArchiver.Collector.scalar.NumberScalar;
import TdbArchiver.Collector.scalar.StateScalar;
import TdbArchiver.Collector.scalar.StringScalar;
import TdbArchiver.Collector.spectrum.BooleanSpectrum;
import TdbArchiver.Collector.spectrum.NumberSpectrum;
import TdbArchiver.Collector.spectrum.StringSpectrum;
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
    private final Map<SuperMode, TdbCollector> tableCollector = new HashMap<SuperMode, TdbCollector>();
    private final ILogger m_logger;

    private String m_currentDbPath = "";
    private String m_currentDsPath = "";

    public TdbCollectorFactory(final ILogger logger, final String dsPath, final String dbPath) {
	m_logger = logger;
	m_currentDbPath = dbPath;
	m_currentDsPath = dsPath;
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
    public TdbCollector get(final AttributeLightMode attributeLightMode) {

	final SuperMode superMode = new SuperMode(attributeLightMode.getData_format(),
		attributeLightMode.getData_type(), attributeLightMode.getWritable(),
		attributeLightMode.getMode());

	TdbCollector collector = null;
	collector = tableCollector.get(superMode);
	return collector;
    }

    /**
     * This method create a new TdbCollector instance if required associated to
     * an attribute. In this method, attributes are not grouped by mode.
     * 
     * @param attributeLightMode
     *            Attribute associated to the looked collector.
     */
    public void createCollectorAndAddSource(final AttributeLightMode attributeLightMode,
	    final DbProxy dbProxy, final int attributePerFile) throws ArchivingException {

	final SuperMode superMode = new SuperMode(attributeLightMode.getData_format(),
		attributeLightMode.getData_type(), attributeLightMode.getWritable(),
		attributeLightMode.getMode());

	TdbCollector collector = null;

	synchronized (tableCollector) {

	    collector = tableCollector.get(superMode);

	    if (collector == null) {

		try {
		    collector = create(attributeLightMode);
		} catch (final ArchivingException e) {
		    throw e;
		}

		collector.setDbProxy(dbProxy);
		collector.setDiaryLogger(m_logger);

		try {
		    collector.addSource(attributeLightMode, attributePerFile);
		} catch (final ArchivingException e) {
		    collector = null;
		    throw e;
		}
		tableCollector.put(superMode, collector);

	    } else {
		collector.addSource(attributeLightMode, attributePerFile);
	    }
	}
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
    private TdbCollector create(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	System.out.println("TdbCollectorFactory.create\r\n\t" + attributeLightMode.toString());
	final String name = attributeLightMode.getAttribute_complete_name();
	final int data_type = attributeLightMode.getData_type();
	final int data_format = attributeLightMode.getData_format();
	final int writable = attributeLightMode.getWritable();
	final TdbModeHandler modeHandler = new TdbModeHandler(attributeLightMode.getMode());
	TdbCollector collector = null;
	if (AttributeSupport.checkAttributeSupport(name, data_type, data_format, writable)) {
	    switch (data_format) { // [0 - > SCALAR] (1 - > SPECTRUM] [2 - >
	    // IMAGE]
	    case AttrDataFormat._SCALAR: // SCALAR
		switch (data_type) {
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
		case TangoConst.Tango_DEV_DOUBLE:
		case TangoConst.Tango_DEV_FLOAT:
		    collector = new NumberScalar(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		case TangoConst.Tango_DEV_BOOLEAN:
		    collector = new BooleanScalar(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		case TangoConst.Tango_DEV_STATE:
		    collector = new StateScalar(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		case TangoConst.Tango_DEV_STRING:
		    collector = new StringScalar(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		default:
		    generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		}
		break;
	    case AttrDataFormat._SPECTRUM: // SPECTRUM
		switch (data_type) {
		case TangoConst.Tango_DEV_SHORT:
		case TangoConst.Tango_DEV_USHORT:
		case TangoConst.Tango_DEV_LONG:
		case TangoConst.Tango_DEV_ULONG:
		case TangoConst.Tango_DEV_DOUBLE:
		case TangoConst.Tango_DEV_FLOAT:
		case TangoConst.Tango_DEV_CHAR:
		case TangoConst.Tango_DEV_UCHAR:
		    collector = new NumberSpectrum(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		case TangoConst.Tango_DEV_BOOLEAN:
		    collector = new BooleanSpectrum(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		case TangoConst.Tango_DEV_STRING:
		    collector = new StringSpectrum(modeHandler, m_currentDsPath, m_currentDbPath,
			    AttrWriteType.from_int(writable));
		    break;
		default:
		    generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		}
		break;
	    case AttrDataFormat._IMAGE: // IMAGE
		collector = new Image_RO(modeHandler, m_currentDsPath, m_currentDbPath,
			AttrWriteType.from_int(writable));
		break;
	    default:
		generateException(GlobalConst.DATA_FORMAT_EXCEPTION, data_format, name);
	    }
	}

	return collector;
    }

    public void destroy(final AttributeLightMode attributeLightMode) {
	final SuperMode superMode = new SuperMode(attributeLightMode.getData_format(),
		attributeLightMode.getData_type(), attributeLightMode.getWritable(),
		attributeLightMode.getMode());
	tableCollector.remove(superMode);
    }

    public void remove(final String attributeName) throws ArchivingException {
	// final List<SuperMode> modes = new ArrayList<SuperMode>();
	for (final Map.Entry<SuperMode, TdbCollector> entry : tableCollector.entrySet()) {
	    // final SuperMode mode = entry.getKey();
	    final TdbCollector collector = entry.getValue();
	    if (collector.getAttributeList().contains(attributeName)) {
		collector.removeSource(attributeName, false);
	    }
	    // if (collector.hasEmptyList()) {
	    // modes.add(mode);
	    // }
	}
	// XXX : collector is not removed from map, because a retry make
	// close/open file all the time
	// for (final SuperMode superMode : modes) {
	// tableCollector.remove(superMode);
	// }
    }

    public void removeAllForAttribute(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	SuperMode superMode;
	// HashSet<SuperMode> toRemove = new HashSet<SuperMode>();

	superMode = new SuperMode(attributeLightMode.getData_format(), attributeLightMode
		.getData_type(), attributeLightMode.getWritable(), attributeLightMode.getMode());

	final TdbCollector collector = tableCollector.get(superMode);

	if (collector != null) {
	    m_logger.trace(ILogger.LEVEL_INFO,
		    "===> Collector found : removeSource is requested ... ");
	    collector.removeSource(attributeLightMode.getAttribute_complete_name(), false);
	    m_logger.trace(ILogger.LEVEL_INFO, "===> ... removeSource is done");
	    // if (collector.hasEmptyList()) {
	    // toRemove.add(superMode);
	    // }
	}
	// XXX : collector is not removed from map, because a retry make
	// close/open file all the time
	// Iterator<SuperMode> toRemoveIterator = toRemove.iterator();
	// while (toRemoveIterator.hasNext()) {
	// tableCollector.remove(toRemoveIterator.next());
	// }

    }

    public String factoryAssessment() {

	final StringBuffer ass = new StringBuffer();

	if (tableCollector != null) {
	    if (!tableCollector.isEmpty()) {

		// to avoid collector addition during tableCollector scanning
		synchronized (tableCollector) {
		    final Collection<TdbCollector> collectors = tableCollector.values();
		    int i = 1;
		    final int size = collectors.size();
		    for (final TdbCollector tdbCollector : collectors) {
			ass.append("*********************************" + " " + i++ + "/" + size
				+ " " + "*********************************" + "\r\n");
			ass.append(tdbCollector.assessment());
		    }
		}
	    }
	}

	return ass.toString();
    }

    private static void generateException(final String cause, final int cause_value,
	    final String name) throws ArchivingException {
	final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
	final String reason = "Failed while executing TdbCollectorFactory.create() method...";
	final String desc = cause + " (" + cause_value + ") not supported !! [" + name + "]";
	throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "");
    }

    public short[] factoryLoadAssessment() {
	final short[] ret = new short[3];

	if (tableCollector != null) {
	    if (!tableCollector.isEmpty()) {
		final Collection<TdbCollector> collectors = tableCollector.values();
		for (final TdbCollector tdbCollector : collectors) {
		    final TdbCollector collector = tdbCollector;
		    final short[] collectorLoad = collector.loadAssessment();
		    ret[0] += collectorLoad[0];
		    ret[1] += collectorLoad[1];
		    ret[2] += collectorLoad[2];
		}

	    }
	}

	return ret;
    }
}
