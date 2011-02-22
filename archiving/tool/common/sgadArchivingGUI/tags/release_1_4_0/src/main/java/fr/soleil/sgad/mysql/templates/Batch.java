//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/mysql/templates/Batch.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Batch.
//						(Chinkumo Jean) - Dec 5, 2004
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: Batch.java,v $
// Revision 1.2  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.1  2005/01/26 10:24:58  chinkumo
// First commit
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.sgad.mysql.templates;

public class Batch
{
	public static String getText(String filePath)
	{
		String batchText = "";
		batchText = "mysql -u root -e \"\\. " + filePath + "\"";
		return batchText;
	}
}
