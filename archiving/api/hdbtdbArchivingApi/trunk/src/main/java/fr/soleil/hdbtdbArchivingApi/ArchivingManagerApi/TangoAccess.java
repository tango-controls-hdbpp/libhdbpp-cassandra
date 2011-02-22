/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi;

import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.ApiUtil;
import fr.esrf.TangoApi.Database;
import fr.esrf.TangoApi.DeviceProxy;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public class TangoAccess {

	/**
	 * 
	 */
	public TangoAccess() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * /** Get the status of the archiver
	 * 
	 * @param archiverName
	 * 
	 * @return the status of the Archiver driver
	 * 
	 * @throws ArchivingException
	 */
	public static String getDeviceStatus(String archiverName)
			throws ArchivingException {
		try {
			DeviceProxy archiverProxy = new DeviceProxy(archiverName);
			return archiverProxy.status();
		} catch (DevFailed devFailed) {
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
					+ GlobalConst.ARC_UNREACH_EXCEPTION;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getArchiverStatus() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}

	/**
	 * Tests if the given device is alive
	 * 
	 * @param deviceName
	 * @return true if the device is running
	 */
	public static boolean deviceLivingTest(String deviceName) {
		try {
			DeviceProxy deviceProxy = new DeviceProxy(deviceName);
			deviceProxy.ping();
			return true;

			/*
			 * DbDevice device = new DbDevice(deviceName); boolean isExported
			 * =device.get_info ().exported; return isExported;
			 */
		} catch (DevFailed e) {
			return false;
		}
	}

	/**
	 * PART Tango side queries
	 */

	public static Vector getDomains() throws ArchivingException {
		try {
			Database database = ApiUtil.get_db_obj();
			Vector domains = new Vector();
			String[] res_domains = database.get_device_domain("*");
			for (int i = 0; i < res_domains.length; i++) {
				String res_domain = res_domains[i];
				if (!res_domain.equals("dserver"))
					domains.add(res_domain);
			}
			return domains;
		} catch (DevFailed devFailed) {
			System.out.println("ArchivingManagerApi.getDomains");
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getDomains() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}

	public static Vector getDomains(String domainsRegExp)
			throws ArchivingException {
		try {
			Database database = ApiUtil.get_db_obj();
			Vector domains = new Vector();
			String[] res_domains = database
					.get_device_domain(domainsRegExp /* + "*" */);
			for (int i = 0; i < res_domains.length; i++) {
				String res_domain = res_domains[i];
				if (!res_domain.equals("dserver"))
					domains.add(res_domain);
			}
			return domains;
		} catch (DevFailed devFailed) {
			System.out.println("ArchivingManagerApi.getDomains");
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getDomains() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}

	public static String[] getFamilies(String domain) throws ArchivingException {
		try {
			Database database = ApiUtil.get_db_obj();
			return database.get_device_family(domain + "/*");
		} catch (DevFailed devFailed) {
			System.out.println("ArchivingManagerApi.getFamilies");
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getFamilies() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}

	public static String[] getMembers(String domain, String family)
			throws ArchivingException {
		try {
			Database database = ApiUtil.get_db_obj();
			return database.get_device_member(domain + "/" + family + "/*");
		} catch (DevFailed devFailed) {
			System.out.println("ArchivingManagerApi.getMembers");
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getMembers() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}

	/*
	 * 
	 */

	public static String getTangoHost() {
		String tangoHost = "Host:port";
		try {
			Database db = new Database();
			tangoHost = db.get_tango_host();
		} catch (DevFailed devFailed) {
			devFailed.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
		}
		return tangoHost;
	}

	public static Hashtable getClassAndDevices(String domain)
			throws ArchivingException {
		try {
			long innerMostLoop = 0;

			Database database = ApiUtil.get_db_obj();
			Vector devicesCapitalised = new Vector();
			Hashtable deviceClassToDevices = new Hashtable();

			String[] families = getFamilies(domain);
			for (int i = 0; i < families.length; i++) {
				String[] members = getMembers(domain, families[i]);

				for (int j = 0; j < members.length; j++) {
					String new_dev = domain + "/" + families[i] + "/"
							+ members[j];
					devicesCapitalised.add(new_dev.toUpperCase());

					String dServer = (database.get_device_info(new_dev)).server;
					String[] res = database.get_device_class_list(dServer);

					// long beforeInnerMostLoop = System.currentTimeMillis ();
					for (int k = 0; k < res.length / 2; k++) {
						String device = res[2 * k];
						String deviceClass = res[2 * k + 1];
						// System.out.println (
						// "CLA/getClassesList/j|"+j+"|device|"+device+"|deviceClass|"+deviceClass
						// );
						if (devicesCapitalised.indexOf(device.toUpperCase()) == -1) {
							continue;
						}

						Vector devicesForTheCurrentDeviceClass = (Vector) deviceClassToDevices
								.get(deviceClass);
						if (devicesForTheCurrentDeviceClass == null) {
							devicesForTheCurrentDeviceClass = new Vector();
							deviceClassToDevices.put(deviceClass,
									devicesForTheCurrentDeviceClass);
						}

						devicesForTheCurrentDeviceClass.add(device);
					}
					/*
					 * long afterInnerMostLoop = System.currentTimeMillis ();
					 * innerMostLoop += ( afterInnerMostLoop -
					 * beforeInnerMostLoop );
					 */
				}
			}

			// System.out.println (
			// "---------------------------------------/innerMostLoop|"+
			// innerMostLoop );
			return deviceClassToDevices;
		} catch (DevFailed devFailed) {
			System.out.println("ArchivingManagerApi.getClassAndDevices");
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getClassAndDevices() method for domain = "
					+ domain;
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}

	public static String[] getAttributesForClass(String classe,
			Vector devOfDomain) throws ArchivingException {
		String[] exported_dev;
		String[] att_list = new String[5];
		try {
			Database database = ApiUtil.get_db_obj();
			DeviceProxy deviceProxy;
			exported_dev = database.get_device_exported_for_class(classe);
			for (int i = 0; i < exported_dev.length; i++) {
				String current_dev = exported_dev[i];
				if (devOfDomain.contains(current_dev)) {
					try {
						deviceProxy = new DeviceProxy(current_dev);
						att_list = deviceProxy.get_attribute_list();
						break;
					} catch (DevFailed devFailed) {
						System.out.println("Unable to reach " + current_dev);

					}
				}
			}
			return att_list;
		} catch (DevFailed devFailed) {
			System.out.println("ArchivingManagerApi.getAttributesForClass");
			String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing ArchivingManagerApi.getAttributesForClass() method...";
			throw new ArchivingException(message, reason, ErrSeverity.WARN,
					desc, "", devFailed);
		}
	}
	// private static Vector getDServersList(Vector devices) throws
	// ArchivingException
	// {
	// try
	// {
	// Database database = ApiUtil.get_db_obj();
	//
	// Vector dservers = new Vector();
	// for ( int i = 0 ; i < devices.size() ; i++ )
	// {
	// String dev = ( String ) devices.elementAt(i);
	// dservers.add(( database.get_device_info(dev) ).server);
	// }
	//
	// return dservers;
	// }
	// catch ( DevFailed devFailed )
	// {
	// System.out.println("ArchivingManagerApi.getDServersList");
	// String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
	// String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	// String desc =
	// "Failed while executing ArchivingManagerApi.getDServersList() method...";
	// throw new ArchivingException(message , reason , ErrSeverity.WARN , desc ,
	// "" , devFailed);
	// }
	// }
	//
	// /**
	// * @param dservers
	// * @param devices
	// * @return
	// * @throws ArchivingException
	// */
	// private static Hashtable arrangeDevByClass(Vector dservers, Vector
	// devices) throws ArchivingException
	// {
	// try
	// {
	// Enumeration enumDevices = devices.elements ( );
	// Vector devicesCapitalised = new Vector ( devices.size () );
	// while (enumDevices.hasMoreElements () )
	// {
	// String nextDevice = (String) enumDevices.nextElement ();
	// //System.out.println ( "CLA/getClassesList/nextDevice|"+nextDevice+"|" );
	// devicesCapitalised.add ( nextDevice.toUpperCase () );
	// }
	//
	// Database database = ApiUtil.get_db_obj();
	// //Vector classes = new Vector();
	//
	// Hashtable deviceToDeviceClass = new Hashtable ();
	// Hashtable deviceClassToDevices = new Hashtable ();
	//
	// for ( int i = 0 ; i < dservers.size() ; i++ )
	// {
	// String ds = ( String ) dservers.elementAt(i);
	// String[] res = database.get_device_class_list(ds);
	//
	// for ( int j = 0 ; j < res.length / 2 ; j++ )
	// {
	// String device = res[ 2*j ];
	// String deviceClass = res[ 2*j + 1 ];
	// //System.out.println (
	// "CLA/getClassesList/j|"+j+"|device|"+device+"|deviceClass|"+deviceClass
	// );
	//
	// String deviceCapitalised = device.toUpperCase ();
	// if ( devicesCapitalised.indexOf ( deviceCapitalised ) == -1 )
	// {
	// //System.out.println ( "		INCONNU|"+device+"|" );
	// continue;
	// }
	// deviceToDeviceClass.put ( device , deviceClass );
	//
	// boolean testContainsIgnoreCase = false;
	// Enumeration en = deviceClassToDevices.keys ();
	// while ( en.hasMoreElements () )
	// {
	// String nextKey = (String) en.nextElement ();
	// if ( nextKey.equalsIgnoreCase ( deviceClass ) )
	// {
	// testContainsIgnoreCase = true;
	// break;
	// }
	// }
	//
	// if ( ! testContainsIgnoreCase )
	// {
	// deviceClassToDevices.put(deviceClass , new Vector());
	// }
	//
	// Vector devicesForTheCurrentDeviceClass = ( Vector )
	// deviceClassToDevices.get(deviceClass);
	// if ( devicesForTheCurrentDeviceClass != null )
	// {
	// devicesForTheCurrentDeviceClass.add ( device );
	// }
	// }
	// }
	//
	// return deviceClassToDevices;
	// }
	// catch ( DevFailed devFailed )
	// {
	// System.out.println("ArchivingManagerApi.getClassesList");
	// String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
	// String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	// String desc =
	// "Failed while executing ArchivingManagerApi.getClassesList() method...";
	// throw new ArchivingException(message , reason , ErrSeverity.WARN , desc ,
	// "" , devFailed);
	// }
	// }
	// private static Vector buildDevicesList(String domain) throws
	// ArchivingException
	// {
	// try
	// {
	// Vector devList = new Vector();
	// String[] families = TangoAccess.getFamilies(domain);
	// for ( int i = 0 ; i < families.length ; i++ )
	// {
	// String[] members = TangoAccess.getMembers(domain , families[ i ]);
	// for ( int j = 0 ; j < members.length ; j++ )
	// {
	// String new_dev = domain + "/" + families[ i ] + "/" + members[ j ];
	// devList.add(new_dev);
	// }
	// }
	// return devList;
	// }
	// catch ( ArchivingException e )
	// {
	// System.out.println("ArchivingManagerApi.buildDevicesList");
	// String message = GlobalConst.ARCHIVING_ERROR_PREFIX;
	// String reason = GlobalConst.TANGO_COMM_EXCEPTION;
	// String desc =
	// "Failed while executing ArchivingManagerApi.buildDevicesList() method...";
	// throw new ArchivingException(message , reason , ErrSeverity.WARN , desc ,
	// "" , e);
	// }
	// }

}
