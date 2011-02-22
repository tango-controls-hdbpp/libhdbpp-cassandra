/**
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.ViewPanel;
import fr.soleil.mambo.tools.Messages;

/**
 * @author MAINGUY
 *
 */
public class VCHideGeneralAction extends AbstractAction {

	private static VCHideGeneralAction instance = null;
	private boolean visible = true;
	private String name;

	/**
	 * @param name
	 */
	public static VCHideGeneralAction getInstance(String name) {
		if (instance == null) {
			instance = new VCHideGeneralAction(name);
		}

		return instance;
	}

	public static VCHideGeneralAction getInstance() {
		return instance;
	}

	public VCHideGeneralAction(String name) {
		instance = this;
		this.name = name;
		putValue(Action.NAME, this.name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			if (visible == true) {
				visible = false;
				ViewPanel.getInstance().hideGeneralPanel();
				name = Messages.getMessage("VIEW_ACTION_SHOW_GENERAL_VIEW");
				putValue(Action.NAME, name);
			}
			else {
				visible = true;
				ViewPanel.getInstance().showGeneralPanel();
				name = Messages.getMessage("VIEW_ACTION_HIDE_GENERAL_VIEW");
				putValue(Action.NAME, name);
			}
		}
		catch (Throwable t) {

		}
	}

	public boolean isVisible() {
		return visible;
	}

}
