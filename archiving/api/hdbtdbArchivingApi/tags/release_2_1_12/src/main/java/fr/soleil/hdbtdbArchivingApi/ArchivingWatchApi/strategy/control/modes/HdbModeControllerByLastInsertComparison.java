/*  Synchrotron Soleil 
 *  
 *   File          :  BasicModeController.java
 *  
 *   Project       :  archiving_watcher
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  28 nov. 2005 
 *  
 *   Revision:                      Author:  
 *   Date:                          State:  
 *  
 *   Log: BasicModeController.java,v 
 *
 */
/*
 * Created on 28 nov. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.modes;

import java.sql.Timestamp;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.DateUtil;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.DBReaderFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db.IDBReader;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.devicelink.Warnable;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ArchivingAttribute;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.ISaferPeriodCalculator;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.control.safetyperiod.SaferPeriodCalculatorFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.tools.Tools;

/**
 * An implementation that looks how many records (if any) have been inserted
 * since <i>f(period)</i> ago. Where <i>f(period)</i> is the "safety period",
 * meaning a time span longer than period to allow for network (or any other
 * reason) delays.
 * 
 * @author CLAISSE
 */
public class HdbModeControllerByLastInsertComparison extends
		HdbModeControllerAdapter {
	private int cnt;

	public HdbModeControllerByLastInsertComparison() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * archwatch.mode.controller.IModeController#controlPeriodicMode(fr.soleil
	 * .hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode,
	 * archwatch.dto.Attribute)
	 */

	protected int controlPeriodicMode(ModePeriode modeP, ArchivingAttribute attr)
			throws DevFailed {
		// modeP = nullAfterAFewTries ( modeP );
		if (modeP == null) {
			Tools
					.trace(
							"HdbModeControllerByLastInsertComparison/controlPeriodicMode/modeP == null!/for attribute|"
									+ attr.getCompleteName() + "|",
							Warnable.LOG_LEVEL_ERROR);
			return IModeController.CONTROL_FAILED;
		}

		String _completeName = attr.getCompleteName();
		try {
			Timestamp _now;
			Timestamp _timeOfLastInsert;
			boolean _isOk = false;
			String _timeValue[];
			int _writable;
			int _dataFormat;
			IDBReader attributesReader = DBReaderFactory.getCurrentImpl();

			// Retrieve the writable value
			_writable = attributesReader.getAttWritableCriterion(_completeName);
			// Retieve the data format
			_dataFormat = attributesReader.getAttFormatCriterion(_completeName);
			// Retrieve the couple max(time), value
			_timeValue = attributesReader.getTimeValueNullOrNotOfLastInsert(
					_completeName, _dataFormat, _writable);

			if (_timeValue == null) {
				return IModeController.CONTROL_KO;
			}

			long stringToMilli;
			stringToMilli = DateUtil.stringToMilli(_timeValue[0]);
			_timeOfLastInsert = new Timestamp(stringToMilli);
			// System.out.println (
			// "-------------/timeOfLastInsert/"+timeOfLastInsert );
			attr.setLastInsert(_timeOfLastInsert);
			if (_timeOfLastInsert == null) {
				return IModeController.CONTROL_KO;
			}

			// Delta calculation between new and the time of the last insertion
			_now = attributesReader.now();
			// System.out.println ( "-------------/now/"+now );
			long delta = _now.getTime() - _timeOfLastInsert.getTime();
			// Safer calculation
			ISaferPeriodCalculator saferPeriodCalculator = SaferPeriodCalculatorFactory
					.getCurrentImpl();
			int saferPeriod = saferPeriodCalculator.getSaferPeriod(modeP
					.getPeriod());

			// Attribute is ok or not
			_isOk = (delta < saferPeriod);
			// System.out.println (
			// "-------------/delta/"+delta+"/saferPeriod/"+saferPeriod+"/isOk/"+isOk
			// );

			if (_isOk) {
				if (_timeValue[1] == "null")
					return IModeController.CONTROL_OK_BUT_VALUE_IS_NULL;

				return IModeController.CONTROL_OK;
			} else {
				return IModeController.CONTROL_KO;
			}
		} catch (DevFailed e) {
			Tools
					.trace(
							"HdbModeControllerByRecordCount/controlPeriodicMode/failed calling getRecordCount for attribute|"
									+ _completeName + "|", e,
							Warnable.LOG_LEVEL_WARN);
			return IModeController.CONTROL_FAILED;
		} catch (Exception e) {
			Tools
					.trace(
							"HdbModeControllerByRecordCount/controlPeriodicMode/failed calling for attribute|"
									+ _completeName + "|", e,
							Warnable.LOG_LEVEL_WARN);
			return IModeController.CONTROL_FAILED;
		}
	}

	private ModePeriode nullAfterAFewTries(ModePeriode modeP) {
		cnt++;
		if (cnt > 5) {
			return null;
		} else {
			return modeP;
		}

	}

}