// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/options/OptionsPrintTab.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OptionsPrintTab.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: OptionsPrintTab.java,v $
// Revision 1.6 2007/08/22 14:47:24 ounsy
// new print system
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:37 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.sub.dialogs.options;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.tools.Messages;

/**
 * The print tab of OptionsDialog, used to set the print options of the
 * application.
 * 
 * @author GIRARDOT
 */
public class OptionsPrintTab extends JPanel {

	private static final long serialVersionUID = 8310887949943919954L;

	private static OptionsPrintTab instance = null;

	private JPanel orientationPanel;
	private ButtonGroup orientationGroup;
	private JPanel portraitPanel;
	private JLabel portraitLabel;
	private JRadioButton portraitButton;
	private JPanel landscapePanel;
	private JLabel landscapeLabel;
	private JRadioButton landscapeButton;

	private JPanel fitPanel;
	private ButtonGroup fitGroup;
	private JPanel fitPagePanel;
	private JLabel fitPageLabel;
	private JRadioButton fitPageButton;
	private JPanel fitWidthPanel;
	private JLabel fitWidthLabel;
	private JRadioButton fitWidthButton;
	private JPanel noFitPanel;
	private JLabel noFitLabel;
	private JRadioButton noFitButton;

	private final static ImageIcon portraitIcon = new ImageIcon(Bensikin.class
			.getResource("icons/portrait.gif"));
	private final static ImageIcon landscapeIcon = new ImageIcon(Bensikin.class
			.getResource("icons/landscape.gif"));

	private final static ImageIcon fitPageIcon = new ImageIcon(Bensikin.class
			.getResource("icons/fitPage.gif"));
	private final static ImageIcon fitWidthIcon = new ImageIcon(Bensikin.class
			.getResource("icons/fitWidth.gif"));
	private final static ImageIcon noFitIcon = new ImageIcon(Bensikin.class
			.getResource("icons/noFit.gif"));

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static OptionsPrintTab getInstance() {
		if (instance == null) {
			instance = new OptionsPrintTab();
		}
		return instance;
	}

	/**
	 * Builds the tab.
	 */
	private OptionsPrintTab() {
		super();
		initialize();
	}

	private void initialize() {

		setLayout(new GridBagLayout());

		initialisePortrait();
		initialiseLandscape();

		String msg = Messages
				.getMessage("DIALOGS_OPTIONS_PRINT_ORIENTATION_BORDER");
		orientationPanel = new JPanel();
		orientationPanel.setLayout(new GridLayout(1, 2));
		orientationPanel.setBorder(new TitledBorder(msg));

		orientationGroup = new ButtonGroup();
		orientationGroup.add(portraitButton);
		orientationPanel.add(portraitPanel);
		orientationGroup.add(landscapeButton);
		orientationPanel.add(landscapePanel);

		initialiseFitPage();
		initialiseFitWidth();
		initialiseNoFit();

		msg = Messages.getMessage("DIALOGS_OPTIONS_PRINT_FIT_BORDER");
		fitPanel = new JPanel();
		fitPanel.setLayout(new GridLayout(1, 3));
		fitPanel.setBorder(new TitledBorder(msg));

		fitGroup = new ButtonGroup();
		fitGroup.add(fitPageButton);
		fitPanel.add(fitPagePanel);
		fitGroup.add(fitWidthButton);
		fitPanel.add(fitWidthPanel);
		fitGroup.add(noFitButton);
		fitPanel.add(noFitPanel);

		Insets gapInsets = new Insets(5, 5, 50, 5);

		GridBagConstraints orientationPanelConstraints = new GridBagConstraints();
		orientationPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		orientationPanelConstraints.gridx = 0;
		orientationPanelConstraints.gridy = 0;
		orientationPanelConstraints.weightx = 1;
		orientationPanelConstraints.weighty = 0;
		orientationPanelConstraints.insets = gapInsets;
		this.add(orientationPanel, orientationPanelConstraints);

		GridBagConstraints fitPanelConstraints = new GridBagConstraints();
		fitPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
		fitPanelConstraints.gridx = 0;
		fitPanelConstraints.gridy = 1;
		fitPanelConstraints.weightx = 1;
		fitPanelConstraints.weighty = 0;
		fitPanelConstraints.insets = gapInsets;
		this.add(fitPanel, fitPanelConstraints);

		GridBagConstraints glueConstraints = new GridBagConstraints();
		glueConstraints.fill = GridBagConstraints.BOTH;
		glueConstraints.gridx = 0;
		glueConstraints.gridy = 2;
		glueConstraints.weightx = 1;
		glueConstraints.weighty = 1;
		this.add(Box.createGlue(), glueConstraints);
	}

