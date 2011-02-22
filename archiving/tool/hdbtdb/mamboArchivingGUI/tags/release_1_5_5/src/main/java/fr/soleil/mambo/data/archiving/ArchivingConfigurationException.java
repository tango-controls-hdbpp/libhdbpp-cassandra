//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationException.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationException.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ArchivingConfigurationException.java,v $
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

public class ArchivingConfigurationException extends Exception
{
    private String attributeName;
    private int code;

    /**
     * @param arg0
     */
    public ArchivingConfigurationException ( String _attributeName , int _code )
    {
        super();

        this.attributeName = _attributeName;
        this.code = _code;
    }


    /**
     * @return Returns the attributeName.
     */
    public String getAttributeName ()
    {
        return attributeName;
    }

    /**
     * @param attributeName The attributeName to set.
     */
    public void setAttributeName ( String attributeName )
    {
        this.attributeName = attributeName;
    }

    /**
     * @return Returns the code.
     */
    public int getCode ()
    {
        return code;
    }

    /**
     * @param code The code to set.
     */
    public void setCode ( int code )
    {
        this.code = code;
    }
}
