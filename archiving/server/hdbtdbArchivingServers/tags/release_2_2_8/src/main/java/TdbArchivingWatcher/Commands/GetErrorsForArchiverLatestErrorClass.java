/**
 * @author	$Author$
 * @version	$Revision$
 */
package TdbArchivingWatcher.Commands;

import org.omg.CORBA.Any;

import TdbArchivingWatcher.TdbArchivingWatcher;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * Class Description:
 * 
 */

public class GetErrorsForArchiverLatestErrorClass extends Command implements TangoConst {
	// ===============================================================
	/**
	 * Constructor for Command class GetErrorsForArchiverCurrentClass
	 * 
	 * @param name
	 *            command name
	 * @param in
	 *            argin type
	 * @param out
	 *            argout type
	 */
	// ===============================================================
	public GetErrorsForArchiverLatestErrorClass(String name, int in, int out) {
		super(name, in, out);
	}

	// ===============================================================
	/**
	 * Constructor for Command class GetErrorsForArchiverCurrentClass
	 * 
	 * @param name
	 *            command name
	 * @param in
	 *            argin type
	 * @param in_comments
	 *            argin description
	 * @param out
	 *            argout type
	 * @param out_comments
	 *            argout description
	 */
	// ===============================================================
	public GetErrorsForArchiverLatestErrorClass(String name, int in, int out, String in_comments, String out_comments) {
		super(name, in, out, in_comments, out_comments);
	}

	// ===============================================================
	/**
	 * Constructor for Command class GetErrorsForArchiverCurrentClass
	 * 
	 * @param name
	 *            command name
	 * @param in
	 *            argin type
	 * @param in_comments
	 *            argin description
	 * @param out
	 *            argout type
	 * @param out_comments
	 *            argout description
	 * @param level
	 *            The command display type OPERATOR or EXPERT
	 */
	// ===============================================================
	public GetErrorsForArchiverLatestErrorClass(String name, int in, int out, String in_comments, String out_comments, DispLevel level) {
		super(name, in, out, in_comments, out_comments, level);
	}

	// ===============================================================
	/**
	 * return the result of the device's command.
	 */
	// ===============================================================
	@Override
	public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
		if (!(device instanceof TdbArchivingWatcher)) {
			Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of TdbArchivingWatcher", "TdbArchivingWatcher");
		}

		Util.out2.println("GetErrorsForArchiverCurrentClass.execute(): arrived");
		String argin = extract_DevString(in_any);
		return insert(((TdbArchivingWatcher) (device)).get_errors_for_archiver_latest_error(argin));
	}

	// ===============================================================
	/**
	 * Check if it is allowed to execute the command.
	 */
	// ===============================================================
	@Override
	public boolean is_allowed(DeviceImpl device, Any data_in) {
		if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
			// End of Generated Code

			// Re-Start of Generated Code
			return false;
		}
		return true;
	}
}
// -----------------------------------------------------------------------------
/*
 * end of $Source:
 * /cvsroot/tango-cs/archiving/server/hdbtdbArchivingServers/src/
 * main/java/TdbArchivingWatcher
 * /Commands/GetErrorsForArchiverLatestErrorClass.java,v $
 */
