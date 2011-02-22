/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 *
 */
public class MySqlTdbDataExport extends TdbDataExport {

	/**
	 * @param f
	 */
	public MySqlTdbDataExport(IDBConnection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbDataExport.DataExport#exportToDB_Data(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public void exportToDB_Data(String remoteDir, String fileName,
			String tableName, int writable) throws ArchivingException {
		// TODO Auto-generated method stub
        if ( remoteDir.endsWith("/") || remoteDir.endsWith("\\") )
            exportToDB(tableName , remoteDir + fileName);
        else
            exportToDB(tableName , remoteDir + File.separator + fileName);

	}

	private void exportToDB(String tableName, String fullFileName)  throws ArchivingException
	{
		// TODO Auto-generated method stub
	        Connection conn = null;
			Statement stmt = null;
			fullFileName = fullFileName.replaceAll(new StringBuffer().append(File.separator).append(File.separator).toString() , new StringBuffer().append(File.separator).append(File.separator).append(File.separator).append(File.separator).toString());
			StringBuffer export_query = new StringBuffer().append("LOAD DATA LOCAL INFILE  '").append(fullFileName).append("' INTO TABLE ").append(tableName).append(" FIELDS TERMINATED BY ',\\n' LINES TERMINATED BY '\\n%%%\\n'");
			//LOAD DATA LOCAL INFILE att_00025-20080314-062059.dat INTO TABLE att_00016 FIELDS TERMINATED BY ',\n' LINES TERMINATED BY '\n%%%\n'
			try
			{
				conn = dbConn.getConnection();
				stmt = conn.createStatement();
				dbConn.setLastStatement( stmt);
	            //System.out.println("export_query.toString()/"+export_query.toString());
	            stmt.executeUpdate(export_query.toString());
			}
			catch ( SQLException e )
			{
				e.printStackTrace ();
			    
				String message = "";
				if (e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
				else
					message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

				String reason = GlobalConst.EXPORTING_FILE_EXCEPTION;
				String desc = "Failed while executing MySqlTdbDataExport.exportToDB() method...";
				throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
			}
			finally
			{
				try {
					ConnectionCommands.close(stmt);
					dbConn.closeConnection(conn);
				} catch (SQLException e) {
					dbConn.closeConnection(conn);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}

	@Override
	public void forceDatabaseToImportFile(String tableName)
			throws ArchivingException {
		// TODO Auto-generated method stub
		
	}
    

}
