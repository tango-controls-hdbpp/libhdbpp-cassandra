// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCEditDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCEditDialog.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: VCEditDialog.java,v $
// Revision 1.8 2007/05/11 09:14:01 ounsy
// VCEditDialog resized
//
// Revision 1.7 2007/05/10 14:48:16 ounsy
// possibility to change "no value" String in chart data file (default is "*")
// through vc option
//
// Revision 1.6 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.5 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.4 2006/01/24 12:51:29 ounsy
// Bug of the new VC replacing the former selected VC corrected
//
// Revision 1.3 2005/12/15 11:32:18 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.mambo.actions.CancelAction;
import fr.soleil.mambo.actions.view.VCBackAction;
import fr.soleil.mambo.actions.view.VCFinishAction;
import fr.soleil.mambo.actions.view.VCNextAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.Messages;

public class VCEditDialog extends JDialog {

    private static final long serialVersionUID = 8508162386974627363L;

    private class VCEditWindowAdapter extends WindowAdapter {

        private VCEditDialog toClose;

        public VCEditWindowAdapter(VCEditDialog _toClose) {
            this.toClose = _toClose;
        }

        public void windowClosing(WindowEvent e) {
            CancelAction.performCancel(this.toClose);
        }
    }

    private GeneralTab                  generalTab;
    private AttributesTab               attributesTab;
    private AttributesPlotPropertiesTab attributesPlotPropertiesTab;
    private ExpressionTab               expressionTab;
    private JScrollPane                 generalScrollPane;
    private JScrollPane                 attributesScrollPane;
    private JScrollPane                 attributesPlotPropertiesScrollPane;
    private JScrollPane                 expressionScrollPane;

    private JPanel                      mainPanel;
    private JButton                     backButton;
    private JButton                     nextButton;
    private JButton                     finishButton;
    private JButton                     cancelButton;
    private VCCustomTabbedPane          vcCustomTabbedPane;
    private boolean                     newVC;

    private VCBackAction                backAction;
    private VCNextAction                nextAction;
    private VCFinishAction              finishAction;
    private ViewConfigurationBean       viewConfigurationBean;

    private JPanel                      buttonPanel;

