package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DateHeure;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.TangoAccess;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.ArchivingWatch;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ModeData;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

/**
 * An implementation that really loads data from the physical database
 * @author CLAISSE 
 */
public abstract class DBReaderAdapter implements IDBReader 
{
	protected DataBaseManager database;
	protected IArchivingManagerApiRef manager;
	protected boolean isReady = false;

	protected int cpt = 0;
	private String last_partition = null;
	public DBReaderAdapter ()
	{
		int type = isHistoric()?ConfigConst.HDB:ConfigConst.TDB;
		this.manager =  ArchivingManagerApiRefFactory.getInstance(type);
	}

	/* (non-Javadoc)
	 * @see archwatch.datasources.in.IArchivedAttributesReader#getArchivedAttributes()
	 */
	public Hashtable getArchivedAttributes() throws DevFailed 
	{
		//throwDummyException(3);
		this.openConnectionIfNecessary ( false );

		String [] currentArchivedAtt = null;
		try
		{
			currentArchivedAtt = database.getMode().getCurrentArchivedAtt ();
		}
		catch ( Throwable e )
		{
			Tools.trace ( "DBReaderAdapter/getArchivedAttributes/problem calling getCurrentArchivedAtt(): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}
		if ( currentArchivedAtt == null )
		{
			return null;    
		}

		int numberOfAttributes = currentArchivedAtt.length;        
		Hashtable ret = new Hashtable ( numberOfAttributes );
		for ( int i = 0 ; i < numberOfAttributes ; i ++ )
		{
			String completeName = currentArchivedAtt [ i ];
			ret.put ( completeName , new ModeData () ); //The ModeData will be loaded when necessary ie. at the beginning of each step
		}

		return ret;
	}

	/**
	 * @param completeName
	 * @return
	 * @throws ArchivingException
	 */
	public ModeData getModeDataForAttribute ( String completeName ) throws DevFailed  
	{
		this.openConnectionIfNecessary ( false );

		ModeData ret = new ModeData ();

		try
		{
			String archiver = this.getArchiverForAttribute ( completeName );
			ret.setArchiver ( archiver );
		}
		catch ( Throwable e )
		{
			Tools.trace ( "DBReaderAdapter/getModeDataForAttribute/problem calling getArchiverForAttribute ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		try
		{
			Mode mode = this.getModeForAttribute ( completeName );
			ret.setMode ( mode );

			if ( mode.getModeP () == null )
			{
				Tools.trace ( "DBReaderAdapter/getModeDataForAttribute/mode.getModeP() == null for attribute|"+ completeName +"|" , Warnable.LOG_LEVEL_ERROR );
				ret.setIncomplete ( true );
			}
		}
		catch ( Throwable e )
		{
			Tools.trace ( "DBReaderAdapter/getModeDataForAttribute/problem calling getModeForAttribute ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		return ret;
	}
	/*
	 * //CLA 22/03/07 The DevFailed should be thrown
	 * public ModeData getModeDataForAttribute ( String completeName ) throws DevFailed 
    {
        this.openConnectionIfNecessary ( false );

        ModeData ret = new ModeData ();

        try
        {
            String archiver = this.getArchiverForAttribute ( completeName );
            ret.setArchiver ( archiver );
        }
        catch ( Throwable e )
        {
            Tools.trace ( "DBReaderAdapter/getModeDataForAttribute/problem calling getArchiverForAttribute ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
            ret.setIncomplete ( true );
        }

        try
        {
            Mode mode = this.getModeForAttribute ( completeName );
            ret.setMode ( mode );

            if ( mode.getModeP () == null )
            {
                Tools.trace ( "DBReaderAdapter/getModeDataForAttribute/mode.getModeP() == null for attribute|"+ completeName +"|" , Warnable.LOG_LEVEL_ERROR );
                ret.setIncomplete ( true );
            }
        }
        catch ( Throwable e )
        {
            Tools.trace ( "DBReaderAdapter/getModeDataForAttribute/problem calling getModeForAttribute ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
            //Tools.throwDevFailed ( e );
            ret.setIncomplete ( true );
        }

        return ret;
    }*/

	/**
	 * @param completeName
	 * @return
	 * @throws ArchivingException
	 */
	private String getArchiverForAttribute(String completeName) throws DevFailed 
	{
		//System.out.println ( "getArchiverForAttribute/completeName|"+completeName+"|" );
		//throwDummyException(completeName);

		this.openConnectionIfNecessary ( false );

		String device = null;

		try
		{
			device = database.getMode().getDeviceInCharge ( completeName );
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getArchiverForAttribute/problem calling getDeviceInCharge ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		return device;
	}

	/**
	 * @param completeName
	 * @return
	 * @throws ArchivingException
	 */
	public Mode getModeForAttribute(String completeName) throws DevFailed 
	{
		//throwDummyException(completeName);

		this.openConnectionIfNecessary ( false );

		Mode mode = null;
		try
		{
			mode = database.getMode().getCurrentArchivingMode ( completeName );
		}
		catch ( Throwable e )
		{
			Tools.trace ( "DBReaderAdapter/getModeForAttribute/problem calling getCurrentArchivingMode ( "+ completeName +" , true ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}
		return mode;
	}

	public void closeConnection () throws DevFailed
	{
		try
		{
			if(this.manager!=null)
				this.manager.closeDatabase ( );
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/closeConnection/problem calling close ()" , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		this.isReady = false;
	}



	/* (non-Javadoc)
	 * @see archwatch.datasource.in.IArchivedAttributesReader#getRecordCount(java.lang.String)
	 */
	public int getRecordCount(String completeName) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );

		int res = 0;
		try
		{
			res = database.getAttribute().getAttRecordCount ( completeName );
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getRecordCount/problem calling getAttRecordCount ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}
		return res;
	}

	public Timestamp getTimeOfLastInsert(String completeName) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );

		Timestamp ret = null;
		try
		{
			ret = database.getDbUtil().getTimeOfLastInsert ( completeName , true);
		}
		catch ( ArchivingException ae )
		{
			if ( ae.getCause () != null && ae.getCause () instanceof SQLException )
			{
				SQLException sqlex = (SQLException) ae.getCause ();
				String state = sqlex.getSQLState ();
				int code = sqlex.getErrorCode ();
				Tools.trace ( "DBReaderAdapter/getTimeOfLastInsert/the table doesn't exist for attribute|"+ completeName +"|SQL state|"+state+"|SQL code|"+code+"|" , ae , Warnable.LOG_LEVEL_INFO );

				//the table doesn't exist
				return null;
			}
			else
			{
				Tools.trace ( "DBReaderAdapter/getTimeOfLastInsert/problem calling getTimeOfLastInsert ( "+ completeName +" ): " , ae , Warnable.LOG_LEVEL_ERROR );    
				Tools.throwDevFailed ( ae );
			}
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getTimeOfLastInsert/problem calling getTimeOfLastInsert ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		return ret;
	}
	/**
	 * 
	 */
	public int getAttributeId(String completeName)throws DevFailed {
		this.openConnectionIfNecessary ( false );
		int ret = -1;
		try
		{
			ret = database.getAttribute().getIds().getBufferedAttID(completeName  );
		}
		catch ( ArchivingException ae )
		{
			if ( ae.getCause () != null && ae.getCause () instanceof SQLException )
			{
				SQLException sqlex = (SQLException) ae.getCause ();
				String state = sqlex.getSQLState ();
				int code = sqlex.getErrorCode ();
				Tools.trace ( "DBReaderAdapter/getAttributeId/the table doesn't exist for attribute|"+ completeName +"|SQL state|"+state+"|SQL code|"+code+"|" , ae , Warnable.LOG_LEVEL_INFO );

				//the table doesn't exist
				return -1;
			}
			else
			{
				Tools.trace ( "DBReaderAdapter/getAttributeId/problem calling getTimeOfLastInsert ( "+ completeName +" ): " , ae , Warnable.LOG_LEVEL_ERROR );    
				Tools.throwDevFailed ( ae );
			}
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getAttributeId/problem calling getTimeOfLastInsert ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		return ret;


	}

	public void startArchivingReport()throws DevFailed {
//		String[] adminReport = null;
		try {
			this.openConnectionIfNecessary ( false );
			this.database.getDbUtil().startWatcherReport();
//			int nbArchived = this.database.getMode().getCurrentArchivedAttCount();
//			int nbOkAttributes = this.database.getDbUtil().getAttributesCountOkOrKo( true);
//			int nbKoAttributes = this.database.getDbUtil().getAttributesCountOkOrKo( false);
//			String[] listKoAttributes = this.database.getDbUtil().getKoAttributes();
//			
//			int koAttrSize = listKoAttributes==null ?0:listKoAttributes.length;
//			int result_size = 3 + koAttrSize ;
//			adminReport = new String[result_size];
//			adminReport[0] = "Archived attributes number = " + nbArchived;
//			adminReport[1] = "OK attributes number = " + nbOkAttributes;
//			adminReport[2] = "KO attributes number = " + nbKoAttributes;
//			int pos = 3;
//			if(listKoAttributes!=null){
//				for (int i = 0; i < listKoAttributes.length; i++,pos++) {
//					adminReport[pos] = listKoAttributes[i];
//				}
//			}

		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			return adminReport;
		}
		
//		return adminReport;
		
	}
	
	/**
	 * 
	 * @return
	 * @throws DevFailed
	 */
	public String[] getDatabaseReport()throws DevFailed {
		String[] adminReport = null;
		try {
			this.openConnectionIfNecessary ( false );
			this.database.getDbUtil().startWatcherReport();
			String[] listOfPartitions = this.database.getDbUtil().getListOfPartitions();
			String[] listOfJobsStatus = this.database.getDbUtil().getListOfJobStatus();
			String[] listOfJobsErrors = this.database.getDbUtil().getListOfJobErrors();
			
			int PartitionsSize = listOfPartitions==null?0:listOfPartitions.length;
			int jobStatusSize = listOfJobsStatus==null?0:listOfJobsStatus.length;
			int jobErrorsSize = listOfJobsErrors==null?0:listOfJobsErrors.length;
			int result_size = PartitionsSize + jobErrorsSize + jobStatusSize;
			adminReport = new String[result_size];
			int pos = 0;

			if(listOfPartitions!=null){
				for (int i = 0; i < listOfPartitions.length; i++,pos++) {
					adminReport[pos] = listOfPartitions[i];
				}
			}
			if(listOfJobsStatus!=null){
				for (int i = 0; i < listOfJobsStatus.length; i++,pos++) {
					adminReport[pos] = listOfJobsStatus[i];
				}
			}
			if(listOfJobsErrors!=null){
				for (int i = 0; i < listOfJobsErrors.length; i++,pos++) {
					adminReport[pos] = listOfJobsErrors[i];
				}
			}

		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return adminReport;
		}
		
		return adminReport;
		
	}
	/**
	 * 
	 */
	public String[] getTimeValueNullOrNotOfLastInsert(String completeName, int dataFormat, int writable) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );

		String ret [] = null;
		try
		{
			if(isHistoric()){
				int last_month = getLastPartition() == null? -1 : Integer.parseInt(getLastPartition().substring(getLastPartition().indexOf("M")+1));
				int actual_month = new Timestamp(System.currentTimeMillis()).getMonth()+1;
				String partition = last_month == actual_month?getLastPartition():null;
				ret = database.getDbUtil().getTimeValueNullOrNotOfLastInsert ( completeName, dataFormat, writable, partition );
				if(ret != null && ret[2]!=null){
					setLastPartition(ret[2]);
				}
			}
			else {
				ret = database.getDbUtil().getTimeValueNullOrNotOfLastInsert ( completeName, dataFormat, writable, null );
			}

		}
		catch ( ArchivingException ae )
		{
			if ( ae.getCause () != null && ae.getCause () instanceof SQLException )
			{
				SQLException sqlex = (SQLException) ae.getCause ();
				String state = sqlex.getSQLState ();
				int code = sqlex.getErrorCode ();
				Tools.trace ( "DBReaderAdapter/getTimeValueNullOrNotOfLastInsert/the table doesn't exist for attribute|"+ completeName +"|SQL state|"+state+"|SQL code|"+code+"|" , ae , Warnable.LOG_LEVEL_INFO );

				//the table doesn't exist
				return null;
			}
			else
			{
				Tools.trace ( "DBReaderAdapter/getTimeValueNullOrNotOfLastInsert/problem calling getTimeValueNullOrNotOfLastInsert ( "+ completeName +" ): " , ae , Warnable.LOG_LEVEL_ERROR );    
				Tools.throwDevFailed ( ae );
			}
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getTimeValueNullOrNotOfLastInsert/problem calling getTimeValueNullOrNotOfLastInsert ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		return ret;
	}
	/* (non-Javadoc)
	 * @see archwatch.datasource.in.IArchivedAttributesReader#now()
	 */
	public Timestamp now () throws DevFailed 
	{
		return new Timestamp ( System.currentTimeMillis () );
	}
	/*public Timestamp now () throws DevFailed 
    {
        //throwDummyException();

        this.openConnectionIfNecessary ( false );

        try
        {
            return database.now ();
        }
        catch ( Exception e )
        {
            Tools.trace ( "DBReaderAdapter/now/problem calling now ()" , e , Warnable.LOG_LEVEL_ERROR );
            Tools.throwDevFailed ( e );
        }
        return null;
    }*/

	/* (non-Javadoc)
	 * @see archwatch.datasource.in.IArchivedAttributesReader#getRecordCount(java.lang.String, int)
	 */
	private void throwDummyException ( int threshold ) throws DevFailed 
	{
		System.out.println ( "CLA/throwDummyException/threshold/"+threshold+ "/cpt/"+cpt);
		cpt++;
		if ( cpt>=threshold)
		{
			Exception e = new Exception ( "CLA/dummy exception throwDummyException-----------------------------------------------------------------------------" );
			Tools.throwDevFailed ( e );    
		}
	}

	private void alwaysThrowDummyException () throws DevFailed 
	{
		Exception e = new Exception ( "CLA/dummy exception alwaysThrowDummyException-----------------------------------------------------------------------------" );
		Tools.throwDevFailed ( e );    
	}

	public int getRecordCount(String completeName, int period) throws DevFailed 
	{
		//throwDummyException ();
		//throwDummyException ( completeName );

		//Tools.trace ( "DBReaderAdapter/completeName|" + completeName + "|period|" + period , Warnable.LOG_LEVEL_DEBUG );

		this.openConnectionIfNecessary ( false );

		Timestamp now = this.now ();
		DateHeure after = new DateHeure ( now );
		DateHeure before = (DateHeure) after.clone();
		before.addMilliseconde ( -period );

		String after_s = now.toString ();
		String before_s = new Timestamp ( before.toDate().getTime() ).toString();
		//Tools.trace ( "DBReaderAdapter/now|" + after_s + "|now-period|" + before_s , Warnable.LOG_LEVEL_DEBUG );

		String [] argin = new String [ 3 ];
		argin [ 0 ] = completeName;
		argin [ 1 ] = before_s;
		argin [ 2 ] = after_s;

		try
		{
			DbData dbData = database.getExtractor().getDataGettersBetweenDates().getAttDataBetweenDates ( argin , SamplingType.getSamplingType ( SamplingType.ALL ) );
			if ( dbData == null || dbData.getData_timed() == null )
			{
				Tools.trace ( "DBReaderAdapter/case ( dbData == null || dbData.getData() == null )" , Warnable.LOG_LEVEL_DEBUG );
				return 0;
			}

			return dbData.getData_timed().length;
		}
		catch ( ArchivingException ae )
		{
			if ( ae.getCause () != null && ae.getCause () instanceof SQLException )
			{
				SQLException sqlex = (SQLException) ae.getCause ();
				String state = sqlex.getSQLState ();
				int code = sqlex.getErrorCode ();
				//Tools.trace ( "DBReaderAdapter/getRecordCount/the table doesn't exist for attribute|"+ completeName +"|SQL state|"+state+"|SQL code|"+code+"|" , ae , Warnable.LOG_LEVEL_INFO );

				//the table doesn't exist             
				return 0;
			}
			else
			{
				Tools.trace ( "DBReaderAdapter/getRecordCount/problem calling getAttDataBetweenDates ( " + completeName + " , " + before_s + " , " + after_s + " )" , ae , Warnable.LOG_LEVEL_ERROR );
				Tools.throwDevFailed ( ae );
			}
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getRecordCount/problem calling getAttDataBetweenDates ( " + completeName + " , " + before_s + " , " + after_s + " )" , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}
		return 0;
	}

	private void throwDummyException(String completeName) throws DevFailed 
	{
		if ( completeName.equals ( "tango/tangotest/1/short_scalar" ) )
		{
			Exception e = new Exception ( "CLA/dummy exception for short_scalar-----------------------------------------------------------------------------" );
			Tools.throwDevFailed ( e );    
		}    
	}

	/* (non-Javadoc)
	 * @see archwatch.datasource.in.IArchivedAttributesReader#isScalar(java.lang.String)
	 */
	public boolean isScalar(String completeName) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );

		try
		{
			int format = database.getAttribute().getAttDataFormat ( completeName );
			boolean ret = (format == AttrDataFormat._SCALAR);

			return ret;
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/isScalar/problem calling getAttDataFormat ( " + completeName + " )" , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		return false;
	}

	public int getAttFormatCriterion( String completeName ) throws DevFailed 
	{
		//throwDummyException(completeName);

		this.openConnectionIfNecessary ( false );

		try
		{
			return database.getAttribute().getAttDataFormat(completeName);
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getAttFormatCriterion/problem calling getAttDataFormat ( " + completeName + " )" , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}
		return -1;
	}

	public int getAttWritableCriterion( String completeName ) throws DevFailed 
	{
		//throwDummyException(completeName);

		this.openConnectionIfNecessary ( false );

		try
		{
			return database.getAttribute().getAttDataWritable(completeName);
		}
		catch ( Exception e )
		{
			Tools.trace ( "DBReaderAdapter/getAttWritableCriterion/problem calling getAttDataWritable ( " + completeName + " )" , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see archwatch.datasources.db.IArchivedAttributesReader#getArchiverLoad(java.lang.String, int)
	 */
	public int getArchiverLoad(String archiverName, int loadType) throws DevFailed
	{        
		this.openConnectionIfNecessary ( false );

		switch ( loadType )
		{
		case IDBReader.LOAD_ALL:
			try
			{
				return ArchivingManagerApiRef.get_charge ( archiverName );
			}
			catch ( Exception e )
			{
				Tools.trace ( "DBReaderAdapter/getArchiverLoad/problem calling get_charge ( " + archiverName + " )" , e , Warnable.LOG_LEVEL_ERROR );
				Tools.throwDevFailed ( e );
			}
		case IDBReader.LOAD_SCALAR:
			try
			{
				return ArchivingManagerApiRef.get_scalar_charge ( archiverName );
			}
			catch ( Exception e )
			{
				Tools.trace ( "DBReaderAdapter/getArchiverLoad/problem calling get_scalar_charge ( " + archiverName + " )" , e , Warnable.LOG_LEVEL_ERROR );
				Tools.throwDevFailed ( e );
			}
		case IDBReader.LOAD_SPECTRUM:
			try
			{
				return ArchivingManagerApiRef.get_spectrum_charge ( archiverName );
			}
			catch ( Exception e )
			{
				Tools.trace ( "DBReaderAdapter/getArchiverLoad/problem calling get_spectrum_charge ( " + archiverName + " )" , e , Warnable.LOG_LEVEL_ERROR );
				Tools.throwDevFailed ( e );
			}
		case IDBReader.LOAD_IMAGE:
			try
			{
				return ArchivingManagerApiRef.get_image_charge ( archiverName );
			}
			catch ( Exception e )
			{
				Tools.trace ( "DBReaderAdapter/getArchiverLoad/problem calling get_image_charge ( " + archiverName + " )" , e , Warnable.LOG_LEVEL_ERROR );
				Tools.throwDevFailed ( e );
			}
		default:
			Tools.throwDevFailed ( new IllegalArgumentException ( "Expected LOAD_ALL(0), LOAD_SCALAR(1), LOAD_SPECTRUM(2), LOAD_IMAGE(3) got " + loadType + " instead." ) );
		}

		return -1;
	}

	/* (non-Javadoc)
	 * @see archwatch.datasources.db.IArchivedAttributesReader#isAlive(java.lang.String)
	 */
	public boolean isAlive(String archiverName) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );

		try
		{
			return TangoAccess.deviceLivingTest ( archiverName );
		}
		catch ( Exception ae )
		{
			Tools.trace ( "DBReaderAdapter/isAlive/problem calling deviceLivingTest ( " + archiverName + " )" , ae , Warnable.LOG_LEVEL_INFO );
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see archwatch.datasources.db.IArchivedAttributesReader#getStatus(java.lang.String)
	 */
	public String getDeviceStatus(String attributeName) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );

		try
		{
			return TangoAccess.getDeviceStatus ( attributeName );
		}
		catch ( ArchivingException ae )
		{
			//ae.printStackTrace ();
			Tools.trace ( "DBReaderAdapter/getDeviceStatus/problem calling getDeviceStatus ( " + attributeName + " )" , ae , Warnable.LOG_LEVEL_INFO );
			return "DOES NOT ANSWER";
		}
	}

	public boolean isDBConnectionAlive () 
	{
		try
		{
			this.openConnectionIfNecessary ( false );
			ConnectionCommands.minimumRequest(database.getDbConn());
		}
		catch ( Throwable e )
		{
			Tools.trace ( "DBReaderAdapter/isDBConnectionAlive/The minimum request failed!!! Trying to reopen the DB connection." , e , Warnable.LOG_LEVEL_ERROR );

			try
			{
				this.openConnectionIfNecessary ( true );
				ConnectionCommands.minimumRequest(database.getDbConn());
			}
			catch ( Throwable e1 )
			{
				Tools.trace ( "DBReaderAdapter/isDBConnectionAlive/The DB connection can't be reopened!!!" , e1 , Warnable.LOG_LEVEL_FATAL );
				return false;    
			}    
		}

		return true;
		//return false;
	}

	public boolean isLastValueNull(String completeName) throws DevFailed 
	{
		this.openConnectionIfNecessary ( false );
		//throwDummyException (4);
		//System.out.println ( "DBReaderAdapter/isLastValueNull/completeName|"+completeName+"|" );
		try
		{
			//System.out.println ( "DBReaderAdapter/isLastValueNull/before database.isLastDataNull" );
			boolean ret = database.getDbUtil().isLastDataNull ( completeName );
			//System.out.println ( "DBReaderAdapter/isLastValueNull/after database.isLastDataNull/ret|"+ret+"|" );
			return ret;
		}
		catch ( ArchivingException ae )
		{
			//System.out.println ( "DBReaderAdapter/isLastValueNull/ArchivingException" );

			if ( ae.getCause () != null && ae.getCause () instanceof SQLException )
			{
				SQLException sqlex = (SQLException) ae.getCause ();
				String state = sqlex.getSQLState ();
				int code = sqlex.getErrorCode ();
				Tools.trace ( "DBReaderAdapter/isLastValueNull/the table doesn't exist for attribute|"+ completeName +"|SQL state|"+state+"|SQL code|"+code+"|" , ae , Warnable.LOG_LEVEL_INFO );

				//the table doesn't exist
				return true;
			}
			else
			{
				Tools.trace ( "DBReaderAdapter/isLastValueNull/problem calling getAttDataLast_n ( "+ completeName +" ): " , ae , Warnable.LOG_LEVEL_ERROR );    
				Tools.throwDevFailed ( ae );
			}
		}
		catch ( Exception e )
		{
			//System.out.println ( "DBReaderAdapter/isLastValueNull/Exception" );

			Tools.trace ( "DBReaderAdapter/isLastValueNull/problem calling getAttDataLast_n ( "+ completeName +" ): " , e , Warnable.LOG_LEVEL_ERROR );
			Tools.throwDevFailed ( e );
		}

		//System.out.println ( "DBReaderAdapter/isLastValueNull/Shouldn't be here!!!!!!!" );
		return true;//this line is never reached since throwDevFailed ALWAYS throws even if the compilator doesn't know that
	}

	public void openConnectionIfNecessary ( boolean forceOpening ) throws DevFailed
	{
		//alwaysThrowDummyException ();
		if(database == null) forceOpening = true;
		if ( ( !this.isReady ) || forceOpening )
		{
			String HDbHost = ArchivingWatch.getHDBHost();
			String HDbName = ArchivingWatch.getHDBName();
			String TDbHost = ArchivingWatch.getTDBHost();
			String TDbName = ArchivingWatch.getTDBName();

			String HDBuser = ArchivingWatch.getHDBuser();
			String HDBpassword = ArchivingWatch.getHDBpassword();
			String TDBuser = ArchivingWatch.getTDBuser();
			String TDBpassword = ArchivingWatch.getTDBpassword();;

			String host = this.isHistoric () ? HDbHost : TDbHost;
			String name = this.isHistoric () ? HDbName : TDbName;

			String user = this.isHistoric () ? HDBuser : TDBuser;
			String pwd = this.isHistoric () ? HDBpassword : TDBpassword;

			boolean rac = this.isHistoric()?ArchivingWatch.isHDBRac(): ArchivingWatch.isTDBRac();
			Tools.trace ( "DBReaderAdapter/openConnection/trying to connect with parameters ( "+ HDBuser + " , " + HDBpassword + " , "  + TDBuser + " , "  + TDBpassword  +" ): " , Warnable.LOG_LEVEL_DEBUG );

			this.closeConnection ();

			try
			{
				if(host.equals("") && name.equals("")){
					this.manager.ArchivingConfigure( user , pwd  );
				} else {
					this.manager.ArchivingConfigure( host, name, user , pwd, rac  );
				}
			}
			catch ( Exception e )
			{
				Tools.trace ( "DBReaderAdapter/openConnection/problem calling ArchivingManagerApi.ArchivingConfigure ( "+ HDBuser + " , " + HDBpassword + " , "  + TDBuser + " , "  + TDBpassword  +" ): " , e , Warnable.LOG_LEVEL_FATAL );
				Tools.throwDevFailed ( e );
			}

			database = this.manager.getDataBase(  );
			try {
				database.getDbConn().setAutoConnect ( false );
			} catch (ArchivingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.isReady = true;
		}
	}

	protected abstract boolean isHistoric ();
	/////////////////////////////////////last_partition setter/getter//////////////////////////////////
	public String getLastPartition() {
		return last_partition;
	}

	public void setLastPartition(String last) {
		this.last_partition = last;
	}

	public DataBaseManager getDatabase() {
		return database;
	}

}
