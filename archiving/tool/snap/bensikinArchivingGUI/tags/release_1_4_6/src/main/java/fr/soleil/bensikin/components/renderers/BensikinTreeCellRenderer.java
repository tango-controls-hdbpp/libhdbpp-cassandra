//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/renderers/BensikinTreeCellRenderer.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BensikinTreeCellRenderer.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: BensikinTreeCellRenderer.java,v $
// Revision 1.1  2005/11/29 18:25:08  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.Bensikin;


public class BensikinTreeCellRenderer extends DefaultTreeCellRenderer
{
	private ImageIcon attributeIcon = new ImageIcon(Bensikin.class.getResource("icons/attribute.gif"));
	private ImageIcon hostIcon = new ImageIcon(Bensikin.class.getResource("icons/host.gif"));
	private ImageIcon deviceIcon = new ImageIcon(Bensikin.class.getResource("icons/device.gif"));
	private ImageIcon bigDeviceIcon = new ImageIcon(Bensikin.class.getResource("icons/big_device.gif"));
	private ImageIcon folderIcon = new ImageIcon(Bensikin.class.getResource("icons/folder.gif"));
	private ImageIcon folderOpenedIcon = new ImageIcon(Bensikin.class.getResource("icons/folder_opened.gif"));

	public BensikinTreeCellRenderer()
	{
		super();
	}

	public Component getTreeCellRendererComponent(JTree tree ,
	                                              Object value ,
	                                              boolean sel ,
	                                              boolean expanded ,
	                                              boolean leaf ,
	                                              int row ,
	                                              boolean hasFocus)
	{
		TreePath path = tree.getPathForRow(row);
		if ( path != null )
		{
			Object[] thePath = path.getPath();
			if ( thePath != null )
			{
				JLabel label = new JLabel(value.toString().trim());
				label.setOpaque(true);
				Font font = new Font("Arial" , Font.PLAIN , 11);
				label.setFont(font);
				label.setBackground(Color.WHITE);
				label.setForeground(Color.BLACK);
				if ( sel )
				{
					label.setBackground(new Color(0 , 0 , 120));
					label.setForeground(Color.WHITE);
				}
				label.setIgnoreRepaint(false);
				label.setIconTextGap(2);
				switch ( thePath.length )
				{
					case 1:
						label.setIcon(hostIcon);
						break;
					case 4:
						if ( sel || expanded )
						{
							label.setIcon(bigDeviceIcon);
						}
						else
						{
							label.setIcon(deviceIcon);
						}
						break;
					case 5:
						label.setIcon(attributeIcon);
						break;
					default :
						if ( sel || expanded )
						{
							label.setIcon(folderOpenedIcon);
						}
						else
						{
							label.setIcon(folderIcon);
						}
						break;
				}
				label.repaint();
				return label;
			}
		}
		return super.getTreeCellRendererComponent(tree , value , sel ,
		                                          expanded , leaf , row ,
		                                          hasFocus);
	}

}
