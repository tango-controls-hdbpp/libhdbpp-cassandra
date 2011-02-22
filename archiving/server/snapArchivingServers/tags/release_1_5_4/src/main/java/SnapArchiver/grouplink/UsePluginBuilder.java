/*  Synchrotron Soleil 
 *  
 *   File          :  TangoGroupForReadingAnyAttributesBuilder.java
 *  
 *   Project       :  javaapi
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  17 janv. 07 
 *  
 *   Revision:                      Author:  
 *   Date:                          State:  
 *  
 *   Log: TangoGroupForReadingAnyAttributesBuilder.java,v 
 *
 */
/*
 * Created on 17 janv. 07
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package SnapArchiver.grouplink;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.Util;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.Target;
import fr.soleil.actiongroup.collectiveaction.components.tangowrapping.target.TargetFactory;
import fr.soleil.actiongroup.collectiveaction.onattributes.UsePlugin;
import fr.soleil.actiongroup.collectiveaction.onattributes.UsePluginImpl;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.PersistencePlugin;
import fr.soleil.actiongroup.collectiveaction.onattributes.plugin.context.PluginContext;
import fr.soleil.snapArchivingApi.SnapshotingApi.persistence.SnapshotPersistenceManager;
import fr.soleil.snapArchivingApi.SnapshotingApi.persistence.context.SnapshotPersistenceContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;

public class UsePluginBuilder {
	private static final String TANGO_SEPARATOR = "/";
	private Map<String, Integer> attributeIds;

	private int snapId;
	private SnapshotPersistenceManager manager;

	public UsePluginBuilder(int _snapId, SnapshotPersistenceManager _manager) {
		this.snapId = _snapId;
		this.manager = _manager;
	}

	// public UsePlugin build ( List <INamedAttribute> attributeList ) throws
	// DevFailed
	public UsePlugin build(ArrayList attributeList) throws DevFailed {
		Iterator it = attributeList.iterator();
		attributeIds = new Hashtable<String, Integer>(attributeList.size());
		Map<String, Collection<String>> deviceToAttributes = new Hashtable<String, Collection<String>>();

		// SORT ATTRIBUTES BY DEVICE
		while (it.hasNext()) {
			// SnapAttributeExtract nextAttribute = (SnapAttributeExtract)
			// it.next ();
			Object nextObject = it.next();
			// System.out.println("UsePluginBuilder/build/nextObject/"+nextObject.getClass().toString());
			SnapAttributeLight nextAttribute = (SnapAttributeLight) nextObject;
			String completeName = nextAttribute.getAttribute_complete_name();
			String[] parsedName = this.parseName(completeName);

			int attributeId = nextAttribute.getAttribute_id();
			attributeIds.put(completeName, attributeId);

			String deviceName = parsedName[0];
			String attributeName = parsedName[1];

			Collection<String> attributesForThisDevice = deviceToAttributes
					.get(deviceName);
			if (attributesForThisDevice == null) {
				attributesForThisDevice = new Vector<String>();
				deviceToAttributes.put(deviceName, attributesForThisDevice);
			}
			attributesForThisDevice.add(attributeName);
		}
		// traceDeviceToAttributes ( deviceToAttributes );

		// BUILD THE PROXIES AND ATTRIBUTES LIST
		Target[] devices = new Target[deviceToAttributes.size()];

		String[][] attributes = new String[deviceToAttributes.size()][];
		Iterator<String> it2 = deviceToAttributes.keySet().iterator();
		int i = 0;

		while (it2.hasNext()) {
			String nextDevice = it2.next();
			try {
				devices[i] = TargetFactory
						.getTarget(new DeviceProxy(nextDevice));

				Collection<String> attributesForThisDevice = deviceToAttributes
						.get(nextDevice);
				Iterator<String> it3 = attributesForThisDevice.iterator();
				attributes[i] = new String[attributesForThisDevice.size()];
				int j = 0;
				while (it3.hasNext()) {
					String nextAttribute = it3.next();
					attributes[i][j] = nextAttribute;
					j++;
				}

				i++;
			} catch (DevFailed e) {
				e.printStackTrace();
				Util.out2.println(e.toString());
				Util.out2.println("Failed to reach the device: " + nextDevice);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		UsePlugin ret = new UsePluginImpl(devices, attributes,
				new PersistencePlugin());

		SnapshotPersistenceContext persistenceContext = new SnapshotPersistenceContext(
				this.snapId, attributeIds);
		persistenceContext.setManager(this.manager);
		PluginContext context = new PluginContext();
		context.setPersistenceContext(persistenceContext);
		ret.setPluginContext(context);

		return ret;
	}

	private void traceDeviceToAttributes(
			Hashtable<String, Collection<String>> deviceToAttributes) {
		Iterator<String> keys = deviceToAttributes.keySet().iterator();

		System.out.println("traceDeviceToAttributes VVVVVVVVVVVVVVVVVVVVVVV");
		while (keys.hasNext()) {
			String nextDevice = keys.next();

			System.out.println("      " + nextDevice + "  VVVVVVVVVVVVVV");
			Collection<String> attributesForThisDevice = deviceToAttributes
					.get(nextDevice);
			Iterator<String> attrs = attributesForThisDevice.iterator();
			while (attrs.hasNext()) {
				String nextAttr = attrs.next();
				System.out.println("              " + nextAttr);
			}
			System.out.println("      " + nextDevice + "  ^^^^^^^^^^^^^^");
		}
		System.out.println("traceDeviceToAttributes ^^^^^^^^^^^^^^^^^^^^^^^^");
	}

	private String[] parseName(String completeName) {
		String[] ret = new String[2];
		StringTokenizer st = new StringTokenizer(completeName, TANGO_SEPARATOR);
		StringBuffer buff = new StringBuffer();

		buff.append(st.nextToken());
		buff.append(TANGO_SEPARATOR);
		buff.append(st.nextToken());
		buff.append(TANGO_SEPARATOR);
		buff.append(st.nextToken());

		ret[0] = buff.toString();
		ret[1] = st.nextToken();
		return ret;
	}

	/**
	 * @return the attributeIds
	 */
	public Map<String, Integer> getAttributeIds() {
		return this.attributeIds;
	}
}
