//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/impl/DummySnapManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummySnapManagerImpl.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.7 $
//
// $Log: DummySnapManagerImpl.java,v $
// Revision 1.7  2007/11/23 09:24:14  pierrejoseph
// new method in the ISnapManager interface
//
// Revision 1.6  2006/10/31 16:54:08  ounsy
// milliseconds and null values management
//
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.impl;

import java.util.Hashtable;

import fr.esrf.TangoDs.TangoConst;
import fr.esrf.Tango.AttrWriteType;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.*;


/**
 * A dummy implementation, returns hard-coded values.
 *
 * @author CLAISSE
 */
public class DummySnapManagerImpl implements ISnapManager
{
	/* (non-Javadoc)
	 * @see fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager#findContexts(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
	 */
	public SnapContext[] findContexts(Criterions searchCriterions) throws SnapshotingException
	{
		SnapContext data1 = new SnapContext("CLA" , "Context 1" , 1 , java.sql.Date.valueOf("2005-08-20") , "Reason 1" , "Description 1");
		SnapContext data2 = new SnapContext("CLA" , "Context 2" , 2 , java.sql.Date.valueOf("2004-07-13") , "Reason 2" , "Description 2");
		SnapContext[] val = null;
		Condition[] idConditions = searchCriterions.getConditions(GlobalConst.TAB_CONTEXT[ 0 ]);

		if ( idConditions != null )
		{
			val = new SnapContext[ 1 ];
			String id_s = idConditions[ 0 ].getValue();
			int id = Integer.parseInt(id_s);
			switch ( id )
			{
				case 1:
					val[ 0 ] = data1;
					break;

				case 2:
					val[ 0 ] = data2;
					break;

				default :
					val[ 0 ] = data1;
					break;
			}
		}
		else
		{
			//System.out.println ( "ID=XXXXX/" );
			val = new SnapContext[ 2 ];
			val[ 0 ] = data1;
			val[ 1 ] = data2;
		}

		return val;
	}

	/* (non-Javadoc)
	 * @see manager.ISnapManager#findSnapshots(java.util.Hashtable)
	 */
	public SnapShotLight[] findSnapshots(Criterions searchCriterions) throws SnapshotingException
	{
		SnapShotLight data1 = new SnapShotLight(1 , java.sql.Timestamp.valueOf("2005-08-20 16:24:44") , "Snapshot de référence");
		SnapShotLight data2 = new SnapShotLight(2 , java.sql.Timestamp.valueOf("2005-07-19 15:11:33") , "Snapshot sur équipement en panne");

		Condition[] idConditions = searchCriterions.getConditions(GlobalConst.TAB_SNAP[ 0 ]);
		SnapShotLight[] val;

		if ( idConditions != null )
		{
			val = new SnapShotLight[ 1 ];
			String id_s = idConditions[ 0 ].getValue();
			int id = Integer.parseInt(id_s);
			switch ( id )
			{
				case 1:
					val[ 0 ] = data1;
					break;

				case 2:
					val[ 0 ] = data2;
					break;

				default :
					val[ 0 ] = data1;
					break;
			}
		}
		else
		{
			//System.out.println ( "ID=XXXXX/" );
			val = new SnapShotLight[ 2 ];
			val[ 0 ] = data1;
			val[ 1 ] = data2;
		}

		return val;
	}

	/* (non-Javadoc)
	 * @see manager.ISnapManager#findSnapshotAttributes(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight)
	 */
	public SnapAttributeExtract[] findSnapshotAttributes(SnapShotLight snapshot) throws SnapshotingException
	{

		String[] argin1 = {"tango/tangotest/1/attr1", "10", String.valueOf(TangoConst.Tango_DEV_DOUBLE), String.valueOf(SnapshotAttributeValue.SCALAR_DATA_FORMAT), String.valueOf(AttrWriteType._READ_WRITE)};
		String[] argin2 = {"tango/tangotest/1/attr2", "11", String.valueOf(TangoConst.Tango_DEV_DOUBLE), String.valueOf(SnapshotAttributeValue.SCALAR_DATA_FORMAT), String.valueOf(AttrWriteType._READ)};
		String[] argin3 = {"tango/tangotest/1/attr3", "12", String.valueOf(TangoConst.Tango_DEV_STRING), String.valueOf(SnapshotAttributeValue.SCALAR_DATA_FORMAT), String.valueOf(AttrWriteType._READ_WRITE)};

		SnapAttributeLight snapAttributeLight1 = new SnapAttributeLight(argin1);
		SnapAttributeLight snapAttributeLight2 = new SnapAttributeLight(argin2);
		SnapAttributeLight snapAttributeLight3 = new SnapAttributeLight(argin3);

		SnapAttributeExtract data1 = new SnapAttributeExtract(snapAttributeLight1);
		SnapAttributeExtract data2 = new SnapAttributeExtract(snapAttributeLight2);
		SnapAttributeExtract data3 = new SnapAttributeExtract(snapAttributeLight3);
		data1.setId_snap(1);
		data1.setSnap_date(java.sql.Timestamp.valueOf("2005-07-19 15:11:33"));
		Double[] value1 = new Double[ 2 ];
		value1[ 0 ] = new Double(0.6768768768); //read
		value1[ 1 ] = new Double(0.4654654645); //write
		data1.setValue(value1);

		data2.setId_snap(1);
		data2.setSnap_date(java.sql.Timestamp.valueOf("2005-07-19 15:11:33"));
		Double value2 = new Double(2.4567465);
		data2.setValue(value2);

		data3.setId_snap(1);
		data1.setSnap_date(java.sql.Timestamp.valueOf("2005-07-19 15:11:33"));
		String[] value3 = new String[ 2 ];
		value3[ 0 ] = "read value"; //read
		value3[ 1 ] = " write value"; //write
		data3.setValue(value3);

		SnapAttributeExtract[] val = new SnapAttributeExtract[ 3 ];
		val[ 0 ] = data1;
		val[ 1 ] = data2;
		val[ 2 ] = data3;

		return val;
	}

