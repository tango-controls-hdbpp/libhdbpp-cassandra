//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  GUIUtilities.
//						(Claisse Laurent) - 13 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.1  2010/11/10 12:54:00  abeilleg
// first import
//
// Revision 1.5  2007/08/23 15:28:48  ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.4  2007/08/23 13:00:37  ounsy
// minor changes
//
// Revision 1.3  2006/06/28 12:54:28  ounsy
// minor changes
//
// Revision 1.2  2005/12/14 16:48:41  ounsy
// added methods necessary for direct clipboard edition
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:32  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.archiving.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import fr.esrf.Tango.DevFailed;

/**
 * The attic class of the application, with various static methods that don't
 * fit anywhere else
 * 
 * @author CLAISSE
 */
public class GUIUtilities {
    // private static MessagesArea messagesArea = null;

    public final static String WORKING_DIR = "fr.soleil.archiving.directory";

    /**
     * A platform-independant "Carriage return/line feed"
     */
    public static final String CRLF = System.getProperty("line.separator");
    /**
     * The delimitor used for tango paths
     */
    public static final String TANGO_DELIM = "/";
    /**
     * The joker used for tango paths
     */
    public static final String TANGO_JOKER = "*";
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public final static Color fColor = new Color(99, 97, 156);
    public final static Color selectionColor = new Color(200, 180, 255);
    public final static Insets zInset = new Insets(0, 0, 0, 0);
    public final static Font labelFont = new Font("Dialog", Font.PLAIN, 12);
    public final static Font labeliFont = new Font("Dialog", Font.ITALIC, 12);
    public final static Font labelbFont = new Font("Dialog", Font.BOLD, 12);
    public final static Font expandButtonFont = new Font("Dialog", Font.BOLD | Font.ITALIC, 12);

    public static final String CSV_SEPARATOR = ";";

    public final static int ARCHIVING_COLOR = 0;
    public final static int VIEW_COLOR = 1;
    public final static int ARCHIVING_COPY_COLOR = 5;
    public final static int VIEW_COPY_COLOR = 6;

    public static boolean hasColor = true;

    private final static Color archivingColor = new Color(210, 190, 190);
    private final static Color archivingCopyColor = new Color(255, 150, 255);
    private final static Color viewColor = new Color(180, 200, 200);
    private final static Color viewSelectionColor = new Color(200, 240, 255);
    private final static Color viewCopyColor = new Color(150, 255, 255);
    private final static Color messageColor = Color.gray;
    private final static Color toolBarColor = Color.lightGray;
    private final static Color expressionTitleColor = Color.BLUE;

    private final static Font expressionTitleFont = new Font("Times New Roman", Font.BOLD, 20);

    private final static Dimension editScrollPaneSize = new Dimension(700, 600);
    private static Dimension viewLabelSize = new Dimension(95, 20);

    /**
     * Paint a container's background with the default color defined for
     * contexts
     */
    public static final int CONTEXT_COLOR = 0;
    /**
     * Paint a container's background with the default color defined for
     * snapshots
     */
    public static final int SNAPSHOT_COLOR = 1;
    /**
     * Paint a container's background with the default color defined for
     * messages
     */
    public static final int MESSAGE_COLOR = 2;
    /**
     * Paint a container's background with the default color defined for the
     * toolbar
     */
    public static final int TOOLBAR_COLOR = 3;
    /**
     * Paint a container's background with the default color defined for
     * profiles
     */
    public static final int PROFILE_COLOR = 4;

    /**
     * Paint a container's background with the default color defined for
     * snapshot clipboard buttons
     */
    public static final int SNAPSHOT_CLIPBOARD_COLOR = 5;

    /**
     * Paint a container's background with the default color defined for select
     * buttons
     */
    public static final int SELECT_COLOR = 6;

    /**
     * Paint a container's background with the default color defined for context
     * clipboard buttons
     */
    public static final int CONTEXT_CLIPBOARD_COLOR = 7;

    /**
     * @param color
     * @return
     */
    public static String colorToString(final Color color) {
	if (color == null) {
	    return null;
	} else {
	    String ret = "";
	    ret += color.getRed() + ",";
	    ret += color.getGreen() + ",";
	    ret += color.getBlue() + ",";
	    return ret;
	}

    }

