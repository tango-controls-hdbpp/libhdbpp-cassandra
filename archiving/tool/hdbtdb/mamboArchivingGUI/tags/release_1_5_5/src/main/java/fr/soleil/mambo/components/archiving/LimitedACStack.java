//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/LimitedACStack.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LimitedACStack.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: LimitedACStack.java,v $
// Revision 1.2  2006/05/16 09:36:11  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:24  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.archiving;

import java.util.Enumeration;

import fr.soleil.mambo.components.LimitedStack;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationData;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.tools.GUIUtilities;


public class LimitedACStack extends LimitedStack
{

    /**
     * @param _maxSize
     */
    public LimitedACStack ( int _maxSize )
    {
        super( _maxSize );
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public LimitedACStack ()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public boolean removeElement ( Object item )
    {
        boolean ret = super.removeElement( item );
        if ( ret )
        {
            return ret;
        }

        ArchivingConfiguration vc = ( ArchivingConfiguration ) item;
        Enumeration enumeration = this.elements();
        while ( enumeration.hasMoreElements() )
        {
            ArchivingConfiguration next = ( ArchivingConfiguration ) enumeration.nextElement();
            if ( next.equals( vc ) )
            {
                return super.removeElement( next );
            }
        }
        return false;
    }

    public String toString ()
    {
        Enumeration enumeration = super.elements();
        String ret = "";

        while ( enumeration.hasMoreElements() )
        {
            ArchivingConfiguration vc = ( ArchivingConfiguration ) enumeration.nextElement();

            ret += vc.toString();
            ret += GUIUtilities.CRLF;
        }

        return ret;
    }

    /**
     * @return
     */
    public Object toString2 ()
    {
        Enumeration enumeration = super.elements();
        String ret = "";

        while ( enumeration.hasMoreElements() )
        {
            ArchivingConfiguration vc = ( ArchivingConfiguration ) enumeration.nextElement();
            ArchivingConfigurationData vcData = vc.getData();
            ret += vcData.getName();
            ret += "|";
            ret += vcData.getPath();
            ret += GUIUtilities.CRLF;
        }

        return ret;
    }

    /**
     * @param viewManager
     * @throws Exception
     */
    public boolean save ( IArchivingConfigurationManager viewManager , ILogger logger )
    {
        Enumeration enumeration = this.elements();
        boolean oneMissed = false;

        while ( enumeration.hasMoreElements() )
        {
            ArchivingConfiguration openedArchivingConfiguration = ( ArchivingConfiguration ) enumeration.nextElement();
            if ( !openedArchivingConfiguration.isModified() )
            {
                continue;
            }

            try
            {
                openedArchivingConfiguration.save( viewManager , false );
            }
            catch ( Exception e )
            {
                openedArchivingConfiguration.setModified( true );
                openedArchivingConfiguration.setPath( null );
                oneMissed = true;

                logger.trace( ILogger.LEVEL_WARNING , e );
            }
        }

        return oneMissed;
    }

    ArchivingConfiguration getSelectedAC ()
    {
        if ( super.size() == 0 )
        {
            return null;
        }

        return ( ArchivingConfiguration ) super.firstElement();
    }
}
