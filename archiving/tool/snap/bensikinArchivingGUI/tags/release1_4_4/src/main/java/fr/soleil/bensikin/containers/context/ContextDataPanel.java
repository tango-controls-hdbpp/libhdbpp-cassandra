//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextDataPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextDataPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ContextDataPanel.java,v $
// Revision 1.10  2007/08/23 15:28:48  ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.9  2006/11/29 09:58:52  ounsy
// minor changes
//
// Revision 1.8  2006/05/03 13:04:14  ounsy
// modified the limited operator rights
//
// Revision 1.7  2006/01/12 10:27:48  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:21:11  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

import fr.soleil.bensikin.actions.listeners.ContextTextListener;
import fr.soleil.bensikin.actions.listeners.SelectedContextListener;
import fr.soleil.bensikin.components.context.detail.IDTextField;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;

/**
 * Contains the attributes-independant information about the current text:
 * id, date (never user-modifiable fields)
 * and name, author, reason, description (user-modifiable fields).
 *
 * @author CLAISSE
 */
public class ContextDataPanel extends JPanel
{
    private IDTextField IDField = null;
    private JTextField CreationDateField = null;
    private JTextField NameField = null;
    private JTextField AuthorNameField = null;
    private JTextField ReasonField = null;
    private JTextField DescriptionField = null;

    //private Color disabledColor;

