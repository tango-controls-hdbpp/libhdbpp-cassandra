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
    public TdbMode(final int type) {
	super(type);
	// TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @seefr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.IMode#
     * getSelectField(java.lang.String)
     */
    @Override
    public String getSelectField(final String select_field) {
	// TODO Auto-generated method stub
	return select_field + ", " + ConfigConst.AMT + "." + ConfigConst.tdbExportPeriod + ", "
		+ ConfigConst.AMT + "." + ConfigConst.tdbKeepingPeriod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.IMode#setSpec
     * (fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode,
     * java.sql.ResultSet)
     */
    @Override
    public void setSpec(final Mode mode, final ResultSet rset) {
	TdbSpec tdbSpec;
	try {
	    tdbSpec = new TdbSpec(rset.getInt(ConfigConst.tdbExportPeriod), rset
		    .getInt(ConfigConst.tdbKeepingPeriod));
	    mode.setTdbSpec(tdbSpec);
	} catch (final SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    @Override
    public void updateSelectField(final StringBuffer select_field) {
	select_field.append(", ").append(ConfigConst.AMT).append(".").append(
		ConfigConst.tdbExportPeriod).append(", ").append(ConfigConst.AMT).append(".")
		.append(ConfigConst.tdbKeepingPeriod);

    }

    @Override
    public void appendQuery(final StringBuffer query, final StringBuffer tableName,
	    final StringBuffer select_field) {
	query.append("INSERT INTO ").append(tableName).append(" (").append(select_field)
		.append(")").append(
			" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    }

    public String[] getArchivedAtt() throws ArchivingException {
	return getAttribute(false);
    }

    /***
     * 
     */
    @Override
    protected void setSpecificStatementForInsertMode(final PreparedStatement preparedStatement,
	    final AttributeLightMode attributeLightMode) {
	// TODO Auto-generated method stub
	if (attributeLightMode.getMode().getTdbSpec() != null) {
	    try {
		preparedStatement.setLong(26, attributeLightMode.getMode().getTdbSpec()
			.getExportPeriod());
		preparedStatement.setLong(27, attributeLightMode.getMode().getTdbSpec()
			.getKeepingPeriod());
	    } catch (final SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}

    }

}
