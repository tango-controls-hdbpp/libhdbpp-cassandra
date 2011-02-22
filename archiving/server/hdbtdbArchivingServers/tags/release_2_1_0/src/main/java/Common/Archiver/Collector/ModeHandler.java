package Common.Archiver.Collector;

import fr.esrf.Tango.DevState;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.Util;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;

public abstract class ModeHandler 
{
	// This class has been defined as abstract just to keep one object in the Hdb part and an other in the Tdb part
	// It has been done after the archiver developement, and it allows to reduce the impacts in the archivers code  
	private Mode m_mode;
	
	private int m_factorModePeriodic = 0;
	private int m_factorModeAbsolute = 0;
	private int m_factorModeRelative = 0;
	private int m_factorModeThreshold = 0;
	private int m_factorModeCalcul = 0;
	private int m_factorModeDifference = 0;
	private int m_factorModeExtern = 0;
	
	private String m_myName;
	
	private int m_refresherInterval;
    private boolean m_hasRounding = true;
    
	public ModeHandler(Mode archiverMode)
	{
		this.m_mode = archiverMode;
		this.m_myName = this.getClass().getSimpleName();
		
		// Minimum polling interval definition
		setMinRefreshInterval();
		// Mode factor calculation
		initFactor();
	}
	
	public int getRefreshInterval()
	{
		return m_refresherInterval;
	}
	
	public Mode getMode()
	{
		return m_mode;
	}
	
	public boolean isDataArchivable(ModesCounters attModeCounter,int data_type , Object currentEvent , Object previousEvent)
	{
		double currentEventD = -1;
		double previousEventD = -1;
		
        boolean isCurrentNull = (currentEvent == null);
        boolean isPreviousNull = (previousEvent == null);
        
        switch ( data_type )
		{
			case TangoConst.Tango_DEV_STRING:
                String currentEventString = currentEvent == null ? "" : (String) currentEvent;
                String previousEventString = previousEvent == null ? "" : (String) previousEvent;
                if ( ! currentEventString.equals ( previousEventString ) )
                {
                    currentEventD = previousEventD + 1;//so that the difference mode's archiving condition is triggered
                }
				break;
			case TangoConst.Tango_DEV_STATE:
                DevState currentEventState = (DevState) currentEvent;
                DevState previousEventState = (DevState) previousEvent;
                currentEventD = isCurrentNull ? -1 : currentEventState.value ();
                previousEventD = isPreviousNull ? -1 : previousEventState.value ();
                //System.out.println( "TdbModeHandler/isDataArchivable/currentEventD/"+currentEventD+"/previousEventD/"+previousEventD );
				break;
			case TangoConst.Tango_DEV_UCHAR:
				currentEventD = isCurrentNull ? -1 : ( ( Byte ) currentEvent ).doubleValue();
				previousEventD = isPreviousNull ? -1 : ( ( Byte ) previousEvent ).doubleValue();
				break;
			case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_ULONG:
				currentEventD = isCurrentNull ? -1 : ( ( Integer ) currentEvent ).doubleValue();
				previousEventD = isPreviousNull ? -1 : ( ( Integer ) previousEvent ).doubleValue();
				break;
			case TangoConst.Tango_DEV_BOOLEAN:
				currentEventD = isCurrentNull ? -1 : ( ( ( Boolean ) currentEvent ).booleanValue() ) ? 1 : 0;
				previousEventD = isPreviousNull ? -1 : ( ( ( Boolean ) previousEvent ).booleanValue() ) ? 1 : 0;
				break;
			case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_USHORT:
				currentEventD = isCurrentNull ? -1 : ( ( Short ) currentEvent ).doubleValue();
				previousEventD = isPreviousNull ? -1 : ( ( Short ) previousEvent ).doubleValue();
				break;
			case TangoConst.Tango_DEV_FLOAT:
				currentEventD = isCurrentNull ? -1 : ( ( Float ) currentEvent ).doubleValue();
				previousEventD = isPreviousNull ? -1 : ( ( Float ) previousEvent ).doubleValue();
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				currentEventD = isCurrentNull ? -1 : ( ( Double ) currentEvent ).doubleValue();
				previousEventD = isPreviousNull ? -1 : ( ( Double ) previousEvent ).doubleValue();
				break;
			default:
                try
                {
                    currentEventD = isCurrentNull ? -1 : ( ( Double ) currentEvent ).doubleValue();
                }
                catch ( Exception e )
                {
                    throw new IllegalArgumentException ( m_myName + "/isDataArchivable/failed to convert currentEvent/"+currentEvent.getClass().toString()+"/data_type/"+data_type );
                }
                try
                {
                    previousEventD = isPreviousNull ? -1 : ( ( Double ) previousEvent ).doubleValue();
                }
                catch ( Exception e )
                {
                    throw new IllegalArgumentException ( m_myName + "/isDataArchivable/failed to convert previousEvent/"+previousEvent.getClass().toString()+"/data_type/"+data_type );
                }
				break;
		}
        
		boolean result = false;
		// All the modes must be tested at least to increment the various counters
		if ( isDataArchivableModeP(attModeCounter) )
		{
			result = true;
		}
		if ( isDataArchivableModeA(attModeCounter,isCurrentNull,isPreviousNull,currentEventD , previousEventD) )
		{
			result = true;
		}
		if ( isDataArchivableModeR(attModeCounter,isCurrentNull,isPreviousNull,currentEventD , previousEventD) )
		{
			result = true;
		}
		if ( isDataArchivableModeT(attModeCounter,isCurrentNull,currentEventD) )
		{
			result = true;
		}
		if ( isDataArchivableModeC(attModeCounter,isCurrentNull,isPreviousNull,currentEventD , previousEventD) )
		{
			result = true;
		}
		if ( isDataArchivableModeD(attModeCounter,isCurrentNull,isPreviousNull,currentEventD , previousEventD) )
		{
			result = true;
		}
		if ( isDataArchivableModeE(attModeCounter,isCurrentNull,isPreviousNull,currentEventD , previousEventD) )
		{
			result = true;
		}
		return result;
	}
	
