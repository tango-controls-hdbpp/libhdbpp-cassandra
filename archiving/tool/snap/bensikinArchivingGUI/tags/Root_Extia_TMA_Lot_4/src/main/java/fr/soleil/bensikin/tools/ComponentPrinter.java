package fr.soleil.bensikin.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Timestamp;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;

/**
 * A class that allows to print any kind of <code>java.awt.Component</code>.
 * <i>(Example : JFrame, JDialog, or any JComponent)</i><br>
 * Based on code of MPanelPrinter developped by mep<br> {@link http
 * ://www.javafr.com/
 * codes/IMPRESSION-JPANEL-SANS-COUPURE-COMPOSANTS-BAS-PAGE_31743.aspx} <br>
 * <b><u>Use example:</u></b> <i>( myComponent represents the component you want
 * to print )</i> <code>
 * String jobName = "My job name";<br>
 * String docTitle = "My document title";<br>
 * RGComponentPrinter printer = new RGComponentPrinter(<i>myComponent</i>);<br>
 * printer.setOrientation(RGComponentPrinter.LANDSCAPE);<br>
 * printer.setFitMode(DTPrinter.FIT_PAGE);<br>
 * printer.setDocumentTitle(docTitle);<br>
 * printer.setJobName(jobName);<br>
 * printer.print();
 * </code> <br>
 * 
 * @author Rapha&euml;l GIRARDOT
 */
public class ComponentPrinter implements Printable, Pageable {

	protected Component component;
	protected PageFormat pageFormat;
	protected PrinterJob printJob;
	protected int yCount;
	protected int xCount;
	protected String documentTitle;
	protected BufferedImage toPrint;
	protected int fitMode;
	protected int pageCount;
	protected double ratio;
	protected Timestamp date;

	/**
	 * Used with setOrientation(). If you use this value with setOrientation(),
	 * your Component will be printed with "Portrait" orientation.
	 * 
	 * @see #setOrientation(int)
	 */
	public final static int PORTRAIT = PageFormat.PORTRAIT;

	/**
	 * Used with setOrientation(). If you use this value with setOrientation(),
	 * your Component will be printed with "Landscape" orientation.
	 * 
	 * @see #setOrientation(int)
	 */
	public final static int LANDSCAPE = PageFormat.LANDSCAPE;

	/**
	 * Used with setFitMode(). If you use this value with setFitMode(), your
	 * component will be scaled in order to fit page width. If your Component is
	 * bigger than page height, it will be splitted into as many pages as
	 * necessary.
	 * 
	 * @see #setFitMode(int)
	 */
	public final static int FIT_PAGE_WIDTH = 0;

	/**
	 * Used with setFitMode(). If you use this value with setFitMode(), your
	 * component will be scaled in order to fit page (width and height).
	 * 
	 * @see #setFitMode(int)
	 */
	public final static int FIT_PAGE = 1;

	/**
	 * Used with setFitMode(). If you use this value with setFitMode(), your
	 * component will not be scaled. However, if your Component is bigger than
	 * page width or heught, it will be splitted into as many pages as
	 * necessary.<br>
	 * If your component is bigger than page width AND height, it will be
	 * splitted horizontally then vertically.<br>
	 * Example: if your component needs 2 pages for width and 2 pages for
	 * height, page 1 is TOP-LEFT, page 2 is TOP-RIGHT, page 3 is BOTTOM-LEFT,
	 * and page 4 is BOTTOM-RIGHT.
	 * 
	 * @see #setFitMode(int)
	 */
	public final static int NO_FIT = 2;

	/**
	 * Constructor
	 * 
	 * @param component
	 *            . The component you wish to print.
	 */
	public ComponentPrinter(Component component) {
		this.component = component;
		initPrintablePanel();
	}

	protected void initPrintablePanel() {
		toPrint = null;
		documentTitle = "";
		pageCount = 0;
		ratio = 1;
		fitMode = NO_FIT;
		printJob = PrinterJob.getPrinterJob();
		pageFormat = printJob.defaultPage();

		Paper paper = pageFormat.getPaper();
		paper.setImageableArea(paper.getImageableX(), paper.getImageableY(),
				paper.getWidth() - 30,// 1/72 inch
				paper.getHeight() - 30 - 28);
		pageFormat.setPaper(paper);
		paper = null;
	}

