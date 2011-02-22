/**
 * 
 */
package SnapExtractor;

import org.omg.CORBA.Any;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DispLevel;
import fr.esrf.TangoDs.Command;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;

/**
 * @author AYADI
 *
 */
public class GetSnapIDClass extends Command implements TangoConst {

	/**
	 * @param s
	 * @param in
	 * @param out
	 */
	public GetSnapIDClass(String s, int in, int out) {
		super(s, in, out);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 * @param in
	 * @param out
	 * @param level
	 */
	public GetSnapIDClass(String s, int in, int out, DispLevel level) {
		super(s, in, out, level);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 * @param in
	 * @param out
	 * @param in_desc
	 * @param out_desc
	 */
	public GetSnapIDClass(String s, int in, int out, String in_desc, String out_desc) {
		super(s, in, out, in_desc, out_desc);
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
	public GetSnapIDClass(String s, int in, int out, String in_desc,
			String out_desc, DispLevel level) {
		super(s, in, out, in_desc, out_desc, level);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.esrf.TangoDs.Command#execute(fr.esrf.TangoDs.DeviceImpl, org.omg.CORBA.Any)
	 */
	@Override
	public Any execute(DeviceImpl device, Any in_any) throws DevFailed {
		// TODO Auto-generated method stub
		Util.out2.println("GetSnapIDClass.execute(): arrived");
		String[] in = extract_DevVarStringArray(in_any);
		int ctxID = Integer.parseInt(in[0]); 
		String[] criterions = getCriterions(in);
		
		return insert(((SnapExtractor)(device)).get_snap_id(ctxID,  criterions));

	}

	private String[] getCriterions(String[] in) {
		// TODO Auto-generated method stub
		String[] res = new String[in.length -1 ];
		for(int i=1; i<in.length;i++){
			res[i-1]  = in[i];
		}
		return res;
	}

}
