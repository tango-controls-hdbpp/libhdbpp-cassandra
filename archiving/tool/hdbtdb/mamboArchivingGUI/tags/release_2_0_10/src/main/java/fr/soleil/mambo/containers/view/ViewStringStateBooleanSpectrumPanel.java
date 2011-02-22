package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.actions.view.ViewCopyAction;
import fr.soleil.mambo.actions.view.ViewEditCopyAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.MamboFormatableTable;
import fr.soleil.mambo.components.renderers.BooleanTableRenderer;
import fr.soleil.mambo.components.renderers.StateTableRenderer;
import fr.soleil.mambo.components.renderers.StringTableRenderer;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewStringStateBooleanSpectrumTableModel;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewStringStateBooleanSpectrumPanel extends MamboCleanablePanel {

    private static final long          serialVersionUID = 296308229968028594L;
    private ViewConfigurationAttribute spectrum;
    private JButton                    copyRead, copyWrite, editRead,
            editWrite;
    DbData                             readData, writeData;
    private MamboFormatableTable       readTable, writeTable;
    private JPanel                     readButtonPanel, writeButtonPanel;
    private int                        data_type        = -1;
    private JLabel                     readLabel, writeLabel;
    private JLabel                     loadingLabel;
    private final static int           minColwidth      = 80;
    private boolean                    cleaning         = false;
    public final static int            READ_REFERENCE   = 0;
    public final static int            WRITE_REFERENCE  = 1;
    private ViewConfigurationBean      viewConfigurationBean;

    public ViewStringStateBooleanSpectrumPanel(
            ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        readData = null;
        writeData = null;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        this.spectrum = theSpectrum;
        if (spectrum == null
                || viewConfigurationBean.getViewConfiguration() == null
                || !spectrum.isSpectrum(viewConfigurationBean
                        .getViewConfiguration().getData().isHistoric()))
            return;
        data_type = spectrum.getDataType(viewConfigurationBean
                .getViewConfiguration().getData().isHistoric());
        if (data_type != TangoConst.Tango_DEV_STRING
                && data_type != TangoConst.Tango_DEV_STATE
                && data_type != TangoConst.Tango_DEV_BOOLEAN) return;
        loadingLabel = new JLabel(Messages
                .getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        if (loadingLabel == null) return;
        add(loadingLabel);
        initLayout();
        initBorder();
    }

    private void initData() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning) return;
        ViewConfiguration selectedViewConfiguration = viewConfigurationBean
                .getViewConfiguration();
        if (selectedViewConfiguration == null) {
            return;
        }
        ViewConfigurationData selectedViewConfigurationData = selectedViewConfiguration
                .getData();

        String[] param = new String[3];
        param[0] = getFullName();
        Timestamp start = selectedViewConfigurationData.getStartDate();
        Timestamp end = selectedViewConfigurationData.getEndDate();
        String startDate = "";
        String endDate = "";
        try {
            startDate = extractingManager.timeToDateSGBD(start.getTime());
            endDate = extractingManager.timeToDateSGBD(end.getTime());
        }
        catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            return;
        }
        param[1] = startDate;
        param[2] = endDate;
        Hashtable<String, Object> table;
        try {
            table = extractingManager.retrieveDataHash(param,
                    selectedViewConfigurationData.isHistoric(),
                    selectedViewConfigurationData.getSamplingType());
            if (extractingManager.isCanceled() || cleaning) return;
        }
        catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            return;
        }
        if (table == null) {
            // System.out.println("table null");
            readData = null;
            writeData = null;
            return;
        }
        if (table.get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY) != null) {
            readData = (DbData) table
                    .get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY);
        }
        else {
            readData = null;
        }
        if (table.get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY) != null) {
            writeData = (DbData) table
                    .get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY);
        }
        else {
            writeData = null;
        }
    }

    private void initComponents() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning) return;
        if (readData != null) {
            readLabel = new JLabel(spectrum.getCompleteName() + " "
                    + Messages.getMessage("VIEW_SPECTRUM_READ"));
            GUIUtilities
                    .setObjectBackground(readLabel, GUIUtilities.VIEW_COLOR);
            readTable = new MamboFormatableTable(
                    new ViewStringStateBooleanSpectrumTableModel(readData,
                            data_type));
            readTable.setAutoResizeMode(MamboFormatableTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < readTable.getColumnCount(); i++) {
                readTable.getColumn(readTable.getColumnName(i)).setMinWidth(
                        minColwidth);
            }
            GUIUtilities
                    .setObjectBackground(readTable, GUIUtilities.VIEW_COLOR);
            if (data_type == TangoConst.Tango_DEV_STATE) {
                readTable.setDefaultRenderer(Object.class,
                        new StateTableRenderer());
            }
            else if (data_type == TangoConst.Tango_DEV_STRING) {
                readTable.setDefaultRenderer(Object.class,
                        new StringTableRenderer());
            }
            else // boolean
            {
                readTable.setDefaultRenderer(Object.class,
                        new BooleanTableRenderer());
            }
            copyRead = new JButton(new ViewCopyAction(Messages
                    .getMessage("VIEW_SPECTRUM_COPY"), this, READ_REFERENCE));
            GUIUtilities.setObjectBackground(copyRead,
                    GUIUtilities.VIEW_COPY_COLOR);
            copyRead.setMargin(new Insets(0, 0, 0, 0));
            editRead = new JButton(new ViewEditCopyAction(Messages
                    .getMessage("VIEW_SPECTRUM_EDIT_COPY"), this,
                    READ_REFERENCE));
            GUIUtilities.setObjectBackground(editRead,
                    GUIUtilities.VIEW_COPY_COLOR);
            editRead.setMargin(new Insets(0, 0, 0, 0));
            readButtonPanel = new JPanel();
            GUIUtilities.setObjectBackground(readButtonPanel,
                    GUIUtilities.VIEW_COLOR);
            if (readButtonPanel == null) return;
            readButtonPanel.setLayout(new SpringLayout());
            if (copyRead == null) return;
            readButtonPanel.add(copyRead);
            readButtonPanel.add(Box.createHorizontalGlue());
            if (editRead == null) return;
            readButtonPanel.add(editRead);
            SpringUtilities.makeCompactGrid(readButtonPanel, 1, readButtonPanel
                    .getComponentCount(), 5, 5, 5, 5, true);
        }
        if (writeData != null) {
            writeLabel = new JLabel(spectrum.getCompleteName() + " "
                    + Messages.getMessage("VIEW_SPECTRUM_WRITE"));
            GUIUtilities.setObjectBackground(writeLabel,
                    GUIUtilities.VIEW_COLOR);
            writeTable = new MamboFormatableTable(
                    new ViewStringStateBooleanSpectrumTableModel(writeData,
                            data_type));
            writeTable.setAutoResizeMode(MamboFormatableTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < writeTable.getColumnCount(); i++) {
                writeTable.getColumn(writeTable.getColumnName(i)).setMinWidth(
                        minColwidth);
            }
            GUIUtilities.setObjectBackground(writeTable,
                    GUIUtilities.VIEW_COLOR);
            if (data_type == TangoConst.Tango_DEV_STATE) {
                writeTable.setDefaultRenderer(Object.class,
                        new StateTableRenderer());
            }
            else if (data_type == TangoConst.Tango_DEV_STRING) {
                writeTable.setDefaultRenderer(Object.class,
                        new StringTableRenderer());
            }
            else // boolean
            {
                writeTable.setDefaultRenderer(Object.class,
                        new BooleanTableRenderer());
            }
            copyWrite = new JButton(new ViewCopyAction(Messages
                    .getMessage("VIEW_SPECTRUM_COPY"), this, WRITE_REFERENCE));
            GUIUtilities.setObjectBackground(copyWrite,
                    GUIUtilities.VIEW_COPY_COLOR);
            copyWrite.setMargin(new Insets(0, 0, 0, 0));
            editWrite = new JButton(new ViewEditCopyAction(Messages
                    .getMessage("VIEW_SPECTRUM_EDIT_COPY"), this,
                    WRITE_REFERENCE));
            GUIUtilities.setObjectBackground(editWrite,
                    GUIUtilities.VIEW_COPY_COLOR);
            editWrite.setMargin(new Insets(0, 0, 0, 0));
            writeButtonPanel = new JPanel();
            GUIUtilities.setObjectBackground(writeButtonPanel,
                    GUIUtilities.VIEW_COLOR);
            if (writeButtonPanel == null) return;
            writeButtonPanel.setLayout(new SpringLayout());
            if (copyWrite == null) return;
            writeButtonPanel.add(copyWrite);
            writeButtonPanel.add(Box.createHorizontalGlue());
            if (editWrite == null) return;
            writeButtonPanel.add(editWrite);
            SpringUtilities.makeCompactGrid(writeButtonPanel, 1,
                    writeButtonPanel.getComponentCount(), 5, 5, 5, 5, true);
        }
    }

    private void addComponents() {
        if (readData != null) {
            if (readLabel == null) return;
            this.add(readLabel);
            if (readButtonPanel == null) return;
            this.add(readButtonPanel);
            JScrollPane readPane = new JScrollPane(readTable);
            GUIUtilities.setObjectBackground(readPane, GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(readPane.getViewport(),
                    GUIUtilities.VIEW_COLOR);
            if (readPane == null) return;
            this.add(readPane);
        }
        if (writeData != null) {
            if (this.getComponentCount() != 0) {
                this.add(new JSeparator());
                this.add(new JSeparator());
            }
            if (writeLabel == null) return;
            this.add(writeLabel);
            if (writeButtonPanel == null) return;
            this.add(writeButtonPanel);
            JScrollPane writePane = new JScrollPane(writeTable);
            GUIUtilities
                    .setObjectBackground(writePane, GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(writePane.getViewport(),
                    GUIUtilities.VIEW_COLOR);
            if (writePane == null) return;
            this.add(writePane);
        }
        if (this.getComponentCount() == 0) {
            readLabel = new JLabel(spectrum.getCompleteName() + ":");
            GUIUtilities
                    .setObjectBackground(readLabel, GUIUtilities.VIEW_COLOR);
            writeLabel = new JLabel(Messages
                    .getMessage("VIEW_ATTRIBUTES_NO_DATA"));
            GUIUtilities.setObjectBackground(writeLabel,
                    GUIUtilities.VIEW_COLOR);
            if (readLabel == null) return;
            if (writeLabel == null) return;
            this.add(readLabel);
            this.add(writeLabel);
        }
    }

    private void initLayout() {
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, 5,
                5, 5, 5, true);
    }

    private void initBorder() {
        String msg = getFullName();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.CENTER, TitledBorder.TOP);
        Border border = (Border) (tb);
        this.setBorder(border);
        border = null;
    }

    public String getFullName() {
        if (spectrum == null) {
            return "";
        }
        else {
            return spectrum.getCompleteName();
        }
    }

    public String getName() {
        if (spectrum == null) {
            return "";
        }
        else {
            return spectrum.getName();
        }
    }

    public String getReadValue() {
        String result = "";
        if (readTable != null) {
            for (int i = 0; i < readTable.getColumnCount(); i++) {
                result += readTable.getColumnName(i)
                        + Options.getInstance().getGeneralOptions()
                                .getSeparator();
            }
            if (!"".equals(result)) {
                result = result.substring(0, result.length() - 1);
            }
            result += "\n" + readTable.toString();
            result = result.replaceAll("\n", GUIUtilities.CRLF);
        }
        return result;
    }

    public String getWriteValue() {
        String result = "";
        if (writeTable != null) {
            for (int i = 0; i < writeTable.getColumnCount(); i++) {
                result += writeTable.getColumnName(i)
                        + Options.getInstance().getGeneralOptions()
                                .getSeparator();
            }
            if (!"".equals(result)) {
                result = result.substring(0, result.length() - 1);
            }
            result += "\n" + writeTable.toString();
            result = result.replaceAll("\n", GUIUtilities.CRLF);
        }
        return result;
    }

    public void clean() {
        cleaning = true;
        removeAll();
        if (readTable != null) {
            if (readTable.getModel() != null
                    && readTable.getModel() instanceof ViewStringStateBooleanSpectrumTableModel) {
                ((ViewStringStateBooleanSpectrumTableModel) readTable
                        .getModel()).clearData();
            }
            readTable = null;
        }
        if (writeTable != null) {
            if (writeTable.getModel() != null
                    && writeTable.getModel() instanceof ViewStringStateBooleanSpectrumTableModel) {
                ((ViewStringStateBooleanSpectrumTableModel) writeTable
                        .getModel()).clearData();
            }
            writeTable = null;
        }
        copyRead = null;
        copyWrite = null;
        editRead = null;
        editWrite = null;
        readData = null;
        writeData = null;
        readTable = null;
        writeTable = null;
        readButtonPanel = null;
        writeButtonPanel = null;
        readLabel = null;
        writeLabel = null;
        loadingLabel = null;
        viewConfigurationBean = null;
    }

    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        try {
            if (extractingManager.isCanceled() || cleaning) return;
            initData();
            if (extractingManager.isCanceled() || cleaning) return;
            initComponents();
            if (extractingManager.isCanceled() || cleaning) return;
            addComponents();
            if (extractingManager.isCanceled() || cleaning) return;
            initLayout();
            if (extractingManager.isCanceled() || cleaning) return;
            if (loadingLabel != null) {
                loadingLabel.setText(Messages
                        .getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setToolTipText(Messages
                        .getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setForeground(Color.GREEN);
            }
            updateUI();
            repaint();
        }
        catch (java.lang.OutOfMemoryError oome) {
            if (extractingManager.isCanceled() || cleaning) return;
            outOfMemoryErrorManagement();
        }
        catch (Exception e) {
            if (extractingManager.isCanceled() || cleaning) return;
            else e.printStackTrace();
        }
    }

    @Override
    public void lightClean() {
        removeAll();
        readData = null;
        writeData = null;
        loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        add(loadingLabel);
        initLayout();
    }

}
