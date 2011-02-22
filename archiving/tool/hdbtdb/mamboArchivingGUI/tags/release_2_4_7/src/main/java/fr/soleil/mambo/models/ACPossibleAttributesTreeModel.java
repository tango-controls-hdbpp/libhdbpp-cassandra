// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/models/ACPossibleAttributesTreeModel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ACPossibleAttributesTreeModel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ACPossibleAttributesTreeModel.java,v $
// Revision 1.5 2006/09/20 12:50:29 ounsy
// changed imports
//
// Revision 1.4 2006/07/18 10:30:28 ounsy
// possibility to reload tango attributes
//
// Revision 1.3 2006/05/29 15:48:26 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:08 chinkumo
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

import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.tango.util.entity.data.Domain;

public class ACPossibleAttributesTreeModel extends ACTreeModel {

	private static final long serialVersionUID = -4095094144274164408L;
	private static ACPossibleAttributesTreeModel instance = null;

	/**
	 * @param newModel
	 * @return 8 juil. 2005
	 */
	public static ACPossibleAttributesTreeModel getInstance() {
		if (instance == null) {
			instance = new ACPossibleAttributesTreeModel();
		}
		return instance;
	}

	/**
	 * @param newModel
	 * @return 8 juil. 2005
	 */
	public static ACPossibleAttributesTreeModel forceGetInstance() {
		instance = new ACPossibleAttributesTreeModel();
		return instance;
	}

	/**
     * 
     */
	private ACPossibleAttributesTreeModel() {
		super();
		this.build(TangoManagerFactory.getCurrentImpl(), new Criterions());
	}

	/**
	 * @param source
	 * @param searchCriterions
	 *            8 juil. 2005
	 */
	public void build(ITangoManager source, Criterions searchCriterions) {
		Vector<Domain> _domains = source.loadDomains(searchCriterions, false);
		super.build(_domains);
	}

}