	/**
	 * Sets paper orientation
	 * 
	 * @see #PORTRAIT
	 * @see #LANDSCAPE
	 * @see #getOrientation()
	 */
	public void setOrientation(int orientation) {
		pageFormat.setOrientation(orientation);
	}

	/**
	 * Returns chosen paper orientation
	 * 
	 * @return Chosen paper orientation
	 * @see #PORTRAIT
	 * @see #LANDSCAPE
	 * @see #setOrientation(int)
	 */
	public int getOrientation() {
		return pageFormat.getOrientation();
	}

	/**
	 * Sets whether your Component should be scaled (and how) before printing. <br>
	 * <i>(Has no effect on the Component itself, but just on how it is rendered
	 * in printing)</i> <br>
	 * default value : <code><b>NO_FIT</b></code>
	 * 
	 * @see #FIT_PAGE
	 * @see #FIT_PAGE_WIDTH
	 * @see #NO_FIT
	 * @see #getFitMode()
	 */
	public void setFitMode(int fitMode) {
		this.fitMode = fitMode;
	}

	/**
	 * Returns chosen fit mode
	 * 
	 * @return Chosen fit mode
	 * @see #FIT_PAGE
	 * @see #FIT_PAGE_WIDTH
	 * @see #NO_FIT
	 * @see #setFitMode(int)
	 */
	public int getFitMode() {
		return fitMode;
	}

	/**
	 * Returns page width
	 * 
	 * @return Page with
	 */
	// public int getPageWidth () {
	// return (int) pageFormat.getImageableWidth();
	// }
	/**
	 * Returns top margin
	 * 
	 * @return Top margin
	 */
	// public double getMarginTop () {
	// return pageFormat.getImageableY();
	// }
	/**
	 * Returns left margin
	 * 
	 * @return Left margin
	 */
	// public double getMarginLeft () {
	// return pageFormat.getImageableX();
	// }
	/**
	 * Sets Left and Right margins (in pixel).
	 * 
	 * @param margin
	 *            The wished margin
	 */
	// public void setLRMargins (double margin) {
	// Paper paper = pageFormat.getPaper();
	// paper.setImageableArea(
	// margin,
	// paper.getImageableY(),
	// paper.getWidth() - margin,
	// paper.getImageableHeight()
	// );
	// pageFormat.setPaper(paper);
	// }
	/**
	 * Sets Top and Bottom margins (in pixel).
	 * 
	 * @param margin
	 *            The wished margin
	 */
	// public void setTBMargins (double margin) {
	// Paper paper = pageFormat.getPaper();
	// paper.setImageableArea(
	// paper.getImageableX(),
	// margin,
	// paper.getImageableWidth(),
	// paper.getHeight() - margin
	// );
	// pageFormat.setPaper(paper);
	// }
	/**
	 * Sets a title to your page. This title is used with bottom assessment : <br>
	 * <i>documentTitle - [pageNumber/pageCount]</i>
	 * 
	 * @param title
	 *            the wished document title
	 */
	public void setDocumentTitle(String title) {
		documentTitle = title;
	}

	/**
	 * Sets a name to your print job.
	 * 
	 * @param jobName
	 *            the wished print job name
	 */
	public void setJobName(String jobName) {
		printJob.setJobName(jobName);
	}

	public int print(Graphics g, PageFormat pf, int pageIndex)
			throws PrinterException {
		int result = PAGE_EXISTS;

		if (pageIndex >= pageCount) {
			result = NO_SUCH_PAGE;
		} else {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			if (ratio != 1) {
				g2d.scale(ratio, ratio);
			}

			int xPosition = 0;
			int xIndex = 0;
			int yPosition = 0;
			int yIndex = 0;

			xIndex = pageIndex % xCount;
			yIndex = pageIndex / xCount;

			if (xIndex > 0) {
				double pageWidth = g2d.getClip().getBounds().width;
				double cumulatedXMove = 0.0D;
				cumulatedXMove -= (xIndex * pageWidth);
				xPosition = (int) Math.rint(cumulatedXMove);
			}
			if (yIndex > 0) {
				double pageHeight = g2d.getClip().getBounds().height;
				double cumulatedYMove = 0.0D;
				cumulatedYMove -= (yIndex * pageHeight);
				yPosition = (int) Math.rint(cumulatedYMove);
			}
			g2d.drawImage(toPrint, xPosition, yPosition, null);

			// write the document information
			StringBuffer details = new StringBuffer(documentTitle);
			details.append(" - [").append(pageIndex + 1).append("/");
			details.append(pageCount).append("] (").append(date).append(") ");
			Font newPrintFont = new Font(g2d.getFont().getName(), g2d.getFont()
					.getStyle(), (int) Math.rint(g2d.getFont().getSize()
					/ ratio));
			g2d.setFont(newPrintFont);
			g2d.drawString(details.toString(), 0,
					g2d.getClip().getBounds().height
							- (int) Math.rint(5 / ratio));

			newPrintFont = null;
			details = null;
			g2d.dispose();
			g2d = null;
		}

		return result;
	}

