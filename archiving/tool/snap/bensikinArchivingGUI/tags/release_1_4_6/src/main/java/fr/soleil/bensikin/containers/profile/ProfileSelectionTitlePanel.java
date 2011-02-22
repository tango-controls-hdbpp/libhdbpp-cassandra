//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/profile/ProfileSelectionTitlePanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionTitlePanel.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileSelectionTitlePanel.java,v $
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.profile;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


/**
 * @author SOLEIL
 */
public class ProfileSelectionTitlePanel extends JPanel
{

	private static ProfileSelectionTitlePanel instance;
	private JLabel titleLabel;

	public static ProfileSelectionTitlePanel getInstance()
	{
		if ( instance == null )
		{
			instance = new ProfileSelectionTitlePanel();
		}
		return instance;
	}

	private ProfileSelectionTitlePanel()
	{
		super();
		titleLabel = new JLabel(Messages.getMessage("PROFILE_CHOOSE") , JLabel.CENTER);
		//titleLabel.setIcon(titleIcon);
		this.setLayout(new SpringLayout());
		GUIUtilities.setObjectBackground(titleLabel , GUIUtilities.PROFILE_COLOR);
		this.add(titleLabel);
		SpringUtilities.makeCompactGrid
		        (this ,
		         1 , 1 , //rows, cols
		         0 , 0 , //initX, initY
		         0 , 0 , //xPad, yPad
		         true); //every component same size
		GUIUtilities.setObjectBackground(this , GUIUtilities.PROFILE_COLOR);
	}

}
