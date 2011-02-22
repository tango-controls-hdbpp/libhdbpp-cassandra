//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/profile/manager/XMLProfileManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLProfileManager.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: XMLProfileManager.java,v $
// Revision 1.3  2006/12/12 13:17:49  ounsy
// minor changees
//
// Revision 1.2  2006/06/28 12:54:28  ounsy
// minor changes
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
package fr.soleil.bensikin.profile.manager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.bensikin.containers.sub.dialogs.BensikinErrorDialog;
import fr.soleil.bensikin.profile.Profile;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.xml.XMLLine;
import fr.soleil.bensikin.xml.XMLUtils;


/**
 * The real Profile Manager
 *
 * @author SOLEIL
 */
class XMLProfileManager implements IProfileManager
{

	private final static String profileFileName = "bensikin.profile";
	private final static String lockFileName = ".lock";
	private final static String mainNodeTag = "BensikinProfiles";
	private String profileFilePath;
	private String lockFilePath;
	private Hashtable profiles;
	private int selectedProfile;

	/**
	 * Represents the fact that the profiles map file could not be created
	 */
	public final static int creationFailed = 1;

	/**
	 * Represents the fact that the profiles map file could not be written,
	 * but not because of a lock.
	 */
	public final static int writeFailed = 2;

	/**
	 * Represents the fact that the profiles map file could not be read
	 */
	public final static int readFailed = 3;

	/**
	 * Represents the fact that the profiles map file is locked for writing
	 */
	public final static int usedFailed = 4;

	/**
	 * Represents the fact that the temporary lock file could not be written
	 */
	public final static int lockFailed = 5;

	/**
	 * Represents the fact that the temporary lock file could not be removed
	 */
	public final static int removeLockFailed = 6;

	/**
	 * Represents the fact that an unexpected exception happened
	 */
	public final static int unknown = 7;

