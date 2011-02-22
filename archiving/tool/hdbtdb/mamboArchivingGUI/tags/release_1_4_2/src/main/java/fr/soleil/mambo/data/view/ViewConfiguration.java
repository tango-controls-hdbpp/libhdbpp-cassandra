//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfiguration.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfiguration.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.14 $
//
// $Log: ViewConfiguration.java,v $
// Revision 1.14  2008/04/09 10:45:46  achouri
// no message
//
// Revision 1.13  2007/02/01 14:21:46  pierrejoseph
// XmlHelper reorg
//
// Revision 1.12  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.11  2006/12/06 15:12:22  ounsy
// added parametrisable sampling
//
// Revision 1.10  2006/10/04 09:59:40  ounsy
// modified the cloneVC() method to something  cleaner
//
// Revision 1.9  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.8  2006/09/18 08:48:30  ounsy
// added start and end dates in charts
//
// Revision 1.7  2006/09/05 14:15:52  ounsy
// updated for sampling compatibility
//
// Revision 1.6  2006/09/05 14:02:23  ounsy
// updated for sampling compatibility
//
// Revision 1.5  2006/08/29 14:19:27  ounsy
// now the view dialog is filled through a thread. Closing the dialog will kill this thread.
//
// Revision 1.4  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.3  2006/01/13 13:37:25  ounsy
// File replacement warning
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.view;

import java.io.File;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.esrf.Tango.DevFailed;
import fr.esrf.tangoatk.widget.util.chart.JLChart;
import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.components.view.VCFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewAttributesPanel;
import fr.soleil.mambo.containers.view.ViewGeneralPanel;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.models.ExpressionTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;


public class ViewConfiguration
{
//  MULTI-CONF
    private ViewConfigurationData data;
    private ViewConfigurationAttributes attributes;
    private TreeMap expressions;

    private static ViewConfiguration currentViewConfiguration;
    private static ViewConfiguration selectedViewConfiguration;

    private boolean isModified = true;

    public static final String XML_TAG = "viewConfiguration";

    public static ViewConfiguration getCurrentViewConfiguration ()
    {
        return currentViewConfiguration;
    }

    public String toString ()
    {
        String ret = "";

        XMLLine openingLine = new XMLLine( ViewConfiguration.XML_TAG , XMLLine.OPENING_TAG_CATEGORY );

        Timestamp creationDate = data.getCreationDate();
        Timestamp lastUpdateDate = data.getLastUpdateDate();
        Timestamp startDate = data.getStartDate();
        Timestamp endDate = data.getEndDate();
        boolean isHistoric = data.isHistoric();
        boolean dynamicDateRange = data.isDynamicDateRange();
        String dateRange = data.getDateRange();
        String name = data.getName();
        String path = data.getPath();
        SamplingType samplingType = data.getSamplingType(); 

        openingLine.setAttribute( ViewConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG , isModified + "" );
        if ( creationDate != null )
        {
            openingLine.setAttribute( ViewConfigurationData.CREATION_DATE_PROPERTY_XML_TAG , creationDate.toString() );
        }
        if ( lastUpdateDate != null )
        {
            openingLine.setAttribute( ViewConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG , lastUpdateDate.toString() );
        }
        if ( startDate != null )
        {
            openingLine.setAttribute( ViewConfigurationData.START_DATE_PROPERTY_XML_TAG , startDate.toString() );
        }
        if ( endDate != null )
        {
            openingLine.setAttribute( ViewConfigurationData.END_DATE_PROPERTY_XML_TAG , endDate.toString() );
        }
        openingLine.setAttribute( ViewConfigurationData.HISTORIC_PROPERTY_XML_TAG , String.valueOf( isHistoric ) );
        openingLine.setAttribute( ViewConfigurationData.DYNAMIC_DATE_RANGE_PROPERTY_XML_TAG , String.valueOf( dynamicDateRange ) );
        openingLine.setAttribute( ViewConfigurationData.DATE_RANGE_PROPERTY_XML_TAG , dateRange );

        if ( name != null )
        {
            openingLine.setAttribute( ViewConfigurationData.NAME_PROPERTY_XML_TAG , name );
        }
        if ( path != null )
        {
            openingLine.setAttribute( ViewConfigurationData.PATH_PROPERTY_XML_TAG , path );
        }
        //System.out.println ( "---------------------------------------/samplingType/"+samplingType+"/" );
        if ( samplingType != null )
        {
            openingLine.setAttribute( ViewConfigurationData.SAMPLING_TYPE_PROPERTY_XML_TAG , samplingType.toString () );
            openingLine.setAttribute( ViewConfigurationData.SAMPLING_FACTOR_PROPERTY_XML_TAG , "" + samplingType.getAdditionalFilteringFactor () );
        }

        XMLLine closingLine = new XMLLine( ViewConfiguration.XML_TAG , XMLLine.CLOSING_TAG_CATEGORY );

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if ( this.data != null )
        {
            ret += this.data.toString();
            ret += GUIUtilities.CRLF;
        }

        //parameters specific to groups of attributes
        if ( this.attributes != null )
        {
            ret += this.attributes.toString();
        }

        ret += closingLine.toString();

        //ret = XMLUtils.replaceXMLChars ( ret );

        return ret;
    }

