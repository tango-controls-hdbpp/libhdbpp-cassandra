/*
 * Synchrotron Soleil File : PersistentColumnModel.java Project : bensikin
 * Description : Author : CLAISSE Original : 20 mars 07 Revision: Author: Date:
 * State: Log: PersistentColumnModel.java,v
 */
/*
 * Created on 20 mars 07 To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package fr.soleil.bensikin.components.snapshot.detail;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;

import javax.swing.table.DefaultTableColumnModel;

import fr.soleil.bensikin.xml.PersistentColumnModelEncoder;

public class PersistentColumnModel extends DefaultTableColumnModel {

	private static final long serialVersionUID = 7639295875315680762L;

	public PersistentColumnModel() {
		super();
	}

	public void save() throws FileNotFoundException {
		XMLEncoder encoder = new PersistentColumnModelEncoder();
		encoder.writeObject(this);
		encoder.close();
	}
}
