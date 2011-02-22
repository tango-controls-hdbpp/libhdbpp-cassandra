//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/models/VCVariationResultTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationResultTableModel.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationResultTableModel.java,v $
// Revision 1.5  2006/11/02 15:03:06  ounsy
// avoiding exception
//
// Revision 1.4  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.3  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.2  2005/12/15 11:44:30  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:08  chinkumo
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
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.table.DefaultTableModel;

import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.Messages;

public class VCVariationResultTableModel extends DefaultTableModel
{
    private static VCVariationResultTableModel instance;
    private boolean historic;
    private TreeMap attributes;
    private String[] attributesNames;

    public static VCVariationResultTableModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new VCVariationResultTableModel();
        }
        return instance;
    }

    private VCVariationResultTableModel ()
    {
        super();
        historic = true;
        attributes = new TreeMap(Collator.getInstance(Locale.FRENCH));
        attributesNames = new String[ 0 ];
    }

    public void update ()
    {
        IAttributeManager attributeManager = AttributeManagerFactory.getCurrentImpl ();
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        int rows = getRowCount();
        ViewConfiguration vc = ViewConfiguration.getSelectedViewConfiguration();
        ViewConfigurationAttributes vca = vc.getAttributes();
        attributes.clear();
        if ( vca != null )
        {
            TreeMap attrTable = vca.getAttributes();
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
                            
                            Double[] minmax = extractingManager.getMinAndMax( param , historic );
                            if ( !minmax[ 0 ].equals( new Double( Double.NaN ) ) && !minmax[ 1 ].equals( new Double( Double.NaN ) ) )
                            {
                                attributes.put( name , minmax );
                            }
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
        attributesNames = new String[ attributes.size() ];
        Set keys = attributes.keySet();
        Iterator it = keys.iterator();
        int i = 0;
        while ( it.hasNext() )
        {
            String key = ( String ) it.next();
            attributesNames[ i ] = key;
            i++;
        }
        if ( rows > 0 )
        {
            rows--;
            fireTableRowsDeleted( 0 , rows );
        }
        fireTableStructureChanged();
        rows = getRowCount();
        if ( rows > 0 )
        {
            rows--;
            fireTableRowsInserted( 0 , rows );
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount ()
    {
        if ( attributesNames != null )
        {
            return attributesNames.length;
        }
        else
        {
            return 0;
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
        if (attributesNames == null || rowIndex >= attributesNames.length) return null;
        String name = attributesNames[ rowIndex ];
        Double[] minmax = ( Double[] ) attributes.get( name );
        switch ( columnIndex )
        {
            case 0:
                return name;
            case 1:
                if ( minmax[ 0 ].doubleValue() == Double.NaN || minmax[ 1 ].doubleValue() == Double.NaN )
                {
                    return new Double( Double.NaN );
                }
                else
                {
                    return new Double( minmax[ 1 ].doubleValue() - minmax[ 0 ].doubleValue() );
                }
            case 2:
                return minmax[ 0 ];
            case 3:
                return minmax[ 1 ];
            default:
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
                ret = Messages.getMessage("DIALOGS_VARIATION_NORMAL");
                break;
            case 2:
                ret = Messages.getMessage("DIALOGS_VARIATION_MIN");
                break;
            case 3:
                ret = Messages.getMessage("DIALOGS_VARIATION_MAX");
                break;
        }
        return ret;
    }
}
