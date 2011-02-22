package fr.soleil.bensikin.containers.context;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.detail.ContextDetailPrintTable;
import fr.soleil.bensikin.components.renderers.BensikinTreeCellRenderer;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.tools.Messages;

public class ContextDetailPrintPanel extends JPanel {

    private JLabel idTitleLabel = null;
    private JLabel idLabel = null;

    private JLabel pathTitleLabel = null;
    private JLabel pathLabel = null;

    private JLabel timeTitleLabel = null;
    private JLabel timeLabel = null;

    private JLabel nameTitleLabel = null;
    private JLabel nameLabel = null;

    private JLabel authorTitleLabel = null;
    private JLabel authorLabel = null;

    private JLabel reasonTitleLabel = null;
    private JLabel reasonLabel = null;

    private JLabel descriptionTitleLabel = null;
    private JLabel descriptionLabel = null;

    private JLabel detailTitleLabel = null;
    private JTextArea detailTextArea = null;

    private JTree attributesTree;
    private ContextDetailPrintTable table;

    private boolean modeText;

    private Context context;

    public ContextDetailPrintPanel (Context context, boolean modeText) {
        super();
        this.context = context;
        this.modeText = modeText;
        initialise();
    }

    private void initialise() {
        setBackground(Color.WHITE);
        idTitleLabel = new JLabel();
        idTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_ID"));
        idLabel = new JLabel();

        pathTitleLabel = new JLabel();
        pathTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_PATH") );
        pathLabel = new JLabel();

        timeTitleLabel = new JLabel();
        timeTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_TIME") );
        timeLabel = new JLabel();

        nameTitleLabel = new JLabel();
        nameTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_NAME") );
        nameLabel = new JLabel();

        authorTitleLabel = new JLabel();
        authorTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_AUTHOR") );
        authorLabel = new JLabel();

        reasonTitleLabel = new JLabel();
        reasonTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_REASON") );
        reasonLabel = new JLabel();

        descriptionTitleLabel = new JLabel();
        descriptionTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_DESRIPTION") );
        descriptionLabel = new JLabel();

        detailTitleLabel = new JLabel();
        detailTitleLabel.setText( Messages.getMessage("CONTEXT_DETAIL_LABELS_ATTRIBUTES") );

        boolean alternate = Options.getInstance().getContextOptions().isAlternateSelectionMode();

        if (context != null && context.getContextData() != null) {
            idLabel.setText( Integer.toString( context.getContextData().getId() ) );
            pathLabel.setText( context.getContextData().getPath() );
            timeLabel.setText( context.getContextData().getCreation_date().toString() );
            nameLabel.setText( context.getContextData().getName() );
            authorLabel.setText( context.getContextData().getAuthor_name() );
            reasonLabel.setText( context.getContextData().getReason() );
            descriptionLabel.setText( context.getContextData().getDescription() );
        }

        if (modeText) {
            detailTextArea = new JTextArea();
            detailTextArea.setEditable(false);
            detailTextArea.setBackground( getBackground() );
            detailTextArea.setBorder( new LineBorder(Color.BLACK, 1) );
            Context context = Context.getSelectedContext();
            if (context != null) {
                String text = context.toUserFriendlyString();
                detailTextArea.setText(text);
            }
            context = null;
        }
        else if (alternate) {
            table = new ContextDetailPrintTable();
        }
        else  {
            attributesTree = new JTree();
            attributesTree.setCellRenderer( new BensikinTreeCellRenderer() );
            ContextAttributesTreeModel model = ContextAttributesTreeModel.getInstance(false);
            attributesTree.setModel(model);
            ContextAttributesTree tree = ContextAttributesTree.getInstance();
            Enumeration<TreePath> expandedPaths = tree.getExpandedDescendants(
                    new TreePath( model.getRoot() )
            );
            while ( expandedPaths.hasMoreElements() ) {
                attributesTree.expandPath( expandedPaths.nextElement() );
            }
        }

        addComponents();
    }

