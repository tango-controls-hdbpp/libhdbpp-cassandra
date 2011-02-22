// +============================================================================
// $Source:
// /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Mode/Mode.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: A Mode is an object that describes the way an attribute is
// archived.
// Let us note that a 'Mode' is a combination 'modes'.
// A 'mode' can be periodic, absolute, relative, on calculation, on difference
// or external.
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: Mode.java,v $
// Revision 1.5 2007/02/16 08:26:29 pierrejoseph
// The HDB minimal period is 10 seconds (instead of 1s).
//
// Revision 1.4 2006/10/30 14:36:07 ounsy
// added a toStringWatcher method used by the ArchivingWatcher's
// getAllArchivingAttributes command
//
// Revision 1.3 2005/11/29 17:11:17 chinkumo
// no message
//
// Revision 1.2.16.2 2005/11/15 13:34:38 chinkumo
// no message
//
// Revision 1.2.16.1 2005/09/26 08:39:14 chinkumo
// checkMode(..) method was added. This method checks if the current mode is
// valid (function of archiving type - historical/temporary).
//
// Revision 1.2 2005/01/26 15:35:38 chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1 2004/12/06 17:39:56 chinkumo
// First commit (new API architecture).
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
// FRANCE
//
// +============================================================================

package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * <p/>
 * <B>Description :</B><BR>
 * A Mode is an object that describes the way an attribute is archived. Let us
 * note that a 'Mode' is a combination 'modes'. A 'mode' can be periodic,
 * absolute, relative, on calculation, on difference or external.
 * </p>
 * 
 * @author Jean CHINKUMO - Synchrotron SOLEIL
 * @version $Revision: 1.5 $
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne
 */

public class Mode {

    // private static int modeLengh = 18;
    private ModePeriode mode_p = null;
    private ModeAbsolu mode_a = null;
    private ModeRelatif mode_r = null;
    private ModeSeuil mode_s = null;
    private ModeCalcul mode_c = null;
    private ModeDifference mode_d = null;
    private ModeExterne mode_e = null;

    private TdbSpec tdbSpec = null;

    private static final int HDB_PERIOD_MIN_VALUE = 10000; // ms
    private static final int TDB_PERIOD_MIN_VALUE = 100; // ms

    /**
     * Default constructor
     * 
     * @see #Mode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode
     *      mp, fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu ma,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif mr,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil ms,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul mc,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference md,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne me ).
     */
    public Mode() {
    }

    /**
     * Constructor
     * 
     * @param mp
     *            : periodic mode
     * @param ma
     *            : absolute mode
     * @param mr
     *            : relative mode
     * @param mc
     *            : on calculation mode
     * @param md
     *            : on difference mode
     * @param me
     *            : external mode
     * @see #Mode().
     */
    public Mode(final ModePeriode mp, final ModeAbsolu ma, final ModeRelatif mr,
	    final ModeSeuil ms, final ModeCalcul mc, final ModeDifference md, final ModeExterne me) {
	mode_p = mp;
	mode_a = ma;
	mode_r = mr;
	mode_s = ms;
	mode_c = mc;
	mode_d = md;
	mode_e = me;
    }

    /**
     * Constructor
     * 
     * @param mp
     *            : periodic mode
     * @param ma
     *            : absolute mode
     * @param mr
     *            : relative mode
     * @param mc
     *            : on calculation mode
     * @param md
     *            : on difference mode
     * @param me
     *            : external mode
     * @see #Mode().
     */
    public Mode(final ModePeriode mp, final ModeAbsolu ma, final ModeRelatif mr,
	    final ModeSeuil ms, final ModeCalcul mc, final ModeDifference md, final ModeExterne me,
	    final TdbSpec ts) {
	mode_p = mp;
	mode_a = ma;
	mode_r = mr;
	mode_s = ms;
	mode_c = mc;
	mode_d = md;
	mode_e = me;
	tdbSpec = ts;
    }

    public void reinitialize() {
	mode_p = null;
	mode_a = null;
	mode_r = null;
	mode_s = null;
	mode_c = null;
	mode_d = null;
	mode_e = null;
	tdbSpec = null;
    }

