/*
 * Synchrotron Soleil File : ViewSpectrumChartDialog.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 27 janv. 2006 Revision: Author:
 * Date: State: Log: ViewSpectrumChartDialog.java,v
 */
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;

import fr.esrf.tangoatk.widget.util.chart.JLChart;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

/**
 * @author SOLEIL
 */
public class ViewSpectrumChartDialog extends JDialog {

	private static final long serialVersionUID = 9026556560129510126L;
	private JLChart chart;
	private JPanel mainPanel;
	private int color;
	private int markerStyle;

	private final static Color[] defaultColor = ColorGenerator.generateColors(
			31, 0xFFFFFF);

	private final static int[] defaultMarkerStyle = { JLDataView.MARKER_BOX,
			JLDataView.MARKER_CIRCLE, JLDataView.MARKER_CROSS,
			JLDataView.MARKER_DIAMOND, JLDataView.MARKER_DOT,
			JLDataView.MARKER_HORIZ_LINE, JLDataView.MARKER_SQUARE,
			JLDataView.MARKER_STAR, JLDataView.MARKER_TRIANGLE,
			JLDataView.MARKER_VERT_LINE };

	private ViewConfigurationBean viewConfigurationBean;
	private ViewSpectrumPanel specPanel;

	public ViewSpectrumChartDialog(ViewSpectrumPanel specPanel,
			ViewConfigurationBean viewConfigurationBean) {
		super(MamboFrame.getInstance(), specPanel.getFullName(), true);
		this.specPanel = specPanel;
		this.viewConfigurationBean = viewConfigurationBean;
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
		if (viewConfigurationBean.getViewConfiguration() == null)
			return;
		initComponents();
		addComponents();
		initLayout();
		initBounds();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void updateContent() {
		clean();
		mainPanel.add(chart);
		JLDataView[] readSpectrumData = specPanel.getSelectedReadView();
		JLDataView[] writeSpectrumData = specPanel.getSelectedWriteView();
		if (readSpectrumData.length == writeSpectrumData.length) {
			for (int i = 0; i < readSpectrumData.length; i++) {
				readSpectrumData[i].setLineWidth(1);
				writeSpectrumData[i].setLineWidth(2);
				readSpectrumData[i].setMarkerSize(3);
				writeSpectrumData[i].setMarkerSize(6);
				readSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
				writeSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
				Color drawColor = getNextColor();
				readSpectrumData[i].setColor(drawColor);
				writeSpectrumData[i].setColor(drawColor);
				readSpectrumData[i].setMarkerColor(drawColor);
				writeSpectrumData[i].setMarkerColor(drawColor);
				readSpectrumData[i].setMarker(getNextMarkerStyle());
				writeSpectrumData[i].setMarker(getNextMarkerStyle());
				readSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
				writeSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
				chart.getY1Axis().addDataView(readSpectrumData[i]);
				chart.getY1Axis().addDataView(writeSpectrumData[i]);
			}
		} else {
			for (int i = 0; i < readSpectrumData.length; i++) {
				readSpectrumData[i].setLineWidth(1);
				readSpectrumData[i].setMarkerSize(3);
				readSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
				Color drawColor = getNextColor();
				readSpectrumData[i].setColor(drawColor);
				readSpectrumData[i].setMarkerColor(drawColor);
				readSpectrumData[i].setMarker(getNextMarkerStyle());
				readSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
				chart.getY1Axis().addDataView(readSpectrumData[i]);
			}
			for (int i = 0; i < writeSpectrumData.length; i++) {
				writeSpectrumData[i].setLineWidth(2);
				writeSpectrumData[i].setMarkerSize(6);
				writeSpectrumData[i].setViewType(JLDataView.TYPE_LINE);
				Color drawColor = getNextColor();
				writeSpectrumData[i].setColor(drawColor);
				writeSpectrumData[i].setMarkerColor(drawColor);
				writeSpectrumData[i].setMarker(getNextMarkerStyle());
				writeSpectrumData[i].setStyle(JLDataView.STYLE_SOLID);
				chart.getY1Axis().addDataView(writeSpectrumData[i]);
			}
		}
	}

	private void initComponents() {
		mainPanel = new JPanel();
		GUIUtilities.setObjectBackground(mainPanel, GUIUtilities.VIEW_COLOR);
		color = 0;
		markerStyle = 0;
		prepareChart();
		GUIUtilities.setObjectBackground(chart, GUIUtilities.VIEW_COLOR);
	}

	public void clean() {
		if (chart != null) {
			chart.getY1Axis().clearDataView();
			chart.getY2Axis().clearDataView();
			chart.getXAxis().clearDataView();
		}
		mainPanel.removeAll();
	}

	private void addComponents() {
		mainPanel.add(chart);
		this.setContentPane(mainPanel);
	}

	private void initLayout() {
		mainPanel.setLayout(new BorderLayout());
	}

	private Color getNextColor() {
		Color colorToReturn = defaultColor[color];
		color = (color + 5) % defaultColor.length;
		return colorToReturn;
	}

	private int getNextMarkerStyle() {
		if (color == 0) {
			return defaultMarkerStyle[(markerStyle++)
					% defaultMarkerStyle.length];
		}
		return defaultMarkerStyle[(markerStyle) % defaultMarkerStyle.length];
	}

	private void initBounds() {
		int x = MamboFrame.getInstance().getX()
				+ MamboFrame.getInstance().getWidth() - 700;
		int y = MamboFrame.getInstance().getY()
				+ MamboFrame.getInstance().getHeight() - 700;
		y /= 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		this.setBounds(x, y, 700, 700);
	}

	private void prepareChart() {
		JLChart refChart = viewConfigurationBean.getViewConfiguration()
				.getChart();
		ViewConfigurationData data = viewConfigurationBean
				.getViewConfiguration().getData();
		GeneralChartProperties generalChartProperties = data
				.getGeneralChartProperties();
		chart = generalChartProperties.getAsJLChart();
		chart.setParentForTableDialog(this);

		String title = "";
		if (chart.getHeader() != null) {
			title += chart.getHeader() + " - ";
		}
		if (data != null) {
			title += Messages.getMessage("VIEW_ATTRIBUTES_START_DATE")
					+ data.getStartDate().toString() + ", "
					+ Messages.getMessage("VIEW_ATTRIBUTES_END_DATE")
					+ data.getEndDate().toString();
		}
		chart.setHeader(title);
		title = null;

		chart.getY1Axis().setAnnotation(refChart.getY1Axis().getAnnotation());
		chart.getY1Axis().setAxeName(refChart.getY1Axis().getAxeName());
		chart.getY1Axis().setAxisColor(refChart.getY1Axis().getAxisColor());
		chart.getY1Axis()
				.setDrawOpposite(refChart.getY1Axis().isDrawOpposite());
		chart.getY1Axis().setFont(refChart.getY1Axis().getFont());
		chart.getY1Axis().setGridStyle(refChart.getY1Axis().getGridStyle());
		chart.getY1Axis().setInverted(refChart.getY1Axis().isInverted());
		chart.getY1Axis().setLabelFormat(refChart.getY1Axis().getLabelFormat());
		chart.getY1Axis().setMaximum(refChart.getY1Axis().getMaximum());
		chart.getY1Axis().setMinimum(refChart.getY1Axis().getMinimum());
		chart.getY1Axis().setName(refChart.getY1Axis().getName());
		chart.getY1Axis().setPercentScrollback(
				refChart.getY1Axis().getPercentScrollback());
		chart.getY1Axis().setScale(refChart.getY1Axis().getScale());
		chart.getY1Axis().setSubGridVisible(
				refChart.getY1Axis().isSubGridVisible());
		chart.getY1Axis().setTickLength(refChart.getY1Axis().getTickLength());
		chart.getY1Axis().setTickSpacing(refChart.getY1Axis().getTickSpacing());
		chart.getY1Axis().setVisible(refChart.getY1Axis().isVisible());
		chart.getY1Axis().setAutoScale(refChart.getY1Axis().isAutoScale());

		chart.getXAxis().setAnnotation(refChart.getXAxis().getAnnotation());
		chart.getXAxis().setAxeName(refChart.getXAxis().getAxeName());
		chart.getXAxis().setAxisColor(refChart.getXAxis().getAxisColor());
		chart.getXAxis().setDrawOpposite(refChart.getXAxis().isDrawOpposite());
		chart.getXAxis().setFont(refChart.getXAxis().getFont());
		chart.getXAxis().setGridStyle(refChart.getXAxis().getGridStyle());
		chart.getXAxis().setInverted(refChart.getXAxis().isInverted());
		chart.getXAxis().setLabelFormat(refChart.getXAxis().getLabelFormat());
		chart.getXAxis().setMaximum(refChart.getXAxis().getMaximum());
		chart.getXAxis().setMinimum(refChart.getXAxis().getMinimum());
		chart.getXAxis().setName(refChart.getXAxis().getName());
		chart.getXAxis().setPercentScrollback(
				refChart.getXAxis().getPercentScrollback());
		chart.getXAxis().setScale(refChart.getXAxis().getScale());
		chart.getXAxis().setSubGridVisible(
				refChart.getXAxis().isSubGridVisible());
		chart.getXAxis().setTickLength(refChart.getXAxis().getTickLength());
		chart.getXAxis().setTickSpacing(refChart.getXAxis().getTickSpacing());
		chart.getXAxis().setVisible(refChart.getXAxis().isVisible());
		chart.getXAxis().setAutoScale(refChart.getXAxis().isAutoScale());

		chart.setChartBackground(refChart.getChartBackground());
		chart.setPaintAxisFirst(true);
		chart.setVisible(true);
	}

}
