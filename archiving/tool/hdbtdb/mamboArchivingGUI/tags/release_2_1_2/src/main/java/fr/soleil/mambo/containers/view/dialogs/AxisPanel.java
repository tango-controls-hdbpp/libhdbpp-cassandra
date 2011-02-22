// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AxisPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AxisPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: AxisPanel.java,v $
// Revision 1.8 2007/03/07 14:47:14 ounsy
// correction of Mantis bug 3273 (chart properties panel size)
//
// Revision 1.7 2006/11/06 09:28:05 ounsy
// icons reorganization
//
// Revision 1.6 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.5 2006/07/28 10:07:13 ounsy
// icons moved to "icons" package
//
// Revision 1.4 2006/06/20 08:40:27 ounsy
// by default, y1 is visible and autoscaled
//
// Revision 1.3 2005/12/15 11:31:34 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.soleil.mambo.actions.view.listeners.YAxisMouseListener;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class AxisPanel extends JPanel {

	private static final long serialVersionUID = -4885230234402093983L;

	protected int type;

	protected JPanel scalePanel;
	protected JPanel settingPanel;

	protected JLabel minLabel;
	protected JTextField minText;
	protected JLabel maxLabel;
	protected JTextField maxText;
	protected JCheckBox autoScaleCheck;

	protected JLabel scaleLabel;
	protected JComboBox scaleCombo;
	protected JCheckBox subGridCheck;
	protected JCheckBox visibleCheck;
	protected JCheckBox oppositeCheck;

	protected JComboBox formatCombo;
	protected JLabel formatLabel;

	protected JLabel titleLabel;
	protected JTextField titleText;

	protected JLabel colorLabel;
	protected JLabel colorView;
	protected JButton colorBtn;

	protected JLabel positionLabel;
	protected JComboBox positionCombo;

	public final static int Y1_TYPE = 1;
	public final static int Y2_TYPE = 2;
	public final static int X_TYPE = 3;

	final static Color fColor = new Color(99, 97, 156);
	final static Insets zInset = new Insets(0, 0, 0, 0);
	final static Font labelFont = new Font("Dialog", Font.PLAIN, 12);
	final static Font labelbFont = new Font("Dialog", Font.BOLD, 12);

	protected AxisPanel(int axisType) {
		type = axisType;
		setLayout(null);

		scalePanel = new JPanel();
		scalePanel.setLayout(null);
		String msg;

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE");
		scalePanel.setBorder(GUIUtilities.getPlotSubPanelsEtchedBorder(msg));

		settingPanel = new JPanel();
		settingPanel.setLayout(null);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS");
		settingPanel.setBorder(GUIUtilities.getPlotSubPanelsEtchedBorder(msg));

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE_MIN");
		minLabel = new JLabel(msg);
		minLabel.setFont(labelFont);
		minText = new JTextField();
		minLabel.setForeground(fColor);
		minText.setEditable(true);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE_MAX");
		maxLabel = new JLabel(msg);
		maxLabel.setFont(labelFont);
		maxText = new JTextField();
		maxLabel.setForeground(fColor);
		maxLabel.setHorizontalAlignment(JLabel.RIGHT);
		maxText.setEditable(true);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE_AUTOSCALE");
		autoScaleCheck = new JCheckBox(msg);
		autoScaleCheck.setFont(labelFont);
		autoScaleCheck.setForeground(fColor);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE_MODE");
		scaleLabel = new JLabel(msg);
		scaleLabel.setFont(labelFont);
		scaleLabel.setForeground(fColor);
		scaleCombo = new JComboBox();
		scaleCombo.setFont(labelFont);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE_MODE_LINEAR");
		scaleCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_SCALE_MODE_LOGARITHMIC");
		scaleCombo.addItem(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_SHOWSUBGRID");
		subGridCheck = new JCheckBox(msg);
		subGridCheck.setFont(labelFont);
		subGridCheck.setForeground(fColor);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_SHOWSUBGRID_TOOLTIP");
		subGridCheck.setToolTipText(msg);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_VISIBLE");
		visibleCheck = new JCheckBox(msg);
		visibleCheck.setFont(labelFont);
		visibleCheck.setForeground(fColor);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_VISIBLE_TOOLTIP");
		visibleCheck.setToolTipText(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_DRAW_OPPOSITE");
		oppositeCheck = new JCheckBox(msg);
		oppositeCheck.setFont(labelFont);
		oppositeCheck.setForeground(fColor);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_DRAW_OPPOSITE_TOOLTIP");
		oppositeCheck.setToolTipText(msg);

		formatCombo = new JComboBox();
		formatCombo.setFont(labelFont);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_AUTOMATIC");
		formatCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_SCIENTIFIC");
		formatCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_TIME");
		formatCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_DECIMALINT");
		formatCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_HEXADECIMALINT");
		formatCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_BINARYINT");
		formatCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT_SCIENTIFICINT");
		formatCombo.addItem(msg);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_LABELFORMAT");
		formatLabel = new JLabel(msg);
		formatLabel.setFont(labelFont);
		formatLabel.setForeground(fColor);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_TITLE");
		titleLabel = new JLabel(msg);
		titleLabel.setFont(labelFont);
		titleLabel.setForeground(fColor);
		titleText = new JTextField();
		titleText.setEditable(true);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_COLOR");
		colorLabel = new JLabel(msg);
		colorLabel.setFont(labelFont);
		colorLabel.setForeground(fColor);
		colorView = new JLabel("");
		colorView.setOpaque(true);
		colorView.setBorder(BorderFactory.createLineBorder(Color.black));
		colorView.setBackground(Color.RED);
		colorBtn = new JButton("...");
		colorBtn.addMouseListener(new YAxisMouseListener(this));
		colorBtn.setMargin(zInset);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION");
		positionLabel = new JLabel(msg);
		positionLabel.setFont(labelFont);
		positionLabel.setForeground(fColor);
		positionCombo = new JComboBox();
		positionCombo.setFont(labelFont);
		switch (type) {

		case X_TYPE:
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_DOWN");
			positionCombo.addItem(msg);
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_UP");
			positionCombo.addItem(msg);
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_Y1ORIGIN");
			positionCombo.addItem(msg);
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_Y2ORIGIN");
			positionCombo.addItem(msg);
			break;

		case Y1_TYPE:
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_LEFT");
			positionCombo.addItem(msg);
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_XORIGIN");
			positionCombo.addItem(msg);
			visibleCheck.setSelected(true);
			autoScaleCheck.setSelected(true);
			break;

		case Y2_TYPE:
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_RIGHT");
			positionCombo.addItem(msg);
			msg = Messages
					.getMessage("DIALOGS_EDIT_VC_YAXIS_AXISSETTINGS_POSITION_XORIGIN");
			positionCombo.addItem(msg);
			break;

		}
		scalePanel.add(minLabel);
		scalePanel.add(minText);
		scalePanel.add(maxLabel);
		scalePanel.add(maxText);
		scalePanel.add(autoScaleCheck);
		scalePanel.add(scaleLabel);
		scalePanel.add(scaleCombo);
		add(scalePanel);

		settingPanel.add(subGridCheck);
		settingPanel.add(oppositeCheck);
		settingPanel.add(visibleCheck);
		settingPanel.add(formatCombo);
		settingPanel.add(formatLabel);
		settingPanel.add(titleLabel);
		settingPanel.add(titleText);
		settingPanel.add(colorLabel);
		settingPanel.add(colorView);
		settingPanel.add(colorBtn);
		settingPanel.add(positionLabel);
		settingPanel.add(positionCombo);
		add(settingPanel);

		minLabel.setBounds(10, 20, 35, 25);
		minText.setBounds(50, 20, 90, 25);
		maxLabel.setBounds(165, 20, 40, 25);
		maxText.setBounds(210, 20, 90, 25);
		scaleLabel.setBounds(10, 50, 100, 25);
		scaleCombo.setBounds(115, 50, 185, 25);
		autoScaleCheck.setBounds(5, 80, 275, 25);
		scalePanel.setBounds(5, 10, 310, 115);

		formatLabel.setBounds(10, 20, 100, 25);
		formatCombo.setBounds(115, 20, 185, 25);
		titleLabel.setBounds(10, 50, 100, 25);
		titleText.setBounds(115, 50, 185, 25);
		colorLabel.setBounds(10, 80, 100, 25);
		colorView.setBounds(115, 80, 150, 25);
		colorBtn.setBounds(270, 80, 30, 25);
		positionLabel.setBounds(10, 110, 100, 25);
		positionCombo.setBounds(115, 110, 185, 25);
		subGridCheck.setBounds(5, 140, 130, 25);
		oppositeCheck.setBounds(160, 140, 130, 25);
		visibleCheck.setBounds(5, 170, 280, 25);
		settingPanel.setBounds(5, 130, 310, 205);
	}

	/**
	 * @return 24 août 2005
	 */
	public YAxis getYAxis() {
		YAxis ret = new YAxis();

		ret.setLabelFormat(formatCombo.getSelectedIndex());
		ret.setPosition(positionCombo.getSelectedIndex());
		ret.setScaleMode(scaleCombo.getSelectedIndex());
		ret.setAutoScale(autoScaleCheck.isSelected());
		ret.setColor(colorView.getBackground());
		ret.setDrawOpposite(oppositeCheck.isSelected());
		try {
			ret.setScaleMax(Double.parseDouble(maxText.getText()));
		} catch (NumberFormatException nfe) {

		}
		try {
			ret.setScaleMin(Double.parseDouble(minText.getText()));
		} catch (NumberFormatException nfe) {

		}
		ret.setShowSubGrid(subGridCheck.isSelected());
		ret.setVisible(visibleCheck.isSelected());
		ret.setTitle(titleText.getText());

		return ret;
	}

	/**
	 * @param labelFormat
	 *            25 août 2005
	 */
	public void setLabelFormat(int labelFormat) {
		formatCombo.setSelectedIndex(labelFormat);
	}

	/**
	 * @param position
	 *            25 août 2005
	 */
	public void setPosition(int position) {
		positionCombo.setSelectedIndex(position);
	}

	/**
	 * @param scaleMode
	 *            25 août 2005
	 */
	public void setScaleMode(int scaleMode) {
		scaleCombo.setSelectedIndex(scaleMode);
	}

	/**
	 * @param b
	 *            25 août 2005
	 */
	public void setAutoScale(boolean b) {
		autoScaleCheck.setSelected(b);
	}

	/**
	 * @param color
	 *            25 août 2005
	 */
	public void setColor(Color color) {
		colorView.setBackground(color);
	}

	public Color getColor() {
		return colorView.getBackground();
	}

	/**
	 * @param b
	 *            25 août 2005
	 */
	public void setDrawOpposite(boolean b) {
		oppositeCheck.setSelected(b);
	}

	/**
	 * @param scaleMax
	 *            25 août 2005
	 */
	public void setScaleMax(double scaleMax) {
		maxText.setText(String.valueOf(scaleMax));
	}

	/**
	 * @param labelFormat
	 *            25 août 2005
	 */
	public void setScaleMin(double scaleMin) {
		minText.setText(String.valueOf(scaleMin));
	}

	/**
	 * @param object
	 *            25 août 2005
	 */
	public void setShowSubGrid(boolean b) {
		subGridCheck.setSelected(b);
	}

	/**
	 * @param b
	 *            25 août 2005
	 */
	@Override
	public void setVisible(boolean b) {
		visibleCheck.setSelected(b);
	}

	/**
	 * @param string
	 *            29 août 2005
	 */
	public void setTitle(String title) {
		titleText.setText(title);
	}

}
