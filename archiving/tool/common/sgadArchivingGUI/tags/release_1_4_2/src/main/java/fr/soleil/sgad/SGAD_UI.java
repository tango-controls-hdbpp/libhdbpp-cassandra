//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/SGAD_UI.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SGAD_UI.
//						(Chinkumo Jean) - Dec 4, 2004
//
// $Author: chinkumo $
//
// $Revision: 1.7 $
//
// $Log: SGAD_UI.java,v $
// Revision 1.7  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.6  2005/06/14 10:46:19  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.5.4.2  2005/04/29 19:02:27  chinkumo
// Default scripts path are now proposed by the application
//
// Revision 1.5.4.1  2005/04/21 19:32:26  chinkumo
// A new 'Options' menu was added :
// It gives the possibility to choose from the GUI :
// 	The OS type (Linux/Windows)
// 	The wished mode (Standard ie. BASE, SPLIT, PARTITION)
//
// Revision 1.5  2005/03/09 09:47:39  chinkumo
// Index tablespaces were removed since indexes are now directly included inside  tables.
//
// Revision 1.4  2005/02/04 16:58:12  chinkumo
// Outputs when launching the application were removed.
//
// Revision 1.3  2005/02/01 18:50:40  chinkumo
// Changes made to support the InnoDB Storage Engine.
//
// Revision 1.2  2005/01/31 10:25:39  chinkumo
// The application now takes care of the carriage return when on UNIX (\r) or WINDOWS (\r\n) Operating System type. An argument must be given when launching the application (0 -> UNIX ; 1 -> WINDOWS)
//
// Revision 1.1  2005/01/26 10:24:58  chinkumo
// First commit
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.sgad;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class SGAD_UI extends JFrame {
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel2 = new JPanel();
	JButton jButton1 = new JButton();
	JButton jButton2 = new JButton();
	JPanel jPanel3 = new JPanel();
	JLabel jTitle = new JLabel();
	JLabel jLabel4 = new JLabel();
	JTextField jTextField4 = new JTextField();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JLabel jLabel5 = new JLabel();
	JLabel jLabel3 = new JLabel();
	JLabel jLabel2 = new JLabel();
	JLabel jLabel1 = new JLabel();
	JTextField jTextField1 = new JTextField();
	JTextField jTextField2 = new JTextField();
	JTextField jTextField3 = new JTextField();
	JTextField jTextField5 = new JTextField();
	JPanel jPanel0 = new JPanel();
	BorderLayout borderLayout0 = new BorderLayout();
	JPanel jPanel4 = new JPanel();
	JCheckBox jCheckBox1 = new JCheckBox();
	JCheckBox jCheckBox2 = new JCheckBox();
	JCheckBox jCheckBox3 = new JCheckBox();
	JTabbedPane jTabbedPane2 = new JTabbedPane();
	JPanel jPanel5 = new JPanel();
	JLabel jLabel7 = new JLabel();
	JTextField jTextField6 = new JTextField();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	JMenuBar jMenuBar1 = new JMenuBar();
	JMenu jMenu1 = new JMenu();
	JMenuItem jMenuItem1 = new JMenuItem();
	JMenu jMenu2 = new JMenu();
	JMenuItem jMenuItem2 = new JMenuItem();
	JRadioButton jRadioButton1 = new JRadioButton();
	JRadioButton jRadioButton2 = new JRadioButton();
	JLabel jLabel6 = new JLabel();
	JMenu jMenu3 = new JMenu();
	JMenu jMenu4 = new JMenu();
	JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem();
	JMenu jMenu5 = new JMenu();
	JCheckBoxMenuItem jCheckBoxMenuItem1 = new JCheckBoxMenuItem();
	JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem();
	JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem();
	JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem();

	public SGAD_UI() {
		try {
			jbInit();
			initOptions();
			pack();
			setPosition(this);
			setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setPosition(Component component) {
		Dimension dimAppl = component.getSize();
		Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		int ax = dimAppl.width;
		int ay = dimAppl.height;
		int bx = dimScreen.width;
		int by = dimScreen.height;
		int x = (int) (bx - ax) / 2;
		int y = (int) (by - ay) / 2;
		component.setLocation(x, y);
	}

	public static void main(String[] args) {
		new SGAD_UI();
	}

	private void jbInit() throws Exception {
		jTitle.setFont(new java.awt.Font("Dialog", 2, 20));
		jTitle.setRequestFocusEnabled(true);
		jTitle.setText("Script Generator for Archiving Databases");
		jPanel1.setLayout(gridBagLayout1);
		jButton2.setText("Close");
		jButton2.addActionListener(new SGAD_UI_jButton2_actionAdapter(this));
		jButton1.setText("Generate");
		jButton1.addActionListener(new SGAD_UI_jButton1_actionAdapter(this));
		jPanel2.setDebugGraphicsOptions(0);
		this.getContentPane().setLayout(borderLayout1);
		jLabel5.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel5.setToolTipText("");
		jLabel5.setText("Redo log path");
		jLabel3.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel3.setText("Oracle base path");
		jLabel2.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel2.setToolTipText("");
		jLabel2.setText("Output path");
		jLabel1.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel1.setText("Database\'s SID");
		jLabel4.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel4.setText("Oracle home path");
		jPanel0.setLayout(borderLayout0);
		jCheckBox1.setSelected(true);
		jCheckBox1.setText("HDB");
		jCheckBox2.setText("TDB");
		jCheckBox3.setText("SNAP");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setEnabled(true);
		this.setLocale(new java.util.Locale("fr", "FR", ""));
		this.setResizable(false);
		this.setTitle("Script Generator for Archiving Databases");

		jPanel5.setLayout(gridBagLayout2);
		jLabel7.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel7.setMaximumSize(new Dimension(56, 15));
		jLabel7.setToolTipText("");
		jLabel7.setText("Output path");
		jMenu1.setText("Help");
		jMenuItem1.setText("About...");
		jMenuItem1
				.addActionListener(new SGAD_UI_jMenuItem1_actionAdapter(this));
		jMenu2.setText("File");
		jMenuItem2.setText("Exit");
		jMenuItem2
				.addActionListener(new SGAD_UI_jMenuItem2_actionAdapter(this));
		jRadioButton1.setActionCommand("MyISAM");
		jRadioButton1.setSelected(true);
		jRadioButton1.setText("MyISAM");
		jRadioButton1
				.addActionListener(new SGAD_UI_jRadioButton1_actionAdapter(this));
		jRadioButton2.setText("InnoDB");
		jRadioButton2
				.addActionListener(new SGAD_UI_jRadioButton2_actionAdapter(this));
		jLabel6.setFont(new java.awt.Font("Dialog", 0, 11));
		jLabel6.setMaximumSize(new Dimension(56, 15));
		jLabel6.setMinimumSize(new Dimension(56, 15));
		jLabel6.setPreferredSize(new Dimension(56, 15));
		jLabel6.setRequestFocusEnabled(true);
		jLabel6.setText("Storage Engine");
		jMenu3.setText("Options");
		jMenu4.setText("OS");
		jCheckBoxMenuItem2.setText("Windows");
		jCheckBoxMenuItem2
				.addActionListener(new SGAD_UI_jCheckBoxMenuItem2_actionAdapter(
						this));
		jMenu5.setText("Modes");
		jCheckBoxMenuItem1.setText("Linux");
		jCheckBoxMenuItem1
				.addActionListener(new SGAD_UI_jCheckBoxMenuItem1_actionAdapter(
						this));
		jCheckBoxMenuItem3.setText("Standard");
		jCheckBoxMenuItem3
				.addActionListener(new SGAD_UI_jCheckBoxMenuItem3_actionAdapter(
						this));
		jCheckBoxMenuItem4.setText("Splitting");
		jCheckBoxMenuItem4
				.addActionListener(new SGAD_UI_jCheckBoxMenuItem4_actionAdapter(
						this));
		jCheckBoxMenuItem5.setText("Partitionning");
		jCheckBoxMenuItem5
				.addActionListener(new SGAD_UI_jCheckBoxMenuItem5_actionAdapter(
						this));
		jTextField5.addActionListener(new SGAD_UI_jTextField5_actionAdapter(
				this));
		jTextField1.setText("HDB");
		jTextField2.setText("C:\\oracle\\admin\\HDB\\create");
		jTextField3.setText("C:\\oracle");
		jTextField4.setText("C:\\oracle\\ora92");
		jTextField5.setText("C:\\oracle\\redo\\HDB");
		jTextField6.setText("C:\\mysql\\scripts");
		jPanel1.add(jLabel4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 20, 1, 1), 45, 5));
		jPanel1.add(jTextField4, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 20), 200, 7));
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);
		jPanel2.add(jButton1, null);
		jPanel2.add(jButton2, null);
		this.getContentPane().add(jMenuBar1, BorderLayout.NORTH);
		jPanel3.add(jTitle, null);
		jPanel1.add(jLabel5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 20, 20, 1), 45, 5));
		jPanel1.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 20, 1, 1), 45, 5));
		jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 20, 1, 1), 45, 5));
		jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(20, 20, 1, 1), 45, 5));
		jPanel1.add(jTextField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(20, 1, 1, 20), 200, 5));
		jPanel1.add(jTextField2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 20), 200, 5));
		jPanel1.add(jTextField3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 20), 200, 5));
		jPanel1.add(jTextField5, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 20, 20), 200, 5));

		jPanel5.add(jLabel7, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 20, 1, 1), 45, 5));
		jPanel5.add(jTextField6, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 20), 200, 5));
		jPanel5.add(jRadioButton1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
		jPanel5.add(jRadioButton2, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
						0, 0, 0, 0), 0, 0));
		jPanel5.add(jLabel6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 20, 0, 0), 45, 5));

		jPanel0.add(jPanel4, BorderLayout.SOUTH);
		jPanel4.add(jCheckBox1, null);
		jPanel4.add(jCheckBox2, null);
		jPanel4.add(jCheckBox3, null);
		jPanel0.add(jTabbedPane2, BorderLayout.CENTER);
		jTabbedPane2.add(jPanel1, "Oracle");
		jTabbedPane2.add(jPanel5, "MySQL");
		this.getContentPane().add(jPanel0, BorderLayout.CENTER);
		jMenuBar1.add(jMenu2);
		jMenuBar1.add(jMenu3);
		jMenuBar1.add(jMenu1);
		jMenu1.add(jMenuItem1);
		jMenu2.add(jMenuItem2);
		jMenu3.add(jMenu4);
		jMenu3.add(jMenu5);
		jMenu4.add(jCheckBoxMenuItem2);
		jMenu4.add(jCheckBoxMenuItem1);
		jMenu5.add(jCheckBoxMenuItem3);
		jMenu5.add(jCheckBoxMenuItem4);
		jMenu5.add(jCheckBoxMenuItem5);
	}

	private void initOptions() {
		// OS
		jCheckBoxMenuItem1.setSelected(false);
		jCheckBoxMenuItem2.setSelected(true);
		Constants.target_OS = "Windows_NT";
		fr.soleil.sgad.Constants.newLine = "\r\n";
		// Modes
		jCheckBoxMenuItem3.setSelected(true);
		jCheckBoxMenuItem4.setSelected(false);
		jCheckBoxMenuItem5.setSelected(false);
		fr.soleil.sgad.oracle.Constants.generationMode = 0;
	}

	void jButton2_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	void jButton1_actionPerformed(ActionEvent e) {
		if (jTabbedPane2.getSelectedIndex() == 0) {

			fr.soleil.sgad.oracle.Generator gen = new fr.soleil.sgad.oracle.Generator();
			gen.reinitialise();
			if (jCheckBox1.isSelected()) {
				fr.soleil.sgad.oracle.Generator.hdb_generation = true;
			} else {
				fr.soleil.sgad.oracle.Generator.hdb_generation = false;
			}
			if (jCheckBox2.isSelected()) {
				fr.soleil.sgad.oracle.Generator.tdb_generation = true;
			} else {
				fr.soleil.sgad.oracle.Generator.tdb_generation = false;
			}
			if (jCheckBox3.isSelected()) {
				fr.soleil.sgad.oracle.Generator.snap_generation = true;
			} else {
				fr.soleil.sgad.oracle.Generator.snap_generation = false;
			}
			// *********************************
			if ((fr.soleil.sgad.oracle.Generator._oracle_sid = jTextField1
					.getText()).equals("")) {
				Messages.emptyField(this, "SID");
			} else if ((fr.soleil.sgad.oracle.Generator._script_Path = jTextField2
					.getText()).equals("")) {
				Messages.emptyField(this, "Script generation path");
			} else if ((fr.soleil.sgad.oracle.Generator._oracleBase_Path = jTextField3
					.getText()).equals("")) {
				Messages.emptyField(this, "Oracle's base path");
			} else if ((fr.soleil.sgad.oracle.Generator._oracleHome_Path = jTextField4
					.getText()).equals("")) {
				Messages.emptyField(this, "Oracle's home path");
			} else {
				if (jTextField5.getText().equals("")) {
					fr.soleil.sgad.oracle.Generator._redo_path = fr.soleil.sgad.oracle.Generator._oracleBase_Path
							+ File.separator
							+ "redo"
							+ File.separator
							+ fr.soleil.sgad.oracle.Generator._oracle_sid;
				} else {
					fr.soleil.sgad.oracle.Generator._redo_path = jTextField5
							.getText();
				}

				if (!(fr.soleil.sgad.oracle.Generator.hdb_generation)
						& !(fr.soleil.sgad.oracle.Generator.tdb_generation)
						& !(fr.soleil.sgad.oracle.Generator.snap_generation)) {
					Messages.noSchema(this);
				} else {
					gen.generate();
				}
			}

		} else {
			fr.soleil.sgad.mysql.Generator gen = new fr.soleil.sgad.mysql.Generator();
			gen.reinitialise();
			if (jCheckBox1.isSelected()) {
				fr.soleil.sgad.mysql.Generator.hdb_generation = true;
			} else {
				fr.soleil.sgad.mysql.Generator.hdb_generation = false;
			}
			if (jCheckBox2.isSelected()) {
				fr.soleil.sgad.mysql.Generator.tdb_generation = true;
			} else {
				fr.soleil.sgad.mysql.Generator.tdb_generation = false;
			}
			if (jCheckBox3.isSelected()) {
				fr.soleil.sgad.mysql.Generator.snap_generation = true;
			} else {
				fr.soleil.sgad.mysql.Generator.snap_generation = false;
			}
			if ((fr.soleil.sgad.mysql.Generator._script_Path = jTextField6
					.getText()).equals("")) {
				Messages.emptyField(this, "Script generation path");
			} else if (!(fr.soleil.sgad.mysql.Generator.hdb_generation)
					& !(fr.soleil.sgad.mysql.Generator.tdb_generation)
					& !(fr.soleil.sgad.mysql.Generator.snap_generation)) {
				Messages.noSchema(this);
			} else {
				gen.generate();

			}
		}
	}

	void jMenuItem1_actionPerformed(ActionEvent e) {
		Messages.about(this);
	}

	void jMenuItem2_actionPerformed(ActionEvent e) {
		System.exit(0);
	}

	void jRadioButton1_actionPerformed(ActionEvent e) {
		jRadioButton1.setSelected(true);
		jRadioButton2.setSelected(false);
		fr.soleil.sgad.mysql.Constants.storage_engine = fr.soleil.sgad.mysql.Constants.storage_engines[0];
	}

	void jRadioButton2_actionPerformed(ActionEvent e) {
		jRadioButton1.setSelected(false);
		jRadioButton2.setSelected(true);
		fr.soleil.sgad.mysql.Constants.storage_engine = fr.soleil.sgad.mysql.Constants.storage_engines[1];
	}

	void jCheckBoxMenuItem2_actionPerformed(ActionEvent e) {
		jCheckBoxMenuItem1.setSelected(false);
		jCheckBoxMenuItem2.setSelected(true);
		Constants.target_OS = "Windows_NT";
		fr.soleil.sgad.Constants.newLine = "\r\n";
		jTextField1.setText("HDB");
		jTextField2.setText("C:\\oracle\\admin\\HDB\\create");
		jTextField3.setText("C:\\oracle");
		jTextField4.setText("C:\\oracle\\ora92");
		jTextField5.setText("C:\\oracle\\redo\\HDB");
		jTextField6.setText("C:\\mysql\\scripts");
		System.out.println("OS : " + Constants.target_OS);
	}

	void jCheckBoxMenuItem1_actionPerformed(ActionEvent e) {
		jCheckBoxMenuItem1.setSelected(true);
		jCheckBoxMenuItem2.setSelected(false);
		Constants.target_OS = "UNIX";
		fr.soleil.sgad.Constants.newLine = "\n";
		jTextField1.setText("HDB");
		jTextField2.setText("/home/oracle/admin/HDB/create");
		jTextField3.setText("/home/oracle");
		jTextField4.setText("/home/oracle/ora92");
		jTextField5.setText("/home/oracle/redo/HDB");
		jTextField6.setText("/home/mysql/scripts");
		System.out.println("OS : " + Constants.target_OS);
	}

	void jCheckBoxMenuItem3_actionPerformed(ActionEvent e) {
		jCheckBoxMenuItem3.setSelected(true);
		jCheckBoxMenuItem4.setSelected(false);
		jCheckBoxMenuItem5.setSelected(false);
		fr.soleil.sgad.oracle.Constants.generationMode = 0;
		System.out
				.println("Mode : "
						+ fr.soleil.sgad.oracle.Constants.generationModes[fr.soleil.sgad.oracle.Constants.generationMode]);
	}

	void jCheckBoxMenuItem4_actionPerformed(ActionEvent e) {
		jCheckBoxMenuItem3.setSelected(false);
		jCheckBoxMenuItem4.setSelected(true);
		jCheckBoxMenuItem5.setSelected(false);
		fr.soleil.sgad.oracle.Constants.generationMode = 1;
		System.out
				.println("Mode : "
						+ fr.soleil.sgad.oracle.Constants.generationModes[fr.soleil.sgad.oracle.Constants.generationMode]);
	}

	void jCheckBoxMenuItem5_actionPerformed(ActionEvent e) {
		jCheckBoxMenuItem3.setSelected(false);
		jCheckBoxMenuItem4.setSelected(false);
		jCheckBoxMenuItem5.setSelected(true);
		fr.soleil.sgad.oracle.Constants.generationMode = 2;
		System.out
				.println("Mode : "
						+ fr.soleil.sgad.oracle.Constants.generationModes[fr.soleil.sgad.oracle.Constants.generationMode]);
	}

	void jTextField5_actionPerformed(ActionEvent e) {

	}

}

