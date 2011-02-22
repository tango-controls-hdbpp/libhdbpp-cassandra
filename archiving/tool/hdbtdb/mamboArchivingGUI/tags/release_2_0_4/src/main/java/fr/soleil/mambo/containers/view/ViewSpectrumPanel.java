/*	Synchrotron Soleil 
 *  
 *   File          :  ViewSpectrumPanel.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  24 janv. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ViewSpectrumPanel.java,v 
 *
 */
package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCViewSpectrumChartAction;
import fr.soleil.mambo.actions.view.VCViewSpectrumFilterAction;
import fr.soleil.mambo.components.editors.AttributesSelectTableEditor;
import fr.soleil.mambo.components.renderers.ViewSpectrumTableRenderer;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.models.ViewSpectrumTableModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * 
 * @author SOLEIL
 */
public class ViewSpectrumPanel extends MamboCleanablePanel {

    private ViewConfigurationAttribute spectrum;
    private JButton viewButton, allRead, allWrite, noneRead, noneWrite;
    private JLDataView[] readData;
    private JLDataView[] writeData;
    private ViewSpectrumTableModel model;
    private JTable choiceTable;
    private JPanel buttonPanel;
    private JLabel loadingLabel;
    private final static ImageIcon viewIcon = new ImageIcon( Mambo.class.getResource( "icons/View.gif" ) );
    int writable;
    private boolean cleaning = false;

    public ViewSpectrumPanel (ViewConfigurationAttribute theSpectrum) {
        super();
        readData = null;
        writeData = null;
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        this.spectrum = theSpectrum;
        if (spectrum == null || ViewConfiguration.getSelectedViewConfiguration() == null || !spectrum.isSpectrum(ViewConfiguration.getSelectedViewConfiguration().getData().isHistoric())) return;
        loadingLabel = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        add(loadingLabel);
        initLayout();
        initBorder();
    }

    private void initComponents() 
    {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        if (extractingManager.isCanceled() || cleaning) return;
        viewButton = new JButton();
        viewButton.setMargin(new Insets(0,0,0,0));
        viewButton.addActionListener( new VCViewSpectrumChartAction(this) );
        allRead = new JButton( 
                new VCViewSpectrumFilterAction(
                        Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL") + " " + Messages.getMessage("VIEW_SPECTRUM_READ"),
                        this,
                        true,
                        true
                )
        );
        allRead.setMargin(new Insets(0,0,0,0));
        noneRead = new JButton( 
                new VCViewSpectrumFilterAction(
                        Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE") + " " + Messages.getMessage("VIEW_SPECTRUM_READ"),
                        this,
                        false,
                        true
                )
        );
        noneRead.setMargin(new Insets(0,0,0,0));
        allWrite = new JButton( 
                new VCViewSpectrumFilterAction(
                        Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_ALL") + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"),
                        this,
                        true,
                        false
                )
        );
        allWrite.setMargin(new Insets(0,0,0,0));
        noneWrite = new JButton( 
                new VCViewSpectrumFilterAction(
                        Messages.getMessage("VIEW_SPECTRUM_FILTER_SELECT_NONE") + " " + Messages.getMessage("VIEW_SPECTRUM_WRITE"),
                        this,
                        false,
                        false
                )
        );
        noneWrite.setMargin(new Insets(0,0,0,0));
        if ( readData.length == 0 && writeData.length == 0 ) {
            writable = ViewSpectrumTableModel.UNKNOWN;
            viewButton.setText(Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA") + " " + getName());
            viewButton.setEnabled(false);
            model = new ViewSpectrumTableModel(0, 0, writable);
            allRead.setEnabled(false);
            noneRead.setEnabled(false);
            allWrite.setEnabled(false);
            noneWrite.setEnabled(false);
        }
        else {
            viewButton.setText(Messages.getMessage("VIEW_ACTION_VIEW_BUTTON") + " " + getName());
            viewButton.setEnabled(true);
            if (readData.length > 0) {
                allRead.setEnabled(true);
                noneRead.setEnabled(true);
                if (writeData.length > 0) {
                    allWrite.setEnabled(true);
                    noneWrite.setEnabled(true);
                    writable = ViewSpectrumTableModel.RW;
                }
                else {
                    allWrite.setEnabled(false);
                    noneWrite.setEnabled(false);
                    writable = ViewSpectrumTableModel.R;
                }
            }
            else {
                allWrite.setEnabled(true);
                noneWrite.setEnabled(true);
                allRead.setEnabled(false);
                noneRead.setEnabled(false);
                writable = ViewSpectrumTableModel.W;
            }
            model = new ViewSpectrumTableModel(readData.length, writeData.length, writable);
        }
        buttonPanel = new JPanel();
        GUIUtilities.setObjectBackground(buttonPanel, GUIUtilities.VIEW_COLOR);
        buttonPanel.setLayout(new SpringLayout());
        switch (writable) {
            case ViewSpectrumTableModel.R:
                buttonPanel.add(allRead);
                buttonPanel.add(noneRead);
                SpringUtilities.makeCompactGrid(
                        buttonPanel,
                        2, 1,
                        5, 5,
                        5, 5,
                        true
                );
                break;
            case ViewSpectrumTableModel.W:
                buttonPanel.add(allWrite);
                buttonPanel.add(noneWrite);
                SpringUtilities.makeCompactGrid(
                        buttonPanel, 
                        2, 1, 
                        5, 5, 
                        5, 5,
                        true);
                break;
            case ViewSpectrumTableModel.RW:
                buttonPanel.add(allRead);
                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(allWrite);
                buttonPanel.add(noneRead);
                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(noneWrite);
                SpringUtilities.makeCompactGrid(
                        buttonPanel, 
                        2, 3, 
                        5, 5, 
                        5, 5,
                        true);
                break;
            case ViewSpectrumTableModel.UNKNOWN:
                break;
        }
        viewButton.setIcon(viewIcon);
        GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
        choiceTable = new JTable(model);
        choiceTable.setMinimumSize(new Dimension(110,200));
        choiceTable.setDefaultRenderer(Object.class, new ViewSpectrumTableRenderer());
        choiceTable.setDefaultEditor(Object.class, new AttributesSelectTableEditor ());
        GUIUtilities.setObjectBackground(choiceTable, GUIUtilities.VIEW_COLOR);
    }

