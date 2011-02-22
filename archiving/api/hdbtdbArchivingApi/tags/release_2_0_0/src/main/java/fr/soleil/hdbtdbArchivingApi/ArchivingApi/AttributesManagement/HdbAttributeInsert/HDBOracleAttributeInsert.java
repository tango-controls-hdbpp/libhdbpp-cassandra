/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class HDBOracleAttributeInsert extends HdbAttributeInsert {

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public HDBOracleAttributeInsert(IDBConnection con, IAdtAptAttributes at) {
		super(con, at);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.HDBExtractor#insert_ImageData_RO_DataBase(java.lang.StringBuffer, java.lang.StringBuffer, java.lang.StringBuffer, int, int, java.sql.Timestamp, java.lang.Double[][], java.lang.StringBuffer)
	 */
	@Override
	protected void insert_ImageData_RO_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			int dim_y, Timestamp timeSt, Double[][] dvalue,
			StringBuffer valueStr, String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
			Connection conn = null;
		    CallableStatement cstmt_ins_spectrum_ro = null;
			query = new StringBuffer().append("{call ").append(dbConn.getSchema()).append(".ins_im_1val (?, ?, ?, ?, ?)}");

			//System.out.println ( "CLA/INSERTING IMAGE VALUE OF LENGTH/" + valueStr.toString().length() );
			try
			{
				conn = dbConn.getConnection();
				cstmt_ins_spectrum_ro = conn.prepareCall(query.toString());
				dbConn.setLastStatement(cstmt_ins_spectrum_ro);
				cstmt_ins_spectrum_ro.setString(1 , att_name);
				cstmt_ins_spectrum_ro.setTimestamp(2 , timeSt);
				cstmt_ins_spectrum_ro.setInt(3 , dim_x);
                cstmt_ins_spectrum_ro.setInt(4 , dim_y);
                if (dvalue == null)
                {
                    cstmt_ins_spectrum_ro.setNull(4 , java.sql.Types.CLOB);
                }
                else cstmt_ins_spectrum_ro.setString(5 , valueStr.toString());

				cstmt_ins_spectrum_ro.executeUpdate();

			}
			catch ( SQLException e )
			{
				String message = "";
				if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.INSERT_FAILURE;
				String desc = "Failed while executing HDBOracleAttributeInsert.insert_ImageData_RO() method..." +
				              "\r\n\t\t Query : " + query +
				              "\r\n\t\t Param 1 (Attribute name) : " + att_name +
				              "\r\n\t\t Param 2 (Timestamp)      : " + timeSt.toString() +
				              "\r\n\t\t Param 3 (X Dimension)    : " + dim_x +
                              "\r\n\t\t Param 3 (Y Dimension)    : " + dim_y +
				              "\r\n\t\t Param 4 (Value)          : " + valueStr.toString() +
				              "\r\n\t\t Code d'erreur : " + e.getErrorCode() +
				              "\r\n\t\t Message : " + e.getMessage() +
				              "\r\n\t\t SQL state : " + e.getSQLState() +
				              "\r\n\t\t Stack : ";
				e.printStackTrace();

				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			}
			finally
			{
				try {
					ConnectionCommands.close(cstmt_ins_spectrum_ro);
					dbConn.closeConnection(conn);
				} catch (SQLException e) {
					dbConn.closeConnection(conn);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		

	}
	/*
	 * (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.HDBExtractor#insert_SpectrumData_RO_DataBase(java.lang.StringBuffer, java.lang.StringBuffer, java.lang.StringBuffer, int, java.sql.Timestamp, java.lang.Double[], java.lang.StringBuffer, java.lang.String)
	 */
	@Override
	protected void insert_SpectrumData_RO_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalue, StringBuffer valueStr,
			String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		CallableStatement cstmt_ins_spectrum_ro = null;
		query = new StringBuffer().append("{call ").append(dbConn.getSchema()).append(".ins_sp_1val (?, ?, ?, ?)}");
		Connection conn = null;
		try
		{
			 conn = dbConn.getConnection();
			cstmt_ins_spectrum_ro = conn.prepareCall(query.toString());
			dbConn.setLastStatement( cstmt_ins_spectrum_ro);
			cstmt_ins_spectrum_ro.setString(1 , att_name);
			cstmt_ins_spectrum_ro.setTimestamp(2 , timeSt);
			cstmt_ins_spectrum_ro.setInt(3 , dim_x);
            if (valueStr == null)
            {
                cstmt_ins_spectrum_ro.setNull(4 , java.sql.Types.CLOB);
            }
            else
            {
                cstmt_ins_spectrum_ro.setString(4 , valueStr.toString());
            }

            try
            {
			cstmt_ins_spectrum_ro.executeUpdate();
		}
		catch ( SQLException e )
		{
            	ConnectionCommands.close(cstmt_ins_spectrum_ro);
            	throw e;
            }
		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) )
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing HDBOracleAttributeInsert.insert_SpectrumData_RO() method..." +
			              "\r\n\t\t Query : " + query +
			              "\r\n\t\t Param 1 (Attribute name) : " + att_name +
			              "\r\n\t\t Param 2 (Timestamp)      : " + timeSt.toString() +
			              "\r\n\t\t Param 3 (Dimension)      : " + dim_x +
			              "\r\n\t\t Param 4 (Value)          : " + valueStr.toString() +
			              "\r\n\t\t Code d'erreur : " + e.getErrorCode() +
			              "\r\n\t\t Message : " + e.getMessage() +
			              "\r\n\t\t SQL state : " + e.getSQLState() +
			              "\r\n\t\t Stack : ";
			e.printStackTrace();

			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally{
			
			try {
				if(cstmt_ins_spectrum_ro!=null)ConnectionCommands.close(cstmt_ins_spectrum_ro);
				if(conn!=null)dbConn.closeConnection(conn);

			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}


	}
	/*
	 * (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.HDBExtractor#insert_SpectrumData_RW_DataBase(java.lang.StringBuffer, java.lang.StringBuffer, java.lang.StringBuffer, int, java.sql.Timestamp, java.lang.Double[], java.lang.Double[], java.lang.StringBuffer, java.lang.StringBuffer, java.lang.String)
	 */
	@Override
	protected void insert_SpectrumData_RW_DataBase(StringBuffer query,
			StringBuffer tableName, StringBuffer tableFields, int dim_x,
			Timestamp timeSt, Double[] dvalueWrite, Double[] dvalueRead,
			StringBuffer valueWriteStr, StringBuffer valueReadStr,
			String att_name) throws ArchivingException {
		// TODO Auto-generated method stub
		CallableStatement cstmt_ins_spectrum_rw= null;
		Connection conn = null;
		query = new StringBuffer().append("{call ").append(dbConn.getSchema()).append(".ins_sp_2val (?, ?, ?, ?, ?)}");

		try
		{
			
			conn = dbConn.getConnection();
			cstmt_ins_spectrum_rw = conn.prepareCall(query.toString());
			dbConn.setLastStatement(cstmt_ins_spectrum_rw);
			cstmt_ins_spectrum_rw.setString(1 , att_name);
			cstmt_ins_spectrum_rw.setTimestamp(2 , timeSt);
			cstmt_ins_spectrum_rw.setInt(3 , dim_x);
			if (valueReadStr == null)
			{
				cstmt_ins_spectrum_rw.setNull(4, java.sql.Types.CLOB);
			}
			else
			{
				cstmt_ins_spectrum_rw.setString(4 , valueReadStr.toString());
			}
			if (valueWriteStr == null)
			{
				cstmt_ins_spectrum_rw.setNull(5, java.sql.Types.CLOB);
			}
			else
			{
				cstmt_ins_spectrum_rw.setString(5 , valueWriteStr.toString());
			}

			try
			{
				cstmt_ins_spectrum_rw.executeUpdate();
				ConnectionCommands.close(cstmt_ins_spectrum_rw);
				
			}
			catch(SQLException e)
			{
				dbConn.closeConnection(conn);
				throw e;
			}

		}
		catch ( SQLException e )
		{
			String message = "";
			if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE))
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.INSERT_FAILURE;
			String desc = "Failed while executing HDBOracleAttributeInsert.insert_SpectrumData_RO() method..." +
			"\r\n\t\t Query : " + query +
			"\r\n\t\t Param 1 (Attribute name) : " + att_name +
			"\r\n\t\t Param 2 (Timestamp)      : " + timeSt.toString() +
			"\r\n\t\t Param 3 (Dimension)      : " + dim_x +
			"\r\n\t\t Param 4 (Value Read)     : " + valueReadStr.toString() +
			"\r\n\t\t Param 5 (Value Write)    : " + valueWriteStr.toString() +
			"\r\n\t\t Code d'erreur : " + e.getErrorCode() +
			"\r\n\t\t Message : " + e.getMessage() +
			"\r\n\t\t SQL state : " + e.getSQLState() +
			"\r\n\t\t Stack : ";
			e.printStackTrace();

			throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
		}
		finally{
			
			try {
				if(cstmt_ins_spectrum_rw!=null)ConnectionCommands.close(cstmt_ins_spectrum_rw);
				if(conn!=null)dbConn.closeConnection(conn);

			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}
}