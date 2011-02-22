/**
 * @author $Author: awo
 */
package HdbExtractor;

import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

public class GetAttFullNameClass extends Command implements TangoConst {

	/**
	 * Constructor for Command class GetAttFullName
	 * 
	 * @param name
	 *            command name
	 * @param in
	 *            argin type
	 * @param out
	 *            argout type
	 */
	public GetAttFullNameClass(final String name, final int in, final int out) {
		super(name, in, out);
	}

	/**
	 * Constructor for Command class GetAttFullName
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
	public GetAttFullNameClass(final String name, final int in, final int out, final String in_comments, final String out_comments) {
		super(name, in, out, in_comments, out_comments);
	}

	/**
	 * Constructor for Command class GetAttFullName
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
	public GetAttFullNameClass(final String name, final int in, final int out, final String in_comments, final String out_comments, final DispLevel level) {
		super(name, in, out, in_comments, out_comments, level);
	}

	/**
	 * return the result of the device's command.
	 */
	@Override
	public Any execute(final DeviceImpl device, final Any in_any) throws DevFailed {
		Util.out2.println("GetAttFullName.execute(): arrived");
		final short argin = extract_DevShort(in_any);

		if (!(device instanceof HdbExtractor)) {
			Except.throw_exception("DEVICE_ERROR", "Device parameter is not instance of HdbExtractor", "HdbExtractor");
		}

		return insert(((HdbExtractor) device).get_att_full_name(argin));
	}

	/**
	 * Check if it is allowed to execute the command.
	 */
	@Override
	public boolean is_allowed(final DeviceImpl device, final Any data_in) {
		if (device.get_state() == DevState.OFF || device.get_state() == DevState.INIT) {
			// End of Generated Code

			// Re-Start of Generated Code
			return false;
		}
		return true;
	}
}
