package SnapArchiver;

import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

public class TriggerLaunchSnapShotCmd extends Command implements TangoConst {

	public TriggerLaunchSnapShotCmd(String arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public TriggerLaunchSnapShotCmd(String name, int in, int out,
			String in_comments, String out_comments) {
		super(name, in, out, in_comments, out_comments);
	}

	public TriggerLaunchSnapShotCmd(String name, int in, int out,
			String in_comments, String out_comments, DispLevel level) {
		super(name, in, out, in_comments, out_comments, level);
	}

	@Override
	public Any execute(DeviceImpl arg0, Any in_any) throws DevFailed {
		Util.out2.println("TriggerLaunchSnapShotCmd.execute(): arrived");
		short argin = extract_DevShort(in_any);
		if (argin < 0) {
			Util.out2.println("Invalid Context ID");
			Except.throw_exception("INPUT_ERROR", "Invalid Context ID",
					"SnapArchiver.TriggerLaunchSnapShot");
		}

		return insert(((SnapArchiver) (arg0)).trigger_launch_snap_shot(argin));
	}

	public boolean is_allowed(DeviceImpl device, Any data_in) {
		return true;
	}
}
