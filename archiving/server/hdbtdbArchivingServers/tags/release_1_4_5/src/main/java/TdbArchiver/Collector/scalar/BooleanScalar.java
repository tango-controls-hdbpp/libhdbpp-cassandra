//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/scalar/BooleanScalar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BooleanScalar.
//						(Chinkumo Jean) - Aug 30, 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.23 $
//
// $Log: BooleanScalar.java,v $
// Revision 1.23  2007/06/14 07:09:36  pierrejoseph
// Exception addition in errorChange + retrieve the data_type of a Number Event
//
// Revision 1.22  2007/06/11 12:16:33  pierrejoseph
// doArchive method has been added in the TdbCollector class with new catched exceptions that can be raised by the isDataArchivable method.
//
// Revision 1.21  2007/06/01 09:47:40  pierrejoseph
// Mantis 5232 : in the errorChange method with the scalarevent was created the data_type was missing so in case of error nothing was stored in the BD
//
// Revision 1.20  2007/05/25 12:03:36  pierrejoseph
// Pb mode counter on various mode in the same collector : one ModesCounters object by attribut stored in a hashtable of the ArchiverCollector object (in common part)
//
// Revision 1.19  2007/04/24 14:29:28  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.18  2007/04/03 15:40:26  ounsy
// removed logs
//
// Revision 1.17  2007/03/27 15:17:43  ounsy
// corrected the processEventScalar method to use the isFirstValue boolean
//
// Revision 1.16  2007/03/20 15:03:15  ounsy
// trying alternate version of processEventScalar
//
// Revision 1.15  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.14  2007/02/13 14:41:45  ounsy
// added diary entry in the case of unexpected exceptions in addSource
//
// Revision 1.13  2007/02/13 14:19:16  ounsy
// corrected a bug in addSource: an infinite nnumber of FileTools instances could potentially be created
//
// Revision 1.12  2006/10/19 12:26:01  ounsy
// modfiied the removeSource to take into account the new isAsuynchronous parameter
//
// Revision 1.11  2006/08/23 09:55:15  ounsy
// FileTools compatible with the new TDB file management
// + keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)
//
// Revision 1.10  2006/07/27 12:42:19  ounsy
// modified processEventScalar so that it calls setLastValue even if the current value doesn't have to be archived
//
// Revision 1.9  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.8  2006/07/21 14:40:50  ounsy
// modified processEventScalar: moved the time condition test to imitate Hdb
//
// Revision 1.7  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.6  2006/05/23 11:58:03  ounsy
// now checks the timeCondition condition before calling FileTools.processEvent
//
// Revision 1.5  2006/05/16 09:29:53  ounsy
// added what's necessary for the old files deletion mechanism
//
// Revision 1.4  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.2  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.1  2005/09/09 10:04:03  chinkumo
// First commit !
// (Dynamic attribut release)
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
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.BooleanScalarEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IBooleanScalar;
import fr.esrf.tangoatk.core.IBooleanScalarListener;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public abstract class BooleanScalar extends TdbCollector implements IBooleanScalarListener
{
	/**
	 * This Hashtable is used to store the last value of an attribute. This value will be compared to the current one (cf. Modes)
	 */
	protected Hashtable lastValueHashtable;

	public BooleanScalar(TdbModeHandler modeHandler)
	{
		super(modeHandler);
		lastValueHashtable = new Hashtable();
	}

	public abstract void booleanScalarChange(BooleanScalarEvent event);
	
	synchronized public void addSource(AttributeLightMode attributeLightMode) throws ArchivingException
	{
		try
		{
			synchronized ( attributeList )
			{
				/*while ( ( IBooleanScalar ) attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
				{*/
					IBooleanScalar attribute = null;
					String att_name = attributeLightMode.getAttribute_complete_name();
					attribute = ( IBooleanScalar ) attributeList.add(att_name);

					attribute.addBooleanScalarListener(this);
					attribute.addErrorListener(this);
					Util.out4.println("\t The attribute named " + att_name +
					                  " was hired to the Collector list...");
					
					// informs the mother class that one new attribute must be managed
					addAttribute(att_name);
					
					String table_name = super.dbProxy.getDataBase().getTableName(att_name);
					FileTools myFile = new FileTools(table_name , attributeLightMode.getData_format() , attributeLightMode.getWritable() , attributeLightMode.getMode().getTdbSpec().getExportPeriod() , super.m_logger , true , super.dbProxy );
					myFile.initialize();
					filesNames.put(att_name , myFile);
					// Verify that the recording file exists

					if ( attributeList.size() == 1 )
					{
						startCollecting();
					}
				//}
                    if ( attributeList.get(att_name) == null )
                    {
                        super.m_logger.trace(ILogger.LEVEL_WARNING, "addSource/The first add test failed for attribute|"+att_name);
                    }
			}
		}
		catch ( ConnectionException e )
		{
            super.m_logger.trace(ILogger.LEVEL_WARNING, e);
            
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing BooleanScalar.addSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
        catch ( Exception e )
        {
            super.m_logger.trace(ILogger.LEVEL_WARNING, "Unexpected exception during addSource:"+ attributeLightMode.getAttribute_complete_name());
            super.m_logger.trace(ILogger.LEVEL_WARNING, e);
        }
	}

	synchronized public void removeSource(String attributeName) throws ArchivingException
	{
		Util.out2.println("BooleanScalar.removeSource");
		try
		{
			synchronized ( attributeList )
			{
				/*while ( ( IBooleanScalar ) attributeList.get(attributeName) != null )
				{*/
				IBooleanScalar attribute = ( IBooleanScalar ) attributeList.get(attributeName);
				if(attribute != null) {	
					attribute.removeBooleanScalarListener(this);
					attribute.removeErrorListener(this);
					attributeList.remove(attributeName);
					
					// informs the mother class that one new attribute must be removed
					removeAttribute(attributeName);
					
					Util.out4.println("\t The attribute named " + attributeName + " was fired from the Collector list...");
					( ( FileTools ) filesNames.get(attributeName) ).closeFile(true);
					filesNames.remove(attributeName);
					if ( attributeList.isEmpty() )
					{
						stopCollecting();
					}
				}
			}
		}
		catch ( Exception e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed removing '" + attributeName + "' from sources";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing BooleanScalar.removeSource() method...";
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
                    super.m_logger.trace(ILogger.LEVEL_WARNING, "BooleanScalar/processEventScalar/scalarEvent.getReadValue() == null/for attribute/"+scalarEvent.getAttribute_complete_name());
                }
                if ( getLastValue(scalarEvent) == null ) 
                {
                    super.m_logger.trace(ILogger.LEVEL_WARNING, "BooleanScalar/processEventScalar/getLastValue(scalarEvent) == null/for attribute/"+scalarEvent.getAttribute_complete_name());
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
				Util.out2.println("BooleanScalar.processEventScalar : \r\n\ttry " + ( try_number ) + "failed...");
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
		String reason = "Failed while executing BooleanScalar.booleanScalarChange()...";
		String desc = ( cause_value != -1 ) ?
		              cause + " (" + cause_value + ") not supported !! [" + name + "]" :
		              "Unable to retrieve the attribute data type";
		Util.out2.println(( devFailed == null ) ?
		                  ( new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "").toString() )
		                  :
		                  ( new ArchivingException(message , reason , ErrSeverity.PANIC , desc , "" , devFailed) ).toString());
	}
	
	protected abstract int getWritableValue();
	
	public void errorChange(ErrorEvent errorEvent)
	{
		int tryNumber = DEFAULT_TRY_NUMBER;
		
		String errorMess = this.getClass().getSimpleName() + ".errorChange : Unable to read the attribute named " + errorEvent.getSource().toString();
		Util.out3.println(errorMess);
	    m_logger.trace(ILogger.LEVEL_ERROR,errorMess);
	    
	    try
	    {
	    	processEventScalar(getNullValueScalarEvent(errorEvent,TangoConst.Tango_DEV_BOOLEAN,getWritableValue()), tryNumber);
		}
		catch(Exception e)
		{
			super.m_logger.trace(ILogger.LEVEL_ERROR,this.getClass().getSimpleName() + ".errorChange : during processEventScalar creation execp : " + e );
			e.printStackTrace();
		}
	}
}
