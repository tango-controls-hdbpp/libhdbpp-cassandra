/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public interface ITDBAdtAptAttribute {
	public int[] getAtt_TFW_Data_By_Id(int id) throws ArchivingException;
}
