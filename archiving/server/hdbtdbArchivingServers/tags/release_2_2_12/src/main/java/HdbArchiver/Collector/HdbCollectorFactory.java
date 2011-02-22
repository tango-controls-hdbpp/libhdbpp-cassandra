//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/HdbCollectorFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HdbCollectorFactory.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author: ounsy $
//
// $Revision: 1.26 $
//
// $Log: HdbCollectorFactory.java,v $
// Revision 1.26  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.25  2007/01/09 15:15:24  ounsy
// put the Giacomo version back for removeAllForAttribute
//
// Revision 1.24  2006/11/24 14:54:17  ounsy
// we re-use the old removeAllForAttribute
//
// Revision 1.23  2006/11/24 14:04:16  ounsy
// the diary entry in case of missing collector is now done by he archiver
//
// Revision 1.22  2006/11/24 13:19:35  ounsy
// TdbCollectorFactory.get(attrLightMode) has a new parameter "doCreateCollectorIfMissing". A missing collector will only be created if this is true, otherwise a message in logged into the archiver's diary and an exception launched
//
// Revision 1.21  2006/11/15 15:48:56  ounsy
// minorc hanges
//
// Revision 1.20  2006/11/13 15:56:20  ounsy
// replaced the removeAllForAttribute(String) by removeAllForAttribute(AttributeLightMode) (cf. the Giacomo modification)
//
// Revision 1.19  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.18  2006/07/25 16:24:18  ounsy
// HdbCollectorFactory no more static
//
// Revision 1.17  2006/07/25 09:47:01  ounsy
// added a factoryLoadAssessment() method
//
// Revision 1.16  2006/06/27 12:07:13  ounsy
// Corrected the bug that made the collectors all have the same logger for a given
// SuperMode (instead of one logger per TdbArchiver)
//
// Revision 1.15  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.14  2006/06/15 15:16:39  ounsy
// added a protection against the ConcurrentModificationException that sometimes occur when launching the archivers
//
// Revision 1.13  2006/06/13 13:28:20  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.12  2006/05/12 09:20:17  ounsy
// list concurrent modification bug correction
//
// Revision 1.11  2006/05/03 14:28:44  ounsy
// renamed Spectrum_... into NumberSpectrum_...
//
// Revision 1.10  2006/04/11 09:12:39  ounsy
// added some attribute removing methods
//
// Revision 1.9  2006/04/05 13:49:52  ounsy
// new types full support
//
// Revision 1.8  2006/03/10 11:58:29  ounsy
// state and string support
//
// Revision 1.7  2006/02/06 13:04:20  ounsy
// added support for spectrum RO/RW
//
// Revision 1.6  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.5.10.3  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.5.10.2  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.5.10.1  2005/09/09 10:10:49  chinkumo
// Since the collecting politic was simplified and improved CollectorFactory was modified.
//
// Revision 1.5  2005/06/24 12:06:27  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.4  2005/06/14 10:30:27  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.3.4.1  2005/06/13 14:09:56  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.3  2005/02/04 17:10:15  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:14  chinkumo
// First commit (new architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package HdbArchiver.Collector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import HdbArchiver.Collector.Tools.SuperMode;
import HdbArchiver.Collector.image.Image_RO;
import HdbArchiver.Collector.scalar.BooleanScalar_RO;
import HdbArchiver.Collector.scalar.BooleanScalar_RW;
import HdbArchiver.Collector.scalar.BooleanScalar_WO;
import HdbArchiver.Collector.scalar.NumberScalar_RO;
import HdbArchiver.Collector.scalar.NumberScalar_RW;
import HdbArchiver.Collector.scalar.NumberScalar_WO;
import HdbArchiver.Collector.scalar.StateScalar_RO;
import HdbArchiver.Collector.scalar.StringScalar_RO;
import HdbArchiver.Collector.scalar.StringScalar_RW;
import HdbArchiver.Collector.scalar.StringScalar_WO;
import HdbArchiver.Collector.spectrum.BooleanSpectrum_RO;
import HdbArchiver.Collector.spectrum.BooleanSpectrum_RW;
import HdbArchiver.Collector.spectrum.NumberSpectrum_RO;
import HdbArchiver.Collector.spectrum.NumberSpectrum_RW;
import HdbArchiver.Collector.spectrum.StringSpectrum_RO;
import HdbArchiver.Collector.spectrum.StringSpectrum_RW;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeSupport;

public class HdbCollectorFactory {
    private final Map<SuperMode, HdbCollector> tableCollector = new HashMap<SuperMode, HdbCollector>();

    public HdbCollectorFactory() {
    }

    /**
     * This method returns the instance of HdbCollector associated / associable
     * to an attribute. In this method, attributes are not grouped by mode.
     * 
     * @param attributeLightMode
     *            Attribute associated to the looked collector.
     * @return the instance of HdbCollector associated / associable to an
     *         attribute.
     */
    public HdbCollector get(final AttributeLightMode attributeLightMode) {
	final SuperMode superMode = new SuperMode(attributeLightMode.getData_format(),
		attributeLightMode.getData_type(), attributeLightMode.getWritable(),
		attributeLightMode.getMode());
	return tableCollector.get(superMode);
    }