    public ViewConfiguration ()
    {
        this.data = new ViewConfigurationData();
        this.attributes = new ViewConfigurationAttributes();
        this.expressions = new TreeMap(Collator.getInstance(Locale.FRENCH));
    }

    /**
     * @return Returns the attributes.
     */
    public ViewConfigurationAttributes getAttributes ()
    {
        return attributes;
    }

    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes ( ViewConfigurationAttributes attributes )
    {
        this.attributes = attributes;
    }

    /**
     * @return Returns the data.
     */
    public ViewConfigurationData getData ()
    {
        return data;
    }

    /**
     * @param data The data to set.
     */
    public void setData ( ViewConfigurationData data )
    {
        this.data = data;
    }

    /**
     * @param newVC 23 août 2005
     */
    public static void setCurrentViewConfiguration ( ViewConfiguration _currentViewConfiguration )
    {
        currentViewConfiguration = _currentViewConfiguration;

    }

    public boolean isNew ()
    {
        return this.data.getCreationDate() == null;
    }

    public void push ()
    {
        ViewConfiguration.setCurrentViewConfiguration( this.cloneVC () );
        ViewConfiguration.setSelectedViewConfiguration( this );

        if ( this.data != null )
        {
            this.data.push();
        }


        if ( this.attributes != null )
        {
            this.attributes.push();
        }

        ExpressionTreeModel.getInstance().setExpressionAttributes(ViewConfiguration.getCurrentViewConfiguration().getExpressions());

        OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        if ( openedVCComboBox != null )
        {
            openedVCComboBox.selectElement( this );
        }
        
        
    }

    /**
     * @return
     */
    private ViewConfiguration cloneVC () 
    {
        ViewConfiguration ret = new ViewConfiguration ();
        
        ret.setModified ( this.isModified () );
        ret.setPath ( this.getPath () == null ? null : new String ( this.getPath () ) );
        ret.setData ( this.getData ().cloneData () );
        ret.setAttributes ( this.getAttributes().cloneAttrs () );
        ret.setExpressions(this.getExpressionsDuplicata());
        
        return ret;
    }

    public void pushLight ()
    {
        ViewConfiguration.setCurrentViewConfiguration( this.cloneVC() );
        if ( this.data != null )
        {
            this.data.push();
        }

        if ( this.attributes != null )
        {
            this.attributes.push();
        }
        ExpressionTreeModel.getInstance().setExpressionAttributes(ViewConfiguration.getCurrentViewConfiguration().getExpressions());
    }


    /**
     * @return Returns the selectedViewConfiguration.
     */
    public static ViewConfiguration getSelectedViewConfiguration ()
    {
        return selectedViewConfiguration;
    }

