//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/manager/SnapshotXMLHelper.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ArchivingConfigurationXMLHelper.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.4 $
//
//$Log: SnapshotXMLHelper.java,v $
//Revision 1.4  2007/02/28 11:18:18  ounsy
//better snapshot file management
//
//Revision 1.3  2006/11/29 10:01:57  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:38:34  ounsy
//added methods necessary for the new Word-like file management
//
//Revision 1.1  2005/12/14 14:07:18  ounsy
//first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
//under "bensikin.bensikin" and removing the same from their former locations
//
//Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.snapshot.manager;

import java.io.File;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotAttribute;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeDeltaValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeReadValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeWriteValue;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributes;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLUtils;

/**
 * A class used to delegate the loading of a snapshot file into a Snapshot
 * object
 * 
 * @author CLAISSE
 */
public class SnapshotXMLHelper {

	/**
	 * Loads a Snapshot given its file location.
	 * 
	 * @param location
	 *            The complete path to the snapshot file to load
	 * @return The Snapshot object built from its XML representation
	 * @throws Exception
	 */
	public static Snapshot loadSnapshotIntoHash(String location)
			throws Exception {
		File file = new File(location);
		Node rootNode = XMLUtils.getRootNode(file);

		Snapshot ret = loadSnapshotIntoHashFromRoot(rootNode);

		return ret;
	}

	/**
	 * Loads a Snapshot given its XML root node.
	 * 
	 * @param rootNode
	 *            The XML root node
	 * @return The loaded Snapshot
	 * @throws Exception
	 */
	private static Snapshot loadSnapshotIntoHashFromRoot(Node rootNode)
			throws Exception {
		Snapshot viewConf = loadSnapshotData(rootNode);

		if (rootNode.hasChildNodes()) {
			NodeList attributesNodes = rootNode.getChildNodes();
			SnapshotAttributes viewConfAttr = new SnapshotAttributes(viewConf);
			Vector listOfAttributesToAdd = new Vector();

			for (int i = 0; i < attributesNodes.getLength(); i++)
			// as many loops as there are attributes in the VC
			{
				Node currentAttributeNode = attributesNodes.item(i);
				if (XMLUtils.isAFakeNode(currentAttributeNode)) {
					continue;
				}

				String currentAttributeType = currentAttributeNode
						.getNodeName().trim();// has to be "attribute" or
				// "genericPlotParameters"
				if (currentAttributeType.equals(SnapshotAttribute.XML_TAG)) {

					SnapshotAttribute currentAttribute = loadCurrentAttribute(
							viewConfAttr, currentAttributeNode);
					currentAttribute.setSnapshotAttributes(viewConfAttr);

					listOfAttributesToAdd.add(currentAttribute);
				} else {
					throw new Exception();
				}

			}

			SnapshotAttribute[] snapshotAttributesTab = new SnapshotAttribute[listOfAttributesToAdd
					.size()];
			Enumeration enumer = listOfAttributesToAdd.elements();
			int i = 0;
			while (enumer.hasMoreElements()) {
				SnapshotAttribute next = (SnapshotAttribute) enumer
						.nextElement();
				snapshotAttributesTab[i] = next;
				i++;
			}
			viewConfAttr.setSnapshotAttributes(snapshotAttributesTab);

			viewConf.setSnapshotAttributes(viewConfAttr);
		}

		return viewConf;
	}

	/**
	 * Loads a SnapshotAttribute from an attribute node.
	 * 
	 * @param snapshotAttributes
	 *            The group the attribute belongs to
	 * @param currentAttributeNode
	 *            An attribute node
	 * @return A SnapshotAttribute complete with read/write/delta values
	 * @throws Exception
	 */
	private static SnapshotAttribute loadCurrentAttribute(
			SnapshotAttributes snapshotAttributes, Node currentAttributeNode)
			throws Exception {

		// setting attribute properties
		Hashtable attributeProperties = XMLUtils
				.loadAttributes(currentAttributeNode);

		String id_s = (String) attributeProperties
				.get(SnapshotAttribute.ID_PROPERTY_XML_TAG);
		String dataType_s = (String) attributeProperties
				.get(SnapshotAttribute.DATA_TYPE_PROPERTY_XML_TAG);
		String dataFormat_s = (String) attributeProperties
				.get(SnapshotAttribute.DATA_FORMAT_PROPERTY_XML_TAG);
		String writable_s = (String) attributeProperties
				.get(SnapshotAttribute.WRITABLE_PROPERTY_XML_TAG);
		String completeName = (String) attributeProperties
				.get(SnapshotAttribute.COMPLETE_NAME_PROPERTY_XML_TAG);
		SnapshotAttribute currentAttribute = new SnapshotAttribute(completeName
				.trim(), snapshotAttributes);

		int id = -1;
		int dataType = -1;
		int dataFormat = -1;
		int writable = -1;

		try {
			id = Integer.parseInt(id_s);
		} catch (Exception e) {
		}
		try {
			dataType = Integer.parseInt(dataType_s);
		} catch (Exception e) {
		}
		try {
			dataFormat = Integer.parseInt(dataFormat_s);
		} catch (Exception e) {
		}
		try {
			writable = Integer.parseInt(writable_s);
		} catch (Exception e) {
		}

		currentAttribute.setAttribute_id(id);
		currentAttribute.setData_format(dataFormat);
		currentAttribute.setData_type(dataType);
		currentAttribute.setPermit(writable);

		// setting attribute Read/Write values
		if (currentAttributeNode.hasChildNodes()) {
			NodeList valueNodes = currentAttributeNode.getChildNodes();

			SnapshotAttributeReadValue readValue = null;
			SnapshotAttributeWriteValue writeValue = null;
			for (int i = 0; i < valueNodes.getLength(); i++)
			// as many loops as there are attributes in the VC
			{
				Node currentValueNode = valueNodes.item(i);
				if (XMLUtils.isAFakeNode(currentValueNode)) {
					continue;
				}

				String currentValueType = currentValueNode.getNodeName().trim();// has
				// to
				// be
				// "ReadValue"
				// or
				// "WriteValue"
				if (currentValueType.equals(SnapshotAttributeReadValue.XML_TAG)) {
					readValue = loadReadValue(dataFormat, dataType,
							currentValueNode);
					currentAttribute.setReadValue(readValue);
				} else if (currentValueType
						.equals(SnapshotAttributeWriteValue.XML_TAG)) {
					writeValue = loadWriteValue(dataFormat, dataType,
							currentValueNode);
					currentAttribute.setWriteValue(writeValue);
				} else {
					throw new Exception();
				}
			}

			SnapshotAttributeDeltaValue deltaValue = SnapshotAttributeDeltaValue
					.getInstance(writeValue, readValue);
			currentAttribute.setDeltaValue(deltaValue);

			currentAttribute.updateDisplayFormat();
		}

		return currentAttribute;
	}