	/**
	 * Constructor - sets every necessary information for this manager
	 * to be able to load the profile list
	 *
	 * @param mainFolderPath The path of the application start folder
	 */
	public XMLProfileManager(String mainFolderPath)
	{
		this.profileFilePath = mainFolderPath
		                       + File.separator
		                       + profileFileName;
		this.lockFilePath = mainFolderPath
		                    + File.separator
		                    + lockFileName;
		this.profiles = new Hashtable();
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#loadProfiles()
	 */
	public int loadProfiles()
	{

		File profileFile = new File(profileFilePath);
		File lockFile = new File(lockFilePath);

		//if the profile configurration file does not exist, create it
		if ( !profileFile.exists() )
		{
			try
			{
				boolean lockFileCreated = lockFile.createNewFile();
				if ( !lockFileCreated )
				{
					showFailedMessage(creationFailed);
					return creationFailed;
				}
				boolean profileFileCreated = profileFile.createNewFile();
				if ( !profileFileCreated )
				{
					showFailedMessage(creationFailed);
					return creationFailed;
				}
				XMLLine emptyLine = new XMLLine(mainNodeTag , XMLLine.EMPTY_TAG_CATEGORY);
				XMLUtils.save(emptyLine.toString() , profileFilePath);
				try
				{
					lockFile.delete();
				}
				catch ( SecurityException security )
				{
					showFailedMessage(removeLockFailed);
					return removeLockFailed;
				}
			}
			catch ( SecurityException security )
			{
				showFailedMessage(creationFailed);
				return creationFailed;
			}
			catch ( IOException io )
			{
				io.printStackTrace();
				showFailedMessage(writeFailed);
				return writeFailed;
			}
			catch ( Exception e )
			{
				e.printStackTrace();
				showFailedMessage(unknown);
				return unknown;
			}
		}

		//Loading start
		try
		{
			Node rootNode = XMLUtils.getRootNode(profileFile);
			if ( rootNode.hasChildNodes() )
			{
				NodeList profileNodes = rootNode.getChildNodes();
				this.profiles = new Hashtable();

				for ( int i = 0 ; i < profileNodes.getLength() ; i++ )
				{
					//as many loops as there are profile sub-blocks in the saved file
					Node currentProfileNode = profileNodes.item(i);
					if ( XMLUtils.isAFakeNode(currentProfileNode) )
					{
						continue;
					}
					Hashtable profileAttrs = XMLUtils.loadAttributes(currentProfileNode);
					String idAsString = ( String ) profileAttrs.get(Profile.ID_TAG);
					String name = ( String ) profileAttrs.get(Profile.NAME_TAG);
					String path = ( String ) profileAttrs.get(Profile.PATH_TAG);
					Integer id = Integer.valueOf(idAsString);
					this.profiles.put(id , new Profile(id.intValue() , name , path));
				}
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			showFailedMessage(unknown);
			return unknown;
		}

		//Loading successfull
		return 0;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bensikin.bensikin.profile.manager.IProfileManager#addProfile(java.lang.String,
	 *      java.lang.String)
	 */
	public int addProfile(String name , String path)
	{

		File lockFile = new File(lockFilePath);
		try
		{
			boolean lockFileCreated = lockFile.createNewFile();
			if ( !lockFileCreated )
			{
				showFailedMessage(usedFailed);
			}
			//be sure to be up to date
			int reload = this.loadProfiles();
			switch ( reload )
			{

				//no error on reload : add profile, save and remove lock
				case 0:
					int id = this.getNewId();
					this.profiles.put(new Integer(id) ,
					                  new Profile(id , name , path));
					this.saveProfiles();
					try
					{
						lockFile.delete();
					}
					catch ( SecurityException security )
					{
						showFailedMessage(removeLockFailed);
						System.exit(removeLockFailed);
					}
					this.setSelectedProfile(id);
					return id;

					//file locked : try again later
				case usedFailed:
					break;

					//any other exception : this was not expected, exit
				default :
					System.exit(reload);

			}
		}
		catch ( SecurityException security )
		{
			showFailedMessage(usedFailed);
		}
		catch ( IOException io )
		{
			io.printStackTrace();
			showFailedMessage(writeFailed);
			System.exit(writeFailed);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			showFailedMessage(unknown);
			System.exit(unknown);
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see bensikin.bensikin.profile.manager.IProfileManager#deleteProfile(int)
	 */
	public void deleteProfile(int profileId)
	{

		File lockFile = new File(lockFilePath);
		try
		{
			boolean lockFileCreated = lockFile.createNewFile();
			if ( !lockFileCreated )
			{
				showFailedMessage(usedFailed);
			}
			//be sure to be up to date
			int reload = this.loadProfiles();
			switch ( reload )
			{

				//no error on reload : remove profile, save and remove lock
				case 0:
					this.profiles.remove(new Integer(profileId));
					this.saveProfiles();
					try
					{
						lockFile.delete();
					}
					catch ( SecurityException security )
					{
						showFailedMessage(removeLockFailed);
						System.exit(removeLockFailed);
					}
					break;

					//file locked : try again later
				case usedFailed:
					break;

					//any other exception : this was not expected, exit
				default :
					System.exit(reload);

			}
		}
		catch ( SecurityException security )
		{
			showFailedMessage(usedFailed);
		}
		catch ( IOException io )
		{
			io.printStackTrace();
			showFailedMessage(writeFailed);
			System.exit(writeFailed);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			showFailedMessage(unknown);
			System.exit(unknown);
		}

	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#getProfiles()
	 */
	public Profile[] getProfiles()
	{
		Vector profileList = new Vector();
		profileList.addAll(profiles.keySet());
		Collections.sort(profileList);
		Profile[] profileArray = new Profile[ profileList.size() ];
		for ( int i = 0 ; i < profileArray.length ; i++ )
		{
			profileArray[ i ] = ( Profile ) profiles.get(profileList.get(i));
		}
		return profileArray;
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#getNewId()
	 */
	public int getNewId()
	{
		int id = 0;
		Profile[] profileArray = this.getProfiles();
		if ( profileArray.length > 0 )
		{
			id = profileArray[ profileArray.length - 1 ].getId() + 1;
		}
		return id;
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#getSelectedProfile()
	 */
	public int getSelectedProfile()
	{
		return this.selectedProfile;
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#setSelectedProfile(int)
	 */
	public void setSelectedProfile(int id)
	{
		this.selectedProfile = id;
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#getSelectedProfilePath()
	 */
	public String getSelectedProfilePath()
	{
		if ( this.getSelectedProfile() == -1 )
		{
			return null;
		}
		else
		{
			return ( ( Profile ) profiles.get(new Integer(this.getSelectedProfile())) ).getPath();
		}
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.profile.manager.IProfileManager#getSelectedProfileName()
	 */
	public String getSelectedProfileName()
	{
		if ( this.getSelectedProfile() == -1 )
		{
			return null;
		}
		else
		{
			return ( ( Profile ) profiles.get(new Integer(this.getSelectedProfile())) ).getName();
		}
	}

	private void showFailedMessage(int failureType)
	{
		String title = Messages.getMessage("PROFILE_ERROR_TITLE");
		String message = "";
		switch ( failureType )
		{
			case creationFailed:
				message = Messages.getMessage("PROFILE_FILE_CREATION_ERROR");
				break;

			case writeFailed:
				message = Messages.getMessage("PROFILE_FILE_WRITE_ERROR");
				break;

			case readFailed:
				message = Messages.getMessage("PROFILE_FILE_READ_ERROR");
				break;

			case usedFailed:
				message = Messages.getMessage("PROFILE_FILE_USED_ERROR");
				break;

			case lockFailed:
				message = Messages.getMessage("PROFILE_FILE_LOCK_ERROR");
				break;

			case removeLockFailed:
				message = Messages.getMessage("PROFILE_FILE_REMOVE_LOCK_ERROR");
				break;

			default :
				message = Messages.getMessage("PROFILE_ERROR_DEFAULT");
		}
		BensikinErrorDialog dialog = new BensikinErrorDialog(title , message);
		dialog.setVisible ( true );
	}

	private void saveProfiles() throws Exception
	{
		//File profileFile = new File(profileFilePath);
		Profile[] profileArray = this.getProfiles();
		XMLLine openingLine = new XMLLine(mainNodeTag , XMLLine.OPENING_TAG_CATEGORY);
		String content = openingLine.toString();
		for ( int i = 0 ; i < profileArray.length ; i++ )
		{
			content += GUIUtilities.CRLF + "    " + profileArray[ i ].toXMLString();
		}
		XMLLine closingLine = new XMLLine(mainNodeTag , XMLLine.CLOSING_TAG_CATEGORY);
		content += GUIUtilities.CRLF + closingLine.toString();
		XMLUtils.save(content , profileFilePath);
	}

}
