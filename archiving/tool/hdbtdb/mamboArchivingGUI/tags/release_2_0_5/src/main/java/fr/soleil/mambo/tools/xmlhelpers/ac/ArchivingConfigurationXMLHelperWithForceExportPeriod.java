package fr.soleil.mambo.tools.xmlhelpers.ac;

import java.util.Hashtable;
import org.w3c.dom.Node;

import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.options.manager.ACDefaultsManagerFactory;
import fr.soleil.mambo.options.manager.IACDefaultsManager;

public class ArchivingConfigurationXMLHelperWithForceExportPeriod extends ArchivingConfigurationXMLHelperStandard implements IArchivingConfigurationXMLHelper
{
	public ArchivingConfigurationXMLHelperWithForceExportPeriod() 
	{
		super ();
	}
	
	public ArchivingConfiguration loadArchivingConfigurationIntoHash ( String location ) throws Exception
	{
		return super.loadArchivingConfigurationIntoHash ( location );
	}
	
	public ArchivingConfiguration loadArchivingConfigurationIntoHashFromRoot ( Node rootNode ) throws Exception
    {
		return super.loadArchivingConfigurationIntoHashFromRoot ( rootNode );
    }
	
	protected long defineTdbExportPeriodForCurrentAttributeModes(Hashtable DBmodeProperties)
    {
		long defaultExportValue;
		try
		{
			defaultExportValue = Long.parseLong(ACDefaultsManagerFactory.getCurrentImpl().getACDefaultsPropertyValue("TDB_EXPORT_PERIOD"));
		}
		catch (NumberFormatException e)
		{
			System.out.println("NumberFormatException has been received during TDB_EXPORT_PERIOD reading");
			defaultExportValue = IACDefaultsManager.DEFAULT_TDB_EXPORT_PERIOD_WHEN_BAD_VALUE;
		}
        return defaultExportValue;
    }
}
