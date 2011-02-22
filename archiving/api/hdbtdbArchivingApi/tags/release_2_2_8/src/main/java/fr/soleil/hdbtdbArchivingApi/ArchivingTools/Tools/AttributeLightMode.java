//+======================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Tools/AttributeLightMode.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributeLightMode.
//						(Chinkumo Jean) - Apr 22, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: AttributeLightMode.java,v $
// Revision 1.4  2007/01/05 12:47:38  pierrejoseph
// Modification of the AttributeLightMode object creation
//
// Revision 1.3  2005/11/29 17:11:17  chinkumo
// no message
//
// Revision 1.2.12.2  2005/11/15 13:34:38  chinkumo
// no message
//
// Revision 1.2.12.1  2005/09/09 08:36:07  chinkumo
// Better management of an exception in the toString() method.
//
// Revision 1.2  2005/06/14 10:12:11  chinkumo
// Branch (tangORBarchiving_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.1.4.1  2005/04/29 18:37:42  chinkumo
// fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode -> Mode
//
// Revision 1.1  2004/12/06 17:39:56  chinkumo
// First commit (new API architecture).
//
// Revision 1.6  2004/09/07 09:32:40  chinkumo
// A bug fixed in the method toString().
//
// Revision 1.5  2004/09/01 13:26:04  chinkumo
// Heading was updated.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;

public class AttributeLightMode extends AttributeLight {
    private String device_in_charge = null;
    private Timestamp trigger_time = null;
    private Mode mode = null;

    public AttributeLightMode() {
	super();
	device_in_charge = "";
	mode = null;
    }

    public AttributeLightMode(final String n) {
	super(n);
	device_in_charge = "";
	mode = null;
    }

    public AttributeLightMode(final String fullName,
	    final fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode mode) {
	super(fullName);
	device_in_charge = "";
	this.mode = mode;
    }

    public AttributeLightMode(final AttributeLight attributeLight, final String deviceIncharge,
	    final fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode mode) {
	setAttribute_complete_name(attributeLight.getAttribute_complete_name());
	setData_type(attributeLight.getData_type());
	setData_format(attributeLight.getData_format());
	setWritable(attributeLight.getWritable());
	device_in_charge = deviceIncharge;
	this.mode = mode;
    }

    /**
     * This constructor takes two parameters as inputs.
     * 
     * @param full_name
     *            the attribute's name
     * @param deviceInCharge
     *            the TANGO device's name in charge of collecting and inserting
     *            datum into the database
     * @param mode
     *            the filling mode applied to the attribute
     * @see #AttributeLightMode()
     * @see #AttributeLightMode(java.lang.String)
     */
    public AttributeLightMode(final String full_name, final String deviceInCharge,
	    final fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode mode) {
	super(full_name);
	device_in_charge = deviceInCharge;
	this.mode = mode;
    }

    /**
     * Create an object from an array who contains all the attributes
     * information (type,format ...)
     * 
     * @return
     */
    static public AttributeLightMode creationWithFullInformation(final String[] att_tab) {
	return new AttributeLightMode(att_tab, true);
    }

    /**
     * Create an object from an array who does not contain all the attributes
     * information (type,format ...)
     * 
     * @return
     */
    static public AttributeLightMode creationWithoutFullInformation(final String[] att_tab) {
	return new AttributeLightMode(att_tab, false);
    }