	private boolean isDataArchivableModeP(ModesCounters attModeCounter)
	{
		if ( m_mode.getModeP() == null ) return false;

		if ( attModeCounter.getCountModeP() < m_factorModePeriodic )
		{
			//Util.out4.println(m_myName + ".isDataArchivableModeP");
			//Util.out4.println("countModePeriodic = " + attModeCounter.getCountModeP() + " < " + "m_factorModePeriodic = " + m_factorModePeriodic);
			attModeCounter.incremCountModeP();
			return false;
		}
		else
		{
			attModeCounter.initCountModeP();
			//Util.out4.println(m_myName + ".isDataArchivableModeP");
			//Util.out4.println("countModePeriodic = " + attModeCounter.getCountModeP() + " > " + "m_factorModePeriodic = " + m_factorModePeriodic);
			return true;
		}
	}
	
	private boolean isDataArchivableModeA(ModesCounters attModeCounter,boolean isCurrentNull, boolean isPreviousNull, double currentEvent , double previousEvent)
	{

		if ( m_mode.getModeA() == null ) return false;

		if ( attModeCounter.getCountModeA() < m_factorModeAbsolute )
		{
			attModeCounter.incremCountModeA();
			return false;
		}
		else
		{
			attModeCounter.initCountModeA();
			
            if ( isCurrentNull || isPreviousNull )
            {
                return ( isCurrentNull != isPreviousNull );
            }
 //          System.out.println("=/=/=/=/=/=/=/=/=/=//=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/  derive lente: " + m_mode.getModeA().isSlow_drift());
            if(m_mode.getModeA().isSlow_drift()){ 
            		if(m_mode.getModeA().getPrev_stored_val() == ModeAbsolu.SLOW_DRIFT_FIRST_VAL)
            			m_mode.getModeA().setPrev_stored_val(previousEvent);
            		else 	previousEvent = m_mode.getModeA().getPrev_stored_val();
            }
            double lower_limit = ( ( double ) previousEvent ) - Math.abs ( m_mode.getModeA().getValInf() );
			double upper_limit = ( ( double ) previousEvent ) + Math.abs ( m_mode.getModeA().getValSup() );

			
            if ( ( currentEvent < lower_limit ) || ( currentEvent > upper_limit ) )
			{
				Util.out4.println(m_myName + ".isDataArchivableModeA \r\n\t" +
				                  "\r\n\t" + "DeltaInf() : " + Math.abs ( m_mode.getModeA().getValInf() ) +
				                  "\r\n\t" + "DeltaSup() : " + Math.abs ( m_mode.getModeA().getValSup() ) +
				                  "\r\n\t" + "current value : " + currentEvent +
				                  "\r\n\t" + "previous value : " + previousEvent +
				                  "\r\n\t" + "lower_limit : " + lower_limit +
				                  "\r\n\t" + "upper_limit  : " + upper_limit +
				                  "\r\n\t" + "status   :   OK");

                //System.out.println("TRUE");
				
//				System.out.println("=/=/=/=/=/=/=/=/=/=//=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/Absolute mode,  Stored value:   "+currentEvent +"<> prev value: "+ previousEvent);
				if(m_mode.getModeA().isSlow_drift()) 
					m_mode.getModeA().setPrev_stored_val(currentEvent);
				return true;
			}
			else
			{
                //System.out.println("FALSE");
				//System.out.println("=/=/=/=/=/=/=/=/=/=//=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/Absolute mode :   " + currentEvent + " not stored <> prev value: "+ previousEvent);
                return false;
			}
		}
	}
	
