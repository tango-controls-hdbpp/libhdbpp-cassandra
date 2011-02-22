//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/open/SearchContextsInDBDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SearchContextsInDBDialog.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: SearchContextsInDBDialog.java,v $
// Revision 1.7  2006/06/28 12:49:12  ounsy
// minor changes
//
// Revision 1.6  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.sub.dialogs.open;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.context.SearchContextsAction;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.context.ContextListPanel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


/**
 * The JDialog where the contexts search criterions are filled.
 *
 * @author CLAISSE
 */
public class SearchContextsInDBDialog extends JDialog
{
	//private Dimension dim = new Dimension(300 , 300);
	private JPanel myPanel;
	private int numCriterions;

	/**
	 * The operators combo box for the id criterion
	 */
	public OperatorsList selectId;

	private OperatorsList selectSince;

	/**
	 * The operators combo box for the start time criterion
	 */
	public OperatorsList selectStartTime;

	/**
	 * The operators combo box for the end time criterion
	 */
	public OperatorsList selectEndTime;

	/**
	 * The operators combo box for the name criterion
	 */
	public OperatorsList selectName;

	/**
	 * The operators combo box for the author criterion
	 */
	public OperatorsList selectAuthor;

	/**
	 * The operators combo box for the reason criterion
	 */
	public OperatorsList selectReason;

	/**
	 * The operators combo box for the description criterion
	 */
	public OperatorsList selectDescription;

	/**
	 * The text field for the id criterion
	 */
	public JTextField textId;

	/**
	 * The text field for the start time criterion
	 */
	public JTextField textStartTime;

	/**
	 * The text field for the end time criterion
	 */
	public JTextField textEndTime;

	/**
	 * The text field for the name criterion
	 */
	public JTextField textName;

	/**
	 * The text field for the author criterion
	 */
	public JTextField textAuthor;

	/**
	 * The text field for the reason criterion
	 */
	public JTextField textReason;

	/**
	 * The text field for the description criterion
	 */
	public JTextField textDescription;

	private JButton filterButton;
	private JButton resetButton;
	private JButton cancelButton;

	private static SearchContextsInDBDialog instance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static SearchContextsInDBDialog getInstance()
	{
		if ( instance == null )
		{
			instance = new SearchContextsInDBDialog();
		}

		return instance;
	}

	/**
	 * Builds the dialog.
	 */
	private SearchContextsInDBDialog()
	{
		super(BensikinFrame.getInstance() , Messages.getMessage("DIALOGS_SEARCH_CONTEXT_TITLE") , true);

		this.initComponents();
		this.addComponents();
		this.initLayout();

		this.setSizeAndLocation();
	}

	/**
	 * Sets the size and location of the dialog.
	 */
	private void setSizeAndLocation()
	{
		Dimension dim = new Dimension(400 , 400);
		this.setSize(dim);
		this.setLocationRelativeTo(ContextListPanel.getInstance());
	}

	/**
	 * Inits the dialog's components.
	 */
	private void initComponents()
	{
		selectId = new OperatorsList(OperatorsList.ID_TYPE);
		selectSince = new OperatorsList(OperatorsList.SINCE_TYPE);
		selectStartTime = new OperatorsList(OperatorsList.AFTER_TIME_TYPE);
		selectEndTime = new OperatorsList(OperatorsList.BEFORE_TIME_TYPE);
		selectName = new OperatorsList(OperatorsList.COMMENT_TYPE);
		selectAuthor = new OperatorsList(OperatorsList.COMMENT_TYPE);
		selectReason = new OperatorsList(OperatorsList.COMMENT_TYPE);
		selectDescription = new OperatorsList(OperatorsList.COMMENT_TYPE);

		textId = new JTextField();
		textStartTime = new JTextField();
		textEndTime = new JTextField();
		textName = new JTextField();
		textAuthor = new JTextField();
		textReason = new JTextField();
		textDescription = new JTextField();

		textStartTime.setPreferredSize(new Dimension(70 , 20));
		textEndTime.setPreferredSize(new Dimension(70 , 20));

		String msg = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_SEARCH");
		filterButton = new JButton(new SearchContextsAction(msg , this));
		filterButton.setPreferredSize(new Dimension(75 , 20));

		String msg2 = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_RESET");
		resetButton = new JButton(new SearchContextsAction(msg2 , this));
		resetButton.setText(msg2);
		resetButton.setPreferredSize(new Dimension(70 , 20));

		String msg3 = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_CANCEL");
		cancelButton = new JButton(new CancelAction(msg3 , this));
		cancelButton.setPreferredSize(new Dimension(75 , 20));


	}

	/**
	 * Inits the dialog's layout.
	 */
	private void initLayout()
	{
		myPanel.setLayout(new SpringLayout());

		SpringUtilities.makeCompactGrid
		        (myPanel ,
		         numCriterions , 9 , //rows, cols
		         6 , 6 , //initX, initY
		         6 , 6 , //xPad, yPad
		         true);
	}


