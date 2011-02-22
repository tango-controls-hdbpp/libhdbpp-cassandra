package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.swing.JLabel;

import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public abstract class AbstractViewSpectrumPanel extends MamboCleanablePanel {

    private static final long            serialVersionUID = 2909121087835355642L;
    protected ViewConfigurationAttribute spectrum;
    protected DbData[]                   splitedData;
    protected ViewConfigurationBean      viewConfigurationBean;
    protected JLabel                     loadingLabel;
    public final static SimpleDateFormat dateFormat       = new SimpleDateFormat(
                                                                  "yyyy-MM-dd HH:mm:ss.SSS");

    public AbstractViewSpectrumPanel(ViewConfigurationAttribute theSpectrum,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        splitedData = null;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        this.spectrum = theSpectrum;
        if (spectrum == null
                || viewConfigurationBean.getViewConfiguration() == null
                || !spectrum.isSpectrum(viewConfigurationBean
                        .getViewConfiguration().getData().isHistoric()))
            return;
        loadingLabel = new JLabel(Messages
                .getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
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
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (isCleaning())
            return;
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
        } catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            return;
        }

        param[1] = startDate;
        param[2] = endDate;
        DbData recoveredData = null;
        try {
            if (!isCleaning()) {
                recoveredData = extractingManager.retrieveData(param,
                        selectedViewConfigurationData.isHistoric(),
                        selectedViewConfigurationData.getSamplingType());
            }
            if ((recoveredData != null) && (!isCleaning())) {
                splitedData = recoveredData.splitDbData();
            } else {
                splitedData = null;
            }
        } catch (Exception e) {
            if (isCleaning())
                return;
            splitedData = null;
            e.printStackTrace();
            if (LoggerFactory.getCurrentImpl() != null) {
                LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            }
        }

    }

    public String getName() {
        if (spectrum == null) {
            return "";
        } else {
            return spectrum.getName();
        }
    }

    public String getFullName() {
        if (spectrum == null) {
            return "";
        } else {
            return spectrum.getCompleteName();
        }
    }

    @Override
    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

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
            if (extractingManager.isCanceled() || isCleaning())
                return;
            if (loadingLabel != null) {
                loadingLabel.setText(Messages
                        .getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setToolTipText(Messages
                        .getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setForeground(Color.GREEN);
            }
            revalidate();
            repaint();
        } catch (OutOfMemoryError oome) {
            if (extractingManager.isCanceled() || isCleaning())
                return;
            outOfMemoryErrorManagement();
        } catch (Exception e) {
            if (extractingManager.isCanceled() || isCleaning())
                return;
            else
                e.printStackTrace();
        }
    }

    public abstract boolean isCleaning();

}
