/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;

/**
 * @author AYADI
 * 
 */
public class HdbMode extends GenericMode {

	/**
	 * @param c
	 */
	public HdbMode(int type) {
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
		return select_field;
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

	}

	public void updateSelectField(StringBuffer select_field) {
		// TODO Auto-generated method stub

	}

	public void appendQuery(StringBuffer query, StringBuffer tableName,
			StringBuffer select_field) {
		// TODO Auto-generated method stub
		query.append("INSERT INTO ").append(tableName).append(" (").append(
				select_field).append(")").append(
				" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	}

	@Override
	protected void setSpecificStatementForInsertMode(
			PreparedStatement preparedStatement,
			AttributeLightMode attributeLightMode) {
		// TODO Auto-generated method stub

	}

}
