//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/scalar/NumberScalar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberScalar.
//						(Chinkumo Jean) - Mar 25, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.34 $
//
// $Log: NumberScalar.java,v $
// Revision 1.34  2007/08/27 14:14:35  pierrejoseph
// Traces addition : the logger object is stored in the TdbCollectorFactory
//
// Revision 1.33  2007/06/14 07:09:37  pierrejoseph
// Exception addition in errorChange + retrieve the data_type of a Number Event
//
// Revision 1.32  2007/06/11 12:16:33  pierrejoseph
// doArchive method has been added in the TdbCollector class with new catched exceptions that can be raised by the isDataArchivable method.
//
// Revision 1.31  2007/06/01 09:47:40  pierrejoseph
// Mantis 5232 : in the errorChange method with the scalarevent was created the data_type was missing so in case of error nothing was stored in the BD
//
// Revision 1.30  2007/05/25 12:03:36  pierrejoseph
// Pb mode counter on various mode in the same collector : one ModesCounters object by attribut stored in a hashtable of the ArchiverCollector object (in common part)
//
// Revision 1.29  2007/04/24 14:29:28  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.28  2007/04/03 15:16:00  ounsy
// removed useless logs
//
// Revision 1.27  2007/03/27 15:17:43  ounsy
// corrected the processEventScalar method to use the isFirstValue boolean
//
// Revision 1.26  2007/03/14 09:17:51  ounsy
// added a call to scalarEvent.avoidUnderFlow () in processNumberScalarEvent
//
// Revision 1.25  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.24  2007/02/13 14:41:45  ounsy
// added diary entry in the case of unexpected exceptions in addSource
//
// Revision 1.23  2007/02/13 14:19:16  ounsy
// corrected a bug in addSource: an infinite nnumber of FileTools instances could potentially be created
//
// Revision 1.22  2007/01/11 14:39:57  ounsy
// minor changes
//
// Revision 1.21  2006/10/19 12:25:51  ounsy
// modfiied the removeSource to take into account the new isAsuynchronous parameter
//
// Revision 1.20  2006/08/23 09:55:15  ounsy
// FileTools compatible with the new TDB file management
// + keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)
//
// Revision 1.19  2006/07/27 12:42:19  ounsy
// modified processEventScalar so that it calls setLastValue even if the current value doesn't have to be archived
//
// Revision 1.18  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.17  2006/07/21 14:40:51  ounsy
// modified processEventScalar: moved the time condition test to imitate Hdb
//
// Revision 1.16  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.15  2006/06/07 12:57:02  ounsy
// minor changes
//
// Revision 1.14  2006/05/23 11:58:03  ounsy
// now checks the timeCondition condition before calling FileTools.processEvent
//
// Revision 1.13  2006/05/16 09:29:53  ounsy
// added what's necessary for the old files deletion mechanism
//
// Revision 1.12  2006/04/05 13:49:51  ounsy
// new types full support
//
// Revision 1.11  2006/03/16 09:37:36  ounsy
// synchronized
//
// Revision 1.10  2006/03/15 16:06:51  ounsy
// corrected the bug where some temp files were randomly not purged
//
// Revision 1.9  2006/02/24 12:09:13  ounsy
// removed useless logs
//
// Revision 1.8  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.7.8.4  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.7.8.3  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.7.8.2  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.7.8.1  2005/09/09 10:17:25  chinkumo
// Since the collecting politic was simplified and improved this class was modified.
//
// Revision 1.7  2005/06/24 12:06:38  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.6  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.5.4.1  2005/06/13 13:57:32  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.5  2005/04/08 15:40:43  chinkumo
// Code reformated.
//
// Revision 1.4  2005/03/07 16:26:25  chinkumo
// Minor change (tag renamed)
//
// Revision 1.3  2005/02/04 17:10:39  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/31 15:09:04  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:24  chinkumo
// First commit (new architecture).
//
// Revision 1.5  2004/09/27 13:17:36  chinkumo
// The addSource method were improved : The two calls 'myFile.checkDirs();' + 'myFile.initFile();' were gathered into one single call (myFile.initialize();).
//
// Revision 1.4  2004/09/14 07:03:43  chinkumo
// Some unused 'import' were removed.
// Some error messages were re-written to fit the 'error policy' recently decided.
//
// Revision 1.3  2004/09/01 15:53:16  chinkumo
// Heading was updated.        
// As the Mode object now includes the exportPeriod information the way to build a FileTools object was modified (see addSource(..)).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package TdbArchiver.Collector.scalar;

