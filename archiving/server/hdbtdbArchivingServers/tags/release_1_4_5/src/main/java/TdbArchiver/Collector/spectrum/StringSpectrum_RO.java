//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Spectrum_RO.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.12  2007/06/01 09:45:53  pierrejoseph
// Attribute ArchiverCollectro.logger has been renamed in m_logger
//
// Revision 1.11  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.10  2007/02/13 14:41:45  ounsy
// added diary entry in the case of unexpected exceptions in addSource
//
// Revision 1.9  2007/02/13 14:19:16  ounsy
// corrected a bug in addSource: an infinite nnumber of FileTools instances could potentially be created
//
// Revision 1.8  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
//
// Revision 1.7  2006/10/19 12:25:51  ounsy
// modfiied the removeSource to take into account the new isAsuynchronous parameter
//
// Revision 1.6  2006/08/23 09:55:15  ounsy
// FileTools compatible with the new TDB file management
// + keeping period removed from FileTools (it was already no more used, but the parameter was still here. Only removed a no more used parameter)
//
// Revision 1.5  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.4  2006/06/08 08:34:31  ounsy
// added new diary logging system: the results of tmp file exports are logged in a text file (one per archiver and per day)
//
// Revision 1.3  2006/05/23 11:58:03  ounsy
// now checks the timeCondition condition before calling FileTools.processEvent
//
// Revision 1.2  2006/05/16 09:30:30  ounsy
// added what's necessary for the old files deletion mechanism
//
// Revision 1.1  2006/04/05 13:49:51  ounsy
// new types full support
//
// Revision 1.8  2006/03/28 11:14:38  ounsy
// better spectrum management
//
// Revision 1.7  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.6.8.2  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.6.8.1  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.6  2005/06/24 12:06:38  chinkumo
// Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.

// This change was reported here.
//
// Revision 1.5  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.4.1  2005/06/13 13:49:34  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/04/08 15:37:06  chinkumo
// errorChange method filled.

// The aim of this method is to manage possible attribute's problem while polling attributes. 

// In case of unavailable value, a record is nevertheless carried out with the event timestamp, but with a kind of NULL value.
//
// Revision 1.3  2005/02/04 17:10:39  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/31 15:09:04  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:26  chinkumo
// First commit (new architecture).
//
// Revision 1.5  2004/09/27 13:17:36  chinkumo
// The addSource method were improved : The two calls 'myFile.checkDirs();' + 'myFile.initFile();' were gathered into one single call (myFile.initialize();).
//
// Revision 1.4  2004/09/14 06:55:44  chinkumo
// Some unused 'import' were removed.
// Some error messages were re-written to fit the 'error policy' recently decided.
//
// Revision 1.3  2004/09/01 15:38:21  chinkumo
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

package TdbArchiver.Collector.spectrum;

import TdbArchiver.Collector.TdbCollector;
import TdbArchiver.Collector.TdbModeHandler;
import TdbArchiver.Collector.Tools.FileTools;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IStringSpectrum;
import fr.esrf.tangoatk.core.IStringSpectrumListener;
import fr.esrf.tangoatk.core.StringSpectrumEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;


public class StringSpectrum_RO extends TdbCollector implements IStringSpectrumListener
{
	public StringSpectrum_RO(TdbModeHandler modeHandler)
	{
		super(modeHandler);
	}

