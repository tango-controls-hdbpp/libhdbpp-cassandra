//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/MamboMainPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboMainPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: MamboMainPanel.java,v $
// Revision 1.5  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.4  2006/02/27 09:34:58  ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.3  2005/12/15 11:20:04  ounsy
// "MamboToolbar" renamed as "MamboToolBar" + minor changes
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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
package fr.soleil.mambo.containers;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.components.MamboToolBar;
import fr.soleil.mambo.containers.archiving.ArchivingPanel;
import fr.soleil.mambo.containers.messages.MessagesPanel;
import fr.soleil.mambo.containers.view.ViewPanel;

public class MamboMainPanel extends JPanel {
	// private static final int INITIAL_MESSAGE_SPLIT_POSITION_HIRES = 800;
	// private static final int INITIAL_MESSAGE_SPLIT_POSITION_LORES = 540;
	// private static final int INITIAL_VIEW_SPLIT_POSITION_HIRES = 550;
	// private static final int INITIAL_VIEW_SPLIT_POSITION_LORES = 540;
	private static final int MESSAGE_SPLIT_DIVIDER_SIZE = 8;
	private static final int VIEW_SPLIT_DIVIDER_SIZE = 8;
	private static final double MESSAGE_SPLIT_RESIZE_WEIGHT = 1.0;
	private static final double VIEW_SPLIT_RESIZE_WEIGHT = 0.5;

	private int initialMessageSplitPosition;
	private int initialViewSplitPosition;

	private static MamboMainPanel instance = null;
	private JScrollPane messageScrollPane;

	/**
	 * @return 8 juil. 2005
	 */
	public static MamboMainPanel getInstance() {
		if (instance == null) {
			instance = new MamboMainPanel();
		}

		return instance;
	}

	/**
     * 
     */
	private MamboMainPanel() {
		this.initSplitsPositions();

		// START HORIZONTAL SPLIT
		JComponent topComponent;
		if (Mambo.hasACs()) {
			JSplitPane archivingSplit = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT, false);
			archivingSplit.setDividerSize(VIEW_SPLIT_DIVIDER_SIZE);
			archivingSplit.setResizeWeight(VIEW_SPLIT_RESIZE_WEIGHT);
			archivingSplit.setOneTouchExpandable(true);
			archivingSplit.setDividerLocation(initialViewSplitPosition);

			JScrollPane archivingScrollPane = new JScrollPane(ArchivingPanel
					.getInstance());
			JScrollPane viewScrollPane = new JScrollPane(ViewPanel
					.getInstance());
			archivingSplit.setLeftComponent(archivingScrollPane);
			archivingSplit.setRightComponent(viewScrollPane);

			topComponent = archivingSplit;
		} else {
			JScrollPane viewScrollPane = new JScrollPane(ViewPanel
					.getInstance());

			topComponent = viewScrollPane;
		}
		// END HORIZONTAL SPLIT

		// START VERTICAL SPLIT
		JSplitPane messageSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				true);
		messageSplit.setDividerSize(MESSAGE_SPLIT_DIVIDER_SIZE);
		messageSplit.setResizeWeight(MESSAGE_SPLIT_RESIZE_WEIGHT);
		messageSplit.setOneTouchExpandable(true);
		messageSplit.setDividerLocation(initialMessageSplitPosition);
		messageSplit.setTopComponent(topComponent);
		messageScrollPane = new JScrollPane(MessagesPanel.getInstance());
		messageSplit.setBottomComponent(messageScrollPane);

		JPanel mainGrid = new JPanel();
		mainGrid.setLayout(new GridLayout(1, 0));
		mainGrid.add(messageSplit);
		// END VERTICAL SPLIT

		// START MAIN PART
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel toolBarPane = new JPanel();
		toolBarPane.setLayout(new GridLayout(1, 0));
		toolBarPane.add(MamboToolBar.getInstance());
		toolBarPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		this.add(toolBarPane);
		this.add(mainGrid);
	}

	/**
	 * 14 sept. 2005
	 */
	private void initSplitsPositions() {
		Dimension dim = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().getSize();

		initialViewSplitPosition = (dim.width / 2)
				- (VIEW_SPLIT_DIVIDER_SIZE / 2);
		initialMessageSplitPosition = dim.height * 4 / 5
				- (MESSAGE_SPLIT_DIVIDER_SIZE / 2);
	}

	public void scrollDownToLatestMessage() {
		JViewport viewport = messageScrollPane.getViewport();
		Point point = new Point(0, 2000);
		Dimension dim = new Dimension();
		Rectangle contentRect = new Rectangle(point, dim);
		viewport.scrollRectToVisible(contentRect);
	}

}
