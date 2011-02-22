/*	Synchrotron Soleil
 *
 *   File          :  Archiver.java
 *
 *   Project       :  archiving_watcher
 *
 *   Description   :
 *
 *   Author        :  CLAISSE
 *
 *   Original      :  15 d�c. 2005
 *
 *   Revision:  					Author:
 *   Date: 							State:
 *
 *   Log: Archiver.java,v
 *
 */
/*
 * Created on 15 d�c. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Chaine;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.comparators.ArchivingAttributeComparator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

/**
 * Models an archiver.
 * 
 * @author CLAISSE
 */
public class Archiver {
    private String name = "";

    private int scalarLoad;
    private int spectrumLoad;
    private int imageLoad;
    private int totalLoad;

    private boolean isLiving;
    private boolean doDiagnosis;
    private String status;

    private Hashtable<String, ArchivingAttribute> KOAttributes;
    private final ArchivingAttributeComparator comparator;

    /**
     * Default constructor
     */
    public Archiver() {
	KOAttributes = new Hashtable<String, ArchivingAttribute>();
	comparator = new ArchivingAttributeComparator();
    }

    /**
     * @return Returns the isLiving.
     */
    public boolean isLiving() {
	return isLiving;
    }

    /**
     * @param isLiving
     *            The isLiving to set.
     */
    public void setLiving(final boolean isLiving) {
	this.isLiving = isLiving;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(final String name) {
	this.name = name;
    }

    /**
     * @return Returns the imageLoad.
     */
    public int getImageLoad() {
	return imageLoad;
    }

    /**
     * @param imageLoad
     *            The imageLoad to set.
     */
    public void setImageLoad(final int imageLoad) {
	this.imageLoad = imageLoad;
    }

    /**
     * @return Returns the scalarLoad.
     */
    public int getScalarLoad() {
	return scalarLoad;
    }

    /**
     * @param scalarLoad
     *            The scalarLoad to set.
     */
    public void setScalarLoad(final int scalarLoad) {
	this.scalarLoad = scalarLoad;
    }

    /**
     * @return Returns the spectrumLoad.
     */
    public int getSpectrumLoad() {
	return spectrumLoad;
    }

    /**
     * @param spectrumLoad
     *            The spectrumLoad to set.
     */
    public void setSpectrumLoad(final int spectrumLoad) {
	this.spectrumLoad = spectrumLoad;
    }

    /**
     * @return Returns the totalLoad.
     */
    public int getTotalLoad() {
	return totalLoad;
    }

    /**
     * @param totalLoad
     *            The totalLoad to set.
     */
    public void setTotalLoad(final int totalLoad) {
	this.totalLoad = totalLoad;
    }

    /**
     * Builds and returns the archiving report for this archiver:
     * <UL>
     * <LI>The archiver's status, loads,..
     * <LI>The archiver's KO attributes
     * </UL>
     * 
     * @return The archiving report for this archiver
     */
    public String getReport() {
	final StringBuffer buff = new StringBuffer();

	final String inLine = getName() + " :  VVVVVVVVVVVVVVVVVVVVVVVVVVVV";
	buff.append(inLine);
	buff.append(Tools.CRLF);

	final String isLiving = isDoDiagnosis() ? "" + isLiving() : "Not tested";
	buff.append("Is device alive? " + isLiving);
	buff.append(Tools.CRLF);
	buff.append("Status: " + getStatus());
	buff.append(Tools.CRLF);
	buff.append("Total load: " + getTotalLoad() + ". Scalar load: " + getScalarLoad()
		+ ". Spectrum load: " + getSpectrumLoad() + ". Image load: " + getImageLoad());

	buff.append(Tools.CRLF);
	buff.append("KO attributes : --------------------------");
	buff.append(Tools.CRLF);

	final List<ArchivingAttribute> list = new Vector<ArchivingAttribute>();
	list.addAll(KOAttributes.values());
	Collections.sort(list, comparator);
	final Iterator<ArchivingAttribute> it = list.iterator();

	while (it.hasNext()) {
	    final ArchivingAttribute attribute = it.next();
	    final String attributeCompleteName = attribute.getCompleteName();
	    buff.append("    " + attributeCompleteName);
	    buff.append(Tools.CRLF);
	}

	final int size = inLine.length();
	final String outLine = Chaine.repeter("^", size);
	buff.append(outLine);

	return buff.toString();
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
	return status;
    }

    /**
     * @param status
     *            The status to set.
     */
    public void setStatus(final String status) {
	this.status = status;
    }

    /**
     * @return Returns the doDiagnosis.
     */
    public boolean isDoDiagnosis() {
	return doDiagnosis;
    }

    /**
     * @param doDiagnosis
     *            The doDiagnosis to set.
     */
    public void setDoDiagnosis(final boolean doDiagnosis) {
	this.doDiagnosis = doDiagnosis;
    }

    /**
     * Takes a list of KO attributes and "absorbs" those that have the right
     * archiver name
     * 
     * @param hashtable
     *            A list of KO attributes, which keys are the attributes
     *            complete names and which values are ArchivingAttribute objects
     */
    public void buildKOAttributes(final Map<String, ArchivingAttribute> _errorAttributes) {
	if (_errorAttributes == null || _errorAttributes.size() == 0) {
	    return;
	}

	for (final ArchivingAttribute value : _errorAttributes.values()) {
	    final String attributeArchiver = value.getArchiver();

	    // Tools.trace (
	    // "Archiver/buildKOAttributes/attributeArchiver|"+attributeArchiver+"|"
	    // , Warnable.LOG_LEVEL_DEBUG );
	    if (attributeArchiver != null && attributeArchiver.equals(name)) {
		// Tools.trace (
		// "Archiver/buildKOAttributes/adding attribute|"+attributeCompleteName+"|"
		// , Warnable.LOG_LEVEL_DEBUG );
		addKOAttribute(value);
	    }
	}
    }

    /**
     * @param attribute
     */
    private void addKOAttribute(final ArchivingAttribute attribute) {
	if (attribute.isDetermined()) {
	    KOAttributes.put(attribute.getCompleteName(), attribute);
	}
    }

    /**
     * Returns the kOAttributes.
     * 
     * @return The list of KO attributes, which keys are the attributes complete
     *         names and which values are ArchivingAttribute objects
     */
    public Hashtable<String, ArchivingAttribute> getKOAttributes() {
	return KOAttributes;
    }

    /**
     * Sets the kOAttributes.
     * 
     * @param attributes
     *            The list of KO attributes, which keys are the attributes
     *            complete names and which values are ArchivingAttribute objects
     */
    public void setKOAttributes(final Hashtable<String, ArchivingAttribute> attributes) {
	KOAttributes = attributes;
    }

    /**
     * Returns whether this archiver has at least one KO attribute
     * 
     * @return Whether this archiver has at least one KO attribute
     */
    public boolean hasKOAttributes() {
	if (KOAttributes == null) {
	    return false;
	}
	if (KOAttributes.size() == 0) {
	    return false;
	}

	return true;
    }

}
