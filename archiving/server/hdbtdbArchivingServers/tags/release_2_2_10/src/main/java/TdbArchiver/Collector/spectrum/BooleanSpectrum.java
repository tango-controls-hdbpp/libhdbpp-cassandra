package TdbArchiver.Collector.spectrum;

import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.BooleanSpectrumEvent;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IBooleanSpectrum;
import fr.esrf.tangoatk.core.IBooleanSpectrumListener;
import fr.esrf.tangoatk.core.IEntity;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

@SuppressWarnings("serial")
public class BooleanSpectrum extends TdbCollector implements IBooleanSpectrumListener {

    public BooleanSpectrum(final TdbModeHandler modeHandler, final String currentDsPath,
	    final String currentDbPath, final AttrWriteType writableType) {
	super(modeHandler, currentDsPath, currentDbPath, writableType);
    }

    @Override
    public void addListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IBooleanSpectrum) {
	    ((IBooleanSpectrum) attribute).addBooleanSpectrumListener(this);
	    ((IBooleanSpectrum) attribute).addErrorListener(this);
	}
    }

    @Override
    public void removeListeners(final IEntity attribute) throws ArchivingException {
	if (attribute instanceof IBooleanSpectrum) {
	    ((IBooleanSpectrum) attribute).removeBooleanSpectrumListener(this);
	    ((IBooleanSpectrum) attribute).removeErrorListener(this);
	}
    }

    @Override
    public void errorChange(final ErrorEvent errorEvent) {
	final int tryNumber = DEFAULT_TRY_NUMBER;
	Util.out3.println("BooleanSpectrum_RO.errorChange : "
		+ "Unable to read the attribute named " + errorEvent.getSource().toString());
	final Boolean[] value = null;
	final SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
	spectrumEvent_ro.setAttribute_complete_name(((IBooleanSpectrum) errorEvent.getSource())
		.getName());
	spectrumEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
	spectrumEvent_ro.setValue(value);
	processEventSpectrum(spectrumEvent_ro, tryNumber);
    }

    public void booleanSpectrumChange(final BooleanSpectrumEvent event) {

	final boolean[] spectrumvalue = event.getValue();
	Boolean[] value;
	if (spectrumvalue == null) {
	    value = null;
	} else {
	    value = new Boolean[spectrumvalue.length];
	    for (int i = 0; i < spectrumvalue.length; i++) {
		value[i] = new Boolean(spectrumvalue[i]);
	    }
	}
	try {
	    if (getWritableValue().equals(AttrWriteType.READ)) {

		final SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		spectrumEvent_ro.setAttribute_complete_name(((IBooleanSpectrum) event.getSource())
			.getName());
		spectrumEvent_ro.setDim_x(value.length);
		spectrumEvent_ro.setTimeStamp(event.getTimeStamp());
		spectrumEvent_ro.setValue(value);
		spectrumEvent_ro.setData_type(((IBooleanSpectrum) event.getSource()).getAttribute()
			.getType());
		processEventSpectrum(spectrumEvent_ro, DEFAULT_TRY_NUMBER);
	    } else {
		final SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
		spectrumEvent_rw.setAttribute_complete_name(((IBooleanSpectrum) event.getSource())
			.getName());
		spectrumEvent_rw.setData_type(((IBooleanSpectrum) event.getSource()).getAttribute()
			.getType());
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
	Util.out4.println("BooleanSpectrum_RO.processEventSpectrum");

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
		    + "BooleanSpectrum_RO.processEventSpectrum" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    e.printStackTrace();
	}
    }

    public void processEventSpectrum(final SpectrumEvent_RW spectrumEvent_rw, final int try_number) {
	Util.out4.println("BooleanSpectrum_RW.processEventSpectrum");

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
		    + "BooleanSpectrum_RW.processEventSpectrum" + "\r\n" + "\t Reason : \t "
		    + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage()
		    + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
	    e.printStackTrace();
	}
    }
}