class SGAD_UI_jButton2_actionAdapter implements java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jButton2_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButton2_actionPerformed(e);
	}
}

class SGAD_UI_jButton1_actionAdapter implements java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jButton1_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButton1_actionPerformed(e);
	}
}

class SGAD_UI_jMenuItem1_actionAdapter implements java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jMenuItem1_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jMenuItem1_actionPerformed(e);
	}
}

class SGAD_UI_jMenuItem2_actionAdapter implements java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jMenuItem2_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jMenuItem2_actionPerformed(e);
	}
}

class SGAD_UI_jRadioButton1_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jRadioButton1_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jRadioButton1_actionPerformed(e);
	}
}

class SGAD_UI_jRadioButton2_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jRadioButton2_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jRadioButton2_actionPerformed(e);
	}
}

class SGAD_UI_jCheckBoxMenuItem2_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jCheckBoxMenuItem2_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jCheckBoxMenuItem2_actionPerformed(e);
	}
}

class SGAD_UI_jCheckBoxMenuItem1_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jCheckBoxMenuItem1_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jCheckBoxMenuItem1_actionPerformed(e);
	}
}

class SGAD_UI_jCheckBoxMenuItem3_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jCheckBoxMenuItem3_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jCheckBoxMenuItem3_actionPerformed(e);
	}
}

class SGAD_UI_jCheckBoxMenuItem4_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jCheckBoxMenuItem4_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jCheckBoxMenuItem4_actionPerformed(e);
	}
}

class SGAD_UI_jCheckBoxMenuItem5_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jCheckBoxMenuItem5_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jCheckBoxMenuItem5_actionPerformed(e);
	}
}

class SGAD_UI_jTextField5_actionAdapter implements
		java.awt.event.ActionListener {
	SGAD_UI adaptee;

	SGAD_UI_jTextField5_actionAdapter(SGAD_UI adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jTextField5_actionPerformed(e);
	}
}
