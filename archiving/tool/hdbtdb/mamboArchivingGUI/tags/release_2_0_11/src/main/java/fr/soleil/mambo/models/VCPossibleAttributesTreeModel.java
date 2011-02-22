// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/models/VCPossibleAttributesTreeModel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCPossibleAttributesTreeModel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCPossibleAttributesTreeModel.java,v $
// Revision 1.5 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.4 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.3 2006/06/28 12:32:12 ounsy
// db attributes buffering
//
// Revision 1.2 2005/11/29 18:27:08 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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

import javax.swing.tree.DefaultMutableTreeNode;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.tango.util.entity.data.Domain;

public class VCPossibleAttributesTreeModel extends VCTreeModel {

    private static final long serialVersionUID = -5522683349038886896L;
    private boolean           historic;

    public VCPossibleAttributesTreeModel() {
        this(true);
    }

    public VCPossibleAttributesTreeModel(boolean _historic) {
        super();
        setHistoric(_historic);
    }

    public void setRootName(boolean b) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getRoot();
        if (b) {
            root.setUserObject("HDB");
        }
        else {
            root.setUserObject("TDB");
        }
    }

    public void build(IAttributeManager source, Criterions searchCriterions,
            boolean historic) {
        Vector<Domain> domains = null;
        setRootName(historic);
        try {
            domains = source.loadDomains(searchCriterions, historic, false);
        }
        catch (DevFailed e) {
            e.printStackTrace();
        }
        catch (ArchivingException e) {
            e.printStackTrace();
        }
        removeAll();
        super.build(domains);
    }

    public void setHistoric(boolean historic) {
        this.historic = historic;
        build(AttributeManagerFactory.getCurrentImpl(), null, historic);
        setRootName(historic);
        reload();
    }

    /**
     * @return Returns the historic.
     */
    public boolean isHistoric() {
        return historic;
    }
}
