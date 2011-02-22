// +======================================================================
// $Source$
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GeneralTabbedPane.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.4  2010/03/23 11:15:57  extia-soleil
// * extia : removed a useless "null" in chart title
//
// Revision 1.3  2009/12/17 12:50:57  pierrejoseph
// CheckStyle:  Organize imports / Format
//
// Revision 1.2  2009/11/24 09:53:30  soleilarc
// * Rapha�l GIRARDOT: VC details UI exported as a new Bean
//
// Revision 1.1 2008/06/11 14:25:02 soleilatk
// * GIRARDOT: GeneralTabbedPane --> ChartGeneralTabbedPane
//
// Revision 1.10 2007/05/11 07:24:00 ounsy
// * minor bug correction with Fonts
//
// Revision 1.9 2007/03/28 13:23:27 ounsy
// minor bug with Fonts correction
//
// Revision 1.8 2007/03/27 13:40:53 ounsy
// Better Font management (correction of Mantis bug 4302)
//
// Revision 1.7 2007/03/07 14:47:14 ounsy
// correction of Mantis bug 3273 (chart properties panel size)
//
// Revision 1.6 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.5 2006/07/25 09:49:46 ounsy
// commented a useless log
//
// Revision 1.4 2006/07/05 12:58:59 ounsy
// VC : data synchronization management
//
// Revision 1.3 2005/12/15 11:32:01 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import fr.esrf.tangoatk.widget.util.JSmoothLabel;
import fr.soleil.mambo.actions.view.listeners.GeneralPanelMouseListener;
import fr.soleil.mambo.components.NumberTextField;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * A class to display global graph settings dialog.
 * 
 * @author JL Pons
 */
public class ChartGeneralTabbedPane extends JPanel {

	private static final long serialVersionUID = 7599956107468871211L;
	protected final static Color fColor = new Color(99, 97, 156);
	protected final static Insets zInset = new Insets(0, 0, 0, 0);
	protected final static Font jLabelFont = new Font("Dialog", Font.PLAIN, 12);
	protected Font labelFont = new Font("Dialog", Font.PLAIN, 12);
	protected Font headerFont = new Font("Dialog", Font.PLAIN, 12);

	// Local declaration
	private JTabbedPane tabPane;

	// general panel
	private JPanel generalPanel;
	private JPanel innerPane;
	private JPanel gLegendPanel;

	private JLabel generalLegendLabel;
	private JTextField generalLegendText;

	private JCheckBox generalLabelVisibleCheck;

	private JPanel gColorFontPanel;

	private JLabel generalFontHeaderLabel;
	private JSmoothLabel generalFontHeaderSampleLabel;
	private JButton generalFontHeaderBtn;

	private JLabel generalFontLabelLabel;
	private JSmoothLabel generalFontLabelSampleLabel;
	private JButton generalFontLabelBtn;

	private JLabel generalBackColorLabel;
	private JLabel generalBackColorView;
	private JButton generalBackColorBtn;

	private JPanel gGridPanel;

	private JComboBox generalGridCombo;

	private JComboBox generalLabelPCombo;
	private JLabel generalLabelPLabel;

	private JComboBox generalGridStyleCombo;
	private JLabel generalGridStyleLabel;

	private JPanel gMiscPanel;

	private JLabel generalDurationLabel, timePrecisionLabel;
	private JTextField generalDurationText, timePrecisionText;

	private Y1AxisPanel y1Panel;
	private Y2AxisPanel y2Panel;

	/**
	 * JLChartOption constructor.
	 * 
	 * @param parent
	 *            Parent frame
	 * @param chart
	 *            Chart to be edited.
	 */
	public ChartGeneralTabbedPane() {
		initComponents();
		this.add(innerPane);
	}

