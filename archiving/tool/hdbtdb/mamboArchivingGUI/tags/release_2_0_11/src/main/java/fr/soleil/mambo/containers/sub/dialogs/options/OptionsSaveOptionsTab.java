//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsSaveOptionsTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsSaveOptionsTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: OptionsSaveOptionsTab.java,v $
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
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
package fr.soleil.mambo.containers.sub.dialogs.options;

//import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.SaveOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class OptionsSaveOptionsTab extends JPanel
{
    private static OptionsSaveOptionsTab instance = null;

    private JRadioButton yesButton;
    private JRadioButton noButton;
    private ButtonGroup buttonGroup;

    /**
     * @return 8 juil. 2005
     */
    public static OptionsSaveOptionsTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsSaveOptionsTab();
        }

        return instance;
    }

    public static void resetInstance ()
    {
        if ( instance != null )
        {
            instance.repaint();
        }

        instance = null;
    }

    /**
     *
     */
    private OptionsSaveOptionsTab() {
		this.initComponents();
		this.initLayout();
	}

    /**
	 * 5 juil. 2005
	 */
    private void initLayout() {
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		buttonBox.add(yesButton);
		buttonBox.add(noButton);

		Box saveHistoryPanel = new Box(BoxLayout.X_AXIS);
		saveHistoryPanel.add(buttonBox);
		saveHistoryPanel.add(Box.createHorizontalGlue());

		String msg = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.TOP, GUIUtilities.getTitleFont());
		CompoundBorder cb = BorderFactory.createCompoundBorder(tb, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		saveHistoryPanel.setBorder(cb);

		// ---

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.add(saveHistoryPanel);
		this.add(Box.createVerticalGlue());
	}

    /**
	 * 5 juil. 2005
	 */
    private void initComponents() {
		String msgYes = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_YES");
		yesButton = new JRadioButton(msgYes, true);
		yesButton.setActionCommand(String.valueOf(SaveOptions.HISTORY_YES));

		String msgNo = Messages.getMessage("DIALOGS_OPTIONS_SAVE_SAVEONSHUTDOWN_NO");
		noButton = new JRadioButton(msgNo, true);
		noButton.setActionCommand(String.valueOf(SaveOptions.HISTORY_NO));

		buttonGroup = new ButtonGroup();
		buttonGroup.add(yesButton);
		buttonGroup.add(noButton);
	}

    /**
	 * @return 8 juil. 2005
	 */
    public ButtonGroup getButtonGroup ()
    {
        return buttonGroup;
    }

    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectHasSaveButton ( int hasSave )
    {
        switch ( hasSave )
        {
            case SaveOptions.HISTORY_YES:
                yesButton.setSelected( true );
                break;

            case SaveOptions.HISTORY_NO:
                noButton.setSelected( true );
                break;
        }
    }
}
