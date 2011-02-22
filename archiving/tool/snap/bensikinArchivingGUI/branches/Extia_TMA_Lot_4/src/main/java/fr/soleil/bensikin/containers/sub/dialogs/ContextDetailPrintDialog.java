package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.bensikin.actions.context.PrintContextDetailAction;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.context.ContextDataPanel;
import fr.soleil.bensikin.containers.context.ContextDetailPrintPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.tools.Messages;

public class ContextDetailPrintDialog extends JDialog {
	private Context context;
	private JPanel mainPanel;
	private boolean modeText;
	private JButton printButton;
	private JButton cancelButton;
	private ContextDetailPrintPanel printPanel;

	public ContextDetailPrintDialog(Context context, boolean modeText) {
		super(BensikinFrame.getInstance(), true);
		this.context = context;
		this.modeText = modeText;
		initialize();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(300, 300));
		ContextDataPanel panel = ContextDataPanel.getInstance();
		if (panel != null) {
			setLocation(panel.getLocationOnScreen());
		}
	}

	private void initialize() {
		String msg = Messages.getMessage("CONTEXT_DETAIL_PRINT_TITLE");
		setTitle(msg);
		printPanel = new ContextDetailPrintPanel(context, modeText);
		String actionTitle = BensikinFrame.getInstance().getTitle() + " - "
				+ msg;
		printButton = new JButton(new PrintContextDetailAction(msg,
				actionTitle, printPanel));
		actionTitle = null;

		msg = Messages.getMessage("CONTEXT_DETAIL_PRINT_CHOOSE_CANCEL");
		cancelButton = new JButton(msg);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ContextDetailPrintDialog.this.setVisible(false);
			}
		});

		mainPanel = new JPanel();
		mainPanel.setBackground(printPanel.getBackground());

		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints printPanelConstraints = new GridBagConstraints();
		printPanelConstraints.fill = GridBagConstraints.BOTH;
		printPanelConstraints.gridx = 0;
		printPanelConstraints.gridy = 0;
		printPanelConstraints.weightx = 1;
		printPanelConstraints.weighty = 1;
		printPanelConstraints.gridwidth = GridBagConstraints.REMAINDER;

		GridBagConstraints printButtonConstraints = new GridBagConstraints();
		printButtonConstraints.fill = GridBagConstraints.NONE;
		printButtonConstraints.gridx = 0;
		printButtonConstraints.gridy = 1;
		printButtonConstraints.weightx = 0;
		printButtonConstraints.weighty = 0;

		GridBagConstraints glueConstraints = new GridBagConstraints();
		glueConstraints.fill = GridBagConstraints.HORIZONTAL;
		glueConstraints.gridx = 1;
		glueConstraints.gridy = 1;
		glueConstraints.weightx = 1;
		glueConstraints.weighty = 0;

		GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
		cancelButtonConstraints.fill = GridBagConstraints.NONE;
		cancelButtonConstraints.gridx = 2;
		cancelButtonConstraints.gridy = 1;
		cancelButtonConstraints.weightx = 0;
		cancelButtonConstraints.weighty = 0;

		JScrollPane scrollPane = new JScrollPane(printPanel);
		scrollPane.getViewport().setBackground(printPanel.getBackground());
		mainPanel.add(scrollPane, printPanelConstraints);

		mainPanel.add(printButton, printButtonConstraints);

		mainPanel.add(Box.createGlue(), glueConstraints);

		mainPanel.add(cancelButton, cancelButtonConstraints);

		setContentPane(mainPanel);
	}
}
