//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsContextTab.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OptionsDisplayTab.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: OptionsContextTab.java,v $
//Revision 1.2  2006/06/28 12:49:44  ounsy
//minor changes
//
//Revision 1.1  2005/12/14 16:54:23  ounsy
//added methods necessary for alternate attribute selection
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
package fr.soleil.bensikin.containers.sub.dialogs.options;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.options.sub.ContextOptions;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;


public class OptionsContextTab extends JPanel
{
	  private static OptionsContextTab instance = null;
	
	  private JRadioButton treeSelectionMode;
	  private JRadioButton tableSelectionMode;
	  private ButtonGroup buttonGroup;
	
	  private Box box;
	  //private Box emptyBox;
	  
	  
	
	  /**
	   * @return 8 juil. 2005
	   */
	  public static OptionsContextTab getInstance ()
	  {
	      if ( instance == null )
	      {
	          instance = new OptionsContextTab();
	      }
	
	      return instance;
	  }
	
	  /**
	   * @return 8 juil. 2005
	   */
	  public static void resetInstance ()
	  {
	      if ( instance != null )
	      {
	          instance.repaint();
	      }
	
	      instance = null;
	  }
	
	  private void initLayout ()
	  {
	      this.setLayout( new SpringLayout() );
	      SpringUtilities.makeCompactGrid( this ,
	                                       2 , 1 , //rows, cols
	                                       6 , 6 , //initX, initY
	                                       6 , 6 ,
	                                       true );       //xPad, yPad
	  }
	
	
	  /**
	   *
	   */
	  private OptionsContextTab ()
	  {
	
	      this.initComponents();
	      this.addComponents();
	      this.initLayout();
	
	      //this.setIgnoreRepaint ( false );
	  }
	
	
	  /**
	   *
	   */
	  private void addComponents ()
	  {
	      this.add( box );
	      //this.add( emptyBox );
	      this.add( Box.createVerticalGlue() );
	      
	  }
	
	  /**
	   *
	   */
	  private void initComponents ()
	  {
	      buttonGroup = new ButtonGroup();
	
	      String msg = Messages.getMessage( "DIALOGS_OPTIONS_CONTEXT_SELECTION_TREE" );
	      treeSelectionMode = new JRadioButton( msg , true );
	      treeSelectionMode.setActionCommand( String.valueOf( ContextOptions.SELECTION_MODE_TREE ) );
	
	      msg = Messages.getMessage( "DIALOGS_OPTIONS_CONTEXT_SELECTION_TABLE" );
	      tableSelectionMode = new JRadioButton( msg , false );
	      tableSelectionMode.setActionCommand( String.valueOf( ContextOptions.SELECTION_MODE_TABLE ) );
	
	      buttonGroup.add( treeSelectionMode );
	      buttonGroup.add( tableSelectionMode );
	
	      box = new Box( BoxLayout.Y_AXIS );
	      box.add( treeSelectionMode );
	      box.add( Box.createVerticalStrut( 5 ) );
	      //box.add( Box.createVerticalGlue() );
	      box.add( tableSelectionMode );
	      msg = Messages.getMessage( "DIALOGS_OPTIONS_CONTEXT_SELECTION_BORDER" );
	      TitledBorder tb = BorderFactory.createTitledBorder
	              ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
	                msg ,
	                TitledBorder.DEFAULT_JUSTIFICATION ,
	                TitledBorder.TOP ,
	                GUIUtilities.getTitleFont() );
	      box.setBorder( tb );
	
	      //emptyBox = new Box( BoxLayout.Y_AXIS );
	      box.add( Box.createVerticalGlue() );
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
	  public void selectPlafButton ( int plaf )
	  {
	      switch ( plaf )
	      {
	          case ContextOptions.SELECTION_MODE_TREE:
	              treeSelectionMode.setSelected( true );
	              break;
	
	          case ContextOptions.SELECTION_MODE_TABLE:
	              tableSelectionMode.setSelected( true );
	              break;
	      }
	  }
	
	  /**
	   * @return Returns the tableSelectionMode.
	   */
	  public JRadioButton getTableSelectionMode ()
	  {
	      return tableSelectionMode;
	  }
	
	  /**
	   * @return Returns the treeSelectionMode.
	   */
	  public JRadioButton getTreeSelectionMode ()
	  {
	      return treeSelectionMode;
	  }
	
	  /**
	   * 20 juil. 2005
	   *
	   * @param plaf
	   */
	  public void selectIsAlternate ( boolean _isAlternate )
	  {
	      if ( _isAlternate )
	      {
	          tableSelectionMode.setSelected( true );
	      }
	      else
	      {
	          treeSelectionMode.setSelected( true );
	      }
	  }
}
