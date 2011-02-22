//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Spectrum_RO.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.3  2010/12/16 10:20:50  abeilleg
// find bugs
//
// Revision 1.2  2010/12/15 16:08:53  abeilleg
// refactoring: try to remove some copy/paste
//
// Revision 1.1  2010/12/15 10:53:56  abeilleg
// refactoring: try to remove some copy/paste
//
// Revision 1.12  2007/06/01 09:45:53  pierrejoseph
// Attribute ArchiverCollectro.logger has been renamed in m_logger
//
// Revision 1.11  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.10  2007/02/13 14:41:45  ounsy
// added diary entry in the case of unexpected exceptions in addSource
//
// Revision 1.9  2007/02/13 14:19:16  ounsy
// corrected a bug in addSource: an infinite nnumber of FileTools instances could potentially be created
//
// Revision 1.8  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
//
// Revision 1.7  2006/10/19 12:25:51  ounsy
// modfiied the removeSource to take into account the new isAsuynchronous parameter
//
// Revision 1.6  2006/08/23 09:55:15  ounsy
// FileTools compatible with the new TDB file management
// + keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)
//
// Revision 1.5  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.4  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.3  2006/05/23 11:58:03  ounsy
// now checks the timeCondition condition before calling FileTools.processEvent
//
// Revision 1.2  2006/05/16 09:30:30  ounsy
// added what's necessary for the old files deletion mechanism
//
// Revision 1.1  2006/04/05 13:49:51  ounsy
// new types full support
//
// Revision 1.8  2006/03/28 11:14:38  ounsy
// better spectrum management
//
// Revision 1.7  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.6.8.2  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.6.8.1  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.6  2005/06/24 12:06:38  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.5  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.4.1  2005/06/13 13:49:34  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/04/08 15:37:06  chinkumo
// errorChange method filled.

// The aim of this method is to manage possible attribute's problem while polling attributes.

// In case of unavailable value, a record is nevertheless carried out with the event timestamp, but with a kind of NULL value.
//
// Revision 1.3  2005/02/04 17:10:39  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/31 15:09:04  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:26  chinkumo
// First commit (new architecture).
//
// Revision 1.5  2004/09/27 13:17:36  chinkumo
// The addSource method were improved : The two calls 'myFile.checkDirs();' + 'myFile.initFile();' were gathered into one single call (myFile.initialize();).
//
// Revision 1.4  2004/09/14 06:55:44  chinkumo
// Some unused 'import' were removed.
// Some error messages were re-written to fit the 'error policy' recently decided.
//
// Revision 1.3  2004/09/01 15:38:21  chinkumo
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

package TdbArchiver.Collector.spectrum;

import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IEntity;
import fr.esrf.tangoatk.core.IStringSpectrum;
import fr.esrf.tangoatk.core.IStringSpectrumListener;
import fr.esrf.tangoatk.core.StringSpectrumEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

@SuppressWarnings("serial")
public class StringSpectrum extends TdbCollector implements IStringSpectrumListener {

    public StringSpectrum(final TdbModeHandler modeHandler, final String currentDsPath,
	    final String currentDbPath, final AttrWriteType writableType) {
	super(modeHandler, currentDsPath, currentDbPath, writableType);
    }

    @Override
    public void addListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IStringSpectrum) {
	    ((IStringSpectrum) attribute).addListener(this);
	    ((IStringSpectrum) attribute).addErrorListener(this);
	}
    }

    @Override
    public void removeListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IStringSpectrum) {
	    ((IStringSpectrum) attribute).removeListener(this);
	    ((IStringSpectrum) attribute).removeErrorListener(this);
	}
    }

    @Override
    public void errorChange(final ErrorEvent errorEvent) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	Util.out3.println("StringSpectrum_RO.errorChange : "
		+ "Unable to read the attribute named " + errorEvent.getSource().toString());
	final String[] value = null;
	final SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
	spectrumEvent_ro.setAttribute_complete_name(((IStringSpectrum) errorEvent.getSource())
		.getName());
	spectrumEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
	spectrumEvent_ro.setValue(value);
	processEventSpectrum(spectrumEvent_ro, tryNumber);
    }

    public void stringSpectrumChange(final StringSpectrumEvent event) {
	final String[] value = event.getValue();
	try {
	    if (getWritableValue().equals(AttrWriteType.READ)) {

		final SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		spectrumEvent_ro.setAttribute_complete_name(((IStringSpectrum) event.getSource())
			.getName());
		spectrumEvent_ro.setData_type(((IStringSpectrum) event.getSource()).getAttribute()
			.getType());
		spectrumEvent_ro.setDim_x(value.length);
		spectrumEvent_ro.setTimeStamp(event.getTimeStamp());
		spectrumEvent_ro.setValue(value);
		processEventSpectrum(spectrumEvent_ro, DEFAULT_TRY_NUMBER);
	    } else {
		final SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
		spectrumEvent_rw.setAttribute_complete_name(((IStringSpectrum) event.getSource())
			.getName());
		spectrumEvent_rw.setData_type(((IStringSpectrum) event.getSource()).getAttribute()
			.getType());
		spectrumEvent_rw.setDim_x(((IStringSpectrum) event.getSource()).getXDimension());
		spectrumEvent_rw.setTimeStamp(event.getTimeStamp());
		spectrumEvent_rw.setValue(value);

		processEventSpectrum(spectrumEvent_rw, DEFAULT_TRY_NUMBER);
	    }
	} catch (final DevFailed devFailed) {
	    Except.print_exception(devFailed);
	}

    }

    public void stateChange(final AttributeStateEvent event) {
    }

    public void processEventSpectrum(final SpectrumEvent_RO spectrumEvent_ro, final int try_number) {
	Util.out4.println("StringSpectrum_RO.processEventSpectrum");

	final boolean timeCondition = super.isDataArchivableTimestampWise(spectrumEvent_ro);
	if (!timeCondition) {
	    return;
	}

	try {
	    filesNames.get(spectrumEvent_ro.getAttribute_complete_name()).processEventSpectrum(
		    spectrumEvent_ro);
	    super.setLastTimestamp(spectrumEvent_ro);
	} catch (final Exception e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "StringSpectrum_RO.processEventSpectrum" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    e.printStackTrace();
	}
    }

    public void processEventSpectrum(final SpectrumEvent_RW spectrumEvent_rw, final int try_number) {
	Util.out4.println("StringSpectrum_RW.processEventSpectrum");

	final boolean timeCondition = super.isDataArchivableTimestampWise(spectrumEvent_rw);
	if (!timeCondition) {
	    return;
	}

	try {
	    filesNames.get(spectrumEvent_rw.getAttribute_complete_name()).processEventSpectrum(
		    spectrumEvent_rw);
	    super.setLastTimestamp(spectrumEvent_rw);
	} catch (final Exception e) {
	    Util.out2.println("ERROR !! " + "\r\n" + "\t Origin : \t "
		    + "StringSpectrum_RW.processEventSpectrum" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    e.printStackTrace();
	}
    }
}
