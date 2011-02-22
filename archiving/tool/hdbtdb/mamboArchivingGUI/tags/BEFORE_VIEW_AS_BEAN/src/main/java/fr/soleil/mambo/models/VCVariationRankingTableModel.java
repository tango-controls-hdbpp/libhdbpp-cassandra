//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/VCVariationRankingTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationRankingTableModel.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCVariationRankingTableModel.java,v $
// Revision 1.4  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.3  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.2  2005/12/15 11:44:17  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.models;

import java.text.Collator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.comparators.ViewDataComparator;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.Messages;

public class VCVariationRankingTableModel extends DefaultTableModel
{
    private static VCVariationRankingTableModel instance;
    private boolean historic;
    private TreeMap attributes;
    private Object[][] attributesData;

    public static VCVariationRankingTableModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new VCVariationRankingTableModel();
        }
        return instance;
    }

    private VCVariationRankingTableModel ()
    {
        super();
        historic = true;
        attributes = new TreeMap(Collator.getInstance());
        attributesData = new Object[ 0 ][ 2 ];
    }

    public void update ()
    {
        IAttributeManager attributeManager = AttributeManagerFactory.getCurrentImpl ();
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        // getting ViewConfigurationAttributes
        ViewConfiguration vc = ViewConfiguration.getSelectedViewConfiguration();
        ViewConfigurationAttributes vcas = vc.getAttributes();
        attributes.clear();
        if ( vcas != null )
        {
            TreeMap attrTable = vcas.getAttributes();
            ViewConfigurationData data = vc.getData();
            if ( data != null )
            {
                historic = data.isHistoric();
                Set keys = attrTable.keySet();
                Iterator it = keys.iterator();
                while ( it.hasNext() )
                {
                    Object key = it.next();
                    ViewConfigurationAttribute attr = ( ViewConfigurationAttribute ) attrTable
                            .get( key );
                    String name = attr.getCompleteName();
                    if ( attributeManager.getFormat( name , historic ) == AttrDataFormat._SCALAR )
                    {
                        String[] param = new String[ 3 ];
                        param[ 0 ] = name;
                        
                        try 
                        {
                            param[ 1 ] =  extractingManager.timeToDateSGBD ( data.getStartDate().getTime() );
                            param[ 2 ] =  extractingManager.timeToDateSGBD ( data.getEndDate().getTime() );
                            
                            attributes.put( name , extractingManager.getMinAndMax( param , historic ) );
                        } 
                        catch (DevFailed e) 
                        {
                            e.printStackTrace();
                        } 
                        catch (ArchivingException e) 
                        {
                            e.printStackTrace();
                        } 
                    }
                }
            }
        }
        // preparing table
        attributesData = new Object[ attributes.size() ][ 2 ];
        Set keys = attributes.keySet();
        Iterator it = keys.iterator();
        int i = 0;
        while ( it.hasNext() )
        {
            Object[] table = new Object[ 2 ];
            String name = ( String ) it.next();
            table[ 0 ] = name;
            double factor = 1;
            if ( vcas != null )
            {
                ViewConfigurationAttribute vca = vcas.getAttribute( name );
                if ( vca != null )
                {
                    factor = vca.getFactor();
                }
            }
            Double balancedFactor;
            Double[] minMax = ( Double[] ) attributes.get( name );
            balancedFactor = new Double( factor * ( minMax[ 1 ].doubleValue() - minMax[ 0 ].doubleValue() ) );
            table[ 1 ] = balancedFactor;
            attributesData[ i ] = table;
            i++;
        }
        // sorting table
        Vector v = new Vector();
        for ( i = 0 ; i < attributesData.length ; i++ )
        {
            Double d = ( Double ) attributesData[ i ][ 1 ];
            if ( !d.equals( new Double( Double.NaN ) ) )
            {
                v.add( attributesData[ i ] );
            }
        }
        Collections.sort( v , new ViewDataComparator() );
        Collections.reverse( v );
        attributesData = new Object[ v.size() ][ 2 ];
        for ( i = 0 ; i < attributesData.length ; i++ )
        {
            attributesData[ i ] = ( Object[] ) v.get( i );
        }
        fireTableStructureChanged();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        return 2;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount ()
    {
        if ( attributesData == null )
        {
            return 0;
        }
        if ( attributesData.length < 100 )
        {
            return attributesData.length;
        }
        else
        {
            return 100;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public void setValueAt ( Object aValue , int rowIndex , int columnIndex )
    {
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt ( int rowIndex , int columnIndex )
    {
        if ( attributesData== null || attributesData.length == 0 )
        {
            return null;
        }
        Object[] line = attributesData [rowIndex];
        switch ( columnIndex )
        {
            case 0:
                return line[ 0 ];
            case 1:
                return line[ 1 ];
            default :
                return null;
        }
    }

    public String getColumnName ( int column )
    {
        String ret = "";
        switch ( column )
        {
            case 0:
                ret = Messages.getMessage("DIALOGS_VARIATION_ATTRIBUTE_NAME");
                break;
            case 1:
                ret = Messages.getMessage("DIALOGS_VARIATION_BALANCED");
                break;
        }
        return ret;
    }
}
