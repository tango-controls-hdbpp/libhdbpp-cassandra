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
import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.components.renderers.BensikinTableCellRenderer;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.snapshot.SnapshotAttributeValue;
import fr.soleil.bensikin.models.SpectrumDeltaValueTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

class DummyWindowListenerDelta extends WindowAdapter {

	private SpectrumDeltaValueDialog dialog;

	public DummyWindowListenerDelta(SpectrumDeltaValueDialog dial) {
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
 * @author SOLEIL
 */
public class SpectrumDeltaValueDialog extends CancelableDialog {

	private SpectrumDeltaValueTableModel tableModel;
	private JButton /* set, setAll, */cancel;
	private JTextField valueAll;
	private JTable table;

	// a variable to avoid undesired modification
	private Object[] duplicata;

	private static SpectrumDeltaValueDialog instance;

	public static SpectrumDeltaValueDialog getInstance() {
		return instance;
	}

	public static SpectrumDeltaValueDialog getInstance(boolean forceReload,
			String name, SnapshotAttributeValue value) {
		if (instance == null || forceReload) {
			instance = new SpectrumDeltaValueDialog(name, value);
		}
		return instance;
	}

	private SpectrumDeltaValueDialog(String name, SnapshotAttributeValue value) {
		super(BensikinFrame.getInstance(), name, true);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DummyWindowListenerDelta(this));

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new SpringLayout());

		cancel = new JButton(new CancelAction(Messages
				.getMessage("DIALOGS_SPECTRUM_ATTRIBUTE_CANCEL"), this));
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
		int length = 0;
		if (value == null)
			return;
		Object valO = value.getSpectrumValue();
		if (valO != null && !"NaN".equals(valO) && !"[NaN]".equals(valO)) {
			switch (value.getDataType()) {
			case TangoConst.Tango_DEV_UCHAR:
			case TangoConst.Tango_DEV_CHAR:
				if (valO != null && !"NaN".equals(valO)) {
					duplicata = new Byte[((Byte[]) valO).length];
					for (int i = 0; i < ((Byte[]) valO).length; i++) {
						((Byte[]) duplicata)[i] = ((Byte[]) valO)[i];
					}
					length = ((Byte[]) duplicata).length;
				}
				break;
			case TangoConst.Tango_DEV_ULONG:
			case TangoConst.Tango_DEV_LONG:
				if (valO != null && !"NaN".equals(valO)) {
					duplicata = new Integer[((Integer[]) valO).length];
					for (int i = 0; i < ((Integer[]) valO).length; i++) {
						((Integer[]) duplicata)[i] = ((Integer[]) valO)[i];
					}
					length = ((Integer[]) duplicata).length;
				}
				break;
			case TangoConst.Tango_DEV_USHORT:
			case TangoConst.Tango_DEV_SHORT:
				if (valO != null && !"NaN".equals(valO)) {
					duplicata = new Short[((Short[]) valO).length];
					for (int i = 0; i < ((Short[]) valO).length; i++) {
						((Short[]) duplicata)[i] = ((Short[]) valO)[i];
					}
					length = ((Short[]) duplicata).length;
				}
				break;
			case TangoConst.Tango_DEV_FLOAT:
				if (valO != null && !"NaN".equals(valO)) {
					duplicata = new Float[((Float[]) valO).length];
					for (int i = 0; i < ((Float[]) valO).length; i++) {
						((Float[]) duplicata)[i] = ((Float[]) valO)[i];
					}
					length = ((Float[]) duplicata).length;
				}
				break;
			case TangoConst.Tango_DEV_DOUBLE:
				if (valO != null && !"NaN".equals(valO)) {
					duplicata = new Double[((Double[]) valO).length];
					for (int i = 0; i < ((Double[]) valO).length; i++) {
						((Double[]) duplicata)[i] = ((Double[]) valO)[i];
					}
					length = ((Double[]) duplicata).length;
				}
				break;
			} // end switch(value.getDataType())
		}

		if (length > 0) {
			tableModel = new SpectrumDeltaValueTableModel(value.getDataType(),
					duplicata);
			table = new JTable(tableModel);
			table.setDefaultRenderer(Object.class,
					new BensikinTableCellRenderer());

			GUIUtilities
					.setObjectBackground(table, GUIUtilities.SNAPSHOT_COLOR);
			JScrollPane scrollPane = new JScrollPane(table);
			GUIUtilities.setObjectBackground(scrollPane,
					GUIUtilities.SNAPSHOT_COLOR);
			GUIUtilities.setObjectBackground(scrollPane.getViewport(),
					GUIUtilities.SNAPSHOT_COLOR);
			this.setSize(500, 400);

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
