//+======================================================================
//$Source:  $
//
//Project :     CommonArchivingApi
//
//Description: This class provides common methodes to access to the Tango DB
//
//$Author:  $
//
//$Revision: $
//
//$Log: $
//
//copyleft :Synchrotron SOLEIL
//			L'Orme des Merisiers
//			Saint-Aubin - BP 48
//			91192 GIF-sur-YVETTE CEDEX
//			FRANCE
//
//+============================================================================
package fr.soleil.commonarchivingapi.ArchivingTools.Tools;

import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.ApiUtil;
import fr.esrf.TangoApi.Database;
import fr.esrf.TangoApi.DeviceProxy;

/**
 * This class provides common methodes to access to the Tango DB
 * 
 * @author PIERREJOSEPH
 * 
 */
public class TangoDbSearch {
	public String[] getAttributesForClass(String classe, Vector devOfDomain)
			throws DevFailed {
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
			System.out.println("TangoDbSearch.getAttributesForClass");
			throw devFailed;
		}
	}

	public Vector getDomains() throws DevFailed {
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
			System.out.println("TangoDbSearch.getDomains");
			throw devFailed;
		}
	}

	public Vector getDomains(String domainsRegExp) throws DevFailed {
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
			System.out.println("TangoDbSearch.getDomains for " + domainsRegExp);
			throw devFailed;
		}
	}

	public Hashtable getClassAndDevices(String domain) throws DevFailed {
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
			System.out.println("TangoDbSearch.getClassAndDevices");
			throw devFailed;
		}
	}

	private String[] getFamilies(String domain) throws DevFailed {
		try {
			Database database = ApiUtil.get_db_obj();
			return database.get_device_family(domain + "/*");
		} catch (DevFailed devFailed) {
			System.out.println("TangoDbSearch.getFamilies");
			throw devFailed;
		}
	}

	private String[] getMembers(String domain, String family) throws DevFailed {
		try {
			Database database = ApiUtil.get_db_obj();
			return database.get_device_member(domain + "/" + family + "/*");
		} catch (DevFailed devFailed) {
			System.out.println("TangoDbSearch.getMembers");
			throw devFailed;
		}
	}
}
