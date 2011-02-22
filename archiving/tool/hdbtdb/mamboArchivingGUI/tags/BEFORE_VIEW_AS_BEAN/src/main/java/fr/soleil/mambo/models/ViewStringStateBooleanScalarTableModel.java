/*
 * Synchrotron Soleil File : ViewStringStateBooleanScalarTableModel.java Project
 * : Mambo_CVS Description : Author : SOLEIL Original : 7 mars 2006 Revision:
 * Author: Date: State: Log: ViewStringStateBooleanScalarTableModel.java,v
 */
package fr.soleil.mambo.models;

import java.sql.Date;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.Messages;

public class ViewStringStateBooleanScalarTableModel extends DefaultTableModel {

    private static final long          serialVersionUID = 1047005533201322779L;
    private DbData                     readReference;
    private DbData                     writeReference;
    private String                     name;
    private java.text.SimpleDateFormat genFormatUS;

    public ViewStringStateBooleanScalarTableModel(String attributeName) {
        super();
        genFormatUS = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        name = attributeName;
        readReference = null;
        writeReference = null;
    }

    @Override
    public int getRowCount() {
        int length = 0;
        if (readReference != null && readReference.getData_timed() != null) {
            length = readReference.getData_timed().length;
        }
        if (writeReference != null && writeReference.getData_timed() != null
                && writeReference.getData_timed().length > length) {
            length = writeReference.getData_timed().length;
        }
        return length;
    }

    @Override
    public int getColumnCount() {
        int col = 2;
        if (readReference != null && readReference.getData_timed() != null
                && writeReference != null
                && writeReference.getData_timed() != null) {
            if (readReference.getData_timed().length > 0
                    && writeReference.getData_timed().length > 0) {
                if ((readReference.getData_timed()[0].value != null && readReference
                        .getData_timed()[0].value.length > 0)
                        && (writeReference.getData_timed()[0].value != null && writeReference
                                .getData_timed()[0].value.length > 0)) {
                    col = 3;
                }
            }
        }
        return col;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                if (readReference != null
                        && readReference.getData_timed() != null
                        && readReference.getData_timed().length > 0) {
                    if (readReference.getData_timed()[0].value != null
                            && readReference.getData_timed()[0].value.length > 0) {
                        return genFormatUS.format(new Date(readReference
                                .getData_timed()[rowIndex].time.longValue()));
                    }
                }
                if (writeReference != null
                        && writeReference.getData_timed() != null
                        && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null
                            && writeReference.getData_timed()[0].value.length > 0) {
                        return genFormatUS.format(new Date(writeReference
                                .getData_timed()[rowIndex].time.longValue()));
                    }
                }
                return null;
            case 1:
                if (readReference != null
                        && readReference.getData_timed() != null
                        && readReference.getData_timed().length > 0) {
                    if (readReference.getData_timed()[0].value != null
                            && readReference.getData_timed()[0].value.length > 0) {
                        return readReference.getData_timed()[rowIndex].value[0];
                    }
                }
                if (writeReference != null
                        && writeReference.getData_timed() != null
                        && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null
                            && writeReference.getData_timed()[0].value.length > 0) {
                        return writeReference.getData_timed()[rowIndex].value[0];
                    }
                }
                return null;
            case 2:
                if (writeReference != null
                        && writeReference.getData_timed() != null
                        && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null
                            && writeReference.getData_timed()[0].value.length > 0) {
                        return writeReference.getData_timed()[rowIndex].value[0];
                    }
                }
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return Messages
                        .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_DATE");
            case 1:
                if (readReference != null
                        && readReference.getData_timed() != null
                        && readReference.getData_timed().length > 0) {
                    if (readReference.getData_timed()[0].value != null
                            && readReference.getData_timed()[0].value.length > 0) {
                        return Messages
                                .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_READ");
                    }
                }
                else if (writeReference != null
                        && writeReference.getData_timed() != null
                        && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null
                            && writeReference.getData_timed()[0].value.length > 0) {
                        return Messages
                                .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_WRITE");
                    }
                }
                else return Messages
                        .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_VALUE");
            case 2:
                if (writeReference != null
                        && writeReference.getData_timed() != null
                        && writeReference.getData_timed().length > 0) {
                    if (writeReference.getData_timed()[0].value != null
                            && writeReference.getData_timed()[0].value.length > 0) {
                        return Messages
                                .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_WRITE");
                    }
                }
            default:
                return "";
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void loadData() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        ViewConfiguration conf = ViewConfiguration
                .getSelectedViewConfiguration();
        if (conf != null) {
            String[] param = new String[3];
            param[0] = name;
            String startDate = "";
            String endDate = "";
            try {
                startDate = extractingManager.timeToDateSGBD(conf.getData()
                        .getStartDate().getTime());
                endDate = extractingManager.timeToDateSGBD(conf.getData()
                        .getEndDate().getTime());
            }
            catch (Exception e) {
                e.printStackTrace();
                LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
                return;
            }
            param[1] = startDate;
            param[2] = endDate;
            try {
                Hashtable<String, Object> table = extractingManager
                        .retrieveDataHash(param, conf.getData().isHistoric(),
                                conf.getData().getSamplingType());
                if (table != null) {
                    if (table
                            .get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY) != null
                            && table
                                    .get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY) instanceof DbData) {
                        readReference = (DbData) table
                                .get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY);
                    }

                    if (table
                            .get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY) != null
                            && table
                                    .get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY) instanceof DbData) {
                        writeReference = (DbData) table
                                .get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY);
                    }
                    table.clear();
                }
                table = null;
            }
            catch (DevFailed e) {
                // nothing to do : let "readReference" and "writeReference"
                // null;
                LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            }
            catch (Exception e) {
                e.printStackTrace();
                LoggerFactory.getCurrentImpl().trace(ILogger.LEVEL_ERROR, e);
            }
        }
        fireTableStructureChanged();
    }

    public void clearData() {
        readReference = null;
        writeReference = null;
    }
}