    /**
     * Constructor
     * 
     * @param mode_array
     *            : an array containing all information defining the mode. This
     *            constructor will be primarily called when building a Mode from
     *            data that are coming from the database.
     * @see #Mode().
     * @see #Mode(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode
     *      mp, fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu ma,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif mr,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil ms,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul mc,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference md,
     *      fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne me ).
     */
    public Mode(final String[] mode_array) {
	int index = 0;
	final int my_array_size = mode_array.length;
	while (index < my_array_size && mode_array[index] != null) {
	    if (mode_array[index].equals(Tag.MODE_P)) {
		// Mode P�riodique
		mode_p = new ModePeriode(Integer.parseInt(mode_array[index + 1]));
		index = index + 2;
	    } else if (mode_array[index].equals(Tag.MODE_A)) {
		// Mode Absolu
		mode_a = new ModeAbsolu(Integer.parseInt(mode_array[index + 1]), Double
			.parseDouble(mode_array[index + 2]), Double
			.parseDouble(mode_array[index + 3]), Boolean
			.parseBoolean(mode_array[index + 4]));
		index = index + 5;
	    } else if (mode_array[index].equals(Tag.MODE_R)) {
		// Mode Relatif
		mode_r = new ModeRelatif(Integer.parseInt(mode_array[index + 1]), Double
			.parseDouble(mode_array[index + 2]), Double
			.parseDouble(mode_array[index + 3]), Boolean
			.parseBoolean(mode_array[index + 4]));
		index = index + 5;
	    } else if (mode_array[index].equals(Tag.MODE_T)) {
		// Mode Threshold
		mode_s = new ModeSeuil(Integer.parseInt(mode_array[index + 1]), Double
			.parseDouble(mode_array[index + 2]), Double
			.parseDouble(mode_array[index + 3]));
		index = index + 4;
	    } else if (mode_array[index].equals(Tag.MODE_C)) {
		// Mode Calcul
		mode_c = new ModeCalcul(Integer.parseInt(mode_array[index + 1]), Integer
			.parseInt(mode_array[index + 2]), Integer.parseInt(mode_array[index + 3]));
		// Warning : there is one more field for this mode type
		index = index + 5;
	    } else if (mode_array[index].equals(Tag.MODE_D)) {
		// Mode Diff�rent
		mode_d = new ModeDifference(Integer.parseInt(mode_array[index + 1]));
		index = index + 2;
	    } else if (mode_array[index].equals(Tag.MODE_E)) {
		// Mode externe
		mode_e = new ModeExterne();
		index = index + 1;
	    } else if (mode_array[index].equals(Tag.TDB_SPEC)) {
		// Specific temporary database
		tdbSpec = new TdbSpec(Long.parseLong(mode_array[index + 1]), Long
			.parseLong(mode_array[index + 2]));
		index = index + 3;
	    } else {
		index++;
	    }
	}
    }

    /*
     * public static int getModeLengh() { return modeLengh; }
     */

    /**
     * getModeP returns the object which corresponds to the 'periodic' field.
     * 
     * @return ModePeriode
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #getModeA()
     * @see #getModeR()
     * @see #getModeC()
     * @see #getModeD()
     * @see #getModeE()
     */
    public ModePeriode getModeP() {
	return mode_p;
    }

    /**
     * setModeP sets the object which corresponds to the 'periodic' field.
     * 
     * @see #getModeP()
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     */
    public void setModeP(final ModePeriode mp) {
	mode_p = mp;
    }

    /**
     * getModeA returns the object which corresponds to the 'absolute' field.
     * 
     * @return ModeAbsolu
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #getModeP()
     * @see #getModeR()
     * @see #getModeC()
     * @see #getModeD()
     * @see #getModeE()
     */
    public ModeAbsolu getModeA() {
	return mode_a;
    }

    /**
     * setModeA sets the object which corresponds to the 'absolute' field.
     * 
     * @see #getModeA()
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     */
    public void setModeA(final ModeAbsolu ma) {
	mode_a = ma;
    }

    /**
     * getModeR returns the object which corresponds to the 'relative' field.
     * 
     * @return ModeRelatif
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #getModeP()
     * @see #getModeA()
     * @see #getModeC()
     * @see #getModeD()
     * @see #getModeE()
     */
    public ModeRelatif getModeR() {
	return mode_r;
    }

