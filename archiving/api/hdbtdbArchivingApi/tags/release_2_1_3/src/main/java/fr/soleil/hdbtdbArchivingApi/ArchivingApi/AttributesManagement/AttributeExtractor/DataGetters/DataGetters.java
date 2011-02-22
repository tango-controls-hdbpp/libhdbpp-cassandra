/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.StringFormater;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataExtractor;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.HdbTdbDbData;

/**
 * @author AYADI
 *
 */
public abstract class DataGetters extends DataExtractor{

	/**
	 * 
	 */
	public DataGetters(int type) {
		// TODO Auto-generated constructor stub
		super(type);
	}
	abstract public void buildAttributeTab(String tableName , int data_type , int data_format , int writable) throws ArchivingException;
	abstract protected  Vector treatStatementResultForGetSpectData(ResultSet rset, boolean isBothReadAndWrite, int data_type, Vector my_spectrumS) ;
	abstract protected  Vector treatStatementResultForGetImageData(ResultSet rset, boolean isBothReadAndWrite, int data_type, Vector my_ImageS) ;
	abstract protected String getDbScalarLast_nRequest(String tableName, String att_name, int number, boolean  ro_fields, String fields) throws ArchivingException;
	abstract protected Vector getAttSpectrumDataLast_n(String att_name , int number , int writable) throws ArchivingException;
	

