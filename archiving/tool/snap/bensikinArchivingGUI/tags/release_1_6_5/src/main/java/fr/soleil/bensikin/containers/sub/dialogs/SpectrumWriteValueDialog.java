/*	Synchrotron Soleil 
 *  
 *   File          :  SpectrumWriteValueDialog.java
 *  
 *   Project       :  Bensikin_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  14 févr. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: SpectrumWriteValueDialog.java,v 
 *
 */
package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.actions.snapshot.SnapSpectrumCancelAction;
import fr.soleil.bensikin.actions.snapshot.SnapSpectrumSetAction;
import fr.soleil.bensikin.actions.snapshot.SnapSpectrumSetAllAction;
import fr.soleil.bensikin.components.editors.SnapSpectrumWriteTableEditor;
import fr.soleil.bensikin.components.editors.SnapshotWriteValueBooleanEditor;
import fr.soleil.bensikin.components.renderers.BensikinTableCellRenderer;
import fr.soleil.bensikin.components.renderers.SnapshotWriteValueBooleanRenderer;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.models.SpectrumWriteValueTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

class DummyWindowListener extends WindowAdapter {

	private SpectrumWriteValueDialog dialog;

	public DummyWindowListener(SpectrumWriteValueDialog dial) {
		super();
		dialog = dial;
	}

	public void windowClosing(WindowEvent e) {
		dialog.cancel();
	}

	public void windowClosed(WindowEvent e) {
		dialog.cancel();
	}

}

/**
 * 
 * @author SOLEIL
 */
public class SpectrumWriteValueDialog extends JDialog {

	private SpectrumWriteValueTableModel tableModel;
	private JButton set, setAll, cancel;
	private JTextField valueAll;
	private JTable table;

	// a variable to avoid undesired modification
	private Object[] duplicata;

	private static SpectrumWriteValueDialog instance;

	public static SpectrumWriteValueDialog getInstance() {
		return instance;
	}

	public static SpectrumWriteValueDialog getInstance(boolean forceReload,
			String name, SnapshotAttributeValue value) {
		if (instance == null || forceReload) {
			instance = new SpectrumWriteValueDialog(name, value);
		}
		return instance;
	}

