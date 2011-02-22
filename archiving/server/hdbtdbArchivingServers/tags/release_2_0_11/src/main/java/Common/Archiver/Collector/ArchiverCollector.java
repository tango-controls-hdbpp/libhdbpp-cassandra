package Common.Archiver.Collector;

import java.util.Hashtable;

import fr.esrf.tangoatk.core.ErrorEvent;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;

public abstract class ArchiverCollector 
{
	// This hashtable contains the mode counters by attribute  
	private Hashtable<String,ModesCounters> m_attributeModeHandlerHastable;
	// Diary file
	protected ILogger m_logger;
	
	/**
	 * An  ModeHandler  is associated to each Collector to handle the archiving mode
	 */
	protected ModeHandler m_modeHandler;
	
	public ArchiverCollector(ModeHandler modeHandler)
	{
		m_attributeModeHandlerHastable = new Hashtable<String,ModesCounters>();
		m_modeHandler = modeHandler;
	}
	
    public void setDiaryLogger(ILogger _logger) 
    {
        this.m_logger = _logger;
    }
    
    /* Allows to indicate to the collector that a new attribute must be managed */
	protected void addAttribute(String name)
	{
		if(m_attributeModeHandlerHastable.containsKey(name))
		{
			m_logger.trace(ILogger.LEVEL_WARNING, "ArchiverCollector.addAttribute : The attribute " + name + "already exists in the map ==> Counter Re-init");
			m_attributeModeHandlerHastable.get(name).init();
		}
		else m_attributeModeHandlerHastable.put(name, new ModesCounters());
	}
	
	/* Allows to indicate to the collector that an attribute has been stopped */
	protected void removeAttribute(String name)
	{
		if(m_attributeModeHandlerHastable.containsKey(name))
			m_attributeModeHandlerHastable.remove(name);
	}
	
	/* Allows to retrieve the ModesCounters object of one attribute */
	protected ModesCounters getModeCounter(String name)
	{
		if (m_attributeModeHandlerHastable.containsKey(name))
			return m_attributeModeHandlerHastable.get(name);
		
		return null;
	}
	
	/* Creates a ScalarEvent object from the ErrorEvent with a null  value */ 
	protected ScalarEvent getNullValueScalarEvent(ErrorEvent errorEvent,int data_type,int writable)
	{
		return new ScalarEvent(errorEvent.getSource().toString(),
				data_type,writable,errorEvent.getTimeStamp(),null);
	}
    /* Return true if the value must be archived, false otherwise */
    protected boolean doArchiveEvent(ModesCounters mc ,int dataType, Object readValueObject, Object lastValueObject, String attCompleteName)
    {
    boolean doArchive = false;
    
        try
        {
            doArchive = m_modeHandler.isDataArchivable(mc ,dataType , readValueObject , lastValueObject);
        }
        catch ( IllegalArgumentException e )
        {
            this.m_logger.trace(ILogger.LEVEL_ERROR, this.getClass().getSimpleName() + "/doArchiveEvent/catch " + e + " with the value of "+ attCompleteName);
        }
        catch(ClassCastException cce)
        {
            this.m_logger.trace(ILogger.LEVEL_ERROR, this.getClass().getSimpleName() + "/doArchiveEvent/catch " + cce + " with the value of "+ attCompleteName);
        }
        catch(Exception e)
        {
            this.m_logger.trace(ILogger.LEVEL_ERROR, this.getClass().getSimpleName() + "/doArchiveEvent/catch " + e + " with the value of "+ attCompleteName);
        }
        
        return doArchive;
    }

}
