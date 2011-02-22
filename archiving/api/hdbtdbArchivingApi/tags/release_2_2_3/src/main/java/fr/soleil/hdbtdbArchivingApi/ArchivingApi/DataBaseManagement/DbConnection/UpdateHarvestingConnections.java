package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbConnection;

import java.util.ArrayList;
import java.util.List;

import oracle.ucp.ConnectionHarvestingCallback;

public class UpdateHarvestingConnections {

	private List<ConnectionHarvestingCallback> jobs;

	private static UpdateHarvestingConnections instance = new UpdateHarvestingConnections();

	public static UpdateHarvestingConnections getInstance(){
		return instance;
	}

	public UpdateHarvestingConnections() {
		this.jobs = new ArrayList<ConnectionHarvestingCallback>();

	}

	public void addUpdateHarvestingConnection(ConnectionHarvestingCallback chc){
		jobs.add(chc);
	}

	public void updateAllConnections(){

		System.out.println("updateAllConnections is ongoing ....");

		for(ConnectionHarvestingCallback job : jobs){
			job.cleanup();
		}
		System.out.println(".... updateAllConnections is done");
	}
}
