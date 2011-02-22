/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevVarDoubleStringArray;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataExtractor;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.HdbTdbDbData;

/**
 * @author AYADI
 *
 */
public class InfSupGettersBetweenDates extends DataExtractor {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public InfSupGettersBetweenDates(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	/**
	 * <b>Description : </b>    	Retrieves data beetwen two dates (date_1 & date_2) - Data are lower than the given value x.
	 *
	 * @param argin The attribute's name, the lower limit, the upper limit, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataInfThanBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return null;

		String att_name = argin[ 0 ];
		int upper_value = Integer.parseInt(argin[ 1 ]);
		String time_0 = argin[ 2 ];
		String time_1 = argin[ 3 ];
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataInfThanBetweenDates(att_name , upper_value , time_0 , time_1 , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/**
	 * <b>Description : </b>    	Returns the number data lower than the given value x, and beetwen two dates (date_1 & date_2).
	 *
	 * @param argin The attribute's name, the lower limit, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The number data lower than the given value x, and beetwen two dates (date_1 & date_2).<br>
	 * @throws ArchivingException
	 */
	public int getAttDataInfThanBetweenDatesCount(String[] argin) throws ArchivingException
	{
		int valuesCount = 0;
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return 0;

		String att_name = argin[ 0 ];
		int upper_value = Integer.parseInt(argin[ 1 ]);
		String time_0 = argin[ 2 ];
		String time_1 = argin[ 3 ];

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataInfThanBetweenDatesCount(att_name , upper_value , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	/**
	 * <b>Description : </b>    	Retrieves data beetwen two dates (date_1 & date_2) - Data are higher than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit, the beginning date  (DD-MM-YYYY HH24:MI:SS.FF) and the ending date  (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataSupThanBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return null;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		String time_0 = argin[ 2 ];
		String time_1 = argin[ 3 ];
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		String message = "" , reason = "" , desc = "";
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataSupThanBetweenDates(att_name , lower_value , time_0 , time_1 , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/**
	 * <b>Description : </b>    	Returns the number of data higher than the given value y, and beetwen two dates (date_1 & date_2).
	 *
	 * @param argin The attribute's name, the lower limit, the beginning date  (DD-MM-YYYY HH24:MI:SS.FF) and the ending date  (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The number of data higher than the given value y, and beetwen two dates (date_1 & date_2).<br>
	 * @throws ArchivingException
	 */
	public int getAttDataSupThanBetweenDatesCount(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return 0;

		int valuesCount = 0;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		String time_0 = argin[ 2 ];
		String time_1 = argin[ 3 ];

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataSupThanBetweenDatesCount(att_name , lower_value , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	   /**
	 * <b>Description : </b>    	Retrieves data beetwen two dates (date_1 & date_2).
	 * Data are lower than the given value x OR higher than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit, the upper limit, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataInfOrSupThanBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return null;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);
		String time_0 = argin[ 3 ];
		String time_1 = argin[ 4 ];
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataInfOrSupThanBetweenDates(att_name , lower_value , upper_value , time_0 , time_1 , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}

	/*
	 * 
	 */
	public int getAttDataInfOrSupThanBetweenDatesCount(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return 0;

		int valuesCount = 0;
		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);
		String time_0 = argin[ 3 ];
		String time_1 = argin[ 4 ];

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataInfOrSupThanBetweenDatesCount(att_name , lower_value , upper_value , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	/**
	 * <b>Description : </b>    	Retrieves data beetwen two dates (date_1 & date_2) - Data are higher than the given value x AND lower than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit, the upper limit, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataSupAndInfThanBetweenDates(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return null;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);
		String time_0 = argin[ 3 ];
		String time_1 = argin[ 4 ];
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataSupAndInfThanBetweenDates(att_name , lower_value , upper_value , time_0 , time_1 , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/**
	 * <b>Description : </b>    	Returns the number of data higher than the given value x, (AND) lower than the given value y, and beetwen two dates (date_1 & date_2).
	 *
	 * @param argin The attribute's name, the lower limit, the upper limit, the beginning date (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
	 * @return The number of data higher than the given value x, (AND) lower than the given value y, and beetwen two dates (date_1 & date_2).<br>
	 * @throws ArchivingException
	 */
	public int getAttDataSupAndInfThanBetweenDatesCount(String[] argin) throws ArchivingException
	{
		int valuesCount = 0;
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att == null)
		return 0;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);
		String time_0 = argin[ 3 ];
		String time_1 = argin[ 4 ];

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataSupAndInfThanBetweenDatesCount(att_name , lower_value , upper_value , time_0 , time_1 , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	/*
	 * 
	 */
	private DevVarDoubleStringArray getAttScalarDataInfThanBetweenDates(String att_name , int upper_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );

		// Create and execute the SQL query string
		// Build the query string
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String dateClause = ConfigConst.TAB_SCALAR_RO[ 0 ] + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
				// My statement
				// String query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField + " < " + lower_value + " OR " + selectField + " > " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")";
				String query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField + " < " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")" + " ORDER BY time";

				return methods.getAttScalarConditionBetweenDates(query, ro_fields, dbConn);
	}
	/*
	 * 
	 */
	private DevVarDoubleStringArray getAttScalarDataSupThanBetweenDates(String att_name , int lower_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		// Create and execute the SQL query string
		// Build the query string
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String dateClause = ConfigConst.TAB_SCALAR_RO[ 0 ] + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
				String query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField + " > " + lower_value + ")" + " AND " + "(" + dateClause + ")" + ")" + " ORDER BY time";
				return methods.getAttScalarConditionBetweenDates(query, ro_fields, dbConn);

	}
	/*
	 * 
	 */
	private DevVarDoubleStringArray getAttScalarDataInfOrSupThanBetweenDates(String att_name , int lower_value , int upper_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );

		// Create and execute the SQL query string
		// Build the query string
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name); 
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String dateClause = ConfigConst.TAB_SCALAR_RO[ 0 ] + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
				String query =
					"SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField + " < " + lower_value + " OR " + selectField + " > " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")" + " ORDER BY time";
				return methods.getAttScalarConditionBetweenDates(query, ro_fields, dbConn);
	}
	/*
	 * 
	 */
	private DevVarDoubleStringArray getAttScalarDataSupAndInfThanBetweenDates(String att_name , int lower_value , int upper_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		// Create and execute the SQL query string
		// Build the query string
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String dateClause = ConfigConst.TAB_SCALAR_RO[ 0 ] + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
				// My statement
				// String query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField + " < " + lower_value + " OR " + selectField + " > " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")";
				String query =
					"SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField + " > " + lower_value + " AND " + selectField + " < " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")" + " ORDER BY time";
				return methods.getAttScalarConditionBetweenDates(query, ro_fields, dbConn);
	}
	/*
	 * 
	 */
	private int getAttScalarDataSupThanBetweenDatesCount(String att_name , int lower_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectFields = "";
		String dateClause = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE )
		{
			selectField_0 = ConfigConst.TAB_SCALAR_RO[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		}
		else
		{ // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			selectField_0 = ConfigConst.TAB_SCALAR_RW[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];
		}
		selectFields = "COUNT(*)";
		dateClause = selectField_0 + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());

		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField_1 + " > " + lower_value + ")" + " AND " + "(" + dateClause + ")" + ")"  ;
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);
	}
	/*
	 * 
	 */
	private int getAttScalarDataInfThanBetweenDatesCount(String att_name , int upper_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectFields = "";
		String dateClause = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE )
		{
			selectField_0 = ConfigConst.TAB_SCALAR_RO[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		}
		else
		{ // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			selectField_0 = ConfigConst.TAB_SCALAR_RW[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];
		}
		selectFields = "COUNT(*)";
		dateClause = selectField_0 + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());

		getAttributeDataQuery = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField_1 + " < " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")";

		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);
}
	/*
	 * 
	 */
	private int getAttScalarDataInfOrSupThanBetweenDatesCount(String att_name , int lower_value , int upper_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectFields = "";
		String dateClause = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE )
		{
			selectField_0 = ConfigConst.TAB_SCALAR_RO[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		}
		else
		{ // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			selectField_0 = ConfigConst.TAB_SCALAR_RW[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];
		}
		selectFields = "COUNT(*)";

		dateClause = selectField_0 + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField_1 + " < " + lower_value + " OR " + selectField_1 + " > " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")";
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);
	}
	/**
	 * 
	 * @param att_name
	 * @param lower_value
	 * @param upper_value
	 * @param time_0
	 * @param time_1
	 * @param writable
	 * @return
	 * @throws ArchivingException
	 */
	private int getAttScalarDataSupAndInfThanBetweenDatesCount(String att_name , int lower_value , int upper_value , String time_0 , String time_1 , int writable) throws ArchivingException
	{
		// Create and execute the SQL query string
		// Build the query string
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		String getAttributeDataQuery = "";
		String selectField_0 = "";
		String selectField_1 = "";
		String selectFields = "";
		String dateClause = "";
		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE )
		{
			selectField_0 = ConfigConst.TAB_SCALAR_RO[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		}
		else
		{ // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			selectField_0 = ConfigConst.TAB_SCALAR_RW[ 0 ];
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];
		}
		selectFields = "COUNT(*)";
		dateClause = selectField_0 + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());

		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + "(" + selectField_1 + " > " + lower_value + " AND " + selectField_1 + " < " + upper_value + ")" + " AND " + "(" + dateClause + ")" + ")";
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);
	}

}
