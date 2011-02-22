//+============================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Mode/ModeAbsolu.java,v $
//
// Project:      Tango Archiving Service
//
// Description: This object is one of the Mode class fields.
//				This class describes the absolute mode (the archiving occurs each time the received value is upper/lower a specified value).
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ModeAbsolu.java,v $
// Revision 1.4  2006/10/30 14:36:06  ounsy
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
 * This object is one of the <I>Mode class</I> fields. This class describes the
 * absolute mode (the archiving occurs each time the received value is
 * upper/lower a specified value).<BR>
 * </p>
 * 
 * @author Jean CHINKUMO - Synchrotron SOLEIL
 * @version $Revision: 1.4 $
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne
 */
public class ModeAbsolu extends ModeRoot {
    /**
     * the <I>upper limit</I> field of the object
     */
    private double val_inf = 0;
    /**
     * the <I>lower limit</I> field of the object
     */
    private double val_sup = 0;
    /**
     * the <I>lower limit</I> field of the object
     */
    private boolean slow_drift = false;

    private double prev_stored_val = SLOW_DRIFT_FIRST_VAL;

    /**
     * Default constructor
     * 
     * @see #ModeAbsolu(double, double)
     * @see #ModeAbsolu(int, double, double)
     */
    public ModeAbsolu() {
    }

    /**
     * This constructor takes two parameters as inputs.
     * 
     * @param i
     *            Any received event upper value below this parameter is
     *            archived
     * @param s
     *            Any received event with value upper this parameter is archived
     * @see #ModeAbsolu()
     * @see #ModeAbsolu(int, double, double)
     */
    public ModeAbsolu(final double i, final double s) {
	val_inf = i;
	val_sup = s;
    }

    /**
     * This constructor takes three parameters as inputs :
     * 
     * @param p
     *            Archiving (polling) period time
     * @param i
     *            Any received event upper value below this parameter is
     *            archived
     * @param s
     *            Any received event with value upper this parameter is archived
     * @see #ModeAbsolu()
     * @see #ModeAbsolu(int, double, double)
     */
    public ModeAbsolu(final int p, final double i, final double s) {
	super.period = p;
	val_inf = i;
	val_sup = s;
    }

    /**
     * This constructor takes four parameters as inputs :
     * 
     * @param p
     *            Archiving (polling) period time
     * @param i
     *            Any received event upper value below this parameter is
     *            archived
     * @param s
     *            Any received event with value upper this parameter is archived
     * @param sd
     *            Activated slow dift mode for archiving process
     * @see #ModeAbsolu()
     * @see #ModeAbsolu(int, double, double, boolean)
     */

    public ModeAbsolu(final int p, final double i, final double s, final boolean sd) {
	super.period = p;
	val_inf = i;
	val_sup = s;
	slow_drift = sd;
    }

    /**
     * Returns the <I>lower limit</I> field of the object.
     * 
     * @return the <I>lower limit</I> field of the object.
     * @see #setValInf
     * @see #getValSup
     * @see #setValSup
     */
    public double getValInf() {
	return val_inf;
    }

    /**
     * Sets the <I>lower limit</I> field of the object.
     * 
     * @param i
     *            : the <I>lower limit</I> field to set.
     * @see #getValInf
     * @see #getValSup
     * @see #setValSup
     */
    public void setValInf(final double i) {
	val_inf = i;
    }

    /**
     * Returns the <I>upper limit</I> field of the object.
     * 
     * @return the <I>upper limit</I> field of the object.
     * @see #getValInf
     * @see #setValInf
     * @see #setValSup
     */
    public double getValSup() {
	return val_sup;
    }

    /**
     * Sets the <I>upper limit</I> field of the object.
     * 
     * @param s
     *            : the <I>upper limit</I> field to set..
     * @see #getValInf
     * @see #setValInf
     * @see #getValSup
     */
    public void setValSup(final double s) {
	val_sup = s;
    }

    /**
     * Returns a string representation of the object <I>ModeAbsolu</I>.
     * 
     * @return a string representation of the object <I>ModeAbsolu</I>.
     */
    @Override
    public String toString() {
	final StringBuffer buf = new StringBuffer("");
	// buf.append("[" + Tag.MODE_A_TAG + "\r\n" + "\t(" + Tag.MODE_P0_TAG +
	// " = \"" + getPeriod()
	// + "\" " + Tag.TIME_UNIT + "\r\n" + "\t" + Tag.MODE_A1_TAG + " = \"" +
	// getValInf()
	// + "\" \r\n" + "\t" + Tag.MODE_A2_TAG + " = \"" + getValSup() +
	// "\" \r\n" + "\t"
	// + Tag.MODE_A3_TAG + " = \"" + isSlow_drift() + "\"" + ")]");
	buf.append(getPeriod());
	buf.append(" ");
	buf.append(Tag.TIME_UNIT);
	buf.append(" valInf=");
	buf.append(getValInf());
	buf.append(" valSup=");
	buf.append(getValSup());
	buf.append(" slowDrift=");
	buf.append(isSlow_drift());
	return buf.toString();
    }

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof ModeAbsolu)) {
	    return false;
	}
	if (!super.equals(o)) {
	    return false;
	}

	final ModeAbsolu modeAbsolu = (ModeAbsolu) o;

	if (val_inf != modeAbsolu.val_inf) {
	    return false;
	}
	if (val_sup != modeAbsolu.val_sup) {
	    return false;
	}
	if (slow_drift != modeAbsolu.slow_drift) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	int result = super.hashCode();
	long temp;
	temp = Double.doubleToLongBits(val_inf);
	result = 29 * result + (int) (temp ^ temp >>> 32);
	temp = Double.doubleToLongBits(val_sup);
	result = 29 * result + (int) (temp ^ temp >>> 32);
	return result;
    }

    public String toStringWatcher() {
	return "MODE_A + " + super.getPeriod() + " + " + getValInf() + " + " + getValSup() + " + "
		+ isSlow_drift();
    }

    public boolean isSlow_drift() {
	return slow_drift;
    }

    public void setSlow_drift(final boolean slow_drift) {
	this.slow_drift = slow_drift;
    }

    public double getPrev_stored_val() {
	return prev_stored_val;
    }

    public void setPrev_stored_val(final double prev_stored_val) {
	this.prev_stored_val = prev_stored_val;
    }
}