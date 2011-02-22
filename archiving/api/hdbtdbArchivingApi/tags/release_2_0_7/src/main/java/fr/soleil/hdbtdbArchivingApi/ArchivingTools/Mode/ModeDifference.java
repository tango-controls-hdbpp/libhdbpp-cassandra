//+============================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Mode/ModeDifference.java,v $
//
// Project:      Tango Archiving Service
//
// Description: This object is one of the Mode class fields.
//				This class describes the 'on difference mode' (the archiving occurs each time the received value is different from the last archived value).
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ModeDifference.java,v $
// Revision 1.4  2006/10/30 14:36:07  ounsy
// added a toStringWatcher method used by the ArchivingWatcher's getAllArchivingAttributes command
//
// Revision 1.3  2005/11/29 17:11:17  chinkumo
// no message
//
// Revision 1.2.16.1  2005/11/15 13:34:38  chinkumo
// no message
//
// Revision 1.2  2005/01/26 15:35:38  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 17:39:56  chinkumo
// First commit (new API architecture).
//
//
// copyleft :   Synchrotron SOLEIL
//			    L'Orme des Merisiers
//			    Saint-Aubin - BP 48
//			    91192 GIF-sur-YVETTE CEDEX
//              FRANCE
//
//+============================================================================

package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode;

/**
 * <p/>
 * <B>Description :</B><BR>
 * This object is one of the <I>Mode class</I> fields.
 * This class describes the 'on difference mode' (the archiving occurs each time the received value is different from the last archived value).<BR>
 * </p>
 *
 * @author Jean CHINKUMO - Synchrotron SOLEIL
 * @version	$Revision: 1.4 $
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne
 */
public class ModeDifference extends ModeRoot
{
//	private int val_precision = 0;
	/**
	 * Default constructor
	 *
	 * @see #ModeDifference(int)
	 */
	public ModeDifference()
	{
	}

	/**
	 * This constructor takes one parameter as inputs.
	 *
	 * @param p
	 */
	public ModeDifference(int p)
	{
		super(p);
	}
	/**
	 * This constructor takes two parameters as inputs.
	 * @param p
	 * @param v
	 */
//	public ModeDifference(int p, int v)
//	{
//		super(p);
//		val_precision = v;
//	}

	/**
	 * Returns a string representation of the object <I>ModeDifference</I>.
	 *
	 * @return a string representation of the object <I>ModeDifference</I>.
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer("");
		buf.append(Tag.MODE_D_TAG + "\r\n" +
		           "\t" + Tag.MODE_P0_TAG + " = \"" + this.getPeriod() + "\" " + Tag.TIME_UNIT + "\r\n");
		//+         "\t" + Tag.MODE_D1_TAG + " = \"" + this.getVal_precision());
		
		
		return buf.toString();
	}

    public String toStringWatcher() 
    {
        return "MODE_D + " + super.getPeriod();// + " + "+ getVal_precision();
    }

//	public int getVal_precision() {
//		return val_precision;
//	}
//
//	public void setVal_precision(int val_precision) {
//		this.val_precision = val_precision;
//	}
}