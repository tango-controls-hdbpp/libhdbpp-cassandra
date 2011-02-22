package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools;

import java.util.Enumeration;
import java.util.Vector;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;

/**
 * A stack used to store the times at which a control step was executed. Thus,
 * after completion of a full cycle, the top element is the date of the last
 * step, while the bottom element is the date of the first step. This is used to
 * keep track of when a ControlResult has started and ended.
 * 
 * @author CLAISSE
 */
public class LimitedStack extends Vector {
	private static final int MAX_SIZE = 5;

	/**
	 * Default constructor
	 */
	public LimitedStack() {
		super();
	}

	/**
	 * Adds a date on top of the stack
	 * 
	 * @param item
	 *            The new date
	 * @return The inserted element
	 */
	public Long push(Long item) {
		super.removeElement(item);

		if (this.isFull()) {
			super.remove(super.lastElement());
		}

		super.insertElementAt(item, 0);
		return item;
	}

	private boolean isFull() {
		return super.size() >= MAX_SIZE;
	}

	public void log() {
		Enumeration enumer = super.elements();
		System.out.println("LimitedStack/log-----IN");
		while (enumer.hasMoreElements()) {
			Long next = (Long) enumer.nextElement();
			System.out.println("        next/" + next);
		}
		System.out.println("LimitedStack/log----OUT");
	}

	public boolean containsDate(long date, ILogger logger) {
		/*
		 * Long dateL = new Long ( date ); return super.contains ( dateL );
		 */

		// System.out.println (
		// "   containsDate/date/"+date+"--------------------------");
		Enumeration enumer = super.elements();
		while (enumer.hasMoreElements()) {
			Long nextOld = (Long) enumer.nextElement();
			// System.out.println ( "containsDate/nextOld/"+nextOld);

			// The timestamp is provided by the equipment, from one equipment to
			// an other we don't know how the timestamp is managed
			// The required precision is 100 ms
			boolean matchFound = (Math.abs((date - nextOld.longValue())) < 100);
			// boolean matchFound = DBTools.areTimestampsEqual ( date ,
			// nextOld.longValue () , logger );
			if (matchFound) {
				// logger.trace ( ILogger.LEVEL_DEBUG , "New date = " + date +
				// " | Existing date = " + nextOld.longValue () );
				return true;
			}
		}
		// System.out.println (
		// "containsDate/----------------------------------------");
		return false;
	}

	/**
	 * 
	 * @param date
	 * @param logger
	 * @return
	 */
	public boolean validateDate(long date, ILogger logger) {
		// TODO Auto-generated method stub
		Enumeration enumer = super.elements();

		while (enumer.hasMoreElements()) {
			Long nextOld = (Long) enumer.nextElement();

			// The timestamp is provided by the equipment, from one equipment to
			// an other we don't know how the timestamp is managed
			// The required precision is 100 ms
			// and the actual timestamp must be greater than the previous one
			boolean matchFound = (date > nextOld.longValue());
			// System.out.println("date: "+ new Timestamp(date).toString() +
			// "\n old date :" + new Timestamp(nextOld.longValue
			// ()).toString());
			if (matchFound) {
				// logger.trace ( ILogger.LEVEL_DEBUG , "New date = " + date +
				// " | Previous date = " + nextOld.longValue () );
				return true;
			}
		}
		return false;

	}

}
