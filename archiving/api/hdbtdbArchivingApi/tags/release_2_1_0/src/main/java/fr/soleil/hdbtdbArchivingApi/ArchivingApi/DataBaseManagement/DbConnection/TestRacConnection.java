/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 * @author AYADI
 *
 */
public class TestRacConnection {
	static String  DRIVER = "oracle.jdbc.driver.OracleDriver" ;
	/**
	 * 
	 */
	public TestRacConnection() {
		// TODO Auto-generated constructor stub
	}

	/**

     * Loads and registers all JDBC drivers. This is done by the

     * DBConnectionManager, as opposed to the DBConnectionPool,

     * since many pools may share the same driver.

     *

     * @param props The connection pool properties

     */

    public static void loadDrivers() {

      

      // Load the driver

        StringTokenizer st = new StringTokenizer(DRIVER);

        while (st.hasMoreElements()) {

            String driverClassName = st.nextToken().trim();

            try {

                Driver driver = (Driver) 

                    Class.forName(driverClassName).newInstance();

                DriverManager.registerDriver(driver);

                //drivers.addElement(driver);

            }

            catch (Exception e) {

              

            }

        }

    }

 


	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		//connection a une base RAC :
//			String URL = "jdbc:oracle:oci8:@(DESCRIPTION = " +
//					"(ADDRESS = (PROTOCOL = TCP)(HOST = ribambelle-vip.synchrotron-soleil.fr)(PORT = 1528))" +
//					"(ADDRESS = (PROTOCOL = TCP)(HOST=tarentelle-vip.synchrotron-soleil.fr)(PORT = 1528))(LOAD_BALANCE = yes)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = TWIST)(FAILOVER_MODE =(TYPE = SELECT)(METHOD = BASIC)(RETRIES = 180)(DELAY = 5))))";
		String URL = "jdbc:oracle:oci8:@TWIST";
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String user = "hdb";
			String password = "hdb";
	       Connection con = null;
			        try
			        {
			        	//loadDrivers();
			            if (user == null)
			            {
			                con = DriverManager.getConnection(URL);
			            }
			            else
			            {
			                con = DriverManager.getConnection(URL, user, password);
			                
			            }
			            if(con!=null)
		                	System.out.println("Connection succeed:"+  con.getAutoCommit());
			        }
			        catch (SQLException e)
			        {
			        	System.out.println("RAC Connection Failed!!!!");
			            throw new SQLException("Can't create new Connection", e.getMessage());
			        }
			        
			        
	}

}
