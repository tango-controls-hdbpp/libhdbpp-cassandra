//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/PrintOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PrintOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: PrintOptions.java,v $
// Revision 1.6  2007/08/30 14:01:51  pierrejoseph
// * java 1.5 programming
//
// Revision 1.5  2007/08/22 14:47:24  ounsy
// new print system
//
// Revision 1.4  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.options.sub;

import java.awt.print.PageFormat;

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsPrintTab;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;
import fr.soleil.bensikin.tools.ComponentPrinter;

/**
 * The print options of the application.
 * @author GIRARDOT
 */
public class PrintOptions extends ReadWriteOptionBook implements
        PushPullOptionBook {

    /**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "print";

    /**
     * The print orientation for CSV extraction
     */
    public static final String PRINT_ORIENTATION = "PRINT_ORIENTATION";

    /**
     * The print fit mode for CSV extraction
     */
    public static final String PRINT_FIT_MODE = "PRINT_FIT_MODE";

    private int orientation = ComponentPrinter.LANDSCAPE;

    private int fitMode = ComponentPrinter.FIT_PAGE;

    /**
	 * Default constructor
	 */
	public PrintOptions () {
		super(XML_TAG);
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog () {
        OptionsPrintTab printTab = OptionsPrintTab.getInstance();
        //Orientation
        orientation = PageFormat.LANDSCAPE;
        if ( printTab.isPortrait() ) {
            orientation = PageFormat.PORTRAIT;
        }
        this.content.put( PRINT_ORIENTATION , Integer.toString(orientation) );

        fitMode = ComponentPrinter.FIT_PAGE;
        if ( printTab.isFitWidth() ) {
            fitMode = ComponentPrinter.FIT_PAGE_WIDTH;
        }
        else if ( printTab.isNoFit() ) {
            fitMode = ComponentPrinter.NO_FIT;
        }
        this.content.put( PRINT_FIT_MODE , Integer.toString(fitMode) );
    }


	/* (non-Javadoc)
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push () throws Exception {
        OptionsPrintTab printTab = OptionsPrintTab.getInstance();

        orientation = PageFormat.LANDSCAPE;
        try {
            orientation = Integer.parseInt( (String)this.content.get(PRINT_ORIENTATION) );
        }
        catch (NumberFormatException nfe) {
            orientation = PageFormat.LANDSCAPE;
        }
        if (orientation == PageFormat.PORTRAIT) {
            printTab.setPortrait(true);
        }
        else {
            printTab.setLandscape(true);
        }

        fitMode = ComponentPrinter.FIT_PAGE;
        try {
            fitMode = Integer.parseInt( (String)this.content.get(PRINT_FIT_MODE) );
        }
        catch (NumberFormatException nfe) {
            fitMode = ComponentPrinter.FIT_PAGE;
        }
        if (fitMode == ComponentPrinter.FIT_PAGE_WIDTH) {
            printTab.setFitWidth(true);
        }
        else if (fitMode == ComponentPrinter.NO_FIT) {
            printTab.setNoFit(true);
        }
        else {
            printTab.setFitPage(true);
        }

    }

    /**
     * @return the orientation
     */
    public int getOrientation () {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation (int orientation) {
        this.orientation = orientation;
    }

    /**
     * @return the fitMode
     */
    public int getFitMode () {
        return fitMode;
    }

    /**
     * @param fitMode the fitMode to set
     */
    public void setFitMode (int fitMode) {
        this.fitMode = fitMode;
    }

}