    /**
     * @param selectedViewConfiguration The selectedViewConfiguration to set.
     */
    public static void setSelectedViewConfiguration ( ViewConfiguration selectedViewConfiguration )
    {
        //System.out.println ("ViewConfiguration/setSelectedViewConfiguration/0");
        ViewConfiguration.selectedViewConfiguration = selectedViewConfiguration;

        OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        openedVCComboBox.push( selectedViewConfiguration );
    }

    /**
     * @param name
     * @return 25 août 2005
     */
    public boolean containsAttribute ( String completeName )
    {
        TreeMap hash = this.attributes.getAttributes();
        return hash.containsKey( completeName );
    }

    public ViewConfigurationAttribute getAttribute ( String completeName )
    {
        TreeMap hash = this.attributes.getAttributes();

        return ( ViewConfigurationAttribute ) hash.get( completeName );
    }

    /**
     * @return 26 août 2005
     */
    public JLChart getChart ()
    {
        StaticChartMathExpression chart = data.getChart();
        //JLChart chart = new JLChart ();

        try
        {
            boolean historic = data.isHistoric();
            if ( data.isDynamicDateRange() )
            {
                Timestamp[] range = GeneralTab.getInstance().getDynamicStartAndEndDates();
                chart = attributes.setChartAttributes( chart , range[ 0 ].toString() , range[ 1 ].toString() , historic , data.getSamplingType () );
            }
            else
            {
                chart = attributes.setChartAttributes( chart , data.getStartDate().toString() , data.getEndDate().toString() , historic , data.getSamplingType () );
            }
            //chart.setLabelVisible( true );
            chart.setPaintAxisFirst( true );
            chart.setVisible( true );
        }
        catch ( DevFailed e )
        {
            e.printStackTrace();
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }

        return chart;
    }

    public StaticChartMathExpression getChartLight()
    {
        StaticChartMathExpression chart = data.getChart();
        chart.setPaintAxisFirst( true );
        chart.setVisible( true );
        return chart;
    }