    /**
     * setModeR sets the object which corresponds to the 'relative' field.
     * 
     * @see #getModeR()
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     */
    public void setModeR(final ModeRelatif mr) {
	mode_r = mr;
    }

    /**
     * getModeT returns the object which corresponds to the 'threshold' field.
     * 
     * @return ModeCalcul
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #getModeP()
     * @see #getModeA()
     * @see #getModeR()
     * @see #getModeC()
     * @see #getModeD()
     * @see #getModeE()
     */
    public ModeSeuil getModeT() {
	return mode_s;
    }

    /**
     * setModeR sets the object which corresponds to the 'relative' field.
     * 
     * @see #getModeR()
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     */
    public void setModeT(final ModeSeuil mode_s) {
	this.mode_s = mode_s;
    }

    /**
     * getModeC returns the object which corresponds to the 'on calculation'
     * field.
     * 
     * @return ModeCalcul
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #getModeP()
     * @see #getModeA()
     * @see #getModeR()
     * @see #getModeD()
     * @see #getModeE()
     */
    public ModeCalcul getModeC() {
	return mode_c;
    }

    /**
     * setModeC sets the object which corresponds to the 'on calculation' field.
     * 
     * @see #getModeC()
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     */
    public void setModeC(final ModeCalcul mc) {
	mode_c = mc;
    }

    /**
     * getModeD returns the object which corresponds to the 'on difference'
     * field.
     * 
     * @return ModeDifference
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     * @see #getModeP()
     * @see #getModeA()
     * @see #getModeR()
     * @see #getModeC()
     * @see #getModeE()
     */
    public ModeDifference getModeD() {
	return mode_d;
    }

    /**
     * setModeD sets the object which corresponds to the 'on difference' field.
     * 
     * @see #getModeD()
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     */
    public void setModeD(final ModeDifference md) {
	mode_d = md;
    }

    /**
     * getModeE returns the object which corresponds to the 'external' field.
     * 
     * @return ModeExterne
     * @see #setModeE(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne)
     * @see #getModeP()
     * @see #getModeA()
     * @see #getModeR()
     * @see #getModeC()
     * @see #getModeD()
     */
    public ModeExterne getModeE() {
	return mode_e;
    }

    /**
     * setModeE sets the object which corresponds to the 'external' field.
     * 
     * @see #getModeE()
     * @see #setModeP(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode)
     * @see #setModeA(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu)
     * @see #setModeR(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif)
     * @see #setModeC(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul)
     * @see #setModeD(fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference)
     */
    public void setModeE(final ModeExterne me) {
	mode_e = me;
    }

    public TdbSpec getTdbSpec() {
	return tdbSpec;
    }

    public void setTdbSpec(final TdbSpec tdbSpec) {
	this.tdbSpec = tdbSpec;
    }

    /**
     * Returns a string representation of the object <I>Mode</I>.
     * 
     * @return a string representation of the object <I>Mode</I>.
     */
    @Override
    public String toString() {
	final StringBuffer buf = new StringBuffer("");
	if (mode_p != null) {
	    buf.append(mode_p.toString() + "\r\n");
	}
	if (mode_a != null) {
	    buf.append(mode_a.toString() + "\r\n");
	}
	if (mode_r != null) {
	    buf.append(mode_r.toString() + "\r\n");
	}
	if (mode_s != null) {
	    buf.append(mode_s.toString() + "\r\n");
	}
	if (mode_c != null) {
	    buf.append(mode_c.toString() + "\r\n");
	}
	if (mode_d != null) {
	    buf.append(mode_d.toString() + "\r\n");
	}
	if (mode_e != null) {
	    buf.append(mode_e.toString() + "\r\n");
	}
	if (tdbSpec != null) {
	    buf.append(tdbSpec.toString() + "\r\n");
	}
	return buf.toString();
    }

    public String toStringSimple() {
	final StringBuffer buf = new StringBuffer("[ ");
	if (mode_p != null) {
	    buf.append(" " + Tag.MODE_P_TAG + " ");
	}
	if (mode_a != null) {
	    buf.append(" " + Tag.MODE_A_TAG + " ");
	}
	if (mode_r != null) {
	    buf.append(" " + Tag.MODE_R_TAG + " ");
	}
	if (mode_s != null) {
	    buf.append(" " + Tag.MODE_T_TAG + " ");
	}
	if (mode_c != null) {
	    buf.append(" " + Tag.MODE_C_TAG + " ");
	}
	if (mode_d != null) {
	    buf.append(" " + Tag.MODE_D_TAG + " ");
	}
	if (mode_e != null) {
	    buf.append(" " + Tag.MODE_E_TAG + " ");
	}
	if (tdbSpec != null) {
	    buf.append(" " + Tag.MODE_SPEC_TAG + " ");
	}
	buf.append(" " + " ]");
	return buf.toString();
    }

