//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/SnapshotCompareDialog.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SnapshotDetailTabbedPaneContent.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: soleilarc $
//
//$Revision: 1.5 $
//
//$Log: SnapshotCompareDialog.java,v $
//Revision 1.5  2007/10/26 13:05:04  soleilarc
//Author: XP
//Mantis bug ID: 6576
//Comment: Add a snapshotsLabel JLabel, to display the ID and the timestamp of the 2 snapshots. This JLabel is defined in the initComponents method, and added to the panel thanks to the addComponents method.
//
//Revision 1.4  2007/04/04 15:13:55  ounsy
//removed test classes
//
//Revision 1.3  2007/03/26 08:07:53  ounsy
//*** empty log message ***
//
//Revision 1.2  2006/06/28 12:48:56  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:25:08  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.snapshot.PrintSnapshotComparisonAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotCompareTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.tools.Messages;


/**
 * A JDialog that displays a comparison table between two snapshots.
 *
 * @author CLAISSE
 */
public class SnapshotCompareDialog extends JDialog
{
	private Snapshot firstSnapshot;
	private Snapshot secondSnapshot;
	//private Snapshot diffSnapshot;

	private boolean showRead;
	private boolean showWrite;
	private boolean showDelta;
	private boolean showDiff;

	private JLabel snapshotsLabel;
	private JScrollPane scrollpane;
	private Box myPanel;
	private Box buttonBox;
	private JButton cancelButton;
	private JButton printButton;
    private JButton saveColumnModelButton;
    private JButton loadColumnModelButton;

	private PrintSnapshotComparisonAction printAction;
    
	/**
	 * Builds the dialog with the data from the two snapshots.
	 *
	 * @param _firstSnapshot  The first snapshot of the comparison
	 * @param _secondSnapshot The second snapshot of the comparison
	 */
	public SnapshotCompareDialog(Snapshot _firstSnapshot , Snapshot _secondSnapshot)
	{
		super(BensikinFrame.getInstance() , Messages.getMessage("DIALOGS_COMPARE_TITLE") , true);
		this.setModal(false);

		this.firstSnapshot = _firstSnapshot;
		this.secondSnapshot = _secondSnapshot;

		this.setSizeAndLocation();
		this.initComponents();
		this.addComponents();
		this.initLayout();
	}


	/**
	 * Sets the size and location of the dialog.
	 */
	private void setSizeAndLocation()
	{
		Dimension dim = new Dimension(700 , 400);
		this.setSize(dim);
	}

	/**
	 * Inits the dialog's components.
	 */
	private void initComponents()
	{
		snapshotsLabel = new JLabel("Snapshot " + firstSnapshot.getSnapshotData().getId() + " : "
				+ firstSnapshot.getSnapshotData().getTime().toString()
				+ "    |    "
				+ "Snapshot " + secondSnapshot.getSnapshotData().getId() + " : "
				+ secondSnapshot.getSnapshotData().getTime().toString()
				);
		
		Options options = Options.getInstance();
		SnapshotOptions snapshotOptions = options.getSnapshotOptions();

		showRead = snapshotOptions.isShowRead();
		showWrite = snapshotOptions.isShowWrite();
		showDelta = snapshotOptions.isShowDelta();
		showDiff = snapshotOptions.isShowDiff();

		SnapshotCompareTable compareTable = new SnapshotCompareTable(showRead , showWrite , showDelta , showDiff);
		compareTable.build(this.firstSnapshot , this.secondSnapshot);

		scrollpane = new JScrollPane(compareTable);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setPreferredSize(new Dimension(650 , 300));

		String msg = Messages.getMessage("DIALOGS_COMPARE_CANCEL");
		cancelButton = new JButton(new CancelAction(msg , this));
		cancelButton.setPreferredSize(new Dimension(75 , 20));

		msg = Messages.getMessage("DIALOGS_COMPARE_PRINT");
		printAction = new PrintSnapshotComparisonAction(msg , compareTable);
		printButton = new JButton(printAction);
		printButton.setPreferredSize(new Dimension(75 , 20));

        /*msg = "Save cols";
        saveColumnModelAction = new SaveColumnModelAction(msg,compareTable);
        saveColumnModelButton = new JButton(saveColumnModelAction);
        saveColumnModelButton.setPreferredSize(new Dimension(75 , 20));
        
        msg = "Load cols";
        loadColumnModelAction = new LoadColumnModelAction(msg,compareTable);
        loadColumnModelButton = new JButton(loadColumnModelAction);
        loadColumnModelButton.setPreferredSize(new Dimension(75 , 20));*/
        
		myPanel = new Box(BoxLayout.Y_AXIS);
		buttonBox = new Box(BoxLayout.X_AXIS);
	}

	/**
	 * Does nothing
	 */
	private void initLayout()
	{

	}

	/**
	 * Adds the initialized components to the dialog.
	 */
	private void addComponents()
	{
		myPanel.add(snapshotsLabel);
		
		buttonBox.add(cancelButton);
		buttonBox.add(printButton);
        /*buttonBox.add(saveColumnModelButton);
        buttonBox.add(loadColumnModelButton);*/

		myPanel.add(scrollpane);
		myPanel.add(buttonBox);

		this.getContentPane().add(myPanel);
	}
}