	/*
	 * 
	 */
	public DbData getAttData(String att_name) throws ArchivingException
	{
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = AdtAptAttributesFactory.getInstance(arch_type).getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarData(att_name , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM:
			    Vector list = getAttSpectrumData ( att_name );
				dbData.setData(list);
				break;
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}
		//System.out.println ( "CLA/getAttData/END" );
		return dbData;
	}
	/*
	 * 
	 */
	private DevVarDoubleStringArray getAttScalarData(String att_name , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
			return null;
		DevVarDoubleStringArray dvdsa;
		Vector timeVect = new Vector();
		Vector valueRVect = new Vector();
		Vector valueWVect = new Vector();
		String[] timeArr = new String[ 5 ];
		double[] valueRWArr = new double[ 5 ];
		int data_type = AdtAptAttributesFactory.getInstance(arch_type).getAtt_TFW_Data(att_name)[0];
		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;

		// Create and execute the SQL query string
		// Build the query string
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				// My statement
				String query = "SELECT " + fields + " FROM " + tableName + " ORDER BY time";

				try
				{
					conn = dbConn.getConnection();
					stmt = conn.createStatement();
					dbConn.setLastStatement(stmt);
					rset = stmt.executeQuery(query);
					while ( rset.next() )
					{
						String rawDate = rset.getString ( 1 );
						String displayDate;

						try
						{
							displayDate = DateUtil.stringToDisplayString ( rawDate );
						}
						catch ( Exception e )
						{
							e.printStackTrace();

							String _reason = "FAILED TO PARSE DATE|"+rawDate+"|";
							String message= _reason;
							String _desc = "Failed while executing DataGetters.getAttScalarData() method...";
							throw new ArchivingException(message , _reason , ErrSeverity.WARN , _desc , this.getClass().getName() , e);
						}

						timeVect.addElement ( displayDate );

						if ( ro_fields )
						{
							if (data_type == TangoConst.Tango_DEV_STRING)
							{
								String result = rset.getString(2);
								if (rset.wasNull())
								{
									valueRVect.addElement(  "null"  );
								}
								else
								{
									valueRVect.addElement(  StringFormater.formatStringToRead( result )  );
								}
							}
							else
							{
								double result = rset.getDouble(2);
								if (rset.wasNull())
								{
									valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
								}
								else
								{
									valueRVect.addElement(new Double(result));
								}
							}
						}
						else
						{
							if (data_type == TangoConst.Tango_DEV_STRING)
							{
								String result1 = rset.getString(2);
								if (rset.wasNull())
								{
									valueRVect.addElement(  "null"  );
								}
								else
								{
									valueRVect.addElement(  StringFormater.formatStringToRead( result1 )  );
								}
								String result2 = rset.getString(3);
								if (rset.wasNull())
								{
									valueWVect.addElement(  "null"  );
								}
								else
								{
									valueWVect.addElement(  StringFormater.formatStringToRead( result2 )  );
								}
							}
							else
							{
								double result1 = rset.getDouble(2);
								if (rset.wasNull())
								{
									valueRVect.addElement(  new Double(GlobalConst.NAN_FOR_NULL)  );
								}
								else
								{
									valueRVect.addElement(  new Double(result1)  );
								}
								double result2 = rset.getDouble(3);
								if (rset.wasNull())
								{
									valueWVect.addElement(  new Double(GlobalConst.NAN_FOR_NULL)  );
								}
								else
								{
									valueWVect.addElement(  new Double(result2)  );
								}
							}
						}
					}
				}
				catch ( SQLException e )
				{
					String message = "";
					if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
						message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
					else
						message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

					String reason = GlobalConst.QUERY_FAILURE;
					String desc = "Failed while executing DataGetters.getAttData() method...";
					throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
				}
				finally
				{
					try {
						if(rset !=null)ConnectionCommands.close(rset);
						if(stmt!=null)ConnectionCommands.close(stmt);
						if(conn!=null)dbConn.closeConnection(conn);
					} catch (SQLException e) {
						dbConn.closeConnection(conn);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (data_type == TangoConst.Tango_DEV_STRING)
				{
					// in case of Tango_DEV_STRING, time is coded in double, value in String
					timeArr = DbUtils.toStringArray(timeVect);
					valueRWArr = new double[timeArr.length];
					for (int i = 0; i < timeArr.length; i++)
					{
						valueRWArr[i] = DateUtil.stringToMilli( timeArr[i] );
					}
					timeArr = DbUtils.toStringArray(valueRVect , valueWVect);
					dvdsa = new DevVarDoubleStringArray(valueRWArr , timeArr);
				}
				else
				{
					timeArr = DbUtils.toStringArray(timeVect);
					valueRWArr = DbUtils.toDoubleArray(valueRVect , valueWVect);
					dvdsa = new DevVarDoubleStringArray(valueRWArr , timeArr);
				}
				return dvdsa;
	}
	
	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributesExtraction.SpectrumAttributes.SpectrumExtractor#getAttSpectrumData(java.lang.String)
	 */
	
	private Vector getAttSpectrumData(String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
//		System.out.println ( "CLA/getAttSpectrumData2/---------------1" );
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || att==null || dbUtils==null)
			return null;
		
		Vector my_spectrumS = new Vector();

		int writable = att.getAttDataWritable(att_name);
		int data_format = att.getAttDataFormat(att_name);
		int data_type = att.getAtt_TFW_Data(att_name)[0];
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectField_2 = "";
		String selectField_3 = null;
		String selectFields = "";
		String message = "" , reason = "" , desc = "";
		//System.out.println ( "CLA/getAttSpectrumData2/---------------2" );
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				message = "Failed retrieving data ! ";
				reason = "The attribute should be Spectrum ";
				desc = "The attribute format is  not Spectrum : " + data_format + " (Scalar) !!";
				throw new ArchivingException(message , reason , null , desc , this.getClass().getName());
			case AttrDataFormat._SPECTRUM:
				//System.out.println ( "CLA/getAttSpectrumData2/---------------3" );
				String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
				//System.out.println ( "CLA/getAttSpectrumData2/tableName/"+tableName+"/" );
				boolean isBothReadAndWrite = !( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE ); 
				if ( ! isBothReadAndWrite )
				{
					selectField_0 = ConfigConst.TAB_SPECTRUM_RO[ 0 ];
					selectField_1 = ConfigConst.TAB_SPECTRUM_RO[ 1 ];
					selectField_2 = ConfigConst.TAB_SPECTRUM_RO[ 2 ];
				}
				else 
				{
					selectField_0 = ConfigConst.TAB_SPECTRUM_RW[ 0 ];
					selectField_1 = ConfigConst.TAB_SPECTRUM_RW[ 1 ];
					selectField_2 = ConfigConst.TAB_SPECTRUM_RW[ 2 ];
					selectField_3 = ConfigConst.TAB_SPECTRUM_RW[ 3 ];
				}
				selectFields = dbUtils.toDbTimeFieldString(selectField_0) + ", " + selectField_1 + ", " + selectField_2;
				//System.out.println ( "CLA/getAttSpectrumData2/---------------4" );
				// todo Correct the BUG on the oracle side
				if ( isBothReadAndWrite )
				{
				    selectFields += ", " + selectField_3;
				}


				getAttributeDataQuery =
				"SELECT " + selectFields + " FROM " + tableName + " ORDER BY time";
				try
				{
				    //System.out.println ( "CLA/getAttSpectrumData2/---------------5" );
					conn = dbConn.getConnection();
				    stmt = conn.createStatement();
				    dbConn.setLastStatement(stmt);
					rset = stmt.executeQuery(getAttributeDataQuery);
					//System.out.println ( "CLA/getAttSpectrumData2/---------------6" );
                   my_spectrumS = treatStatementResultForGetSpectData(rset, isBothReadAndWrite, data_type, my_spectrumS);

				}
				catch ( SQLException e )
				{
				    //System.out.println ( "CLA/getAttSpectrumData2/---------------9" );
				    e.printStackTrace();
				    
				    if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
						message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
					else
						message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

					String _reason = GlobalConst.QUERY_FAILURE;
					String _desc = "Failed while executing DataGetters.getAttSpectrumData() method...";
					throw new ArchivingException(message , _reason , ErrSeverity.WARN , _desc , this.getClass().getName() , e);
				}
				finally
		        {
		        	try {
						if(rset !=null)ConnectionCommands.close(rset);
						if(stmt!=null)ConnectionCommands.close(stmt);
						if(conn!=null)dbConn.closeConnection(conn);
					} catch (SQLException e) {
						dbConn.closeConnection(conn);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
				break;
			case AttrDataFormat._IMAGE:
				message = "Failed retrieving data ! ";
				reason = "The attribute should be Spectrum ";
				desc = "The attribute format is  not Spectrum : " + data_format + " (Image) !!";
				throw new ArchivingException(message , reason , null , desc , this.getClass().getName());
		}
		//System.out.println ( "CLA/getAttSpectrumData2/---------------10" );
		return my_spectrumS;
	

	}
	/*
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributesExtraction.ImageAttributes.ImageExtractor#getAttImageData(java.lang.String)
	 */
	
	private Vector getAttImageData(String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
        // System.out.println ( "CLA/getAttSpectrumData2/---------------1" );
        Vector my_imageS = new Vector();
    	IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || att==null || dbUtils==null)
			return my_imageS;

        int writable = att.getAttDataWritable(att_name);
        int data_format = att.getAttDataFormat(att_name);
        int data_type = att.getAtt_TFW_Data(att_name)[0];

        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        // Create and execute the SQL query string
        // Build the query string
        String getAttributeDataQuery = "";
        String selectField_0 = "";
        String selectField_1 = "";
        String selectField_2 = "";
        String selectField_3 = "";
        String selectField_4 = null;
        String selectFields = "";
        String message = "" , reason = "" , desc = "";
        //System.out.println ( "CLA/getAttSpectrumData2/---------------2" );
        switch ( data_format )
        {  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
            case AttrDataFormat._SCALAR: methods.makeDataException(data_format, "Image", "Scalar");
            case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Image", "Spectrum");
            case AttrDataFormat._IMAGE:
                String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
                //System.out.println ( "CLA/getAttSpectrumData2/tableName/"+tableName+"/" );
                    boolean isBothReadAndWrite = !( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE ); 
                    if ( ! isBothReadAndWrite )
                    {
                        selectField_0 = ConfigConst.TAB_IMAGE_RO[ 0 ];
                        selectField_1 = ConfigConst.TAB_IMAGE_RO[ 1 ];
                        selectField_2 = ConfigConst.TAB_IMAGE_RO[ 2 ];
                        selectField_3 = ConfigConst.TAB_IMAGE_RO[ 3 ];
                    }
                    else 
                    {
                        selectField_0 = ConfigConst.TAB_IMAGE_RW[ 0 ];
                        selectField_1 = ConfigConst.TAB_IMAGE_RW[ 1 ];
                        selectField_2 = ConfigConst.TAB_IMAGE_RW[ 2 ];
                        selectField_3 = ConfigConst.TAB_IMAGE_RW[ 3 ];
                        selectField_4 = ConfigConst.TAB_IMAGE_RW[ 4 ];
                    }
                    selectFields = dbUtils.toDbTimeFieldString(selectField_0) + ", " + selectField_1 + ", " + selectField_2 + ", " + selectField_3;
                    if ( isBothReadAndWrite )
                    {
                        selectFields += ", " + selectField_4;
                    }


                    getAttributeDataQuery =
                    "SELECT " + selectFields + " FROM " + tableName + " ORDER BY time";
                    try
                    {
                        //System.out.println ( "CLA/getAttSpectrumData2/---------------5" );
                    	conn = dbConn.getConnection();
                        stmt = conn.createStatement();
                        dbConn.setLastStatement(stmt);
                        rset = stmt.executeQuery(getAttributeDataQuery);
                        //System.out.println ( "CLA/getAttSpectrumData2/---------------6" );
                        my_imageS = treatStatementResultForGetImageData(rset, isBothReadAndWrite, data_type, my_imageS);
                    }
                    catch ( SQLException e )
                    {
                        //System.out.println ( "CLA/getAttSpectrumData2/---------------9" );
                        e.printStackTrace();
                        
                        if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
                            message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
                        else
                            message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

                        String _reason = GlobalConst.QUERY_FAILURE;
                        String _desc = "Failed while executing DataGetters.getAttImageData() method...";
                        throw new ArchivingException(message , _reason , ErrSeverity.WARN , _desc , this.getClass().getName() , e);
                    }
                    finally
                    {
                    	try {
            				if(rset !=null)ConnectionCommands.close(rset);
            				if(stmt!=null)ConnectionCommands.close(stmt);
            				if(conn!=null)dbConn.closeConnection(conn);
            			} catch (SQLException e) {
            				dbConn.closeConnection(conn);
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}
                    }
                    break;
        }
        //System.out.println ( "CLA/getAttSpectrumData2/---------------10" );
        return my_imageS;

	}

	/**
	 * <b>Description : </b>    	Returns the number of the data archieved for an attribute
	 *
	 * @return The number of the data archieved for an attribute<br>
	 * @throws ArchivingException
	 */
	public int getAttDataCount(String att_name) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null ||  dbUtils==null)
			return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name.trim());
		// MySQL and Oracle querry are the same in this case : no test
		getAttributeDataQuery =
		"SELECT " + " COUNT(*) " + " FROM " + tableName;
		return methods.getDataCountFromQuery( getAttributeDataQuery, dbConn);
	}
	/**
	 * <b>Description : </b>    	Retrieves n last data, for a given scalar attribute.
	 *
	 * @param argin The attribute's name and the number which specifies the number of desired data.
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataLast_n(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return null;

		String att_name = argin[ 0 ];
		int number = Integer.parseInt(argin[ 1 ]);
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);

		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataLast_n(att_name , number , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM:
			    Vector list = getAttSpectrumDataLast_n ( att_name , number , tfw[ 2 ] );
				dbData.setData(list);
				break;
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/*
	 * 
	 */
	public DevVarDoubleStringArray getAttScalarDataLast_n(String att_name , int number , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || att==null || dbUtils==null)
			return null;

		DevVarDoubleStringArray dvdsa;
		Vector timeVect = new Vector();
		Vector valueRVect = new Vector();
		Vector valueWVect = new Vector();
		String[] timeArr = new String[ 5 ];
		double[] valueRWArr = new double[ 5 ];
		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		int data_type = att.getAtt_TFW_Data(att_name)[0];
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)

				// Create and execute the SQL query string
				// Build the query string
				String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
				String query = getDbScalarLast_nRequest(tableName, att_name, number, ro_fields, fields);
				try
				{
					conn = dbConn.getConnection();
					stmt = conn.createStatement();
					dbConn.setLastStatement(stmt);
					rset = stmt.executeQuery(query);
					if ( ro_fields )
					{
						while ( rset.next() )
						{
							if (data_type == TangoConst.Tango_DEV_STRING)
							{
								timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
								String result = rset.getString(2);
								if (rset.wasNull())
								{
									valueRVect.addElement("null");
								}
								else
								{
									valueRVect.addElement( StringFormater.formatStringToRead(result) );
								}
								//System.out.println ( "CLA/getAttScalarDataLast_n/rset.getString(2)/"+rset.getString(2) );
							}
							else
							{
								timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
								double result = rset.getDouble(2);
								if (rset.wasNull())
								{
									valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
								}
								else
								{
									valueRVect.addElement(new Double(result));
								}
								//System.out.println ( "CLA/getAttScalarDataLast_n/rset.getDouble(2)/"+rset.getDouble(2)+"/rset.getString(2)/"+rset.getString(2) );
							}
						}
					}
					else
					{
						while ( rset.next() )
						{
							if (data_type == TangoConst.Tango_DEV_STRING)
							{
								timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
								String result1 = rset.getString(2);
								if (rset.wasNull())
								{
									valueRVect.addElement("null");
								}
								else
								{
									valueRVect.addElement( StringFormater.formatStringToRead(result1) );
								}
								String result2 = rset.getString(3);
								if (rset.wasNull())
								{
									valueWVect.addElement("null");
								}
								else
								{
									valueWVect.addElement( StringFormater.formatStringToRead(result2) );
								}
								//System.out.println ( "CLA/getAttScalarDataLast_n/rset.getString(2)/"+rset.getString(2)+"/rset.getString(3)/"+rset.getString(3) );
							}
							else
							{
								timeVect.addElement(DateUtil.stringToDisplayString(rset.getString(1)));
								double result1 = rset.getDouble(2);
								if (rset.wasNull())
								{
									valueRVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
								}
								else
								{
									valueRVect.addElement(new Double(result1));
								}
								double result2 = rset.getDouble(3);
								if (rset.wasNull())
								{
									valueWVect.addElement(new Double(GlobalConst.NAN_FOR_NULL));
								}
								else
								{
									valueWVect.addElement(new Double(result2));
								}

								//System.out.println ( "CLA/getAttScalarDataLast_n/rset.getDouble(2)/"+rset.getDouble(2)+"/rset.getDouble(3)/"+rset.getDouble(3)+"/rset.getString(2)/"+rset.getString(2)+"/rset.getString(3)/"+rset.getString(3) );
							}
						}
					}
				}
				catch ( SQLException e )
				{
					String message = "";
					if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
						message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
					else
						message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

					String reason = GlobalConst.QUERY_FAILURE;
					String desc = "Failed while executing DataGetters.getAttDataLast_n() method...";
					throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
				}
				finally
				{
					try {
						ConnectionCommands.close(rset);
						ConnectionCommands.close(stmt);
						dbConn.closeConnection(conn);
					} catch (SQLException e) {
						dbConn.closeConnection(conn);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (data_type == TangoConst.Tango_DEV_STRING)
				{
					// in case of Tango_DEV_STRING, time is coded in double, value in String
					timeArr = DbUtils.toStringArray(timeVect);
					valueRWArr = new double[timeArr.length];
					for (int i = 0; i < timeArr.length; i++)
					{
						valueRWArr[i] = DateUtil.stringToMilli(timeArr[i]);
					}
					timeArr = DbUtils.toStringArray(valueRVect , valueWVect);
					dvdsa = new DevVarDoubleStringArray(valueRWArr , timeArr);
				}
				else
				{
					timeArr = DbUtils.toStringArray(timeVect);
					valueRWArr = DbUtils.toDoubleArray(valueRVect , valueWVect);
					dvdsa = new DevVarDoubleStringArray(valueRWArr , timeArr);
				}
				return dvdsa;
	}
	/**
	 * 
	 * @param argin
	 * @return
	 * @throws ArchivingException
	 */
	public Vector getAttSpectrumDataLast_n(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null )
			return null;

		String att_name = argin[ 0 ];
		int number = Integer.parseInt(argin[ 1 ]);
		Vector my_spectrumS = new Vector();
		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		String message = "" , reason = "" , desc = "";
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR: methods.makeDataException(data_format, "Spectrum", "Scalar");
			case AttrDataFormat._SPECTRUM:
				my_spectrumS = getAttSpectrumDataLast_n(att_name , number , writable);
				break;
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Spectrum", "Image");
                }

		// Returns the names list
		return my_spectrumS;
	}


}
