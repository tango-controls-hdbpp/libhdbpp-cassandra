//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AxisPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AxisPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: AxisPanel.java,v $
// Revision 1.8  2007/03/07 14:47:14  ounsy
// correction of Mantis bug 3273 (chart properties panel size)
//
// Revision 1.7  2006/11/06 09:28:05  ounsy
// icons reorganization
//
// Revision 1.6  2006/10/02 14:13:25  ounsy
// minor changes (look and feel)
//
// Revision 1.5  2006/07/28 10:07:13  ounsy
// icons moved to "icons" package
//
// Revision 1.4  2006/06/20 08:40:27  ounsy
// by default, y1 is visible and autoscaled
//
// Revision 1.3  2005/12/15 11:31:34  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.containers.view.dialogs;


import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.soleil.mambo.actions.view.listeners.YAxisMouseListener;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class AxisPanel extends JPanel
{

    /*private JLAxis  pAxis;
    private JLChart pChart;*/
    protected int type;

    protected JPanel scalePanel;
    protected JPanel settingPanel;

    protected JLabel MinLabel;
    protected JTextField MinText;
    protected JLabel MaxLabel;
    protected JTextField MaxText;
    protected JCheckBox AutoScaleCheck;

    protected JLabel ScaleLabel;
    protected JComboBox ScaleCombo;
    protected JCheckBox SubGridCheck;
    protected JCheckBox VisibleCheck;
    protected JCheckBox OppositeCheck;

    protected JComboBox FormatCombo;
    protected JLabel FormatLabel;

    protected JLabel TitleLabel;
    protected JTextField TitleText;

    protected JLabel ColorLabel;
    protected JLabel ColorView;
    protected JButton ColorBtn;

    protected JLabel PositionLabel;
    protected JComboBox PositionCombo;

    public final static int Y1_TYPE = 1;
    public final static int Y2_TYPE = 2;
    public final static int X_TYPE = 3;

    final static Color fColor = new Color( 99 , 97 , 156 );
    final static Insets zInset = new Insets( 0 , 0 , 0 , 0 );
    final static Font labelFont = new Font( "Dialog" , Font.PLAIN , 12 );
    final static Font labelbFont = new Font( "Dialog" , Font.BOLD , 12 );


    protected AxisPanel ( int axisType )
    {
        type = axisType;
        setLayout( null );

        scalePanel = new JPanel();
        scalePanel.setLayout( null );
        //scalePanel.setBorder(GraphicsUtils.createTitleBorder("Scale"));
        String msg;

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE" );
        scalePanel.setBorder( GUIUtilities.getPlotSubPanelsEtchedBorder( msg ) );

        settingPanel = new JPanel();
        settingPanel.setLayout( null );
        //settingPanel.setBorder(GraphicsUtils.createTitleBorder("Axis settings"));
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS" );
        settingPanel.setBorder( GUIUtilities.getPlotSubPanelsEtchedBorder( msg ) );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE_MIN" );
        MinLabel = new JLabel( msg );
        MinLabel.setFont( labelFont );
        MinText = new JTextField();
        MinLabel.setForeground( fColor );
        /*MinLabel.setEnabled(!a.isAutoScale());
        MinText.setText(Double.toString(a.getMinimum()));*/
        MinText.setEditable( true );
        /*MinText.setEnabled(!a.isAutoScale());
        MinText.addKeyListener(this);*/

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE_MAX" );
        MaxLabel = new JLabel( msg );
        MaxLabel.setFont( labelFont );
        MaxText = new JTextField();
        MaxLabel.setForeground( fColor );
        MaxLabel.setHorizontalAlignment( JLabel.RIGHT );
        //MaxLabel.setEnabled(!a.isAutoScale());
        //MaxText.setText(Double.toString(a.getMaximum()));
        MaxText.setEditable( true );
        //MaxText.setEnabled(!a.isAutoScale());
        //MaxText.addKeyListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE_AUTOSCALE" );
        AutoScaleCheck = new JCheckBox( msg );
        AutoScaleCheck.setFont( labelFont );
        AutoScaleCheck.setForeground( fColor );
        //AutoScaleCheck.setSelected(a.isAutoScale());
        //AutoScaleCheck.addActionListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE_MODE" );
        ScaleLabel = new JLabel( msg );
        ScaleLabel.setFont( labelFont );
        ScaleLabel.setForeground( fColor );
        ScaleCombo = new JComboBox();
        ScaleCombo.setFont( labelFont );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE_MODE_LINEAR" );
        ScaleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_SCALE_MODE_LOGARITHMIC" );
        ScaleCombo.addItem( msg );
        //ScaleCombo.setSelectedIndex(a.getScale());
        //ScaleCombo.addActionListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_SHOWSUBGRID" );
        SubGridCheck = new JCheckBox( msg );
        SubGridCheck.setFont( labelFont );
        SubGridCheck.setForeground( fColor );
        //SubGridCheck.setSelected(a.isSubGridVisible());
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_SHOWSUBGRID_TOOLTIP" );
        SubGridCheck.setToolTipText( msg );
        //SubGridCheck.addActionListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_VISIBLE" );
        VisibleCheck = new JCheckBox( msg );
        VisibleCheck.setFont( labelFont );
        VisibleCheck.setForeground( fColor );
        //VisibleCheck.setSelected(a.isVisible());
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_VISIBLE_TOOLTIP" );
        VisibleCheck.setToolTipText( msg );
        //VisibleCheck.addActionListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_DRAW_OPPOSITE" );
        OppositeCheck = new JCheckBox( msg );
        OppositeCheck.setFont( labelFont );
        OppositeCheck.setForeground( fColor );
        //OppositeCheck.setSelected(a.isDrawOpposite());
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_DRAW_OPPOSITE_TOOLTIP" );
        OppositeCheck.setToolTipText( msg );
        //OppositeCheck.addActionListener(this);

        FormatCombo = new JComboBox();
        FormatCombo.setFont( labelFont );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_AUTOMATIC" );
        FormatCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_SCIENTIFIC" );
        FormatCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_TIME" );
        FormatCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_DECIMALINT" );
        FormatCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_HEXADECIMALINT" );
        FormatCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_BINARYINT" );
        FormatCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_SCIENTIFICINT" );
        FormatCombo.addItem( msg );
        /*FormatCombo.setSelectedIndex(a.getLabelFormat());
        FormatCombo.addActionListener(this);*/

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT" );
        FormatLabel = new JLabel( msg );
        FormatLabel.setFont( labelFont );
        FormatLabel.setForeground( fColor );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_TITLE" );
        TitleLabel = new JLabel( msg );
        TitleLabel.setFont( labelFont );
        TitleLabel.setForeground( fColor );
        TitleText = new JTextField();
        TitleText.setEditable( true );
        //TitleText.setText(a.getName());
        //TitleText.addKeyListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_COLOR" );
        ColorLabel = new JLabel( msg );
        ColorLabel.setFont( labelFont );
        ColorLabel.setForeground( fColor );
        ColorView = new JLabel( "" );
        ColorView.setOpaque( true );
        ColorView.setBorder( BorderFactory.createLineBorder( Color.black ) );
        ColorView.setBackground( Color.RED );
        ColorBtn = new JButton( "..." );
        ColorBtn.addMouseListener( new YAxisMouseListener( type ) );
        ColorBtn.setMargin( zInset );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION" );
        PositionLabel = new JLabel( msg );
        PositionLabel.setFont( labelFont );
        PositionLabel.setForeground( fColor );
        PositionCombo = new JComboBox();
        PositionCombo.setFont( labelFont );
        switch ( type )
        {

            case X_TYPE:
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_DOWN" );
                PositionCombo.addItem( msg );
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_UP" );
                PositionCombo.addItem( msg );
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_Y1ORIGIN" );
                PositionCombo.addItem( msg );
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_Y2ORIGIN" );
                PositionCombo.addItem( msg );
                //PositionCombo.setSelectedIndex(a.getPosition()-1);
                break;

            case Y1_TYPE:
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_LEFT" );
                PositionCombo.addItem( msg );
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_XORIGIN" );
                PositionCombo.addItem( msg );
                //PositionCombo.setSelectedIndex((a.getPosition() == JLAxis.VERTICAL_ORG) ? 1 : 0);
                VisibleCheck.setSelected(true);
                AutoScaleCheck.setSelected(true);
                break;

            case Y2_TYPE:
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_RIGHT" );
                PositionCombo.addItem( msg );
                msg = Messages.getMessage( "DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_XORIGIN" );
                PositionCombo.addItem( msg );
                //PositionCombo.setSelectedIndex((a.getPosition() == JLAxis.VERTICAL_ORG) ? 1 : 0);
                break;

        }
        //PositionCombo.addActionListener(this);

        scalePanel.add( MinLabel );
        scalePanel.add( MinText );
        scalePanel.add( MaxLabel );
        scalePanel.add( MaxText );
        scalePanel.add( AutoScaleCheck );
        scalePanel.add( ScaleLabel );
        scalePanel.add( ScaleCombo );
        add( scalePanel );

        settingPanel.add( SubGridCheck );
        settingPanel.add( OppositeCheck );
        settingPanel.add( VisibleCheck );
        settingPanel.add( FormatCombo );
        settingPanel.add( FormatLabel );
        settingPanel.add( TitleLabel );
        settingPanel.add( TitleText );
        settingPanel.add( ColorLabel );
        settingPanel.add( ColorView );
        settingPanel.add( ColorBtn );
        settingPanel.add( PositionLabel );
        settingPanel.add( PositionCombo );
        add( settingPanel );

        MinLabel.setBounds( 10 , 20 , 35 , 25 );
        MinText.setBounds( 50 , 20 , 90 , 25 );
        MaxLabel.setBounds( 165 , 20 , 40 , 25 );
        MaxText.setBounds( 210 , 20 , 90 , 25 );
        ScaleLabel.setBounds( 10 , 50 , 100 , 25 );
        ScaleCombo.setBounds( 115 , 50 , 185 , 25 );
        AutoScaleCheck.setBounds( 5 , 80 , 275 , 25 );
        scalePanel.setBounds( 5 , 10 , 310 , 115 );

        FormatLabel.setBounds( 10 , 20 , 100 , 25 );
        FormatCombo.setBounds( 115 , 20 , 185 , 25 );
        TitleLabel.setBounds( 10 , 50 , 100 , 25 );
        TitleText.setBounds( 115 , 50 , 185 , 25 );
        ColorLabel.setBounds( 10 , 80 , 100 , 25 );
        ColorView.setBounds( 115 , 80 , 150 , 25 );
        ColorBtn.setBounds( 270 , 80 , 30 , 25 );
        PositionLabel.setBounds( 10 , 110 , 100 , 25 );
        PositionCombo.setBounds( 115 , 110 , 185 , 25 );
        SubGridCheck.setBounds( 5 , 140 , 130 , 25 );
        OppositeCheck.setBounds( 160 , 140 , 130 , 25 );
        VisibleCheck.setBounds( 5 , 170 , 280 , 25 );
        settingPanel.setBounds( 5 , 130 , 310 , 205 );
    }

    /**
     * @return 24 août 2005
     */
    public YAxis getYAxis ()
    {
        YAxis ret = new YAxis();

        ret.setLabelFormat( FormatCombo.getSelectedIndex() );
        ret.setPosition( PositionCombo.getSelectedIndex() );
        ret.setScaleMode( ScaleCombo.getSelectedIndex() );
        ret.setAutoScale( AutoScaleCheck.isSelected() );
        ret.setColor( ColorView.getBackground() );
        ret.setDrawOpposite( OppositeCheck.isSelected() );
        try
        {
            ret.setScaleMax( Double.parseDouble( MaxText.getText() ) );
        }
        catch ( NumberFormatException nfe )
        {

        }
        try
        {
            ret.setScaleMin( Double.parseDouble( MinText.getText() ) );
        }
        catch ( NumberFormatException nfe )
        {

        }
        ret.setShowSubGrid( SubGridCheck.isSelected() );
        ret.setVisible( VisibleCheck.isSelected() );
        ret.setTitle( TitleText.getText() );

        return ret;
    }

    /**
     * @param labelFormat 25 août 2005
     */
    public void setLabelFormat ( int labelFormat )
    {
        FormatCombo.setSelectedIndex( labelFormat );
    }

    /**
     * @param position 25 août 2005
     */
    public void setPosition ( int position )
    {
        PositionCombo.setSelectedIndex( position );
    }

    /**
     * @param scaleMode 25 août 2005
     */
    public void setScaleMode ( int scaleMode )
    {
        ScaleCombo.setSelectedIndex( scaleMode );
    }

    /**
     * @param b 25 août 2005
     */
    public void setAutoScale ( boolean b )
    {
        AutoScaleCheck.setSelected( b );
    }

    /**
     * @param color 25 août 2005
     */
    public void setColor ( Color color )
    {
        ColorView.setBackground( color );
    }

    public Color getColor ()
    {
        return ColorView.getBackground();
    }

    /**
     * @param b 25 août 2005
     */
    public void setDrawOpposite ( boolean b )
    {
        OppositeCheck.setSelected( b );
    }

    /**
     * @param scaleMax 25 août 2005
     */
    public void setScaleMax ( double scaleMax )
    {
        MaxText.setText( String.valueOf( scaleMax ) );
    }

    /**
     * @param labelFormat 25 août 2005
     */
    public void setScaleMin ( double scaleMin )
    {
        MinText.setText( String.valueOf( scaleMin ) );
    }

    /**
     * @param object 25 août 2005
     */
    public void setShowSubGrid ( boolean b )
    {
        SubGridCheck.setSelected( b );
    }

    /**
     * @param b 25 août 2005
     */
    @Override
	public void setVisible ( boolean b )
    {
        VisibleCheck.setSelected( b );
    }

    /**
     * @param string 29 août 2005
     */
    public void setTitle ( String title )
    {
        TitleText.setText( title );
    }


}
