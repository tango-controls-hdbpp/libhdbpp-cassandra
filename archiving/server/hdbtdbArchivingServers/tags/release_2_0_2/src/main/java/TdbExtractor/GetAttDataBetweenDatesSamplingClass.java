/**
 * @author  $Author$
 * @version $Revision$
 */
package TdbExtractor;


import org.omg.CORBA.*;
import fr.esrf.Tango.*;
import fr.esrf.TangoDs.*;

/**
 * Class Description:
 * Retrieves data beetwen two dates, for a given scalar attribute.
 * Create a dynamic attribute, retrieve data from database and prepare result for attribute_history call.
 */


public class GetAttDataBetweenDatesSamplingClass extends Command implements TangoConst
{
    //===============================================================
    /**
     * Constructor for Command class GetAttDataBetweenDatesClass
     *
     * @param   name    command name
     * @param   in  argin type
     * @param   out argout type
     */
    //===============================================================
    public GetAttDataBetweenDatesSamplingClass(String name , int in , int out)
    {
        super(name , in , out);
    }

    //===============================================================
    /**
     * Constructor for Command class GetAttDataBetweenDatesClass
     *
     * @param   name command name
     * @param   in argin type
     * @param   in_comments argin description
     * @param   out argout type
     * @param   out_comments argout description
     */
    //===============================================================
    public GetAttDataBetweenDatesSamplingClass(String name , int in , int out , String in_comments , String out_comments)
    {
        super(name , in , out , in_comments , out_comments);
    }
    //===============================================================
    /**
     * Constructor for Command class GetAttDataBetweenDatesClass
     *
     * @param   name command name
     * @param   in argin type
     * @param   in_comments argin description
     * @param   out argout type
     * @param   out_comments argout description
     * @param   level The command display type OPERATOR or EXPERT
     */
    //===============================================================
    public GetAttDataBetweenDatesSamplingClass(String name , int in , int out , String in_comments , String out_comments , DispLevel level)
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
        Util.out2.println("GetAttDataBetweenDatesSamplingClass.execute(): arrived");
        String[] argin = extract_DevVarStringArray(in_any);
        return insert(( ( TdbExtractor ) ( device ) ).get_att_data_between_dates_sampling(argin));
    }

    //===============================================================
    /**
     * Check if it is allowed to execute the command.
     */
    //===============================================================
    public boolean is_allowed(DeviceImpl device , Any data_in)
    {
        //  End of Generated Code

        //  Re-Start of Generated Code
        return true;
    }
}

//-----------------------------------------------------------------------------
/* end of $Source$ */
