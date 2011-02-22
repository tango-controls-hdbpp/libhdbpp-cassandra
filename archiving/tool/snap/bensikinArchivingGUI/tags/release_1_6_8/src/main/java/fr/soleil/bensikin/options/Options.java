// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/Options.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Options.
// (Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: Options.java,v $
// Revision 1.5 2005/12/14 16:46:02 ounsy
// added methods necessary for alternate attribute selection
//
// Revision 1.4 2005/11/29 18:25:27 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:41 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.options;

import java.util.ArrayList;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.options.sub.ContextOptions;
import fr.soleil.bensikin.options.sub.DisplayOptions;
import fr.soleil.bensikin.options.sub.LogsOptions;
import fr.soleil.bensikin.options.sub.PrintOptions;
import fr.soleil.bensikin.options.sub.SaveOptions;
import fr.soleil.bensikin.options.sub.SnapshotOptions;
import fr.soleil.bensikin.options.sub.WordlistOptions;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * The model for the application's options, this class is a singleton. Contains
 * all the options that can be saved/loaded in the life cycle.
 * 
 * @author CLAISSE
 */
public class Options {

	private DisplayOptions displayOptions;
	private PrintOptions printOptions;
	private LogsOptions logsOptions;
	private SaveOptions saveOptions;
	private WordlistOptions wordlistOptions;
	private SnapshotOptions snapshotOptions;
	private ContextOptions contextOptions;

	private static Options optionsInstance = null;

	public static final String OPTIONS_TAG = "options";

	/**
	 * Default constructor Display, print, logs , save, wordlist, and snapshot
	 * options are initialized.
	 */
	private Options() {
		displayOptions = new DisplayOptions();
		printOptions = new PrintOptions();
		logsOptions = new LogsOptions();
		saveOptions = new SaveOptions();
		wordlistOptions = new WordlistOptions();
		snapshotOptions = new SnapshotOptions();
		contextOptions = new ContextOptions();
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static Options getInstance() {
		if (optionsInstance == null) {
			optionsInstance = new Options();
		}

		return optionsInstance;
	}

	/**
	 * Delegates push to each option part: Display, print, logs , save,
	 * wordlist, and snapshot options are pushed.
	 * 
	 * @throws Exception
	 */
	public void push() throws Exception {
		StringBuffer errorBuffer = new StringBuffer();
		ArrayList<StackTraceElement> errorStack = new ArrayList<StackTraceElement>();
		try {
			this.displayOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
		try {
			this.printOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
		try {
			this.logsOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
		try {
			this.saveOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
		try {
			this.wordlistOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
		try {
			this.snapshotOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
		try {
			this.contextOptions.push();
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				errorBuffer.append(e.getMessage());
			}
			StackTraceElement[] stack = e.getStackTrace();
			if (stack != null) {
				for (int i = 0; i < stack.length; i++) {
					errorStack.add(stack[i]);
				}
			}
			stack = null;
		}
	}

	/**
	 * Returns a XML representation of the options
	 * 
	 * @return a XML representation of the options
	 */
	public String toString() {
		String ret = "";

		XMLLine openingLine = new XMLLine(OPTIONS_TAG,
				XMLLine.OPENING_TAG_CATEGORY);
		ret += openingLine.toString();
		ret += GUIUtilities.CRLF;

		ret += displayOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += printOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += logsOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += saveOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += wordlistOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += snapshotOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += contextOptions.toString();
		ret += GUIUtilities.CRLF;

		XMLLine closingLine = new XMLLine(OPTIONS_TAG,
				XMLLine.CLOSING_TAG_CATEGORY);
		ret += closingLine.toString();

		return ret;
	}

	/**
	 * Forces the instance of Options to this new value
	 * 
	 * @param _optionsInstance
	 */
	public static void setOptionsInstance(Options _optionsInstance) {
		Options.optionsInstance = _optionsInstance;
	}

	/**
	 * Fills itself using the current parameters of the Options dialog
	 */
	public void fillFromOptionsDialog() {
		this.displayOptions.fillFromOptionsDialog();
		this.printOptions.fillFromOptionsDialog();
		this.logsOptions.fillFromOptionsDialog();
		this.saveOptions.fillFromOptionsDialog();
		this.wordlistOptions.fillFromOptionsDialog();
		this.snapshotOptions.fillFromOptionsDialog();
		this.contextOptions.fillFromOptionsDialog();
	}

	/**
	 * @return Returns the snapshotOptions.
	 */
	public SnapshotOptions getSnapshotOptions() {
		return snapshotOptions;
	}

	/**
	 * @param _snapshotOptions
	 *            The _snapshotOptions to set.
	 */
	public void seSnapshotOptions(SnapshotOptions _snapshotOptions) {
		this.snapshotOptions = _snapshotOptions;
	}

	/**
	 * @return Returns the displayOptions.
	 */
	public DisplayOptions getDisplayOptions() {
		return displayOptions;
	}

	/**
	 * @param displayOptions
	 *            The displayOptions to set.
	 */
	public void setDisplayOptions(DisplayOptions displayOptions) {
		this.displayOptions = displayOptions;
	}

	/**
	 * @return Returns the logsOptions.
	 */
	public LogsOptions getLogsOptions() {
		return logsOptions;
	}

	/**
	 * @param logsOptions
	 *            The logsOptions to set.
	 */
	public void setLogsOptions(LogsOptions logsOptions) {
		this.logsOptions = logsOptions;
	}

	/**
	 * @return Returns the printOptions.
	 */
	public PrintOptions getPrintOptions() {
		return printOptions;
	}

	/**
	 * @param printOptions
	 *            The printOptions to set.
	 */
	public void setPrintOptions(PrintOptions printOptions) {
		this.printOptions = printOptions;
	}

	/**
	 * @return Returns the saveOptions.
	 */
	public SaveOptions getSaveOptions() {
		return saveOptions;
	}

	/**
	 * @param saveOptions
	 *            The saveOptions to set.
	 */
	public void setSaveOptions(SaveOptions saveOptions) {
		this.saveOptions = saveOptions;
	}

	/**
	 * @return Returns the wordlistOptions.
	 */
	public WordlistOptions getWordlistOptions() {
		return wordlistOptions;
	}

	/**
	 * @param wordlistOptions
	 *            The wordlistOptions to set.
	 */
	public void setWordlistOptions(WordlistOptions wordlistOptions) {
		this.wordlistOptions = wordlistOptions;
	}

	/**
	 * @return Returns the optionsInstance.
	 */
	public static Options getOptionsInstance() {
		return optionsInstance;
	}

	/**
	 * @param snapshotOptions
	 *            The snapshotOptions to set.
	 */
	public void setSnapshotOptions(SnapshotOptions snapshotOptions) {
		this.snapshotOptions = snapshotOptions;
	}

	/**
	 * @return Returns the contextOptions.
	 */
	public ContextOptions getContextOptions() {
		return contextOptions;
	}

	/**
	 * @param contextOptions
	 *            The contextOptions to set.
	 */
	public void setContextOptions(ContextOptions contextOptions) {
		this.contextOptions = contextOptions;
	}
}
