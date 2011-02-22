package fr.soleil.commonarchivingapi.ArchivingTools.Tools;

//+======================================================================
//$Source$
//
//Project:      Tango Archiving Service
//
//Description:  Tango DevState management.
//				Sandra Pierre-Joseph - 07 mai 2008
//
//$Author$
//
//$Revision$
//
//$Log$
//Revision 1.1  2008/05/07 16:30:29  pierrejoseph
//first commit
//
//
//copyleft :	Synchrotron SOLEIL
//				L'Orme des Merisiers
//				Saint-Aubin - BP 48
//				91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

import fr.esrf.Tango.DevState;
import fr.esrf.tangoatk.core.IDevice;

public class TangoStateTranslation {

	public static String getStrStateValue(int intVal) {
		String val;

		switch (intVal) {
		case DevState._ALARM:
			val = IDevice.ALARM;
			break;
		case DevState._CLOSE:
			val = IDevice.CLOSE;
			break;
		case DevState._DISABLE:
			val = IDevice.DISABLE;
			break;
		case DevState._EXTRACT:
			val = IDevice.EXTRACT;
			break;
		case DevState._FAULT:
			val = IDevice.FAULT;
			break;
		case DevState._INIT:
			val = IDevice.INIT;
			break;
		case DevState._INSERT:
			val = IDevice.INSERT;
			break;
		case DevState._MOVING:
			val = IDevice.MOVING;
			break;
		case DevState._OFF:
			val = IDevice.OFF;
			break;
		case DevState._ON:
			val = IDevice.ON;
			break;
		case DevState._OPEN:
			val = IDevice.OPEN;
			break;
		case DevState._RUNNING:
			val = IDevice.RUNNING;
			break;
		case DevState._STANDBY:
			val = IDevice.STANDBY;
			break;
		case DevState._UNKNOWN:
		default:
			val = IDevice.UNKNOWN;
		}

		return val;
	}

	public static DevState getTangoDevState(String fromVal) {

		DevState val;

		if (fromVal.equals(IDevice.ON)) {
			val = DevState.ON;
		} else if (fromVal.equals(IDevice.OFF)) {
			val = DevState.OFF;
		} else if (fromVal.equals(IDevice.CLOSE)) {
			val = DevState.CLOSE;
		} else if (fromVal.equals(IDevice.OPEN)) {
			val = DevState.OPEN;
		} else if (fromVal.equals(IDevice.INSERT)) {
			val = DevState.INSERT;
		} else if (fromVal.equals(IDevice.EXTRACT)) {
			val = DevState.EXTRACT;
		} else if (fromVal.equals(IDevice.MOVING)) {
			val = DevState.MOVING;
		} else if (fromVal.equals(IDevice.STANDBY)) {
			val = DevState.STANDBY;
		} else if (fromVal.equals(IDevice.FAULT)) {
			val = DevState.FAULT;
		} else if (fromVal.equals(IDevice.INIT)) {
			val = DevState.INIT;
		} else if (fromVal.equals(IDevice.RUNNING)) {
			val = DevState.RUNNING;
		} else if (fromVal.equals(IDevice.ALARM)) {
			val = DevState.ALARM;
		} else if (fromVal.equals(IDevice.DISABLE)) {
			val = DevState.DISABLE;
		} else {
			val = DevState.UNKNOWN;
		}

		return val;
	}

}
