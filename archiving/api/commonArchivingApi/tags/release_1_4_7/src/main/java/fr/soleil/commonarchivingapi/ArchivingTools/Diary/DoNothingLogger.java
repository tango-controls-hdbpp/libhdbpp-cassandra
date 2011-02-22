/*	Synchrotron Soleil 
 *  
 *   File          :  DoNothingLogger.java
 *  
 *   Project       :  javaapi
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  29 juin 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: DoNothingLogger.java,v 
 *
 */
/*
 * Created on 29 juin 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.commonarchivingapi.ArchivingTools.Diary;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DoNothingLogger implements ILogger {

    public DoNothingLogger() {
	super();
	System.err.println("DoNothingLogger logger, will trace in console");
    }

    public int getTraceLevel() {
	// TODO Auto-generated method stub
	return 0;
    }

    public void setTraceLevel(final int level) {
	// TODO Auto-generated method stub

    }

    public void trace(final int level, final Object o) {
	final Date date = new Date();
	final String format = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss::SS").format(date);
	System.out.println("DoNothingLogger - " + format + " - " + o);

    }

    public void initDiaryWriter(final String path, final String archiver) throws IOException {
	// TODO Auto-generated method stub

    }

    public void close() {
	// TODO Auto-generated method stub

    }

    public int getTraceLevel(final String level_s) {
	// TODO Auto-generated method stub
	return 0;
    }

    public void setTraceLevel(final String string) {
	// TODO Auto-generated method stub

    }

}
