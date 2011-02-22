//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/messages/MessagesArea.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MessagesArea.
//						(Claisse Laurent) - 13 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: MessagesArea.java,v $
// Revision 1.5  2005/11/29 18:25:27  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.messages;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.SearchPredicate;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * A JTextArea component with preset rows and columns
 * 
 * @author CLAISSE
 */
@SuppressWarnings("serial")
public class MessagesArea extends JXTable {

    /**
     * Default constructor, sets up rows and columns
     */
    public MessagesArea() {
	// super(MessagesArea.ROWS, MessagesArea.COLUMNS);
	// setLineWrap(true);
	// super(1000, 1);
	super(new DefaultTableModel(new String[] { "logs" }, 0));
	setEditable(false);
	setAutoResizeMode(JXTable.AUTO_RESIZE_ALL_COLUMNS);
	// setRolloverEnabled(true);
	// setColumnControlVisible(true);
	// setAutoscrolls(true);
	// setShowHorizontalLines(true);
	// setPreferredScrollableViewportSize(new Dimension(400, 200));
	getModel().addTableModelListener(new TableModelListener() {
	    public void tableChanged(final TableModelEvent e) {
		if (e.getType() == TableModelEvent.INSERT) {
		    SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    scrollRectToVisible(getCellRect(e.getLastRow(), 0, false));
			}
		    });

		}
	    }
	});

	// add a default highligther for errors
	// new Color(1f, 0.2f, 0.2f, 1f)
	final ColorHighlighter errorHighlight = new ColorHighlighter(HighlightPredicate.ALWAYS,
		Color.RED, null);
	final HighlightPredicate error = new SearchPredicate(Pattern.compile(ILogger.ERROR + "|"
		+ ILogger.CRITIC));
	errorHighlight.setHighlightPredicate(error);
	addHighlighter(errorHighlight);

	final ColorHighlighter warnHighlight = new ColorHighlighter(HighlightPredicate.ALWAYS,
		Color.ORANGE, null);
	final HighlightPredicate warn = new SearchPredicate(Pattern.compile(ILogger.WARNING));
	warnHighlight.setHighlightPredicate(warn);
	addHighlighter(warnHighlight);
    }

    public void addTrace(final String trace) {
	final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss:SSS");
	((DefaultTableModel) getModel()).addRow(new Object[] { dateFormat.format(new Date())
		+ " - " + trace });
    }

    @Override
    public Component prepareRenderer(final TableCellRenderer renderer, final int rowIndex,
	    final int vColIndex) {
	final Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
	if (c instanceof JComponent) {
	    final JComponent jc = (JComponent) c;
	    jc.setToolTipText((String) getValueAt(rowIndex, vColIndex));
	}
	return c;
    }

}
