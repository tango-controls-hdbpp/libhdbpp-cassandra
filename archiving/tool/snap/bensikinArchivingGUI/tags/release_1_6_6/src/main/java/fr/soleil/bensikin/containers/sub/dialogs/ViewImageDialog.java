package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.data.snapshot.BensikinDAOFactory;
import fr.soleil.bensikin.data.snapshot.BensikinDAOKey;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.SpringUtilities;
import fr.soleil.comete.dao.util.DefaultMatrixDAO;
import fr.soleil.comete.util.NumberMatrix;
import fr.soleil.comete.widget.ImageViewer;
import fr.soleil.comete.widget.awt.AwtColorTool;

public class ViewImageDialog extends JDialog {

    private static final long serialVersionUID = -993763702000345451L;
    private ImageViewer imageViewer;
    protected JPanel myPanel;
    private Object value;
    private String displayFormat;
    private int data_type;

    public ViewImageDialog(String name, Object value, String displayFormat, int data_type) {
        super(BensikinFrame.getInstance(), name, true);
        this.value = value;
        this.displayFormat = displayFormat;
        this.data_type = data_type;
        initComponents();
        addComponents();
        initLayout();
        initBounds();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (imageViewer != null) {
                    clean();
                }
            }
        });
    }

    private void clean() {
        if (imageViewer != null) {
            imageViewer.removeKey(BensikinDAOFactory.class.getName());
            imageViewer.clearDAO();
        }
        myPanel.removeAll();
    }

    private void initBounds() {
        imageViewer.getComponent().setSize(imageViewer.getComponent().getWidth() + 200,
                imageViewer.getComponent().getHeight() + 200);
        int x = BensikinFrame.getInstance().getX() + BensikinFrame.getInstance().getWidth();
        x -= (this.getWidth() + 10);
        if (x < 0)
            x = 0;
        int y = BensikinFrame.getInstance().getY() + BensikinFrame.getInstance().getHeight();
        y -= (this.getHeight());
        y /= 2;
        if (y < 0)
            y = 0;
        this.setLocation(x, y);
    }

    private void initComponents() {
        myPanel = new JPanel();
        GUIUtilities.setObjectBackground(myPanel, GUIUtilities.SNAPSHOT_COLOR);
        prepareViewer();
    }

    private void prepareViewer() {
        imageViewer = new ImageViewer();
        imageViewer.getComponent().setUseMaskManagement(false);
        imageViewer.getComponent().setTableEditable(false);
        imageViewer.getComponent().setCometeBackground(
                AwtColorTool.getCometeColor(GUIUtilities.getSnapshotColor()));
    }

    private void addComponents() {
        myPanel.add((Component) imageViewer.getComponent());
        this.setContentPane(myPanel);
    }

    protected void initLayout() {
        myPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(myPanel, myPanel.getComponentCount(), 1, 0, 0, 0, 0, true);
    }

    public void updateContent() {
        clean();
        DefaultMatrixDAO dao = createDao();
        imageViewer.addKey(BensikinDAOFactory.class.getName(), new BensikinDAOKey(dao));
        imageViewer.switchDAOFactory(BensikinDAOFactory.class.getName());
        myPanel.add((Component) imageViewer.getComponent());
    }

    private DefaultMatrixDAO createDao() {
        Number[][] val = null;
        switch (data_type) {
            case TangoConst.Tango_DEV_CHAR:
            case TangoConst.Tango_DEV_UCHAR:
                val = (Byte[][]) value;
                break;

            case TangoConst.Tango_DEV_SHORT:
            case TangoConst.Tango_DEV_USHORT:
                val = (Short[][]) value;
                break;

            case TangoConst.Tango_DEV_LONG:
            case TangoConst.Tango_DEV_ULONG:
                val = (Integer[][]) value;
                break;

            case TangoConst.Tango_DEV_FLOAT:
                val = (Float[][]) value;
                break;

            case TangoConst.Tango_DEV_DOUBLE:
                val = (Double[][]) value;
                break;

            default: // nothing to do
        }
        if (val == null) {
            val = new Double[0][0];
        }
        NumberMatrix matrice = new NumberMatrix();
        matrice.setValues(val);
        DefaultMatrixDAO dao = new DefaultMatrixDAO();
        dao.setFormat(displayFormat);
        dao.setData(matrice);
        return dao;
    }
}