    private AttributeLightMode(final String[] att_tab, final boolean fullInformation) {
	super(att_tab[0]); // Full_name
	/*
	 * Example With full Information :
	 * "tango/tangotest/spjz_1/double_spectrum_ro"
	 * ,5,1,0,"archiving/hdbarchiver/01",NULL,4,MODE_P,10000,MODE_D,20000 or
	 * "tango/tangotest/spjz_1/double_spectrum_ro"
	 * ,5,1,0,"archiving/hdbarchiver/01",NULL,2,MODE_P,10000
	 * 
	 * "tango/tangotest/spjz_2/double_spectrum_ro",5,1,0,
	 * "archiving/tdbarchiver/01"
	 * ,NULL,10,MODE_P,2000,TDB_SPEC,300000,21600000
	 * ,MODE_D,2000,TDB_SPEC,300000,21600000 or
	 * "tango/tangotest/spjz_2/double_spectrum_ro"
	 * ,5,1,0,"archiving/tdbarchiver/01"
	 * ,NULL,5,MODE_P,2000,TDB_SPEC,300000,21600000
	 */

	/*
	 * Example With Not full Information :
	 * "tango/tangotest/spjz_1/double_spectrum_ro",MODE_P,10000,MODE_D,20000
	 * or "tango/tangotest/spjz_1/double_spectrum_ro",MODE_P,10000
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * "tango/tangotest/spjz_2/double_spectrum_ro",MODE_P,2000,TDB_SPEC,300000
	 * ,21600000,MODE_D,2000,TDB_SPEC,300000,21600000 or
	 * "tango/tangotest/spjz_2/double_spectrum_ro"
	 * ,MODE_P,2000,TDB_SPEC,300000,21600000
	 */
	int modeArraySize;
	int iModePos;
	if (fullInformation) {
	    setData_type(Integer.parseInt(att_tab[1]));
	    setData_format(Integer.parseInt(att_tab[2]));
	    setWritable(Integer.parseInt(att_tab[3]));
	    setDevice_in_charge(att_tab[4]);
	    if (!att_tab[5].equals("NULL")) {
		setTrigger_time(Timestamp.valueOf(att_tab[5]));
	    }

	    iModePos = 7;
	    modeArraySize = Integer.parseInt(att_tab[iModePos - 1]);
	} else {
	    iModePos = 1;
	    modeArraySize = att_tab.length - 1;
	}

	// Build the mode object
	final String[] modeArray = new String[modeArraySize];

	for (int i = iModePos; i < att_tab.length; i++) {
	    modeArray[i - iModePos] = att_tab[i];
	}
	final Mode att_mode = new Mode(modeArray);
	setMode(att_mode);
    }

    /**
     * This constructor builds an AttributeLightMode from an array
     * 
     * @param att_tab
     *            an array that contains the AttributeLight's name, the name of
     *            the device in charge and the mode of filling informations.
     */
    /*
     * public AttributeLightMode(String[] att_tab) {
     * setAttribute_complete_name(att_tab[ 0 ]);
     * setData_type(Integer.parseInt(att_tab[ 1 ]));
     * setData_format(Integer.parseInt(att_tab[ 2 ]));
     * setWritable(Integer.parseInt(att_tab[ 3 ])); setDevice_in_charge(att_tab[
     * 4 ]); if ( !att_tab[ 5 ].equals("NULL") )
     * setTrigger_time(Timestamp.valueOf(att_tab[ 5 ])); int modeArraySize =
     * Integer.parseInt(att_tab[ 6 ]); // Build the mode object String[]
     * modeArray = new String[ modeArraySize ];
     * 
     * for ( int i = 7 ; i < att_tab.length ; i++ ) { modeArray[ i - 7 ] =
     * att_tab[ i ]; } Mode att_mode = new Mode(modeArray); setMode(att_mode); }
     */

    /**
     * Returns the AttributeLight's target.
     * 
     * @return the AttributeLight's target.
     * @see #getAttribute_complete_name
     * @see #setDevice_in_charge
     */
    public String getDevice_in_charge() {
	return device_in_charge;
    }

    /**
     * Sets the AttributeLight's target.
     * 
     * @param device_in_charge
     *            the AttributeLight's target.
     * @see #setAttribute_complete_name
     * @see #getDevice_in_charge
     */
    public void setDevice_in_charge(final String device_in_charge) {
	this.device_in_charge = device_in_charge;
    }

    public Timestamp getTrigger_time() {
	return trigger_time;
    }