import java.util.Hashtable;

import Common.Archiver.Collector.ModesCounters;
import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import TdbArchiver.Collector.Tools.FileTools;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.INumberScalar;
import fr.esrf.tangoatk.core.INumberScalarListener;
import fr.esrf.tangoatk.core.NumberScalarEvent;
import fr.esrf.tangoatk.core.attribute.AAttribute;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public abstract class NumberScalar extends TdbCollector implements INumberScalarListener
{
	/**
	 * This Hashtable is used to store the last value of an attribute. This value will be compared to the current one (cf. Modes)
	 */
	protected Hashtable lastValueHashtable;

	public NumberScalar(TdbModeHandler modeHandler)
	{
		super(modeHandler);
		lastValueHashtable = new Hashtable();
	}

	synchronized public void addSource(AttributeLightMode attributeLightMode) throws ArchivingException
	{
        try
		{
            synchronized ( attributeList )
            {
				/*while ( ( INumberScalar ) attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
				{*/
					INumberScalar attribute = null;
					String att_name = attributeLightMode.getAttribute_complete_name();
					attribute = ( INumberScalar ) attributeList.add(att_name);

					attribute.addNumberScalarListener(this);
					attribute.addErrorListener(this);
					Util.out4.println("\t The attribute named " + att_name+
					                  " was hired to the Collector list...");
					// informs the mother class that one new attribute must be managed
					addAttribute(att_name);
					
					String table_name = super.dbProxy.getDataBase().getDbUtil().getTableName(att_name);
					FileTools myFile = new FileTools(table_name , attributeLightMode.getData_format() , attributeLightMode.getWritable() , attributeLightMode.getMode().getTdbSpec().getExportPeriod() , super.m_logger , true , super.dbProxy);
					myFile.initialize();
					filesNames.put(att_name , myFile);
					// Verify that the recording file exists

					if ( attributeList.size() == 1 )
					{
						startCollecting();
					}
				//}
                    if ( attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
                    {
                        super.m_logger.trace(ILogger.LEVEL_WARNING, "addSource/The first add test failed for attribute|"+att_name);
                    }
            }
		}
		catch ( ConnectionException e )
		{
            super.m_logger.trace(ILogger.LEVEL_ERROR, e);
            
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name()+ "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing NumberScalar.addSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
        catch ( Exception e )
        {
           
           super.m_logger.trace(ILogger.LEVEL_ERROR, e);
           
 	       String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Unknown exception when adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
	       String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	       String desc = "Failed while executing NumberScalar.addSource() method...";
	       throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
        }
	}

	synchronized public void removeSource(String attributeName) throws ArchivingException
	{
		Util.out2.println("NumberScalar.removeSource");
//        System.out.println("********NumberScalar.removeResource");
		try
		{
			synchronized ( attributeList )
			{
				//while ( ( INumberScalar ) attributeList.get(attributeName) != null )
				//{
//                    System.out.println("********removeResource : getAttribute");
				INumberScalar attribute = ( INumberScalar ) attributeList.get(attributeName);
				if(attribute != null) {
//                    System.out.println("********removeResource : removeNumberScalarListener");
					attribute.removeNumberScalarListener(this);
//                    System.out.println("********removeResource : removeErrorListener");
					attribute.removeErrorListener(this);
//                    System.out.println("********removeResource : remove");
					attributeList.remove(attributeName);
					Util.out4.println("\t The attribute named " + attributeName + " was fired from the Collector list...");
					// informs the mother class that one new attribute must be removed
					removeAttribute(attributeName);
//                    System.out.println("********removeResource : closeFile");
					( ( FileTools ) filesNames.get(attributeName) ).closeFile(true);
//                    System.out.println("********removeResource : remove from file");
					filesNames.remove(attributeName);
					if ( attributeList.isEmpty() )
					{
//                        System.out.println("********removeResource : stopCollecting");
						stopCollecting();
					}
				}
				//}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed removing '" + attributeName + "' from sources";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing NumberScalar.removeSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
	}


	public void stateChange(AttributeStateEvent event)
	{
	}

	public void processEventScalar(ScalarEvent scalarEvent , int try_number)
	{
        boolean timeCondition = super.isDataArchivableTimestampWise ( scalarEvent );
        if ( ! timeCondition )
        {
            return;
        }
        
        try
		{
            scalarEvent.avoidUnderFlow ();
            
            boolean doArchive = false;
            if ( this.isFirstValue )
            {
                doArchive = true;
                this.isFirstValue = false;
            }
            else
            {
                /*if ( scalarEvent.getReadValue() == null )
                {
                    super.m_logger.trace(ILogger.LEVEL_WARNING, "NumberScalar/processEventScalar/scalarEvent.getReadValue() == null/for attribute/"+scalarEvent.getAttribute_complete_name());
                }
                if ( getLastValue(scalarEvent) == null ) 
                {
                    super.m_logger.trace(ILogger.LEVEL_WARNING, "NumberScalar/processEventScalar/getLastValue(scalarEvent) == null/for attribute/"+scalarEvent.getAttribute_complete_name());
                }*/
            	ModesCounters mc = getModeCounter(scalarEvent.getAttribute_complete_name());
            	if(mc == null)
                     this.m_logger.trace(ILogger.LEVEL_ERROR, "Attribute Counters unknown");
            	else doArchive = doArchiveEvent(mc, scalarEvent.getData_type(), scalarEvent.getReadValue(), 
            			getLastValue(scalarEvent), scalarEvent.getAttribute_complete_name());
            }
            
            if ( doArchive )
            {
                ( ( FileTools ) filesNames.get(scalarEvent.getAttribute_complete_name()) ).processEventScalar(scalarEvent);
            }
            /*if ( scalarEvent.getReadValue() != null && getLastValue(scalarEvent) != null )
			{
                boolean valueCondition = modeHandler.isDataArchivable(scalarEvent.getData_type() , scalarEvent.getReadValue() , getLastValue(scalarEvent));
                if ( valueCondition )
				{
				    ( ( FileTools ) filesNames.get(scalarEvent.getAttribute_complete_name()) ).processEventScalar(scalarEvent);
				}
			}
			else
			{
				( ( FileTools ) filesNames.get(scalarEvent.getAttribute_complete_name()) ).processEventScalar(scalarEvent);
			}*/
            
            setLastValue(scalarEvent , scalarEvent.getReadValue());
		}
		catch ( Exception e )
		{
			try_number--;
			if ( try_number > 0 )
			{
				Util.out2.println("NumberScalar.processEventScalar : \r\n\ttry " + ( try_number ) + "failed...");
				processEventScalar(scalarEvent , try_number);
			}
		}
		checkGC();
	}

	protected void setLastValue(ScalarEvent scalarEvent , Object lastValue)
	{
		super.setLastTimestamp ( scalarEvent );
        if ( lastValue != null )
        {
			lastValueHashtable.put(scalarEvent.getAttribute_complete_name() , lastValue);
        }
	}

	protected Object getLastValue(ScalarEvent scalarEvent)
	{
		return lastValueHashtable.get(scalarEvent.getAttribute_complete_name());
	}
    
    protected void printException(String cause , int cause_value , String name , DevFailed devFailed)
	{
		String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + cause;
		String reason = "Failed while executing NumberScalar.numberScalarChange()...";
		String desc = ( cause_value != -1 ) ?
		              cause + " (" + cause_value + ") not supported !! [" + name + "]" :
		              "Unable to retrieve the attribute data type";
		Util.out2.println(( devFailed == null ) ?
		                  ( new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "").toString() )
		                  :
		                  ( new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , devFailed) ).toString());
	}
    
	public abstract void numberScalarChange(NumberScalarEvent event);
	
	protected abstract int getWritableValue();
	
	public void errorChange(ErrorEvent errorEvent)
	{
		int tryNumber = DEFAULT_TRY_NUMBER;
		
		String errorMess = this.getClass().getSimpleName() + ".errorChange : Unable to read the attribute named " + errorEvent.getSource().toString();
		Util.out3.println(errorMess);
	    super.m_logger.trace(ILogger.LEVEL_ERROR,errorMess);
	    
	    try
	    {
			processEventScalar(getNullValueScalarEvent(errorEvent,((AAttribute) errorEvent.getSource()).getTangoDataType(),
						getWritableValue()), tryNumber);
		}
		catch(Exception e)
		{
			super.m_logger.trace(ILogger.LEVEL_ERROR,this.getClass().getSimpleName() + ".errorChange : during processEventScalar creation execp : " + e );
			e.printStackTrace();
		}
	}
}
