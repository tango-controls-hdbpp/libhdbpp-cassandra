// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttributes.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ArchivingConfigurationAttributes.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.12 $
//
// $Log: ArchivingConfigurationAttributes.java,v $
// Revision 1.12 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.11 2006/11/07 14:34:19 ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.10 2006/08/23 10:02:46 ounsy
// avoided a concurrent modification exception
//
// Revision 1.9 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.8 2006/07/24 07:37:51 ounsy
// image support with partial loading
//
// Revision 1.7 2006/07/18 10:26:42 ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.6 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.5 2006/05/16 12:05:28 ounsy
// minor changes
//
// Revision 1.4 2006/04/10 09:15:00 ounsy
// optimisation on loading an AC
//
// Revision 1.3 2005/12/15 11:34:39 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:56 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.data.archiving;

import java.text.Collator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.tango.util.entity.data.Attributes;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;

public class ArchivingConfigurationAttributes extends Attributes {

	private static final long serialVersionUID = -5427446917729749484L;
	private TreeMap<String, ArchivingConfigurationAttribute> attributes;
	private ArchivingConfiguration archivingConfiguration;

	public ArchivingConfigurationAttributes() {
		attributes = new TreeMap<String, ArchivingConfigurationAttribute>(
				Collator.getInstance());
	}

	/**
	 * @param _attributes
	 */
	public ArchivingConfigurationAttributes(
			ArchivingConfigurationAttribute[] _attributes) {
		super(_attributes);

		if (_attributes != null) {
			for (int i = 0; i < _attributes.length; i++) {
				if (_attributes[i] != null) {
					_attributes[i].setGroup(this);
				}
			}
		}
	}

	public boolean equals(ArchivingConfigurationAttributes attrs) {
		return this.attributes.size() == attrs.attributes.size();
	}

	public void addAttribute(ArchivingConfigurationAttribute _attribute) {
		_attribute.setGroup(this);
		String completeName = _attribute.getCompleteName();
		if (completeName.endsWith("/")) {
			completeName = completeName.substring(0, completeName.length() - 1);
		}
		attributes.put(completeName, _attribute);
	}

	public ArchivingConfigurationAttribute getAttribute(String completeName) {
		return (ArchivingConfigurationAttribute) attributes.get(completeName);
	}

	public String toString() {
		String ret = "";

		if (attributes != null) {
			Set<String> keySet = attributes.keySet();
			Iterator<String> keyIterator = keySet.iterator();
			int i = 0;
			while (keyIterator.hasNext()) {
				String nextKey = keyIterator.next();
				ArchivingConfigurationAttribute nextValue = (ArchivingConfigurationAttribute) attributes
						.get(nextKey);

				ret += nextValue.toString();
				if (i < attributes.size() - 1) {
					ret += GUIUtilities.CRLF;
				}

				i++;
			}
		}

		return ret;
	}

	/**
	 * @return Returns the archivingConfiguration.
	 */
	public ArchivingConfiguration getArchivingConfiguration() {
		return archivingConfiguration;
	}

	/**
	 * @param archivingConfiguration
	 *            The archivingConfiguration to set.
	 */
	public void setArchivingConfiguration(
			ArchivingConfiguration archivingConfiguration) {
		this.archivingConfiguration = archivingConfiguration;
	}

	/**
	 * @return Returns the attributes.
	 */
	public TreeMap<String, ArchivingConfigurationAttribute> getAttributesMap() {
		return attributes;
	}

	public ArchivingConfigurationAttribute[] getAttributesList() {
		ArchivingConfigurationAttribute[] ret = new ArchivingConfigurationAttribute[attributes
				.size()];
		Set<String> keySet = attributes.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		int i = 0;
		while (keyIterator.hasNext()) {
			String nextKey = keyIterator.next();
			ArchivingConfigurationAttribute nextValue = (ArchivingConfigurationAttribute) attributes
					.get(nextKey);
			ret[i] = nextValue;
			i++;
		}
		return ret;
	}

	public void push() {
		if (this.getAttributesMap() != null) {
			Options options = Options.getInstance();
			ACOptions acOptions = options.getAcOptions();
			if (acOptions.isAlternateSelectionMode()) {
				ArchivingConfigurationAttribute[] list = this.getListCLA(this
						.getAttributesMap());
				this.reloadTableModelCLA(list);
			}
			Vector<Domain> _domains = this.getDomainsCLA(this
					.getAttributesMap());
			this.reloadTreeModelCLA(_domains);
		}
	}

