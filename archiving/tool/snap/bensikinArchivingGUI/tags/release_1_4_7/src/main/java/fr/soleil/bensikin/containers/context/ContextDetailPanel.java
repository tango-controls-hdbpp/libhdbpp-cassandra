//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextDetailPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextDetailPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: ContextDetailPanel.java,v $
// Revision 1.7  2006/01/12 10:27:48  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:21:39  ounsy
// added alternate selection mode
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;


/**
 * Contains all informations about the current context, via inclusion of
 * ContextDataPanel and ContextAttributesPanel.
 *
 * @author CLAISSE
 */
public class ContextDetailPanel extends JPanel
{
    private static ContextDetailPanel contextDetailPanelInstance = null;
    private boolean isAlternateSelectionMode;
    private JPanel contextAttributesPanel = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static ContextDetailPanel getInstance ()
    {
        if ( contextDetailPanelInstance == null )
        {
            contextDetailPanelInstance = new ContextDetailPanel();
        }

        return contextDetailPanelInstance;
    }

    /**
     * Builds the panel
     */
    private ContextDetailPanel ()
    {
        GUIUtilities.setObjectBackground(this, GUIUtilities.CONTEXT_COLOR);

        this.initLayout();


        boolean _hires = Bensikin.isHires();
        if ( !_hires )
        {
            //box.setMaximumSize( new Dimension( 350 , 100 ) );
            //box.setPreferredSize( new Dimension( 350 , 100 ) );
            this.setMaximumSize( new Dimension( 350 , 100 ) );
        }
        this.setPreferredSize( new Dimension( 350 , 100 ) );


        //this.add( ContextDataPanel.getInstance() , 0 );
        //this.add( ContextAttributesPanelAlternate.getInstance() , 1 );
        //this.add( ContextActionPanel.getInstance() , 2 );
        //this.add( ContextAttributesPanelAlternate.getInstance() );
        //this.add( new JLabel ("TOTO...") );

        //this.setLayout ( new BoxLayout ( this , BoxLayout.Y_AXIS ) );

//        initLayout();

        //GUIUtilities.addDebugBorderToPanel( this , true , Color.GREEN , 5 );

        //this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 100 ) );
    }

    /**
     *
     */
    private void initLayout()
    {
        String msg = Messages.getMessage("CONTEXT_DETAIL_BORDER");
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP, GUIUtilities
                        .getTitleFont());
        CompoundBorder cb = BorderFactory.createCompoundBorder(tb, BorderFactory.createEmptyBorder(2, 4, 4, 4));
        this.setBorder(cb);

        this.setLayout(new BorderLayout(5, 5));

		this.add(ContextDataPanel.getInstance(), BorderLayout.NORTH);
		this.add(ContextActionPanel.getInstance(), BorderLayout.SOUTH);

		if (isAlternateSelectionMode) {
			contextAttributesPanel = ContextAttributesPanelAlternate.getInstance();
		}
		else {
			contextAttributesPanel = ContextAttributesPanel.getInstance();
		}
		this.add(contextAttributesPanel, BorderLayout.CENTER);
    }

    /**
	 * @param isAlternateSelectionMode
	 *            The isAlternateSelectionMode to set.
	 */
    public void setAlternateSelectionMode(boolean _isAlternateSelectionMode) {
		if (isAlternateSelectionMode != _isAlternateSelectionMode) {
			this.remove(contextAttributesPanel);

			isAlternateSelectionMode = _isAlternateSelectionMode;

			if (isAlternateSelectionMode) {
				contextAttributesPanel = ContextAttributesPanelAlternate.getInstance();
			}
			else {
				contextAttributesPanel = ContextAttributesPanel.getInstance();
			}
			this.add(contextAttributesPanel, BorderLayout.CENTER);

			this.validate();
			this.repaint();
		}
	}
    /**
	 * @return Returns the isAlternateSelectionMode.
	 */
    public boolean isAlternateSelectionMode() {
        return isAlternateSelectionMode;
    }
}
