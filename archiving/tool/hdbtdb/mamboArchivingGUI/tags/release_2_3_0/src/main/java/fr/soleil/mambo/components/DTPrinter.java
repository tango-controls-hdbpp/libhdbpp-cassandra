// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/DTPrinter.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DTPrinter.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: DTPrinter.java,v $
// Revision 1.5 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.4 2006/05/16 09:35:45 ounsy
// minor changes
//
// Revision 1.3 2005/12/15 10:54:50 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components;

import java.awt.Color;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

import fr.soleil.mambo.tools.GUIUtilities;

/**
 * Usefull class to print
 * 
 * @author GIRARDOT
 */
public class DTPrinter {

	/**
	 * Displays the print dialog and sends a component snapshot to the printer.
	 * Using the printerResolution can be usefull to print your frame bigger or
	 * smaller. A screen typicaly has a resolution of ~100dpi. This method does
	 * not support multiple page documents.
	 * 
	 * @param comp
	 *            JFrame to be printed out.
	 * @param title
	 *            Title of the print dialog.
	 * @param fitToPage
	 *            True to fit the component to the page (printerResolution
	 *            ignored).
	 * @param printerResolution
	 *            Printer resolution when fitToPage is not enabled.
	 */
	// printerResolution=0
	public static void printFrame(JFrame comp, String title, boolean fitToPage,
			int printerResolution) {
		// Default
		PageAttributes pa = new PageAttributes();
		JobAttributes ja = new JobAttributes();
		pa.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
		pa.setColor(PageAttributes.ColorType.COLOR);
		pa.setMedia(PageAttributes.MediaType.A4);
		if (fitToPage) {
			// Default resolution
			pa.setPrinterResolution(72);
		} else {
			pa.setPrinterResolution(printerResolution);
		}
		ja.setMaxPage(1);
		ja.setMinPage(1);
		ja.setDialog(JobAttributes.DialogType.COMMON);

		// Displays print window
		PrintJob printJob = java.awt.Toolkit.getDefaultToolkit().getPrintJob(
				comp, title, ja, pa);
		if (printJob != null) {
			// Get image dimension
			int w = comp.getSize().width;
			int h = comp.getSize().height;
			int tx, ty;

			// Make a screenshot of the graph
			BufferedImage img = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			Color oldBackground = comp.getBackground();
			comp.setBackground(Color.WHITE);
			comp.printAll(img.getGraphics());
			comp.setBackground(oldBackground);

			try {
				// Fit the draw to the page by changing the printer resolution
				if (fitToPage) {
					// Get page dimension (should be given for 72dpi resolution)
					int wp = printJob.getPageDimension().width - 72; // 0.5inch
					// margin
					int hp = printJob.getPageDimension().height - 72; // 0.5inch
					// margin

					// Fit the graph to the page
					double ratioW = (double) w / (double) wp;
					double ratioH = (double) h / (double) hp;
					double nResolution;

					if (ratioW > ratioH) {
						// We get ratioW
						// We center verticaly
						nResolution = 72.0 * ratioW;
						tx = (int) (nResolution * 0.5);
						double cH = nResolution / 72.0 * hp - h;
						ty = (int) (0.5 * (nResolution + cH));
					} else {
						// We get ratioH
						// We center horizontaly
						nResolution = 72.0 * ratioH;
						double cW = nResolution / 72.0 * wp - w;
						tx = (int) (0.5 * (nResolution + cW));
						ty = (int) (nResolution * 0.5);
					}

					pa.setPrinterResolution((int) (nResolution + 0.5));
				} else {
					// 0.5 inch margin
					tx = printerResolution / 2;
					ty = printerResolution / 2;
				}

				// Print it
				java.awt.Graphics g = printJob.getGraphics();
				g.translate(tx, ty);
				g.setClip(0, 0, w, h);
				g.drawImage(img, 0, 0, null);
				g.dispose();
				printJob.end();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(comp,
						"Exception occured while printing" + GUIUtilities.CRLF
								+ e.getMessage(), title,
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/*
	 * public static void printText(String title,String content) { int
	 * printerResolution = 72; JTextPane component = new JTextPane ( new
	 * HTMLDocument() ); component.setContentType ("text/xml");
	 * component.setText (content); int h = component.getPreferredSize().height;
	 * component.setSize ( 500 , h );
	 * ATKGraphicsUtils.printComponent(component,title,false,printerResolution);
	 * }
	 */
	public static void printText(String title, String content) {
		int printerResolution = 72;

		Vector<String> contentParts = GUIUtilities.getContentParts(content);
		printStrings(contentParts, title, false, printerResolution);
		/*
		 * Enumeration enum = contentParts.elements (); System.out.println (
		 * "----------------------" ); while ( enum.hasMoreElements () ) {
		 * String nextPart = (String) enum.nextElement (); System.out.println (
		 * "*********" ); System.out.println ( nextPart ); System.out.println (
		 * "*********" ); JTextPane component = new JTextPane ( new
		 * HTMLDocument() ); component.setContentType ("text/xml");
		 * component.setText (nextPart); component.setSize ( 500 , 1000 );
		 * //ATKGraphicsUtils
		 * .printComponent(component,title,false,printerResolution); }
		 * System.out.println ( "----------------------" );
		 */
	}

	/*
	 * OK public static void printText(String title,String content) { int
	 * printerResolution = 72; Vector contentParts =
	 * GUIUtilities.getContentParts ( content ); Enumeration enum =
	 * contentParts.elements (); System.out.println ( "----------------------"
	 * ); String nextPart = (String) enum.nextElement (); System.out.println (
	 * nextPart ); JTextPane component = new JTextPane ( new HTMLDocument() );
	 * component.setContentType ("text/xml"); component.setText (nextPart);
	 * component.setSize ( 500 , 1000 );
	 * ATKGraphicsUtils.printComponent(component,title,false,printerResolution);
	 * System.out.println ( "----------------------" ); }
	 */

	/**
	 * @param contentParts
	 * @param title
	 * @param b
	 * @param printerResolution
	 *            31 ao�t 2005
	 */
	private static void printStrings(Vector<String> contentParts, String title,
			boolean fitToPage, int printerResolution) {
		PageAttributes pa = new PageAttributes();
		JobAttributes ja = new JobAttributes();
		pa.setPrintQuality(PageAttributes.PrintQualityType.HIGH);
		pa.setColor(PageAttributes.ColorType.COLOR);
		pa.setMedia(PageAttributes.MediaType.A4);
		if (fitToPage) {
			pa.setPrinterResolution(72);
		} else {
			pa.setPrinterResolution(printerResolution);
		}

		ja.setMinPage(1);
		ja.setMaxPage(1);
		ja.setDialog(JobAttributes.DialogType.NATIVE);// COMMON ou NATIVE
		ja.setDefaultSelection(JobAttributes.DefaultSelectionType.ALL);

		// Displays print window

		JFrame dummy = new JFrame();
		PrintJob printJob = java.awt.Toolkit.getDefaultToolkit().getPrintJob(
				dummy, title, ja, pa);

		if (printJob != null) {
			// Get image dimension
			int w = 500;
			int h = 1000;
			int tx, ty;

			Enumeration<String> enumeration = contentParts.elements();
			// System.out.println ( "----------------------" );
			while (enumeration.hasMoreElements()) {
				String nextPart = enumeration.nextElement();
				// System.out.println ( "*********" );
				// System.out.println ( nextPart );
				// System.out.println ( "*********" );
				JTextPane comp = new JTextPane(new HTMLDocument());
				comp.setContentType("text/xml");
				comp.setText(nextPart);
				comp.setSize(500, 1000);

				// Make a screenshot of the graph
				BufferedImage img = new BufferedImage(w, h,
						BufferedImage.TYPE_INT_RGB);
				Color oldBackground = comp.getBackground();
				comp.setBackground(Color.WHITE);
				comp.paint(img.getGraphics());
				comp.setBackground(oldBackground);

				// Fit the draw to the page by changing the printer resolution
				// 0.5 inch margin
				tx = printerResolution / 2;
				ty = printerResolution / 2;

				// Print it
				java.awt.Graphics g = printJob.getGraphics();
				g.translate(tx, ty);
				g.setClip(0, 0, w, h);
				g.drawImage(img, 0, 0, null);
				g.dispose();
			}
			printJob.end();
			// System.out.println ( "----------------------" );
		}

	}

	/**
	 * Displays the print dialog and sends a component snapshot to the printer.
	 * Using the printerResolution can be usefull to print your component bigger
	 * or smaller. A screen typicaly has a resolution of ~100dpi. This method
	 * does not support multiple page documents.
	 * 
	 * @param comp
	 *            Component to be printed out.
	 * @param title
	 *            Title of the print dialog.
	 * @param fitToPage
	 *            True to fit the component to the page (printerResolution
	 *            ignored).
	 * @param printerResolution
	 *            Printer resolution when fitToPage is not enabled.
	 */
	/*
	 * private static void printComponent ( JComponent comp , String title ,
	 * boolean fitToPage , int printerResolution ) { PageAttributes pa = new
	 * PageAttributes(); JobAttributes ja = new JobAttributes();
	 * pa.setPrintQuality( PageAttributes.PrintQualityType.HIGH ); pa.setColor(
	 * PageAttributes.ColorType.COLOR ); pa.setMedia(
	 * PageAttributes.MediaType.A4 ); if ( fitToPage ) { // Default resolution
	 * pa.setPrinterResolution( 72 ); } else { pa.setPrinterResolution(
	 * printerResolution ); } if ( fitToPage )//CLA modification {
	 * ja.setMaxPage( 1 ); } else { ja.setMaxPage( Integer.MAX_VALUE ); }
	 * ja.setMinPage( 1 ); ja.setDialog( JobAttributes.DialogType.NATIVE );
	 * ja.setDefaultSelection( JobAttributes.DefaultSelectionType.ALL ); //
	 * Displays print window Window parent =
	 * ATKGraphicsUtils.getWindowForComponent( comp ); PrintJob printJob; if (
	 * parent instanceof Frame ) { printJob =
	 * java.awt.Toolkit.getDefaultToolkit().getPrintJob( ( Frame ) parent ,
	 * title , ja , pa ); } else { Frame dummy = new Frame(); printJob =
	 * java.awt.Toolkit.getDefaultToolkit().getPrintJob( dummy , title , ja , pa
	 * ); } if ( printJob != null ) { // Get image dimension int w =
	 * comp.getSize().width; int h = comp.getSize().height; int tx , ty; // Make
	 * a screenshot of the graph BufferedImage img = new BufferedImage( w , h ,
	 * BufferedImage.TYPE_INT_RGB ); Color oldBackground = comp.getBackground();
	 * comp.setBackground( Color.WHITE ); comp.paint( img.getGraphics() );
	 * comp.setBackground( oldBackground ); try { // Fit the draw to the page by
	 * changing the printer resolution if ( fitToPage ) { // Get page dimension
	 * (should be given for 72dpi resolution) int wp =
	 * printJob.getPageDimension().width - 72; // 0.5inch margin int hp =
	 * printJob.getPageDimension().height - 72; // 0.5inch margin // Fit the
	 * graph to the page double ratioW = ( double ) w / ( double ) wp; double
	 * ratioH = ( double ) h / ( double ) hp; double nResolution; if ( ratioW >
	 * ratioH ) { // We get ratioW // We center verticaly nResolution = 72.0 *
	 * ratioW; tx = ( int ) ( nResolution * 0.5 ); double cH = nResolution /
	 * 72.0 * ( double ) hp - ( double ) h; ty = ( int ) ( 0.5 * ( nResolution +
	 * cH ) ); } else { // We get ratioH // We center horizontaly nResolution =
	 * 72.0 * ratioH; double cW = nResolution / 72.0 * ( double ) wp - ( double
	 * ) w; tx = ( int ) ( 0.5 * ( nResolution + cW ) ); ty = ( int ) (
	 * nResolution * 0.5 ); } pa.setPrinterResolution( ( int ) ( nResolution +
	 * 0.5 ) ); } else { // 0.5 inch margin tx = printerResolution / 2; ty =
	 * printerResolution / 2; } // Print it java.awt.Graphics g =
	 * printJob.getGraphics(); g.translate( tx , ty ); g.setClip( 0 , 0 , w , h
	 * ); g.drawImage( img , 0 , 0 , null ); g.dispose(); printJob.end(); }
	 * catch ( Exception e ) { e.printStackTrace();
	 * JOptionPane.showMessageDialog( parent ,
	 * "Exception occured while printing" + GUIUtilities.CRLF + e.getMessage() ,
	 * title , JOptionPane.ERROR_MESSAGE ); } } }
	 */

}
