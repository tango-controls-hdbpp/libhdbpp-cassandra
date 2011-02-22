//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/VCPossibleAttributesTreeModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCPossibleAttributesTreeModel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCPossibleAttributesTreeModel.java,v $
// Revision 1.5  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.4  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.3  2006/06/28 12:32:12  ounsy
// db attributes buffering
//
// Revision 1.2  2005/11/29 18:27:08  chinkumo
// no message
//
// Revision 1.1.2.4  2005/09/26 07:52:25  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.models;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;


public class VCPossibleAttributesTreeModel extends AttributesTreeModel
{
    private static VCPossibleAttributesTreeModel instance = null;
    private boolean historic;

    /**
     * @param newModel
     * @return 8 juil. 2005
     */
    public static VCPossibleAttributesTreeModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new VCPossibleAttributesTreeModel();
        }

        return instance;
    }

    public static VCPossibleAttributesTreeModel forceGetInstance ( boolean _historic )
    {
        instance = new VCPossibleAttributesTreeModel( _historic );
        return instance;
    }

    /**
     * 
     */
    private VCPossibleAttributesTreeModel ()
    {
        super();
        //System.out.println ( "VCPossibleAttributesTreeModel/new" );
        IAttributeManager source = AttributeManagerFactory.getCurrentImpl();

        this.historic = true;
        this.build( source , null , true );

        setRootName( true );

    }

    private VCPossibleAttributesTreeModel ( boolean _historic )
    {
        super();
        //System.out.println ( "VCPossibleAttributesTreeModel/new/_historic/"+_historic+"/" );
        this.historic = _historic;

        IAttributeManager source = AttributeManagerFactory.getCurrentImpl();

        //--
        /*AttributesTab attributesTab = AttributesTab.getInstance ();
        String pattern = attributesTab.getRightRegexp ();
        Criterions searchCriterions = GUIUtilities.getAttributesSearchCriterions ( pattern );
        this.build ( source , searchCriterions , _historic );*/
        //--

        this.build( source , null , _historic );

        setRootName( _historic );
    }

    /**
     * @param b 26 ao�t 2005
     */
    public void setRootName ( boolean b )
    {
        DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) this.getRoot();
        if ( b )
        {
            root.setUserObject( "HDB" );
        }
        else
        {
            root.setUserObject( "TDB" );
        }

    }

    public void build ( IAttributeManager source , Criterions searchCriterions , boolean historic )
    {
        Vector _domains = null;
        setRootName( historic );
        try
        {
            _domains = source.loadDomains( searchCriterions , historic, false );
        }
        catch ( DevFailed e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( ArchivingException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        super.removeAll();
        super.build( _domains );
    }

    /**
     * @return Returns the historic.
     */
    public boolean isHistoric ()
    {
        return historic;
    }
}
