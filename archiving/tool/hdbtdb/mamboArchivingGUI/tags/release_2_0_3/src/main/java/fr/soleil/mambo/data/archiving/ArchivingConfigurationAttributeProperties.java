//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttributeProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationAttributeProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ArchivingConfigurationAttributeProperties.java,v $
// Revision 1.3  2006/02/24 12:21:19  ounsy
// small modifications
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

import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.GUIUtilities;

public class ArchivingConfigurationAttributeProperties
{
    private ArchivingConfigurationAttributeHDBProperties HDBProperties;
    private ArchivingConfigurationAttributeTDBProperties TDBProperties;
    private ArchivingConfigurationAttribute attribute;

    private static ArchivingConfigurationAttributeProperties currentProperties = null;

    public ArchivingConfigurationAttributeProperties ( ArchivingConfigurationAttributeHDBProperties _HDBProperties , ArchivingConfigurationAttributeTDBProperties _TDBProperties )
    {
        this.HDBProperties = _HDBProperties;
        this.TDBProperties = _TDBProperties;

        this.HDBProperties.setProperties( this );
        this.TDBProperties.setProperties( this );
    }


    /**
     * 
     */
    public ArchivingConfigurationAttributeProperties ()
    {
        this.HDBProperties = new ArchivingConfigurationAttributeHDBProperties();
        this.TDBProperties = new ArchivingConfigurationAttributeTDBProperties();

        this.HDBProperties.setProperties( this );
        this.TDBProperties.setProperties( this );
    }


    public ArchivingConfigurationAttributeHDBProperties getHDBProperties ()
    {
        return HDBProperties;
    }

    public void setHDBProperties ( ArchivingConfigurationAttributeHDBProperties properties )
    {
        HDBProperties = properties;

        this.HDBProperties = properties;
        if ( this.HDBProperties != null )
        {
            this.HDBProperties.setProperties( this );
        }
    }

    public ArchivingConfigurationAttributeTDBProperties getTDBProperties ()
    {
        return TDBProperties;
    }

    public void setTDBProperties ( ArchivingConfigurationAttributeTDBProperties properties )
    {
        this.TDBProperties = properties;
        if ( this.TDBProperties != null )
        {
            this.TDBProperties.setProperties( this );
        }
    }

    /**
     * @return Returns the attribute.
     */
    public ArchivingConfigurationAttribute getAttribute ()
    {
        return attribute;
    }

    /**
     * @param attribute The attribute to set.
     */
    public void setAttribute ( ArchivingConfigurationAttribute attribute )
    {
        this.attribute = attribute;
    }

    /**
     * @return Returns the currentProperties.
     */
    public static ArchivingConfigurationAttributeProperties getCurrentProperties ()
    {
        return currentProperties;
    }

    /**
     * @param currentProperties The currentProperties to set.
     */
    public static void setCurrentProperties ( ArchivingConfigurationAttributeProperties currentProperties )
    {
        ArchivingConfigurationAttributeProperties.currentProperties = currentProperties;
    }


    /**
     * @param currentMode
     * @param historic    25 juil. 2005
     */
    public void addMode ( ArchivingConfigurationMode currentMode , boolean historic )
    {
        if ( historic )
        {
            //ArchivingConfigurationAttributeHDBProperties currentHDBProperties = this.getHDBProperties ();
            this.HDBProperties.addMode( currentMode );

            /*System.out.println ( "--------currentHDBProperties START---------" );
            System.out.println ( this.HDBProperties.toString () );
            System.out.println ( "--------currentHDBProperties END-----------" );*/
        }
        else
        {
            //ArchivingConfigurationAttributeTDBProperties currentTDBProperties = this.getTDBProperties ();
            this.TDBProperties.addMode( currentMode );

            /*System.out.println ( "--------currentTDBProperties START---------" );
            System.out.println ( this.TDBProperties.toString () );
            System.out.println ( "--------currentTDBProperties END-----------" );*/
        }

    }


    /**
     * 26 juil. 2005
     */
    public void reset ()
    {
        this.HDBProperties = new ArchivingConfigurationAttributeHDBProperties();
        this.TDBProperties = new ArchivingConfigurationAttributeTDBProperties();

        this.HDBProperties.setProperties( this );
        this.TDBProperties.setProperties( this );

    }


    /**
     * 26 juil. 2005
     */
    public static void resetCurrentProperties ()
    {
        //currentProperties = new ArchivingConfigurationAttributeProperties();
        Options options = Options.getInstance();
        ACOptions acOptions = options.getAcOptions();
        currentProperties = acOptions.getDefaultACProperties();

    }
    
    public String toString ()
    {
        String ret = "";
        
        ret += this.HDBProperties.toString();
        ret += GUIUtilities.CRLF;

        ret += this.TDBProperties.toString();
        ret += GUIUtilities.CRLF;

        return ret;
    }
}
