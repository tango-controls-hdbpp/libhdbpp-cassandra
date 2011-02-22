package fr.soleil.bensikin.containers.snapshot;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailPrintTable;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.tools.Messages;

public class SnapshotDetailPrintPanel extends JPanel {

	public final static int MODE_TABLE = 0;
	public final static int MODE_TEXT = 1;

	private SnapshotDetailPrintTable table = null;

	private JLabel idTitleLabel = null;
	private JLabel idLabel = null;

	private JLabel pathTitleLabel = null;
	private JLabel pathLabel = null;

	private JLabel timeTitleLabel = null;
	private JLabel timeLabel = null;

	private JLabel commentTitleLabel = null;
	private JLabel commentLabel = null;

	private JLabel detailTitleLabel = null;
	private JTextArea detailTextArea = null;

	public SnapshotDetailPrintPanel(SnapshotDetailPrintTable table, int mode) {
		super();
		this.table = table;
		initialize(mode);
	}

	private void initialize(int mode) {
		setBackground(Color.WHITE);
		idTitleLabel = new JLabel();
		idTitleLabel.setText(Messages.getMessage("SNAPSHOT_LIST_LABELS_ID")
				+ ":");
		idLabel = new JLabel();

		pathTitleLabel = new JLabel();
		pathTitleLabel.setText(Messages.getMessage("SNAPSHOT_LIST_LABELS_PATH")
				+ ":");
		pathLabel = new JLabel();

		timeTitleLabel = new JLabel();
		timeTitleLabel.setText(Messages.getMessage("SNAPSHOT_LIST_LABELS_TIME")
				+ ":");
		timeLabel = new JLabel();

		commentTitleLabel = new JLabel();
		commentTitleLabel.setText(Messages
				.getMessage("SNAPSHOT_LIST_LABELS_COMMENT")
				+ ":");
		commentLabel = new JLabel();

		detailTitleLabel = new JLabel();
		detailTitleLabel.setText(Messages.getMessage("SNAPSHOT_DETAIL_VALUES")
				+ ":");

		if (table != null) {
			Snapshot snapshot = table.getSnapshot();
			if (snapshot != null && snapshot.getSnapshotData() != null) {
				idLabel.setText(Integer.toString(snapshot.getSnapshotData()
						.getId()));
				pathLabel.setText(snapshot.getSnapshotData().getPath());
				timeLabel.setText(snapshot.getSnapshotData().getTime()
						.toString());
				commentLabel.setText(snapshot.getSnapshotData().getComment());
			}
		}

		if (mode == MODE_TEXT) {
			detailTextArea = new JTextArea();
			detailTextArea.setEditable(false);
			detailTextArea.setBackground(getBackground());
			detailTextArea.setBorder(new LineBorder(Color.BLACK, 1));
			if (table != null) {
				String text = table.toUserFriendlyString();
				detailTextArea.setText(text);
			}
		}

		addComponents();
	}

