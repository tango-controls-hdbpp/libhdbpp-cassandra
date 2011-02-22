package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection;

import javax.sql.DataSource;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public interface IPoolConnection {
	DataSource getPoolInstance(String url, String user, String pwd)
			throws ArchivingException;
}
