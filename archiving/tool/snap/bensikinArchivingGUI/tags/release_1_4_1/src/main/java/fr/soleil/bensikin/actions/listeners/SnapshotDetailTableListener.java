/*	Synchrotron Soleil 
 *  
 *   File          :  SnapshotDetailTableListener.java
 *  
 *   Project       :  Bensikin_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  3 févr. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SnapshotDetailTableListener.java,v 
 *
 */
package fr.soleil.bensikin.actions.listeners;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableCellRenderer;

import fr.soleil.bensikin.components.snapshot.ImageButton;
import fr.soleil.bensikin.components.snapshot.ImageWriteButton;
import fr.soleil.bensikin.components.snapshot.SpectrumButton;
import fr.soleil.bensikin.components.snapshot.SpectrumDeltaValueButton;
import fr.soleil.bensikin.components.snapshot.SpectrumWriteValueButton;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailTable;


/**
 * 
 * @author SOLEIL
 */
public class SnapshotDetailTableListener extends MouseAdapter {
    /* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent event)
	{
		if ( !isEvent(event) )
		{
			return;
		}
		SnapshotDetailTable source = ( SnapshotDetailTable ) event.getComponent();
		int row = source.rowAtPoint(event.getPoint());
		int col = source.columnAtPoint(event.getPoint());

		TableCellRenderer renderer = source.getDefaultRenderer(Object.class);
		Component comp = renderer.getTableCellRendererComponent(source, source.getValueAt(row,col), true, true, row, col);
		/*System.out.println("test1");
		System.out.println(comp.getClass().toString());
		System.out.println(source.getValueAt(row,col).getClass().toString());*/
		if (comp instanceof SpectrumButton)
		{
		    //System.out.println("test2");
		    source.getDefaultEditor(Object.class).cancelCellEditing();
		    ((SpectrumButton)comp).actionPerformed();
		}
		else if (comp instanceof SpectrumWriteValueButton)
		{
		    source.getDefaultEditor(Object.class).cancelCellEditing();
		    ((SpectrumWriteValueButton)comp).actionPerformed();
		}
		else if (comp instanceof SpectrumDeltaValueButton)
		{
		    source.getDefaultEditor(Object.class).cancelCellEditing();
		    ((SpectrumDeltaValueButton)comp).actionPerformed();
		}
        else if (comp instanceof ImageButton)
        {
            //System.out.println("test2");
            source.getDefaultEditor(Object.class).cancelCellEditing();
            ((ImageButton)comp).actionPerformed();
        }
        else if (comp instanceof ImageWriteButton)
        {
            //System.out.println("test2");
            source.getDefaultEditor(Object.class).cancelCellEditing();
            ((ImageWriteButton)comp).actionPerformed();
        }
	}

	/**
	 * @param event
	 * @return 23 juin 2005
	 */
	private boolean isEvent(MouseEvent event)
	{
		if ( event.getClickCount() > 1 )
		{
			return false;
		}

		return true;
	}
}