	private boolean isDataArchivableModeR(ModesCounters attModeCounter,boolean isCurrentNull, boolean isPreviousNull, double currentEvent , double previousEvent)
	{
		
		if ( m_mode.getModeR() == null ) return false;

		if ( attModeCounter.getCountModeR() < m_factorModeRelative )
		{
			//Util.out4.println(m_myName + ".isDataArchivableModeR");
			//Util.out4.println("countModeRelative = " + attModeCounter.getCountModeR() + " < " + "m_factorModeRelative = " + m_factorModeRelative);
			attModeCounter.incremCountModeR();
			return false;
		}
		else
		{
			attModeCounter.initCountModeR();
			
            if ( isCurrentNull || isPreviousNull )
            {
                return ( isCurrentNull != isPreviousNull );
            }
            
            if((m_mode.getModeR().isSlow_drift()) && (m_mode.getModeR().getPrev_stored_val() != ModeRelatif.SLOW_DRIFT_FIRST_VAL))
            	previousEvent = m_mode.getModeR().getPrev_stored_val();
			double lower_limit = 0;
			double upper_limit = 0;
			
            double percentInf = Math.abs ( m_mode.getModeR().getPercentInf() / 100.0 );
            double percentSup = Math.abs ( m_mode.getModeR().getPercentInf() / 100.0 );
            
            double deltaInf = percentInf * Math.abs ( previousEvent );
            double deltaSup = percentSup * Math.abs ( previousEvent );
            
            lower_limit = previousEvent - deltaInf;
            upper_limit = previousEvent + deltaSup;
                
			if ( ( currentEvent < lower_limit ) || ( currentEvent > upper_limit ) ){
				Util.out4.println(m_myName + ".isDataArchivableModeR \r\n\t" +
				                  "\r\n\t" + "PercentInf() : " + Math.abs ( m_mode.getModeR().getPercentInf() ) +
				                  "\r\n\t" + "PercentSup() : " + Math.abs ( m_mode.getModeR().getPercentSup() ) +
				                  "\r\n\t" + "current value : " + currentEvent +
				                  "\r\n\t" + "previous value : " + previousEvent +
				                  "\r\n\t" + "lower_limit : " + lower_limit +
				                  "\r\n\t" + "upper_limit  : " + upper_limit +
				                  "\r\n\t" + "status   :   OK");
				
				if(m_mode.getModeR().isSlow_drift()) 
					m_mode.getModeR().setPrev_stored_val(currentEvent);

                //System.out.println("TRUE");
                return true;
			}
			else
			{
				//System.out.println("FALSE");
                return false;
			}
		}
	}
	
