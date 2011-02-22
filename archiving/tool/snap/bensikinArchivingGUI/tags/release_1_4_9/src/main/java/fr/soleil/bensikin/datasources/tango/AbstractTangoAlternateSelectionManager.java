/*	Synchrotron Soleil 
 *  
 *   File          :  AbstractTangoAlternateSelectionManager.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  14 sept. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: AbstractTangoAlternateSelectionManager.java,v 
 *
 */
 /*
 * Created on 14 sept. 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.datasources.tango;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public abstract class AbstractTangoAlternateSelectionManager implements ITangoAlternateSelectionManager 
{
    AbstractTangoAlternateSelectionManager() 
    {
        super();
    }

    public BensikinDeviceClass [] loadDeviceClasses ( Domain [] domains ) throws SnapshotingException 
    {
        //int len = domains == null ? 0 : domains.length;
        //long before = System.currentTimeMillis ();
        //System.out.println ( "  BasicTangoAlternateSelectionManagerImpl|loadDeviceClasses|number of domains|"+len+"|" );
        
        if ( domains == null || domains.length == 0 )
        {
            return null;
        }
        int numberOfDomains = domains.length;

        Vector deviceClassesSubSets = new Vector ( numberOfDomains );
        int totalNumberOfDeviceClasses = 0;
        
        for ( int i = 0 ; i < numberOfDomains ; i++ )
        {
            Domain currentDomain = domains [ i ];

            BensikinDeviceClass [] currentDeviceClassesSubset = this.loadDeviceClasses ( currentDomain );
            int sizeOfCurrentDeviceClassesSubset = currentDeviceClassesSubset == null ? 0 : currentDeviceClassesSubset.length;
            for ( int j = 0; j < sizeOfCurrentDeviceClassesSubset ; j ++ )
            {
                currentDeviceClassesSubset [ j ].setDomain ( currentDomain );
            }
            
            totalNumberOfDeviceClasses += sizeOfCurrentDeviceClassesSubset;
            if ( sizeOfCurrentDeviceClassesSubset != 0 )
            {
                deviceClassesSubSets.add ( currentDeviceClassesSubset );    
            }
        }
        
        //long middle = System.currentTimeMillis ();
        //long delta1 = middle - before;
        //System.out.println ( "  BasicTangoAlternateSelectionManagerImpl|loadDeviceClasses|Time to load|"+delta1+"|" );
        
        BensikinDeviceClass [] ret = AbstractTangoAlternateSelectionManager.buildDeviceClassesList ( totalNumberOfDeviceClasses , deviceClassesSubSets );
        
        return ret;
    }

   /* public abstract Domain[] loadDomains(String domainsRegExp) throws ArchivingException;
    public abstract Domain[] loadDomains() throws ArchivingException;
    public abstract BensikinDeviceClass[] loadDeviceClasses(Domain domain) throws ArchivingException;
    public abstract ContextAttribute[] loadACAttributes(Domain domain, BensikinDeviceClass deviceClass) throws ArchivingException ;
    public abstract ContextAttribute[] loadACAttributes(BensikinDeviceClass deviceClass, ContextAttribute brother);*/

    private static BensikinDeviceClass[] buildDeviceClassesList(int totalNumberOfDeviceClasses, Vector deviceClassesSubSets) 
    {
        //System.out.println ( "AbstractTangoAlternateSelectionManager/buildDeviceClassesList/START/totalNumberOfDeviceClasses|"+totalNumberOfDeviceClasses+"|" );
        //long middle = System.currentTimeMillis ();
        
        BensikinDeviceClass [] ret = new BensikinDeviceClass [ totalNumberOfDeviceClasses ];
        Enumeration enumeration = deviceClassesSubSets.elements ();
        int k = 0;
        while ( enumeration.hasMoreElements () )
        {
            BensikinDeviceClass [] currentDeviceClassesSubset = (BensikinDeviceClass[]) enumeration.nextElement ();
            int sizeOfCurrentDeviceClassesSubset = currentDeviceClassesSubset.length;
            //System.out.println ( "    AbstractTangoAlternateSelectionManager/buildDeviceClassesList/1/sizeOfCurrentDeviceClassesSubset|"+sizeOfCurrentDeviceClassesSubset+"|" );
            
            for ( int i = 0 ; i < sizeOfCurrentDeviceClassesSubset; i++ )
            {
                ret [ k ] = currentDeviceClassesSubset [ i ];
                //System.out.println ( "        AbstractTangoAlternateSelectionManager/buildDeviceClassesList/2/i|"+i+"|currentDeviceClassesSubset [ i ].getName()|"+ currentDeviceClassesSubset [ i ].getName()+"|" );
                /*Iterator it = ret [ k ].getDevices().iterator ();
                while ( it.hasNext () )
                {
                    Member nextMember = (Member) it.next ();
                    System.out.println ( "            AbstractTangoAlternateSelectionManager/buildDeviceClassesList/2 BIS/nextMember|"+ nextMember.getName()+"|" );
                }*/
                
                k ++;
            }
        }
        //System.out.println ();
        //let's make sure the device classes are unique, ie. haven't been loaded for different domains 
        Hashtable uniqueNames = new Hashtable ();
        for ( int i = 0 ; i < totalNumberOfDeviceClasses ; i++ )
        {
            //System.out.println ( "AbstractTangoAlternateSelectionManager/buildDeviceClassesList/3/i|"+i+"|ret [ i ].getName ()|"+ret [ i ].getName ()+"|" );
            BensikinDeviceClass alreadyAccountedForDeviceClass = (BensikinDeviceClass) uniqueNames.get ( ret [ i ].getName () );
            if ( alreadyAccountedForDeviceClass == null )
            {
                alreadyAccountedForDeviceClass = ret [ i ];
            }
            else
            {
                Vector membersToAdd = ret [ i ].getDevices ();
                alreadyAccountedForDeviceClass.addDevices ( membersToAdd );    
            }
            uniqueNames.put ( alreadyAccountedForDeviceClass.getName () , alreadyAccountedForDeviceClass );
            //uniqueNames.put ( ret [ i ].getName () , ret [ i ] );
        }
        ret = new BensikinDeviceClass [ uniqueNames.size () ];
        Enumeration uniqueNamesEnum = uniqueNames.keys ();
        int l = 0;
        while ( uniqueNamesEnum.hasMoreElements () )
        {
            String nextUniqueName = (String) uniqueNamesEnum.nextElement ();
            BensikinDeviceClass nextUniqueDeviceClass = (BensikinDeviceClass) uniqueNames.get ( nextUniqueName );
            ret [ l ] = nextUniqueDeviceClass;
            l ++;
        }
        
        //long after = System.currentTimeMillis ();
        //long delta2 = after - middle;
        //System.out.println ( "  BasicTangoAlternateSelectionManagerImpl|loadDeviceClasses|Time to order|"+delta2+"|" );
        
        return ret;
    }
}
