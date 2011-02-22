// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/BensikinFrame.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class BensikinFrame.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: BensikinFrame.java,v $
// Revision 1.5 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:35 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.JFrame;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.lifecycle.LifeCycleManager;
import fr.soleil.bensikin.tools.Messages;

/**
 * The top level container of the whole application, BensikinFrame is a
 * singleton.
 *
 * @author CLAISSE
 */
public class BensikinFrame extends JFrame {

	private static BensikinFrame bensikinFrameInstance = null;

	/**
	 * Used to instantiate the top level container.
	 *
	 * @param lifeCycleManager
	 *            The object in charge of startup and shutdown
	 * @return The instance
	 */
	public static BensikinFrame getInstance(LifeCycleManager lifeCycleManager) {
		if (bensikinFrameInstance == null) {
			bensikinFrameInstance = new BensikinFrame(lifeCycleManager);
		}

		return bensikinFrameInstance;
	}

	/**
	 * Used to get the already instantiated top level container.
	 *
	 * @return The instance
	 */
	public static BensikinFrame getInstance() {
		return bensikinFrameInstance;
	}

	/**
	 * Builds the top level container with the specified LifeCycleManager
	 * <UL>
	 * <LI>Sets up the application's title
	 * <LI>Sets up the application's menu bar (BensikinMenuBar)
	 * <LI>Sets up the application's content (BensikinMainPanel)
	 * <LI>Tells the application to do nothing on window closing, instead
	 * delegating shutdown to <code>lifeCycleManager</code>
	 * <LI>Sets up the application's window opening/closing events treatment
	 * (see <code>initOpeningListener (lifeCycleManager)</code>)
	 * </UL>
	 *
	 * @param lifeCycleManager
	 */
	private BensikinFrame(LifeCycleManager lifeCycleManager) {
		super();
		// String title = "Bensikin";
		String title = Messages.getAppMessage("project.name") + " v"
				+ Messages.getAppMessage("project.version");
		String name = Bensikin.getAccountManager().getSelectedAccountName();
		if (name != null) {
			setTitle(title + " - " + name);
		} else {
			setTitle(title);
		}

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.setJMenuBar(BensikinMenuBar.getInstance());
		this.getContentPane().add(BensikinMainPanel.getInstance());

		initOpeningListener(lifeCycleManager);
	}

	/**
	 * Sets up the application's window opening/closing events treatment.
	 * <UL>
	 * <LI>On window opening, lifeCycleManager.applicationStarted will be called
	 * <LI>On window closing, lifeCycleManager.applicationClosed will be called
	 * </UL>
	 *
	 * @param lifeCycleManager
	 */
	private void initOpeningListener(final LifeCycleManager lifeCycleManager) {
		final Hashtable openParameters = new Hashtable();
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
}
