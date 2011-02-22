package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

import fr.esrf.Tango.AttrWriteType;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ExpressionNamesChooser extends JDialog {

    private static final long       serialVersionUID = -6224737935449662097L;
    protected JLabel[]              possibleNames;
    protected int                   nameIndex;
    protected JPanel                mainPanel;
    protected JLabel                lastClicked      = null;
    protected Dimension             prefSize;
    protected ViewConfigurationBean viewConfigurationBean;
    protected VCEditDialog          editDialog;

    public ExpressionNamesChooser(ViewConfigurationBean viewConfigurationBean,
            VCEditDialog editDialog) throws HeadlessException {
        super(editDialog, Messages.getMessage("EXPRESSION_VARIABLE_CHOOSE"),
                true);
        this.editDialog = editDialog;
        this.viewConfigurationBean = viewConfigurationBean;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public void reset() {
        clean();
        prefSize = new Dimension(50, 0);
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        ViewConfigurationAttributes attributes = null;
        if ((viewConfigurationBean != null)
                && (viewConfigurationBean.getEditingViewConfiguration() != null)) {
            attributes = viewConfigurationBean.getEditingViewConfiguration()
                    .getAttributes();
        }
        if (attributes != null) {
            TreeMap<String, ViewConfigurationAttribute> attributesList = attributes
                    .getAttributes();
            if (attributesList != null) {
                Vector<String> dataViewsNames = new Vector<String>();
                Set<String> keySet = attributesList.keySet();
                Iterator<String> keyIterator = keySet.iterator();
                boolean historic = viewConfigurationBean
                        .getEditingViewConfiguration().getData().isHistoric();
                while (keyIterator.hasNext()) {
                    ViewConfigurationAttribute attr = attributesList
                            .get(keyIterator.next());
                    if (attr.isScalar(historic)) {
                        int dataType = attr.getDataType(historic);
                        switch (dataType) {
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                int writable = attr.getDataWritable(historic);
                                switch (writable) {
                                    case AttrWriteType._READ:
                                        dataViewsNames
                                                .add(attr.getCompleteName()
                                                        + "/"
                                                        + ViewConfigurationAttribute.READ_DATA_VIEW_KEY);
                                        break;
                                    case AttrWriteType._WRITE:
                                        dataViewsNames
                                                .add(attr.getCompleteName()
                                                        + "/"
                                                        + ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY);
                                        break;
                                    case AttrWriteType._READ_WITH_WRITE:
                                    case AttrWriteType._READ_WRITE:
                                        dataViewsNames
                                                .add(attr.getCompleteName()
                                                        + "/"
                                                        + ViewConfigurationAttribute.READ_DATA_VIEW_KEY);
                                        dataViewsNames
                                                .add(attr.getCompleteName()
                                                        + "/"
                                                        + ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY);
                                        break;
                                }
                        }
                    }
                    attr = null;
                }
                keyIterator = null;
                keySet = null;
                attributesList = null;
                attributes = null;
                possibleNames = new JLabel[dataViewsNames.size()];
                for (int i = 0; i < possibleNames.length; i++) {
                    possibleNames[i] = new JLabel((String) dataViewsNames
                            .get(i));
                    possibleNames[i].setBackground(GUIUtilities.selectionColor);
                    possibleNames[i].setBorder(new EtchedBorder());
                    possibleNames[i].addMouseListener(new NameSetter());
                    possibleNames[i].setMaximumSize(new Dimension(
                            Integer.MAX_VALUE, 25));
                    int width = possibleNames[i].getPreferredSize().width;
                    if (prefSize.width < width) prefSize.width = width;
                    prefSize.height += possibleNames[i].getPreferredSize().height;
                    mainPanel.add(possibleNames[i]);
                }
                if (possibleNames.length > 0) {
                    mainPanel.add(Box.createGlue());
                }
                else {
                    JLabel koLabel = new JLabel(Messages
                            .getMessage("EXPRESSION_VARIABLE_CHOOSE_NO_SCALAR"));
                    mainPanel.add(koLabel);
                    prefSize = koLabel.getPreferredSize();
                    koLabel = null;
                }
                initLayout();
                initBounds();
            }
            attributesList = null;
        }
    }

    public void clean() {
        if (mainPanel != null) {
            mainPanel.removeAll();
            mainPanel = null;
        }
        if (possibleNames != null) {
            for (int i = 0; i < possibleNames.length; i++) {
                possibleNames[i] = null;
            }
            possibleNames = null;
        }
        prefSize = null;
    }

    protected void initLayout() {
        mainPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(mainPanel, mainPanel
                .getComponentCount(), 1, 0, 0, 0, 0, true);
        mainPanel.setPreferredSize(prefSize);
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.setContentPane(scrollPane);
    }

    protected void initBounds() {
        int width = prefSize.width + 30;
        int height = prefSize.height + 30 < 600 ? prefSize.height + 30 : 600;
        int x = editDialog.getExpressionTab()
                .getExpressionEditDialog().getX()
                - (width + 50);
        if (x < 0) x = 0;
        this.setBounds(x, 50, width, height);
        this.setResizable(false);
    }

    protected class NameSetter extends MouseAdapter {

        public NameSetter() {
            super();
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                if (lastClicked != null) {
                    lastClicked.setOpaque(false);
                    lastClicked.repaint();
                }
                JLabel label = (JLabel) e.getSource();
                lastClicked = label;
                label.setOpaque(true);
                label.repaint();
                editDialog.getExpressionTab()
                        .getExpressionEditDialog()
                        .getViewExpressionVariablesEditPanel().setVariableName(
                                label.getText(), nameIndex);
                label = null;
            }
            else {
                setVisible(false);
            }
        }
    }

}
