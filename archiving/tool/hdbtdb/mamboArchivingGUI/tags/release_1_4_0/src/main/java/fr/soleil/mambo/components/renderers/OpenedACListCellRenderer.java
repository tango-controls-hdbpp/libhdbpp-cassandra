//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/renderers/OpenedACListCellRenderer.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenedACListCellRenderer.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OpenedACListCellRenderer.java,v $
// Revision 1.2  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.1  2005/11/29 18:27:45  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components.renderers;

import java.awt.Component;
import java.awt.Image;
import java.sql.Timestamp;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationData;


public class OpenedACListCellRenderer implements ListCellRenderer
{

    private static final String SEPARATOR = " : ";

    /**
     * 
     */
    public OpenedACListCellRenderer ()
    {
        super();
    }

    /* (non-Javadoc)
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent ( JList list , Object value , int index , boolean isSelected , boolean cellHasFocus )
    {
        if ( value == null )
        {
            return new JLabel( "" );
        }

        ArchivingConfiguration vc = ( ArchivingConfiguration ) value;
        ArchivingConfigurationData vcData = vc.getData();

        index++;
        String text = " ";
        if ( index != 0 )
        {
            text += index;
            text += OpenedACListCellRenderer.SEPARATOR;
        }

        text += vcData.getName();

        Timestamp lastUpdateDate = vcData.getLastUpdateDate();
        if ( lastUpdateDate != null )
        {
            text += OpenedACListCellRenderer.SEPARATOR;
            text += lastUpdateDate.toString();
        }

        JLabel ret = new JLabel();
        ret.setText( text );
        if ( vc.isModified() )
        {
            ImageIcon newActionIcon = new ImageIcon( Mambo.class.getResource( "icons/star_red.gif" ) );
            Image newActionIconImage = newActionIcon.getImage();
            newActionIconImage = newActionIconImage.getScaledInstance( 10 , 10 , Image.SCALE_DEFAULT );
            newActionIcon.setImage( newActionIconImage );

            ret.setIcon( newActionIcon );
        }
        return ret;
    }

}
