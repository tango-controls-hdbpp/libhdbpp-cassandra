// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/renderers/OpenedVCListCellRenderer.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenedVCListCellRenderer.
// (Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: OpenedVCListCellRenderer.java,v $
// Revision 1.2 2006/07/28 10:07:12 ounsy
// icons moved to "icons" package
//
// Revision 1.1 2005/11/29 18:27:45 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.renderers;

import java.awt.Component;
import java.awt.Image;
import java.sql.Timestamp;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;

public class OpenedVCListCellRenderer implements ListCellRenderer {

    public static final String SEPARATOR = " : ";

    /**
     * 
     */
    public OpenedVCListCellRenderer() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            return new JLabel("");
        }

        // System.out.println
        // ("getListCellRendererComponent/"+value.getClass().toString ());

        ViewConfiguration vc = (ViewConfiguration) value;
        ViewConfigurationData vcData = vc.getData();

        index++;
        String text = " ";
        if (index != 0) {
            text += index;
            text += OpenedVCListCellRenderer.SEPARATOR;
        }

        text += vcData.getName();
        /*
         * text += OpenedVCListCellRenderer.SEPARATOR; text += vc.getPath ();
         */

        Timestamp lastUpdateDate = vcData.getLastUpdateDate();
        if (lastUpdateDate != null) {
            text += OpenedVCListCellRenderer.SEPARATOR;
            text += lastUpdateDate.toString();
        }

        JLabel ret = new JLabel();
        ret.setText(text);
        if (vc.isModified()) {
            ImageIcon newActionIcon = new ImageIcon(Mambo.class
                    .getResource("icons/star_red.gif"));
            Image newActionIconImage = newActionIcon.getImage();
            newActionIconImage = newActionIconImage.getScaledInstance(10, 10,
                    Image.SCALE_DEFAULT);
            newActionIcon.setImage(newActionIconImage);

            ret.setIcon(newActionIcon);
        }
        return ret;
    }

}
