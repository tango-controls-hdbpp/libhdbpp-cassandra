// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/PrintSnapshotComparisonAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AddSnapshotToCompareAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.4 $
//
// $Log: PrintSnapshotComparisonAction.java,v $
// Revision 1.4 2007/10/29 14:42:13 soleilarc
// Author: XP
// Mantis bug ID: 5629
// Comment : In the actionPerfomed method, change the state of the
// titlesDisplayed data before and after printing.
//
// Revision 1.3 2007/08/22 14:47:24 ounsy
// new print system
//
// Revision 1.2 2006/04/10 08:47:14 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.1 2005/12/14 14:07:18 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2 2005/08/22 11:58:33 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotCompareTable;
import fr.soleil.bensikin.models.SnapshotCompareTablePrintModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.sub.PrintOptions;
import fr.soleil.bensikin.tools.ComponentPrinter;

/**
 * An action that prints the current snapshot comparison's table to a printer.
 * <UL>
 * <LI>Uses DTPrinter to print its SnapshotCompareTable attribute
 * </UL>
 * 
 * @author CLAISSE
 */
public class PrintSnapshotComparisonAction extends BensikinAction {

    private SnapshotCompareTable compareTable;

    /**
     * Standard action constructor that sets the action's name, plus initializes
     * the reference to the <code>SnapshotCompareTable</code> component to print
     * on <code>actionPerformed</code>
     * 
     * @param name
     *            The action name
     * @param _compareTable
     */
    public PrintSnapshotComparisonAction(String name,
            SnapshotCompareTable _compareTable) {
        super(name);
        this.putValue(Action.NAME, name);

        this.compareTable = _compareTable;
    }

    /*
     * (non-Javadoc)
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        SnapshotCompareTablePrintModel model = null;
        if (compareTable != null
                && compareTable.getModel() instanceof SnapshotCompareTablePrintModel) {
            model = (SnapshotCompareTablePrintModel) compareTable.getModel();
        }
        model.setTitlesDisplayed(true);
        ComponentPrinter printer = new ComponentPrinter(this.compareTable);
        printer.setDocumentTitle(getValue(Action.NAME).toString());
        printer.setJobName(getValue(Action.NAME).toString());
        PrintOptions options = Options.getInstance().getPrintOptions();
        printer.setFitMode(options.getFitMode());
        printer.setOrientation(options.getOrientation());
        printer.print();
        printer = null;
        options = null;
        model.setTitlesDisplayed(false);
        model = null;
    }

}