    private void addComponents() {
        JScrollPane scrollpane = new JScrollPane(choiceTable);
        GUIUtilities.setObjectBackground(scrollpane, GUIUtilities.VIEW_COLOR);
        GUIUtilities.setObjectBackground(scrollpane.getViewport(), GUIUtilities.VIEW_COLOR);
        if (buttonPanel == null) return;
        this.add(buttonPanel);
        if (scrollpane == null) return;
        this.add(scrollpane);
        if (viewButton == null) return;
        this.add(viewButton);
    }

    public void initLayout() {
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                this,
                this.getComponentCount(), 1,
                5, 5,
                5, 5,
                true
        );
    }

    private void initBorder() {
        String msg = getFullName();
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );
    }

    private void initData() 
    {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        if (cleaning) return;
        ViewConfiguration selectedViewConfiguration = ViewConfiguration.getSelectedViewConfiguration ();
        if ( selectedViewConfiguration == null ) 
        {
            return;
        }
        ViewConfigurationData selectedViewConfigurationData = selectedViewConfiguration.getData ();
        
        String [] param = new String[3];
        param[0] = getFullName();
        Timestamp start = selectedViewConfigurationData.getStartDate();
        Timestamp end = selectedViewConfigurationData.getEndDate();
        String startDate = "";
        String endDate = "";
        try 
        {
            startDate = extractingManager.timeToDateSGBD(start.getTime());
            endDate = extractingManager.timeToDateSGBD(end.getTime());
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            return;
        }
        
        param[1] = startDate;
        param[2] = endDate;
        Hashtable table;
        try 
        {
            table = extractingManager.retrieveDataHash ( param, selectedViewConfigurationData.isHistoric() , selectedViewConfigurationData.getSamplingType () );
            if ( cleaning) return;
        }
        catch (Exception e) 
        {
            if (cleaning) return;
            e.printStackTrace();
            return;
        }
        
        if (table == null) {
            //System.out.println("table null");
            readData = new JLDataView[0];
            writeData = new JLDataView[0];
            return;
        }
        if (table.get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY) != null) {
            readData = (JLDataView[])table.get(ViewConfigurationAttribute.READ_DATA_VIEW_KEY);
        }
        else {
            readData = new JLDataView[0];
        }
        if (table.get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY) != null) {
            writeData = (JLDataView[])table.get(ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY);
        }
        else {
            writeData = new JLDataView[0];
        }
        table.clear();
        table = null;
    }

    public String getName() {
        if (spectrum == null) {
            return "";
        }
        else {
            return spectrum.getName();
        }
    }

    public JLDataView[] getSelectedReadView() {
        if (model == null) {
            return null;
        }
        JLDataView[] selection = new JLDataView[model.getSelectedReadRowCount()];
        int index = 0;
        switch (writable) {
            case ViewSpectrumTableModel.R:
            case ViewSpectrumTableModel.RW:
                for (int i = 0; i < model.getRowCount(); i++) {
                    Boolean val = new Boolean(false);
                    if (model.getValueAt(i,0) instanceof Boolean)
                    {
                        val = (Boolean)model.getValueAt(i,0);
                    }
                    if (val.booleanValue()) {
                        selection[index++] = readData[i];
                    }
                }
                break;
            default: //nothing to do
        }
        
        return selection;
    }

    public JLDataView[] getSelectedWriteView() {
        if (model == null) {
            return null;
        }
        JLDataView[] selection = new JLDataView[model.getSelectedWriteRowCount()];
        int index = 0;
        switch (writable) {
            case ViewSpectrumTableModel.W:
                for (int i = 0; i < model.getRowCount(); i++) {
                    Boolean val = new Boolean(false);
                    if (model.getValueAt(i,0) instanceof Boolean)
                    {
                        val = (Boolean)model.getValueAt(i,0);
                    }
                    if (val.booleanValue()) {
                        selection[index++] = writeData[i];
                    }
                }
                break;
            case ViewSpectrumTableModel.RW:
                for (int i = 0; i < model.getRowCount(); i++) {
                    Boolean val = new Boolean(false);
                    if (model.getValueAt(i,3) instanceof Boolean)
                    {
                        val = (Boolean)model.getValueAt(i,3);
                    }
                    if (val.booleanValue()) {
                        selection[index++] = writeData[i];
                    }
                }
                break;
            default: //nothing to do
        }
        return selection;
    }

    public String getFullName() {
        if (spectrum == null) {
            return "";
        }
        else {
            return spectrum.getCompleteName();
        }
    }

    public void setAllReadSelected(boolean selected) {
        //System.out.println("writable :" + writable);
        if ( choiceTable == null || choiceTable.getRowCount() == 0 
             || writable == ViewSpectrumTableModel.UNKNOWN
             || writable == ViewSpectrumTableModel.W ) {
            return;
        }
        for (int i = 0; i < ((ViewSpectrumTableModel)choiceTable.getModel()).getReadLength(); i++) {
            choiceTable.getModel().setValueAt(new Boolean(selected), i, 0);
        }
    }

    public void setAllWriteSelected(boolean selected) {
        //System.out.println("writable :" + writable);
        if ( choiceTable == null || choiceTable.getRowCount() == 0 
                || writable == ViewSpectrumTableModel.UNKNOWN
                || writable == ViewSpectrumTableModel.R ) {
               return;
           }
        if ( writable == ViewSpectrumTableModel.W ) {
            for (int i = 0; i < choiceTable.getRowCount(); i++) {
                choiceTable.getModel().setValueAt(new Boolean(selected), i, 0);
            }
        }
        else { // RW
            for (int i = 0; i < ((ViewSpectrumTableModel)choiceTable.getModel()).getWriteLength(); i++) {
                choiceTable.getModel().setValueAt(new Boolean(selected), i, 3);
            }
        }
    }

    public void disposeTable() {
        if (choiceTable == null || choiceTable.getRowCount() == 0) {
            return;
        }
        if (choiceTable.isEditing()) {
            int row = choiceTable.getEditingRow();
            int col = choiceTable.getEditingColumn();
            Component comp = choiceTable.getEditorComponent();
            if (comp != null && comp instanceof JCheckBox) {
                choiceTable.getModel().setValueAt(  new Boolean( ((JCheckBox)comp).isSelected() ), row, col  );
            }
            choiceTable.getDefaultEditor(Object.class).cancelCellEditing();
        }
    }

    public void clean() {
        cleaning = true;
        removeAll();
        if (buttonPanel != null)
        {
            buttonPanel.removeAll();
            buttonPanel = null;
        }
        viewButton = null;
        allRead = null;
        allWrite = null;
        noneRead = null;
        noneWrite = null;
        choiceTable = null;
        model = null;
        spectrum = null;
        if (readData != null)
        {
            for (int i = 0; i < readData.length; i++)
            {
                readData[i] = null;
            }
            readData = null;
        }
        if (writeData != null)
        {
            for (int i = 0; i < writeData.length; i++)
            {
                writeData[i] = null;
            }
            writeData = null;
        }
        loadingLabel = null;
    }

    public void loadPanel ()
    {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        try
        {
            if (cleaning) return;
            initData();
            if (cleaning) return;
            initComponents();
            if (cleaning) return;
            addComponents();
            if (cleaning) return;
            initLayout();
            if (cleaning) return;
            if (loadingLabel != null)
            {
                loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setToolTipText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
                loadingLabel.setForeground(Color.GREEN);
            }
            if (extractingManager.isCanceled() || cleaning) return;
            updateUI();
            repaint();
           
        }
        catch(java.lang.OutOfMemoryError oome)
        {
           	if (extractingManager.isCanceled() || cleaning) return;
        	outOfMemoryErrorManagement(); 
        	return ;
        }
        catch(Exception e)
        {
            if (extractingManager.isCanceled()) return;
            else e.printStackTrace();
        }
    }

}
