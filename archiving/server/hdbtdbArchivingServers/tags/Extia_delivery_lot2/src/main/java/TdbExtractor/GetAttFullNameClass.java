/**
 * @author $Author: awo
 */
package TdbExtractor;

import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
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
    public GetAttFullNameClass(String name, int in, int out) {
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
    public GetAttFullNameClass(String name, int in, int out, String in_comments,
            String out_comments) {
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
    public GetAttFullNameClass(String name, int in, int out, String in_comments,
            String out_comments, DispLevel level) {
        super(name, in, out, in_comments, out_comments, level);
    }

    /**
     * return the result of the device's command.
     */
    public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
        Util.out2.println("GetAttFullName.execute(): arrived");
        short argin = extract_DevShort(in_any);
        return insert(((TdbExtractor) (device)).get_att_full_name(argin));
    }

    /**
     * Check if it is allowed to execute the command.
     */
    public boolean is_allowed(DeviceImpl device, Any data_in) {
        if (device.get_state() == DevState.OFF
                || device.get_state() == DevState.INIT) {
            // End of Generated Code

            // Re-Start of Generated Code
            return false;
        }
        return true;
    }
}
