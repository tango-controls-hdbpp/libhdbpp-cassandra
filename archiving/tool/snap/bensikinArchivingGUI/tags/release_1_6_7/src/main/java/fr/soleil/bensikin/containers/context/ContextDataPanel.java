// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextDataPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ContextDataPanel.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ContextDataPanel.java,v $
// Revision 1.10 2007/08/23 15:28:48 ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.9 2006/11/29 09:58:52 ounsy
// minor changes
//
// Revision 1.8 2006/05/03 13:04:14 ounsy
// modified the limited operator rights
//
// Revision 1.7 2006/01/12 10:27:48 ounsy
// minor changes
//
// Revision 1.6 2005/12/14 16:21:11 ounsy
// minor changes
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.soleil.bensikin.actions.listeners.ContextTextListener;
import fr.soleil.bensikin.actions.listeners.SelectedContextListener;
import fr.soleil.bensikin.components.context.detail.IDTextField;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;

/**
 * Contains the attributes-independant information about the current text: id, date (never
 * user-modifiable fields) and name, author, reason, description (user-modifiable fields).
 * 
 * @author CLAISSE
 */
public class ContextDataPanel extends JPanel {

    private static final long serialVersionUID = 4693572269520139244L;
    private IDTextField idField = null;
    private JTextField creationDateField = null;
    private JTextField nameField = null;
    private JTextField authorNameField = null;
    private JTextField reasonField = null;
    private JTextField descriptionField = null;
    private JTextField attributeCountField = null;

    // private Color disabledColor;

    private static ContextDataPanel instance = null;
    private static boolean isInputEnabled = true;

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static ContextDataPanel getInstance() {
        if (instance == null) {
            instance = new ContextDataPanel();
            instance.applyEnabled();
        }

        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Component#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return nameField.isEnabled();
        // we use the name field in this test, but it could as well have been
        // any other field
        // (except never editable field id and date)
    }

    public void applyEnabled() {
        if (!ContextDataPanel.isInputEnabled) {
            nameField.setEnabled(false);
            authorNameField.setEnabled(false);
            reasonField.setEnabled(false);
            descriptionField.setEnabled(false);
        }
    }

    public static void disableInput() {
        ContextDataPanel.isInputEnabled = false;
        if (instance != null) {
            instance.applyEnabled();
        }
    }

    /**
     * Enables modification of the text fields
     * 
     * @param forNewContext If true
     */
    public void enableInput(boolean forNewContext) {
        Color enabledColor = Color.WHITE;

        nameField.setEnabled(true);
        // disabledColor = NameField.getBackground();
        nameField.setBackground(enabledColor);

        authorNameField.setEnabled(true);
        authorNameField.setBackground(enabledColor);

        reasonField.setEnabled(true);
        reasonField.setBackground(enabledColor);

        descriptionField.setEnabled(true);
        descriptionField.setBackground(enabledColor);

        if (forNewContext) {
            this.resetFields();
        }
    }

    /**
     * 29 juin 2005
     */
    public void resetFields() {
        idField.setText("");
        creationDateField.setText("");
        nameField.setText("");
        authorNameField.setText("");
        reasonField.setText("");
        descriptionField.setText("");
        attributeCountField.setText("");
    }

