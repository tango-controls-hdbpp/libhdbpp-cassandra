//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/BensikinToolbar.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  BensikinToolbar.
//						(Claisse Laurent) - 13 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.9 $
//
//$Log: BensikinToolbar.java,v $
//Revision 1.9  2007/08/21 15:13:18  ounsy
//Print Snapshot as table or text (Mantis bug 3913)
//
//Revision 1.8  2006/11/29 09:57:25  ounsy
//minor changes
//
//Revision 1.7  2006/05/03 13:04:14  ounsy
//modified the limited operator rights
//
//Revision 1.6  2006/04/10 08:49:50  ounsy
//modified so that the "New" button doesn't appear in limited rights mode
//
//Revision 1.5  2005/11/29 18:25:08  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.PrintAction;
import fr.soleil.bensikin.actions.ResetAction;
import fr.soleil.bensikin.actions.SaveAllToDiskAction;
import fr.soleil.bensikin.actions.SaveToDiskAction;
import fr.soleil.bensikin.actions.context.NewContextAction;
import fr.soleil.bensikin.tools.Messages;

/**
 * The application's JMenuBar; a singleton class.
 * 
 * @author CLAISSE
 */
public class BensikinToolbar extends JMenuBar {
	private static BensikinToolbar instance;

	private ImageIcon newActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/new.gif"));
	private ImageIcon saveToDiskActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/save.gif"));
	private ImageIcon saveAllToDiskActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/save_all.gif"));
	private ImageIcon saveToDBActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/save.gif"));
	private ImageIcon saveAllToDBActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/save_all.gif"));
	private ImageIcon printActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/print_menu.gif"));
	private ImageIcon resetActionIcon = new ImageIcon(Bensikin.class
			.getResource("icons/trash_reset.gif"));

	private ToolBarButton newButton;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static BensikinToolbar getInstance() {
		if (instance == null) {
			instance = new BensikinToolbar();
		}

		return instance;
	}

