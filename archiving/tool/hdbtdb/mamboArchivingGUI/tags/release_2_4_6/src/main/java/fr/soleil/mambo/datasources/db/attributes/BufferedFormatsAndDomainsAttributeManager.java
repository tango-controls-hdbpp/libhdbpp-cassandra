/*
 * Synchrotron Soleil File : CompletelyBufferedAttrDBManagerImpl.java Project :
 * mambo Description : Author : CLAISSE Original : 10 mai 2006 Revision: Author:
 * Date: State: Log: CompletelyBufferedAttrDBManagerImpl.java,v
 */
/*
 * Created on 10 mai 2006 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.db.attributes;

import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Condition;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.tango.util.entity.data.Domain;

public class BufferedFormatsAndDomainsAttributeManager extends BufferedFormatsAttributeManager {

    private final Hashtable<Criterions, Vector<Domain>> hdbBuffer;
    private final Hashtable<Criterions, Vector<Domain>> tdbBuffer;
    private static final int MAX_BUFFER_SIZE_DB = 20;
    private static final Criterions NULL_CRITERION = new Criterions() {

	private static final long serialVersionUID = -6486138663602394620L;

	@Override
	public void addCondition(final Condition condition) {
	    // nothing
	    // to
	    // do
	}

	@Override
	public Condition[] getConditions(final String columnName) {
	    return null;
	}

	@Override
	public java.util.Hashtable<String, java.util.Vector<Condition>> getConditionsHT() {
	    return null;
	};

	@Override
	public String toString() {
	    return "No Criterion";
	};
    };

    public BufferedFormatsAndDomainsAttributeManager() {
	super();

	hdbBuffer = new Hashtable<Criterions, Vector<Domain>>(MAX_BUFFER_SIZE_DB);
	tdbBuffer = new Hashtable<Criterions, Vector<Domain>>(MAX_BUFFER_SIZE_DB);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * mambo.datasources.db.IAttrDBManager#loadDomains(fr.soleil.snapArchivingApi
     * .SnapshotingTools.Tools.Criterions, boolean, boolean)
     */
    @Override
    public Vector<Domain> loadDomains(final Criterions searchCriterions, final boolean _historic,
	    final boolean forceReload) {
	Vector<Domain> bufferisedResult;
	final Hashtable<Criterions, Vector<Domain>> domainsBuffer = _historic ? hdbBuffer
		: tdbBuffer;

	if (searchCriterions == null) {
	    return this.loadDomains(_historic, forceReload);
	} else {
	    final Hashtable<String, Vector<Condition>> conditionsHT = searchCriterions
		    .getConditionsHT();

	    if (conditionsHT == null) {
		return this.loadDomains(_historic, forceReload);
	    }

	    bufferisedResult = domainsBuffer.get(conditionsHT);
	    if (bufferisedResult == null || forceReload) {
		final Vector<Domain> newResult = super.loadDomains(searchCriterions, _historic,
			forceReload);
		domainsBuffer.put(searchCriterions, newResult);
		return newResult;
	    }
	}
	return bufferisedResult;
    }

    @Override
    public Vector<Domain> loadDomains(final boolean _historic, final boolean forceReload) {
	final Hashtable<Criterions, Vector<Domain>> domainsBuffer = _historic ? hdbBuffer
		: tdbBuffer;
	final Vector<Domain> bufferisedResult = domainsBuffer.get(NULL_CRITERION);

	if (bufferisedResult == null || forceReload) {
	    final Vector<Domain> newResult = super.loadDomains(_historic, forceReload);
	    if (newResult != null) {
		domainsBuffer.put(NULL_CRITERION, newResult);
	    }
	    return newResult;
	} else {
	    return bufferisedResult;
	}
    }
}
