package fr.soleil.archiving.utils;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;

public class TangoStateUtils {

    public static void setFault(final DeviceImpl device, final String msg) {
	device.set_state(DevState.FAULT);
	device.set_status("ERROR " + msg);
    }

    public static void setDisable(final DeviceImpl device, final String msg) {
	device.set_state(DevState.DISABLE);
	device.set_status("ERROR " + msg);
    }

    public static void setRunning(final DeviceImpl device) {
	device.set_state(DevState.RUNNING);
	device.set_status("Device is RUNNING");
    }

    public static void setOn(final DeviceImpl device) {
	device.set_state(DevState.ON);
	device.set_status("Device is ON");
    }

    public static void isAllowed(final DeviceImpl device) throws DevFailed {
	if (device.get_state().equals(DevState.FAULT)
		|| device.get_state().equals(DevState.DISABLE)
		|| device.get_state().equals(DevState.RUNNING)) {
	    Except.throw_exception("NOT_ALLOWED", "action is not allowed while device is in "
		    + TangoConst.Tango_DevStateName[device.get_state().value()], "isAllowed");
	}
    }
}
