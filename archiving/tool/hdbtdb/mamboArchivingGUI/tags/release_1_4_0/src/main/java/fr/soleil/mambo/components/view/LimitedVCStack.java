//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/LimitedVCStack.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LimitedVCStack.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: LimitedVCStack.java,v $
// Revision 1.2  2006/05/16 09:36:27  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:28:12  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.view;

import java.util.Enumeration;

import fr.soleil.mambo.components.LimitedStack;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.tools.GUIUtilities;


public class LimitedVCStack extends LimitedStack
{

    /**
     * @param _maxSize
     */
    public LimitedVCStack ( int _maxSize )
    {
        super( _maxSize );
        // TODO Auto-generated constructor stub
    }

    /**
     * 
     */
    public LimitedVCStack ()
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

        ViewConfiguration vc = ( ViewConfiguration ) item;
        Enumeration enumeration = this.elements();
        while ( enumeration.hasMoreElements() )
        {
            ViewConfiguration next = ( ViewConfiguration ) enumeration.nextElement();
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
            ViewConfiguration vc = ( ViewConfiguration ) enumeration.nextElement();

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
            ViewConfiguration vc = ( ViewConfiguration ) enumeration.nextElement();
            ViewConfigurationData vcData = vc.getData();
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
    public boolean save ( IViewConfigurationManager viewManager , ILogger logger )
    {
        Enumeration enumeration = this.elements();
        boolean oneMissed = false;

        while ( enumeration.hasMoreElements() )
        {
            ViewConfiguration openedViewConfiguration = ( ViewConfiguration ) enumeration.nextElement();
            if ( !openedViewConfiguration.isModified() )
            {
                continue;
            }

            try
            {
                //System.out.println ( "LimitedVCStack/save/BEFORE/"+this.size () );
                openedViewConfiguration.save( viewManager , false );
                //System.out.println ( "LimitedVCStack/save/AFTER/"+this.size () );
            }
            catch ( Exception e )
            {
                openedViewConfiguration.setModified( true );
                openedViewConfiguration.setPath( null );
                oneMissed = true;

                logger.trace( ILogger.LEVEL_WARNING , e );
            }
        }

        return oneMissed;
    }

    ViewConfiguration getSelectedVC ()
    {
        if ( super.size() == 0 )
        {
            return null;
        }

        return ( ViewConfiguration ) super.firstElement();
    }
}
