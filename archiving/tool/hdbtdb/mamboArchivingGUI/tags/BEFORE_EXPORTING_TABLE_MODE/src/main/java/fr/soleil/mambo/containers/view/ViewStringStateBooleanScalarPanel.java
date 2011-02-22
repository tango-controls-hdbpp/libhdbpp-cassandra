/*
 * Synchrotron Soleil File : ViewStringStateBooleanScalarPanel.java Project :
 * Mambo_CVS Description : Author : SOLEIL Original : 6 mars 2006 Revision:
 * Author: Date: State: Log: ViewStringStateBooleanScalarPanel.java,v
 */
package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import fr.esrf.TangoDs.TangoConst;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.LoggerFactory;
import fr.soleil.mambo.actions.view.ViewCopyAction;
import fr.soleil.mambo.actions.view.ViewEditCopyAction;
import fr.soleil.mambo.components.MamboFormatableTable;
import fr.soleil.mambo.components.renderers.BooleanTableRenderer;
import fr.soleil.mambo.components.renderers.StateTableRenderer;
import fr.soleil.mambo.components.renderers.StringTableRenderer;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewStringStateBooleanScalarTableModel;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewStringStateBooleanScalarPanel extends MamboCleanablePanel {

    private static final long            serialVersionUID = -2477970690362040503L;
    private ViewConfigurationAttribute[] attributes;
    private MamboFormatableTable[]       viewTables;
    private JButton[]                    edit             = null, copy = null;
    private JLabel[]                     nameLabels;
    private final static int             minWidth         = 300;
    private final static int             maxHeight        = 100;
    private JLabel                       loadingLabel;
    private boolean                      cleaning         = false;

    public ViewStringStateBooleanScalarPanel(
            ViewConfigurationAttribute[] stringScalars) {
        super();
        loadingLabel = new JLabel(Messages
                .getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        if (loadingLabel == null) return;
        add(loadingLabel);
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        attributes = stringScalars;
        if (attributes != null && attributes.length > 0) {
            edit = new JButton[attributes.length];
            copy = new JButton[attributes.length];

            viewTables = new MamboFormatableTable[attributes.length];
            nameLabels = new JLabel[attributes.length];
            for (int i = 0; i < attributes.length; i++) {
                nameLabels[i] = new JLabel(attributes[i].getCompleteName());
                viewTables[i] = new MamboFormatableTable(
                        new ViewStringStateBooleanScalarTableModel(
                                attributes[i].getCompleteName()));
                if (attributes[i].getDataType(ViewConfiguration
                        .getSelectedViewConfiguration().getData().isHistoric()) == TangoConst.Tango_DEV_STATE) {
                    viewTables[i].setDefaultRenderer(Object.class,
                            new StateTableRenderer());
                }
                else if (attributes[i].getDataType(ViewConfiguration
                        .getSelectedViewConfiguration().getData().isHistoric()) == TangoConst.Tango_DEV_STRING) {
                    viewTables[i].setDefaultRenderer(Object.class,
                            new StringTableRenderer());
                }
                else {
                    viewTables[i].setDefaultRenderer(Object.class,
                            new BooleanTableRenderer());
                }
                GUIUtilities.setObjectBackground(nameLabels[i],
                        GUIUtilities.VIEW_COLOR);
                GUIUtilities.setObjectBackground(viewTables[i],
                        GUIUtilities.VIEW_COLOR);
            }
            addComponents();
        }
        else {
            JLabel label = new JLabel();
            GUIUtilities.setObjectBackground(label, GUIUtilities.VIEW_COLOR);
            add(label);
        }
        initLayout();
    }

    private void addComponents() {
        Dimension minDim = new Dimension(minWidth, maxHeight);
        Dimension maxDim = new Dimension(Integer.MAX_VALUE, maxHeight);
        for (int i = 0; i < viewTables.length; i++) {
            JPanel titlePanel = new JPanel();
            GUIUtilities.setObjectBackground(titlePanel,
                    GUIUtilities.VIEW_COLOR);
            copy[i] = new JButton(
                    Messages
                            .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_COPY")
                            + " (" + attributes[i].getName() + ")");
            copy[i].setEnabled(false);
            edit[i] = new JButton(
                    Messages
                            .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_EDIT_COPY")
                            + " (" + attributes[i].getName() + ")");
            edit[i].setEnabled(false);
            copy[i].setMargin(new Insets(0, 0, 0, 0));
            edit[i].setMargin(new Insets(0, 0, 0, 0));
            GUIUtilities.setObjectBackground(copy[i],
                    GUIUtilities.VIEW_COPY_COLOR);
            GUIUtilities.setObjectBackground(edit[i],
                    GUIUtilities.VIEW_COPY_COLOR);
            if (titlePanel == null) return;
            if (nameLabels[i] == null) return;
            if (copy[i] == null) return;
            if (edit[i] == null) return;
            titlePanel.add(nameLabels[i]);
            titlePanel.add(copy[i]);
            titlePanel.add(edit[i]);
            titlePanel.setLayout(new SpringLayout());
            SpringUtilities.makeCompactGrid(titlePanel, 1, titlePanel
                    .getComponentCount(), // rows, columns
                    5, 5, // init x, init y
                    5, 5, // xpad, ypad
                    true // components same size
                    );
            if (titlePanel == null) return;
            add(titlePanel);
            if (viewTables == null) return;
            if (viewTables[i] == null) return;
            JScrollPane scrollpane = new JScrollPane(viewTables[i]);
            scrollpane.setMinimumSize(minDim);
            if (viewTables.length > 1) {
                scrollpane.setPreferredSize(minDim);
                scrollpane.setMaximumSize(maxDim);
            }
            GUIUtilities.setObjectBackground(scrollpane,
                    GUIUtilities.VIEW_COLOR);
            GUIUtilities.setObjectBackground(scrollpane.getViewport(),
                    GUIUtilities.VIEW_COLOR);
            if (scrollpane == null) return;
            add(scrollpane);
            if (i < viewTables.length - 1) {
                add(Box.createVerticalGlue());
            }
        }
    }

    private void initLayout() {
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, // rows,
                // columns
                5, 5, // init x, init y
                5, 5, // xpad, ypad
                true // components same size
                );
    }

    public String getSelectedToString(int tableIndex) {
        String s = "";
        if (viewTables != null && tableIndex < viewTables.length) {
            s = nameLabels[tableIndex].getText();
            s += "\n";
            for (int i = 0; i < viewTables[tableIndex].getColumnCount(); i++) {
                s += viewTables[tableIndex].getColumnName(i)
                        + Options.getInstance().getGeneralOptions()
                                .getSeparator();
            }
            s = s.substring(0, s.lastIndexOf(Options.getInstance()
                    .getGeneralOptions().getSeparator()));
            s += "\n";
            s += viewTables[tableIndex].toString();
        }
        return s;
    }

    public void clean() {
        cleaning = true;
        removeAll();
        loadingLabel = null;
        if (attributes != null) {
            for (int i = 0; i < attributes.length; i++) {
                attributes[i] = null;
            }
            attributes = null;
        }
        if (viewTables != null) {
            for (int i = 0; i < viewTables.length; i++) {
                if (viewTables[i] != null) {
                    if (viewTables[i].getModel() != null
                            && viewTables[i].getModel() instanceof ViewStringStateBooleanScalarTableModel) {
                        ((ViewStringStateBooleanScalarTableModel) viewTables[i]
                                .getModel()).clearData();
                    }
                    viewTables[i] = null;
                }
            }
            viewTables = null;
        }
        if (nameLabels != null) {
            for (int i = 0; i < nameLabels.length; i++) {
                nameLabels[i] = null;
            }
            nameLabels = null;
        }
        if (edit != null) {
            for (int i = 0; i < edit.length; i++) {
                edit[i] = null;
            }
            edit = null;
        }
        if (copy != null) {
            for (int i = 0; i < copy.length; i++) {
                copy[i] = null;
            }
            copy = null;
        }
    }

    public void loadPanel() {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled() || cleaning) return;
        try {
            for (int i = 0; i < viewTables.length; i++) {
                try {
                    if (extractingManager.isCanceled() || cleaning) return;
                    ((ViewStringStateBooleanScalarTableModel) viewTables[i]
                            .getModel()).loadData();
                    copy[i].setEnabled(true);
                    copy[i]
                            .addActionListener(new ViewCopyAction(
                                    Messages
                                            .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_COPY")
                                            + " ("
                                            + attributes[i].getName()
                                            + ")", this, i));
                    edit[i].setEnabled(true);
                    edit[i]
                            .addActionListener(new ViewEditCopyAction(
                                    Messages
                                            .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_EDIT_COPY")
                                            + " ("
                                            + attributes[i].getName()
                                            + ")", this, i));
                    updateUI();
                    repaint();
                    loadingLabel.setText(Messages
                            .getMessage("VIEW_ATTRIBUTES_LOADED"));
                    loadingLabel.setToolTipText(Messages
                            .getMessage("VIEW_ATTRIBUTES_LOADED"));
                    loadingLabel.setForeground(Color.GREEN);
                    updateUI();
                    repaint();
                }
                catch (Exception e) {
                    if (extractingManager.isCanceled() || cleaning) return;
                    else {
                        LoggerFactory.getCurrentImpl().trace(
                                ILogger.LEVEL_ERROR, e);
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (java.lang.OutOfMemoryError oome) {
            if (extractingManager.isCanceled() || cleaning) return;
            outOfMemoryErrorManagement();
            return;

        }
    }

    public String getName() {
        return Messages
                .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_TITLE");
    }

    public String getFullName() {
        return Messages
                .getMessage("DIALOGS_VIEW_TAB_SCALAR_STRING_AND_STATE_TITLE");
    }
}
