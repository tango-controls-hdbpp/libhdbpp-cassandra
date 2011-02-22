//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationData.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationData.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ArchivingConfigurationData.java,v $
// Revision 1.3  2006/02/24 12:21:12  ounsy
// modified for HDB/TDB separation
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
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
package fr.soleil.mambo.data.archiving;

import java.sql.Timestamp;

public class ArchivingConfigurationData
{
    private Timestamp creationDate;
    private Timestamp lastUpdateDate;

    private String name;
    private String path;

    private ArchivingConfiguration archivingConfiguration;
    public static final String XML_TAG = "archivingConfiguration";
    public static final String CREATION_DATE_PROPERTY_XML_TAG = "creationDate";
    public static final String LAST_UPDATE_DATE_PROPERTY_XML_TAG = "lastUpdateDate";
    public static final String NAME_PROPERTY_XML_TAG = "name";
    public static final String IS_MODIFIED_PROPERTY_XML_TAG = "isModified";
    public static final String IS_HISTORIC_PROPERTY_XML_TAG = "isHistoric";
    public static final String PATH_PROPERTY_XML_TAG = "path";

    public ArchivingConfigurationData ( Timestamp _creationDate , Timestamp _lastUpdateDate )
    {
        this.creationDate = _creationDate;
        this.lastUpdateDate = _lastUpdateDate;
    }


    /**
     * 
     */
    public ArchivingConfigurationData ()
    {
    }


    public Timestamp getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate ( Timestamp creationDate )
    {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdateDate ()
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate ( Timestamp lastUpdateDate )
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return Returns the archivingConfiguration.
     */
    public ArchivingConfiguration getArchivingConfiguration ()
    {
        return archivingConfiguration;
    }

    /**
     * @param archivingConfiguration The archivingConfiguration to set.
     */
    public void setArchivingConfiguration ( ArchivingConfiguration archivingConfiguration )
    {
        this.archivingConfiguration = archivingConfiguration;
    }

    /**
     * @return Returns the name.
     */
    public String getName ()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName ( String name )
    {
        this.name = name;
    }

    /**
     * @return Returns the path.
     */
    public String getPath ()
    {
        return path;
    }

    /**
     * @param path The path to set.
     */
    public void setPath ( String path )
    {
        this.path = path;
    }

    public boolean equals ( ArchivingConfigurationData this2 )
    {
        String name1 = this.getName() == null ? "" : this.getName();
        String name2 = this2.getName() == null ? "" : this2.getName();
        if ( !name1.equals( name2 ) )
        {
            return false;
        }

        Timestamp creationDate1 = this.getCreationDate();
        Timestamp creationDate2 = this2.getCreationDate();
        if ( creationDate1 == null )
        {
            if ( creationDate2 != null )
            {
                return false;
            }
        }
        else
        {
            if ( creationDate2 == null )
            {
                return false;
            }
            else
            {
                if ( !creationDate1.equals( creationDate2 ) )
                {
                    return false;
                }
            }
        }

        Timestamp lastUpdateDate1 = this.getLastUpdateDate();
        Timestamp lastUpdateDate2 = this2.getLastUpdateDate();
        if ( lastUpdateDate1 == null )
        {
            if ( lastUpdateDate2 != null )
            {
                return false;
            }
        }
        else
        {
            if ( lastUpdateDate2 == null )
            {
                return false;
            }
            else
            {
                if ( !lastUpdateDate1.equals( lastUpdateDate2 ) )
                {
                    return false;
                }
            }
        }

        return true;
    }
}
