package fr.soleil.mambo.containers.view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Timestamp;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.Tango.DevFailed;
import fr.soleil.mambo.components.view.images.PartialImageDataTable;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * 
 * @author SOLEIL
 */
public class ViewImagePanel extends MamboCleanablePanel 
{
    private ViewConfigurationAttribute image;
    private boolean isHistoric;
    
    private JLabel startDateField;
    private JLabel endDateField;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel loadingLabel;
    private JPanel rowsPanel;
    private JScrollPane scrollPane;
    
    private String [] param;
    private Vector partialImageData;

    public ViewImagePanel (ViewConfigurationAttribute theImage) 
    {
        super();
        
        GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
        this.image = theImage;
        
        if ( image == null )
        {
        	return;
        }
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration ();
        if ( selectedVC == null )
        {
        	return;
        }
        this.isHistoric = selectedVC.getData ().isHistoric ();
        if ( ! image.isImage ( this.isHistoric ) )
        {
        	return;
        }
        
        initData();
        initComponents();
        addComponents();
        
        initLayout();
        initBorder();
    }

    private void initComponents() 
    {
        loadingLabel = new JLabel(Messages.getMessage("VIEW_ATTRIBUTES_NOT_LOADED"));
        loadingLabel.setForeground(Color.RED);
        startDateField = new JLabel ();
        endDateField = new JLabel ();

        startDateField.setText (  param [1] );
        endDateField.setText (  param [2] );

        startDateLabel = new JLabel ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_START_DATE" ) );
        endDateLabel = new JLabel ( Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_END_DATE" ) );
        Font newFont = startDateLabel.getFont().deriveFont( Font.ITALIC );
        startDateLabel.setFont( newFont );
        endDateLabel.setFont( newFont );

        rowsPanel = new JPanel ();
        GUIUtilities.setObjectBackground ( rowsPanel , GUIUtilities.VIEW_COLOR );

            JPanel emptyPanel = new JPanel();
        scrollPane = new JScrollPane(emptyPanel);
            GUIUtilities.setObjectBackground ( emptyPanel , GUIUtilities.VIEW_COLOR );
        GUIUtilities.setObjectBackground ( scrollPane , GUIUtilities.VIEW_COLOR );
        GUIUtilities.setObjectBackground ( scrollPane.getViewport() , GUIUtilities.VIEW_COLOR );
    }

    private void addComponents() 
    {
        this.add(loadingLabel);
        Box topBox = new Box( BoxLayout.X_AXIS );
        
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( startDateLabel );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( startDateField );
        topBox.add( Box.createHorizontalStrut( 10 ) );
        topBox.add( endDateLabel );
        topBox.add( Box.createHorizontalStrut( 5 ) );
        topBox.add( endDateField );
        topBox.add( Box.createHorizontalGlue() );
        topBox.setPreferredSize(new Dimension(100, 40));
        topBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        this.add ( topBox );
        
        rowsPanel.add ( scrollPane );
        this.add ( rowsPanel );
    }

    public void initLayout() 
    {
        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid
        (
                this,
                this.getComponentCount(), 1,
                0, 0,
                0, 0,
                true
        );
        rowsPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid
        (
                rowsPanel,
                rowsPanel.getComponentCount(), 1,
                0, 0,
                0, 0,
                false
        );
       
        //this.setLayout ( new BoxLayout ( this , BoxLayout.Y_AXIS ) );
    }

    private void initBorder() 
    {
        String msg = getFullName ();
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
        
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration ();
        param = new String[3];
        param[0] = getFullName ();
        Timestamp start = selectedVC.getData().getStartDate();
        Timestamp end = selectedVC.getData().getEndDate();
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
        
        partialImageData = new Vector (); 
        try 
        {
            partialImageData = extractingManager.retrieveImageDatas ( param , this.isHistoric , selectedVC.getData().getSamplingType() );
        } 
        catch (DevFailed e1) 
        {
            e1.printStackTrace();
        }
    }

    public String getName() 
    {
        if (image == null) 
        {
            return "";
        }
        else 
        {
            return image.getName();
        }
    }

    public String getFullName() 
    {
        if (image == null) 
        {
            return "";
        }
        else 
        {
            return image.getCompleteName();
        }
    }

    public void clean() 
    {
        removeAll();
        loadingLabel = null;
        startDateField = null;
        endDateField = null;
        startDateLabel = null;
        endDateLabel = null;
        loadingLabel = null;
        rowsPanel = null;
        scrollPane = null;
        param = null;
        partialImageData.clear();
        partialImageData = null;
    }

    public void loadPanel ()
    {
        IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl ();
        
        if (extractingManager.isCanceled()) return;
        try
        {
            rowsPanel.remove(scrollPane);
            PartialImageDataTable table = new PartialImageDataTable ( partialImageData, image.getDisplayFormat(this.isHistoric) );
            table.setImageName ( param [ 0 ] );

            if (table.getRowCount() == 0)
            {
                JLabel noData = new JLabel (Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA"));
                JPanel emptyPanel = new JPanel();
                GUIUtilities.setObjectBackground ( emptyPanel , GUIUtilities.VIEW_COLOR );
                emptyPanel.add(noData);
                emptyPanel.add(Box.createVerticalGlue());
                emptyPanel.setLayout(new SpringLayout());
                SpringUtilities.makeCompactGrid
                (
                        emptyPanel,
                        emptyPanel.getComponentCount(), 1,
                        0, 0,
                        0, 0,
                        false
                );
                scrollPane = new JScrollPane ( emptyPanel );
            }
            else scrollPane = new JScrollPane ( table );
            rowsPanel.add(scrollPane);
            initLayout();
            loadingLabel.setText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
            loadingLabel.setToolTipText(Messages.getMessage("VIEW_ATTRIBUTES_LOADED"));
            loadingLabel.setForeground(Color.GREEN);
            updateUI();
            repaint();
        }
        catch(Exception e)
        {
            if (extractingManager.isCanceled()) return;
            else e.printStackTrace();
        }
    }
           
}