    /**
     * Returns an array representation of the object <I>Mode</I>. This array
     * only contains not-null canonicals components of this Object.
     * 
     * @return an array representation of the object <I>Mode</I>.
     */
    public String[] toArray() {
	String[] mode_array;
	mode_array = new String[getArraySizeSmall()];
	int index = 0;
	if (getModeP() != null) {
	    mode_array[index] = Tag.MODE_P;
	    index++;
	    mode_array[index] = Integer.toString(getModeP().getPeriod());
	    index++;
	}
	if (getModeA() != null) {
	    mode_array[index] = Tag.MODE_A;
	    index++;
	    mode_array[index] = Integer.toString(getModeA().getPeriod());
	    index++;
	    mode_array[index] = Double.toString(getModeA().getValInf());
	    index++;
	    mode_array[index] = Double.toString(getModeA().getValSup());
	    index++;
	    mode_array[index] = Boolean.toString(getModeA().isSlow_drift());
	    index++;

	}
	if (getModeR() != null) {
	    mode_array[index] = Tag.MODE_R;
	    index++;
	    mode_array[index] = Integer.toString(getModeR().getPeriod());
	    index++;
	    mode_array[index] = Double.toString(getModeR().getPercentInf());
	    index++;
	    mode_array[index] = Double.toString(getModeR().getPercentSup());
	    index++;
	    mode_array[index] = Boolean.toString(getModeR().isSlow_drift());
	    index++;
	}
	if (getModeT() != null) {
	    mode_array[index] = Tag.MODE_T;
	    index++;
	    mode_array[index] = Integer.toString(getModeT().getPeriod());
	    index++;
	    mode_array[index] = Double.toString(getModeT().getThresholdInf());
	    index++;
	    mode_array[index] = Double.toString(getModeT().getThresholdSup());
	    index++;
	}
	if (getModeC() != null) {
	    mode_array[index] = Tag.MODE_C;
	    index++;
	    mode_array[index] = Integer.toString(getModeC().getPeriod());
	    index++;
	    mode_array[index] = Double.toString(getModeC().getRange());
	    index++;
	    mode_array[index] = Double.toString(getModeC().getTypeCalcul());
	    index++;
	    mode_array[index] = "NULL";
	    index++;// Algo
	}
	if (getModeD() != null) {
	    mode_array[index] = Tag.MODE_D;
	    index++;
	    mode_array[index] = Integer.toString(getModeD().getPeriod());
	    index++;

	}
	if (getModeE() != null) {
	    mode_array[index] = Tag.MODE_E;
	    index++;
	}
	if (getTdbSpec() != null) {
	    mode_array[index] = Tag.TDB_SPEC;
	    index++;
	    mode_array[index] = Long.toString(getTdbSpec().getExportPeriod());
	    index++;
	    mode_array[index] = Long.toString(getTdbSpec().getKeepingPeriod());
	    index++;
	}
	return mode_array;
    }

