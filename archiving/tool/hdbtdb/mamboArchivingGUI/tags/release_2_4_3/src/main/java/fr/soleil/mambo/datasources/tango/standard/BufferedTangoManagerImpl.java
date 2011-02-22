/*	Synchrotron Soleil 
 *  
 *   File          :  BufferedTangoManagerImpl.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  10 mai 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: BufferedTangoManagerImpl.java,v 
 *
 */
/*
 * Created on 10 mai 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.datasources.tango.standard;

import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.tango.util.entity.data.Domain;

public class BufferedTangoManagerImpl extends BasicTangoManagerImpl {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private Hashtable<Criterions, Vector<Domain>> buffer;
    private static final int MAX_BUFFER_SIZE = 20;

    public BufferedTangoManagerImpl() {
	super();
	buffer = new Hashtable<Criterions, Vector<Domain>>(MAX_BUFFER_SIZE);

	// System.out.println ( "CLA/new BufferedTangoManagerImpl!!!!!!!!!!!!!"
	// );
    }

    @Override
    public Vector<Domain> loadDomains(final Criterions searchCriterions, final boolean forceReload) {
	if (searchCriterions == null) {
	    final Vector<Domain> newResult = super.loadDomains(searchCriterions, forceReload);
	    return newResult;
	}

	final Hashtable conditionsHT = searchCriterions.getConditionsHT();
	if (conditionsHT == null) {
	    final Vector<Domain> newResult = super.loadDomains(searchCriterions, forceReload);
	    return newResult;
	}

	final Vector<Domain> bufferisedResult = buffer.get(conditionsHT);
	if (bufferisedResult == null || forceReload) {
	    final Vector<Domain> newResult = super.loadDomains(searchCriterions, forceReload);
	    bufferResult(searchCriterions, newResult);
	    return newResult;
	}

	return bufferisedResult;
    }

    private void bufferResult(final Criterions searchCriterions, final Vector<Domain> newResult) {
	if (searchCriterions == null || newResult == null) {
	    return;
	}

	final int currentBufferSize = buffer.size();
	if (currentBufferSize >= MAX_BUFFER_SIZE) {
	    buffer = new Hashtable(MAX_BUFFER_SIZE);
	}

	try {
	    buffer.put(searchCriterions, newResult);
	} catch (final Throwable t) // Catching possible OutOfMemoryError
	{
	    logger.trace(ILogger.LEVEL_ERROR, t);
	}
    }
}
