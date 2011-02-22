// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/ContextAttributes.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ContextAttributes.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.12 $
//
// $Log: ContextAttributes.java,v $
// Revision 1.12 2008/01/08 16:10:24 pierrejoseph
// Pb with the validate cmd in table mode : correction in the remove method
// - see with RGIR
//
// Revision 1.11 2007/08/24 14:09:13 ounsy
// Context attributes ordering (Mantis bug 3912)
//
// Revision 1.10 2007/08/23 15:28:48 ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.9 2007/02/08 14:45:09 ounsy
// corrected a bug in toString() that left out some attributes
//
// Revision 1.8 2006/11/29 10:00:26 ounsy
// minor changes
//
// Revision 1.7 2006/01/12 12:57:39 ounsy
// traces removed
//
// Revision 1.6 2005/12/14 16:33:27 ounsy
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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.tango.util.entity.data.Attribute;
import fr.soleil.tango.util.entity.data.Attributes;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;
import fr.soleil.tango.util.entity.ui.model.AttributesSelectTableModel;

/**
 * Represents the list of attributes attached to a context
 * 
 * @author CLAISSE
 */
public class ContextAttributes extends Attributes {

	private static final long serialVersionUID = 8105080402541406471L;
	private ContextAttribute[] contextAttributes;
	private Context context;

	/**
	 * Builds a ContextAttribute with a reference to its Context.
	 * 
	 * @param _context
	 *            The Context this attributes belong to
	 */
	public ContextAttributes(Context _context) {
		super();
		this.context = _context;
	}

	/**
	 * @param _context
	 *            The Context this attributes belong to
	 * @param _contextAttributes
	 *            The list of attributes
	 */
	public ContextAttributes(Context _context,
			ContextAttribute[] _contextAttributes) {
		super();
		this.context = _context;
		this.contextAttributes = _contextAttributes;
	}

	public ContextAttributes(Context _context, Attributes attributes) {
		super();
		if (attributes != null) {
			Attribute[] attrArray = attributes.getAttributes();
			if (attrArray != null) {
				contextAttributes = new ContextAttribute[attrArray.length];
				for (int i = 0; i < attrArray.length; i++) {
					contextAttributes[i] = new ContextAttribute(attrArray[i]);
					contextAttributes[i].setContextAttributes(this);
				}
			}
		}
	}

	/**
	 * Returns a XML representation of the attributes.
	 * 
	 * @return a XML representation of the attributes
	 */
	public String toString() {
		String ret = "";

		if (this.contextAttributes != null) {
			for (int i = 0; i < this.contextAttributes.length; i++) {
				ContextAttribute nextValue = this.contextAttributes[i];
				ret += nextValue.toString();
				if (i < contextAttributes.length - 1) {
					ret += GUIUtilities.CRLF;
				}
			}
		}

		return ret;
	}

	public void appendUserFriendlyString(StringBuffer buffer) {
		if (buffer != null) {
			if (this.contextAttributes != null) {
				for (int i = 0; i < this.contextAttributes.length; i++) {
					ContextAttribute nextValue = this.contextAttributes[i];
					nextValue.appendUserFriendlyString(buffer);
					if (i < contextAttributes.length - 1) {
						buffer.append(GUIUtilities.CRLF);
					}
				}
			}
		}
	}

	/**
	 * Visibly make these attributes the current context's attributes
	 */
	public void push() {
		int count = 0;
		if (contextAttributes != null) {
			count = contextAttributes.length;
			for (int i = 0; i < count; i++) {
				if (contextAttributes[i] != null) {
					contextAttributes[i].setNew(false);
				}
			}
		}
		ContextDetailPanel contextDetailPanel = ContextDetailPanel
				.getInstance();

		// update table
		AttributesSelectTableModel tableModel = ContextDetailPanel
				.getInstance().getAttributeTableSelectionBean()
				.getSelectionPanel().getAttributesSelectTable().getModel();
		tableModel.reset();
		tableModel.setRows(contextAttributes);
		// update tree
		Vector<Domain> newAttributes = this.getHierarchy();
		ContextAttributesTreeModel model = ContextAttributesTreeModel
				.getInstance(false);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		model.setRoot(new DefaultMutableTreeNode(root.getUserObject()));
		model.build(newAttributes);
		model.reload();

		contextDetailPanel.revalidate();
		contextDetailPanel.repaint();

		ContextDataPanel.getInstance().getAttributeCountField().setText(
				Integer.toString(count));
	}

	/**
	 * @return Returns the context.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * @param context
	 *            The context to set.
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * @return Returns the contextAttributes.
	 */
	public ContextAttribute[] getContextAttributes() {
		return contextAttributes;
	}

	/**
	 * @param contextAttributes
	 *            The contextAttributes to set.
	 */
	public void setContextAttributes(ContextAttribute[] contextAttributes) {
		this.contextAttributes = contextAttributes;
	}

