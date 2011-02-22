//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/Options.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Options.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: Options.java,v $
// Revision 1.5  2007/02/01 14:14:09  pierrejoseph
// XmlHelper reorg
//
// Revision 1.4  2006/03/29 10:28:03  ounsy
// removed useless "throws Exception "
//
// Revision 1.3  2005/12/15 11:44:47  ounsy
// "copy table to clipboard" management
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options;

import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.options.sub.DisplayOptions;
import fr.soleil.mambo.options.sub.GeneralOptions;
import fr.soleil.mambo.options.sub.LogsOptions;
import fr.soleil.mambo.options.sub.PrintOptions;
import fr.soleil.mambo.options.sub.SaveOptions;
import fr.soleil.mambo.options.sub.VCOptions;
import fr.soleil.mambo.options.sub.WordlistOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Options {
	private DisplayOptions displayOptions;
	private PrintOptions printOptions;
	private LogsOptions logsOptions;
	private SaveOptions saveOptions;
	private WordlistOptions wordlistOptions;
	private GeneralOptions generalOptions;
	private ACOptions acOptions;
	private VCOptions vcOptions;

	private static Options optionsInstance = null;

	public static final String OPTIONS_TAG = "options";

	/**
     * 
     */
	private Options() {
		displayOptions = new DisplayOptions();
		printOptions = new PrintOptions();
		logsOptions = new LogsOptions();
		saveOptions = new SaveOptions();
		wordlistOptions = new WordlistOptions();
		generalOptions = new GeneralOptions();
		acOptions = new ACOptions();
		vcOptions = new VCOptions();
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static Options getInstance() {
		if (optionsInstance == null) {
			optionsInstance = new Options();
		}

		return optionsInstance;
	}

	/**
	 * @throws Exception
	 *             8 juil. 2005
	 */
	public void push()// throws Exception
	{
		this.displayOptions.push();
		this.printOptions.push();
		this.logsOptions.push();
		this.saveOptions.push();
		this.wordlistOptions.push();
		this.generalOptions.push();
		this.acOptions.push();
		this.vcOptions.push();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
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

		ret += generalOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += acOptions.toString();
		ret += GUIUtilities.CRLF;

		ret += vcOptions.toString();
		ret += GUIUtilities.CRLF;

		XMLLine closingLine = new XMLLine(OPTIONS_TAG,
				XMLLine.CLOSING_TAG_CATEGORY);
		ret += closingLine.toString();

		// ret = XMLUtils.replaceXMLChars ( ret );

		return ret;
	}

	/**
	 * 5 juil. 2005 : displayOptionsHT2
	 * 
	 * @param optionsInstance
	 */
	public static void setOptionsInstance(Options optionsInstance) {
		Options.optionsInstance = optionsInstance;
	}

	/**
	 * 5 juil. 2005
	 */
	public void fillFromOptionsDialog() {
		this.displayOptions.fillFromOptionsDialog();
		this.printOptions.fillFromOptionsDialog();
		this.logsOptions.fillFromOptionsDialog();
		this.saveOptions.fillFromOptionsDialog();
		this.wordlistOptions.fillFromOptionsDialog();
		this.generalOptions.fillFromOptionsDialog();
		this.acOptions.fillFromOptionsDialog();
		this.vcOptions.fillFromOptionsDialog();
	}

	/**
	 * @return 8 juil. 2005
	 */
	public DisplayOptions getDisplayOptions() {
		return displayOptions;
	}

	/**
	 * @param displayOptions
	 *            8 juil. 2005
	 */
	public void setDisplayOptions(DisplayOptions displayOptions) {
		this.displayOptions = displayOptions;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public LogsOptions getLogsOptions() {
		return logsOptions;
	}

	/**
	 * @param logsOptions
	 *            8 juil. 2005
	 */
	public void setLogsOptions(LogsOptions logsOptions) {
		this.logsOptions = logsOptions;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public PrintOptions getPrintOptions() {
		return printOptions;
	}

	/**
	 * @param printOptions
	 *            8 juil. 2005
	 */
	public void setPrintOptions(PrintOptions printOptions) {
		this.printOptions = printOptions;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public SaveOptions getSaveOptions() {
		return saveOptions;
	}

	/**
	 * @param saveOptions
	 *            8 juil. 2005
	 */
	public void setSaveOptions(SaveOptions saveOptions) {
		this.saveOptions = saveOptions;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public WordlistOptions getWordlistOptions() {
		return wordlistOptions;
	}

	/**
	 * @param wordlistOptions
	 *            8 juil. 2005
	 */
	public void setWordlistOptions(WordlistOptions wordlistOptions) {
		this.wordlistOptions = wordlistOptions;
	}

	/**
	 * @return 8 juil. 2005
	 */
	public static Options getOptionsInstance() {
		return optionsInstance;
	}

	/**
	 * @return Returns the acOptions.
	 */
	public ACOptions getAcOptions() {
		return acOptions;
	}

	/**
	 * @param acOptions
	 *            The acOptions to set.
	 */
	public void setAcOptions(ACOptions acOptions) {
		this.acOptions = acOptions;
	}

	/**
	 * @return Returns the vcOptions.
	 */
	public VCOptions getVcOptions() {
		return vcOptions;
	}

	/**
	 * @param vcOptions
	 *            The vcOptions to set.
	 */
	public void setVcOptions(VCOptions vcOptions) {
		this.vcOptions = vcOptions;
	}

	public GeneralOptions getGeneralOptions() {
		return generalOptions;
	}

	public void setGeneralOptions(GeneralOptions generalOptions) {
		this.generalOptions = generalOptions;
	}
}
