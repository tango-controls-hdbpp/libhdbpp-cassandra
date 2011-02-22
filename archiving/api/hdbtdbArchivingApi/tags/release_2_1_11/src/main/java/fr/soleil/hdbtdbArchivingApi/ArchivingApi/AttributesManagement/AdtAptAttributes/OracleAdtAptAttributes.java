package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeHeavy;

public class OracleAdtAptAttributes extends AdtAptAttributes {

	public OracleAdtAptAttributes(int type) {
		// TODO Auto-generated constructor stub
		super(type);
	}

	@Override
	protected String getRequest(String att_name) {
		IDBConnection dbConn;
		try {
			dbConn = ConnectionFactory.getInstance(arch_type);
			if (dbConn == null)
				return null;

			// TODO Auto-generated method stub

			String str = "SELECT ";
			for (int i = 0; i < ConfigConst.TAB_DEF.length - 1; i++) {
				str = str + "to_char(" + ConfigConst.TAB_DEF[i] + ")" + ", ";
			}
			str = str + "to_char("
					+ ConfigConst.TAB_DEF[ConfigConst.TAB_DEF.length - 1] + ")";
			str = str + " FROM " + dbConn.getSchema() + "."
					+ ConfigConst.TABS[0] + " WHERE " + ConfigConst.TAB_DEF[2]
					+ "='" + att_name.trim() + "'";

		} catch (ArchivingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * This method registers a given attribute into the hdb database It inserts
	 * a record in the "Attribute Definition Table" <I>(mySQL only)</I> This
	 * methos does not take care of id parameter of the given attribute as this
	 * parameter is managed in the database side (autoincrement).
	 *
	 * @param attributeHeavy
	 *            the attribute to register
	 * @throws ArchivingException
	 */

	@Override
	public void registerAttribute(AttributeHeavy attributeHeavy)
			throws ArchivingException {
		// TODO Auto-generated method stub
		IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		if (dbConn == null)
			return;

		Connection conn = null;

		CallableStatement cstmt_register_in_hdb = null;
		String myStatement = "{call "
				+ dbConn.getSchema()
				+ ".crtab (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		try {
			// slim
			conn = dbConn.getConnection();
			cstmt_register_in_hdb = conn.prepareCall(myStatement);
			dbConn.setLastStatement(cstmt_register_in_hdb);
			cstmt_register_in_hdb.setTimestamp(1, attributeHeavy
					.getRegistration_time());
			cstmt_register_in_hdb.setString(2, attributeHeavy
					.getAttribute_complete_name());
			cstmt_register_in_hdb.setString(3, attributeHeavy
					.getAttribute_device_name());
			cstmt_register_in_hdb.setString(4, attributeHeavy.getDomain());

			cstmt_register_in_hdb.setString(5, attributeHeavy.getFamily());
			cstmt_register_in_hdb.setString(6, attributeHeavy.getMember());
			cstmt_register_in_hdb.setString(7, attributeHeavy
					.getAttribute_name());

			cstmt_register_in_hdb.setInt(8, attributeHeavy.getData_type());
			cstmt_register_in_hdb.setInt(9, attributeHeavy.getData_format());
			cstmt_register_in_hdb.setInt(10, attributeHeavy.getWritable());
			// System.out.println (
			// "CLA/cstmt_register_in_hdb/attributeHeavy.getWritable()/"+attributeHeavy.getWritable()
			// );

			cstmt_register_in_hdb.setInt(11, attributeHeavy.getMax_dim_x());
			cstmt_register_in_hdb.setInt(12, attributeHeavy.getMax_dim_y());

			cstmt_register_in_hdb.setInt(13, attributeHeavy.getLevel());
			cstmt_register_in_hdb.setString(14, attributeHeavy.getCtrl_sys());
			cstmt_register_in_hdb.setInt(15, attributeHeavy.getArchivable());
			cstmt_register_in_hdb.setInt(16, attributeHeavy.getSubstitute());

			cstmt_register_in_hdb
					.setString(17, attributeHeavy.getDescription());
			cstmt_register_in_hdb.setString(18, attributeHeavy.getLabel());
			cstmt_register_in_hdb.setString(19, attributeHeavy.getUnit());
			cstmt_register_in_hdb.setString(20, attributeHeavy
					.getStandard_unit());

			cstmt_register_in_hdb.setString(21, attributeHeavy
					.getDisplay_unit());
			cstmt_register_in_hdb.setString(22, attributeHeavy.getFormat());
			cstmt_register_in_hdb.setString(23, attributeHeavy.getMin_value());
			cstmt_register_in_hdb.setString(24, attributeHeavy.getMax_value());
			cstmt_register_in_hdb.setString(25, attributeHeavy.getMin_alarm());
			cstmt_register_in_hdb.setString(26, attributeHeavy.getMax_alarm());
			cstmt_register_in_hdb.setString(27, "");

			cstmt_register_in_hdb.executeUpdate();
			if (!dbConn.getAutoCommit()) {
				ConnectionCommands.commit(conn, DbUtilsFactory
						.getInstance(arch_type));
			}
		} catch (SQLException e) {
			ConnectionCommands.rollback(conn, DbUtilsFactory
					.getInstance(arch_type));
			String message = "";
			if (e.getMessage()
					.equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
					|| e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.ADB_CONNECTION_FAILURE;
			else
				message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
						+ GlobalConst.STATEMENT_FAILURE;

			String reason = GlobalConst.QUERY_FAILURE;
			String desc = "Failed while executing OracleAdtAptAttributes.registerAttribute() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, this.getClass().getName(), e);

		} catch (ArchivingException ae) {
			throw ae;
		} finally {
			ConnectionCommands.close(cstmt_register_in_hdb);
			cstmt_register_in_hdb = null;
			dbConn.closeConnection(conn);
			conn = null;
//			try {
//				if (cstmt_register_in_hdb != null)
//					ConnectionCommands.close(cstmt_register_in_hdb);
//				if (conn != null)
//			} catch (SQLException e) {
//				dbConn.closeConnection(conn);
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}

	@Override
	public void CreateAttributeTableIfNotExist(String attributeName,
			AttributeHeavy attr) throws ArchivingException {
		// TODO Auto-generated method stub

	}

}