    /**
     * @return 27 juil. 2005
     */
    public static Timestamp now() {
	final long _now = System.currentTimeMillis();
	return new Timestamp(_now);
    }

    public static Color getViewColor() {
	return viewColor;
    }

    public static Color getViewCopyColor() {
	return viewCopyColor;
    }

    public static Color getViewSelectionColor() {
	return viewSelectionColor;
    }

    public static void throwDevFailed(final Throwable exception) throws DevFailed {
	final DevFailed devFailed = new DevFailed();
	devFailed.initCause(exception);
	throw devFailed;
    }

    /**
     * Writes content to an existing BufferedWriter
     * 
     * @param bw
     *            The BufferedWriter to write to
     * @param s
     *            The content to write
     * @param hasNewLine
     *            True if a carriage return has to be inserted at the end of the
     *            line
     * @throws Exception
     */
    public static void write(final BufferedWriter bw, final String s, final boolean hasNewLine)
	    throws Exception {
	bw.write(s, 0, s.length());
	if (hasNewLine) {
	    bw.newLine();
	}
    }

    /**
     * Writes content to an existing PrintWriter
     * 
     * @param pw
     *            The PrintWriter to write to
     * @param s
     *            The content to write
     * @param hasNewLine
     *            True if a carriage return has to be inserted at the end of the
     *            line
     * @throws Exception
     */
    public static void write2(final PrintWriter pw, final String s, final boolean hasNewLine)
	    throws Exception {
	final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss:SSS");
	final String msg = dateFormat.format(new Date()) + s;
	if (hasNewLine) {
	    pw.println(msg);
	} else {
	    pw.print(msg);
	}
	pw.flush();
    }

    /**
     * Adds a colored border to a container.
     * 
     * @param panelToDebug
     *            The panel to add the colored border to
     * @param debug
     *            If false, the methods does nothing
     * @param borderColor
     *            The desired border color
     * @param thickness
     *            The desired border thickness
     */
    public static void addDebugBorderToPanel(final JPanel panelToDebug, final boolean debug,
	    final Color borderColor, final int thickness) {
	if (debug) {
	    final Border border = BorderFactory.createLineBorder(borderColor, thickness);
	    panelToDebug.setBorder(border);
	}
    }

    /**
     * Returns the font used for title borders.
     * 
     * @return The font used for title borders
     */
    public static Font getTitleFont() {
	return new Font(null, Font.BOLD, 12);
    }

    /**
     * Divides a String content into smaller parts (about 100 lines), and puts
     * those parts into a Vector.
     * 
     * @param content
     *            The content to divide
     * @return A Vector containing small content parts
     */
    public static Vector getContentParts(final String content) {
	final Vector<String> ret = new Vector<String>();

	final StringTokenizer st = new StringTokenizer(content, CRLF, true);

	StringBuffer buffer = new StringBuffer();
	final int CRLF_COUNTER_MAX = 100;
	int crlfCounter = 0;
	int totalTokensCount = 0;
	final int countTokens = st.countTokens();

	while (st.hasMoreTokens()) {
	    final String next = st.nextToken();
	    crlfCounter++;
	    totalTokensCount++;

	    buffer.append(next);

	    final boolean isLastToken = totalTokensCount == countTokens;
	    if (crlfCounter == CRLF_COUNTER_MAX || isLastToken) {
		final String toAdd = buffer.toString();
		ret.add(toAdd);

		buffer = new StringBuffer();
		crlfCounter = 0;
	    }
	}

	return ret;
    }

    /**
     * Replaces all occurences of <CODE>toReplace</CODE> in <CODE>in</CODE> by
     * <CODE>replacement</CODE>.
     * 
     * @param in
     *            The string to alter.
     * @param toReplace
     *            The string to replace.
     * @param replacement
     *            The replacement string.
     * @return The altered string.
     */
    public static String replace(final String in, final String toReplace, final String replacement) {
	final int lgDelim = toReplace.length();
	final ArrayList<Integer> limitersList = new ArrayList<Integer>();
	StringBuffer finalString;

	int startIdx;
	int listIdx;

	// Looking for the string to replace
	startIdx = 0;
	do {
	    startIdx = in.indexOf(toReplace, startIdx);

	    if (startIdx >= 0) {
		limitersList.add(new Integer(startIdx));
		startIdx += lgDelim;
	    }
	} while (startIdx >= 0);

	// Check if there is something to do
	if (limitersList.size() == 0) {
	    return in;
	}

	// Backwards replace
	finalString = new StringBuffer(in);
	listIdx = limitersList.size() - 1;

	do {
	    startIdx = limitersList.get(listIdx--).intValue();
	    finalString.replace(startIdx, startIdx + lgDelim, replacement);
	} while (listIdx >= 0);

	return finalString.toString();
    }

