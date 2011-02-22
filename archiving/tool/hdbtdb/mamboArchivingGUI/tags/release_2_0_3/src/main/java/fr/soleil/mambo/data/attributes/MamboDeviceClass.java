//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/attributes/MamboDeviceClass.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboDeviceClass.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: MamboDeviceClass.java,v $
// Revision 1.5  2007/09/17 07:02:58  ounsy
// for Mambo Web classes implements java.io.serializable.
//
// Revision 1.4  2006/10/19 12:41:51  ounsy
// added addDevices() to add several devices at once
//
// Revision 1.3  2006/05/16 12:00:33  ounsy
// minor changes
//
// Revision 1.2  2006/03/07 14:40:14  ounsy
// "Domain *" bug correction
//
// Revision 1.1  2005/11/29 18:28:26  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.attributes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import fr.esrf.TangoDs.DeviceClass;

public class MamboDeviceClass implements java.io.Serializable //extends DeviceClass 
{
    private String name;
    private Vector devices;
    private Domain domain;

    /**
     * @param arg0
     * @throws fr.esrf.Tango.DevFailed
     */
    public MamboDeviceClass ( String _name )
    {
        //super ( arg0 );
        this.name = _name;
        this.devices = new Vector();
    }

    /**
     * @param arg0
     * @throws fr.esrf.Tango.DevFailed
     */
    public MamboDeviceClass ( DeviceClass in )
    {
        //super ( arg0 );
        this.name = in.get_name();
        this.devices = new Vector();
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

    public void addDevice ( Member member )
    {
        //--------
        String memberName = member.getName();
        Enumeration enumeration = this.devices.elements();
        while ( enumeration.hasMoreElements() )
        {
            Member mem = ( Member ) enumeration.nextElement();
            String mem_s = mem.getName();
            if ( mem_s != null && mem_s.equals( memberName ) )
            {
                devices.remove( mem );
            }
        }
        //--------

        devices.add( member );
    }

    /**
     * @return Returns the devices.
     */
    public Vector getDevices ()
    {
        return devices;
    }

    /**
     * @param devices The devices to set.
     */
    public void setDevices ( Vector devices )
    {
        this.devices = devices;
    }
    /**
     * @return Returns the domain.
     */
    public Domain getDomain() {
        return domain;
    }
    /**
     * @param domain The domain to set.
     */
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public void addDevices(Vector membersToAdd) 
    {
        if ( membersToAdd == null )
        {
            return;
        }
     
        Iterator it = membersToAdd.iterator ();
        while ( it.hasNext () )
        {
            Member next = (Member) it.next (); 
            this.addDevice ( next );   
        }
    }
}
