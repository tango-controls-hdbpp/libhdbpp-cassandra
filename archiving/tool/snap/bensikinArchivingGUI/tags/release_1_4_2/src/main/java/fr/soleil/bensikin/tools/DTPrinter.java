//+======================================================================
//$Source$
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ConfigurationFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author$
//
//$Revision$
//
//$Log$
//Revision 1.1  2007/08/22 14:47:24  ounsy
//new print system
//
//Revision 1.3  2006/11/29 09:57:25  ounsy
//minor changes
//
//Revision 1.2  2005/12/14 16:10:32  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:25:08  chinkumo
//no message
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================


package fr.soleil.bensikin.tools;

import java.awt.Color;
import java.awt.Frame;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;

/**
 * Usefull class to print
 *
 * @author GIRARDOT
 */
public class DTPrinter
{
    /**
     * Displays the print dialog and sends a component snapshot to the printer.
     * Using the printerResolution can be usefull to print your frame bigger
     * or smaller. A screen typicaly has a resolution of ~100dpi. This method does
     * not support multiple page documents.
     *
     * @param comp              JFrame to be printed out.
     * @param title             Title of the print dialog.
     * @param fitToPage         True to fit the component to the page (printerResolution ignored).
     * @param printerResolution Printer resolution when fitToPage is not enabled.
     */
    //printerResolution=0
    public static void printFrame ( JFrame comp , String title , boolean fitToPage , int printerResolution )
    {
        // Default
        PageAttributes pa = new PageAttributes();
        JobAttributes ja = new JobAttributes();
        pa.setPrintQuality( PageAttributes.PrintQualityType.HIGH );
        pa.setColor( PageAttributes.ColorType.COLOR );
        pa.setMedia( PageAttributes.MediaType.A4 );
        if ( fitToPage )
        {
            // Default resolution
            pa.setPrinterResolution( 72 );
        }
        else
        {
            pa.setPrinterResolution( printerResolution );
        }
        ja.setMaxPage( 1 );
        ja.setMinPage( 1 );
        ja.setDialog( JobAttributes.DialogType.COMMON );

        // Displays print window
        PrintJob printJob = java.awt.Toolkit.getDefaultToolkit().getPrintJob( comp , title , ja , pa );
        if ( printJob != null )
        {
            // Get image dimension
            int w = comp.getSize().width;
            int h = comp.getSize().height;
            int tx , ty;

            // Make a screenshot of the graph
            BufferedImage img = new BufferedImage( w , h , BufferedImage.TYPE_INT_RGB );
            Color oldBackground = comp.getBackground();
            comp.setBackground( Color.WHITE );
            comp.printAll( img.getGraphics() );
            comp.setBackground( oldBackground );

            try
            {
                // Fit the draw to the page by changing the printer resolution
                if ( fitToPage )
                {
                    // Get page dimension (should be given for 72dpi resolution)
                    int wp = printJob.getPageDimension().width - 72; // 0.5inch margin
                    int hp = printJob.getPageDimension().height - 72; // 0.5inch margin

                    // Fit the graph to the page
                    double ratioW = ( double ) w / ( double ) wp;
                    double ratioH = ( double ) h / ( double ) hp;
                    double nResolution;

                    if ( ratioW > ratioH )
                    {
                        // We get ratioW
                        // We center verticaly
                        nResolution = 72.0 * ratioW;
                        tx = ( int ) ( nResolution * 0.5 );
                        double cH = nResolution / 72.0 * ( double ) hp - ( double ) h;
                        ty = ( int ) ( 0.5 * ( nResolution + cH ) );
                    }
                    else
                    {
                        // We get ratioH
                        // We center horizontaly
                        nResolution = 72.0 * ratioH;
                        double cW = nResolution / 72.0 * ( double ) wp - ( double ) w;
                        tx = ( int ) ( 0.5 * ( nResolution + cW ) );
                        ty = ( int ) ( nResolution * 0.5 );
                    }

                    pa.setPrinterResolution( ( int ) ( nResolution + 0.5 ) );
                }
                else
                {
                    // 0.5 inch margin
                    tx = printerResolution / 2;
                    ty = printerResolution / 2;
                }

                // Print it
                java.awt.Graphics g = printJob.getGraphics();
                g.translate( tx , ty );
                g.setClip( 0 , 0 , w , h );
                g.drawImage( img , 0 , 0 , null );
                g.dispose();
                printJob.end();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog
                        ( comp ,
                          "Exception occured while printing\n" + e.getMessage() ,
                          title ,
                          JOptionPane.ERROR_MESSAGE );
            }
        }
    }
    
