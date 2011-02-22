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
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.options.sub.GeneralOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class OptionsGeneralTab extends JPanel
{
    private static OptionsGeneralTab instance = null;

    private JComboBox separatorBox;
    private JLabel separatorLabel;
    private String[] separatorList = {";", "\t", "|"};
    private String[] separatorTitleList = {";", "TAB", "|"};
    
    private JRadioButton bufferTangoAttributesYes;
    private JRadioButton bufferTangoAttributesNo;
    private ButtonGroup bufferTangoAttributesButtonGroup;
    private Box bufferTangoAttributesBox;
    
    private JPanel tablePanel;

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
        this.addComponents();
        this.initLayout();
    }
    
    private void initLayout() 
    {
        this.setLayout(new SpringLayout());
        
        String msg = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_TABLE_TITLE");
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP);
        Border border = (Border) (tb);
        tablePanel.setBorder(border);
        tablePanel.setLayout(new SpringLayout());
        
        SpringUtilities.makeCompactGrid( this ,
                2 , 1 , // rows, cols
                5 , 5 , // initX, initY
                5 , 5 , // xPad, yPad
                true ); // every component same size
        SpringUtilities.makeCompactGrid( tablePanel ,
                1 , 2 , // rows, cols
                5 , 5 , // initX, initY
                5 , 5 , // xPad, yPad
                false ); // every component same size
    }

    private void addComponents() 
    {
        tablePanel.add(separatorLabel);
        tablePanel.add(separatorBox);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        
        
        bufferTangoAttributesBox = new Box( BoxLayout.Y_AXIS );
        bufferTangoAttributesBox.add( bufferTangoAttributesYes );
        bufferTangoAttributesBox.add( Box.createVerticalStrut( 5 ) );
        bufferTangoAttributesBox.add( bufferTangoAttributesNo );
        String msg = Messages.getMessage( "DIALOGS_OPTIONS_GENERAL_BUFFER_TANGO_ATTRIBUTES_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.DEFAULT_JUSTIFICATION ,
                  TitledBorder.TOP ,
                  GUIUtilities.getTitleFont() );
        bufferTangoAttributesBox.setBorder( tb );
        
        
        this.add(tablePanel);
        this.add(bufferTangoAttributesBox);
        this.add(Box.createVerticalGlue());
    }

    private void initComponents() 
    {
        tablePanel = new JPanel();
        
        separatorBox = new JComboBox();
        String msg = Messages.getMessage("DIALOGS_OPTIONS_GENERAL_TABLE_SEPARATOR");
        separatorLabel = new JLabel(msg);
        for (int i = 0; i < separatorTitleList.length; i++) 
        {
            separatorBox.addItem(separatorTitleList[i]);
        }
        separatorBox.setSelectedIndex(0);
        
//---
        bufferTangoAttributesButtonGroup = new ButtonGroup();

        msg = Messages.getMessage( "DIALOGS_OPTIONS_GENERAL_BUFFER_TANGO_ATTRIBUTES_YES" );
        bufferTangoAttributesYes = new JRadioButton( msg , true );
        bufferTangoAttributesYes.setActionCommand( String.valueOf( GeneralOptions.BUFFER_TANGO_ATTRIBUTES_YES ) );

        msg = Messages.getMessage( "DIALOGS_OPTIONS_GENERAL_BUFFER_TANGO_ATTRIBUTES_NO" );
        bufferTangoAttributesNo = new JRadioButton( msg , false );
        bufferTangoAttributesNo.setActionCommand( String.valueOf( GeneralOptions.BUFFER_TANGO_ATTRIBUTES_NO ) );

        bufferTangoAttributesButtonGroup.add( bufferTangoAttributesYes );
        bufferTangoAttributesButtonGroup.add( bufferTangoAttributesNo );
        //--
    }
    
    public String getSeparator() {
        return separatorList[separatorBox.getSelectedIndex()];
    }

    public void setSeparator(String separator) {
        for (int i = 0; i < separatorList.length; i++) {
            if (separatorList[i].equals(separator)) {
                separatorBox.setSelectedIndex(i);
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
