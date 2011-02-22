package fr.soleil.mambo.components.archiving;

import java.util.Hashtable;
import javax.swing.JComboBox;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.options.manager.ACDefaultsManagerFactory;
import fr.soleil.mambo.options.manager.IACDefaultsManager;
import fr.soleil.mambo.tools.Messages;

public class ACTdbExportPeriodComboBox extends JComboBox 
{
	// Its value come from the default configuration
    private long defaultExportValue;
    // Use only if the defaultExportValue is not valid
    private static final String DEFAULT_TDB_EXPORT_PERIOD = 
    	Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_1H" );
    
    private long[] exportPeriodsTable;
    private Hashtable reverseExportPeriodsTable;
    
	public ACTdbExportPeriodComboBox()
	{
		super();
		
    	IACDefaultsManager manager = ACDefaultsManagerFactory.getCurrentImpl();
		try
		{
			defaultExportValue = Long.parseLong(manager.getACDefaultsPropertyValue("TDB_EXPORT_PERIOD"));
		}
		catch (NumberFormatException e)
		{
			System.out.println("NumberFormatException has been received during TDB_EXPORT_PERIOD reading");
			defaultExportValue = IACDefaultsManager.DEFAULT_TDB_EXPORT_PERIOD_WHEN_BAD_VALUE;
		}
     	
     	initComponent();
	}
	
	public long getExportPeriod ()
    {
        int selected = getSelectedIndex();
        return exportPeriodsTable[ selected ];
    }
	
	/**
     * The export period can be modified only by the admin profile 
     */
	public void setExportPeriod ( long period )
    {  	
    	Integer key = null;
	 	try
        {
            key = ( Integer ) reverseExportPeriodsTable.get( 
            		new Long( (!Mambo.canModifyExportPeriod()? defaultExportValue : period) ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        if ( key == null )
        {
        	setSelectedItem(DEFAULT_TDB_EXPORT_PERIOD);
        }
        else
        {
        	setSelectedIndex(key.intValue());
        }
    }
	
	private void initComponent ()
    {
        initExportPeriodsTable();
        
        String msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_5MN" );
        addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_10MN" );
        addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_30MN" );
        addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_1H" );
        addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_2H" );
        addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_4H" );
        addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_EXPORT_PERIOD_6H" );
        addItem( msg );
        
        if(!Mambo.canModifyExportPeriod())
        {
        	setEnabled(false);
        }     
        // Select item is done via the setMethod
    }
	
	private void initExportPeriodsTable ()
    {
        exportPeriodsTable = new long[ 7 ];
        reverseExportPeriodsTable = new Hashtable( 7 );

        long millisecondsInOneMinute = 60000;
        exportPeriodsTable[ 0 ] = 5 * millisecondsInOneMinute;
        exportPeriodsTable[ 1 ] = 10 * millisecondsInOneMinute;
        exportPeriodsTable[ 2 ] = 30 * millisecondsInOneMinute;
        exportPeriodsTable[ 3 ] = 1 * 60 * millisecondsInOneMinute;
        exportPeriodsTable[ 4 ] = 2 * 60 * millisecondsInOneMinute;
        exportPeriodsTable[ 5 ] = 4 * 60 * millisecondsInOneMinute;
        exportPeriodsTable[ 6 ] = 6 * 60 * millisecondsInOneMinute;

        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 0 ] ) , new Integer( 0 ) );
        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 1 ] ) , new Integer( 1 ) );
        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 2 ] ) , new Integer( 2 ) );
        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 3 ] ) , new Integer( 3 ) );
        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 4 ] ) , new Integer( 4 ) );
        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 5 ] ) , new Integer( 5 ) );
        reverseExportPeriodsTable.put( new Long( exportPeriodsTable[ 6 ] ) , new Integer( 6 ) );
    }


}
