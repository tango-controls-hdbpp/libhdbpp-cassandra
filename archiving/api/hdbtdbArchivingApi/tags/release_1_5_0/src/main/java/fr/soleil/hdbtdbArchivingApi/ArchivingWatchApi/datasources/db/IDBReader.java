/*	Synchrotron Soleil 
 *  
 *   File          :  IArchivedAttributesReader.java
 *  
 *   Project       :  archiving_watcher
 *  
 *   Description   :  
 *  
 *   Author        :  CLAISSE
 *  
 *   Original      :  28 nov. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: IArchivedAttributesReader.java,v 
 *
 */
 /*
 * Created on 28 nov. 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.datasources.db;

import java.sql.Timestamp;
import java.util.Hashtable;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.dto.ModeData;

/**
 * Contains methods to access the necessary HDB data for the Archiving Watcher API 
 * @author CLAISSE 
 */
public interface IDBReader 
{
    /**
     * Code for the total load of an HDB Archiver
     */
    public static final int LOAD_ALL = 0;
    /**
     * Code for the scalar load of an HDB Archiver
     */
    public static final int LOAD_SCALAR = 1;
    /**
     * Code for the spectrum load of an HDB Archiver
     */
    public static final int LOAD_SPECTRUM = 2;
    /**
     * Code for the image load of an HDB Archiver
     */
    public static final int LOAD_IMAGE = 3;
    
    /**
     * Connects to the HDB database, if not already connected.
     * @param forceOpening True if the method must reconnect even if already connected 
     * @throws DevFailed
     */
    public void openConnectionIfNecessary ( boolean forceOpening ) throws DevFailed;
    /**
     * Closes the connection to the HDB database
     * @throws DevFailed
     */
    public void closeConnection () throws DevFailed;
    /**
     * Tests whether the HDB connection works correctly
     * @return Whether the HDB connection works correctly
     */
    public boolean isDBConnectionAlive();
    
    /**
     * Returns the sysdate of the database
     * @return The sysdate of the database
     * @throws DevFailed
     */
    public Timestamp now () throws DevFailed;
    
    /**
     * Loads from the HDB table AMT into a Hashtable the attributes that are supposed to be archiving.
     * The keys are the attributes complete names, the values are ModeData objects that describe a given attribute's archiving. 
     * @return A Hashtable which keys are the attributes complete names, and which values are ModeData objects that describe a given attribute's archiving.
     * @throws DevFailed
     */
    public Hashtable getArchivedAttributes () throws DevFailed; 
    
    /**
     * Loads from the HDB table ADT the attribute corresponding to the attribute's name.
     * @return an integer which is the attribute identifier.
     * @throws DevFailed
     */
    
    public int getAttributeId(String completeName)throws DevFailed ;
    /**
     * Returns whether a given attribute is scalar or not.
     * @param completeName The attribute's complete name
     * @return Whether the given attribute is scalar or not.
     * @throws DevFailed
     */
    public boolean isScalar ( String completeName ) throws DevFailed;

    /**
     * Returns the time a given attribute was recorded, or null if it hasn't. 
     * @param completeName The attribute's complete name
     * @return The time a given attribute was recorded, or null if it hasn't
     * @throws DevFailed
     */
    public Timestamp getTimeOfLastInsert ( String completeName ) throws DevFailed;
    
    /**
     * Returns a String array which contains as first element the time value
     * and the second element indicates if the corresponding value is null or not
     * The writable criterion allows to determine the column name
     * @param completeName The attribute's full name
     * @param dataFormat : AttrDataFormat._SCALAR or AttrDataFormat._SPECTRUM ot AttrDataFormat._IMAGE
     * @param writable AttrWriteType._READ_WRITE or AttrWriteType._READ or AttrWriteType._WRITE
     * @return The string array
     * @throws DevFailed
     */
    public String[] getTimeValueNullOrNotOfLastInsert ( String completeName,int dataFormat, int writable ) throws DevFailed;
    
    /**
     * Returns the total number of records for a given attribute. 
     * @param completeName The attribute's complete name
     * @return The total number of records
     * @throws DevFailed
     */
    public int getRecordCount ( String completeName ) throws DevFailed;
    /**
     * Returns the number of records for a given attribute, that are more recent than a given age. 
     * @param completeName The attribute's complete name
     * @param period The threshold age
     * @return The number of records
     * @throws DevFailed
     */
    public int getRecordCount ( String completeName , int period ) throws DevFailed;
    /**
     * Returns whether a given attribute's most recent recorded value is null or otherwise empty 
     * @param completeName The attribute's complete name
     * @return Whether that value was null or not 
     * @throws DevFailed
     */
    public boolean isLastValueNull ( String completeName ) throws DevFailed;
    
    /**
     * Returns a given archiver's load
     * @param archiverName The archiver's complete name
     * @param loadType The type of load, can be any of (LOAD_ALL,LOAD_SCALAR,LOAD_SPECTRUM,LOAD_IMAGE)
     * @return The archiver's load
     * @throws DevFailed
     */
    public int getArchiverLoad ( String archiverName , int loadType ) throws DevFailed;
    /**
     * Tests whether a given archiver is currently an exported device
     * @param archiverName The archiver's complete name
     * @return Whether the archiver is alive
     * @throws DevFailed
     */
    public boolean isAlive(String archiverName) throws DevFailed;
    
    /**
     * Returns a given archiver's status
     * @param archiverName The archiver's complete name
     * @return The archiver's status
     * @throws DevFailed
     */
    public String getDeviceStatus(String archiverName) throws DevFailed;
    
    public ModeData getModeDataForAttribute ( String completeName ) throws DevFailed;
    
    public Mode getModeForAttribute(String completeName) throws DevFailed;
    
    /**
     * Returns the write value of the attribute 
     * @param completeName
     * @return AttrWriteType._READ_WRITE or AttrWriteType._READ or AttrWriteType._WRITE
     * @throws DevFailed
     */
    public int getAttWritableCriterion(String completeName ) throws DevFailed;
    
    /**
     * Returns the format of the attribute 
     * @param completeName
     * @return AttrDataFormat._SCALAR or AttrDataFormat._SPECTRUM ot AttrDataFormat._IMAGE
     * @throws DevFailed
     */
    public int getAttFormatCriterion(String completeName ) throws DevFailed;
}
