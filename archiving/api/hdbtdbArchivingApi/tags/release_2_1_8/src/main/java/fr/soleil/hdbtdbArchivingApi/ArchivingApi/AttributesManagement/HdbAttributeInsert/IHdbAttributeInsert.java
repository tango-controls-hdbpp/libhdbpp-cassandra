/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.HdbAttributeInsert;

import java.io.IOException;
import java.sql.SQLException;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ImageEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ScalarEvent;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RO;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SpectrumEvent_RW;

/**
 * @author AYADI
 *
 */
public interface IHdbAttributeInsert {
	public void insert_ScalarData(ScalarEvent scalarEvent) throws ArchivingException;
	public void insert_SpectrumData_RO(SpectrumEvent_RO hdbSpectrumEvent_RO) throws ArchivingException;
	public void insert_SpectrumData_RW(SpectrumEvent_RW hdbSpectrumEvent_RW) throws ArchivingException;
	public void insert_ImageData_RO(ImageEvent_RO imageEvent_RO) throws SQLException , IOException, ArchivingException;

}
