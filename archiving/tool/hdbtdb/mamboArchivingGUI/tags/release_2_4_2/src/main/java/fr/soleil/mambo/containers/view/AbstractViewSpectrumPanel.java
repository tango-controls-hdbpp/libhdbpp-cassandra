package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.SaveSpectrumAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpectrumDataWriter;

public abstract class AbstractViewSpectrumPanel extends MamboCleanablePanel {

    private static final long serialVersionUID = 2909121087835355642L;
    protected ViewConfigurationAttribute spectrum;
    protected DbData[] splitedData;
    protected ViewConfigurationBean viewConfigurationBean;
    protected JLabel loadingLabel;
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss.SSS");
    protected String fileDirectory;
    protected JButton saveButton;

    public AbstractViewSpectrumPanel(ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        fileDirectory = Mambo.getPathToResources();
        this.viewConfigurationBean = viewConfigurationBean;
        splitedData = null;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        this.spectrum = theSpectrum;
        if (spectrum == null
                || viewConfigurationBean.getViewConfiguration() == null
                || !spectrum.isSpectrum(viewConfigurationBean.getViewConfiguration().getData()
                        .isHistoric()))
            return;
        loadingLabel = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        saveButton = new JButton(new SaveSpectrumAction(this));
        GUIUtilities.setObjectBackground(saveButton, GUIUtilities.VIEW_COLOR);
        afterInit();
    }

    protected void afterInit() {
        initBorder();
        initLayout();
        addLoadingLabel();
    }

    protected void initLayout() {
        setLayout(new GridBagLayout());
    }

    protected abstract void initBorder();

    protected abstract void initComponents();

    protected void addLoadingLabel() {
        GridBagConstraints loadingConstraints = new GridBagConstraints();
        loadingConstraints.fill = GridBagConstraints.HORIZONTAL;
        loadingConstraints.gridx = 0;
        loadingConstraints.gridy = 0;
        loadingConstraints.weightx = 1;
        loadingConstraints.weighty = 0;
        loadingConstraints.gridwidth = GridBagConstraints.REMAINDER;
        loadingConstraints.insets = new Insets(5, 5, 5, 5);
        add(loadingLabel, loadingConstraints);
    }

    protected abstract void addComponents();

    protected void loadData() {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();

        if (isCleaning())
            return;
        ViewConfiguration selectedViewConfiguration = viewConfigurationBean.getViewConfiguration();
        if (selectedViewConfiguration == null) {
            return;
        }
        ViewConfigurationData selectedViewConfigurationData = selectedViewConfiguration.getData();

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
        DbData recoveredData = null;
        try {
            if (!isCleaning()) {
                recoveredData = extractingManager.retrieveData(param, selectedViewConfigurationData
                        .isHistoric(), selectedViewConfigurationData.getSamplingType());
            }
            if ((recoveredData != null) && (!isCleaning())) {
                splitedData = recoveredData.splitDbData();
            }
            else {
                splitedData = null;
            }
        }
        catch (Exception e) {
            if (isCleaning())
                return;
            splitedData = null;
            e.printStackTrace();
            if (LoggerFactory.getCurrentImpl() != null) {
                LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            }
        }
        if (splitedData != null) {
            // if no data on read, splitedData[0] = null
            if (splitedData[0] != null) {
                if (!extractingManager.isShowRead()) {
                    splitedData[0] = null;
                }
                else if (splitedData[0].getMax_x() < 1) {
                    splitedData[0] = null;
                }
            }
            // if no data on write, splitedData[1] = null
            if (splitedData[1] != null) {
                if (!extractingManager.isShowWrite()) {
                    splitedData[1] = null;
                }
                else if (splitedData[1].getMax_x() < 1) {
                    splitedData[1] = null;
                }
            }
            // if no data on read and write, splitedData = null
            if (splitedData[0] == null && splitedData[1] == null) {
                splitedData = null;
            }
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

    public String getFullName() {
        if (spectrum == null) {
            return "";
        }
        else {
            return spectrum.getCompleteName();
        }
    }

    @Override
    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl();

        try {
            if (extractingManager.isCanceled() || isCleaning())
                return;
            loadData();
            if (extractingManager.isCanceled() || isCleaning())
                return;
            initComponents();
            if (extractingManager.isCanceled() || isCleaning())
                return;
            addComponents();
            if (extractingManager.isCanceled() || isCleaning())
                return;
            if (loadingLabel != null) {
                loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setToolTipText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setForeground(Color.GREEN);
            }
            revalidate();
            repaint();
        }
        catch (OutOfMemoryError oome) {
            if ((!extractingManager.isCanceled()) && (!isCleaning())) {
                outOfMemoryErrorManagement();
            }
        }
        catch (Exception e) {
            if ((!extractingManager.isCanceled()) && (!isCleaning())) {
                e.printStackTrace();
            }
        }
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public void saveDataToFile(String path) {
        try {
            if (path != null) {
                File toSave = new File(path);
                File directory = toSave.getParentFile();
                if (directory != null) {
                    setFileDirectory(directory.getAbsolutePath());
                    writeDataInFile(toSave);
                }
            }
        }
        catch (OutOfMemoryError oome) {
            if ((!ExtractingManagerFactory.getCurrentImpl().isCanceled()) && (!isCleaning())) {
                outOfMemoryErrorManagement();
            }
        }
    }

    protected void writeDataInFile(File toSave) {
        int ok = JOptionPane.YES_OPTION;
        if (toSave != null) {
            if (toSave.exists()) {
                ok = JOptionPane.showConfirmDialog(this, Messages
                        .getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS"), Messages
                        .getMessage("DIALOGS_FILE_CHOOSER_FILE_EXISTS_TITLE"),
                        JOptionPane.YES_NO_OPTION);
            }
            if (ok == JOptionPane.YES_OPTION) {
                SpectrumDataWriter.writeDataInFile(toSave, getReadData(), getWriteData(),
                        getReadFilter(), getWriteFilter(), getViewSpectrumType(), this);
            }
        }
    }

    protected List<Integer> getReadFilter() {
        return null;
    }

    protected List<Integer> getWriteFilter() {
        return null;
    }

    public DbData getReadData() {
        if (splitedData == null) {
            return null;
        }
        else {
            return splitedData[0];
        }
    }

    public DbData getWriteData() {
        if (splitedData == null) {
            return null;
        }
        else {
            return splitedData[1];
        }
    }

    public int getViewSpectrumType() {
        return ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_INDEX;
    }

    public abstract boolean isCleaning();

}
