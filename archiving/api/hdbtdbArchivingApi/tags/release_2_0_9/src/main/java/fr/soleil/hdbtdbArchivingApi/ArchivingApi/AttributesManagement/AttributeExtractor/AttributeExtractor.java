/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.Vector;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ImageData;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.AdtAptAttributesFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.IAdtAptAttributes;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.DataGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.InfSupGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.MinMaxAvgGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.DataGettersBetweenDates;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.InfSupGettersBetweenDates;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGettersBetweenDates.MinMaxAvgGettersBetweenDates;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands.ConnectionCommands;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.ConnectionFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.DbUtilsFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

/**
 * @author AYADI
 *
 */
public  class AttributeExtractor extends DataExtractor {
	protected DataGetters dataGetters;
	protected DataGettersBetweenDates dataGettersBetweenDates;
	protected InfSupGetters infSupGetters;
	protected InfSupGettersBetweenDates infSupGettersBetweenDates;
	protected MinMaxAvgGetters minMaxAvgGetters;
	protected MinMaxAvgGettersBetweenDates minMaxAvgGettersBetweenDates; 

	/**
	 * @param con
	 * @param ut
	 * @param at
	 */
	public AttributeExtractor(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}
	
		

	   /**
     * <b>Description : </b>        Retrieves timestamps associated with data records data beetwen two dates, for a given scalar attribute.
     *
     * @param argin The attribute's name, the beginning date  (DD-MM-YYYY HH24:MI:SS.FF) and the ending date (DD-MM-YYYY HH24:MI:SS.FF).
     * @param samplingType 
     * @return The list of timestamps for the specified attribute <br>
     * @throws ArchivingException
     */
    public Vector getAttPartialImageDataBetweenDates(String[] argin ,  SamplingType samplingType) throws ArchivingException
    {
    	IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IAdtAptAttributes att = AdtAptAttributesFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || att==null || dbUtils==null)
			return null;

        String att_name = argin[ 0 ].trim();
        
        String time_0 = dbUtils.getTime(argin[ 1 ]);
        String time_1 = dbUtils.getTime(argin[ 2 ]);

