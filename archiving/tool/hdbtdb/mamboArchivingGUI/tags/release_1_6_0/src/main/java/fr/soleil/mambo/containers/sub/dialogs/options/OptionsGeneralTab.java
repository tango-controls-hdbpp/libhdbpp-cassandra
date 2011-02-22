//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/options/OptionsGeneralTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsGeneralTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OptionsGeneralTab.java,v $
// Revision 1.2  2006/05/16 12:00:05  ounsy
// added a Tango buffering option
//
// Revision 1.1  2005/12/15 11:28:53  ounsy
// "copy table to clipboard" management
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

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.GeneralOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class OptionsGeneralTab extends JPanel
{
    private static OptionsGeneralTab instance = null;

    private JLabel separatorLabel;
    private JComboBox separatorCombo;
    private String[] separatorList = {";", "\t", "|"};
    private String[] separatorTitleList = {";", "TAB", "|"};

    private JRadioButton bufferTangoAttributesYes;
    private JRadioButton bufferTangoAttributesNo;
    private ButtonGroup bufferTangoAttributesButtonGroup;

    /**
     * @return 8 juil. 2005
     */
    public static OptionsGeneralTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new OptionsGeneralTab();
        }

        return instance;
    }

    private OptionsGeneralTab()
    {
        this.initComponents();
        this.initLayout();
    }

    private void initLayout() {
		Box tableBox = new Box(BoxLayout.X_AXIS);
		tableBox.add(separatorLabel);
		tableBox.add(Box.createRigidArea(new Dimension(5, 0)));
		tableBox.add(separatorCombo);
		tableBox.add(Box.createHorizontalGlue());

		separatorCombo.setPreferredSize(new Dimension(100, separatorCombo.getPreferredSize().height));
		separatorCombo.setMaximumSize(new Dimension(100, separatorCombo.getPreferredSize().height));

		String msg1 = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_TABLE_TITLE");
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg1,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP);
		CompoundBorder cb1 = BorderFactory.createCompoundBorder(tb1, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		tableBox.setBorder(cb1);

		// ---

		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		buttonBox.add(bufferTangoAttributesYes);
		buttonBox.add(bufferTangoAttributesNo);

		Box bufferTangoAttributesBox = new Box(BoxLayout.X_AXIS);
		bufferTangoAttributesBox.add(buttonBox);
		bufferTangoAttributesBox.add(Box.createHorizontalGlue());

		String msg2 = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_BUFFER_TANGO_ATTRIBUTES_BORDER");
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg2,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, GUIUtilities.getTitleFont());
		CompoundBorder cb2 = BorderFactory.createCompoundBorder(tb2, BorderFactory
				.createEmptyBorder(2, 4, 4, 4));
		bufferTangoAttributesBox.setBorder(cb2);

		// ---

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		this.add(tableBox);
		this.add(Box.createVerticalStrut(4));
		this.add(bufferTangoAttributesBox);
		this.add(Box.createVerticalGlue());
	}


    private void initComponents() {
		separatorCombo = new JComboBox();
		for (int i = 0; i < separatorTitleList.length; i++) {
			separatorCombo.addItem(separatorTitleList[i]);
		}
		separatorCombo.setSelectedIndex(0);

		String msg = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_TABLE_SEPARATOR");
		separatorLabel = new JLabel(msg);
		separatorLabel.setLabelFor(separatorCombo);

		// ---

		msg = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_BUFFER_TANGO_ATTRIBUTES_YES");
		bufferTangoAttributesYes = new JRadioButton(msg, true);
		bufferTangoAttributesYes.setActionCommand(String
				.valueOf(GeneralOptions.BUFFER_TANGO_ATTRIBUTES_YES));

		msg = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_BUFFER_TANGO_ATTRIBUTES_NO");
		bufferTangoAttributesNo = new JRadioButton(msg, false);
		bufferTangoAttributesNo.setActionCommand(String
				.valueOf(GeneralOptions.BUFFER_TANGO_ATTRIBUTES_NO));

		bufferTangoAttributesButtonGroup = new ButtonGroup();
		bufferTangoAttributesButtonGroup.add(bufferTangoAttributesYes);
		bufferTangoAttributesButtonGroup.add(bufferTangoAttributesNo);
	}

    public String getSeparator() {
        return separatorList[separatorCombo.getSelectedIndex()];
    }

    public void setSeparator(String separator) {
        for (int i = 0; i < separatorList.length; i++) {
            if (separatorList[i].equals(separator)) {
                separatorCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * @return Returns the bufferTangoAttributesButtonGroup.
     */
    public ButtonGroup getBufferTangoAttributesButtonGroup() {
        return bufferTangoAttributesButtonGroup;
    }

    public void setBufferTangoAttributes(boolean b)
    {
        //System.out.println ( "CLA/setBufferTangoAttributes/b/"+b );
        if ( b )
        {
            bufferTangoAttributesYes.setSelected ( true );
        }
        else
        {
            bufferTangoAttributesNo.setSelected ( true );
        }
    }
}
