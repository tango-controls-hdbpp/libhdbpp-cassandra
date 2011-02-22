/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

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
public class InfSupGetters extends DataExtractor {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public InfSupGetters(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	/**
	 * <b>Description : </b>    	Returns the data lower than the given value.
	 *
	 * @param argin The attribute's name and the  upper limit.
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataInfThan(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return null;
		String att_name = argin[ 0 ];
		int upper_value = Integer.parseInt(argin[ 1 ]);
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataInfThan(att_name , upper_value , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/**
	 * <b>Description : </b>    	Returns the number of data lower than the given value.
	 *
	 * @param argin The attribute's name and the  upper limit.
	 * @return The number of scalar data lower than the given value and for the specified attribute.<br>
	 * @throws ArchivingException
	 */
	public int getAttDataInfThanCount(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return 0;
		int valuesCount = 0;

		String att_name = argin[ 0 ];
		int upper_value = Integer.parseInt(argin[ 1 ]);

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataInfThanCount(att_name , upper_value , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	/**
	 * <b>Description : </b>    	Returns the data higher than the given value.
	 *
	 * @param argin The attribute's name and the  lower limit
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataSupThan(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return null;
		String att_name = argin[ 0 ];
		int upper_value = Integer.parseInt(argin[ 1 ]);
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataSupThan(att_name , upper_value , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/**
	 * <b>Description : </b>    	Returns the number of data higher than the given value.
	 *
	 * @param argin The attribute's name and the  lower limit
	 * @return The number of data higher than the given value.<br>
	 * @throws ArchivingException
	 */
	public int getAttDataSupThanCount(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return 0;
		int valuesCount = 0;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		//int data_type = tfw[0];
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataSupThanCount(att_name , lower_value , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	/**
	 * <b>Description : </b>    	Returns data that are lower than the given value x OR higher than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit and the upper limit
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataInfOrSupThan(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return null;
		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataInfOrSupThan(att_name , lower_value , upper_value , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}
	/**
	 * <b>Description : </b>    	Returns the number of data lower than the given value x OR higher than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit and the upper limit
	 * @return The number of scalar data lower than the given value x OR higher than the given value y, associated with their corresponding timestamp <br>
	 * @throws ArchivingException
	 */
	public int getAttDataInfOrSupThanCount(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return 0;
		int valuesCount = 0;

		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataInfOrSupThanCount(att_name , lower_value , upper_value , writable);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(data_format, "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(data_format, "Scalar", "Image");
		}
		// Returns the number of records
		return valuesCount;
	}
	/**
	 * <b>Description : </b>    	Returns data that are highter than the given value x OR lower than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit and the upper limit
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public DbData getAttDataSupAndInfThan(String[] argin) throws ArchivingException
	{
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return null;
		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);
		DevVarDoubleStringArray dvdsa = new DevVarDoubleStringArray();
		HdbTdbDbData dbData = new HdbTdbDbData(att_name);
		int[] tfw = att.getAtt_TFW_Data(att_name);
		dbData.setData_type(tfw[ 0 ]);
		dbData.setData_format(tfw[ 1 ]);
		dbData.setWritable(tfw[ 2 ]);
		switch ( dbData.getData_format() )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				dvdsa = getAttScalarDataSupAndInfThan(att_name , lower_value , upper_value , dbData.getWritable());
				dbData.setData(dvdsa);
				break;
			case AttrDataFormat._SPECTRUM: methods.makeDataException(dbData.getData_format(), "Scalar", "Spectrum");
			case AttrDataFormat._IMAGE: methods.makeDataException(dbData.getData_format(), "Scalar", "Image");
		}

		// Returns the names list
		return dbData;
	}

	/**
	 * <b>Description : </b>    	Returns data that are highter than the given value x AND lower than the given value y.
	 *
	 * @param argin The attribute's name, the lower limit and the upper limit
	 * @return The scalar data for the specified attribute<br>
	 * @throws ArchivingException
	 */
	public int getAttDataSupAndInfThanCount(String[] argin) throws ArchivingException
	{
		int valuesCount = 0;
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if(att==null)
			return valuesCount;
		String att_name = argin[ 0 ];
		int lower_value = Integer.parseInt(argin[ 1 ]);
		int upper_value = Integer.parseInt(argin[ 2 ]);

		// Retreive informations on format and writable
		int[] tfw = att.getAtt_TFW_Data(att_name);
		int data_format = tfw[ 1 ];
		int writable = tfw[ 2 ];
		switch ( data_format )
		{  // [0 - > SCALAR] (1 - > SPECTRUM] [2 - > IMAGE]
			case AttrDataFormat._SCALAR:
				valuesCount = getAttScalarDataSupAndInfThanCount(att_name , lower_value , upper_value , writable);
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
	private DevVarDoubleStringArray getAttScalarDataInfThan(String att_name , int upper_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;
		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
				String query =
					"SELECT " + fields + " FROM " + tableName + " WHERE " +selectField + " < " + upper_value + " ORDER BY time";

				return methods.getAttScalarDataCondition(att_name, query, ro_fields, dbConn);

	}
	/*
	 * 
	 */
	private DevVarDoubleStringArray getAttScalarDataSupThan(String att_name , int lower_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
				String query =
					"SELECT " + fields + " FROM " + tableName + " WHERE (" +selectField + " > " + lower_value + ") ORDER BY time";

				return methods.getAttScalarDataCondition(att_name, query, ro_fields, dbConn);

	}
	/*
	 * 
	 */
	public DevVarDoubleStringArray getAttScalarDataInfOrSupThan(String att_name , int lower_value , int upper_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
				String query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + selectField + " < " + lower_value + " OR " + selectField + " > " + upper_value + ")" +  " ORDER BY time";
				return methods.getAttScalarDataCondition(att_name, query, ro_fields, dbConn);

	}
	/*
	 * 
	 */
	public DevVarDoubleStringArray getAttScalarDataSupAndInfThan(String att_name , int lower_value , int upper_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return null;

		boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
		String fields = ro_fields ?
				( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RO[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RO[ 1 ] ) :
					( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SCALAR_RW[ 0 ]) + ", " + ConfigConst.TAB_SCALAR_RW[ 1 ] + ", " + ConfigConst.TAB_SCALAR_RW[ 2 ] ); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
				String selectField = ro_fields ? ConfigConst.TAB_SCALAR_RO[ 1 ] : ConfigConst.TAB_SCALAR_RW[ 1 ];
				String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
				String query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + selectField + " > " + lower_value + " AND " + selectField + " < " + upper_value + ")" + " ORDER BY time";
				return methods.getAttScalarDataCondition(att_name, query, ro_fields, dbConn);

	}

	/*
	 * 
	 */
	private int getAttScalarDataInfThanCount(String att_name , int upper_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_1 = "";
		String selectFields = "";

		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE ) 
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		else		 
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];

		selectFields = "COUNT(*)";
		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE (" + selectField_1 + " < " + upper_value  + ") ORDER BY time";
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);
	}
	/*
	 * 
	 */
	public int getAttScalarDataSupThanCount(String att_name , int lower_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_1 = "";
		String selectFields = "";

		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE ) 
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		else		 
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];

		selectFields = "COUNT(*)";

		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE (" + selectField_1 + " > " + lower_value + ") ORDER BY time";
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);

	}
	/*
	 * 
	 */
	public int getAttScalarDataInfOrSupThanCount(String att_name , int lower_value , int upper_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_1 = "";
		String selectFields = "";

		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE ) 
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		else		 
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];

		selectFields = "COUNT(*)";
		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + selectField_1 + " < " + lower_value + " OR " + selectField_1 + " > " + upper_value + ") "  + " ORDER BY time";
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);

	}
	/*
	 * 
	 */
	public int getAttScalarDataSupAndInfThanCount(String att_name , int lower_value , int upper_value , int writable) throws ArchivingException
	{
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils == null)
		return 0;

		// Create and execute the SQL query string
		// Build the query string
		String getAttributeDataQuery = "";
		String selectField_1 = "";
		String selectFields = "";

		String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
		if ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE ) 
			selectField_1 = ConfigConst.TAB_SCALAR_RO[ 1 ];
		else		 
			selectField_1 = ConfigConst.TAB_SCALAR_RW[ 1 ];

		selectFields = "COUNT(*)";
		getAttributeDataQuery =
			"SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + selectField_1 + " > " + lower_value + " AND " + selectField_1 + " < " + upper_value + ") " + " ORDER BY time";
		return methods.getDataCountFromQuery(getAttributeDataQuery, dbConn);

	}

}
