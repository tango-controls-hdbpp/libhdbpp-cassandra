//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LoggerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.3  2005/11/29 18:25:08  chinkumo
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
package fr.soleil.bensikin.logs;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * A factory used to instantiate IHistoryManager of 1 types: DEFAULT_TYPE
 * 
 * @author CLAISSE
 */
public class BensikinLoggerFactory {
    private static ILogger currentImpl = new BensikinLogger();

    public static synchronized ILogger getLogger() {
	return currentImpl;
    }

}