	private void initialisePortrait() {
		portraitPanel = new JPanel();
		portraitPanel.setLayout(new GridBagLayout());

		portraitButton = new JRadioButton(Messages
				.getMessage("DIALOGS_OPTIONS_PRINT_ORIENTATION_PORTRAIT"));
		portraitButton.setSelected(false);

		portraitLabel = new JLabel();
		portraitLabel.setIcon(portraitIcon);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 1;

		portraitPanel.add(portraitLabel, labelConstraints);
		portraitPanel.add(portraitButton, buttonConstraints);
	}

	private void initialiseLandscape() {
		landscapePanel = new JPanel();
		landscapePanel.setLayout(new GridBagLayout());

		landscapeButton = new JRadioButton(Messages
				.getMessage("DIALOGS_OPTIONS_PRINT_ORIENTATION_LANDSCAPE"));
		landscapeButton.setSelected(true);

		landscapeLabel = new JLabel();
		landscapeLabel.setIcon(landscapeIcon);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 1;

		landscapePanel.add(landscapeLabel, labelConstraints);
		landscapePanel.add(landscapeButton, buttonConstraints);
	}

	private void initialiseFitPage() {
		fitPagePanel = new JPanel();
		fitPagePanel.setLayout(new GridBagLayout());

		fitPageButton = new JRadioButton(Messages
				.getMessage("DIALOGS_OPTIONS_PRINT_FIT_PAGE"));
		fitPageButton.setSelected(true);

		fitPageLabel = new JLabel();
		fitPageLabel.setIcon(fitPageIcon);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 1;

		fitPagePanel.add(fitPageLabel, labelConstraints);
		fitPagePanel.add(fitPageButton, buttonConstraints);
	}

	private void initialiseFitWidth() {
		fitWidthPanel = new JPanel();
		fitWidthPanel.setLayout(new GridBagLayout());

		fitWidthButton = new JRadioButton(Messages
				.getMessage("DIALOGS_OPTIONS_PRINT_FIT_WIDTH"));
		fitWidthButton.setSelected(false);

		fitWidthLabel = new JLabel();
		fitWidthLabel.setIcon(fitWidthIcon);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 1;

		fitWidthPanel.add(fitWidthLabel, labelConstraints);
		fitWidthPanel.add(fitWidthButton, buttonConstraints);
	}

	private void initialiseNoFit() {
		noFitPanel = new JPanel();
		noFitPanel.setLayout(new GridBagLayout());

		noFitButton = new JRadioButton(Messages
				.getMessage("DIALOGS_OPTIONS_PRINT_FIT_NO"));
		noFitButton.setSelected(false);

		noFitLabel = new JLabel();
		noFitLabel.setIcon(noFitIcon);

		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.fill = GridBagConstraints.NONE;
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;

		GridBagConstraints buttonConstraints = new GridBagConstraints();
		buttonConstraints.fill = GridBagConstraints.NONE;
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 1;

		noFitPanel.add(noFitLabel, labelConstraints);
		noFitPanel.add(noFitButton, buttonConstraints);
	}

	public boolean isLandscape() {
		return landscapeButton.isSelected();
	}

	public boolean isPortrait() {
		return portraitButton.isSelected();
	}

	public boolean isFitPage() {
		return fitPageButton.isSelected();
	}

	public boolean isFitWidth() {
		return fitWidthButton.isSelected();
	}

	public boolean isNoFit() {
		return noFitButton.isSelected();
	}

	public void setLandscape(boolean selected) {
		landscapeButton.setSelected(selected);
	}

	public void setPortrait(boolean selected) {
		portraitButton.setSelected(selected);
	}

	public void setFitPage(boolean selected) {
		fitPageButton.setSelected(selected);
	}

	public void setFitWidth(boolean selected) {
		fitWidthButton.setSelected(selected);
	}

	public void setNoFit(boolean selected) {
		noFitButton.setSelected(selected);
	}

}
