/*	Synchrotron Soleil 
 *  
 *   File          :  SamplingTypeComboBox.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  29 août 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SamplingTypeComboBox.java,v 
 *
 */
 /*
 * Created on 29 août 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.mambo.components.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;

public class SamplingTypeComboBox extends JComboBox 
{
    public SamplingTypeComboBox() 
    {
        super();
        this.setRenderer ( new SamplingTypeComboBoxRenderer () );
        this.initChoices ();
        
        Dimension maxDimension = new Dimension( 300 , 25 );
        this.setMaximumSize( maxDimension );
        this.setMinimumSize( maxDimension );
        this.setPreferredSize( maxDimension );
        this.setSize( maxDimension );
    }
    
    private void initChoices() 
    {
        SamplingType [] choices = SamplingType.getChoices ();
        for ( int i = 0 ; i < choices.length ; i ++ )
        {
            SamplingType nextChoice = choices [ i ];  
            //System.out.println ( "SamplingTypeComboBox/initChoices/2/nextChoice.getType()/"+nextChoice.getType()+"/nextChoice.getFormat()/"+nextChoice.getFormat()+"/nextChoice.getLabel()/"+nextChoice.getLabel()+"/" );
            super.addItem ( nextChoice );
        }
    }
    
    public void setSelectedSamplingType ( Object anObject )
    {
        SamplingType convert = (SamplingType) anObject;
        
        for ( int i = 0 ; i < this.getItemCount () ; i ++ )
        {
            SamplingType aSamplingType = (SamplingType) this.getItemAt ( i );
            
            if ( aSamplingType.isSameTypeAs ( convert ) )
            {
                this.setSelectedIndex ( i );
                break;
            }
        }
    }
    
    public class SamplingTypeComboBoxRenderer implements ListCellRenderer 
    {

        public SamplingTypeComboBoxRenderer() 
        {
            super();
        }

        public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
            DefaultListCellRenderer defaultListCellRendererComponent = new DefaultListCellRenderer ();
            if ( value == null || ! ( value instanceof SamplingType ) )
            {
                return defaultListCellRendererComponent.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus);
            }
            
            SamplingType samplingType = (SamplingType) value;
            JLabel ret = new JLabel ( samplingType.getLabel () );
            
            return ret;
        }
    }
}