	public void removeAttributesNotInList(
			TreeMap<String, ContextAttribute> attrs) {
		if ((this.contextAttributes != null)
				&& (this.contextAttributes.length > 0)) {
			TreeMap<String, ContextAttribute> ht = new TreeMap<String, ContextAttribute>(
					Collator.getInstance());
			for (int i = 0; i < this.contextAttributes.length; i++) {
				ht.put(this.contextAttributes[i].getCompleteName(),
						this.contextAttributes[i]);
			}

			// ---
			Iterator<String> enumer = ht.keySet().iterator();
			ArrayList<String> toRemove = new ArrayList<String>();
			while (enumer.hasNext()) {
				String next = enumer.next();
				if (!attrs.containsKey(next)) {
					toRemove.add(next);
				}
			}
			for (int i = 0; i < toRemove.size(); i++) {
				ht.remove(toRemove.get(i));
			}
			toRemove.clear();
			toRemove = null;
			// ---

			ContextAttribute[] result = new ContextAttribute[ht.size()];
			enumer = ht.keySet().iterator();
			int i = 0;
			while (enumer.hasNext()) {
				String key = enumer.next();
				ContextAttribute val = (ContextAttribute) ht.get(key);
				result[i] = val;
				i++;
			}

			this.contextAttributes = result;
		}
	}

	@Override
	public Vector<Domain> getHierarchy() {
		if (this.contextAttributes == null) {
			return null;
		}

		int nbAttributes = this.contextAttributes.length;

		// Hashtable domains
		Hashtable<String, Hashtable<String, Hashtable<String, Vector<String>>>> htDomains = new Hashtable<String, Hashtable<String, Hashtable<String, Vector<String>>>>();
		for (int i = 0; i < nbAttributes; i++) {
			Attribute currentAttribute = this.contextAttributes[i];
			String currentDomain = currentAttribute.getDomainName();
			htDomains.put(currentDomain,
					new Hashtable<String, Hashtable<String, Vector<String>>>());
		}

		// Hashtable families
		for (int i = 0; i < nbAttributes; i++) {
			Attribute currentAttribute = this.contextAttributes[i];
			String currentDomain = currentAttribute.getDomainName();
			String currentFamily = currentAttribute.getFamilyName();

			Hashtable<String, Hashtable<String, Vector<String>>> htCurrentDomain = htDomains
					.get(currentDomain);
			htCurrentDomain.put(currentFamily,
					new Hashtable<String, Vector<String>>());
		}

		// Hashtable members
		for (int i = 0; i < nbAttributes; i++) {
			Attribute currentAttribute = this.contextAttributes[i];
			String currentDomain = currentAttribute.getDomainName();
			String currentFamily = currentAttribute.getFamilyName();
			String currentMember = currentAttribute.getMemberName();

			Hashtable<String, Hashtable<String, Vector<String>>> htCurrentDomain = htDomains
					.get(currentDomain);
			Hashtable<String, Vector<String>> htCurrentFamily = htCurrentDomain
					.get(currentFamily);

			htCurrentFamily.put(currentMember, new Vector<String>());
		}

		// Vector attributes
		for (int i = 0; i < nbAttributes; i++) {
			Attribute currentAttribute = this.contextAttributes[i];
			String currentDomain = currentAttribute.getDomainName();
			String currentFamily = currentAttribute.getFamilyName();
			String currentMember = currentAttribute.getMemberName();
			String currentAttributeName = currentAttribute.getName();

			Hashtable<String, Hashtable<String, Vector<String>>> htCurrentDomain = htDomains
					.get(currentDomain);
			Hashtable<String, Vector<String>> htCurrentFamily = htCurrentDomain
					.get(currentFamily);
			Vector<String> veCurrentMember = htCurrentFamily.get(currentMember);

			veCurrentMember.add(currentAttributeName);
		}

		Vector<Domain> result = new Vector<Domain>();

		Enumeration<String> enumDomains = htDomains.keys();
		while (enumDomains.hasMoreElements()) {
			String nextDomainName = enumDomains.nextElement();
			Domain nextDomain = new Domain(nextDomainName);
			result.add(nextDomain);

			Hashtable<String, Hashtable<String, Vector<String>>> htFamilies = htDomains
					.get(nextDomainName);
			Enumeration<String> enumFamilies = htFamilies.keys();
			while (enumFamilies.hasMoreElements()) {
				String nextFamilyName = enumFamilies.nextElement();
				Family nextFamily = new Family(nextFamilyName);
				nextDomain.addFamily(nextFamily);

				Hashtable<String, Vector<String>> htMembers = htFamilies
						.get(nextFamilyName);
				Enumeration<String> enumMembers = htMembers.keys();
				while (enumMembers.hasMoreElements()) {
					String nextMemberName = enumMembers.nextElement();
					Member nextMember = new Member(nextMemberName);
					nextFamily.addMember(nextMember);

					Vector<String> vectAttr = htMembers.get(nextMemberName);
					for (int i = 0; i < vectAttr.size(); i++) {
						nextMember.addAttribute(new Attribute(vectAttr.get(i)));
					}
				}
			}
		}

		result.trimToSize();
		return result;
	}

}