    /**
     * Returns the smallest size of this object's array representation.
     * 
     * @return the smallest size of this object's array representation.
     */
    public int getArraySizeSmall() {
	int my_size = 0;
	if (getModeP() != null) {
	    my_size = my_size + 2;
	}
	if (getModeA() != null) {
	    my_size = my_size + 5;
	}
	if (getModeR() != null) {
	    my_size = my_size + 5;
	}
	if (getModeT() != null) {
	    my_size = my_size + 4;
	}
	if (getModeC() != null) {
	    my_size = my_size + 5;
	}
	if (getModeD() != null) {
	    my_size = my_size + 2;
	}
	if (getModeE() != null) {
	    my_size = my_size + 1;
	}
	if (getTdbSpec() != null) {
	    my_size = my_size + 3;
	}
	return my_size;
    }

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Mode)) {
	    return false;
	}

	final Mode mode = (Mode) o;

	if (mode_a != null ? !mode_a.equals(mode.mode_a) : mode.mode_a != null) {
	    return false;
	}
	if (mode_c != null ? !mode_c.equals(mode.mode_c) : mode.mode_c != null) {
	    return false;
	}
	if (mode_d != null ? !mode_d.equals(mode.mode_d) : mode.mode_d != null) {
	    return false;
	}
	if (mode_e != null ? !mode_e.equals(mode.mode_e) : mode.mode_e != null) {
	    return false;
	}
	if (mode_p != null ? !mode_p.equals(mode.mode_p) : mode.mode_p != null) {
	    return false;
	}
	if (mode_r != null ? !mode_r.equals(mode.mode_r) : mode.mode_r != null) {
	    return false;
	}
	if (mode_s != null ? !mode_s.equals(mode.mode_s) : mode.mode_s != null) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	int result;
	result = mode_p != null ? mode_p.hashCode() : 0;
	result = 29 * result + (mode_a != null ? mode_a.hashCode() : 0);
	result = 29 * result + (mode_r != null ? mode_r.hashCode() : 0);
	result = 29 * result + (mode_s != null ? mode_s.hashCode() : 0);
	result = 29 * result + (mode_c != null ? mode_c.hashCode() : 0);
	result = 29 * result + (mode_d != null ? mode_d.hashCode() : 0);
	result = 29 * result + (mode_e != null ? mode_e.hashCode() : 0);
	return result;
    }

    public void checkMode(final boolean historic, final String attributeName)
	    throws ArchivingException {
	if (mode_p == null
		&& (mode_a != null || mode_r != null || mode_s != null || mode_c != null
			|| mode_d != null || mode_e != null)) {
	    throw generateException("Periodic mode missing !!");
	}

	doCheck(mode_p, attributeName, historic);
	doCheck(mode_a, attributeName, historic);
	doCheck(mode_r, attributeName, historic);
	doCheck(mode_s, attributeName, historic);
	doCheck(mode_c, attributeName, historic);
	doCheck(mode_d, attributeName, historic);
	doCheck(mode_e, attributeName, historic);
    }

    private void doCheck(final ModeRoot mode, final String attributeName, final boolean isHistoric)
	    throws ArchivingException {
	if (mode != null) {
	    int minVal = HDB_PERIOD_MIN_VALUE;
	    if (!isHistoric) {
		minVal = TDB_PERIOD_MIN_VALUE;
	    }
	    if (mode.getPeriod() < minVal) {
		// allowed short period in HDB only for previously declared
		// attributes
		if (ShortPeriodAttributesManager.isShortPeriodAttribute(attributeName)
			&& isHistoric) {
		    final int attributePeriod = ShortPeriodAttributesManager
			    .getPeriodFromShortAttributeName(attributeName);
		    if (mode.getPeriod() / 1000 < attributePeriod) {
			throw generateException("Archiving period too low !!" + "(" + attributeName
				+ "'s period must be greater or equal to " + attributePeriod
				+ " ms" + ")");
		    }
		} else {
		    throw generateException("archiving period to low !!" + "(" + mode.getPeriod()
			    + "<" + minVal + " ms" + ")");
		}
	    }
	}
    }

    private static ArchivingException generateException(final String cause_msg) {
	final String cause = "MODE_EXCEPTION";
	final String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
	final String reason = "Failed while executing Mode.checkMode()...";
	final String desc = cause + " (" + cause_msg + ")";
	return new ArchivingException(message, reason, ErrSeverity.WARN, desc, "");
    }

    public String toStringWatcher() {
	final StringBuffer ret = new StringBuffer();

	if (getModeP() != null) {
	    ret.append(getModeP().toStringWatcher());
	}
	if (getModeA() != null) {
	    ret.append(" ");
	    ret.append(getModeA().toStringWatcher());
	}
	if (getModeR() != null) {
	    ret.append(" ");
	    ret.append(getModeR().toStringWatcher());
	}
	if (getModeT() != null) {
	    ret.append(" ");
	    ret.append(getModeT().toStringWatcher());
	}
	if (getModeD() != null) {
	    ret.append(" ");
	    ret.append(getModeD().toStringWatcher());
	}

	return ret.toString();
    }
}