    /*public static void printText(String title,String content)
    {
        int printerResolution = 72;
        
    	JTextPane component = new JTextPane ( new HTMLDocument() );
        component.setContentType ("text/xml");        
        component.setText (content);
        int h = component.getPreferredSize().height;
        component.setSize ( 500 , h );
        ATKGraphicsUtils.printComponent(component,title,false,printerResolution);
    }*/
    /**
     * @param title   The title of the document to print.
     * @param content The content of the document to print.
     */
    public static void printText ( String title , String content )
    {
        int printerResolution = 72;

        Vector contentParts = GUIUtilities.getContentParts( content );
        printComponent( contentParts , title , false , printerResolution );
    }


    /**
     * Prints all text parts contained in contentParts.
     *
     * @param contentParts
     * @param title
     * @param fitToPage
     * @param printerResolution
     */
    private static void printComponent ( Vector contentParts , String title , boolean fitToPage , int printerResolution )
    {
        PageAttributes pa = new PageAttributes();
        JobAttributes ja = new JobAttributes();
        pa.setPrintQuality( PageAttributes.PrintQualityType.HIGH );
        pa.setColor( PageAttributes.ColorType.COLOR );
        pa.setMedia( PageAttributes.MediaType.A4 );
        if ( fitToPage )
        {
            pa.setPrinterResolution( 72 );
        }
        else
        {
            pa.setPrinterResolution( printerResolution );
        }


        ja.setMinPage( 1 );
        ja.setMaxPage( 1 );
        ja.setDialog( JobAttributes.DialogType.NATIVE );
        ja.setDefaultSelection( JobAttributes.DefaultSelectionType.ALL );

        // Displays print window

        Frame dummy = new Frame();
        PrintJob printJob = java.awt.Toolkit.getDefaultToolkit().getPrintJob( dummy , title , ja , pa );

        if ( printJob != null )
        {
//          Get image dimension
            int w = 500;
            int h = 1000;
            int tx , ty;

            Enumeration enumer = contentParts.elements();
            while ( enumer.hasMoreElements() )
            {
                String nextPart = ( String ) enumer.nextElement();

                JTextPane comp = new JTextPane( new HTMLDocument() );
                comp.setContentType( "text/xml" );
                comp.setText( nextPart );
                comp.setSize( 500 , 1000 );

//              Make a screenshot of the graph
                BufferedImage img = new BufferedImage( w , h , BufferedImage.TYPE_INT_RGB );
                Color oldBackground = comp.getBackground();
                comp.setBackground( Color.WHITE );
                comp.paint( img.getGraphics() );
                comp.setBackground( oldBackground );

                // Fit the draw to the page by changing the printer resolution
                // 0.5 inch margin
                tx = printerResolution / 2;
                ty = printerResolution / 2;

                // Print it
                java.awt.Graphics g = printJob.getGraphics();
                g.translate( tx , ty );
                g.setClip( 0 , 0 , w , h );
                g.drawImage( img , 0 , 0 , null );
                g.dispose();
            }
            printJob.end();
        }
    }

