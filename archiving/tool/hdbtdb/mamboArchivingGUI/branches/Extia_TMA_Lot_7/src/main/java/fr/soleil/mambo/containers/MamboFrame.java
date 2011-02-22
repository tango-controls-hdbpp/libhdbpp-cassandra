// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/MamboFrame.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class MamboFrame.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: MamboFrame.java,v $
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.JFrame;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.components.MamboMenuBar;
import fr.soleil.mambo.lifecycle.LifeCycleManager;
import fr.soleil.mambo.tools.Messages;

public class MamboFrame extends JFrame {

	private static MamboFrame instance = null;

	/**
	 * @param lifeCycleManager
	 * @return 8 juil. 2005
	 */
	public static MamboFrame getInstance(LifeCycleManager lifeCycleManager) {
		if (instance == null) {
			instance = new MamboFrame(lifeCycleManager);
		}

		return instance;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static MamboFrame getInstance() {
		return instance;
	}

	/**
	 * @param lifeCycleManager
	 */
	private MamboFrame(LifeCycleManager lifeCycleManager) {
		super();
		initParameters();

		addMamboComponents();
		initOpeningListener(lifeCycleManager);
	}

	/**
	 * 22 juin 2005
	 *
	 * @param lifeCycleManager
	 */
	private void initOpeningListener(final LifeCycleManager lifeCycleManager) {
		final Hashtable openParameters = new Hashtable();
		// openParameters.put( "BENSIKIN_FRAME" , this );
		// openParameters.put( "WINDOW_SIZE" , new Integer (
		// JFrame.MAXIMIZED_BOTH ) );

		final Hashtable closeParameters = new Hashtable();

		this.addWindowListener(new WindowAdapter() {

			public void windowOpened(WindowEvent e) {
				lifeCycleManager.applicationStarted(openParameters);
			}

			public void windowClosing(WindowEvent e) {
				lifeCycleManager.applicationClosed(closeParameters);
			}
		});

	}

	/**
	 * 10 juin 2005
	 */
	private void initParameters() {
		// setTitle ( "Mambo" );
		// String title = "Mambo";
		String title = Messages.getAppMessage("project.name") + " v"
				+ Messages.getAppMessage("project.version");

		String name = Mambo.getAccountManager().getSelectedAccountName();
		if (name != null) {
			setTitle(title + " - " + name);
		} else {
			setTitle(title);
		}
	}

	/**
	 * 8 juil. 2005
	 */
	private void addMamboComponents() {
		this.setJMenuBar(MamboMenuBar.getInstance());

		this.getContentPane().add(MamboMainPanel.getInstance());

	}

}
