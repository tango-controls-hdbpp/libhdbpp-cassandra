//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/DoFutureTaskAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  CancelAction.
//                      (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: DoFutureTaskAction.java,v $
// Revision 1.1  2007/04/06 13:05:05  ounsy
// creation
//
// Revision 1.7  2006/05/29 15:44:47  ounsy
// minor changes
//
// Revision 1.6  2006/04/05 13:40:03  ounsy
// optimization
//
// Revision 1.5  2006/01/24 12:50:05  ounsy
// Bug of the new VC replacing the former selected VC corrected
//
// Revision 1.4  2006/01/24 12:25:54  ounsy
// Bug of the new AC replacing the former selected AC corrected
//
// Revision 1.3  2005/12/15 10:38:17  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :   Synchrotron SOLEIL
//                  L'Orme des Merisiers
//                  Saint-Aubin - BP 48
//                  91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.swing.AbstractAction;

public class DoFutureTaskAction extends AbstractAction {
	public DoFutureTaskAction(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		System.out.println("DoFutureTaskAction/actionPerformed/START");

		Callable<String> callable = new TaskCLA();
		FutureTask<String> futureTask = new FutureTask<String>(callable);
		CancelFutureTaskAction.getInstance(null).setTaskToCancel(futureTask);

		Thread thread = new Thread(futureTask);
		thread.start();

		/*
		 * try { String res = futureTask.get ();//blocks until completion
		 * System.out.println("DoFutureTaskAction/actionPerformed/res/"+res); }
		 * catch (Exception e) { e.printStackTrace(); }
		 */
	}

	private class TaskCLA implements Callable<String> {
		private CLACompletionListener listener;

		public TaskCLA() {
			listener = new CLACompletionListenerImpl();
		}

		public String call() throws Exception {
			int seconds = 10;
			Thread.sleep(seconds * 1000);
			String ret = "TaskCLA complete!!!!!!!!";
			listener.CLAdone(ret);
			return ret;
		}
	}

	private interface CLACompletionListener {
		public void CLAdone(String result);
	}

	private class CLACompletionListenerImpl implements CLACompletionListener {
		public void CLAdone(String result) {
			System.out.println("CLACompletionListenerImpl/CLAdone/result/"
					+ result);
		}
	}
}
