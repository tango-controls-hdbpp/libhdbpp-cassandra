package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.bensikin.models.ImageWriteValueTableModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * Dialog used to represent image write values
 */
public class ImageWriteAttibuteDialog extends ImageAttibuteDialog
{

    private ImageWriteValueTableModel tableModel;
    private JTextField setAllField;
    private JButton setAllButton;
    private JPanel setAllPanel;

    public ImageWriteAttibuteDialog (String attributeName, Object value,
            int data_type, String displayFormat)
    {
        super( attributeName, value, data_type, displayFormat );
    }

    protected void initTable()
    {
        tableModel = new ImageWriteValueTableModel(value,data_type);
        imageTable = new JTable(tableModel);
        imageTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < imageTable.getColumnCount(); i++)
        {
            imageTable.getColumn(imageTable.getColumnName(i)).setMinWidth(initColumnWidth);
            imageTable.getColumn(imageTable.getColumnName(i)).setWidth(initColumnWidth);
            imageTable.getColumn(imageTable.getColumnName(i)).setPreferredWidth(initColumnWidth);
        }
    }

    protected void initOthers()
    {
        setAllPanel = new JPanel();
        GUIUtilities.setObjectBackground(setAllPanel, GUIUtilities.SNAPSHOT_COLOR);
        setAllField = new JTextField("0");
        setAllButton = new JButton( Messages.getMessage("DIALOGS_IMAGE_ATTRIBUTE_SETALL") );
        setAllButton.setMargin(new Insets(0,0,0,0));
        super.initOthers();
    }

    protected void addWhenData()
    {
        setAllPanel.add(setAllField);
        setAllPanel.add(setAllButton);
        myPanel.add(setAllPanel);
        super.addWhenData();
    }

    protected void initLayout()
    {
        if (setAllPanel != null)
        {
            setAllPanel.setLayout(new SpringLayout());
            SpringUtilities.makeCompactGrid(setAllPanel,
                    1, myPanel.getComponentCount(),
                    0, 0,
                    0, 0,
                    true
            );
        }
        super.initLayout();
    }

    public Object getValue()
    {
        return value;
    }
}
