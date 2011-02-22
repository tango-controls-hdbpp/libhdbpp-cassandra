//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/AttributesSelectComboBox.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OperatorsList.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.8 $
//
//$Log: AttributesSelectComboBox.java,v $
//Revision 1.8  2006/11/29 10:08:39  ounsy
//minor changes
//
//Revision 1.7  2006/11/23 10:03:26  ounsy
//minor changes
//
//Revision 1.6  2006/10/31 16:50:17  ounsy
//minor changes
//
//Revision 1.5  2006/10/19 12:37:16  ounsy
//minor changes
//
//Revision 1.4  2006/09/20 12:50:06  ounsy
//changed imports
//
//Revision 1.3  2006/09/14 11:30:00  ounsy
//minor changes
//
//Revision 1.2  2006/02/24 12:18:37  ounsy
//added a setDeviceClasses (Domain []) method for domain* loading
//
//Revision 1.1  2005/11/29 18:27:24  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.archiving;

import java.awt.Dimension;
import java.util.Hashtable;
import javax.swing.JComboBox;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.actions.archiving.listeners.AttributesSelectItemListener;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;
import fr.soleil.mambo.datasources.tango.alternate.ITangoAlternateSelectionManager;
import fr.soleil.mambo.datasources.tango.alternate.TangoAlternateSelectionManagerFactory;

public class AttributesSelectComboBox extends JComboBox
{
    public static final String NO_SELECTION = "---";

    public static final int DOMAIN_TYPE = 0;
    public static final int DEVICE_CLASS_TYPE = 1;
    public static final int ATTRIBUTE_TYPE = 2;

    private int type;
    private Hashtable elements;

    /**
     * @param type
     * @throws IllegalStateException
     */
    public AttributesSelectComboBox ( int _type ) throws IllegalStateException
    {
        super();
        this.type = _type;
        this.reset();

        switch ( _type )
        {
            case DOMAIN_TYPE:
                this.setDomains();
                break;

            case DEVICE_CLASS_TYPE:

                break;

            case ATTRIBUTE_TYPE:

                break;

            default :
                throw new IllegalStateException( "Expected either of " + DOMAIN_TYPE + "," + DEVICE_CLASS_TYPE + "," + ATTRIBUTE_TYPE + " as a parameter. Received " + type + " instead." );
        }

        Dimension maxDimension = new Dimension( Integer.MAX_VALUE , 20 );
        this.setMaximumSize( maxDimension );

        this.addItemListener( new AttributesSelectItemListener() );
    }


