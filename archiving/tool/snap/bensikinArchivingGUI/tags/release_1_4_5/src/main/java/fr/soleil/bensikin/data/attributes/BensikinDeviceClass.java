package fr.soleil.bensikin.data.attributes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import fr.esrf.TangoDs.DeviceClass;

public class BensikinDeviceClass
{
    private String name;
    private Vector devices;
    private Domain domain;

    /**
     * @param arg0
     * @throws fr.esrf.Tango.DevFailed
     */
    public BensikinDeviceClass ( String _name )
    {
        //super ( arg0 );
        this.name = _name;
        this.devices = new Vector();
    }

    /**
     * @param arg0
     * @throws fr.esrf.Tango.DevFailed
     */
    public BensikinDeviceClass ( DeviceClass in )
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
