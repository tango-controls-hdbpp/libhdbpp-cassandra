//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingAttributesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingAttributesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ArchivingAttributesPanel.java,v $
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:18:56  ounsy
// modified for HDB/TDB separation
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.archiving;

import javax.swing.BorderFactory;
//import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;



public class ArchivingAttributesPanel extends JPanel
{
    private static ArchivingAttributesPanel instance = null;

    private ArchivingAttributesDetailPanel archivingAttributesDetailPanel;
    private ArchivingAttributesTreePanel archivingAttributesTreePanel;
    //private Box attributeDetailBox;

    private boolean historic;

    /**
     * @return 8 juil. 2005
     */
    public static ArchivingAttributesPanel getInstance ( boolean _historic )
    {
        if ( instance == null )
        {
            instance = new ArchivingAttributesPanel ( _historic );
            GUIUtilities.setObjectBackground( instance , GUIUtilities.ARCHIVING_COLOR );
        }

        return instance;
    }

    /**
     * 
     */
    private ArchivingAttributesPanel ( boolean _historic )
    {
        super();
        this.historic = _historic;
        
        initComponents();
        addComponents();
        setLayout();
        initBorder();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout ()
    {
        this.setLayout( new SpringLayout() );

        //Lay out the panel.
        SpringUtilities.makeCompactGrid( this ,
                                         1 , 2 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad

    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        this.add( archivingAttributesTreePanel );
        this.add( archivingAttributesDetailPanel );

    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        //String msg = "";

        /*ACAttributesTree tree = ACAttributesTree.getInstance ();
        JScrollPane scrollPane = new JScrollPane ( tree );*/
        archivingAttributesTreePanel = ArchivingAttributesTreePanel.getInstance();
        archivingAttributesDetailPanel = ArchivingAttributesDetailPanel.getInstance ( this.historic );
    }

    /**
     * 19 juil. 2005
     */
    private void initBorder ()
    {
        String msg = Messages.getMessage( "ARCHIVING_ATTRIBUTES_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );

    }

}