    /**
     * Returns the font used for title borders in the options dialog's snapshots
     * tab.
     * 
     * @return The font used for title borders in the options dialog's snapshots
     *         tab
     */
    public static Font getOptionsTitleFont() {
	return new Font(null, Font.BOLD, 12);
    }

    /**
     * Returns the background color used for contexts.
     * 
     * @return The background color used for contexts
     */
    public static Color getContextColor() {
	return new Color(210, 190, 190);
    }

    /**
     * Returns the background color used for snapshots.
     * 
     * @return The background color used for snapshots
     */
    public static Color getSnapshotColor() {
	return new Color(180, 200, 200);
    }

    /**
     * Returns the background color used for messages.
     * 
     * @return The background color used for messages
     */
    public static Color getMessageColor() {
	// return new Color(170, 170, 200);
	return Color.gray;
    }

    /**
     * Returns the background color used for the tool bar.
     * 
     * @return The background color used for the tool bar
     */
    public static Color getToolBarColor() {
	// return new Color(170, 170, 200);
	return Color.lightGray;
    }

    /**
     * Returns the background color used for profiles.
     * 
     * @return The background color used for profiles
     */
    public static Color getProfileColor() {
	return new Color(192, 192, 255);
    }

    public static Color getSnapshotClipboardColor() {
	return new Color(0, 204, 255);
    }

    public static Color getContextClipboardColor() {
	return new Color(255, 0, 204);
    }

    public static Color getSelectColor() {
	return new Color(255, 153, 204);
    }

    public static Color getArchivingColor() {
	return archivingColor;
    }

    public static Color getArchivingCopyColor() {
	return archivingCopyColor;
    }

    public static Dimension getEditScrollPaneSize() {
	return editScrollPaneSize;
    }

