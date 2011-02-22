package fr.soleil.hdbtdbArchivingApi.ArchivingApi;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbFactories.IDataBaseApiUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DbImpl.IDataBaseApi;

public interface IDataBaseApiFactory 
{
	public IDataBaseApi getDataBaseApi();
	
	public IDataBaseApiUtilities getDataBaseUtilities();
}
