//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/image/Image_RO.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Image_RO.
//						(Chinkumo Jean) - Mar 24, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.17 $
//
// $Log: Image_RO.java,v $
// Revision 1.17  2007/09/28 14:49:22  pierrejoseph
// Merged between Polling and Events code
//
// Revision 1.16  2007/06/11 12:18:51  pierrejoseph
// m_logger from the mother class (ArchiverCollector)
//
// Revision 1.15  2007/03/05 16:25:19  ounsy
// non-static DataBase
//
// Revision 1.14  2006/10/31 16:54:12  ounsy
// milliseconds and null values management
//
// Revision 1.13  2006/07/26 08:37:21  ounsy
// try number no more static and reinitialized with change events (errorchange, numberscalarchange, etc...)
//
// Revision 1.12  2006/07/18 08:02:59  ounsy
// minor changes
//
// Revision 1.11  2006/06/16 09:25:33  ounsy
// changed imports because of the diary package moving to the javaapi project
//
// Revision 1.10  2006/06/13 13:38:40  ounsy
// minor changes
//
// Revision 1.9  2006/06/13 13:28:19  ounsy
// added a file logging system (diary) that records data storing errors
//
// Revision 1.8  2006/05/23 11:57:17  ounsy
// now checks the timeCondition condition before calling DbProxy.store
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
// Revision 1.4.2.1  2005/06/13 14:01:04  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.4  2005/04/08 15:37:00  chinkumo
// errorChange method filled.

// The aim of this method is to manage possible attribute's problem while polling attributes. 

// In case of unavailable value, a record is nevertheless carried out with the event timestamp, but with a kind of NULL value.
//
// Revision 1.3  2005/02/04 17:10:12  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:26  chinkumo
// First commit (new architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package HdbArchiver.Collector.image;

import HdbArchiver.Collector.HdbCollector;
import HdbArchiver.Collector.HdbModeHandler;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.Util;
import fr.esrf.tangoatk.core.AttributeStateEvent;
import fr.esrf.tangoatk.core.ConnectionException;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.IImageListener;
import fr.esrf.tangoatk.core.INumberImage;
import fr.esrf.tangoatk.core.NumberImageEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;

public class Image_RO extends HdbCollector implements IImageListener
{
	private static final long serialVersionUID = 8000955241L;
	
	public Image_RO(HdbModeHandler hdbModeHandler)
	{
		super(hdbModeHandler);
	}

	synchronized public void addSource(AttributeLightMode attributeLightMode) throws ArchivingException
	{
		System.out.println("Image_RO.addSource");
		try
		{
			synchronized ( attributeList )
			{
				while ( ( INumberImage ) attributeList.get(attributeLightMode.getAttribute_complete_name()) == null )
				{
					INumberImage attribute = ( INumberImage ) attributeList.add(attributeLightMode.getAttribute_complete_name());
					attribute.addImageListener(this);
					attribute.addErrorListener(this);
					Util.out4.println("\t The attribute named " +
					                  attributeLightMode.getAttribute_complete_name() +
					                  " was hired to the Collector list...");
					if ( attributeList.size() == 1 )
					{
						startCollecting();
					}
				}
			}
		}
		catch ( ConnectionException e )
		{
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "Failed adding '" + attributeLightMode.getAttribute_complete_name() + "' as source";
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing Image_RO.addSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);

		}
	}

	synchronized public void removeSource(String attributeName) throws ArchivingException
	{
		System.out.println("Image_RO.removeSource");
		try
		{
			synchronized ( attributeList )
			{
				/*while ( ( INumberImage ) attributeList.get(attributeName) != null )
				{*/
				
				INumberImage attribute = ( INumberImage ) attributeList.get(attributeName);
                if(attribute != null) {
					attribute.removeImageListener(this);
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
			String desc = "Failed while executing Image_RO.removeSource() method...";
			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , "" , e);

		}
	}

	public void errorChange(ErrorEvent errorEvent)
	{
        int tryNumber = DEFAULT_TRY_NUMBER;
        Util.out3.println("Image_RO.errorChange : " + "Unable to read the attribute named " + errorEvent.getSource().toString());
		Double[][] value = null;
		ImageEvent_RO imageEvent_ro = new ImageEvent_RO();
		imageEvent_ro.setAttribute_complete_name(errorEvent.getSource().toString());
		imageEvent_ro.setTimeStamp(errorEvent.getTimeStamp());
		imageEvent_ro.setImageValueRO(value);
		processEventImage(imageEvent_ro , tryNumber);
	}

	public void imageChange(NumberImageEvent event)
	{
        int tryNumber = DEFAULT_TRY_NUMBER;
        String attribute_name = ( ( INumberImage ) event.getSource() ).getName();
		double[][] value = ( double[][] ) event.getValue();
        int x = 0, y = 0;
        if (value != null)
        {
            x = value.length;
            if(x > 0 && value[0] != null)
            {
                y = value[0].length;
            }
        }
        Double[][] imageValue = new Double[x][y];
        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
                imageValue[i][j] = new Double(value[i][j]);
            }
        }

		ImageEvent_RO imageEvent_RO = new ImageEvent_RO();

		imageEvent_RO.setAttribute_complete_name(attribute_name);
		imageEvent_RO.setTimeStamp(event.getTimeStamp());
		imageEvent_RO.setDim_x(x);
		imageEvent_RO.setDim_y(y);
		imageEvent_RO.setImageValueRO(imageValue);

		processEventImage(imageEvent_RO , tryNumber);
	}

	public void stateChange(AttributeStateEvent event)
	{
	}


	public void processEventImage(ImageEvent_RO snapImageEvent_RO , int try_number)
	{
		Util.out4.println("Image_RO.processEventImage");
        
        boolean timeCondition = super.isDataArchivableTimestampWise ( snapImageEvent_RO );
        if ( !timeCondition )
        {
            return;
        }

		try        //spectrum values can only have periodic modes, we don't need their lastValue
		{
            super.dbProxy.store(snapImageEvent_RO);
            super.setLastTimestamp ( snapImageEvent_RO );
		}
		catch ( Exception e )
		{
            String message = "Problem (ArchivingException) storing Image_RO value";
            super.m_logger.trace ( ILogger.LEVEL_ERROR , message );
            super.m_logger.trace ( ILogger.LEVEL_ERROR , e );
            
			try_number--;
			if ( try_number > 0 )
			{
				System.out.println("Image_RO.processEventImage : \r\n\ttry " + ( try_number ) + "failed...");
				processEventImage(snapImageEvent_RO , try_number);
			}
		}
		checkGC();
	}
}

