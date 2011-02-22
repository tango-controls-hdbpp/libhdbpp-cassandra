//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/tools/GUIUtilities.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  GUIUtilities.
//						(Claisse Laurent) - 13 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: GUIUtilities.java,v $
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
package fr.soleil.bensikin.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * The attic class of the application, with various static methods that don't
 * fit anywhere else
 * 
 * @author CLAISSE
 */
public class GUIUtilities {
	// private static MessagesArea messagesArea = null;

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

	public static final String CSV_SEPARATOR = ";";

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

	private static boolean hasColor = true;

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
	public static void write(BufferedWriter bw, String s, boolean hasNewLine)
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
	public static void write2(PrintWriter pw, String s, boolean hasNewLine)
			throws Exception {
		if (hasNewLine) {
			pw.println(s);
		} else {
			pw.print(s);
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
	public static void addDebugBorderToPanel(JPanel panelToDebug,
			boolean debug, Color borderColor, int thickness) {
		if (debug) {
			Border border = (Border) (BorderFactory.createLineBorder(
					borderColor, thickness));
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
	public static Vector getContentParts(String content) {
		Vector<String> ret = new Vector<String>();

		StringTokenizer st = new StringTokenizer(content, CRLF, true);

		StringBuffer buffer = new StringBuffer();
		int CRLF_COUNTER_MAX = 100;
		int crlfCounter = 0;
		int totalTokensCount = 0;
		int countTokens = st.countTokens();

		while (st.hasMoreTokens()) {
			String next = st.nextToken();
			crlfCounter++;
			totalTokensCount++;

			buffer.append(next);

			boolean isLastToken = (totalTokensCount == countTokens);
			if (crlfCounter == CRLF_COUNTER_MAX || isLastToken) {
				String toAdd = buffer.toString();
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
	public static String replace(String in, String toReplace, String replacement) {
		int lgDelim = toReplace.length();
		ArrayList<Integer> limitersList = new ArrayList<Integer>();
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

	/**
	 * Sets the background color of <CODE>panel</CODE>
	 * 
	 * @param panel
	 *            The panel to set
	 * @param colorCase
	 *            The color type; can be any of (CONTEXT_COLOR, SNAPSHOT_COLOR,
	 *            MESSAGE_COLOR, TOOLBAR_COLOR)
	 */
	public static void setObjectBackground(JComponent panel, int colorCase) {
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
	 * @param in
	 * @return
	 */
	public static boolean StringToBoolean(String in) {
		try {
			Boolean b = Boolean.valueOf(in);
			return b.booleanValue();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param string
	 */
	public static void setClipboardContent(String string) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection stringSelection = new StringSelection(string);
		clipboard.setContents(stringSelection, stringSelection);
	}

}
