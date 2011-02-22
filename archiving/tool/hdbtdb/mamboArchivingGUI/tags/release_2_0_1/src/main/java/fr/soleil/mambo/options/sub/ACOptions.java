//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/ACOptions.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  DisplayOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.7 $
//
//$Log: ACOptions.java,v $
//Revision 1.7  2006/12/07 16:45:39  ounsy
//removed keeping period
//
//Revision 1.6  2006/05/19 15:05:29  ounsy
//minor changes
//
//Revision 1.5  2006/05/16 12:04:08  ounsy
//minor changes
//
//Revision 1.4  2006/03/29 10:28:16  ounsy
//corected so that the tree/table selection works in the menu/tool bar too and not only in the bottom buttons
//
//Revision 1.3  2006/02/24 12:22:45  ounsy
//small modifications
//
//Revision 1.2  2005/12/15 11:46:07  ounsy
//period formating
//
//Revision 1.1  2005/11/29 18:28:12  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options.sub;

import java.util.Enumeration;

import javax.swing.ButtonModel;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.mambo.components.MamboMenuBar;
import fr.soleil.mambo.components.MamboToolBar;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.containers.archiving.ArchivingActionPanel;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.containers.sub.dialogs.options.ACTabDefaultPanel;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsACTab;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;
import fr.soleil.mambo.tools.GUIUtilities;