    public VCEditDialog(ViewConfigurationBean viewConfigurationBean) {
        super(MamboFrame.getInstance(), Messages
                .getMessage("DIALOGS_EDIT_VC_TITLE"), true);
        this.viewConfigurationBean = viewConfigurationBean;
        initComponents();
        addComponents();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new VCEditWindowAdapter(this));
        newVC = false;
    }

    /**
     * 5 juil. 2005
     */
    private void initComponents() {
        vcCustomTabbedPane = new VCCustomTabbedPane(this);
        mainPanel = new JPanel(new GridBagLayout());

        String msgBack = Messages.getMessage("DIALOGS_EDIT_VC_BACK");
        String msgNext = Messages.getMessage("DIALOGS_EDIT_VC_NEXT");
        String msgFinish = Messages.getMessage("DIALOGS_EDIT_VC_FINISH");
        String msgCancel = Messages.getMessage("DIALOGS_EDIT_VC_CANCEL");

        attributesPlotPropertiesTab = new AttributesPlotPropertiesTab(
                viewConfigurationBean, this);

        generalTab = new GeneralTab(viewConfigurationBean, this);
        generalScrollPane = new JScrollPane(generalTab);

        attributesTab = new AttributesTab(viewConfigurationBean, this,
                attributesPlotPropertiesTab.getVcAttributesPropertiesTree());
        attributesScrollPane = new JScrollPane(attributesTab);

        attributesPlotPropertiesTab.setPreferredSize(new Dimension(600, 830));
        attributesPlotPropertiesScrollPane = new JScrollPane(
                attributesPlotPropertiesTab);

        expressionTab = new ExpressionTab(viewConfigurationBean, this);
        expressionTab.setPreferredSize(new Dimension(610, 750));
        expressionScrollPane = new JScrollPane(expressionTab);

        backAction = new VCBackAction(msgBack, this);
        backButton = new JButton(backAction);
        nextAction = new VCNextAction(msgNext, this);
        nextButton = new JButton(nextAction);
        finishAction = new VCFinishAction(msgFinish, viewConfigurationBean);
        finishButton = new JButton(finishAction);
        cancelButton = new JButton(new CancelAction(msgCancel, this));

        buttonPanel = new JPanel(new GridBagLayout());
    }

    /**
     * 8 juil. 2005
     */
    private void addComponents() {
        // START BUTTON PANEL
        Insets defaultInsets = new Insets(0, 10, 0, 0);
        GridBagConstraints firstGlueConstraints = new GridBagConstraints();
        firstGlueConstraints.fill = GridBagConstraints.HORIZONTAL;
        firstGlueConstraints.gridx = 0;
        firstGlueConstraints.gridy = 0;
        firstGlueConstraints.weightx = 0.5;
        firstGlueConstraints.weighty = 0;
        buttonPanel.add(Box.createGlue(), firstGlueConstraints);
        GridBagConstraints backButtonConstraints = new GridBagConstraints();
        backButtonConstraints.fill = GridBagConstraints.NONE;
        backButtonConstraints.gridx = 1;
        backButtonConstraints.gridy = 0;
        backButtonConstraints.weightx = 0;
        backButtonConstraints.weighty = 0;
        backButtonConstraints.insets = defaultInsets;
        buttonPanel.add(backButton, backButtonConstraints);
        GridBagConstraints nextButtonConstraints = new GridBagConstraints();
        nextButtonConstraints.fill = GridBagConstraints.NONE;
        nextButtonConstraints.gridx = 2;
        nextButtonConstraints.gridy = 0;
        nextButtonConstraints.weightx = 0;
        nextButtonConstraints.weighty = 0;
        nextButtonConstraints.insets = defaultInsets;
        buttonPanel.add(nextButton, nextButtonConstraints);
        GridBagConstraints finishButtonConstraints = new GridBagConstraints();
        finishButtonConstraints.fill = GridBagConstraints.NONE;
        finishButtonConstraints.gridx = 3;
        finishButtonConstraints.gridy = 0;
        finishButtonConstraints.weightx = 0;
        finishButtonConstraints.weighty = 0;
        finishButtonConstraints.insets = new Insets(0, 4 * defaultInsets.left,
                0, 0);
        buttonPanel.add(finishButton, finishButtonConstraints);
        GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
        cancelButtonConstraints.fill = GridBagConstraints.NONE;
        cancelButtonConstraints.gridx = 4;
        cancelButtonConstraints.gridy = 0;
        cancelButtonConstraints.weightx = 00;
        cancelButtonConstraints.weighty = 0;
        cancelButtonConstraints.insets = defaultInsets;
        buttonPanel.add(cancelButton, cancelButtonConstraints);
        GridBagConstraints secondGlueConstraints = new GridBagConstraints();
        secondGlueConstraints.fill = GridBagConstraints.HORIZONTAL;
        secondGlueConstraints.gridx = 5;
        secondGlueConstraints.gridy = 0;
        secondGlueConstraints.weightx = 0.5;
        secondGlueConstraints.weighty = 0;
        buttonPanel.add(Box.createGlue(), secondGlueConstraints);
        // END BUTTON PANEL

        String msgDisplayTab = Messages
                .getMessage("DIALOGS_EDIT_VC_GENERAL_TITLE");
        String msgPrint = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_TITLE");
        String msgLogs = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_TITLE");
        String msgExpr = Messages.getMessage("EXPRESSION_EVALUATION_TITLE");

        vcCustomTabbedPane.addTab(msgDisplayTab, generalScrollPane);
        vcCustomTabbedPane.addTab(msgPrint, attributesScrollPane);
        vcCustomTabbedPane.addTab(msgLogs, attributesPlotPropertiesScrollPane);
        vcCustomTabbedPane.addTab(msgExpr, expressionScrollPane);

        initBackAndNextStatus();

        GridBagConstraints vcCustomTabbedPaneConstraints = new GridBagConstraints();
        vcCustomTabbedPaneConstraints.fill = GridBagConstraints.BOTH;
        vcCustomTabbedPaneConstraints.gridx = 0;
        vcCustomTabbedPaneConstraints.gridy = 0;
        vcCustomTabbedPaneConstraints.weightx = 1;
        vcCustomTabbedPaneConstraints.weighty = 1;
        mainPanel.add(vcCustomTabbedPane, vcCustomTabbedPaneConstraints);
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 1;
        buttonPanelConstraints.weightx = 1;
        buttonPanelConstraints.weighty = 0;
        mainPanel.add(buttonPanel, buttonPanelConstraints);

        setContentPane(mainPanel);
    }

    private void initBackAndNextStatus() {
        vcCustomTabbedPane.setSelectedIndex(0);
        vcCustomTabbedPane.setEnabledAt(1, false);
        vcCustomTabbedPane.setEnabledAt(2, false);
        vcCustomTabbedPane.setEnabledAt(3, false);
        backAction.setEnabled(false);
        nextAction.setEnabled(true);
        finishAction.setEnabled(false);
    }

    public void resetTabbedPane() {
        vcCustomTabbedPane.setEnabledAt(0, true);
        this.initBackAndNextStatus();
    }

    public void setNewVC(boolean vc) {
        newVC = vc;
    }

    public boolean isNewVC() {
        return newVC;
    }

    public GeneralTab getGeneralTab() {
        return generalTab;
    }

    public AttributesTab getAttributesTab() {
        return attributesTab;
    }

    public ExpressionTab getExpressionTab() {
        return expressionTab;
    }

    public AttributesPlotPropertiesTab getAttributesPlotPropertiesTab() {
        return attributesPlotPropertiesTab;
    }

    public VCNextAction getNextAction() {
        return nextAction;
    }

    public VCBackAction getBackAction() {
        return backAction;
    }

    public VCFinishAction getFinishAction() {
        return finishAction;
    }

    public VCCustomTabbedPane getVcCustomTabbedPane() {
        return vcCustomTabbedPane;
    }

}
