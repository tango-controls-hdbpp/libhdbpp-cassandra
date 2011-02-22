//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/DbProxy.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DbProxy.
//						(Chinkumo Jean) - 23 janv. 2005
//
// $Author: ounsy $
//
// $Revision: 1.11 $
//
// $Log: DbProxy.java,v $
// Revision 1.11  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.10  2006/11/09 15:47:36  ounsy
// calls to exportToDB_Scalar2 now renamed to exportToDB_Scalar
//
// Revision 1.9  2006/07/06 09:46:18  ounsy
// modified the exportToDB_XXX so that they call the new file export methods
// (job importing)
//
// Revision 1.8  2006/04/11 09:13:15  ounsy
// added some attribute checking methods
//
// Revision 1.7  2006/02/24 12:08:57  ounsy
// removed useless logs
//
// Revision 1.6  2006/02/13 09:36:49  chinkumo
// Changes made into the DataBaseApi Class were reported here.
// (Methods 'exportToDB_ScalarRO/WO/RW', 'exportToDB_SpectrumRO/WO/RW' and 'exportToDB_ImageRO/WO/RW' were generalized into 'exportToDB_Scalar', 'exportToDB_Spectrum' and 'exportToDB_Image')
//
// Revision 1.5  2006/01/13 14:28:35  chinkumo
// Changes made to avoid multi lines in AMT table when archivers are rebooted.
//
// Revision 1.4  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.3.10.2  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3.10.1  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.3  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.2.6.2  2005/06/13 13:59:17  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.2.6.1  2005/05/11 15:55:22  chinkumo
// Minor change in a logg message.
//
// Revision 1.2  2005/02/04 17:10:40  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.1  2005/01/31 14:51:32  chinkumo
// This class replaces the old TdbProxy class.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package TdbArchiver.Collector;

import java.util.Vector;

import fr.esrf.TangoApi.Database;
import fr.esrf.TangoDs.Util;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.TDBDataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.TdbDataExport.ITdbDataExport;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributeLightMode;

public class DbProxy
{
    private IArchivingManagerApiRef manager;

    public DbProxy()
	{
		try
		{
            this.manager = ArchivingManagerApiRefFactory.getInstance(ConfigConst.TDB);
            this.manager.ArchivingConfigure(TdbArchiver.TdbArchiver.myDbHost, TdbArchiver.TdbArchiver.myDbName, TdbArchiver.TdbArchiver.myDbUser , TdbArchiver.TdbArchiver.myDbPassword , TdbArchiver.TdbArchiver.myRacConnection);
		}
		catch ( ArchivingException e )
		{
			System.err.println(e.toString());
		}
	}

	public boolean is_db_connected()
	{
		return this.manager.is_db_connected();
	}

	public DataBaseManager getDataBase()
	{
		return this.manager.getDataBase();
	}

	public void insertModeRecord(AttributeLightMode attributeLightMode) throws ArchivingException
	{
	    //System.out.println("CLA/insertModeRecord/getAttribute_complete_name/"+attributeLightMode.getAttribute_complete_name()+" BEFORE");
        this.manager.getDataBase().getMode().insertModeRecord(attributeLightMode);
	    //System.out.println("CLA/insertModeRecord/getAttribute_complete_name/"+attributeLightMode.getAttribute_complete_name()+" AFTER");
	}

	public boolean isArchived(String att_name) throws ArchivingException
	{
		Util.out4.println("DbProxy.isArchived");
		try
		{
			return this.manager.getDataBase().getMode().isArchived(att_name);
		}
		catch ( ArchivingException e )
		{
			Util.out2.println(e.toString());
			throw e;
		}
	}

    public boolean isArchived(String att_name, String device_name) throws ArchivingException
    {
        Util.out4.println("DbProxy.isArchived");
        try
        {
            return this.manager.getDataBase().getMode().isArchived(att_name, device_name);
        }
        catch ( ArchivingException e )
        {
            Util.out2.println(e.toString());
            throw e;
        }
    }

	public void updateModeRecord(AttributeLightMode attributeLightMode) throws ArchivingException
	{
        this.manager.getDataBase().getMode().updateModeRecord(attributeLightMode.getAttribute_complete_name());
	}

