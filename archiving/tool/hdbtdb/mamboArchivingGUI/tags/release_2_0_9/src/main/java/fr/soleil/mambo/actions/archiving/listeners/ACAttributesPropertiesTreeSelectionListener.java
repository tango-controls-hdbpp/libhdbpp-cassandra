//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/listeners/ACAttributesPropertiesTreeSelectionListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributesPropertiesTreeSelectionListener.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: ACAttributesPropertiesTreeSelectionListener.java,v $
// Revision 1.7  2006/10/19 12:36:09  ounsy
// modified applyDefault() to display the current dedicated archiver
//
// Revision 1.6  2006/10/05 15:37:20  ounsy
//  corrected the valueChanged method
//
// Revision 1.5  2006/05/19 15:00:45  ounsy
// minor changes
//
// Revision 1.4  2006/03/20 10:34:40  ounsy
// removed useless logs
//
// Revision 1.3  2006/02/24 12:16:30  ounsy
// modified for HDB/TDB separation
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
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
package fr.soleil.mambo.actions.archiving.listeners;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.mambo.components.archiving.ACAttributesPropertiesTree;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.models.AttributesTreeModel;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;


public class ACAttributesPropertiesTreeSelectionListener implements TreeSelectionListener
{
    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged ( TreeSelectionEvent event )
    {
        //System.out.println ( "ACAttributesPropertiesTreeSelectionListener/valueChanged/START" );
        
        ACAttributesPropertiesTree tree = ( ACAttributesPropertiesTree ) event.getSource();
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) tree.getLastSelectedPathComponent();
        if ( node == null )
        {
            return;
        }
        if ( !node.isLeaf() )
        {
            applyDefaults ( "" , false );
            return;
        }

        TreeNode[] path = node.getPath();
        String completeName = AttributesTreeModel.translatePathIntoKey( path );
        ArchivingConfiguration currentArchivingConfiguration = ArchivingConfiguration.getCurrentArchivingConfiguration();
        ArchivingConfigurationAttribute selectedAttribute = currentArchivingConfiguration.getAttributes().getAttribute( completeName );

        ArchivingConfigurationAttributeHDBProperties HDBProperties = new ArchivingConfigurationAttributeHDBProperties();
        ArchivingConfigurationAttributeTDBProperties TDBProperties = new ArchivingConfigurationAttributeTDBProperties();

        boolean theSelectedAttributeBelongsToTheCurrentAC = false;
        if ( selectedAttribute == null )
        {
            theSelectedAttributeBelongsToTheCurrentAC = true;
        }
        else
        {
            String selectedAttributeName = selectedAttribute.getCompleteName();
            if ( !currentArchivingConfiguration.containsAttribute( selectedAttributeName ) )
            {
                theSelectedAttributeBelongsToTheCurrentAC = true;
            }
            else if ( currentArchivingConfiguration.getAttributes().getAttribute( selectedAttributeName ).isEmpty() )
            {
                theSelectedAttributeBelongsToTheCurrentAC = true;
            }
        }

        if ( theSelectedAttributeBelongsToTheCurrentAC )
        {
            // we reset the fields to defaults
            applyDefaults ( completeName , true );
        }
        else
        {
            ArchivingConfigurationAttributeProperties properties = selectedAttribute.getProperties();
            if ( currentArchivingConfiguration.isHistoric () )
            {
                HDBProperties = properties.getHDBProperties();
                HDBProperties.push();
            }
            else
            {
                TDBProperties = properties.getTDBProperties();
                TDBProperties.push();    
            }
        }
    }

    /**
     * @param completeName 
     * @param isLeafNode 
     * 
     */
    private void applyDefaults (String completeName, boolean isLeafNode)
    {
        Options options = Options.getInstance();
        ACOptions acOptions = options.getAcOptions();
        try
        {
            acOptions.applyDefaults();
        }
        catch ( Exception e )
        {
            //if there is a problem, we push empty properties (fields are resetted)
            ArchivingConfigurationAttributeHDBProperties HDBProperties = new ArchivingConfigurationAttributeHDBProperties();
            ArchivingConfigurationAttributeTDBProperties TDBProperties = new ArchivingConfigurationAttributeTDBProperties();

            HDBProperties.push();
            TDBProperties.push();
        }
        
        AttributesPropertiesPanel panel = AttributesPropertiesPanel.getInstance ();
        panel.setCurrentlySelectedAttribute ( completeName );
        
        if ( isLeafNode )
        {
            try 
            {
                IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl ();
                boolean _historic = panel.isHistoric ();
                Archiver currentArchiver = manager.getCurrentArchiverForAttribute ( completeName , _historic );
                panel.pushCurrentArchiver ( currentArchiver , completeName );
                panel.pushDedicatedArchiver ( completeName );
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }

}