    /**
     * Builds the panel
     */
    private ContextDataPanel() {
        Color disabledColor = Color.lightGray;

        idField = new IDTextField(5);
        idField.setMinimumSize(idField.getPreferredSize());
        idField.setEnabled(false);
        idField.setDisabledTextColor(Color.black);
        idField.setBackground(disabledColor);
        idField.addPropertyChangeListener(new SelectedContextListener());

        creationDateField = new JTextField(10);
        creationDateField.setMinimumSize(creationDateField.getPreferredSize());
        creationDateField.setEnabled(false);
        creationDateField.setDisabledTextColor(Color.black);
        creationDateField.setBackground(disabledColor);

        Font font = new Font("Arial", Font.BOLD, 11);
        Font smallFont = new Font("Arial", Font.PLAIN, 10);

        nameField = new JTextField(20);
        nameField.setMinimumSize(nameField.getPreferredSize());
        // NameField.setPreferredSize( new Dimension( 250 , 20 ) );
        // NameField.setMaximumSize( new Dimension( 250 , 20 ) );

        authorNameField = new JTextField(20);
        authorNameField.setMinimumSize(authorNameField.getPreferredSize());
        // AuthorNameField.setPreferredSize( new Dimension( 250 , 20 ) );
        // AuthorNameField.setMaximumSize( new Dimension( 250 , 20 ) );

        reasonField = new JTextField(20);
        reasonField.setMinimumSize(reasonField.getPreferredSize());
        // ReasonField.setPreferredSize( new Dimension( 250 , 20 ) );
        // ReasonField.setMaximumSize( new Dimension( 250 , 20 ) );

        descriptionField = new JTextField(20);
        descriptionField.setMinimumSize(descriptionField.getPreferredSize());
        // DescriptionField.setPreferredSize( new Dimension( 250 , 20 ) );
        // DescriptionField.setMaximumSize( new Dimension( 250 , 20 ) );

        attributeCountField = new JTextField(10);
        attributeCountField.setEnabled(false);
        attributeCountField.setEditable(false);
        attributeCountField.setDisabledTextColor(Color.black);
        attributeCountField.setBackground(disabledColor);
        attributeCountField.setText("");
        attributeCountField.setMinimumSize(attributeCountField.getPreferredSize());

        nameField.getDocument().addDocumentListener(new ContextTextListener());
        authorNameField.getDocument().addDocumentListener(new ContextTextListener());
        reasonField.getDocument().addDocumentListener(new ContextTextListener());
        descriptionField.getDocument().addDocumentListener(new ContextTextListener());

        String msgId = Messages.getMessage("CONTEXT_DETAIL_LABELS_ID");
        String msgTime = Messages.getMessage("CONTEXT_DETAIL_LABELS_TIME");
        String msgName = "(*) " + Messages.getMessage("CONTEXT_DETAIL_LABELS_NAME");
        String msgAuthor = "(*) " + Messages.getMessage("CONTEXT_DETAIL_LABELS_AUTHOR");
        String msgReason = "(*) " + Messages.getMessage("CONTEXT_DETAIL_LABELS_REASON");
        String msgDescription = "(*) " + Messages.getMessage("CONTEXT_DETAIL_LABELS_DESRIPTION");
        String oblig = "(*) " + Messages.getMessage("CONTEXT_DETAIL_LABELS_MANDATORY");
        String attrCount = Messages.getMessage("CONTEXT_DETAIL_LABELS_ATTRIBUTE_COUNT");

        JLabel mandatoryLabel = new JLabel(oblig, JLabel.RIGHT);
        mandatoryLabel.setForeground(new Color(150, 0, 0));
        mandatoryLabel.setFont(smallFont);
        JLabel idLabel = new JLabel(msgId);
        idLabel.setFont(font);
        JLabel timeLabel = new JLabel(msgTime);
        timeLabel.setFont(font);
        JLabel nameLabel = new JLabel(msgName);
        nameLabel.setFont(font);
        nameLabel.setForeground(new Color(150, 0, 0));
        JLabel authorLabel = new JLabel(msgAuthor);
        authorLabel.setFont(font);
        authorLabel.setForeground(new Color(150, 0, 0));
        JLabel reasonLabel = new JLabel(msgReason);
        reasonLabel.setFont(font);
        reasonLabel.setForeground(new Color(150, 0, 0));
        JLabel descrLabel = new JLabel(msgDescription);
        descrLabel.setFont(font);
        descrLabel.setForeground(new Color(150, 0, 0));
        JLabel attrCountLabel = new JLabel(attrCount, JLabel.RIGHT);
        attrCountLabel.setFont(font);

        this.setLayout(new GridBagLayout());

        Insets titleMargin = new Insets(0, 0, 2, 0);
        Insets valueMargin = new Insets(0, 2, 2, 0);

        GridBagConstraints idLabelConstraints = new GridBagConstraints();
        idLabelConstraints.fill = GridBagConstraints.NONE;
        idLabelConstraints.gridx = 0;
        idLabelConstraints.gridy = 0;
        idLabelConstraints.weightx = 0;
        idLabelConstraints.weighty = 0;
        idLabelConstraints.anchor = GridBagConstraints.EAST;
        idLabelConstraints.insets = titleMargin;
        add(idLabel, idLabelConstraints);
        GridBagConstraints idFieldConstraints = new GridBagConstraints();
        idFieldConstraints.fill = GridBagConstraints.NONE;
        idFieldConstraints.gridx = 1;
        idFieldConstraints.gridy = 0;
        idFieldConstraints.weightx = 0;
        idFieldConstraints.weighty = 0;
        idFieldConstraints.anchor = GridBagConstraints.WEST;
        idFieldConstraints.insets = valueMargin;
        add(idField, idFieldConstraints);
        GridBagConstraints attrCountLabelConstraints = new GridBagConstraints();
        attrCountLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        attrCountLabelConstraints.gridx = 2;
        attrCountLabelConstraints.gridy = 0;
        attrCountLabelConstraints.weightx = 1;
        attrCountLabelConstraints.weighty = 0;
        attrCountLabelConstraints.anchor = GridBagConstraints.EAST;
        attrCountLabelConstraints.insets = titleMargin;
        add(attrCountLabel, attrCountLabelConstraints);
        GridBagConstraints attrCountFieldConstraints = new GridBagConstraints();
        attrCountFieldConstraints.fill = GridBagConstraints.NONE;
        attrCountFieldConstraints.gridx = 3;
        attrCountFieldConstraints.gridy = 0;
        attrCountFieldConstraints.weightx = 0;
        attrCountFieldConstraints.weighty = 0;
        attrCountFieldConstraints.anchor = GridBagConstraints.EAST;
        attrCountFieldConstraints.insets = valueMargin;
        add(attributeCountField, attrCountFieldConstraints);

        GridBagConstraints timeLabelConstraints = new GridBagConstraints();
        timeLabelConstraints.fill = GridBagConstraints.NONE;
        timeLabelConstraints.gridx = 0;
        timeLabelConstraints.gridy = 1;
        timeLabelConstraints.weightx = 0;
        timeLabelConstraints.weighty = 0;
        timeLabelConstraints.anchor = GridBagConstraints.EAST;
        timeLabelConstraints.insets = titleMargin;
        add(timeLabel, timeLabelConstraints);
        GridBagConstraints timeFieldConstraints = new GridBagConstraints();
        timeFieldConstraints.fill = GridBagConstraints.NONE;
        timeFieldConstraints.gridx = 1;
        timeFieldConstraints.gridy = 1;
        timeFieldConstraints.weightx = 0;
        timeFieldConstraints.weighty = 0;
        timeFieldConstraints.anchor = GridBagConstraints.WEST;
        timeFieldConstraints.insets = valueMargin;
        add(creationDateField, timeFieldConstraints);
        GridBagConstraints mandatoryLabelConstraints = new GridBagConstraints();
        mandatoryLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        mandatoryLabelConstraints.gridx = 2;
        mandatoryLabelConstraints.gridy = 1;
        mandatoryLabelConstraints.weightx = 1;
        mandatoryLabelConstraints.weighty = 0;
        mandatoryLabelConstraints.anchor = GridBagConstraints.SOUTHEAST;
        mandatoryLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
        mandatoryLabelConstraints.insets = valueMargin;
        add(mandatoryLabel, mandatoryLabelConstraints);

        GridBagConstraints nameLabelConstraints = new GridBagConstraints();
        nameLabelConstraints.fill = GridBagConstraints.NONE;
        nameLabelConstraints.gridx = 0;
        nameLabelConstraints.gridy = 2;
        nameLabelConstraints.weightx = 0;
        nameLabelConstraints.weighty = 0;
        nameLabelConstraints.anchor = GridBagConstraints.EAST;
        nameLabelConstraints.insets = titleMargin;
        add(nameLabel, nameLabelConstraints);
        GridBagConstraints nameFieldConstraints = new GridBagConstraints();
        nameFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameFieldConstraints.gridx = 1;
        nameFieldConstraints.gridy = 2;
        nameFieldConstraints.weightx = 1;
        nameFieldConstraints.weighty = 0;
        nameFieldConstraints.anchor = GridBagConstraints.WEST;
        nameFieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
        nameFieldConstraints.insets = valueMargin;
        add(nameField, nameFieldConstraints);

        GridBagConstraints authorLabelConstraints = new GridBagConstraints();
        authorLabelConstraints.fill = GridBagConstraints.NONE;
        authorLabelConstraints.gridx = 0;
        authorLabelConstraints.gridy = 3;
        authorLabelConstraints.weightx = 0;
        authorLabelConstraints.weighty = 0;
        authorLabelConstraints.anchor = GridBagConstraints.EAST;
        authorLabelConstraints.insets = titleMargin;
        add(authorLabel, authorLabelConstraints);
        GridBagConstraints authorFieldConstraints = new GridBagConstraints();
        authorFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        authorFieldConstraints.gridx = 1;
        authorFieldConstraints.gridy = 3;
        authorFieldConstraints.weightx = 1;
        authorFieldConstraints.weighty = 0;
        authorFieldConstraints.anchor = GridBagConstraints.WEST;
        authorFieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
        authorFieldConstraints.insets = valueMargin;
        add(authorNameField, authorFieldConstraints);

        GridBagConstraints reasonLabelConstraints = new GridBagConstraints();
        reasonLabelConstraints.fill = GridBagConstraints.NONE;
        reasonLabelConstraints.gridx = 0;
        reasonLabelConstraints.gridy = 4;
        reasonLabelConstraints.weightx = 0;
        reasonLabelConstraints.weighty = 0;
        reasonLabelConstraints.anchor = GridBagConstraints.EAST;
        reasonLabelConstraints.insets = titleMargin;
        add(reasonLabel, reasonLabelConstraints);
        GridBagConstraints reasonFieldConstraints = new GridBagConstraints();
        reasonFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        reasonFieldConstraints.gridx = 1;
        reasonFieldConstraints.gridy = 4;
        reasonFieldConstraints.weightx = 1;
        reasonFieldConstraints.weighty = 0;
        reasonFieldConstraints.anchor = GridBagConstraints.WEST;
        reasonFieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
        reasonFieldConstraints.insets = valueMargin;
        add(reasonField, reasonFieldConstraints);

        GridBagConstraints descriptionLabelConstraints = new GridBagConstraints();
        descriptionLabelConstraints.fill = GridBagConstraints.NONE;
        descriptionLabelConstraints.gridx = 0;
        descriptionLabelConstraints.gridy = 5;
        descriptionLabelConstraints.weightx = 0;
        descriptionLabelConstraints.weighty = 0;
        descriptionLabelConstraints.anchor = GridBagConstraints.EAST;
        descriptionLabelConstraints.insets = new Insets(0, 0, 0, 0);
        add(descrLabel, descriptionLabelConstraints);
        GridBagConstraints descriptionFieldConstraints = new GridBagConstraints();
        descriptionFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        descriptionFieldConstraints.gridx = 1;
        descriptionFieldConstraints.gridy = 5;
        descriptionFieldConstraints.weightx = 1;
        descriptionFieldConstraints.weighty = 0;
        descriptionFieldConstraints.anchor = GridBagConstraints.WEST;
        descriptionFieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
        descriptionFieldConstraints.insets = new Insets(0, valueMargin.left, 0, 0);
        add(descriptionField, descriptionFieldConstraints);

        // this.setMaximumSize( new Dimension( 700 , 180 ) );
        // this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 180 ) );

        GUIUtilities.setObjectBackground(this, GUIUtilities.CONTEXT_COLOR);
    }

    /**
     * @return Returns the authorNameField.
     */
    public JTextField getAuthorNameField() {
        return authorNameField;
    }

    /**
     * @return Returns the creationDateField.
     */
    public JTextField getCreationDateField() {
        return creationDateField;
    }

    /**
     * @return Returns the descriptionField.
     */
    public JTextField getDescriptionField() {
        return descriptionField;
    }

    /**
     * @return Returns the iDField.
     */
    public IDTextField getIDField() {
        return idField;
    }

    /**
     * @return Returns the nameField.
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * @return Returns the reasonField.
     */
    public JTextField getReasonField() {
        return reasonField;
    }

    public JTextField getAttributeCountField() {
        return attributeCountField;
    }

}