public class ACOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
    public static final String SELECTION_MODE = "SELECTION_MODE";
    public static final int SELECTION_MODE_TREE = 0;
    public static final int SELECTION_MODE_TABLE = 1;

    public static final String STACK_DEPTH = "STACK_DEPTH";
    private static final int DEFAULT_STACK_DEPTH = 5;
    //--
    public static final String HDB_PERIODIC_MODE = "HDB_PERIODIC_MODE";
    public static final String HDB_ABSOLUTE_MODE = "HDB_ABSOLUTE_MODE";
    public static final String HDB_RELATIVE_MODE = "HDB_RELATIVE_MODE";
    public static final String HDB_THRESHOLD_MODE = "HDB_THRESHOLD_MODE";
    public static final String HDB_DIFFERENCE_MODE = "HDB_DIFFERENCE_MODE";

    public static final String TDB_PERIODIC_MODE = "TDB_PERIODIC_MODE";
    public static final String TDB_ABSOLUTE_MODE = "TDB_ABSOLUTE_MODE";
    public static final String TDB_RELATIVE_MODE = "TDB_RELATIVE_MODE";
    public static final String TDB_THRESHOLD_MODE = "TDB_THRESHOLD_MODE";
    public static final String TDB_DIFFERENCE_MODE = "TDB_DIFFERENCE_MODE";
    //----

    public static final String HDB_ABSOLUTE_MODE_PERIOD = "HDB_ABSOLUTE_MODE_PERIOD";
    public static final String HDB_ABSOLUTE_MODE_UPPER = "HDB_ABSOLUTE_MODE_UPPER";
    public static final String HDB_ABSOLUTE_MODE_LOWER = "HDB_ABSOLUTE_MODE_LOWER";
    public static final String HDB_ABSOLUTE_SLOW_DRIFT = "HDB_ABSOLUTE_SLOW_DRIFT";

    public static final String HDB_RELATIVE_MODE_PERIOD = "HDB_RELATIVE_MODE_PERIOD";
    public static final String HDB_RELATIVE_MODE_UPPER = "HDB_RELATIVE_MODE_UPPER";
    public static final String HDB_RELATIVE_MODE_LOWER = "HDB_RELATIVE_MODE_LOWER";
    public static final String HDB_RELATIVE_SLOW_DRIFT = "HDB_RELATIVE_SLOW_DRIFT";

    public static final String HDB_THRESHOLD_MODE_PERIOD = "HDB_THRESHOLD_MODE_PERIOD";
    public static final String HDB_THRESHOLD_MODE_UPPER = "HDB_THRESHOLD_MODE_UPPER";
    public static final String HDB_THRESHOLD_MODE_LOWER = "HDB_THRESHOLD_MODE_LOWER";

    public static final String HDB_DIFFERENCE_MODE_PERIOD = "HDB_DIFFERENCE_MODE_PERIOD";
    //--
    //--

    public static final String TDB_ABSOLUTE_MODE_PERIOD = "TDB_ABSOLUTE_MODE_PERIOD";
    public static final String TDB_ABSOLUTE_MODE_UPPER = "TDB_ABSOLUTE_MODE_UPPER";
    public static final String TDB_ABSOLUTE_MODE_LOWER = "TDB_ABSOLUTE_MODE_LOWER";
    public static final String TDB_ABSOLUTE_SLOW_DRIFT = "TDB_ABSOLUTE_SLOW_DRIFT";

    public static final String TDB_RELATIVE_MODE_PERIOD = "TDB_RELATIVE_MODE_PERIOD";
    public static final String TDB_RELATIVE_MODE_UPPER = "TDB_RELATIVE_MODE_UPPER";
    public static final String TDB_RELATIVE_MODE_LOWER = "TDB_RELATIVE_MODE_LOWER";
    public static final String TDB_RELATIVE_SLOW_DRIFT = "TDB_RELATIVE_SLOW_DRIFT";

    public static final String TDB_THRESHOLD_MODE_PERIOD = "TDB_THRESHOLD_MODE_PERIOD";
    public static final String TDB_THRESHOLD_MODE_UPPER = "TDB_THRESHOLD_MODE_UPPER";
    public static final String TDB_THRESHOLD_MODE_LOWER = "TDB_THRESHOLD_MODE_LOWER";

    public static final String TDB_DIFFERENCE_MODE_PERIOD = "TDB_DIFFERENCE_MODE_PERIOD";
    //--
    //--


    //----
    public static final String HDB_PERIOD = "HDB_PERIOD";

    public static final String TDB_PERIOD = "TDB_PERIOD";
    public static final String TDB_EXPORT_PERIOD = "TDB_EXPORT_PERIOD";
    //public static final String TDB_KEEPING_PERIOD = "TDB_KEEPING_PERIOD";
    //----

    public static final String KEY = "ACs"; //for XML save and load

    private boolean isAlternateSelectionMode;
    private int stackDepth = DEFAULT_STACK_DEPTH;

    /**
     *
     */
    public ACOptions ()
    {
        super( KEY );
    }

    public ArchivingConfigurationAttributeProperties getDefaultACProperties ()
    {
        ArchivingConfigurationAttributeProperties ret = new ArchivingConfigurationAttributeProperties();
        String selection;
        String period;
        String lower;
        String upper;
        String slow_drift;
        

        //-------------HDB
        selection = ( String ) content.get( HDB_PERIODIC_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( HDB_PERIOD );
                int period_i = Integer.parseInt( period );
                
                ModePeriode _mode = new ModePeriode( period_i );

                Mode mode1 = new Mode();
                mode1.setModeP( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , true );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( HDB_ABSOLUTE_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( HDB_ABSOLUTE_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                lower = ( String ) content.get( HDB_ABSOLUTE_MODE_LOWER );
                double lower_d = Double.parseDouble( lower );
                upper = ( String ) content.get( HDB_ABSOLUTE_MODE_UPPER );
                double upper_d = Double.parseDouble( upper );
                slow_drift = ( String ) content.get( HDB_ABSOLUTE_SLOW_DRIFT );
                boolean sd = Boolean.parseBoolean(slow_drift);
                ModeAbsolu _mode = new ModeAbsolu( period_i , lower_d , upper_d, sd );

                Mode mode1 = new Mode();
                mode1.setModeA( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , true );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( HDB_RELATIVE_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( HDB_RELATIVE_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                lower = ( String ) content.get( HDB_RELATIVE_MODE_LOWER );
                double lower_d = Double.parseDouble( lower );
                upper = ( String ) content.get( HDB_RELATIVE_MODE_UPPER );
                double upper_d = Double.parseDouble( upper );
                slow_drift = ( String ) content.get( HDB_RELATIVE_SLOW_DRIFT);
                boolean sd = Boolean.parseBoolean(slow_drift);

                ModeRelatif _mode = new ModeRelatif( period_i , lower_d , upper_d, sd);

                Mode mode1 = new Mode();
                mode1.setModeR( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , true );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( HDB_THRESHOLD_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( HDB_THRESHOLD_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                lower = ( String ) content.get( HDB_THRESHOLD_MODE_LOWER );
                double lower_d = Double.parseDouble( lower );
                upper = ( String ) content.get( HDB_THRESHOLD_MODE_UPPER );
                double upper_d = Double.parseDouble( upper );

                ModeSeuil _mode = new ModeSeuil( period_i , lower_d , upper_d );

                Mode mode1 = new Mode();
                mode1.setModeT( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , true );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( HDB_DIFFERENCE_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( HDB_DIFFERENCE_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                ModeDifference _mode = new ModeDifference( period_i );

                Mode mode1 = new Mode();
                mode1.setModeD( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , true );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        //-------------TDB
        selection = ( String ) content.get( TDB_PERIODIC_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( TDB_PERIOD );
                int period_i = Integer.parseInt( period );
                ModePeriode _mode = new ModePeriode( period_i );

                Mode mode1 = new Mode();
                mode1.setModeP( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , false );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( TDB_ABSOLUTE_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( TDB_ABSOLUTE_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                lower = ( String ) content.get( TDB_ABSOLUTE_MODE_LOWER );
                double lower_d = Double.parseDouble( lower );
                upper = ( String ) content.get( TDB_ABSOLUTE_MODE_UPPER );
                double upper_d = Double.parseDouble( upper );
                slow_drift = ( String ) content.get( TDB_ABSOLUTE_SLOW_DRIFT );
                boolean sd = Boolean.parseBoolean(slow_drift);

                ModeAbsolu _mode = new ModeAbsolu( period_i , lower_d , upper_d, sd);

                Mode mode1 = new Mode();
                mode1.setModeA( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , false );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( TDB_RELATIVE_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( TDB_RELATIVE_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                lower = ( String ) content.get( TDB_RELATIVE_MODE_LOWER );
                double lower_d = Double.parseDouble( lower );
                upper = ( String ) content.get( TDB_RELATIVE_MODE_UPPER );
                double upper_d = Double.parseDouble( upper );
                slow_drift = ( String ) content.get( TDB_RELATIVE_SLOW_DRIFT );
                boolean sd = Boolean.parseBoolean(slow_drift);

                ModeRelatif _mode = new ModeRelatif( period_i , lower_d , upper_d, sd);

                Mode mode1 = new Mode();
                mode1.setModeR( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , false );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( TDB_THRESHOLD_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( TDB_THRESHOLD_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                lower = ( String ) content.get( TDB_THRESHOLD_MODE_LOWER );
                double lower_d = Double.parseDouble( lower );
                upper = ( String ) content.get( TDB_THRESHOLD_MODE_UPPER );
                double upper_d = Double.parseDouble( upper );

                ModeSeuil _mode = new ModeSeuil( period_i , lower_d , upper_d );

                Mode mode1 = new Mode();
                mode1.setModeT( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , false );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        selection = ( String ) content.get( TDB_DIFFERENCE_MODE );
        if ( selection != null )
        {
            try
            {
                period = ( String ) content.get( TDB_DIFFERENCE_MODE_PERIOD );
                int period_i = Integer.parseInt( period );
                ModeDifference _mode = new ModeDifference( period_i );

                Mode mode1 = new Mode();
                mode1.setModeD( _mode );
                ArchivingConfigurationMode acMode = new ArchivingConfigurationMode( mode1 );

                ret.addMode( acMode , false );
            }
            catch ( Exception e )
            {
                //do nothing
            }
        }

        //----export period
        try
        {
            period = ( String ) content.get( TDB_EXPORT_PERIOD );
            long period_l = Long.parseLong( period );
            ret.getTDBProperties().setExportPeriod( period_l );
        }
        catch ( Exception e )
        {
//do nothing
        }

        /*try
        {
            period = ( String ) this.content.get( TDB_KEEPING_PERIOD );
            long period_l = Long.parseLong( period );
            ret.getTDBProperties().setKeepingPeriod( period_l );
        }
        catch ( Exception e )
        {
//do nothing
        }*/

        return ret;
    }
    //

    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog ()
    {
        fillSelectionMode();
        fillStackDepth();

        fillDefaultModes();
        fillDefaultValues();
    }

    /**
     *
     */
    private void fillStackDepth() {
		OptionsACTab acTab = OptionsACTab.getInstance();
		String stackDepth_s = acTab.getStackDepth();
		try {
			stackDepth = Integer.parseInt(stackDepth_s);
			content.put(STACK_DEPTH, stackDepth_s);
		}
		catch (Exception e) {
			// If the new value is bad, we keep the last good one.
			// We could also put the default value:
			// content.put( STACK_DEPTH , String.valueOf(this.getStackDepth())
			// );
		}
	}

    /**
	 *
	 */
    @SuppressWarnings({ "unchecked", "unchecked", "unchecked" })
	private void fillDefaultValues ()
    {
        ACTabDefaultPanel defaultPanel = ACTabDefaultPanel.getInstance();
        int value;
        String value_s;
        boolean value_b;
        long l;

        //------HDB Periods
        value = defaultPanel.getHDBperiod();
        content.put( HDB_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getHDBAbsolutePeriod();
        content.put( HDB_ABSOLUTE_MODE_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getHDBRelativePeriod();
        content.put( HDB_RELATIVE_MODE_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getHDBThresholdPeriod();
        content.put( HDB_THRESHOLD_MODE_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getHDBDifferencePeriod();
        content.put( HDB_DIFFERENCE_MODE_PERIOD , String.valueOf( value ) );
        //------
        //------TDB Periods
        value = defaultPanel.getTDBperiod();
        content.put( TDB_PERIOD , String.valueOf( value ) );

        l = defaultPanel.getExportPeriod();
        content.put( TDB_EXPORT_PERIOD , String.valueOf( l ) );

        /*l = defaultPanel.getKeepingPeriod();
        this.content.put( TDB_KEEPING_PERIOD , String.valueOf( l ) );*/

        value = defaultPanel.getTDBAbsolutePeriod();
        content.put( TDB_ABSOLUTE_MODE_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getTDBRelativePeriod();
        content.put( TDB_RELATIVE_MODE_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getTDBThresholdPeriod();
        content.put( TDB_THRESHOLD_MODE_PERIOD , String.valueOf( value ) );

        value = defaultPanel.getTDBDifferencePeriod();
        content.put( TDB_DIFFERENCE_MODE_PERIOD , String.valueOf( value ) );
        //------

        //------HDB upper/lower
        value_s = defaultPanel.getHDBAbsoluteUpper();
        content.put( HDB_ABSOLUTE_MODE_UPPER , value_s );

        value_s = defaultPanel.getHDBRelativeUpper();
        content.put( HDB_RELATIVE_MODE_UPPER , value_s );

        value_s = defaultPanel.getHDBThresholdUpper();
        content.put( HDB_THRESHOLD_MODE_UPPER , value_s );

        value_s = defaultPanel.getHDBAbsoluteLower();
        content.put( HDB_ABSOLUTE_MODE_LOWER , value_s );

        value_s = defaultPanel.getHDBRelativeLower();
        content.put( HDB_RELATIVE_MODE_LOWER , value_s );

        value_s = defaultPanel.getHDBThresholdLower();
        content.put( HDB_THRESHOLD_MODE_LOWER , value_s );
        
        

        
        //------

        //------TDB upper/lower
        value_s = defaultPanel.getTDBAbsoluteUpper();
        content.put( TDB_ABSOLUTE_MODE_UPPER , value_s );

        value_s = defaultPanel.getTDBRelativeUpper();
        content.put( TDB_RELATIVE_MODE_UPPER , value_s );

        value_s = defaultPanel.getTDBThresholdUpper();
        content.put( TDB_THRESHOLD_MODE_UPPER , value_s );

        value_s = defaultPanel.getTDBAbsoluteLower();
        content.put( TDB_ABSOLUTE_MODE_LOWER , value_s );

        value_s = defaultPanel.getTDBRelativeLower();
        content.put( TDB_RELATIVE_MODE_LOWER , value_s );

        value_s = defaultPanel.getTDBThresholdLower();
        content.put( TDB_THRESHOLD_MODE_LOWER , value_s );
        //------
        
        
        //---HDB Slow Drift values
        value_b = defaultPanel.getHDBAbsoluteDLCheck();
        content.put( HDB_ABSOLUTE_SLOW_DRIFT , Boolean.toString(value_b) );
        
        value_b = defaultPanel.getHDBRelativeDLCheck();
        content.put( HDB_RELATIVE_SLOW_DRIFT , Boolean.toString(value_b) );

        //---TDB Slow Drift values
        value_b = defaultPanel.getTDBAbsoluteDLCheck();
        content.put( TDB_ABSOLUTE_SLOW_DRIFT , Boolean.toString(value_b));
        
        value_b = defaultPanel.getTDBRelativeDLCheck();
        content.put( TDB_RELATIVE_SLOW_DRIFT , Boolean.toString(value_b) );

        
 
        
    }

    /**
     *
     */
    private void fillDefaultModes ()
    {
        ACTabDefaultPanel defaultPanel = ACTabDefaultPanel.getInstance();

        if ( defaultPanel.hasModeHDB_P() )
        {
            content.put( HDB_PERIODIC_MODE , HDB_PERIODIC_MODE );
        }
        else
        {
            content.remove( HDB_PERIODIC_MODE );
        }

        if ( defaultPanel.hasModeHDB_A() )
        {
            content.put( HDB_ABSOLUTE_MODE , HDB_ABSOLUTE_MODE );
        }
        else
        {
            content.remove( HDB_ABSOLUTE_MODE );
        }

        if ( defaultPanel.hasModeHDB_R() )
        {
            content.put( HDB_RELATIVE_MODE , HDB_RELATIVE_MODE );
        }
        else
        {
            content.remove( HDB_RELATIVE_MODE );
        }

        if ( defaultPanel.hasModeHDB_T() )
        {
            content.put( HDB_THRESHOLD_MODE , HDB_THRESHOLD_MODE );
        }
        else
        {
            content.remove( HDB_THRESHOLD_MODE );
        }

        if ( defaultPanel.hasModeHDB_D() )
        {
            content.put( HDB_DIFFERENCE_MODE , HDB_DIFFERENCE_MODE );
        }
        else
        {
            content.remove( HDB_DIFFERENCE_MODE );
        }
        //----
        if ( defaultPanel.hasModeTDB_P() )
        {
            content.put( TDB_PERIODIC_MODE , TDB_PERIODIC_MODE );
        }
        else
        {
            content.remove( TDB_PERIODIC_MODE );
        }

        if ( defaultPanel.hasModeTDB_A() )
        {
            content.put( TDB_ABSOLUTE_MODE , TDB_ABSOLUTE_MODE );
        }
        else
        {
            content.remove( TDB_ABSOLUTE_MODE );
        }

        if ( defaultPanel.hasModeTDB_R() )
        {
            content.put( TDB_RELATIVE_MODE , TDB_RELATIVE_MODE );
        }
        else
        {
            content.remove( TDB_RELATIVE_MODE );
        }

        if ( defaultPanel.hasModeTDB_T() )
        {
            content.put( TDB_THRESHOLD_MODE , TDB_THRESHOLD_MODE );
        }
        else
        {
            content.remove( TDB_THRESHOLD_MODE );
        }

        if ( defaultPanel.hasModeTDB_D() )
        {
            content.put( TDB_DIFFERENCE_MODE , TDB_DIFFERENCE_MODE );
        }
        else
        {
            content.remove( TDB_DIFFERENCE_MODE );
        }
    }

    /**
     *
     */
    private void fillSelectionMode ()
    {
        OptionsACTab acTab = OptionsACTab.getInstance();

        ButtonModel selectedModel = acTab.getButtonGroup().getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();
        content.put( SELECTION_MODE , selectedActionCommand );

        int plaf = Integer.parseInt( selectedActionCommand );
        //boolean isAlternate = false;

        switch ( plaf )
        {
            case SELECTION_MODE_TREE:
                isAlternateSelectionMode = false;
                break;

            case SELECTION_MODE_TABLE:
                isAlternateSelectionMode = true;
                break;
        }
    }

    /* (non-Javadoc)
      * @see bensikin.bensikin.options.PushPullOptionBook#push()
      */
    public void push ()
    {
        pushSelectionMode();
        pushStackDepth();

        pushDefaultModes();
        pushDefaultValues();
    }

    /**
     *
     */
    private void pushStackDepth() {
		OptionsACTab acTab = OptionsACTab.getInstance();

		String stackDepth_s = (String) content.get(STACK_DEPTH);
		try {
			stackDepth = Integer.parseInt(stackDepth_s);
		}
		catch (Exception e) {
			stackDepth = DEFAULT_STACK_DEPTH;
			stackDepth_s = String.valueOf(stackDepth);
			content.put(STACK_DEPTH, stackDepth_s);
		}
		acTab.setStackDepth(stackDepth_s);
		OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
		openedACComboBox.setMaxSize(stackDepth);
	}

    /**
	 *
	 */
    private void pushDefaultValues ()
    {
        /*
		 * System.out.println ( "ACOptions/pushDefaultValues/IN--------------" );
		 * System.out.println ( this.toPropertiesString () ); System.out.println (
		 * "ACOptions/pushDefaultValues/IN--------------" );
		 */

        ACTabDefaultPanel defaultPanel = ACTabDefaultPanel.getInstance();
        String s;
        boolean b;
        int i;
        long l;

        //---HDB periods
        s = ( String ) content.get( HDB_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setHDBPeriod( i );
        }

        s = ( String ) content.get( HDB_ABSOLUTE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setHDBAbsolutePeriod( i );
        }

        s = ( String ) content.get( HDB_RELATIVE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setHDBRelativePeriod( i );
        }

        s = ( String ) content.get( HDB_THRESHOLD_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setHDBThresholdPeriod( i );
        }

        s = ( String ) content.get( HDB_DIFFERENCE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setHDBDifferencePeriod( i );
        }

        //---TDB periods
        s = ( String ) content.get( TDB_PERIOD );
        //System.out.println ( "ACOptions/pushDefaultValues/TDB_PERIOD/"+s );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setTDBPeriod( i );
        }

        s = ( String ) content.get( TDB_EXPORT_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            l = Long.parseLong( s );
            defaultPanel.setExportPeriod( l );
        }

        /*s = ( String ) this.content.get( TDB_KEEPING_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            l = Long.parseLong( s );
            defaultPanel.setKeepingPeriod( l );
        }*/

        s = ( String ) content.get( TDB_ABSOLUTE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setTDBAbsolutePeriod( i );
        }

        s = ( String ) content.get( TDB_RELATIVE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setTDBRelativePeriod( i );
        }

        s = ( String ) content.get( TDB_THRESHOLD_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setTDBThresholdPeriod( i );
        }

        s = ( String ) content.get( TDB_DIFFERENCE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            defaultPanel.setTDBDifferencePeriod( i );
        }

        //---HDB values
        //s = s == null ? "" : s;

        s = ( String ) content.get( HDB_ABSOLUTE_MODE_UPPER );
        defaultPanel.setHDBAbsoluteUpper( s );

        s = ( String ) content.get( HDB_RELATIVE_MODE_UPPER );
        defaultPanel.setHDBRelativeUpper( s );

        s = ( String ) content.get( HDB_THRESHOLD_MODE_UPPER );
        defaultPanel.setHDBThresholdUpper( s );

        s = ( String ) content.get( HDB_ABSOLUTE_MODE_LOWER );
        defaultPanel.setHDBAbsoluteLower( s );

        s = ( String ) content.get( HDB_RELATIVE_MODE_LOWER );
        defaultPanel.setHDBRelativeLower( s );

        s = ( String ) content.get( HDB_THRESHOLD_MODE_LOWER );
        defaultPanel.setHDBThresholdLower( s );

        //---TDB values
        s = ( String ) content.get( TDB_ABSOLUTE_MODE_UPPER );
        defaultPanel.setTDBAbsoluteUpper( s );

        s = ( String ) content.get( TDB_RELATIVE_MODE_UPPER );
        defaultPanel.setTDBRelativeUpper( s );

        s = ( String ) content.get( TDB_THRESHOLD_MODE_UPPER );
        defaultPanel.setTDBThresholdUpper( s );

        s = ( String ) content.get( TDB_ABSOLUTE_MODE_LOWER );
        defaultPanel.setTDBAbsoluteLower( s );

        s = ( String ) content.get( TDB_RELATIVE_MODE_LOWER );
        defaultPanel.setTDBRelativeLower( s );

        s = ( String ) content.get( TDB_THRESHOLD_MODE_LOWER );
        defaultPanel.setTDBThresholdLower( s );
        
      //---HDB Slow Drift values
        b =  Boolean.parseBoolean(( String )content.get( HDB_ABSOLUTE_SLOW_DRIFT ));
        defaultPanel.setHDBAbsoluteDLCheck(b);

        b =  Boolean.parseBoolean(( String )content.get( HDB_RELATIVE_SLOW_DRIFT ));
        defaultPanel.setHDBRelativeDLCheck(b);

        //---TDB Slow Drift values
        b =  Boolean.parseBoolean(( String )content.get( TDB_ABSOLUTE_SLOW_DRIFT ));
        defaultPanel.setTDBAbsoluteDLCheck(b);

        b =  Boolean.parseBoolean(( String )content.get( TDB_RELATIVE_SLOW_DRIFT ));
        defaultPanel.setTDBRelativeDLCheck(b);

        
    }

    /**
     *
     */
    private void pushDefaultModes ()
    {
        ACTabDefaultPanel defaultPanel = ACTabDefaultPanel.getInstance();
        String s;

        defaultPanel.doUnselectAllModes();

        s = ( String ) content.get( HDB_PERIODIC_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( true , ArchivingConfigurationMode.TYPE_P );
        }

        s = ( String ) content.get( HDB_ABSOLUTE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( true , ArchivingConfigurationMode.TYPE_A );
        }

        s = ( String ) content.get( HDB_RELATIVE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( true , ArchivingConfigurationMode.TYPE_R );
        }

        s = ( String ) content.get( HDB_THRESHOLD_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( true , ArchivingConfigurationMode.TYPE_T );
        }

        s = ( String ) content.get( HDB_DIFFERENCE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( true , ArchivingConfigurationMode.TYPE_D );
        }

        s = ( String ) content.get( TDB_PERIODIC_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( false , ArchivingConfigurationMode.TYPE_P );
        }

        s = ( String ) content.get( TDB_ABSOLUTE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( false , ArchivingConfigurationMode.TYPE_A );
        }

        s = ( String ) content.get( TDB_RELATIVE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( false , ArchivingConfigurationMode.TYPE_R );
        }

        s = ( String ) content.get( TDB_THRESHOLD_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( false , ArchivingConfigurationMode.TYPE_T );
        }

        s = ( String ) content.get( TDB_DIFFERENCE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            defaultPanel.doSelectMode( false , ArchivingConfigurationMode.TYPE_D );
        }

    }

    public void pushSelectionMode ()
    {
        String plaf_s = ( String ) content.get( SELECTION_MODE );

        if ( plaf_s != null )
        {
            int plaf = Integer.parseInt( plaf_s );
            boolean isAlternate = false;

            switch ( plaf )
            {
                case SELECTION_MODE_TREE:
                    isAlternate = false;
                    break;

                case SELECTION_MODE_TABLE:
                    isAlternate = true;
                    break;
            }

            isAlternateSelectionMode = isAlternate;
            ArchivingActionPanel.setAlternateSelectionMode( isAlternate );

            ACEditDialog dialog = ACEditDialog.getInstance();
            if ( dialog != null )
            {
                dialog.setAlternateSelectionMode( isAlternate );
            }

            if ( MamboToolBar.getCurrentInstance() != null )
            {
                MamboToolBar.getCurrentInstance().setAlternateSelection ( isAlternate );
            }
            if ( MamboMenuBar.getCurrentInstance() != null )
            {
                MamboMenuBar.getCurrentInstance().setAlternateSelection ( isAlternate );
            }

            OptionsACTab acTab = OptionsACTab.getInstance();
            acTab.selectIsAlternate( isAlternate );
        }
    }

    public void applyValueDefaultsMain ()
    {
        AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
        String s;
        int i;

        //---HDB periods
        s = ( String ) content.get( HDB_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
                else if (i>Integer.MAX_VALUE/1000) {
                    i = Integer.MAX_VALUE/1000;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE/1000;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setHDBPeriod( i );
        }

        //---TDB periods
        s = ( String ) content.get( TDB_PERIOD );
        //System.out.println ( "ACOptions/applyValueDefaultsMain/TDB_PERIOD/"+s );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setTDBPeriod( i );
        }

        s = ( String ) content.get( TDB_EXPORT_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            panel.setExportPeriod( i );
        }

        /*s = ( String ) this.content.get( TDB_KEEPING_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            i = Integer.parseInt( s );
            panel.setKeepingPeriod( i );
        }*/


    }

    public void applyValueDefaultsSub ()
    {
        AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
        String s;
        boolean b;
        int i;
        //long l;

        //---HDB periods
        s = ( String ) content.get( HDB_ABSOLUTE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
                else if (i>Integer.MAX_VALUE/1000) {
                    i = Integer.MAX_VALUE/1000;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE/1000;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setHDBAbsolutePeriod( i );
        }

        s = ( String ) content.get( HDB_RELATIVE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
                else if (i>Integer.MAX_VALUE/1000) {
                    i = Integer.MAX_VALUE/1000;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE/1000;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setHDBRelativePeriod( i );
        }

        s = ( String ) content.get( HDB_THRESHOLD_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
                else if (i>Integer.MAX_VALUE/1000) {
                    i = Integer.MAX_VALUE/1000;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE/1000;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setHDBThresholdPeriod( i );
        }

        s = ( String ) content.get( HDB_DIFFERENCE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
                else if (i>Integer.MAX_VALUE/1000) {
                    i = Integer.MAX_VALUE/1000;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE/1000;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setHDBDifferencePeriod( i );
        }

        //---TDB periods
        s = ( String ) content.get( TDB_ABSOLUTE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setTDBAbsolutePeriod( i );
        }

        s = ( String ) content.get( TDB_RELATIVE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setTDBRelativePeriod( i );
        }

        s = ( String ) content.get( TDB_THRESHOLD_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setTDBThresholdPeriod( i );
        }

        s = ( String ) content.get( TDB_DIFFERENCE_MODE_PERIOD );
        if ( s != null && !s.equals ( "" ) )
        {
            try {
                i = Integer.parseInt( s );
                if (i<0) {
                    i = 0;
                }
            }
            catch (NumberFormatException n) {
                double d;
                try {
                    d = Double.parseDouble(s);
                    if (d > 0) {
                        i = Integer.MAX_VALUE;
                    }
                    else {
                        i = 0;
                    }
                }
                catch (Exception e) {
                    throw n;
                }
            }
            panel.setTDBDifferencePeriod( i );
        }

        //---HDB values
        s = ( String ) content.get( HDB_ABSOLUTE_MODE_UPPER );
        panel.setHDBAbsoluteUpper( s );

        s = ( String ) content.get( HDB_RELATIVE_MODE_UPPER );
        panel.setHDBRelativeUpper( s );

        s = ( String ) content.get( HDB_THRESHOLD_MODE_UPPER );
        panel.setHDBThresholdUpper( s );

        s = ( String ) content.get( HDB_ABSOLUTE_MODE_LOWER );
        panel.setHDBAbsoluteLower( s );

        s = ( String ) content.get( HDB_RELATIVE_MODE_LOWER );
        panel.setHDBRelativeLower( s );

        s = ( String ) content.get( HDB_THRESHOLD_MODE_LOWER );
        panel.setHDBThresholdLower( s );

        //---TDB values
        s = ( String ) content.get( TDB_ABSOLUTE_MODE_UPPER );
        panel.setTDBAbsoluteUpper( s );

        s = ( String ) content.get( TDB_RELATIVE_MODE_UPPER );
        panel.setTDBRelativeUpper( s );

        s = ( String ) content.get( TDB_THRESHOLD_MODE_UPPER );
        panel.setTDBThresholdUpper( s );

        s = ( String ) content.get( TDB_ABSOLUTE_MODE_LOWER );
        panel.setTDBAbsoluteLower( s );

        s = ( String ) content.get( TDB_RELATIVE_MODE_LOWER );
        panel.setTDBRelativeLower( s );

        s = ( String ) content.get( TDB_THRESHOLD_MODE_LOWER );
        panel.setTDBThresholdLower( s );
        
        //---HDB slow drift values 
        b =  Boolean.parseBoolean(( String )content.get( HDB_ABSOLUTE_SLOW_DRIFT ));
        panel.setHDBAbsoluteDLCheck(b);

        b =  Boolean.parseBoolean(( String )content.get( HDB_RELATIVE_SLOW_DRIFT ));
        panel.setHDBRelativeDLCheck(b);


        //---TDB slow drift values 
        b =  Boolean.parseBoolean(( String )content.get( TDB_ABSOLUTE_SLOW_DRIFT ));
        panel.setTDBAbsoluteDLCheck(b);

        b =  Boolean.parseBoolean(( String )content.get( TDB_RELATIVE_SLOW_DRIFT ));
        panel.setTDBRelativeDLCheck(b);

    }

    public void applyDefaults ()
    {
        this.applySelectionDefaults();
        this.applyValueDefaultsMain();
        this.applyValueDefaultsSub();
    }

    /**
     *
     */
    public void applySelectionDefaults ()
    {
        AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance();
        String s;

        panel.doUnselectAllModes();

        s = ( String ) content.get( HDB_PERIODIC_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( true , ArchivingConfigurationMode.TYPE_P );
        }

        s = ( String ) content.get( HDB_ABSOLUTE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( true , ArchivingConfigurationMode.TYPE_A );
        }

        s = ( String ) content.get( HDB_RELATIVE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( true , ArchivingConfigurationMode.TYPE_R );
        }

        s = ( String ) content.get( HDB_THRESHOLD_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( true , ArchivingConfigurationMode.TYPE_T );
        }

        s = ( String ) content.get( HDB_DIFFERENCE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( true , ArchivingConfigurationMode.TYPE_D );
        }

        s = ( String ) content.get( TDB_PERIODIC_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( false , ArchivingConfigurationMode.TYPE_P );
        }

        s = ( String ) content.get( TDB_ABSOLUTE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( false , ArchivingConfigurationMode.TYPE_A );
        }

        s = ( String ) content.get( TDB_RELATIVE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( false , ArchivingConfigurationMode.TYPE_R );
        }

        s = ( String ) content.get( TDB_THRESHOLD_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( false , ArchivingConfigurationMode.TYPE_T );
        }

        s = ( String ) content.get( TDB_DIFFERENCE_MODE );
        if ( s != null && !s.equals ( "" ) )
        {
            panel.doSelectMode( false , ArchivingConfigurationMode.TYPE_D );
        }

    }

    /**
     * @return
     */
    public String toPropertiesString ()
    {
        String ret = "";

        Enumeration enumeration = content.keys();
        while ( enumeration.hasMoreElements() )
        {
            String nextOptionKey = ( String ) enumeration.nextElement();
            String nextOptionValue = ( String ) content.get( nextOptionKey );
            if ( SELECTION_MODE.equals( nextOptionKey ) )
            {
                continue;
            }

            String nextLine = nextOptionKey + " = " + nextOptionValue;

            ret += nextLine;
            ret += GUIUtilities.CRLF;
        }

        return ret;
    }

    /**
     * @return Returns the stackDepth.
     */
    public int getStackDepth ()
    {
        return stackDepth;
    }

    /**
     * @param stackDepth The stackDepth to set.
     */
    public void setStackDepth ( int stackDepth )
    {
        this.stackDepth = stackDepth;
    }

    /**
     * @return
     */
    public boolean isAlternateSelectionMode ()
    {
        return isAlternateSelectionMode;
    }
}
