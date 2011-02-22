/**
 * 
 */
package fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi;

import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

/**
 * @author AYADI
 * 
 */
public class HdbArchivingManagerApiRef extends ArchivingManagerApiRef {
	private static final String m_hdbDevicePattern = "archiving/hdbarchiver/*";
	private static final String m_ClassDevice = "HdbArchiver";
	public static boolean m_Facility = false;
	public static String[] m_ExportedArchiverList;
	private static String[] m_NotExportedArchiverList;

	/**
	 * 
	 */
	public HdbArchivingManagerApiRef() {
		// TODO Auto-generated constructor stub
		super();
		type = ConfigConst.HDB;
	}

	@Override
	protected void setArchivingException() throws ArchivingException {
		// TODO Auto-generated method stub
		String message = GlobalConst.ARCHIVING_ERROR_PREFIX + " : "
				+ GlobalConst.CANNOT_TALK_TO_ADB;
		String reason = "Failed while executing ArchivingManagerApi.ArchivingStart() method...";
		String desc = "The connection to the archiving database has not been initialized !";
		throw new ArchivingException(message, reason, ErrSeverity.PANIC, desc,
				"");

	}

	@Override
	protected String getHost() {
		// TODO Auto-generated method stub
		return ConfigConst.HDB_HOST;
	}

	@Override
	protected String getPassword() {
		// TODO Auto-generated method stub
		return ConfigConst.HDB_BROWSER_PASSWORD;
	}

	@Override
	protected String getSchema() {
		// TODO Auto-generated method stub
		return ConfigConst.HDB_SCHEMA_NAME;
	}

	@Override
	protected String getUser() {
		// TODO Auto-generated method stub
		return ConfigConst.HDB_BROWSER_USER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seefr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.TangoDataBaseApi.
	 * TangoDataBaseApi#geDevicePattern()
	 */
	@Override
	protected String geDevicePattern() {
		// TODO Auto-generated method stub
		return m_hdbDevicePattern;
	}

	@Override
	public String getClassDevice() {
		// TODO Auto-generated method stub
		return m_ClassDevice;
	}

	@Override
	protected String getDB() {
		// TODO Auto-generated method stub
		return "Hdb";
	}

	@Override
	public String[] getM_ExportedArchiverList() {
		// TODO Auto-generated method stub
		return m_ExportedArchiverList;
	}

	@Override
	public String[] getM_NotExportedArchiverList() {
		// TODO Auto-generated method stub
		return m_NotExportedArchiverList;
	}

	@Override
	public boolean isM_Facility() {
		// TODO Auto-generated method stub
		return m_Facility;
	}

	@Override
	public void setM_ExportedArchiverList(String[] exportedArchiverList) {
		// TODO Auto-generated method stub
		m_ExportedArchiverList = exportedArchiverList;
	}

	@Override
	public void setM_Facility(boolean facility) {
		// TODO Auto-generated method stub
		m_Facility = facility;
	}

	@Override
	public void setM_NotExportedArchiverList(String[] notExportedArchiverList) {
		// TODO Auto-generated method stub
		m_NotExportedArchiverList = notExportedArchiverList;
	}

}