    private static ContextDataPanel instance = null;
    private static boolean isInputEnabled = true;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static ContextDataPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ContextDataPanel();
            instance.applyEnabled ();
        }

        return instance;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#isEnabled()
     */
    @Override
	public boolean isEnabled ()
    {
        return NameField.isEnabled();
        //we use the name field in this test, but it could as well have been any other field
        // (except never editable field id and date)
    }

    public void applyEnabled ()
    {
        if ( ! ContextDataPanel.isInputEnabled )
        {
            NameField.setEnabled ( false );
            AuthorNameField.setEnabled ( false );
            ReasonField.setEnabled ( false );
            DescriptionField.setEnabled ( false );
        }
    }

    public static void disableInput ()
    {
        ContextDataPanel.isInputEnabled  = false;
        if ( instance != null )
        {
            instance.applyEnabled ();
        }
    }

    /**
     * Enables modification of the text fields
     *
     * @param forNewContext If true
     */
    public void enableInput ( boolean forNewContext )
    {
        Color enabledColor = Color.WHITE;

        NameField.setEnabled( true );
        //disabledColor = NameField.getBackground();
        NameField.setBackground( enabledColor );

        AuthorNameField.setEnabled( true );
        AuthorNameField.setBackground( enabledColor );

        ReasonField.setEnabled( true );
        ReasonField.setBackground( enabledColor );

        DescriptionField.setEnabled( true );
        DescriptionField.setBackground( enabledColor );

        if ( forNewContext )
        {
            this.resetFields();
        }
    }

    /**
     * 29 juin 2005
     */
    public void resetFields ()
    {
        IDField.setText( "" );
        CreationDateField.setText( "" );
        NameField.setText( "" );
        AuthorNameField.setText( "" );
        ReasonField.setText( "" );
        DescriptionField.setText( "" );
    }

    /**
     * Builds the panel
     */
    private ContextDataPanel ()
    {
        Color disabledColor = Color.lightGray;

        IDField = new IDTextField();
        IDField.setEnabled( false );
        IDField.setDisabledTextColor( Color.black );
        IDField.setBackground( disabledColor );
        IDField.addPropertyChangeListener( new SelectedContextListener() );
        IDField.setPreferredSize( new Dimension( 50 , 20 ) );
        IDField.setMaximumSize( new Dimension( 50 , 20 ) );

        CreationDateField = new JTextField();
        CreationDateField.setEnabled( false );
        CreationDateField.setDisabledTextColor( Color.black );
        CreationDateField.setBackground( disabledColor );
        CreationDateField.setPreferredSize( new Dimension( 100 , 20 ) );
        CreationDateField.setMaximumSize( new Dimension( 100 , 20 ) );

        Font font = new Font( "Arial" , Font.BOLD , 11 );
        Font smallFont = new Font( "Arial" , Font.PLAIN , 10 );

        NameField = new JTextField();
//        NameField.setPreferredSize( new Dimension( 250 , 20 ) );
        //NameField.setMaximumSize( new Dimension( 250 , 20 ) );

        AuthorNameField = new JTextField();
//        AuthorNameField.setPreferredSize( new Dimension( 250 , 20 ) );
        //AuthorNameField.setMaximumSize( new Dimension( 250 , 20 ) );

        ReasonField = new JTextField();
//        ReasonField.setPreferredSize( new Dimension( 250 , 20 ) );
        //ReasonField.setMaximumSize( new Dimension( 250 , 20 ) );

        DescriptionField = new JTextField();
//        DescriptionField.setPreferredSize( new Dimension( 250 , 20 ) );
        //DescriptionField.setMaximumSize( new Dimension( 250 , 20 ) );

        NameField.getDocument().addDocumentListener( new ContextTextListener() );
        AuthorNameField.getDocument().addDocumentListener( new ContextTextListener() );
        ReasonField.getDocument().addDocumentListener( new ContextTextListener() );
        DescriptionField.getDocument().addDocumentListener( new ContextTextListener() );

        String msgId = Messages.getMessage( "CONTEXT_DETAIL_LABELS_ID" );
        String msgTime = Messages.getMessage( "CONTEXT_DETAIL_LABELS_TIME" );
        String msgName = "(*) " + Messages.getMessage( "CONTEXT_DETAIL_LABELS_NAME" );
        String msgAuthor = "(*) " + Messages.getMessage( "CONTEXT_DETAIL_LABELS_AUTHOR" );
        String msgReason = "(*) " + Messages.getMessage( "CONTEXT_DETAIL_LABELS_REASON" );
        String msgDescription = "(*) " + Messages.getMessage( "CONTEXT_DETAIL_LABELS_DESRIPTION" );
        String oblig = "(*) " + Messages.getMessage("CONTEXT_DETAIL_LABELS_MANDATORY");

        JLabel mandatoryLabel = new JLabel( oblig );
        mandatoryLabel.setForeground( new Color( 150 , 0 , 0 ) );
        mandatoryLabel.setFont( smallFont );
        JLabel idLabel = new JLabel(msgId);
        idLabel.setFont( font );
        JLabel timeLabel = new JLabel(msgTime);
        timeLabel.setFont( font );
        JLabel nameLabel = new JLabel(msgName);
        nameLabel.setFont( font );
        nameLabel.setForeground( new Color( 150 , 0 , 0 ) );
        JLabel authorLabel = new JLabel(msgAuthor);
        authorLabel.setFont( font );
        authorLabel.setForeground( new Color( 150 , 0 , 0 ) );
        JLabel reasonLabel = new JLabel(msgReason);
        reasonLabel.setFont( font );
        reasonLabel.setForeground( new Color( 150 , 0 , 0 ) );
        JLabel descrLabel = new JLabel(msgDescription);
        descrLabel.setFont( font );
        descrLabel.setForeground( new Color( 150 , 0 , 0 ) );


		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);

		layout.setAutocreateGaps(true);

		SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.add(layout.createParallelGroup(GroupLayout.TRAILING, false)
				.add(idLabel)
				.add(timeLabel)
				.add(nameLabel)
				.add(authorLabel)
				.add(reasonLabel)
				.add(descrLabel));
		hGroup.add(layout.createParallelGroup(GroupLayout.LEADING, true)
				.add(layout.createParallelGroup(GroupLayout.LEADING, true)
						.add(IDField)
						.add(layout.createSequentialGroup()
								.add(layout.createParallelGroup(GroupLayout.LEADING, false)
										.add(CreationDateField))
								.addPreferredGap(CreationDateField, mandatoryLabel, LayoutStyle.UNRELATED, true)
								.add(layout.createParallelGroup(GroupLayout.LEADING, false)
										.add(mandatoryLabel))))
				.add(layout.createParallelGroup(GroupLayout.LEADING, true)
						.add(NameField)
						.add(AuthorNameField)
						.add(ReasonField)
						.add(DescriptionField)));
		layout.setHorizontalGroup(hGroup);


		SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(idLabel)
				.add(IDField));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(timeLabel)
				.add(CreationDateField)
				.add(mandatoryLabel));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(nameLabel)
				.add(NameField));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(authorLabel)
				.add(AuthorNameField));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(reasonLabel)
				.add(ReasonField));
		vGroup.add(layout.createParallelGroup(GroupLayout.BASELINE)
				.add(descrLabel)
				.add(DescriptionField));
		layout.setVerticalGroup(vGroup);



        //this.setMaximumSize( new Dimension( 700 , 180 ) );
//        this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 180 ) );

        GUIUtilities.setObjectBackground( this , GUIUtilities.CONTEXT_COLOR );
    }

    /**
     * @return Returns the authorNameField.
     */
    public JTextField getAuthorNameField ()
    {
        return AuthorNameField;
    }

    /**
     * @return Returns the creationDateField.
     */
    public JTextField getCreationDateField ()
    {
        return CreationDateField;
    }

    /**
     * @return Returns the descriptionField.
     */
    public JTextField getDescriptionField ()
    {
        return DescriptionField;
    }

    /**
     * @return Returns the iDField.
     */
    public IDTextField getIDField ()
    {
        return IDField;
    }

    /**
     * @return Returns the nameField.
     */
    public JTextField getNameField ()
    {
        return NameField;
    }

    /**
     * @return Returns the reasonField.
     */
    public JTextField getReasonField ()
    {
        return ReasonField;
    }

}