	private void reloadTableModelCLA(ArchivingConfigurationAttribute[] list) {
		if ((ACEditDialog.getInstance() != null)
				&& (ACEditDialog.getInstance().getAttributeTableSelectionBean() != null)) {
			ACEditDialog.getInstance().getAttributeTableSelectionBean()
					.getSelectionPanel().getAttributesSelectTable().getModel()
					.setRows(list);
		}
	}

	private void reloadTreeModelCLA(Vector<Domain> _domains) {
		ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
		model.build(_domains);
		model.reload();
	}

	private Vector<Domain> getDomainsCLA(
			TreeMap<String, ArchivingConfigurationAttribute> attributesHash) {
		Set<String> keySet = attributesHash.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		Vector<Domain> _domains = new Vector<Domain>();
		ArchivingConfigurationAttribute[] _list = new ArchivingConfigurationAttribute[attributesHash
				.size()];
		int i = 0;

		while (keyIterator.hasNext()) {
			String completeName = keyIterator.next();
			ArchivingConfigurationAttribute next = attributesHash
					.get(completeName);
			_list[i] = next;
			i++;

			String domain_s = next.getDomainName();
			String family_s = next.getFamilyName();
			String member_s = next.getMemberName();

			Domain domain = new Domain(domain_s);
			Family family = new Family(family_s);
			Member member = new Member(member_s);

			Domain dom = Domain.hasDomain(_domains, domain_s);
			if (dom != null) {
				Family fam = dom.getFamily(family_s);
				if (fam != null) {
					Member mem = fam.getMember(member_s);
					if (mem != null) {
						mem.addAttribute(next);
						fam.addMember(mem);
						dom.addFamily(fam);
						_domains = Domain.addDomain(_domains, dom);
					} else {
						member.addAttribute(next);
						fam.addMember(member);
						dom.addFamily(fam);
						_domains = Domain.addDomain(_domains, dom);
					}
				} else {
					member.addAttribute(next);
					family.addMember(member);
					dom.addFamily(family);
					_domains = Domain.addDomain(_domains, dom);
				}
			} else {
				member.addAttribute(next);
				family.addMember(member);
				domain.addFamily(family);
				_domains = Domain.addDomain(_domains, domain);
			}
		}
		return _domains;
	}

	/**
	 * @return
	 */
	private ArchivingConfigurationAttribute[] getListCLA(
			TreeMap<String, ArchivingConfigurationAttribute> attributesHash) {
		ArchivingConfigurationAttribute[] list = new ArchivingConfigurationAttribute[attributesHash
				.size()];
		int i = 0;
		Set<String> keySet = attributesHash.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String completeName = keyIterator.next();
			ArchivingConfigurationAttribute next = attributesHash
					.get(completeName);
			list[i] = next;

			i++;
		}
		return list;
	}

	public void setAttributes(
			TreeMap<String, ArchivingConfigurationAttribute> attributes) {
		this.attributes = attributes;
	}

	public void removeAttributesNotInList(
			TreeMap<String, ArchivingConfigurationAttribute> attrs) {
		Set<String> keySet = this.attributes.keySet();
		Set<String> toRemoveSet = new HashSet<String>();
		Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			String next = keyIterator.next();
			if (!attrs.containsKey(next)) {
				toRemoveSet.add(next);
			}
		}
		Iterator<String> toRemoveIterator = toRemoveSet.iterator();
		while (toRemoveIterator.hasNext()) {
			this.attributes.remove(toRemoveIterator.next());
		}
	}

	public void controlValues() throws ArchivingConfigurationException {
		ArchivingConfigurationAttribute[] list = this.getAttributesList();
		if (list == null || list.length == 0) {
			throw new ArchivingConfigurationException(null,
					ArchivingConfiguration.EMPTY_ATTRIBUTES);
		}

		int lg = list.length;
		int ret = -1;
		for (int i = 0; i < lg; i++) {
			ArchivingConfigurationAttribute attr = list[i];
			attr.controlValues();
			if (ret != -1) {
				break;
			}
		}

	}

	@SuppressWarnings("unchecked")
	public ArchivingConfigurationAttributes cloneAttrs() {
		ArchivingConfigurationAttributes ret = new ArchivingConfigurationAttributes();

		ret
				.setAttributes((TreeMap<String, ArchivingConfigurationAttribute>) this
						.getAttributesMap().clone());

		return ret;
	}
}
