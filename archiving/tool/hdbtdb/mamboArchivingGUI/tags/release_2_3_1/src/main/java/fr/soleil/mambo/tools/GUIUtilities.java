// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/GUIUtilities.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GUIUtilities.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.16 $
//
// $Log: GUIUtilities.java,v $
// Revision 1.16 2007/05/10 14:48:16 ounsy
// possibility to change "no value" String in chart data file (default is "*")
// through vc option
//
// Revision 1.15 2007/02/01 14:13:30 pierrejoseph
// XmlHelper reorg
//
// Revision 1.14 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.13 2007/01/10 14:28:18 ounsy
// minor changes (look&feel)
//
// Revision 1.12 2007/01/09 16:25:48 ounsy
// look & feel with "expand all" buttons in main frame
//
// Revision 1.11 2006/11/07 14:34:28 ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.10 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.9 2006/09/22 14:52:23 ounsy
// minor changes
//
// Revision 1.8 2006/09/20 12:50:29 ounsy
// changed imports
//
// Revision 1.7 2006/07/18 10:32:22 ounsy
// throwDevFailed method added
//
// Revision 1.6 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.5 2006/03/27 14:08:01 ounsy
// organized imports
//
// Revision 1.4 2006/02/24 12:24:26 ounsy
// the addDebugBorderToPanel method now accepts JComponents
//
// Revision 1.3 2005/12/15 11:49:00 ounsy
// "copy table to clipboard" management
//
// Revision 1.2 2005/11/29 18:28:26 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Condition;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class GUIUtilities {

    public static int trace_treshold = 9;
    // private static MessagesArea messagesArea = null;

    public static final String CRLF = System.getProperty("line.separator");
    public static final String TANGO_DELIM = "/";
    public static final String TANGO_JOKER = "*";
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    public final static Color fColor = new Color(99, 97, 156);
    public final static Color selectionColor = new Color(200, 180, 255);
    public final static Insets zInset = new Insets(0, 0, 0, 0);
    public final static Font labelFont = new Font("Dialog", Font.PLAIN, 12);
    public final static Font labeliFont = new Font("Dialog", Font.ITALIC, 12);
    public final static Font labelbFont = new Font("Dialog", Font.BOLD, 12);
    public final static Font expandButtonFont = new Font("Dialog", Font.BOLD | Font.ITALIC, 12);

    public final static int ARCHIVING_COLOR = 0;
    public final static int VIEW_COLOR = 1;
    public final static int MESSAGE_COLOR = 2;
    public final static int TOOLBAR_COLOR = 3;
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
     * @return 26 sept. 2005
     */
    public static Font getOptionsTitleFont() {
        return new Font(null, Font.BOLD, 12);
    }

    /**
     * @return 29 sept. 2005
     */
    public static Color getArchivingColor() {
        return archivingColor;
    }

    /**
     * @return 29 sept. 2005
     */
    public static Color getArchivingCopyColor() {
        return archivingCopyColor;
    }

    /**
     * @return 29 sept. 2005
     */
    public static Color getViewColor() {
        return viewColor;
    }

    /**
     * @return 29 sept. 2005
     */
    public static Color getViewCopyColor() {
        return viewCopyColor;
    }

    public static Color getViewSelectionColor() {
        return viewSelectionColor;
    }

    public static Color getMessageColor() {
        return messageColor;
    }

    public static Color getToolBarColor() {
        return toolBarColor;
    }

    /**
     * @param panel
     * @param context_color2 29 sept. 2005
     */
    public static void setObjectBackground(Component comp, int colorCase) {
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
     * @param pw
     * @param s
     * @param hasNewLine
     * @throws Exception 8 juil. 2005
     */
    public static void write2(PrintWriter pw, String s, boolean hasNewLine) throws Exception {
        if (hasNewLine) {
            pw.println(s);
        }
        else {
            pw.print(s);
        }

        pw.flush();
    }

    /**
     * @param panelToDebug
     * @param debug
     * @param borderColor
     * @param thickness 8 juil. 2005
     */
    public static void addDebugBorderToPanel(JComponent panelToDebug, boolean debug,
            Color borderColor, int thickness) {
        if (debug) {
            Border border = (Border) (BorderFactory.createLineBorder(borderColor, thickness));
            panelToDebug.setBorder(border);
        }
    }

    /**
     * @return 8 juil. 2005
     */
    public static Font getTitleFont() {
        return new Font(null, Font.BOLD, 12);
    }

    /**
     * @return 27 juil. 2005
     */
    public static Timestamp now() {
        long _now = System.currentTimeMillis();
        return new Timestamp(_now);
    }

    /**
     * @return 29 juil. 2005
     */
    public static TitledBorder getPlotSubPanelsEtchedBorder(String name) {
        Font labelbFont = new Font("Dialog", Font.BOLD, 12);
        Color fColor = new Color(99, 97, 156);

        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), name,
                TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, fColor);
    }

    public static TitledBorder getPlotSubPanelsEtchedBorder(String name, Color color) {
        Font labelbFont = new Font("Dialog", Font.BOLD, 12);

        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), name,
                TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, color);
    }

    public static TitledBorder getPlotSubPanelsLineBorder(String name) {
        Font labelbFont = new Font("Dialog", Font.BOLD, 12);
        Color fColor = new Color(99, 97, 156);

        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(fColor, 1), name,
                TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, fColor);
    }

    public static TitledBorder getPlotSubPanelsLineBorder(String name, Color lineColor) {
        Font labelbFont = new Font("Dialog", Font.BOLD, 12);
        Color fColor = new Color(99, 97, 156);

        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(lineColor, 1), name,
                TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, fColor);
    }

    public static TitledBorder getPlotSubPanelsLineBorder(String name, Color textColor,
            Color lineColor) {
        Font labelbFont = new Font("Dialog", Font.BOLD, 12);

        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(lineColor, 1), name,
                TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION, labelbFont, textColor);
    }

    /**
     * @param color
     * @return
     */
    public static String colorToString(Color color) {
        if (color == null) {
            return null;
        }
        else {
            String ret = "";
            ret += color.getRed() + ",";
            ret += color.getGreen() + ",";
            ret += color.getBlue() + ",";
            return ret;
        }

    }

    /**
     * @param font
     * @return
     */
    public static String fontToString(Font font) {
        if (font == null) {
            return null;
        }
        else {
            String ret = "";

            ret += font.getName() + ",";
            ret += font.getStyle() + ",";
            ret += font.getSize();
            return ret;
        }
    }

    /**
     * @param in
     * @return
     */
    public static Color StringToColor(String in) {
        try {
            StringTokenizer st = new StringTokenizer(in, ",");

            String r_s = st.nextToken();
            String g_s = st.nextToken();
            String b_s = st.nextToken();

            int r = Integer.parseInt(r_s);
            int g = Integer.parseInt(g_s);
            int b = Integer.parseInt(b_s);

            return new Color(r, g, b);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * @param in
     * @return
     */
    public static boolean StringToBoolean(String in) {
        try {
            return "true".equalsIgnoreCase(in);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * @param in
     * @return
     */
    public static double StringToDouble(String in) {
        try {
            return Double.parseDouble(in);
        }
        catch (Exception e) {
            return -1;
        }
    }

    /**
     * @param in
     * @return
     */
    public static int StringToInt(String in) {
        try {
            return Integer.parseInt(in);
        }
        catch (Exception e) {
            return -1;
        }
    }

    /**
     * @param in
     * @return
     */
    public static long StringToLong(String in) {
        try {
            return Long.parseLong(in);
        }
        catch (Exception e) {
            return -1;
        }
    }

    /**
     * @param in
     * @return
     */
    public static Font StringToFont(String in) {
        try {
            StringTokenizer st = new StringTokenizer(in, ",");

            String name = st.nextToken();
            String style_s = st.nextToken();
            String size_s = st.nextToken();

            int style = Integer.parseInt(style_s);
            int size = Integer.parseInt(size_s);

            return new Font(name, style, size);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * @param content
     * @return 31 ao�t 2005
     */
    public static Vector<String> getContentParts(String content) {
        Vector<String> ret = new Vector<String>();

        StringTokenizer st = new StringTokenizer(content, CRLF, true);
        // XMLLine line =
        new XMLLine(ArchivingConfigurationAttribute.XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

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
     * @param attrs
     * @param attrsToRemove 14 sept. 2005
     */
    public static void removeVectorElements(Map<?, ?> attrs, Vector<?> attrsToRemove) {
        Enumeration<?> e = attrsToRemove.elements();
        while (e.hasMoreElements()) {
            attrs.remove(e.nextElement());
        }
    }

    /**
     * Replaces all occurences of <CODE>toReplace</CODE> in <CODE>in</CODE> by
     * <CODE>replacement</CODE>.
     * 
     * @param in The string to alter.
     * @param toReplace The string to replace.
     * @param replacement The replacement string.
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
        }
        while (startIdx >= 0);

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
        }
        while (listIdx >= 0);

        return finalString.toString();
    }

    /**
     * @param exception
     * @return
     * @throws DevFailed
     */
    public static void throwDevFailed(Throwable exception) throws DevFailed {
        DevFailed devFailed = new DevFailed();
        devFailed.initCause(exception);
        throw devFailed;
    }

    /**
     * @param pattern
     * @return 11 juil. 2005
     */
    public static Criterions getAttributesSearchCriterions(String pattern) {
        Criterions ret = new Criterions();
        Condition cond;

        StringTokenizer st = new StringTokenizer(pattern, GUIUtilities.TANGO_DELIM);

        String domain = GUIUtilities.TANGO_JOKER;
        String family = GUIUtilities.TANGO_JOKER;
        String member = GUIUtilities.TANGO_JOKER;
        String attribute = GUIUtilities.TANGO_JOKER;

        try {
            domain = st.nextToken();
            family = st.nextToken();
            member = st.nextToken();
            attribute = st.nextToken();
        }
        catch (NoSuchElementException e) {
            // do nothing
        }

        cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_DOMAIN, ITangoManager.OP_EQUALS, domain);
        ret.addCondition(cond);

        cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_FAMILY, ITangoManager.OP_EQUALS, family);
        ret.addCondition(cond);

        cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_MEMBER, ITangoManager.OP_EQUALS, member);
        ret.addCondition(cond);

        cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_NAME, ITangoManager.OP_EQUALS, attribute);
        ret.addCondition(cond);

        return ret;
    }

    public static Font getExpressionTitleFont() {
        return expressionTitleFont;
    }

    public static Color getExpressionTitleColor() {
        return expressionTitleColor;
    }

    public static Dimension getEditScrollPaneSize() {
        return editScrollPaneSize;
    }

    public static Dimension getViewLabelSize() {
        return viewLabelSize;
    }
}
