// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/open/AddFavoriteContextDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AddFavoriteContextDialog.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: AddFavoriteContextDialog.java,v $
// Revision 1.6 2006/03/27 14:04:24 ounsy
// favorites contexts now have a label
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
package fr.soleil.bensikin.containers.sub.dialogs.open;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.actions.CancelAction;
import fr.soleil.bensikin.actions.NewFavoritesDirectoryAction;
import fr.soleil.bensikin.actions.context.AddFavoriteContextAction;
import fr.soleil.bensikin.actions.context.RemoveFavoriteContextItemAction;
import fr.soleil.bensikin.components.BensikinToolbar;
import fr.soleil.bensikin.components.context.FavoritesContextTree;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.sub.dialogs.CancelableDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.favorites.FavoritesContextSubMenu;
import fr.soleil.bensikin.tools.Messages;

/**
 * The JDialog where the user can add the current context to his favorites.
 * 
 * @author CLAISSE
 */
public class AddFavoriteContextDialog extends CancelableDialog {

    private Dimension                       dim      = new Dimension(300, 300);
    private JPanel                          myPanel;
    private JTextField                      newFileTextField;
    private JTextField                      favoriteLabelField;
    private Box                             actionBox;
    private Box                             contextDataBox;
    private String                          id;
    public FavoritesContextTree             tree;
    private JScrollPane                     scrollPane;

    private static AddFavoriteContextDialog instance = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static AddFavoriteContextDialog getInstance() {
        if (instance == null) {
            instance = new AddFavoriteContextDialog();
        }

        return instance;
    }

    /**
     * Builds the dialog.
     */
    private AddFavoriteContextDialog() {
        super(BensikinFrame.getInstance(), Messages
                .getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_TITLE"), true);

        this.initComponents();
        this.addComponents();
        this.initLayout();

        this.setSizeAndLocation();

        // doesn't work
        /*
         * this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
         * this.addWindowListener ( new FavoritesWindowListener ( this ) );
         */
    }

    /*
     * private class FavoritesWindowListener extends WindowAdapter { private
     * AddFavoriteContextDialog dialog; public
     * FavoritesWindowListener(AddFavoriteContextDialog dial) { super(); dialog
     * = dial; } public void windowClosing(WindowEvent e) { System.out.println (
     * "CLA/windowClosed!!" ); BensikinFrame frame =
     * BensikinFrame.getInstance(); BensikinMenuBar menuBar = ( BensikinMenuBar
     * ) frame.getJMenuBar(); Favorites favorites = Favorites.getInstance();
     * FavoritesContextSubMenu treeMenu = favorites.getContextSubMenu();
     * frame.remove(menuBar);
     * menuBar.setFavorites_contexts(treeMenu.getMenuRoot());
     * frame.setJMenuBar(menuBar); } }
     */

    /**
     * Sets the size and location of the dialog.
     */
    private void setSizeAndLocation() {
        this.setSize(dim);
        this.setLocationRelativeTo(BensikinToolbar.getInstance());
    }

    /**
     * @param model
     *            The new model to use for the favorites tree.
     */
    public void reBuildTree(DefaultTreeModel model) {
        tree.setModel(model);
    }

    /**
     * Inits the dialog's components.
     */
    private void initComponents() {
        Context selectedContext = Context.getSelectedContext();
        id = String.valueOf(selectedContext.getContextData().getId());

        newFileTextField = new JTextField();
        newFileTextField.setPreferredSize(new Dimension(80, 20));

        favoriteLabelField = new JTextField();
        favoriteLabelField.setPreferredSize(new Dimension(80, 20));

        Favorites favorites = Favorites.getInstance();
        FavoritesContextSubMenu treeMenu = favorites.getContextSubMenu();
        DefaultTreeModel model = treeMenu.getTreeModel();

        tree = FavoritesContextTree.getInstance();
        tree.setModel(model);
        tree.expandAll1Level(true);
    }

    /**
     * Inits the dialog's layout.
     */
    private void initLayout() {
        myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());

        myPanel.add(contextDataBox, BorderLayout.PAGE_START);
        myPanel.add(scrollPane, BorderLayout.CENTER);
        myPanel.add(actionBox, BorderLayout.PAGE_END);

        this.getContentPane().add(myPanel);
        myPanel.setPreferredSize(new Dimension(400, 400));
    }

    /**
     * Adds the initialized components to the dialog.
     */
    private void addComponents() {
        contextDataBox = new Box(BoxLayout.X_AXIS);
        String idMsg = Messages.getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_ID");
        contextDataBox.add(new JLabel(idMsg));
        contextDataBox.add(Box.createHorizontalStrut(3));
        contextDataBox.add(new JLabel(id));
        contextDataBox.add(Box.createHorizontalStrut(10));
        String labelMsg = Messages
                .getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_LABEL");
        contextDataBox.add(new JLabel(labelMsg));
        contextDataBox.add(Box.createHorizontalStrut(3));
        contextDataBox.add(favoriteLabelField);

        scrollPane = new JScrollPane(tree);
        scrollPane
                .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane
                .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(new Dimension(200, 200));
        scrollPane.setPreferredSize(new Dimension(200, 200));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        actionBox = new Box(BoxLayout.X_AXIS);
        actionBox.add(newFileTextField);
        actionBox.add(Box.createHorizontalStrut(3));

        String newFavoritesDirectoryMsg = Messages
                .getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_NEW_FILE");
        JButton newFavoritesDirectoryButton = new JButton(
                new NewFavoritesDirectoryAction(newFavoritesDirectoryMsg));

        String removeFavoritesItemMsg = Messages
                .getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_REMOVE");
        JButton removeFavoritesItemButton = new JButton(
                new RemoveFavoriteContextItemAction(removeFavoritesItemMsg));

        actionBox.add(newFavoritesDirectoryButton);
        actionBox.add(Box.createHorizontalStrut(3));
        actionBox.add(removeFavoritesItemButton);

        actionBox.add(Box.createHorizontalStrut(50));
        String okMsg = Messages.getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_OK");
        JButton okButton = new JButton(new AddFavoriteContextAction(okMsg));
        actionBox.add(okButton);
        actionBox.add(Box.createHorizontalStrut(3));

        String cancelMsg = Messages
                .getMessage("DIALOGS_ADD_FAVORITE_CONTEXT_CANCEL");
        JButton cancelButton = new JButton(new CancelAction(cancelMsg, this));
        actionBox.add(cancelButton);
    }

    /**
     * Returns the name of the new favorites directory to create.
     * 
     * @return The name of the new favorites directory to create
     */
    public String getNewFileName() {
        return newFileTextField.getText();
    }

    /**
     * Returns the selected path in the favorites tree, eg. where to add the new
     * context.
     * 
     * @return The selected path in the favorites tree, eg. where to add the new
     *         context
     */
    public TreePath getSelectedTreePath() {
        return tree.getSelectionPath();
    }

    /**
     * @return
     */
    public String getFavoriteLabel() {
        return favoriteLabelField.getText();
    }

    @Override
    public void cancel() {
        // nothing particular to do
    }
}
