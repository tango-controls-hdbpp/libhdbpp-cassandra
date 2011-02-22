package TdbArchiver;

import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.TangoConst;

/**
 * Class Description: Command that stops the archiving of an attibute. This
 * command need an AttributeLightMode type object. An AttributeLightMode type
 * object encapsulate all the informations needed to found the Collector in
 * charge of the archiving of the specified attribute The informations needed
 * are the type, the format, the writable property and the archiving mode
 */

public class RetryForAttributeCmd extends Command implements TangoConst {
	// ===============================================================
	/**
	 * Constructor for Command class StopArchiveAttCmd
	 * 
	 * @param name
	 *            command name
	 * @param in
	 *            argin type
	 * @param out
	 *            argout type
	 */
	// ===============================================================
	public RetryForAttributeCmd(String name, int in, int out) {
		super(name, in, out);
	}

	// ===============================================================
	/**
	 * Constructor for Command class StopArchiveAttCmd
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
	public RetryForAttributeCmd(String name, int in, int out,
			String in_comments, String out_comments) {
		super(name, in, out, in_comments, out_comments);
	}

	// ===============================================================
	/**
	 * Constructor for Command class StopArchiveAttCmd
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
	public RetryForAttributeCmd(String name, int in, int out,
			String in_comments, String out_comments, DispLevel level) {
		super(name, in, out, in_comments, out_comments, level);
	}

	// ===============================================================
	/**
	 * return the result of the device's command.
	 */
	// ===============================================================
	public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
		String argin = super.extract_DevString(in_any);
		return insert(((TdbArchiver) (device)).retry_for_attribute(argin));
	}

	// ===============================================================
	/**
	 * Check if it is allowed to execute the command.
	 */
	// ===============================================================
	public boolean is_allowed(DeviceImpl device, Any data_in) {
		// if ( device.get_state() == DevState.FAULT )
		if (device.get_state() != DevState.ON) {
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
 * /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/RetryForAttributeCmd
 * .java,v $
 */
