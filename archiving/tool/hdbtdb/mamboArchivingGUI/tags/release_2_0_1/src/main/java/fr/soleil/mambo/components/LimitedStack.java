//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/LimitedStack.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LimitedStack.
//						(Claisse Laurent) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: LimitedStack.java,v $
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
package fr.soleil.mambo.components;

import java.util.Vector;

import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.VCOptions;


public class LimitedStack extends Vector
{
    //MULTI-CONF
    private int maxSize;

    /**
     * 
     */
    public LimitedStack ( int _maxSize )
    {
        super();
        this.maxSize = _maxSize;
    }

    /**
     * 
     */
    public LimitedStack ()
    {

        super();

        Options options = Options.getInstance();
        VCOptions vcOptions = options.getVcOptions();
        int _maxSize = vcOptions.getStackDepth();
        this.maxSize = _maxSize;
    }

    public Object push ( Object item )
    {
        super.removeElement( item );

        if ( super.size() == this.maxSize )
        {
            super.remove( super.lastElement() );
        }

        super.insertElementAt( item , 0 );
        return item;
    }


    /**
     * @return Returns the maxSize.
     */
    public int getMaxSize ()
    {
        return maxSize;
    }

    /**
     * @param maxSize The maxSize to set.
     */
    public void setMaxSize ( int _maxSize )
    {
        while ( _maxSize < super.size() )
        {
            super.remove( super.lastElement() );
        }

        this.maxSize = _maxSize;
    }

    public void selectElement ( Object item )
    {
        super.removeElement( item );
        this.push( item );
    }

    public void selectElement ( int idx )
    {
        Object item = super.elementAt( idx );
        this.selectElement( item );
    }
}