	private void initComponents() {

		innerPane = new JPanel();
		innerPane.setLayout(null);

		tabPane = new JTabbedPane();

		// **********************************************
		// General panel construction
		// **********************************************
		String msg;

		generalPanel = new JPanel();
		generalPanel.setLayout(null);

		gLegendPanel = new JPanel();
		gLegendPanel.setLayout(null);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS");
		TitledBorder legendBorder = GUIUtilities
				.getPlotSubPanelsEtchedBorder(msg);
		gLegendPanel.setBorder(legendBorder);

		gColorFontPanel = new JPanel();
		gColorFontPanel.setLayout(null);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_COLORSANDFONT");
		TitledBorder colorFontBorder = GUIUtilities
				.getPlotSubPanelsEtchedBorder(msg);
		gColorFontPanel.setBorder(colorFontBorder);

		gGridPanel = new JPanel();
		gGridPanel.setLayout(null);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID");
		TitledBorder gridBorder = GUIUtilities
				.getPlotSubPanelsEtchedBorder(msg);
		gGridPanel.setBorder(gridBorder);

		gMiscPanel = new JPanel();
		gMiscPanel.setLayout(null);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_MISC");
		TitledBorder miscBorder = GUIUtilities
				.getPlotSubPanelsEtchedBorder(msg);
		gMiscPanel.setBorder(miscBorder);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_MISC_CHART_TITLE");
		generalLegendLabel = new JLabel(msg);
		generalLegendLabel.setFont(jLabelFont);
		generalLegendLabel.setForeground(fColor);
		generalLegendText = new JTextField();
		generalLegendText.setEditable(true);
		// generalLegendText.setText(chart.getHeader());
		generalLegendText.setText("");
		// generalLegendText.addKeyListener(this);

		generalLabelVisibleCheck = new JCheckBox();
		generalLabelVisibleCheck.setFont(jLabelFont);
		generalLabelVisibleCheck.setForeground(fColor);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS_VISIBLE");
		generalLabelVisibleCheck.setText(msg);
		generalLabelVisibleCheck.setSelected(true);
		// generalLabelVisibleCheck.addActionListener(this);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_COLORSANDFONT_CHARTBACKGROUND");
		generalBackColorLabel = new JLabel(msg);
		generalBackColorLabel.setFont(jLabelFont);
		generalBackColorLabel.setForeground(fColor);
		generalBackColorView = new JLabel("");
		generalBackColorView.setOpaque(true);
		generalBackColorView.setBorder(BorderFactory
				.createLineBorder(Color.black));
		generalBackColorView.setBackground(Color.RED);
		generalBackColorBtn = new JButton("...");
		generalBackColorBtn
				.addMouseListener(new GeneralPanelMouseListener(this));
		generalBackColorBtn.setMargin(zInset);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS_PLACEMENT");
		generalLabelPLabel = new JLabel(msg);
		generalLabelPLabel.setHorizontalAlignment(JLabel.RIGHT);
		generalLabelPLabel.setFont(jLabelFont);
		generalLabelPLabel.setForeground(fColor);

		generalLabelPCombo = new JComboBox();
		generalLabelPCombo.setFont(jLabelFont);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS_PLACEMENT_BOTTOM");
		generalLabelPCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS_PLACEMENT_TOP");
		generalLabelPCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS_PLACEMENT_RIGHT");
		generalLabelPCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_LEGENDS_PLACEMENT_LEFT");
		generalLabelPCombo.addItem(msg);
		// generalLabelPCombo.setSelectedIndex(chart.getLabelPlacement());
		// generalLabelPCombo.addActionListener(this);

		generalGridCombo = new JComboBox();
		generalGridCombo.setFont(jLabelFont);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_NONE");
		generalGridCombo.addItem(msg);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_ONX");
		generalGridCombo.addItem(msg);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_ONY1");
		generalGridCombo.addItem(msg);
		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_ONY2");
		generalGridCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_ONXANDY1");
		generalGridCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_ONXANDY2");
		generalGridCombo.addItem(msg);

		boolean vx = false;// chart.getXAxis().isGridVisible();
		boolean vy1 = false;// chart.getY1Axis().isGridVisible();
		boolean vy2 = false;// chart.getY2Axis().isGridVisible();

		int sel = 0;
		if (vx && !vy1 && !vy2)
			sel = 1;
		if (!vx && vy1 && !vy2)
			sel = 2;
		if (!vx && !vy1 && vy2)
			sel = 3;
		if (vx && vy1 && !vy2)
			sel = 4;
		if (vx && !vy1 && vy2)
			sel = 5;

		generalGridCombo.setSelectedIndex(sel);
		// generalGridCombo.addActionListener(this);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_STYLE");
		generalGridStyleLabel = new JLabel(msg);
		generalGridStyleLabel.setFont(jLabelFont);
		generalGridStyleLabel.setHorizontalAlignment(JLabel.RIGHT);
		generalGridStyleLabel.setForeground(fColor);

		generalGridStyleCombo = new JComboBox();
		generalGridStyleCombo.setFont(jLabelFont);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_STYLE_SOLID");
		generalGridStyleCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_STYLE_DOT");
		generalGridStyleCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_STYLE_SHORT_DASH");
		generalGridStyleCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_STYLE_LONG_DASH");
		generalGridStyleCombo.addItem(msg);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_AXISGRID_STYLE_DOT_DASH");
		generalGridStyleCombo.addItem(msg);
		// generalGridStyleCombo.setSelectedIndex(chart.getY1Axis().getGridStyle());
		// generalGridStyleCombo.addActionListener(this);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_MISC_DISPLAY_DURATION");
		generalDurationLabel = new JLabel(msg);
		generalDurationLabel.setFont(jLabelFont);
		generalDurationLabel.setForeground(fColor);
		generalDurationText = new JTextField();
		generalDurationText.setEditable(true);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_MISC_TIME_PRECISION");
		timePrecisionLabel = new JLabel(msg);
		timePrecisionLabel.setFont(jLabelFont);
		timePrecisionLabel.setForeground(fColor);
		timePrecisionText = new NumberTextField(
				NumberTextField.POSITIVE_INTEGER_LIKE);
		timePrecisionText.setEditable(true);
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_MISC_DISPLAY_DURATION_TOOLTIP");
		generalDurationText.setToolTipText(msg);
		// generalDurationText.setText(Double.toString(chart.getDisplayDuration()
		// / 1000.0));
		// generalDurationText.addKeyListener(this);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_COLORSANDFONT_HEADERFONT");
		generalFontHeaderLabel = new JLabel(msg);
		generalFontHeaderLabel.setFont(jLabelFont);
		generalFontHeaderLabel.setForeground(fColor);
		generalFontHeaderSampleLabel = new JSmoothLabel();
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_COLORSANDFONT_SAMPLETEXT");
		generalFontHeaderSampleLabel.setText(msg);
		generalFontHeaderSampleLabel.setForeground(fColor);
		generalFontHeaderSampleLabel.setOpaque(false);
		headerFont = generalFontHeaderSampleLabel.getFont();
		generalFontHeaderBtn = new JButton("...");
		generalFontHeaderBtn.addMouseListener(new GeneralPanelMouseListener(
				this));
		generalFontHeaderBtn.setMargin(zInset);

		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_COLORSANDFONT_LABELFONT");
		generalFontLabelLabel = new JLabel(msg);
		generalFontLabelLabel.setFont(jLabelFont);
		generalFontLabelLabel.setForeground(fColor);
		generalFontLabelSampleLabel = new JSmoothLabel();
		msg = Messages
				.getMessage("DIALOGS_EDIT_VC_GENERAL_MAIN_COLORSANDFONT_SAMPLE0123");
		generalFontLabelSampleLabel.setText(msg);
		generalFontLabelSampleLabel.setForeground(fColor);
		generalFontLabelSampleLabel.setOpaque(false);
		labelFont = generalFontLabelSampleLabel.getFont();
		// generalFontLabelSampleLabel.setFont(chart.getXAxis().getFont());
		generalFontLabelBtn = new JButton("...");
		generalFontLabelBtn
				.addMouseListener(new GeneralPanelMouseListener(this));
		generalFontHeaderBtn.setMargin(zInset);

		gLegendPanel.add(generalLabelVisibleCheck);
		gLegendPanel.add(generalLabelPLabel);
		gLegendPanel.add(generalLabelPCombo);
		generalPanel.add(gLegendPanel);

		gGridPanel.add(generalGridCombo);
		gGridPanel.add(generalGridStyleLabel);
		gGridPanel.add(generalGridStyleCombo);
		generalPanel.add(gGridPanel);

		gColorFontPanel.add(generalBackColorLabel);
		gColorFontPanel.add(generalBackColorView);
		gColorFontPanel.add(generalBackColorBtn);
		gColorFontPanel.add(generalFontHeaderLabel);
		gColorFontPanel.add(generalFontHeaderSampleLabel);
		gColorFontPanel.add(generalFontHeaderBtn);
		gColorFontPanel.add(generalFontLabelLabel);
		gColorFontPanel.add(generalFontLabelSampleLabel);
		gColorFontPanel.add(generalFontLabelBtn);
		generalPanel.add(gColorFontPanel);

		gMiscPanel.add(generalLegendLabel);
		gMiscPanel.add(generalLegendText);
		gMiscPanel.add(generalDurationLabel);
		gMiscPanel.add(generalDurationText);
		gMiscPanel.add(timePrecisionLabel);
		gMiscPanel.add(timePrecisionText);
		generalPanel.add(gMiscPanel);

		generalLabelVisibleCheck.setBounds(5, 20, 80, 25);
		generalLabelPLabel.setBounds(105, 20, 95, 25);
		generalLabelPCombo.setBounds(205, 20, 95, 25);
		gLegendPanel.setBounds(5, 10, 310, 55);

		generalBackColorLabel.setBounds(10, 20, 140, 25);
		generalBackColorView.setBounds(155, 20, 110, 25);
		generalBackColorBtn.setBounds(270, 20, 30, 25);
		generalFontHeaderLabel.setBounds(10, 50, 90, 25);
		generalFontHeaderSampleLabel.setBounds(105, 50, 160, 25);
		generalFontHeaderBtn.setBounds(270, 50, 30, 25);
		generalFontLabelLabel.setBounds(10, 80, 90, 25);
		generalFontLabelSampleLabel.setBounds(105, 80, 160, 25);
		generalFontLabelBtn.setBounds(270, 80, 30, 25);
		gColorFontPanel.setBounds(5, 70, 310, 115);

		generalGridCombo.setBounds(10, 20, 120, 25);
		generalGridStyleLabel.setBounds(150, 20, 45, 25);
		generalGridStyleCombo.setBounds(200, 20, 100, 25);
		gGridPanel.setBounds(5, 190, 310, 55);

		generalLegendLabel.setBounds(10, 20, 70, 25);
		generalLegendText.setBounds(80, 20, 220, 25);
		generalDurationLabel.setBounds(10, 50, 120, 25);
		generalDurationText.setBounds(130, 50, 170, 25);
		timePrecisionLabel.setBounds(10, 80, 205, 25);
		timePrecisionText.setBounds(220, 80, 80, 25);
		gMiscPanel.setBounds(5, 250, 310, 110);

		msg = Messages.getMessage("DIALOGS_EDIT_VC_GENERAL_TITLE");
		tabPane.add(msg, generalPanel);

		y1Panel = new Y1AxisPanel();
		y2Panel = new Y2AxisPanel();

		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_TITLE1");
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.add(y1Panel);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.add(y2Panel);
		tabPane.add(msg, panel1);
		tabPane.revalidate();
		tabPane.repaint();
		msg = Messages.getMessage("DIALOGS_EDIT_VC_YAXIS_TITLE2");
		tabPane.add(msg, panel2);
		tabPane.revalidate();
		tabPane.repaint();
		innerPane.add(tabPane);

		tabPane.setBounds(5, 5, 320, 395);
		innerPane.setPreferredSize(new Dimension(410, 410));

	}

