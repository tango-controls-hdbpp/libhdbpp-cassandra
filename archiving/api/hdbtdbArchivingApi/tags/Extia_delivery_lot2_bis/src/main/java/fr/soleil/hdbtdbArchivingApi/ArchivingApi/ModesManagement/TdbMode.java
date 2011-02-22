/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.TdbSpec;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;

/**
 * @author AYADI
 * 
 */
public class TdbMode extends GenericMode {

	/**
	 * @param c
	 */
	public TdbMode(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.IMode#
	 * getSelectField(java.lang.String)
	 */
	public String getSelectField(String select_field) {
		// TODO Auto-generated method stub
		return select_field + ", " + ConfigConst.TABS[2] + "."
				+ ConfigConst.TAB_MOD[26] + ", " + ConfigConst.TABS[2] + "."
				+ ConfigConst.TAB_MOD[27];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.IMode#setSpec
	 * (fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode,
	 * java.sql.ResultSet)
	 */
	public void setSpec(Mode mode, ResultSet rset) {
		// TODO Auto-generated method stub
		TdbSpec tdbSpec;
		try {
			tdbSpec = new TdbSpec(rset.getInt(ConfigConst.TAB_MOD[26]), rset
					.getInt(ConfigConst.TAB_MOD[27]));
			mode.setTdbSpec(tdbSpec);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateSelectField(StringBuffer select_field) {
		// TODO Auto-generated method stub
		select_field.append(", ").append(ConfigConst.TABS[2]).append(".")
				.append(ConfigConst.TAB_MOD[26]).append(", ").append(
						ConfigConst.TABS[2]).append(".").append(
						ConfigConst.TAB_MOD[27]);

	}

	public void appendQuery(StringBuffer query, StringBuffer tableName,
			StringBuffer select_field) {
		// TODO Auto-generated method stub
		query
				.append("INSERT INTO ")
				.append(tableName)
				.append(" (")
				.append(select_field)
				.append(")")
				.append(
						" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	}

	public String[] getArchivedAtt() throws ArchivingException {
		return getAttribute(false);
	}

	/***
     * 
     */
	@Override
	protected void setSpecificStatementForInsertMode(
			PreparedStatement preparedStatement,
			AttributeLightMode attributeLightMode) {
		// TODO Auto-generated method stub
		if (attributeLightMode.getMode().getTdbSpec() != null) {
			try {
				preparedStatement.setLong(26, attributeLightMode.getMode()
						.getTdbSpec().getExportPeriod());
				preparedStatement.setLong(27, attributeLightMode.getMode()
						.getTdbSpec().getKeepingPeriod());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