    public void setTrigger_time(final Timestamp trigger_time) {
	this.trigger_time = trigger_time;
    }

    /**
     * Sets the AttributeLight's mode of filling.
     * 
     * @param m
     *            the AttributeLight's mode of filling.
     * @see #setAttribute_complete_name
     * @see #setDevice_in_charge
     * @see #getMode
     */
    public void setMode(final Mode m) {
	mode = m;
    }

    /**
     * Returns the AttributeLight's mode of filling.
     * 
     * @return the AttributeLight's mode of filling.
     * @see #getAttribute_complete_name
     * @see #getDevice_in_charge
     * @see #setMode
     */
    public Mode getMode() {
	return mode;
    }

    /**
     * Returns a string representation of the object <I>AttributeLight</I>.
     * 
     * @return a string representation of the object <I>AttributeLight</I>.
     */
    @Override
    public String toString() {

	final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
	builder.append(getAttribute_complete_name());
	builder.append("dataType", getData_type());
	builder.append("dataFormat", getData_format());
	builder.append("writable", getWritable());
	builder.append("archiver", getDevice_in_charge());
	builder.append("trigger time", getTrigger_time());
	builder.append("modes", getMode());
	// StringBuffer buf = new StringBuffer();
	// buf.append("[name"
	// + getAttribute_complete_name() + "\n");
	// buf.append("\t DataType : ................" + getData_type() + "\n");
	// buf.append("\t DataFormat : ............" + getData_format() + "\n");
	// buf.append("\t Writable : ................." + getWritable() + "\n");
	// buf.append("\t Device in charge ......" + getDevice_in_charge() +
	// "\n");
	// if (getTrigger_time() != null)
	// buf.append("\t Trigger Time ............"
	// + getTrigger_time().toString() + "\n");
	// buf.append("\tmodes : \n"
	// + ((mode != null) ? getMode().toString() : "") + "\n");
	return builder.toString();
    }

    @Override
    public String[] toArray() {
	final int modeArraySize = mode.getArraySizeSmall();
	final String[] mode_tab = getMode().toArray();
	String[] att_tab;
	att_tab = new String[mode_tab.length + 7];
	att_tab[0] = getAttribute_complete_name();
	att_tab[1] = Integer.toString(getData_type());
	att_tab[2] = Integer.toString(getData_format());
	att_tab[3] = Integer.toString(getWritable());

	if (getDevice_in_charge() == null) {
	    att_tab[4] = "NULL";
	} else {
	    att_tab[4] = getDevice_in_charge();
	}

	if (getTrigger_time() != null) {
	    att_tab[5] = getTrigger_time().toString();
	} else {
	    att_tab[5] = "NULL";
	}
	// Int�gration des modes
	att_tab[6] = Integer.toString(modeArraySize);

	for (int i = 7; i < att_tab.length; i++) {
	    att_tab[i] = mode_tab[i - 7];
	}
	return att_tab;
    }

    @Override
    public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof AttributeLightMode)) {
	    return false;
	}
	if (!super.equals(o)) {
	    return false;
	}

	final AttributeLightMode attributeLightMode = (AttributeLightMode) o;

	if (device_in_charge != null ? !device_in_charge
		.equals(attributeLightMode.device_in_charge)
		: attributeLightMode.device_in_charge != null) {
	    return false;
	}
	if (mode != null ? !mode.equals(attributeLightMode.mode) : attributeLightMode.mode != null) {
	    return false;
	}
	if (trigger_time != null ? !trigger_time.equals(attributeLightMode.trigger_time)
		: attributeLightMode.trigger_time != null) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	int result = super.hashCode();
	result = 29 * result + (device_in_charge != null ? device_in_charge.hashCode() : 0);
	result = 29 * result + (trigger_time != null ? trigger_time.hashCode() : 0);
	result = 29 * result + (mode != null ? mode.hashCode() : 0);
	return result;
    }

}
