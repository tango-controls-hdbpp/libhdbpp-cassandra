package fr.soleil.mambo.tools.xmlhelpers.ac;


import org.w3c.dom.Node;

import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public interface IArchivingConfigurationXMLHelper
{

    /**
     * @param location
     * @return 26 juil. 2005
     * @throws Exception
     */
    public ArchivingConfiguration loadArchivingConfigurationIntoHash ( String location ) throws Exception;
    

    /**
     * @param rootNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    public ArchivingConfiguration loadArchivingConfigurationIntoHashFromRoot ( Node rootNode ) throws Exception;

}
