//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NumberSpectrum_RO.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.13  2007/10/03 15:23:29  pierrejoseph
// Minor changes
//
// Revision 1.12  2007/09/28 14:49:22  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.11  2007/09/25 14:59:17  pierrejoseph
// 5251 : sometimes the device stayed in Running state sue to a blocking while in  the addAttribute method.
//
// Revision 1.10  2007/06/11 12:18:51  pierrejoseph
// m_logger from the mother class (ArchiverCollector)
//
// Revision 1.9  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.8  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
//
// Revision 1.7  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.6  2006/07/21 14:39:26  ounsy
// removed useless logs
//
// Revision 1.5  2006/07/18 08:03:18  ounsy
// minor changes
//
// Revision 1.4  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.3  2006/06/13 13:28:19  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.2  2006/05/23 11:57:17  ounsy
// now checks the timeCondition condition before calling DbProxy.store
//
// Revision 1.1  2006/05/03 14:28:16  ounsy
// renamed Spectrum_... into NumberSpectrum_...
//
// Revision 1.9  2006/04/05 13:49:51  ounsy
// new types full support
//
// Revision 1.8  2006/03/28 11:13:49  ounsy
// better spectrum management
//
// Revision 1.7  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.6.10.2  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.6.10.1  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.6  2005/06/24 12:06:27  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.5  2005/06/14 10:30:28  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.2.1  2005/06/13 14:01:07  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/04/08 15:37:00  chinkumo
// errorChange method filled.

// The aim of this method is to manage possible attribute's problem while polling attributes. 

// In case of unavailable value, a record is nevertheless carried out with the event timestamp, but with a kind of NULL value.
//
// Revision 1.3  2005/02/04 17:10:14  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:25  chinkumo
// First commit (new architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package HdbArchiver.Collector.spectrum;

import java.util.HashMap;

import HdbArchiver.HdbArchiver;
import HdbArchiver.Collector.HdbCollector;
import HdbArchiver.Collector.HdbModeHandler;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoApi.events.ITangoArchiveListener;
import fr.esrf.TangoApi.events.TangoArchive;
import fr.esrf.TangoApi.events.TangoArchiveEvent;
import fr.esrf.TangoApi.events.TangoEventsAdapter;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.Device;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.INumberSpectrum;
import fr.esrf.tangoatk.core.ISpectrumListener;
import fr.esrf.tangoatk.core.NumberSpectrumEvent;
import fr.esrf.tangoatk.core.attribute.AttributeFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;

public class NumberSpectrum_RO extends HdbCollector implements ISpectrumListener, ITangoArchiveListener
{
	private static final long serialVersionUID = 4013L;
	
	private HashMap <Device, TangoEventsAdapter> evtAdaptHMap;
	
	public NumberSpectrum_RO(HdbModeHandler hdbModeHandler)
	{
		super(hdbModeHandler);
		evtAdaptHMap = new HashMap <Device, TangoEventsAdapter> ();
	}

	synchronized public void addSource(AttributeLightMode attributeLightMode) throws ArchivingException
	{
		if(HdbArchiver.isUseEvents)
		{
			addSourceForEvents(attributeLightMode);
		}
		else
		{
			addSourceForPolling(attributeLightMode);
		}
	}
	
