/*	Synchrotron Soleil 
 *  
 *   File          :  MamboAlternateEntitiesComparator.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  16 mai 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: MamboAlternateEntitiesComparator.java,v 
 *
 */
 /*
 * Created on 16 mai 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.comparators;

import java.util.Comparator;

import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;


public class MamboAlternateEntitiesComparator implements Comparator 
{

    public MamboAlternateEntitiesComparator() 
    {
        super();
    }

    public int compare(Object obj1, Object obj2) 
    {
        int ret;
        
        if ( obj1 == null )
        {
            if ( obj2 != null )
            {
                ret = 1;
            }
            else
            {
                ret = 0;
            }
        }
        else if ( obj2 == null )
        {
            ret = -1;
        }
        else
        {
            //ret = obj1.compareNotNull(obj2);
            ret = compareNotNull ( obj1 , obj2 );
        }
        return ret;
    }

    private int compareNotNull(Object obj1, Object obj2) 
    {
        String firstCriterion = null;
        String secondCriterion = null;
        
        if ( obj1 instanceof MamboDeviceClass )
        {
            MamboDeviceClass mamboDeviceClass1 = (MamboDeviceClass) obj1;
            MamboDeviceClass mamboDeviceClass2 = (MamboDeviceClass) obj2;
            
            firstCriterion = mamboDeviceClass1.getName ();
            secondCriterion = mamboDeviceClass2.getName ();
        }
        if ( obj1 instanceof ArchivingConfigurationAttribute )
        {
            ArchivingConfigurationAttribute archivingConfigurationAttribute1 = (ArchivingConfigurationAttribute) obj1;
            ArchivingConfigurationAttribute archivingConfigurationAttribute2 = (ArchivingConfigurationAttribute) obj2;
            
            firstCriterion = archivingConfigurationAttribute1.getName ();
            secondCriterion = archivingConfigurationAttribute2.getName ();
        }
        if ( obj1 instanceof Domain )
        {
            Domain domain1 = (Domain) obj1;
            Domain domain2 = (Domain) obj2;
            
            firstCriterion = domain1.getName ();
            secondCriterion = domain2.getName ();
        }
        
        return this.compareStrings ( firstCriterion , secondCriterion );
    }
    
    public int compareStrings(String name1, String name2) 
    {
        int ret;
        
        if ( name1 == null )
        {
            if ( name2 != null )
            {
                ret = 1;
            }
            else
            {
                ret = 0;
            }
        }
        else if ( name2 == null )
        {
            ret = -1;
        }
        else
        {
            name1 = name1.toLowerCase ();
            name2 = name2.toLowerCase ();
            
            ret = name1.compareTo(name2);
        }
        
        return ret;
    }

}