	private boolean isDataArchivableModeT(ModesCounters attModeCounter,boolean isCurrentNull, double currentEvent)
	{
		if ( m_mode.getModeT() == null ) return false;

		if ( attModeCounter.getCountModeT() < m_factorModeThreshold )
		{
			attModeCounter.incremCountModeT();
			return false;
		}
		else
		{
			attModeCounter.initCountModeT();
			
            if ( isCurrentNull )
            {
                return false;
            }

            /*System.out.println (m_myName + ".isDataArchivableModeT \r\n\t" +
                      "\r\n\t" + "current value :" + currentEvent +
                      "\r\n\t" + "previous value :" + previousEvent);*/

            if ( ( currentEvent < ( double ) m_mode.getModeT().getThresholdInf() ) || ( currentEvent > ( double ) m_mode.getModeT().getThresholdSup() ) )
			{
				Util.out4.println(m_myName + ".isDataArchivableModeT \r\n\t" +
				                  "\r\n\t" + "current value :" + currentEvent);

                //System.out.println ("TRUE");
				return true;
			}
			else
			{
                //System.out.println ("FALSE");
                return false;
			}
		}
	}

	private boolean isDataArchivableModeD(ModesCounters attModeCounter,boolean isCurrentNull, boolean isPreviousNull, double currentEvent , double previousEvent)
	{
		if ( m_mode.getModeD() == null ) return false;

		if ( attModeCounter.getCountModeD() < m_factorModeDifference )
		{
			attModeCounter.incremCountModeD();
			return false;
		}
		else
		{
			attModeCounter.initCountModeD();
			
            if ( isCurrentNull || isPreviousNull )
            {
                return ( isCurrentNull != isPreviousNull );
            }        

            /*if ( Math.random() > 0.5 )
            {
                currentEvent = previousEvent;
            }
            System.out.println(m_myName + ".isDataArchivableModeD \r\n\t" +
                      "\r\n\t" + "current value : " + currentEvent +
                      "\r\n\t" + "previous value : " + previousEvent);*/


			if ( currentEvent != previousEvent )
			{
				Util.out4.println(m_myName + ".isDataArchivableModeD \r\n\t" +
				                  "\r\n\t" + "current value : " + currentEvent +
				                  "\r\n\t" + "previous value : " + previousEvent);
                //System.out.println("TRUE");
                return true;
			}
			else
			{
                //System.out.println("FALSE");
                return false;
			}
		}
	}
	
	// todo faire la suite ...
	private boolean isDataArchivableModeC(ModesCounters attModeCounter,boolean isCurrentNull, boolean isPreviousNull, double currentEvent , double previousEvent)
	{
		return false;
	}

	private boolean isDataArchivableModeE(ModesCounters attModeCounter,boolean isCurrentNull, boolean isPreviousNull, double currentEvent , double previousEvent)
	{
		return false;
	}
	
    private void initFactor()
    {
        if ( this.m_hasRounding  )
        {
            this.initFactorWithRounding ();
        }
        else
        {
            this.initFactorWithoutRounding ();
        }
    }
    
