package SnapArchiver;

import junit.framework.TestCase;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;

public class SnapArchiverTest extends TestCase {

	private short testTriggerLaunchSnap(DeviceProxy deviceProxy, short ctx) {
		DeviceData device_data = null;
		DeviceData device_data_out = null;
		short snapId = -1;
		try {
			device_data = new DeviceData();
			device_data.insert(ctx);
			device_data_out = deviceProxy.command_inout(
					"TriggerLaunchSnapShot", device_data);
			snapId = device_data_out.extractShort();
			device_data = null;
		} catch (DevFailed e) {
			StringBuffer buffer = new StringBuffer(
					"ERROR DevFailed : argin ctx = " + ctx + "\n");
			for (int i = 0; i < e.errors.length; i++) {
				buffer.append("Error Level " + i + ":\n");
				buffer.append("\t - desc: " + e.errors[i].desc.toString()
						+ "\n");
				buffer.append("\t - origin: " + e.errors[i].origin.toString()
						+ "\n");
				buffer.append("\t - reason: " + e.errors[i].reason.toString()
						+ "\n");

				String sev = "";
				if (e.errors[i].severity.value() == ErrSeverity.ERR.value()) {
					sev = "ERROR";
				} else if (e.errors[i].severity.value() == ErrSeverity.PANIC
						.value()) {
					sev = "PANIC";
				} else if (e.errors[i].severity.value() == ErrSeverity.WARN
						.value()) {
					sev = "WARN";
				}
				buffer.append("\t - severity: " + sev + "\n");
			}

			System.out.println(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unknown Exception argin ctx = " + ctx);
		}
		return snapId;
	}

	public void testTriggerLaunchSnapShot() {
		try {
			DeviceProxy deviceProxy = new DeviceProxy(
					"archiving/snap/snaparchiver.1");
			short snapId;
			short ctx = -1;
			// Un exception doit être levée
			snapId = testTriggerLaunchSnap(deviceProxy, ctx);
			System.out.println("1 - ctx = " + ctx + " snapId = " + snapId);

			ctx = 20; // contexte existant
			snapId = testTriggerLaunchSnap(deviceProxy, ctx);
			System.out.println("2 - ctx = " + ctx + " snapId = " + snapId);

			ctx = 100; // contexte inexistant mais valide
			snapId = testTriggerLaunchSnap(deviceProxy, ctx);
			System.out.println("3 - ctx = " + ctx + " snapId = " + snapId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