	public void updateModeRecord(String att_name) throws ArchivingException
	{
	    
	    Util.out4.println("DbProxy.updateModeRecord");
		try
		{
		    //System.out.println("CLA/updateModeRecord/att_name/"+att_name+"/BEFORE");
            this.manager.getDataBase().getMode().updateModeRecord(att_name);
		    //System.out.println("CLA/updateModeRecord/att_name/"+att_name+"/AFTER");
		}
		catch ( ArchivingException e )
		{
			Util.out2.println(e.toString());
			throw e;
		}
		catch ( Throwable t )
		{
		    t.printStackTrace ();
		}
	}

	public void exportToDB_Scalar(String remoteFilePath , String fileName , String tableName, int writable) throws ArchivingException
	{
		//ArchivingManagerApi.getDataBase(false).exportToDB_Scalar(remoteFilePath , fileName , tableName, writable);
        ((ITdbDataExport)((TDBDataBaseManager)this.manager.getDataBase()).getTdbExport()).exportToDB_Data(remoteFilePath, fileName, tableName, writable);
	}

	public void exportToDB_Spectrum(String remoteFilePath , String fileName , String tableName, int writable) throws ArchivingException
	{
		//ArchivingManagerApi.getDataBase(false).exportToDB_Spectrum(remoteFilePath , fileName , tableName, writable);
		((ITdbDataExport)((TDBDataBaseManager)this.manager.getDataBase()).getTdbExport()).exportToDB_Data(remoteFilePath , fileName , tableName, writable);
	}

	public void exportToDB_Image(String remoteFilePath , String fileName , String tableName, int writable) throws ArchivingException
	{
		//ArchivingManagerApi.getDataBase(false).exportToDB_Image(remoteFilePath , fileName , tableName, writable);
		((ITdbDataExport)((TDBDataBaseManager)this.manager.getDataBase()).getTdbExport()).exportToDB_Data(remoteFilePath , fileName , tableName, writable);
	}

	public Vector getArchiverCurrentTasks(String archiverName) //throws ArchivingException , DevFailed
	{
		System.out.println("DbProxy.getArchiverCurrentTasks");
		Vector archiverCurrentTasks = new Vector();
		boolean facility = this.manager.getFacility();
		try
		{
			archiverCurrentTasks = this.manager.getDataBase().getMode().getArchiverCurrentTasks(( ( facility ) ? "//" + ( new Database() ).get_tango_host() + "/" : "" ) + archiverName );
			System.out.println("Current Tasks (" + archiverCurrentTasks.size() + "): .... \n\r\t");
			for ( int i = 0 ; i < archiverCurrentTasks.size() ; i++ )
			{
				AttributeLightMode attributeLightMode = ( AttributeLightMode ) archiverCurrentTasks.elementAt(i);
				System.out.println(">>>>>>>>>>>>\t" + i + "\t<<<<<<<<<<<<");
				System.out.println("attributeLightMode.toString() : \r\n" + attributeLightMode.toString() + "\r\n");
			}
			return archiverCurrentTasks;
		}
		/*catch ( ArchivingException e )
		{
			System.err.println("ERROR !! " + "\r\n" +
			                   "\t Origin : \t " + "DbProxy.getArchiverCurrentTasks" + "\r\n" +
			                   "\t Reason : \t " + e.getClass().getName() + "\r\n" +
			                   "\t Description : \t " + e.getMessage() + "\r\n" +
			                   "\t Additional information : \t " + "Unable to get current tasks..." + "\r\n");
			throw e;
		}
		catch ( DevFailed devFailed )
		{
			Util.out2.println("ERROR !! " + "\r\n" +
			                  "\t Origin : \t " + "DbProxy.getArchiverCurrentTasks" + "\r\n" +
			                  "\t Reason : \t " + "UNKNOWN_ERROR" + "\r\n" +
			                  "\t Description : \t " + devFailed.getMessage() + "\r\n" +
			                  "\t Additional information : \t " + "" + "\r\n");
			throw devFailed;
		}*/
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
	}

	public void deleteOldRecords(long time , String[] attributeList) throws ArchivingException
	{
        ((TDBDataBaseManager)this.manager.getDataBase()).getTdbDeleteAttribute().deleteOldRecords(time , attributeList);
	}
}