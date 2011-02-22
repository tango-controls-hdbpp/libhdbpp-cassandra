package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.TdbAttributeDelete;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TdbAttributeDelete {

	int arch_type;

	public TdbAttributeDelete(int type) {
		// TODO Auto-generated constructor stub
		this.arch_type = type;
	}

	/*
	 * 
	 */
	public void deleteOldRecords(long keepedPeriod, String[] attributeList)
			throws ArchivingException {
		long current_date = System.currentTimeMillis();
		long keeped_date = current_date - keepedPeriod;
		long time = keeped_date;
		Timestamp timeSt = new Timestamp(time);
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if (dbConn == null || dbUtils == null)
			return;

		Connection conn = null;
		PreparedStatement ps_delete = null;
		try {
			conn = dbConn.getConnection();

			for (int i = 0; i < attributeList.length; i++) {
				try {
					String name = attributeList[i];
					String tableName = dbConn.getSchema() + "."
							+ dbUtils.getTableName(name);
					String tableField = ConfigConst.TAB_SCALAR_RO[0];

					String deleteString = "DELETE FROM  " + tableName
							+ " WHERE " + tableField + " <= ?";
					String truncateString = "TRUNCATE TABLE  " + tableName;
					boolean everythingIsOld = false;

					Timestamp lastInsert = dbUtils.getTimeOfLastInsert(name,
							true);

					if (lastInsert != null) {
						everythingIsOld = lastInsert.getTime()
								- timeSt.getTime() < 0;
					}
					System.out.println("DataBaseApi/deleteOldRecords/name|"
							+ name + "|lastInsert|" + lastInsert
							+ "|threshold|" + timeSt + "|everythingIsOld|"
							+ everythingIsOld);

					String query = everythingIsOld ? truncateString
							: deleteString;
					System.out.println("DataBaseApi/deleteOldRecords/query|"
							+ query);

					ps_delete = conn.prepareStatement(query);
					dbConn.setLastStatement(ps_delete);

					if (!everythingIsOld) {
						ps_delete.setTimestamp(1, timeSt);
					}

					ps_delete.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
					System.out
							.println("SQLException received (go to the next element) : "
									+ e);
					continue;
				} catch (ArchivingException e) {
					e.printStackTrace();
					System.out
							.println("ArchivingException received (go to the next element) : "
									+ e);
					continue;
				} catch (Exception e) {
					e.printStackTrace();
					System.out
							.println("Unknown Exception received (go to the next element) : "
									+ e);
					continue;
				}
			}
		} finally {
			try {
				ConnectionCommands.close(ps_delete);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
		}
	}

}
