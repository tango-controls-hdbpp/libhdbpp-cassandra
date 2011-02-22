package fr.soleil.mambo.containers.view.dialogs;

import java.awt.HeadlessException;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import fr.esrf.tangoatk.widget.attribute.NumberImageViewer;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.attributes.FakeNumberImage;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewImageDialog extends JDialog
{
    protected NumberImageViewer viewer;
    protected JPanel myPanel;

    public ViewImageDialog (FakeNumberImage image) throws HeadlessException
    {
        super(MamboFrame.getInstance(), image.getName(), true);
        myPanel = new JPanel();
        GUIUtilities.setObjectBackground(myPanel, GUIUtilities.VIEW_COLOR);
        viewer = image.getViewer();
        viewer.setModel(image);
        viewer.setBestFit(true);
        GUIUtilities.setObjectBackground(viewer, GUIUtilities.VIEW_COLOR);
        JScrollPane scrollpane = new JScrollPane(viewer);
        GUIUtilities.setObjectBackground(scrollpane, GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(scrollpane.getViewport(), GUIUtilities.VIEW_COLOR);
        myPanel.add(viewer);
        initLayout();
        this.setContentPane(myPanel);
        this.setSize((int)viewer.getPreferredSize().getWidth() + 200, (int)viewer.getPreferredSize().getWidth() + 200);
        int x = MamboFrame.getInstance().getX() + MamboFrame.getInstance().getWidth();
        x -= (this.getWidth() + 10);
        if (x < 0) x = 0;
        int y = MamboFrame.getInstance().getY() + MamboFrame.getInstance().getHeight();
        y -= (this.getHeight());
        y /= 2;
        if (y < 0) y = 0;
        this.setLocation(x,y);
    }

    protected void initLayout()
    {
        myPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(myPanel,
                myPanel.getComponentCount(), 1,
                0, 0,
                0, 0,
                true
        );
    }
}
