package fr.soleil.mambo.components.archiving;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;


public class ArchiversComboBox extends JComboBox
{
    public static final String NO_SELECTION = "---";
    public static final String DEFAULT = "DEFAULT";
    
    private Hashtable elements;
    private boolean isHistoric;
    private String currentlySelectedAttribute; 

    /**
     * @param type
     * @throws IllegalStateException
     */
    public ArchiversComboBox ( boolean _isHistoric ) throws IllegalStateException
    {
        super();   
        this.isHistoric = _isHistoric;
        this.reset();

        this.setArchivers();
                
        Dimension maxDimension = new Dimension( Integer.MAX_VALUE , 20 );
        this.setMaximumSize( maxDimension );
        
        this.addActionListener ( new ArchiversComboBoxActionListener () );
        this.setRenderer ( new ArchiversComboBoxRenderer () );
    }

    /**
     * 
     */
    public void setArchivers ()
    {
        this.reset();
        
        ITangoManager manager = TangoManagerFactory.getCurrentImpl();
        
        Archiver [] archivers = null;
        try
        {
            archivers = manager.findArchivers ( this.isHistoric );
        }
        catch ( ArchivingException e )
        {
            e.printStackTrace();
        }
        if ( archivers == null )
        {
            return;
        }

        for ( int i = 0 ; i < archivers.length ; i++ )
        {
            archivers[ i ].load ();
            //System.out.println ( "ArchiversComboBox/setArchivers/i|"+i+"|getName|"+archivers[ i ].getName()+"|isDedicated|"+archivers[ i ].isDedicated() );
            //archivers[ i ].trace ();
            if ( ! archivers[ i ].isDedicated () )
            {
                super.addItem( archivers[ i ].getName() );
                this.elements.put( archivers[ i ].getName() , archivers[ i ] );
            }          
        }
    }

    
    /**
     * 
     */
    public void reset ()
    {
        super.removeAllItems();
        super.addItem( NO_SELECTION );

        this.elements = new Hashtable();
    }

    public Archiver getSelectedArchiver ()
    {
        String currentAttributeName = ( String ) super.getSelectedItem();
        if ( currentAttributeName == null || currentAttributeName.equals( ArchiversComboBox.NO_SELECTION ) )
        {
            return null;
        }

        Archiver ret = ( Archiver ) this.elements.get( currentAttributeName );
        return ret;
    }


    public void selectDefault () 
    {
        super.setSelectedItem ( NO_SELECTION );
    }
    
    public void selectArchiver ( String selected ) 
    {
        selected = selected == null ? "" : selected;
        if ( this.elements.containsKey ( selected ) )
        {
            super.setSelectedItem ( selected );    
        }
        else
        {
            this.selectDefault ();
        }
    }
    
    private class ArchiversComboBoxActionListener implements ActionListener
    {
        public ArchiversComboBoxActionListener ()
        {
            
        }

        public void actionPerformed(ActionEvent arg0) 
        {
            Archiver selectedArchiver = ArchiversComboBox.this.getSelectedArchiver ();
            String selectedArchiverText = selectedArchiver == null ? DEFAULT : selectedArchiver.getName (); 
            
            AttributesPropertiesPanel attributesPropertiesPanel = AttributesPropertiesPanel.getInstance ();
            if ( attributesPropertiesPanel != null )
            {
                attributesPropertiesPanel.setArchiverChoiceText ( selectedArchiverText );
            }
        }
    }

    public void setCurrentlySelectedAttribute(String attributeCompleteName) 
    {
        this.currentlySelectedAttribute = attributeCompleteName;
    }
    
    /*public Archiver getRunningArchiverForName 
    Archiver archiver = ( Archiver ) ArchiversComboBox.this.elements.get( archiverName );*/
    
    private class ArchiversComboBoxRenderer extends BasicComboBoxRenderer 
    {
        public ArchiversComboBoxRenderer ()
        {
            super ();
        }
        
        public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus)
        {
            Component ret = super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            
            String archiverName = ( String ) value;
            Archiver archiver = ( Archiver ) ArchiversComboBox.this.elements.get( archiverName );
            
            if ( archiver == null )
            {
                return ret;
            }
            
            Color color = archiver.getAssociationColor ( ArchiversComboBox.this.currentlySelectedAttribute );    
            ret.setForeground ( color );
            
            return ret;
        }
    }

    /**
     * @return Returns the currentlySelectedAttribute.
     */
    public String getCurrentlySelectedAttribute() {
        return this.currentlySelectedAttribute;
    }
}
