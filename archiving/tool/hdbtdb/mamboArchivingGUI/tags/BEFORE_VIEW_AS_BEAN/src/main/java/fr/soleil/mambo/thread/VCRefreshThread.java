package fr.soleil.mambo.thread;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.components.view.VCAttributesRecapTree;
import fr.soleil.mambo.containers.view.ViewActionPanel;
import fr.soleil.mambo.containers.view.ViewAttributesTreePanel;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.Messages;

public class VCRefreshThread extends Thread {

    private boolean canceled;

    public VCRefreshThread() {
        super();
        canceled = false;
    }

    @Override
    public void run() {
        if ((!canceled) && (VCAttributesRecapTree.getInstance() != null)) {
            VCAttributesRecapTree.getInstance().saveExpandedPath();
        }
        if ((!canceled) && (VCAttributesPropertiesTree.getInstance() != null)) {
            VCAttributesPropertiesTree.getInstance().saveExpandedPath();
        }

        this.refreshGraph();

        if ((!canceled) && (VCAttributesRecapTree.getInstance() != null)) {
            VCAttributesRecapTree.getInstance().openExpandedPath();
        }
        if ((!canceled) && (VCAttributesPropertiesTree.getInstance() != null)) {
            VCAttributesPropertiesTree.getInstance().openExpandedPath();
        }

    }

    private void refreshGraph() {
        if (ExtractingManagerFactory.getCurrentImpl() != null) {
            // allow database communication
            ExtractingManagerFactory.getCurrentImpl().allow();
        }
        ViewConfiguration selectedVC = ViewConfiguration
                .getSelectedViewConfiguration();
        if ((!canceled) && (selectedVC != null)) {
            try {
                boolean modification_status = selectedVC.isModified();
                boolean doForceTdbExport = Options.getInstance().getVcOptions()
                        .isDoForceTdbExport();

                if ((!canceled) && selectedVC.getData() != null
                        && (!selectedVC.getData().isHistoric())) {
                    prepareLegends();
                    if (!canceled) {
                        GeneralTab.getInstance().getDateRangeBox().update(
                                ViewAttributesTreePanel.getInstance()
                                        .getDateRangeBox());
                        if (!canceled) {
                            if (doForceTdbExport) {
                                if ((!canceled)
                                        && (selectedVC.getAttributes() != null)) {
                                    LoggerFactory
                                            .getCurrentImpl()
                                            .trace(
                                                    ILogger.LEVEL_INFO,
                                                    Messages
                                                            .getMessage("VIEW_ACTION_EXPORT"));
                                    TreeMap<String, ViewConfigurationAttribute> attrs = selectedVC
                                            .getAttributes().getAttributes();
                                    Set<String> keySet = attrs.keySet();
                                    Iterator<String> keyIterator = keySet
                                            .iterator();
                                    while ((!canceled) && keyIterator.hasNext()) {
                                        String attributeName = keyIterator
                                                .next();
                                        try {
                                            if (!canceled) {
                                                ArchivingManagerFactory
                                                        .getCurrentImpl()
                                                        .exportData2Tdb(
                                                                attributeName,
                                                                GeneralTab
                                                                        .getInstance()
                                                                        .getDateRangeBox()
                                                                        .getEndDateField()
                                                                        .getText());
                                            }
                                        }
                                        catch (ArchivingException e) {
                                            String msg = Messages
                                                    .getMessage("VIEW_ACTION_EXPORT_WARNING")
                                                    + attributeName;
                                            LoggerFactory
                                                    .getCurrentImpl()
                                                    .trace(
                                                            ILogger.LEVEL_WARNING,
                                                            msg);
                                            LoggerFactory
                                                    .getCurrentImpl()
                                                    .trace(
                                                            ILogger.LEVEL_WARNING,
                                                            e);
                                        }
                                    } // end while ((!canceled) &&
                                    // keyIterator.hasNext())
                                } // end if ((!canceled) &&
                                // (selectedVC.getAttributes() != null))
                            } // end if (doForceTdbExport)
                            else if (!canceled) {
                                LoggerFactory
                                        .getCurrentImpl()
                                        .trace(
                                                ILogger.LEVEL_INFO,
                                                Messages
                                                        .getMessage("VIEW_ACTION_NO_EXPORT"));
                            }
                        } // end if (!canceled)
                    } // end if (!canceled)
                } // end if ((!canceled) && selectedVC.getData() != null &&
                // (!selectedVC.getData().isHistoric()))
                if (!canceled) {
                    ViewConfigurationData newData = null;
                    // --setting the generic plot data
                    newData = new ViewConfigurationData(ChartGeneralTabbedPane
                            .getInstance().getGeneralChartProperties(),
                            ChartGeneralTabbedPane.getInstance().getY1Axis(),
                            ChartGeneralTabbedPane.getInstance().getY2Axis());

                    newData.setHistoric(ViewConfiguration
                            .getCurrentViewConfiguration().getData()
                            .isHistoric());
                    newData.setName(ViewConfiguration
                            .getCurrentViewConfiguration().getData().getName());
                    newData.setSamplingType(ViewConfiguration
                            .getCurrentViewConfiguration().getData()
                            .getSamplingType());
                    // --setting the generic plot data
                    if (canceled) {
                        newData = null;
                    }
                    else {
                        if (ViewConfigurationData
                                .verifyDates(ViewAttributesTreePanel
                                        .getInstance().getDateRangeBox())) {
                            newData.upDateVCData(ViewAttributesTreePanel
                                    .getInstance().getDateRangeBox());
                        }
                        if (!canceled) {
                            // This is where database communication
                            // happens
                            ViewAttributesGraphPanel.getInstance()
                                    .resetComponents();
                            ViewAttributesGraphPanel.getInstance().repaint();
                            if (!canceled) {
                                selectedVC.setModified(modification_status);
                                ViewActionPanel.getInstance()
                                        .refreshButtonDefaultColor();
                            }
                        }
                    }
                } // end if (!canceled)
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        } // end if ((!canceled) && (selectedVC != null))
    } // end run()

    private void prepareLegends() {
        if (!canceled) {
            ViewConfiguration.getSelectedViewConfiguration().refreshContent();
        }
    }

    public void cancel() {
        canceled = true;
        if (ExtractingManagerFactory.getCurrentImpl() != null) {
            // Doing so avoids any further database communication
            ExtractingManagerFactory.getCurrentImpl().cancel();
        }
    }

}
