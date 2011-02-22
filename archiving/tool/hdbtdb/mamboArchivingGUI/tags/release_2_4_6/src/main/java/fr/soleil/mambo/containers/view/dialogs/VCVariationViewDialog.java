// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCVariationViewDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationViewDialog.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationViewDialog.java,v $
// Revision 1.5 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.4 2006/09/05 14:11:49 ounsy
// updated for sampling compatibility
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/12/15 11:33:28 ounsy
// minor changes
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
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewDAOKey;
import fr.soleil.mambo.tools.Messages;

public class VCVariationViewDialog extends JDialog {

    private static final long serialVersionUID = 2886611541351110548L;
    private Dimension dim = new Dimension(600, 600);
    private ChartViewer chart;
    private String id;

    public VCVariationViewDialog(ViewConfiguration conf) {
        super(MamboFrame.getInstance(), Messages.getMessage("DIALOGS_VARIATION_VIEW"), true);
        initComponents(conf);
        addComponents();
        int mainWidth = MamboFrame.getInstance().getWidth();
        int width = getWidth();
        setLocation(mainWidth - width - 10, 0);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (chart != null) {
                    MamboViewDAOFactory daoFactory = (MamboViewDAOFactory) chart.getDaoFactory();
                    if ((daoFactory != null) && (id != null)) {
                        daoFactory.removeKeyForDaoScalar(id);
                    }
                    chart.clearDAO();
                }
                chart = null;
                id = null;
            }
        });
        repaint();
    }

    private void initComponents(ViewConfiguration vc) {
        id = vc.getCurrentId();
        chart = vc.getData().getChart();
        try {
            chart.addKey(MamboViewDAOFactory.class.getName(), new ViewDAOKey(
                    ViewDAOKey.TYPE_NUMBER_SCALAR, id));
            chart.switchDAOFactory(MamboViewDAOFactory.class.getName());
            vc.loadAttributes(chart);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 8 juil. 2005
     */
    private void addComponents() {
        IChartViewer chartViewerComponent = chart.getComponent();
        chartViewerComponent.setSize(dim.width, dim.height);
        JScrollPane scrollPane = new JScrollPane((Component) chartViewerComponent);
        getContentPane().add(scrollPane);
        getContentPane().setLayout(new GridLayout());
        Dimension dim2 = new Dimension(dim.width + 50, dim.height + 50);
        setSize(dim2);
    }

}