	private SpectrumWriteValueDialog(String name, SnapshotAttributeValue value) {
		super(BensikinFrame.getInstance(), name, true);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DummyWindowListener(this));
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new SpringLayout());
		cancel = new JButton(new SnapSpectrumCancelAction(Messages
				.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_CANCEL")));
		cancel.setMargin(new Insets(0, 0, 0, 0));
		cancel.setBackground(Color.RED);
		cancel.setForeground(Color.WHITE);
		JPanel cancelPanel = new JPanel();
		cancelPanel.add(cancel);
		cancelPanel.add(Box.createHorizontalGlue());
		cancelPanel.setLayout(new SpringLayout());
		GUIUtilities.setObjectBackground(cancelPanel,
				GUIUtilities.SNAPSHOT_COLOR);
		SpringUtilities.makeCompactGrid(cancelPanel, 1, cancelPanel
				.getComponentCount(), // rows, cols
				0, 0, // initX, initY
				0, 0, // xPad, yPad
				true);
		myPanel.add(cancelPanel);
		GUIUtilities.setObjectBackground(myPanel, GUIUtilities.SNAPSHOT_COLOR);
		duplicata = null;
		if (value == null)
			return;
		Object val = value.getSpectrumValue();
		if (val != null && !"NaN".equals(val) && !"[NaN]".equals(val)) {
			switch (value.getDataType()) {
			case TangoConst.Tango_DEV_BOOLEAN:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new Boolean[((Boolean[]) val).length];
					for (int i = 0; i < ((Boolean[]) val).length; i++) {
						((Boolean[]) duplicata)[i] = ((Boolean[]) val)[i];
					}
				}
				break;
			case TangoConst.Tango_DEV_STRING:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new String[((String[]) val).length];
					for (int i = 0; i < ((String[]) val).length; i++) {
						((String[]) duplicata)[i] = new String(
								((String[]) val)[i]);
					}
				}
				break;
			case TangoConst.Tango_DEV_UCHAR:
			case TangoConst.Tango_DEV_CHAR:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new Byte[((Byte[]) val).length];
					for (int i = 0; i < ((Byte[]) val).length; i++) {
						((Byte[]) duplicata)[i] = ((Byte[]) val)[i];
					}
				}
				break;
			case TangoConst.Tango_DEV_ULONG:
			case TangoConst.Tango_DEV_LONG:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new Integer[((Integer[]) val).length];
					for (int i = 0; i < ((Integer[]) val).length; i++) {
						((Integer[]) duplicata)[i] = ((Integer[]) val)[i];
					}
				}
				break;
			case TangoConst.Tango_DEV_USHORT:
			case TangoConst.Tango_DEV_SHORT:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new Short[((Short[]) val).length];
					for (int i = 0; i < ((Short[]) val).length; i++) {
						((Short[]) duplicata)[i] = ((Short[]) val)[i];
					}
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new Float[((Float[]) val).length];
					for (int i = 0; i < ((Float[]) val).length; i++) {
						((Float[]) duplicata)[i] = ((Float[]) val)[i];
					}
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (val != null && !"NaN".equals(val)) {
					duplicata = new Double[((Double[]) val).length];
					for (int i = 0; i < ((Double[]) val).length; i++) {
						((Double[]) duplicata)[i] = ((Double[]) val)[i];
					}
				}
				break;
			} // end switch(value.getDataType())
			tableModel = new SpectrumWriteValueTableModel(value.getDataType(),
					duplicata);
			table = new JTable(tableModel);
			if (value.getDataType() == TangoConst.Tango_DEV_BOOLEAN) {
				table.setDefaultRenderer(Object.class,
						new SnapshotWriteValueBooleanRenderer());
				table.setDefaultEditor(Object.class,
						new SnapshotWriteValueBooleanEditor());
			} else {
				table.setDefaultRenderer(Object.class,
						new BensikinTableCellRenderer());
				table.setDefaultEditor(Object.class,
						new SnapSpectrumWriteTableEditor(table));
			}
			GUIUtilities
					.setObjectBackground(table, GUIUtilities.SNAPSHOT_COLOR);
			JScrollPane scrollPane = new JScrollPane(table);
			GUIUtilities.setObjectBackground(scrollPane,
					GUIUtilities.SNAPSHOT_COLOR);
			GUIUtilities.setObjectBackground(scrollPane.getViewport(),
					GUIUtilities.SNAPSHOT_COLOR);
			this.setSize(500, 400);
			JPanel setPanel = new JPanel();
			GUIUtilities.setObjectBackground(setPanel,
					GUIUtilities.SNAPSHOT_COLOR);
			setPanel.setLayout(new SpringLayout());
			set = new JButton(new SnapSpectrumSetAction(Messages
					.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_SET")));
			setAll = new JButton(new SnapSpectrumSetAllAction(Messages
					.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_SETALL")));
			set.setBackground(new Color(0, 150, 0));
			set.setForeground(Color.WHITE);
			setAll.setBackground(new Color(0, 150, 0));
			setAll.setForeground(Color.WHITE);
			set.setMargin(new Insets(0, 0, 0, 0));
			setAll.setMargin(new Insets(0, 0, 0, 0));
			valueAll = new JTextField("0");
			setPanel.add(set);
			setPanel.add(Box.createHorizontalGlue());
			setPanel.add(setAll);
			setPanel.add(valueAll);
			SpringUtilities.makeCompactGrid(setPanel, 1, setPanel
					.getComponentCount(), // rows, cols
					0, 0, // initX, initY
					0, 0, // xPad, yPad
					true);
			myPanel.add(setPanel);
			myPanel.add(scrollPane);
		} else {
			JLabel label = new JLabel(Messages
					.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_NAN"),
					JLabel.CENTER);
			GUIUtilities
					.setObjectBackground(label, GUIUtilities.SNAPSHOT_COLOR);
			myPanel.add(label);
			this.setSize(300, 300);
		}
		SpringUtilities.makeCompactGrid(myPanel, myPanel.getComponentCount(),
				1, // rows, cols
				0, 0, // initX, initY
				0, 0, // xPad, yPad
				true);
		this.setContentPane(myPanel);
		this.setLocation((BensikinFrame.getInstance().getX() + BensikinFrame
				.getInstance().getWidth())
				- (this.getWidth() + 50),
				(BensikinFrame.getInstance().getY() + BensikinFrame
						.getInstance().getHeight())
						- (this.getHeight() + 50));
	}

	public Object[] getValues() {
		if (duplicata != null) {
			return tableModel.getValues();
		}
		return null;
	}

	public boolean isCanceled() {
		if (duplicata != null) {
			return (!tableModel.isCanSet());
		}
		return true;
	}

	public void cancel() {
		if (duplicata != null) {
			tableModel.setCanSet(false);
		}
		this.setVisible(false);
	}

	public void setAll() {
		if (duplicata != null) {
			tableModel.setAll(valueAll.getText());
		}
	}

	public void closeToSet() {
		if (duplicata != null) {
			if (table.isEditing()) {
				JTextField textField = (JTextField) table.getEditorComponent();
				tableModel.setValueAt(textField.getText(), table
						.getEditingRow(), table.getEditingColumn());
				table.getDefaultEditor(Object.class).stopCellEditing();
			}
			tableModel.setCanSet(true);
			setVisible(false);
		}
	}

}
