//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/tools/BensikinDate.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BensikinDate.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: BensikinDate.java,v $
// Revision 1.2  2006/01/09 12:52:25  ounsy
// Date filter updated
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.tools;

import java.util.*;
import java.sql.Timestamp;
import java.text.*;

import fr.soleil.bensikin.actions.exceptions.FieldFormatException;
import fr.soleil.snapArchivingApi.SnapManagerApi.SnapManagerApi;
import fr.soleil.snapArchivingApi.SnapshotingApi.ConfigConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.DateUtil;


public class BensikinDate implements Cloneable , Comparable
{
	/**
	 * Format : <CODE>"yyyy-mm-dd"</CODE>.
	 */
	public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Format : <CODE>"yyyy-mm-dd HH:mm:ss"</CODE>.
	 */
	public static final String SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private int year;
	private int month;
	private int day;
	private int hour;
	private int minutes;
	private int seconds;
	private int milli;

	private boolean dateWasValid;


	/**
	 * Builds itself from the String representation and the specified format description.
	 *
	 * @param date   The String representation of the date
	 * @param format The format description
	 * @throws ParseException Invalid parameters
	 */
	public BensikinDate(String date , String format) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date dateObj = dateFormat.parse(date);

		dateFormat.setLenient(false);
		try
		{
			dateFormat.parse(date);
			dateWasValid = true;
		}
		catch ( ParseException pEx )
		{
			dateWasValid = false;
		}

		dateToNumber(dateObj , this);
	}

	private static void dateToNumber(Date date , BensikinDate obj)
	{
		long timeMilli = date.getTime();

		GregorianCalendar calend = new GregorianCalendar();
		calend.setTime(date);

		obj.year = calend.get(Calendar.YEAR);
		obj.month = calend.get(Calendar.MONTH) + 1;
		obj.day = calend.get(Calendar.DAY_OF_MONTH);
		obj.hour = calend.get(Calendar.HOUR_OF_DAY);
		obj.minutes = calend.get(Calendar.MINUTE);
		obj.seconds = calend.get(Calendar.SECOND);

		calend = new GregorianCalendar(obj.year , obj.month - 1 , obj.day ,
		                               obj.hour , obj.minutes , obj.seconds);

		obj.milli = ( int ) ( timeMilli - calend.getTime().getTime() );
	}

	/**
	 * Returns true if the date was a valid date.
	 *
	 * @return True if the date was a valid date
	 */
	public boolean wasValid()
	{
		return dateWasValid;
	}

	/**
	 * Converts to a <CODE>java.util.Date</CODE> object.
	 *
	 * @return The <CODE>java.util.Date</CODE> equivalent.
	 */
	public Date toDate()
	{
		GregorianCalendar calend = new GregorianCalendar(year , month - 1 , day , hour , minutes , seconds);
		return new Date(calend.getTime().getTime() + milli);
	}

	/**
	 * Returns the difference, in milliseconds, between this date and <code>anotherDate</code>;
	 * The result is:
	 * <UL>
	 * <LI> 0 if a == b
	 * <LI> <0 if a < b
	 * <LI> >0 if a > b
	 * </UL>
	 *
	 * @param anotherDate The date to compare to
	 * @return The difference, in milliseconds, between this date and <code>anotherDate</code>
	 */
	public long millisecondsBetween(BensikinDate anotherDate)
	{
		long milliThis = toDate().getTime();
		long milliOther = anotherDate.toDate().getTime();

		return milliThis - milliOther;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{
		if ( o == null )
		{
			throw new NullPointerException();
		}

		BensikinDate anotherDate = ( BensikinDate ) o;

		long difference = millisecondsBetween(anotherDate);

		if ( difference < 0 )
		{
			return -1;
		}

		if ( difference > 0 )
		{
			return 1;
		}

		return 0;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( obj == null )
		{
			return false;
		}

		if ( obj.getClass() != BensikinDate.class )
		{
			return false;
		}

		if ( millisecondsBetween(( BensikinDate ) obj) == 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch ( CloneNotSupportedException e )
		{
			return null;
		}
	}

	/**
	 * Converts a String to a Timestamp. The String can have the formats FORMAT_DATE_SQL or FORMAT_DATE_HEURE_SQL
	 *
	 * @param in           The String to convert
	 * @param hasTime      True if the String was supposed to have a time part, false otherwise
	 * @param errorMessage The error message to display if need be
	 * @return The Timestamp representation of in
	 * @throws FieldFormatException If the date is invalid, or has a time part it's not supposed to
	 */
	public static Timestamp stringToTimestamp(String in , boolean hasTime , int errorMessage) throws FieldFormatException
	{
		int l = in.length();
		Timestamp ret = null;
		String format = null;

		try
		{
			switch ( l )
			{
				case 10:
					format = BensikinDate.SQL_DATE_FORMAT;
					break;

				case 19:
					if ( !hasTime )
					{
						//throw new FieldFormatException(FieldFormatException.FILTER_SNAPSHOTS_START_TIME);
						throw new FieldFormatException(errorMessage);
					}
					format = BensikinDate.SQL_DATETIME_FORMAT;
					break;

				default :
					throw new FieldFormatException(errorMessage);
			}

			BensikinDate dh = new BensikinDate(in , format);
			if ( !dh.wasValid() )
			{
				throw new FieldFormatException(errorMessage);
			}

			java.util.Date date = dh.toDate();
			long date_l = date.getTime();
			ret = new Timestamp(date_l);

			return ret;
		}
		catch ( Exception e )
		{
			throw new FieldFormatException(errorMessage);
		}
	}

//	/**
//	 * Returns a date as a String formated for the database.
//	 * @param milliseconds the date in milliseconds
//	 * @return A date as a String formated for the database.
//	 */
//	public static String formatDate(long milliseconds) {
//        int type = SnapManagerApi.getSnapDbType();
//        String date = "";
//        switch (type) {
//            case ConfigConst.BD_ORACLE :
//                date = DateUtil.milliToString(milliseconds,
//                        DateUtil.FR_DATE_PATTERN);
//                break;
//            case ConfigConst.BD_MYSQL :
//            default :
//                date = DateUtil.milliToString(milliseconds,
//                        DateUtil.US_DATE_PATTERN);
//        }
//        return date;
//	}
}
