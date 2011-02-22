//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttributeDBProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationAttributeDBProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ArchivingConfigurationAttributeDBProperties.java,v $
// Revision 1.8  2006/11/20 09:36:37  ounsy
// removed the devFailed
//
// Revision 1.7  2006/10/19 12:40:38  ounsy
// modified pushDedicatedArchiver() to call pushDedicatedArchiver()
//
// Revision 1.6  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.5  2006/06/15 15:39:11  ounsy
// added support for dedicate archivers definition
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 12:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
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
package fr.soleil.mambo.data.archiving;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;

public class ArchivingConfigurationAttributeDBProperties {
	public static final String DEDICATED_ARCHIVER_PROPERTY_XML_TAG = "dedicatedArchiver";

	private String status;
	private Hashtable modes = new Hashtable();
	private boolean isEmpty = true;
	private String dedicatedArchiver = "";

	private ArchivingConfigurationAttributeProperties properties;

	public ArchivingConfigurationAttributeDBProperties() {
		modes = new Hashtable();
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public String toString() {
		String ret = "";

		if (modes != null) {
			Enumeration enumeration = modes.keys();
			while (enumeration.hasMoreElements()) {
				Integer nextKey = (Integer) enumeration.nextElement();
				ArchivingConfigurationMode nextValue = (ArchivingConfigurationMode) modes
						.get(nextKey);
				ret += nextValue.toString();
				ret += GUIUtilities.CRLF;
			}
		}

		return ret;
	}

	/**
	 * @return Returns the properties.
	 */
	public ArchivingConfigurationAttributeProperties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            The properties to set.
	 */
	public void setProperties(
			ArchivingConfigurationAttributeProperties properties) {
		this.properties = properties;
	}

	/**
	 * @return Returns the isArchived.
	 */
	/*
	 * public boolean isArchived() { return isArchived; }
	 */
	/**
	 * @param isArchived
	 *            The isArchived to set.
	 */
	/*
	 * public void setArchived(boolean isArchived) { this.isArchived =
	 * isArchived; }
	 */

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return Returns the modes.
	 */
	public ArchivingConfigurationMode[] getModes() {
		if (modes == null) {
			return null;
		}
		ArchivingConfigurationMode[] _modes = new ArchivingConfigurationMode[modes
				.size()];

		Enumeration enumeration = modes.keys();
		int i = 0;
		while (enumeration.hasMoreElements()) {
			Integer nextKey = (Integer) enumeration.nextElement();
			_modes[i] = (ArchivingConfigurationMode) modes.get(nextKey);
			i++;
		}

		return _modes;
	}

	public Vector getModesTypes() {
		if (modes == null) {
			return null;
		}

		Vector ret = new Vector(7);
		Enumeration enumeration = modes.keys();
		while (enumeration.hasMoreElements()) {
			ret.add(enumeration.nextElement());
		}

		return ret;
	}

	/**
	 * @param modes
	 *            The modes to set.
	 */
	public void setModes(ArchivingConfigurationMode[] _modes) {
		if (_modes == null) {
			this.modes = new Hashtable();
			return;
		}

		for (int i = 0; i < _modes.length; i++) {
			ArchivingConfigurationMode _currentMode = _modes[i];
			Integer type = new Integer(_currentMode.getType());
			this.modes.put(type, _currentMode);

			this.isEmpty = false;
		}
	}

	public void addMode(ArchivingConfigurationMode _mode) {
		// ArchivingConfiguration currentAC =
		// ArchivingConfiguration.getCurrentArchivingConfiguration();
		/*
		 * System.out.println (
		 * "-----------addMode REPERE 1 currentAC ------------" );
		 * System.out.println ( currentAC.toString () ); System.out.println (
		 * "----------addMode REPERE 1 currentAC  -------------" );
		 */

		this.modes.put(new Integer(_mode.getType()), _mode);

		this.isEmpty = false;

		// ArchivingConfiguration currentAC4 =
		// ArchivingConfiguration.getCurrentArchivingConfiguration ();
		/*
		 * System.out.println (
		 * "-----------addMode REPERE 2 currentAC ------------" );
		 * System.out.println ( currentAC4.toString () ); System.out.println (
		 * "-----------addMode REPERE 2 currentAC ------------" );
		 */
	}

	public void removeMode(int _modeToRemove) {
		// System.out.println (
		// "ArchivingConfigurationAttributeDBProperties/removeMode/_modeToRemove/"+_modeToRemove+"/this.modes/"+this.modes.size()+"/"
		// );

		/*
		 * Enumeration enumeration = this.modes.keys(); while (
		 * enumeration.hasMoreElements() ) { Integer next = ( Integer )
		 * enumeration.nextElement(); //System.out.println (
		 * "ArchivingConfigurationAttributeDBProperties/removeMode/next/"
		 * +next+"/" ); }
		 */

		Integer i = new Integer(_modeToRemove);
		this.modes.remove(i);
		this.isEmpty = this.modes.isEmpty();
	}

	/**
	 * @param type_p
	 * @return 25 juil. 2005
	 */
	public ArchivingConfigurationMode getMode(int _type) {
		return (ArchivingConfigurationMode) this.modes.get(new Integer(_type));
	}

	public Boolean hasMode(int _type) {
		if (modes == null) {
			return new Boolean(false);
		}

		Integer key = new Integer(_type);
		return new Boolean(modes.containsKey(key));
	}

	public void controlValues() throws ArchivingConfigurationException {
		ArchivingConfigurationMode[] modes = this.getModes();
		if (modes == null || modes.length > 0) {
			return;
		}
		int lg = modes.length;
		for (int i = 0; i < lg; i++) {
			ArchivingConfigurationMode mode = modes[i];
			mode.controlValues();
		}
	}

	public void setDedicatedArchiver(String _dedicatedArchiver) {
		this.dedicatedArchiver = _dedicatedArchiver;
	}

	/**
	 * @return Returns the dedicatedArchiver.
	 */
	public String getDedicatedArchiver() {
		return this.dedicatedArchiver;
	}

	public void push() {
		if (!Mambo.canChooseArchivers()) {
			return;
		}

		AttributesPropertiesPanel panel = AttributesPropertiesPanel
				.getInstance();

		if (panel != null) {
			panel.pushDefaultArchiver(this.getDedicatedArchiver());

			IArchivingManager manager = ArchivingManagerFactory
					.getCurrentImpl();
			boolean _historic = panel.isHistoric();

			try {
				String completeName = this.properties.getAttribute()
						.getCompleteName();

				Archiver currentArchiver = manager
						.getCurrentArchiverForAttribute(completeName, _historic);
				panel.pushCurrentArchiver(currentArchiver, completeName);
				panel.pushDedicatedArchiver(completeName);
				panel.pushChoiceArchiverColor(completeName);
			} catch (ArchivingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
