/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

/**
 * @author AYADI
 *
 */
public class MySqlDataGetters extends DataGetters {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public MySqlDataGetters(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters#treatStatementResultForGetSpectData()
	 */
	@Override
	protected Vector treatStatementResultForGetSpectData(ResultSet rset, boolean isBothReadAndWrite, int data_type, Vector my_spectrumS) {
		// TODO Auto-generated method stub
		IDBConnection dbConn = null;
		try {
			dbConn = ConnectionFactory.getInstance(arch_type);
		} catch (ArchivingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils==null)
			return null;

        try {
			while ( rset.next() )
			{
			    SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
			    SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
			    // Timestamp
			    try
			    {
			        spectrumEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
			        spectrumEvent_rw.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
			    }
			    catch (Exception e)
			    {
			    }

			    // Dim
			    int dim_x = 0;
			    try
			    {
			        dim_x = rset.getInt(2);
			    }
			    catch (Exception e)
			    {
			    }
			    spectrumEvent_ro.setDim_x(dim_x);
			    spectrumEvent_rw.setDim_x(dim_x);
			    // Value
			    String valueReadSt = null;
			    try
			    {
			        valueReadSt = rset.getString(3);
			        if (rset.wasNull())
			        {
			            valueReadSt = "null";
			        }
			    }
			    catch (Exception e)
			    {
			    }
			    String valueWriteSt = null;
			    if ( isBothReadAndWrite )
			    {
			        try
			        {
			            valueWriteSt = rset.getString(4);
			            if (rset.wasNull())
			            {
			                valueWriteSt = "null";
			            }
			        }
			        catch (Exception e)
			        {
			        }
			    }
			    Object value = dbUtils.getSpectrumValue(valueReadSt, valueWriteSt, data_type);
			    if (isBothReadAndWrite)
			    {
			        spectrumEvent_rw.setValue(value);
			        my_spectrumS.add(spectrumEvent_rw);
			    }
			    else
			    {
			        spectrumEvent_ro.setValue(value);
			        my_spectrumS.add(spectrumEvent_ro);
			    }
			    spectrumEvent_ro = null;
			    spectrumEvent_rw = null;
			    value = null;
			    
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return my_spectrumS;
	}

	/*
	 * (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.DataGetters#treatStatementResultForGetImageData(java.sql.ResultSet, boolean, int, java.util.Vector)
	 */
	@Override
	protected Vector treatStatementResultForGetImageData(ResultSet rset,
			boolean isBothReadAndWrite, int data_type, Vector my_ImageS) {
		// TODO Auto-generated method stub
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbUtils==null)
			return null;

        try {
			while ( rset.next() )
			{
			    //System.out.println ( "CLA/getAttSpectrumData2/--------------7" );
			    ImageEvent_RO imageEvent_ro = new ImageEvent_RO();
			    //SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
			    // Timestamp
			    try
			    {
			        imageEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
			    }
			    catch (Exception e)
			    {
			    }
			    //spectrumEvent_rw.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));

			    // Dim
			    int dim_x = rset.getInt(2);
			    int dim_y = rset.getInt(3);
			    //System.out.println ( "CLA/getAttSpectrumData/dim_x/"+dim_x+"/" );
			    imageEvent_ro.setDim_x(dim_x);
			    imageEvent_ro.setDim_y(dim_y);
			    //spectrumEvent_rw.setDim_x(dim_x);
			    // Value
			    String valueReadSt = rset.getString(4);
			    if (rset.wasNull())
			    {
			        valueReadSt = "null";
			    }

			    
			    if ( isBothReadAndWrite )
			    {
			        // TODO: not supported yet
			    }
			    else
			    {
			        imageEvent_ro.setValue( dbUtils.getImageValue(valueReadSt, data_type) );
			        my_ImageS.add(imageEvent_ro);
			    }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return my_ImageS;
	}

	@Override
	protected String getDbScalarLast_nRequest(String tableName,
			String att_name, int number, boolean ro_fields, String fields)
			throws ArchivingException {
		// TODO Auto-generated method stub
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		if( att==null )
			return null;

				int total_count = att.getAttRecordCount(att_name);
				int lim_inf = total_count - number;
				if ( lim_inf < 0 )
				{
					lim_inf = 0;
				}
				int lim_sup = total_count;
				return  "SELECT " + fields + " FROM " + tableName + " LIMIT " + lim_inf + ", " + lim_sup+ " ORDER BY time";
			

	}

	@Override
	public Vector getAttSpectrumDataLast_n(String att_name, int number,
			int writable) throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || att==null || dbUtils==null)
			return null;
	
		Vector my_spectrumS = new Vector();
			boolean ro_fields = ( writable == AttrWriteType._READ || writable == AttrWriteType._WRITE );
			int data_type = att.getAtt_TFW_Data(att_name)[0];

			Connection conn = null;
			Statement stmt = null;
			ResultSet rset = null;
			// Create and execute the SQL query string
			// Build the query string

			String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
			String fields = ro_fields ?
			                ( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SPECTRUM_RO[ 0 ]) + ", " + ConfigConst.TAB_SPECTRUM_RO[ 1 ] + ", " + ConfigConst.TAB_SPECTRUM_RO[ 2 ] ) :
			                ( dbUtils.toDbTimeFieldString(ConfigConst.TAB_SPECTRUM_RW[ 0 ]) + ", " + ConfigConst.TAB_SPECTRUM_RW[ 1 ] + ", " + ConfigConst.TAB_SPECTRUM_RW[ 2 ] + ", " + ConfigConst.TAB_SPECTRUM_RW[ 3 ]); // if (writable == AttrWriteType._READ_WITH_WRITE || writable == AttrWriteType._READ_WRITE)
			//String selectField = ro_fields ? ConfigConst.TAB_SPECTRUM_RO[1] : ConfigConst.TAB_SPECTRUM_RW[1];

			int total_count = att.getAttRecordCount(att_name);
			int lim_inf = total_count - number;
			if ( lim_inf < 0 )
			{
				lim_inf = 0;
			}
			int lim_sup = total_count;
			String query = "SELECT " + fields + " FROM " + tableName + " LIMIT " + lim_inf + ", " + lim_sup  + " ORDER BY time";

			try
			{
				conn = dbConn.getConnection();
				stmt = conn.createStatement();
				dbConn.setLastStatement( stmt);
				rset = stmt.executeQuery(query);
	            while ( rset.next() )
	            {
	                SpectrumEvent_RO spectrumEvent_ro = new SpectrumEvent_RO();
	                SpectrumEvent_RW spectrumEvent_rw = new SpectrumEvent_RW();
	                // Timestamp
	                try
	                {
	                    spectrumEvent_ro.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
	                    spectrumEvent_rw.setTimeStamp(DateUtil.stringToMilli(rset.getString(1)));
	                }
	                catch (Exception e)
	                {
	                }

	                // Dim
	                int dim_x = 0;
	                try
	                {
	                    dim_x = rset.getInt(2);
	                }
	                catch (Exception e)
	                {
	                }
	                spectrumEvent_ro.setDim_x(dim_x);
	                spectrumEvent_rw.setDim_x(dim_x);
	                // Value
	                String valueReadSt = null;
	                try
	                {
	                    valueReadSt = rset.getString(3);
	                    if (rset.wasNull())
	                    {
	                        valueReadSt = "null";
	                    }
	                }
	                catch (Exception e)
	                {
	                }
	                String valueWriteSt = null;
	                if ( !ro_fields )
	                {
	                    try
	                    {
	                        valueWriteSt = rset.getString(4);
	                        if (rset.wasNull())
	                        {
	                            valueWriteSt = "null";
	                        }
	                    }
	                    catch (Exception e)
	                    {
	                    }
	                }
	                Object value = dbUtils.getSpectrumValue(valueReadSt, valueWriteSt, data_type);
	                if (!ro_fields)
	                {
	                    spectrumEvent_rw.setValue(value);
	                    my_spectrumS.add(spectrumEvent_rw);
	                }
	                else
	                {
	                    spectrumEvent_ro.setValue(value);
	                    my_spectrumS.add(spectrumEvent_ro);
	                }
	                spectrumEvent_ro = null;
	                spectrumEvent_rw = null;
	                value = null;
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
				String desc = "Failed while executing MySqlDataGetters.getAttSpectrumDataLast_n() method..." +
				              "\r\n" + query;
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

			return my_spectrumS;

	}
	/**
	 * 
	 * @param tableName
	 * @param data_type
	 * @param data_format
	 * @param writable
	 * @throws ArchivingException
	 */
	@Override
	public void buildAttributeTab(String tableName , int data_type , int data_format , int writable) throws ArchivingException
	{
		switch ( data_format )
		{
			case fr.esrf.Tango.AttrDataFormat._SCALAR:
				switch ( writable )
				{
					case fr.esrf.Tango.AttrWriteType._READ:
						buildAttributeScalarTab_R(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._READ_WITH_WRITE:
						buildAttributeScalarTab_RW(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._WRITE:
						buildAttributeScalarTab_W(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._READ_WRITE:
						buildAttributeScalarTab_RW(tableName , data_type);
						break;
				}
				break;
			case fr.esrf.Tango.AttrDataFormat._SPECTRUM:
				switch ( writable )
				{
					case fr.esrf.Tango.AttrWriteType._READ:
						buildAttributeSpectrumTab_R(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._READ_WITH_WRITE:
						buildAttributeSpectrumTab_RW(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._WRITE:
						//buildAttributeSpectrumTab_W(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._READ_WRITE:
						buildAttributeSpectrumTab_RW(tableName , data_type);
						break;
				}
				break;
			case fr.esrf.Tango.AttrDataFormat._IMAGE:
				switch ( writable )
				{
					case fr.esrf.Tango.AttrWriteType._READ:
						buildAttributeImageTab_R(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._READ_WITH_WRITE:
						buildAttributeImageTab_RW(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._WRITE:
						//buildAttributeImageTab_W(tableName , data_type);
						break;
					case fr.esrf.Tango.AttrWriteType._READ_WRITE:
						buildAttributeImageTab_RW(tableName , data_type);
						break;
				}
				break;
		}
	}

	
	private void buildAttributeScalarTab_R(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeScalarTab(att_id, data_type, ConfigConst.TAB_SCALAR_RO);
	}

	
	private void buildAttributeScalarTab_RW(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeScalarTab(att_id, data_type, ConfigConst.TAB_SCALAR_RW);
	}

	
	private void buildAttributeScalarTab_W(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeScalarTab(att_id, data_type, ConfigConst.TAB_SCALAR_WO);
	}
	
	private void buildAttributeSpectrumTab_R(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeSpectrumTab(att_id, ConfigConst.TAB_SPECTRUM_RO, data_type);
	}

	
	private void buildAttributeSpectrumTab_RW(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeSpectrumTab(att_id, ConfigConst.TAB_SPECTRUM_RW, data_type);
	}
	
	private void buildAttributeImageTab_R(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeImageTab(att_id, ConfigConst.TAB_IMAGE_RO, data_type);
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributesExtraction.ImageAttributes.ImageExtractor#buildAttributeImageTab_RW(java.lang.String, int)
	 */
	
	private void buildAttributeImageTab_RW(String att_id, int data_type)
			throws ArchivingException {
		// TODO Auto-generated method stub
		methods.buildAttributeImageTab(att_id, ConfigConst.TAB_IMAGE_RW, data_type);
	}


}