	synchronized public void addSource(AttributeLightMode attributeLightMode) throws ArchivingException
	{
		try
		{
			synchronized ( attributeList )
			{
				/*while ( ( IStringSpectrum ) attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
				{*/
					IStringSpectrum attribute = ( IStringSpectrum ) attributeList.add(attributeLightMode.getAttribute_complete_name());
					attribute.addListener(this);
					attribute.addErrorListener(this);
					Util.out4.println("\t The attribute named " +
					                  attributeLightMode.getAttribute_complete_name() +
					                  " was hired to the Collector list...");
					String table_name = super.dbProxy.getDataBase().getTableName(attributeLightMode.getAttribute_complete_name());
					FileTools myFile = new FileTools(table_name , attributeLightMode.getData_format() , attributeLightMode.getWritable() , attributeLightMode.getMode().getTdbSpec().getExportPeriod() , super.m_logger , true , super.dbProxy );
					myFile.initialize();
					filesNames.put(attributeLightMode.getAttribute_complete_name() , myFile);

					if ( attributeList.size() == 1 )
					{
						startCollecting();
					}
				//}
                    if ( attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
                    {
                        super.m_logger.trace(ILogger.LEVEL_WARNING, "addSource/The first add test failed for attribute|"+attributeLightMode.getAttribute_complete_name());
                    }
			}
		}
		catch ( ConnectionException e )
		{
            super.m_logger.trace(ILogger.LEVEL_WARNING, e);
            
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing Spectrum_RO.addSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
        catch ( Exception e )
        {
            super.m_logger.trace(ILogger.LEVEL_WARNING, "Unexpected exception during addSource:");
            super.m_logger.trace(ILogger.LEVEL_WARNING, e);
        }
	}

	synchronized public void removeSource(String attributeName) throws ArchivingException
	{
		System.out.println("StringSpectrum_RO.removeSource");
		try
		{
			synchronized ( attributeList )
			{
				/*while ( ( IStringSpectrum ) attributeList.get(attributeName) != null )
				{*/
				IStringSpectrum attribute = ( IStringSpectrum ) attributeList.get(attributeName);
				
				if(attribute != null) {

					attribute.removeListener(this);
					attribute.removeErrorListener(this);

					this.attributeList.remove(attributeName);
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
			String desc = "Failed while executing StringSpectrum_RO.removeSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);
		}
	}

	public void errorChange(ErrorEvent errorEvent)
	{
        int tryNumber = DEFAULT_TRY_NUMBER;
        Util.out3.println("StringSpectrum_RO.errorChange : " + "Unable to read the attribute named " + errorEvent.getSource().toString());
        String[] value = null;
		SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		spectrumEvent_ro.setAttribute_complete_name(( ( IStringSpectrum ) errorEvent.getSource() ).getName());
		spectrumEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
		spectrumEvent_ro.setValue(value);
		processEventSpectrum(spectrumEvent_ro , tryNumber);
	}

	public void stringSpectrumChange(StringSpectrumEvent event)
	{
        int tryNumber = DEFAULT_TRY_NUMBER;
        String[] value = event.getValue();
		SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
		spectrumEvent_ro.setAttribute_complete_name(( ( IStringSpectrum ) event.getSource() ).getName());
		spectrumEvent_ro.setDim_x(value.length);
		spectrumEvent_ro.setTimeStamp(event.getTimeStamp());
		spectrumEvent_ro.setValue(value);

		processEventSpectrum(spectrumEvent_ro , tryNumber);

	}

	public void stateChange(AttributeStateEvent event)
	{
	}

	public void processEventSpectrum(SpectrumEvent_RO spectrumEvent_ro , int try_number)
	{
		Util.out4.println("StringSpectrum_RO.processEventSpectrum");
        
        boolean timeCondition = super.isDataArchivableTimestampWise ( spectrumEvent_ro );
        if ( !timeCondition )
        {
            return;
        }
            
		try
		{
			( ( FileTools ) filesNames.get(spectrumEvent_ro.getAttribute_complete_name()) ).processEventSpectrum(spectrumEvent_ro);
            super.setLastTimestamp ( spectrumEvent_ro );
		}
		catch ( Exception e )
		{
			Util.out2.println("ERROR !! " + "\r\n" +
			                  "\t Origin : \t " + "StringSpectrum_RO.processEventSpectrum" + "\r\n" +
			                  "\t Reason : \t " + e.getClass().getName() + "\r\n" +
			                  "\t Description : \t " + e.getMessage() + "\r\n" +
			                  "\t Additional information : \t " + "" + "\r\n");
			e.printStackTrace();
		}
		checkGC();
	}
}