	/**
	 * Loads a SnapshotAttributeWriteValue from a write value node.
	 * 
	 * @param _dataFormat
	 *            The attribute's data format (as precedently loaded)
	 * @param _dataType
	 *            The attribute's data type (as precedently loaded)
	 * @param currentValueNode
	 *            A write value node
	 * @return The write value for this node
	 */
	private static SnapshotAttributeWriteValue loadWriteValue(int _dataFormat,
			int _dataType, Node currentValueNode) {
		String nodeValue = XMLUtils.getNodeValue(currentValueNode);
		boolean notApplicable = false;
		try {
			String notApplicableString = (String) XMLUtils.loadAttributes(
					currentValueNode).get(
					SnapshotAttributeValue.NOT_APPLICABLE_XML_TAG);
			if (notApplicableString != null) {
				notApplicable = "true".equalsIgnoreCase(notApplicableString
						.trim());
			}
		} catch (Exception e) {
			// nothing to do
		}
		SnapshotAttributeWriteValue ret = new SnapshotAttributeWriteValue(
				_dataFormat, _dataType, nodeValue);
		if (!ret.isNotApplicable()) {
			ret.setNotApplicable(notApplicable);
		}

		return ret;
	}

	/**
	 * Loads a SnapshotAttributeWriteValue from a read value node.
	 * 
	 * @param _dataFormat
	 *            The attribute's data format (as precedently loaded)
	 * @param _dataType
	 *            The attribute's data type (as precedently loaded)
	 * @param currentValueNode
	 *            A read value node
	 * @return The read value for this node
	 */
	private static SnapshotAttributeReadValue loadReadValue(int _dataFormat,
			int _dataType, Node currentValueNode) {
		String nodeValue = XMLUtils.getNodeValue(currentValueNode);
		boolean notApplicable = false;
		try {
			String notApplicableString = (String) XMLUtils.loadAttributes(
					currentValueNode).get(
					SnapshotAttributeValue.NOT_APPLICABLE_XML_TAG);
			if (notApplicableString != null) {
				notApplicable = "true".equalsIgnoreCase(notApplicableString
						.trim());
			}
		} catch (Exception e) {
			// nothing to do
		}
		SnapshotAttributeReadValue ret = new SnapshotAttributeReadValue(
				_dataFormat, _dataType, nodeValue);
		if (!ret.isNotApplicable()) {
			ret.setNotApplicable(notApplicable);
		}

		return ret;
	}

	/**
	 * Loads the SnapshotData part of the Snapshot described by this node.
	 * 
	 * @param rootNode
	 *            The root snapshot node
	 * @return A Snapshot with only its SnapshotData part loaded
	 * @throws Exception
	 */
	private static Snapshot loadSnapshotData(Node rootNode) throws Exception {
		Snapshot viewConf = new Snapshot();
		SnapshotData viewConfData = new SnapshotData();

		Hashtable VCProperties = XMLUtils.loadAttributes(rootNode);

		String time_s = (String) VCProperties
				.get(SnapshotData.TIME_PROPERTY_XML_TAG);
		String id_s = (String) VCProperties
				.get(SnapshotData.ID_PROPERTY_XML_TAG);
		String comment_s = (String) VCProperties
				.get(SnapshotData.COMMENT_PROPERTY_XML_TAG);
		String path_s = (String) VCProperties
				.get(SnapshotData.PATH_PROPERTY_XML_TAG);
		String isModified_s = (String) VCProperties
				.get(SnapshotData.IS_MODIFIED_PROPERTY_XML_TAG);
		boolean isModified = GUIUtilities.StringToBoolean(isModified_s);

		if (time_s != null) {
			Timestamp time = Timestamp.valueOf(time_s);
			viewConfData.setTime(time);
		}
		if (id_s != null) {
			int id = Integer.parseInt(id_s);
			viewConfData.setId(id);
		}

		viewConfData.setComment(comment_s);
		viewConfData.setPath(path_s);
		viewConf.setModified(isModified);

		viewConf.setSnapshotData(viewConfData);
		return viewConf;
	}
}
