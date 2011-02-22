//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/renderers/ACAttributeDetailTableRenderer.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailTableRenderer.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ACAttributeDetailTableRenderer.java,v $
// Revision 1.5  2006/08/09 10:35:27  ounsy
// Time formating in assessments + differences between database and archiving configuration highlighted
//
// Revision 1.4  2006/04/10 08:59:42  ounsy
// removed useless logs
//
// Revision 1.3  2005/12/15 11:17:34  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.components.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbTitleTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailTdbTitleTable;
import fr.soleil.mambo.tools.GUIUtilities;



public class ACAttributeDetailTableRenderer implements TableCellRenderer
{
    protected Font font = new Font( "Arial" , Font.PLAIN , 11 );
    protected Font titleFont = new Font( "Arial" , Font.BOLD , 11 );
    protected boolean forceTitle = false;

    public ACAttributeDetailTableRenderer()
    {
        // just to have a constructor
    }

    public ACAttributeDetailTableRenderer(boolean _forceTitle)
    {
        forceTitle = _forceTitle;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent ( JTable table , Object value , boolean isSelected , boolean hasFocus , int row , int column )
    {
        if ( value instanceof Boolean )
        {
            boolean hasMode = ( ( Boolean ) value ).booleanValue();

            JCheckBoxMenuItem ret = new JCheckBoxMenuItem();
            ret.setBorderPainted( false );

            ret.setSelected( false );
            ret.setEnabled( false );
            ret.setFocusable( false );
            ret.setContentAreaFilled( false );
            Component c = table.getParent();
            if ( c != null )
            {
                ret.setBackground( c.getBackground() );
            }

            if ( hasMode )
            {
                /*ret.setText ( "O" );
                Font font = new Font( "SansSerif" , Font.BOLD , 14 );
                ret.setFont ( font );*/
                ret.setSelected( true );
            }
            else
            {
                ret.setSelected( false );
            }
            return ret;
        }
        else if ( value instanceof String )
        {
            JLabel label = new JLabel( ( String ) value );
            label.setOpaque(true);
            GUIUtilities.setObjectBackground(label, GUIUtilities.ARCHIVING_COLOR);
            label.setForeground(Color.BLACK);
            if (isSelected || hasFocus) {
                label.setBackground(Color.LIGHT_GRAY);
            }
            if ( forceTitle
                 || ( ( table instanceof ACAttributeDetailHdbTitleTable 
                        || table instanceof ACAttributeDetailTdbTitleTable )
                      && row == 0
                    )
               )
            {
                label.setFont(titleFont);
            }
            else
            {
                label.setFont(font);
                if (value != null && column == 3 && !value.equals(table.getModel().getValueAt(row,2)) )
                {
                    label.setForeground(new Color(150,0,0));
                }
                else
                {
                    label.setForeground(Color.BLACK);
                }
            }
            return label;
        }
        else if ( value instanceof JScrollPane )
        {
            return ( JScrollPane ) value;
        }
        else
        {
            return new DefaultTableCellRenderer().getTableCellRendererComponent( table , value , isSelected , hasFocus , row , column );
        }

    }

}
