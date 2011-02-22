//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotData.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotData.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotData.java,v $
// Revision 1.6  2005/12/14 16:37:50  ounsy
// added methods necessary for the new Word-like file management
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:38  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.snapshot;

import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;

/**
 * Represents the non-attribute dependant informations attached to a snapshot
 * 
 * @author CLAISSE
 */
public class SnapshotData {
	private int id;
	private java.sql.Timestamp time;
	private String comment;
	private String path = null;

	private Snapshot snapshot;

	/**
	 * The XML tag name used for the "id" property
	 */
	public static final String ID_PROPERTY_XML_TAG = "Id";

	/**
	 * The XML tag name used for the "time" property
	 */
	public static final String TIME_PROPERTY_XML_TAG = "Time";

	/**
	 * The XML tag name used for the "comment" property
	 */
	public static final String COMMENT_PROPERTY_XML_TAG = "Comment";

	public static final String IS_MODIFIED_PROPERTY_XML_TAG = "isModified";
	public static final String PATH_PROPERTY_XML_TAG = "path";

	/**
	 * Build a SnapshotData from fields.
	 * 
	 * @param _id
	 *            The snapshot's id
	 * @param _time
	 *            The snapshot's time
	 * @param _comment
	 *            The snapshot's comment
	 */
	public SnapshotData(int _id, java.sql.Timestamp _time, String _comment) {
		this.id = _id;
		this.time = _time;
		this.comment = _comment;
	}

	/**
	 * Convert from a SnapShotLight.
	 * 
	 * @param line
	 *            The SnapShotLight
	 */
	public SnapshotData(SnapShotLight line) {
		this.id = line.getId_snap();
		this.time = line.getSnap_date();
		this.comment = line.getComment();
	}

	/**
	 * Default constructor, does nothing.
	 */
	public SnapshotData() {
	}

	/**
	 * Converts to a SnapShotLight
	 * 
	 * @return A SnapShotLight equivalent 24 juin 2005
	 */
	SnapShotLight getAsSnapShotLight() {
		SnapShotLight ret = new SnapShotLight(this.getId(), this.getTime(),
				this.getComment());
		return ret;
	}

	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the snapshot.
	 */
	public Snapshot getSnapshot() {
		return snapshot;
	}

	/**
	 * @param snapshot
	 *            The snapshot to set.
	 */
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}

	/**
	 * @return Returns the time.
	 */
	public java.sql.Timestamp getTime() {
		return time;
	}

	/**
	 * @param time
	 *            The time to set.
	 */
	public void setTime(java.sql.Timestamp time) {
		this.time = time;
	}

	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
