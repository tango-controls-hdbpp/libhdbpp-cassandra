package fr.soleil.hdbtdbArchivingApi.ArchivingApi.AttributesManagement.AdtAptAttributes;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection.IDBConnection;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class TDBMySqlAdtAptAttributes extends MySqlAdtAptAttributes implements ITDBAdtAptAttribute{

	public TDBMySqlAdtAptAttributes(IDBConnection c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
    /**
     * Returns the type, the format and the writable property of a given attribute
     *
     * @param att_name
     * @return An array (int) containing the type, the format and the writable property of the given attribute.
     * @throws ArchivingException
     */
    public int[] getAtt_TFW_Data_By_Id(int id) throws ArchivingException
    {
    	TDBAdtAptMethods tdbAtt = new TDBAdtAptMethods();
    	return tdbAtt.getAtt_TFW_Data_By_Id(id, this);    }

}