        String message = "" , reason = "" , desc = "";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        // Create and execute the SQL query string
        // Build the query string
        String query = "";
        String fields = "";
        String whereClause = "";
        String groupByClause = "";
        String orderByClause = "";
        String tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);

        if ( samplingType.hasSampling () )
        {            
            
            String format = dbUtils.getFormat(samplingType);//isMySQL ? samplingType.getMySqlFormat () : samplingType.getOracleFormat ();
            
            String selectField_0 = dbUtils.toDbTimeFieldString( ConfigConst.TAB_IMAGE_RO[ 0 ] , format );
            String selectField_1 = SamplingType.AVERAGING_NORMALISATION + " ( " + ConfigConst.TAB_IMAGE_RO[ 1 ]  + " ) "; 
            String selectField_2 = SamplingType.AVERAGING_NORMALISATION + " ( " + ConfigConst.TAB_IMAGE_RO[ 2 ]  + " ) ";
            fields = selectField_0 + ", " + selectField_1 + ", " + selectField_2;
            
            whereClause = ConfigConst.TAB_IMAGE_RO[ 0 ] + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
            groupByClause = " GROUP BY " +dbUtils.toDbTimeFieldString(ConfigConst.TAB_IMAGE_RO[ 0 ], format);
            orderByClause = " ORDER BY " +dbUtils.toDbTimeFieldString(ConfigConst.TAB_IMAGE_RO[ 0 ], format);
            
            query = "SELECT " + fields + " FROM " + tableName + " WHERE " + whereClause + groupByClause + orderByClause;;            
        }
        else
        {
            fields = ( dbUtils.toDbTimeFieldString(ConfigConst.TAB_IMAGE_RO[ 0 ]) + ", " + ConfigConst.TAB_IMAGE_RO[ 1 ] + ", " + ConfigConst.TAB_IMAGE_RO[ 2 ] );
            whereClause = ConfigConst.TAB_IMAGE_RO[ 0 ] + " BETWEEN " + dbUtils.toDbTimeString(time_0.trim()) + " AND " + dbUtils.toDbTimeString(time_1.trim());
            query = "SELECT " + fields + " FROM " + tableName + " WHERE " + "(" + whereClause + ")";    
        }
        
        //System.out.println ( "CLA/getAttPartialImageDataBetweenDates/query/"+query+"/" );
        
        try
        {
        	conn = dbConn.getConnection();
            stmt = conn.createStatement();
            dbConn.setLastStatement(stmt);
            
            rset = stmt.executeQuery(query);
            //System.out.println ( "CLA/getAttPartialImageDataBetweenDates/AFTER/"+new Timestamp (System.currentTimeMillis()) );
            Vector ret = new Vector ();
            
            while ( rset.next() )
            {
                String date = rset.getString (1);
                Timestamp _date = new Timestamp ( DateUtil.stringToMilli ( date ) );
                int dimX = rset.getInt ( 2 );
                int dimY = rset.getInt ( 3 );
                
                ImageData data = new ImageData (this);
                data.setDate ( _date );
                data.setDimX ( dimX );
                data.setDimY ( dimY );
                data.setName ( att_name );
                data.setHistoric(DbUtils.getHdbTdbType(dbConn.getDbType())== ConfigConst.HDB);
                
                ret.add ( data );
                //System.out.println ( "CLA/getAttPartialImageDataBetweenDates/next/date|"+date+"|" );
            }
            
            return ret;
        }
        catch ( SQLException e )
        {
            message = "";
            if ( e.getMessage().equalsIgnoreCase(GlobalConst.COMM_FAILURE_ORACLE) || e.getMessage().indexOf(GlobalConst.COMM_FAILURE_MYSQL) != -1 )
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.ADB_CONNECTION_FAILURE;
            else
                message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + GlobalConst.STATEMENT_FAILURE;

            reason = GlobalConst.QUERY_FAILURE;
            desc = "Failed while executing AttributeExtractor.getAttDataDatesBetweenDates() method...";
            throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
        }
        finally
        {
        	try {
        		ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public double [][] getAttImageDataForDate(String att_name , String time ) throws ArchivingException
    {
    	IDBConnection dbConn = ConnectionFactory.getInstance(arch_type);
		IDbUtils dbUtils = DbUtilsFactory.getInstance(arch_type);
		if(dbConn == null || dbUtils==null)
			return null;

        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        // Create and execute the SQL query string
        // Build the query string
        String query = "";
        String selectField_0 = "";
        String selectFields = "";
        String dateClause = "";
        String tableName = "";
        tableName = dbConn.getSchema() + "." + dbUtils.getTableName(att_name);
        
        selectField_0 = ConfigConst.TAB_IMAGE_RO[ 3 ];
        String dateField = ConfigConst.TAB_IMAGE_RO [ 0 ];
        selectFields = selectField_0;
        
        dateClause = dateField + " = " + "'" + time.trim() + "'";
        query = "SELECT " + selectFields + " FROM " + tableName + " WHERE " + "(" + dateClause + ")" + " ORDER BY time";
        //System.out.println ( "CLA/AttributeExtractor/getAttImageDataForDate/query/"+query+"/" );
        
        double [][]  dvalueArr  = null;

        try
        {
        	conn = dbConn.getConnection();
            stmt = conn.createStatement();
            dbConn.setLastStatement(stmt);
            rset = stmt.executeQuery(query);

            if ( !rset.next() )
            {
                return null;
            }

            // Value
            String valueReadSt = null;
            java.util.StringTokenizer stringTokenizerReadRows = null;
            if ( rset.getObject(1) != null )
            {
                valueReadSt = rset.getString(1);
                if (rset.wasNull())
                {
                    valueReadSt = "null";
                }
                stringTokenizerReadRows = new StringTokenizer(valueReadSt , GlobalConst.CLOB_SEPARATOR_IMAGE_ROWS);
            }

            int rowIndex = 0;
            int numberOfRows = 0;
            int numberOfCols = 0;

            if (stringTokenizerReadRows != null)
            {
                numberOfRows = stringTokenizerReadRows.countTokens (); 
                dvalueArr = new double [numberOfRows][];

                while ( stringTokenizerReadRows.hasMoreTokens() )
                {
                    String currentRowRead = stringTokenizerReadRows.nextToken();
                    if ( currentRowRead == null || currentRowRead.trim().equals ( "" ) )
                    {
                        break;
                    }
                
                    StringTokenizer stringTokenizerReadCols = new StringTokenizer(currentRowRead , GlobalConst.CLOB_SEPARATOR_IMAGE_COLS);
                    numberOfCols = stringTokenizerReadCols.countTokens ();
                    double [] currentRow = new double [ numberOfCols ]; 
                    int colIndex = 0;
                
                    while ( stringTokenizerReadCols.hasMoreTokens() )
                    {
                        String currentValRead = stringTokenizerReadCols.nextToken();
                        if ( currentValRead == null )
                        {
                            break;
                        }
                        currentValRead = currentValRead.trim ();
                        if ( currentValRead.equals ( "" ) )
                        {
                            break;
                        }
                        
                        currentRow [ colIndex ] = Double.parseDouble(currentValRead);
                        colIndex++;
                    }
                    
                    dvalueArr [ rowIndex ] = currentRow;
                    rowIndex++;
                }
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
            String desc = "Failed while executing AttributeExtractor.getAttSpectrumDataBetweenDatesMySql() method...";
            throw new ArchivingException(message , reason , ErrSeverity.WARN , desc , this.getClass().getName() , e);
        }
        finally
        {
        	try {
        		ConnectionCommands.close(rset);
				ConnectionCommands.close(stmt);
				dbConn.closeConnection(conn);
			} catch (SQLException e) {
				dbConn.closeConnection(conn);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return dvalueArr;
    }

	public DataGetters getDataGetters() {
		return dataGetters;
	}

	public DataGettersBetweenDates getDataGettersBetweenDates() {
		return dataGettersBetweenDates;
	}

	public InfSupGetters getInfSupGetters() {
		return infSupGetters;
	}

	public InfSupGettersBetweenDates getInfSupGettersBetweenDates() {
		return infSupGettersBetweenDates;
	}

	public MinMaxAvgGetters getMinMaxAvgGetters() {
		return minMaxAvgGetters;
	}

	public MinMaxAvgGettersBetweenDates getMinMaxAvgGettersBetweenDates() {
		return minMaxAvgGettersBetweenDates;
	}
}
