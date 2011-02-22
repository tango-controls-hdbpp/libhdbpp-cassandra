// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/models/ACAttributesTreeModel.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACAttributesTreeModel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.11 $
//
// $Log: ACAttributesTreeModel.java,v $
// Revision 1.11 2006/11/07 14:34:28 ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.10 2006/09/20 12:50:29 ounsy
// changed imports
//
// Revision 1.9 2006/08/29 14:13:30 ounsy
// minor changes
//
// Revision 1.8 2006/08/09 16:12:54 ounsy
// No more automatic tree expanding : user has to click on a button to fully
// expand a tree
//
// Revision 1.7 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.6 2006/07/18 10:30:03 ounsy
// possibility to reload tango attributes
//
// Revision 1.5 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.4 2006/05/16 12:03:57 ounsy
// minor changes
//
// Revision 1.3 2005/12/15 11:43:23 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.models;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.comparators.MamboToStringObjectComparator;
import fr.soleil.mambo.containers.archiving.ACAttributeDetailHdbPanel;
import fr.soleil.mambo.containers.archiving.ACAttributeDetailTdbPanel;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.tango.util.entity.data.Attribute;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;

public class ACAttributesTreeModel extends ACTreeModel {

	private static final long serialVersionUID = 548610307089654507L;
	private static ACAttributesTreeModel instance = null;

	/**
	 * @param root
	 */
	private ACAttributesTreeModel() {
		super();
	}

	public static ACAttributesTreeModel getInstance() {
		if (instance == null) {
			instance = new ACAttributesTreeModel();
		}

		return instance;
	}

	/**
	 * @param tree
	 *            8 juil. 2005
	 */
	/*
	 * public void setTree ( ACAttributesSelectTree tree ) { this.treeInstance =
	 * tree; }
	 */

	/**
	 * @param validSelected
	 *            27 juin 2005
	 */

	// moved from daughter ACAttr
	public void addSelectedAttibutes(Vector<TreePath> validSelected) {
		TreeMap<String, ArchivingConfigurationAttribute> possibleAttr = ACPossibleAttributesTreeModel
				.getInstance().getAttributes();
		TreeMap<String, ArchivingConfigurationAttribute> htAttr = this
				.getAttributes();

		Enumeration<TreePath> enumeration = validSelected.elements();
		while (enumeration.hasMoreElements()) {
			TreePath aPath = enumeration.nextElement();

			// START CURRENT SELECTED PATH
			DefaultMutableTreeNode currentTreeObject = null;
			for (int depth = 0; depth < CONTEXT_TREE_DEPTH; depth++) {
				Object currentPathObject = aPath.getPathComponent(depth);

				if (depth == 0) {
					currentTreeObject = (DefaultMutableTreeNode) this.getRoot();
					continue;
				}

				int numOfChildren = this.getChildCount(currentTreeObject);
				Vector<String> vect = new Vector<String>();
				for (int childIndex = 0; childIndex < numOfChildren; childIndex++) {
					Object childAt = this.getChild(currentTreeObject,
							childIndex);
					vect.add(childAt.toString());
				}

				if (vect.contains(currentPathObject.toString())) {
					int pos = vect.indexOf(currentPathObject.toString());
					currentTreeObject = (DefaultMutableTreeNode) this.getChild(
							currentTreeObject, pos);
				} else {
					vect.add(currentPathObject.toString());
					Collections.sort(vect, new MamboToStringObjectComparator());
					DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(
							currentPathObject.toString());
					// int index = 0;
					int index = vect.indexOf(currentPathObject.toString());
					this.insertNodeInto(newChild, currentTreeObject, index);

					if (depth == CONTEXT_TREE_DEPTH - 1) {
						TreeNode[] path = newChild.getPath();

						Attribute _attribute = (Attribute) possibleAttr
								.get(translatePathIntoKey(path));
						// --------CLA xxx
						if (_attribute == null) {
							_attribute = new Attribute(path);
						}
						// --------CLA xxx

						ArchivingConfigurationAttribute attribute = new ArchivingConfigurationAttribute(
								_attribute);
						if (attribute != null) {
							htAttr.put(translatePathIntoKey(path), attribute);
						}
					}

					currentTreeObject = newChild;
				}
			}
			// END CURRENT SELECTED PATH
		}
	}

	/**
	 * @param validSelected
	 *            29 juin 2005
	 */
	public void removeSelectedAttributes(Vector<TreePath> validSelected) {
		Enumeration<TreePath> enumeration = validSelected.elements();
		while (enumeration.hasMoreElements()) {
			TreePath aPath = enumeration.nextElement();
			TreeMap<String, ArchivingConfigurationAttribute> htAttr = this
					.getAttributes();

			String dom_s = aPath.getPathComponent(1).toString();
			String fam_s = aPath.getPathComponent(2).toString();
			String mem_s = aPath.getPathComponent(3).toString();
			String att_s = aPath.getPathComponent(4).toString();
			String path = dom_s + GUIUtilities.TANGO_DELIM + fam_s
					+ GUIUtilities.TANGO_DELIM + mem_s
					+ GUIUtilities.TANGO_DELIM + att_s;

			htAttr.remove(path);
			super.removeFromDomains(path);

			MutableTreeNode node = (MutableTreeNode) aPath
					.getLastPathComponent();

			try {
				this.removeNodeFromParent(node);
			} catch (Exception e) {
				// do nothing
			}
		}

		this.traceHtAttr();
	}