	/**
	 * @return Returns the generalFontHeaderBtn.
	 */
	public JButton getGeneralFontHeaderBtn() {
		return generalFontHeaderBtn;
	}

	/**
	 * @return Returns the generalFontLabelBtn.
	 */
	public JButton getGeneralFontLabelBtn() {
		return generalFontLabelBtn;
	}

	/**
	 * @return 24 ao�t 2005
	 */
	public YAxis getY1Axis() {
		return y1Panel.getYAxis();
	}

	/**
	 * @return 24 ao�t 2005
	 */
	public GeneralChartProperties getGeneralChartProperties() {
		GeneralChartProperties ret = new GeneralChartProperties();

		ret.setAxisGrid(generalGridCombo.getSelectedIndex());
		ret.setAxisGridStyle(generalGridStyleCombo.getSelectedIndex());
		ret.setLegendsPlacement(generalLabelPCombo.getSelectedIndex());
		ret.setBackgroundColor(generalBackColorView.getBackground());
		try {
			ret.setDisplayDuration(Long
					.parseLong(generalDurationText.getText()));
		} catch (NumberFormatException nfe) {
			// nfe.printStackTrace();
		}
		ret.setLegendsAreVisible(generalLabelVisibleCheck.isSelected());
		ret.setHeaderFont(headerFont);
		String title;
		if ((generalLegendText.getText() == null) || generalLegendText.getText().trim().isEmpty()) {
		    title = null;
		}
		else {
		    title = generalLegendText.getText();
		}
		ret.setTitle(title);
		title = null;
		ret.setLabelFont(labelFont);
		try {
			ret.setTimePrecision(Integer.parseInt(timePrecisionText.getText()));
		} catch (NumberFormatException nfe) {
			ret.setTimePrecision(0);
		}
		return ret;
	}

