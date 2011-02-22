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
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.TdbSpec;
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
public class TdbModeControllerByLastInsertComparison extends
		TdbModeControllerAdapter {
	public TdbModeControllerByLastInsertComparison() {
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
	protected int controlPeriodicMode(ModePeriode modeP, TdbSpec spec,
			ArchivingAttribute attr) throws DevFailed {
		if (modeP == null) {
			Tools
					.trace(
							"TdbModeControllerByLastInsertComparison/controlPeriodicMode/modeP == null!/for attribute|"
									+ attr.getCompleteName() + "|",
							Warnable.LOG_LEVEL_ERROR);
			return IModeController.CONTROL_FAILED;
		}

		String _completeName = attr.getCompleteName();

		try {
			Timestamp _now;
			Timestamp _timeOfLastInsert;
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

			long _stringToMilli;
			_stringToMilli = DateUtil.stringToMilli(_timeValue[0]);
			_timeOfLastInsert = new Timestamp(_stringToMilli);
			// System.out.println (
			// "-------------/timeOfLastInsert/"+timeOfLastInsert );

			attr.setLastInsert(_timeOfLastInsert);
			if (_timeOfLastInsert == null) {
				return IModeController.CONTROL_KO;
			}

			long _exportPeriod_millis = spec.getExportPeriod();
			int _exportPeriod = (int) _exportPeriod_millis;
			ISaferPeriodCalculator _saferPeriodCalculator = SaferPeriodCalculatorFactory
					.getCurrentImpl();
			int _saferPeriod = _saferPeriodCalculator.getSaferPeriod(modeP
					.getPeriod()
					+ _exportPeriod);

			_now = attributesReader.now();
			long _delta = _now.getTime() - _timeOfLastInsert.getTime();

			// System.out.println (
			// "-------------/delta/"+delta+"/saferPeriod/"+saferPeriod+"/isOk/"+isOk
			// );

			if (_delta < _saferPeriod) {
				if (_timeValue[1] == "null")
					return IModeController.CONTROL_OK_BUT_VALUE_IS_NULL;
				return IModeController.CONTROL_OK;
			} else {
				return IModeController.CONTROL_KO;
			}
		} catch (DevFailed e) {
			Tools
					.trace(
							"TdbModeControllerByRecordCount/controlPeriodicMode/failed calling getRecordCount for attribute|"
									+ _completeName + "|", e,
							Warnable.LOG_LEVEL_WARN);
			return IModeController.CONTROL_FAILED;
		} catch (Exception e) {
			Tools
					.trace(
							"TdbModeControllerByRecordCount/controlPeriodicMode/failed calling for attribute|"
									+ _completeName + "|", e,
							Warnable.LOG_LEVEL_WARN);
			return IModeController.CONTROL_FAILED;
		}

	}

}