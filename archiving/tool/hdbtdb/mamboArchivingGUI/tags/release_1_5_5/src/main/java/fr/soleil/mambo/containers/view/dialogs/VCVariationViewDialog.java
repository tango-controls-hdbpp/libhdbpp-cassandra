//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCVariationViewDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCVariationViewDialog.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationViewDialog.java,v $
// Revision 1.5  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.4  2006/09/05 14:11:49  ounsy
// updated for sampling compatibility
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/12/15 11:33:28  ounsy
// minor changes
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
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.tools.Messages;

public class VCVariationViewDialog extends JDialog
{
    private Dimension dim = new Dimension( 600 , 600 );
    //private JPanel myPanel;
    //private static VCVariationViewDialog instance = null;
    private StaticChartMathExpression chart;

    /**
     * 
     */
    public VCVariationViewDialog ( ViewConfiguration vc )
    {
        super( MamboFrame.getInstance() , Messages.getMessage( "DIALOGS_VARIATION_VIEW" ) , true );
        //this.setSize ( dim );
        initComponents( vc );
        addComponents();
        initLayout();
        int mainWidth = MamboFrame.getInstance().getWidth();
        int width = getWidth();
        setLocation( mainWidth - width - 10 , 0 );
    }

    /**
     * 5 juil. 2005
     */
    private void initComponents ( ViewConfiguration vc )
    {
        chart = vc.getData().getChart();
        String startDate = vc.getData().getStartDate().toString();
        String endDate = vc.getData().getEndDate().toString();
        boolean historic = vc.getData().isHistoric();
        SamplingType samplingType = vc.getData ().getSamplingType ();
        
        try
        {
            vc.getAttributes().setChartAttributes( chart , startDate , endDate , historic , samplingType );
        }
        catch ( Exception e )
        {
            return;
        }
    }

    /**
     * 15 juin 2005
     */
    private void initLayout ()
    {

    }

    /**
     * 8 juil. 2005
     */
    private void addComponents ()
    {
        chart.setMinimumSize( dim );
        chart.setPreferredSize( dim );

        JScrollPane scrollPane = new JScrollPane( chart );
        getContentPane().add( scrollPane );
        getContentPane().setLayout( new GridLayout() );
        Dimension dim2 = new Dimension( 650 , 650 );
        setSize( dim2 );

        //this.getContentPane ().add ( chart );
    }
    
}