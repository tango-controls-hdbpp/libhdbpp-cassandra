/*
 * Synchrotron Soleil File : ViewStringStateBooleanScalarPanel.java Project :
 * Mambo_CVS Description : Author : SOLEIL Original : 6 mars 2006 Revision:
 * Author: Date: State: Log: ViewStringStateBooleanScalarPanel.java,v
 */
package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.mambo.actions.view.ViewCopyAction;
import fr.soleil.mambo.actions.view.ViewEditCopyAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.MamboFormatableTable;
import fr.soleil.mambo.components.renderers.BooleanTableRenderer;
import fr.soleil.mambo.components.renderers.StateTableRenderer;
import fr.soleil.mambo.components.renderers.StringTableRenderer;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewStringStateBooleanScalarTableModel;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewStringStateBooleanScalarPanel extends MamboCleanablePanel {

	private static final long serialVersionUID = -2477970690362040503L;
	private ViewConfigurationAttribute[] attributes;
	private MamboFormatableTable[] viewTables;
	private JButton[] edit = null, copy = null;
	private JLabel[] nameLabels;
	private JLabel loadingLabel;
	private boolean cleaning = false;
	private final static Insets gapBetweenTables = new Insets(20, 5, 0, 5);
	private final static Dimension refSize = new Dimension(50, 50);
	private final static Insets DEFAULT_INSETS = new Insets(5, 5, 0, 5);

	public ViewStringStateBooleanScalarPanel(
			ViewConfigurationAttribute[] stringScalars,
			ViewConfigurationBean viewConfigurationBean) {
		super();
		initLayout();
		loadingLabel = new JLabel(Messages
				.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
		loadingLabel.setForeground(Color.RED);
		if (loadingLabel == null)
			return;
		GridBagConstraints loadingConstraints = new GridBagConstraints();
		loadingConstraints.fill = GridBagConstraints.HORIZONTAL;
		loadingConstraints.gridx = 0;
		loadingConstraints.gridy = 0;
		loadingConstraints.weightx = 1;
		loadingConstraints.weighty = 0;
		loadingConstraints.insets = new Insets(0, 5, 0, 5);
		add(loadingLabel, loadingConstraints);
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
		attributes = stringScalars;
		if (attributes != null && attributes.length > 0) {
			edit = new JButton[attributes.length];
			copy = new JButton[attributes.length];

			viewTables = new MamboFormatableTable[attributes.length];
			nameLabels = new JLabel[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				nameLabels[i] = new JLabel(attributes[i].getCompleteName());
				viewTables[i] = new MamboFormatableTable(
						new ViewStringStateBooleanScalarTableModel(
								attributes[i].getCompleteName(),
								viewConfigurationBean));
				if (attributes[i].getDataType(viewConfigurationBean
						.getViewConfiguration().getData().isHistoric()) == TangoConst.Tango_DEV_STATE) {
					viewTables[i].setDefaultRenderer(Object.class,
							new StateTableRenderer());
				} else if (attributes[i].getDataType(viewConfigurationBean
						.getViewConfiguration().getData().isHistoric()) == TangoConst.Tango_DEV_STRING) {
					viewTables[i].setDefaultRenderer(Object.class,
							new StringTableRenderer());
				} else {
					viewTables[i].setDefaultRenderer(Object.class,
							new BooleanTableRenderer());
				}
				GUIUtilities.setObjectBackground(nameLabels[i],
						GUIUtilities.VIEW_COLOR);
				GUIUtilities.setObjectBackground(viewTables[i],
						GUIUtilities.VIEW_COLOR);
			}
			addComponents();
		} else {
			Component glue = Box.createGlue();
			GUIUtilities.setObjectBackground(glue, GUIUtilities.VIEW_COLOR);
			GridBagConstraints glueConstraints = new GridBagConstraints();
			glueConstraints.fill = GridBagConstraints.BOTH;
			glueConstraints.gridx = 0;
			glueConstraints.gridy = 1;
			glueConstraints.weightx = 1;
			glueConstraints.weighty = 1;
			add(glue, glueConstraints);
		}
	}

	private void addComponents() {
		double weighty = 1.0d / (double) viewTables.length;
		for (int i = 0; i < viewTables.length; i++) {
			GridBagConstraints titleConstraints = new GridBagConstraints();
			titleConstraints.fill = GridBagConstraints.HORIZONTAL;
			titleConstraints.gridx = 0;
			titleConstraints.gridy = (2 * i) + 1;
			titleConstraints.weightx = 1;
			titleConstraints.weighty = 0;
			if (i > 0) {
				titleConstraints.insets = gapBetweenTables;
			} else {
				titleConstraints.insets = DEFAULT_INSETS;
			}

			GridBagConstraints tableConstraints = new GridBagConstraints();
			tableConstraints.fill = GridBagConstraints.BOTH;
			tableConstraints.gridx = 0;
			tableConstraints.gridy = (2 * i) + 2;
			tableConstraints.weightx = 1;
			tableConstraints.weighty = weighty;
			tableConstraints.insets = DEFAULT_INSETS;

			JPanel titlePanel = new JPanel(new GridBagLayout());
			GUIUtilities.setObjectBackground(titlePanel,
					GUIUtilities.VIEW_COLOR);
			copy[i] = new JButton(
					Messages
							.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_COPY")
							+ " (" + attributes[i].getName() + ")");
			copy[i].setEnabled(false);
			edit[i] = new JButton(
					Messages
							.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_EDIT_COPY")
							+ " (" + attributes[i].getName() + ")");
			edit[i].setEnabled(false);
			copy[i].setMargin(new Insets(0, 0, 0, 0));
			edit[i].setMargin(new Insets(0, 0, 0, 0));
			GUIUtilities.setObjectBackground(copy[i],
					GUIUtilities.VIEW_COPY_COLOR);
			GUIUtilities.setObjectBackground(edit[i],
					GUIUtilities.VIEW_COPY_COLOR);
			if (titlePanel == null)
				return;
			if (nameLabels[i] == null)
				return;
			if (copy[i] == null)
				return;
			if (edit[i] == null)
				return;
			Insets defaultGap = new Insets(0, 0, 0, 5);
			GridBagConstraints nameConstraints = new GridBagConstraints();
			nameConstraints.fill = GridBagConstraints.NONE;
			nameConstraints.gridx = 0;
			nameConstraints.gridy = 0;
			nameConstraints.weightx = 0;
			nameConstraints.weighty = 0;
			nameConstraints.insets = defaultGap;
			titlePanel.add(nameLabels[i], nameConstraints);
			GridBagConstraints copyConstraints = new GridBagConstraints();
			copyConstraints.fill = GridBagConstraints.NONE;
			copyConstraints.gridx = 1;
			copyConstraints.gridy = 0;
			copyConstraints.weightx = 0;
			copyConstraints.weighty = 0;
			copyConstraints.insets = defaultGap;
			titlePanel.add(copy[i], copyConstraints);
			GridBagConstraints editConstraints = new GridBagConstraints();
			editConstraints.fill = GridBagConstraints.NONE;
			editConstraints.gridx = 2;
			editConstraints.gridy = 0;
			editConstraints.weightx = 0;
			editConstraints.weighty = 0;
			editConstraints.insets = defaultGap;
			titlePanel.add(edit[i], editConstraints);
			GridBagConstraints glueConstraints = new GridBagConstraints();
			glueConstraints.fill = GridBagConstraints.HORIZONTAL;
			glueConstraints.gridx = 3;
			glueConstraints.gridy = 0;
			glueConstraints.weightx = 1;
			glueConstraints.weighty = 0;
			titlePanel.add(Box.createGlue(), glueConstraints);
			if (titlePanel == null)
				return;
			add(titlePanel, titleConstraints);
			if (viewTables == null)
				return;
			if (viewTables[i] == null)
				return;
			JScrollPane scrollpane = new JScrollPane(viewTables[i]);
			scrollpane.setMinimumSize(refSize);
			scrollpane.setPreferredSize(refSize);
			GUIUtilities.setObjectBackground(scrollpane,
					GUIUtilities.VIEW_COLOR);
			GUIUtilities.setObjectBackground(scrollpane.getViewport(),
					GUIUtilities.VIEW_COLOR);
			if (scrollpane == null)
				return;
			add(scrollpane, tableConstraints);
		}
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
	}

	public String getSelectedToString(int tableIndex) {
		String s = "";
		if (viewTables != null && tableIndex < viewTables.length) {
			s = nameLabels[tableIndex].getText();
			s += "\n";
			for (int i = 0; i < viewTables[tableIndex].getColumnCount(); i++) {
				s += viewTables[tableIndex].getColumnName(i)
						+ Options.getInstance().getGeneralOptions()
								.getSeparator();
			}
			s = s.substring(0, s.lastIndexOf(Options.getInstance()
					.getGeneralOptions().getSeparator()));
			s += "\n";
			s += viewTables[tableIndex].toString();
		}
		return s;
	}

	public void clean() {
		cleaning = true;
		removeAll();
		loadingLabel = null;
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				attributes[i] = null;
			}
			attributes = null;
		}
		if (viewTables != null) {
			for (int i = 0; i < viewTables.length; i++) {
				if (viewTables[i] != null) {
					if (viewTables[i].getModel() != null
							&& viewTables[i].getModel() instanceof ViewStringStateBooleanScalarTableModel) {
						((ViewStringStateBooleanScalarTableModel) viewTables[i]
								.getModel()).clearData();
					}
					viewTables[i] = null;
				}
			}
			viewTables = null;
		}
		if (nameLabels != null) {
			for (int i = 0; i < nameLabels.length; i++) {
				nameLabels[i] = null;
			}
			nameLabels = null;
		}
		if (edit != null) {
			for (int i = 0; i < edit.length; i++) {
				edit[i] = null;
			}
			edit = null;
		}
		if (copy != null) {
			for (int i = 0; i < copy.length; i++) {
				copy[i] = null;
			}
			copy = null;
		}
	}

	public void loadPanel() {
		IExtractingManager extractingManager = ExtractingManagerFactory
				.getCurrentImpl();

		if (extractingManager.isCanceled() || cleaning)
			return;
		try {
			for (int i = 0; i < viewTables.length; i++) {
				try {
					if (extractingManager.isCanceled() || cleaning)
						return;
					((ViewStringStateBooleanScalarTableModel) viewTables[i]
							.getModel()).loadData();
					copy[i].setEnabled(true);
					copy[i]
							.addActionListener(new ViewCopyAction(
									Messages
											.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_COPY")
											+ " ("
											+ attributes[i].getName()
											+ ")", this, i));
					edit[i].setEnabled(true);
					edit[i]
							.addActionListener(new ViewEditCopyAction(
									Messages
											.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_EDIT_COPY")
											+ " ("
											+ attributes[i].getName()
											+ ")", this, i));
					updateUI();
					repaint();
					loadingLabel.setText(Messages
							.getMessage("VIEW_ATTRIBUTES_LOADED"));
					loadingLabel.setToolTipText(Messages
							.getMessage("VIEW_ATTRIBUTES_LOADED"));
					loadingLabel.setForeground(Color.GREEN);
					updateUI();
					repaint();
				} catch (Exception e) {
					if (extractingManager.isCanceled() || cleaning)
						return;
					else {
						LoggerFactory.getCurrentImpl().trace(
								ILogger.LEVEL_ERROR, e);
						e.printStackTrace();
					}
				}
			}
		} catch (java.lang.OutOfMemoryError oome) {
			if (extractingManager.isCanceled() || cleaning)
				return;
			outOfMemoryErrorManagement();
			return;

		}
	}

	public String getName() {
		return Messages
				.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_TITLE");
	}

	public String getFullName() {
		return Messages
				.getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_TITLE");
	}

	@Override
	public void lightClean() {
		loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
		loadingLabel.setForeground(Color.RED);
		for (int i = 0; i < viewTables.length; i++) {
			((ViewStringStateBooleanScalarTableModel) viewTables[i].getModel())
					.clearData();
		}
	}
}
