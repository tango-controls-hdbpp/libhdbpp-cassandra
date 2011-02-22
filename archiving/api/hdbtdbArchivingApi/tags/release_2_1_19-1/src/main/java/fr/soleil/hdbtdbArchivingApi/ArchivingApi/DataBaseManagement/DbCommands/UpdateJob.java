package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.DbCommands;

import java.util.ArrayList;
import java.util.List;

public class UpdateJob {
	private List<IUpdateJobs> jobs;

	private static UpdateJob instance = new UpdateJob();

	public static UpdateJob getInstance() {
		return instance;
	}

	public UpdateJob() {
		this.jobs = new ArrayList<IUpdateJobs>();
		System.out.println("SPJZ====> UpdateJob Creation");
	}

	public void addUpdateJob(IUpdateJobs iuj) {
		jobs.add(iuj);
	}

	public void updateAllJobs() {
		// while (true) {
		// try
		// {
		// Déclenchement à intervale de temps régulier
		// Thread.sleep(Main.COMMIT_INTERVAL);

		System.out.println("Updating database ...");

		for (IUpdateJobs job : jobs) {
			job.synchronizedUpdate();
		}

		System.out.println("Database updated successfully.");
		// }
		// catch (InterruptedException e)
		// {
		// System.out.println("Error occured while pausing thread :" + e);
		// }
		// }
	}
}
