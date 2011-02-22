package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import fr.esrf.tangoatk.widget.attribute.NumberImageViewer;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.attributes.FakeNumberImage;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.SpringUtilities;

public class ViewImageDialog extends JDialog {
	protected NumberImageViewer viewer;
	protected JPanel myPanel;

	public ViewImageDialog(FakeNumberImage image) throws HeadlessException {
		super(BensikinFrame.getInstance(), image.getName(), true);
		myPanel = new JPanel();
		GUIUtilities.setObjectBackground(myPanel, GUIUtilities.SNAPSHOT_COLOR);
		viewer = image.getViewer();
		viewer.setModel(image);
		viewer.setBestFit(true);
		GUIUtilities.setObjectBackground(viewer, GUIUtilities.SNAPSHOT_COLOR);
		JScrollPane scrollpane = new JScrollPane(viewer);
		GUIUtilities.setObjectBackground(scrollpane,
				GUIUtilities.SNAPSHOT_COLOR);
		GUIUtilities.setObjectBackground(scrollpane.getViewport(),
				GUIUtilities.SNAPSHOT_COLOR);
		myPanel.add(viewer);
		initLayout();
		this.setContentPane(myPanel);
		this.setSize((int) viewer.getPreferredSize().getWidth() + 200,
				(int) viewer.getPreferredSize().getWidth() + 200);
		int x = BensikinFrame.getInstance().getX()
				+ BensikinFrame.getInstance().getWidth();
		x -= (this.getWidth() + 10);
		if (x < 0)
			x = 0;
		int y = BensikinFrame.getInstance().getY()
				+ BensikinFrame.getInstance().getHeight();
		y -= (this.getHeight() + 10);
		if (y < 0)
			y = 0;
		this.setLocation(x, y);
	}

	protected void initLayout() {
		myPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(myPanel, myPanel.getComponentCount(),
				1, 0, 0, 0, 0, true);
	}
}