    /**
     * Displays the print dialog and sends a component snapshot to the printer.
     * Using the printerResolution can be usefull to print your component bigger
     * or smaller. A screen typicaly has a resolution of ~100dpi. This method does
     * not support multiple page documents.
     *
     * @param comp              Component to be printed out.
     * @param title             Title of the print dialog.
     * @param fitToPage         True to fit the component to the page (printerResolution ignored).
     * @param printerResolution Printer resolution when fitToPage is not enabled.
     */
    public static void printComponent ( JComponent comp , String title , boolean fitToPage , int printerResolution )
    {
        PageAttributes pa = new PageAttributes();
        JobAttributes ja = new JobAttributes();
        pa.setPrintQuality( PageAttributes.PrintQualityType.HIGH );
        pa.setColor( PageAttributes.ColorType.COLOR );
        pa.setMedia( PageAttributes.MediaType.A4 );
        if ( fitToPage )
        {
            // Default resolution
            pa.setPrinterResolution( 72 );
        }
        else
        {
            pa.setPrinterResolution( printerResolution );
        }
        if ( fitToPage )//CLA modification
        {
            ja.setMaxPage( 1 );
        }
        else
        {
            ja.setMaxPage( Integer.MAX_VALUE );
        }
        ja.setMinPage( 1 );
        ja.setDialog( JobAttributes.DialogType.NATIVE );
        ja.setDefaultSelection( JobAttributes.DefaultSelectionType.ALL );

        // Displays print window

        Window parent = ATKGraphicsUtils.getWindowForComponent( comp );
        PrintJob printJob;
        if ( parent instanceof Frame )
        {
            printJob = java.awt.Toolkit.getDefaultToolkit().getPrintJob( ( Frame ) parent , title , ja , pa );
        }
        else
        {
            Frame dummy = new Frame();
            printJob = java.awt.Toolkit.getDefaultToolkit().getPrintJob( dummy , title , ja , pa );
        }

        if ( printJob != null )
        {
            // Get image dimension
            int w = comp.getSize().width;
            int h = comp.getSize().height;
            int tx , ty;

            // Make a screenshot of the graph
            BufferedImage img = new BufferedImage( w , h , BufferedImage.TYPE_INT_RGB );
            Color oldBackground = comp.getBackground();
            comp.setBackground( Color.WHITE );
            //comp.paint(img.getGraphics());
            //comp.print(img.getGraphics());
            comp.printAll( img.getGraphics() );
            comp.setBackground( oldBackground );

            try
            {

                // Fit the draw to the page by changing the printer resolution
                if ( fitToPage )
                {

                    // Get page dimension (should be given for 72dpi resolution)
                    int wp = printJob.getPageDimension().width - 72; // 0.5inch margin
                    int hp = printJob.getPageDimension().height - 72; // 0.5inch margin

                    // Fit the graph to the page
                    double ratioW = ( double ) w / ( double ) wp;
                    double ratioH = ( double ) h / ( double ) hp;
                    double nResolution;

                    if ( ratioW > ratioH )
                    {
                        // We get ratioW
                        // We center verticaly
                        nResolution = 72.0 * ratioW;
                        tx = ( int ) ( nResolution * 0.5 );
                        double cH = nResolution / 72.0 * ( double ) hp - ( double ) h;
                        ty = ( int ) ( 0.5 * ( nResolution + cH ) );

                    }
                    else
                    {
                        // We get ratioH
                        // We center horizontaly
                        nResolution = 72.0 * ratioH;
                        double cW = nResolution / 72.0 * ( double ) wp - ( double ) w;
                        tx = ( int ) ( 0.5 * ( nResolution + cW ) );
                        ty = ( int ) ( nResolution * 0.5 );
                    }

                    pa.setPrinterResolution( ( int ) ( nResolution + 0.5 ) );

                }
                else
                {
                    // 0.5 inch margin
                    tx = printerResolution / 2;
                    ty = printerResolution / 2;
                }

                // Print it
                java.awt.Graphics g = printJob.getGraphics();
                g.translate( tx , ty );
                g.setClip( 0 , 0 , w , h );
                g.drawImage( img , 0 , 0 , null );
                g.dispose();
                printJob.end();
            }
            catch ( Exception e )
            {

                e.printStackTrace();
                JOptionPane.showMessageDialog( parent , "Exception occured while printing\n" + e.getMessage() ,
                                               title , JOptionPane.ERROR_MESSAGE );

            }
        }
    }

}