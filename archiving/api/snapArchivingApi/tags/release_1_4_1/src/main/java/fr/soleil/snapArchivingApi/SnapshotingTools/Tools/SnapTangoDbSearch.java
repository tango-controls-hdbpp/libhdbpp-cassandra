//+======================================================================
//$Source:  $
//
//Project :     SnapArchivingApi
//
//Description: This class wraps the TangoDbSearch class 
// 	by generating an Exception dedicated to the Snap Management
//
//$Author:  $
//
//$Revision: $
//
//$Log: $
//
//copyleft :Synchrotron SOLEIL
//			L'Orme des Merisiers
//			Saint-Aubin - BP 48
//			91192 GIF-sur-YVETTE CEDEX
//			FRANCE
//
//+============================================================================
package fr.soleil.snapArchivingApi.SnapshotingTools.Tools;

import java.util.Hashtable;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.TangoDbSearch;

/**
 * This class wraps the TangoDbSearch class by generating an Exception dedicated to the Snap Management
 * @author PIERREJOSEPH
 *
 */
public class SnapTangoDbSearch {

	// Singleton object
	private static SnapTangoDbSearch currentImpl = null;
	
	// Wrapped object
	private TangoDbSearch wrappedTangoDbSearch = null;
	
	public static SnapTangoDbSearch getInstance()
	{
		if(currentImpl == null){
			currentImpl = new SnapTangoDbSearch();
		}
		
		return currentImpl;
	}

	private SnapTangoDbSearch()
	{
		wrappedTangoDbSearch = new TangoDbSearch();
	}
	
	public String[] getAttributesForClass(String classe , Vector devOfDomain) throws SnapshotingException
	{
		try
		{
			return wrappedTangoDbSearch.getAttributesForClass(classe,devOfDomain);
		}
		catch(DevFailed e)
		{
			String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DBT_EXCEPTION;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing SnapTangoDbSearch.getAttributesForClass() method...";
			throw new SnapshotingException(message , reason , ErrSeverity.ERR , desc , "" , e);
		}
	}
	
	public Vector getDomains() throws SnapshotingException
	{
		try
		{
			return wrappedTangoDbSearch.getDomains();
		}
		catch(DevFailed e)
		{
			String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DBT_EXCEPTION;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing SnapTangoDbSearch.getDomains() method...";
			throw new SnapshotingException(message , reason , ErrSeverity.ERR , desc , "" , e);
		}
	}
	
	public Vector getDomains ( String domainsRegExp ) throws SnapshotingException
	{
		try
		{
			return wrappedTangoDbSearch.getDomains(domainsRegExp);
		}
		catch(DevFailed e)
		{
			String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DBT_EXCEPTION;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing SnapTangoDbSearch.getDomains(domain) method...";
			throw new SnapshotingException(message , reason , ErrSeverity.ERR , desc , "" , e);
		}
	}
	
	public Hashtable getClassAndDevices(String domain) throws SnapshotingException
	{
		try
		{
			return wrappedTangoDbSearch.getClassAndDevices(domain);
		}
		catch(DevFailed e)
		{
			String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DBT_EXCEPTION;
			String reason = GlobalConst.TANGO_COMM_EXCEPTION;
			String desc = "Failed while executing SnapTangoDbSearch.getClassAndDevices(domain) method...";
			throw new SnapshotingException(message , reason , ErrSeverity.ERR , desc , "" , e);
		}
	}
}
