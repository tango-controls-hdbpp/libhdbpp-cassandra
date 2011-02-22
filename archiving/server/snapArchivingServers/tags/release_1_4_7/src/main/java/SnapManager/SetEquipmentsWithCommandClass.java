
/**
 * @author	$Author$
 * @version	$Revision$
 */
package SnapManager;


import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * Class Description:
 * This command is used to set command to equipments.
 */


public class SetEquipmentsWithCommandClass extends Command implements TangoConst
{
	//===============================================================
	/**
	 * Constructor for Command class SetEquipmentsWithCommandClass
	 *
	 * @param	name	command name
	 * @param	in	argin type
	 * @param	out	argout type
	 */
	//===============================================================
	public SetEquipmentsWithCommandClass(String name , int in , int out)
	{
		super(name , in , out);
	}

	//===============================================================
	/**
	 * Constructor for Command class SetEquipmentsWithCommandClass
	 *
	 * @param	name command name
	 * @param	in argin type
	 * @param	in_comments argin description
	 * @param	out argout type
	 * @param	out_comments argout description
	 */
	//===============================================================
	public SetEquipmentsWithCommandClass(String name , int in , int out , String in_comments , String out_comments)
	{
		super(name , in , out , in_comments , out_comments);
	}
	//===============================================================
	/**
	 * Constructor for Command class SetEquipmentsWithCommandClass
	 *
	 * @param	name command name
	 * @param	in argin type
	 * @param	in_comments argin description
	 * @param	out argout type
	 * @param	out_comments argout description
	 * @param	level The command display type OPERATOR or EXPERT
	 */
	//===============================================================
	public SetEquipmentsWithCommandClass(String name , int in , int out , String in_comments , String out_comments , DispLevel level)
	{
		super(name , in , out , in_comments , out_comments , level);
	}
	//===============================================================
	/**
	 * return the result of the device's command.
	 */
	//===============================================================
	public Any execute(DeviceImpl device , Any in_any) throws DevFailed
	{
		Util.out2.println("SetEquipmentsWithCommandClass.execute(): arrived");
		String[] argin = extract_DevVarStringArray(in_any);
		String command_name = argin[0];
		int snap_id = Integer.parseInt(argin[1]);
		( ( SnapManager ) ( device ) ).set_equipments_with_command(command_name, snap_id);
		return insert();
	}

	//===============================================================
	/**
	 * Check if it is allowed to execute the command.
	 */
	//===============================================================
	public boolean is_allowed(DeviceImpl device , Any data_in)
	{
		//	End of Generated Code

		//	Re-Start of Generated Code
		return true;
	}
}

//-----------------------------------------------------------------------------
/* end of $Source$ */
