package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import fr.esrf.tangoatk.widget.attribute.NumberImageViewer;
import fr.soleil.comete.dao.util.DefaultMatrixDAO;
import fr.soleil.comete.util.NumberMatrix;
import fr.soleil.comete.widget.ImageViewer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.attributes.FakeNumberImage;
import fr.soleil.mambo.data.view.MamboViewDAOFactory;
import fr.soleil.mambo.data.view.ViewDAOKey;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewImageDialog extends JDialog {
    // Ready to Comete 0.1.0-SNAPSHOT
    //
    // private static final long serialVersionUID = 2386262235125478167L;
    // private ImageViewer imageViewer;
    // protected JPanel myPanel;
    // private double[][] value;
    // private String displayFormat;
    //
    // public ViewImageDialog(String name, double[][] value, String
    // displayFormat) {
    // super(MamboFrame.getInstance(), name, true);
    // this.value = value;
    // this.displayFormat = displayFormat;
    // initComponents();
    // addComponents();
    // initLayout();
    // initBounds();
    // this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    // addWindowListener(new WindowAdapter() {
    // @Override
    // public void windowClosing(WindowEvent e) {
    // if (imageViewer != null) {
    // clean();
    // }
    // }
    // });
    // }
    //
    // private void clean() {
    // if (imageViewer != null) {
    // imageViewer.removeKey(MamboViewDAOFactory.class.getName());
    // imageViewer.clearDAO();
    // }
    // myPanel.removeAll();
    // }
    //
    // private void initBounds() {
    // imageViewer.getComponent().setSize(
    // imageViewer.getComponent().getWidth() + 200,
    // imageViewer.getComponent().getHeight() + 200);
    // int x = MamboFrame.getInstance().getX()
    // + MamboFrame.getInstance().getWidth();
    // x -= (this.getWidth() + 10);
    // if (x < 0)
    // x = 0;
    // int y = MamboFrame.getInstance().getY()
    // + MamboFrame.getInstance().getHeight();
    // y -= (this.getHeight());
    // y /= 2;
    // if (y < 0)
    // y = 0;
    // this.setLocation(x, y);
    // }
    //
    // private void initComponents() {
    // myPanel = new JPanel();
    // GUIUtilities.setObjectBackground(myPanel, GUIUtilities.VIEW_COLOR);
    // prepareViewer();
    // }
    //
    // private void prepareViewer() {
    // imageViewer = new ImageViewer();
    // imageViewer.getComponent().setUseMaskManagement(false);
    // imageViewer.getComponent().setTableEditable(false);
    // imageViewer.getComponent().setCometeBackground(
    // CometeColor.getColor(GUIUtilities.getViewColor()));
    // }
    //
    // private void addComponents() {
    // myPanel.add((Component) imageViewer.getComponent());
    // this.setContentPane(myPanel);
    // }
    //
    // protected void initLayout() {
    // myPanel.setLayout(new SpringLayout());
    // SpringUtilities.makeCompactGrid(myPanel, myPanel.getComponentCount(),
    // 1, 0, 0, 0, 0, true);
    // }
    //
    // public void updateContent() {
    // clean();
    // DefaultMatrixDAO dao = new DefaultMatrixDAO();
    // dao.setFormat(displayFormat);
    //
    // NumberMatrix matrice = new NumberMatrix();
    //
    // Double[][] data = null;
    // if (value != null) {
    // if (value.length > 0) {
    // data = new Double[value.length][value[0].length];
    // for (int i = 0; i < value.length; i++) {
    // for (int j = 0; j < value[i].length; j++) {
    // data[i][j] = Double.valueOf(value[i][j]);
    // }
    // }
    // } else {
    // data = new Double[0][0];
    // }
    // }
    // matrice.setValues(data);
    // dao.setData(matrice);
    //
    // imageViewer.addKey(MamboViewDAOFactory.class.getName(), new ViewDAOKey(
    // dao));
    // imageViewer.switchDAOFactory(MamboViewDAOFactory.class.getName());
    // myPanel.add((Component) imageViewer.getComponent());
    // }
    private static final long   serialVersionUID = 2386262235125478167L;
    protected NumberImageViewer viewer;
    protected JPanel            myPanel;

    public ViewImageDialog(FakeNumberImage image) throws HeadlessException {
        super(MamboFrame.getInstance(), image.getName(), true);
        myPanel = new JPanel();
        GUIUtilities.setObjectBackground(myPanel, GUIUtilities.VIEW_COLOR);
        viewer = image.getViewer();
        viewer.setModel(image);
        viewer.setBestFit(true);
        GUIUtilities.setObjectBackground(viewer, GUIUtilities.VIEW_COLOR);
        JScrollPane scrollpane = new JScrollPane(viewer);
        GUIUtilities.setObjectBackground(scrollpane, GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(scrollpane.getViewport(),
                GUIUtilities.VIEW_COLOR);
        myPanel.add(viewer);
        initLayout();
        this.setContentPane(myPanel);
        this.setSize((int) viewer.getPreferredSize().getWidth() + 200,
                (int) viewer.getPreferredSize().getWidth() + 200);
        int x = MamboFrame.getInstance().getX()
                + MamboFrame.getInstance().getWidth();
        x -= (this.getWidth() + 10);
        if (x < 0)
            x = 0;
        int y = MamboFrame.getInstance().getY()
                + MamboFrame.getInstance().getHeight();
        y -= (this.getHeight());
        y /= 2;
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
