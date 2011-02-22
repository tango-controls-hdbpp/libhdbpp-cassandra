//+============================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Mode/ModeExterne.java,v $
//
// Project:      Tango Archiving Service
//
// Description: This object is one of the Mode class fields.
//				This class describes the 'external mode' (the archiving is not triggered by the filing service).
//
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: ModeExterne.java,v $
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
 * 'external mode' (the archiving is not triggered by the filing service).<BR>
 * </p>
 * 
 * @author Jean CHINKUMO - Synchrotron SOLEIL
 * @version $Revision: 1.3 $
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul
 * @see fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference
 */

public class ModeExterne extends ModeRoot {
    private String description;

    /**
     * Default constructor
     */
    public ModeExterne() {
    }

    /**
     * Gets the description of this object.
     * 
     * @return the description of this object.
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the description of this object.
     * 
     * @param desc
     *            the description of this object.
     */
    public void setDescription(final String desc) {
	description = desc;
    }

    /**
     * Returns a string representation of the object <I>ModeExterne</I>.
     * 
     * @return a string representation of the object <I>ModeExterne</I>.
     */
    @Override
    public String toString() {
	final StringBuffer buf = new StringBuffer("");
	buf.append(Tag.MODE_E_TAG + "\r\n" + "\t" + Tag.MODE_E1_TAG + " = \"" + getDescription()
		+ "\"");
	return buf.toString();
    }

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof ModeExterne)) {
	    return false;
	}

	final ModeExterne modeExterne = (ModeExterne) o;

	if (!description.equals(modeExterne.description)) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	return description.hashCode();
    }
}