	/**
	 * Adds the initialized components to the dialog.
	 */
	private void addComponents()
	{
		String msgId = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_ID");
		String msgSince = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_SINCE");
		String msgTime = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_TIME");
		String msgName = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_NAME");
		String msgAuthor = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_AUTHOR");
		String msgReason = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_REASON");
		String msgDescription = Messages.getMessage("DIALOGS_SEARCH_CONTEXT_LABELS_DESCRIPTION");

		String[] labels =
		        {msgId, msgSince, msgTime, msgName, msgAuthor, msgReason, msgDescription};
		numCriterions = labels.length;
		Dimension emptyBoxDimension = new Dimension(40 , 20);

		myPanel = new JPanel();
		this.getContentPane().add(myPanel);

		//Create and populate the panel.
		for ( int i = 0 ; i < numCriterions ; i++ )
		{
			JLabel l = new JLabel(labels[ i ] , JLabel.TRAILING);

			//START COLUMN 1
			myPanel.add(l);
			//END COLUMN 1

			//START COLUMN 2
			switch ( i )
			{
				case 0: // "ID" line
					myPanel.add(selectId);
					break;

				case 1: // "Since" line
					myPanel.add(selectSince);
					break;

				case 2: // "Time" line
					myPanel.add(selectStartTime);
					break;

				case 3: // "Name" line
					myPanel.add(selectName);
					break;

				case 4: // "Author" line
					myPanel.add(selectAuthor);
					break;

				case 5: // "Reason" line
					myPanel.add(selectReason);
					break;

				case 6: // "Description" line
					myPanel.add(selectDescription);
					break;

				default: // we have a problem

					break;
			}
			//START COLUMN 2

			//START COLUMN 3
			switch ( i )
			{
				case 0: // "ID" line
					myPanel.add(textId);
					break;

				case 1: // "Since" line
					myPanel.add(Box.createRigidArea(emptyBoxDimension));
					break;

				case 2: // "Time" line
					myPanel.add(textStartTime);
					break;

				case 3: // "Name" line
					myPanel.add(textName);
					break;

				case 4: // "Author" line
					myPanel.add(textAuthor);
					break;

				case 5: // "Reason" line
					myPanel.add(textReason);
					break;

				case 6: // "Description" line
					myPanel.add(textDescription);
					break;

				default: // we have a problem

					break;
			}
			//START COLUMN 3


			//START COLUMN 4
			if ( i == 2 )
			{
				myPanel.add(selectEndTime);
			}
			else
			{
				myPanel.add(Box.createRigidArea(emptyBoxDimension));
			}
			//END COLUMN 4

			//START COLUMN 5
			if ( i == 2 )
			{
				myPanel.add(textEndTime);
			}
			else
			{
				myPanel.add(Box.createRigidArea(emptyBoxDimension));
			}
			//END COLUMN 5


			//START COLUMN 6
			myPanel.add(Box.createRigidArea(emptyBoxDimension));
			//END COLUMN 6

			//START COLUMN 7
			if ( i == 6 )
			{
				myPanel.add(cancelButton);
			}
			else
			{
				myPanel.add(Box.createRigidArea(emptyBoxDimension));
			}
			//END COLUMN 7

			//START COLUMN 7
			if ( i == 6 )
			{
				myPanel.add(filterButton);
			}
			else
			{
				myPanel.add(Box.createRigidArea(emptyBoxDimension));
			}
			//END COLUMN 7

			//START COLUMN 8
			if ( i == 6 )
			{
				myPanel.add(resetButton);
			}
			else
			{
				myPanel.add(Box.createRigidArea(emptyBoxDimension));
			}
			//END COLUMN 8
		}
	}


	/**
	 * Resets the values of all text fields and combo boxes
	 */
	public void resetFields()
	{
		selectId.setSelectedItem(OperatorsList.NO_SELECTION);
		selectStartTime.setSelectedItem(OperatorsList.NO_SELECTION);
		selectEndTime.setSelectedItem(OperatorsList.NO_SELECTION);
		selectName.setSelectedItem(OperatorsList.NO_SELECTION);
		selectAuthor.setSelectedItem(OperatorsList.NO_SELECTION);
		selectReason.setSelectedItem(OperatorsList.NO_SELECTION);
		selectDescription.setSelectedItem(OperatorsList.NO_SELECTION);

		textId.setText("");
		textStartTime.setText("");
		textEndTime.setText("");
		textName.setText("");
		textAuthor.setText("");
		textReason.setText("");
		textDescription.setText("");
	}

	/**
	 * @return Returns the resetButton.
	 */
	public JButton getResetButton()
	{
		return resetButton;
	}
}