	/* (non-Javadoc)
	 * @see manager.ISnapManager#saveContext(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext, java.util.Hashtable)
	 */
	public SnapContext saveContext(SnapContext context , Hashtable saveOptions) throws SnapshotingException
	{
		context.setId(10);
		context.setCreation_date(new java.sql.Date(System.currentTimeMillis()));

		return context;
	}

	/* (non-Javadoc)
	 * @see manager.ISnapManager#launchSnapshot(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext)
	 */
	public SnapShot launchSnapshot(SnapContext context) throws SnapshotingException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see manager.ISnapManager#updateCommentOfSnapshot(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight, java.lang.String)
	 */
	public SnapShotLight updateCommentOfSnapshot(SnapShotLight snapshot , String comment) throws SnapshotingException
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see manager.ISnapManager#setEquipmentsWithSnapshot(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot)
	 */
	public void setEquipmentsWithSnapshot(SnapShot snapshot) throws SnapshotingException
	{
		// TODO Auto-generated method stub

	}
	public SnapAttributeExtract[] getSnap ( int id ) throws SnapshotingException
	{
		return null;
	}
	/* (non-Javadoc)
	 * @see manager.ISnapManager#findContextAttributes(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext, java.lang.String)
	 */
	public SnapAttributeHeavy[] findContextAttributes(SnapContext context , Criterions searchCriterions) throws SnapshotingException
	{
		SnapAttributeHeavy[] val = new SnapAttributeHeavy[ 8 ];

		val[ 0 ] = new SnapAttributeHeavy();
		val[ 1 ] = new SnapAttributeHeavy();
		val[ 2 ] = new SnapAttributeHeavy();
		val[ 3 ] = new SnapAttributeHeavy();
		val[ 4 ] = new SnapAttributeHeavy();
		val[ 5 ] = new SnapAttributeHeavy();
		val[ 6 ] = new SnapAttributeHeavy();
		val[ 7 ] = new SnapAttributeHeavy();

		val[ 0 ].setDomain("tango");
		val[ 1 ].setDomain("tango");
		val[ 2 ].setDomain("tango");
		val[ 3 ].setDomain("tango");
		val[ 4 ].setDomain("tango");
		val[ 5 ].setDomain("tango");
		val[ 6 ].setDomain("tango");
		val[ 7 ].setDomain("tango");

		val[ 0 ].setFamily("tangotest");
		val[ 1 ].setFamily("tangotest");
		val[ 2 ].setFamily("tangotest");
		val[ 3 ].setFamily("tangotest");
		val[ 4 ].setFamily("tangotest");
		val[ 5 ].setFamily("tangotest");
		val[ 6 ].setFamily("tangotest");
		val[ 7 ].setFamily("tangotest");

		val[ 0 ].setMember("1");
		val[ 1 ].setMember("1");
		val[ 2 ].setMember("1");
		val[ 3 ].setMember("1");
		val[ 4 ].setMember("1");
		val[ 5 ].setMember("1");
		val[ 6 ].setMember("1");
		val[ 7 ].setMember("1");

		if ( context.getId() == 1 )
		{
			val[ 0 ].setAttribute_name("Attr1");
			val[ 1 ].setAttribute_name("Attr2");
			val[ 2 ].setAttribute_name("Attr3");
			val[ 3 ].setAttribute_name("Attr4");
			val[ 4 ].setAttribute_name("Attr5");
			val[ 5 ].setAttribute_name("Attr6");
			val[ 6 ].setAttribute_name("Attr7");
			val[ 7 ].setAttribute_name("Attr8");
		}
		else
		{
			val[ 0 ].setAttribute_name("Bttr1");
			val[ 1 ].setAttribute_name("Bttr2");
			val[ 2 ].setAttribute_name("Bttr3");
			val[ 3 ].setAttribute_name("Bttr4");
			val[ 4 ].setAttribute_name("Bttr5");
			val[ 5 ].setAttribute_name("Bttr6");
			val[ 6 ].setAttribute_name("Bttr7");
			val[ 7 ].setAttribute_name("Bttr8");
		}

		return val;
	}

	public String setEquipmentsWithCommand(String cmd_name, String option,
			SnapShot snapShot) {
		// TODO Auto-generated method stub
		return "";
	}

}
