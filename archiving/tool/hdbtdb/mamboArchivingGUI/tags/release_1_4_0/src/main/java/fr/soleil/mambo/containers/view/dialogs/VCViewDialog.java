//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCViewDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCViewDialog.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.20 $
//
// $Log: VCViewDialog.java,v $
// Revision 1.20  2007/04/06 14:27:31  ounsy
// added HDB/TDB to the dialog title
//
// Revision 1.19  2007/03/28 13:24:45  ounsy
// boolean and number scalars displayed on the same panel (Mantis bug 4428)
//
// Revision 1.18  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.17  2006/10/31 16:50:48  ounsy
// minor changes
//
// Revision 1.16  2006/10/02 15:00:45  ounsy
// setting parent of JLChart
//
// Revision 1.15  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.14  2006/09/11 14:34:40  ounsy
// avoiding a dead lock
//
// Revision 1.13  2006/08/30 12:25:46  ounsy
// small bug correction
//
// Revision 1.12  2006/08/29 15:06:44  ounsy
// better cleaning
//
// Revision 1.11  2006/08/29 14:19:27  ounsy
// now the view dialog is filled through a thread. Closing the dialog will kill this thread.
//
// Revision 1.10  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.9  2006/07/24 07:37:51  ounsy
// image support with partial loading
//
// Revision 1.8  2006/07/04 09:46:02  ounsy
// minor changes
//
// Revision 1.7  2006/04/18 12:31:58  ounsy
// For number scalars, "no data" label instead of empty graph
//
// Revision 1.6  2006/04/05 13:47:07  ounsy
// new types full support
//
// Revision 1.5  2006/03/10 12:03:25  ounsy
// state and string support
//
// Revision 1.4  2006/02/01 14:10:09  ounsy
// spectrum management
//
// Revision 1.3  2005/12/15 11:33:41  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewImagePanel;
import fr.soleil.mambo.containers.view.ViewNumberScalarPanel;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanScalarPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanSpectrumPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class VCViewDialog extends JDialog
{
    private final static Dimension dim = new Dimension( 600 , 600 );
    private final static Dimension dim2 = new Dimension( 700 , 700 );

    private JTabbedPane attributesTab;

    private static VCViewDialog instance = null;

    private JPanel mainPanel;

    private StaticChartMathExpression chart;

    private ViewConfigurationAttribute[] strings;

    private MamboCleanablePanel[] spectrumPanels;
    private MamboCleanablePanel[] imagePanels;
    private MamboCleanablePanel numberScalarsPanel;
    private MamboCleanablePanel otherScalarsPanel;
    protected DataLoader loader;

    /**
     * @param
     * @return 8 juil. 2005
     */
    public static VCViewDialog getInstance ( boolean forceReload )
    {
        if ( instance == null || forceReload )
        {
            instance = new VCViewDialog();
        }

        return instance;
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
            System.gc();
        }
    }

    /**
     * 
     */
    private VCViewDialog ()
    {
        //super( MamboFrame.getInstance() , Messages.getMessage( "DIALOGS_VIEW_VC_TITLE" ));
        super( MamboFrame.getInstance() );
        this.initTitle();
        this.initComponents();
        this.addComponents();
        this.initLayout();
        this.setModal(true);

        loader = new DataLoader();
        super.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new DialogKiller());
        int x = MamboFrame.getInstance().getWidth() - (dim2.width + 50);
        if (x < 0) x = 0;
        int y = MamboFrame.getInstance().getHeight() - (dim2.height + 100);
        if (y < 0) y = 0;
        this.setLocation( x , y  );
        loader.start();
    }

    private void initTitle() 
    {
        String title = Messages.getMessage( "DIALOGS_VIEW_VC_TITLE" );
        boolean isHistoric = ViewConfiguration.getCurrentViewConfiguration().getData().isHistoric();
        String dbName = isHistoric ? "HDB" : "TDB";
        super.setTitle ( title + " (" + dbName + ")" );
    }

    /**
     * 5 juil. 2005
     */
    private void initComponents ()
    {
        mainPanel = new JPanel();
        GUIUtilities.setObjectBackground(mainPanel,GUIUtilities.VIEW_COLOR);
        attributesTab = new JTabbedPane();
        GUIUtilities.setObjectBackground(attributesTab,GUIUtilities.VIEW_COLOR);
        ViewConfiguration vc = ViewConfiguration.getSelectedViewConfiguration();
        if (vc == null) return;
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
        spectrumPanels = new MamboCleanablePanel[spectrumTable.size()];
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
        this.setTitle(this.getTitle() + " - " + vc.getData().getName());
        chart = vc.getChartLight();
        chart.setParentForTableDialog(this);
        chart.setMinimumSize( dim );
        chart.setPreferredSize( dim );
        numberScalarsPanel = new ViewNumberScalarPanel(chart);
        //chart.setTimePrecision(40);
        GUIUtilities.setObjectBackground(chart,GUIUtilities.VIEW_COLOR);
        //chart.setLabelVisible(false);-->curves legend
        //chart.setHeaderVisible (false);-->general title

    }

    /**
     * 15 juin 2005
     */
    private void initLayout ()
    {
        this.getContentPane().setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                mainPanel,
                1, 1,
                0, 0,
                0, 0,
                true
        );
    }

    private void clean()
    {
        attributesTab.removeAll();
        this.removeAll();
        this.setModal(false);
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

    /**
     * 8 juil. 2005
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
        mainPanel.add( attributesTab );
        this.setContentPane(mainPanel);

        //this.getContentPane ().add ( chart );
    }

    protected void loadPanels()
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

    public void killLoading()
    {
        if (loader != null && loader.isAlive())
        {
            try
            {
                loader.join(2000);
                if (loader.isAlive()) loader.interrupt();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            loader = null;
        }
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

    protected class DialogKiller extends WindowAdapter
    {
        DialogKiller()
        {
            super();
        }
        public void windowClosing (WindowEvent e)
        {
            killWindow();
        }
        public void windowClosed (WindowEvent e)
        {
            killWindow();
        }
        protected void killWindow()
        {
            IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
            
            //System.out.println("****Want to kill dialog");
            if (isVisible())
            {
                if (loader.isAlive())
                {
                    try
                    {
                        //System.out.println("****Want to kill dialog test 1");
                        extractingManager.cancel();
                        //System.out.println("****Want to kill dialog test 2");
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //System.out.println("****Want to kill dialog test 3");
                killLoading();
                //System.out.println("****Want to kill dialog test 4");
                setVisible(false);
                //System.out.println("****Want to kill dialog test 5");
                clearInstance();
                //System.out.println("****Want to kill dialog test 6");
                try
                {
                    extractingManager.allow();
                    //System.out.println("****Want to kill dialog test 7");
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //System.out.println("****killed dialog");
        }
    }
}
