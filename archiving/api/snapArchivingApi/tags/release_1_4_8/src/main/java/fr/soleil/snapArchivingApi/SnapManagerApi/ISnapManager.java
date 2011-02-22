//+======================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoSnapshoting/SnapManagerApi/ISnapManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ISnapManager.
//						(Garda Laure) - 30 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ISnapManager.java,v $
// Revision 1.4  2007/11/23 09:21:51  pierrejoseph
// The getSnap method content comes from the RealSnapReader class.
//
// Revision 1.3  2005/11/29 17:11:17  chinkumo
// no message
//
// Revision 1.2.2.1  2005/11/15 13:34:38  chinkumo
// no message
//
// Revision 1.2  2005/08/19 14:48:37  chinkumo
// no message
//
// Revision 1.1.4.3  2005/08/04 08:01:16  chinkumo
// The defined constants were removed as they were already defined in the TangoSnapshoting.SnapshotingTools.Tools.GlobalConst class.
//
// Revision 1.1.4.2  2005/08/01 13:51:39  chinkumo
// Classes added for the support of the new graphical application (Bensikin).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.snapArchivingApi.SnapManagerApi;

import java.util.Hashtable;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeHeavy;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * @author GARDA
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ISnapManager
{

	public SnapContext[] findContexts(Criterions criterions) throws SnapshotingException;

	public SnapShotLight[] findSnapshots(Criterions criterions) throws SnapshotingException;

	public SnapAttributeExtract[] findSnapshotAttributes(SnapShotLight snapshot) throws SnapshotingException;

	public SnapContext saveContext(SnapContext context , Hashtable saveOptions) throws SnapshotingException;

	public SnapShot launchSnapshot(SnapContext context) throws SnapshotingException;

	public SnapShotLight updateCommentOfSnapshot(SnapShotLight snapshot , String comment) throws SnapshotingException;

	public void setEquipmentsWithSnapshot(SnapShot snapshot) throws SnapshotingException;

	public SnapAttributeHeavy[] findContextAttributes(SnapContext context , Criterions criterions) throws SnapshotingException;
	
	public SnapAttributeExtract[] getSnap ( int id ) throws SnapshotingException;

	public String setEquipmentsWithCommand(String cmd_name, String option, SnapShot snapshot); 

}
