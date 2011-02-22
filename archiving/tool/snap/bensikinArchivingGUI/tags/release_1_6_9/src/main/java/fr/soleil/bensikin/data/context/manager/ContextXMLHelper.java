//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/manager/ContextXMLHelper.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ArchivingConfigurationXMLHelper.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.3 $
//
//$Log: ContextXMLHelper.java,v $
//Revision 1.3  2006/11/29 10:00:45  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:34:24  ounsy
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
package fr.soleil.bensikin.data.context.manager;

import java.io.File;
import java.sql.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.data.context.ContextAttributes;
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.xml.XMLUtils;

/**
 * A class used to delegate the loading of a context file into a Context object
 * 
 * @author CLAISSE
 */
public class ContextXMLHelper {

	/**
	 * Loads a Context given its file location.
	 * 
	 * @param location
	 *            The complete path to the context file to load
	 * @return The Context object built from its XML representation
	 * @throws Exception
	 */
	public static Context loadContextIntoHash(String location) throws Exception {
		File file = new File(location);
		Node rootNode = XMLUtils.getRootNode(file);

		Context ret = loadContextIntoHashFromRoot(rootNode);

		return ret;
	}

	/**
	 * Loads a Context given its XML root node.
	 * 
	 * @param rootNode
	 *            The file's root node
	 * @return The Context object built from its XML representation
	 * @throws Exception
	 */
	private static Context loadContextIntoHashFromRoot(Node rootNode)
			throws Exception {
		Context viewConf = loadContextData(rootNode);

		if (rootNode.hasChildNodes()) {
			NodeList attributesNodes = rootNode.getChildNodes();
			ContextAttributes viewConfAttr = new ContextAttributes(viewConf);
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
				if (currentAttributeType.equals(ContextAttribute.XML_TAG)) {
					ContextAttribute currentAttribute = new ContextAttribute(
							viewConfAttr);
					Hashtable attributeProperties = XMLUtils
							.loadAttributes(currentAttributeNode);

					String attributeCompleteName = (String) attributeProperties
							.get(ContextAttribute.COMPLETE_NAME_PROPERTY_XML_TAG);
					String device = (String) attributeProperties
							.get(ContextAttribute.DEVICE_PROPERTY_XML_TAG);
					String domain = (String) attributeProperties
							.get(ContextAttribute.DOMAIN_PROPERTY_XML_TAG);
					String family = (String) attributeProperties
							.get(ContextAttribute.FAMILY_PROPERTY_XML_TAG);
					String member = (String) attributeProperties
							.get(ContextAttribute.MEMBER_PROPERTY_XML_TAG);
					String name = (String) attributeProperties
							.get(ContextAttribute.NAME_PROPERTY_XML_TAG);

					currentAttribute.setCompleteName(attributeCompleteName);
					currentAttribute.setDevice(device);
					currentAttribute.setDomain(domain);
					currentAttribute.setFamily(family);
					currentAttribute.setMember(member);
					currentAttribute.setName(name);

					currentAttribute.setContextAttributes(viewConfAttr);
					listOfAttributesToAdd.add(currentAttribute);
				} else {
					throw new Exception();
				}

			}

			ContextAttribute[] contextAttributesTab = new ContextAttribute[listOfAttributesToAdd
					.size()];
			Enumeration enumer = listOfAttributesToAdd.elements();
			int i = 0;
			while (enumer.hasMoreElements()) {
				ContextAttribute next = (ContextAttribute) enumer.nextElement();
				contextAttributesTab[i] = next;
				i++;
			}
			viewConfAttr.setContextAttributes(contextAttributesTab);

			viewConf.setContextAttributes(viewConfAttr);
		}

		return viewConf;
	}

	/**
	 * Loads the ContextData part of a Context given the XML Context node.
	 * 
	 * @param node
	 *            The XML Context node
	 * @return A Context with only the ContextData part loaded
	 * @throws Exception
	 */
	private static Context loadContextData(Node node) throws Exception {
		Context viewConf = new Context();
		ContextData viewConfData = new ContextData();

		Hashtable VCProperties = XMLUtils.loadAttributes(node);

		String creationDate_s = (String) VCProperties
				.get(ContextData.CREATION_DATE_PROPERTY_XML_TAG);
		String author = (String) VCProperties
				.get(ContextData.AUTHOR_PROPERTY_XML_TAG);
		String description = (String) VCProperties
				.get(ContextData.DESCRIPTION_DATE_PROPERTY_XML_TAG);
		String id_s = (String) VCProperties
				.get(ContextData.ID_PROPERTY_XML_TAG);
		String name = (String) VCProperties
				.get(ContextData.NAME_PROPERTY_XML_TAG);
		String reason = (String) VCProperties
				.get(ContextData.REASON_PROPERTY_XML_TAG);

		String path = (String) VCProperties
				.get(ContextData.PATH_PROPERTY_XML_TAG);
		String isModified_s = (String) VCProperties
				.get(ContextData.IS_MODIFIED_PROPERTY_XML_TAG);
		boolean isModified = GUIUtilities.StringToBoolean(isModified_s);

		if (creationDate_s != null) {
			Date creationDate = Date.valueOf(creationDate_s);
			viewConfData.setCreation_date(creationDate);
		}
		if (id_s != null) {
			int id = Integer.parseInt(id_s);
			viewConfData.setId(id);
		}
		viewConfData.setAuthor_name(author);
		viewConfData.setDescription(description);
		viewConfData.setName(name);
		viewConfData.setReason(reason);
		viewConfData.setPath(path);
                viewConf.setContextId(viewConfData.getId());
		viewConf.setContextData(viewConfData);
		viewConf.setModified(isModified);

		return viewConf;
	}
}