    private void addComponents() {
        setLayout( new GridBagLayout() );

        GridBagConstraints idTitleLabelConstraints = new GridBagConstraints();
        idTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        idTitleLabelConstraints.gridx = 0;
        idTitleLabelConstraints.gridy = 0;
        idTitleLabelConstraints.weightx = 0;
        idTitleLabelConstraints.weighty = 0;
        GridBagConstraints idLabelConstraints = new GridBagConstraints();
        idLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        idLabelConstraints.gridx = 1;
        idLabelConstraints.gridy = 0;
        idLabelConstraints.weightx = 1;
        idTitleLabelConstraints.weighty = 0;
        add(idTitleLabel, idTitleLabelConstraints);
        add(idLabel, idLabelConstraints);

        GridBagConstraints pathTitleLabelConstraints = new GridBagConstraints();
        pathTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        pathTitleLabelConstraints.gridx = 0;
        pathTitleLabelConstraints.gridy = 1;
        pathTitleLabelConstraints.weightx = 0;
        pathTitleLabelConstraints.weighty = 0;
        GridBagConstraints pathLabelConstraints = new GridBagConstraints();
        pathLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        pathLabelConstraints.gridx = 1;
        pathLabelConstraints.gridy = 1;
        pathLabelConstraints.weightx = 1;
        pathTitleLabelConstraints.weighty = 0;
        add(pathTitleLabel, pathTitleLabelConstraints);
        add(pathLabel, pathLabelConstraints);

        GridBagConstraints timeTitleLabelConstraints = new GridBagConstraints();
        timeTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        timeTitleLabelConstraints.gridx = 0;
        timeTitleLabelConstraints.gridy = 2;
        timeTitleLabelConstraints.weightx = 0;
        timeTitleLabelConstraints.weighty = 0;
        GridBagConstraints timeLabelConstraints = new GridBagConstraints();
        timeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        timeLabelConstraints.gridx = 1;
        timeLabelConstraints.gridy = 2;
        timeLabelConstraints.weightx = 1;
        timeTitleLabelConstraints.weighty = 0;
        add(timeTitleLabel, timeTitleLabelConstraints);
        add(timeLabel, timeLabelConstraints);

        GridBagConstraints nameTitleLabelConstraints = new GridBagConstraints();
        nameTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameTitleLabelConstraints.gridx = 0;
        nameTitleLabelConstraints.gridy = 3;
        nameTitleLabelConstraints.weightx = 0;
        nameTitleLabelConstraints.weighty = 0;
        GridBagConstraints nameLabelConstraints = new GridBagConstraints();
        nameLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameLabelConstraints.gridx = 1;
        nameLabelConstraints.gridy = 3;
        nameLabelConstraints.weightx = 1;
        nameTitleLabelConstraints.weighty = 0;
        add(nameTitleLabel, nameTitleLabelConstraints);
        add(nameLabel, nameLabelConstraints);

        GridBagConstraints authorTitleLabelConstraints = new GridBagConstraints();
        authorTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        authorTitleLabelConstraints.gridx = 0;
        authorTitleLabelConstraints.gridy = 4;
        authorTitleLabelConstraints.weightx = 0;
        authorTitleLabelConstraints.weighty = 0;
        GridBagConstraints authorLabelConstraints = new GridBagConstraints();
        authorLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        authorLabelConstraints.gridx = 1;
        authorLabelConstraints.gridy = 4;
        authorLabelConstraints.weightx = 1;
        authorTitleLabelConstraints.weighty = 0;
        add(authorTitleLabel, authorTitleLabelConstraints);
        add(authorLabel, authorLabelConstraints);

        GridBagConstraints reasonTitleLabelConstraints = new GridBagConstraints();
        reasonTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        reasonTitleLabelConstraints.gridx = 0;
        reasonTitleLabelConstraints.gridy = 5;
        reasonTitleLabelConstraints.weightx = 0;
        reasonTitleLabelConstraints.weighty = 0;
        GridBagConstraints reasonLabelConstraints = new GridBagConstraints();
        reasonLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        reasonLabelConstraints.gridx = 1;
        reasonLabelConstraints.gridy = 5;
        reasonLabelConstraints.weightx = 1;
        reasonTitleLabelConstraints.weighty = 0;
        add(reasonTitleLabel, reasonTitleLabelConstraints);
        add(reasonLabel, reasonLabelConstraints);

        GridBagConstraints descriptionTitleLabelConstraints = new GridBagConstraints();
        descriptionTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        descriptionTitleLabelConstraints.gridx = 0;
        descriptionTitleLabelConstraints.gridy = 6;
        descriptionTitleLabelConstraints.weightx = 0;
        descriptionTitleLabelConstraints.weighty = 0;
        GridBagConstraints descriptionLabelConstraints = new GridBagConstraints();
        descriptionLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        descriptionLabelConstraints.gridx = 1;
        descriptionLabelConstraints.gridy = 6;
        descriptionLabelConstraints.weightx = 1;
        descriptionTitleLabelConstraints.weighty = 0;
        add(descriptionTitleLabel, descriptionTitleLabelConstraints);
        add(descriptionLabel, descriptionLabelConstraints);

        GridBagConstraints glueConstraints = new GridBagConstraints();
        glueConstraints.fill = GridBagConstraints.HORIZONTAL;
        glueConstraints.gridx = 0;
        glueConstraints.gridy = 7;
        glueConstraints.weightx = 1;
        glueConstraints.weighty = 0;
        glueConstraints.gridwidth = GridBagConstraints.REMAINDER;
        Component glue = Box.createGlue();
        glue.setPreferredSize( idTitleLabel.getPreferredSize() );
        add(glue, glueConstraints);

        GridBagConstraints detailTitleLabelConstraints = new GridBagConstraints();
        detailTitleLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        detailTitleLabelConstraints.gridx = 0;
        detailTitleLabelConstraints.gridy = 8;
        detailTitleLabelConstraints.weightx = 1;
        detailTitleLabelConstraints.weighty = 0;
        detailTitleLabelConstraints.gridwidth = GridBagConstraints.REMAINDER;
        add(detailTitleLabel, detailTitleLabelConstraints);

        if (detailTextArea != null) {
            GridBagConstraints textConstraints = new GridBagConstraints();
            textConstraints.fill = GridBagConstraints.BOTH;
            textConstraints.gridx = 0;
            textConstraints.gridy = 9;
            textConstraints.weightx = 1;
            textConstraints.weighty = 1;
            textConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(detailTextArea, textConstraints);
        }
        else if (attributesTree != null) {
            GridBagConstraints treeConstraints = new GridBagConstraints();
            treeConstraints.fill = GridBagConstraints.BOTH;
            treeConstraints.gridx = 0;
            treeConstraints.gridy = 9;
            treeConstraints.weightx = 1;
            treeConstraints.weighty = 1;
            treeConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(attributesTree, treeConstraints);
        }
        else {
            GridBagConstraints headerConstraints = new GridBagConstraints();
            headerConstraints.fill = GridBagConstraints.HORIZONTAL;
            headerConstraints.gridx = 0;
            headerConstraints.gridy = 9;
            headerConstraints.weightx = 0;
            headerConstraints.weighty = 0;
            headerConstraints.gridwidth = GridBagConstraints.REMAINDER;
            GridBagConstraints tableConstraints = new GridBagConstraints();
            tableConstraints.fill = GridBagConstraints.BOTH;
            tableConstraints.gridx = 0;
            tableConstraints.gridy = 10;
            tableConstraints.weightx = 1;
            tableConstraints.weighty = 1;
            tableConstraints.gridwidth = GridBagConstraints.REMAINDER;
            add(table.getTableHeader(), headerConstraints);
            add(table, tableConstraints);
        }
    }

}