    public void loadAttributes(StaticChartMathExpression chart)
    {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        if (extractingManager.isCanceled()) return;
        try
        {
            boolean historic = data.isHistoric();
            if ( data.isDynamicDateRange() )
            {
                Timestamp[] range = GeneralTab.getInstance().getDynamicStartAndEndDates();
                chart = attributes.setChartAttributes( chart , range[ 0 ].toString() , range[ 1 ].toString() , historic , data.getSamplingType () );
            }
            else
            {
                chart = attributes.setChartAttributes( chart , data.getStartDate().toString() , data.getEndDate().toString() , historic , data.getSamplingType () );
            }

        }
        catch ( DevFailed e )
        {
            e.printStackTrace();
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * @return Returns the isModified.
     */
    public boolean isModified ()
    {
        return isModified;
    }

    /**
     * @param isModified The isModified to set.
     */
    public void setModified ( boolean isModified )
    {
        this.isModified = isModified;
        OpenedVCComboBox.refresh();
    }

    /**
     * @param saveLocation
     */
    public void setPath ( String path )
    {
        if ( this.data != null )
        {
            this.data.setPath( path );
        }
    }

    public String getPath ()
    {
        if ( this.data != null )
        {
            return this.data.getPath();
        }
        else
        {
            return null;
        }
    }

    public boolean equals ( ViewConfiguration this2 )
    {
        boolean ret;
        boolean dataEquals;
        boolean attrEquals;

        String path1 = this.data.getPath() == null ? "" : this.data.getPath();
        String path2 = this2.data.getPath() == null ? "" : this2.data.getPath();
        if ( path1.equals( path2 ) )
        {
            return true;
        }

        ViewConfigurationData data1 = this.getData();
        ViewConfigurationData data2 = this2.getData();
        if ( data1 == null )
        {
            if ( data2 == null )
            {
                dataEquals = true;
            }
            else
            {
                dataEquals = false;
            }
        }
        else
        {
            if ( data2 == null )
            {
                dataEquals = false;
            }
            else
            {
                dataEquals = data1.equals( data2 );
            }
        }

        ViewConfigurationAttributes attrs1 = this.getAttributes();
        ViewConfigurationAttributes attrs2 = this2.getAttributes();
        if ( attrs1 == null )
        {
            if ( attrs2 == null )
            {
                attrEquals = true;
            }
            else
            {
                attrEquals = false;
            }
        }
        else
        {
            if ( attrs2 == null )
            {
                attrEquals = false;
            }
            else
            {
                attrEquals = attrs1.equals( attrs2 );
            }
        }

        ret = dataEquals && attrEquals;
        return ret;
    }

    public void save ( IViewConfigurationManager manager , boolean saveAs )
    {
        String pathToUse = getPathToUse( manager , saveAs );
        if ( pathToUse == null )
        {
            return;
        }

        manager.setNonDefaultSaveLocation( pathToUse );
        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            this.setPath( pathToUse );
            this.setModified( false );
            manager.saveViewConfiguration( this , null );

            String msg = Messages.getLogMessage( "SAVE_VIEW_CONFIGURATION_ACTION_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "SAVE_VIEW_CONFIGURATION_ACTION_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );

            this.setPath( null );
            this.setModified( true );

            return;
        }
        //-----WARNING!!!!!
        //this.push ();
        ViewGeneralPanel panel = ViewGeneralPanel.getInstance();
        panel.setPath( this.getPath() );
        //-----WARNING!!!!!
    }

    /**
     * @return
     */
    private String getPathToUse ( IViewConfigurationManager manager , boolean saveAs )
    {
        String pathToUse = this.getPath();
        JFileChooser chooser = new JFileChooser();
        VCFileFilter VCfilter = new VCFileFilter();
        chooser.addChoosableFileFilter( VCfilter );

        if ( pathToUse != null )
        {
            if ( saveAs )
            {
                chooser.setCurrentDirectory( new File( pathToUse ) );
            }
            else
            {
                return pathToUse;
            }
        }
        else
        {
            chooser.setCurrentDirectory( new File( manager.getDefaultSaveLocation() ) );
        }

        //----show the user what he's saving
        OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        openedVCComboBox.selectElement( this );
        //----

        int returnVal = chooser.showSaveDialog( MamboFrame.getInstance() );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            File f = chooser.getSelectedFile();
            String path = f.getAbsolutePath();

            if ( f != null )
            {
                String extension = ConfigurationFileFilter.getExtension( f );
                String expectedExtension = VCfilter.getExtension();

                if ( extension == null || !extension.equalsIgnoreCase( expectedExtension ) )
                {
                    path += ".";
                    path += expectedExtension;
                }
            }
            if (f.exists()) {
                int choice = JOptionPane.showConfirmDialog(MamboFrame.getInstance(),
                                                           Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"),
                                                           Messages.getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"),
                                                           JOptionPane.YES_NO_OPTION,
                                                           JOptionPane.WARNING_MESSAGE);
                if (choice != JOptionPane.OK_OPTION) {
                    return null;
                }
            }
            manager.setNonDefaultSaveLocation( path );
            return path;
        }
        else
        {
            return null;
        }

    }

    public void refreshContent() 
    {
        this.data.refreshContent (); 
    }

    public TreeMap getExpressions ()
    {
        return expressions;
    }

    public TreeMap getExpressionsDuplicata ()
    {
        if (expressions == null) return null;
        TreeMap duplicata = new TreeMap();
        Set keySet = expressions.keySet();
        Iterator keyIterator = keySet.iterator();
        while(keyIterator.hasNext())
        {
            String key = (String)keyIterator.next();
            duplicata.put(new String(key), ((ExpressionAttribute)expressions.get(key)).duplicate() );
        }
        return duplicata;
    }

    public void setExpressions (TreeMap expressions)
    {
        this.expressions = expressions;
    }

    public void clearExpressions()
    {
        this.expressions.clear();
    }
    
    
}