	/**
	 * @return 24 ao�t 2005
	 */
	public YAxis getY2Axis() {
		return y2Panel.getYAxis();
	}

	/**
	 * @param axisGrid
	 *            25 ao�t 2005
	 */
	public void setAxisGrid(int axisGrid) {
		generalGridCombo.setSelectedIndex(axisGrid);
	}

	/**
	 * @param axisGridStyle
	 *            25 ao�t 2005
	 */
	public void setAxisGridStyle(int axisGridStyle) {
		generalGridStyleCombo.setSelectedIndex(axisGridStyle);
	}

	/**
	 * @param legendsPlacement
	 *            25 ao�t 2005
	 */
	public void setLegendsPlacement(int legendsPlacement) {
		generalLabelPCombo.setSelectedIndex(legendsPlacement);
	}

	/**
	 * @param backgroundColor
	 *            25 ao�t 2005
	 */
	public void setBackgroundColor(Color backgroundColor) {
		generalBackColorView.setBackground(backgroundColor);
	}

	public Color getBackgroundColor() {
		return generalBackColorView.getBackground();
	}

	/**
	 * @param displayDuration
	 *            25 ao�t 2005
	 */
	public void setDisplayDuration(long displayDuration) {
		generalDurationText.setText(String.valueOf(displayDuration));
	}