	synchronized public void removeSource(String attributeName) throws ArchivingException
	{
		Util.out2.println("NumberSpectrum_RO.removeSource");
		
		if(HdbArchiver.isUseEvents)
		{
			removeSourceForEvents(attributeName);
		}

		try
		{
			synchronized ( attributeList )
			{
				/*while ( ( INumberSpectrum ) attributeList.get(attributeName) != null )
				{*/
				INumberSpectrum attribute = ( INumberSpectrum ) attributeList.get(attributeName);
	            if(attribute != null) {

					attribute.removeSpectrumListener(this);
					attribute.removeErrorListener(this);

					this.attributeList.remove(attributeName);
					Util.out4.println("\t The attribute named " + attributeName + " was fired from the Collector list...");
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
			String desc = "Failed while executing NumberSpectrum_RO.removeSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
	}

	private void addSourceForPolling(AttributeLightMode attributeLightMode) throws ArchivingException
	{
		try
		{
			synchronized ( attributeList )
			{
				//while ( ( INumberSpectrum ) attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
				//{
					INumberSpectrum attribute = ( INumberSpectrum ) attributeList.add(attributeLightMode.getAttribute_complete_name());
					attribute.addSpectrumListener(this);
					attribute.addErrorListener(this);
					Util.out4.println("\t The attribute named " +
					                  attributeLightMode.getAttribute_complete_name() +
					                  " was hired to the Collector list...");
					if ( attributeList.size() == 1 )
					{
						startCollecting();
					}
				//}
			}
		}
		catch ( ConnectionException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing NumberSpectrum_RO.addSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
	}
//	--------------------------------------------------------------------------//
//	ELETTRA : Archiving Events 
//	--------------------------------------------------------------------------//	
    private void addSourceForEvents(AttributeLightMode attributeLightMode) throws ArchivingException
	{
		/* Adapter for the Tango archive events */
		TangoEventsAdapter evtAdapt = null;
		INumberSpectrum attribute = null;
		
		try
		{
			/* Get the attribute from the AttributeFactory, so that it is not added to the 
			 * attribute polled list. Remember that `attributeList' is an AttributePolledList().
			 */
			attribute = (INumberSpectrum) AttributeFactory.getInstance().
			getAttribute(attributeLightMode.getAttribute_complete_name());
			if(attribute == null)
			{
				System.out.println("\033[1;31mNumberSpectrum_RO.java: the attribute \"" +
						attributeLightMode.getAttribute_complete_name() +
				" is null (NumberSpectrum_RO.java): not adding it!");
				return;
			}
		}
		catch (DevFailed e)
		{
			System.out.println("\033[1;31mNumberSpectrum_RO.java: Failed to get the attribute from the AttributeFactory\033[0m");
			print_exception(e);
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
			String reason = "DevFailed exception while calling AttributeFactory.getInstance().getAttribute();";
			String desc = "Failed while executing Spectrum_RO.addSource() method...\nSpectrum_RO.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
		catch ( ConnectionException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing NumberSpectrum_RO.addSource() method...\nNumberSpectrum_RO.java: Failed to get the attribute from the AttributeFactory";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		} /* end of first try/catch block, to retrieve the attribute from the AttributeFactory */

		System.out.print("\033[1;44mregistering\033[0m the \033[4mSPECTRUM\033[0m [RO] attribute \"" + attribute.getNameSansDevice() + 
		"\"...\t");
		System.out.flush();
		
		/* If an attribute (name + device server) is already in the map, it means it
		 * has already been registered for the Tango Archive events.
		 * So it does not have to be added to the archive events once again, nor
		 * to the attributePolledList: we can return.
		 * The same goes for an attribute already present in the attributePolledList
		 * `attributeList'.
		 */		
		/*if(HdbArchiver.useEvents.equalsIgnoreCase("no"))
		{
			System.out.print("[\033[1;35mEVENTS DISABLED\033[0m].");
			registerForScalarListener(attributeLightMode);
		}
		else
		{*/
			try{
				synchronized(evtAdaptHMap)
				{
					evtAdapt = evtAdaptHMap.get(attribute.getDevice() );
					if(evtAdapt == null)
					{
						evtAdapt = new TangoEventsAdapter(attribute.getDevice() );
						evtAdaptHMap.put(attribute.getDevice(), evtAdapt);
					}
					else
						System.out.println("\nThe adapter for the attribute is already configured");
				}
				String[] filter = new String[0];

				/* Try to register for the archive events: evtAdapt is the new adapter or the one
				 * already found in the map.
				 */
				evtAdapt.addTangoArchiveListener(this, attribute.getNameSansDevice(), filter );
				archive_listeners_counter++;
				System.out.println("[\033[0;42mEVENTS\033[0m]." );
			}
			catch(DevFailed e)
			{
			/* If archive events are not enabled for the attribute, we will poll it */
			for(int err = 0; err < e.errors.length; err++)
			{
				
				if(e.errors[err].desc.contains("Already connected to event"))
				{
					System.out.println("Already connected to events for attribute");
					return;
				}
				else if(e.errors[err].desc.contains("The polling (necessary to send events) for the attribute") )
				{
					System.out.print(" {\033[1;35mpoller not started\033[0m} ");
					break;
				}
				else if(e.errors[err].desc.contains("Archive event properties") )
				{
					System.out.print(" {\033[1;35marchive event properties not set\033[0m} ");
					break;
				}
				else
					System.out.println("error registering: " + e.errors[err].desc);	

			}
				/* Register for polling */
				addSourceForPolling(attributeLightMode);
			 /* unlock the attributeList */
			}
		//} /* function finishes */
	}
    
	private void removeSourceForEvents(String attributeName)
	{
		Util.out2.println("NumberSpectrum_RO.removeSource");
		System.out.print("\033[0;42mremoving\033[0m source for \033[4mspectrum\033[0m [RO]: " + attributeName + "\"...\t");
		TangoEventsAdapter adapter;
		INumberSpectrum attribute;
		/* If useEvents is enabled, we should remove the eventListener, if not
		 * we can skip this piece of code
		 */
		//if (HdbArchiver.useEvents.equalsIgnoreCase("yes")) 
		//{
			try {
				synchronized (evtAdaptHMap) 
				{
					attribute = (INumberSpectrum) AttributeFactory.getInstance()
							.getAttribute(attributeName);
					/*
					 * Retrieve in the hash table the event adapter associated
					 * to the attributeName.
					 */
					if (attribute == null)
						System.out.println("\033[1;31mThe attribute "
								+ attributeName + " is null\033[0m");
					adapter = evtAdaptHMap.get(attribute.getDevice()); 
					
					if (adapter != null) 
					{
						adapter.removeTangoArchiveListener(this, attribute
								.getNameSansDevice());
						archive_listeners_counter--;
						System.out.println("[\033[0;42mEVENTS\033[0m].");
						Util.out2.println(" (adapter: " + adapter.device_name() + ")");
						/* Should be ok now to return */
						return;
					} 
				} /* unlock event adapters map */
			} 
			catch (ConnectionException e) /* getAttribute() failed */
			{
				Util.out2.println(
						"\033[1;31mConnection exception while retrieving the attribute from the AttributeFactory!\033[0m");
			} 
			catch (DevFailed f) 
			{
				Util.out2.println("Error removing tangoArchiveListener");
				for (int err = 0; err < f.errors.length; err++)
					Util.out2.println("error: " + f.errors[err].desc);
			}
		//}
	}
	
	/** 
	 * @since events
	 * @author giacomo S. elettra
	 * This is called when a Tango archive event is sent from the server
	 * (the attribute has changed)
	 */
	public void archive(TangoArchiveEvent event)
	{
		int tryNumber = DEFAULT_TRY_NUMBER;
		DeviceAttribute attrib = null;
		TangoArchive arch;
		DeviceProxy proxy;
		SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		
		try{
			attrib = event.getValue();
		}
		catch(DevFailed f)
		{
			System.out.println("Spectrum_RO.java: archive(): Error getting archive event attribute value");
			print_exception(f);
			f.printStackTrace();
		    return;
		}
		catch (Exception e) /* Shouldn't be reached */
		{
			System.out.println("Spectrum_RO.java.archive.getValue() failed, caught generic Exception, code failure");
			e.printStackTrace();
			return;
		}
		try{
			/* To correctly archive the attribute, we have to know its complete name.
			 * To acquire this information, we must o back to the TangoArchive object
			 * (which contains the DeviceProxy).
			 */
			arch = (TangoArchive) event.getSource();
			proxy = arch.getEventSupplier(); /* The device that supplied the event */
			if(arch == null || proxy == null || attrib == null)
			{
				System.out.println("\033[1;31mSpectrum_RO.java: event.getValue() or event.getSource() or event.getSource().getEventSupplier()) returned null!\033[0m");
				return;
			}
			int i;
			int dim = attrib.getDimX();
			spectrumEvent_ro.setAttribute_complete_name(proxy.name() + "/" + attrib.getName());
			spectrumEvent_ro.setTimeStamp(attrib.getTime() );
			spectrumEvent_ro.setData_type(attrib.getType());
			spectrumEvent_ro.setWritable(AttrWriteType._READ);		
			spectrumEvent_ro.setDim_x(dim);	
			
			switch(attrib.getType() )
			{
				case TangoConst.Tango_DEV_SHORT:
					Short [] value = new Short[dim];
					short[] tval = new short[dim];
					tval = attrib.extractShortArray();
					for(i = 0; i < dim; i++)
						value[i] = (Short) tval[i];
					spectrumEvent_ro.setValue(value);
					break;
					
				case TangoConst.Tango_DEV_USHORT:
					/* Unsigned short is mapped to an integer */
					int [] usvalue = new int[dim];
					Integer[] ushort = new Integer[dim]; 
					usvalue = attrib.extractUShortArray();
					for(i = 0; i < dim; i++)
						ushort[i] = (Integer) usvalue[i];
					spectrumEvent_ro.setValue(ushort);					
					break;
					
				case TangoConst.Tango_DEV_LONG:
				case TangoConst.Tango_DEV_ULONG:
					int [] lval = new int[dim];
					Integer[] lvalue = new Integer[dim];
					lval = attrib.extractLongArray();

					for(i = 0; i < dim; i++)
						lvalue[i] = (Integer) lval[i]; 
					spectrumEvent_ro.setValue(lvalue);
					break;		
					
				case TangoConst.Tango_DEV_FLOAT:
					float[] fvaluetmp  = attrib.extractFloatArray(); 
					Float[] fvalue = new Float[dim];
					for(i = 0; i < dim; i++)
						fvalue[i] = fvaluetmp[i];
					spectrumEvent_ro.setValue(fvalue);
					break;
					
				case TangoConst.Tango_DEV_BOOLEAN:
					boolean[] bvaluetmp = attrib.extractBooleanArray();
					Boolean[] bvalue = new Boolean[dim];
					for(i = 0; i < dim; i++)
						bvalue[i] = bvaluetmp[i];
					spectrumEvent_ro.setValue(bvalue);
					System.out.println("\033[1;31mboolean spectrum here? In NumberSpectrum_RW!!??\033[0m");
					break;
					
				case TangoConst.Tango_DEV_DOUBLE:
				default:
					double[] dval  = new double[dim]; 
					Double[] dvalue = new Double[dim];
					dval = attrib.extractDoubleArray();
					for(i = 0; i < dim; i++)
						dvalue[i] = dval[i];
					spectrumEvent_ro.setValue(dvalue);
					break;

			}
			System.out.println(proxy.name() + ": " +attrib.getName() + 
					" {\033[4mspectrum\033[0m, RO} [\033[1;32mEVENT\033[0m]" 
					+ " (dim: " + attrib.getDimX() + ")");
			
			/* Process the just built spectrum event */
			processEventSpectrum(spectrumEvent_ro , tryNumber);
			
		}
		catch ( DevFailed devFailed )
		{
			print_exception("Spectrum_RO.java: archive() : " + 
					GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + 
					"Problem while reading " + spectrumEvent_ro.getAttribute_complete_name() + " values...",
					devFailed);		
			Object value = null;
			spectrumEvent_ro.setValue(value);
			processEventSpectrum(spectrumEvent_ro , tryNumber);
		}
		catch ( Exception exE )
		{
			System.err.println("Spectrum_RO.java: archive : " + GlobalConst.ARCHIVING_ERROR_PREFIX + "\r\n\t" + "Problem while reading " + spectrumEvent_ro.getAttribute_complete_name() + " values...");
			exE.printStackTrace();
			Object value = null;
			spectrumEvent_ro.setValue(value);
			processEventSpectrum(spectrumEvent_ro , tryNumber);
		}
		
	}
		
//	--------------------------------------------------------------------------//
//	ELETTRA : Archiving Events 
//	--------------------------------------------------------------------------//	    	
	public void errorChange(ErrorEvent errorEvent)
	{
        int tryNumber = DEFAULT_TRY_NUMBER;
        Util.out3.println("NumberSpectrum_RO.errorChange : " + "Unable to read the attribute named " + errorEvent.getSource().toString());
		Double[] value = null;
		SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		spectrumEvent_ro.setAttribute_complete_name(errorEvent.getSource().toString());
		spectrumEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
		spectrumEvent_ro.setValue(value);
		processEventSpectrum(spectrumEvent_ro , tryNumber);
	}

	public void spectrumChange(NumberSpectrumEvent event)
	{
        int tryNumber = DEFAULT_TRY_NUMBER;
        double[] spectrumvalue = event.getValue();
        Double[] value;
        if (spectrumvalue == null) value = null;
        else
        {
            value = new Double[spectrumvalue.length];
            for (int i = 0; i < spectrumvalue.length; i++)
            {
                value[i] = new Double(spectrumvalue[i]);
            }
        }
		SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		spectrumEvent_ro.setAttribute_complete_name(( ( INumberSpectrum ) event.getSource() ).getName());
		spectrumEvent_ro.setDim_x(value.length);
		spectrumEvent_ro.setTimeStamp(event.getTimeStamp());
		spectrumEvent_ro.setValue(value);
		processEventSpectrum(spectrumEvent_ro , tryNumber);
	}

	public void stateChange(AttributeStateEvent event)
	{
	}

	public void processEventSpectrum(SpectrumEvent_RO snapSpectrumEvent_RO , int try_number)
	{
		boolean timeCondition = super.isDataArchivableTimestampWise ( snapSpectrumEvent_RO );
        if ( !timeCondition )
        {
            return;
        }
        
		try        //spectrum values can only have periodic modes, we don't need their lastValue
		{
            super.dbProxy.store(snapSpectrumEvent_RO);
            super.setLastTimestamp ( snapSpectrumEvent_RO );
		}
		catch ( ArchivingException e )
		{
            String message = "Problem (ArchivingException) storing NumberSpectrum_RO value";
            super.m_logger.trace ( ILogger.LEVEL_ERROR , message );
            super.m_logger.trace ( ILogger.LEVEL_ERROR , e );
            
            try_number--;
			if ( try_number > 0 )
			{
				Util.out2.println("NumberSpectrum_RO.processEventSpectrum : \r\n\ttry " + ( try_number ) + "failed...");
				processEventSpectrum(snapSpectrumEvent_RO , try_number);
			}
		}
		checkGC();
	}
}

