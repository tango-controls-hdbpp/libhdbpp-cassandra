//+======================================================================
//$Source$
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AddSnapshotToCompareAction.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author$
//
//$Revision$
//
//$Log$
//Revision 1.2  2007/08/22 14:47:24  ounsy
//new print system
//
//Revision 1.1  2007/08/21 15:13:17  ounsy
//Print Snapshot as table or text (Mantis bug 3913)
//
//Revision 1.2  2006/04/10 08:47:14  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/12/14 14:07:18  ounsy
//first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
//under "bensikin.bensikin" and removing the same from their former locations
//
//Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailPrintPanel;
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
public class PrintSnapshotDetailAction extends BensikinAction {

    private SnapshotDetailPrintPanel panel;
    private String title;

	/**
     * Standard action constructor that sets the action's name, plus initializes
     * the reference to the <code>SnapshotDetailPrintPanel</code> component to
     * print on <code>actionPerformed</code>
     * 
     * @param name
     *            The action name
     * @param title
     *            The print title
     * @param panel
     *            The SnapshotDetailPrintPanel
     */
	public PrintSnapshotDetailAction(String name, String title, SnapshotDetailPrintPanel panel) {
		super(name);
		this.putValue(Action.NAME , name);

		this.panel = panel;
        this.title = title;
	}

	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	public void actionPerformed(ActionEvent arg0) {
        ComponentPrinter printer = new ComponentPrinter(this.panel);
        printer.setDocumentTitle(title);
        printer.setJobName(title);
        PrintOptions options = Options.getInstance().getPrintOptions();
        printer.setFitMode( options.getFitMode() );
        printer.setOrientation( options.getOrientation() );
        printer.print();
        printer = null;
        options = null;
	}

}
