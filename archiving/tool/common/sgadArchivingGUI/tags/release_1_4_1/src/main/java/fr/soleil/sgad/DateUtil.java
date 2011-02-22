//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/DateUtil.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DateUtil.
//						(Chinkumo Jean) - Nov 22, 2004
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: DateUtil.java,v $
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

package fr.soleil.sgad;

import java.util.Date;

public class DateUtil
{

	public static String getTime()
	{
		long milli = System.currentTimeMillis();
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		//java.text.SimpleDateFormat genFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.text.SimpleDateFormat genFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
		Date date;
		calendar.setTimeInMillis(milli);
		date = calendar.getTime();
		return genFormat.format(date);
	}
}