    public static TitledBorder getPlotSubPanelsEtchedBorder(final String name) {
	final Font labelbFont = new Font("Dialog", Font.BOLD, 12);
	final Color fColor = new Color(99, 97, 156);

	return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), name,
		TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, fColor);
    }

    public static TitledBorder getPlotSubPanelsEtchedBorder(final String name, final Color color) {
	final Font labelbFont = new Font("Dialog", Font.BOLD, 12);

	return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), name,
		TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, color);
    }

    public static TitledBorder getPlotSubPanelsLineBorder(final String name) {
	final Font labelbFont = new Font("Dialog", Font.BOLD, 12);
	final Color fColor = new Color(99, 97, 156);

	return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(fColor, 1), name,
		TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, fColor);
    }

    public static TitledBorder getPlotSubPanelsLineBorder(final String name, final Color lineColor) {
	final Font labelbFont = new Font("Dialog", Font.BOLD, 12);
	final Color fColor = new Color(99, 97, 156);

	return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(lineColor, 1), name,
		TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, fColor);
    }

    public static TitledBorder getPlotSubPanelsLineBorder(final String name, final Color textColor,
	    final Color lineColor) {
	final Font labelbFont = new Font("Dialog", Font.BOLD, 12);

	return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(lineColor, 1), name,
		TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, textColor);
    }

    public static Dimension getViewLabelSize() {
	return viewLabelSize;
    }

    public static String fontToString(final Font font) {
	if (font == null) {
	    return null;
	} else {
	    String ret = "";

	    ret += font.getName() + ",";
	    ret += font.getStyle() + ",";
	    ret += font.getSize();
	    return ret;
	}
    }

    public static void removeVectorElements(final Map<?, ?> attrs, final Vector<?> attrsToRemove) {
	final Enumeration<?> e = attrsToRemove.elements();
	while (e.hasMoreElements()) {
	    attrs.remove(e.nextElement());
	}
    }

    /**
     * @param in
     * @return
     */
    public static boolean StringToBoolean(final String in) {
	try {
	    return "true".equalsIgnoreCase(in);
	} catch (final Exception e) {
	    return false;
	}
    }

    /**
     * @param in
     * @return
     */
    public static double StringToDouble(final String in) {
	try {
	    return Double.parseDouble(in);
	} catch (final Exception e) {
	    return -1;
	}
    }

    /**
     * @param in
     * @return
     */
    public static int StringToInt(final String in) {
	try {
	    return Integer.parseInt(in);
	} catch (final Exception e) {
	    return -1;
	}
    }

    /**
     * @param in
     * @return
     */
    public static long StringToLong(final String in) {
	try {
	    return Long.parseLong(in);
	} catch (final Exception e) {
	    return -1;
	}
    }

    /**
     * @param in
     * @return
     */
    public static Font StringToFont(final String in) {
	try {
	    final StringTokenizer st = new StringTokenizer(in, ",");

	    final String name = st.nextToken();
	    final String style_s = st.nextToken();
	    final String size_s = st.nextToken();

	    final int style = Integer.parseInt(style_s);
	    final int size = Integer.parseInt(size_s);

	    return new Font(name, style, size);
	} catch (final Exception e) {
	    return null;
	}
    }

    /**
     * @param in
     * @return
     */
    public static Color StringToColor(final String in) {
	try {
	    final StringTokenizer st = new StringTokenizer(in, ",");

	    final String r_s = st.nextToken();
	    final String g_s = st.nextToken();
	    final String b_s = st.nextToken();

	    final int r = Integer.parseInt(r_s);
	    final int g = Integer.parseInt(g_s);
	    final int b = Integer.parseInt(b_s);

	    return new Color(r, g, b);
	} catch (final Exception e) {
	    return null;
	}
    }

    /**
     * Sets the background color of <CODE>panel</CODE>
     * 
     * @param panel
     *            The panel to set
     * @param colorCase
     *            The color type; can be any of (CONTEXT_COLOR, SNAPSHOT_COLOR,
     *            MESSAGE_COLOR, TOOLBAR_COLOR)
     */
    public static void setObjectBackground(final JComponent panel, final int colorCase) {
	if (!hasColor) {
	    return;
	}

	switch (colorCase) {
	case CONTEXT_COLOR:
	    panel.setBackground(getContextColor());
	    break;

	case SNAPSHOT_COLOR:
	    panel.setBackground(getSnapshotColor());
	    break;

	case MESSAGE_COLOR:
	    panel.setBackground(getMessageColor());
	    break;

	case TOOLBAR_COLOR:
	    panel.setBackground(getToolBarColor());
	    break;

	case PROFILE_COLOR:
	    panel.setBackground(getProfileColor());
	    break;

	case SNAPSHOT_CLIPBOARD_COLOR:
	    panel.setBackground(getSnapshotClipboardColor());
	    break;

	case SELECT_COLOR:
	    panel.setBackground(getSelectColor());
	    break;

	case CONTEXT_CLIPBOARD_COLOR:
	    panel.setBackground(getContextClipboardColor());
	    break;
	}
    }

    /**
     * @param panel
     * @param context_color2
     *            29 sept. 2005
     */
    public static void setObjectBackground(final Component comp, final int colorCase) {
	if (!hasColor) {
	    return;
	}

	switch (colorCase) {
	case ARCHIVING_COLOR:
	    comp.setBackground(getArchivingColor());
	    break;

	case VIEW_COLOR:
	    comp.setBackground(getViewColor());
	    break;

	case MESSAGE_COLOR:
	    comp.setBackground(getMessageColor());
	    break;

	case TOOLBAR_COLOR:
	    comp.setBackground(getToolBarColor());
	    break;

	case ARCHIVING_COPY_COLOR:
	    comp.setBackground(getArchivingCopyColor());
	    break;

	case VIEW_COPY_COLOR:
	    comp.setBackground(getViewCopyColor());
	    break;
	}
    }

    /**
     * @param string
     */
    public static void setClipboardContent(final String string) {
	final Toolkit toolkit = Toolkit.getDefaultToolkit();
	final Clipboard clipboard = toolkit.getSystemClipboard();
	final StringSelection stringSelection = new StringSelection(string);
	clipboard.setContents(stringSelection, stringSelection);
    }

}
