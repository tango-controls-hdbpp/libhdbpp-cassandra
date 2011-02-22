//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/profile/ProfileSelectionListPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionListPanel.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ProfileSelectionListPanel.java,v $
// Revision 1.2  2006/06/08 09:18:46  ounsy
// "show profile path" option added
//
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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.profile.ProfileDeleteAction;
import fr.soleil.bensikin.actions.profile.ProfileValidateAction;
import fr.soleil.bensikin.containers.sub.dialogs.ProfileSelectionDialog;
import fr.soleil.bensikin.profile.Profile;
import fr.soleil.bensikin.profile.manager.IProfileManager;
import fr.soleil.bensikin.profile.manager.ProfileManagerFactory;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


/**
 * @author SOLEIL
 */
public class ProfileSelectionListPanel extends JPanel
{

	private static ProfileSelectionListPanel instance;

    private String path;
    private JLabel chooseLabel, pathLabel;
    private JCheckBox showPath;
	private ImageIcon chooseIcon = new ImageIcon(Bensikin.class.getResource("icons/profile.gif"));
	private JComboBox chooseCombo;
    private final static Font pathFont = new Font("Arial", Font.ITALIC, 11) ;

	public static ProfileSelectionListPanel getInstance()
	{
		if ( instance == null )
		{
			instance = new ProfileSelectionListPanel();
		}
		return instance;
	}

	private ProfileSelectionListPanel()
	{
		super();
        JPanel choosePanel = new JPanel();
        GUIUtilities.setObjectBackground( choosePanel , GUIUtilities.PROFILE_COLOR );
		chooseLabel = new JLabel("");
		chooseLabel.setIcon(chooseIcon);
        GUIUtilities.setObjectBackground( chooseLabel , GUIUtilities.PROFILE_COLOR );
		chooseCombo = new JComboBox();
		GUIUtilities.setObjectBackground(chooseCombo , GUIUtilities.PROFILE_COLOR);
        pathLabel = new JLabel(Messages.getMessage("PROFILE_PATH_NOT_DEFINED"));
        GUIUtilities.setObjectBackground( pathLabel , GUIUtilities.PROFILE_COLOR );
        showPath = new JCheckBox(Messages.getMessage("PROFILE_SHOW_PATH"));
        GUIUtilities.setObjectBackground( showPath , GUIUtilities.PROFILE_COLOR );
        showPath.setFont(pathFont);
		reload();
        choosePanel.setLayout( new SpringLayout() );
        choosePanel.add( chooseLabel );
        choosePanel.add( chooseCombo );
		SpringUtilities.makeCompactGrid
                ( choosePanel ,
                  1 , choosePanel.getComponentCount() , //rows, cols
		         6 , 6 , //initX, initY
		         6 , 6 , //xPad, yPad
		         true); //every component same size

        this.setLayout( new SpringLayout() );
        this.add(choosePanel);
        this.add(showPath);
        showPath.addActionListener(new VisiblePathAdapter());
        chooseCombo.addActionListener(new SetPathAdapter());
		GUIUtilities.setObjectBackground(this , GUIUtilities.PROFILE_COLOR);
        updateLayout();
	}

	/**
	 * reloads profile list
	 */
	public void reload()
	{
		IProfileManager manager = ProfileManagerFactory.getCurrentImpl();
		manager.loadProfiles();
		Profile[] profiles = manager.getProfiles();
		chooseCombo.removeAllItems();
		for ( int i = 0 ; i < profiles.length ; i++ )
		{
			chooseCombo.addItem(profiles[ i ]);
			if ( manager.getSelectedProfile() == profiles[ i ].getId() )
			{
				chooseCombo.setSelectedItem(profiles[ i ]);
			}
		}
		if ( profiles.length == 0 )
		{
			ProfileValidateAction.getInstance().setEnabled(false);
			ProfileDeleteAction.getInstance().setEnabled(false);
		}
		else
		{
			ProfileValidateAction.getInstance().setEnabled(true);
			ProfileDeleteAction.getInstance().setEnabled(true);
		}
        pathString();
        pathLabel.setText(path);
        pathLabel.setToolTipText(path);
	}

	/**
     * updates the panel's layout
     */
    public void updateLayout()
    {
        SpringUtilities.makeCompactGrid
        ( this ,
          this.getComponentCount() , 1 , //rows, cols
          6 , 6 , //initX, initY
          6 , 6 , //xPad, yPad
          true ); //every component same size
    }

    /**
     * A convenient method to prepare the path label
     * @return The String to be displayed in path label
     */
    public String pathString()
    {
        path = Messages.getMessage("PROFILE_PATH_NOT_DEFINED");
        if (chooseCombo != null)
        {
            Profile selected = (Profile)chooseCombo.getSelectedItem();
            if (selected != null)
            {
                path = selected.getPath();
                if (path == null)
                {
                    path = Messages.getMessage("PROFILE_PATH_NOT_DEFINED");
                }
            }
        }
        return path;
    }

    /**
	 * Returns the selected Profile
	 *
	 * @return The selected Profile
	 */
	public int getSelectedProfile()
	{
		if ( chooseCombo.getSelectedItem() != null )
		{
			return ( ( Profile ) chooseCombo.getSelectedItem() ).getId();
		}
		return -1;
	}

    private class VisiblePathAdapter implements ActionListener
    {

        public VisiblePathAdapter()
        {

        }
        public void actionPerformed (ActionEvent e)
        {
            boolean selected = showPath.isSelected();
            if (selected)
            {
                ProfileSelectionListPanel.getInstance().add(pathLabel);
                ProfileSelectionDialog.getInstance().setSize(500, 250);
            }
            else
            {
                ProfileSelectionListPanel.getInstance().remove(pathLabel);
                ProfileSelectionDialog.getInstance().setSize(500, 230);
            }
            ProfileSelectionListPanel.getInstance().updateLayout();
            System.gc();
        }
    }

    private class SetPathAdapter implements ActionListener
    {

        public SetPathAdapter()
        {
        }

        public void actionPerformed (ActionEvent e)
        {
            pathString();
            pathLabel.setText(path);
            pathLabel.setToolTipText(path);
        }
        
    }

}
