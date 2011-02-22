package SnapManager;

import junit.framework.TestCase;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;

public class SnapManagerTest extends TestCase {

	public void testSet_equipments() {
		try
		{
//			DeviceProxy deviceProxy = new DeviceProxy("archiving/snap/snapmanager.1");
//			DeviceData device_data = null;

/*			try {
				device_data = new DeviceData();
				// tester une chaine vide ==> Une exception doit-être levée
				device_data.insert(new String[] {});
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
			catch(Exception e){
				System.out.println("1 - ERROR : argin vide " + e);
			}			

			try {
				device_data = new DeviceData();
				// tester un snapid uniquement ==> Une exception doit-être levée
				device_data.insert(new String[] {"12"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
			catch(Exception e){
				System.out.println("2 - ERROR : SnapId only  " + e);
			}	

			try {
				// AUCUNE erreur de remontée dans ce cas là
				device_data = new DeviceData();
				// tester un snapid invalide
				device_data.insert(new String[] {"100","STORED_READ_VALUE"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
			catch(Exception e){
				System.out.println("3 - ERROR : SnapId n'existe pas" + e);
			}	
			
			try {
				device_data = new DeviceData();
				// tester une chaine invalide
				device_data.insert(new String[] {"40","STORED_READ_VALUEXXXX"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
			catch(Exception e){
				System.out.println("4 - ERROR : paramètre stored_read invalide " + e);
			}
			try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib uniquement ==> Une exception doit-être levée
				device_data.insert(new String[] {"42","4"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
			catch(Exception e){
				System.out.println("5 - ERROR : SnapId + Nb Attribut uniquement " + e);
			}			


			try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib + invalid value ==> Une exception doit-être levée
				device_data.insert(new String[] {"42","1","NEW_VALUE","tango/tangotest/spjz_1/long_scalar","chaine" });
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
			catch(Exception e){
				System.out.println("6 - ERROR : SnapId + Nb Attribut + invalid new value " + e);
			}
			
			try {
			device_data = new DeviceData();
			// tester un snapid + nb attrib + read value 
			device_data.insert(new String[] {"42","1","STORED_READ_VALUECCCC","tango/tangotest/spjz_1/long_scalar" });
			deviceProxy.command_inout("SetEquipments" , device_data);
			device_data = null;
		}
		catch(Exception e){
			System.out.println("7 - ERROR : SnapId + Nb Attribut + value stored_read value " + e);
		}	
		
		try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib + read value 
				device_data.insert(new String[] {"42","3","STORED_READ_VALUE","tango/tangotest/spjz_1/long_scalar",
						"NEW_VALUE","tango/tangotest/spjz_1/long_scalar_w",
						"STORED_WRITE_VALUE","tango/tangotest/spjz_1/short_scalar"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
			}
		catch(Exception e){
			System.out.println("8 - ERROR : SnapId + attribute name + STORED_READ_VALUE + NEW_VALUE (no) + STORED_WRITE_VALUE " + e);
		}	

		try {
			device_data = new DeviceData();
			// tester un snapid + nb attrib + read value 
			device_data.insert(new String[] {"42","1","STORED_READ_VALUE","tango/tangotest/spjz_1/string_scalar"});
			deviceProxy.command_inout("SetEquipments" , device_data);
			device_data = null;
		}
		catch(Exception e){
			System.out.println("9 - ERROR : SnapId + attribute doesn't exist " + e);
		}

		try {
			device_data = new DeviceData();
			// tester un snapid + nb attrib + read value 
			device_data.insert(new String[] {"blabla"});
			deviceProxy.command_inout("SetEquipments" , device_data);
			device_data = null;
		}
		catch(Exception e){
			System.out.println("10 - ERROR : invalid SnapId " + e);
		}
*/					
/*			try {
				device_data = new DeviceData();
				// tester un STORED_READ_VALUE
				device_data.insert(new String[] {"42","STORED_READ_VALUE"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				System.out.println("1 - SnapId + STORED_READ_VALUE done");
				device_data = null;
			}
			catch(Exception e){
				System.out.println("SnapId + STORED_READ_VALUE  " + e);
			}		
			
			try {
				device_data = new DeviceData();
				// tester un STORED_WRITE_VALUE
				device_data.insert(new String[] {"42","STORED_WRITE_VALUE"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				System.out.println("2 - SnapId + STORED_WRITE_VALUE done");
				device_data = null;
			}
			catch(Exception e){
				System.out.println("SnapId + STORED_WRITE_VALUE  " + e);
			}		
					
			try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib + value
				device_data.insert(new String[] {"42","1","NEW_VALUE","tango/tangotest/spjz_1/long_scalar","50" });
				deviceProxy.command_inout("SetEquipments" , device_data);
				System.out.println("3 - SnapId + attribute name + NEW_VALUE done");
				device_data = null;
			}
			catch(Exception e){
				System.out.println("SnapId + Nb Attribut + good value " + e);
			}		
			
			try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib + value
				device_data.insert(new String[] {"42","1","STORED_WRITE_VALUE","tango/tangotest/spjz_1/long_scalar" });
				deviceProxy.command_inout("SetEquipments" , device_data);
				device_data = null;
				System.out.println("4 - SnapId + attribute name + STORED_WRITE_VALUE done");
			}
			catch(Exception e){
				System.out.println("SnapId + Nb Attribut + write " + e);
			}	
			
			try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib + read value 
				device_data.insert(new String[] {"42","1","STORED_READ_VALUE","tango/tangotest/spjz_1/long_scalar" });
				deviceProxy.command_inout("SetEquipments" , device_data);
				System.out.println("5 - SnapId + attribute name + STORED_READ_VALUE done");
				device_data = null;
			}
			catch(Exception e){
				System.out.println("SnapId + Nb Attribut + read value " + e);
			}	
			
			try {
				device_data = new DeviceData();
				// tester un snapid + nb attrib + read value 
				device_data.insert(new String[] {"42","3","STORED_READ_VALUE","tango/tangotest/spjz_1/long_scalar",
						"NEW_VALUE","tango/tangotest/spjz_1/long_scalar_w","500",
						"STORED_WRITE_VALUE","tango/tangotest/spjz_1/short_scalar"});
				deviceProxy.command_inout("SetEquipments" , device_data);
				System.out.println("6 - SnapId + attribute name + STORED_READ_VALUE + NEW_VALUE + STORED_WRITE_VALUE done");
				device_data = null;
			}
			catch(Exception e){
				System.out.println("SnapId + Nb Attribut + read value " + e);
			}	
*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
