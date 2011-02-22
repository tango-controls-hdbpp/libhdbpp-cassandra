//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/ConfigurationFileFilter.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ConfigurationFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ConfigurationFileFilter.java,v $
// Revision 1.2  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.components;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ConfigurationFileFilter extends FileFilter
{
    protected String description;
    protected String extension;

    /**
     * 
     */
    public ConfigurationFileFilter ( String _extension )
    {
        super();
        this.extension = _extension;
    }

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept ( File f )
    {
        if ( f.isDirectory() )
        {
            return true;
        }

        String _extension = getExtension( f );

        if ( _extension != null && _extension.equals( extension ) )
        {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription ()
    {
        return description;
    }

    public static String getExtension ( File f )
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf( '.' );

        if ( i > 0 && i < s.length() - 1 )
        {
            ext = s.substring( i + 1 ).toLowerCase();
        }

        return ext;
    }

    /**
     * @return Returns the extension.
     */
    public String getExtension ()
    {
        return extension;
    }
}