	/**
	 * Builds the Toolbar
	 */
	private BensikinToolbar() {
		String newActionName = Messages.getMessage("TOOLBAR_NEW");
		String saveToDiskActionName = Messages
				.getMessage("TOOLBAR_SAVE_TO_DISK");
		String saveAllToDiskActionName = Messages
				.getMessage("TOOLBAR_SAVE_ALL_TO_DISK");
		// String saveToDBActionName =
		// Messages.getMessage("TOOLBAR_SAVE_TO_DB");
		// String saveAllToDBActionName =
		// Messages.getMessage("TOOLBAR_SAVE_ALL_TO_DB");
		String printActionName = Messages.getMessage("TOOLBAR_PRINT");
		String resetActionName = Messages.getMessage("TOOLBAR_RESET");

		String snapshotLabel = Messages.getMessage("MENU_SNAPSHOT");
		String contextLabel = Messages.getMessage("MENU_CONTEXT");

		Image newActionIconImage = newActionIcon.getImage();
		newActionIconImage = newActionIconImage.getScaledInstance(
				saveToDiskActionIcon.getIconWidth(), saveToDiskActionIcon
						.getIconHeight(), Image.SCALE_DEFAULT);
		newActionIcon.setImage(newActionIconImage);

		Image saveAllToDiskActionIconImage = saveAllToDiskActionIcon.getImage();
		saveAllToDiskActionIconImage = saveAllToDiskActionIconImage
				.getScaledInstance(saveToDiskActionIcon.getIconWidth(),
						saveToDiskActionIcon.getIconHeight(),
						Image.SCALE_DEFAULT);
		saveAllToDiskActionIcon.setImage(saveAllToDiskActionIconImage);

		Image saveToDBActionIconImage = saveToDBActionIcon.getImage();
		saveToDBActionIconImage = saveToDBActionIconImage.getScaledInstance(
				saveToDiskActionIcon.getIconWidth(), saveToDiskActionIcon
						.getIconHeight(), Image.SCALE_DEFAULT);
		saveToDBActionIcon.setImage(saveToDBActionIconImage);

		Image saveAllToDBActionIconImage = saveAllToDBActionIcon.getImage();
		saveAllToDBActionIconImage = saveAllToDBActionIconImage
				.getScaledInstance(saveToDiskActionIcon.getIconWidth(),
						saveToDiskActionIcon.getIconHeight(),
						Image.SCALE_DEFAULT);
		saveAllToDBActionIcon.setImage(saveAllToDBActionIconImage);

		Image printActionIconImage = printActionIcon.getImage();
		printActionIconImage = printActionIconImage.getScaledInstance(
				saveToDiskActionIcon.getIconWidth(), saveToDiskActionIcon
						.getIconHeight(), Image.SCALE_DEFAULT);
		printActionIcon.setImage(printActionIconImage);

		Image resetActionIconImage = resetActionIcon.getImage();
		resetActionIconImage = resetActionIconImage.getScaledInstance(
				saveToDiskActionIcon.getIconWidth(), saveToDiskActionIcon
						.getIconHeight(), Image.SCALE_DEFAULT);
		resetActionIcon.setImage(resetActionIconImage);

		NewContextAction newAction = new NewContextAction(newActionName,
				newActionIcon);
		// SaveToDiskAction saveToDiskAction = new
		// SaveToDiskAction(saveToDiskActionName , saveToDiskActionIcon ,
		// SaveToDiskAction.NO_PRESET_TYPE);
		SaveAllToDiskAction saveAllToDiskAction = new SaveAllToDiskAction(
				saveAllToDiskActionName, saveAllToDiskActionIcon);
		// PrintAction printAction = new PrintAction(printActionName ,
		// printActionIcon , PrintAction.NO_PRESET_TYPE);
		ResetAction resetAction = new ResetAction(resetActionName,
				resetActionIcon);

		newButton = new ToolBarButton(newAction, true);
		ToolBarMenu saveToDiskButton = new ToolBarMenu(saveToDiskActionName,
				saveToDiskActionIcon);
		JMenuItem saveToDiskButton_context = new JMenuItem(
				new SaveToDiskAction(contextLabel, null,
						SaveToDiskAction.CONTEXT_TYPE));
		JMenuItem saveToDiskButton_snapshot = new JMenuItem(
				new SaveToDiskAction(snapshotLabel, null,
						SaveToDiskAction.SNAPSHOT_TYPE));
		saveToDiskButton.add(saveToDiskButton_context);
		saveToDiskButton.add(saveToDiskButton_snapshot);

		ToolBarButton saveAllToDiskButton = new ToolBarButton(
				saveAllToDiskAction, true);
		ToolBarMenu printButton = new ToolBarMenu(printActionName,
				printActionIcon);
		JMenuItem print_context = new JMenuItem(new PrintAction(contextLabel,
				null, PrintAction.CONTEXT_TYPE));
		JMenuItem print_snapshot = new JMenuItem(new PrintAction(snapshotLabel,
				null, PrintAction.SNAPSHOT_TYPE));
		printButton.add(print_context);
		printButton.add(print_snapshot);

		ToolBarButton resetButton = new ToolBarButton(resetAction, true);

		this.add(newButton);
		this.add(saveToDiskButton);
		this.add(saveAllToDiskButton);
		this.add(printButton);
		this.add(resetButton);

		GUIUtilities.setObjectBackground(newButton, GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(saveToDiskButton,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(saveAllToDiskButton,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(printButton,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(resetButton,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(this, GUIUtilities.TOOLBAR_COLOR);

		GUIUtilities.setObjectBackground(saveToDiskButton_context,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(saveToDiskButton_snapshot,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(print_context,
				GUIUtilities.TOOLBAR_COLOR);
		GUIUtilities.setObjectBackground(print_snapshot,
				GUIUtilities.TOOLBAR_COLOR);
		// this.setBorder( BorderFactory.createLineBorder ( Color.YELLOW , 3 )
		// );
		this.repaint();
	}

	public void removeNewContextButton() {
		this.remove(newButton);
	}

	/**
	 * A toolbar item, whether it directly calls an action or not.
	 * 
	 * @author CLAISSE
	 */
	private class ToolBarButton extends JButton {
		/**
		 * @param a
		 *            The action to put on the button
		 * @param doSomething
		 *            True if the action has to be called upon button press;
		 *            false if the button has sub-menus
		 */
		public ToolBarButton(Action a, boolean doSomething) {
			super((Icon) a.getValue(Action.SMALL_ICON));

			String toolTip = (String) a.getValue(Action.SHORT_DESCRIPTION);
			if (toolTip == null) {
				toolTip = (String) a.getValue(Action.NAME);
			}

			if (toolTip != null) {
				setToolTipText(toolTip);
			}

			if (doSomething) {
				addActionListener(a);
			}
			addMouseListener(new ToolBarMouseListener());

			setMargin(new Insets(0, 3, 0, 3));
			setBorderPainted(false);
			setFocusable(false);
		}
	}

	/**
	 * A toolbar item, whether it directly calls an action or not.
	 * 
	 * @author CLAISSE
	 */
	private class ToolBarMenu extends JMenu {
		/**
		 * @param a
		 *            The action to put on the button
		 * @param doSomething
		 *            True if the action has to be called upon button press;
		 *            false if the button has sub-menus
		 */
		public ToolBarMenu(String text, ImageIcon icon) {
			super();

			setToolTipText(text);
			setIcon(icon);

			addMouseListener(new ToolBarMouseListener());
			setFocusable(false);

			setMargin(new Insets(0, 1, 0, 1));
			setBorderPainted(false);
			repaint();
		}
	}

	private class ToolBarMouseListener extends MouseAdapter {
		public ToolBarMouseListener() {

		}

		public void mouseEntered(MouseEvent e) {
			if (e.getSource() instanceof AbstractButton) {
				((AbstractButton) e.getSource()).setBackground(Color.darkGray);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (e.getSource() instanceof AbstractButton) {
				((AbstractButton) e.getSource()).setBackground(GUIUtilities
						.getToolBarColor());
			}
		}
	}

}