	private void initFactorWithRounding()
	{
		m_factorModePeriodic = (int) Math.round ( ( m_mode.getModeP().getPeriod() * 1.0 ) / ( m_refresherInterval * 1.0) );

		if ( m_mode.getModeA() != null )
		{
			m_factorModeAbsolute = (int) Math.round ( ( m_mode.getModeA().getPeriod() * 1.0 ) / ( m_refresherInterval * 1.0) );    
		}

		if ( m_mode.getModeR() != null )
		{
			m_factorModeRelative = (int) Math.round ( ( m_mode.getModeR().getPeriod() * 1.0 ) / ( m_refresherInterval * 1.0) );    
		}

		if ( m_mode.getModeT() != null )
		{
			m_factorModeThreshold = (int) Math.round ( ( m_mode.getModeT().getPeriod() * 1.0 ) / ( m_refresherInterval * 1.0) );    
		}

		if ( m_mode.getModeD() != null )
		{
			m_factorModeDifference = (int) Math.round ( ( m_mode.getModeD().getPeriod() * 1.0 ) / ( m_refresherInterval * 1.0) );    
		}

		if ( m_mode.getModeC() != null )
		{
			m_factorModeCalcul = (int) Math.round ( ( m_mode.getModeC().getPeriod() * 1.0 ) / ( m_refresherInterval * 1.0) );    
		}
	}
	
	// Not really used
	private void initFactorWithoutRounding()
	{
		if ( m_mode.getModeP().getPeriod() % m_refresherInterval == 0 )
		{
			m_factorModePeriodic = m_mode.getModeP().getPeriod() / m_refresherInterval;
		}
		else
		{
            // Todo send an exception
		}
		if ( ( m_mode.getModeA() != null ) && ( m_mode.getModeA().getPeriod() % m_refresherInterval == 0 ) )
		{
			m_factorModeAbsolute = m_mode.getModeA().getPeriod() / m_refresherInterval;
		}
		else
		{
			// Todo send an exception
		}
		if ( ( m_mode.getModeR() != null ) && ( m_mode.getModeR().getPeriod() % m_refresherInterval == 0 ) )
		{
			m_factorModeRelative = m_mode.getModeR().getPeriod() / m_refresherInterval;
		}
		else
		{
			// Todo send an exception
		}
		if ( ( m_mode.getModeT() != null ) && ( m_mode.getModeT().getPeriod() % m_refresherInterval == 0 ) )
		{
			m_factorModeThreshold = m_mode.getModeT().getPeriod() / m_refresherInterval;
		}
		else
		{
			// Todo send an exception
		}
		if ( ( m_mode.getModeC() != null ) && ( m_mode.getModeC().getPeriod() % m_refresherInterval == 0 ) )
		{
			m_factorModeCalcul = m_mode.getModeC().getPeriod() / m_refresherInterval;
		}
		else
		{
			// Todo send an exception
		}
		if ( ( m_mode.getModeD() != null ) && ( m_mode.getModeD().getPeriod() % m_refresherInterval == 0 ) )
		{
			m_factorModeDifference = m_mode.getModeD().getPeriod() / m_refresherInterval;
		}
		else
		{
			// Todo send an exception
		}
	}
	
	private void setMinRefreshInterval()
	{
		// the Periodical Mode is supposed to never be null !!
		m_refresherInterval = m_mode.getModeP().getPeriod();
		
		if ( ( m_mode.getModeA() != null ) && ( m_refresherInterval > m_mode.getModeA().getPeriod() ) )
		{
			m_refresherInterval = m_mode.getModeA().getPeriod();
		}
		if ( ( m_mode.getModeR() != null ) && ( m_refresherInterval > m_mode.getModeR().getPeriod() ) )
		{
			m_refresherInterval = m_mode.getModeR().getPeriod();
		}
		if ( ( m_mode.getModeT() != null ) && ( m_refresherInterval > m_mode.getModeT().getPeriod() ) )
		{
			m_refresherInterval = m_mode.getModeT().getPeriod();
		}
		/*if ( ( m_mode.getModeC() != null ) && ( min > m_mode.getModeC().getPeriod() ) )
		{
			m_refresherInterval = m_mode.getModeC().getPeriod();
		}*/
		if ( ( m_mode.getModeD() != null ) && ( m_refresherInterval > m_mode.getModeD().getPeriod() ) )
		{
			m_refresherInterval = m_mode.getModeD().getPeriod();
		}
	}

}
