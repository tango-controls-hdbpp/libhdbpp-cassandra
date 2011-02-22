/**
 *
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement.DbMode.DbModeFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeExterne;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;

/**
 * @author AYADI
 *
 */
public abstract class GenericMode implements IGenericMode {
    protected int arch_type;

    /**
	 *
	 */
    public GenericMode(int type) {
        // TODO Auto-generated constructor stub
        this.arch_type = type;
    }

    protected abstract String getSelectField(String select_field);

    protected abstract void setSpec(Mode mode, ResultSet rset);

    protected abstract void setSpecificStatementForInsertMode(PreparedStatement preparedStatement,
            AttributeLightMode attributeLightMode);

    protected abstract void updateSelectField(StringBuffer select_field);

    protected abstract void appendQuery(StringBuffer query, StringBuffer tableName,
            StringBuffer select_field);

    /**
     *
     * @return
     * @throws ArchivingException
     */
    public String[] getCurrentArchivedAtt() throws ArchivingException {
        return getAttribute(true);
    }

    /**
     *
     * @param archivingOnly
     * @return
     * @throws ArchivingException
     */
    public String[] getAttribute(boolean archivingOnly) throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        try {
            Vector nameVect = new Vector();
            String[] nameArr = new String[5];

            // First connect with the database
            // if ( dbConn.isAutoConnect() )
            // {
            // dbConn.connect();
            // }

            // Create and execute the SQL query string
            // Build the query string
            String select_field = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2];
            String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
            String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
            String tables = table_1 + ", " + table_2;
            String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL";
            String clause_2 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0] + " = "
                    + ConfigConst.TABS[0] + "." + ConfigConst.TAB_MOD[0];

            String whereClause = archivingOnly ? "(" + clause_1 + " AND " + clause_2 + ")"
                    : clause_2;
            String getAttributeDataQuery;
            if (archivingOnly)
                getAttributeDataQuery = "SELECT " + select_field + " FROM " + tables + " WHERE "
                        + whereClause;
            else
                getAttributeDataQuery = "SELECT " + select_field + " FROM " + table_1;

            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            dbConn.setLastStatement(stmt);
            System.out.println("CLA/DataBaseApi/getCurrentArchivedAtt/query|"
                    + getAttributeDataQuery + "|");
            rset = stmt.executeQuery(getAttributeDataQuery);
            while (rset.next()) {
                String next = rset.getString(1);
                if (next != null) {
                    nameVect.addElement(next);
                }
            }

            nameArr = DbUtils.toStringArray(nameVect);

            // Returns the names list
            return nameArr;
        }
        catch (SQLException e) {
            e.printStackTrace();

            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed while executing DataBaseApi.getCurrentArchivedAtt() method...";
            throw new ArchivingException(message, reason, ErrSeverity.ERR, desc, this.getClass()
                    .getName(), e);
        }
        catch (Throwable t) {
            t.printStackTrace();

            SQLException sqle = new SQLException(t.toString());
            sqle.setStackTrace(t.getStackTrace());

            String message = "";
            if (t.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                    || t.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed2 while executing GenericMode.getCurrentArchivedAtt() method...";
            throw new ArchivingException(message, reason, ErrSeverity.ERR, desc, this.getClass()
                    .getName(), sqle);
        }
        finally {
            ConnectionCommands.close(rset);
            rset = null;
            ConnectionCommands.close(stmt);
            stmt = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
    }

    /**
     * <b>Description : </b> Gets the number of attributes that are being archived in <I>HDB</I>
     *
     * @return An integer which is the number of attributes being archived in <I>HDB</I>
     * @throws ArchivingException
     */
    public int getCurrentArchivedAttCount() throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return 0;

        // todo test
        int activeSimpleSignalCount = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;

        // First connect with the database
        // (if not already done)
        // if ( dbConn.isAutoConnect() )
        // dbConn.connect();

        // Create and execute the SQL query string
        String select_field = "COUNT(*)";
        String table = dbConn.getSchema() + "." + ConfigConst.TABS[2];
        String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[2] + " IS NOT NULL";
        String clause_2 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL";
        String getAttributeDataQuery = "SELECT DISTINCT(" + select_field + ")" + " FROM " + table
                + " WHERE " + "(" + clause_1 + " AND " + clause_2 + ")";

        try {
            conn = dbConn.getConnection();
            stmt = conn.createStatement();
            dbConn.setLastStatement(stmt);
            rset = stmt.executeQuery(getAttributeDataQuery);
            // Gets the result of the query
            if (rset.next())
                activeSimpleSignalCount = rset.getInt(1);
        }
        catch (SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed while executing GenericMode.getCurrentArchivedAttCount() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
                    .getName(), e);
        }
        finally {
            ConnectionCommands.close(rset);
            rset = null;
            ConnectionCommands.close(stmt);
            stmt = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
        // Returns the number of active simple signals defined in HDB
        return activeSimpleSignalCount;
    }

    /**
     * <b>Description : </b> Gets the name of the device in charge of the archiving of the given
     * attribute.
     *
     * @return the name of the device in charge of the archiving of the given attribute.
     * @throws ArchivingException
     */
    public String getDeviceInCharge(String attribut_name) throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return null;

        String deviceInCharge = "";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        // Create and execute the SQL query string
        // Build the query string
        String getDeviceInChargeQuery = "";
        String select_field = "";
        select_field = select_field + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[1];

        String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
        String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
        String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0] + " = "
                + ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[0];
        // String clause_2 = ConfigConst.TABS[ 0 ] + "." + ConfigConst.TAB_DEF[
        // 2 ] + " LIKE " + "?";
        String clause_2 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2] + "=" + "?";
        String clause_3 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL ";

        getDeviceInChargeQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
                + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")" + " AND "
                + "(" + clause_3 + ")" + ") ";

        try {
            // System.out.println (
            // "CLA/GenericMode/getDeviceInCharge/query|"+getDeviceInChargeQuery+"|"
            // );
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(getDeviceInChargeQuery);
            dbConn.setLastStatement(preparedStatement);
            preparedStatement.setString(1, attribut_name.trim());
            rset = preparedStatement.executeQuery();
            while (rset.next()) {
                deviceInCharge = rset.getString(ConfigConst.TAB_MOD[1]);
            }
        }
        catch (SQLException e) {
            String message = GlobalConst.ARCHIVING_ERROR_PREFIX + GlobalConst.EXTRAC_FAILURE;
            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed while executing GenericMode.getDeviceInCharge() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
                    .getName(), e);
        }
        finally {
            ConnectionCommands.close(rset);
            rset = null;
            ConnectionCommands.close(preparedStatement);
            preparedStatement = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
        // Returns the names list
        return deviceInCharge;
    }

    /**
     * <b>Description : </b>
     *
     * @param att_name The name of the attribute
     * @return a boolean : "true" if the attribute of given name is currently archived, "false"
     *         otherwise.
     * @throws ArchivingException
     */
    public boolean isArchived(String att_name) throws ArchivingException {
        return isArchived(att_name, null);
    }

    /**
     * <b>Description : </b>
     *
     * @param att_name The name of the attribute
     * @param device_name The name of the device in charge
     * @return a boolean : "true" if the attribute named att_name is currently archived by the
     *         device named device_name, "false" otherwise.
     * @throws ArchivingException
     */
    public boolean isArchived(String att_name, String device_name) throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return false;

        boolean ret = false;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        // Build the query string
        String isArchivedQuery = "";
        String select_field = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0];
        String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
        String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
        String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0] + " = "
                + ConfigConst.TABS[0] + "." + ConfigConst.TAB_MOD[0];
        String clause_2 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
        String clause_3 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[1] + " = " + "?";
        String clause_4 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL ";

        isArchivedQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
                + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")";

        if (device_name != null)
            isArchivedQuery += " AND " + "(" + clause_3 + ")";

        isArchivedQuery += " AND " + "(" + clause_4 + ")" + ") ";
        conn = dbConn.getConnection();
        if (conn != null) {
            try {
                preparedStatement = conn.prepareStatement(isArchivedQuery);
                dbConn.setLastStatement(preparedStatement);
                preparedStatement.setString(1, att_name.trim());
                if (device_name != null)
                    preparedStatement.setString(2, device_name.trim());
                rset = preparedStatement.executeQuery();
                if (rset.next()) {
                    ret = true;
                }
            }
            catch (SQLException e) {
                String message = "";
                if (e.getMessage().equalsIgnoreCase("Io exception: Broken pipe")
                        || e.getMessage().indexOf("Communication link failure") != -1)
                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.ADB_CONNECTION_FAILURE;
                else
                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.STATEMENT_FAILURE;

                String reason = GlobalConst.QUERY_FAILURE;
                String desc = "Failed while executing GenericMode.isArchived(att_name,device_name) method...";
                throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this
                        .getClass().getName(), e);
            }
            finally {
                ConnectionCommands.close(rset);
                rset = null;
                ConnectionCommands.close(preparedStatement);
                preparedStatement = null;
                dbConn.closeConnection(conn);
                conn = null;
            }
        }
        // Close the connection with the database
        return ret;
    }

    /**
     * <b>Description : </b>
     *
     * @param att_name The name of the attribute
     * @return a String : The name of the corresponding archiver if the attribute is beeing
     *         archived, an empty String otherwise
     * @throws ArchivingException
     */
    public String getArchiverForAttribute(String att_name) throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return null;

        Vector archivVect = new Vector();
        int res = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        // Build the query string
        String isArchivedQuery = "";
        String select_field = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[1];
        String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
        String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
        String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0] + " = "
                + ConfigConst.TABS[0] + "." + ConfigConst.TAB_MOD[0];
        String clause_2 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
        String clause_3 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL ";

        isArchivedQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
                + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")" + " AND "
                + "(" + clause_3 + ")" + ") ";
        try {
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(isArchivedQuery);
            dbConn.setLastStatement(preparedStatement);
            preparedStatement.setString(1, att_name.trim());
            rset = preparedStatement.executeQuery();
            while (rset.next()) {
                archivVect.addElement(rset.getString(1));
                res++;
            }
        }
        catch (SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase("Io exception: Broken pipe")
                    || e.getMessage().indexOf("Communication link failure") != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed while executing GenericMode.getArchiverForAttribute(att_name) method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
                    .getName(), e);
        }
        finally {
            ConnectionCommands.close(rset);
            rset = null;
            ConnectionCommands.close(preparedStatement);
            preparedStatement = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
        // Close the connection with the database
        if (res > 0) {
            return (String) archivVect.firstElement();
        }
        else {
            return "";
        }
    }

    /**
     * <b>Description : </b>
     *
     * @param att_name The name of the attribute
     * @return a String : The name of the corresponding archiver if the attribute is beeing
     *         archived, an empty String otherwise
     * @throws ArchivingException
     */
    public String getArchiverForAttributeEvenIfTheStopDateIsNotNull(String att_name)
            throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return null;

        Vector archivVect = new Vector();
        int res = 0;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        // Build the query string
        String isArchivedQuery = "";
        String select_field = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[1];
        String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
        String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
        String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0] + " = "
                + ConfigConst.TABS[0] + "." + ConfigConst.TAB_MOD[0];
        String clause_2 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
        String clause_3 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL ";

        isArchivedQuery = "SELECT " + select_field + " FROM " + table_1 + ", " + table_2
                + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")" + " AND "
                + "(" + clause_3 + ")" + ") ";
        try {
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(isArchivedQuery);
            dbConn.setLastStatement(preparedStatement);
            preparedStatement.setString(1, att_name.trim());
            rset = preparedStatement.executeQuery();
            while (rset.next()) {
                archivVect.addElement(rset.getString(1));
                res++;
            }
        }
        catch (SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase("Io exception: Broken pipe")
                    || e.getMessage().indexOf("Communication link failure") != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed while executing GenericMode.getArchiverForAttribute(att_name) method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
                    .getName(), e);
        }
        finally {
            ConnectionCommands.close(rset);
            rset = null;
            ConnectionCommands.close(preparedStatement);
            preparedStatement = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
        // Close the connection with the database
        if (res > 0) {
            return (String) archivVect.firstElement();
        }
        else {
            return "";
        }
    }

    /**
     * <b>Description : </b> Gets the current archiving mode for a given attribute name.
     *
     * @return An array of string containing all the current mode's informations for a given
     *         attibute's name
     * @throws ArchivingException
     */
    public Mode getCurrentArchivingMode(String attribut_name) throws ArchivingException {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        boolean isArchived = false;
        Mode mode = new Mode();
        if (dbConn != null) {
            try {

                // Create and execute the SQL query string
                // Build the query string
                String getCurrentArchivingModeQuery = "";
                String select_field = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[4] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[5] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[6] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[7] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[8] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[9] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[10] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[11] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[12] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[13] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[14] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[15] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[16] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[17] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[18] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[19] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[20] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[21] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[22] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[23] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[24] + ", "
                        + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[25];

                select_field = getSelectField(select_field);

                String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
                String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
                String clause_1 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0] + " = "
                        + ConfigConst.TABS[0] + "." + ConfigConst.TAB_MOD[0];
                // String clause_2 = ConfigConst.TABS[ 0 ] + "." +
                // ConfigConst.TAB_DEF[ 2 ] + " LIKE " + "?";
                String clause_2 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2] + " = " + "?";
                String clause_3 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL ";

                getCurrentArchivingModeQuery = "SELECT " + select_field + " FROM " + table_1 + ", "
                        + table_2 + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2
                        + ")" + " AND " + "(" + clause_3 + ")" + ") ";

                // System.out.println (
                // "CLA/GenericMode/getCurrentArchivingMode/query|"+getCurrentArchivingModeQuery+"|"
                // );

                conn = dbConn.getConnection();
                preparedStatement = conn.prepareStatement(getCurrentArchivingModeQuery);
                dbConn.setLastStatement(preparedStatement);
                preparedStatement.setString(1, attribut_name.trim());
                rset = preparedStatement.executeQuery();
                while (rset.next()) {
                    isArchived = true;
                    if (rset.getInt(ConfigConst.TAB_MOD[4]) == 1) {
                        ModePeriode modePeriode = new ModePeriode(rset
                                .getInt(ConfigConst.TAB_MOD[5]));
                        mode.setModeP(modePeriode);
                    }
                    if (rset.getInt(ConfigConst.TAB_MOD[6]) == 1) {
                        ModeAbsolu modeAbsolu = new ModeAbsolu(rset.getInt(ConfigConst.TAB_MOD[7]),
                                rset.getDouble(ConfigConst.TAB_MOD[8]), rset
                                        .getDouble(ConfigConst.TAB_MOD[9]), false);
                        mode.setModeA(modeAbsolu);
                    }
                    else if (rset.getInt(ConfigConst.TAB_MOD[6]) == 2) {
                        ModeAbsolu modeAbsolu = new ModeAbsolu(rset.getInt(ConfigConst.TAB_MOD[7]),
                                rset.getDouble(ConfigConst.TAB_MOD[8]), rset
                                        .getDouble(ConfigConst.TAB_MOD[9]), true);
                        mode.setModeA(modeAbsolu);
                    }
                    if (rset.getInt(ConfigConst.TAB_MOD[10]) == 1) {
                        ModeRelatif modeRelatif = new ModeRelatif(rset
                                .getInt(ConfigConst.TAB_MOD[11]), rset
                                .getDouble(ConfigConst.TAB_MOD[12]), rset
                                .getDouble(ConfigConst.TAB_MOD[13]), false);
                        mode.setModeR(modeRelatif);
                    }
                    else if (rset.getInt(ConfigConst.TAB_MOD[10]) == 2) {
                        ModeRelatif modeRelatif = new ModeRelatif(rset
                                .getInt(ConfigConst.TAB_MOD[11]), rset
                                .getDouble(ConfigConst.TAB_MOD[12]), rset
                                .getDouble(ConfigConst.TAB_MOD[13]), true);
                        mode.setModeR(modeRelatif);
                    }
                    if (rset.getInt(ConfigConst.TAB_MOD[14]) == 1) {
                        ModeSeuil modeSeuil = new ModeSeuil(rset.getInt(ConfigConst.TAB_MOD[15]),
                                rset.getDouble(ConfigConst.TAB_MOD[16]), rset
                                        .getDouble(ConfigConst.TAB_MOD[17]));
                        mode.setModeT(modeSeuil);
                    }
                    if (rset.getInt(ConfigConst.TAB_MOD[18]) == 1) {
                        ModeCalcul modeCalcul = new ModeCalcul(
                                rset.getInt(ConfigConst.TAB_MOD[19]), rset
                                        .getInt(ConfigConst.TAB_MOD[20]), rset
                                        .getInt(ConfigConst.TAB_MOD[21]));
                        mode.setModeC(modeCalcul);
                        // Warning Field 18 is not used yet ...
                    }
                    if (rset.getInt(ConfigConst.TAB_MOD[23]) == 1) {
                        ModeDifference modeDifference = new ModeDifference(rset
                                .getInt(ConfigConst.TAB_MOD[24]));
                        mode.setModeD(modeDifference);
                    }
                    if (rset.getInt(ConfigConst.TAB_MOD[25]) == 1) {
                        ModeExterne modeExterne = new ModeExterne();
                        mode.setModeE(modeExterne);
                    }
                    setSpec(mode, rset);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();

                String message = "";
                if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                        || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.ADB_CONNECTION_FAILURE;
                else
                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.STATEMENT_FAILURE;

                String reason = GlobalConst.QUERY_FAILURE;
                String desc = "Failed while executing GenericMode.getCurrentArchivingMode() method...";
                throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this
                        .getClass().getName(), e);
            }
            catch (Throwable t) {
                t.printStackTrace();

                SQLException sqle = new SQLException(t.toString());
                sqle.setStackTrace(t.getStackTrace());

                String message = "";
                if (t.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                        || t.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.ADB_CONNECTION_FAILURE;
                else
                    message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                            + GlobalConst.STATEMENT_FAILURE;

                String reason = GlobalConst.QUERY_FAILURE;
                String desc = "Failed2 while executing GenericMode.getCurrentArchivingMode() method...";
                throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this
                        .getClass().getName(), sqle);
            }
            finally {
                ConnectionCommands.close(rset);
                rset = null;
                ConnectionCommands.close(preparedStatement);
                preparedStatement = null;
                dbConn.closeConnection(conn);
                conn = null;
            }
        }

        if (isArchived) {
            return mode;
        }
        else {
            mode = null;
            throw new ArchivingException("Invalid attribute: " + attribut_name,
                    "Invalid attribute: " + attribut_name, ErrSeverity.WARN,
                    "No database connection or \"" + attribut_name
                            + "\" attribute not found in database", this.getClass().getName());
        }
    }

    /**
     * This method retrieves a given archiver's current tasks and for a given archiving type.
     *
     * @param archiverName
     * @param historic
     * @return The current task for a given archiver and for a given archiving type.
     * @throws ArchivingException
     */
    public Vector<AttributeLightMode> getArchiverCurrentTasks(String archiverName) throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return null;

        Vector<AttributeLightMode> attributeListConfig = new Vector();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        // Create and execute the SQL query string
        // Build the query string
        String getArchiverCurrentTasksQuery = "";
        // ADT
        String select_field = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[2] + ", " + // fullname
                ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[8] + ", " + // data_type
                ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[9] + ", " + // data_format
                ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[10] + ", "; // writable
        // AMT
        select_field = select_field + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[4] + ", "
                + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[5] + ", " + ConfigConst.TABS[2]
                + "." + ConfigConst.TAB_MOD[6] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[7] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[8] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[9] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[10] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[11] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[12] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[13] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[14] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[15] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[16] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[17] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[18] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[19] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[20] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[21] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[22] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[23] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[24] + ", " + ConfigConst.TABS[2] + "."
                + ConfigConst.TAB_MOD[25];
        select_field = getSelectField(select_field);

        // adt.full_name, adt.data_type, adt.data_format, adt.writable, amt.*
        String table_1 = dbConn.getSchema() + "." + ConfigConst.TABS[0];
        String table_2 = dbConn.getSchema() + "." + ConfigConst.TABS[2];
        String clause_1 = ConfigConst.TABS[0] + "." + ConfigConst.TAB_DEF[0] + " = "
                + ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[0];
        // String clause_2 = ConfigConst.TABS[ 2 ] + "." + ConfigConst.TAB_MOD[
        // 1 ] + " LIKE " + "?";
        String clause_2 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[1] + " = " + "?";
        String clause_3 = ConfigConst.TABS[2] + "." + ConfigConst.TAB_MOD[3] + " IS NULL ";

        getArchiverCurrentTasksQuery = "SELECT " + select_field + " FROM " + table_1 + ", "
                + table_2 + " WHERE (" + "(" + clause_1 + ")" + " AND " + "(" + clause_2 + ")"
                + " AND " + "(" + clause_3 + ")" + ") ";

        try {
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(getArchiverCurrentTasksQuery);
            // System.out.println("/historic/"+historic+"/getArchiverCurrentTasksQuery/"+getArchiverCurrentTasksQuery);
            dbConn.setLastStatement(preparedStatement);
            preparedStatement.setString(1, archiverName.trim());
            rset = preparedStatement.executeQuery();
            while (rset.next()) {
                boolean sd;
                AttributeLightMode attributeLightMode = new AttributeLightMode();

                attributeLightMode.setAttribute_complete_name(rset.getString(1));
                attributeLightMode.setData_type(rset.getInt(2));
                attributeLightMode.setData_format(rset.getInt(3));
                attributeLightMode.setWritable(rset.getInt(4));
                attributeLightMode.setDevice_in_charge(archiverName);
                // Mode
                Mode mode = new Mode();
                if (rset.getInt(ConfigConst.TAB_MOD[4]) != 0) {
                    ModePeriode modePeriode = new ModePeriode(rset.getInt(ConfigConst.TAB_MOD[5]));
                    mode.setModeP(modePeriode);
                }
                if (rset.getInt(ConfigConst.TAB_MOD[6]) != 0) {
                    sd = (rset.getInt(ConfigConst.TAB_MOD[6]) == 2);
                    ModeAbsolu modeAbsolu = new ModeAbsolu(rset.getInt(ConfigConst.TAB_MOD[7]),
                            rset.getDouble(ConfigConst.TAB_MOD[8]), rset
                                    .getDouble(ConfigConst.TAB_MOD[9]), sd);
                    mode.setModeA(modeAbsolu);
                }
                if (rset.getInt(ConfigConst.TAB_MOD[10]) != 0) {
                    sd = (rset.getInt(ConfigConst.TAB_MOD[10]) == 2);
                    ModeRelatif modeRelatif = new ModeRelatif(rset.getInt(ConfigConst.TAB_MOD[11]),
                            rset.getDouble(ConfigConst.TAB_MOD[12]), rset
                                    .getDouble(ConfigConst.TAB_MOD[13]), sd);
                    mode.setModeR(modeRelatif);
                }
                if (rset.getInt(ConfigConst.TAB_MOD[14]) != 0) {
                    ModeSeuil modeSeuil = new ModeSeuil(rset.getInt(ConfigConst.TAB_MOD[15]), rset
                            .getDouble(ConfigConst.TAB_MOD[16]), rset
                            .getDouble(ConfigConst.TAB_MOD[17]));
                    mode.setModeT(modeSeuil);
                }
                if (rset.getInt(ConfigConst.TAB_MOD[18]) != 0) {
                    ModeCalcul modeCalcul = new ModeCalcul(rset.getInt(ConfigConst.TAB_MOD[19]),
                            rset.getInt(ConfigConst.TAB_MOD[20]), rset
                                    .getInt(ConfigConst.TAB_MOD[21]));
                    mode.setModeC(modeCalcul);
                }
                if (rset.getInt(ConfigConst.TAB_MOD[23]) != 0) {
                    ModeDifference modeDifference = new ModeDifference(rset
                            .getInt(ConfigConst.TAB_MOD[24]));
                    mode.setModeD(modeDifference);
                }
                if (rset.getInt(ConfigConst.TAB_MOD[25]) != 0) {
                    ModeExterne modeExterne = new ModeExterne();
                    mode.setModeE(modeExterne);
                }
                setSpec(mode, rset);
                attributeLightMode.setMode(mode);
                attributeListConfig.add(attributeLightMode);
            }
        }
        catch (SQLException e) {
            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.QUERY_FAILURE;
            String desc = "Failed while executing GenericMode.getArchiverCurrentTasks() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
                    .getName(), e);
        }
        finally {
            ConnectionCommands.close(rset);
            rset = null;
            ConnectionCommands.close(preparedStatement);
            preparedStatement = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
        return attributeListConfig;
    }

    /**
     * <b>Description : </b> Inserts a record in the "Attribut Mode Table" <I>(mySQL only)</I>. Each
     * time that the archiving of an attribute is triggered, this table is fielded.
     */
    public void insertModeRecord(AttributeLightMode attributeLightMode) throws ArchivingException {
        IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
        if (dbConn == null)
            return;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        int id = AdtAptAttributesFactory.getInstance(arch_type).getIds().getBufferedAttID(
                attributeLightMode.getAttribute_complete_name());
        StringBuffer tableName = new StringBuffer().append(dbConn.getSchema()).append(".").append(
                ConfigConst.TABS[2]);

        StringBuffer select_field = new StringBuffer().append(ConfigConst.TABS[2]).append(".")
                .append(ConfigConst.TAB_MOD[0]).append(", ").append(ConfigConst.TABS[2])
                .append(".").append(ConfigConst.TAB_MOD[1]).append(", ")
                .append(ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[2])
                .append(", ").append(ConfigConst.TABS[2]).append(".")
                .append(ConfigConst.TAB_MOD[4]).append(", ").append(ConfigConst.TABS[2])
                .append(".").append(ConfigConst.TAB_MOD[5]).append(", ")
                .append(ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[6])
                .append(", ").append(ConfigConst.TABS[2]).append(".")
                .append(ConfigConst.TAB_MOD[7]).append(", ").append(ConfigConst.TABS[2])
                .append(".").append(ConfigConst.TAB_MOD[8]).append(", ")
                .append(ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[9])
                .append(", ").append(ConfigConst.TABS[2]).append(".").append(
                        ConfigConst.TAB_MOD[10]).append(", ").append(ConfigConst.TABS[2]).append(
                        ".").append(ConfigConst.TAB_MOD[11]).append(", ").append(
                        ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[12]).append(
                        ", ").append(ConfigConst.TABS[2]).append(".").append(
                        ConfigConst.TAB_MOD[13]).append(", ").append(ConfigConst.TABS[2]).append(
                        ".").append(ConfigConst.TAB_MOD[14]).append(", ").append(
                        ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[15]).append(
                        ", ").append(ConfigConst.TABS[2]).append(".").append(
                        ConfigConst.TAB_MOD[16]).append(", ").append(ConfigConst.TABS[2]).append(
                        ".").append(ConfigConst.TAB_MOD[17]).append(", ").append(
                        ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[18]).append(
                        ", ").append(ConfigConst.TABS[2]).append(".").append(
                        ConfigConst.TAB_MOD[19]).append(", ").append(ConfigConst.TABS[2]).append(
                        ".").append(ConfigConst.TAB_MOD[20]).append(", ").append(
                        ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[21]).append(
                        ", ").append(ConfigConst.TABS[2]).append(".").append(
                        ConfigConst.TAB_MOD[22]).append(", ").append(ConfigConst.TABS[2]).append(
                        ".").append(ConfigConst.TAB_MOD[23]).append(", ").append(
                        ConfigConst.TABS[2]).append(".").append(ConfigConst.TAB_MOD[24]).append(
                        ", ").append(ConfigConst.TABS[2]).append(".").append(
                        ConfigConst.TAB_MOD[25]);
        updateSelectField(select_field);

        // Create and execute the SQL query string
        // Build the query string
        StringBuffer query = new StringBuffer();
        appendQuery(query, tableName, select_field);
        // System.out.println("GenericMode.insertModeRecord : Query ... \r\n\t"
        // + query);

        try {
            conn = dbConn.getConnection();
            preparedStatement = conn.prepareStatement(query.toString());
            dbConn.setLastStatement(preparedStatement);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, attributeLightMode.getDevice_in_charge());
            // preparedStatement.setTimestamp(3 ,
            // attributeLightMode.getTrigger_time());
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            // the field named "stop_date (3) is not included"

            // Periodical Mode
            if (attributeLightMode.getMode().getModeP() != null) {
                preparedStatement.setInt(4, 1);
                preparedStatement.setInt(5, attributeLightMode.getMode().getModeP().getPeriod());
            }
            else {
                preparedStatement.setInt(4, 0);
                preparedStatement.setInt(5, 0);
            }
            // Absolute Mode
            if (attributeLightMode.getMode().getModeA() != null) {
                if (attributeLightMode.getMode().getModeA().isSlow_drift())
                    preparedStatement.setInt(6, 2);
                else
                    preparedStatement.setInt(6, 1);
                preparedStatement.setInt(7, attributeLightMode.getMode().getModeA().getPeriod());
                preparedStatement.setDouble(8, attributeLightMode.getMode().getModeA().getValInf());
                preparedStatement.setDouble(9, attributeLightMode.getMode().getModeA().getValSup());
            }
            else {
                preparedStatement.setInt(6, 0);
                preparedStatement.setInt(7, 0);
                preparedStatement.setInt(8, 0);
                preparedStatement.setInt(9, 0);
            }
            // Relative Mode
            if (attributeLightMode.getMode().getModeR() != null) {
                if (attributeLightMode.getMode().getModeR().isSlow_drift())
                    preparedStatement.setInt(10, 2);
                else
                    preparedStatement.setInt(10, 1);
                preparedStatement.setInt(11, attributeLightMode.getMode().getModeR().getPeriod());
                preparedStatement.setDouble(12, attributeLightMode.getMode().getModeR()
                        .getPercentInf());
                preparedStatement.setDouble(13, attributeLightMode.getMode().getModeR()
                        .getPercentSup());
            }
            else {
                preparedStatement.setInt(10, 0);
                preparedStatement.setInt(11, 0);
                preparedStatement.setInt(12, 0);
                preparedStatement.setInt(13, 0);
            }
            // Threshold Mode
            if (attributeLightMode.getMode().getModeT() != null) {
                preparedStatement.setInt(14, 1);
                preparedStatement.setInt(15, attributeLightMode.getMode().getModeT().getPeriod());
                preparedStatement.setDouble(16, attributeLightMode.getMode().getModeT()
                        .getThresholdInf());
                preparedStatement.setDouble(17, attributeLightMode.getMode().getModeT()
                        .getThresholdSup());
            }
            else {
                preparedStatement.setInt(14, 0);
                preparedStatement.setInt(15, 0);
                preparedStatement.setInt(16, 0);
                preparedStatement.setInt(17, 0);
            }
            // On Calculation Mode
            if (attributeLightMode.getMode().getModeC() != null) {
                preparedStatement.setInt(18, 1);
                preparedStatement.setInt(19, attributeLightMode.getMode().getModeC().getPeriod());
                preparedStatement.setInt(20, attributeLightMode.getMode().getModeC().getRange());
                preparedStatement.setInt(21, attributeLightMode.getMode().getModeC()
                        .getTypeCalcul());
                preparedStatement.setString(22, "");
            }
            else {
                preparedStatement.setInt(18, 0);
                preparedStatement.setInt(19, 0);
                preparedStatement.setInt(20, 0);
                preparedStatement.setInt(21, 0);
                preparedStatement.setString(22, "");
            }
            // On Difference Mode
            if (attributeLightMode.getMode().getModeD() != null) {
                preparedStatement.setInt(23, 1);
                preparedStatement.setInt(24, attributeLightMode.getMode().getModeD().getPeriod());
            }
            else {
                preparedStatement.setInt(23, 0);
                preparedStatement.setInt(24, 0);
            }
            // On External Mode
            if (attributeLightMode.getMode().getModeE() != null) {
                preparedStatement.setInt(25, 1);
            }
            else {
                preparedStatement.setInt(25, 0);
            }
            // Specif Tdb

            setSpecificStatementForInsertMode(preparedStatement, attributeLightMode);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();

            String message = "";
            if (e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE)
                    || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1)
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
                        + GlobalConst.STATEMENT_FAILURE;

            String reason = GlobalConst.UPDATE_FAILURE;
            String desc = "Failed while executing GenericMode.insertModeRecord() method...";
            throw new ArchivingException(message, reason, ErrSeverity.WARN, desc, this.getClass()
                    .getName(), e);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            ConnectionCommands.close(preparedStatement);
            preparedStatement = null;
            dbConn.closeConnection(conn);
            conn = null;
        }
    }

    /**
     * Method which updates a record in the "Attribute Mode Table" Each time that the archiving of
     * an attribute is stopped, one of this table's row is fielded.
     */

    public void updateModeRecord(String attribute_name) throws ArchivingException {
        DbModeFactory.getInstance(arch_type).updateModeRecord(attribute_name);
    }
}
