//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewAttributesTreePanel.
//						(Claisse Laurent) - 13 mars 2008
//
// $Author$
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewImagePanel;
import fr.soleil.mambo.containers.view.ViewNumberScalarPanel;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanScalarPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanSpectrumPanel;
import fr.soleil.mambo.containers.view.dialogs.VCViewDialog.DataLoader;
import fr.soleil.mambo.containers.view.dialogs.VCViewDialog.DialogKiller;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewAttributesGraphePanel extends JPanel
{
    private static ViewAttributesGraphePanel instance = null;

    private final static Dimension dim  = new Dimension( 400 , 400 );
    private final static Dimension dim2 = new Dimension( 500 , 500 );

    private  JTabbedPane attributesTab;

    private ViewConfigurationAttribute[] strings;
    private StaticChartMathExpression chart;

    private MamboCleanablePanel[] spectrumPanels;
    private MamboCleanablePanel[] imagePanels;
    private MamboCleanablePanel numberScalarsPanel;
    private MamboCleanablePanel otherScalarsPanel;

	private ViewConfiguration vc;
    protected DataLoader loader;
    
    public static ViewAttributesGraphePanel getInstance ()
    {
        if ( instance == null)
        {
        	instance = new ViewAttributesGraphePanel();
        }

        return instance;
    }

    public ViewAttributesGraphePanel()
    {
    	super();
    	initBorder();
    	initBounds();
    	vc = ViewConfiguration.getSelectedViewConfiguration();
        if(vc != null){
//        	vc = new ViewConfiguration();
        	initComponents();
            addComponents();
            setLayout();
            loader();
        }
        
    }

    public void loader(){
    	
    	loader = new DataLoader();
        loader.start();
        
    }
    
    public static void clearInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
            System.gc();
        }
    }

    public void setLayout ()
    {
        this.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                this,
                1, 1,
                0, 0,
                0, 0,
                true
        );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        attributesTab.add(
                Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_TITLE"),
                numberScalarsPanel
        );
        attributesTab.setToolTipTextAt(0,Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_TOOLTIP"));

        if (strings != null && strings.length > 0)
        {
            JScrollPane stringScrollPane = new JScrollPane( otherScalarsPanel );
            GUIUtilities.setObjectBackground(stringScrollPane, GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(stringScrollPane.getViewport(), GUIUtilities.VIEW_COLOR);
            attributesTab.add(
                    Messages.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_TITLE"),
                    stringScrollPane
            );
        }

        for (int i = 0; i < spectrumPanels.length; i++)
        {
            spectrumPanels[i].setSize( dim );
            attributesTab.add(
                    spectrumPanels[i].getName(),
                    spectrumPanels[i]
            );
            attributesTab.setToolTipTextAt(attributesTab.getComponentCount()-1,spectrumPanels[i].getFullName());
        }
        
        for (int i = 0; i < imagePanels.length; i++)
        {
            imagePanels[i].setSize( dim );
            attributesTab.add(
                    imagePanels[i].getName(),
                    imagePanels[i]
            );
            attributesTab.setToolTipTextAt(attributesTab.getComponentCount()-1,imagePanels[i].getFullName());
        }

        attributesTab.setPreferredSize(dim2);
        attributesTab.setSize(dim2);
        this.add( attributesTab );
 
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
    	
        GUIUtilities.setObjectBackground(this,GUIUtilities.VIEW_COLOR);
        attributesTab = new JTabbedPane();
        GUIUtilities.setObjectBackground(attributesTab,GUIUtilities.VIEW_COLOR);
//        if (vc == null) return;	
        TreeMap stringTable = vc.getAttributes().getStringStateScalarAttributes(vc.getData().isHistoric());
        strings = new ViewConfigurationAttribute[stringTable.size()];
        Set stringSet = stringTable.keySet();
        Iterator stringIt = stringSet.iterator();
        int i = 0;
        while (stringIt.hasNext()) {
            strings[i++] = ( (ViewConfigurationAttribute)stringTable.get(stringIt.next()) );
        }
        if (i > 0)
        {
            otherScalarsPanel = new ViewStringStateBooleanScalarPanel(strings);
        }
        else
        {
        	otherScalarsPanel = null;
        }

        TreeMap imageTable = vc.getAttributes ().getImageAttributes ( vc.getData ().isHistoric () );
        //System.out.println("imageTable size : " + imageTable.size());
        imagePanels = new MamboCleanablePanel [ imageTable.size () ];
        Set imageSet = imageTable.keySet();
        Iterator imageIt = imageSet.iterator();
        i=0;
        while (imageIt.hasNext())
        {
            ViewConfigurationAttribute attribute = (ViewConfigurationAttribute) imageTable.get ( imageIt.next () );
            imagePanels [ i++ ] = new ViewImagePanel ( attribute );
        }

        TreeMap spectrumTable = vc.getAttributes().getSpectrumAttributes(vc.getData().isHistoric());
        //System.out.println("spectrumTable size : " + spectrumTable.size());
        setSpectrumPanels(new MamboCleanablePanel[spectrumTable.size()]);
        Set spectrumSet = spectrumTable.keySet();
        Iterator spectrIt = spectrumSet.iterator();
        i=0;
        while (spectrIt.hasNext())
        {
            ViewConfigurationAttribute attribute = (ViewConfigurationAttribute)spectrumTable.get(spectrIt.next());
            int data_type = attribute.getDataType(vc.getData().isHistoric());
            if ( data_type == TangoConst.Tango_DEV_STRING
                 || data_type == TangoConst.Tango_DEV_STATE
                 || data_type == TangoConst.Tango_DEV_BOOLEAN
                )
            {
                spectrumPanels[i++] = new ViewStringStateBooleanSpectrumPanel ( attribute );
            }
            else
            {
                spectrumPanels[i++] = new ViewSpectrumPanel ( attribute );
            }
        }
        setChart(vc.getChartLight());
//        chart = new StaticChartMathExpression();
        chart.setMinimumSize( dim );
        chart.setPreferredSize( dim );
        numberScalarsPanel = new ViewNumberScalarPanel(chart);
        //chart.setTimePrecision(40);
        GUIUtilities.setObjectBackground(chart,GUIUtilities.VIEW_COLOR);
        //chart.setLabelVisible(false);-->curves legend
        //chart.setHeaderVisible (false);-->general title

    }

    private void initBounds ()
    {
        this.setBounds( new Rectangle(0, 0, 2000, 2000) );
    }
    

    private void initBorder ()
    {
//        String msg = Messages.getMessage( "View" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  "" ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );
    }
    

    
    private void prepareLegends()
    {
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration();
        selectedVC.refreshContent ();
    }

    public void clean()
    {
    	attributesTab.removeAll();
        if (spectrumPanels != null) {
            for (int i =0; i < spectrumPanels.length; i++) {
                if (spectrumPanels[i] != null) {
                	spectrumPanels[i].clean();
                }
            }
        }
        if (imagePanels != null) {
            for (int i =0; i < imagePanels.length; i++) {
                if (imagePanels[i] != null) {
                    imagePanels[i].clean();
                }
            }
        }
        if (numberScalarsPanel != null)
        {
        	numberScalarsPanel.clean();
        }
        if (otherScalarsPanel != null)
        {
        	otherScalarsPanel.clean();
        }
        this.removeAll();
    }
    
    public void loadPanels()
    {
        if (numberScalarsPanel != null)
        {
            numberScalarsPanel.loadPanel();
        }
        if (otherScalarsPanel != null)
        {
            otherScalarsPanel.loadPanel();
        }
        if (spectrumPanels != null)
        {
            for (int i = 0; i < spectrumPanels.length; i++)
            {
                spectrumPanels[i].loadPanel();
            }
        }
        if (imagePanels != null)
        {
            for (int i = 0; i < imagePanels.length; i++)
            {
                imagePanels[i].loadPanel();
            }
        }
    }

    
    
	public void setAttributesTab(JTabbedPane attributesTab) {
		this.attributesTab = attributesTab;
	}

	public JTabbedPane getAttributesTab() {
		return attributesTab;
	}

	public void setVc(ViewConfiguration vc) {
		this.vc = vc;
	}

	public ViewConfiguration getVc() {
		return vc;
	}

	public void setNumberScalarsPanel(MamboCleanablePanel numberScalarsPanel) {
		this.numberScalarsPanel = numberScalarsPanel;
	}

	public MamboCleanablePanel getNumberScalarsPanel() {
		return numberScalarsPanel;
	}

	public void setSpectrumPanels(MamboCleanablePanel[] spectrumPanels) {
		this.spectrumPanels = spectrumPanels;
	}

	public MamboCleanablePanel[] getSpectrumPanels() {
		return spectrumPanels;
	}

	public void setOtherScalarsPanel(MamboCleanablePanel otherScalarsPanel) {
		this.otherScalarsPanel = otherScalarsPanel;
	}

	public MamboCleanablePanel getOtherScalarsPanel() {
		return otherScalarsPanel;
	}

	public void setChart(StaticChartMathExpression chart) {
		this.chart = chart;
	}

	public StaticChartMathExpression getChart() {
		return chart;
	}

	protected class DataLoader extends Thread
	{
	    DataLoader()
	    {
	        super();
	        this.setPriority(Thread.MIN_PRIORITY);
	    }
	    public void run() {
	        loadPanels();
	        return;
	    }
	}

	public static void setInstance(ViewAttributesGraphePanel instance) {
		ViewAttributesGraphePanel.instance = instance;
	}


}