    /**
     * This method create a new HdbCollector instance if required associated to
     * an attribute. In this method, attributes are not grouped by mode.
     * 
     * @param attributeLightMode
     *            Attribute associated to the looked collector.
     */
    public void createCollectorAndAddSource(final AttributeLightMode attributeLightMode,
	    final ILogger logger, final DbProxy dbProxy) throws ArchivingException {

	logger.trace(ILogger.LEVEL_DEBUG, "createCollectorAndAddSource for " + attributeLightMode);
	final SuperMode superMode = new SuperMode(attributeLightMode.getData_format(),
		attributeLightMode.getData_type(), attributeLightMode.getWritable(),
		attributeLightMode.getMode());
	HdbCollector collector = tableCollector.get(superMode);
	if (collector == null) {
	    logger.trace(ILogger.LEVEL_DEBUG, "new collector ");
	    try {
		collector = create(attributeLightMode);
	    } catch (final ArchivingException e) {
		throw e;
	    }
	    collector.setDbProxy(dbProxy);
	    collector.setDiaryLogger(logger);

	    try {
		collector.addSource(attributeLightMode);
	    } catch (final ArchivingException e) {
		collector = null;
		throw e;
	    }
	    tableCollector.put(superMode, collector);

	} else {
	    logger.trace(ILogger.LEVEL_DEBUG, "use existing collector " + collector.assessment());
	    collector.addSource(attributeLightMode);
	}
    }

    /**
     * This method returns the instance of Hdb_Collector associated / associable
     * to an attribute. In this method, attributes are not grouped by mode.
     * 
     * @param attributeLightMode
     *            Attribute associated to the looked collector.
     * @return the instance of Hdb_Collector associated / associable to an
     *         attribute.
     */