	/**
	 * Call this method to print your Component
	 */
	public void print() {
		Color formerBackground = component.getBackground();
		pageFormat = printJob.validatePage(pageFormat);
		printJob.setPrintable(this, pageFormat);
		// printJob.setPageable(this); // not linux compatible
		if (pageCount == 0) {
			calculatePages();
		}
		try {
			// set some specific attributes
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			pras.add(new JobName(printJob.getJobName(), null));
			pras.add(MediaSizeName.ISO_A4);
			pras.add(new MediaPrintableArea(5, 5, 210 - 2 * 5, 297 - 2 * 5,
					MediaPrintableArea.MM));
			switch (getOrientation()) {
			case LANDSCAPE:
				pras.add(OrientationRequested.LANDSCAPE);
				break;

			case PORTRAIT:
				pras.add(OrientationRequested.PORTRAIT);
				break;
			}
			// pras.add(PrintQuality.NORMAL);
			// pras.add(new Copies(1));

			// if ( printJob.printDialog() ) {
			if (printJob.printDialog(pras)) {

				pageFormat = printJob.validatePage(pageFormat);
				calculatePages();
				date = new Timestamp(System.currentTimeMillis());
				component.setBackground(Color.WHITE);
				toPrint = new BufferedImage(component.getWidth(), component
						.getHeight(), BufferedImage.TYPE_INT_ARGB);
				component.printAll(toPrint.getGraphics());
				// printJob.print();
				printJob.print(pras);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			JOptionPane.showMessageDialog(component,
					"Error while printing document:\n" + t.getClass().getName()
							+ ":\n" + t.getMessage(), "Print Error",
					JOptionPane.ERROR_MESSAGE);
			printJob.cancel();
		} finally {
			date = null;
			if (toPrint != null) {
				toPrint.flush();
				toPrint = null;
			}
			component.setBackground(formerBackground);
			formerBackground = null;
		}
	}

	protected void calculatePages() {
		double pageHeight = pageFormat.getImageableHeight();
		double pageWidth = pageFormat.getImageableWidth();
		double documentWidth = component.getWidth();
		double documentHeight = component.getHeight();
		double scaleX = pageWidth / documentWidth;
		double scaleY = pageHeight / documentHeight;
		double documentScaledHeight;
		double documentScaledWidth;
		switch (fitMode) {
		case FIT_PAGE:
			ratio = Math.min(scaleX, scaleY);
			break;
		case FIT_PAGE_WIDTH:
			ratio = scaleX;
			break;
		case NO_FIT:
			ratio = 1;
			break;
		}
		documentScaledHeight = documentHeight * ratio;
		documentScaledWidth = documentWidth * ratio;
		if (documentScaledWidth > pageWidth) {
			xCount = (int) Math.ceil(documentScaledWidth / pageWidth);
		} else {
			xCount = 1;
		}
		if (documentScaledHeight > pageHeight) {
			yCount = (int) Math.ceil(documentScaledHeight / pageHeight);
		} else {
			yCount = 1;
		}
		pageCount = xCount * yCount;
	}

	public int getNumberOfPages() {
		if (pageCount == 0) {
			calculatePages();
		}
		return pageCount;
	}

	public PageFormat getPageFormat(int pageIndex)
			throws IndexOutOfBoundsException {
		return pageFormat;
	}

	public Printable getPrintable(int pageIndex)
			throws IndexOutOfBoundsException {
		return this;
	}

}
