package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;

public class TDBOracleConnection extends AbstractOracleConnection {

	public TDBOracleConnection(int type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public TDBOracleConnection(int type, String host, String name,
			String schema, String user, String pass) {
		super(type, host, name, schema, user, pass);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getClassDeviceName() {
		// TODO Auto-generated method stub
		return ConfigConst.TDB_CLASS_DEVICE;
	}

	@Override
	protected String getDbName() {
		// TODO Auto-generated method stub
		return "TDB";
	}
}