    /**
     * 
     */
    public void setDomains ()
    {
        if ( this.type != AttributesSelectComboBox.DOMAIN_TYPE )
        {
            throw new IllegalStateException();
        }

        this.reset();
        ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();

        Domain[] domains = null;
        try
        {
            domains = manager.loadDomains();
        }
        catch ( ArchivingException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ( domains == null )
        {
            return;
        }

        for ( int i = 0 ; i < domains.length ; i++ )
        {
            super.addItem( domains[ i ].getName() );
            this.elements.put( domains[ i ].getName() , domains[ i ] );
        }
    }

    public void setDeviceClasses ( Domain domain ) throws ArchivingException
    {
        if ( this.type != AttributesSelectComboBox.DEVICE_CLASS_TYPE )
        {
            throw new IllegalStateException();
        }
        this.reset();
        ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();

        MamboDeviceClass[] deviceClasses = manager.loadDeviceClasses( domain );       
        if ( deviceClasses == null )
        {
            return;
        }

        for ( int i = 0 ; i < deviceClasses.length ; i++ )
        {
            super.addItem( deviceClasses[ i ].getName() );
            this.elements.put( deviceClasses[ i ].getName() , deviceClasses[ i ] );
        }

    }

    public void setACAttributes ( Domain domain , MamboDeviceClass deviceClass )
    {
        if ( this.type != AttributesSelectComboBox.ATTRIBUTE_TYPE )
        {
            throw new IllegalStateException();
        }
        this.reset();
        ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();

        ArchivingConfigurationAttribute[] attributes = null;
        try
        {
            attributes = manager.loadACAttributes( domain , deviceClass );
        }
        catch ( ArchivingException e )
        {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        if ( attributes == null )
        {
            return;
        }

        for ( int i = 0 ; i < attributes.length ; i++ )
        {
            super.addItem( attributes[ i ].getName() );
            if ( attributes[ i ] == null || attributes[ i ].getName() == null )
            {
                continue;
            }
            this.elements.put( attributes[ i ].getName() , attributes[ i ] );
        }
    }


    /**
     * @return Returns the type.
     */
    public int getType ()
    {
        return type;
    }

    /**
     * 
     */
    public void reset ()
    {
        super.removeAllItems();
        super.addItem( NO_SELECTION );

        this.elements = new Hashtable();
    }

    public ArchivingConfigurationAttribute getSelectedAttribute ()
    {
        if ( this.type != AttributesSelectComboBox.ATTRIBUTE_TYPE )
        {
            throw new IllegalStateException();
        }

        String currentAttributeName = ( String ) super.getSelectedItem();
        if ( currentAttributeName == null || currentAttributeName.equals( AttributesSelectComboBox.NO_SELECTION ) )
        {
            return null;
        }

        ArchivingConfigurationAttribute ret = ( ArchivingConfigurationAttribute ) this.elements.get( currentAttributeName );
        return ret;
    }


    /**
     * @return
     */
    public MamboDeviceClass getSelectedDeviceClass ()
    {
        if ( this.type != AttributesSelectComboBox.DEVICE_CLASS_TYPE )
        {
            throw new IllegalStateException();
        }

        String currentDeviceClassName = ( String ) super.getSelectedItem();
        if ( currentDeviceClassName == null || currentDeviceClassName.equals( AttributesSelectComboBox.NO_SELECTION ) )
        {
            return null;
        }

        MamboDeviceClass ret = ( MamboDeviceClass ) this.elements.get( currentDeviceClassName );
        return ret;
    }


    /**
     * @return
     */
    public Domain getSelectedDomain ()
    {
        if ( this.type != AttributesSelectComboBox.DOMAIN_TYPE )
        {
            throw new IllegalStateException();
        }

        String currentDomainName = ( String ) super.getSelectedItem();
        if ( currentDomainName == null || currentDomainName.equals( AttributesSelectComboBox.NO_SELECTION ) )
        {
            return null;
        }

        Domain ret = ( Domain ) this.elements.get( currentDomainName );
        return ret;
    }


    /**
     * @param domains
     * @throws ArchivingException
     */
    public void setDeviceClasses(Domain[] domains) throws ArchivingException 
    {
        if ( this.type != AttributesSelectComboBox.DEVICE_CLASS_TYPE )
        {
            throw new IllegalStateException();
        }
        this.reset();
        ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();

        MamboDeviceClass[] deviceClasses = manager.loadDeviceClasses( domains );
        if ( deviceClasses == null )
        {
            return;
        }

        for ( int i = 0 ; i < deviceClasses.length ; i++ )
        {
            /*System.out.println ( "AttributesSelectComboBox/setDeviceClasses/i|"+i+"|deviceClasses[ i ].getName()|"+deviceClasses[ i ].getName()+"|" );
            Iterator it = deviceClasses[ i ].getDevices ().iterator ();
            while ( it.hasNext () )
            {
                Member nextDevice = (Member) it.next ();
                System.out.println ( "AttributesSelectComboBox/setDeviceClasses/nextDevice|"+nextDevice.getName()+"|" );
            }*/
            
            super.addItem( deviceClasses[ i ].getName() );
            this.elements.put( deviceClasses[ i ].getName() , deviceClasses[ i ] );
        }
    }
    
    public void setDeviceClasses(MamboDeviceClass[] deviceClasses) throws ArchivingException 
    {
        if ( this.type != AttributesSelectComboBox.DEVICE_CLASS_TYPE )
        {
            throw new IllegalStateException();
        }
        this.reset();
        
        if ( deviceClasses == null )
        {
            return;
        }

        for ( int i = 0 ; i < deviceClasses.length ; i++ )
        {
            super.addItem( deviceClasses[ i ].getName() );
            this.elements.put( deviceClasses[ i ].getName() , deviceClasses[ i ] );
        }
    }


    public void selectNeutralElement() 
    {
        this.setSelectedItem ( NO_SELECTION );    
    }
}
