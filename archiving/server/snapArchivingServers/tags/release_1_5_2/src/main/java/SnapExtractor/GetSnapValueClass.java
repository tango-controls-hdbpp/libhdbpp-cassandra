/**
 * 
 */
package SnapExtractor;

import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * @author AYADI
 *
 */
public class GetSnapValueClass extends Command implements TangoConst {

	/**
	 * @param s
	 * @param in
	 * @param out
	 * @param level
	 */
	public GetSnapValueClass(String s, int in, int out, DispLevel level) {
		super(s, in, out, level);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 * @param in
	 * @param out
	 * @param in_desc
	 * @param out_desc
	 * @param level
	 */
	public GetSnapValueClass(String s, int in, int out, String in_desc,
			String out_desc, DispLevel level) {
		super(s, in, out, in_desc, out_desc, level);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 * @param in
	 * @param out
	 * @param in_desc
	 * @param out_desc
	 */
	public GetSnapValueClass(String s, int in, int out, String in_desc,
			String out_desc) {
		super(s, in, out, in_desc, out_desc);
		// TODO Auto-generated constructor stub
	}

	public GetSnapValueClass(String s, int in, int out) {
		super(s, in, out);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.esrf.TangoDs.Command#execute(fr.esrf.TangoDs.DeviceImpl, org.omg.CORBA.Any)
	 */
	@Override
	public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
		// TODO Auto-generated method stub
		Util.out2.println("GetSnapValueClass.execute(): arrived");
		String[] in = extract_DevVarStringArray(in_any);
		int snapID = -1;
		try{
			snapID = Integer.parseInt(in[0]); 
			 if(snapID<0){
				 Except.throw_exception("INPUT_ERROR" ,
							"Invalid Snapshot ID" ,
					"SnapExtractor.GetSnapValue");
			 }
		}catch(Exception e){
			 Except.throw_exception("INPUT_ERROR" ,
						"Invalid Snapshot ID" ,
				"SnapExtractor.GetSnapValue");
		}

		String attr_name = in[1];
		return insert(((SnapExtractor)(device)).get_snap_value(snapID, attr_name));

	}
	//===============================================================
	/**
	 *	Check if it is allowed to execute the command.
	 */
	//===============================================================
	public boolean is_allowed(DeviceImpl device, Any data_in)
	{
		if (device.get_state() == DevState.OFF  ||
			device.get_state() == DevState.INIT  ||
			device.get_state() == DevState.FAULT  ||
			device.get_state() == DevState.EXTRACT  ||
			device.get_state() == DevState.UNKNOWN)
		{
			//	End of Generated Code

			//	Re-Start of Generated Code
			return false;
		}
		return true;
	}

}