	/**
	 * @param b
	 *            25 ao�t 2005
	 */
	public void setLegendsAreVisible(boolean b) {
		generalLabelVisibleCheck.setSelected(b);
	}

	public Font getHeaderFont() {
		if (headerFont == null) {
			headerFont = generalFontHeaderSampleLabel.getFont();
		}
		return headerFont;
	}

	/**
	 * @param headerFont
	 *            25 ao�t 2005
	 */
	public void setHeaderFont(Font headerFont2) {
		if (headerFont2 != null) {
			headerFont = headerFont2;
			generalFontHeaderSampleLabel.setFont(headerFont);
		}
	}

	public Font getLabelFont() {
		if (labelFont == null) {
			labelFont = generalFontLabelSampleLabel.getFont();
		}
		return labelFont;
	}

	/**
	 * @param labelFont2
	 *            25 ao�t 2005
	 */
	public void setLabelFont(Font labelFont2) {
		if (labelFont2 != null) {
			labelFont = labelFont2;
			generalFontLabelSampleLabel.setFont(labelFont);
		}
	}

	/**
	 * @return Returns the generalBackColorBtn.
	 */
	public JButton getGeneralBackColorBtn() {
		return generalBackColorBtn;
	}

	/**
	 * @param title
	 *            29 ao�t 2005
	 */
	public void setTitle(String title) {
		generalLegendText.setText(title == null ? "":title);
	}

	public void setTimePrecision(String timePrecision) {
		timePrecisionText.setText(timePrecision);
	}

	public Y1AxisPanel getY1Panel() {
		return y1Panel;
	}

	public Y2AxisPanel getY2Panel() {
		return y2Panel;
	}

}
