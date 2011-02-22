//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsVCTab.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.5 $
//
//$Log: OptionsVCTab.java,v $
//Revision 1.5  2007/05/10 14:48:16  ounsy
//possibility to change "no value" String in chart data file (default is "*") through vc option
//
//Revision 1.4  2006/08/31 08:45:19  ounsy
//forceExport is now false on defaults
//
//Revision 1.3  2006/07/13 12:50:50  ounsy
//added optionnal doForceTdbExport
//
//Revision 1.2  2006/05/19 15:05:29  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:28:13  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.sub.dialogs.options;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.VCOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class OptionsVCTab extends JPanel
{
    private static OptionsVCTab instance = null;

    private JRadioButton displayWriteYes;
    private JRadioButton displayWriteNo;
    private ButtonGroup writeButtonGroup;

    private JRadioButton displayReadYes;
    private JRadioButton displayReadNo;
    private ButtonGroup readButtonGroup;

    private JRadioButton forceTdbExportYes;
    private JRadioButton forceTdbExportNo;
    private ButtonGroup forceTdbExportButtonGroup;

    private JLabel stackDepthLabel;
    private JTextField stackDepthField;

    private JLabel noValueLabel;
    private JComboBox noValueCombo;
    protected final static String[]  noValueStringChoice = {"*",".","-","+","#","X","!"};


    /**
     * @return 8 juil. 2005
     */
    public static OptionsVCTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsVCTab();
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

    private void initLayout() {
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		buttonBox.add(displayReadYes);
		buttonBox.add(displayReadNo);

		Box displayReadBox = new Box(BoxLayout.X_AXIS);
		displayReadBox.add(buttonBox);
		displayReadBox.add(Box.createHorizontalGlue());

		String msg1 = Messages.getMessage("DIALOGS_OPTIONS_VC_DISPLAY_READ_BORDER");
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg1,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, GUIUtilities.getTitleFont());
		CompoundBorder cb1 = BorderFactory.createCompoundBorder(tb1, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		displayReadBox.setBorder(cb1);

		// ---

		buttonBox = new Box(BoxLayout.Y_AXIS);
		buttonBox.add(displayWriteYes);
		buttonBox.add(displayWriteNo);

		Box displayWriteBox = new Box(BoxLayout.X_AXIS);
		displayWriteBox.add(buttonBox);
		displayWriteBox.add(Box.createHorizontalGlue());

		String msg2 = Messages.getMessage("DIALOGS_OPTIONS_VC_DISPLAY_WRITE_BORDER");
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg2,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, GUIUtilities.getTitleFont());
		CompoundBorder cb2 = BorderFactory.createCompoundBorder(tb2, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		displayWriteBox.setBorder(cb2);

		// ---

		buttonBox = new Box(BoxLayout.Y_AXIS);
		buttonBox.add(forceTdbExportYes);
		buttonBox.add(forceTdbExportNo);

		Box forceTdbExportBox = new Box(BoxLayout.X_AXIS);
		forceTdbExportBox.add(buttonBox);
		forceTdbExportBox.add(Box.createHorizontalGlue());

		String msg3 = Messages.getMessage("DIALOGS_OPTIONS_VC_FORCE_TDB_EXPORT_BORDER");
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg3,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, GUIUtilities.getTitleFont());
		CompoundBorder cb3 = BorderFactory.createCompoundBorder(tb3, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		forceTdbExportBox.setBorder(cb3);

		// ---

		Box miscBox = new Box(BoxLayout.X_AXIS);
		miscBox.add(stackDepthLabel);
		miscBox.add(Box.createRigidArea(new Dimension(5, 0)));
		miscBox.add(stackDepthField);
		miscBox.add(Box.createHorizontalGlue());

		stackDepthField.setPreferredSize(new Dimension(50, stackDepthField.getPreferredSize().height));
		stackDepthField.setMaximumSize(new Dimension(50, stackDepthField.getPreferredSize().height));

		String msg4 = Messages.getMessage("DIALOGS_OPTIONS_VC_MISC_BORDER");
		TitledBorder tb4 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg4,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
		CompoundBorder cb4 = BorderFactory.createCompoundBorder(tb4, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		miscBox.setBorder(cb4);

		// ---

		Box chartPropertiesBox = new Box(BoxLayout.X_AXIS);
		chartPropertiesBox.add(noValueLabel);
		chartPropertiesBox.add(Box.createRigidArea(new Dimension(5, 0)));
		chartPropertiesBox.add(noValueCombo);
		chartPropertiesBox.add(Box.createHorizontalGlue());

		noValueCombo.setPreferredSize(new Dimension(40, noValueCombo.getPreferredSize().height));
		noValueCombo.setMaximumSize(new Dimension(40, noValueCombo.getPreferredSize().height));

		String msg5 = Messages.getMessage("DIALOGS_OPTIONS_VC_CHART_BORDER");
		TitledBorder tb5 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg5,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
		CompoundBorder cb5 = BorderFactory.createCompoundBorder(tb5, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		chartPropertiesBox.setBorder(cb5);

		// ---

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.add(displayReadBox);
		this.add(Box.createVerticalStrut(4));
		this.add(displayWriteBox);
		this.add(Box.createVerticalStrut(4));
		this.add(forceTdbExportBox);
		this.add(Box.createVerticalStrut(4));
		this.add(miscBox);
		this.add(Box.createVerticalStrut(4));
		this.add(chartPropertiesBox);
		this.add(Box.createVerticalGlue());
	}


    /**
	 *
	 */
    private OptionsVCTab() {
		this.initComponents();
		this.initLayout();
	}

    /**
	 *
	 */
    private void initComponents() {
		String msg = Messages.getMessage("DIALOGS_OPTIONS_VC_DISPLAY_READ_YES");
		displayReadYes = new JRadioButton(msg, true);
		displayReadYes.setActionCommand(String.valueOf(VCOptions.DISPLAY_READ_YES));

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_DISPLAY_READ_NO");
		displayReadNo = new JRadioButton(msg, false);
		displayReadNo.setActionCommand(String.valueOf(VCOptions.DISPLAY_READ_NO));

		readButtonGroup = new ButtonGroup();
		readButtonGroup.add(displayReadYes);
		readButtonGroup.add(displayReadNo);

		// ---

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_DISPLAY_WRITE_YES");
		displayWriteYes = new JRadioButton(msg, true);
		displayWriteYes.setActionCommand(String.valueOf(VCOptions.DISPLAY_WRITE_YES));

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_DISPLAY_WRITE_NO");
		displayWriteNo = new JRadioButton(msg, false);
		displayWriteNo.setActionCommand(String.valueOf(VCOptions.DISPLAY_WRITE_NO));

		writeButtonGroup = new ButtonGroup();
		writeButtonGroup.add(displayWriteYes);
		writeButtonGroup.add(displayWriteNo);

		// ---

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_FORCE_TDB_EXPORT_YES");
		forceTdbExportYes = new JRadioButton(msg, true);
		forceTdbExportYes.setActionCommand(String.valueOf(VCOptions.FORCE_TDB_EXPORT_YES));

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_FORCE_TDB_EXPORT_NO");
		forceTdbExportNo = new JRadioButton(msg, false);
		forceTdbExportNo.setActionCommand(String.valueOf(VCOptions.FORCE_TDB_EXPORT_NO));
		forceTdbExportNo.setSelected(true);

		forceTdbExportButtonGroup = new ButtonGroup();
		forceTdbExportButtonGroup.add(forceTdbExportYes);
		forceTdbExportButtonGroup.add(forceTdbExportNo);

		// ---

		stackDepthField = new JTextField();

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_MISC_STACK_DEPTH");
		stackDepthLabel = new JLabel(msg);
		stackDepthLabel.setLabelFor(stackDepthField);

		// ---

		noValueCombo = new JComboBox();
		for (int i = 0; i < noValueStringChoice.length; i++) {
			noValueCombo.addItem(noValueStringChoice[i]);
		}

		msg = Messages.getMessage("DIALOGS_OPTIONS_VC_CHART_NO_VALUE_STRING");
		noValueLabel = new JLabel(msg);
		noValueLabel.setLabelFor(noValueCombo);
    }

    /**
	 * @return 8 juil. 2005
	 */
    public ButtonGroup getWriteButtonGroup ()
    {
        return writeButtonGroup;
    }


    /**
	 * 20 juil. 2005
	 *
	 * @param plaf
	 */
    public void selectDisplayWrite ( int displayCase )
    {
        switch ( displayCase )
        {
            case VCOptions.DISPLAY_WRITE_YES:
                displayWriteYes.setSelected( true );
                break;

            case VCOptions.DISPLAY_WRITE_NO:
                displayWriteNo.setSelected( true );
                break;
        }
    }


    /**
     * @return Returns the displayWriteNo.
     */
    public JRadioButton getDisplayWriteNo ()
    {
        return displayWriteNo;
    }

    /**
     * @return Returns the displayWriteYes.
     */
    public JRadioButton getDisplayWriteYes ()
    {
        return displayWriteYes;
    }

    /**
     * @return 8 juil. 2005
     */
    public ButtonGroup getReadButtonGroup ()
    {
        return readButtonGroup;
    }


    /**
     * 20 juil. 2005
     *
     * @param plaf
     */
    public void selectDisplayRead ( int displayCase )
    {
        switch ( displayCase )
        {
            case VCOptions.DISPLAY_READ_YES:
                displayReadYes.setSelected( true );
                break;

            case VCOptions.DISPLAY_READ_NO:
                displayReadNo.setSelected( true );
                break;
        }
    }

    public void selectForceTdbExport ( int displayCase )
    {
        //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/displayCase/"+displayCase );
        switch ( displayCase )
        {
            case VCOptions.FORCE_TDB_EXPORT_YES:
                //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/0" );
                forceTdbExportYes.setSelected( true );
                break;

            case VCOptions.FORCE_TDB_EXPORT_NO:
                //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/1" );
                forceTdbExportNo.setSelected( true );
                break;

            default:
                //System.out.println ( "CLA/OptionsVCTab/selectForceTdbExport/2" );
                forceTdbExportNo.setSelected( true );
            break;
        }
    }



    /**
     * @return Returns the displayReadNo.
     */
    public JRadioButton getDisplayReadNo ()
    {
        return displayReadNo;
    }

    /**
     * @return Returns the displayReadYes.
     */
    public JRadioButton getDisplayReadYes ()
    {
        return displayReadYes;
    }

    /**
     * @return
     */
    public String getStackDepth ()
    {
        return stackDepthField.getText();
    }

    /**
     * @param stackDepth_s
     */
    public void setStackDepth ( String stackDepth_s )
    {
        stackDepthField.setText( stackDepth_s );
    }

    /**
     * @return Returns the forceTdbExportButtonGroup.
     */
    public ButtonGroup getForceTdbExportButtonGroup() {
        return forceTdbExportButtonGroup;
    }

    public void setNoValueString (String noValueString) {
        for (int i = 0; i < noValueCombo.getItemCount(); i++) {
            if ( noValueCombo.getItemAt(i).equals(noValueString ) ) {
                noValueCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    public String getNoValueString() {
        return noValueCombo.getSelectedItem().toString();
    }
}