	private void addComponents() {
		setLayout(new GridBagLayout());

		GridBagConstraints idTitleLabelConstraints = new GridBagConstraints();
		idTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		idTitleLabelConstraints.gridx = 0;
		idTitleLabelConstraints.gridy = 0;
		idTitleLabelConstraints.weightx = 0;
		idTitleLabelConstraints.weighty = 0;
		GridBagConstraints idLabelConstraints = new GridBagConstraints();
		idLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		idLabelConstraints.gridx = 1;
		idLabelConstraints.gridy = 0;
		idLabelConstraints.weightx = 1;
		idTitleLabelConstraints.weighty = 0;
		add(idTitleLabel, idTitleLabelConstraints);
		add(idLabel, idLabelConstraints);

		GridBagConstraints pathTitleLabelConstraints = new GridBagConstraints();
		pathTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		pathTitleLabelConstraints.gridx = 0;
		pathTitleLabelConstraints.gridy = 1;
		pathTitleLabelConstraints.weightx = 0;
		pathTitleLabelConstraints.weighty = 0;
		GridBagConstraints pathLabelConstraints = new GridBagConstraints();
		pathLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		pathLabelConstraints.gridx = 1;
		pathLabelConstraints.gridy = 1;
		pathLabelConstraints.weightx = 1;
		pathTitleLabelConstraints.weighty = 0;
		add(pathTitleLabel, pathTitleLabelConstraints);
		add(pathLabel, pathLabelConstraints);

		GridBagConstraints timeTitleLabelConstraints = new GridBagConstraints();
		timeTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeTitleLabelConstraints.gridx = 0;
		timeTitleLabelConstraints.gridy = 2;
		timeTitleLabelConstraints.weightx = 0;
		timeTitleLabelConstraints.weighty = 0;
		GridBagConstraints timeLabelConstraints = new GridBagConstraints();
		timeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		timeLabelConstraints.gridx = 1;
		timeLabelConstraints.gridy = 2;
		timeLabelConstraints.weightx = 1;
		timeTitleLabelConstraints.weighty = 0;
		add(timeTitleLabel, timeTitleLabelConstraints);
		add(timeLabel, timeLabelConstraints);

		GridBagConstraints commentTitleLabelConstraints = new GridBagConstraints();
		commentTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		commentTitleLabelConstraints.gridx = 0;
		commentTitleLabelConstraints.gridy = 3;
		commentTitleLabelConstraints.weightx = 0;
		commentTitleLabelConstraints.weighty = 0;
		GridBagConstraints commentLabelConstraints = new GridBagConstraints();
		commentLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		commentLabelConstraints.gridx = 1;
		commentLabelConstraints.gridy = 3;
		commentLabelConstraints.weightx = 1;
		commentTitleLabelConstraints.weighty = 0;
		add(commentTitleLabel, commentTitleLabelConstraints);
		add(commentLabel, commentLabelConstraints);

		GridBagConstraints glueConstraints = new GridBagConstraints();
		glueConstraints.fill = GridBagConstraints.HORIZONTAL;
		glueConstraints.gridx = 0;
		glueConstraints.gridy = 4;
		glueConstraints.weightx = 1;
		glueConstraints.weighty = 0;
		glueConstraints.gridwidth = GridBagConstraints.REMAINDER;
		Component glue = Box.createGlue();
		glue.setPreferredSize(idTitleLabel.getPreferredSize());
		add(glue, glueConstraints);

		GridBagConstraints detailTitleLabelConstraints = new GridBagConstraints();
		detailTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
		detailTitleLabelConstraints.gridx = 0;
		detailTitleLabelConstraints.gridy = 5;
		detailTitleLabelConstraints.weightx = 1;
		detailTitleLabelConstraints.weighty = 0;
		detailTitleLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
		add(detailTitleLabel, detailTitleLabelConstraints);

		if (table != null) {
			if (detailTextArea == null) {
				GridBagConstraints headerConstraints = new GridBagConstraints();
				headerConstraints.fill = GridBagConstraints.HORIZONTAL;
				headerConstraints.gridx = 0;
				headerConstraints.gridy = 6;
				headerConstraints.weightx = 0;
				headerConstraints.weighty = 0;
				headerConstraints.gridwidth = GridBagConstraints.REMAINDER;
				GridBagConstraints tableConstraints = new GridBagConstraints();
				tableConstraints.fill = GridBagConstraints.BOTH;
				tableConstraints.gridx = 0;
				tableConstraints.gridy = 7;
				tableConstraints.weightx = 1;
				tableConstraints.weighty = 1;
				tableConstraints.gridwidth = GridBagConstraints.REMAINDER;
				add(table.getTableHeader(), headerConstraints);
				add(table, tableConstraints);
				table.packAll();
			} else {
				GridBagConstraints textConstraints = new GridBagConstraints();
				textConstraints.fill = GridBagConstraints.BOTH;
				textConstraints.gridx = 0;
				textConstraints.gridy = 6;
				textConstraints.weightx = 1;
				textConstraints.weighty = 1;
				textConstraints.gridwidth = GridBagConstraints.REMAINDER;
				add(detailTextArea, textConstraints);
			}
		}
	}

}