	/**
	 * @param validSelected
	 *            29 juin 2005
	 */
	public void removeSelectedAttributes2(Vector<TreePath> validSelected) {
		Enumeration<TreePath> enumeration = validSelected.elements();
		while (enumeration.hasMoreElements()) {
			TreePath aPath = enumeration.nextElement();

			DefaultMutableTreeNode currentTreeObject = null;
			for (int depth = 0; depth < CONTEXT_TREE_DEPTH; depth++) {
				String userObjectRef = (String) aPath.getPathComponent(depth);

				if (depth == 0) {
					currentTreeObject = (DefaultMutableTreeNode) this.getRoot();
					continue;
				}

				int numOfChildren = this.getChildCount(currentTreeObject);
				// Vector vect = new Vector();
				for (int childIndex = 0; childIndex < numOfChildren; childIndex++) {
					DefaultMutableTreeNode childAt = (DefaultMutableTreeNode) this
							.getChild(currentTreeObject, childIndex);
					String userObjectAt = (String) childAt.getUserObject();

					if (userObjectRef.equals(userObjectAt)) {
						currentTreeObject = childAt;
						break;
					}
				}
			}

			TreeNode[] path = currentTreeObject.getPath();
			this.removeNodeFromParent(currentTreeObject);

			// System.out.println ( "path"+path.length );

			/*
			 * Enumeration enum1 = this.getAttributes().keys(); while (
			 * enum1.hasMoreElements() ) { String next = ( String )
			 * enum1.nextElement(); //System.out.println (
			 * "xxx/next 1|"+next+"|" ); }
			 */

			// System.out.println ( "before/"+this.getAttributes
			// ().size()+"/translatePathIntoKey ( path )|"+translatePathIntoKey
			// ( path )+"|" );
			this.getAttributes().remove(translatePathIntoKey(path));
			// System.out.println ( "after/"+this.getAttributes ().size() );
		}
	}

	public void reload() {
		super.reload();
		ACAttributeDetailHdbPanel.getInstance().load(
				new ArchivingConfigurationAttribute());
		ACAttributeDetailTdbPanel.getInstance().load(
				new ArchivingConfigurationAttribute());
		/*
		 * if (ACAttributesRecapTree.getInstance () != null) {
		 * ACAttributesRecapTree.getInstance ().expandAll(true); }
		 */
	}

	public void removeAll() {
		super.removeAll();

		ACAttributeDetailHdbPanel.getInstance().load(
				new ArchivingConfigurationAttribute());
		ACAttributeDetailTdbPanel.getInstance().load(
				new ArchivingConfigurationAttribute());
	}

	public void match(ITangoManager source, Criterions searchCriterions) {
		Vector<Domain> _domains = source.loadDomains(searchCriterions, false);

		Enumeration<?> enumeration = ((DefaultMutableTreeNode) root)
				.preorderEnumeration();
		Vector<TreePath> attributesToRemove = new Vector<TreePath>();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode currentTraversedNode = (DefaultMutableTreeNode) enumeration
					.nextElement();

			if (currentTraversedNode.getLevel() == AttributesTreeModel.CONTEXT_TREE_DEPTH - 1) {
				Object[] currentPath = currentTraversedNode.getUserObjectPath();
				boolean isWanted = isWanted(_domains, currentPath);

				if (isWanted) {
					TreeNode[] obj = currentTraversedNode.getPath();
					TreePath path = new TreePath(obj);
					attributesToRemove.add(path);
				}
			}
		}

		this.removeAll();
		this.addSelectedAttibutes(attributesToRemove);
	}

	/**
	 * @param _domains
	 * @param currentPath
	 * @return 25 ao�t 2005
	 */
	private boolean isWanted(Vector<Domain> _domains, Object[] currentPath) {
		String dom = (String) (currentPath[1]);
		String fam = (String) (currentPath[2]);
		String mem = (String) (currentPath[3]);
		// String att = ( String ) ( currentPath[ 4 ] );

		Enumeration<Domain> e = _domains.elements();
		while (e.hasMoreElements()) {
			Domain d = e.nextElement();
			String d_name = d.getName();
			if (!d_name.equals(dom)) {
				continue;
			}

			Map<String, Family> _families = d.getFamilies();
			Iterator<Family> e1 = _families.values().iterator();
			while (e1.hasNext()) {
				Family f = e1.next();
				String f_name = f.getName();
				if (!f_name.equals(fam)) {
					continue;
				}

				Iterator<Member> e2 = f.getMembers().values().iterator();
				while (e2.hasNext()) {
					Member m = e2.next();
					String m_name = m.getName();
					if (!m_name.equals(mem)) {
						continue;
					} else {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void setAttributes(
			TreeMap<String, ArchivingConfigurationAttribute> in) {
		this.attributesHT = in;
	}

	public TreeMap<String, ArchivingConfigurationAttribute> getAttributes() {
		return this.attributesHT;
	}

}
