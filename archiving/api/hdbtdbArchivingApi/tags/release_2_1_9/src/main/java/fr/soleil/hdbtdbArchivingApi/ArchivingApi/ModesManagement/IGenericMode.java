package fr.soleil.hdbtdbArchivingApi.ArchivingApi.ModesManagement;

import java.util.Vector;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;

public interface IGenericMode {
	public String[] getCurrentArchivedAtt() throws ArchivingException;

	public String[] getAttribute(boolean archivingOnly)
			throws ArchivingException;

	public int getCurrentArchivedAttCount() throws ArchivingException;

	public String getDeviceInCharge(String attribut_name)
			throws ArchivingException;

	public boolean isArchived(String att_name) throws ArchivingException;

	public boolean isArchived(String att_name, String device_name)
			throws ArchivingException;

	public String getArchiverForAttribute(String att_name)
			throws ArchivingException;

	public String getArchiverForAttributeEvenIfTheStopDateIsNotNull(
			String att_name) throws ArchivingException;

	public Mode getCurrentArchivingMode(String attribut_name)
			throws ArchivingException;

	public Vector getArchiverCurrentTasks(String archiverName)
			throws ArchivingException;

	public void insertModeRecord(AttributeLightMode attributeLightMode)
			throws ArchivingException;

	public void updateModeRecord(String attribute_name)
			throws ArchivingException;

}
