package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;


public class HDBOracleConnection extends AbstractOracleConnection {



	public HDBOracleConnection(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public HDBOracleConnection(int type, String host, String name, String schema,
			String user, String pass) {
		super(type, host, name, schema, user, pass);

	}

	@Override
	protected String getClassDeviceName() {
		// TODO Auto-generated method stub
		return ConfigConst.HDB_CLASS_DEVICE;
	}

	@Override
	protected String getDbName() {
		// TODO Auto-generated method stub
		return "HDB";
	}

}