    /**
     * This method returns the instance of HdbCollector associated / associable
     * to an attribute. In this method, attributes are grouped by mode.
     * 
     * @param attributeLightMode
     *            Attribute associated to the looked collector.
     * @return the instance of HdbCollector associated / associable to an
     *         attribute.
     */
    public HdbCollector create(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	// System.out.println("HdbCollectorFactory.create\r\n\t" +
	// attributeLightMode.toString());
	final String name = attributeLightMode.getAttribute_complete_name();
	final int data_type = attributeLightMode.getData_type();
	final int data_format = attributeLightMode.getData_format();
	final int writable = attributeLightMode.getWritable();
	final HdbModeHandler modeHandler = new HdbModeHandler(attributeLightMode.getMode());
	HdbCollector collector = null;
	if (AttributeSupport.checkAttributeSupport(name, data_type, data_format, writable)) {
	    switch (data_format) { // [0 - > SCALAR] (1 - > SPECTRUM] [2 - >
	    // IMAGE]
	    case AttrDataFormat._SCALAR: // SCALAR
		switch (writable) { // [0 - > READ] [1 - > READ_WITH_WRITE] ([2
		// - > WRITE)] [3 - > READ_WRITE]
		case AttrWriteType._READ: // READ -> 0
		    switch (data_type) {
		    case TangoConst.Tango_DEV_SHORT:
		    case TangoConst.Tango_DEV_USHORT:
		    case TangoConst.Tango_DEV_LONG:
		    case TangoConst.Tango_DEV_ULONG:
		    case TangoConst.Tango_DEV_DOUBLE:
		    case TangoConst.Tango_DEV_FLOAT:
			collector = new NumberScalar_RO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_BOOLEAN:
			collector = new BooleanScalar_RO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STATE:
			collector = new StateScalar_RO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STRING:
			collector = new StringScalar_RO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_CHAR:
		    case TangoConst.Tango_DEV_UCHAR:
		    default:
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;
		case AttrWriteType._READ_WITH_WRITE: // READ_WITH_WRITE -> 1
		    switch (data_type) {
		    case TangoConst.Tango_DEV_SHORT:
		    case TangoConst.Tango_DEV_USHORT:
		    case TangoConst.Tango_DEV_LONG:
		    case TangoConst.Tango_DEV_ULONG:
		    case TangoConst.Tango_DEV_DOUBLE:
		    case TangoConst.Tango_DEV_FLOAT:
			collector = new NumberScalar_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_BOOLEAN:
			collector = new BooleanScalar_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STRING:
			collector = new StringScalar_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_CHAR:
		    case TangoConst.Tango_DEV_UCHAR:
		    case TangoConst.Tango_DEV_STATE:
		    default:
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;
		case AttrWriteType._WRITE: // WRITE -> 2
		    switch (data_type) {
		    case TangoConst.Tango_DEV_SHORT:
		    case TangoConst.Tango_DEV_USHORT:
		    case TangoConst.Tango_DEV_LONG:
		    case TangoConst.Tango_DEV_ULONG:
		    case TangoConst.Tango_DEV_DOUBLE:
		    case TangoConst.Tango_DEV_FLOAT:
			collector = new NumberScalar_WO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_BOOLEAN:
			collector = new BooleanScalar_WO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STRING:
			collector = new StringScalar_WO(modeHandler);
			break;
		    default:
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;
		case AttrWriteType._READ_WRITE: // READ_WRITE -> 3
		    switch (data_type) {
		    case TangoConst.Tango_DEV_SHORT:
		    case TangoConst.Tango_DEV_FLOAT:
		    case TangoConst.Tango_DEV_USHORT:
		    case TangoConst.Tango_DEV_LONG:
		    case TangoConst.Tango_DEV_ULONG:
		    case TangoConst.Tango_DEV_DOUBLE:
			collector = new NumberScalar_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_BOOLEAN:
			collector = new BooleanScalar_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STRING:
			collector = new StringScalar_RW(modeHandler);
			break;
		    default:
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;
		default:
		    generateException(GlobalConst.DATA_WRITABLE_EXCEPTION, writable, name);
		}
		break;
	    case AttrDataFormat._SPECTRUM: // SPECTRUM
		// collector = new NumberSpectrum_RO(modeHandler);
		switch (writable)// [0 - > READ] [1 - > READ_WITH_WRITE] ([2 - >
		// WRITE)] [3 - > READ_WRITE]
		{
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
			collector = new NumberSpectrum_RO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_BOOLEAN:
			collector = new BooleanSpectrum_RO(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STRING:
			collector = new StringSpectrum_RO(modeHandler);
			break;
		    default:
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;
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
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;

		case AttrWriteType._READ_WRITE:
		case AttrWriteType._READ_WITH_WRITE:
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
			collector = new NumberSpectrum_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_BOOLEAN:
			collector = new BooleanSpectrum_RW(modeHandler);
			break;
		    case TangoConst.Tango_DEV_STRING:
			collector = new StringSpectrum_RW(modeHandler);
			break;
		    default:
			generateException(GlobalConst.DATA_TYPE_EXCEPTION, data_type, name);
		    }
		    break;
		}
		break;
	    case AttrDataFormat._IMAGE: // IMAGE
		collector = new Image_RO(modeHandler);
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
	final List<SuperMode> modes = new ArrayList<SuperMode>();
	for (final Map.Entry<SuperMode, HdbCollector> entry : tableCollector.entrySet()) {
	    final SuperMode mode = entry.getKey();
	    final HdbCollector collector = entry.getValue();
	    if (collector.getAttributeList().contains(attributeName)) {
		collector.removeSource(attributeName);
	    }
	    if (collector.hasEmptyList()) {
		modes.add(mode);
	    }
	}
	for (final SuperMode superMode : modes) {
	    tableCollector.remove(superMode);
	}
    }

    // GIACOMO VERSION
    public void removeAllForAttribute(final AttributeLightMode attributeLightMode)
	    throws ArchivingException {
	SuperMode superMode;
	final HashSet<SuperMode> toRemove = new HashSet<SuperMode>();
	superMode = new SuperMode(attributeLightMode.getData_format(), attributeLightMode
		.getData_type(), attributeLightMode.getWritable(), attributeLightMode.getMode());
	final HdbCollector collector = tableCollector.get(superMode);

	if (collector != null) {
	    // collector.
	    collector.removeSource(attributeLightMode.getAttribute_complete_name());
	    if (collector.hasEmptyList()) {
		toRemove.add(superMode);
	    }
	}
	final Iterator<SuperMode> toRemoveIterator = toRemove.iterator();
	while (toRemoveIterator.hasNext()) {
	    tableCollector.remove(toRemoveIterator.next());
	}

    }

    public String factoryAssessment() {
	final StringBuffer ass = new StringBuffer();
	if (tableCollector != null) {

	    if (!tableCollector.isEmpty()) {
		final Collection<HdbCollector> collectors = tableCollector.values();
		int i = 1;
		final int size = collectors.size();
		for (final HdbCollector hdbCollector : collectors) {
		    ass.append("*********************************" + " " + i++ + "/" + size + " "
			    + "*********************************" + "\r\n");
		    ass.append(hdbCollector.assessment());
		}

	    }
	}
	return ass.toString();
    }

    public short[] factoryLoadAssessment() {
	final short[] ret = new short[3];
	synchronized (tableCollector) {
	    if (tableCollector != null) {

		if (!tableCollector.isEmpty()) {

		    // Collection<HdbCollector> collectors =
		    // tableCollector.values();
		    for (final HdbCollector collector : tableCollector.values()) {
			final short[] collectorLoad = collector.loadAssessment();
			ret[0] += collectorLoad[0];
			ret[1] += collectorLoad[1];
			ret[2] += collectorLoad[2];
		    }
		}
	    }
	}

	return ret;
    }

    private static void generateException(final String cause, final int cause_value,
	    final String name) throws ArchivingException {
	final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
	final String reason = "Failed while executing HdbCollectorFactory.create() method...";
	final String desc = cause + " (" + cause_value + ") not supported !! [" + name + "]";
	throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc, "");
    }
}
