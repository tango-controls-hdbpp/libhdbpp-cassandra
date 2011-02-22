/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import java.sql.SQLException;
import java.util.Vector;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeDomains;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeFamilies;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeIds;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeMembers;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeNames;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes.TableSelector.AttributeProperties;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AttributeExtractor.DataGetters.DataGetters;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseUtils.IDbUtils;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeHeavy;

/**
 * @author AYADI
 *
 */
public interface IAdtAptAttributes {
	public AttributeFamilies getFamilies();
	public AttributeMembers getMembers();
	public AttributeNames getNames();
	public AttributeDomains getDomains();
	public AttributeIds getIds() ;
	public AttributeProperties getProperties() ;
	public int[] getAtt_TFW_Data(String att_name) throws ArchivingException;
	public int getAttDataWritable(String att_name) throws ArchivingException;
	public int getAttDataFormat(String att_name) throws ArchivingException;
	public int getAttDataType(String att_name) throws SQLException , ArchivingException;
	public int getAttRecordCount(String att_name, IDbUtils utils) throws ArchivingException;
	public boolean isRegisteredADT(String att_name) throws ArchivingException;
	public void registerAttribute(AttributeHeavy attributeHeavy, IDbUtils dbUtils ) throws ArchivingException;
	public void CreateAttributeTableIfNotExist(String attributeName, DataGetters extractor, IDbUtils dbUtils) throws ArchivingException ;
	public Vector<String> getAttDefinitionData(String att_name) throws ArchivingException;
}
