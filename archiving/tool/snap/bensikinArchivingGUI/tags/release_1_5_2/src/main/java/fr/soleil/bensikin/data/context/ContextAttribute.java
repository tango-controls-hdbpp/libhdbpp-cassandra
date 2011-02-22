// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/ContextAttribute.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ContextAttribute.
// (Garda Laure) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ContextAttribute.java,v $
// Revision 1.8 2007/08/23 15:28:48 ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.7 2007/03/15 14:26:35 ounsy
// corrected the table mode add bug and added domains buffer
//
// Revision 1.6 2005/12/14 16:33:07 ounsy
// added methods necessary for alternate attribute selection
//
// Revision 1.5 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:37 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.data.context;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.tango.util.entity.data.Attribute;

/**
 * Represents an attribute attached to a context
 * 
 * @author CLAISSE
 */
public class ContextAttribute extends Attribute {

	private static final long serialVersionUID = 379706688928812664L;

	private ContextAttributes contextAttributes;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "Attribute";

	/**
	 * The XML tag name used for the "complete name" property
	 */
	public static final String COMPLETE_NAME_PROPERTY_XML_TAG = "CompleteName";

	/**
	 * The XML tag name used for the "name" property
	 */
	public static final String NAME_PROPERTY_XML_TAG = "Name";

	/**
	 * The XML tag name used for the "domain" property
	 */
	public static final String DOMAIN_PROPERTY_XML_TAG = "Domain";

	/**
	 * The XML tag name used for the "family" property
	 */
	public static final String FAMILY_PROPERTY_XML_TAG = "Family";

	/**
	 * The XML tag name used for the "member" property
	 */
	public static final String MEMBER_PROPERTY_XML_TAG = "Member";

	/**
	 * The XML tag name used for the "device" property
	 */
	public static final String DEVICE_PROPERTY_XML_TAG = "Device";

	/**
	 * Builds a ContextAttribute with a reference to its ContextAttributes
	 * group.
	 * 
	 * @param _contextAttributes
	 *            The group this attributes belongs to
	 */
	public ContextAttribute(ContextAttributes _contextAttributes) {
		this.contextAttributes = _contextAttributes;
	}

	/**
	 * Builds a ContextAttribute of name <code>_name</code>.
	 * 
	 * @param _name
	 */
	public ContextAttribute(String _name) {
		name = _name;
	}

	/**
	 * Default constructor
	 */
	public ContextAttribute() {
		super();
	}

	/**
	 * @param path
	 */
	public ContextAttribute(TreeNode[] path) {
		super(path);
		String _domain = path[1].toString();
		String _family = path[2].toString();
		String _member = path[3].toString();
		setDevice(_domain + GUIUtilities.TANGO_DELIM + _family
				+ GUIUtilities.TANGO_DELIM + _member);
	}

	public ContextAttribute(Attribute attribute) {
		super();
		if (attribute != null) {
			setCompleteName(attribute.getCompleteName());
			setDevice(attribute.getDeviceName());
			setDeviceClass(attribute.getDeviceClass());
			setDomain(attribute.getDomainName());
			setFamily(attribute.getFamilyName());
			setMember(attribute.getMemberName());
			setName(attribute.getName());
		}
	}

	/**
	 * Returns a XML representation of the attribute.
	 * 
	 * @return a XML representation of the attribute
	 */
	public String toString() {
		String ret = "";

		XMLLine emptyLine = new XMLLine(ContextAttribute.XML_TAG,
				XMLLine.EMPTY_TAG_CATEGORY);

		if (getCompleteName() != null) {
			emptyLine.setAttribute(
					ContextAttribute.COMPLETE_NAME_PROPERTY_XML_TAG,
					getCompleteName());
		}
		if (getName() != null) {
			emptyLine.setAttribute(ContextAttribute.NAME_PROPERTY_XML_TAG,
					getName());
		}
		if (getDomainName() != null) {
			emptyLine.setAttribute(ContextAttribute.DOMAIN_PROPERTY_XML_TAG,
					getDomainName());
		}
		if (getFamilyName() != null) {
			emptyLine.setAttribute(ContextAttribute.FAMILY_PROPERTY_XML_TAG,
					getFamilyName());
		}
		if (getMemberName() != null) {
			emptyLine.setAttribute(ContextAttribute.MEMBER_PROPERTY_XML_TAG,
					getMemberName());
		}
		if (getDeviceName() != null) {
			emptyLine.setAttribute(ContextAttribute.DEVICE_PROPERTY_XML_TAG,
					getDeviceName());
		}

		ret += emptyLine.toString();
		ret += GUIUtilities.CRLF;

		return ret;
	}

	public void appendUserFriendlyString(StringBuffer buffer) {
		if (buffer != null) {
			buffer.append(getCompleteName());
		}
	}

	/**
	 * @return Returns the contextAttributes.
	 */
	public ContextAttributes getContextAttributes() {
		return contextAttributes;
	}

	/**
	 * @param contextAttributes
	 *            The contextAttributes to set.
	 */
	public void setContextAttributes(ContextAttributes contextAttributes) {
		this.contextAttributes = contextAttributes;
	}

	/**
	 * @return
	 */
	public TreePath getTreePath() {
		return new TreePath(getPath());
	}

}
